package prototyp.client.util;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;

/**
 * 
 * Spielt die Musik des Spiels ab. Ist mit dem Single-Ton-Pattern realisiert
 * worden. Diese Klasse besitzt einen Default-SoundHandler, der die Sounddatei
 * im Loop abspielt.
 * 
 * @author Andreas
 * @version 1.0
 */
public class MusicManager extends VoiceManager {

	/** Instanz (Single-Ton) */
	private static MusicManager instance = null;

	// Default-SoundHandler erstellen
	private static SoundHandler DEFAULT_SOUNDHANDLER = new SoundHandler() {
		/**
		 * Wird ausgeführt, wenn die Datei abgespielt wurde
		 */
		@Override
		public void onPlaybackComplete(PlaybackCompleteEvent event) {
			((Sound) event.getSource()).play();
		}

		/**
		 * Sobald Musik geladen
		 */
		@Override
		public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {
			// Nichts
		}
	};

	/**
	 * Liefert die Instanz des MusicManagers
	 * 
	 * @return instance
	 */
	public static MusicManager getInstance() {
		if (MusicManager.instance == null) {
			MusicManager.instance = new MusicManager();
		}

		return MusicManager.instance;
	}

	/**
	 * privater Konstruktor
	 */
	private MusicManager() {
		this.volume = 50;
	}

	/**
	 * Nimmt die Default-Einstellungen zum Soundabspielen.
	 * 
	 * @param file
	 *            Dateiname der Sounddatei
	 */
	@Override
	public void play(String file) {
		// Sound laden
		prepareSound(file, MusicManager.DEFAULT_SOUNDHANDLER);

		// Überprüfen, ob er Sounds abspielen darf
		if (this.canPlaySounds) {

			// Abspielen
			this.sound.play();
		}
	}
}