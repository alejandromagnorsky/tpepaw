package ar.edu.itba.it.paw.model;

public class User extends AbstractModel implements Comparable<User> {

	private boolean admin;
	private boolean valid;
	private String name;
	private String password;
	private String fullname;

	public User(boolean admin, boolean valid, String name, String password,
			String fullname) {

		if (!ValidationUtils.validateLength(name, 1, 25)
				|| !ValidationUtils.validateLength(password, 1, 25)
				|| !ValidationUtils.validateLength(fullname, 1, 25))
			throw new IllegalArgumentException();

		this.admin = admin;
		this.valid = valid;
		this.name = name;
		this.password = password;
		this.fullname = fullname;
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

	// TODO SOLO EL DAO DEBE USARLO
	public String getPassword() {
		return password;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean verifyPassword(String password) {
		return this.password.equals(password);
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

	public int compareTo(User user) {
		return this.getName().compareTo(user.getName());
	}
}
