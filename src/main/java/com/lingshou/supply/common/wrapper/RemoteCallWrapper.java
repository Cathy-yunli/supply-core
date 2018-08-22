package com.lingshou.supply.common.wrapper;

import com.lingshou.ed.logger.EDLogger;
import com.lingshou.supply.common.asserts.AssertUtils;
import com.lingshou.supply.common.handler.ExceptionHandler;
import com.lingshou.supply.contract.exception.ServiceException;

import java.util.function.Supplier;

public class RemoteCallWrapper {
    /**
     * 用来包装远程调用
     *
     * 比如调用第三方rpc
     *
     *
     * @param actionName
     * @param supplier
     * @param errLogger
     * @param exceptionHandler
     * @param <T>
     * @return
     */
    public static <T> T call(String actionName, Supplier<T> supplier, EDLogger errLogger, ExceptionHandler exceptionHandler) {
        AssertUtils.notNull(errLogger, "errorLogger can not be null!");

        T t = null;
        try {
            t = supplier.get();
        } catch (Exception e) {
            errLogger.error(String.format("exec remote call: %s failed!", actionName), e);
            exceptionHandler.onException(e);
        }

        return t;
    }

    /**
     * 默认异常处理, 是包装成ServiceException
     *
     * @param actionName
     * @param supplier
     * @param errLogger
     * @param <T>
     * @return
     */
    public static <T> T call(String actionName, Supplier<T> supplier, EDLogger errLogger) {
        return call(actionName, supplier, errLogger, e -> {
            throw new ServiceException(actionName + "执行失败!", e);
        });
    }
}
