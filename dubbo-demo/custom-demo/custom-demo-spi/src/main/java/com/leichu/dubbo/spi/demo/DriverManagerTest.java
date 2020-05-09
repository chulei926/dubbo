package com.leichu.dubbo.spi.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DriverManagerTest {

	private static final String URL = "jdbc:mysql://localhost:3306/leichu?characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";
	private static final String NAME = "root";
	private static final String PASSWORD = "123456";

	public static void main(String[] args) throws Exception {

		//1. 加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		//2. 获得数据库的连接
		Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);
		//3. 通过数据库的连接操作数据库，实现增删改查
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select `id`, `name`, `pwd` from tb_user"); //选择import java.sql.ResultSet;
		while (rs.next()) {//如果对象中有数据，就会循环打印出来
			System.out.println(">>>>> " + rs.getInt("id") + "   " + rs.getString("name") + "   " + rs.getString("pwd"));
		}
	}

}
