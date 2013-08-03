package ar.edu.itba.it.paw.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.IssueRelation;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.repo.IssueRepo;
import ar.edu.itba.it.paw.repo.UserRepo;
import ar.edu.itba.it.paw.web.command.CollaboratorForm;
import ar.edu.itba.it.paw.web.command.CommentForm;
import ar.edu.itba.it.paw.web.command.IssueForm;
import ar.edu.itba.it.paw.web.command.IssueRelationForm;
import ar.edu.itba.it.paw.web.formatter.TimeFormatter;
import ar.edu.itba.it.paw.web.validator.CommentFormValidator;
import ar.edu.itba.it.paw.web.validator.IssueFormValidator;

@Controller
public class IssueController {

	private static final State DEFAULT_STATE = State.Open;
	private static final Priority DEFAULT_PRIORITY = Priority.Normal;
	private static final Type DEFAULT_TYPE = Type.Issue;

	private UserRepo userRepo;
	private IssueRepo issueRepo;
	private TimeFormatter timeFormatter;
	private IssueFormValidator issueFormValidator;
	private CommentFormValidator commentFormValidator;

	@Autowired
	public IssueController(UserRepo userRepo, IssueRepo issueRepo,
			TimeFormatter timeFormatter, IssueFormValidator issueFormValidator,
			CommentFormValidator commentFormValidator) {
		this.userRepo = userRepo;
		this.issueRepo = issueRepo;
		this.timeFormatter = timeFormatter;
		this.issueFormValidator = issueFormValidator;
		this.commentFormValidator = commentFormValidator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req,
			@RequestParam("code") Project project) {
		ModelAndView mav = new ModelAndView("issue/list");

		HttpSession session = ((HttpServletRequest) req).getSession();
		session.setAttribute("projectId", project.getId());
		User user = (User) req.getAttribute("user");
		
		if (!project.canViewProject(user))
			return new ModelAndView(new RedirectView("../project/view"));
		
		
		List<User> toAdd = userRepo.getValidUsers();
		toAdd.removeAll(project.getUsers());
		toAdd.remove(project.getLeader());
		
		mav.addObject("canAddIssue", project.canAddIssue(user));
		mav.addObject("canViewWorkReport", project.canViewWorkReport(user));
		mav.addObject("canViewStatus", project.canViewStatus(user));
		
		
		mav.addObject("canAddUserToProject", project.canAddUser(user) && toAdd.size() > 0);
		mav.addObject("canRemoveUserToProject", project.canRemoveUser(user) && project.getUsers().size() > 0);
		mav.addObject("canViewVersionList", project.canViewVersionList(user));
		mav.addObject("canViewFilterManager", project.canViewFilterManager(user));
					
		List<Issue> issueList = issueRepo.get(project);
		List<Issue> filteredIssues = new ArrayList<Issue>();

		Integer filterId = (Integer) session.getAttribute("filterId");
		if (filterId == null) {
			filteredIssues = issueList;
			mav.addObject("hasFilter", false);
		} else {
			Filter filter;
			if (filterId != 0)
				filter = (Filter) req.getAttribute("filter");
			else
				filter = (Filter) session.getAttribute("tempFilter");
			
			if (filter.getProject().equals(project)
					&& ((user == null && filter.getUser() == null) || (user != null
							&& filter.getUser() != null && filter.getUser()
							.equals(user)))) {
				for (Issue i : issueList)
					if (i.filter(filter))
						filteredIssues.add(i);
				mav.addObject("hasFilter", true);
			} else {
				filteredIssues = issueList;
				mav.addObject("hasFilter", false);
			}
		}

		Map<Issue, String> issues = new HashMap<Issue, String>();
		for (Issue issue : filteredIssues)
			issues.put(issue, timeFormatter.print(issue.getEstimatedTime(),
					null));

		mav.addObject("project", project);
		mav.addObject("changelog", project.getLastChanges(5));
		mav.addObject("issues", issues);
		mav.addObject("issuesQuant", issues.size());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest req, IssueForm form, Errors errors) {
		Project project = (Project) req.getAttribute("project");
		User source = (User) req.getAttribute("user");

		issueFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			Issue issue = form.build(DEFAULT_STATE, new DateTime(), source);

			if (form.getAffectedVersions() != null)
				for (Version v : form.getAffectedVersions())
					v.addAffectingIssue(source, issue);

			if (form.getFixedVersions() != null)
				for (Version v : form.getFixedVersions())
					v.addFixedIssue(source, issue);

			issueRepo.add(issue);

			return view(req, issue);
		} else {
			ModelAndView mav = new ModelAndView();
			mav.addObject("versions", project.getVersions());
			mav.addObject("projectName", project.getName());
			mav.addObject("priorityList", Issue.Priority.values());
			mav.addObject("userList", project.getUsers());
			mav.addObject("typeList", Issue.Type.values());
			return mav;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest req) {
		Project project = (Project) req.getAttribute("project");
			
		IssueForm issueForm = new IssueForm();
		issueForm.setProject(project);
		issueForm.setPriority(DEFAULT_PRIORITY);
		issueForm.setType(DEFAULT_TYPE);
		ModelAndView mav = new ModelAndView();
		mav.addObject("issueForm", issueForm);
		mav.addObject("projectName", project.getName());
		mav.addObject("priorityList", Issue.Priority.values());
		mav.addObject("versions", project.getVersions());
		mav.addObject("userList", project.getUsers());
		mav.addObject("typeList", Issue.Type.values());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView edit(HttpServletRequest req, IssueForm form,
			Errors errors) {
		Project project = (Project) req.getAttribute("project");

		issueFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			User source = (User) req.getAttribute("user");
			if (source != null)
				form.update(source);
			return view(req, form.getOriginalIssue());
		} else {
			
			ModelAndView mav = new ModelAndView();
			mav.addObject("projectName", project.getName());
			mav.addObject("originalIssueName", form.getOriginalIssue().getTitle());
			mav.addObject("issueCode", form.getOriginalIssue().getCode());
			mav.addObject("reportedUserName", form.getOriginalIssue().getReportedUser().getName());
			mav.addObject("priorityList", Issue.Priority.values());
			mav.addObject("userList", project.getUsers());
			mav.addObject("versions", project.getVersions());
			mav.addObject("typeList", Issue.Type.values());
			return mav;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		Project project = (Project) req.getAttribute("project");		

		IssueForm issueForm = new IssueForm();
		issueForm.setOriginalIssue(issue);
		issueForm.setTitle(issue.getTitle());
		issueForm.setDescription(issue.getDescription());
		issueForm.setAssignedUser(issue.getAssignedUser());
		issueForm.setEstimatedTime(issue.getEstimatedTime());
		issueForm.setPriority(issue.getPriority());
		issueForm.setType(issue.getType());
		issueForm.setProject(project);
		issueForm.setAffectedVersions(issue.getAffectedVersions());
		issueForm.setFixedVersions(issue.getFixedVersions());

		ModelAndView mav = new ModelAndView();
		mav.addObject("projectName", project.getName());
		mav.addObject("issueCode", issue.getCode());
		mav.addObject("reportedUserName", issue.getReportedUser().getName());
		mav.addObject("originalIssueName", issue.getTitle());
		mav.addObject("priorityList", Issue.Priority.values());
		mav.addObject("issueForm", issueForm);
		mav.addObject("versions", project.getVersions());
		mav.addObject("userList", project.getUsers());
		mav.addObject("typeList", Issue.Type.values());
		return mav;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView assigned(HttpServletRequest req) {
		User source = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");		
	
		Map<Issue, String> issues = new HashMap<Issue, String>();
		for (Issue issue : source.getIssues(project))
			if (issue.getState().equals(Issue.State.Open)
					|| issue.getState().equals(Issue.State.Ongoing))
				issues.put(issue, timeFormatter.print(issue.getEstimatedTime(),
						null));

		ModelAndView mav = new ModelAndView();
		mav.addObject("project", project);
		mav.addObject("issues", issues);
		mav.addObject("issuesQuant", issues.size());
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView view(HttpServletRequest req, CommentForm form, Errors errors) {
		User user = (User) req.getAttribute("user");
		Issue issue = form.getIssue();
		Project project = (Project) req.getAttribute("project");
		
		if(!issue.canAddComment(user))
			return new ModelAndView(new RedirectView("../issue/view?code="
					+ issue.getCode()));	
			
		commentFormValidator.validate(form, errors);
		if (!errors.hasErrors()) {
			form.build(user);
			return view(req, issue);
		} else {
			ModelAndView mav = getMavView(issue, user, project);
			return mav;
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		Project project = (Project) req.getAttribute("project");

		ModelAndView mav = getMavView(issue, user, project);
		CommentForm commentForm = new CommentForm();
		commentForm.setIssue(issue);
		mav.addObject("commentForm", commentForm);
		return mav;
	}
	
	private ModelAndView getMavView(Issue issue, User user, Project project){
		ModelAndView mav = new ModelAndView("issue/view");
		if (issue.getResolution() != null)
			mav.addObject("hasResolution", true);

		// No other way to tell jsp of data...
		if (user != null) {
			mav.addObject("canAddComment", issue.canAddComment(user));
			mav.addObject("canEditIssue", project.canEditIssue(user));	
			mav.addObject("canCloseIssue", issue.canMark(user, State.Closed));
			mav.addObject("canMarkIssueAsOpen", issue.canMark(user, State.Open));
			mav.addObject("canMarkIssueAsOngoing", issue.canMark(user, State.Ongoing));
			mav.addObject("canResolveIssue", issue.canMark(user, State.Completed));
			mav.addObject("canAssignIssue", issue.canAssignIssue(user));
			mav.addObject("canVoteIssue", issue.canVote(user));
			mav.addObject("voted", issue.voted(user));
			mav.addObject("canAddCollaborator", issue.canAddCollaborator(user)
					&& issueRepo.getCollaboratorCandidates(issue) != null
					&& issueRepo.getCollaboratorCandidates(issue).size() > 0);
			mav.addObject("canRemoveCollaborator", issue.canRemoveCollaborator(user)
					&& issue.getCollaborators() != null
					&& issue.getCollaborators().size() > 0);
			mav.addObject("resolutionStates", Issue.Resolution.values());
			mav.addObject("fixedVersions", issue.getFixedVersions());
			mav.addObject("affectedVersions", issue.getAffectedVersions());
		}
		
		mav.addObject("canRelateIssues", project.canRelateIssues(user));
		mav.addObject("canViewIssueFiles", project.canViewIssueFiles(user));
		mav.addObject("dependsOnList",		issue.getRelatedIssues(IssueRelation.Type.DependsOn));
		mav.addObject("necessaryForList",	issue.getRelatedIssues(IssueRelation.Type.NecessaryFor));
		mav.addObject("relatedToList",		issue.getRelatedIssues(IssueRelation.Type.RelatedTo));
		mav.addObject("duplicatedWithList",	issue.getRelatedIssues(IssueRelation.Type.DuplicatedWith));

		SortedSet<Comment> comments = issue.getComments();

		mav.addObject("commentList", comments);
		mav.addObject("commentListSize", (comments == null) ? 0 : comments
				.size());
		mav.addObject("progressPercentage", issue.getProgressPercentage());
		mav.addObject("issue", issue);
		mav.addObject("estimatedTime", timeFormatter.print(issue
				.getEstimatedTime(), null));
		mav.addObject("project", project);
		mav.addObject("issueLogs", issue.getIssueLogs());
		return mav;
	}
	
	

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView mark(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User source = (User) req.getAttribute("user");

		String op = req.getParameter("operation");
		String resolution = req.getParameter("resolution");

		// This ifs are merely for presentation, to prevent the Illegal argument
		// from appearing in case of double POST
		if (op != null && issue != null && source != null) {
			if (op.equals("open") && issue.canMark(source, State.Open)) {
				issue.mark(source, State.Open);
			} else if (op.equals("ongoing")
					&& issue.canMark(source, State.Ongoing)) {
				issue.mark(source, State.Ongoing);
			} else if (op.equals("close")
					&& issue.canMark(source, State.Closed)) {
				issue.mark(source, State.Closed);
			} else if (op.equals("resolve") && resolution != null
					&& issue.canMark(source, State.Completed)) {
				Resolution r = Resolution.valueOf(resolution);
				issue.setResolution(source, r);
			}
		}

		return view(req, issue);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView assign(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");

		if (issue.canAssignIssue(user))
			issue.setAssignedUser(user, user);

		return view(req, issue);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView vote(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		if (issue.canVote(user) && !issue.voted(user))
			issue.vote(user);
		return view(req, issue);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView removeVote(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		if (issue.canVote(user) &&  issue.voted(user))
			issue.removeVote(user);
		return view(req, issue);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView addCollaborator(HttpServletRequest req,
			CollaboratorForm form) {
		User user = (User) req.getAttribute("user");
		form.getIssue().addCollaborator(user, form.getTarget());
		return view(req, form.getIssue());
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView addCollaborator(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		if (!issue.canAddCollaborator(user))
			return new ModelAndView(new RedirectView("../issue/view?code="
					+ issue.getCode()));

		ModelAndView mav = new ModelAndView();
		CollaboratorForm collaboratorForm = new CollaboratorForm();
		collaboratorForm.setIssue(issue);
		mav.addObject("issue", issue);
		mav.addObject("collaboratorForm", collaboratorForm);
		mav.addObject("candidates", issueRepo.getCollaboratorCandidates(issue));

		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView removeCollaborator(HttpServletRequest req,
			CollaboratorForm form) {
		User user = (User) req.getAttribute("user");
		form.getIssue().removeCollaborator(user, form.getTarget());
		return view(req, form.getIssue());
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView removeCollaborator(HttpServletRequest req,
			@RequestParam("code") Issue issue) {
		User user = (User) req.getAttribute("user");
		if (!issue.canRemoveCollaborator(user))
			return new ModelAndView(new RedirectView("../issue/view?code="
					+ issue.getCode()));

		ModelAndView mav = new ModelAndView();
		CollaboratorForm collaboratorForm = new CollaboratorForm();
		collaboratorForm.setIssue(issue);
		mav.addObject("issue", issue);
		mav.addObject("collaboratorForm", collaboratorForm);
		mav.addObject("collaborators", issue.getCollaborators());

		return mav;
	}
	

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView relate(HttpServletRequest req, IssueRelationForm form, Errors errors) {
		User user = (User) req.getAttribute("user");
		Issue issue = form.getIssue();
		form.update(user);
		return view(req, issue);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView relate(HttpServletRequest req, @RequestParam("code") Issue issue) {
		Project project = (Project) req.getAttribute("project");
						
		ModelAndView mav = new ModelAndView();
		IssueRelationForm issueRelationForm = new IssueRelationForm();
		issueRelationForm.setIssue(issue);
		issueRelationForm.setDependsOn(issue.getRelatedIssues(IssueRelation.Type.DependsOn));
		issueRelationForm.setNecessaryFor(issue.getRelatedIssues(IssueRelation.Type.NecessaryFor));
		issueRelationForm.setRelatedTo(issue.getRelatedIssues(IssueRelation.Type.RelatedTo));
		issueRelationForm.setDuplicatedWith(issue.getRelatedIssues(IssueRelation.Type.DuplicatedWith));
		
		mav.addObject("issueRelationForm", issueRelationForm);
		mav.addObject("issue", issue);
		mav.addObject("projectName", issue.getProject().getName());
		List<Issue> issues = new ArrayList<Issue>(project.getIssues());
		issues.remove(issue);
		mav.addObject("issues", issues);
		
		return mav;
	}
}
