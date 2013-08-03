package ar.edu.itba.it.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;

@Entity
@Table(name = "filter", uniqueConstraints={
		 @UniqueConstraint(columnNames={"name", "userid", "projectid"})})
public class Filter extends AbstractModel implements Comparable<Filter>{
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "userid", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "projectid", nullable = false)
	private Project project;
	
	@Column(name="issuecode")
	private String issueCode;
	
	@Column(name="issuetitle")
	private String issueTitle;
	
	@Column(name="issuedescription")
	private String issueDescription;
	
	@ManyToOne
	@JoinColumn(name = "issuereporteduserid")
	private User issueReportedUser;
	
	@ManyToOne
	@JoinColumn(name = "issueassigneduserid")
	private User issueAssignedUser;
	
	@Column(name = "issuetype")
	@Enumerated(EnumType.STRING)
	private Type issueType;
	
	@Column(name = "issuestate")
	@Enumerated(EnumType.STRING)
	private State issueState;
	
	@Column(name = "issueresolution")
	@Enumerated(EnumType.STRING)
	private Resolution issueResolution;
	
	@Column(name = "datefrom")
	@org.hibernate.annotations.Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private DateTime dateFrom;
	
	@Column(name = "dateto")
	@org.hibernate.annotations.Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private DateTime dateTo;
	
	@ManyToOne
	@JoinColumn(name = "affectedversion")
	private Version affectedVersion;
	
	@ManyToOne
	@JoinColumn(name = "fixedversion")
	private Version fixedVersion;
	
	public Filter(){
		
	}
	
	public Filter(String name, User user, Project project, String issueCode, String issueTitle,
			String issueDescription, User issueReportedUser, User issueAssignedUser,
			Type issueType, State issueState, Resolution issueResolution,
			Version affectedVersion, Version fixedVersion,
			DateTime dateFrom, DateTime dateTo) {
		
		if (name == null || name.length() > 25 || project == null	|| issueTitle.length() > 25
				|| issueCode.length() > 25 || issueDescription.length() > 255 )
			throw new IllegalArgumentException();
		
		this.name = name;
		this.user = user;
		this.project = project;
		
		this.issueCode = issueCode;
		this.issueTitle = issueTitle;
		this.issueDescription = issueDescription;
		this.issueReportedUser = issueReportedUser;
		this.issueAssignedUser = issueAssignedUser;
		this.issueType = issueType;
		this.issueState = issueState;
		this.issueResolution = issueResolution;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.affectedVersion = affectedVersion;
		this.fixedVersion = fixedVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser(){
		return user;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		project.addFilter(user, this);
	}

	public String getIssueCode() {
		return issueCode;
	}

	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
	}

	public String getIssueTitle() {
		return issueTitle;
	}

	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public User getIssueReportedUser() {
		return issueReportedUser;
	}

	public void setIssueReportedUser(User issueReportedUser) {
		this.issueReportedUser = issueReportedUser;
	}

	public User getIssueAssignedUser() {
		return issueAssignedUser;
	}

	public void setIssueAssignedUser(User issueAssignedUser) {
		this.issueAssignedUser = issueAssignedUser;
	}

	public Type getIssueType(){
		return issueType;
	}
	
	public void setIssueType(Type issueType){
		this.issueType = issueType;
	}
	
	public State getIssueState() {
		return issueState;
	}

	public void setIssueState(State issueState) {
		this.issueState = issueState;
	}

	public Resolution getIssueResolution() {
		return issueResolution;
	}

	public void setIssueResolution(Resolution issueResolution) {
		this.issueResolution = issueResolution;
	}

	public DateTime getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(DateTime dateFrom) {
		this.dateFrom = dateFrom;
	}

	public DateTime getDateTo() {
		return dateTo;
	}

	public void setDateTo(DateTime dateTo) {
		this.dateTo = dateTo;
	}

	public Version getAffectedVersion() {
		return affectedVersion;
	}

	public void setAffectedVersion(Version affectedVersion) {
		this.affectedVersion = affectedVersion;
	}

	public Version getFixedVersion() {
		return fixedVersion;
	}

	public void setFixedVersion(Version fixedVersion) {
		this.fixedVersion = fixedVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Filter other = (Filter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public int compareTo(Filter other) {
		if(this.name.compareTo(other.getName()) == 0){
			if(this.user.compareTo(other.getUser()) == 0)
				return this.project.compareTo(other.getProject());
			else
				return this.user.compareTo(other.getUser());
		} else
			return this.name.compareTo(other.getName());
	}
}