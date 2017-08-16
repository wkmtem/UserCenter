package request;

import org.junit.Test;

import com.compass.common.http.HttpRequest;
import com.compass.examination.common.junit.SpringJunitTest;

public class RequestTest extends SpringJunitTest {
	
	@Test
	public void testRequest() {
		
		HttpRequest request = new HttpRequest();

		System.out.println("获取当前协议域名IP端口: " + request.getBaseURL());
		System.out.println("获取应用相对路径: " + request.getContextPath());
		System.out.println("获取当前应用的服务器绝对路径: " + request.getRealPath());
		System.out.println("获取完整的请求地址: " + request.getRequestURL());
		System.out.println("获取完整的资源地址: " + request.getRequestURI());
		System.out.println("获取客户端IP: " + request.getRemoteAddr());
		
		System.out.println("请求内容类型" + request.getRequest().getContentType());
    }

}
