package com.whz.asm.adviceadaptorDemo1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @Author:ChenZhangKun
 * @Date: 2021/9/8 17:48
 */
public class ParameterUtils {

    private static final DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void printText(String str) {
        System.out.println("printText输出:" + str);
    }

    public static void printValueOnStack(Object value) {
        System.out.println("printValueOnStack输出:" + value);
    }

}
