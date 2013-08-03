package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (RuntimeException e) {
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpServletRequest req = (HttpServletRequest) request;
			req.setAttribute("errormsg", "500 - Error interno del servidor.");
			
			// Para la version final sacar el stacktrace
			e.printStackTrace();
			
			req.getRequestDispatcher("/error.jsp").forward(req, resp);
		}
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
