package com.leichu.dubbo.spi.demo;

import com.leichu.dubbo.spi.demo.model.User;

import java.util.ServiceLoader;

public class JavaSPI {

	public static void main(String[] args) {
		ServiceLoader<User> serviceLoader = ServiceLoader.load(User.class);
		System.out.println("Java SPI");
		serviceLoader.forEach(User::sayHello);
	}

}
