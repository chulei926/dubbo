package com.leichu.dubbo.test;
import org.junit.Test;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.demo.model.dubbo_spi.Color;

public class Dubbo_SPI_Test {

	@Test
	public void dubboSPI_Test(){
		ExtensionLoader<Color> extensionLoader = ExtensionLoader.getExtensionLoader(Color.class);
		Color red = extensionLoader.getExtension("red");
		red.draw();

		Color blue = extensionLoader.getExtension("blue");
		blue.draw();

		Color black = extensionLoader.getExtension("black");
		black.draw();
	}

}
