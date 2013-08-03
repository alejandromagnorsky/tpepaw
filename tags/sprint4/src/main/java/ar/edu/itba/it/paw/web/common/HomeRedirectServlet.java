package ar.edu.itba.it.paw.web.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class HomeRedirectServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String target = this.getInitParameter("homeURL");
		if (target == null)
			throw new UnavailableException("No home has been defined. Set property");
		
		resp.sendRedirect(target);
	}
}
