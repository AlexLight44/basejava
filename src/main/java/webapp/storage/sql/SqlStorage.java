package webapp.storage.sql;

import webapp.exeption.NotExistStorageException;
import webapp.exeption.StorageException;
import webapp.model.Resume;
import webapp.storage.Storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper("DELETE FROM resume", ps ->
        {
            ps.execute();
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper("UPDATE resume SET full_name = ? WHERE uuid = ?",
                ps -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 0) {
                        throw new NotExistStorageException("Resume not found for update " + r.getUuid());
                    }
                    return null;
                });
    }

    @Override
    public void save(Resume r) {
        sqlHelper("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                ps -> {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, r.getFullName());
                    ps.execute();
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper("SELECT * FROM resume r WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet resultSet = ps.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, resultSet.getString("full_name"));
                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper("DELETE FROM resume WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected == 0) {
                        throw new NotExistStorageException("Resume not found for delete: " + uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper("SELECT * FROM resume", ps ->
        {
            try (ResultSet resultSet = ps.executeQuery()) {
                List<Resume> resumes = new ArrayList<>();
                while (resultSet.next()) {
                    Resume resume = new Resume(resultSet.getString("uuid"), resultSet.getString("full_name"));
                    resumes.add(resume);
                }
                return resumes;
            }
        });
    }

    @Override
    public int size() {
        return sqlHelper("SELECT COUNT(*) FROM resume", ps ->
        {
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        });
    }

    private <T> T sqlHelper(String sql, SqlHelper<T> sqlHelper) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            return sqlHelper.execute(preparedStatement);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
