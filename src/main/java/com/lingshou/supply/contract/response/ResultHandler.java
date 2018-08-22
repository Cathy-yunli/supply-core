package com.lingshou.supply.contract.response;


import com.lingshou.ed.logger.EDLogger;
import com.lingshou.supply.contract.exception.ServiceException;

/**
 * Created by diwa on 29/8/2017.
 */
public class ResultHandler {

    private static final String DEFAULT_ERROR_ERROR_CODE = "500";

    public static <T> T handle(BaseResult<T> baseResult) {
        return handle(baseResult, () -> {
        });
    }

    public static <T> T handle(BaseResult<T> baseResult, EDLogger edLogger) {

        return handle(baseResult, () -> doLog(baseResult, edLogger));
    }

    public static <T> T handle(BaseResult<T> baseResult, HandleAction action) {

        if (baseResult.hasSuccess()) {
            return baseResult.getResult();
        } else {
            if (action != null) action.invoke();
            throw new ServiceException(analysisResultCode(baseResult.getResultCode()), baseResult.getResultDesc());
        }
    }

    private static void doLog(BaseResult<?> baseResult, EDLogger edLogger) {
        edLogger.error("baseResult:" + baseResult + " failed!");
    }

    private static String analysisResultCode(Integer code) {
        return null == code ? DEFAULT_ERROR_ERROR_CODE : String.valueOf(code);
    }
}
