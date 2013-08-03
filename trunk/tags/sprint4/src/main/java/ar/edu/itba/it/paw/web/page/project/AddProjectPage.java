package ar.edu.itba.it.paw.web.page.project;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.EmailNotifier;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectRepo;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.page.project.ProjectFormPanel.Visibility;

@SuppressWarnings("serial")
public class AddProjectPage extends BasePage {
	
	@SpringBean
	private transient ProjectRepo projectRepo;
	
	private transient String name;
	private transient String code;
	private transient String description;
	private transient User leader;
	private transient Visibility visibility;	
	
	public AddProjectPage() {
		Form<Project> form = new Form<Project>("addProjectForm", new CompoundPropertyModel<Project>(this));
		form.add(new ProjectFormPanel("projectFormPanel"));
		form.add(new Button("addProject", new ResourceModel("add")){
			@Override
			public void onSubmit(){
				if(projectRepo.get(code) != null)
					error(getString("ExistentProject"));
				else {
					Project project = new Project(code, name, description, leader, visibility.getIsPublic(), new EmailNotifier());
					projectRepo.add(WicketSession.get().getUser(), project);
					setResponsePage(ProjectListPage.class);
				}				
			}
		});
		add(form);
	}
}
