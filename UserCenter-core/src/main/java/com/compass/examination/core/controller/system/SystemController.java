package com.compass.examination.core.controller.system;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.examination.annotation.LogExceController;
import com.compass.examination.pojo.bo.ResultBO;
import com.compass.examination.rpc.api.IPersonRPCService;

/**
 * 
 * <p>Class Name: SystemController</p>
 * <p>Description: 系统</p>
 * <p>Company: www.compass.com</p> 
 * @author wkm
 * @date 2017年8月15日上午11:23:20
 * @version 2.0
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController {
	
	@Autowired
	private IPersonRPCService personRPCService;

	
	/**
	 * 测试RPC服务调用
	 */
	@RequestMapping(value = "/testRPC", method = RequestMethod.GET)
	@LogExceController(name = "测试RPC服务调用")
	@ResponseBody
	public ResultBO testRPC(String name) throws Exception {
		String showName = personRPCService.showName(name);
		return ResultBO.succeeded(showName);
	}
	
	
	/**
	 * 
	 * <p>Method Name: getCurrentTime</p>
	 * <p>Description: 获取当前时间</p>
	 * @author wkm
	 * @date 2017年8月15日上午11:23:32
	 * @version 2.0
	 * @return resultBO(code[1, 0], msg[str], info[str:yyyy-MM-dd HH:mm:ss])
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCurrentTime", method = RequestMethod.GET)
	@LogExceController(name = "获取当前时间")
	@ResponseBody
	public ResultBO getCurrentTime() throws Exception {		
		return ResultBO.succeeded(new Date());
	}
	
}
