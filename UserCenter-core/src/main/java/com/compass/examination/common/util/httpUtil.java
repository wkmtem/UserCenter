package com.compass.examination.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.base.Charsets;

public class httpUtil {
	
	/**
	 * 
	 * @Method Name: getIPaddress
	 * @Description: 通过HttpServletRequest获取用户IP地址
	 * @params:
	 * @author: wkm
	 * @version: 2.0
	 * @Create date: 2016-11-27下午2:55:43
	 * @param request
	 * @return
	 * @throws Exception:
	 */
	public static String getIPaddress(HttpServletRequest request) throws Exception {
		String ipUser = request.getHeader("x-forwarded-for");
		if (ipUser == null || ipUser.length() == 0
				|| "unknown".equalsIgnoreCase(ipUser)) {
			ipUser = request.getHeader("Proxy-Client-IP");
		}
		if (ipUser == null || ipUser.length() == 0
				|| "unknown".equalsIgnoreCase(ipUser)) {
			ipUser = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipUser == null || ipUser.length() == 0
				|| "unknown".equalsIgnoreCase(ipUser)) {
			ipUser = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ipUser == null || ipUser.length() == 0
				|| "unknown".equalsIgnoreCase(ipUser)) {
			ipUser = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ipUser == null || ipUser.length() == 0
				|| "unknown".equalsIgnoreCase(ipUser)) {
			ipUser = request.getRemoteAddr();
		}
		return ipUser;
	}
	
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String user) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Cookie", user);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }  
    
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            httprequest请求参数。
     * @param headers
     *            需要添加的httpheader参数
     * @param timeout
     *            请求超时时间
     * @return result 所代表远程资源的响应结果
     */
    public static String Get(String url, String param, Map<String, String> headers, int timeout) {
        String result = "";
        BufferedReader in = null;
        String reqUrl = url ;
        try {
            // 构造httprequest设置
            RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout).build();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpGet htGet = new HttpGet(reqUrl);
            // 添加http headers
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    htGet.addHeader(key, headers.get(key));
                }
            }
            // 读取数据
            HttpResponse r = client.execute(htGet);
            in = new BufferedReader(new InputStreamReader(r.getEntity().getContent(), Charsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in = null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
