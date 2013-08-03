package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.page.project.ProjectViewPage;

@SuppressWarnings("serial")
public class CurrentFilterPanel extends Panel {

	public CurrentFilterPanel(String id){
		super(id);
		Filter filter = WicketSession.get().getFilter();
		Project project = WicketSession.get().getProject();

		if(filter != null && filter.getProject().equals(project)){
			add(new Label("filterLabel", new ResourceModel("activeFilter")));
			add(new Label("filterName", filter.getName()));
			add(new Link<Void>("stopFilterLink"){
				@Override
				public void onClick() {
					WicketSession.get().removeFilter(WicketSession.get().getFilter());
					setResponsePage(new ProjectViewPage(WicketSession.get().getProject()));
				}				
			});
		} else {
			add(new Label("filterLabel", new ResourceModel("noActiveFilter")));
			add(new Label("filterName", "").setVisible(false));
			add(new Link<Void>("stopFilterLink"){
				@Override
				public void onClick() {
					
				}				
			}.setVisible(false));
		}			
	}
}
