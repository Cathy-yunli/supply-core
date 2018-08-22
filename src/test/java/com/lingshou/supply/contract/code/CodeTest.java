package com.lingshou.supply.contract.code;

public class CodeTest {
    public static void main(String[] args) {
        System.out.println(Code.displayValue(CodeTestBean.class, CodeTestBean.TEST_1));

        System.out.println(Code.displayValue(CodeTestBean.class, 4));

        System.out.println(Code.getIntValue(CodeTestBean.class, "test5"));
    }
}
