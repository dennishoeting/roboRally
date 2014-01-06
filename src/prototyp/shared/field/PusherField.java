package prototyp.shared.field;

import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.RobotMovement;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für Schieberfeld
 * 
 * @author Marcus
 * 
 */
public class PusherField extends DestroyerField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -322299083090944324L;
	/**
	 * gibt an, ob das Feld nur in jedem ungeraden Spielschritt seine Aktion
	 * ausführt
	 */
	private boolean activeInUneven;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public PusherField() {
	}

	/**
	 * Konstruktor für ein Schieberfeld
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param laserCannonInfo
	 *            Speichert Informationen zu Laserkanonen.
	 *            LaserCannonInfo-Instanz wird mit "new" erzeugt.
	 * @param direction
	 *            Ist die Richtung des Feldes.
	 * @param activeInUneven
	 *            Gibt an, ob der Schieber nur in ungeraden Spieschritten aktiv
	 *            ist.
	 */
	public PusherField(final WallInfo wallInfo, final LaserCannonInfo laserCannonInfo,
			final Direction direction, final boolean activeInUneven) {
		super(wallInfo, laserCannonInfo, direction);
		this.activeInUneven = activeInUneven;

		/*
		 * Wand hinzufügen
		 */
		switch (this.direction) {
		case NORTH:
			this.wallInfo.setWallNorth(true);
			break;
		case EAST:
			this.wallInfo.setWallEast(true);
			break;
		case SOUTH:
			this.wallInfo.setWallSouth(true);
			break;
		case WEST:
			this.wallInfo.setWallWest(true);
			break;
		}

		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PusherField) {
			return super.equals(obj)
					&& this.activeInUneven == ((PusherField) obj).activeInUneven;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return Boolean.valueOf(this.activeInUneven).hashCode() * (super.hashCode() + this.direction.hashCode());
	}

	/**
	 * Gibt an, ob der Schieber nur in ungeraden Spieschritten aktiv ist.
	 * 
	 * @return the activeInUneven
	 */
	public boolean isActiveInUneven() {
		return this.activeInUneven;
	}

	/**
	 * Diese Methode fügt der ImagePathList das Schieberfeld, Wandbilder sowie
	 * Laserkanonen hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		this.imagePathList.add(new DrawingInfo("images/fields/basicfield.png",
				50, 50, 0, 0));

		switch (this.direction) {
		case NORTH:
			this.imagePathList.add(new DrawingInfo(
					"images/fields/pusher_north_part1.png", 50, 8, 0, 6));
			break;
		case EAST:
			this.imagePathList.add(new DrawingInfo(
					"images/fields/pusher_east_part1.png", 8, 50, 35, 0));
			break;
		case SOUTH:
			this.imagePathList.add(new DrawingInfo(
					"images/fields/pusher_south_part1.png", 50, 8, 0, 35));
			break;
		case WEST:
			this.imagePathList.add(new DrawingInfo(
					"images/fields/pusher_west_part1.png", 8, 50, 6, 0));
			break;
		}

		this.imagePathList
				.add(new DrawingInfo("images/fields/pusher_"
						+ this.direction.name().toLowerCase() + "_part2"
						+ (this.activeInUneven ? "_uneven" : "") + ".png", 50,
						50, 0, 0));

		addWallsToImagePathList();

		addLaserCannonsToImagePathList();

		return true;
	}
	
	/**
	 * Transformiert ein Schieberfeld auf Grundlage der übergebenen Transformation
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
			return new PusherField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest),
				new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest), pusherDirection, this.activeInUneven);
		case Y_AXIS : 
			if(this.direction == Direction.EAST) {
				pusherDirection = Direction.WEST;
			} else if(this.direction == Direction.WEST) {
				pusherDirection = Direction.EAST;
			} else {
				pusherDirection = this.direction;
			}
			return new PusherField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast),
					new LaserCannonInfo(laserNorth, laserWest, laserSouth, laserEast), pusherDirection, this.activeInUneven);
		case QUARTER_RIGHT :
			pusherDirection = Direction.getDirection(this.direction, RobotMovement.TURN_RIGHT);
			return new PusherField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth),
					new LaserCannonInfo(laserWest, laserNorth, laserEast, laserSouth), pusherDirection, this.activeInUneven);
		case QUARTER_LEFT :
			pusherDirection = Direction.getDirection(this.direction, RobotMovement.TURN_LEFT);
			return new PusherField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth),
					new LaserCannonInfo(laserEast, laserSouth, laserWest, laserNorth), pusherDirection, this.activeInUneven);
		case HALF_RIGHT:
			pusherDirection = Direction.getDirection(this.direction, RobotMovement.TURN_AROUND);
			return new PusherField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast),
					new LaserCannonInfo(laserSouth, laserWest, laserNorth, laserEast), pusherDirection, this.activeInUneven);
		}
		
		return null;
	}

}
