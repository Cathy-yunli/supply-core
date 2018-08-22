package com.lingshou.supply.common.handler;

/**
 * 用于各种lambda场景需要处理异常时
 *
 * 实现onException方法即可
 */
@FunctionalInterface
public interface ExceptionHandler {
    /**
     * 处理异常
     */
    void onException(Exception e);
}
