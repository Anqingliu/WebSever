package cn.anqing.webserver.servlet;

import java.io.RandomAccessFile;

import cn.anqing.webserver.http.HttpRequest;
import cn.anqing.webserver.http.HttpResponse;

public class LoginServlet extends HttpServlet{

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		boolean hava = false;
		try (
				RandomAccessFile raf = new RandomAccessFile("user.dat", "r");
		){
			for(int i = 0; i < raf.length() / 100; i++){
				raf.seek(100 * i);
				byte[] data = new byte[32];
				raf.read(data);
				String oldName  = new String(data, "UTF-8").trim();
				String oldPassword = new String(data, "UTF-8").trim();
				if(username.equals(oldName) && password.equals(oldPassword)){
					this.forward("/myweb/login_success.html", request, response);
					hava = true;
					break;
				}
				if(!hava){
					this.forward("/myweb/login_fail.html", request, response);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
