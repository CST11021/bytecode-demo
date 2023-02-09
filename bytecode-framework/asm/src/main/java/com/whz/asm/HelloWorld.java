package com.whz.asm;

import java.util.function.Consumer;

/**
 * @Author:ChenZhangKun
 * @Date: 2021/7/19 21:58
 */
public class HelloWorld {

    public static void main(String[] args) {
        Consumer<String> c = System.out::println;
        c.accept("helloworld");
    }

    public void test(int a, int b) {
        int c = Math.max(a, b);
        int d = Math.max(a, b);
        System.out.println(d);
        System.out.println(c);
    }

    public void test1(int a, int b) {
        int c = a + b;
        int d = c + 0;
        System.out.println(d);
    }

    private int val;
    public void test2(int a, int b){
        int c=a+b;
        // 删除这个语句
        this.val=this.val;
        System.out.println(c);
    }
}
