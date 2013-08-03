package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("projects").forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String referer = req.getHeader("Referer");
		UserService userService = UserServiceImpl.getInstance();
		
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		if(!userService.verifyUser(name, password)){
			req.setAttribute("loginStatus", false);
			req.getRequestDispatcher("projects").forward(req, resp);
		} else {
			HttpSession session = req.getSession();
			session.setAttribute("logged", true);
			session.setAttribute("name", name);
			resp.sendRedirect(referer);
		}

	}
}
