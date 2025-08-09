package main.java.webapp;

import main.java.webapp.model.Resume;
import main.java.webapp.storage.SortedArrayStorage;

/**
 * Test for your main.java.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final SortedArrayStorage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        final Resume r1 = new Resume("uuid1");
        final Resume r2 = new Resume("uuid2");
        final Resume r3 = new Resume("uuid3");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        printAll();
        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));
        //тест метода update
        ARRAY_STORAGE.update(r2);
        System.out.println("Update: " + ARRAY_STORAGE.get(r2.getUuid()));
        ARRAY_STORAGE.delete(r1.getUuid());
        System.out.println("Size: " + ARRAY_STORAGE.size());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}
