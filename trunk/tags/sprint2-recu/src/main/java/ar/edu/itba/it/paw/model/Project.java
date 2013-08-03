package ar.edu.itba.it.paw.model;

import java.util.ArrayList;
import java.util.List;

public class Project extends AbstractModel {

	private String code;
	private String name;
	private String description;
	private User leader;
	private List<User> users;
	private boolean isPublic;

	public Project(String code, String name, String description, User leader,
			boolean isPublic, List<User> users) {

		this.code = code;
		setName(name);
		setDescription(description);
		setLeader(leader);
		this.isPublic = isPublic;

		if (users == null)
			this.users = new ArrayList<User>();
		else
			this.users = users;
	}

	public boolean getIsPublic() {
		return isPublic;
	}

	public List<User> getUsers() {
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

	public void addUser(User user) {
		this.users.add(user);
	}

	public void removeUser(User user) {
		this.users.remove(user);
	}

	public void setName(String name) {
		if (!ValidationUtils.validateLength(name, 1, 25))
			throw new IllegalArgumentException();
		this.name = name;
	}
	
	public void setDescription(String description) {
		if (!ValidationUtils.validateMaxLength(description, 255))
			throw new IllegalArgumentException();
		this.description = description;
	}
	
	public void setIsPublic(boolean isPublic){
		this.isPublic = isPublic;
	}
	
	public void setLeader(User leader){
		if(leader == null)
			throw new IllegalArgumentException();
		this.leader = leader;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public void setUsers(List<User> users){
		this.users = users;
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

}
