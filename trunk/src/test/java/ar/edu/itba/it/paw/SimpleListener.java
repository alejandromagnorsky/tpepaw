package ar.edu.itba.it.paw;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueListener;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.ProjectListener;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Issue.State;

public class SimpleListener implements IssueListener, ProjectListener {

	public void onIssueCreate(Issue issue) {
		System.out.println("Se ha creado la tarea " + issue.getTitle()
				+ " en el proyecto " + issue.getProject().getName()+".");		
	}

	public void onIssueStateChange(Issue issue, State state) {
		System.out.println("Se ha modificado el estado de la tarea " + issue.getTitle()
						+ " del proyecto " + issue.getProject().getName()
						+ " de " + issue.getState().getCaption() + " a "
						+ state.getCaption()+".");		
	}

	public void onProjectAddUser(Project project, User user) {
		System.out.println("Se ha agregado a "+user.getFullname()+" como nuevo miembro del proyecto "
						+ project.getName()+".");		
	}

}
