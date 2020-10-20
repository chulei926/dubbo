package com.leichu.dubbo.test;

import com.alibaba.dubbo.demo.model.java_spi.Color;
import org.junit.Test;

import java.util.ServiceLoader;

public class Java_SPI_Test {

	@Test
	public void spiTest(){
		ServiceLoader<Color> serviceLoader = ServiceLoader.load(Color.class);
		System.out.println("Java SPI");
		serviceLoader.forEach(Color::draw);
		// Java SPI
		// I am black
		// I am blue
		// I am Red
	}

}
