package com.lingshou.supply.common.validate.test;

import com.lingshou.supply.common.validate.annotation.*;
import com.lingshou.supply.common.validate.function.ValidateHelper;

public class TestBean {
    @Min(value = 10, errMessage = "不能小于10")
    private int anInt;

    @Max(value = 100, errMessage = "最大100!")
    private long aLong;

    @StringCanCastNumber(errMessage = "必须是数字")
    private String aString;

    @NotNull(errMessage = "obj必填")
    private Object object;

    @StringCanCastNumber
    private String numericString;

    @StringNotEmpty(errMessage = "字符串不能为空")
    private String emptyString;

    public String getEmptyString() {
        return emptyString;
    }

    public void setEmptyString(String emptyString) {
        this.emptyString = emptyString;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }


    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getNumericString() {
        return numericString;
    }

    public void setNumericString(String numericString) {
        this.numericString = numericString;
    }

    public int getAnInt() {

        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public static void main(String[] args) {
        TestBean testBean = new TestBean();

        testBean.setAnInt(50);
        testBean.setaLong(100);
        testBean.setaString("1");
        testBean.setObject(1);
        testBean.setNumericString("1");
        testBean.setEmptyString("");

        ValidateHelper.validate(testBean);

        System.out.println("test pass");
    }
}
