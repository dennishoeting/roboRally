package prototyp.client.model.easterEggs;

import prototyp.client.service.ChatServiceAsync;
import prototyp.client.util.SoundManager;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.HTMLPane;

/**
 * EasterEgg: David-Guetta-Party-Chatter!
 * 
 * @author Dennis (Verantwortlicher), Andreas
 * @version 1.1
 */
public class DavidGuetta {
	/** Speicherung der ChatDomain */
	private static String domain;

	/** Speicherung des ChatService */
	private static ChatServiceAsync serviceProxy;

	/** Speicherung der TextArea */
	private static HTMLPane chatTextArea;

	/** Speicherung des UserName */
	private static String userName;

	/** Die Lyrics */
	private static String[] lyrics;

	/** Die Zeilendauern der Lyrics */
	private static int[] lyricDuration;

	/** Die Timer */
	private static Timer guettaSingTimer, guettaBlinkTimer;

	/** Hilfsvariable */
	private static int counter;

	/** Hilfsvariable */
	private static boolean run;

	static {
		/*
		 * Lyrics
		 */
		DavidGuetta.lyrics = new String[12];
		DavidGuetta.lyrics[0] = "All the things I know right now";
		DavidGuetta.lyrics[1] = "If I only knew back then";
		DavidGuetta.lyrics[2] = "There's no gettin' over";
		DavidGuetta.lyrics[3] = "No gettin over'";
		DavidGuetta.lyrics[4] = "There's just no getting over you.";
		DavidGuetta.lyrics[5] = "WISH I COULD SPIN MY WORLD INTO REVERSE...";
		DavidGuetta.lyrics[6] = "JUST TO HAVE YOU BACK AGAIN!!";
		DavidGuetta.lyrics[7] = "THERE'S NO GETTIN' OVER";
		DavidGuetta.lyrics[8] = "NO GETTIN' OVER!!!'";
		DavidGuetta.lyrics[9] = "THERE'S JUST NO GEEETTING OVER YOU!!!";
		DavidGuetta.lyrics[10] = "OOOH YEEEEEEEEEEEEEEEAHHH";
		DavidGuetta.lyrics[11] = "... jezz aber weiterspielen :-)";

		/*
		 * Zeilendauern
		 */
		DavidGuetta.lyricDuration = new int[12];
		DavidGuetta.lyricDuration[0] = 3200;
		DavidGuetta.lyricDuration[1] = 3000;
		DavidGuetta.lyricDuration[2] = 2000;
		DavidGuetta.lyricDuration[3] = 2000;
		DavidGuetta.lyricDuration[4] = 3750;
		DavidGuetta.lyricDuration[5] = 3500;
		DavidGuetta.lyricDuration[6] = 3500;
		DavidGuetta.lyricDuration[7] = 2000;
		DavidGuetta.lyricDuration[8] = 1500;
		DavidGuetta.lyricDuration[9] = 4000;
		DavidGuetta.lyricDuration[10] = 4000;
		DavidGuetta.lyricDuration[11] = 500;
	}

	/**
	 * Hier geschiets
	 * 
	 * @param serviceProxy
	 *            der ServiceProxy
	 * @param domain
	 *            die Domain
	 * @param chatTextArea
	 *            die TextArea
	 * @param userName
	 *            der UserName
	 */
	public static synchronized void doIt(final ChatServiceAsync serviceProxy,
			String domain, HTMLPane chatTextArea, String userName) {
		/*
		 * Attribute speichern
		 */
		DavidGuetta.domain = domain;
		DavidGuetta.serviceProxy = serviceProxy;
		DavidGuetta.chatTextArea = chatTextArea;
		DavidGuetta.userName = userName;

		/*
		 * Standardwerte
		 */
		DavidGuetta.counter = 0;

		/*
		 * Ankündigung: Einfache Nachricht in Chat schreiben
		 */
		DavidGuetta.serviceProxy.sendMessage(
				"<font color='#FF00FF'>Looks like <b>" + DavidGuetta.userName
						+ "</b> wants PARTY!!</font>", "David Guetta",
				DavidGuetta.domain, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) { /* nix */
					}

					@Override
					public void onSuccess(Boolean result) { /* nix */
					}
				});

		/*
		 * Singen: Timer
		 */
		DavidGuetta.guettaSingTimer = new Timer() {
			@Override
			public void run() {
				/*
				 * Falls noch Lyrics da, singe!
				 */
				if (DavidGuetta.counter < DavidGuetta.lyrics.length) {
					/*
					 * Bei Zeile 5 fang an zu blinken!!
					 */
					if (DavidGuetta.counter == 5) {
						DavidGuetta.guettaBlinkTimer.schedule(1);
					}

					/*
					 * Chat-Nachricht senden
					 */
					DavidGuetta.serviceProxy.sendMessage(
							"<font color='#FF00FF'>"
									+ DavidGuetta.lyrics[DavidGuetta.counter]
									+ "</font>", "David Guetta",
							DavidGuetta.domain, new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {
								}

								@Override
								public void onSuccess(Boolean result) {
								}
							});

					/*
					 * Nächste Zeile nach gegebener Zeit
					 */
					schedule(DavidGuetta.lyricDuration[DavidGuetta.counter]);

					/*
					 * Counter hoch
					 */
					DavidGuetta.counter++;
				}
				/*
				 * Ansonsten hör auf!
				 */
				else {
					DavidGuetta.run = false;
					DavidGuetta.guettaBlinkTimer.cancel();
					DavidGuetta.chatTextArea.setBackgroundColor(null);
					cancel();
				}
			}
		};

		/*
		 * Blinken: Timer
		 */
		DavidGuetta.guettaBlinkTimer = new Timer() {
			/*
			 * Wechsel-Flag
			 */
			boolean flag = true;

			@Override
			public void run() {
				if (this.flag) {
					/*
					 * Setze auf rot
					 */
					DavidGuetta.chatTextArea.setBackgroundColor("#FFCCCC");
				} else {
					/*
					 * Setze auf blau
					 */
					DavidGuetta.chatTextArea.setBackgroundColor("#CCCCFF");
				}

				/*
				 * Flag wechseln
				 */
				this.flag = !this.flag;

				/*
				 * Wiederhole nach 250ms
				 */
				schedule(250);
			}
		};

		/*
		 * Sound
		 */
		SoundManager soundManager = new SoundManager();
		soundManager.play("davidSound.mp3", new SoundHandler() {
			@Override
			public void onPlaybackComplete(PlaybackCompleteEvent event) {
			}

			@Override
			public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {
				if (event.getLoadState().compareTo(
						Sound.LoadState.LOAD_STATE_SUPPORTED_AND_READY) == 0) {
					DavidGuetta.run = true;
					DavidGuetta.guettaSingTimer.schedule(7000);
				}
			}
		});
	}

	/**
	 * True, falls David Guetta schon Stimmung macht.
	 * 
	 * @return true, falls PARTY
	 */
	public static boolean isRunning() {
		return DavidGuetta.run;
	}
}