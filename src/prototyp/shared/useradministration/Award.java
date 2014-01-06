package prototyp.shared.useradministration;

import java.io.Serializable;

/**
 * Model: Award
 * 
 * @author Robert
 * @version 1.0 (30.09.10)
 */
public class Award implements Serializable {
	/** Pfad zu den Bildern */
	public static final String IMAGE_PATH = "awards/";

	/** Seriennummer */
	private static final long serialVersionUID = 392906151324921655L;

	/** Beschreibung */
	private String description;

	/** Dateiname */
	private String imageFileName;

	/** Name */
	private String name;

	/** Default-Konstruktor */
	public Award() {
		this.name = null;
		this.description = null;
		this.imageFileName = null;
	}

	/**
	 * Konstruktor
	 * 
	 * @param name
	 *            steht für den Awardnamen
	 * @param description
	 *            für die Awardbeschreibung
	 * @param imageFileName
	 *            für den Imagepfad des Awards
	 */
	public Award(String name, String description, String imageFileName) {
		this.name = name;
		this.description = description;
		this.imageFileName = imageFileName;
	}

	/**
	 * Liefert die Beschreibung
	 * 
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Liefert den Dateinamen
	 * 
	 * @return imageFileName
	 */
	public String getImageFileName() {
		return this.imageFileName;
	}

	/**
	 * Liefert den Dateinamen
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setzt die Beschreibung
	 * 
	 * @param description
	 *            Beschreibung
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Setzt den Dateinamen
	 * 
	 * @param path
	 *            Dateinamen
	 */
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	/**
	 * Setzt den Namen
	 * 
	 * @param name
	 *            Namen
	 */
	public void setName(String name) {
		this.name = name;
	}
}
