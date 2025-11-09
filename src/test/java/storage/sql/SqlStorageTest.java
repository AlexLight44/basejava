package storage.sql;

import com.urise.TestMain.Config;
import storage.AbstractStorageTest;


public class SqlStorageTest extends AbstractStorageTest {
    public SqlStorageTest() {
        super(Config.get().getStorage());
    }
}
