package cn.anqing.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import cn.anqing.webserver.http.HttpRequest;
import cn.anqing.webserver.http.HttpResponse;
import cn.anqing.webserver.servlet.HttpServlet;

public class ClientHandler implements Runnable{
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		super();
		this.socket = socket;
	}

	

	@Override
	public void run() {
		try {
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			String url = request.getRequestURI();
			System.out.println(url);
			if(ServerContext.hasServlet(url)){
				String servletName = ServerContext.getServletByUrl(url);
				Class cls = Class.forName(servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else{
				File file = new File("webapps" + url);
				if(file.exists()){
					response.setEntity(file);
				}else{
					file = new File("webapps/sys/404.html");
					response.setStatusCode(404);
					response.setEntity(file);
				}
				
			}
			response.flush();
		} catch(EmptyRequestException e){
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
