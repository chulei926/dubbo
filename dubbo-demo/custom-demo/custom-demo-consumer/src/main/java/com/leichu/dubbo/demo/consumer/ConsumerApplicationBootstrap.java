package com.leichu.dubbo.demo.consumer;

import com.leichu.dubbo.demo.model.User;
import com.leichu.dubbo.demo.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerApplicationBootstrap {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
		context.start();
		UserService userService = context.getBean(UserService.class);
		User user = userService.get(1);
		System.out.println("result :" + user);

		try {
			while (true) {
				Thread.sleep(100 * 1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
