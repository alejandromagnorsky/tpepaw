package ar.edu.itba.it.paw;

public class User {

	int id;
	private boolean admin;
	private boolean valid;
	private String name;
	private String password;
	private String fullname;
	
	
	public User(boolean admin, boolean valid, String name, String password, String fullname) {
		this.admin = admin;
		this.valid = valid;
		this.name = name;
		this.password = password;
		this.fullname = fullname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	//TODO SOLO EL DAO DEBE USARLO
	public String getPassword(){
		return password;
	}
	
	public void setValid(boolean valid){
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
}
