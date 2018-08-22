package com.lingshou.supply.contract.response;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by diwa on 24/8/2017.
 * <p>
 * 用此类做结果解析
 *
 * @see ResultHandler
 */
public class BaseResult<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int SUCCESS_CODE = 200;
    private static final int FAILED_CODE = 500;
    private static final String SUCCESS_VALUE = "success";

    //是否成功
    private boolean success;

    //结果码
    private int resultCode;

    //结果描述
    private String resultDesc;

    private E result;

    public boolean hasSuccess() {
        return Objects.equals(this.success, true);
    }

    public static <T> BaseResult<T> success(T e) {
        BaseResult<T> tTmsBaseResult = new BaseResult<>();

        tTmsBaseResult.setSuccess(true);
        tTmsBaseResult.setResultCode(SUCCESS_CODE);
        tTmsBaseResult.setResultDesc(SUCCESS_VALUE);
        tTmsBaseResult.setResult(e);

        return tTmsBaseResult;
    }

    public static <T> BaseResult<T> success() {
        BaseResult<T> tTmsBaseResult = new BaseResult<>();

        tTmsBaseResult.setSuccess(true);
        tTmsBaseResult.setResultCode(SUCCESS_CODE);
        tTmsBaseResult.setResultDesc(SUCCESS_VALUE);

        return tTmsBaseResult;
    }

    public static <T> BaseResult<T> fail(String resultDesc) {
        return fail(FAILED_CODE, resultDesc);
    }

    public static <T> BaseResult<T> fail(int resultCode, String resultDesc) {
        BaseResult<T> tTmsBaseResult = new BaseResult<>();

        tTmsBaseResult.setSuccess(false);
        tTmsBaseResult.setResultCode(resultCode);
        tTmsBaseResult.setResultDesc(resultDesc);

        return tTmsBaseResult;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "success=" + success +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", result=" + result +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }
}
