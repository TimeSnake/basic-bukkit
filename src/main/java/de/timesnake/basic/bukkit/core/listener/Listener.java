package de.timesnake.basic.bukkit.core.listener;

import org.bukkit.event.EventHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;

public class Listener {

    public static Collection<Method> getMethods(Class cls, Class... parameterTypes) {
        Collection<Method> foundMethods = new ArrayList<>();

        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotationsByType(EventHandler.class);
            if (annotations.length != 0) {
                checkParameters(foundMethods, method, parameterTypes);
            }
        }
        return foundMethods;
    }

    public static Collection<Method> getMethodsWithReturnType(Class cls, Class returnType, Class... parameterTypes) {
        Collection<Method> foundMethods = new ArrayList<>();

        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotationsByType(EventHandler.class);
            if (annotations.length != 0) {
                if (method.getReturnType().equals(returnType)) {
                    checkParameters(foundMethods, method, parameterTypes);
                }
            }
        }
        return foundMethods;
    }

    private static void checkParameters(Collection<Method> foundMethods, Method method, Class[] parameterTypes) {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            for (Class parameterType : parameterTypes) {
                int i = 0;
                if (parameter.getParameterizedType().equals(parameterType)) {
                    i++;
                }
                if (i == parameterTypes.length) {
                    foundMethods.add(method);
                }
            }
        }
    }
}
