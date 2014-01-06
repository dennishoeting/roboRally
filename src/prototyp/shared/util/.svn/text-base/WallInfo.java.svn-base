package prototyp.shared.util;

import java.io.Serializable;

/**
 * Klasse, die Informationen zu den Wänden eines Feldes kapselt
 * @author Marcus
 * 
 */
public final class WallInfo implements Serializable {

	/**
	 * Seriennumemr
	 */
	private static final long serialVersionUID = -7201520087385011561L;

	/** boolean Flag für Wand Ost */
	private boolean wallEast;

	/** boolean Flag für Wand Nord */
	private boolean wallNorth;

	/** boolean Flag für Wand Süd */
	private boolean wallSouth;

	/** boolean Flag für Wand West */
	private boolean wallWest;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public WallInfo() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param wallNorth gibt an, ob im Norden eine Wand existert
	 * @param wallEast gibt an, ob im Osten eine Wand existert
	 * @param wallSouth gibt an, ob im Süden eine Wand existert
	 * @param WallWest gibt an, ob im Westen eine Wand existert
	 */
	public WallInfo(final boolean wallNorth, final boolean wallEast, final boolean wallSouth,
			final boolean wallWest) {
		this.wallNorth = wallNorth;
		this.wallEast = wallEast;
		this.wallSouth = wallSouth;
		this.wallWest = wallWest;
	}
	
	/**
	 * Konstruktor zur Erstellung einer WallInfo auf Grundlage einer anderen WallInfo
	 * Alle Informationen über Wände werden kopiert. Wenn "null" übergeben wird, wird eine 
	 * WallInfo ohne Wände erstellt.
	 * @param wallInfo
	 * 		die WallInfo, auf dessen Grundlage die Kopie erstellt wird
	 */
	public WallInfo(final WallInfo wallInfo) {
		
		if(wallInfo != null) {
			this.wallNorth = wallInfo.wallNorth;
			this.wallEast = wallInfo.wallEast;
			this.wallSouth = wallInfo.wallSouth;
			this.wallWest = wallInfo.wallWest;
		}
	}

	/**
	 * Vergleicht das WallInfo-Objekt mit einem anderen Objekt. Wenn das übergebene Objekt ein
	 * WallInfo-Objekt ist, wird "true" geliefert, falls die Wände in allen Himmelsrichtungen übereinstimmen.
	 * Ansonsten wird immer "false" geliefert
	 * @param obj das zu vergleichende Objekt
	 * @return true, das übergebende Objekt vom Typ "WallInfo" ist und 
	 * 		die Wände in allen Himmelsrichtungen übereinstimmen
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WallInfo) {
			final WallInfo wallInfo = (WallInfo) obj;
			return this.wallNorth == wallInfo.wallNorth
					&& this.wallEast == wallInfo.wallEast
					&& this.wallSouth == wallInfo.wallSouth
					&& this.wallWest == wallInfo.wallWest;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return 1 << (this.wallNorth ? 1 : 0) | 1 << (this.wallEast ? 2 : 0) | 1 << (this.wallSouth ? 3 : 0) | 1 << (this.wallWest ? 4 : 0);
	}

	/**
	 * Liefert die Anzahl der Wände, die sich auf dem Wandfeld befinden.
	 * 
	 * @return die Anzahl der Wände
	 */
	public int getNumberOfWalls() {
		return (this.wallNorth ? 1 : 0) + (this.wallEast ? 1 : 0)
				+ (this.wallSouth ? 1 : 0) + (this.wallWest ? 1 : 0);
	}

	/**
	 * @return the wallEast
	 */
	public boolean isWallEast() {
		return this.wallEast;
	}

	/**
	 * @return the wallNorth
	 */
	public boolean isWallNorth() {
		return this.wallNorth;
	}

	/**
	 * @return the wallSouth
	 */
	public boolean isWallSouth() {
		return this.wallSouth;
	}

	/**
	 * @return the wallWest
	 */
	public boolean isWallWest() {
		return this.wallWest;
	}

	/**
	 * @param wallEast
	 *            the wallEast to set
	 */
	public void setWallEast(boolean wallEast) {
		this.wallEast = wallEast;
	}

	/**
	 * @param wallNorth
	 *            the wallNorth to set
	 */
	public void setWallNorth(boolean wallNorth) {
		this.wallNorth = wallNorth;
	}

	/**
	 * @param wallSouth
	 *            the wallSouth to set
	 */
	public void setWallSouth(boolean wallSouth) {
		this.wallSouth = wallSouth;
	}

	/**
	 * @param wallWest
	 *            the wallWest to set
	 */
	public void setWallWest(boolean wallWest) {
		this.wallWest = wallWest;
	}

	@Override
	public String toString() {
		return (this.wallNorth ? "North, " : "")
				+ (this.wallEast ? "East, " : "")
				+ (this.wallSouth ? "South, " : "")
				+ (this.wallWest ? "West" : "");
	}
}
