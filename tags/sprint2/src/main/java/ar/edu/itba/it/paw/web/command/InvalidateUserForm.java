package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.model.User;

public class InvalidateUserForm {
	private User source;
	private User target;

	public InvalidateUserForm() {
	}

	public User getSource() {
		return source;
	}

	public void setSource(User source) {
		this.source = source;
	}

	public void setTarget(User target) {
		this.target = target;
	}

	public User getTarget() {
		return target;
	}

}
