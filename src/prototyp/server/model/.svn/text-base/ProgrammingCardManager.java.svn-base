package prototyp.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.util.RobotMovement;

/**
 * Klasse zum Verwalten der Spielkarten (u.a Mischen, Verteilen)
 * 
 * @author Marcus
 * @version 1.1
 */
public class ProgrammingCardManager {

	/** der Kartenstapel */
	private final Stack<Programmingcard> cards = new Stack<Programmingcard>();
	
	/** KartenStapel, der nur Bewegungskarten enthält */
	private final Stack<Programmingcard> moveCards = new Stack<Programmingcard>();

	/** abgelegte Karten */
	private final Stack<Programmingcard> used = new Stack<Programmingcard>();
	
	/** abgelegte Karten nur für Bewegung */
	private final Stack<Programmingcard> moveUsed = new Stack<Programmingcard>();

	/** Set für die Prioritäten */
	private final Set<Integer> priorities = new HashSet<Integer>();

	/**
	 * Konstruktor
	 */
	public ProgrammingCardManager() {

		/*
		 * Karten werden erstellt und auf den Stapel gebracht.
		 */
		createCards();

		/*
		 * Karten mischen
		 */
		mixCards(this.cards);
		mixCards(this.moveCards);
	}

	/**
	 * Liefert die Karten
	 * 
	 * @return List<Programmingcard>
	 */
	public List<Programmingcard> getCards() {

		/*
		 * Kartenliste für einen Spieler erstellen, die später zurückgelierfert wird
		 */
		final List<Programmingcard> cardsList = new ArrayList<Programmingcard>(9);
		
		/*
		 * 
		 * die erste Karte muss eine Bewegungskarte sein
		 * deshalb wird sie von moveCards genommen. Beim Client wird 
		 * die Liste gemischt, dass sie nicht immer an erster Stelle steht
		 * 
		 */
		
		/*
		 *  Wenn der KartenStapel leer ist
		 */
		if(this.moveCards.empty()) {
			/*
			 * Werden die Stapel vertauscht und neu gemixt
			 */
			this.moveCards.addAll(this.moveUsed);
			this.moveUsed.clear();
			mixCards(this.moveCards);
		}
		
		/*
		 * Programmierkarte vom Stapel nehmen
		 */
		Programmingcard programmingcard = this.moveCards.pop();
		
		/*
		 * die Karte zur KartenListe des Spielers hinzufügen
		 */
		cardsList.add(programmingcard);
		
		/* 
		 * die verbrauchte Karte auf used legen 
		 */
		this.moveUsed.push(programmingcard);
		
		
		/*
		 * Die restlichen 8 Karten werden vom normalen Stapel genommen
		 */
		for (int i = 0; i < 8; i++) {
			
			/*
			 *  Wenn der KartenStapel leer ist
			 */
			if (this.cards.empty()) {
				
				/*
				 * Werden die Stapel vertauscht und neu gemixt
				 */
				this.cards.addAll(this.used);
				this.used.clear();
				mixCards(this.cards);
			}

			/*
			 * Programmierkarte vom Stapel nehmen
			 */
			programmingcard = this.cards.pop();
			
			/*
			 * die Karte zur KartenListe des Spielers hinzufügen
			 */
			cardsList.add(programmingcard);
			
			/* 
			 * die verbrauchte Karte auf used legen 
			 */
			this.used.push(programmingcard);
		}
		return cardsList;
	}

	/**
	 * Permutiert den Kartenstapel
	 */
	public void mixCards(final Stack<Programmingcard> cardsStack) {
		/*
		 * Liste mischen
		 */
		for (int i = 0; i < cardsStack.size(); i++) {
			final int random = (int) (Math.random() * cardsStack.size());
			final Programmingcard card = cardsStack.set(i, cardsStack.get(random));
			cardsStack.set(random, card);
		}
	}

	/**
	 * Erstellt die Karten
	 */
	private void createCards() {
		// One-Forward
		for (int i = 0; i < 30; i++) {
			this.cards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_FORWARDS));
		}

		// Two-Forward
		for (int i = 0; i < 30; i++) {
			this.cards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_TWO_FORWARDS));
		}

		// Three-Forward
		for (int i = 0; i < 30; i++) {
			this.cards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_THREE_FORWARDS));
		}

		// One Backward
		for (int i = 0; i < 30; i++) {
			this.cards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_BACKWARDS));
		}

		// turn left
		for (int i = 0; i < 30; i++) {
			this.cards.push(new Programmingcard(getPriority(), RobotMovement.TURN_LEFT));
		}

		// turn right
		for (int i = 0; i < 30; i++) {
			this.cards.push(new Programmingcard(getPriority(), RobotMovement.TURN_RIGHT));
		}

		// turn Around
		for (int i = 0; i < 30; i++) {
			this.cards.push(new Programmingcard(getPriority(), RobotMovement.TURN_AROUND));
		}
		
		
		/*
		 * Jetzt wird der Stapel für NUR Vorwärtskarten mit Vorwärtskarten gefüllt
		 */
		// One-Forward
		for (int i = 0; i < 5; i++) {
			this.moveCards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_FORWARDS));
		}

		// Two-Forward
		for (int i = 0; i < 5; i++) {
			this.moveCards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_TWO_FORWARDS));
		}

		// Three-Forward
		for (int i = 0; i < 5; i++) {
			this.moveCards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_THREE_FORWARDS));
		}

		// One Backward
		for (int i = 0; i < 5; i++) {
			this.moveCards.push(new Programmingcard(getPriority(), RobotMovement.MOVE_BACKWARDS));
		}
	}

	/**
	 * Liefert eine Priorität
	 * 
	 * @return zufallszahl
	 */
	private int getPriority() {
		while (true) {
			final int random = (int) (Math.random() * 999) + 1;
			if (!priorities.contains(random)) {
				priorities.add(random);

				return random;
			}
		}
	}
}
