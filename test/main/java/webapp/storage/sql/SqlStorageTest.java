package main.java.webapp.storage.sql;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.storage.sql.SqlStorage;

public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(new SqlStorage("jdbc:postgresql://localhost:5432/resumes", "postgres", "alex123"));
    }
}
