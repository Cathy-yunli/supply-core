package com.lingshou.supply.common.bean;

import com.lingshou.supply.common.bean.anno.EmptyStr2Null;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by diwa on 8/11/17.
 */
public class VoStrEmpty2NullUtil {
    public static <T> T apply(T bean) {
        if (null == bean) {
            return null;
        }

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            System.out.println(e);
        }

        Map<String, Method> nameReadMethodMap = Stream.of(beanInfo.getPropertyDescriptors())
                .collect(HashMap::new, (hashMap, propertyDescriptor) -> hashMap.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod()), Map::putAll);

        Map<String, Method> nameWriteMethodMap = Stream.of(beanInfo.getPropertyDescriptors())
                .collect(HashMap::new, (hashMap, propertyDescriptor) -> hashMap.put(propertyDescriptor.getName(), propertyDescriptor.getWriteMethod()), Map::putAll);

        Stream.of(bean.getClass().getDeclaredFields())
                .filter(field -> field.getType() == String.class)
                .filter(field -> field.getAnnotationsByType(EmptyStr2Null.class) != null)
                .filter(field -> {
                    String fieldName = field.getName();

                    Method readMethod = nameReadMethodMap.get(fieldName);

                    if (readMethod == null) {
                        return false;
                    }

                    try {
                        return readMethod.invoke(bean) == "";
                    } catch (Exception e) {
                        return false;
                    }
                })
                .forEach(field -> {
                    String fieldName = field.getName();

                    Method writeMethod = nameWriteMethodMap.get(fieldName);

                    if (writeMethod == null) {
                        return;
                    }

                    try {
                        writeMethod.invoke(bean, new Object[]{null});
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });

        return bean;
    }
}
