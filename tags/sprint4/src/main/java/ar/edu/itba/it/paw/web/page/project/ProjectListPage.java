package ar.edu.itba.it.paw.web.page.project;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectRepo;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.common.UsernamePanel;
import ar.edu.itba.it.paw.web.common.AjaxUtils;

@SuppressWarnings("serial")
public class ProjectListPage extends BasePage {
	
	private static final int MAX_ROWS_PER_PAGE = 10;
	private static final int MAX_DESCRIPTION_PREVIEW_CHARS = 45;
	
	@SpringBean
	private transient ProjectRepo projectRepo;
	
	public ProjectListPage() {
		WicketSession.get().setProject(null);
		User source = WicketSession.get().getUser();
		
		Link<Void> addProjectLink = new Link<Void>("addProject") {
			@Override
			public void onClick() {
				setResponsePage(AddProjectPage.class);
			}
		};
		if(source == null || !source.canAddProject())
			addProjectLink.setVisible(false);
		add(addProjectLink);
		
		displayProjects(new ProjectDataProvider(projectRepo));
	}
	
	private void displayProjects(ProjectDataProvider provider){
		final WebMarkupContainer container = new WebMarkupContainer("container");
		
		final DataView<Project> dataView = new DataView<Project>("projects", provider){
			@Override
			protected void populateItem(final Item<Project> item) {
				Project project = item.getModelObject();
				
				Link<Project> viewProjectLink = new Link<Project>("viewProjectLink", item.getModel()) {
					@Override
					public void onClick() {
						setResponsePage(new ProjectViewPage(getModelObject()));
					}
				};
				item.add(new Label("code", project.getCode()));
				item.add(viewProjectLink.add(new Label("name", new PropertyModel<String>(item.getModel(), "name"))));
				item.add(new UsernamePanel("leader", new EntityModel<User>(User.class, item.getModelObject().getLeader())));
				item.add(new Label("visibility", new LoadableDetachableModel<String>(){
					@Override
					protected String load() {
						boolean isPublic = item.getModelObject().getIsPublic();
						return (isPublic)	? new ResourceModel("project").getObject() + " " + new ResourceModel("public").getObject()
											: new ResourceModel("project").getObject() + " " + new ResourceModel("private").getObject();
					}					
				}));				
				item.add(new Label("description", new LoadableDetachableModel<String>(){
					@Override
					protected String load() {
						String description = item.getModel().getObject().getDescription();
						if (description == null || description.isEmpty())
							return new ResourceModel("noDescription").getObject();
						else {
							if (description.length() <= MAX_DESCRIPTION_PREVIEW_CHARS)
								return description;
							return description.substring(0, MAX_DESCRIPTION_PREVIEW_CHARS) + "...";
						}
					}					
				}));
				
				if (item.getIndex() % 2 == 0)
					item.add(new AttributeModifier("class", new Model<String>("content-row even-row")));
				else
					item.add(new AttributeModifier("class", new Model<String>("content-row odd-row")));
			}
		};        
        dataView.setItemsPerPage(MAX_ROWS_PER_PAGE);
		
		container.add(AjaxUtils.getOrderLink("orderByCode", "code", dataView, container, provider));		
		container.add(AjaxUtils.getOrderLink("orderByName", "name", dataView, container, provider));		
		container.add(AjaxUtils.getOrderLink("orderByLeader", "leader", dataView, container, provider));
        container.add(AjaxUtils.getPager("navigator", dataView, container));
		container.add(dataView);
		add(container.setOutputMarkupId(true));
	}
}
