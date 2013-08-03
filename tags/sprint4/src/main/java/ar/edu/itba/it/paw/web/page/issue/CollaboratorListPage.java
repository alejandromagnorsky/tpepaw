package ar.edu.itba.it.paw.web.page.issue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueRepo;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;

@SuppressWarnings("serial")
public class CollaboratorListPage extends BasePage {
	
	@SpringBean
	private IssueRepo issueRepo;
	
	private EntityModel<Issue> issueModel;
		
	public CollaboratorListPage(Issue issue){
		issueModel = new EntityModel<Issue>(Issue.class, issue);
				
		add(new BreadcrumbsPanel("breadcrumbsPanel", new EntityModel<Project>(Project.class, issueModel.getObject().getProject()), issueModel));
		
		
		IModel<List<User>> collaboratorsModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				return new ArrayList<User>(issueModel.getObject().getCollaborators());
			}
		};
		IModel<List<User>> candidatesModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				List<User> result = new ArrayList<User>(issueModel.getObject().getCollaborators());
				result.addAll(issueRepo.getCollaboratorCandidates(issueModel.getObject()));
				return result;
			}
		};
		ChoiceRenderer<User> choiceRenderer = new ChoiceRenderer<User>("name", "name");
		final Palette<User> collaboratorPalette = new Palette<User>("collaborators", collaboratorsModel, candidatesModel, choiceRenderer, 10, false);
		Form<CollaboratorListPage> form = new Form<CollaboratorListPage>("collaboratorForm", new CompoundPropertyModel<CollaboratorListPage>(this)) {
			@Override
			protected void onSubmit() {
				Iterator<User> collaborators = collaboratorPalette.getSelectedChoices();
				Issue issue = issueModel.getObject();
				User source = WicketSession.get().getUser();
				// Add new collaborator
				while(collaborators.hasNext()){
					User collaborator = collaborators.next();
					if(!issue.getCollaborators().contains(collaborator))
						issue.addCollaborator(source, collaborator);
				}
				// Remove old collaborator
				Iterator<User> commonUsers = collaboratorPalette.getUnselectedChoices();
				while(commonUsers.hasNext()){
					User commonUser = commonUsers.next();
					if(issue.getCollaborators().contains(commonUser))
						issue.removeCollaborator(source, commonUser);
				}
				setResponsePage(new IssueViewPage(issueModel.getObject()));
			}
		};
		form.add(collaboratorPalette);
		form.add(new Button("accept", new ResourceModel("accept")));
		add(form);
	}

}
