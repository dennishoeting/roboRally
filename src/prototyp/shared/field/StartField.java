package prototyp.shared.field;

import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.RobotMovement;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für Startfelder
 * 
 * @author Marcus
 * 
 */
public class StartField extends BackupField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7760914335801506714L;

	/** die Startnummer des Startfeldes */
	private int startNumber;

	/**
	 * die Richtug, in der neu mit der der Roboter respawnt standartmäßig auf
	 * South
	 */
	protected Direction respawnDirection = Direction.SOUTH;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public StartField() {
	}

	/**
	 * Konstruktor für das Erzeugen eines Startfeldes
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param x
	 *            Ist der X-Index des Feldes auf dem Spielbrett.
	 * @param y
	 *            Ist der Y-Index des Feldes auf dem Spielbrett.
	 * @param startNumber
	 *            Ist die Startnummer des Feldes.
	 */
	public StartField(WallInfo wallInfo, int startNumber) {
		super(wallInfo);
		this.startNumber = startNumber;
		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StartField) {
			return super.equals(obj)
					&& this.startNumber == ((StartField) obj).startNumber
					&& this.respawnDirection == ((StartField) obj).respawnDirection;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return super.hashCode() + this.startNumber;
	}

	/**
	 * @return the respawnDirection
	 */
	public Direction getRespawnDirection() {
		return this.respawnDirection;
	}

	/**
	 * Liefert die StartNummer des Startfeldes.
	 * 
	 * @return die Startnummer des Startfeldes
	 */
	public int getStartNumber() {
		return this.startNumber;
	}

	/**
	 * Diese Methode fügt der ImagePathList das entsprechnende Startfeldbild und
	 * die Wandbilder hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		this.imagePathList.add(new DrawingInfo("images/fields/basicfield.png",
				50, 50, 0, 0));
		this.imagePathList.add(new DrawingInfo("images/fields/startfield_"
				+ this.startNumber + ".png", 50, 50, 0, 0));
		addWallsToImagePathList();
		return true;
	}

	/**
	 * @param respawnDirection
	 *            the respawnDirection to set
	 */
	public void setRespawnDirection(Direction respawnDirection) {
		this.respawnDirection = respawnDirection;
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

		final StartField field;
		switch(transformation) {
		case X_AXIS : 
			field = new StartField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest), this.startNumber);
			if(this.respawnDirection == Direction.NORTH) {
				field.setRespawnDirection(Direction.SOUTH);
			} else if(this.respawnDirection == Direction.SOUTH) {
				field.setRespawnDirection(Direction.NORTH);
			} else {
				field.setRespawnDirection(this.respawnDirection);
			}
			return field;
		case Y_AXIS : 
			field = new StartField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast), this.startNumber);
			if(this.respawnDirection == Direction.EAST) {
				field.setRespawnDirection(Direction.WEST);
			} else if(this.respawnDirection == Direction.WEST) {
				field.setRespawnDirection(Direction.EAST);
			} else {
				field.setRespawnDirection(this.respawnDirection);
			}
			return field;
		case QUARTER_RIGHT :
			field = new StartField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth), this.startNumber);
			field.setRespawnDirection(Direction.getDirection(this.respawnDirection, RobotMovement.TURN_RIGHT));
			return field;
		case QUARTER_LEFT :
			field = new StartField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth), this.startNumber);
			field.setRespawnDirection(Direction.getDirection(this.respawnDirection, RobotMovement.TURN_LEFT));
			return field;
		case HALF_RIGHT:
			field = new StartField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast), this.startNumber);
			field.setRespawnDirection(Direction.getDirection(this.respawnDirection, RobotMovement.TURN_AROUND));
			return field;
		}
		
		return null;
	}

}
