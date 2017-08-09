package com.compass.examination.rpc.impl;

import com.compass.examination.rpc.api.IPerson;

public class PersonImpl implements IPerson {

	@Override
	public String hello(String name) {
		return "hello " + name;
	}

	@Override
	public String hello2(String name) {
		return null;
	}

}
