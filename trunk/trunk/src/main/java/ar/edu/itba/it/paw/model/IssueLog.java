package ar.edu.itba.it.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "issue_log")
public class IssueLog extends AbstractModel {

	public enum ChangeType {

		Assigned("Usuario asignado"), Title("Título"), Description(
				"Descripción"), Estimated("Tiempo estimado"), Reported(
				"Usuario reportado"), State("Estado de tarea"), Resolution(
				"Resolución de tarea"), Priority("Prioridad de tarea"), AffectedVersions(
				"Versiones afectadas"), FixedVersions("Versiones arregladas"),
				Type("Tipo de tarea");

		String caption;

		ChangeType(String caption) {
			this.caption = caption;
		}

		public String getCaption() {
			return caption;
		}
	}

	@ManyToOne
	@JoinColumn(name = "issueid", nullable = false)
	private Issue issue;

	@ManyToOne
	@JoinColumn(name = "userid", nullable = false)
	private User source;

	@Column(name = "date", nullable = false)
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime date;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChangeType type;

	private String previous;
	
	private String actual;

	IssueLog() {
	}

	public IssueLog(Issue issue, User source, DateTime date, ChangeType type,
			String previous, String actual) {
		this.issue = issue;
		this.source = source;
		this.date = date;
		this.type = type;
		this.previous = previous;
		this.actual = actual;

		if (previous == null || !previous.equals(actual))
			this.issue.addIssueLog(this);
	}

	public Issue getIssue() {
		return issue;
	}

	public User getSource() {
		return source;
	}

	public DateTime getDate() {
		return date;
	}

	public ChangeType getType() {
		return type;
	}

	public String getPrevious() {
		return previous;
	}

	public String getActual() {
		return actual;
	}
}
