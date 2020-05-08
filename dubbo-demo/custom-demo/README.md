### Java SPI机制

SPI 全称为 (Service Provider Interface) ，是JDK内置的一种服务提供发现机制。

Java SPI 实际上是“基于接口的编程＋策略模式＋配置文件”组合实现的动态加载机制。

#### 使用场景
概括地说，适用于：调用者根据实际使用需要，启用、扩展、或者替换框架的实现策略

    1. 数据库驱动加载接口实现类的加载：JDBC加载不同类型数据库的驱动
    2. 日志门面接口实现类加载：SLF4J 加载不同提供商的日志实现类
    3. Spring中大量使用了SPI,比如：对servlet3.0规范对ServletContainerInitializer的实现、自动类型转换Type Conversion SPI(Converter SPI、Formatter SPI)等
    4. Dubbo中也大量使用SPI的方式实现框架的扩展, 不过它对Java提供的原生SPI做了封装，允许用户扩展实现Filter接口
 
#### 使用介绍
要使用Java SPI，需要遵循如下约定：

    1. 当服务提供者提供了接口的一种具体实现后，在jar包的META-INF/services目录下创建一个以“接口全限定名”为命名的文件，内容为实现类的全限定名；
    2. 接口实现类所在的jar包放在主程序的classpath中；
    3. 主程序通过java.util.ServiceLoder动态装载实现模块，它通过扫描META-INF/services目录下的配置文件找到实现类的全限定名，把类加载到JVM；
    4. SPI的实现类必须携带一个不带参数的构造方法；

#### 实例代码
```
// 1. 定义接口
package com.leichu.dubbo.spi.demo.model;
public interface User { 
	void sayHello();
}

// 2. 定义接口实现类
package com.leichu.dubbo.spi.demo.model;
public class Student implements User{
	@Override
	public void sayHello() {
		System.out.println(">>>>>> Hello ...... I am a student ...");
	}
}

package com.leichu.dubbo.spi.demo.model;
public class Teacher implements User {
	@Override
	public void sayHello() {
		System.out.println(">>>>>> Hello ...... I am a teacher ...");
	}
}

// 3. src/main/resources/ 下创建 /META-INF/services 目录。
// 在此目录内创建一个文件，文件名为接口的全限定名（com.leichu.dubbo.spi.demo.model.User）
// 文件内容是实现类的全限定名。如下：
com.leichu.dubbo.spi.demo.model.Student
com.leichu.dubbo.spi.demo.model.Teacher

// 4. 测试
public class JavaSPI {
	public static void main(String[] args) {
		ServiceLoader<User> serviceLoader = ServiceLoader.load(User.class);
		System.out.println("Java SPI");
		serviceLoader.forEach(User::sayHello);
	}
}
```
