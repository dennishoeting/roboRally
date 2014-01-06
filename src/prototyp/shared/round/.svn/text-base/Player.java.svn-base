package prototyp.shared.round;

import java.util.ArrayList;
import java.util.List;

import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.useradministration.User;

/**
 * Der Spieler in einer Spielrunde
 * 
 * @author Marcus, Andreas, Mischa
 * @version 1.0
 * @version 1.1 Interface entfernt(Marcus)
 * @version 1.2 (23.9.10) ready-Funktionalität in den Player verschoben, es
 *          macht wenig Sinn, auf den Watcher zu warten --Jannik
 * @version 1.3 (26.09.10) ein Spieler hat Spielkarten auf der Hand
 * @version 1.4 JavaDoc und Color hinzugefügt --Andreas
 * @version 1.5 (27.10.10) Attribut Robot hinzugefügt -Timo
 * @version 1.6 (9.11.10) Anzahl der möglichen Programmierkarten wird nun aus
 *          dem Roboter ausgelesen - Marcus
 */
public class Player extends Watcher {

	/** Seriennummer */
	private static final long serialVersionUID = -1315268196531555675L;

	/** Ein Spieler hat Spielkarten auf der Hand */
	private List<Programmingcard> cards;

	/** Gibt an, ob der Spieler bereit ist. */
	private boolean isReady = false;

	/** Roboter des Players, wird beim Erzeugen des Spielers gestetzt. */
	private Robot robot;

	/**
	 * Default-Konstruktor
	 */
	public Player() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param user
	 *            Zugehöriger Nutzer
	 */
	public Player(User user) {
		super(user);
		this.robot = new Robot();
	}

	/**
	 * Liefert die Programmierkarten des Spielers
	 * 
	 * @return cardsOnHand
	 */
	public List<Programmingcard> getCards() {
		return this.cards;
	}

	/**
	 * Liefert die Anzahl der Karten, die einem Spieler ausgeteilt werden dürfen
	 * 
	 * @return die Anzahl der ausgeteilten Karten
	 */
	public int getNumberOfAllowedGivenCards() {
		return 9 - this.robot.getDamageToken();
	}

	/**
	 * Liefert die Anzahl der Karten, die gelegt werden dürfen
	 */
	public int getNumberOfAllowedSetCards() {
		return 5;
	}

	/**
	 * Liefert die Roboter Instanz des Players. Wird erst nach dem Starten der
	 * Runde gesetzt.
	 * 
	 * @return
	 */
	public Robot getRobot() {
		return this.robot;
	}

	/**
	 * Gibt an, ob der Beobachter/Spieler bereit zum Spielen ist.
	 * 
	 * @return true, falls der Beobachter/Spieler bereit ist.
	 */
	public boolean isReady() {
		return this.isReady;
	}

	/**
	 * Setzt die Programmierkarten des Spielers
	 * 
	 * @param cardsOnHand
	 *            Programmierkarten des Spielers
	 */
	public void setCards(ArrayList<Programmingcard> cardsOnHand) {
		this.cards = cardsOnHand;
	}

	/**
	 * Setzt die Angabe für das Legen der Porgrammierkarten
	 * 
	 * @param cardsSet
	 *            Gibt an, ob der Spieler seine Programmierkarten gelegt hat.
	 */
	public void setCards(List<Programmingcard> cards) {
		this.cards = cards;
	}

	/**
	 * Setzt die gelegten Programmierkarten des Spielers
	 * 
	 * @param cardsOnHand
	 *            Gelegte Programmierkarten des Spielers
	 */
	public void setCardsOnHand(ArrayList<Programmingcard> cardsOnHand) {
		this.cards = cardsOnHand;
	}

	/**
	 * Setzt den Status eines Beobachters/Spielers
	 * 
	 * @param isReady
	 *            Status des Beobachters/Spielers
	 */
	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	/**
	 * Setzt die Roboter Instanz für den Player. Wird vom RoundManager beim
	 * Starten der Runde aufgerufen.
	 * 
	 * @param robot
	 * @return
	 */
	public boolean setRobot(Robot robot) {
		this.robot = robot;
		return true;
	}

}
