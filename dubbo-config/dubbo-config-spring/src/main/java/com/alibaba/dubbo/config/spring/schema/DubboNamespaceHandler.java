/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.config.spring.schema;

import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * TODO 入口。
 *
 * DubboNamespaceHandler：dubbo的命名空间处理器。
 * <pre>
 * 解析 dubbo 配置文件中的 各种标签。
 * 在 dubbo-config-spring/src/main/resources 目录下，有一个 spring.handlers 文件，
 * 该文件中配置了 ：
 * http\://dubbo.apache.org/schema/dubbo=org.apache.dubbo.config.spring.schema.DubboNamespaceHandler
 * http\://code.alibabatech.com/schema/dubbo=org.apache.dubbo.config.spring.schema.DubboNamespaceHandler
 * 也就是说，http://dubbo.apache.org/schema/dubbo 和 http://code.alibabatech.com/schema/dubbo 命名空间 使用 DubboNamespaceHandler 处理器来解析。
 *
 * </pre>
 *
 * @export
 */
public class DubboNamespaceHandler extends NamespaceHandlerSupport {

	static {
		Version.checkDuplicate(DubboNamespaceHandler.class);
	}

	@Override
	public void init() {
		registerBeanDefinitionParser("application", new DubboBeanDefinitionParser(ApplicationConfig.class, true));
		registerBeanDefinitionParser("module", new DubboBeanDefinitionParser(ModuleConfig.class, true));
		registerBeanDefinitionParser("registry", new DubboBeanDefinitionParser(RegistryConfig.class, true));
		registerBeanDefinitionParser("monitor", new DubboBeanDefinitionParser(MonitorConfig.class, true));
		registerBeanDefinitionParser("provider", new DubboBeanDefinitionParser(ProviderConfig.class, true));
		registerBeanDefinitionParser("consumer", new DubboBeanDefinitionParser(ConsumerConfig.class, true));
		registerBeanDefinitionParser("protocol", new DubboBeanDefinitionParser(ProtocolConfig.class, true));
		// 服务暴露
		registerBeanDefinitionParser("service", new DubboBeanDefinitionParser(ServiceBean.class, true));
		registerBeanDefinitionParser("reference", new DubboBeanDefinitionParser(ReferenceBean.class, false));
		registerBeanDefinitionParser("annotation", new AnnotationBeanDefinitionParser());
	}

}
