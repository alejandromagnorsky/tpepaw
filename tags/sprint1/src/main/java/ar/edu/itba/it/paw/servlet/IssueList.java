package ar.edu.itba.it.paw.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ar.edu.itba.it.paw.Issue;
import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class IssueList extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final PermissionManager permissionManager = PermissionManager.getInstance();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int id = Integer.valueOf(req.getParameter("id"));

		Project project = ProjectServiceImpl.getInstance().getProject(id);
		List<Issue> issues = null;
		if (project != null) {
			req.setAttribute("project", project);
			issues = IssueServiceImpl.getInstance().getIssues(project);
			HttpSession session = ((HttpServletRequest) req).getSession();

			if (session.getAttribute("logged") != null) {
				String name = (String) session.getAttribute("name");
				User user = UserServiceImpl.getInstance().getUser(name);
				
				req.setAttribute("canViewProject",
						permissionManager.canViewProject(user, project));
			}

		}
		req.setAttribute("issueList", issues);
		req.setAttribute("issueListSize", issues.size());
		req.getRequestDispatcher("project.jsp").forward(req, resp);
	}
}
