package com.leichu.dubbo.demo.service;

import com.leichu.dubbo.demo.model.User;

public interface UserService {

	User get(long id);

	User login(String name, String pwd);

}
