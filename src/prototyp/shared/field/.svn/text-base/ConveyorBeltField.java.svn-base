package prototyp.shared.field;

import java.util.HashSet;
import java.util.Set;

import prototyp.shared.exception.field.ConveyorBeltFieldException;
import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.RobotMovement;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für alle FließBänder
 * 
 * @author Marcus
 */
public class ConveyorBeltField extends LaserCannonField {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -8674287082407045015L;

	/** Ausgangsrichtung des Pfeils */
	private Direction arrowOutDirection;

	/** Eingansrichtungen */
	private Set<Direction> arrowInDirections;

	/** die Reichweite */
	private int range;

	/** gibt an, ob das Feld andere Förderbänder zusammen führt */
	private boolean isMerging;

	/** das Bild, wenn das FörderBand Aktiv ist */
	private String imagePathOn;

	/** das Bild für das Aus-Bild */
	private String imagePathOff;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public ConveyorBeltField() {
		this.imagePathOn = "";
		this.imagePathOff = "";
	}

	/**
	 * Konstruktor zum Erzeugen eines Drehfließbandfeldes.
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param laserCannonInfo
	 *            Speichert Informationen zu Laserkanonen.
	 *            LaserCannonInfo-Instanz wird mit "new" erzeugt.
	 * @param arrowInDirection
	 *            Ist die Eingangsrichtung des Drehfließbandfeldes.
	 * @param arrowOutDirection
	 *            Ist die Richtung, in der der Pfeil eines Fließbandes zeigt.
	 * @throws ConveyorBeltFieldException
	 */
	public ConveyorBeltField(final WallInfo wallInfo,
			final LaserCannonInfo laserCannonInfo,
			final Direction arrowOutDirection,
			final Set<Direction> arrowInDirections, final int range)
			throws ConveyorBeltFieldException {
		super(wallInfo, laserCannonInfo);

		if (arrowInDirections.contains(arrowOutDirection)) {
			throw new ConveyorBeltFieldException();
		}
		this.arrowOutDirection = arrowOutDirection;
		this.arrowInDirections = arrowInDirections;
		this.range = range;

		if (this.arrowInDirections.size() == 1
				&& Direction.getDirection(
						(Direction) this.arrowInDirections.toArray()[0],
						RobotMovement.TURN_AROUND) == this.arrowOutDirection) {
			this.isMerging = false;
		} else {
			this.isMerging = true;
		}

		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConveyorBeltField) {
			return super.equals(obj)
					&& this.arrowOutDirection == ((ConveyorBeltField) obj).arrowOutDirection
					&& this.range == ((ConveyorBeltField) obj).range
					&& this.arrowInDirections
							.equals(((ConveyorBeltField) obj).arrowInDirections);
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		 return super.hashCode() * this.arrowOutDirection.hashCode() * this.arrowInDirections.hashCode();
	}

	/**
	 * Liefert die Eingangsrichtung eines Drehfließbandfeldes.
	 * 
	 * @return die die Eingangsrichtung eines Drehfließbandfeldes
	 */
	public Set<Direction> getArrowInDirections() {
		return this.arrowInDirections;
	}

	/**
	 * Liefert die Richtung, in die der Pfeil des Fließbandes zeigt.
	 * 
	 * @return die Fließrichtung des Pfeils
	 */
	public Direction getArrowOutDirection() {
		return this.arrowOutDirection;
	}

	public String getImagePathOff() {
		return this.imagePathOff;
	}

	public String getImagePathOn() {
		return this.imagePathOn;
	}

	/**
	 * Liefert die Schubreichweite des geraden Fließbandfeldes.
	 * 
	 * @return die Schubreichweite
	 */
	public int getRange() {
		return this.range;
	}

	/**
	 * Gibt an, ob das Förderband ander Förderbänder im Reißverschlussverfahren
	 * zusammenführt
	 * 
	 * @return
	 */
	public boolean isMerging() {
		return this.isMerging;
	}

	/**
	 * Diese Methode fügt der ImagePathList das Drehfließbandfeld, Wandbilder
	 * sowie Laserkanonen hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {

		final StringBuilder inDirections = new StringBuilder();

		if (this.arrowInDirections.contains(Direction.NORTH)) {
			inDirections.append("_north");
		}
		if (this.arrowInDirections.contains(Direction.EAST)) {
			inDirections.append("_east");
		}
		if (this.arrowInDirections.contains(Direction.SOUTH)) {
			inDirections.append("_south");
		}
		if (this.arrowInDirections.contains(Direction.WEST)) {
			inDirections.append("_west");
		}

		inDirections.append("-");
		inDirections.append(this.arrowOutDirection.name().toLowerCase());
		inDirections.append("-");

		this.imagePathOn = "images/fields/conveyor" + inDirections.toString()
				+ this.range + ".png";

		this.imagePathOff = "images/fields/conveyor" + inDirections.toString()
				+ this.range + "_off.png";

		this.imagePathList
				.add(new DrawingInfo(this.imagePathOff, 50, 50, 0, 0));
		addWallsToImagePathList();
		addLaserCannonsToImagePathList();
		return true;
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
		
		final Direction arrowOut;
		final Set<Direction> arrowsIn = new HashSet<Direction>();
		
		try {
		switch(transformation) {
		case X_AXIS : 
				if(this.arrowOutDirection == Direction.NORTH) {
					arrowOut = Direction.SOUTH;
				} else if(this.arrowOutDirection == Direction.SOUTH) {
					arrowOut = Direction.NORTH;
				} else {
					arrowOut = this.arrowOutDirection;
				}
				
				if(this.arrowInDirections.contains(Direction.NORTH)) {
					arrowsIn.add(Direction.SOUTH);
				}
				if(this.arrowInDirections.contains(Direction.SOUTH)) {
					arrowsIn.add(Direction.NORTH);
				}
				if(this.arrowInDirections.contains(Direction.EAST)) {
					arrowsIn.add(Direction.EAST);
				}
				if(this.arrowInDirections.contains(Direction.WEST)) {
					arrowsIn.add(Direction.WEST);
				}
				
				return new ConveyorBeltField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest),
					new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest), arrowOut, arrowsIn, this.range);
		case Y_AXIS : 
			
			if(this.arrowOutDirection == Direction.EAST) {
				arrowOut = Direction.WEST;
			} else if(this.arrowOutDirection == Direction.WEST) {
				arrowOut = Direction.EAST;
			} else {
				arrowOut = this.arrowOutDirection;
			}
			
			if(this.arrowInDirections.contains(Direction.EAST)) {
				arrowsIn.add(Direction.WEST);
			}
			if(this.arrowInDirections.contains(Direction.WEST)) {
				arrowsIn.add(Direction.EAST);
			}
			if(this.arrowInDirections.contains(Direction.NORTH)) {
				arrowsIn.add(Direction.NORTH);
			}
			if(this.arrowInDirections.contains(Direction.SOUTH)) {
				arrowsIn.add(Direction.SOUTH);
			}
			
			return new ConveyorBeltField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast),
				new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest), arrowOut, arrowsIn, this.range);
		case QUARTER_RIGHT :

			arrowOut = Direction.getDirection(this.arrowOutDirection, RobotMovement.TURN_RIGHT);
			
			if(this.arrowInDirections.contains(Direction.NORTH)) {
				arrowsIn.add(Direction.EAST);
			}
			if(this.arrowInDirections.contains(Direction.SOUTH)) {
				arrowsIn.add(Direction.WEST);
			}
			if(this.arrowInDirections.contains(Direction.EAST)) {
				arrowsIn.add(Direction.SOUTH);
			}
			if(this.arrowInDirections.contains(Direction.WEST)) {
				arrowsIn.add(Direction.NORTH);
			}
			
			return new ConveyorBeltField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth),
				new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest), arrowOut, arrowsIn, this.range);
		case QUARTER_LEFT :

			arrowOut = Direction.getDirection(this.arrowOutDirection, RobotMovement.TURN_LEFT);
			
			if(this.arrowInDirections.contains(Direction.NORTH)) {
				arrowsIn.add(Direction.WEST);
			}
			if(this.arrowInDirections.contains(Direction.SOUTH)) {
				arrowsIn.add(Direction.EAST);
			}
			if(this.arrowInDirections.contains(Direction.EAST)) {
				arrowsIn.add(Direction.NORTH);
			}
			if(this.arrowInDirections.contains(Direction.WEST)) {
				arrowsIn.add(Direction.SOUTH);
			}
			
			return new ConveyorBeltField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth),
				new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest), arrowOut, arrowsIn, this.range);
		case HALF_RIGHT:

			arrowOut = Direction.getDirection(this.arrowOutDirection, RobotMovement.TURN_AROUND);
			
			if(this.arrowInDirections.contains(Direction.NORTH)) {
				arrowsIn.add(Direction.SOUTH);
			}
			if(this.arrowInDirections.contains(Direction.SOUTH)) {
				arrowsIn.add(Direction.NORTH);
			}
			if(this.arrowInDirections.contains(Direction.EAST)) {
				arrowsIn.add(Direction.WEST);
			}
			if(this.arrowInDirections.contains(Direction.WEST)) {
				arrowsIn.add(Direction.EAST);
			}
			
			return new ConveyorBeltField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast),
				new LaserCannonInfo(laserSouth, laserWest, laserNorth, laserEast), arrowOut, arrowsIn, this.range);
		}
		} catch(ConveyorBeltFieldException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}