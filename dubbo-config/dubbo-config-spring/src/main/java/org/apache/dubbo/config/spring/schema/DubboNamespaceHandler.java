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
package org.apache.dubbo.config.spring.schema;

import com.alibaba.spring.util.AnnotatedBeanDefinitionRegistryUtils;
import org.apache.dubbo.common.Version;
import org.apache.dubbo.config.*;
import org.apache.dubbo.config.spring.ConfigCenterBean;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.config.spring.ServiceBean;
import org.apache.dubbo.config.spring.beans.factory.config.ConfigurableSourceBeanMetadataElement;
import org.apache.dubbo.config.spring.context.DubboLifecycleComponentApplicationListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.w3c.dom.Element;

import static com.alibaba.spring.util.AnnotatedBeanDefinitionRegistryUtils.registerBeans;

/**
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
public class DubboNamespaceHandler extends NamespaceHandlerSupport implements ConfigurableSourceBeanMetadataElement {

	static {
		Version.checkDuplicate(DubboNamespaceHandler.class);
	}

	@Override
	public void init() {
		System.out.println(">>>>> DubboNamespaceHandler >>> 开始注册 dubbo 配置文件解析器 ");
		// 用于配置当前应用信息，不管该应用是提供者还是消费者
		registerBeanDefinitionParser("application", new DubboBeanDefinitionParser(ApplicationConfig.class, true));
		// 用于配置当前模块信息，可选
		registerBeanDefinitionParser("module", new DubboBeanDefinitionParser(ModuleConfig.class, true));
		// 用于配置连接注册中心相关信息
		registerBeanDefinitionParser("registry", new DubboBeanDefinitionParser(RegistryConfig.class, true));
		// 配置中心。
		registerBeanDefinitionParser("config-center", new DubboBeanDefinitionParser(ConfigCenterBean.class, true));
		//
		registerBeanDefinitionParser("metadata-report", new DubboBeanDefinitionParser(MetadataReportConfig.class, true));
		// 用于配置连接监控中心相关信息，可选
		registerBeanDefinitionParser("monitor", new DubboBeanDefinitionParser(MonitorConfig.class, true));
		//
		registerBeanDefinitionParser("metrics", new DubboBeanDefinitionParser(MetricsConfig.class, true));
		// 当 ProtocolConfig 和 ServiceConfig 某属性没有配置时，采用此缺省值，可选
		registerBeanDefinitionParser("provider", new DubboBeanDefinitionParser(ProviderConfig.class, true));
		// 当 ReferenceConfig 某属性没有配置时，采用此缺省值，可选
		registerBeanDefinitionParser("consumer", new DubboBeanDefinitionParser(ConsumerConfig.class, true));
		// 用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受
		registerBeanDefinitionParser("protocol", new DubboBeanDefinitionParser(ProtocolConfig.class, true));
		// 服务提供者暴露服务配置。用于暴露一个服务，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心
		registerBeanDefinitionParser("service", new DubboBeanDefinitionParser(ServiceBean.class, true));
		// 服务消费者引用服务配置。用于创建一个远程服务代理，一个引用可以指向多个注册中心
		registerBeanDefinitionParser("reference", new DubboBeanDefinitionParser(ReferenceBean.class, false));
		//
		registerBeanDefinitionParser("annotation", new AnnotationBeanDefinitionParser());
	}

	/**
	 * 解析dubbo配置文件。
	 * <p>
	 * 有多少标签，就解析多少次！<br/>
	 * 每解析到一个 dubbo:service，就暴露一个接口服务。
	 * </p>
	 * Override {@link NamespaceHandlerSupport#parse(Element, ParserContext)} method
	 *
	 * @param element       {@link Element}
	 * @param parserContext {@link ParserContext}
	 * @return
	 * @since 2.7.5
	 */
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		/**
		 * >>>>> DubboNamespaceHandler >>> 开始解析 dubbo 配置文件 >>> dubbo:application
		 * >>>>> DubboNamespaceHandler >>> 开始解析 dubbo 配置文件 >>> dubbo:registry
		 * >>>>> DubboNamespaceHandler >>> 开始解析 dubbo 配置文件 >>> dubbo:protocol
		 * >>>>> DubboNamespaceHandler >>> 开始解析 dubbo 配置文件 >>> dubbo:service
		 */
		System.out.println(">>>>> DubboNamespaceHandler >>> 开始解析 dubbo 配置文件 >>> " + element.getTagName());
		//获取 BeanFactory
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		// 注册 注解相关的解析器
		registerAnnotationConfigProcessors(registry);
		//注册 两个监听器：
		// 1. DubboLifecycleComponentApplicationListener （Dubbo 生命周期监听器）
		// 2. DubboBootstrapApplicationListener（处理服务注册逻辑，IOC 初始化 refresh 的最后一步 finishrefresh()）
		registerDubboLifecycleComponentApplicationListener(registry);
		BeanDefinition beanDefinition = super.parse(element, parserContext);
		setSource(beanDefinition);
		return beanDefinition;
	}

	/**
	 * Register {@link DubboLifecycleComponentApplicationListener} as a Spring Bean
	 *
	 * @param registry {@link BeanDefinitionRegistry}
	 * @see DubboLifecycleComponentApplicationListener
	 * @see AnnotatedBeanDefinitionRegistryUtils#registerBeans(BeanDefinitionRegistry, Class[])
	 * @since 2.7.5
	 */
	private void registerDubboLifecycleComponentApplicationListener(BeanDefinitionRegistry registry) {
		System.out.println(">>>>> DubboNamespaceHandler >>> 注册 dubbo 生命周期监听器 >>> " + registry.getClass().getName());
		registerBeans(registry, DubboLifecycleComponentApplicationListener.class);
	}

	/**
	 * Register the processors for the Spring Annotation-Driven features
	 *
	 * @param registry {@link BeanDefinitionRegistry}
	 * @see AnnotationConfigUtils
	 * @since 2.7.5
	 */
	private void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
		AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);
	}
}
