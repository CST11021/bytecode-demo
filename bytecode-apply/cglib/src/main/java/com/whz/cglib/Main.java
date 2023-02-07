package com.whz.cglib;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Cglib测试类
 * @author cuiran
 * @version 1.0
 */
public class Main {

    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws ClassNotFoundException {

		// 动态生成 Bean
		CglibBean bean = buildCglibBean();

		// 获得bean的实体
		Object object = bean.getObject();
		Class clazz = object.getClass();

		// 通过反射查看所有方法名
		Method[] methods = clazz.getDeclaredMethods();
		System.out.println("一、查看目标类的所有方法：");
		for (int i = 0; i < methods.length; i++) {
			System.out.println(methods[i].getName());
		}

		// 从 Bean 中获取值，当然了获得值的类型是 Object
		System.out.println("二、获取目标类的字段值：");
		System.out.println(">> id      = " + bean.getValue("id"));
		System.out.println(">> name    = " + bean.getValue("name"));
		System.out.println(">> address = " + bean.getValue("address"));

	}

	private static CglibBean buildCglibBean() throws ClassNotFoundException {
		// 构建类的字段信息
		HashMap propertyMap = new HashMap();
		propertyMap.put("id", Class.forName("java.lang.Integer"));
		propertyMap.put("name", Class.forName("java.lang.String"));
		propertyMap.put("address", Class.forName("java.lang.String"));

		// 生成动态 Bean
		CglibBean bean = new CglibBean(propertyMap);
		bean.setValue("id", new Integer(123));
		bean.setValue("name", "454");
		bean.setValue("address", "789");

		return bean;
	}

}