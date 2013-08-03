package ar.edu.itba.it.paw;

import org.joda.time.DateTime;

public class Issue {

	public enum State {
		Open {
			public String toString() {
				return "Abierta";
			}
		},
		Ongoing {
			public String toString() {
				return "En curso";
			}
		},
		Completed {
			public String toString() {
				return "Finalizada";
			}
		},
		Closed {
			public String toString() {
				return "Cerrada";
			}
		},
	}

	public enum Priority {
		Trivial("Trivial"),
		Low("Baja"),
		Normal("Normal"),
		High("Alta"),
		Critical("Crítica");
		String name;
		
		Priority(String name){
			this.name=name;
		}
		public String getName(){
			return name();
		}
		public String toString(){
			return name;
		}
	}

	public enum Resolution {
		Resolved {
			public String toString() {
				return "Resuelta";
			}
		},
		WontResolve {
			public String toString() {
				return "No se resuelve";
			}
		},
		Duplicated {
			public String toString() {
				return "Duplicada";
			}
		},
		Irreproducible {
			public String toString() {
				return "Irreproducible";
			}
		},
		Incomplete {
			public String toString() {
				return "Incompleta";
			}
		}
	}

	private String title;
	private String description;
	private DateTime creationDate;
	private DateTime completionDate;
	private float estimatedTime;
	private User assignedUser;
	private User reportedUser;
	private State state;

	private Resolution resolution;
	private Priority priority;
	private Project project;
	private int id;

	public Issue(Project p, String title, String description,
			DateTime creationDate, float estimatedTime, User assignedUser,
			User reportedUser, State state, Priority priority) {
		this.project = p;
		this.title = title;
		this.description = description;
		this.creationDate = creationDate;
		this.completionDate = null;
		this.estimatedTime = estimatedTime;
		this.assignedUser = assignedUser;
		this.reportedUser = reportedUser;
		this.state = state;
		this.priority = priority;
	}

	public void setCompletionDate(DateTime completionDate) {
		this.completionDate = completionDate;
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
		this.priority = p;
	}

	public String getTitle() {
		return title;
	}

	public Project getProject() {
		return project;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public float getEstimatedTime() {
		return estimatedTime;
	}

	public DateTime getCompletionDate() {
		return completionDate;
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
		this.state = state;
	}
}
