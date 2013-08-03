package ar.edu.itba.it.paw.web.page.issue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Issue.Priority;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Time;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;

@SuppressWarnings("serial")
public class IssueFormPanel extends Panel {
	
	private static final int MAX_DESCRIPTION_LENGTH = 255;
	
	public IssueFormPanel(String id, final IModel<Project> projectModel){
		this(id, projectModel, null);
	}
	
	public IssueFormPanel(String id, final IModel<Project> projectModel, final IModel<Issue> issueModel){
		super(id);
		
		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				SortedSet<User> users = projectModel.getObject().getUsers();
				users.add(projectModel.getObject().getLeader());
				return new ArrayList<User>(users);
			}
		};

		IModel<List<Type>> typeModel = new LoadableDetachableModel<List<Type>>() {
			@Override
			protected List<Type> load() {
				return Arrays.asList(Type.values());
			}
		};

		IModel<List<Priority>> priorityModel = new LoadableDetachableModel<List<Priority>>() {
			@Override
			protected List<Priority> load() {
				return Arrays.asList(Priority.values());
			}
		};
		
		IModel<List<Version>> versionsModel = new LoadableDetachableModel<List<Version>>() {
			@Override
			protected List<Version> load() {
				return projectModel.getObject().getVersions();
			}
		};
		
		ListView<Version> affectedVersionsView = new ListView<Version>("affectedVersionsView", versionsModel) {
			@Override
			protected void populateItem(ListItem<Version> item) {
				item.add(new Check<Version>("checkbox", item.getModel()));
				item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
			}
		};
		
		ListView<Version> fixedVersionsView = new ListView<Version>("fixedVersionsView", versionsModel) {
			@Override
			protected void populateItem(ListItem<Version> item) {
				item.add(new Check<Version>("checkbox", item.getModel()));
				item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
			}
		};
		
		add(new RequiredTextField<String>("title"));
		add(new TextArea<String>("description").add(new MaximumLengthValidator(MAX_DESCRIPTION_LENGTH)));
		add(new DropDownChoice<User>("assignedUser", usersModel).setNullValid(true));
		add(new TextField<Time>("estimatedTime"));
		add(new DropDownChoice<Priority>("priority", priorityModel).setChoiceRenderer(new ChoiceRenderer<Priority>("caption")));
		add(new DropDownChoice<Type>("type", typeModel).setChoiceRenderer(new ChoiceRenderer<Type>("caption")));
		
		CheckGroup<Version> affectedVersionsGroup = new CheckGroup<Version>("affectedVersions");
		CheckGroup<Version> fixedVersionsGroup = new CheckGroup<Version>("fixedVersions");
		
		affectedVersionsGroup.add(new Label("affectedVersionsLabel", new ResourceModel("affectedVersions")));
		affectedVersionsGroup.add(new CheckGroupSelector("versionSelector"));
		affectedVersionsGroup.add(affectedVersionsView);
		affectedVersionsView.setReuseItems(true);
		add(affectedVersionsGroup);
		
		fixedVersionsGroup.add(new Label("fixedVersionsLabel", new ResourceModel("fixedVersions")));
		fixedVersionsGroup.add(new CheckGroupSelector("versionSelector"));
		fixedVersionsGroup.add(fixedVersionsView);
		fixedVersionsView.setReuseItems(true);
		add(fixedVersionsGroup);
		
		if(versionsModel.getObject() == null || versionsModel.getObject().isEmpty()){
			affectedVersionsGroup.setVisible(false);
			fixedVersionsGroup.setVisible(false);
		}
				
		add(new FeedbackPanel("feedback"));
	}
}
