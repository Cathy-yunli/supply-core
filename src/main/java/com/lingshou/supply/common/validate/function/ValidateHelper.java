package com.lingshou.supply.common.validate.function;


import com.lingshou.supply.common.validate.ValidateException.ValidateException;
import com.lingshou.supply.common.validate.annotation.*;
import com.lingshou.supply.common.validate.function.handlers.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by di on 19/7/2016.
 */
public class ValidateHelper {
    /**
     * default value
     */
    private static final ValidateException DEFAULT_EXCEPTION = new ValidateException();
    private static final boolean DEFAULT_IS_DEEP = false;
    private static final boolean DEFAULT_FORCE_EXCEPTION = false;

    private static final ValidateException NULL_EXCEPTION = new ValidateException("Target can not be null!");

    /**
     * effective annotations
     */
    private static final List<Class> EFFECTIVE_ANNOS = new ArrayList<Class>(Arrays.asList(
            Max.class,
            Min.class,
            NotNull.class,
            IsNumber.class,
            StringCanCastNumber.class,
            StringNotEmpty.class
    ));

    /**
     * register HANDLERS
     */
    private static final Map<Class, AbstractHandler> HANDLERS = new HashMap<Class, AbstractHandler>();

    static {
        HANDLERS.put(Max.class, new MaxHandler());
        HANDLERS.put(Min.class, new MinHandler());
        HANDLERS.put(NotNull.class, new NotNullHandler());
        HANDLERS.put(IsNumber.class, new IsNumberHandler());
        HANDLERS.put(StringCanCastNumber.class, new IsNumberHandler());
        HANDLERS.put(StringNotEmpty.class, new StringNotEmptyHandler());
    }

    /**
     * a hash map to cache class 's structure
     * when a new bean call this util , the structure of this bean will be cached
     * to reduce reflection 's time
     * <p>
     * 感谢政委的想法, 特此加一行注释
     */
    private static final Map<Class, Map<Class, Set<Field>>> CLASS_STRUCTURE_CACHE = new ConcurrentHashMap<>();

    public static <T, E extends Throwable> void validate(T dto) {
        validate(dto, DEFAULT_IS_DEEP);
    }

    public static <T, E extends Throwable> void validate(T dto, boolean isDeep) {
        validate(dto, isDeep, DEFAULT_FORCE_EXCEPTION, DEFAULT_EXCEPTION);
    }

    /**
     * 用于校验传入dto是否满足validate条件
     *
     * @param bean           待检查的bean
     * @param isDeep         是否需要深度检查, 适用于List, Map, Set 等
     * @param forceException 是否要自定义异常
     * @param exception      传入的自定义异常
     * @param <T>            Bean本身
     * @param <E>            自定义异常
     */
    public static <T, E extends RuntimeException> void validate(T bean, boolean isDeep, boolean forceException, E exception) {
        validateNull(bean, isDeep, forceException, exception);

        //通过缓存获取bean结构
        Map<Class, Set<Field>> classSetMap = analyseBean(bean);

        //调用各自handler 校验
        for (Map.Entry<Class, Set<Field>> entry : classSetMap.entrySet()) {
            Set<Field> fieldSet = entry.getValue();

            AbstractHandler handler = HANDLERS.get(entry.getKey());
            for (Field field : fieldSet) {
                handler.handle(bean, field, forceException, exception, isDeep);
            }
        }
    }

    //cache
    private static <T> Map<Class, Set<Field>> analyseBean(T bean) {
        Class<?> beanClass = bean.getClass();

        Map<Class, Set<Field>> classSetMap = CLASS_STRUCTURE_CACHE.get(beanClass);

        if (classSetMap == null) {
            classSetMap = new HashMap<Class, Set<Field>>();

            //根据effectiveAnnos 遍历bean的field 按照effectiveAnnos归类
            for (Class effectiveAnno : EFFECTIVE_ANNOS) {
                classSetMap.put(effectiveAnno, new HashSet<Field>());
            }

            //遍历bean中所有field, 添加到相应set中
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                for (Class anno : EFFECTIVE_ANNOS) {
                    Annotation annotation = field.getAnnotation(anno);
                    if (annotation != null) {
                        classSetMap.get(anno).add(field);
                    }
                }
            }

            //击穿缓存, 讲该bean加入缓存
            CLASS_STRUCTURE_CACHE.put(beanClass, classSetMap);
        }

        return classSetMap;
    }


    //validate
    private static <T, E extends RuntimeException> void validateNull(T bean, boolean isDeep, boolean forceException, E exception) {
        if (isDeep) {
            notNull(exception);
        }
        notNull(bean);
    }

    private static <E> void notNull(E bean) {
        if (bean == null) throw NULL_EXCEPTION;
    }
}