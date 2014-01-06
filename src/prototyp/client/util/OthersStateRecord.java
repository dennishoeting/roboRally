package prototyp.client.util;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Record für das othersStateListGrid aus der RoundPlayerPage
 * 
 * @author dennis
 * @version 1.0
 */
public class OthersStateRecord extends ListGridRecord {

	/**
	 * Konstruktor
	 * 
	 * @param id
	 *            ID des Players
	 * @param iconPath
	 *            Pfad zum Icon des Players
	 * @param name
	 *            Name des Players
	 * @param numberOfLeftLifeToken
	 *            Anzahl der LifeToken des Players
	 * @param numberOfDamageToken
	 *            Anzahl der DamageToken des Players
	 */
	public OthersStateRecord(int id, String iconPath, String name) {

		/*
		 * Anfangs muss checkpoint "1" erreicht werden
		 */
		this.setAttribute("nextCheckpoint", "robotStatus/checkpoint_00.png");
		
		/*
		 * LifeToken
		 */
		for (int i = 1; i <= 3; i++) {
			this.setAttribute("lifeToken" + i, "robotStatus/lToken.png");
		}

		/*
		 * DamageToken
		 */
		for (int i = 1; i <= 10; i++) {
			this.setAttribute("damageToken" + i,
					"robotStatus/dTokenTemplate.png");
		}

		this.setAttribute("id", id);
		this.setAttribute("icon", iconPath);
		this.setAttribute("name", name);
	}

	/**
	 * Verringert die visualisierte Anzahl an LifeToken um 1
	 */
	public void clearLifeToken() {
		for (int i = 1; i <= 3; i++) {
			this.setAttribute("lifeToken" + i, "");
		}
	}
	
	/**
	 * setzt den nächsten Checkpoint
	 * @param next
	 */
	public void setNexCheckpoint(int next) {
		this.setAttribute("checkpoint", next);
	}

	public void setDead() {
		this.setAttribute("icon", "robotStatus/dead.png");
		this.setAttribute("nextCheckpoint", "");
		for (int i = 1; i <= 10; i++) {
			this.setAttribute("damageToken" + i, "");
		}
		for (int i = 1; i <= 3; i++) {
			this.setAttribute("lifeToken" + i, "");
		}
	}

	/**
	 * Verringert die visualisierte Anzahl an LifeToken um 1
	 */
	public void setNumberOfLifeToken(int lifeToken) {
		this.setAttribute("lifeToken" + lifeToken, "robotStatus/lTokenAway.png");
	}

	/**
	 * Aktualisiert die visualisierte Anzahl der DamageToken
	 * 
	 * @param newNumberOfDamageToken
	 *            neue Anzahl
	 */
	public void updateDamageToken(int newNumberOfDamageToken) {
		int i = 1;
		for (; i <= newNumberOfDamageToken; i++) {
			this.setAttribute("damageToken" + i, "robotStatus/dToken.png");
		}
		for (; i <= 10; i++) {
			this.setAttribute("damageToken" + i,
					"robotStatus/dTokenTemplate.png");
		}
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
