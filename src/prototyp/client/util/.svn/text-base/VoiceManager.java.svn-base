package prototyp.client.util;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;

/**
 * Abstrakte Oberklasse der SoundManager und des MusicManager.
 * 
 * @author Andreas
 * @version 1.2
 * 
 * @see {@link MusicManager}, {@link SoundController}
 */
public abstract class VoiceManager {
	/** Seriennummer */
	private static final long serialVersionUID = 1117188998577100479L;

	/** Pfad zu den Sounddateien */
	public static final String SOUND_PATH = "sound/";

	/** SoundConroller */
	protected SoundController controller;

	/** Aktuell gespielte Sounddatei */
	protected Sound sound;

	/** Zur Zeit gespielte Datei */
	protected String soundfile;

	/** Angabe, ob der SoundManager Sounds abspielen darf */
	protected boolean canPlaySounds = true;

	/** Volume */
	protected int volume = 100;

	/**
	 * Konstruktor
	 */
	public VoiceManager() {
		this.controller = new SoundController();
		this.sound = null;
		this.soundfile = "";
	}

	/**
	 * Liefert den SoundController
	 * 
	 * @return controller
	 */
	public SoundController getController() {
		return this.controller;
	}

	/**
	 * Liefert den aktuell abgespielten Sound
	 * 
	 * @return sound
	 */
	public Sound getSound() {
		return this.sound;
	}

	/**
	 * Liefert die aktuelle Sounddatei
	 * 
	 * @return Dateiname der Sounddatei
	 */
	public String getSoundfile() {
		return this.soundfile;
	}

	/**
	 * Liefert die aktuell eingestellte Lautstärke
	 * 
	 * @return volume
	 */
	public int getVolume() {
		return this.volume;
	}

	/**
	 * Liefert die Angabe, ob der SoundManager Sounds abspielen darf
	 * 
	 * @return canPlaySounds
	 */
	public boolean isCanPlaySounds() {
		return this.canPlaySounds;
	}

	/**
	 * Spielt einen vorher geladenen/gestoppten Sound wieder ab.
	 */
	public void play() {
		if (this.sound != null) {
			this.sound.play();
		}
	}

	/**
	 * Spielt eine Datei ab
	 * 
	 * @param file
	 *            Dateiname der Sounddatei
	 */
	public abstract void play(String file);

	/**
	 * Bereitet die Sounddatei zum Abspielen vor
	 * 
	 * @param file
	 *            Dateiname der Sounddatei
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn das Format nicht passt.
	 */
	@SuppressWarnings("deprecation")
	protected void prepareSound(String file, SoundHandler handler) throws IllegalArgumentException {
		// Überprüfen, ob nicht schon die Datei geladen wurde
		if (this.sound == null || !file.equals(this.soundfile)) {
			this.sound = null;

			// Sound erstellen
			this.sound = this.controller.createSound(Sound.MIME_TYPE_AUDIO_MPEG, VoiceManager.SOUND_PATH + file, true);
			this.soundfile = file;

			// Lautstärke für den Sound setzen
			this.sound.setVolume(this.volume);

			// EventHandler setzen, wenn einer übergeben wird
			if (handler != null) {
				this.sound.addEventHandler(handler);
			}
		}
	}

	/**
	 * Setzt einen neuen Wert für die Angabe, ob der SoundManager Sounds abspielen darf.
	 * 
	 * @param canPlaySounds
	 *            true, wenn er Sounds abspielen darf
	 */
	public void setCanPlaySounds(boolean canPlaySounds) {
		this.canPlaySounds = canPlaySounds;

		if (!canPlaySounds && this.sound != null) {
			stop();
		} else if (canPlaySounds && this.sound != null) {
			this.play();
		}
	}

	/**
	 * Setzt einen neuen SoundController
	 * 
	 * @param controller
	 *            neuer SoundController
	 */
	public void setController(SoundController controller) {
		this.controller = controller;
	}

	/**
	 * Setzt eine neue Lautstärke. Läuft zur Zeit ein Sound, dann wird die gewählte Lautstärke angewendet.
	 * 
	 * @param volume
	 *            neue Lautstärke
	 */
	public void setVolume(int volume) {
		this.volume = volume % 101;

		// Ist ein Sound gerade erstellt?
		if (this.sound != null) {
			this.sound.setVolume(this.volume);

			// Default-Volume setzen
			this.controller.setDefaultVolume(this.volume);
		}
	}

	/**
	 * Stoppt einen Sound. Dieser bleibt aber noch geladen.
	 */
	public void stop() {
		if (this.sound != null) {
			this.sound.stop();
		}
	}

	/**
	 * Stoppt einen Sound und löscht ihn, sodass ein neues Sound abgespielt werden kann.
	 */
	public void stopAndClear() {
		if (this.sound != null) {
			// Stoppen
			this.sound.stop();

			// Löschen
			this.soundfile = "";
			this.sound = null;
		}
	}
}
