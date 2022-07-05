package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) {
        try {
            Method method = Resume.class.getMethod("toString");
            Constructor<Resume> resumeConstructor = Resume.class.getConstructor(String.class);
            Resume resume = resumeConstructor.newInstance("uuid_1");
            System.out.println(method.invoke(resume));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
