package com.unise.webapp.storage;

import com.unise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    int size;
    Resume[] storage = new Resume[10000];

    //добавил метод для нахождения по uuid дабы уменьшить дублирование кода
    public int find(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    //проходим по всей длинне массива size где нет null и присваеваем им значения null
    //за пределы существующих Resume size не выйдет
//    public void clear() {
//        for (int i = 0; i < size; i++) {
//            storage[i] = null;
//        }
//        size = 0;
//    }
    //очень извеняюсь но только с помощью ии нашел этот метод в java.util.Arrays
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    //метод update принимает новый resume и когда находит, заменяет его на новый
    //так же проверка на отсутствие нужного uuid в базе данных с помощью boolean
    public void update(Resume resume) {
        //TODO check if resume present
        int ind = find(resume.uuid);
        if (ind == -1) System.out.println("Не найден ваш " + resume.uuid);
        else storage[ind] = resume;
    }

    //увеличиваем size на 1 и записываем туда новый Resume.
    //проверка на null ячейки куда записываем, в случае присутствия resume выводим ошибку
    public void save(Resume r) {
        //TODO check if resume not present
        if (size > 1000) System.out.println("База переполнена");
        else {
            if (storage[size] == null) {
                storage[size] = r;
                size++;
            } else System.out.println("Ячейка занята: " + storage[size].uuid);
        }
    }

    //перебираем массив до size пока не найдем нужное совпадение иначе возвращаем null.
    public Resume get(String uuid) {
        int ind = find(uuid);
        if (ind == -1) {System.out.println("Не найден ваш " + uuid);
            return null;} else return storage[find(uuid)];

    }

    //перебираем массив до size пока не найдем совпадение, затем перезаписываем туда последний индекс.
    //соотвестственно последний удаляем и уменьшаем size на 1.
    //добавил boolean для вывода сообщения в случае ненайденного uuid.
    public void delete(String uuid) {
        //TODO check if resume present
        int ind = find(uuid);
        if (ind == -1) {
            System.out.println("Не найден ваш " + uuid);
        } else {
        storage[find(uuid)] = storage[size-1];
        storage[size-1] = null;
        size --;}
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    //присваеваем новому массиву наши значения из storage и возвращаем его с тем количеством элементов которых нужно
    //а не изначальный массив в 10000 элементов
//    public Resume[] getAll() {
//        Resume[] resumes = new Resume[size];
//        for (int i = 0; i < size; i++) {
//            resumes[i] = storage[i];
//        }
//        return resumes;
//    }
    //нашел этот метод в java.util.Arrays но только с помощью ии к сожалению, уж очень громоздкая библиотека.
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    //начинаем перебирать весь массив с 0 пока не уткнемся в null, это и будет заполненный размер массива.
    public int size() {
        return size;
    }
}
