package ex02.pyrmont.mine;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

public class ServletResourceHandler {
	
	private static final String SERVLET_CLASS_BASE_PATH = System.getProperty("user.dir") + File.separator + "bin" + File.separator; 
	
	private static Map<String, Object> servlets = new HashMap<String, Object>();
	
	private static URLClassLoader classLoader = null;
	
	static {
		
		URL[] urls = new URL[1];
	    URLStreamHandler streamHandler = null;
		File classPath = new File(SERVLET_CLASS_BASE_PATH);
		String repository;
		try {
			repository = (new URL("file", null, classPath.getCanonicalPath())).toString();
			urls[0] = new URL(null, repository, streamHandler);
			classLoader = new URLClassLoader(urls);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static void handler(Request request, Response response) {
		
		try {
			String className = request.getRequestUri().substring(1);
			if (servlets.containsKey(className)) {
				Servlet servlet = (Servlet) servlets.get(className);
				servlet.service(request, response);
			} else {
				Class<?> clazz = classLoader.loadClass(className);
				Servlet servlet = (Servlet) clazz.newInstance();
				servlets.put(className, servlet);
				servlet.service(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			StaticResourceHandler.response_500(response);
		} 
		
	}

}
