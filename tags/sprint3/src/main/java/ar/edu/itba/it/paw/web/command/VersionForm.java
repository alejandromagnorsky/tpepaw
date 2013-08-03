package ar.edu.itba.it.paw.web.command;

import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import ar.edu.itba.it.paw.model.Version;
import ar.edu.itba.it.paw.model.Version.VersionState;

public class VersionForm {

	private String name;
	private String description;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private DateTime releaseDate;
	private VersionState state;

	private Version original;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(DateTime releaseDate) {
		this.releaseDate = releaseDate;
	}

	public VersionState getState() {
		return state;
	}

	public void setState(VersionState state) {
		this.state = state;
	}

	public Version build() {
		return new Version(name, description, releaseDate);
	}

	public void update() {
		original.setDescription(description);
		original.setName(name);
		original.setReleaseDate(releaseDate);
		original.setState(state);
	}

	public void setOriginal(Version original) {
		this.original = original;
	}

	public Version getOriginal() {
		return original;
	}
}
