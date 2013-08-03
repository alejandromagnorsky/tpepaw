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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.model.Version.VersionState;
import ar.edu.itba.it.paw.repo.IssueRepo;
import ar.edu.itba.it.paw.repo.ProjectRepo;
import ar.edu.itba.it.paw.repo.UserRepo;
import ar.edu.itba.it.paw.web.command.ProjectForm;
import ar.edu.itba.it.paw.web.command.ProjectUserForm;
import ar.edu.itba.it.paw.web.command.VersionForm;
import ar.edu.itba.it.paw.web.command.WorkReportForm;
import ar.edu.itba.it.paw.web.formatter.TimeFormatter;
import ar.edu.itba.it.paw.web.validator.AddProjectFormValidator;
import ar.edu.itba.it.paw.web.validator.EditProjectFormValidator;
import ar.edu.itba.it.paw.web.validator.VersionFormValidator;
import ar.edu.itba.it.paw.web.validator.WorkReportFormValidator;

@Controller
public class ProjectController {

	private UserRepo userRepo;
	private IssueRepo issueRepo;
	private ProjectRepo projectRepo;
	private TimeFormatter timeFormatter;
	private IssueController issueController;
	private VersionFormValidator versionFormValidator;
	private AddProjectFormValidator addProjectFormValidator;
	private WorkReportFormValidator workReportFormValidator;
	private EditProjectFormValidator editProjectFormValidator;

	@Autowired
	public ProjectController(UserRepo userRepo,IssueRepo issueRepo,  ProjectRepo projectRepo, 
			TimeFormatter timeFormatter, IssueController issueController,
			VersionFormValidator addVersionFormValidator,
			AddProjectFormValidator addProjectFormValidator,
			
			WorkReportFormValidator workReportFormValidator,
			EditProjectFormValidator editProjectFormValidator) {
		this.userRepo = userRepo;
		this.issueRepo = issueRepo;
		this.projectRepo = projectRepo;
		this.timeFormatter = timeFormatter;
		this.issueController = issueController;
		this.versionFormValidator = addVersionFormValidator;
		this.addProjectFormValidator = addProjectFormValidator;
		this.workReportFormValidator = workReportFormValidator;
		this.editProjectFormValidator = editProjectFormValidator;		
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest req, ProjectForm form, Errors errors) {
		addProjectFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User source = (User) req.getAttribute("user");
			Project project = new Project(form.getCode(), form.getName(),
					form.getDescription(), form.getLeader(), form.getIsPublic());
			projectRepo.add(source, project);
			return view(req);
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("userList", userRepo.getValidUsers());
			return mav;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("projectForm", new ProjectForm());
		mav.addObject("userList", userRepo.getValidUsers());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView edit(HttpServletRequest req, ProjectForm form, Errors errors) {
		editProjectFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			form.update();
			return view(req);
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("originalProjectName", form.getOriginalProject().getName());
			mav.addObject("userList", userRepo.getValidUsers());
			return mav;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest req) {
		Project project = (Project) req.getAttribute("project");
			
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
		mav.addObject("userList", userRepo.getValidUsers());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest req) {
		List<Project> projects = null;
		User user = (User) req.getAttribute("user");

		ModelAndView mav = new ModelAndView("project/view");

		if (user != null) {
			mav.addObject("public", false);
			projects = projectRepo.getViewableList(user);
		} else {
			mav.addObject("public", true);
			projects = projectRepo.getPublic();
		}

		mav.addObject("projectList", projects);
		mav.addObject("projectListSize", projects.size());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView status(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		Project project = (Project) req.getAttribute("project");		

		Project.Status projectStatus = project.getStatus();
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
		User source = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");

		ModelAndView mav = new ModelAndView();
		ProjectUserForm addProjectUserForm = new ProjectUserForm();
		addProjectUserForm.setSource(source);
		addProjectUserForm.setProject(project);

		List<User> users = userRepo.getValidUsers();
		users.removeAll(project.getUsers());
		users.remove(project.getLeader());

		mav.addObject("validUsers", users);
		mav.addObject("project", project);
		mav.addObject("projectUserForm", addProjectUserForm);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView addUser(HttpServletRequest req, ProjectUserForm form,
			Errors errors) {
		if (!errors.hasErrors()) {
			User source = form.getSource();
			Project project = form.getProject();
			User target = form.getTarget();
			project.addUser(source, target);
			return issueController.list(req, project);
		} else 
			return new ModelAndView();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView removeUser(HttpServletRequest req) {
		User source = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");

		ModelAndView mav = new ModelAndView();
		ProjectUserForm removeProjectUserForm = new ProjectUserForm();
		removeProjectUserForm.setSource(source);
		removeProjectUserForm.setProject(project);

		mav.addObject("project", project);
		mav.addObject("projectUserForm", removeProjectUserForm);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView removeUser(HttpServletRequest req, ProjectUserForm form,
			Errors errors) {
		if (!errors.hasErrors()) {
			User source = form.getSource();
			Project project = form.getProject();
			User target = form.getTarget();
			project.removeUser(source, target);
			return issueController.list(req, project);
		} else
			return new ModelAndView();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView workReport(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();

		Project project = (Project) req.getAttribute("project");
		User source = (User) req.getAttribute("user");

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
		Project project = (Project) req.getAttribute("project");

		mav.addObject("workReportForm", form);
		mav.addObject("project", project);

		workReportFormValidator.validate(form, errors);

		if (!errors.hasErrors()) {
			DateTime from = form.getFrom();
			DateTime to = form.getTo();

			TimeFormatter timeFormatter = new TimeFormatter();
			
			SortedMap<User, String> parsedEntries = new TreeMap<User, String>();
			for (Entry<User, Time> e : issueRepo.getWorkReport(from, to,
					project).entrySet())
				parsedEntries.put(e.getKey(),
						timeFormatter.print(e.getValue(), null));

			mav.addObject("userSet", parsedEntries.entrySet());
		}
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView addVersion(HttpServletRequest req, VersionForm form,
			Errors errors) {
		versionFormValidator.validate(form, errors);
		User source = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");
		if (project != null && !errors.hasErrors()) {
			if (project.canAddVersion(source)) {
				Version version = form.build();
				project.addVersion(version);
				projectRepo.persist(version);
			}
			return versionList(req);
		} else
			return new ModelAndView("project/addVersion");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView addVersion(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("versionForm", new VersionForm());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView editVersion(HttpServletRequest req, VersionForm form,
			Errors errors) {
		versionFormValidator.validate(form, errors);
		User source = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");

		if (project != null && !errors.hasErrors()) {
			if (project.canEditVersion(source))
				form.update();
			return versionList(req);
		} else
			return new ModelAndView("project/editVersion");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView editVersion(HttpServletRequest req,
			@RequestParam("id") Version version) {
		VersionForm form = new VersionForm();
		form.setDescription(version.getDescription());
		form.setName(version.getName());
		form.setReleaseDate(version.getReleaseDate());
		form.setState(version.getState());
		form.setOriginal(version);

		ModelAndView mav = new ModelAndView();
		mav.addObject("versionForm", form);
		mav.addObject("version", version);
		mav.addObject("versionStates", VersionState.values());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView versionList(HttpServletRequest req) {
		User source = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");

		ModelAndView mav = new ModelAndView("project/versionList");
		mav.addObject("versions", project.getVersions());
		mav.addObject("nonReleasedVersions", project.getNonReleasedVersions());
		mav.addObject("canAddVersion", project.canAddVersion(source));
		mav.addObject("canDeleteVersion", project.canDeleteVersion(source));
		mav.addObject("canEditVersion", project.canEditVersion(source));
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String deleteVersion(HttpServletRequest req,
			@RequestParam("id") Version version) {
		User source = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");

		project.deleteVersion(source, version);

		return "redirect:../project/versionList";
	}

}
