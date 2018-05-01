package cn.anqing.webserver.servlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

import cn.anqing.webserver.http.HttpRequest;
import cn.anqing.webserver.http.HttpResponse;

public class RegServlet extends HttpServlet{

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		
		try (
				RandomAccessFile raf = new RandomAccessFile("user.dat", "rw");
		){
			raf.seek(raf.length());
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			raf.writeInt(age);
			forward("/myweb/reg_success.html", request, response);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
