package com.whz.aspectj.demo2;


import com.whz.aspectj.demo2.impl.SmartSeller;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;


@Aspect
public class AspectBean2 {

   @DeclareParents(value="com.whz.aspectj.demo2.impl.NaiveWaiter", defaultImpl= SmartSeller.class)
   public Seller seller;

}
