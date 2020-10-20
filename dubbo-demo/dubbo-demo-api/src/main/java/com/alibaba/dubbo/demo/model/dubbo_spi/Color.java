package com.alibaba.dubbo.demo.model.dubbo_spi;

import com.alibaba.dubbo.common.extension.SPI;

@SPI
public interface Color {
	void draw();
}
