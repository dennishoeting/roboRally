package prototyp.shared.util;

import java.io.Serializable;

/**
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public class LaserCannonInfo implements Serializable {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -2021094499389157312L;

	/** Anzahl der LaserKanonen Ost */
	private int cannonsEast;

	/** Anzahl der LaserKanonen Nord */
	private int cannonsNorth;

	/** Anzahl der LaserKanonen Süd */
	private int cannonsSouth;

	/** Anzahl der LaserKanonen West */
	private int cannonsWest;

	/**
	 * Default-Konstruktor für Serializable-Interface Setzt alle Laser
	 * standartmäßig auf 0
	 */
	public LaserCannonInfo() {
	}

	/**
	 * Kontruktor
	 * 
	 * @param cannonsNorth
	 *            Anzahl der Laser Nord
	 * @param cannonsEast
	 *            Anzahl der Laser Ost
	 * @param cannonsSouth
	 *            Anzahl der Laser S�d
	 * @param cannonsWest
	 *            Anzahl der Laser West
	 */
	public LaserCannonInfo(final int cannonsNorth, final int cannonsEast, final int cannonsSouth,
			final int cannonsWest) {
		this.cannonsNorth = cannonsNorth;
		this.cannonsEast = cannonsEast;
		this.cannonsSouth = cannonsSouth;
		this.cannonsWest = cannonsWest;
	}
	
	/**
	 * Konstruktor zur Erstellung einer LaserCannonInfo auf Grundlage einer anderen LaserCannonInfo
	 * Alle Informationen über Laserkanonen werden kopiert. Wenn "null" übergeben wird, wird eine 
	 * LaserCannonInfo ohne Laserkanonen erstellt.
	 * @param laserCannonInfo
	 * 		die LaserCannonInfo, auf dessen Grundlage die Kopie erstellt wird
	 */
	public LaserCannonInfo(final LaserCannonInfo laserCannonInfo) {
		if(laserCannonInfo != null) {
			this.cannonsNorth = laserCannonInfo.cannonsNorth;
			this.cannonsEast = laserCannonInfo.cannonsEast;
			this.cannonsSouth = laserCannonInfo.cannonsSouth;
			this.cannonsWest = laserCannonInfo.cannonsWest;
		}
	}

	/**
	 * Vergleicht das LaserCannonInfo-Objekt mit einem anderen Objekt. Wenn das übergebene Objekt ein
	 * LaserCannonInfo-Objekt ist, wird "true" geliefert, falls die Anzahl der Laserkanonen in allen Himmelsrichtungen übereinstimmen.
	 * Ansonsten wird immer "false" geliefert
	 * @param obj das zu vergleichende Objekt
	 * @return true, das übergebende Objekt vom Typ "LaserCannonInfo" ist und 
	 * 		die Anzahl der Laserkanonen in allen Himmelsrichtungen übereinstimmen
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LaserCannonInfo) {
			LaserCannonInfo laserCannonInfo = (LaserCannonInfo) obj;
			return this.cannonsNorth == laserCannonInfo.cannonsNorth
					&& this.cannonsEast == laserCannonInfo.cannonsEast
					&& this.cannonsSouth == laserCannonInfo.cannonsSouth
					&& this.cannonsWest == laserCannonInfo.cannonsWest;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return 1 << this.cannonsNorth | 1 << (this.cannonsEast+4) | 1 << (this.cannonsSouth+8) | 1 << (this.cannonsWest);
	}

	/**
	 * @return the cannonsEast
	 */
	public int getCannonsEast() {
		return this.cannonsEast;
	}

	/**
	 * @return the cannonsNorth
	 */
	public int getCannonsNorth() {
		return this.cannonsNorth;
	}

	/**
	 * @return the cannonsSouth
	 */
	public int getCannonsSouth() {
		return this.cannonsSouth;
	}

	/**
	 * @return the cannonsWest
	 */
	public int getCannonsWest() {
		return this.cannonsWest;
	}

	/**
	 * @param cannonsEast
	 *            the cannonsEast to set
	 */
	public void setCannonsEast(int cannonsEast) {
		this.cannonsEast = cannonsEast;
	}

	/**
	 * @param cannonsNorth
	 *            the cannonsNorth to set
	 */
	public void setCannonsNorth(int cannonsNorth) {
		this.cannonsNorth = cannonsNorth;
	}

	/**
	 * @param cannonsSouth
	 *            the cannonsSouth to set
	 */
	public void setCannonsSouth(int cannonsSouth) {
		this.cannonsSouth = cannonsSouth;
	}

	/**
	 * @param cannonsWest
	 *            the cannonsWest to set
	 */
	public void setCannonsWest(int cannonsWest) {
		this.cannonsWest = cannonsWest;
	}
	
	/**
	 * Liefert die Anzahl der Laserkanonen für die übergebene Richtung
	 * @param direction
	 * 		die Himmelsrichtung der zu ermittelnden Laserkasnonenanzahl
	 * @return
	 * 		die Anzahl der Laserkanonen für die übergebende Richtung
	 */
	public int getCannons(final Direction direction) {
		switch(direction) {
		case NORTH : return this.cannonsNorth;
		case EAST : return this.cannonsEast;
		case SOUTH : return this.cannonsSouth;
		default : return this.cannonsWest;
		}
	}
	
	/**
	 * Setzt die Anzahl der Laserkanonen für die übergebene Richtung
	 * @param direction
	 * 		die Himmelsrichtung für die zu setzende Laserkasnonenanzahl
	 * @param numberOfCannons
	 * 		die Anzahl der Laserkanonen für die übergebende Richtung
	 */
	public void setCannons(final Direction direction, final int numberOfCannons) {
		switch(direction) {
		case NORTH : this.cannonsNorth = numberOfCannons;
			break;
		case EAST : this.cannonsEast = numberOfCannons;
			break;
		case SOUTH : this.cannonsSouth = numberOfCannons;
			break;
		default : this.cannonsWest = numberOfCannons;
		}
		
	}

}
