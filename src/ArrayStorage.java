import java.sql.Array;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    //проходим по всей длинне массива где нет null с помощью метода size() и присваеваем им значения null
    void clear() {
        for (int i = 0; i < size(); i++) {
            storage[i] = null;
        }
    }

    //вызываем size() для проверки ближайшего пустого индекса массива и записываем туда класс Resume.
    void save(Resume r) {
        storage[size()] = r;
    }

    //перебираем массив до size пока не найдем нужное совпадение иначе возвращаем null.
    Resume get(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }
    //самый сложный метод для меня: нужно сдвигать индексы массива влево чтобы заполнить пустоты
    //добавил boolean found т.к. не понял как еще вывести сообщение в случае ошибки
    void delete(String uuid) {
        boolean found = false;
        for (int i = 0; i < size(); i++) {
            if (storage[i].uuid.equals(uuid)) {
                for (int j = i; j < size() - 1; j++) {
                    storage[j] = storage[j + 1];
                }
                storage[size() - 1] = null;
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("не существует такого uuid");
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    //присваеваем новому массиву наши значения из storage и возвращаем его с тем количеством элементов которых нужно
    //а не изначальный массив в 10000 элементов
    Resume[] getAll() {
        Resume[] resumes = new Resume[size()];
        for (int i = 0; i < size(); i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    //начинаем перебирать весь массив с 0 пока не уткнемся в null, это и будет заполненный размер массива.
    int size() {
        int i = 0;
        for (Resume resume : storage) {
            if (resume == null) break;
            else i++;
        }
        return i;
    }
}
