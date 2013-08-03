package ar.edu.itba.it.paw.model;

import org.joda.time.DateTime;

public class Issue extends AbstractModel {

	public enum State {
		Open("Abierta"), Ongoing("En curso"), Completed("Finalizada"), Closed(
				"Cerrada");

		String caption;

		State(String caption) {
			this.caption = caption;
		}

		public String getCaption() {
			return caption;
		}
	}

	public enum Priority {
		Trivial("Trivial"), Low("Baja"), Normal("Normal"), High("Alta"), Critical(
				"Crítica");

		String caption;

		Priority(String caption) {
			this.caption = caption;
		}

		public String getCaption() {
			return caption;
		}
	}

	public enum Resolution {

		Resolved("Resuelta"), WontResolve("No se resuelve"), Duplicated(
				"Duplicada"), Irreproducible("Irreproducible"), Incomplete(
				"Incompleta");

		private String caption;

		Resolution(String caption) {
			this.caption = caption;
		}

		public String getCaption() {
			return caption;
		}
	}

	private String title;
	private String description;
	private DateTime creationDate;
	private Time estimatedTime;
	private User assignedUser;
	private User reportedUser;
	private State state;
	private Resolution resolution;
	private Priority priority;
	private Project project;
	
	public Issue(Project p, String title, String description,
			DateTime creationDate, Time estimatedTime, User assignedUser,
			User reportedUser, State state, Priority priority) {

		if (p == null || reportedUser == null || creationDate == null)
			throw new IllegalArgumentException();

		this.project = p;
		setTitle(title);
		setDescription(description);
		this.creationDate = creationDate;
		this.estimatedTime = estimatedTime;
		this.assignedUser = assignedUser;
		this.reportedUser = reportedUser;
		setState(state);
		setPriority(priority);
	}

	public String getCode() {
		return project.getCode() + "-" + id;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public void setResolution(Resolution p) {
		this.resolution = p;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority p) {
		if (p == null)
			throw new IllegalArgumentException();
		this.priority = p;
	}

	public String getTitle() {
		return title;
	}

	public Project getProject() {
		return project;
	}

	public String getDescription() {
		return description;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public Time getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(Time estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User user) {
		this.assignedUser = user;
	}

	public User getReportedUser() {
		return reportedUser;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		if (state == null)
			throw new IllegalArgumentException();
		this.state = state;
	}

	public void setTitle(String title) {
		if (!ValidationUtils.validateLength(title, 1, 25))
			throw new IllegalArgumentException();
		this.title = title;
	}

	public void setDescription(String description) {
		if (!ValidationUtils.validateMaxLength(description, 255))
			throw new IllegalArgumentException();
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getCode().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (!getCode().equals(other.getCode()))
			return false;
		return true;
	}
}
