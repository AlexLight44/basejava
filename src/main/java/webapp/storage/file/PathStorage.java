package webapp.storage.file;

import webapp.exeption.StorageException;
import webapp.model.Resume;
import webapp.storage.AbstractStorage;
import webapp.storage.file.serializers.ISerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final ISerializer serializer;

    public PathStorage(String dir, ISerializer serializer) {
        Objects.requireNonNull(dir, "directory must not be null");
        Objects.requireNonNull(serializer, "serializer must not be null");

        this.serializer = serializer;
        directory = Paths.get(dir);

        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            try (var bos = new BufferedOutputStream(Files.newOutputStream(path, StandardOpenOption.TRUNCATE_EXISTING))) {
                serializer.doWrite(resume, bos);
                bos.flush();
            }
        } catch (IOException e) {
            throw new StorageException("Path write error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            try (var bis = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
                return serializer.doRead(bis);
            }
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected boolean isExisting(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            try (var bos = new BufferedOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE))) {
                serializer.doWrite(resume, bos);
                bos.flush();
            }
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
        doUpdate(resume, path);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("File write error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> doGetAll() {
        return getListPaths().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getListPaths().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getListPaths().count();
    }

    private Stream<Path> getListPaths() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory read error", null);
        }
    }
}
