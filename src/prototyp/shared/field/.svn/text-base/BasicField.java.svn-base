package prototyp.shared.field;

import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.WallInfo;

/**
 * Klasse für Basisfelder
 * 
 * @author Marcus
 * 
 */
public class BasicField extends LaserCannonField {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 3063222035508473444L;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public BasicField() {
	}

	/**
	 * Konstruktor ür das erzeugen eines Basisfeldes
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu W�nden. WallInfo-Instanz wird mit
	 *            "new" erzeugt.
	 * @param laserCannonInfo
	 *            Speichert Informationen zu Laserkanonen.
	 *            LaserCannonInfo-Instanz wird mit "new" erzeugt.
	 */
	public BasicField(WallInfo wallInfo, LaserCannonInfo laserCannonInfo) {
		super(wallInfo, laserCannonInfo);

		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BasicField) {
			return super.equals(obj);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return super.hashCode();
	}


	/**
	 * Diese Methode fügt der ImagePathList das BasicField, Wandbilder sowie
	 * Laserkanonen hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		this.imagePathList.add(new DrawingInfo("images/fields/basicfield.png",
				50, 50, 0, 0));
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
		
		switch(transformation) {
		case X_AXIS : 
			return new BasicField(new WallInfo(wallSouth, wallEast, wallNorth, wallWest),
				new LaserCannonInfo(laserSouth, laserEast, laserNorth, laserWest));
		case Y_AXIS : 
			return new BasicField(new WallInfo(wallNorth, wallWest, wallSouth, wallEast),
					new LaserCannonInfo(laserNorth, laserWest, laserSouth, laserEast));
		case QUARTER_RIGHT :
			return new BasicField(new WallInfo(wallWest, wallNorth, wallEast, wallSouth),
					new LaserCannonInfo(laserWest, laserNorth, laserEast, laserSouth));
		case QUARTER_LEFT :
			return new BasicField(new WallInfo(wallEast, wallSouth, wallWest, wallNorth),
					new LaserCannonInfo(laserEast, laserSouth, laserWest, laserNorth));
		case HALF_RIGHT:
			return new BasicField(new WallInfo(wallSouth, wallWest, wallNorth, wallEast),
					new LaserCannonInfo(laserSouth, laserWest, laserNorth, laserEast));
		}
		
		return null;
	}
}
