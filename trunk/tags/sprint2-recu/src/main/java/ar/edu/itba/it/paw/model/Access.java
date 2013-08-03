package ar.edu.itba.it.paw.model;

import org.joda.time.LocalDate;


public class Access extends AbstractModel {

	private LocalDate date;
	private Issue issue;
	
	
	public Access(LocalDate date, Issue issue) {
		if(date == null || issue == null)
			throw new IllegalArgumentException();
		this.date = date;
		this.issue = issue;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public Issue getIssue() {
		return issue;
	}
}
