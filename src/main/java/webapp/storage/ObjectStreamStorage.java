package main.java.webapp.storage;

import main.java.webapp.exeption.StorageException;
import main.java.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage{

    protected ObjectStreamStorage(File directory) {
        super(directory);
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }

    @Override
    protected void doWrite(Resume resume, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(resume);
        }
    }
}
