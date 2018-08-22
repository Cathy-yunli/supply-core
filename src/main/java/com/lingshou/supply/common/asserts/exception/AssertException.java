package com.lingshou.supply.common.asserts.exception;

import com.lingshou.supply.contract.exception.IExceptionCode;
import com.lingshou.supply.contract.exception.ServiceException;

import java.io.Serializable;

/**
 * Created by diwa on 1/12/17.
 */
public class AssertException extends ServiceException implements Serializable{

    public AssertException(IExceptionCode code) {
        super(code);
    }

    public AssertException(IExceptionCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public AssertException(IExceptionCode code, String message) {
        super(code, message);
    }

    public AssertException(IExceptionCode code, Throwable cause) {
        super(code, cause);
    }

    public AssertException(String errorMessage) {
        super(errorMessage);
    }

    public AssertException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public AssertException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
