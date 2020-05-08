package com.leichu.dubbo.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProviderApplicationBootstrap {

	private static ClassPathXmlApplicationContext context = null;

	static {
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread(() -> {
			if (context != null) {
				context.close();
				context = null;
			}
		}));
	}

	public static void main(String[] args) {
		try {
			context = new ClassPathXmlApplicationContext("spring.xml", "provider.xml");
			context.start();
			System.out.println(">>>>>> 启动成功 <<<<<<");
			while (true) {
				Thread.sleep(10 * 1000);
			}
		} catch (Exception e) {
			System.err.println(">>>>>> 启动失败 <<<<<<");
			e.printStackTrace();
			System.exit(0);
		}
	}

}
