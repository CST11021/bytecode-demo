package com.whz.aspectj.demo1;

public class TestBean {

    private String testStr = "testStr";

    public void printStr() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("print test ... ");
    }

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }
}
