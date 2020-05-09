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
package org.apache.dubbo.config.spring;

import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.event.ServiceBeanExportedEvent;
import org.apache.dubbo.config.spring.extension.SpringExtensionFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * ServiceFactoryBean：工厂bean。
 * <p>
 * 1. 实现了 InitializingBean 接口，实现 afterPropertiesSet() 方法，在属性设置完成之后执行。<br/>
 * 2. 实现了 DisposableBean 接口，实现 destroy() 方法，在 容器销毁时执行。<br/>
 * 3. 实现了 ApplicationContextAware 接口，实现 setApplicationContext() 方法，给 bean 添加容器上下文。<br/>
 * 4. 实现了 BeanNameAware 接口，实现 setBeanName() 方法，设置 bean 名称。<br/>
 * 5. 实现了 ApplicationEventPublisherAware 接口，实现 setApplicationEventPublisher() 方法，在容器刷新完成之后执行。
 * <p>
 * Dubbo 服务导出过程始于 Spring 容器发布刷新事件，Dubbo 在接收到事件后，会立即执行服务导出逻辑。<br/>
 * 整个逻辑大致可分为<b>三个部分</b>：<br/>
 * 第一部分是前置工作，主要用于检查参数，组装 URL。<br/>
 * 第二部分是导出服务，包含导出服务到本地 (JVM)，和导出服务到远程两个过程。<br/>
 * 第三部分是向注册中心注册服务，用于服务发现。
 *
 * @export
 */
public class ServiceBean<T> extends ServiceConfig<T> implements InitializingBean, DisposableBean, ApplicationContextAware, BeanNameAware, ApplicationEventPublisherAware {


	private static final long serialVersionUID = 213195494150089726L;

	private final transient Service service;

	private transient ApplicationContext applicationContext;

	private transient String beanName;

	private ApplicationEventPublisher applicationEventPublisher;

	public ServiceBean() {
		super();
		this.service = null;
	}

	public ServiceBean(Service service) {
		super(service);
		this.service = service;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		SpringExtensionFactory.addApplicationContext(applicationContext);
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	/**
	 * Gets associated {@link Service}
	 *
	 * @return associated {@link Service}
	 */
	public Service getService() {
		return service;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isEmpty(getPath())) {
			if (StringUtils.isNotEmpty(beanName) && StringUtils.isNotEmpty(getInterface()) && beanName.startsWith(getInterface())) {
				setPath(beanName);
			}
		}
	}

	/**
	 * Get the name of {@link ServiceBean}
	 *
	 * @return {@link ServiceBean}'s name
	 * @since 2.6.5
	 */
	public String getBeanName() {
		return this.beanName;
	}

	@Override
	public void exported() {
		super.exported();
		// Publish ServiceBeanExportedEvent
		publishExportEvent();
	}

	/**
	 * @since 2.6.5
	 */
	private void publishExportEvent() {
		ServiceBeanExportedEvent exportEvent = new ServiceBeanExportedEvent(this);
		applicationEventPublisher.publishEvent(exportEvent);
	}

	@Override
	public void destroy() throws Exception {
		// no need to call unexport() here, see
		// org.apache.dubbo.config.spring.extension.SpringExtensionFactory.ShutdownHookListener
	}

	// merged from dubbox
	@Override
	protected Class getServiceClass(T ref) {
		if (AopUtils.isAopProxy(ref)) {
			return AopUtils.getTargetClass(ref);
		}
		return super.getServiceClass(ref);
	}

	/**
	 * @param applicationEventPublisher
	 * @since 2.6.5
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
