package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;

public class ProjectUserForm {
	private Project project;
	private User source;
	private User target;
	
	public ProjectUserForm(){		
	}

	public User getSource() {
		return source;
	}

	public void setSource(User source) {
		this.source = source;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public void setTarget(User target) {
		this.target = target;
	}

	public User getTarget() {
		return target;
	}
}
