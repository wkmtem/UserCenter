package request;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.compass.examination.common.junit.SpringJunitTest;

public class RequestTest extends SpringJunitTest {
	
	@Test
	public void testRequest() {
		HttpServletRequest request = 
				((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
		StringBuffer requestURL = request.getRequestURL();
		System.out.println(request.getScheme()); // 协议
		System.out.println(request.getServerName()); // 域名或ip
		System.out.println(request.getServerPort()); // 端口
		System.out.println(request.getContextPath()); // 容器路径
		System.out.println("------------------------");
		System.out.println(request.getRequestURL()); // 完整请求地址
		System.out.println(request.getRequestURI()); // 完整资源地址
		System.out.println("------------------------");
		System.out.println(request.getServletContext().getRealPath("/")); // 应用项目服务器路径
		System.out.println(request.getRealPath("/")); // 同上，过时
		System.out.println(request.getQueryString()); // 
		System.out.println(); // 
    }

}
