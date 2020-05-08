package com.leichu.dubbo.spi.demo.model;

public class Student implements User{
	@Override
	public void sayHello() {
		System.out.println(">>>>>> Hello ...... I am a student ...");
	}
}
