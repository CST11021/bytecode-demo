package com.whz.asm.adviceadaptorDemo1;

/**
 * @Author:ChenZhangKun
 * @Date: 2021/9/8 17:43
 */
public class Sample {

    private String name;

    private int age;

    public Sample(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void test(long idCard, Object obj) {
        int hashCode = 0;
        hashCode += name.hashCode();
        hashCode += age;
        hashCode += (int) (idCard % Integer.MAX_VALUE);
        hashCode += obj.hashCode();
        hashCode = Math.abs(hashCode);
        System.out.println("Hash Code is " + hashCode);
    }

}
