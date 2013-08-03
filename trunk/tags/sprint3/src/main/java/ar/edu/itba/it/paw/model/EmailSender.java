package ar.edu.itba.it.paw.model;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class EmailSender {

	private static final String SMTPHost = "smtp.gmail.com";
	private static final String SMTPPort = "25";
	private static final String USER = "nicosogtulakk@gmail.com";
	private static final String PASSWORD = "abc123///";

	public static void send(Email email) {
		try {
			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", SMTPHost);
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", SMTPPort);
			props.setProperty("mail.smtp.user", USER);
			props.setProperty("mail.smtp.auth", "true");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);

			// Construimos el mensaje
			MimeMessage message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO, email.getTo());
			message.setSubject(email.getSubject());
			message.setText(email.getBody());

			Transport t = session.getTransport("smtp");
			t.connect(USER, PASSWORD);
			t.sendMessage(message, message.getAllRecipients());

			t.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException("Error enviando el e-mail", e);
		}
	}
}
