package prototyp.shared.util;

import java.io.Serializable;

import prototyp.shared.field.Field;

/**
 * Informationen über Restartfelder der Spieler
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public class RestartFieldInfo implements Serializable {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -2859770979316561961L;

	/** die Id des Spielers */
	private int playerId;

	/** das RestartField des Spielers */
	private Field restartField;

	/**
	 * Default-Konstruktor
	 */
	public RestartFieldInfo() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId
	 * @param restartField
	 */
	public RestartFieldInfo(final int playerId, final Field restartField) {
		this.playerId = playerId;
		this.restartField = restartField;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + playerId;
		return result;
	}

	/**
	 * Überschreibt equals(Object obj)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RestartFieldInfo) {
			return this.playerId == ((RestartFieldInfo) obj).playerId;
		}
		return super.equals(obj);
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return this.playerId;
	}

	/**
	 * @return the restartField
	 */
	public Field getRestartField() {
		return this.restartField;
	}
}