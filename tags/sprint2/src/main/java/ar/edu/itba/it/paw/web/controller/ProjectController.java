package ar.edu.itba.it.paw.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectStatus;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.service.ProjectService;
import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.web.command.ProjectForm;
import ar.edu.itba.it.paw.web.command.ProjectUserForm;
import ar.edu.itba.it.paw.web.command.WorkReportForm;
import ar.edu.itba.it.paw.web.formatter.TimeFormatter;
import ar.edu.itba.it.paw.web.validator.AddProjectFormValidator;
import ar.edu.itba.it.paw.web.validator.EditProjectFormValidator;
import ar.edu.itba.it.paw.web.validator.WorkReportFormValidator;

@Controller
public class ProjectController {

	private UserService userService;
	private TimeFormatter timeFormatter;
	private ProjectService projectService;
	private AddProjectFormValidator addProjectFormValidator;
	private WorkReportFormValidator workReportFormValidator;
	private EditProjectFormValidator editProjectFormValidator;

	@Autowired
	public ProjectController(UserService userService,
			TimeFormatter timeFormatter, ProjectService projectService,
			AddProjectFormValidator addProjectFormValidator,
			WorkReportFormValidator workReportFormValidator,
			EditProjectFormValidator editProjectFormValidator) {
		this.userService = userService;
		this.timeFormatter = timeFormatter;
		this.projectService = projectService;
		this.addProjectFormValidator = addProjectFormValidator;
		this.workReportFormValidator = workReportFormValidator;
		this.editProjectFormValidator = editProjectFormValidator;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String add(HttpServletRequest req, ProjectForm form, Errors errors) {
		addProjectFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User source = (User) req.getSession().getAttribute("user");
			projectService.addProject(source, form.build(null));
			return "redirect:../project/view";
		} else {
			req.setAttribute("userList", userService.getValidUsers());
			return "project/add";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req) {
		User source = (User) req.getSession().getAttribute("user");
		if (!projectService.canAddProject(source))
			return new ModelAndView(new RedirectView("../project/view"));

		ModelAndView mav = new ModelAndView();
		mav.addObject("projectForm", new ProjectForm());
		mav.addObject("userList", userService.getValidUsers());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String edit(HttpServletRequest req, ProjectForm form, Errors errors) {
		editProjectFormValidator.validate(form, errors);
		User source = (User) req.getSession().getAttribute("user");
		if (!errors.hasErrors()) {
			form.update();
			projectService.updateProject(source, form.getOriginalProject());
			return "redirect:../project/view";
		} else {
			req.setAttribute("originalProjectName", form.getOriginalProject().getName());
			req.setAttribute("userList", userService.getValidUsers());
			return "project/edit";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest req) {
		Project project = (Project) req.getSession().getAttribute("project");
		User source = (User) req.getSession().getAttribute("user");
		if (!projectService.canEditProject(source))
			return new ModelAndView(new RedirectView("../issue/list?code="
					+ project.getCode()));

		ModelAndView mav = new ModelAndView();

		ProjectForm projectForm = new ProjectForm();
		projectForm.setOriginalProject(project);
		projectForm.setName(project.getName());
		projectForm.setDescription(project.getDescription());
		projectForm.setCode(project.getCode());
		projectForm.setLeader(project.getLeader());
		projectForm.setIsPublic(project.getIsPublic());
		mav.addObject("originalProjectName", project.getName());
		mav.addObject("projectForm", projectForm);
		mav.addObject("userList", userService.getValidUsers());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest req) {
		List<Project> projects = null;
		User user = (User) req.getSession().getAttribute("user");

		ModelAndView mav = new ModelAndView();

		if (user != null) {
			mav.addObject("public", false);
			projects = projectService.getViewableList(user);
		} else {
			mav.addObject("public", true);
			projects = projectService.getPublicList();
		}

		mav.addObject("projectList", projects);
		mav.addObject("projectListSize", projects.size());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView status(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();

		Project project = (Project) req.getSession().getAttribute("project");
		User source = (User) req.getSession().getAttribute("user");
		if (!projectService.canViewStatus(source, project))
			return new ModelAndView(new RedirectView("../project/view"));

		ProjectStatus projectStatus = projectService.getProjectStatus(project);
		List<String> rows = new ArrayList<String>();
		for(Issue.State state: Issue.State.values()){
			StringBuilder row = new StringBuilder();
			row.append("<tr>");
			row.append("<td><span>"+state.getCaption()+"</span></td>");
			row.append("<td><span>"+projectStatus.getQuant(state)+"</span></td>");
			if (timeFormatter.print(projectStatus.getTime(state), null).isEmpty())
				row.append("<td><span>-</span></td>");
			else
				row.append("<td><span>"+timeFormatter.print(projectStatus.getTime(state), null)+"</span></td>");
			row.append("</tr>");
			rows.add(row.toString());		
		}
		
		mav.addObject("project", project);
		mav.addObject("rows", rows);
		
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView addUser(HttpServletRequest req) {
		User source = (User) req.getSession().getAttribute("user");
		Project project = (Project) req.getSession().getAttribute("project");

		if (project == null)
			return new ModelAndView(new RedirectView("../project/view"));

		if (!projectService.canAddUserToProject(source, project))
			return new ModelAndView(new RedirectView("../project/view"));

		ModelAndView mav = new ModelAndView();
		ProjectUserForm addProjectUserForm = new ProjectUserForm();
		addProjectUserForm.setSource(source);
		addProjectUserForm.setProject(project);

		List<User> users = userService.getValidUsers();
		users.removeAll(project.getUsers());
		users.remove(project.getLeader());

		mav.addObject("validUsers", users);
		mav.addObject("project", project);
		mav.addObject("projectUserForm", addProjectUserForm);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addUser(HttpServletRequest req, ProjectUserForm form,
			Errors errors) {

		if (!errors.hasErrors()) {
			User source = form.getSource();
			Project project = form.getProject();
			User target = form.getTarget();
			projectService.addProjectUser(source, project, target);
			return "redirect:../issue/list?code=" + form.getProject().getCode();
		} else {
			return "project/addUser";
		}

	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView removeUser(HttpServletRequest req) {
		User source = (User) req.getSession().getAttribute("user");
		Project project = (Project) req.getSession().getAttribute("project");

		if (project == null)
			return new ModelAndView(new RedirectView("../project/view"));

		if (!projectService.canRemoveUserFromProject(source, project))
			return new ModelAndView(new RedirectView("../project/view"));

		ModelAndView mav = new ModelAndView();
		ProjectUserForm removeProjectUserForm = new ProjectUserForm();
		removeProjectUserForm.setSource(source);
		removeProjectUserForm.setProject(project);

		mav.addObject("project", project);
		mav.addObject("projectUserForm", removeProjectUserForm);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String removeUser(HttpServletRequest req, ProjectUserForm form,
			Errors errors) {

		if (!errors.hasErrors()) {
			User source = form.getSource();
			Project project = form.getProject();
			User target = form.getTarget();
			projectService.removeProjectUser(source, project, target);
			return "redirect:../issue/list?code=" + form.getProject().getCode();
		} else
			return "project/removeUser";
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView workReport(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();

		Project project = (Project) req.getSession().getAttribute("project");
		User source = (User) req.getSession().getAttribute("user");

		if (!projectService.canViewWorkReport(source, project))
			return new ModelAndView(new RedirectView("../project/view"));

		WorkReportForm workReportForm = new WorkReportForm();
		workReportForm.setSource(source);
		workReportForm.setProject(project);

		mav.addObject("workReportForm", workReportForm);
		mav.addObject("project", project);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView workReport(HttpServletRequest req, WorkReportForm form,
			Errors errors) {

		ModelAndView mav = new ModelAndView();
		Project project = (Project) req.getSession().getAttribute("project");

		mav.addObject("workReportForm", form);
		mav.addObject("project", project);

		workReportFormValidator.validate(form, errors);

		if (!errors.hasErrors()) {
			DateTime from = form.getFrom();
			DateTime to = form.getTo();

			TimeFormatter timeFormatter = new TimeFormatter();
			
			SortedMap<User, String> parsedEntries = new TreeMap<User, String>();
			for (Entry<User, Time> e : projectService.getWorkReport(
					project, from, to).entrySet())
				parsedEntries.put(e.getKey(), timeFormatter.print(e.getValue(), null));

			mav.addObject("userSet", parsedEntries.entrySet());
		}
		return mav;
	}

}
