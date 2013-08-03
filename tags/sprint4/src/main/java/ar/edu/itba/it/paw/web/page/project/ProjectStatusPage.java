package ar.edu.itba.it.paw.web.page.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.converter.TimeConverter;

@SuppressWarnings("serial")
public class ProjectStatusPage extends BasePage {

	private transient Project.Status projectStatus;
	
	public ProjectStatusPage(){
		Project project = WicketSession.get().getProject();
		final EntityModel<Project> projectModel = new EntityModel<Project>(Project.class, project);
		
		add(new BreadcrumbsPanel("breadcrumbsPanel", new EntityModel<Project>(Project.class, project)));
		add(new ProjectPanel("projectPanel", projectModel));
		
		add(new VersionsChart("versionsChart", projectModel));
		
		add(new RefreshingView<State>("states") {
			@Override
			protected Iterator<IModel<State>> getItemModels() {
				List<IModel<State>> result = new ArrayList<IModel<State>>();
				projectStatus = projectModel.getObject().getStatus();
				for (State state : Issue.State.values())
					result.add(new Model<State>(state));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<State> item) {
				item.add(new Label("state", new PropertyModel<String>(item.getModelObject(), "caption")));
				item.add(new Label("stateQuant", String.valueOf(projectStatus.getQuant(item.getModelObject()))));
				item.add(new Label("estimatedTime", new TimeConverter().convertToString(projectStatus.getTime(item.getModelObject()), null)));
			}
		});
	}
}
