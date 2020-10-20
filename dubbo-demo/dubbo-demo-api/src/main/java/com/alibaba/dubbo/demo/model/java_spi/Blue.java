package com.alibaba.dubbo.demo.model.java_spi;

public class Blue implements Color {
	@Override
	public void draw() {
		System.out.println(" I am blue");
	}
}
