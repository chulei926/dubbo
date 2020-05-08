package com.leichu.dubbo.demo.model;

import java.io.Serializable;

/**
 * 用户.
 *
 * @author leichu 2020-05-07.
 */
public class User implements Serializable {

	private long id;
	private String name;
	private String pwd;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
