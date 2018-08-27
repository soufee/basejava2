package ru.shoma.webapp;

import ru.shoma.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Resume r = new Resume("Ashamaz");
        Class<? extends Resume> clazz = r.getClass();
        Field field = clazz.getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        System.out.println(r);
        field.set(r, "ToStringInvoke");
        Method method = clazz.getDeclaredMethod("toString");
        System.out.println(method.invoke(r));

    }
}
