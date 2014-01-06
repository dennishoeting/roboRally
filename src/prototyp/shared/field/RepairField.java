package prototyp.shared.field;

import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für Reparaturfelder
 * 
 * @author Marcus
 * 
 */
public final class RepairField extends BackupField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6022695372605807744L;

	/** Anzahl der Schraubenschlüssel auf dem Reparaturfeld */
	private int numberOfWrench;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public RepairField() {
	}

	/**
	 * Konstruktor fürs das Erzeugen eines Reparaturfeldes
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param numberOfWrench
	 *            Ist die Anzahl der Schraubenschlüssel auf dem Reparaturfeld.
	 */
	public RepairField(WallInfo wallInfo, int numberOfWrench) {
		super(wallInfo);
		this.numberOfWrench = numberOfWrench;

		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RepairField) {
			return super.equals(obj)
					&& this.numberOfWrench == ((RepairField) obj).numberOfWrench;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return super.hashCode() + this.numberOfWrench;
	}

	/**
	 * Diese Methode liefert die Anzahl der Schraubenschlüssel auf dem
	 * Reparaturfeld.
	 * 
	 * @return die Anzahl der Schraubenschlüssel auf dem Reparaturfeld
	 */
	public final int getNumberOfWrench() {
		return this.numberOfWrench;
	}

	/**
	 * Diese Methode fügt der ImagePathList das entsprechende Reparaturfeldbild
	 * und die Wandbilder hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		this.imagePathList.add(new DrawingInfo("images/fields/basicfield.png",
				50, 50, 0, 0));
		this.imagePathList.add(new DrawingInfo("images/fields/repair_"
				+ this.numberOfWrench + ".png", 50, 50, 0, 0));
		addWallsToImagePathList();
		return true;
	}
	
	/**
	 * Transformiert ein Reparaturfeld auf Grundlage der übergebenen Transformation
	 */
	@Override
	public Field transform(FieldTransformation transformation) {
		
		final boolean wallNorth = this.wallInfo.isWallNorth();
		final boolean wallEast = this.wallInfo.isWallEast();
		final boolean wallSouth = this.wallInfo.isWallSouth();
		final boolean wallWest = this.wallInfo.isWallWest();
		
		
		switch(transformation) {
		case X_AXIS : 
			return new RepairField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest), this.numberOfWrench);
		case Y_AXIS : 
			return new RepairField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast), this.numberOfWrench);
		case QUARTER_RIGHT :
			return new RepairField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth), this.numberOfWrench);
		case QUARTER_LEFT :
			return new RepairField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth), this.numberOfWrench);
		case HALF_RIGHT:
			return new RepairField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast), this.numberOfWrench);
		}
		
		return null;
	}

}
