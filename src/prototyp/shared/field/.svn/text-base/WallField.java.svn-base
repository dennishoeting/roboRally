package prototyp.shared.field;

import prototyp.shared.exception.playingboard.WallAlreadyExistsException;
import prototyp.shared.exception.playingboard.WallNotExistsException;
import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.WallInfo;

/**
 * Abstrakte Oberklasse für alle Spielfelder, die Wände besitzen können
 * 
 * @author Marcus
 * @version 1.1
 */
public abstract class WallField extends Field {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 5018320058429701445L;

	/** WallInfo speichert die Informationen über die die Wände von Wandfeldern. */
	protected WallInfo wallInfo;

	/**
	 * DefaultKonstruktor für Serializable-Interface
	 */
	public WallField() {
	}

	/**
	 * Konstruktor zum Initialisieren eines Wandfeldes
	 * 
	 * @param wallInfo
	 *            Speichert Informationen zu Wänden. WallInfo-Instanz wird mit "new" erzeugt.
	 */
	public WallField(WallInfo wallInfo) {
		this.wallInfo = wallInfo;
	}

	/**
	 * Fügt eine Wand dem Spielfeld hinzu und leidert den Pad zum Wandbild als String zurück.
	 * 
	 * @param direction
	 *            Da wo die Wand entfernt werden soll.
	 * @return der der ImagePathList hinzugefügte Bildpfad als String
	 * @throws WallAlreadyExistsException
	 *             Wird geworfen, wenn die Wand an der besagten Position bereits existiert.
	 */
	public final String addWall(Direction direction) throws WallAlreadyExistsException {
		switch (direction) {
		case NORTH:
			if (this.wallInfo.isWallNorth()) {
				throw new WallAlreadyExistsException("Es existiert bereits eine Wand" + "(" + direction.name() + ")"
						+ " auf Spielfeld" + "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallNorth(true);
			this.imagePathList.add(new DrawingInfo("images/fields/wall_north.png", 50, 50, 0, 0));
			return "images/fields/wall_north.png";
		case EAST:
			if (this.wallInfo.isWallEast()) {
				throw new WallAlreadyExistsException("Es existiert bereits eine Wand" + "(" + direction.name() + ")"
						+ " auf Spielfeld" + "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallEast(true);
			this.imagePathList.add(new DrawingInfo("images/fields/wall_east.png", 50, 50, 0, 0));
			return "images/fields/wall_east.png";
		case SOUTH:
			if (this.wallInfo.isWallSouth()) {
				throw new WallAlreadyExistsException("Es existiert bereits eine Wand" + "(" + direction.name() + ")"
						+ " auf Spielfeld" + "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallSouth(true);
			this.imagePathList.add(new DrawingInfo("images/fields/wall_south.png", 50, 50, 0, 0));
			return "images/fields/wall_south.png";
		default:
			if (this.wallInfo.isWallWest()) {
				throw new WallAlreadyExistsException("Es existiert bereits eine Wand" + "(" + direction.name() + ")"
						+ " auf Spielfeld" + "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallWest(true);
			this.imagePathList.add(new DrawingInfo("images/fields/wall_west.png", 50, 50, 0, 0));
			return "images/fields/wall_west.png";
		}
	}

	/**
	 * Diese Methode fügt der ImagePathList die Wände hinzu.
	 * 
	 * @return immer true
	 */
	protected final boolean addWallsToImagePathList() {
		if (this.wallInfo.isWallNorth()) {
			this.imagePathList.add(new DrawingInfo("images/fields/wall_north.png", 50, 50, 0, 0));
		}
		if (this.wallInfo.isWallSouth()) {
			this.imagePathList.add(new DrawingInfo("images/fields/wall_south.png", 50, 50, 0, 0));
		}
		if (this.wallInfo.isWallEast()) {
			this.imagePathList.add(new DrawingInfo("images/fields/wall_east.png", 50, 50, 0, 0));
		}
		if (this.wallInfo.isWallWest()) {
			this.imagePathList.add(new DrawingInfo("images/fields/wall_west.png", 50, 50, 0, 0));
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WallField) {
			return this.wallInfo.equals(((WallField) obj).wallInfo);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		 return this.wallInfo.hashCode();
	}

	/**
	 * Liefert Informationen, ob und in welchen Himmelsrichtungen sich Wände befinden. Mit Instanzmethoden können auf die
	 * entsprechenden Informationen zugegriffen werden.
	 * 
	 * @return Info über Wände
	 */
	public final WallInfo getWallInfo() {
		return this.wallInfo;
	}

	/**
	 * Entfernt eine Wand vom Spielfeld und liefert den Pfad des Wandbildes als String zurück
	 * 
	 * @param direction
	 *            Da wo die Wand entfernt werden soll.
	 * @return der aus der ImagePathList entfernte Bildpfad als String * @throws WallNotExistsException Wird geworfen, wenn keine
	 *         Wand an der besagten Position existiert.
	 */
	public final String removeWall(Direction direction) throws WallNotExistsException {
		switch (direction) {
		case NORTH:
			if (!this.wallInfo.isWallNorth()) {
				throw new WallNotExistsException("Es existiert keine Wand" + "(" + direction.name() + ")" + " auf Spielfeld"
						+ "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallNorth(false);
			this.imagePathList.remove(new DrawingInfo("images/fields/wall_north.png"));
			return "images/fields/wall_north.png";
		case EAST:
			if (!this.wallInfo.isWallEast()) {
				throw new WallNotExistsException("Es existiert keine Wand" + "(" + direction.name() + ")" + " auf Spielfeld"
						+ "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallEast(false);
			this.imagePathList.remove(new DrawingInfo("images/fields/wall_east.png"));
			return "images/fields/wall_east.png";
		case SOUTH:
			if (!this.wallInfo.isWallSouth()) {
				throw new WallNotExistsException("Es existiert keine Wand" + "(" + direction.name() + ")" + " auf Spielfeld"
						+ "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallSouth(false);
			this.imagePathList.remove(new DrawingInfo("images/fields/wall_south.png"));
			return "images/fields/wall_south.png";
		default:
			if (!this.wallInfo.isWallWest()) {
				throw new WallNotExistsException("Es existiert keine Wand" + "(" + direction.name() + ")" + " auf Spielfeld"
						+ "(" + this.i + ", " + this.j + ")");
			}
			this.wallInfo.setWallWest(false);
			this.imagePathList.remove(new DrawingInfo("images/fields/wall_west.png"));
			return "images/fields/wall_west.png";
		}
	}
}
