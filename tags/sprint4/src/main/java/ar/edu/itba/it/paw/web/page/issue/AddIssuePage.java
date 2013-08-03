package ar.edu.itba.it.paw.web.page.issue;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.EmailNotifier;
import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.IssueRepo;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.page.project.ProjectViewPage;

@SuppressWarnings("serial")
public class AddIssuePage extends BasePage {
	
	private static final State DEFAULT_STATE = State.Open;
	private static final Priority DEFAULT_PRIORITY = Priority.Normal;
	private static final Type DEFAULT_TYPE = Type.Issue;

	@SpringBean
	private IssueRepo issueRepo;
	
	public transient String title;
	public transient String description;
	public transient User assignedUser;
	public transient Time estimatedTime;
	public transient Type type;
	public transient Priority priority;
	public transient List<Version> affectedVersions;
	public transient List<Version> fixedVersions;
	
	public AddIssuePage(){
		final EntityModel<Project> projectModel = new EntityModel<Project>(Project.class, WicketSession.get().getProject());
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));
		
		initForm();
		
		Form<Issue> form = new Form<Issue>("addIssueForm", new CompoundPropertyModel<Issue>(this));
		form.add(new IssueFormPanel("issueFormPanel", projectModel));
		form.add(new Button("addIssue", new ResourceModel("add")){
			@Override
			public void onSubmit(){
				User source = WicketSession.get().getUser();
				Project project = projectModel.getObject();
				
				Issue issue = new Issue(project, title, description, new DateTime(), estimatedTime,
										assignedUser, source, DEFAULT_STATE, priority, type, new EmailNotifier());

				if (affectedVersions != null)
					for (Version v : affectedVersions)
						v.addAffectingIssue(source, issue);

				if (fixedVersions != null)
					for (Version v : fixedVersions)
						v.addFixedIssue(source, issue);
					
				issueRepo.add(issue);
				setResponsePage(new ProjectViewPage(project));
			}
		});
		
		add(form);
		
	}
	
	private void initForm(){
		this.type = DEFAULT_TYPE;
		this.priority = DEFAULT_PRIORITY;
		this.affectedVersions = new ArrayList<Version>();
		this.fixedVersions = new ArrayList<Version>();
	}
}
