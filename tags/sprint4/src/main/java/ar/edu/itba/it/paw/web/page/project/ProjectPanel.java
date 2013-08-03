package ar.edu.itba.it.paw.web.page.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;

@SuppressWarnings("serial")
public class ProjectPanel extends Panel {
	
	public ProjectPanel(String id, final IModel<Project> projectModel){
		super(id);
		setDefaultModel(projectModel);
		add(new Label("visibility", new LoadableDetachableModel<String>(){
			@Override
			protected String load() {
				Project project = projectModel.getObject();
				return project.getIsPublic()? new ResourceModel("public").getObject() : new ResourceModel("private").getObject();
			}		
		}));
		
		add(new UsernamePanel("leader", new LoadableDetachableModel<User>(){
			@Override
			protected User load() {
				Project project = projectModel.getObject();
				return project.getLeader();
			}			
		}));
		add(new Label("code", new PropertyModel<String>(projectModel, "code")));
		add(new Label("description", new PropertyModel<String>(projectModel, "description")));
		
		add(new RefreshingView<User>("users") {
			@Override
			protected Iterator<IModel<User>> getItemModels() {
				List<IModel<User>> result = new ArrayList<IModel<User>>();
				for (User u : projectModel.getObject().getUsers())
					result.add(new EntityModel<User>(User.class, u));
				return result.iterator();
			}

			@Override
			protected void populateItem(Item<User> item) {
				item.add(new UsernamePanel("name", item.getModel()));
			}
		});
	}

}
