package prototyp.shared.field;

import java.io.Serializable;
import java.util.ArrayList;

import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;

/**
 * Abstrakte Oberklasse für alle Spielfelder
 * 
 * @author Marcus
 * @version 1.1 Referenzen auf Nachbarfelder eingefügt(Marcus)
 * 
 */
public abstract class Field implements Serializable {

	/**
	 * Delegate-Interface
	 * 
	 * @author Marcus
	 * @version 1.0
	 */
	static public interface NeighbourDelegate {
		Field invoke(Field field);
	}

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 2747655582868233045L;

	/** der X-Index des Feldes auf dem Spielbrett */
	protected int i;

	/*
	 * Felder speichern Referenzen auf ihre Nachbarfelder. Wenn eine Referenz null ist, kann das Nachbarfeld nicht erreicht
	 * werden, da der Weg durch eine Wand blockiert ist.
	 */

	/** Liste, die DrawingInfos enthält */
	protected ArrayList<DrawingInfo> imagePathList = new ArrayList<DrawingInfo>();

	/** der Y-Index des Feldes auf dem Spielbrett */
	protected int j;

	/**
	 * Speichert die Referenz auf das östliche Spielfeld. Falls null, ist das Spielfeld aufgrund einer Mauer nicht zu erreichen
	 */
	protected Field east;

	/**
	 * Speichert die Referenz auf das nördliche Spielfeld. Falls null, ist das Spielfeld aufgrund einer Mauer nicht zu erreichen
	 */
	protected Field north;

	/**
	 * Speichert die Referenz auf das südliche Spielfeld. Falls null, ist das Spielfeld aufgrund einer Mauer nicht zu erreichen
	 */
	protected Field south;

	/**
	 * Speichert die Referenz auf das westliche Spielfeld. Falls null, ist das Spielfeld aufgrund einer Mauer nicht zu erreichen
	 */
	protected Field west;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public Field() {
	}

	/**
	 * @return the east
	 */
	public final Field getEast() {
		return this.east;
	}

	public final Field getField(final Direction direction) {
		switch (direction) {
		case NORTH:
			return this.north;
		case EAST:
			return this.east;
		case SOUTH:
			return this.south;
		default:
			return this.west;
		}
	}

	/**
	 * Liefert den X-Index den das Feld auf dem Spielbrett inne hat.
	 * 
	 * @return der X-Index
	 */
	public final int getI() {
		return this.i;
	}

	/**
	 * Liefert eine Liste, die die Pfade zu den Bilddateien als String speichert.
	 * 
	 * @return the imagePathList
	 */
	public final ArrayList<DrawingInfo> getImagePathList() {
		return this.imagePathList;
	}

	/**
	 * Liefert den Y-Index den das Feld auf dem Spielbrett inne hat.
	 * 
	 * @return der Y-Index
	 */
	public final int getJ() {
		return this.j;
	}

	/**
	 * @return the north
	 */
	public final Field getNorth() {
		return this.north;
	}

	/**
	 * @return the south
	 */
	public final Field getSouth() {
		return this.south;
	}

	/**
	 * @return the west
	 */
	public final Field getWest() {
		return this.west;
	}

	/**
	 * Hier wird das entsprechende Bild zur ImagePathList hinzugefügt
	 * 
	 * @return immer true
	 */
	protected abstract boolean setBasicImagePath();

	/**
	 * @param east
	 *            the east to set
	 */
	public final void setEast(final Field east) {
		this.east = east;
	}

	/**
	 * Setzt den X-Index den das Feld auf dem Spielbrett inne hat.
	 */
	public final void setI(final int i) {
		this.i = i;
	}

	/**
	 * Setzt den Y-Index den das Feld auf dem Spielbrett inne hat.
	 */
	public final void setJ(final int j) {
		this.j = j;
	}

	/**
	 * @param north
	 *            the north to set
	 */
	public final void setNorth(final Field north) {
		this.north = north;
	}

	/**
	 * @param south
	 *            the south to set
	 */
	public final void setSouth(final Field south) {
		this.south = south;
	}

	/**
	 * @param west
	 *            the west to set
	 */
	public void setWest(final Field west) {
		this.west = west;
	}
	
	/**
	 * abstrakte Methode, die Unterklassen für eine Transformierung implementieren müssen
	 * @param transformation
	 * @return
	 */
	public abstract Field transform(final FieldTransformation transformation);
}