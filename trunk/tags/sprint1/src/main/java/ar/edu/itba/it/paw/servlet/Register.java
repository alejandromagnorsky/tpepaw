package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.UserServiceImpl;
import ar.edu.itba.it.paw.validators.RegisterFormValidator;

public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final PermissionManager permissionManager = PermissionManager.getInstance();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User source = UserServiceImpl.getInstance().getUser(
				(String) req.getSession().getAttribute("name"));
		if (!permissionManager.canRegister(source)) {
			resp.sendRedirect("projects");
			return;
		}
		req.setAttribute("usernameRegister", "");
		req.setAttribute("passwordRegister", "");
		req.setAttribute("fullnameRegister", "");

		req.setAttribute("registerNameError", "");
		req.setAttribute("registerPasswordError", "");
		req.setAttribute("registerFullnameError", "");

		req.getRequestDispatcher("register.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (RegisterFormValidator.getInstance().validate(req, resp)) {
			req.setAttribute("registerNameError", "");
			req.setAttribute("registerPasswordError", "");
			req.setAttribute("registerFullnameError", "");
			String username = req.getParameter("usernameRegister");
			String password = req.getParameter("passwordRegister");
			String fullname = req.getParameter("fullnameRegister");
			User source = UserServiceImpl.getInstance().getUser(
					(String) req.getSession().getAttribute("name"));
			User target = new User(false, true, username, password, fullname);
			UserServiceImpl.getInstance().register(source, target);
			resp.sendRedirect("projects");
		} else {
			req.getRequestDispatcher("register.jsp").forward(req, resp);
		}

	}

}
