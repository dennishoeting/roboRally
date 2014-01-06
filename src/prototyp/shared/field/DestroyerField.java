package prototyp.shared.field;

import prototyp.shared.util.Direction;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.WallInfo;

/**
 * Abstrakte Oberklasse für PusherFields und CompactorFields
 * 
 * @author Marcus
 * 
 */
public abstract class DestroyerField extends LaserCannonField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1869760448992973754L;
	/**
	 * Gibt die Himmelsrichtung an, an dem sich der Pusher oder Compactor
	 * befindet
	 */
	protected Direction direction;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public DestroyerField() {
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
	 * @param direction
	 *            Ist die Richtung des Feldes.
	 */
	public DestroyerField(WallInfo wallInfo, LaserCannonInfo laserCannonInfo,
			Direction direction) {
		super(wallInfo, laserCannonInfo);
		this.direction = direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DestroyerField) {
			return super.equals(obj)
					&& this.direction == ((DestroyerField) obj).direction;
		}
		return false;
	}

	/**
	 * Liefert die Richung des Feldes.
	 * 
	 * @return die Richung des Feldes
	 */
	public Direction getDirection() {
		return this.direction;
	}
}
