package webapp.storage.sql;

import webapp.exeption.NotExistStorageException;
import webapp.model.*;
import webapp.storage.Storage;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {
    private final Executor executor;
    private static final String INSERT_CONTACT =
            "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)";
    private static final String INSERT_ORGANIZATION =
            "INSERT INTO organization (name, website) VALUES (?, ?) RETURNING id";

    private static final String INSERT_PERIOD =
            "INSERT INTO period (organization_id, start_date, end_date, title, description) VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_RESUME_SECTION =
            "INSERT INTO resume_section (resume_uuid, section_type, organization_id, position_order) VALUES (?, ?, ?, ?)";
    private static final String LOAD_RESUME = "SELECT full_name FROM resume WHERE uuid = ?";
    private static final String LOAD_CONTACT = "SELECT type, value FROM contact WHERE resume_uuid = ?";
    private static final String LOAD_SECTION = "SELECT type, value FROM section WHERE resume_uuid = ? AND type NOT IN ('EXPERIENCE', 'EDUCATION')";
    private static final String LOAD_ORGANIZATION_SECTION = "SELECT rs.section_type, rs.position_order, o.id, o.name, o.website\n" +
            "                FROM resume_section rs\n" +
            "                JOIN organization o ON rs.organization_id = o.id\n" +
            "                WHERE rs.resume_uuid = ?\n" +
            "                ORDER BY rs.section_type, rs.position_order";

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        ConnectionFactory connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        this.executor = new Executor(connectionFactory);
    }

    @Override
    public void clear() {
        final String queryResume = "DELETE FROM resume";
        executor.execute(queryResume, ps -> {
            ps.executeUpdate();
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        executor.transactionExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }

            deleteByUuid(conn, "contact", r.getUuid());
            deleteByUuid(conn, "section", r.getUuid());
            deleteByUuidAndType(conn, "resume_section", r.getUuid(), "EXPERIENCE", "EDUCATION");
            addSqlContacts(r, conn);
            addSqlSections(r, conn);
            addSqlOrganizationSections(r, conn);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        executor.transactionExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.executeUpdate();
            }
            addSqlContacts(r, conn);
            addSqlSections(r, conn);
            addSqlOrganizationSections(r, conn);
            return null;
        });
    }


    @Override
    public Resume get(String uuid) {
        return executor.transactionExecute(conn -> {
            Resume r = new Resume(uuid, "");

            try (PreparedStatement ps = conn.prepareStatement(LOAD_RESUME)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) throw new NotExistStorageException(uuid);
                r.setFullName(rs.getString("full_name"));
            }

            loadSqlContacts(r, conn);
            loadSqlSections(r, conn);
            loadOrganizationSections(r, conn);

            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        final String query = "DELETE FROM resume WHERE uuid = ?";

        executor.execute(query, ps -> {
            ps.setString(1, uuid);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new NotExistStorageException("Resume not found for delete: " + uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        final String query = "SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid ORDER BY full_name,uuid";

        return executor.execute(query, ps ->
        {
            try (ResultSet resultSet = ps.executeQuery()) {
                Map<String, Resume> resumeMap = new LinkedHashMap<>();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    String fullName = resultSet.getString("full_name");
                    Resume resume = resumeMap.computeIfAbsent(uuid, k -> new Resume(uuid, fullName));

                    String contactType = resultSet.getString("type");
                    String contactValue = resultSet.getString("value");
                    if (contactType != null && contactValue != null) {
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

        return executor.execute(query, ps ->
        {
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        });
    }

    private void addSqlContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_CONTACT)) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSqlSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO section (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                SectionType type = e.getKey();
                Section section = e.getValue();
                if (type == SectionType.EXPERIENCE || type == SectionType.EDUCATION) {
                    continue;
                }
                String value = switch (section) {
                    case TextSection ts -> ts.getContent();
                    case ListSection ls -> String.join("\n", ls.getItems());
                    default -> throw new IllegalStateException("Unsupported section: " + section);
                };
                ps.setString(1, r.getUuid());
                ps.setString(2, type.name());
                ps.setString(3, value);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSqlOrganizationSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement psOrg = conn.prepareStatement(INSERT_ORGANIZATION, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psPeriod = conn.prepareStatement(INSERT_PERIOD);
             PreparedStatement psLink = conn.prepareStatement(INSERT_RESUME_SECTION)) {

            for (SectionType type : List.of(SectionType.EXPERIENCE, SectionType.EDUCATION)) {
                OrganizationSection orgSection = (OrganizationSection) r.getSection(type);
                if (orgSection == null) continue;

                int orgOrder = 0;
                for (Organization org : orgSection.getOrganizations()) {
                    psOrg.setString(1, org.getName());
                    psOrg.setString(2, org.getUrl());
                    psOrg.executeUpdate();

                    int orgId = getGeneratedId(psOrg);

                    for (Organization.Period period : org.getPeriods()) {
                        psPeriod.setInt(1, orgId);
                        psPeriod.setObject(2, period.getStartDate());
                        psPeriod.setObject(3, period.getEndDate());
                        psPeriod.setString(4, period.getTitle());
                        psPeriod.setString(5, period.getDescription());
                        psPeriod.addBatch();
                    }
                    psPeriod.executeBatch();
                    psPeriod.clearBatch();

                    psLink.setString(1, r.getUuid());
                    psLink.setString(2, type.name());
                    psLink.setInt(3, orgId);
                    psLink.setInt(4, orgOrder++);
                    psLink.addBatch();
                }
                psLink.executeBatch();
                psLink.clearBatch();
            }
        }
    }

    private int getGeneratedId(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No ID returned");
        }
    }

    private void loadSqlContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(LOAD_CONTACT)) {
            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ContactType type = ContactType.valueOf(rs.getString("type"));
                String value = rs.getString("value");
                r.addContact(type, value);
            }
        }
    }

    private void loadSqlSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(LOAD_SECTION)) {
            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SectionType type = SectionType.valueOf(rs.getString("type"));
                String value = rs.getString("value");
                Section section = switch (type) {
                    case OBJECTIVE, PERSONAL -> new TextSection(value);
                    case ACHIEVEMENT, QUALIFICATIONS -> new ListSection(value.split("\n"));
                    default -> throw new IllegalStateException();
                };
                r.addSection(type, section);
            }
        }
    }

    private void loadOrganizationSections(Resume r, Connection conn) throws SQLException {
        Map<SectionType, List<Organization>> orgMap = new EnumMap<>(SectionType.class);
        orgMap.put(SectionType.EXPERIENCE, new ArrayList<>());
        orgMap.put(SectionType.EDUCATION, new ArrayList<>());

        try (PreparedStatement ps = conn.prepareStatement(LOAD_ORGANIZATION_SECTION)) {
            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();

            Map<Integer, Organization> orgById = new HashMap<>();
            while (rs.next()) {
                SectionType type = SectionType.valueOf(rs.getString("section_type"));
                int orgId = rs.getInt("id");
                String name = rs.getString("name");
                String website = rs.getString("website");

                orgById.putIfAbsent(orgId, new Organization(name, website));
                orgMap.get(type).add(orgById.get(orgId));
            }
        }

        if (!orgMap.values().stream().allMatch(List::isEmpty)) {
            String orgIds = orgMap.values().stream()
                    .flatMap(List::stream)
                    .map(org -> String.valueOf(org.hashCode()))
                    .collect(Collectors.joining(","));

            if (!orgIds.isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT organization_id, start_date, end_date, title, description FROM period WHERE organization_id IN (" + orgIds + ")")) {
                }
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(
                """
                SELECT p.organization_id, p.start_date, p.end_date, p.title, p.description
                FROM period p
                JOIN resume_section rs ON p.organization_id = rs.organization_id
                WHERE rs.resume_uuid = ?
                ORDER BY p.organization_id, p.start_date
                """)) {
            ps.setString(1, r.getUuid());
            ResultSet rs = ps.executeQuery();

            Map<Integer, List<Organization.Period>> periods = new HashMap<>();
            while (rs.next()) {
                int orgId = rs.getInt("organization_id");
                LocalDate start = rs.getObject("start_date", LocalDate.class);
                LocalDate end = rs.getObject("end_date", LocalDate.class);
                String title = rs.getString("title");
                String desc = rs.getString("description");

                periods.computeIfAbsent(orgId, k -> new ArrayList<>())
                        .add(new Organization.Period(start.getYear(), start.getMonth(), end != null ? end.getYear() : 0, end != null ? end.getMonth() : null, title, desc));
            }

            for (var entry : orgMap.entrySet()) {
                for (Organization org : entry.getValue()) {
                    List<Organization.Period> list = periods.getOrDefault(org.hashCode(), List.of());
                    org.getPeriods().addAll(list);
                }
                r.addSection(entry.getKey(), new OrganizationSection(entry.getValue()));
            }
        }
    }

    private void deleteByUuid(Connection conn, String table, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM " + table + " WHERE resume_uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        }
    }

    private void deleteByUuidAndType(Connection conn, String table, String uuid, String... types) throws SQLException {
        String placeholders = String.join(",", Collections.nCopies(types.length, "?"));
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM " + table + " WHERE resume_uuid = ? AND section_type IN (" + placeholders + ")")) {
            ps.setString(1, uuid);
            for (int i = 0; i < types.length; i++) {
                ps.setString(i + 2, types[i]);
            }
            ps.executeUpdate();
        }
    }
}
