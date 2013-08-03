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
import ar.edu.itba.it.paw.repo.ProjectRepo;

@Component
public class ProjectFilter extends OncePerRequestFilter {

	private ProjectRepo projectRepo;

	@Autowired
	public ProjectFilter(ProjectRepo projectRepo) {
		this.projectRepo = projectRepo;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
			HttpServletResponse resp, FilterChain chain)
			throws ServletException, IOException {

		String uri = ((HttpServletRequest) req).getRequestURI();
		String context = uri.substring(0, uri.indexOf('/', 1) + 1);
		
		HttpSession session = ((HttpServletRequest) req).getSession();
		User source = (User) req.getAttribute("user");
		
		Integer projectId = (Integer) session.getAttribute("projectId");
		Project project = (projectId == null) ? null : projectRepo.get(projectId);
		req.setAttribute("project", project);

		// TODO
		// Hacer tests del model
		
		if (project != null) {
				// Project
			if  ((uri.contains("bin/project/status") && !project.canViewStatus(source))
			|| (uri.contains("bin/project/workReport") && !project.canViewWorkReport(source))
			|| (uri.contains("bin/project/addUser") && !project.canAddUser(source))
			|| (uri.contains("bin/project/removeUser") && !project.canRemoveUser(source))
			|| (uri.contains("bin/project/addVersion") && !project.canAddVersion(source))
			|| (uri.contains("bin/project/deleteVersion") && !project.canDeleteVersion(source))
			|| (uri.contains("bin/project/editVersion") && !project.canEditVersion(source))
			|| (uri.contains("bin/project/versionList") && !project.canViewVersionList(source))
			
				// Issue
			|| (uri.contains("bin/issue/add") && !project.canAddIssue(source))
			|| (uri.contains("bin/issue/edit") && !project.canEditIssue(source))
			|| (uri.contains("bin/issue/assigned") && !project.canViewAssigned(source))
			|| (uri.contains("bin/issue/view") && !project.canViewIssue(source))
			|| (uri.contains("bin/issue/relate") && !project.canRelateIssues(source))
			
				// Filter
			|| (uri.contains("bin/filter/list") && !project.canViewFilterManager(source))
			|| (uri.contains("bin/filter/edit") && !project.canEditFilter(source))
			|| (uri.contains("bin/filter/delete") && !project.canDeleteFilter(source))
			
				// Issue File
			|| (uri.contains("bin/issuefile/list") && !project.canViewIssueFiles(source))
			){
				((HttpServletResponse) resp).sendRedirect(context + "bin/project/view");
				return;
			}					
		} else if (!uri.contains("bin/project/view") 
					&& !uri.contains("bin/project/add") 
					&& !uri.contains("bin/issue/list") 
					&& !uri.contains("bin/user")){
			((HttpServletResponse) resp).sendRedirect(context + "bin/project/view");
			return;
		}
		
		Integer filterId = (Integer) session.getAttribute("filterId");			
		if (filterId != null && filterId != 0)
			req.setAttribute("filter", projectRepo.getFilter(filterId));
			
		chain.doFilter(req, resp);
	}
}
