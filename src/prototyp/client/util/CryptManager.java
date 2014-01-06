package prototyp.client.util;

import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.googlecode.gwt.crypto.util.Str;

/**
 * Verschlüsselt clientseitig mit dem TripleDES-Algorithmus. Die Klasse ist nach dem SingleTon-Pattern realisiert.
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see FrontPagePresenter, Prototyp
 */
public class CryptManager {

	/** Schlüssel für die Verschlüsselung des Passworts */
	private static final byte[] ROBOCRAFT_KEY = Str.toBytes("robocraftfourever!passwd".toCharArray());

	/** Instanz des CryptManager */
	private static CryptManager instance = null;

	/** Cipher für den Algorithmus */
	private TripleDesCipher cipher;

	/**
	 * Privater Konstruktor
	 */
	private CryptManager() {
		cipher = new TripleDesCipher();
		cipher.setKey(ROBOCRAFT_KEY);
	}

	/**
	 * Liefert eine Instance des CryptManager
	 * 
	 * @return CryptManager
	 */
	public static CryptManager getInstance() {
		if (instance == null) {
			instance = new CryptManager();
		}

		return instance;
	}

	/**
	 * Verschlüsselt einen String
	 * 
	 * @param text
	 *            String, der verschlüsselt werden soll
	 * @return
	 */
	public String encrypt(String text) {
		if (text == null || text.equals("")) {
			return null;
		}

		try {
			return cipher.encrypt(text);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Entschlüsselt einen String
	 * 
	 * @param text
	 *            String, der entschlüsselt werden soll
	 * @return
	 */
	public String decrypt(String text) {
		if (text == null || text.equals("")) {
			return null;
		}

		try {
			return cipher.decrypt(text);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}
