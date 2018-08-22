package com.lingshou.supply.common.wrapper;

import com.lingshou.ed.logger.EDLogger;
import com.lingshou.supply.common.asserts.AssertUtils;
import com.lingshou.supply.common.handler.ExceptionHandler;
import com.lingshou.supply.contract.exception.ServiceException;

import java.util.function.Supplier;

public class MetricWrapper {

    /**
     * 对于处理某个事件
     * 可以在外层包装此方法
     * <p>
     * 从外部传入一个logger, 建议用infoLog
     * 会对方法执行做耗时监控
     *
     * @param supplier
     * @param errorLog
     * @param infoLog
     * @param <T>
     * @return
     */
    public static <T> T handleAndMetric(String actionName, Supplier<T> supplier, EDLogger infoLog, EDLogger errorLog, ExceptionHandler exceptionHandler) {
        AssertUtils.notNull(infoLog, "infoLog can not be null!");

        long start = System.currentTimeMillis();
        infoLog.info(String.format("action:%s begin, time start:%s", actionName, start));

        T t = null;
        try {
            t = supplier.get();

            infoLog.info(String.format("action:%s, finished, last:%s ms", actionName, (System.currentTimeMillis() - start)));
        } catch (Exception e) {
            if (errorLog != null) {
                errorLog.error(String.format("action:%s, exec failed!", actionName), e);
            }

            infoLog.info(String.format("action:%s, exec failed! last:%s ms, now begin exceptionHandler", actionName, (System.currentTimeMillis() - start)));
            exceptionHandler.onException(e);
        }
        return t;
    }

    public static <T> T handleAndMetric(String actionName, Supplier<T> supplier, EDLogger infoLog, EDLogger errLogger) {
        return handleAndMetric(actionName, supplier, infoLog, errLogger, e -> {
            throw new ServiceException(actionName + "exec failed!", e);
        });
    }

    public static <T> T handleAndMetric(String actionName, Supplier<T> supplier, EDLogger infoLog) {
        return handleAndMetric(actionName, supplier, infoLog, null);
    }

}
