package com.compass.examination.core.controller.system;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.compass.examination.annotation.LogExceController;
import com.compass.examination.pojo.bo.ResultBO;

/**
 * 
 * @Class Name: SystemController
 * @Description: 系统
 * @author: wkm
 * @Company: www.compass.com
 * @Create date: 2017年8月14日下午5:11:16
 * @version: 2.0
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController {

	/**
	 * 
	 * @Method Name: getCurrentTime
	 * @Description: 获取当前时间
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2017年8月14日下午4:41:34
	 * @return
	 * @throws Exception: 
	 */
	@RequestMapping(value = "/getCurrentTime", method = RequestMethod.GET)
	@LogExceController(name = "获取当前时间")
	@ResponseBody
	public ResultBO getCurrentTime() throws Exception {
		return ResultBO.ok(new Date());
	}
}
