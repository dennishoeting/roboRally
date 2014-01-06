package prototyp.server.lib;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import prototyp.server.view.FrontPageImpl;
import prototyp.shared.exception.frontpage.PasswordGenerationException;

/**
 * Hilfsklasse zum Verschlüsseln.
 * 
 * @author Andreas
 * @version 1.0
 */
public class Encrypt {

	/**
	 * Wandelt Hex in Strings.
	 * 
	 * @param data
	 *            Daten, die in einen String umgewandelt werden sollen
	 * @return String
	 */
	private static String convertToString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buf.append(Integer.toHexString(data[i] & 0xff));
		}
		return buf.toString();
	}

	/**
	 * Benutzt von {@link FrontPageImpl}.
	 * 
	 * Generiert ein verschlüsseltes Passwort aus dem Nickname und dem
	 * eigentlich Passwort. Es wird der SHA-1 Algorithmus angewendet, sodass man
	 * aus dem verschlüsselten String nicht mehr den Klartext bekommt. Durch die
	 * Verwendung von Nickname und Passwort erhöht sich die Sicherheit, sodass
	 * es zu keinen ungewollten Kollisionen kommt.
	 * 
	 * @param nickname
	 *            Nickname des Nutzers
	 * @param password
	 *            Passwort des Nutzers im Klartext
	 * @return verschlüsselter String
	 * @throws PasswordGenerationException
	 *             Wird geworfen, wenn das Passwort nicht erstellt kann.
	 */
	public static String generatePassword(String nickname, String password)
			throws PasswordGenerationException {
		byte[] hashedBytes = null;
		try {
			// Erzeugen eines neuen MessageDigest-Objektes
			MessageDigest digest = MessageDigest.getInstance("SHA-1");

			digest.reset();
			// Hashwert berechnen
			byte[] temp = (nickname + password).getBytes("UTF-8");
			hashedBytes = digest.digest(temp);

			return convertToString(hashedBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new PasswordGenerationException();
		} catch (UnsupportedEncodingException e) {
			throw new PasswordGenerationException();
		}
	}
}
