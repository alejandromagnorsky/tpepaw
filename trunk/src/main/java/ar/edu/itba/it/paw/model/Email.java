package ar.edu.itba.it.paw.model;

import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Email {
	
	private InternetAddress[] to;
	private String subject;
	private String body;
	
	public Email(List<String> to, String subject, String body) throws AddressException{
		this.to = new InternetAddress[to.size()];
		for(int i = 0; i < to.size(); i++)
			this.to[i] = new InternetAddress(to.get(i));
		this.subject = subject;
		this.body = body;
	}

	public InternetAddress[] getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}
}
