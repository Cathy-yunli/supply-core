package com.lingshou.supply.common.asserts;

import com.lingshou.supply.common.asserts.exception.AssertException;
import com.lingshou.supply.common.asserts.predicate.Predicate;

import java.util.Objects;

/**
 * Created by diwa on 1/12/17.
 */
public class AssertUtils {
    private static final String DEFAULT_ERROR_MSG = "can not pass assert!";

    public static <T> void notNull(T t) {
        notNull(t, DEFAULT_ERROR_MSG);
    }

    public static <T> void notNull(T t, String errorMsg) {
        check(() -> !Objects.isNull(t), errorMsg);
    }

    public static void isTrue(boolean expression, String mes) {
        if (!expression) {
            throw new AssertException(mes);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "expression is false");
    }

    public static void check(Predicate predicate) {
        check(predicate, DEFAULT_ERROR_MSG);
    }

    public static void check(Predicate predicate, String errorMsg) {
        check(predicate, errorMsg, false, null);
    }

    public static <T extends RuntimeException> void check(Predicate predicate, String errorMsg, boolean isCustomException, T throwable) {
        if (null == predicate) {
            throw new AssertException("predicate can not be null!");
        }

        if (!predicate.test()) {
            throw isCustomException ? throwable : new AssertException(errorMsg);
        }
    }


}
