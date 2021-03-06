package com.lingshou.supply.common.validate.function.handlers;


import com.lingshou.supply.common.validate.ValidateException.ValidateException;
import com.lingshou.supply.common.validate.annotation.Max;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by di on 19/7/2016.
 */
public class MaxHandler extends AbstractHandler {

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

            Max maxAnnotation = field.getAnnotation(Max.class);
            if (maxAnnotation != null) {
                if (o instanceof Integer) {
                    if ((Integer) o > maxAnnotation.value()) flag = true;
                } else if (o instanceof Long) {
                    if ((Long) o > maxAnnotation.value()) flag = true;
                } else {
                    throw new ClassCastException();
                }
                if (flag)
                    throw forceException ? exception : new ValidateException(genErrorMessage(maxAnnotation, beanName, fieldName));
            }
        } catch (IllegalAccessException e) {
            throw forceException ? exception : new ValidateException(String.format("%s 's field :%s can not be null!", beanName, fieldName));
        } catch (ClassCastException e) {
            throw forceException ? exception : new ValidateException(String.format("%s 's field :%s is not num!", beanName, fieldName));
        }
    }

    private static <F extends Field> String genErrorMessage(Max max, String beanName, String fieldName) {
        String errMessage = max == null ? "" : max.errMessage();

        return StringUtils.isEmpty(errMessage) ? String.format("%s 's field :%s is bigger than max :%s!", beanName, fieldName, max.value()) : errMessage;
    }

}
