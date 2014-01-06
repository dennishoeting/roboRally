package prototyp.server.lib;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Die Klasse ist für das Versenden von Emails zuständig. Sie legt den Host und
 * die Daten für die Authentifizierung fest.
 * 
 * @author Marina, Andreas
 * @version 1.0
 */
public class DeliverMail {
	private static final String SMTP_AUTH_PWD = "atlg34x";
	private static final String SMTP_AUTH_USER = "sp10g2";
	// Daten für die Authentifizierung
	private static final String SMTP_HOST_NAME = "taifun.informatik.uni-oldenburg.de";
	private static final int SMTP_PORT = 587;

	/**
	 * Verschickt eine E-Mail mit einem neu generiertem Passwort an die
	 * übergebene E-Mail Adresse.
	 * 
	 * @param recipient
	 *            E-Mail Adresse des Empfängers
	 * @param subject
	 *            Betreff der Email
	 * @param content
	 *            Inhalt der Email
	 */
	public void send(String recipient, String subject, String content)
			throws Exception {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtps.host", DeliverMail.SMTP_HOST_NAME);
		props.put("mail.smtps.auth", "true");

		Session mailSession = Session.getDefaultInstance(props);
		Transport transport = mailSession.getTransport();

		MimeMessage message = new MimeMessage(mailSession);
		// Alles was im Content stehen soll
		message.setContent(content, "text/html");
		// Betreff der E-Mail
		message.setSubject(subject);
		// Absender
		message.setFrom(new InternetAddress(
				"robocraft@informatik.uni-oldenburg.de"));
		// Empfänger
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				recipient));

		// Verbindung mit den Authentifizierungsdaten
		transport.connect(DeliverMail.SMTP_HOST_NAME, DeliverMail.SMTP_PORT,
				DeliverMail.SMTP_AUTH_USER, DeliverMail.SMTP_AUTH_PWD);
		// E-Mail verschicken
		transport.sendMessage(message,
				message.getRecipients(Message.RecipientType.TO));
		// Verbindung schließen
		transport.close();
	}
}
