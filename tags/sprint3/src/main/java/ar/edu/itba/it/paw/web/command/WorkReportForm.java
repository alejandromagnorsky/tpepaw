package ar.edu.itba.it.paw.web.command;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;

public class WorkReportForm {
	private Project project;
	private User source;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private DateTime from;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private DateTime to;

	public WorkReportForm() {
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

	public void setFrom(DateTime from) {
		this.from = from;
	}

	public DateTime getFrom() {
		return from;
	}

	public void setTo(DateTime to) {
		this.to = to;
	}

	public DateTime getTo() {
		return to;
	}
}
