package ar.edu.itba.it.paw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "systemuser")
public class User extends AbstractModel implements Comparable<User> {

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

	public User() {

	}

	public User(boolean admin, boolean valid, String name, String password,
			String fullname, String email) {

		if (!ValidationUtils.validateLength(name, 1, 25)
				|| !ValidationUtils.validateLength(password, 1, 25)
				|| !ValidationUtils.validateLength(fullname, 1, 25)
				|| !ValidationUtils.validateLength(email, 1, 50)
				|| !email
						.matches("[A-Za-z0-9\\._]+@[A-Za-z0-9]+(\\.[A-Za-z]{2,4}){1,2}"))
			throw new IllegalArgumentException();

		this.admin = admin;
		this.valid = valid;
		this.name = name;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.projects = new ArrayList<Project>();
		this.issues = new ArrayList<Issue>();
	}

	public List<Project> getProjects(){
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

	public void setValid(boolean valid) {
		this.valid = valid;
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
}
