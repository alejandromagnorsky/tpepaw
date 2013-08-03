package ar.edu.itba.it.paw.web.command;

import java.io.IOException;

import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.it.paw.model.Issue;
import ar.edu.itba.it.paw.model.IssueFile;
import ar.edu.itba.it.paw.model.User;

public class IssueFileForm {
	private Issue issue;
	private User uploader;
	private MultipartFile file;
	
	public Issue getIssue() {
		return issue;
	}
	
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	public User getUploader(){
		return uploader;
	}
	
	public void setUploader(User uploader){
		this.uploader = uploader;
	}
	
	public MultipartFile getFile() {
		return file;
	}
	
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	public IssueFile build(){
		try {
			return new IssueFile(	this.issue, this.file.getBytes(), 
									file.getOriginalFilename(), file.getSize(), 
									new DateTime(), this.uploader);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
