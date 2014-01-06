package prototyp.shared.field;

import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.WallInfo;

/**
 * Abstrakte Klasse für ein Feld, das Laserkanonen hat.
 * 
 * @author Marcus
 * 
 */
public abstract class LaserCannonField extends WallField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 952791455858020258L;

	/** LaserInfo speichert Informationen über WandLaser von Laserfeldern */
	protected LaserCannonInfo laserCannonInfo;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public LaserCannonField() {
	}

	/**
	 * Konstruktor für ein LaserCannonField
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param laserCannonInfo
	 *            Speichert Informationen zu Laserkanonen.
	 *            LaserCannonInfo-Instanz wird mit "new" erzeugt.
	 */
	public LaserCannonField(WallInfo wallInfo, LaserCannonInfo laserCannonInfo) {
		super(wallInfo);
		this.laserCannonInfo = laserCannonInfo;

		/*
		 * Wandlaser mit Wänden vergleichen
		 */
		if (this.laserCannonInfo.getCannonsNorth() > 0) {
			this.wallInfo.setWallNorth(true);
		}
		if (this.laserCannonInfo.getCannonsEast() > 0) {
			this.wallInfo.setWallEast(true);
		}
		if (this.laserCannonInfo.getCannonsSouth() > 0) {
			this.wallInfo.setWallSouth(true);
		}
		if (this.laserCannonInfo.getCannonsWest() > 0) {
			this.wallInfo.setWallWest(true);
		}
	}

	/**
	 * Füge LaserCannons hinzu
	 * 
	 * @param direction
	 *            Richtig
	 * @param numberOflaserCannons
	 *            Anzahl der Laser
	 * @return String[]
	 */
	public final String[] addLaserCannons(Direction direction,
			int numberOflaserCannons) {
		switch (direction) {
		case NORTH:
			this.laserCannonInfo.setCannonsNorth(numberOflaserCannons);
			break;
		case EAST:
			this.laserCannonInfo.setCannonsEast(numberOflaserCannons);
			break;
		case SOUTH:
			this.laserCannonInfo.setCannonsSouth(numberOflaserCannons);
			break;
		case WEST:
			this.laserCannonInfo.setCannonsWest(numberOflaserCannons);
			break;
		}

		final String removed = removePath(direction.name().toLowerCase());
		final String added = "images/fields/" + numberOflaserCannons
				+ "_laser_canon_" + direction.name().toLowerCase() + ".png";
		this.imagePathList.add(new DrawingInfo(added, 50, 50, 0, 0));
		return new String[] { added, removed };
	}

	/**
	 * Fügt der ImagePathList die LaserKanonen hinzu.
	 * 
	 * @return immer true
	 */
	protected final boolean addLaserCannonsToImagePathList() {
		if (this.laserCannonInfo.getCannonsNorth() > 0) {
			this.imagePathList.add(new DrawingInfo("images/fields/"
					+ this.laserCannonInfo.getCannonsNorth()
					+ "_laser_canon_north.png", 50, 50, 0, 0));
		}
		if (this.laserCannonInfo.getCannonsEast() > 0) {
			this.imagePathList.add(new DrawingInfo("images/fields/"
					+ this.laserCannonInfo.getCannonsEast()
					+ "_laser_canon_east.png", 50, 50, 0, 0));
		}
		if (this.laserCannonInfo.getCannonsSouth() > 0) {
			this.imagePathList.add(new DrawingInfo("images/fields/"
					+ this.laserCannonInfo.getCannonsSouth()
					+ "_laser_canon_south.png", 50, 50, 0, 0));
		}
		if (this.laserCannonInfo.getCannonsWest() > 0) {
			this.imagePathList.add(new DrawingInfo("images/fields/"
					+ this.laserCannonInfo.getCannonsWest()
					+ "_laser_canon_west.png", 50, 50, 0, 0));
		}

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LaserCannonField) {
			return super.equals(obj)
					&& this.laserCannonInfo
							.equals(((LaserCannonField) obj).laserCannonInfo);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return super.hashCode() + this.laserCannonInfo.hashCode();
	}

	/**
	 * Liefert Informationen, ob, in welchen Himmelsrichtungen und wie viele
	 * Laserkanonen sich auf einem LaserCannonField befinden. Mit
	 * Instanzmethoden können auf die entsprechenden Informationen zugegriffen
	 * werden.
	 * 
	 * @return Info über Laserkanonen
	 */
	public final LaserCannonInfo getLaserCannonInfo() {
		return this.laserCannonInfo;
	}

	/**
	 * gibt an, ob das Feld über mindestens eine Laserkanone verfügt.
	 * 
	 * @return true, wenn das Feld mindestens eine laserkanone besitzt
	 */
	public final boolean hasLaserCannons() {
		return this.laserCannonInfo.getCannonsNorth() > 0
				|| this.laserCannonInfo.getCannonsEast() > 0
				|| this.laserCannonInfo.getCannonsSouth() > 0
				|| this.laserCannonInfo.getCannonsWest() > 0;
	}

	/**
	 * Löscht die Laserkanonen
	 * 
	 * @param direction
	 *            Richtung
	 * @return Pfad
	 */
	public final String removeLaserCannons(Direction direction) {
		switch (direction) {
		case NORTH:
			this.laserCannonInfo.setCannonsNorth(0);
			break;
		case EAST:
			this.laserCannonInfo.setCannonsEast(0);
			break;
		case SOUTH:
			this.laserCannonInfo.setCannonsSouth(0);
			break;
		case WEST:
			this.laserCannonInfo.setCannonsWest(0);
			break;
		}

		return removePath(direction.name().toLowerCase());
	}

	private final String removePath(final String subSequence) {
		for (DrawingInfo info : this.imagePathList) {
			String str = info.getImageFileName();
			if (str.contains("_laser_canon_" + subSequence + ".png")) {
				this.imagePathList.remove(info);
				return str;
			}
		}
		return "";
	}
}
