package com.whz.aspectj.demo2.impl;


import com.whz.aspectj.demo2.Seller;

public class SmartSeller implements Seller {

	public int sell(String goods,String clientName) {
        System.out.println("SmartSeller: sell " + goods + " to " + clientName+"...");
		return 100;
	}
	
	public void checkBill(int billId){
		if(billId == 1) throw new IllegalArgumentException("iae Exception");
		else throw new RuntimeException("re Exception");
	}
}