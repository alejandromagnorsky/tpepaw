package ar.edu.itba.it.paw;

public class PermissionManager {

	private static PermissionManager instance = new PermissionManager();

	public static PermissionManager getInstance() {
		return instance;
	}

	private PermissionManager() {
	}

	private static boolean hasAdminRights(User user) {
		if (user != null)
			return user.getAdmin();
		else
			return false;
	}

	private boolean hasLeaderRights(User user, Project project) {
		return project.getLeader().getName().equals(user.getName())
				|| hasAdminRights(user);
	}

	private boolean hasUserRights(User user) {
		return user != null;
	}

	public boolean canEditProject(User user) {
		return hasAdminRights(user);
	}

	public boolean canAddProject(User user) {
		return hasAdminRights(user);
	}

	public boolean canAddIssue(User user) {
		return hasUserRights(user);
	}

	public boolean canEditIssue(User user) {
		return hasUserRights(user);
	}

	public boolean canMarkIssue(User user) {
		return hasUserRights(user);
	}

	public boolean canCloseIssue(User user, Project project, Issue issue) {
		return hasLeaderRights(user, project)
				&& !issue.getState().equals(Issue.State.Closed);
	}

	public boolean canViewProject(User user, Project project) {
		return hasLeaderRights(user, project);
	}

	public boolean canAssignIssue(User user, Issue issue) {
		return hasUserRights(user)
				&& (issue.getAssignedUser() == null || !issue.getAssignedUser()
						.getName().equals(user.getName()));
	}

	public boolean canMarkIssueAsOpen(User user, Issue issue) {
		return hasUserRights(user)
				&& issue.getState().equals(Issue.State.Ongoing)
				&& issue.getAssignedUser() != null
				&& issue.getAssignedUser().getName().equals(user.getName());
	}

	public boolean canMarkIssueAsOngoing(User user, Issue issue) {
		return hasUserRights(user) && issue.getState().equals(Issue.State.Open)
				&& issue.getAssignedUser() != null
				&& issue.getAssignedUser().getName().equals(user.getName());
	}

	public boolean canResolveIssue(User user, Issue issue) {
		return hasUserRights(user)
				&& issue.getState().equals(Issue.State.Ongoing);
	}

	public boolean canInvalidateUser(User user) {
		return hasAdminRights(user);
	}

	public boolean canRegister(User user) {
		return hasAdminRights(user);
	}
}
