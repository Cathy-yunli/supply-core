package com.lingshou.supply.common.validate.function.handlers;

import com.lingshou.supply.common.validate.ValidateException.ValidateException;
import com.lingshou.supply.common.validate.annotation.IsNumber;
import com.lingshou.supply.common.validate.annotation.StringCanCastNumber;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;

/**
 * @author siliang.zheng
 * Date : 2018/1/4
 * Describle
 */
public class IsNumberHandler extends AbstractHandler {

    @Override
    public <T, F extends Field, E extends RuntimeException> void handle(T originBean, F field, boolean forceException, E exception, boolean isDeep) {
        String beanName = originBean.getClass().getName();
        String fieldName = field.getName();

        boolean isValid = true;
        try {
            field.setAccessible(true);
            Object obj = field.get(originBean);
            if (!(obj instanceof String)) {
                throw new ValidateException(genErrorMessage(field, beanName, fieldName));
            }
            if (!NumberUtils.isNumber((String) obj)) {
                isValid = false;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (!isValid) {
            throw forceException ? exception : new ValidateException(genErrorMessage(field, beanName, fieldName));
        }
    }

    private <F extends Field> String genErrorMessage(F field, String beanName, String fieldName) {
        IsNumber isNumberAnno = field.getAnnotation(IsNumber.class);
        StringCanCastNumber canCastNumberAnno = field.getAnnotation(StringCanCastNumber.class);

        String isNumErr = isNumberAnno == null ? "" : isNumberAnno.errMessage();
        String canCastErr = canCastNumberAnno == null ? "" : canCastNumberAnno.errMessage();

        if (!StringUtils.isEmpty(isNumErr)){
            return isNumErr;
        }

        if (!StringUtils.isEmpty(canCastErr)){
            return canCastErr;
        }

        return String.format("%s's field %s is not a String!", beanName, fieldName);
    }
}
