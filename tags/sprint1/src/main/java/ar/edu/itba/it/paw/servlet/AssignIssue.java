package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class AssignIssue extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int issueId = Integer.valueOf(req.getParameter("id"));
		Issue issue = IssueServiceImpl.getInstance().getIssue(issueId);

		HttpSession session = req.getSession();
		String username = (String) session.getAttribute("name");
		if (username != null) {
			User user = UserServiceImpl.getInstance().getUser(username);
			IssueServiceImpl.getInstance().assignUserToIssue(user, issue);
		}

		resp.sendRedirect("issue?id=" + issueId);
	}

	// POST type servlet, get should do nothing!
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("projects");
	}
}
