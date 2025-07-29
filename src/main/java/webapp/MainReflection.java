package main.java.webapp;

import main.java.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Resume r = new Resume();
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new uuid");
        //TODO : invoke r.toString via reflection
        System.out.println(r);
        Class<?> classR = r.getClass();
        Method method = classR.getMethod("toString");
        Object res = method.invoke(r);
        System.out.println(res);
    }
}
