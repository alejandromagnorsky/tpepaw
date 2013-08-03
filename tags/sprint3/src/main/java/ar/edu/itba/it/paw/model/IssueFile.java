package ar.edu.itba.it.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "file")
public class IssueFile extends AbstractModel implements Comparable<IssueFile> {
	
	@ManyToOne
	@JoinColumn(name = "issueid", nullable = false)
	private Issue issue;

	@Lob
	@Column(name="file", nullable = false)
	private byte[] file;
	
	@Column(name="filename", nullable = false)
	private String filename;
	
	@Column(name="size", nullable = false)
	private long size;
	
	@Column(name="uploaddate", nullable = false)
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime uploadDate;
	
	@ManyToOne
	@JoinColumn(name = "uploader", nullable = false)
	private User uploader;
	
	public IssueFile(){
		
	}
	
	public IssueFile(Issue issue, byte[] file, String filename, long size, DateTime uploadDate, User uploader){
		if(issue == null || filename == null || uploadDate == null || uploader == null)
			throw new IllegalArgumentException();
		this.issue = issue;
		this.file = file;
		this.filename = filename;
		this.size = size;
		this.uploadDate = uploadDate;
		this.uploader = uploader;
		issue.addIssueFile(uploader, this);
	}
	
	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		if(issue != null)
			issue.addIssueFile(uploader, this);
		else
			this.issue.deleteIssueFile(uploader, this);
		this.issue = issue;
	}

	public byte[] getFile() {
		return file;
	}

	public String getFilename() {
		return filename;
	}

	public long getSize() {
		return size;
	}
	
	public float getSizeInKilobytes(){
		Float ans = (float)size / 1024;
		return  (float)Math.round(ans * 100) / 100;
	}

	public DateTime getUploadDate() {
		return uploadDate;
	}

	public User getUploader() {
		return uploader;
	}

	public int compareTo(IssueFile o) {
		return uploadDate.compareTo(o.getUploadDate());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (size ^ (size >>> 32));
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
		IssueFile other = (IssueFile) obj;
		if (this.getId() != other.getId())
			return false;
		return true;
	}
}
