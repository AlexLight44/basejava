package main.java.webapp.storage.sql;

import main.java.webapp.storage.AbstractStorageTest;
import webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.get().getStorage());
    }
}
