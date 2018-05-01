package cn.anqing.webserver.core;

import java.util.HashMap;
import java.util.Map;

public class ServerContext {
	private static Map<String, String> SERVER_MAPPING = new HashMap<String, String>();
	static{
		initServletMapping();
	}
	private static void initServletMapping(){
		SERVER_MAPPING.put("/myweb/reg", "cn.anqing.webserver.servlet.RegServlet");
		SERVER_MAPPING.put("/myweb/login", "cn.anqing.webserver.servlet.LoginServlet");
	}
	public static boolean hasServlet(String url){
		return SERVER_MAPPING.containsKey(url);
		
	}
	public static String getServletByUrl(String url){
		return SERVER_MAPPING.get(url);
	}
}
