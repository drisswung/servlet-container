package ex02.pyrmont.mine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletResponse;

public class StaticResourceHandler {

	private static final String STATIC_RESOURCE_ROOT_PATH = System.getProperty("user.dir") + File.separator; 
	
	public static void handler(Request request, Response response) {
		
		String filePath = STATIC_RESOURCE_ROOT_PATH + request.getRequestUri().substring(1);
		File file = new File(filePath);
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			byte[] bytes = new byte[1024];
			int readSize = fileInputStream.read(bytes);
			response.getOutputStream().write(getBaseInfo(200).getBytes());
			while (readSize != -1) {
				response.getOutputStream().write(bytes);
				readSize = fileInputStream.read(bytes);
			}
			response.getOutputStream().flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			response_404(response);
		} catch (IOException e) {
			e.printStackTrace();
			response_500(response);
		} 
	}

	public static void response_200(ServletResponse response, String content) {
		String fileNotFound = getBaseInfo(200) + content;
		try {
			response.getOutputStream().write(fileNotFound.getBytes());
			response.getOutputStream().flush();
		} catch (IOException e1) {}
	}
	
	public static void response_404(ServletResponse response) {
		String fileNotFound = getBaseInfo(404) + "File not found!";
		try {
			response.getOutputStream().write(fileNotFound.getBytes());
			response.getOutputStream().flush();
		} catch (IOException e1) {}
	}
	
	public static void response_500(ServletResponse response) {
		String fileNotFound = getBaseInfo(500) + "wait a moment¡£";
		try {
			response.getOutputStream().write(fileNotFound.getBytes());
			response.getOutputStream().flush();
		} catch (IOException e1) {}
	}
	
	private static String getBaseInfo(int status) {
		StringBuilder baseInfo = new StringBuilder();
		baseInfo.append("HTTP/1.1 ").append(status).append(" \r\n")
		   	.append("Content-Type: text/html\r\n")
			.append("Server: localhost\r\n")
			.append("\r\n");
		return baseInfo.toString();
	}
}
