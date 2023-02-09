package com.whz.asm.adviceadaptorDemo1;

import java.util.Date;

/**
 * @Author:ChenZhangKun
 * @Date: 2021/9/8 17:45
 */
public class SampleTest {

    /**
     * 测试步骤：
     * 1、执行：SampleTest#main()
     * 2、执行：AdviceAdaptorTransformDemo#main() 对SampleTest的方法植入增强逻辑
     * 3、再次执行：SampleTest#main()
     *
     * 结果：先后两次执行SampleTest#main()的输出变了
     *
     * @param args
     */
    public static void main(String[] args) {
        Sample sample = new Sample("tomcat", 10);
        sample.test(4644545L, new Date());
    }

}
