package ar.edu.itba.it.paw;

public class Project {

	private int id;
	private String code;
	private String name;
	private String description;
	private User leader;
	boolean isPublic;

	public Project(String code, String name, String description, User leader,
			boolean isPublic) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.leader = leader;
		this.isPublic = isPublic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getIsPublic() {
		return isPublic;
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
}
