package cn.anqing.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponse {
	private int statusCode = 200;
	private String statusReason = "OK";
	private Socket socket;
	private OutputStream out;
	private File entity;
	private Map<String, String> headers = new HashMap<String, String>();
	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void flush(){
		sendStatusLine();
		sendHeaders();
		sendContent();
	}
	private void sendContent() {
		try (
				FileInputStream fis = new FileInputStream(entity);
		){
			int len = -1;
			byte[] data = new byte[1024 * 10];
			if((len = fis.read(data)) != -1){
				out.write(data, 0, len);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void sendHeaders() {
		try {
			for(Entry<String, String> header : headers.entrySet()){
				String name = header.getKey();
				String value = header.getValue();
				println(name + ": " + value);
			}
			println("");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void sendStatusLine() {
		try {
			String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
			println(line);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void println(String line){
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public File getEntity() {
		return entity;
	}
	public void setEntity(File entity) {
		if(entity.getName().matches(".+\\.[a-zA-Z0-9]+")){
			String ext = entity.getName().split("\\.")[1];
			//不懂
			headers.put("Content-Type", HttpContext.getMimeMapping(ext));
		}
		headers.put("Content-Length", entity.length() + "");
		this.entity = entity;
	}
	public void putHeaders(String name, String value){
		this.headers.put(name, value);
	}
	public String getHeader(String name){
		return this.headers.get(name);
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		this.statusReason = HttpContext.getStatusReason(statusCode);
	}
	
}
