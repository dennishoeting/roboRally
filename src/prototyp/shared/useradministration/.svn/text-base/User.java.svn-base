package prototyp.shared.useradministration;

import java.io.Serializable;

import prototyp.client.view.administration.AdministrationPage;

/**
 * User
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see {@link AccountData}
 */
public class User implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = -7534744798020773326L;

	/** Zugehörige AccountData des Nutzers */
	private AccountData accountData;

	/** Angabe, ob der Nutzer Adminrechte besitzt */
	private boolean admin;

	/** DatenbankID */
	private int id;

	/** Zeitstempel, wann sich der Nutzer eingeloggt hat */
	private int loginDate;

	/**
	 * Default-Konstruktor
	 */
	public User() {
		this.id = 0;
		this.loginDate = 0;
		this.admin = false;
		this.accountData = null;
	}

	/**
	 * Konstruktor zum Editieren von Nutzern. Wird bei der
	 * {@link AdministrationPage} benötigt.
	 * 
	 * @param id
	 *            DatenbankID des Nutzers
	 * @param accountData
	 *            Zugehörige AccountData des Nutzers
	 */
	public User(int id, AccountData accountData) {
		this.id = id;
		this.loginDate = 0;
		this.admin = false;
		this.accountData = accountData;
	}

	/**
	 * Konstruktor
	 * 
	 * @param id
	 *            DatenbankID
	 * @param loginDate
	 *            Zeitstempel, wann sich der Nutzer eingeloggt hat
	 * @param admin
	 *            Angabe, ob der Nutzer Adminrechte besitzt
	 * @param accountData
	 *            Zugehörige AccountData des Nutzers
	 */
	public User(int id, int loginDate, boolean admin, AccountData accountData,
			Statistic statistic) {
		this.id = id;
		this.loginDate = loginDate;
		this.admin = admin;
		this.accountData = accountData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	/**
	 * @return the accountData
	 */
	public AccountData getAccountData() {
		return this.accountData;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the loginDate
	 */
	public int getLoginDate() {
		return this.loginDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		return result;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return this.admin;
	}

	/**
	 * @param accountData
	 *            the accountData to set
	 */
	public void setAccountData(AccountData accountData) {
		this.accountData = accountData;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param loginDate
	 *            the loginDate to set
	 */
	public void setLoginDate(int loginDate) {
		this.loginDate = loginDate;
	}
}
