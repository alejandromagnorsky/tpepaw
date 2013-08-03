package ar.edu.itba.it.paw.web.command;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import ar.edu.itba.it.paw.model.Filter;
import ar.edu.itba.it.paw.model.Issue.Resolution;
import ar.edu.itba.it.paw.model.Issue.State;
import ar.edu.itba.it.paw.model.Issue.Type;
import ar.edu.itba.it.paw.model.Project;
import ar.edu.itba.it.paw.model.User;
import ar.edu.itba.it.paw.model.Version;

public class FilterForm {
	private Filter originalFilter;		// For editing purposes only.
	private String name;
	private User user;
	private Project project;
	private String issueCode;
	private String issueTitle;
	private String issueDescription;
	private User issueReportedUser;
	private User issueAssignedUser;
	private Type issueType;
	private State issueState;
	private Resolution issueResolution;
	private Version affectedVersion;
	private Version fixedVersion;
	private Action action;
	@DateTimeFormat(pattern="dd/MM/yyyy") private DateTime dateFrom;
	@DateTimeFormat(pattern="dd/MM/yyyy") private DateTime dateTo;
	
	public enum Action {
		Save("Sólo guardar", "El filtro se guarda pero no se aplica."),
		Apply("Sólo aplicar", "El filtro existe sólo en esta sesión hasta ser removido."),
		SaveAndApply("Guardar y aplicar", "El filtro se guarda y se aplica.");
		
		String caption;
		String description;
	
		Action(String caption, String description) {
			this.caption = caption;
			this.description = description;
		}
	
		public String getCaption() {
			return caption;
		}
		
		public String getDescription(){
			return description;
		}
	}
	
	public Filter getOriginalFilter(){
		return originalFilter;
	}
	
	public void setOriginalFilter(Filter originalFilter){
		this.originalFilter = originalFilter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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

	public Action getAction(){
		return action;
	}
	
	public void setAction(Action action){
		this.action = action;
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

	public Filter build(){
		return new Filter(	this.name,				this.user,				this.project,
							this.issueCode,			this.issueTitle,		this.issueDescription,
							this.issueReportedUser,	this.issueAssignedUser,	this.issueType,
							this.issueState,		this.issueResolution,	this.affectedVersion,
							this.fixedVersion,		this.dateFrom,			this.dateTo);
	}
	
	public void update(Filter filter){
		filter.setId(this.originalFilter.getId());
		filter.setName(this.name);
		filter.setIssueCode(this.issueCode);
		filter.setIssueTitle(this.issueTitle);
		filter.setIssueDescription(this.issueDescription);
		filter.setIssueReportedUser(this.issueReportedUser);
		filter.setIssueAssignedUser(this.issueAssignedUser);
		filter.setIssueType(this.issueType);
		filter.setIssueState(this.issueState);
		filter.setIssueResolution(this.issueResolution);
		filter.setAffectedVersion(this.affectedVersion);
		filter.setFixedVersion(this.fixedVersion);
		filter.setDateFrom(this.dateFrom);
		filter.setDateTo(this.dateTo);
	}
}