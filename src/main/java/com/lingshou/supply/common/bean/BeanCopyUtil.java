package com.lingshou.supply.common.bean;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by diwa on 24/8/2017.
 */
public class BeanCopyUtil {
    public static <E, T> T copy(E e, Class<? extends T> clazz) {
        Assert.notNull(e, "source can not be null!");
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(e, t);
            return t;
        } catch (Exception e1) {
            throw new RuntimeException("copy properties error!");
        }
    }

    public static <E, T> List<T> copyList(List<E> e, Class<? extends T> clazz) {
        if (CollectionUtils.isEmpty(e)) return Lists.newArrayList();

        List<T> list = new ArrayList<>();
        for (E object : e) {
            list.add(copy(object, clazz));
        }
        return list;
    }

    public static <Tin, Tout> List<Tout> selectToList(List<Tin> list, Function<Tin, Tout> callback) {

        if (CollectionUtils.isEmpty(list)) return Lists.newArrayList();

        return list.stream().map(callback).collect(Collectors.toList());
    }
}
