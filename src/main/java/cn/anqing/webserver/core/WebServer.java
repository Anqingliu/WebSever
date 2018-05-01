package cn.anqing.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
	private ServerSocket server;
	private ExecutorService threadPool;
	public WebServer() {
		try {
			this.server = new ServerSocket(8080);
			threadPool = Executors.newFixedThreadPool(40);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start(){
		try {
			while (true) {
				Socket socket = server.accept();
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		WebServer serever = new WebServer();
		serever.start();
	}
	
}
