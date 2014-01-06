package prototyp.shared.field;

import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für Checkpointfelder
 * 
 * @author Marcus
 * 
 */
public class CheckpointField extends BackupField {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 1127431867661510099L;

	/** die Nummer des Checkpoints */
	private int numberOfCheckpoint;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public CheckpointField() {
	}

	/**
	 * Konstruktor für das erzeugen eines Checkpointfeldes
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param numberOfCheckpoint
	 *            Ist die Nummer des Checkpoints.
	 */
	public CheckpointField(WallInfo wallInfo, int numberOfCheckpoint) {
		super(wallInfo);
		this.numberOfCheckpoint = numberOfCheckpoint;
		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CheckpointField) {
			return super.equals(obj)
					&& this.numberOfCheckpoint == ((CheckpointField) obj).numberOfCheckpoint;
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		 return super.hashCode() + this.numberOfCheckpoint;
	}

	/**
	 * Diese Methode liefert die Nummer des Checkpoints.
	 * 
	 * @return die Nummer des Checkpoints
	 */
	public int getNumberOfCheckpoint() {
		return this.numberOfCheckpoint;
	}

	/**
	 * Diese Methode fügt der ImagePathList das entsprechnende
	 * Checkpointfeldbild und die Wandbilder hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		this.imagePathList.add(new DrawingInfo("images/fields/basicfield.png",
				50, 50, 0, 0));
		this.imagePathList.add(new DrawingInfo("images/fields/checkpoint_"
				+ this.numberOfCheckpoint + ".png", 50, 50, 0, 0));
		addWallsToImagePathList();
		return true;
	}
	
	/**
	 * Transformiert ein Checkpointfeld auf Grundlage der übergebenen Transformation
	 */
	@Override
	public Field transform(FieldTransformation transformation) {
		
		final boolean wallNorth = this.wallInfo.isWallNorth();
		final boolean wallEast = this.wallInfo.isWallEast();
		final boolean wallSouth = this.wallInfo.isWallSouth();
		final boolean wallWest = this.wallInfo.isWallWest();
		
		
		switch(transformation) {
		case X_AXIS : 
			return new CheckpointField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest), this.numberOfCheckpoint);
		case Y_AXIS : 
			return new CheckpointField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast), this.numberOfCheckpoint);
		case QUARTER_RIGHT :
			return new CheckpointField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth), this.numberOfCheckpoint);
		case QUARTER_LEFT :
			return new CheckpointField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth), this.numberOfCheckpoint);
		case HALF_RIGHT:
			return new CheckpointField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast), this.numberOfCheckpoint);
		}
		
		return null;
	}

}
