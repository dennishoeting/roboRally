package prototyp.shared.field;

import prototyp.shared.util.DrawingInfo;

/**
 * Klasse für ein Lochfeld
 * 
 * @author Marcus
 * @version 1.0
 */
public class HoleField extends Field {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1163086407505130222L;

	/**
	 * Konstruktor zum Erzeugen eines Lochfeldes
	 */
	public HoleField() {
		setBasicImagePath();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof HoleField;
	}

	/**
	 * Diese Methode fügt der ImagePathList das Lochfeld hinzu.
	 * 
	 * @return immer true
	 */
	@Override
	protected boolean setBasicImagePath() {
		this.imagePathList.add(new DrawingInfo("images/fields/hole.png", 50, 50, 0, 0));
		return true;
	}
	
	/**
	 * Transformiert ein Lochfeld auf Grundlage der übergebenen Transformation
	 */
	@Override
	public Field transform(FieldTransformation transformation) {
		return new HoleField();
	}
}
