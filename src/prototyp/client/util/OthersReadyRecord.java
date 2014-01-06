package prototyp.client.util;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Record für das othersReadyListGrid aus der RoundPlayerPage
 * 
 * @author dennis
 * @version 1.0
 */
public class OthersReadyRecord extends ListGridRecord {

	/**
	 * Konstruktor
	 * 
	 * @param id
	 *            ID des Players
	 * @param iconPath
	 *            Pfad zum Icon des Players
	 * @param name
	 *            Name des Players
	 * @param numberOfSetCards
	 *            Anzahl der gesetzten Karten des Players
	 * @param ready
	 *            true, falls Player ready
	 */
	public OthersReadyRecord(int id, String iconPath, String name) {

		/*
		 * Anfangs: keine Karte gelegt
		 */
		for (int i = 1; i <= 5; i++) {
			this.setAttribute("card" + i, "robotStatus/cardNotSet.png");
		}
		
		this.setAttribute("nextCheckpoint", "robotStatus/checkpoint_00.png");
		
		this.setAttribute("id", id);
		this.setAttribute("icon", iconPath);
		this.setAttribute("name", name);
		this.setAttribute("ready", "robotStatus/notReady.png"); // nicht Ready
	}

	/**
	 * Setzt die Anzahl der visualisierten gesetzten Karten wieder auf 0
	 */
	public void resetNumberOfSetCards() {

		for (int i = 1; i <= 5; i++) {
			this.setAttribute("card" + i, "robotStatus/cardNotSet.png");
		}
	}
	
	/**
	 * setzt den nächsten Checkpoint
	 * @param next
	 */
	public void setNexCheckpoint(int next) {
		this.setAttribute("checkpoint", next);
	}

	/**
	 * Zeigt die Karten im ReadyGridList
	 * @param slot 
	 * 		die Nummer des Kartenslots
	 * @param set 
	 * 		1 : Karte gesetzt
	 * 		2: Karte nicht gesetzt
	 * 		3: Karte gesperrt
	 */
	public void setCardSet(int slot, int set) {
		if (set == 0) {
			this.setAttribute("card" + slot, "robotStatus/cardSet.png");
		} else if (set == 1) {
			this.setAttribute("card" + slot, "robotStatus/cardNotSet.png");
		} else {
			this.setAttribute("card" + slot, "robotStatus/cardLocked.png");
		}
	}

	/**
	 * Markiert den Spieler als gestorben im Listgrid
	 */
	public void setDead() {
		this.setAttribute("icon", "robotStatus/dead.png");
		this.setAttribute("nextCheckpoint", "");
		setPowerDown();
	}

	/**
	 * Setzt den Hacken hinter den Player.
	 */
	public void setIsReady(boolean ready) {
		if (ready) {
			this.setAttribute("ready", "robotStatus/ready.png");
		} else {
			this.setAttribute("ready", "robotStatus/notReady.png");
		}
	}

	/**
	 * Setzt den Spieler PowerDown im Listgrid
	 */
	public void setPowerDown() {
		for (int i = 1; i <= 5; i++) {
			this.setAttribute("card" + i, "");
		}
		this.setAttribute("ready", "");
	}
	
	/**
	 * 
	 * @param newCheckpoint
	 * 		die neue erreichte Checkpointnummer
	 */
	public void setNewCheckpoint(final int newCheckpoint) {
		this.setAttribute("nextCheckpoint", "robotStatus/checkpoint_0" +  (newCheckpoint+1)  + ".png");
	}
}
