package prototyp.shared.useradministration;

import java.io.Serializable;

/**
 * AccountData eines Nutzers
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see {@link User}
 */
public class AccountData implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = 2548584938679259288L;

	/** Gibt an, ob der Spieler gesperrt ist */
	private boolean locked;

	private String nickname, password, email, firstname, surname;

	/** Zugehörige Statistic des Nutzers */
	private Statistic statistic;

	/**
	 * Default-Konstruktor
	 */
	public AccountData() {
		this.nickname = null;
		this.password = null;
		this.email = null;
		this.firstname = null;
		this.surname = null;
		this.locked = false;
		this.statistic = null;
	}

	/**
	 * Konstruktor
	 * 
	 * @param nickname
	 *            Nickname des Nutzers
	 * @param password
	 *            Passwort des Nutzers
	 * @param email
	 *            Emailadresse des Nutzers
	 * @param firstname
	 *            Vorname des Nutzers
	 * @param surname
	 *            Nachname des Nutzers
	 * @param locked
	 *            Gibt an, ob der Spieler gesperrt ist
	 * @param statistic
	 *            Zugehörige Statistic des Nutzers
	 */
	public AccountData(String nickname, String password, String email,
			String firstname, String surname, boolean locked,
			Statistic statistic) {
		this.nickname = nickname;
		this.password = password;
		this.email = email;
		this.firstname = firstname;
		this.surname = surname;
		this.locked = locked;
		this.statistic = statistic;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return this.firstname;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return this.nickname;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @return the statistic
	 */
	public Statistic getStatistic() {
		return this.statistic;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return this.surname;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return this.locked;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param firstname
	 *            the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @param nickname
	 *            the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param statistic
	 *            the statistic to set
	 */
	public void setStatistic(Statistic statistic) {
		this.statistic = statistic;
	}

	/**
	 * @param surname
	 *            the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

}
