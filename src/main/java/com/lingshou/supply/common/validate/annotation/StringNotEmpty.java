package com.lingshou.supply.common.validate.annotation;

import java.lang.annotation.*;

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/27 上午11:35
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringNotEmpty {
    String errMessage() default "";
}
