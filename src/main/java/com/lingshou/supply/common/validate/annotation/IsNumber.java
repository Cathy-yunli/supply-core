package com.lingshou.supply.common.validate.annotation;

import java.lang.annotation.*;

/**
 * @author siliang.zheng
 * Date : 2018/1/4
 * Describle 字符串是否为数字
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface IsNumber{
    String errMessage() default "";
}
