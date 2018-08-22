package com.lingshou.supply.common.validate.function.handlers;

import com.lingshou.supply.common.validate.ValidateException.ValidateException;
import com.lingshou.supply.common.validate.annotation.StringNotEmpty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/27 上午11:35
 */
public class StringNotEmptyHandler extends AbstractHandler {


    @Override
    public <T, F extends Field, E extends RuntimeException> void handle(T originBean, F field, boolean forceException, E exception, boolean isDeep) {
        if (isDeep) {
            deepCheck(originBean, field, forceException, exception);
        } else {
            normalCheck(originBean, field, forceException, exception);
        }
    }

    private static <T, F extends Field, E extends RuntimeException> void deepCheck(T originBean, F field, boolean forceException, E exception) {
        // TODO: 20/7/2016 日后支持深度
        System.out.println("not support deep check now.");
    }


    private static <T, F extends Field, E extends RuntimeException> void normalCheck(T originBean, F field, boolean forceException, E exception) {
        String beanName = originBean.getClass().getName();
        String fieldName = field.getName();

        StringNotEmpty stringNotEmpty = field.getAnnotation(StringNotEmpty.class);

        boolean isEmpty = false;
        try {
            field.setAccessible(true);
            String clazzName = field.getType().getName();
            if (!Objects.equals(clazzName, String.class.getName())) {
                throw new ValidateException("field is not string class");
            }
            Object o = field.get(originBean);

            if (StringUtils.isEmpty((String)o)){
                isEmpty = true;
            }

        } catch (IllegalAccessException e) {
            isEmpty = true;
        }

        if (isEmpty) {
            throw forceException ? exception : new ValidateException(genErrorMessage(stringNotEmpty, beanName, fieldName));
        }
    }

    private static <F extends Field> String genErrorMessage(StringNotEmpty stringNotEmpty, String beanName, String fieldName) {
        String errMessage = stringNotEmpty == null ? "" : stringNotEmpty.errMessage();

        return StringUtils.isEmpty(errMessage) ? String.format("%s 's field :%s can not be empty!", beanName, fieldName) : errMessage;
    }
}
