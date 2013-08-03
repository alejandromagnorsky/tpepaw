package ar.edu.itba.it.paw.web.page.issue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.Comment;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueLog;
import ar.edu.itba.it.paw.model.IssueRelation;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.DateTimePanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.IssuePriorityPanel;
import ar.edu.itba.it.paw.web.common.IssueStateLabel;
import ar.edu.itba.it.paw.web.common.IssueTypePanel;
import ar.edu.itba.it.paw.web.common.ProgressBarPanel;
import ar.edu.itba.it.paw.web.common.TextPropertyPanel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;
import ar.edu.itba.it.paw.web.page.issuefile.IssueFileListPage;
import ar.edu.itba.it.paw.web.page.project.ProjectViewPage;

@SuppressWarnings("serial")
public class IssueViewPage extends BasePage {

	private EntityModel<Issue> issueModel;

	public IssueViewPage(Issue issue) {
		User source = WicketSession.get().getUser();
		this.issueModel = new EntityModel<Issue>(Issue.class, issue);
		EntityModel<Project> projectModel = new EntityModel<Project>(
				Project.class, issueModel.getObject().getProject());

		setDefaultModel(issueModel);
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));

		Link<Project> projectLink = new Link<Project>("link", projectModel) {
			@Override
			public void onClick() {
				setResponsePage(new ProjectViewPage(getModelObject()));
			}
		};
		projectLink.add(new Label("project", new PropertyModel<String>(
				projectModel, "name")));
		add(projectLink);

		Link<Void> viewIssueFilesLink = new Link<Void>("viewIssueFilesLink") {
			@Override
			public void onClick() {
				setResponsePage(new IssueFileListPage(issueModel.getObject()));
			}
		};
		add(viewIssueFilesLink);

		Link<Void> manageIssueRelationsLink = new Link<Void>(
				"manageIssueRelationsLink") {
			@Override
			public void onClick() {
				setResponsePage(new IssueRelationPage(issueModel.getObject()));
			}
		};
		if (!projectModel.getObject().canRelateIssues(source))
			manageIssueRelationsLink.setVisible(false);
		add(manageIssueRelationsLink);

		Link<Void> editIssueLink = new Link<Void>("editIssueLink") {
			@Override
			public void onClick() {
				setResponsePage(new EditIssuePage(issueModel.getObject()));
			}
		};
		if (!projectModel.getObject().canAddIssue(source))
			editIssueLink.setVisible(false);
		add(editIssueLink);

		add(new Label("title", new PropertyModel<String>(issueModel, "title")));
		add(new Label("code", new PropertyModel<String>(issueModel, "code")));
		add(new DateTimePanel("date", issueModel.getObject().getCreationDate()));

		add(new UsernamePanel("reportedUser", new EntityModel<User>(User.class,
				issueModel.getObject().getReportedUser())));
		add(new TextPropertyPanel("assignedUserPanel", new EntityModel<User>(
				User.class, issueModel.getObject().getAssignedUser()),
				"assignedUser"));

		add(new TextPropertyPanel("estimatedTimePanel",
				new PropertyModel<String>(issueModel, "estimatedTime"),
				"estimatedTime"));

		add(new IssueStateLabel("state", new PropertyModel<Issue.State>(
				issueModel, "state")));

		add(new IssuePriorityPanel("priority",
				new PropertyModel<Issue.Priority>(issueModel, "priority")));

		add(new IssueTypePanel("type", new PropertyModel<Issue.Type>(
				issueModel, "type")));

		add(new TextPropertyPanel("resolutionPanel", new PropertyModel<String>(
				issueModel, "resolution.caption"), "resolution"));

		add(new Label("votes", new PropertyModel<String>(issueModel,
				"quantOfVotes")));
		add(new Label("description", new PropertyModel<String>(issueModel,
				"description")));

		add(new Label("progress", new LoadableDetachableModel<String>() {
			@Override
			protected String load() {
				int progressPercentage = issueModel.getObject()
						.getProgressPercentage();
				if (progressPercentage < 0)
					return new ResourceModel("noEstimatedTime").getObject();
				else if (progressPercentage == 0)
					return new ResourceModel("notStarted").getObject();
				else if (progressPercentage > 0 && progressPercentage < 100)
					return new ResourceModel("inProcess").getObject();
				else if (progressPercentage == 100)
					return new ResourceModel("workedEstimatedTime").getObject();
				else
					return new ResourceModel("exceededEstimatedTime")
							.getObject();
			}
		}));
		add(new ProgressBarPanel("progressBarContent", issueModel));

		add(new Link<Void>("workList") {
			@Override
			public void onClick() {
				setResponsePage(new WorkListPage(issueModel.getObject()));
			}
		});

		Link<Void> collaboratorsLink = new Link<Void>("collaborators") {
			@Override
			public void onClick() {
				setResponsePage(new CollaboratorListPage(issueModel.getObject()));
			}
		};
		if (source == null
				|| !issueModel.getObject().canAddCollaborator(source))
			collaboratorsLink.setVisible(false);
		add(collaboratorsLink);

		Link<Void> voteLink = new Link<Void>("vote") {
			@Override
			public void onClick() {
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();
				if (!issue.voted(source))
					issue.vote(source);
				else
					issue.removeVote(source);

				addOrReplace(getVoteLabel());
			}
		};
		if (!issueModel.getObject().canVote(source))
			voteLink.setVisible(false);
		voteLink.add(getVoteLabel());
		add(voteLink);

		Link<Void> followLink = new Link<Void>("followLink") {
			@Override
			public void onClick() {
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();

				if (source != null) {
					if (issue.canRemoveFollower(source))
						issue.removeFollower(source);
					else if (issue.canAddFollower(source))
						issue.addFollower(source);
				}
				setResponsePage(new IssueViewPage(issueModel.getObject()));
			}
		};
		if (!issue.canAddFollower(source) && !issue.canRemoveFollower(source))
			followLink.setVisible(false);
		followLink.add(getFollowLabel(source));
		add(followLink);

		Link<Void> markLink = new Link<Void>("markLink") {
			@Override
			public void onClick() {
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();

				if (issue.getState().equals(Issue.State.Open)) {
					if (issue.canMark(source, Issue.State.Ongoing))
						issue.mark(source, Issue.State.Ongoing);
				} else if (issue.getState().equals(Issue.State.Ongoing)) {
					if (issue.canMark(source, Issue.State.Open))
						issue.mark(source, Issue.State.Open);
				}

				setResponsePage(new IssueViewPage(issueModel.getObject()));
			}
		};

		if (!issueModel.getObject().canMark(source, Issue.State.Open) && !issueModel
						.getObject().canMark(source, Issue.State.Ongoing))
			markLink.setVisible(false);
		markLink.add(getMarkLabel());
		add(markLink);

		Link<Void> closeLink = new Link<Void>("closeLink") {
			@Override
			public void onClick() {
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();

				if (issue.canMark(source, Issue.State.Closed))
					issue.mark(source, Issue.State.Closed);

				setResponsePage(new IssueViewPage(issueModel.getObject()));
			}
		};

		if (!issueModel.getObject().canMark(source, Issue.State.Closed))
			closeLink.setVisible(false);
		add(closeLink);

		Link<Void> assignLink = new Link<Void>("assignLink") {
			@Override
			public void onClick() {
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();

				if (issue.canAssignIssue(source))
					issue.setAssignedUser(source, source);

				setResponsePage(new IssueViewPage(issueModel.getObject()));
			}
		};
		if (!issueModel.getObject().canAssignIssue(source))
			assignLink.setVisible(false);
		add(assignLink);

		add(new RefreshingView<IssueLog>("recentChanges") {

			@Override
			protected Iterator<IModel<IssueLog>> getItemModels() {
				SortedSet<IModel<IssueLog>> result = new TreeSet<IModel<IssueLog>>(
						(new Comparator<IModel<IssueLog>>() {
							public int compare(IModel<IssueLog> o1,
									IModel<IssueLog> o2) {
								return o2.getObject().getDate()
										.compareTo(o1.getObject().getDate());
							}
						}));

				for (IssueLog i : issueModel.getObject().getIssueLogs())
					result.add(new EntityModel<IssueLog>(IssueLog.class, i));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<IssueLog> item) {
				item.add(new UsernamePanel("source", new EntityModel<User>(
						User.class, item.getModelObject().getSource())));
				item.add(new DateTimePanel("date", item.getModelObject()
						.getDate()));
				item.add(new Label("type", new PropertyModel<String>(item
						.getModel(), "type.caption")));
				item.add(new Label("previous", new PropertyModel<String>(item
						.getModel(), "previous")));
				item.add(new Label("actual", new PropertyModel<String>(item
						.getModel(), "actual")));
			}

		});

		add(new RefreshingView<Version>("affectedVersions") {
			@Override
			protected Iterator<IModel<Version>> getItemModels() {
				SortedSet<IModel<Version>> result = new TreeSet<IModel<Version>>(
						(new Comparator<IModel<Version>>() {
							public int compare(IModel<Version> o1,
									IModel<Version> o2) {
								return o1
										.getObject()
										.getReleaseDate()
										.compareTo(
												o2.getObject().getReleaseDate());
							}
						}));

				for (Version i : issueModel.getObject().getAffectedVersions())
					result.add(new EntityModel<Version>(Version.class, i));

				return result.iterator();
			}

			@Override
			protected void populateItem(Item<Version> item) {
				item.add(new Label("version", new PropertyModel<String>(
						new EntityModel<Version>(Version.class, item
								.getModelObject()), "name")));
			}

		});

		add(new RefreshingView<Version>("fixedVersions") {
			@Override
			protected Iterator<IModel<Version>> getItemModels() {
				SortedSet<IModel<Version>> result = new TreeSet<IModel<Version>>(
						(new Comparator<IModel<Version>>() {
							public int compare(IModel<Version> o1,
									IModel<Version> o2) {
								return o1
										.getObject()
										.getReleaseDate()
										.compareTo(
												o2.getObject().getReleaseDate());
							}
						}));

				for (Version i : issueModel.getObject().getFixedVersions())
					result.add(new EntityModel<Version>(Version.class, i));

				return result.iterator();
			}

			@Override
			protected void populateItem(Item<Version> item) {
				item.add(new Label("version", new PropertyModel<String>(
						new EntityModel<Version>(Version.class, item
								.getModelObject()), "name")));
			}

		});

		addRelationList("dependsOnList", IssueRelation.Type.DependsOn);
		addRelationList("necessaryForList", IssueRelation.Type.NecessaryFor);
		addRelationList("relatedToList", IssueRelation.Type.RelatedTo);
		addRelationList("duplicatedWithList", IssueRelation.Type.DuplicatedWith);

		add(new RefreshingView<User>("collaboratorList") {
			@Override
			protected Iterator<IModel<User>> getItemModels() {
				SortedSet<IModel<User>> result = new TreeSet<IModel<User>>(
						(new Comparator<IModel<User>>() {
							public int compare(IModel<User> o1, IModel<User> o2) {
								return o1.getObject().compareTo(o2.getObject());
							}
						}));

				for (User u : issueModel.getObject().getCollaborators())
					result.add(new EntityModel<User>(User.class, u));

				return result.iterator();
			}

			@Override
			protected void populateItem(Item<User> item) {
				item.add(new UsernamePanel("user", new EntityModel<User>(
						User.class, item.getModelObject())));
			}
		});

		add(new RefreshingView<User>("followersList") {
			@Override
			protected Iterator<IModel<User>> getItemModels() {
				SortedSet<IModel<User>> result = new TreeSet<IModel<User>>(
						(new Comparator<IModel<User>>() {
							public int compare(IModel<User> o1, IModel<User> o2) {
								return o1.getObject().compareTo(o2.getObject());
							}
						}));
				for (User u : issueModel.getObject().getFollowers())
					result.add(new EntityModel<User>(User.class, u));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<User> item) {
				item.add(new UsernamePanel("user", new EntityModel<User>(
						User.class, item.getModelObject())));
			}
		});

		add(new RefreshingView<Comment>("commentList") {
			@Override
			protected Iterator<IModel<Comment>> getItemModels() {
				SortedSet<IModel<Comment>> result = new TreeSet<IModel<Comment>>(
						new Comparator<IModel<Comment>>() {
							public int compare(IModel<Comment> o1,
									IModel<Comment> o2) {
								return o1.getObject().compareTo(o2.getObject());
							}
						});

				for (Comment c : issueModel.getObject().getComments())
					result.add(new EntityModel<Comment>(Comment.class, c));

				return result.iterator();
			}

			@Override
			protected void populateItem(Item<Comment> item) {
				item.add(new UsernamePanel("user", new EntityModel<User>(
						User.class, item.getModelObject().getUser())));
				item.add(new Label("description", new PropertyModel<String>(
						item.getModel(), "description")));
				item.add(new DateTimePanel("date", item.getModelObject()
						.getDate()));
			}
		});

		if (!issueModel.getObject().canMark(source,
						Issue.State.Completed))
			addResolutionForm(false);
		else
			addResolutionForm(true);

		if (!issueModel.getObject().canAddComment(source))
			addCommentForm(false);
		else
			addCommentForm(true);
	}

	private void addRelationList(String name, final IssueRelation.Type type) {
		add(new RefreshingView<Issue>(name) {
			@Override
			protected Iterator<IModel<Issue>> getItemModels() {
				List<IModel<Issue>> result = new ArrayList<IModel<Issue>>();
				for (Issue i : issueModel.getObject().getRelatedIssues(type))
					result.add(new EntityModel<Issue>(Issue.class, i));
				return result.iterator();
			}

			@Override
			protected void populateItem(final Item<Issue> item) {
				Link<Void> issueLink = new Link<Void>("issueLink") {
					@Override
					public void onClick() {
						setResponsePage(new IssueViewPage(item.getModel()
								.getObject()));
					}
				};
				issueLink.add(new Label("issue", new PropertyModel<String>(item
						.getModel(), "title")));
				item.add(issueLink);
			}
		});
	}

	public transient Issue.Resolution resolution;

	private void addResolutionForm(boolean visible) {
		Form<Issue> form = new Form<Issue>("setResolutionForm",
				new CompoundPropertyModel<Issue>(this));

		IModel<List<Resolution>> stateModel = new LoadableDetachableModel<List<Resolution>>() {
			@Override
			protected List<Resolution> load() {
				return Arrays.asList(Resolution.values());
			}
		};

		form.add(new DropDownChoice<Resolution>("resolution", stateModel) {
			public boolean wantOnSelectionChangedNotifications() {
				return true;
			}

			public void onSelectionChanged(Resolution r) {
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();

				if (issue.canMark(source, Issue.State.Completed))
					issue.setResolution(source, r);

				setResponsePage(new IssueViewPage(issue));
			}
		}.setChoiceRenderer(new ChoiceRenderer<Resolution>("caption"))
				.setRequired(true));

		form.add(new FeedbackPanel("feedback"));

		if (!visible)
			form.setVisible(false);
		add(form);
	}

	public transient String description;

	private void addCommentForm(boolean visible) {
		Form<Comment> form = new Form<Comment>("addCommentForm",
				new CompoundPropertyModel<Comment>(this));

		form.add(new CommentFormPanel("commentFormPanel"));

		form.add(new Button("addComment", new ResourceModel("add")) {
			@Override
			public void onSubmit() {
				User source = WicketSession.get().getUser();
				Issue issue = issueModel.getObject();
				Comment comment = new Comment(source, new DateTime(),
						description, issue);

				issue.addComment(source, comment);
				setResponsePage(new IssueViewPage(issue));
			}
		});
		if (!visible)
			form.setVisible(false);
		add(form);
	}

	private Label getVoteLabel() {
		User source = WicketSession.get().getUser();
		if (!issueModel.getObject().voted(source))
			return new Label("voteHint", new ResourceModel("addHint"));
		else
			return new Label("voteHint", new ResourceModel("removeHint"));
	}

	private Label getMarkLabel() {
		Issue issue = issueModel.getObject();

		if (issue.getState().equals(Issue.State.Open))
			return new Label("markLabel", Issue.State.Ongoing.getCaption());
		else if (issue.getState().equals(Issue.State.Ongoing))
			return new Label("markLabel", Issue.State.Open.getCaption());
		return new Label("markLabel", "");
	}

	private Label getFollowLabel(User user) {
		Issue issue = issueModel.getObject();

		if (user != null) {
			if (issue.getFollowers().contains(user))
				return new Label("followHint",
						new ResourceModel("removeHint").getObject());
			else
				return new Label("followHint",
						new ResourceModel("addHint").getObject());
		}
		return new Label("followHint", "");

	}
}
