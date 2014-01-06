package prototyp.shared.util;

import prototyp.client.properties.PropertiesDe;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Img;

/**
 * Enum für Roboterfarbe Enthält Name, Preview Bild (String), Roboter Bild (String)
 * 
 * @version 1.0
 * @version 1.1 Alle Farben hinzugefügt. -Timo 24.10.10
 * @version 1.2 Weitere Farben, Bilder und Methoden hinzugefügt. -Timo 30.10.10
 * 
 * @author Marcus, Timo
 * 
 */
public enum Color {

	BLUE(0, "Farbe_Blau", "robot_blue"), LIGHT_BLUE(1, "Farbe_HellBlau",
			"robot_light_blue"), RED(2, "Farbe_Rot", "robot_red"), YELLOW(3,
			"Farbe_Gelb", "robot_yellow"), GREEN(4, "Farbe_Gruen",
			"robot_green"), VIOLET(5, "Farbe_Lila", "robot_violet"), ORANGE(6,
			"Farbe_Orange", "robot_orange"), TURQUOISE(7, "Farbe_Tuerkis",
			"robot_turquoise"), LIGHT_GREEN(8, "Farbe_HellGruen",
			"robot_light_green"), GREY(9, "Farbe_Grau", "robot_grey"), BROWN(10,
			"Farbe_Braun", "robot_brown"), PINK(11, "Farbe_Rosa", "robot_pink"), WHITE(
			12, "Farbe_Weiss", "robot_white");

	private final String colorPreview, robotPicture;

	/**
	 * Integer um zu jeder Farbe den Namen zu bekommen. Geht nur Clientseitig!
	 */
	private final int colorCode;

	/**
	 * Konstruktor
	 * 
	 * @param colorCode
	 *            Integerwert, der für einen bestimmten Propseintrag steht (0-12)
	 * @param colorPreview
	 *            Name der Farbe
	 * @param robotPicture
	 *            Roboterbild
	 */
	private Color(int colorCode, String colorPreview, String robotPicture) {
		this.colorCode = colorCode;
		this.colorPreview = colorPreview;
		this.robotPicture = robotPicture;
	}

	/**
	 * Liefert den übersetzen Namen der jeweiligen Farbe.
	 * 
	 * ACHTUNG nur Clientseitig aufrufen!
	 * 
	 * Serverseitig können die Properties mittels GWT.create nicht verwendet werden!
	 * 
	 * @param color
	 * @return Name aus den Props, ansonsten null
	 */
	public String getColorName() {

		PropertiesDe props = GWT.create(PropertiesDe.class);

		switch (this.colorCode) {
		case 0:
			return props.colorBLUE();
		case 1:
			return props.colorLIGHT_BLUE();
		case 2:
			return props.colorRED();
		case 3:
			return props.colorYELLOW();
		case 4:
			return props.colorGREEN();
		case 5:
			return props.colorVIOLET();
		case 6:
			return props.colorORANGE();
		case 7:
			return props.colorTURQUOISE();
		case 8:
			return props.colorLIGHT_GREEN();
		case 9:
			return props.colorGREY();
		case 10:
			return props.colorBROWN();
		case 11:
			return props.colorPINK();
		case 12:
			return "Weiss";
		}

		return null;
	}

	/**
	 * Liefert ein Img der Farbvorschau.
	 * 
	 * @return Img
	 */
	public Img getColorPreview() {
		return new Img(getImagePrefix() + this.colorPreview + getImageSuffix());
	}

	/**
	 * Liefert den Vorschaunamen für die Farbe
	 * 
	 * @return colorPreview
	 */
	public String getColorPreviewName() {
		return this.colorPreview;
	}

	/**
	 * Liefert den Pfad zum Farbvorschau Bild.
	 * 
	 * @return Pfad zm Bild
	 */
	public String getColorPreviewPath() {
		return this.colorPreview + getImageSuffix();
	}

	/**
	 * Liefert das Präfix des Dateipfades
	 * 
	 * @return Präfix des Dateipfades
	 */
	public String getImagePrefix() {
		return "robots/";
	}

	/**
	 * Liefert die Dateiendung der Datei
	 * 
	 * @return .png
	 */
	public String getImageSuffix() {
		return ".png";
	}

	/**
	 * Liefert ein Img des Roboters
	 * 
	 * @return Img
	 */
	public Img getRobotPicture() {
		return new Img(getImagePrefix() + this.robotPicture + getImageSuffix(), 134, 134);
	}

	/**
	 * Liefert den Pfad zum Roboter Bild.
	 * 
	 * @return robotPicture
	 */
	public String getRobotPicturePath() {
		return this.robotPicture;
	}
}
