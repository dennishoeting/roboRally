package prototyp.client.util;

import prototyp.client.view.administration.AdministrationPage;
import prototyp.shared.useradministration.User;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse für das ListGrid auf der AdministrationPage.
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see {@link AdministrationPage}
 */
public class UserListRecord extends ListGridRecord {

	/** Userobjekt */
	private User user;

	/**
	 * Konstruktor
	 * 
	 * @param user
	 *            Userobjekt
	 */
	public UserListRecord(User user) {
		this.user = user;
		this.setAttribute("userid", user.getId());
		this.setAttribute("email", user.getAccountData().getEmail());
		this.setAttribute("nickname", user.getAccountData().getNickname());
		if (user.getAccountData().isLocked()) {
			this.setAttribute("locked", "icons/lock.png");
		} else {
			this.setAttribute("locked", "");
		}

	}

	/**
	 * Liefert das zugehörige Userobjekt
	 * 
	 * @return user Userobjekt
	 */
	public User getUser() {
		return this.user;
	}
}
