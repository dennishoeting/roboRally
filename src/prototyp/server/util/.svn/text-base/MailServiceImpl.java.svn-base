package prototyp.server.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.server.lib.DeliverMail;
import prototyp.shared.exception.mail.AccountLockedException;
import prototyp.shared.exception.mail.MailNotFoundException;

/**
 * Mailfunktionen
 * 
 * @author Andreas
 * @version 1.0
 * 
 */
public class MailServiceImpl {

	/**
	 * Prüft die Methode DeliverMail auf Fehler.
	 * 
	 * @param recipient
	 *            E-Mailadresse des Empfängers
	 * @param subject
	 *            Titel der Nachricht
	 * @param content
	 *            Inhalt der Nachricht
	 * @param validate
	 *            Gibt an, ob die Email noch validiert werden soll
	 * @return Integer 1: E-Mail wurde versandt 2: E-Mail konnte nicht versandt
	 *         werden
	 * @throws AccountLockedException
	 *             Wird geworfen, wenn der Nutzer gesperrt ist
	 * @throws MailNotFoundException
	 *             Wird geworfen, wenn die E-Mailadresse nicht in der Datenbank
	 *             existiert
	 */
	public static boolean sendEMail(String recipient, String subject,
			String content, boolean validate) throws MailNotFoundException,
			AccountLockedException {
		if (validate && !validateEMail(recipient)) {
			return false; // E-Mail wird nicht akzeptiert
		}

		try {
			new DeliverMail().send(recipient, subject, content);
			return true; // E-Mail wurde versandt
		} catch (Exception e) {
			return false; // E-Mail wurde nicht versandt
		}
	}

	/**
	 * Überprüft die Benutzereingaben. Emailadresse wird mit der Datenbank
	 * abgeglichen und ein Code als String zurückgegeben.
	 * 
	 * @param recipient
	 *            E-Mail Adresse
	 * @return true, wenn die Emailadresse akzeptiert wurde. False, wenn die
	 *         Emailadresse nicht existiert.
	 * @throws AccountLockedException
	 *             Wird geworfen, wenn der Nutzer gesperrt ist
	 * @throws MailNotFoundException
	 *             Wird geworfen, wenn die E-Mailadresse nicht in der Datenbank
	 *             existiert
	 * 
	 * @see MailServiceImpl#sendEMail(String, String, String)
	 */
	public static boolean validateEMail(final String recipient)
			throws AccountLockedException, MailNotFoundException {
		try {
			// PreparedStatement vorbereiten
			PreparedStatement eMailStmt = DBConnection
					.getPstmt(DBStatements.SELECT_EMAIL_AND_LOCKED_BY_EMAIL
							.getStmt());
			eMailStmt.setString(1, recipient); // E-Mail an das Statement binden
			ResultSet eMailResult = eMailStmt.executeQuery(); // Statement
																// ausführen

			if (eMailResult.next()) {
				if (eMailResult.getInt("locked") == 1) {
					throw new AccountLockedException();
				}
			} else {
				throw new MailNotFoundException();
			}

			eMailResult.close(); // Result schließen

		} catch (SQLException e) {
			return false;
		}

		return true;
	}
}
