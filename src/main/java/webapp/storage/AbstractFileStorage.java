package main.java.webapp.storage;

import main.java.webapp.exeption.StorageException;
import main.java.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() && directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract Resume doRead(File file) throws IOException;

    @Override
    protected boolean isExisting(File file) {
        return file.exists();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(Resume resume, File file) throws IOException;

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File has not been deleted", file.getName());
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        List<Resume> resumes = new ArrayList<>();
        for (File file : getListFiles()) {
            resumes.add(doGet(file));
        }
        return resumes;
    }

    @Override
    public void clear() {
        for (File file : getListFiles()) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        return getListFiles().length;
    }

    protected File[] getListFiles() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("File not found: ", directory.getName());
        }
        return files;
    }
}
