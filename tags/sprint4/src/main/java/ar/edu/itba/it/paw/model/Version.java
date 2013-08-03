package ar.edu.itba.it.paw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "version")
public class Version extends AbstractModel implements Progress, Comparable<Version> {

	public enum VersionState {
		Open("Abierta"), Ongoing("En curso"), Released("Liberada");
		String caption;

		VersionState(String caption) {
			this.caption = caption;
		}

		public String getCaption() {
			return caption;
		}
	}

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	private String description;

	@Column(name = "release", nullable = false)
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime releaseDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private VersionState state;

	@ManyToMany
	@JoinTable(name = "affected_versions", joinColumns = @JoinColumn(name = "versionid"), inverseJoinColumns = @JoinColumn(name = "issueid"))
	private List<Issue> affectingIssues;

	@ManyToMany
	@JoinTable(name = "fixed_versions", joinColumns = @JoinColumn(name = "versionid"), inverseJoinColumns = @JoinColumn(name = "issueid"))
	private List<Issue> fixedIssues;

	Version() {
	}

	public Version(String name, String desc, DateTime release) {
		this.setName(name);
		this.setDescription(desc);
		this.setReleaseDate(release);
		this.state = VersionState.Open;
		this.affectingIssues = new ArrayList<Issue>();
		this.fixedIssues = new ArrayList<Issue>();
	}

	public void setName(String name) {
		if (!ValidationUtils.validateLength(name, 1, 25))
			throw new IllegalArgumentException();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		if (!ValidationUtils.validateMaxLength(description, 255))
			throw new IllegalArgumentException();
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setReleaseDate(DateTime releaseDate) {
		if (releaseDate == null)
			throw new IllegalArgumentException();
		this.releaseDate = releaseDate;
	}

	public DateTime getReleaseDate() {
		return releaseDate;
	}

	public void setState(VersionState state) {
		this.state = state;
	}

	public VersionState getState() {
		return state;
	}

	public List<Issue> getAffectingIssues() {
		return affectingIssues;
	}

	public List<Issue> getFixedIssues() {
		return fixedIssues;
	}

	public void addFixedIssue(User source, Issue issue) {
		if (!fixedIssues.contains(issue)) {
			this.fixedIssues.add(issue);
			issue.addFixedVersion(source, this);
		}
	}

	public void addAffectingIssue(User source, Issue issue) {
		if (!affectingIssues.contains(issue)) {
			this.affectingIssues.add(issue);
			issue.addAffectedVersion(source, this);
		}
	}

	public void removeAffectingIssue(User source, Issue issue) {
		if (affectingIssues.contains(issue)) {
			this.affectingIssues.remove(issue);
			issue.removeAffectedVersion(source, this);
		}
	}

	public void removeFixedIssue(User source, Issue issue) {
		if (fixedIssues.contains(issue)) {
			this.fixedIssues.remove(issue);
			issue.removeFixedVersion(source, this);
		}
	}

	public int getProgressPercentage() {
		int out = 0;

		for (Issue issue : fixedIssues)
			if (issue.getState() == Issue.State.Closed
					|| issue.getState() == Issue.State.Completed)
				out++;

		double tmp = ((double) out) / fixedIssues.size();
		return (int) (tmp * 100);
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
		Version other = (Version) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(Version arg0) {
		return this.name.compareTo(arg0.getName());
	}

}
