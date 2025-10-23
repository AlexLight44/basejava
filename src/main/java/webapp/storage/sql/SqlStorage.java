package webapp.storage.sql;

import webapp.exeption.NotExistStorageException;
import webapp.model.ContactType;
import webapp.model.Resume;
import webapp.storage.Storage;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        final String queryName = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
        final String queryContacts = "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)";

        sqlHelper.execute(queryName, ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet())
            sqlHelper.execute(queryContacts, ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.execute();
                return null;
            });
    }

    @Override
    public Resume get(String uuid) {
        final String query = "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "WHERE r.uuid = ? ";

        return sqlHelper.execute(query, statement -> {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, resultSet.getString("full_name"));
            do {
                String value = resultSet.getString("value");
                ContactType type = ContactType.valueOf(resultSet.getString("type"));
                r.addContact(type, value);
            } while (resultSet.next());

            return r;

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
        final String query = "SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid ORDER BY full_name,uuid";
// доделать контакты тут
        return sqlHelper.execute(query, ps ->
        {
            try (ResultSet resultSet = ps.executeQuery()) {
                Map<String, Resume> resumeMap = new LinkedHashMap<>();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    String fullName = resultSet.getString("full_name");
                    Resume resume = resumeMap.computeIfAbsent(uuid, k -> new Resume(uuid, fullName));

                    String contactType = resultSet.getString("type");
                    String contactValue = resultSet.getString("value");
                    if (contactType != null && contactValue != null){
                        resume.addContact(ContactType.valueOf(contactType), contactValue);
                    }
                }
                return new ArrayList<>(resumeMap.values());
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
