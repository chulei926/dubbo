package com.alibaba.dubbo.demo.model.java_spi;

public class Black implements Color {
	@Override
	public void draw() {
		System.out.println(" I am black");
	}
}
