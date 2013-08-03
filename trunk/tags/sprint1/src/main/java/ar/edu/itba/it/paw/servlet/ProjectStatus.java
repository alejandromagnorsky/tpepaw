package ar.edu.itba.it.paw.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.edu.itba.it.paw.PermissionManager;
import ar.edu.itba.it.paw.Project;
import ar.edu.itba.it.paw.User;
import ar.edu.itba.it.paw.service.IssueService;
import ar.edu.itba.it.paw.service.IssueServiceImpl;
import ar.edu.itba.it.paw.service.ProjectService;
import ar.edu.itba.it.paw.service.ProjectServiceImpl;
import ar.edu.itba.it.paw.service.UserServiceImpl;

public class ProjectStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final PermissionManager permissionManager = PermissionManager.getInstance();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		int id = Integer.valueOf(req.getParameter("id"));
		ProjectService projectService = ProjectServiceImpl.getInstance();
		Project project = projectService.getProject(id);
		
		User source = UserServiceImpl.getInstance().getUser(
				(String) req.getSession().getAttribute("name"));
		
		if (!permissionManager.canViewProject(source, project)) {
			resp.sendRedirect("projects");
			return;
		}
		
		IssueService issueService = IssueServiceImpl.getInstance();
		int stateQuant[] = issueService.getQuantPerState(project);
		float stateHours[] = issueService.getHoursPerState(project);

		req.setAttribute("project", project);
		req.setAttribute("stateQuant", stateQuant);
		req.setAttribute("stateHours", stateHours);

		req.getRequestDispatcher("projectStatus.jsp").forward(req, resp);
	}
}
