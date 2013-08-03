package ar.edu.itba.it.paw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.joda.time.DateTime;

import ar.edu.itba.it.paw.model.IssueLog.ChangeType;
import ar.edu.itba.it.paw.web.formatter.TimeFormatter;

@Entity
@Table(name = "issue")
@SuppressWarnings("unused")
public class Issue extends AbstractModel implements Comparable<Issue> {

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

	public enum Type {
		Error("Error"), NewFeature("Nueva característica"), Improvement(
				"Mejora"), Issue("Tarea"), Technique("Técnica");

		private String caption;

		Type(String caption) {
			this.caption = caption;
		}

		public String getCaption() {
			return caption;
		}
	}

	@Column(nullable = false)
	private String title;

	private String description;

	@Column(name = "creationdate", nullable = false)
	@org.hibernate.annotations.Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime creationDate;

	@Embedded
	@AttributeOverride(name = "minutes", column = @Column(name = "estimatedtime"))
	private Time estimatedTime;

	@ManyToOne
	@JoinColumn(name = "assigneduser")
	private User assignedUser;

	@ManyToOne
	@JoinColumn(name = "reporteduser", nullable = false)
	private User reportedUser;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private State state;

	@Enumerated(EnumType.STRING)
	private Resolution resolution;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Priority priority;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Type type;

	@ManyToOne
	@JoinColumn(name = "projectid", nullable = false)
	private Project project;

	@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
	@JoinTable(name = "comment", joinColumns = @JoinColumn(name = "issueid"), inverseJoinColumns = @JoinColumn(name = "id"))
	@Sort(type = SortType.NATURAL)
	private SortedSet<Comment> comments;

	@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
	@JoinTable(name = "work", joinColumns = @JoinColumn(name = "issueid"), inverseJoinColumns = @JoinColumn(name = "id"))
	@Sort(type = SortType.NATURAL)
	private SortedSet<Work> works;

	@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
	@JoinColumn(name = "issueid", nullable = false)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Sort(type = SortType.NATURAL)
	private SortedSet<IssueFile> files;

	@ManyToMany
	@JoinTable(name = "vote", joinColumns = @JoinColumn(name = "issueid"), inverseJoinColumns = @JoinColumn(name = "userid"))
	private Set<User> voters;

	@ManyToMany(mappedBy = "affectingIssues", cascade = CascadeType.ALL)
	private List<Version> affectedVersions;

	@ManyToMany(mappedBy = "fixedIssues", cascade = CascadeType.ALL)
	private List<Version> fixedVersions;

	@ManyToMany
	@JoinTable(name = "collaborator", joinColumns = @JoinColumn(name = "issueid"), inverseJoinColumns = @JoinColumn(name = "userid"))
	@Sort(type = SortType.NATURAL)
	private SortedSet<User> collaborators;

	@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
	private List<IssueLog> issueLogs;
	
	@OneToMany(mappedBy = "issueA", cascade = CascadeType.ALL)
	@JoinColumn(name = "issuea")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<IssueRelation> relations;
	

	public Issue() {

	}

	public Issue(Project p, String title, String description,
			DateTime creationDate, Time estimatedTime, User assignedUser,
			User reportedUser, State state, Priority priority, Type type) {

		if (p == null || reportedUser == null || creationDate == null
				|| priority == null || state == null || type == null
				|| !ValidationUtils.validateLength(title, 1, 25)
				|| !ValidationUtils.validateMaxLength(description, 255))
			throw new IllegalArgumentException();

		this.project = p;
		this.title = title;
		this.description = description;
		this.creationDate = creationDate;
		this.estimatedTime = estimatedTime;
		this.assignedUser = assignedUser;
		if (assignedUser != null)
			assignedUser.addIssue(this);
		this.reportedUser = reportedUser;
		this.state = state;
		this.priority = priority;
		this.project.addIssue(this);
		this.type = type;

		this.fixedVersions = new ArrayList<Version>();
		this.affectedVersions = new ArrayList<Version>();
		this.issueLogs = new ArrayList<IssueLog>();
		this.relations = new HashSet<IssueRelation>();

		EmailNotifier emailNotifier = new EmailNotifier();
		emailNotifier.onIssueCreate(this);
	}

	public String getCode() {
		return project.getCode() + "-" + id;
	}

	public Resolution getResolution() {
		return resolution;
	}

	private void setResolution(Resolution p) {
		this.resolution = p;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(User user, Priority p) {
		if (p == null)
			throw new IllegalArgumentException();
		IssueLog log = new IssueLog(this, user, new DateTime(),
				ChangeType.Priority, priority.getCaption(), p.getCaption());
		this.priority = p;
	}

	public Type getType() {
		return type;
	}

	public void setType(User source, Type t) {
		if (t == null)
			throw new IllegalArgumentException();
		
		IssueLog log = new IssueLog(this, source, new DateTime(),
				ChangeType.Type, type.getCaption(), t.getCaption());
		this.type = t;
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

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User source, User user) {
		if (user != null) {
			IssueLog log = new IssueLog(this, source, new DateTime(),
					ChangeType.Assigned, (assignedUser == null) ? null
							: assignedUser.getName(), user.getName());
			this.assignedUser = user;
			user.addIssue(this);
		}
	}

	public User getReportedUser() {
		return reportedUser;
	}

	public State getState() {
		return state;
	}

	public SortedSet<Comment> getComments() {
		return comments;
	}

	public void addComment(User source, Comment comment) {
		if (!canAddComment(source))
			throw new IllegalArgumentException();
		this.comments.add(comment);
	}

	public boolean canAddComment(User user) {
		return user != null && (project.isMember(user) || user.getAdmin());
	}

	public SortedSet<Work> getWorks() {
		return works;
	}

	public boolean canDownloadIssueFile(User user) {
		return user != null
				&& (project.getIsPublic() || project.isMember(user) || user
						.getAdmin());
	}

	public boolean canAddIssueFile(User user) {
		return user != null && (project.isMember(user) || user.getAdmin());
	}

	public boolean canDeleteIssueFile(User user) {
		return user != null && (project.isMember(user) || user.getAdmin());
	}

	public void addIssueFile(User user, IssueFile f) {
		if (!canAddIssueFile(user))
			throw new IllegalArgumentException();
		this.files.add(f);
	}

	public void deleteIssueFile(User user, IssueFile file) {
		if (!canDeleteIssueFile(user))
			throw new IllegalArgumentException();
		this.files.remove(file);
	}

	public void addWork(User source, Work w) {
		if (!canAddWork(source))
			throw new IllegalArgumentException();
		this.works.add(w);
	}

	public boolean canAddWork(User user) {
		return user != null
				&& (collaborators.contains(user)
						|| (getAssignedUser() != null && getAssignedUser()
								.equals(user)) || user.getAdmin());
	}

	public boolean canEditWork(User user) {
		return user != null
				&& (collaborators.contains(user)
						|| (getAssignedUser() != null && getAssignedUser()
								.equals(user)) || user.getAdmin());
	}

	public void vote(User user) {
		if (!canVote(user) || voted(user))
			throw new IllegalArgumentException();
		this.voters.add(user);
	}

	public void removeVote(User user) {
		if (!canVote(user) || !voted(user))
			throw new IllegalArgumentException();
		this.voters.remove(user);
	}

	public boolean canVote(User user) {
		return user != null
				&& !getReportedUser().equals(user)
				&& !getState().equals(State.Completed)
				&& (project.isMember(user) || user.getAdmin() || project
						.getIsPublic());
	}

	public int getQuantOfVotes() {
		if (voters == null)
			return 0;
		return this.voters.size();
	}

	public boolean voted(User user) {
		if (voters == null)
			return false;
		return this.voters.contains(user);
	}

	public void addCollaborator(User source, User user) {
		if (!canAddCollaborator(source) || !collaborators.add(user))
			throw new IllegalArgumentException();
	}

	public void removeCollaborator(User source, User user) {
		if (!canRemoveCollaborator(source) || !collaborators.remove(user))
			throw new IllegalArgumentException();
	}

	public SortedSet<User> getCollaborators() {
		return this.collaborators;
	}

	public boolean canAddCollaborator(User user) {
		return user != null
				&& ((getAssignedUser() != null && getAssignedUser()
						.equals(user)) || user.getAdmin());
	}

	public boolean canRemoveCollaborator(User user) {
		return user != null
				&& ((getAssignedUser() != null && getAssignedUser()
						.equals(user)) || user.getAdmin());
	}

	private void setState(User source, State state) {
		if (state == null)
			throw new IllegalArgumentException();
		IssueLog log = new IssueLog(this, source, new DateTime(),
				ChangeType.State, this.state.getCaption(), state.getCaption());
		EmailNotifier emailNotifier = new EmailNotifier();
		emailNotifier.onIssueStateChange(this, state);
		this.state = state;
	}

	public void setTitle(User source, String title) {
		if (!ValidationUtils.validateLength(title, 1, 25))
			throw new IllegalArgumentException();
		IssueLog log = new IssueLog(this, source, new DateTime(),
				ChangeType.Title, this.title, title);
		this.title = title;
	}

	public void setDescription(User source, String description) {
		if (!ValidationUtils.validateMaxLength(description, 255))
			throw new IllegalArgumentException();
		IssueLog log = new IssueLog(this, source, new DateTime(),
				ChangeType.Description, this.description, description);
		this.description = description;
	}

	public void setEstimatedTime(User source, Time estimatedTime) {
		if (estimatedTime != null) {
			TimeFormatter f = new TimeFormatter();
			IssueLog log = new IssueLog(this, source, new DateTime(),
					ChangeType.Estimated, f.print(this.estimatedTime, null),
					f.print(estimatedTime, null));
		} else {
			IssueLog log = new IssueLog(this, source, new DateTime(),
					ChangeType.Estimated, null, null);
		}

		this.estimatedTime = estimatedTime;
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

	public boolean canAssignIssue(User user) {
		return user != null
				&& (getProject().isMember(user) || user.getAdmin()) 
				&& (getAssignedUser() == null || !getAssignedUser().equals(user));
	}

	public boolean canMark(User user, State state) {
		if(user == null || (!getProject().isMember(user) && !user.getAdmin()))
			return false;
		switch (state) {
		case Open:
			return getState().equals(State.Ongoing)
					&& getAssignedUser() != null
					&& getAssignedUser().equals(user);
		case Ongoing:
			return getState().equals(State.Open)
					&& getAssignedUser() != null
					&& (getAssignedUser().equals(user));
		case Completed:
			return getState().equals(State.Ongoing);
		case Closed:
			Project project = getProject();
			return project.hasLeaderRights(user)
					&& !getState().equals(State.Closed);
		}
		return false;
	}

	public void setResolution(User source, Resolution resolution) {
		if (!canMark(source, State.Completed))
			throw new IllegalArgumentException();

		
		IssueLog log = new IssueLog(this, source, new DateTime(),
				ChangeType.Resolution, this.resolution != null ? this.resolution.getCaption() : "",
				resolution.getCaption());

		setResolution(resolution);
		setState(source, State.Completed);
	}

	public void mark(User source, State state) {
		if (!canMark(source, state))
			throw new IllegalArgumentException();
		setState(source, state);
	}

	private String getVersionNameList(List<Version> list) {
		String out = "[ ";
		for (Version v : list)
			out += v.getName() + " ";
		out += "]";
		return out;
	}

	public void setFixedVersions(User source, List<Version> fixedVersions) {
		this.fixedVersions = fixedVersions;
	}

	public List<Version> getFixedVersions() {
		return fixedVersions;
	}

	public void addFixedVersion(User source, Version version) {
		if (!fixedVersions.contains(version)) {

			String prev = getVersionNameList(fixedVersions);
			this.fixedVersions.add(version);
			String actual = getVersionNameList(fixedVersions);

			IssueLog log = new IssueLog(this, source, new DateTime(),
					ChangeType.FixedVersions, prev, actual);
		}
	}

	public void setAffectedVersions(List<Version> affectedVersions) {
		this.affectedVersions = affectedVersions;
	}

	public List<Version> getAffectedVersions() {
		return affectedVersions;
	}

	public void addAffectedVersion(User source, Version version) {
		if (!affectedVersions.contains(version)) {
			String prev = getVersionNameList(affectedVersions);
			this.affectedVersions.add(version);
			String actual = getVersionNameList(affectedVersions);

			IssueLog log = new IssueLog(this, source, new DateTime(),
					ChangeType.AffectedVersions, prev, actual);
		}
	}

	public void removeAffectedVersion(User source, Version version) {
		if (affectedVersions.contains(version)) {
			String prev = getVersionNameList(affectedVersions);
			affectedVersions.remove(version);
			String actual = getVersionNameList(affectedVersions);

			IssueLog log = new IssueLog(this, source, new DateTime(),
					ChangeType.AffectedVersions, prev, actual);
		}
	}

	public void removeFixedVersion(User source, Version version) {
		if (fixedVersions.contains(version)) {

			String prev = getVersionNameList(fixedVersions);
			fixedVersions.remove(version);
			String actual = getVersionNameList(fixedVersions);

			IssueLog log = new IssueLog(this, source, new DateTime(),
					ChangeType.FixedVersions, prev, actual);
		}
	}

	public boolean filter(Filter filter) {
		if (!this.getProject().equals(filter.getProject())
				|| !this.getCode().contains(filter.getIssueCode())
				|| !this.getTitle().contains(filter.getIssueTitle())
				|| !this.getDescription()
						.contains(filter.getIssueDescription()))
			return false;

		if (filter.getIssueReportedUser() != null
				&& !filter.getIssueReportedUser()
						.equals(this.getReportedUser()))
			return false;

		if (filter.getIssueAssignedUser() != null
				&& !filter.getIssueAssignedUser()
						.equals(this.getAssignedUser()))
			return false;

		if (filter.getIssueType() != null
				&& !filter.getIssueType().equals(this.getType()))
			return false;

		if (filter.getIssueState() != null
				&& !filter.getIssueState().equals(this.getState()))
			return false;

		if (filter.getIssueResolution() != null
				&& !filter.getIssueResolution().equals(this.getResolution()))
			return false;

		DateTime creationDate = this.getCreationDate();
		if ((filter.getDateFrom() != null && creationDate.isBefore(filter
				.getDateFrom()))
				|| (filter.getDateTo() != null && creationDate.isAfter(filter
						.getDateTo())))
			return false;
		
		if (filter.getAffectedVersion() != null && this.getAffectedVersions() != null
				&& !this.getAffectedVersions().contains(filter.getAffectedVersion()) )
			return false;

		if (filter.getFixedVersion() != null && this.getFixedVersions() != null
				&& !this.getFixedVersions().contains(filter.getFixedVersion()) )
			return false;

		return true;
	}

	public int getProgressPercentage() {
		Time dedicatedTime = new Time(0);
		if (works != null)
			for (Work work : works)
				dedicatedTime.add(work.getDedicatedTime());

		if (estimatedTime == null || estimatedTime.getMinutes() == 0)
			return -1;
		return (int) (((float) dedicatedTime.getMinutes() / estimatedTime
				.getMinutes()) * 100);
	}

	public boolean canAddVersionToIssue(User source, Project project) {
		return source != null
				&& (project.isMember(source) || source.getAdmin());
	}

	public void setIssueLogs(List<IssueLog> issueLogs) {
		this.issueLogs = issueLogs;
	}

	public List<IssueLog> getIssueLogs() {
		return issueLogs;
	}
	
	public Set<IssueRelation> getRelations() {
		return relations;
	}
	
	public List<Issue> getRelatedIssues(IssueRelation.Type type){
		List<Issue> out = new ArrayList<Issue>();
		for (IssueRelation r : this.relations)
			if (r.getType().equals(type))
				out.add(r.getIssueB());
		return out;
	}
	
	public SortedSet<Issue> getUnrelatedIssues(IssueRelation.Type type){
		List<Issue> all = project.getIssues();
		List<Issue> related = new ArrayList<Issue>();
		SortedSet<Issue> out = new TreeSet<Issue>();
		
		for (IssueRelation r : this.relations)
			if (r.getType().equals(type))
				related.add(r.getIssueB());
		
		for (Issue i : all)
			if (!this.equals(i) && !related.contains(i))
				out.add(i);
		
		return out;
	}
	
	public void addRelation(User user, IssueRelation relation){
		if (!getProject().canRelateIssues(user))
			throw new IllegalArgumentException();
		this.relations.add(relation);
	}
	
	public void deleteRelation(User user, IssueRelation relation){
		if (!getProject().canRelateIssues(user))
			throw new IllegalArgumentException();
		this.relations.remove(relation);
	}

	public void addIssueLog(IssueLog log) {
		this.issueLogs.add(log);
	}

	public SortedSet<IssueFile> getFiles() {
		return files;
	}

	public int getFilesQuantity() {
		return this.files.size();
	}

	public void setFiles(SortedSet<IssueFile> files) {
		this.files = files;
	}

	public int compareTo(Issue issue) {
		if (!this.title.equals(issue.getTitle()))
			return this.title.compareTo(issue.getTitle());
		return this.getCode().compareTo(issue.getCode());
	}
}
