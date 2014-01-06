package prototyp.shared.field;

import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.RobotMovement;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für Pressenfeld
 * 
 * @author Marcus
 * 
 */
public class CompactorField extends DestroyerField {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -5011908513491671919L;

	/**
	 * gibt an, ob die Presse nur beim ersten und fünften Spielschritt eines
	 * Spielzugs ihre Aktion ausführt
	 */
	private boolean activeInFirstAndFifth;
	
	
	/** das Bild, wenn das FörderBand Aktiv ist */
	private String imagePathOn;

	/** das Bild für das Aus-Bild */
	private String imagePathOff;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public CompactorField() {
	}

	/**
	 * Konstruktor für ein Pressenfeld
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param laserCannonInfo
	 *            Speichert Informationen zu Laserkanonen.
	 *            LaserCannonInfo-Instanz wird mit "new" erzeugt.
	 * @param direction
	 *            Ist die Richtung des Feldes.
	 * @param activeInFirstAndLast
	 *            Gibt an, ob die Presse nur im ersten und letztem Spielschritt
	 *            aktiv ist.
	 */
	public CompactorField(WallInfo wallInfo, LaserCannonInfo laserCannonInfo,
			Direction direction, boolean activeInFirstAndLast) {
		super(wallInfo, laserCannonInfo, direction);
		this.activeInFirstAndFifth = activeInFirstAndLast;

		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CompactorField) {
			return super.equals(obj)
					&& this.activeInFirstAndFifth == ((CompactorField) obj).activeInFirstAndFifth;
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		 return Boolean.valueOf(this.activeInFirstAndFifth).hashCode() * 
		 (super.hashCode() + this.direction.hashCode());
	}
	
	

	/**
	 * Gibt an, ob diePresse nur im ersten und letztem Spieschritt aktiv ist.
	 * 
	 * @return the activeInUneven
	 */
	public boolean isActiveInFirstAndFifth() {
		return this.activeInFirstAndFifth;
	}

	/**
	 * Diese Methode fügt der ImagePathList das Pressenfeld, Wandbilder sowie
	 * Laserkanonen hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		
		
		this.imagePathOn = "images/fields/compactor_"
			+ this.direction.name().toLowerCase()
			+ (this.activeInFirstAndFifth ? "_uneven" : "") + "_active.png";

		this.imagePathOff = "images/fields/compactor_"
			+ this.direction.name().toLowerCase()
			+ (this.activeInFirstAndFifth ? "_uneven" : "") + ".png";
		
		this.imagePathList.add(new DrawingInfo(this.imagePathOff, 50,
				50, 0, 0));
		addWallsToImagePathList();
		addLaserCannonsToImagePathList();
		return true;
	}
	
	public String getImagePathOff() {
		return this.imagePathOff;
	}

	public String getImagePathOn() {
		return this.imagePathOn;
	}
	
	/**
	 * Transformiert ein Basisfeld auf Grundlage der übergebenen Transformation
	 */
	@Override
	public Field transform(FieldTransformation transformation) {
		
		final boolean wallNorth = this.wallInfo.isWallNorth();
		final boolean wallEast = this.wallInfo.isWallEast();
		final boolean wallSouth = this.wallInfo.isWallSouth();
		final boolean wallWest = this.wallInfo.isWallWest();
		
		final int laserNorth = this.laserCannonInfo.getCannonsNorth();
		final int laserEast = this.laserCannonInfo.getCannonsEast();
		final int laserSouth = this.laserCannonInfo.getCannonsSouth();
		final int laserWest = this.laserCannonInfo.getCannonsWest();
		
		final Direction pusherDirection;
		
		switch(transformation) {
		case X_AXIS : 
			if(this.direction == Direction.NORTH) {
				pusherDirection = Direction.SOUTH;
			} else if(this.direction == Direction.SOUTH) {
				pusherDirection = Direction.NORTH;
			} else {
				pusherDirection = this.direction;
			}
			return new CompactorField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest),
				new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest), pusherDirection, this.activeInFirstAndFifth);
		case Y_AXIS : 
			if(this.direction == Direction.EAST) {
				pusherDirection = Direction.WEST;
			} else if(this.direction == Direction.WEST) {
				pusherDirection = Direction.EAST;
			} else {
				pusherDirection = this.direction;
			}
			return new CompactorField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast),
					new LaserCannonInfo(laserNorth, laserWest, laserSouth, laserEast), pusherDirection, this.activeInFirstAndFifth);
		case QUARTER_RIGHT :
			pusherDirection = Direction.getDirection(this.direction, RobotMovement.TURN_RIGHT);
			return new CompactorField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth),
					new LaserCannonInfo(laserWest, laserNorth, laserEast, laserSouth), pusherDirection, this.activeInFirstAndFifth);
		case QUARTER_LEFT :
			pusherDirection = Direction.getDirection(this.direction, RobotMovement.TURN_LEFT);
			return new CompactorField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth),
					new LaserCannonInfo(laserEast, laserSouth, laserWest, laserNorth), pusherDirection, this.activeInFirstAndFifth);
		case HALF_RIGHT:
			pusherDirection = Direction.getDirection(this.direction, RobotMovement.TURN_AROUND);
			return new CompactorField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast),
					new LaserCannonInfo(laserSouth, laserWest, laserNorth, laserEast), pusherDirection, this.activeInFirstAndFifth);
		}
		
		return null;
	}

}
