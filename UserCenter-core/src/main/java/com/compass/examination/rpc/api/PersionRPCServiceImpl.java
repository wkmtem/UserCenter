package com.compass.examination.rpc.api;

import org.springframework.stereotype.Service;

@Service
public class PersionRPCServiceImpl implements IPersonRPCService{

	@Override
	public String showName(String name) {
		
		return name;
	}

}
