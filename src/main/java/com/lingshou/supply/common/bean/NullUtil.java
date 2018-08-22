package com.lingshou.supply.common.bean;

import com.lingshou.supply.common.bean.anno.AutowireIfNull;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class NullUtil {

    public static <T> T autowire(T bean) {
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
                .filter(field -> {
                    AutowireIfNull[] annotationsByType = field.getAnnotationsByType(AutowireIfNull.class);

                    return annotationsByType != null && annotationsByType.length > 0;
                })
                .filter(field -> {
                    String fieldName = field.getName();

                    Method readMethod = nameReadMethodMap.get(fieldName);

                    if (readMethod == null) {
                        return false;
                    }

                    try {
                        return readMethod.invoke(bean) == null;
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
                        writeMethod.invoke(bean, new Object[]{genValue(field)});
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });

        return bean;
    }

    private static Object genValue(Field field) {
        if (field == null) {
            return null;
        }

        String type = field.getGenericType().toString();

        return DEFAULT_TYPE_MAP.getOrDefault(type, null);
    }

    /**
     * field.getGenericType 以及对应的默认值
     */
    private static final HashMap<String, Object> DEFAULT_TYPE_MAP = new HashMap<String, Object>() {
        {
            put("class java.lang.Integer", -1);
            put("class java.lang.Long", -1L);
            put("class java.lang.String", "");
            put("class java.lang.Double", 0.0D);
            put("class java.math.BigDecimal", BigDecimal.ZERO);
            put("class java.lang.Boolean", false);
            put("class java.lang.Short", 0);
        }
    };

    public static String killNull(String s) {
        return StringUtils.isEmpty(s) ? "" : s;
    }

    public static Integer killNull(Integer integer) {
        return Objects.isNull(integer) ? -1 : integer;
    }

    public static Long killNull(Long longL) {
        return Objects.isNull(longL) ? -1L : longL;
    }

    public static BigDecimal killNull(BigDecimal decimal) {
        return Objects.isNull(decimal) ? BigDecimal.ZERO : decimal;
    }

    public static void main(String[] args) {
        TestAutowireBean testAutowireBean = new TestAutowireBean();

        System.out.println(testAutowireBean);

        TestAutowireBean autowire = autowire(testAutowireBean);

        System.out.println(autowire);

        //test speed
//        int count = 100000;
//        TestAutowireBean[] testArray = new TestAutowireBean[count];
//
//        for (int i = 0; i < count; i++) {
//            testArray[i] = new TestAutowireBean();
//        }
//
//        long start = System.currentTimeMillis();
//
//        for (int i = 0; i < count; i++) {
//            autowire(testArray[i]);
//        }
//
//        long end = System.currentTimeMillis();
//
//        //last 953ms avg on macbook pro 15, 0.009 ms each
//        System.out.println(end - start);
    }
}
