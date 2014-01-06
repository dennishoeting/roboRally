package prototyp.shared.field;

import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für ein Zahnradfeld
 * 
 * @author Marcus
 * 
 */
public class GearField extends LaserCannonField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4878815056130853282L;
	/** Gibt an, ob sich das Zahnrad im Uhrzeigersinn dreht. */
	private boolean clockwiseDirection;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public GearField() {
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
	 * @param clockwiseDirection
	 *            Gibt an, ob sich das Zahnrad im Uhrzeiger drehen soll
	 */
	public GearField(WallInfo wallInfo, LaserCannonInfo laserCannonInfo,
			boolean clockwiseDirection) {
		super(wallInfo, laserCannonInfo);
		this.clockwiseDirection = clockwiseDirection;

		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GearField) {
			return super.equals(obj)
					&& this.clockwiseDirection == ((GearField) obj).clockwiseDirection;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return Boolean.valueOf(this.clockwiseDirection).hashCode() * super.hashCode();
	}

	/**
	 * Gibt an, ob sich das Zahnrad im Uhrzeigersinn dreht.
	 * 
	 * @return the clockWiseDirection
	 */
	public boolean isClockwiseDirection() {
		return this.clockwiseDirection;
	}

	/**
	 * Diese Methode fügt der ImagePathList das Zahnradfeld, Wandbilder sowie
	 * Laserkanonen hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		this.imagePathList.add(new DrawingInfo("images/fields/basicfield.png",
				50, 50, 0, 0));
		this.imagePathList.add(new DrawingInfo("images/fields/gear_part2.png",
				14, 14, 5, 5));
		this.imagePathList.add(new DrawingInfo("images/fields/gear_"
				+ (this.clockwiseDirection ? "east" : "west") + "_part1.png",
				50, 50, 0, 0));
		addWallsToImagePathList();
		addLaserCannonsToImagePathList();
		return true;
	}
	
	/**
	 * Transformiert ein Zahnradfeld auf Grundlage der übergebenen Transformation
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
		
		switch(transformation) {
		case X_AXIS : 
			return new GearField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest),
				new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest), !this.clockwiseDirection);
		case Y_AXIS : 
			return new GearField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast),
					new LaserCannonInfo(laserNorth, laserWest, laserSouth, laserEast), !this.clockwiseDirection);
		case QUARTER_RIGHT :
			return new GearField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth),
					new LaserCannonInfo(laserWest, laserNorth, laserEast, laserSouth), this.clockwiseDirection);
		case QUARTER_LEFT :
			return new GearField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth),
					new LaserCannonInfo(laserEast, laserSouth, laserWest, laserNorth), this.clockwiseDirection);
		case HALF_RIGHT:
			return new GearField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast),
					new LaserCannonInfo(laserSouth, laserWest, laserNorth, laserEast), this.clockwiseDirection);
		}
		
		return null;
	}
}
