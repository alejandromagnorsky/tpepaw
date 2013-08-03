package ar.edu.itba.it.paw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import ar.edu.itba.it.paw.model.Version.VersionState;

@Entity
@Table(name = "project")
public class Project extends AbstractModel implements Comparable<Project>{

	@Column(unique = true, nullable = false)
	private String code;

	@Column(nullable = false)
	private String name;

	private String description;

	@ManyToOne
	@JoinColumn(name = "leaderid", nullable = false)
	private User leader;

	@ManyToMany
	@JoinTable(name = "projectusers", joinColumns = @JoinColumn(name = "projectid"), inverseJoinColumns = @JoinColumn(name = "userid"))
	@Sort(type = SortType.NATURAL)
	private SortedSet<User> users;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@JoinTable(name = "issue", joinColumns = @JoinColumn(name = "projectid"), inverseJoinColumns = @JoinColumn(name = "id"))
	private List<Issue> issues;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "projectid", nullable = false)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Version> versions;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@JoinColumn(name = "projectid", nullable = false)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Filter> filters;

	@Column(nullable = false)
	private boolean isPublic;

	public Project() {

	}

	public Project(String code, String name, String description, User leader,
			boolean isPublic) {
		if (!ValidationUtils.validateLength(name, 1, 25)
				|| !ValidationUtils.validateMaxLength(description, 255)
				|| leader == null)
			throw new IllegalArgumentException();
		this.code = code;
		this.name = name;
		this.description = description;
		this.leader = leader;
		this.isPublic = isPublic;
		this.users = new TreeSet<User>();
		this.issues = new ArrayList<Issue>();
		this.versions = new ArrayList<Version>();
		this.filters = new TreeSet<Filter>();
	}

	public boolean getIsPublic() {
		return isPublic;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public SortedSet<User> getUsers() {
		return users;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public User getLeader() {
		return leader;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		if (!ValidationUtils.validateLength(name, 1, 25))
			throw new IllegalArgumentException();
		this.name = name;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setDescription(String description) {
		if (!ValidationUtils.validateMaxLength(description, 255))
			throw new IllegalArgumentException();
		this.description = description;
	}

	public void addIssue(Issue issue) {
		this.issues.add(issue);
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public boolean hasLeaderRights(User user) {
		return user != null && (user.getAdmin() || getLeader().equals(user));
	}

	public boolean canAddUser(User user) {
		return hasLeaderRights(user);
	}

	public boolean canRemoveUser(User user) {
		return hasLeaderRights(user);
	}

	public boolean canAddVersion(User user) {
		return hasLeaderRights(user);
	}

	public boolean canEditVersion(User user) {
		return hasLeaderRights(user);
	}

	public boolean canDeleteVersion(User source) {
		return hasLeaderRights(source);
	}

	public boolean canViewWorkReport(User user) {
		return hasLeaderRights(user);
	}

	public boolean canViewStatus(User user) {
		return hasLeaderRights(user);
	}

	public void addUser(User source, User target) {
		if (!canAddUser(source))
			throw new IllegalArgumentException();
		EmailNotifier emailNotifier = new EmailNotifier();
		emailNotifier.onProjectAddUser(this, target);
		this.users.add(target);
		target.addProject(this);
	}

	public void removeUser(User source, User target) {
		if (!canRemoveUser(source))
			throw new IllegalArgumentException();
		this.users.remove(target);
		target.removeProject(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Project other = (Project) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}	
	
	public boolean canAddIssue(User user) {
		return user != null && (isMember(user) || user.getAdmin());
	}
	
	public boolean canViewIssue(User user) {
		return getIsPublic() || canAddIssue(user);
	}

	public boolean canEditIssue(User user) {
		return canAddIssue(user);
	}

	public boolean canViewAssigned(User user) {
		return canAddIssue(user);
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	public boolean canViewVersionList(User source) {
		return getIsPublic() || hasLeaderRights(source);
	}

	public void addVersion(Version version) {
		this.versions.add(version);
	}

	public void deleteVersion(User user, Version version) {
		if (!canDeleteVersion(user))
			throw new IllegalArgumentException();
		this.versions.remove(version);
	}

	public List<Version> getVersions() {
		return versions;
	}

	public List<Version> getNonReleasedVersions() {
		List<Version> out = new ArrayList<Version>();
		for (Version version : versions)
			if (version.getState() != VersionState.Released)
				out.add(version);
		return out;
	}

	public void setFilters(SortedSet<Filter> filters) {
		this.filters = filters;
	}

	public SortedSet<Filter> getFilters() {
		return filters;
	}

	public boolean canAddFilter(User user) {
		return user != null && (this.isPublic || isMember(user) || user.getAdmin());
	}

	public boolean canViewFilterManager(User user) {
		return canAddFilter(user);
	}

	public boolean canEditFilter(User user) {
		return canAddFilter(user);
	}

	public boolean canDeleteFilter(User user) {
		return canAddFilter(user);
	}

	public void addFilter(User user, Filter filter) {
		if (!canAddFilter(user))
			throw new IllegalArgumentException();
		this.filters.add(filter);
	}

	public void deleteFilter(User user, Filter filter) {
		if (!canDeleteFilter(user))
			throw new IllegalArgumentException();
		this.filters.remove(filter);
	}

	public SortedSet<Filter> getFilters(User user) {
		SortedSet<Filter> out = new TreeSet<Filter>();
		for (Filter filter : filters)
			if (filter.getUser().equals(user))
				out.add(filter);
		return out;
	}

	public boolean canViewIssueFiles(User user) {
		return getIsPublic() || (user != null && (isMember(user) || user.getAdmin()));
	}	

	public boolean canRelateIssues(User user){
		return user != null && (isMember(user) || user.getAdmin());
	}

	public Filter getFilter(String name, User user) {
		for (Filter filter : filters)
			if (filter.getUser().equals(user) && filter.getName().equals(name))
				return filter;
		return null;
	}

	public Status getStatus() {
		return new Status();
	}

	public boolean isMember(User user) {
		if (user == null || getUsers() == null)
			return false;
		return getUsers().contains(user) || user.equals(leader);
	}

	public boolean canViewProject(User user) {
		return getIsPublic() || (user != null && (isMember(user) || user.getAdmin()));
	}

	public class Status {

		private Map<Issue.State, Node> map;

		private class Node {
			private Time time;
			private int quant;

			public Node() {
				this.quant = 0;
				this.time = new Time(0);
			}

			public Node(Time time) {
				this.quant = 0;
				this.time = time;
			}

			public void add(Node node) {
				this.quant++;
				this.time.add(node.time);
			}
		}

		public Status() {
			map = new HashMap<Issue.State, Node>();
			for (Issue.State state : Issue.State.values())
				map.put(state, new Node());
			for (Issue issue : Project.this.getIssues())
				map.get(issue.getState()).add(
						new Node(issue.getEstimatedTime()));
		}

		public Time getTime(Issue.State state) {
			return map.get(state).time;
		}

		public int getQuant(Issue.State state) {
			return map.get(state).quant;
		}
	}

	public List<IssueLog> getLastChanges(int qty) {
		List<IssueLog> tmp = new ArrayList<IssueLog>();

		for (Issue issue : issues)
			tmp.addAll(issue.getIssueLogs());

		Collections.sort(tmp, new Comparator<IssueLog>() {
			public int compare(IssueLog arg0, IssueLog arg1) {
				return arg1.getDate().compareTo(arg0.getDate());
			}
		});

		List<IssueLog> out = new ArrayList<IssueLog>();
		int i = 0;
		for (IssueLog log : tmp) {
			if (i++ < qty)
				out.add(log);
			else
				return out;
		}

		return out;
	}

	public int compareTo(Project other) {
		return this.code.compareTo(other.code);
	}
}
