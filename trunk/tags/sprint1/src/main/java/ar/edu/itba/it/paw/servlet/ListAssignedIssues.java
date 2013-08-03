package ar.edu.itba.it.paw.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class ListAssignedIssues extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession();
		int projectId = Integer.valueOf(req.getParameter("id"));

		List<Issue> issues = null;
		Project project = ProjectServiceImpl.getInstance()
				.getProject(projectId);

		if (project != null) {
			req.setAttribute("project", project);
			User user = UserServiceImpl.getInstance().getUser(
					(String) session.getAttribute("name"));
			issues = IssueServiceImpl.getInstance().getAssignedIssues(user,
					project);
		}

		req.setAttribute("issueList", issues);
		req.setAttribute("issueListSize", issues.size());
		req.getRequestDispatcher("assignedIssues.jsp").forward(req, resp);
	}

}
