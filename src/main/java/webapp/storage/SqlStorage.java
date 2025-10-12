package webapp.storage;

import webapp.exeption.NotExistStorageException;
import webapp.exeption.StorageException;
import webapp.model.Resume;
import webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM resume")
        ) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume r) {

    }

    @Override
    public void save(Resume r) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")
        ) {
            preparedStatement.setString(1, r.getUuid());
            preparedStatement.setString(2, r.getFullName());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }

    }

    @Override
    public Resume get(String uuid) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume r WHERE r.uuid = ?")
        ) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, resultSet.getString("full name"));
            return resume;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {

    }

    @Override
    public List<Resume> getAllSorted() {
        return List.of();
    }

    @Override
    public int size() {
        return 0;
    }
}
