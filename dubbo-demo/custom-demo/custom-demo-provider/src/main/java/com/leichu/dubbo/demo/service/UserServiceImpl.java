package com.leichu.dubbo.demo.service;

import com.leichu.dubbo.demo.model.User;

public class UserServiceImpl implements UserService {

	@Override
	public User get(long id) {
		System.out.println(">>>>>> com.leichu.dubbo.demo.provider.service.UserServiceImpl.get...");
		return null;
	}

	@Override
	public User login(String name, String pwd) {
		System.out.println(">>>>>> com.leichu.dubbo.demo.provider.service.UserServiceImpl.login...");
		return null;
	}
}
