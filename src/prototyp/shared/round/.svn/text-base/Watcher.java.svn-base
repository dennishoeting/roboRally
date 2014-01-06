package prototyp.shared.round;

import java.io.Serializable;

import prototyp.shared.useradministration.User;

/**
 * Der Beobachter in einer Spielrunde.
 * 
 * @author Marcus, Andreas
 * @version 1.0
 * @version 1.1 (23.9.10) ready-Funktionalität in den Player verschoben, es
 *          macht wenig Sinn, auf den Watcher zu warten --Jannik
 */
public class Watcher implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = -4115716401293991928L;

	/** Zugehöriger Nutzer */
	private User user;

	/**
	 * Default-Konstruktor
	 */
	public Watcher() {
		this.user = null;
	}

	/**
	 * Konstruktor
	 * 
	 * @param user
	 *            Zugehöriger Nutzer
	 */
	public Watcher(User user) {
		this.user = user;
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
		Watcher other = (Watcher) obj;
		if (this.user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!this.user.equals(other.user)) {
			return false;
		}

		return true;
	}

	/**
	 * Liefert den zugehörigen Nutzer
	 * 
	 * @return User
	 */
	public User getUser() {
		return this.user;
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
		result = prime * result
				+ (this.user == null ? 0 : this.user.hashCode());
		return result;
	}

	/**
	 * Setzt einen neuen Nutzer
	 * 
	 * @param user
	 *            Neuer zugehöriger Nutzer
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
