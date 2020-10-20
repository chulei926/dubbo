package com.alibaba.dubbo.demo.model.dubbo_spi;

public class Blue implements Color{
	@Override
	public void draw() {
		System.out.println(" I am blue");
	}
}
