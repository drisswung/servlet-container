package ex01.pyrmont;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestBackLog {

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(8080, 1);
		
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println(socket.getPort());
			new MyThread(socket).start();;
		}
		
	}
}

class MyThread extends Thread {

	private Socket socket = null;
	
	public MyThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				System.out.println(socket.getInputStream().read());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
