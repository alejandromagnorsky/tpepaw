package ar.edu.itba.it.paw.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.service.IssueService;
import ar.edu.itba.it.paw.service.ProjectService;

@Component
public class ProjectFilter extends OncePerRequestFilter {

	private ProjectService projectService;
	private IssueService issueService;

	@Autowired
	public ProjectFilter(ProjectService projectService, IssueService issueService) {
		this.projectService = projectService;
		this.issueService = issueService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			HttpServletResponse resp, FilterChain chain)
			throws ServletException, IOException {

		HttpSession session = ((HttpServletRequest) req).getSession();
		User source = (User) session.getAttribute("user");
		Project project = (Project) session.getAttribute("project");
		
		String uri = ((HttpServletRequest) req).getRequestURI();
		String context = uri.substring(0, uri.indexOf('/', 1) + 1);
		
		
		if (project != null) {
				// Project
			if  ((uri.contains("bin/project/status") && !projectService.canViewStatus(source, project))
			|| (uri.contains("bin/project/workReport") && !projectService.canViewWorkReport(source, project))
			|| (uri.contains("bin/project/addUser") && !projectService.canAddUserToProject(source, project))
			|| (uri.contains("bin/project/removeUser") && !projectService.canRemoveUserFromProject(source, project))
			
				// Issue
			|| (uri.contains("bin/issue/add") && !issueService.canAddIssue(source, project))
			|| (uri.contains("bin/issue/edit") && !issueService.canEditIssue(source, project))
			|| (uri.contains("bin/issue/hottest") && !issueService.canViewHottestIssues(source, project))
			|| (uri.contains("bin/issue/assigned") && !issueService.canViewAssigned(source, project))
			|| (uri.contains("bin/issue/view") && !issueService.canViewIssue(source, project))
			){
				((HttpServletResponse) resp).sendRedirect(context + "bin/project/view");
				return;
			}					
		} else if (!uri.contains("bin/project/view")
				&& !uri.contains("bin/project/add") 
				&& !uri.contains("bin/issue/list") 
				&& !uri.contains("bin/user")
				&& !uri.contains("css")
				&& !uri.contains("js/utils.js")
				&& !uri.contains("resources")){
			((HttpServletResponse) resp).sendRedirect(context + "bin/project/view");
			return;
		}
		
		chain.doFilter(req, resp);
	}
}
