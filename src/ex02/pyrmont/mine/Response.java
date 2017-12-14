package ex02.pyrmont.mine;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class Response extends ServletResponseAdapter {
	
	private OutputStream out = null;
	
	public Response(OutputStream out) {
		this.out = out;
	}

	public void destory() {
		if (this.out != null) {
			try {
				this.out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				out.write(b);
			}
		};
	}

	
}
