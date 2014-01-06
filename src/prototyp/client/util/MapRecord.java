package prototyp.client.util;

import prototyp.shared.round.PlayingBoard;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse zum Anzeigen der Karteninformationen in der Tabelle.
 * 
 * @author Andreas, Jannik
 * @version 1.0
 * @version 1.1 (in eigene Klasse geändert --Jannik)
 * @version 1.2 (Decription hinzugefügt und Setter gelöscht --Andreas)
 */
public class MapRecord extends ListGridRecord {

	/**
	 * Konstruktor. Erstellt ein {@link ListGridRecord} mit den übergebenen
	 * Parametern.
	 * 
	 * @param id
	 *            PlayingBoardID
	 * @param name
	 *            Name
	 * @param description
	 *            Beschreibung der Karte
	 * @param width
	 *            Breite
	 * @param height
	 *            Höhe
	 * @param numberOfCheckpoints
	 *            Anzahl der Checkpoints
	 * @param maxPlayers
	 *            Maximale Anzahl der Spieler
	 * @param previewPicture
	 *            Pfad zum Vorschaubild
	 */
	public MapRecord(int id, String name, String description, int width,
			int height, int numberOfCheckpoints, int maxPlayers,
			String previewPicture) {
		this.setAttribute("playingboardid", id);
		this.setAttribute("name", name);
		this.setAttribute("description", description);
		this.setAttribute("size", width + "*" + height);
		this.setAttribute("numberOfCheckpoints", numberOfCheckpoints);
		this.setAttribute("maxPlayers", maxPlayers);
		this.setAttribute("previewPicture", "maps/" + previewPicture);
	}

	public MapRecord(PlayingBoard playingBoard) {
		this.setAttribute("playingboardid", playingBoard.getID());
		this.setAttribute("name", playingBoard.getName());
		this.setAttribute("description", playingBoard.getDescription());
		this.setAttribute("size",
				playingBoard.getWidth() + "*" + playingBoard.getHeight());
		this.setAttribute("numberOfCheckpoints",
				playingBoard.getNumberOfCheckpoints());
		this.setAttribute("maxPlayers", playingBoard.getMaxPlayers());
		this.setAttribute("previewPicture",
				"maps/" + playingBoard.getImageFileName());
	}

}
