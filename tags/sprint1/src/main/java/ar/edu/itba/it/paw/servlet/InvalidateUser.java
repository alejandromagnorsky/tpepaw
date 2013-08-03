package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.UserServiceImpl;
import ar.edu.itba.it.paw.validators.InvalidateUserFormValidator;

public class InvalidateUser extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final PermissionManager permissionManager = PermissionManager.getInstance();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User source = UserServiceImpl.getInstance().getUser(
				(String) req.getSession().getAttribute("name"));
		if (!permissionManager.canInvalidateUser(source)) {
			resp.sendRedirect("projects");
			return;
		}
		req.setAttribute("usernameInvalidate", "");
		req.setAttribute("invalidateNameError", "");

		req.getRequestDispatcher("invalidateUser.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (InvalidateUserFormValidator.getInstance().validate(req, resp)) {
			req.setAttribute("invalidateNameError", "");
			User source = UserServiceImpl.getInstance().getUser((String)req.getSession().getAttribute("name"));
			User target = UserServiceImpl.getInstance().getUser(req.getParameter("usernameInvalidate"));
			UserServiceImpl.getInstance().invalidateUser(source, target);
			resp.sendRedirect("projects");
		} else {
			req.getRequestDispatcher("invalidateUser.jsp").forward(req, resp);
		}

	}
}
