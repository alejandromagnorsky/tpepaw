package ar.edu.itba.it.paw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

@Entity
@Table(name = "systemuser")
public class User extends AbstractModel implements Comparable<User> {

	public enum View {
		Issues("Tareas"),
		Filters("Filtros"),
		Versions("Versiones"),
		RecentChanges("Cambios recientes");
		
		private String caption;
		
		View(String caption){
			this.caption = caption;
		}
		
		public String getCaption(){
			return this.caption;
		}
	}
	
	@Column(name = "isadmin", nullable = false)
	private boolean admin;

	@Column(nullable = false)
	private boolean valid;

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String fullname;

	@Column(nullable = false)
	private String email;

	@ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
	private List<Project> projects;

	@OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL)
	private List<Issue> issues;

	@ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
	@Sort(type = SortType.NATURAL)
	private SortedSet<Issue> followingIssues;
	
	@CollectionOfElements(targetElement = View.class)
	@JoinTable(name = "defaultviews", joinColumns = @JoinColumn(name = "userid"))
	@Column(name = "view", nullable = false)
	@Enumerated(EnumType.STRING)
	private List<View> defaultViews;

	User() {

	}

	public User(boolean admin, boolean valid, String name, String password,
			String fullname, String email) {

		if (!ValidationUtils.validateLength(name, 1, 25)
				|| !ValidationUtils.validateLength(password, 1, 25)
				|| !ValidationUtils.validateLength(fullname, 1, 25)
				|| !ValidationUtils.validateLength(email, 1, 50)
				|| !email.matches("[A-Za-z0-9\\._]+@[A-Za-z0-9]+(\\.[A-Za-z]{2,4}){1,2}"))
			throw new IllegalArgumentException();

		this.admin = admin;
		this.valid = valid;
		this.name = name;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.projects = new ArrayList<Project>();
		this.issues = new ArrayList<Issue>();
		this.followingIssues = new TreeSet<Issue>();
		this.defaultViews = new ArrayList<View>();
	}

	public List<Project> getProjects() {
		return this.projects;
	}

	public String getName() {
		return name;
	}

	public String getFullname() {
		return fullname;
	}

	public boolean getAdmin() {
		return admin;
	}

	public boolean getValid() {
		return valid;
	}

	private void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setName(User source, String name) {
		if (!source.canEditUser(this)
				|| !ValidationUtils.validateLength(name, 1, 25))
			throw new IllegalArgumentException();
		this.name = name;
	}

	public void setFullname(User source, String fullname) {
		if (!source.canEditUser(this)
				|| !ValidationUtils.validateLength(fullname, 1, 25))
			throw new IllegalArgumentException();
		this.fullname = fullname;
	}

	public void setPassword(User source, String password) {
		if (!source.canEditUser(this)
				|| !ValidationUtils.validateLength(password, 1, 25))
			throw new IllegalArgumentException();
		this.password = password;
	}

	public void setEmail(User source, String email) {
		if (!source.canEditUser(this)
				|| !ValidationUtils.validateLength(email, 1, 50)
				|| !email.matches("[A-Za-z0-9\\._]+@[A-Za-z0-9]+(\\.[A-Za-z]{2,4}){1,2}"))
			throw new IllegalArgumentException();
		this.email = email;
	}

	public List<Issue> getIssues() {
		return issues;
	}

	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "User [admin=" + admin + ", name=" + name + ", password="
				+ password + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void addIssue(Issue issue) {
		this.issues.add(issue);
	}

	public void addProject(Project p) {
		this.projects.add(p);
	}

	public void removeProject(Project p) {
		this.projects.remove(p);
	}

	public boolean canAddProject() {
		return getAdmin();
	}

	public boolean canEditProject() {
		return getAdmin();
	}

	public boolean canRegisterUser() {
		return getAdmin();
	}

	public boolean canInvalidateUser() {
		return getAdmin();
	}

	public boolean canEditUser(User target) {
		return getAdmin() || this.equals(target);
	}

	public void invalidate(User source) {
		if (!source.canInvalidateUser() || isActive() || !getValid())
			throw new IllegalArgumentException();
		setValid(false);
	}

	public boolean isActive() {
		return getIssues().size() != 0;
	}

	public List<Issue> getIssues(Project project) {
		List<Issue> out = new ArrayList<Issue>();
		for (Issue i : issues)
			if (i.getProject().equals(project))
				out.add(i);
		return out;
	}

	public int compareTo(User user) {
		return this.getName().compareTo(user.getName());
	}

	public void followIssue(Issue i) {
		this.followingIssues.add(i);
	}

	public SortedSet<Issue> getFollowingIssues() {
		return followingIssues;
	}

	public void unfollowIssue(Issue issue) {
		this.followingIssues.remove(issue);
	}
	
	public List<View> getDefaultViews(){
		return defaultViews;
	}
	
	public void addDefaultView(View view){
		if (!defaultViews.contains(view))
			defaultViews.add(view);
	}
	
	public void setDefaultViews(List<View> views){
		defaultViews.retainAll(views);
		for (View v : views)
			if (!defaultViews.contains(v))
				defaultViews.add(v);
	}
}
