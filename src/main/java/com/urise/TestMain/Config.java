package com.urise.TestMain;

import com.urise.storage.Storage;
import com.urise.storage.sql.SqlStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    protected static final File PROPS = new File(getHomeDir(), "config\\resume.properties");
    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final Storage storage;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream inputStream = new FileInputStream(PROPS)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            storageDir = new File(properties.getProperty("storage.dir"));
            storage = new SqlStorage(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }
    public static File getHomeDir(){
        String prop = System.getProperty("homeDir");
        File homeDir = new File(prop==null ? "." : prop);
        if (!homeDir.isDirectory()){
            throw new IllegalStateException(homeDir + " is not directory");
        }
        return homeDir;
    }
}
