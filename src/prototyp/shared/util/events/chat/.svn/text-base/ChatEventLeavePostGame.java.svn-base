package prototyp.shared.util.events.chat;

import prototyp.client.view.Page;

/**
 * ChatEvent, wenn das PostGame geschlossen wird.
 * 
 * @author timo
 * @version 1.0
 */
public class ChatEventLeavePostGame extends ChatEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 1580078376075572478L;

	/** Nickname */
	private String nickname;

	/** Zeitangabe */
	private String time;

	/** Farbe */
	private String color;

	/**
	 * Default
	 */
	public ChatEventLeavePostGame() {

	}

	/**
	 * Normaler Konstruktor
	 * 
	 * @param nickname
	 *            Nickname
	 * @param time
	 *            Zeit
	 * @param color
	 *            Farbe
	 * @param sound
	 *            Dateiname
	 */
	public ChatEventLeavePostGame(String nickname, String time, String color, String sound) {
		super.sound = sound;
		this.nickname = nickname;
		this.time = time;
		this.color = color;
	}

	/**
	 * getMessage() überschreiben, um die Props zu nutzen
	 * 
	 * @return Chatnachricht
	 */
	@Override
	public String getMessage() {
		return "<strong><font color=" + this.color + ">" + this.time + " " + this.nickname + " "
				+ Page.props.chatPresenterLeavePostGame() + " </font></strong>";
	}
}
