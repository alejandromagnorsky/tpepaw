package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;

public class ProjectForm {
	private String	name;
	private String	description;
	private String	code;
	private User	leader;
	private boolean	isPublic;
	private Project	originalProject;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Project getOriginalProject() {
		return originalProject;
	}

	public void setOriginalProject(Project originalProject) {
		this.originalProject = originalProject;
	}

	public Project build(){
		return new Project(	this.code,		this.name,		this.description,
							this.leader,	this.isPublic);
	}
	
	public void update(){
		this.originalProject.setName(this.name);
		this.originalProject.setDescription(this.description);
		this.originalProject.setCode(this.code);
		this.originalProject.setLeader(this.leader);
		this.originalProject.setIsPublic(this.isPublic);
	}
}
