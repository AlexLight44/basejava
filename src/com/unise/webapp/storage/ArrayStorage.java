package com.unise.webapp.storage;

import com.unise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    int size;
    Resume[] storage = new Resume[10000];

    //проходим по всей длинне массива size где нет null и присваеваем им значения null
    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }
    //метод update принимает новый resume и когда находит, заменяет его на новый
    //так же проверка на отсутствие нужного uuid в базе данных с помощью boolean
    public void update (Resume resume){
        //TODO check if resume present
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(resume.uuid)){
                storage[i] = resume;
                found = true;
                break;
            }

        } if (!found)System.out.println("ERROR не найден ваш " + resume.uuid);
    }

    //увеличиваем size на 1 и записываем туда новый Resume.
    //проверка на null ячейки куда записываем, в случае присутствия resume выводим ошибку
    public void save(Resume r) {
        //TODO check if resume not present
            if (storage[size] == null) {
                storage[size] = r;
                size++;
            }else System.out.println("Ячейка занята: " + storage[size].uuid);
    }

    //перебираем массив до size пока не найдем нужное совпадение иначе возвращаем null.
    public Resume get(String uuid) {
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
    public void delete(String uuid) {
        //TODO check if resume present
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
    public Resume[] getAll() {
        Resume[] resumes = new Resume[size];
        for (int i = 0; i < size; i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    //начинаем перебирать весь массив с 0 пока не уткнемся в null, это и будет заполненный размер массива.
    public int size() {
        return size;
    }
}
