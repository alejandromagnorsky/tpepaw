package ar.edu.itba.it.paw.web.page.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.StringValidator.MaximumLengthValidator;

import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.web.converter.DateTimeConverter;

@SuppressWarnings("serial")
public class FilterFormPanel extends Panel {

	private static final int MAX_DESCRIPTION_LENGTH = 255;
	
	public FilterFormPanel(String id, final IModel<Project> projectModel){
		super(id);
		
		add(new TextField<String>("issueCode"));
		add(new TextField<String>("issueTitle"));
		add(new TextArea<String>("issueDescription").add(new MaximumLengthValidator(MAX_DESCRIPTION_LENGTH)));
		
		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			@Override
			protected List<User> load() {
				SortedSet<User> users = projectModel.getObject().getUsers();
				users.add(projectModel.getObject().getLeader());
				return new ArrayList<User>(users);
			}
		};
		add(new DropDownChoice<User>("issueReportedUser", usersModel));
		add(new DropDownChoice<User>("issueAssignedUser", usersModel));
		
		add(new DateTextField("dateFrom") {
			@Override
			public IConverter getConverter(Class<?> type) {
				return new DateTimeConverter();
			}
		});
		
		add(new DateTextField("dateTo") {
			@Override
			public IConverter getConverter(Class<?> type) {
				return new DateTimeConverter();
			}
		});
		
		IModel<List<Type>> typeModel = new LoadableDetachableModel<List<Type>>() {
			@Override
			protected List<Type> load() {
				return Arrays.asList(Type.values());
			}			
		};
		add(new DropDownChoice<Type>("issueType", typeModel).setChoiceRenderer(new ChoiceRenderer<Type>("caption")));
		
		IModel<List<State>> stateModel = new LoadableDetachableModel<List<State>>() {
			@Override
			protected List<State> load() {
				return Arrays.asList(State.values());
			}			
		};
		add(new DropDownChoice<State>("issueState", stateModel).setChoiceRenderer(new ChoiceRenderer<State>("caption")));
		
		IModel<List<Resolution>> resolutionModel = new LoadableDetachableModel<List<Resolution>>() {
			@Override
			protected List<Resolution> load() {
				return Arrays.asList(Resolution.values());
			}			
		};
		add(new DropDownChoice<Resolution>("issueResolution", resolutionModel).setChoiceRenderer(new ChoiceRenderer<Resolution>("caption")));

		IModel<List<Version>> versionModel = new LoadableDetachableModel<List<Version>>() {
			@Override
			protected List<Version> load() {
				return projectModel.getObject().getVersions();
			}			
		};
		add(new DropDownChoice<Version>("affectedVersion", versionModel).setChoiceRenderer(new ChoiceRenderer<Version>("name")));
		add(new DropDownChoice<Version>("fixedVersion", versionModel).setChoiceRenderer(new ChoiceRenderer<Version>("name")));

		add(new FilterNamePanel("namePanel", projectModel));
		add(new ActionPanel("actionPanel"));
		
		add(new FeedbackPanel("feedback"));
	}
}
