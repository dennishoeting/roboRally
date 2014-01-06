package prototyp.shared.util.events.chat;

import prototyp.client.view.Page;

/**
 * Chatnachricht, wenn das Spiel verlassen wird. Als extra Klasse wegen den Properties.
 * 
 * @author timo
 * @version 1.0
 */
public class ChatEventLeaveGame extends ChatEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 2956708376635903078L;

	/** Nickname */
	private String nickname;

	/** Zeitangabe */
	private String time;

	/** Farbe */
	private String color;

	/**
	 * Default
	 */
	public ChatEventLeaveGame() {
		super();
		this.nickname = null;
		this.time = null;
		this.color = null;
	}

	/**
	 * Konstruktor
	 * 
	 * @param nickname
	 *            Nickname
	 * @param time
	 *            Zeitangabe
	 * @param color
	 *            Farbe
	 * @param sound
	 *            Sounddatei
	 */
	public ChatEventLeaveGame(String nickname, String time, String color, String sound) {
		super.sound = sound;
		this.nickname = nickname;
		this.time = time;
		this.color = color;
	}

	/**
	 * Liefert die Chatnachricht
	 * 
	 * @return Nachricht
	 */
	@Override
	public String getMessage() {
		return "<strong><font color=" + this.color + ">" + this.time + " " + this.nickname + " "
				+ Page.props.chatPresenterLeaveGame() + " </font></strong>";
	}

}
