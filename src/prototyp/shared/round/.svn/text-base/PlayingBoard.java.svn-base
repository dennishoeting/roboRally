package prototyp.shared.round;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import prototyp.client.model.mapGenerator.WayPointComputer;
import prototyp.server.model.Round;
import prototyp.shared.exception.playingboard.CheckpointFieldNumeratedExecption;
import prototyp.shared.exception.playingboard.CompactorFieldException;
import prototyp.shared.exception.playingboard.ConveyorBeltsInconsistentException;
import prototyp.shared.exception.playingboard.LessThanTwoStartFieldsException;
import prototyp.shared.exception.playingboard.NoCheckointFieldException;
import prototyp.shared.exception.playingboard.PlayingboardException;
import prototyp.shared.exception.playingboard.PusherFieldException;
import prototyp.shared.exception.playingboard.StartFieldNumeratedException;
import prototyp.shared.field.CheckpointField;
import prototyp.shared.field.CompactorField;
import prototyp.shared.field.ConveyorBeltField;
import prototyp.shared.field.Field;
import prototyp.shared.field.GearField;
import prototyp.shared.field.HoleField;
import prototyp.shared.field.LaserCannonField;
import prototyp.shared.field.PusherField;
import prototyp.shared.field.RepairField;
import prototyp.shared.field.StartField;
import prototyp.shared.field.WallField;
import prototyp.shared.util.Direction;
import prototyp.shared.util.RobotMovement;

/**
 * Nähere Beschreibung eines Spielbrettes.
 * 
 * @author Tim, Andreas, Marcus
 * @version 1.3
 * @version 1.4 (Marcus)
 * @see {@link Round}
 */
public class PlayingBoard implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = -4715727512618924342L;

	/*
	 * Es folgen allgemeine Informationen über das Spielbrett.
	 */

	/** Liste mit allen Checkpointfelder */
	private List<CheckpointField> checkpointFieldList = new ArrayList<CheckpointField>();

	/** Liste mit allen Pressenfeldern */
	private List<CompactorField> compactorFieldList = new ArrayList<CompactorField>();

	/** Die Beschreibung des Spielbrettes */
	private String description;

	/** Schwierigkeitsgrad */
	private String difficulty;

	/** Die Felder des Spielbretts */
	private Field[][] fields;

	/** Liste alle ZahnradFeldern */
	private List<GearField> gearFieldList = new ArrayList<GearField>();

	/** Die Höhe des PlayingBoards */
	private int height;

	/** Eindeutige Nummer zur Identifizierung des PlayingBoards */
	private int id;

	/** Der Dateiname des Vorschaubildes */
	private String imageFileName;

	/** das Image-Format (png, jpg, bmp, .....) */
	private String imageFormat;

	/** Liste mit allen Laserkanonenfeldern */
	private List<LaserCannonField> laserCannonFieldList = new ArrayList<LaserCannonField>();

	/** Die Anzahl der maximalen Spieler */
	private int maxPlayers;

	/** Name des PlayingBoards */
	private String name;

	/** die Anzahl der Förderbänder auf dem Spielbrett */
	private int numberOfConveyorBeltFields;
	
	/** die Anzahl der Pressen auf dem Spielbrett */
	private int numberOfCompactorFields;
	
	/** die Anzahl der Schieber auf dem Spielbrett */
	private int numberOfPusherFields;
	
	/** die Anzahl der Zahnräder auf dem Spielbrett */
	private int numberOfGearFields;
	
	/** die Anzahl der Laserkanonenfelder auf dem Spielbrett */
	private int numberOfLaserCannonFields;
	/*
	 * Im folgenden werden Listen für bestimmte Feldtypen deklariert. Damit
	 * können die bestimmten Felder im Spiel schnell gefunden werden. Hilfreich
	 * beim Aktivieren der Maschinenelemente.
	 */

	/**
	 * Nickname des Nutzers, der das Spielbrett erstellt hat (Wird bei jedem
	 * Laden mit der DB aktualisiert)
	 */
	private String nickname;

	/** Die Anzahl der Checkpoints auf dem PlayingBoard */
	private int numberOfCheckpoints;

	/** Liste mit allen Schieberfeldern */
	private List<PusherField> pusherFieldList = new ArrayList<PusherField>();

	/**
	 * wird für rekursive Funktion gebraucht und zeigt an, ob ein Checkpoint
	 * erreicht wurde
	 */
	private boolean reachedCheckpoint;

	/** Liste mit allen Reparaturelder */
	private List<RepairField> repairFieldList = new ArrayList<RepairField>();

	/** Liste mit allen Startfeldern */
	private List<StartField> startFieldList = new ArrayList<StartField>();

	/** Liste mit allen geraden Föderbändern und reichweite 1 */
	private List<ConveyorBeltField> ConveyorBeltFieldRangeOneList = new ArrayList<ConveyorBeltField>();

	/** Liste mit allen geraden Föderbändern und reichweite 2 */
	private List<ConveyorBeltField> ConveyorBeltFieldRangeTwoList = new ArrayList<ConveyorBeltField>();

	/** Name des Nutzers, der das Spielbrett erstellt hat */
	private int userID = -1;

	/** Die Breite des PlayingBoards */
	private int width;

	/**
	 * Default-Konstruktor
	 */
	public PlayingBoard() {
	}

	/**
	 * Konstruktor fürs Speichern
	 * 
	 * @param fields
	 *            Felder
	 * @param name
	 *            Spielbrettname
	 * @param description
	 *            Beschreibung
	 * @param difficulty
	 *            Schwierigkeiten
	 * @param userID
	 *            ErstellerID
	 */
	public PlayingBoard(final Field[][] fields, final String name,
			final String description, final String difficulty, final int userID) {
		this.fields = fields;
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
		this.width = fields[0].length;
		this.height = fields.length;
		this.imageFileName = "";
		this.imageFormat = "png";
	}

	/**
	 * @return the checkpointFieldList
	 */
	public final List<CheckpointField> getCheckpointFieldList() {
		return this.checkpointFieldList;
	}

	/**
	 * @return the compactorFieldList
	 */
	public final List<CompactorField> getCompactorFieldList() {
		return this.compactorFieldList;
	}

	/**
	 * @return the straightConveyorBeltFieldList
	 */
	public final List<ConveyorBeltField> getConveyorBeltFieldRangeOneList() {
		return this.ConveyorBeltFieldRangeOneList;
	}

	/**
	 * @return the straightConveyorBeltFieldList
	 */
	public final List<ConveyorBeltField> getConveyorBeltFieldRangeTwoList() {
		return this.ConveyorBeltFieldRangeTwoList;
	}

	/**
	 * Liefert die Beschreibung des Spielbrettes
	 * 
	 * @return die Beschreibung des Spielbrettes
	 */
	public final String getDescription() {
		return this.description;
	}

	/**
	 * Liefert den Schwierigkeitsgrad
	 * 
	 * @return the difficulty
	 */
	public final String getDifficulty() {
		return this.difficulty;
	}

	/**
	 * Setzt die Fielder des Spielbrettes
	 * 
	 * @return fields
	 */
	public final Field[][] getFields() {
		return this.fields;
	}

	/**
	 * @return the gearFieldList
	 */
	public final List<GearField> getGearFieldList() {
		return this.gearFieldList;
	}

	/**
	 * Liefert die Höhe eines PlayingBoards
	 * 
	 * @return die Höhe des PlayingBoards
	 */
	public final int getHeight() {
		return this.height;
	}

	/**
	 * Liefert die ID eines PlayingBoards
	 * 
	 * @return die ID des PlayingBoards
	 */
	public final int getID() {
		return this.id;
	}

	/**
	 * Liefert den Dateinamen des Vorschaubildes
	 * 
	 * @return den Dateinamen des Vorschaubildes
	 */
	public final String getImageFileName() {
		return this.imageFileName;
	}

	/**
	 * @return the imageFormat
	 */
	public final String getImageFormat() {
		return this.imageFormat;
	}

	/**
	 * @return the laserCannonFieldList
	 */
	public final List<LaserCannonField> getLaserCannonFieldList() {
		return this.laserCannonFieldList;
	}

	/**
	 * Liefert die maximale Anzahl der Spieler
	 * 
	 * @return maximale Anzahl der Spieler
	 */
	public final int getMaxPlayers() {
		return this.maxPlayers;
	}

	/**
	 * Liefert den Name eines PlayingBoards
	 * 
	 * @return den Namen des PlayingBoards
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Liefert den Nickname des PlayingBoard Erstellers.
	 * 
	 * @return
	 */
	public final String getNickname() {
		return this.nickname;
	}

	/**
	 * Liefert die Anzahl der Checkpoints eines PlayingBoards
	 * 
	 * @return die Anzahl der Checkpoints
	 */
	public final int getNumberOfCheckpoints() {
		return this.numberOfCheckpoints;
	}

	/**
	 * @return the pusherFieldList
	 */
	public final List<PusherField> getPusherFieldList() {
		return this.pusherFieldList;
	}

	/**
	 * @return the repairFieldList
	 */
	public final List<RepairField> getRepairFieldList() {
		return this.repairFieldList;
	}

	/**
	 * @return the startFieldList
	 */
	public final List<StartField> getStartFieldList() {
		return this.startFieldList;
	}

	/**
	 * @return the userID
	 */
	public final int getUserID() {
		return this.userID;
	}

	/**
	 * Liefert die Breite eines PlayingBoards
	 * 
	 * @return die Breite des PlayingBoards
	 */
	public final int getWidth() {
		return this.width;
	}

	/**
	 * @return the reachedCheckpoint
	 */
	public final boolean isReachedCheckpoint() {
		return this.reachedCheckpoint;
	}

	/**
	 * Verlinkt die Felder und testet auf Sinn Diese Methode wird auf dem vom
	 * Server aufgerufen.
	 * 
	 * @throws PlayingboardException
	 */
	public final void linkFieldsAndCheckSense() throws PlayingboardException {

		/*
		 * 1.Felder Koordinaten setzen
		 * 
		 * 2. Verlinkungen zu Nachbarfeldern einfügen Standartmäßig, wird das
		 * erste und das letzte bzw das oberste und unterste Feld nicht
		 * verlinkt, auch wenn dazwischen keine Mauer ist Wenn doch, könnte man
		 * das Spielbrett verlassen und auf der anderen Seite wieder
		 * herauskommen.
		 * 
		 * 3.Feld-ArrayListen werden gefüllt
		 * 
		 * 4. Checkpoints werden gezählt
		 */
		for (int i = 0; i < this.fields.length; i++) {
			for (int j = 0; j < this.fields[i].length; j++) {
				// 1.
				this.fields[i][j].setI(i);
				this.fields[i][j].setJ(j);

				// 2.
				// Alle Refernezen auf null stetzen
				this.fields[i][j].setNorth(null);
				this.fields[i][j].setEast(null);
				this.fields[i][j].setSouth(null);
				this.fields[i][j].setWest(null);

				// nördliche Verlinkung setzen
				if (i != 0) {
					if ((this.fields[i][j] instanceof HoleField || !((WallField) this.fields[i][j])
							.getWallInfo().isWallNorth())
							&& (this.fields[i - 1][j] instanceof HoleField || !((WallField) this.fields[i - 1][j])
									.getWallInfo().isWallSouth())) {
						this.fields[i][j].setNorth(this.fields[i - 1][j]);
					}
				}

				// östliche Verlinkung setzen
				if (j != this.fields[i].length - 1) {
					if ((this.fields[i][j] instanceof HoleField || !((WallField) this.fields[i][j])
							.getWallInfo().isWallEast())
							&& (this.fields[i][j + 1] instanceof HoleField || !((WallField) this.fields[i][j + 1])
									.getWallInfo().isWallWest())) {
						this.fields[i][j].setEast(this.fields[i][j + 1]);
					}
				}

				// südliche Verlinkung setzen
				if (i != this.fields.length - 1) {
					if ((this.fields[i][j] instanceof HoleField || !((WallField) this.fields[i][j])
							.getWallInfo().isWallSouth())
							&& (this.fields[i + 1][j] instanceof HoleField || !((WallField) this.fields[i + 1][j])
									.getWallInfo().isWallNorth())) {
						this.fields[i][j].setSouth(this.fields[i + 1][j]);
					}
				}

				// westliche Verlinkung setzen
				if (j != 0) {
					if ((this.fields[i][j] instanceof HoleField || !((WallField) this.fields[i][j])
							.getWallInfo().isWallWest())
							&& (this.fields[i][j - 1] instanceof HoleField || !((WallField) this.fields[i][j - 1])
									.getWallInfo().isWallEast())) {
						this.fields[i][j].setWest(this.fields[i][j - 1]);
					}
				}

				// 3.

				if (this.fields[i][j] instanceof StartField) {
					this.startFieldList.add((StartField) this.fields[i][j]);
				} else if (this.fields[i][j] instanceof RepairField) {
					this.repairFieldList.add((RepairField) this.fields[i][j]);
				} else if (this.fields[i][j] instanceof CheckpointField) {
					this.checkpointFieldList
							.add((CheckpointField) this.fields[i][j]);
				}

				if (this.fields[i][j] instanceof LaserCannonField) {

					if (((LaserCannonField) this.fields[i][j]).hasLaserCannons()) {
						this.laserCannonFieldList.add((LaserCannonField) this.fields[i][j]);
					}

					if (this.fields[i][j] instanceof GearField) {
						this.gearFieldList.add((GearField) this.fields[i][j]);
					} else if (this.fields[i][j] instanceof PusherField) {
						this.pusherFieldList.add((PusherField) this.fields[i][j]);
					} else if (this.fields[i][j] instanceof CompactorField) {
						this.compactorFieldList.add((CompactorField) this.fields[i][j]);
					} else if (this.fields[i][j] instanceof ConveyorBeltField) {
						if (((ConveyorBeltField) this.fields[i][j]).getRange() == 1) {
							this.ConveyorBeltFieldRangeOneList.add((ConveyorBeltField) this.fields[i][j]);
						} else {
							this.ConveyorBeltFieldRangeTwoList.add((ConveyorBeltField) this.fields[i][j]);
						}
					}
				}

			}
		}		
		
		
		/*
		 * 
		 * Auf Sinn testen
		 * 
		 */
		
		// Anzahl der Startfelder testen
		if (this.startFieldList.size() < 2) {
			throw new LessThanTwoStartFieldsException();
		}

		// Startfeldliste sortieren
		Collections.sort(this.startFieldList, new Comparator<StartField>() {
			@Override
			public int compare(StartField o1, StartField o2) {
				return o1.getStartNumber() - o2.getStartNumber();
			}
		});
		// Startfeldliste durchlaufen und Nummerierung überprüfen
		int startNumber = 1;
		for (final StartField startField : this.startFieldList) {
			if (startField.getStartNumber() != startNumber++) {
				throw new StartFieldNumeratedException();
			}
		}

		// Anzahl der Checkpointfelder prüfen
		this.numberOfCheckpoints = this.checkpointFieldList.size();
		if (this.checkpointFieldList.size() == 0) {
			throw new NoCheckointFieldException();
		}

		// Checkpointfeldliste sortieren
		Collections.sort(this.checkpointFieldList,
				new Comparator<CheckpointField>() {
					@Override
					public int compare(CheckpointField o1, CheckpointField o2) {
						return o1.getNumberOfCheckpoint()
								- o2.getNumberOfCheckpoint();
					}
				});
		// Nummerierung der Checkpointfelder prüfen
		int numberOfCheckpoint = 1;
		for (final CheckpointField checkPointField : this.checkpointFieldList) {
			if (checkPointField.getNumberOfCheckpoint() != numberOfCheckpoint++) {
				throw new CheckpointFieldNumeratedExecption();
			}
		}

		// Pusher überprüfen
		for (final PusherField pusherField : this.pusherFieldList) {
			final Direction direction = Direction.getDirection(
					pusherField.getDirection(), RobotMovement.TURN_AROUND);
			final Field field = pusherField.getField(direction);
			if (field != null && field instanceof PusherField
					&& ((PusherField) field).getDirection() == direction) {
				throw new PusherFieldException(pusherField.getJ(), pusherField.getI());
			}
		}

		/*
		 * Compactor überprüfen
		 */
		for (final CompactorField compactorField : this.compactorFieldList) {
			// Vorderes Feld überprüfen
			final Direction outDirection = compactorField.getDirection();

			Field next = compactorField.getField(outDirection);
			if (next == null
					|| !(next instanceof ConveyorBeltField)
					|| !((ConveyorBeltField) next).getArrowInDirections()
							.contains(
									Direction.getDirection(outDirection,
											RobotMovement.TURN_AROUND))) {

				throw new CompactorFieldException(compactorField.getJ(), compactorField.getI(), false);
			}

			next = compactorField.getField(Direction.getDirection(outDirection,
					RobotMovement.TURN_AROUND));
			if (next == null
					|| !(next instanceof ConveyorBeltField)
					|| ((ConveyorBeltField) next).getArrowOutDirection() != outDirection) {

				throw new CompactorFieldException(compactorField.getJ(), compactorField.getI(), true);
			}
		}

		/*
		 * Förderbänder mit reichweite "1" überprüfen
		 */
		for (final ConveyorBeltField conveyorBeltField : this.ConveyorBeltFieldRangeOneList) {
			if (conveyorBeltField.isMerging()) {
				for (final Direction direction : conveyorBeltField
						.getArrowInDirections()) {
					final Field field = conveyorBeltField.getField(direction);
					if (field == null
							|| !(field instanceof ConveyorBeltField)
							|| ((ConveyorBeltField) field).getRange() != 1
							|| ((ConveyorBeltField) field)
									.getArrowOutDirection() != Direction
									.getDirection(direction,
											RobotMovement.TURN_AROUND)) {

						throw new ConveyorBeltsInconsistentException(
								"Reißverschlussförderband("
										+ conveyorBeltField.getJ()
										+ ", "
										+ conveyorBeltField.getI()
										+ ") befindet sich in einem inkonsistenten Zustand!");
					}
				}
			} else {
				final Field next = conveyorBeltField.getField(conveyorBeltField
						.getArrowOutDirection());

				if (next != null
						&& next instanceof ConveyorBeltField
						&& (((ConveyorBeltField) next).getRange() != 1 || ((ConveyorBeltField) next)
								.getArrowOutDirection() == Direction
								.getDirection(conveyorBeltField
										.getArrowOutDirection(),
										RobotMovement.TURN_AROUND))) {

					if (((ConveyorBeltField) next).getRange() != 1) {
						throw new ConveyorBeltsInconsistentException(
								"Die Reichweite des Förderbands("
										+ conveyorBeltField.getJ()
										+ ", "
										+ conveyorBeltField.getI()
										+ ") entspricht nicht der seines Nachfolgers (Reichweite = 2)!");
					}

					if (((ConveyorBeltField) next).getArrowOutDirection() == Direction
							.getDirection(
									conveyorBeltField.getArrowOutDirection(),
									RobotMovement.TURN_AROUND)) {

						throw new ConveyorBeltsInconsistentException(
								"Das Förderband(" + conveyorBeltField.getJ()
										+ ", " + conveyorBeltField.getI()
										+ ") und das Förderband(" + next.getJ()
										+ ", " + next.getI()
										+ ") laufen frontal aufeinander zu!");
					}
				}
			}
		}

		/*
		 * Förderbänder mit reichweite "2" überprüfen
		 */
		for (ConveyorBeltField conveyorBeltField : this.ConveyorBeltFieldRangeTwoList) {
			if (conveyorBeltField.isMerging()) {
				for (final Direction direction : conveyorBeltField
						.getArrowInDirections()) {
					final Field field = conveyorBeltField.getField(direction);
					if (field == null
							|| !(field instanceof ConveyorBeltField)
							|| ((ConveyorBeltField) field).getRange() != 2
							|| ((ConveyorBeltField) field)
									.getArrowOutDirection() != Direction
									.getDirection(direction,
											RobotMovement.TURN_AROUND)) {

						throw new ConveyorBeltsInconsistentException(
								"Reißverschlussförderband("
										+ conveyorBeltField.getJ()
										+ ", "
										+ conveyorBeltField.getI()
										+ ") befindet sich in einem inkonsistenten Zustand!");
					}
				}
			} else {
				final Field next = conveyorBeltField.getField(conveyorBeltField
						.getArrowOutDirection());

				if (next != null
						&& next instanceof ConveyorBeltField
						&& (((ConveyorBeltField) next).getRange() != 1 || ((ConveyorBeltField) next)
								.getArrowOutDirection() == Direction
								.getDirection(conveyorBeltField
										.getArrowOutDirection(),
										RobotMovement.TURN_AROUND))) {

					if (((ConveyorBeltField) next).getRange() != 2) {
						throw new ConveyorBeltsInconsistentException(
								"Die Reichweite des Förderbands("
										+ conveyorBeltField.getJ()
										+ ", "
										+ conveyorBeltField.getI()
										+ ") entspricht nicht der seines Nachfolgers (Reichweite = 1)!");
					}

					if (((ConveyorBeltField) next).getArrowOutDirection() == Direction
							.getDirection(
									conveyorBeltField.getArrowOutDirection(),
									RobotMovement.TURN_AROUND)) {

						throw new ConveyorBeltsInconsistentException(
								"Das Förderband(" + conveyorBeltField.getJ()
										+ ", " + conveyorBeltField.getI()
										+ ") und das Förderband(" + next.getJ()
										+ ", " + next.getI()
										+ ") laufen frontal aufeinander zu!");
					}
				}
			}
		}

		/*
		 * Erreichbarkeit von Checkpoint- und Repairfeldern überprüfen
		 */
		final WayPointComputer wayPointComputer = new WayPointComputer(
				this.fields, null);
		wayPointComputer.findWaysToRepairFields();
		wayPointComputer.findWaysToCheckpoints();
		
		/*
		 * maxPlayers wird gesetzt
		 */
		this.maxPlayers = this.startFieldList.size();

		/*
		 * Anzahl der Förderbänder wird gesetzt
		 */
		this.numberOfConveyorBeltFields = this.ConveyorBeltFieldRangeOneList.size() + this.ConveyorBeltFieldRangeTwoList.size();
		
		/*
		 * Anzahl der Pressen wird gesetzt
		 */
		this.numberOfCompactorFields = this.compactorFieldList.size();
		
		/*
		 * Anzahl der Schieber wird gesetzt
		 */
		this.numberOfPusherFields = this.pusherFieldList.size();
		
		/*
		 * Anzahl der Zahnräder wird gesetzt
		 */
		this.numberOfGearFields = this.gearFieldList.size();
		
		/*
		 * Anzahl der Laserkanonenfeldern wird gesetzt
		 */
		this.numberOfLaserCannonFields = this.laserCannonFieldList.size();
	}

	/**
	 * Setzt die Beschreibung des Spielbrettes
	 * 
	 * @param description
	 *            neue Beschreibung des Spielbrettes
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Setzt den Schwierigkeitsgrad
	 * 
	 * @param difficulty
	 *            the difficulty to set
	 */
	public final void setDifficulty(final String difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Setzt die Fielder des Spielbrettes
	 * 
	 * @param fields
	 *            Felder des Spielbrettes
	 */
	public final void setFields(final Field[][] fields) {
		this.fields = fields;
	}

	/**
	 * Setzt die übergebene Höhe für das PlayingBoard
	 * 
	 * @param height
	 *            neue Höhe für das PlayingBoard
	 */
	public final void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Setzt die übergebene ID für das PlayingBoard
	 * 
	 * @param id
	 *            neue ID des PlayingBoards
	 */
	public final void setID(final int id) {
		this.id = id;
	}

	/**
	 * Setzt den Dateinamen des Vorschaubildes
	 * 
	 * @param imageFileName
	 *            Dateinamen des Vorschaubildes
	 */
	public final void setImageFileName(final String imageFileName) {
		this.imageFileName = imageFileName;
	}

	/**
	 * Setzt die maximale Anzahl der Spieler
	 * 
	 * @param maxPlayers
	 *            die maximale Anzahl der Spieler
	 */
	public final void setMaxPlayers(final int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	/**
	 * Setzt den übergebenen Namen für das PlayingBoard
	 * 
	 * @param name
	 *            neuer Name des PlayingBoards
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Setzt den Nickname des PlayingBoard Erstellers.
	 * 
	 * @param nickname
	 */
	public final void setNickname(final String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Setzt die übergebene Anzahl an Checkpoints für das PlayingBoard
	 * 
	 * @param numberOfCheckpoints
	 *            neue Anzahl an Checkpoints für das PlayingBoard
	 */
	public final void setNumberOfCheckpoints(final int numberOfCheckpoints) {
		this.numberOfCheckpoints = numberOfCheckpoints;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public final void setUserID(final int userID) {
		this.userID = userID;
	}

	/**
	 * Setzt die übergebene Breite für das PlayingBoard
	 * 
	 * @param width
	 *            neue Breite für das PlayingBoard
	 */
	public final void setWidth(final int width) {
		this.width = width;
	}

	public int getNumberOfConveyorBeltFields() {
		return numberOfConveyorBeltFields;
	}

	public void setNumberOfConveyorBeltFields(int numberOfConveyorBeltFields) {
		this.numberOfConveyorBeltFields = numberOfConveyorBeltFields;
	}

	public int getNumberOfCompactorFields() {
		return numberOfCompactorFields;
	}

	public void setNumberOfCompactorFields(int numberOfCompactorFields) {
		this.numberOfCompactorFields = numberOfCompactorFields;
	}

	public int getNumberOfPusherFields() {
		return numberOfPusherFields;
	}

	public void setNumberOfPusherFields(int numberOfPusherFields) {
		this.numberOfPusherFields = numberOfPusherFields;
	}

	public int getNumberOfGearFields() {
		return numberOfGearFields;
	}

	public void setNumberOfGearFields(int numberOfGearFields) {
		this.numberOfGearFields = numberOfGearFields;
	}

	public int getNumberOfLaserCannonFields() {
		return numberOfLaserCannonFields;
	}

	public void setNumberOfLaserCannonFields(int numberOfLaserCannonFields) {
		this.numberOfLaserCannonFields = numberOfLaserCannonFields;
	}

	
}
