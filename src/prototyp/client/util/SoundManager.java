package prototyp.client.util;

import com.allen_sauer.gwt.voices.client.handler.SoundHandler;

/**
 * SoundManager. Er spielt die Soundeffekte des Spiels ab. Diese Klasse besitzt
 * einen Default-SoundHandler, der nichts macht.
 * 
 * @author Andreas
 * @version 1.3
 * 
 * @see VoiceManager
 * 
 */
public class SoundManager extends VoiceManager {

	/**
	 * Spielt einen Sound mit den Default-Eigenschaften ab. Davor wird die Datei
	 * geladen.
	 * 
	 * @param file
	 *            Sounddatei
	 */
	@Override
	public void play(String file) {
		// Sound laden
		prepareSound(file, null);

		// Überprüfen, ob er Sounds abspielen darf.
		if (this.canPlaySounds) {
			// Abspielen
			this.sound.play();
		}
	}

	/**
	 * Spielt eine Sounddatei ab. Davor wird die Datei geladen und der
	 * übergebene Handler wird an den Sound angehängt.
	 * 
	 * @param file
	 *            Dateiname der Sounddatei
	 * @param handler
	 *            neuen SoundHandler für die Sound
	 * 
	 */
	public void play(String file, final SoundHandler handler)
			throws IllegalArgumentException {
		// Sound laden
		prepareSound(file, handler);

		// Überprüfen, ob er Sounds abspielen darf.
		if (this.canPlaySounds) {
			// Abspielen
			this.sound.play();
		}
	}

}
