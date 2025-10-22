package webapp.storage.sql;

import webapp.exeption.NotExistStorageException;
import webapp.model.Resume;
import webapp.storage.Storage;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        ConnectionFactory connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        this.sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public void update(Resume r) {
        final String query = "UPDATE resume SET full_name = ? WHERE uuid = ?";

        sqlHelper.execute(query, ps -> {
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
        final String query = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";

        sqlHelper.execute(query, ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        final String query = "SELECT * FROM resume r WHERE r.uuid = ?";

        return sqlHelper.execute(query, statement -> {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        final String query = "DELETE FROM resume WHERE uuid = ?";

        sqlHelper.execute(query, ps -> {
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
        final String query = "SELECT * FROM resume ORDER BY full_name,uuid";

        return sqlHelper.execute(query, ps ->
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
        final String query = "SELECT COUNT(*) FROM resume";

        return sqlHelper.execute(query, ps ->
        {
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        });
    }
}
