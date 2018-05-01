package cn.anqing.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import cn.anqing.webserver.core.EmptyRequestException;

public class HttpRequest {
	private Socket socket;
	private InputStream in;
	private String method;
	private String url;
	private String requestURI;
	private String queryString;
	private String protocol;
	private Map<String, String> parameters = new HashMap<String, String>();
	private Map<String, String>	 headers = new HashMap<String, String>();
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket = socket;
			this.in = socket.getInputStream();
			parseRequestLine();
			parseHeaders();
			parseContent();
		}catch(EmptyRequestException e){
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void parseContent(){
		try {
			if(this.headers.containsKey("Content-Length")){
				int len = Integer.parseInt(this.headers.get("Content-Length"));
				byte[] data = new byte[len];
				in.read(data);
				String type = this.headers.get("Content-Type");
				//判断提交的是否为表单数据
				if("application/x-www-form-urlencoded".equals(type)){
					String str = new String(data, "ISO8859-1");
					URLDecoder.decode(str, "UTF-8");
					parseParameters(str);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void parseHeaders(){
		String line = null;
		while(!(line = readLine()).equals("")){
			String[] content = line.split(":\\s");
			headers.put(content[0], content[1]);
		}
	}
	
	private void parseRequestLine() throws EmptyRequestException, UnsupportedEncodingException{
		String line = readLine();
		String[] str = line.split("\\s");
		if(str.length < 3){
			throw new EmptyRequestException();
		}
		this.method = str[0];
		this.url = str[1];
		this.protocol = str[2];
		parseURL();
	}
	private void parseURL() throws UnsupportedEncodingException {
		this.url = URLDecoder.decode(this.url, "UTF-8");
		if(!(url.indexOf("?") == -1)){
			String[] data = url.split("\\?");
			this.requestURI = data[0];
			if(data.length > 1){
				this.queryString = data[1];
				parseParameters(this.queryString);
			}else{
				this.queryString = "";
			}
		}else{
			this.requestURI = url;
		}
	}
	private void parseParameters(String line) {
		String[] data = line.split("&");
		for(String parameter : data){
			String[] paraArr = parameter.split("=");
			if(paraArr.length > 1){
				parameters.put(paraArr[0], paraArr[1]);
			}else{
				parameters.put(paraArr[0], "");
			}
		}
	}
	private String readLine(){
		try {
			StringBuilder builder = new StringBuilder();
			int d = -1;
			char c1 = 'a', c2 = 'a';
			while((d = in.read()) != -1){
				c2 = (char)d;
				if(c1 == 13 && c2 == 10){
					break;
				}
				c1 = c2;
				builder.append(c2);
			}
			return builder.toString().trim();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	public String getParameter(String name){
		return this.parameters.get(name);
	}
	
}
