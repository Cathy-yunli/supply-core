package com.lingshou.supply.contract.code;

import com.lingshou.supply.contract.exception.ServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author di
 * example:
 * System.out.println(Code.displayValue(TerminalType.class, TerminalType.TERMINAL_TYPE_SHELF));
 */
public abstract class Code {

    protected static Map<String, Map<Integer, String>> values = new HashMap<String, Map<Integer, String>>();

    public static String displayValue(Class<? extends Code> clazz, int intValue) {
        if (clazz != null) {
            return values.get(getClassName(clazz)).get(intValue);
        } else {
            return null;
        }
    }

    public static int getIntValue(Class<? extends Code> clazz, String displayValue) {
        if (clazz != null) {
            Optional<Map.Entry<Integer, String>> first = values.get(getClassName(clazz)).entrySet().stream()
                    .filter(entry -> Objects.equals(entry.getValue(), displayValue))
                    .findFirst();

            if (!first.isPresent()){
                throw new ServiceException(displayValue + " not found!");
            }

            return first.get().getKey();
        }
        throw new ServiceException("code class can not be null!");
    }

    public static Set<Integer> values(Class<? extends Code> clazz) {
        if (clazz != null) {
            return values.get(getClassName(clazz)).keySet();
        } else {
            return new HashSet<Integer>();
        }
    }

    public static boolean validate(Class<? extends Code> clazz, int value) {
        if (clazz != null) {
            return values.get(getClassName(clazz)).keySet().contains(value);
        }
        return false;
    }

    private static String getClassName(Class<? extends Code> clazz) {
        String className = clazz.getName();
        if (!values.containsKey(className)) {
            try {
                init(clazz);
            } catch (Exception e) {
                //ignore
                return "";
            }
        }
        return className;
    }

    private static synchronized void init(Class<? extends Code> clazz) throws IllegalAccessException {
        if (!values.containsKey(clazz.getName())) {
            Map<Integer, String> map = new HashMap<Integer, String>();
            for (Field field : clazz.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers()) && field.isAnnotationPresent(Display.class)) {
                    Display annotation = field.getAnnotation(Display.class);
                    map.put(field.getInt(null), annotation.value());
                }
            }
            values.put(clazz.getName(), map);
        }
    }
}
