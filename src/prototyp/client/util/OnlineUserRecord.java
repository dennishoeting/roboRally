package prototyp.client.util;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Für das ListGrid in der Round.
 * 
 * @author Timo
 * 
 */
public class OnlineUserRecord extends ListGridRecord {
	public OnlineUserRecord(int key, String nickname) {
		setAttribute("userID", key);
		setAttribute("nickname", nickname);
	}
}
