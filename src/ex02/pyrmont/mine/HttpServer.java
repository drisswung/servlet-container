package ex02.pyrmont.mine;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpServer {
	
	private static final int BACK_LOG = 120;
	
	private static boolean shutdown = false;
	
	static final String STATIC_RESOURCE_PREFIX = "/static";
	
	private static ExecutorService executorService = new ThreadPoolExecutor(2, Runtime.getRuntime().availableProcessors() * 2,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

	public static void main(String[] args) {
		new HttpServer().await(8080);
		executorService.shutdown();
	}
	
	private void await(int port) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port, BACK_LOG);
			while (!shutdown) {
				Socket socket = serverSocket.accept();
				executorService.execute(new SocketHandler(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {}
		}
		
	}
}

class SocketHandler implements Runnable {

	private Socket socket = null;
	
	public SocketHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		Request request = null;
		Response response = null;
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			request = new Request(in);
			response = new Response(out);
			request.parse();
			if (request.getRequestUri().startsWith(HttpServer.STATIC_RESOURCE_PREFIX)) {
				StaticResourceHandler.handler(request, response);
			} else {
				ServletResourceHandler.handler(request, response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				request.destory();
			}
			if (response != null) {
				response.destory();
			}
		}
		
	}
	
}
