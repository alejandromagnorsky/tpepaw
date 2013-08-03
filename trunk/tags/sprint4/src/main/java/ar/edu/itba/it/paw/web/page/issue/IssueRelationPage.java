package ar.edu.itba.it.paw.web.page.issue;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueRelation;
import ar.edu.itba.it.paw.model.IssueRelation.Type;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class IssueRelationPage extends BasePage {

	public transient List<Issue> dependsOn;
	public transient List<Issue> necessaryFor;
	public transient List<Issue> relatedTo;
	public transient List<Issue> duplicatedWith;
	
	public IssueRelationPage(Issue issue){
		final IModel<Issue> issueModel = new EntityModel<Issue>(Issue.class, issue);
		final IModel<Project> projectModel = new EntityModel<Project>(Project.class, WicketSession.get().getProject());
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel, issueModel));
		
		initForm(issueModel.getObject());
		
		Form<IssueRelation> form = new Form<IssueRelation>("issueRelationForm", new CompoundPropertyModel<IssueRelation>(this));
		form.add(new IssueRelationFormPanel("issueRelationFormPanel", projectModel, issueModel));
		form.add(new Button("acceptIssueRelation", new ResourceModel("accept")){
			@Override
			public void onSubmit(){
				updateRelations(issueModel);
				setResponsePage(new IssueViewPage(issueModel.getObject()));
			}
		});
		
		add(form);
	}
	
	private void initForm(Issue issue){
		dependsOn = issue.getRelatedIssues(Type.DependsOn);
		necessaryFor = issue.getRelatedIssues(Type.NecessaryFor);
		relatedTo = issue.getRelatedIssues(Type.RelatedTo);
		duplicatedWith = issue.getRelatedIssues(Type.DuplicatedWith);
	}
	
	private void updateRelations(IModel<Issue> issueModel){
		User user = WicketSession.get().getUser();
		addRelations(user, issueModel.getObject());
		deleteRelations(user, issueModel.getObject());
	}
	
	private void addRelations(User user, Issue issue){
		if (dependsOn != null)
			for (Issue i : dependsOn){
				issue.addRelation(user, new IssueRelation(Type.DependsOn, issue, i));
				i.addRelation(user, new IssueRelation(Type.NecessaryFor, i, issue));
			}
		
		if (necessaryFor != null)
			for (Issue i : necessaryFor){
				issue.addRelation(user, new IssueRelation(Type.NecessaryFor, issue, i));
				i.addRelation(user, new IssueRelation(Type.DependsOn, i, issue));
			}
		
		if (relatedTo != null)
			for (Issue i : relatedTo){
				issue.addRelation(user, new IssueRelation(Type.RelatedTo, issue, i));
				i.addRelation(user, new IssueRelation(Type.RelatedTo, i, issue));
			}
		
		if (duplicatedWith != null)
			for (Issue i : duplicatedWith){
				issue.addRelation(user, new IssueRelation(Type.DuplicatedWith, issue, i));
				i.addRelation(user, new IssueRelation(Type.DuplicatedWith, i, issue));
			}
	}
	
	private void deleteRelations(User user, Issue issue){
		List<Issue> toRemoveDependsOn = new ArrayList<Issue>(issue.getProject().getIssues());
		List<Issue> toRemoveNecessaryFor = new ArrayList<Issue>(issue.getProject().getIssues());
		List<Issue> toRemoveRelatedTo = new ArrayList<Issue>(issue.getProject().getIssues());
		List<Issue> toRemoveDuplicatedWith = new ArrayList<Issue>(issue.getProject().getIssues());
		
		toRemoveDependsOn.remove(issue);
		toRemoveNecessaryFor.remove(issue);
		toRemoveRelatedTo.remove(issue);
		toRemoveDuplicatedWith.remove(issue);

		if (dependsOn != null)
			toRemoveDependsOn.removeAll(dependsOn);
			for (Issue i : toRemoveDependsOn){
				issue.deleteRelation(user, new IssueRelation(Type.DependsOn, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.NecessaryFor, i, issue));
			}
		
		if (necessaryFor != null)
			toRemoveNecessaryFor.removeAll(necessaryFor);
			for (Issue i : toRemoveNecessaryFor){
				issue.deleteRelation(user, new IssueRelation(Type.NecessaryFor, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.DependsOn, i, issue));
			}
		
		if (relatedTo != null)
			toRemoveRelatedTo.removeAll(relatedTo);
			for (Issue i : toRemoveRelatedTo){
				issue.deleteRelation(user, new IssueRelation(Type.RelatedTo, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.RelatedTo, i, issue));
			}
		
		if (duplicatedWith != null)
			toRemoveDuplicatedWith.removeAll(duplicatedWith);
			for (Issue i : toRemoveDuplicatedWith){
				issue.deleteRelation(user, new IssueRelation(Type.DuplicatedWith, issue, i));
				i.deleteRelation(user, new IssueRelation(Type.DuplicatedWith, i, issue));
			}
	}
}
