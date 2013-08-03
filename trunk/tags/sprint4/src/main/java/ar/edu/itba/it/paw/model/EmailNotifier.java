package ar.edu.itba.it.paw.model;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.it.paw.model.Issue.State;

public class EmailNotifier implements ProjectListener, IssueListener {

	public void onProjectAddUser(Project project, User user) {
		sendEmail(getRecipients(project), "Trackr",
				"Se ha agregado a "+user.getFullname()+" como nuevo miembro del proyecto "
						+ project.getName()+".");
	}

	public void onIssueCreate(Issue issue) {
		sendEmail(getRecipients(issue.getProject()), "Trackr",
				"Se ha creado la tarea " + issue.getTitle()
						+ " en el proyecto " + issue.getProject().getName()+".");
	}

	public void onIssueStateChange(Issue issue, State state) {
		sendEmail(getRecipients(issue.getProject()), "Trackr",
				"Se ha modificado el estado de la tarea " + issue.getTitle()
						+ " del proyecto " + issue.getProject().getName()
						+ " de " + issue.getState().getCaption() + " a "
						+ state.getCaption()+".");
	}

	private void sendEmail(List<String> to, String subject, String body) {
		try {
			Email email = new Email(to, subject, body);
			EmailSender.getInstance().send(email);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	private List<String> getRecipients(Project p) {
		List<String> to = new ArrayList<String>();
		to.add(p.getLeader().getEmail());
		for (User user : p.getUsers())
			to.add(user.getEmail());
		return to;
	}

}
