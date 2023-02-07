package com.whz.aspectj;

import com.whz.aspectj.demo1.TestBean;
import com.whz.aspectj.demo2.Seller;
import com.whz.aspectj.demo2.Waiter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:aspectJConfig.xml"})
public class TestMain {

    @Resource
    private TestBean testBean;

    /**
     * 测试一般类型的增强
     */
    @Test
    public void testInterceptor1(){
        testBean.printStr();
    }






    @Resource
    private Waiter waiter;

    /**
     * 测试引介增强
     */
    @Test
    public void testInterceptor2() {
        waiter.greetTo("John");
        Seller seller = (Seller) waiter;
        seller.sell("Beer", "John");
    }

}