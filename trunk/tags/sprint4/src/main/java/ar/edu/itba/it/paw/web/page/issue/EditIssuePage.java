package ar.edu.itba.it.paw.web.page.issue;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class EditIssuePage extends BasePage {

	private transient String title;
	private transient String description;
	private transient User assignedUser;
	private transient Time estimatedTime;
	private transient Type type;
	private transient Priority priority;
	private transient List<Version> affectedVersions;
	private transient List<Version> fixedVersions;

	public EditIssuePage(Issue issue){
		final EntityModel<Project> projectModel = new EntityModel<Project>(Project.class, WicketSession.get().getProject());
		final EntityModel<Issue> issueModel = new EntityModel<Issue>(Issue.class, issue);
		final IModel<User> userModel = new EntityModel<User>(User.class, WicketSession.get().getUser());
		setDefaultModel(userModel);
		
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel, issueModel));
		
		initForm(issue);
		
		Form<Issue> form = new Form<Issue>("editIssueForm", new CompoundPropertyModel<Issue>(this));
		form.add(new IssueFormPanel("issueFormPanel", projectModel, issueModel));
		form.add(new Button("editIssue", new ResourceModel("edit")){
			@Override
			public void onSubmit(){
				updateIssue(userModel, issueModel);
				setResponsePage(new IssueViewPage(issueModel.getObject()));
			}
		});
		
		add(form);
	}
	
	private void initForm(Issue issue){
		this.title = issue.getTitle();
		this.description = issue.getDescription();
		this.assignedUser = issue.getAssignedUser();
		this.estimatedTime = issue.getEstimatedTime();
		this.type = issue.getType();
		this.priority = issue.getPriority();
		this.affectedVersions = issue.getAffectedVersions();
		this.fixedVersions = issue.getFixedVersions();
	}
	
	private void updateIssue(IModel<User> userModel, IModel<Issue> issueModel){
		issueModel.getObject().setTitle(userModel.getObject(), this.title);
		issueModel.getObject().setDescription(userModel.getObject(), this.description);
		if(assignedUser != null){
			IModel<User> assignedUserModel = new EntityModel<User>(User.class, this.assignedUser.getId());
			issueModel.getObject().setAssignedUser(userModel.getObject(), assignedUserModel.getObject());
		} else 
			issueModel.getObject().setAssignedUser(userModel.getObject(), null);
		issueModel.getObject().setEstimatedTime(userModel.getObject(), this.estimatedTime);
		issueModel.getObject().setType(userModel.getObject(), this.type);
		issueModel.getObject().setPriority(userModel.getObject(), this.priority);
		if(issueModel.getObject().getProject().getVersions().size() != 0){
			issueModel.getObject().setAffectedVersions(userModel.getObject(), this.affectedVersions);
			issueModel.getObject().setFixedVersions(userModel.getObject(), this.fixedVersions);
		}
	}
}
