package com.compass.examination.rpc.api;

public class IPersonImpl implements IPerson {

	@Override
	public String hello(String name) {
		
		return name;
	}

}
