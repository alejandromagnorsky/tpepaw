package ar.edu.itba.it.paw.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ar.edu.itba.it.paw.model.Access;
import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.service.IssueService;
import ar.edu.itba.it.paw.service.ProjectService;
import ar.edu.itba.it.paw.service.UserService;
import ar.edu.itba.it.paw.service.IssueServiceImpl.AccessPerIssue;
import ar.edu.itba.it.paw.web.command.CommentForm;
import ar.edu.itba.it.paw.web.command.IssueForm;
import ar.edu.itba.it.paw.web.formatter.TimeFormatter;
import ar.edu.itba.it.paw.web.validator.CommentFormValidator;
import ar.edu.itba.it.paw.web.validator.IssueFormValidator;

@Controller
public class IssueController {

	private static final State DEFAULT_STATE = State.Open;
	private static final Priority DEFAULT_PRIORITY = Priority.Normal;

	private UserService userService;
	private IssueService issueService;
	private TimeFormatter timeFormatter;
	private ProjectService projectService;
	private IssueFormValidator issueFormValidator;
	private CommentFormValidator commentFormValidator;

	@Autowired
	public IssueController(UserService userService, IssueService issueService,
			TimeFormatter timeFormatter, ProjectService projectService,
			IssueFormValidator issueFormValidator,
			CommentFormValidator commentFormValidator) {
		this.userService = userService;
		this.issueService = issueService;
		this.timeFormatter = timeFormatter;
		this.projectService = projectService;
		this.issueFormValidator = issueFormValidator;
		this.commentFormValidator = commentFormValidator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req,
			@RequestParam("code") Project project) {
		ModelAndView mav = new ModelAndView();

		HttpSession session = ((HttpServletRequest) req).getSession();
		session.setAttribute("project", project);

		User user = (User) session.getAttribute("user");

		if (!projectService.canViewProject(user, project))
			return new ModelAndView(new RedirectView("../project/view"));

		List<User> toAdd = userService.getValidUsers();
		toAdd.removeAll(project.getUsers());
		toAdd.remove(project.getLeader());

		mav.addObject("canAddIssue", issueService.canAddIssue(user, project));
		mav.addObject("canViewWorkReport",
				projectService.canViewWorkReport(user, project));
		mav.addObject("canViewStatus",
				projectService.canViewStatus(user, project));
		mav.addObject(
				"canAddUserToProject",
				toAdd.size() > 0
						&& projectService.canAddUserToProject(user, project));
		mav.addObject("canRemoveUserToProject", project.getUsers().size() > 0
				&& projectService.canRemoveUserFromProject(user, project));

		List<Issue> issueList = issueService.getIssues(project);
		List<Issue> filteredIssues = new ArrayList<Issue>();
		Filter filter = (Filter) session.getAttribute("filter");
		if (filter != null
				&& filter.getProject().equals(project)
				&& ((user == null && filter.getUser() == null) || (user != null
						&& filter.getUser() != null && filter.getUser().equals(
						user)))) {
			for (Issue i : issueList)
				if (issueService.filter(filter, i))
					filteredIssues.add(i);
			mav.addObject("hasFilter", true);
		} else {
			filteredIssues = issueList;
			session.setAttribute("filter", null);
			mav.addObject("hasFilter", false);
		}

		Map<Issue, String> issues = new HashMap<Issue, String>();
		for (Issue issue : filteredIssues)
			issues.put(issue,
					timeFormatter.print(issue.getEstimatedTime(), null));

		mav.addObject("project", project);
		mav.addObject("issues", issues);
		mav.addObject("issuesQuant", issues.size());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String add(HttpServletRequest req, IssueForm form, Errors errors) {
		Project project = (Project) req.getSession().getAttribute("project");
		User source = (User) req.getSession().getAttribute("user");

		issueFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			issueService.updateIssue(source,
					form.build(DEFAULT_STATE, new DateTime(), source));
			return "redirect:../issue/list?code=" + project.getCode();
		} else {
			req.setAttribute("projectName", project.getName());
			req.setAttribute("priorityList", Issue.Priority.values());
			List<User> userList = new ArrayList<User>(project.getUsers());
			userList.add(project.getLeader());
			req.setAttribute("userList", userList);
			return "issue/add";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req) {
		Project project = (Project) req.getSession().getAttribute("project");

		IssueForm issueForm = new IssueForm();
		issueForm.setProject(project);
		issueForm.setPriority(DEFAULT_PRIORITY);
		ModelAndView mav = new ModelAndView();
		mav.addObject("issueForm", issueForm);
		mav.addObject("projectName", project.getName());
		mav.addObject("priorityList", Issue.Priority.values());
		List<User> userList = new ArrayList<User>(project.getUsers());
		userList.add(project.getLeader());
		req.setAttribute("userList", userList);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String edit(HttpServletRequest req, IssueForm form, Errors errors) {
		Project project = (Project) req.getSession().getAttribute("project");
		User source = (User) req.getSession().getAttribute("user");

		issueFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			form.update();
			issueService.updateIssue(source, form.getOriginalIssue());
			return "redirect:../issue/list?code=" + project.getCode();
		} else {
			req.setAttribute("projectName", project.getName());
			req.setAttribute("originalIssueName", form.getOriginalIssue()
					.getTitle());
			req.setAttribute("issueCode", form.getOriginalIssue().getCode());
			req.setAttribute("reportedUserName", form.getOriginalIssue()
					.getReportedUser().getName());
			req.setAttribute("priorityList", Issue.Priority.values());
			List<User> userList = new ArrayList<User>(project.getUsers());
			userList.add(project.getLeader());
			req.setAttribute("userList", userList);
			return "issue/edit";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		Project project = (Project) req.getSession().getAttribute("project");

		IssueForm issueForm = new IssueForm();
		issueForm.setOriginalIssue(issue);
		issueForm.setTitle(issue.getTitle());
		issueForm.setDescription(issue.getDescription());
		issueForm.setAssignedUser(issue.getAssignedUser());
		issueForm.setEstimatedTime(issue.getEstimatedTime());
		issueForm.setPriority(issue.getPriority());
		issueForm.setProject(project);

		ModelAndView mav = new ModelAndView();
		mav.addObject("projectName", project.getName());
		mav.addObject("issueCode", issue.getCode());
		mav.addObject("reportedUserName", issue.getReportedUser().getName());
		mav.addObject("originalIssueName", issue.getTitle());
		mav.addObject("priorityList", Issue.Priority.values());
		mav.addObject("issueForm", issueForm);
		List<User> userList = new ArrayList<User>(project.getUsers());
		userList.add(project.getLeader());
		req.setAttribute("userList", userList);
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView assigned(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Project project = (Project) session.getAttribute("project");
		User source = (User) session.getAttribute("user");

		Map<Issue, String> issues = new HashMap<Issue, String>();
		for (Issue issue : issueService.getAssignedIssues(source, project))
			issues.put(issue,
					timeFormatter.print(issue.getEstimatedTime(), null));

		ModelAndView mav = new ModelAndView();
		mav.addObject("project", project);
		mav.addObject("issues", issues);
		mav.addObject("issuesQuant", issues.size());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView view(HttpServletRequest req, CommentForm form,
			Errors errors) {
		HttpSession session = req.getSession();
		Project project = (Project) session.getAttribute("project");
		User user = (User) session.getAttribute("user");
		Issue issue = form.getIssue();

		if (!issueService.canAddComment(user, issue))
			return new ModelAndView(new RedirectView("../issue/view?code="
					+ issue.getCode()));

		commentFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			issueService.updateComment(user, form.build(user));
			return new ModelAndView(new RedirectView("../issue/view?code="
					+ issue.getCode()));
		} else {
			return getMavView(issue, user, project);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		Project project = (Project) session.getAttribute("project");

		issueService.addAccess(user, new Access(new LocalDate(), issue));

		ModelAndView mav = getMavView(issue, user, project);
		CommentForm commentForm = new CommentForm();
		commentForm.setIssue(issue);
		mav.addObject("commentForm", commentForm);
		return mav;
	}

	private ModelAndView getMavView(Issue issue, User user, Project project) {
		ModelAndView mav = new ModelAndView();

		if (issue.getResolution() != null)
			mav.addObject("hasResolution", true);

		if (user != null) {
			mav.addObject("canAddComment",
					issueService.canAddComment(user, issue));
			mav.addObject("canEditIssue",
					issueService.canEditIssue(user, project));
			mav.addObject("canCloseIssue",
					issueService.canMarkIssue(user, issue, State.Closed));
			mav.addObject("canMarkIssueAsOpen",
					issueService.canMarkIssue(user, issue, State.Open));
			mav.addObject("canMarkIssueAsOngoing",
					issueService.canMarkIssue(user, issue, State.Ongoing));
			mav.addObject("canResolveIssue",
					issueService.canMarkIssue(user, issue, State.Completed));
			mav.addObject("canAssignIssue",
					issueService.canAssignIssue(user, issue));
			mav.addObject("resolutionStates", Issue.Resolution.values());
		}

		SortedSet<Comment> comments = issueService.getComments(issue);

		mav.addObject("commentList", comments);
		mav.addObject("commentListSize", comments.size());
		mav.addObject("progressPercentage",
				issueService.getProgressPercentage(issue));
		mav.addObject("issue", issue);
		mav.addObject("estimatedTime",
				timeFormatter.print(issue.getEstimatedTime(), null));
		mav.addObject("project", project);

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView mark(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		HttpSession session = req.getSession();
		User source = (User) session.getAttribute("user");

		String op = req.getParameter("operation");
		String resolution = req.getParameter("resolution");

		// This ifs are merely for presentation, to prevent the Illegal argument
		// from appearing in case of double POST
		if (op != null && issue != null && source != null) {
			if (op.equals("open")
					&& issueService.canMarkIssue(source, issue, State.Open)) {
				issueService.markIssueAs(source, issue, State.Open);
			} else if (op.equals("ongoing")
					&& issueService.canMarkIssue(source, issue, State.Ongoing)) {
				issueService.markIssueAs(source, issue, State.Ongoing);
			} else if (op.equals("close")
					&& issueService.canMarkIssue(source, issue, State.Closed)) {
				issueService.markIssueAs(source, issue, State.Closed);
			} else if (op.equals("resolve")
					&& resolution != null
					&& issueService
							.canMarkIssue(source, issue, State.Completed)) {
				Resolution r = Resolution.valueOf(resolution);
				issueService.setResolutionAs(source, issue, r);
			}
		}

		return new ModelAndView(new RedirectView("../issue/view?code="
				+ issue.getCode()));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView assign(HttpServletRequest req,
			@RequestParam("code") Issue issue) {

		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");

		// This if is merely for presentation, to prevent the Illegal argument
		// from appearing in case of double POST
		if (issueService.canAssignIssue(user, issue))
			issueService.assignUserToIssue(user, issue);

		return new ModelAndView(new RedirectView("../issue/view?code="
				+ issue.getCode()));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView hottest(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Project project = (Project) session.getAttribute("project");

		List<AccessPerIssue> accessPerIssue = issueService
				.getHottestIssues(project);

		ModelAndView mav = new ModelAndView();
		mav.addObject("projectName", project.getName());
		mav.addObject("accessPerIssue", accessPerIssue);
		mav.addObject("accessPerIssueSize", accessPerIssue.size());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView hottestOfMonth(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Project project = (Project) session.getAttribute("project");

		List<AccessPerIssue> accessPerIssue = issueService
				.getHottestOfMonthIssues(project);

		ModelAndView mav = new ModelAndView();
		mav.addObject("projectName", project.getName());
		mav.addObject("accessPerIssue", accessPerIssue);
		mav.addObject("accessPerIssueSize", accessPerIssue.size());
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView hottestOfWeek(HttpServletRequest req) {
		HttpSession session = req.getSession();
		Project project = (Project) session.getAttribute("project");

		List<AccessPerIssue> accessPerIssue = issueService
				.getHottestOfWeekIssues(project);

		ModelAndView mav = new ModelAndView();
		mav.addObject("projectName", project.getName());
		mav.addObject("accessPerIssue", accessPerIssue);
		mav.addObject("accessPerIssueSize", accessPerIssue.size());
		return mav;
	}
}
