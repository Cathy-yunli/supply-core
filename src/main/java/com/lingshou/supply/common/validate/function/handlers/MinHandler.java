package com.lingshou.supply.common.validate.function.handlers;

import com.lingshou.supply.common.validate.ValidateException.ValidateException;
import com.lingshou.supply.common.validate.annotation.Min;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by pengfei.feng on 2017/9/12
 */
public class MinHandler extends AbstractHandler {

    @Override
    public <T, F extends Field, E extends RuntimeException> void handle(T originBean, F field, boolean forceException, E exception, boolean isDeep) {
        if (isDeep) deepCheck(originBean, field, forceException, exception);
        else normalCheck(originBean, field, forceException, exception);
    }

    private static <T, F extends Field, E extends RuntimeException> void deepCheck(T originBean, F field, boolean forceException, E exception) {
        // TODO: 20/7/2016 日后支持深度
        System.out.println("not support deep check now.");
    }


    private static <T, F extends Field, E extends RuntimeException> void normalCheck(T originBean, F field, boolean forceException, E exception) {
        String beanName = originBean.getClass().getName();
        String fieldName = field.getName();
        boolean flag = false;

        field.setAccessible(true);

        try {
            Object o = field.get(originBean);
            if (o == null) throw new IllegalAccessException();

            Min minAnnotation = field.getAnnotation(Min.class);
            if (minAnnotation != null) {
                if (o instanceof Integer) {
                    if ((Integer) o < minAnnotation.value()) flag = true;
                } else if (o instanceof Long) {
                    if ((Long) o < minAnnotation.value()) flag = true;
                } else {
                    throw new ClassCastException();
                }
                if (flag)
                    throw forceException ? exception : new ValidateException(genErrorMessage(minAnnotation, beanName, fieldName));
            }
        } catch (IllegalAccessException e) {
            throw forceException ? exception : new ValidateException(String.format("%s 's field :%s can not be null!", beanName, fieldName));
        } catch (ClassCastException e) {
            throw forceException ? exception : new ValidateException(String.format("%s 's field :%s is not num!", beanName, fieldName));
        }
    }

    private static <F extends Field> String genErrorMessage(Min min, String beanName, String fieldName) {
        String errMessage = min == null ? "" : min.errMessage();

        return StringUtils.isEmpty(errMessage) ? String.format("%s 's field :%s is smaller than min :%s!", beanName, fieldName, min.value()) : errMessage;
    }
}
