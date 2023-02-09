package com.whz.asm;

import java.util.Random;

/**
 * @Author:ChenZhangKun
 * @Date: 2021/8/4 16:43
 */
public class Test {
    private int intValue;
    private String strValue;

    public void test(boolean b) {
        if (b) {
            System.out.println("你好");
        } else {
            System.out.println("你不好");
        }
    }

    public void add(boolean b) {
        if (b) {
            System.out.println(3 + 5);
        } else {
            System.out.println(7);
        }

    }

    public int addNum(int a, int b) throws Exception {
        int c = a + b;
        Random random = new Random(System.currentTimeMillis());
        int i = random.nextInt(300);
        Thread.sleep(i + 100);
        return c;
    }

    public int sub(int a, int b) throws Exception {
        int c = a - b;
        Random random = new Random(System.currentTimeMillis());
        int i = random.nextInt(300);
        Thread.sleep(i + 100);
        return c;
    }
}
