<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/tag.jsp"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title> 注册-后台管理系统</title>
<link rel="stylesheet" type="text/css" href="${baseurl}css/base.css" />
<link rel="stylesheet" type="text/css" href="${baseurl}css/style.css" />
<link rel="stylesheet" type="text/css" href="${baseurl}css/register.css" />
<script src="${baseurl}js/jquery-1.11.1.min.js"></script>
<script src="${baseurl}js/layer.js"></script>
</head>

<body>
	<input id="hide_flag" type="hide">
	<div class="register">
		<div class="register-header clearfix">
			<div class="register-leftb"></div>
			<div class="register-rightb"></div>
			<img src="${baseurl}images/reg-backg.png" alt="" class="register-img">
		</div>

		<div class="register-forms">
			<div class="register-form">
				<h2 class="register-title">注册开通企业</h2>

				<div class="register-success">
					<img src="${baseurl}images/reg-success.png" alt="">

					<p class="register-smsg">激活成功</p>
					<p class="register-swel">欢迎使用“面试系统”</p>
				</div>

				<div class="register-fail">
					<img src="${baseurl}images/reg-fail.png" alt="">

					<p class="register-fmsg">激活失败</p>
					<p class="register-fwel">您的注册信息已经失效</p>
				</div>
			</div>

			<div class="form-cont register-again">
				<input type="submit" class="btn btn-primary" value="重新激活" style="margin:20px auto;display: block;">
			</div>
		</div>
		<!-- <div class="register-footer"></div -->
	</div>
</body>
<script type="text/javascript">
	var flag = $("#hide_flag").val();
	if(flag === "EM27"){ // 激活成功
		$(".register-success").show();
		$(".register-fail").hide();
		$(".form-cont register-again").hide();
	} else if (flag === "EM28") { // 激活失败
		
	} else { // 激活超时
		
	}
</script>

</html>