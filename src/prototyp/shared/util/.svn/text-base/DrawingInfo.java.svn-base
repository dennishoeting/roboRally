package prototyp.shared.util;

import java.io.Serializable;

/**
 * Kapselt Zeichennformationen zu Bildern
 * 
 * @author Marcus
 * @version 1.0
 */
public class DrawingInfo implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = 6727241726430466968L;

	/** der Dateipfad */
	private String imageFileName;

	/** die Breite des Bildes */
	private int width;

	/** die Höhe des Bildes */
	private int height;

	/** die X-Koordinate, wo das Bild gezeichnet werden soll */
	private int x;

	/** die y-Koordinate, wo das Bild gezeichnet werden soll */
	private int y;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public DrawingInfo() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param imageFileName
	 */
	public DrawingInfo(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	/**
	 * Konstruktor
	 * 
	 * @param imageFileName
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 */
	public DrawingInfo(String imageFileName, int width, int height, int x, int y) {
		this.imageFileName = imageFileName;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DrawingInfo) {
			return this.imageFileName.equals(((DrawingInfo) obj).imageFileName);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		 return this.imageFileName.hashCode();
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * @return the imageFileName
	 */
	public String getImageFileName() {
		return this.imageFileName;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return this.y;
	}

}
