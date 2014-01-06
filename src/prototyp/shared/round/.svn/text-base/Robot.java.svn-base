package prototyp.shared.round;

import java.io.Serializable;

import prototyp.shared.field.BackupField;
import prototyp.shared.util.Color;
import prototyp.shared.util.Direction;
import prototyp.shared.util.Position;

/**
 * Roboter
 * 
 * @author Marcus, Andreas
 * @version 1.0
 * @version 1.1 Position eingefügt(Marcus)
 * @version 1.2 Konstruktor geändert(Marcus)
 * @version 1.3 kleinere Änderungen(Marcus)
 * @version 1.4 Robot kennt nun seinen Spieler
 */
public class Robot implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = 1883311345376915962L;

	/** die Farbe des Roboters */
	private Color color;

	/** die Schadenspunkte, die ein Roboter hat */
	private int damageToken = 0;

	/** Blickrichtung des Roboters */
	private Direction direction;

	/** die Lebenspunkte, die ein Roboter hat */
	private int lifePoints = 3;

	/**
	 * die Anzahl der bereits erreichten Checkpoints beim Initialisieren immer 0
	 */
	private int numberOfReachedCheckoints = 0;

	/** i-Index des Roboters */
	private int i;

	/** j-Index des Roboters */
	private int j;

	/**
	 * Gibt an, ob ein Roboter für einen Spielzug abgeschaltet ist. Steht
	 * standardmäßig auf 0.
	 */
	private int powerDown = 0;

	/** das Restart-Field für den Roboter */
	private BackupField startField;

	/** der Spieler, der zum Roboter gehört */
	private Player player;

	/** die Anzahl der Programmierkarten, die einem Spieler zustehen */
	private int numberOfFreeCardSlots = 5;

	/** gibt an, ob der Roboter für den jeweiligen Spielzug tot ist */
	private boolean deadForTurn = false;

	/** gibt an, ob der Roboter endgültig tot ist */
	private boolean dead = false;

	/** die Anzahl der Backupkarten */
	private int numberOfBackupCards = 1;

	/** die Punkte des Roboters */
	private int points;

	/*****
	 * 
	 * 
	 * Speicherungen für awards
	 * 
	 * ******/
	/** speichert die Anzahl der drehungen, die ein Roboter gemacht hat */
	private int numberOfTurns = 0;

	/** speichert die Anzahl der Tode */
	private int numberOfDeaths = 0;

	/** speichert die Anzahl der Reparauren */
	private int numberOfRepairs = 0;

	/** speichert, wie viele Roboter verschoben wurden */
	private int numberOfPushedRobots = 0;

	/** Speichert die Vorwärts und Rückwärtsschritte */
	private int numberOfSteps = 0;

	/** Speichert die Anzahl der Roboter, die ins Loch geschoben wurden */
	private int numberOfRobotsPushedInHole = 0;

	/** Speichert wie oft mein Roboter durch Laser getroffen wurde */
	private int numberOfLaserHits = 0;

	/** Speichert wie viele Laser durch meinen Roboterlaser getroffen wurden */
	private int numberOfOtherRobotHit = 0;

	/** hat der Roboter den Award "Hamster" schon bekommen? */
	private boolean hasAwardHamster = false;

	/**
	 * hat der Roboter einen anderen Roboter schon auf ein Reparaturfeld
	 * geschoben?
	 */
	private boolean hasPushedOnRepairfield = false;

	/** hat der Roboter alle Arten von Progerammierkarten auf einmal erhalten? */
	private boolean hasRobotBeenPressed = false;

	/**
	 * Default-Konstruktor
	 */
	public Robot() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param x
	 *            X-Startkoordinate
	 * @param y
	 *            Y-Startkoordinate
	 * @param color
	 *            Roboterfarbe
	 * @param direction
	 *            Die Blickrichtung eines Roboters
	 */
	public Robot(final int i, final int j, final Color color,
			final Direction direction) {
		this.i = i;
		this.j = j;
		this.color = color;
		this.direction = direction;
	}

	/**
	 * Konstruktor
	 * 
	 * @param position
	 *            die Position eines Roboters
	 * @param color
	 *            die Farbe eines Roboters
	 * @param direction
	 *            die Blickrichtung eines Roboters
	 */
	public Robot(final Position position, final Color color,
			final Direction direction) {
		this(position.getI(), position.getJ(), color, direction);
	}

	/*
	 * hier folgen die Methoden der Roboter-Klasse
	 */

	/**
	 * @return the color
	 */
	public final Color getColor() {
		return this.color;
	}

	/**
	 * @return the damageToken
	 */
	public final int getDamageToken() {
		return this.damageToken;
	}

	/**
	 * Diese Methode liefert die Blickrichtung des Roboters
	 * 
	 * @return
	 */
	public final Direction getDirection() {
		return this.direction;
	}

	/**
	 * @return the x
	 */
	public final int getI() {
		return this.i;
	}

	/**
	 * Liefert den Bildpfad des Roboters
	 * 
	 * @return der Pfad des Roboterbilds
	 */
	public final String getImagePath() {
		return "images/robots/robot_" + this.color.name().toLowerCase() + "_"
				+ this.direction.name().toLowerCase() + ".png";
	}

	/**
	 * @return the y
	 */
	public final int getJ() {
		return this.j;
	}

	/**
	 * @return the lifePoints
	 */
	public final int getLifePoints() {
		return this.lifePoints;
	}

	/**
	 * Liefert die Anzahl der Karten, die an den Spieler ausgeteilt werden
	 * 
	 * @return
	 */
	public final int getNumberOfAvailableCards() {
		return 9 - this.damageToken;
	}

	/**
	 * @return the numberOfBackupCards
	 */
	public final int getNumberOfBackupCards() {
		return this.numberOfBackupCards;
	}

	public int getNumberOfDeaths() {
		return this.numberOfDeaths;
	}

	/**
	 * 
	 * @return
	 */
	public final int getNumberOfFreeCardSlots() {
		return this.numberOfFreeCardSlots;
	}

	public int getNumberOfLaserHits() {
		return this.numberOfLaserHits;
	}

	public int getNumberOfOtherRobotHit() {
		return this.numberOfOtherRobotHit;
	}

	public int getNumberOfPushedRobots() {
		return this.numberOfPushedRobots;
	}

	/**
	 * @return the numberOfReachedCheckoints
	 */
	public final int getNumberOfReachedCheckpoints() {
		return this.numberOfReachedCheckoints;
	}

	public int getNumberOfRepairs() {
		return this.numberOfRepairs;
	}

	public int getNumberOfRobotsPushedInHole() {
		return this.numberOfRobotsPushedInHole;
	}

	public int getNumberOfSteps() {
		return this.numberOfSteps;
	}

	public int getNumberOfTurns() {
		return this.numberOfTurns;
	}

	/**
	 * Liefert den Spieler, der zum Roboter gehört.
	 * 
	 * @param der
	 *            Spieler, der zum Roboter gehört
	 */
	public final Player getPlayer() {
		return this.player;
	}

	public int getPoints() {
		return this.points;
	}

	/**
	 * Diese Methode liefert die aktuelle Position eines Roboters auf dem
	 * Spielfeld
	 * 
	 * @return
	 */
	public final Position getPosition() {
		return new Position(this.i, this.j);
	}

	/**
	 * @return the powerDown 0 : Wenn kein PowerDown aktiviert oder angekündikt
	 *         wurde 1 : Wenn ein PowerDown für den nächsten Spielzug
	 *         angekündigt wurde >=2 : PowerDown ist aktiviert
	 */
	public final int getPowerDown() {
		return this.powerDown;
	}

	public final String getPreviewImagePath() {
		return "robots/robot_" + this.color.name().toLowerCase() + ".png";
	}

	/**
	 * @return the restartField
	 */
	public final BackupField getStartField() {
		return this.startField;
	}

	/**
	 * @return the dead
	 */
	public final boolean isDead() {
		return this.dead;
	}

	/**
	 * @return the dead
	 */
	public final boolean isDeadForTurn() {
		return this.deadForTurn;
	}

	public boolean isHasAwardHamster() {
		return this.hasAwardHamster;
	}

	public boolean isHasPushedOnRepairfield() {
		return this.hasPushedOnRepairfield;
	}

	public boolean isHasRobotBeenPressed() {
		return this.hasRobotBeenPressed;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public final void setColor(final Color color) {
		this.color = color;
	}

	/**
	 * @param damageToken
	 *            the damageToken to set
	 */
	public final boolean setDamageToken(int damageToken) {
		this.damageToken = damageToken;

		if (this.damageToken < 0) {
			this.damageToken = 0;
		}

		/*
		 * die Anzahl der verfügbaren Karten setzten
		 */
		if (damageToken < 5) {
			this.numberOfFreeCardSlots = 5;
		} else {
			this.numberOfFreeCardSlots = 9 - this.damageToken;
			if (this.damageToken >= 10) {
				this.damageToken = 10;
				return true;
			}
		}

		return false;
	}

	/**
	 * @param dead
	 *            the dead to set
	 */
	public final void setDead(final boolean dead) {
		this.dead = this.deadForTurn = dead;
	}

	/**
	 * @param dead
	 *            the dead to set
	 */
	public final void setDeadForTurn(final boolean dead) {
		this.deadForTurn = dead;
	}

	/**
	 * Setzt die Blickrichtung des Roboters
	 * 
	 * @param direction
	 *            die neue Blickrichtung des Roboters
	 */
	public final void setDirection(final Direction direction) {
		this.direction = direction;
	}

	public void setHasAwardHamster(boolean hasAwardHamster) {
		this.hasAwardHamster = hasAwardHamster;
	}

	public void setHasPushedOnRepairfield(boolean hasPushedOnRepairfield) {
		this.hasPushedOnRepairfield = hasPushedOnRepairfield;
	}

	public void setHasRobotBeenPressed(boolean hasRobotBeenPressed) {
		this.hasRobotBeenPressed = hasRobotBeenPressed;
	}

	/**
	 * Setzt den I-Index des Roboters neu
	 * 
	 * @param der
	 *            neue I-Index
	 */
	public final void setI(final int i) {
		this.i = i;
	}

	/**
	 * Setzt den J-Index des Roboters neu
	 * 
	 * @param der
	 *            neue J-Index
	 */
	public final void setJ(final int j) {
		this.j = j;
	}

	/**
	 * @param lifePoints
	 *            the lifePoints to set
	 */
	public final void setLifePoints(final int lifePoints) {
		this.lifePoints = lifePoints;
	}

	/**
	 * @param numberOfBackupCards
	 *            the numberOfBackupCards to set
	 */
	public final void setNumberOfBackupCards(final int numberOfBackupCards) {
		this.numberOfBackupCards = numberOfBackupCards;
	}

	public void setNumberOfDeaths(int numberOfDeaths) {
		this.numberOfDeaths = numberOfDeaths;
	}

	public void setNumberOfFreeCardSlots(int numberOfFreeCardSlots) {
		this.numberOfFreeCardSlots = numberOfFreeCardSlots;
	}

	public void setNumberOfLaserHits(int numberOfLaserHits) {
		this.numberOfLaserHits = numberOfLaserHits;
	}

	public void setNumberOfOtherRobotHit(int numberOfOtherRobotHit) {
		this.numberOfOtherRobotHit = numberOfOtherRobotHit;
	}

	public void setNumberOfPushedRobots(int numberOfPushedRobots) {
		this.numberOfPushedRobots = numberOfPushedRobots;
	}

	/**
	 * @param numberOfReachedCheckoints
	 *            the numberOfReachedCheckoints to set
	 */
	public final void setNumberOfReachedCheckoints(
			final int numberOfReachedCheckoints) {
		this.numberOfReachedCheckoints = numberOfReachedCheckoints;
	}

	public void setNumberOfRepairs(int numberOfRepairs) {
		this.numberOfRepairs = numberOfRepairs;
	}

	public void setNumberOfRobotsPushedInHole(int numberOfRobotsPushedInHole) {
		this.numberOfRobotsPushedInHole = numberOfRobotsPushedInHole;
	}

	public void setNumberOfSteps(int numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}

	public void setNumberOfTurns(int numberOfturns) {
		this.numberOfTurns = numberOfturns;
	}

	/**
	 * Setzt den Spieler, der zum Roboter gehört.
	 * 
	 * @param der
	 *            Spieler, der zum Roboter gehört
	 */
	public final void setPlayer(final Player player) {
		this.player = player;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Setzt die position des Roboters neu.
	 * 
	 * @param p
	 *            die neue position
	 */
	public final void setPosition(final Position p) {
		this.i = p.getI();
		this.j = p.getJ();
	}

	/**
	 * Setzt den Powerdown-Status des Roboters
	 * 
	 * @param * 0 : Wenn kein PowerDown aktiviert oder angekündikt wurde 1 :
	 *        Wenn ein PowerDown für den nächsten Spielzug angekündigt wurde >=2
	 *        : PowerDown ist aktiviert
	 */
	public final void setPowerDown(final int powerDown) {
		this.powerDown = powerDown;
	}

	/**
	 * @param restartField
	 *            the restartField to set
	 */
	public final void setStartField(final BackupField startField) {
		this.startField = startField;
	}

}
