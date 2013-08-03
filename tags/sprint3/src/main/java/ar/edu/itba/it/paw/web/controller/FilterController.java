package ar.edu.itba.it.paw.web.controller;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.repo.ProjectRepo;
import ar.edu.itba.it.paw.repo.UserRepo;
import ar.edu.itba.it.paw.web.command.FilterForm;
import ar.edu.itba.it.paw.web.command.FilterForm.Action;
import ar.edu.itba.it.paw.web.validator.AddFilterFormValidator;
import ar.edu.itba.it.paw.web.validator.EditFilterFormValidator;

@Controller
public class FilterController {
	
	private UserRepo userRepo;
	private ProjectRepo projectRepo;
	private AddFilterFormValidator addFilterFormValidator;
	private EditFilterFormValidator editFilterFormValidator;
	
	@Autowired
	public FilterController(	UserRepo userRepo, ProjectRepo projectRepo,
								AddFilterFormValidator addFilterFormValidator,
								EditFilterFormValidator editFilterFormValidator) {
		this.userRepo = userRepo;
		this.projectRepo = projectRepo;
		this.addFilterFormValidator = addFilterFormValidator;
		this.editFilterFormValidator = editFilterFormValidator;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req) {
		User user = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");
		
		ModelAndView mav = new ModelAndView();
		FilterForm filterForm = new FilterForm();
		filterForm.setUser(user);
		filterForm.setProject(project);
		if (project.canAddFilter(user))
			filterForm.setAction(Action.SaveAndApply);
		else
			filterForm.setAction(Action.Apply);
		
		mav.addObject("canAddFilter", project.canAddFilter(user));
		mav.addObject("filterForm", filterForm);
		mav.addObject("projectName", project.getName());
		mav.addObject("stateList", State.values());
		mav.addObject("actionList", Action.values());
		mav.addObject("resolutionList", Resolution.values());
		mav.addObject("userList", userRepo.getValidUsers());
		mav.addObject("typeList", Type.values());
		mav.addObject("versions", project.getVersions());
		
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest req, FilterForm form, Errors errors) {
		HttpSession session = req.getSession();
		User user = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");
		
		addFilterFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			Filter filter = form.build();
			
			if (form.getAction().equals(Action.Apply)){
				session.setAttribute("filterId", 0);				
				session.setAttribute("tempFilter", form.build());
			} else if (form.getAction().equals(Action.Save)){
				project.addFilter(user, filter);
				projectRepo.persist(filter);
				
				SortedSet<Filter> filters = project.getFilters(user);
				
				req.setAttribute("canAddFilter", project.canAddFilter(user));
				req.setAttribute("canEditFilter", project.canEditFilter(user));
				req.setAttribute("canDeleteFilter", project.canDeleteFilter(user));
				req.setAttribute("filtersQuant", filters.size());
				req.setAttribute("filters", filters);
				return list(req);
			} else {
				project.addFilter(user, filter);
				projectRepo.persist(filter);
				session.setAttribute("filterId", filter.getId());
			}
		
			return new ModelAndView(new RedirectView("../issue/list?code=" + project.getCode()));
			
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("canAddFilter", project.canAddFilter(user));
			mav.addObject("projectName", project.getName());
			mav.addObject("stateList", State.values());
			mav.addObject("actionList", Action.values());
			mav.addObject("resolutionList", Resolution.values());
			mav.addObject("userList", userRepo.getValidUsers());
			mav.addObject("typeList", Type.values());
			mav.addObject("versions", project.getVersions());
			return mav;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView apply(HttpServletRequest req, @RequestParam("id") Filter filter) {
		HttpSession session = req.getSession();
		session.setAttribute("filterId", filter.getId());
		return new ModelAndView(new RedirectView("../issue/list?code=" + filter.getProject().getCode()));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView delete(HttpServletRequest req, @RequestParam("id") Filter filter) {
		HttpSession session = req.getSession();
		User user = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");
		
		project.deleteFilter(user, filter);
		
		Integer filterId = (Integer)session.getAttribute("filterId");
		if (filterId != null && filterId.equals(filter.getId()))
			session.setAttribute("filterId", null);
		
		return new ModelAndView(new RedirectView("../filter/list"));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest req, @RequestParam("id") Filter filter) {
		User user = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");
		
		ModelAndView mav = new ModelAndView();
		FilterForm filterForm = new FilterForm();
		filterForm.setOriginalFilter(filter);
		filterForm.setName(filter.getName());
		filterForm.setUser(user);
		filterForm.setProject(project);
		filterForm.setIssueCode(filter.getIssueCode());
		filterForm.setIssueTitle(filter.getIssueTitle());
		filterForm.setIssueDescription(filter.getIssueDescription());
		filterForm.setIssueReportedUser(filter.getIssueReportedUser());
		filterForm.setIssueAssignedUser(filter.getIssueAssignedUser());
		filterForm.setIssueType(filter.getIssueType());
		filterForm.setIssueState(filter.getIssueState());
		filterForm.setIssueResolution(filter.getIssueResolution());
		filterForm.setAffectedVersion(filter.getAffectedVersion());
		filterForm.setFixedVersion(filter.getFixedVersion());
		filterForm.setAction(Action.Save);
		
		mav.addObject("originalFilterName", filter.getName());
		mav.addObject("filterForm", filterForm);
		mav.addObject("projectName", project.getName());
		mav.addObject("stateList", State.values());
		mav.addObject("resolutionList", Resolution.values());
		mav.addObject("userList", userRepo.getValidUsers());
		mav.addObject("typeList", Type.values());
		mav.addObject("versions", project.getVersions());
		
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView edit(HttpServletRequest req, FilterForm form, Errors errors) {
		Project project = (Project) req.getAttribute("project");
		
		editFilterFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			form.update(form.getOriginalFilter());
			return list(req);
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("originalFilterName", form.getOriginalFilter().getName());
			mav.addObject("projectName", project.getName());
			mav.addObject("stateList", State.values());
			mav.addObject("actionList", Action.values());
			mav.addObject("resolutionList", Resolution.values());
			mav.addObject("userList", userRepo.getValidUsers());
			mav.addObject("typeList", Type.values());
			mav.addObject("versions", project.getVersions());
			return mav;
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req) {
		User user = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");		
	
		SortedSet<Filter> filters = project.getFilters(user);
		
		ModelAndView mav = new ModelAndView("filter/list");
		mav.addObject("canAddFilter", project.canAddFilter(user));
		mav.addObject("canEditFilter", project.canEditFilter(user));
		mav.addObject("canDeleteFilter", project.canDeleteFilter(user));
		mav.addObject("filtersQuant", filters.size());
		mav.addObject("filters", filters);
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String remove(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Project project = (Project) req.getAttribute("project");

		session.setAttribute("filterId", null);
		session.setAttribute("tempFilter", null);
		return "redirect:../issue/list?code=" + project.getCode();
	}
}