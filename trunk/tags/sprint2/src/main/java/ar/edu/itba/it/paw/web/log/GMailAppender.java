package ar.edu.itba.it.paw.web.log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.apache.log4j.net.SMTPAppender;

public class GMailAppender extends SMTPAppender {
	
	private boolean startTLS = false;

	@Override
	protected Session createSession() {
		Properties props = null;
		try {
			props = new Properties(System.getProperties());
		} catch (SecurityException ex) {
			props = new Properties();
		}
		String prefix = "mail.smtp";
		if (this.getSMTPProtocol() != null) {
			props.put("mail.transport.protocol", this.getSMTPProtocol());
			prefix = "mail." + this.getSMTPProtocol();
		}

		if (getSMTPHost() != null)
			props.put(prefix + ".host", this.getSMTPHost());
		if (getSMTPPort() > 0)
			props.put(prefix + ".port", String.valueOf(this.getSMTPPort()));
		if (startTLS)
			props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = null;
		if (getSMTPPassword() != null && getSMTPUsername() != null) {
			props.put(prefix + ".auth", "true");
			auth = new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(getSMTPUsername(),
							getSMTPPassword());
				}

			};
		}
		Session session = Session.getInstance(props, auth);
		if (getSMTPProtocol() != null)
			session.setProtocolForAddress("rfc822", this.getSMTPProtocol());
		if (getSMTPDebug())
			session.setDebug(this.getSMTPDebug());
		return session;
	}

	public void setStartTLS(boolean startTLS) {
		this.startTLS = startTLS;
	}
}
