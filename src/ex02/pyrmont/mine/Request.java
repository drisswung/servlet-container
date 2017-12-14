package ex02.pyrmont.mine;

import java.io.IOException;
import java.io.InputStream;

public class Request extends ServletRequestAdapter {
	
	private InputStream in = null;
	
	private String requestUri = null;
	
	public Request(InputStream in) {
		this.in = in;
	}

	public void parse() {
		
		byte[] bytes = new byte[1024];
		try {
			in.read(bytes);
			String content = new String(bytes);
			int firstSpace = content.indexOf(" ");
			int secondSpace = content.indexOf(" ", firstSpace + 1);
			this.requestUri = content.substring(firstSpace + 1, secondSpace);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public void destory() {
		try {
			if (this.in != null) {
				in.close();
			}
		} catch (IOException e) {}
	}
	
}
