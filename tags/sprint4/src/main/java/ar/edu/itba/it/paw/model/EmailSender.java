package ar.edu.itba.it.paw.model;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	
	private static EmailSender emailSender;
	
	private String smtpHost;
	private String smtpPort;
	private String user;
	private String password;
	
	private EmailSender() throws IOException{
		Properties prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream("email.properties"));		
		smtpHost = prop.getProperty("smtpHost");
		smtpPort = prop.getProperty("smtpPort");
		user = prop.getProperty("user");
		password = prop.getProperty("password");
	}
	
	public static EmailSender getInstance() throws IOException{
		if(emailSender == null)
			emailSender = new EmailSender();
		return emailSender;
	}

	public void send(Email email) {
		try {
			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", smtpHost);
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", smtpPort);
			props.setProperty("mail.smtp.user", user);
			props.setProperty("mail.smtp.auth", "true");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);

			// Construimos el mensaje
			MimeMessage message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, email.getTo());
			message.setSubject(email.getSubject());
			message.setText(email.getBody());

			Transport t = session.getTransport("smtp");
			t.connect(user, password);
			t.sendMessage(message, message.getAllRecipients());

			t.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException("Error enviando el e-mail", e);
		}
	}
}
