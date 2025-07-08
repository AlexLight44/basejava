/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    int size;
    Resume[] storage = new Resume[10000];

    //проходим по всей длинне массива size где нет null и присваеваем им значения null
    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    //увеличиваем size на 1 и записываем туда новый Resume.
    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    //перебираем массив до size пока не найдем нужное совпадение иначе возвращаем null.
    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    //перебираем массив до size пока не найдем совпадение, затем перезаписываем туда последний индекс.
    //соотвестственно последний удаляем и уменьшаем size на 1.
    //добавил boolean для вывода сообщения в случае ненайденного uuid.
    void delete(String uuid) {
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                storage[i] = storage[size - 1];
                storage[size] = null;
                size--;
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
        Resume[] resumes = new Resume[size];
        for (int i = 0; i < size; i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    //начинаем перебирать весь массив с 0 пока не уткнемся в null, это и будет заполненный размер массива.
    int size() {
        return size;
    }
}
