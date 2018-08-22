package com.lingshou.supply.common.bean;

import com.lingshou.supply.common.bean.anno.AutowireIfNull;

import java.math.BigDecimal;

public class TestAutowireBean {
    @AutowireIfNull
    private boolean aBoolean;

    private Boolean bigBoolean;

    @AutowireIfNull
    private long aLong;

    @AutowireIfNull
    private Long bigLong;

    @AutowireIfNull
    private int anInt;

    @AutowireIfNull
    private Integer bigInt;

    @AutowireIfNull
    private String string;

    @AutowireIfNull
    private double aDouble;

    @AutowireIfNull
    private Double bigDouble;

    @AutowireIfNull
    private BigDecimal bigDecimal;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestAutowireBean{");
        sb.append("aBoolean=").append(aBoolean);
        sb.append(", bigBoolean=").append(bigBoolean);
        sb.append(", aLong=").append(aLong);
        sb.append(", bigLong=").append(bigLong);
        sb.append(", anInt=").append(anInt);
        sb.append(", bigInt=").append(bigInt);
        sb.append(", string='").append(string).append('\'');
        sb.append(", aDouble=").append(aDouble);
        sb.append(", bigDouble=").append(bigDouble);
        sb.append(", bigDecimal=").append(bigDecimal);
        sb.append('}');
        return sb.toString();
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public Boolean getBigBoolean() {
        return bigBoolean;
    }

    public void setBigBoolean(Boolean bigBoolean) {
        this.bigBoolean = bigBoolean;
    }

    public long getaLong() {
        return aLong;
    }

    public void setaLong(long aLong) {
        this.aLong = aLong;
    }

    public Long getBigLong() {
        return bigLong;
    }

    public void setBigLong(Long bigLong) {
        this.bigLong = bigLong;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public Integer getBigInt() {
        return bigInt;
    }

    public void setBigInt(Integer bigInt) {
        this.bigInt = bigInt;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public Double getBigDouble() {
        return bigDouble;
    }

    public void setBigDouble(Double bigDouble) {
        this.bigDouble = bigDouble;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
}
