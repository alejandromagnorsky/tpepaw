package ar.edu.itba.it.paw.web.page.project;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectRepo;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.web.WicketSession;
import ar.edu.itba.it.paw.web.common.BasePage;
import ar.edu.itba.it.paw.web.common.BreadcrumbsPanel;
import ar.edu.itba.it.paw.web.common.EntityModel;
import ar.edu.itba.it.paw.web.page.project.ProjectFormPanel.Visibility;

@SuppressWarnings("serial")
public class EditProjectPage extends BasePage {
	
	@SpringBean
	private transient ProjectRepo projectRepo;
	
	private transient String name;
	private transient String code;
	private transient String description;
	private transient User leader;
	private transient Visibility visibility;	
	
	public EditProjectPage() {
		Project project = WicketSession.get().getProject();
		final EntityModel<Project> projectModel = new EntityModel<Project>(Project.class, project);
		name = project.getName();
		code = project.getCode();
		description = project.getDescription();
		leader = project.getLeader();
		visibility = project.getIsPublic()? Visibility.Public: Visibility.Private;
		
		add(new BreadcrumbsPanel("breadcrumbsPanel", projectModel));
		Form<Project> form = new Form<Project>("editProjectForm", new CompoundPropertyModel<Project>(this));
		form.add(new ProjectFormPanel("projectFormPanel"));
		form.add(new Button("editProject", new ResourceModel("edit")){
			@Override
			public void onSubmit(){
				Project originalProject = projectModel.getObject();
				if(!originalProject.getCode().equals(code) && projectRepo.get(code) != null)
					error(getString("ExistentProject"));
				else {
					originalProject.setCode(code);
					originalProject.setName(name);
					originalProject.setDescription(description);					
					originalProject.setLeader(leader);
					originalProject.setIsPublic(visibility.getIsPublic());
					setResponsePage(ProjectListPage.class);
				}
			}
		});
		add(form);
	}
}
