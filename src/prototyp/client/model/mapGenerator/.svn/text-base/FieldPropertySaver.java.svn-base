package prototyp.client.model.mapGenerator;

import java.util.HashSet;
import java.util.Set;

import prototyp.shared.util.Direction;

/**
 * Klasse, die die letzten Eigenschaften der Feldtypen speichert
 * @author Marcus
 *
 */
public class FieldPropertySaver {
	
	/** letzte Aktivität einer Presse */
	private boolean lastCompactorFieldActivity = false;

	/** letzte Richtung einer Presse */
	private Direction lastCompactorFieldDirection = Direction.SOUTH;

	/** letzte Aktivität eines Schubserfeldes */
	private boolean lastPusherFieldActivity = false;

	/** letzte Richtung eines Schubserfeldes */
	private Direction lastPusherFieldDirection = Direction.SOUTH;
	
	/** letzte Drehrichtung eines Zahnradfeldes */
	private boolean lastGearFieldClockwise = true;
	
	/** letzter Reparaturwert für ein Reparaturfeld */
	private int lastRepairFieldValue = 1;

	/** letzer Vorwärtswert für ein Förderband */
	private int lastConveyorbeltFieldRange = 1;

	/** die letzen Eingangsrichtungungen für ein Förderband */
	private Set<Direction> lastConveyorbeltFieldInDirections = new HashSet<Direction>();

	/** letzte Ausgangsrichtung für ein Förderband */
	private Direction lastConveyorbeltFieldOutDirection = Direction.SOUTH;
	
	/** letzte RestartRichtung für ein Startfeld */
	private Direction lastStartFieldRestartDirection = Direction.SOUTH;
	/**
	 * Konstruktor
	 */
	public FieldPropertySaver() {
		this.lastConveyorbeltFieldInDirections.add(Direction.NORTH);
	}
	
	
	

	/**
	 * Liefert die letze Aktivität einer Presse
	 * @return true, wenn die Presse nur im ersten und letzten Spielschritt aktiv ist
	 * 	false, wenn die Presse in jedem Spielschritt aktiv ist
	 */
	public boolean isLastCompactorFieldActivity() {
		return lastCompactorFieldActivity;
	}

	/**
	 * Setzt die letze Aktivität einer Presse
	 * @param lastCompactorFieldActivity true, wenn die Presse nur im ersten und letzten Spielschritt aktiv ist
	 * 	false, wenn die Presse in jedem Spielschritt aktiv ist
	 */
	public void setLastCompactorFieldActivity(boolean lastCompactorFieldActivity) {
		this.lastCompactorFieldActivity = lastCompactorFieldActivity;
	}

	/**
	 * Liefert die letzte Richtung einer Presse
	 * @return die letzte Richtung der Presse
	 */
	public Direction getLastCompactorFieldDirection() {
		return lastCompactorFieldDirection;
	}

	/**
	 * Setzt die letzte Richtung einer Presse
	 * @param lastCompactorFieldDirection 
	 * 				die letzte Richtung der Presse
	 */
	public void setLastCompactorFieldDirection(Direction lastCompactorFieldDirection) {
		this.lastCompactorFieldDirection = lastCompactorFieldDirection;
	}

	/**
	 * Liefert die letze Aktivität eines Schubserfeldes
	 * @return true, wenn das Schubserfeld nur in ungeraden Spielschritten aktiv ist
	 * 	false, wenn das Schubserfeld in jedem Spielschritt aktiv ist
	 */
	public boolean isLastPusherFieldActivity() {
		return lastPusherFieldActivity;
	}

	/**
	 * Setzt die letze Aktivität eines Schubserfeldes
	 * @param lastPusherFieldActivity
	 * 		true, wenn das Schubserfeld nur in ungeraden Spielschritten aktiv ist
	 * 		false, wenn das Schubserfeld in jedem Spielschritt aktiv ist
	 */
	public void setLastPusherFieldActivity(boolean lastPusherFieldActivity) {
		this.lastPusherFieldActivity = lastPusherFieldActivity;
	}

	/**
	 * Liefert die letzte Richtung eines Schubserfeldes
	 * @return die letzte Richtung eines Schubserfeldes
	 */
	public Direction getLastPusherFieldDirection() {
		return lastPusherFieldDirection;
	}

	/**
	 * Setzt die letzte Richtung eines Schubserfeldes
	 * @param lastPusherFieldDirection 
	 * 		die letzte Richtung eines Schubserfeldes
	 */
	public void setLastPusherFieldDirection(Direction lastPusherFieldDirection) {
		this.lastPusherFieldDirection = lastPusherFieldDirection;
	}

	/**
	 * Liefert die letzte Drehrichtung eines Zahnradfeldes
	 * @return
	 * 		true, wenn das Zahnrad sich im Uhrzeigersinn dreht
	 * 		false,  wenn das Zahnrad sich gegen den Uhrzeigersinn dreht
	 */
	public boolean isLastGearFieldClockwise() {
		return lastGearFieldClockwise;
	}

	/**
	 * Setzt die letzte Drehrichtung eines Zahnradfeldes
	 * @param lastGearFieldClockwise 
	 * 		true, wenn das Zahnrad sich im Uhrzeigersinn dreht
	 * 		false,  wenn das Zahnrad sich gegen den Uhrzeigersinn dreht
	 */		
	public void setLastGearFieldClockwise(boolean lastGearFieldClockwise) {
		this.lastGearFieldClockwise = lastGearFieldClockwise;
	}

	/**
	 * Liefert den letzten Reparaturwert eines Reparaturfeldes
	 * @return 
	 * 		den letzten Reparaturwert eines Reparaturfeldes
	 */
	public int getLastRepairFieldValue() {
		return lastRepairFieldValue;
	}

	/**
	 * Setzt den letzten Reparaturwert eines Reparaturfeldes
	 * @param lastRepairFieldValue
	 * 		den letzten Reparaturwert eines Reparaturfeldes
	 */
	public void setLastRepairFieldValue(int lastRepairFieldValue) {
		this.lastRepairFieldValue = lastRepairFieldValue;
	}

	/**
	 * liefert die letzte Reichweite eines Förderbandes
	 * @return
	 * 		die letzte Reichweite eines Förderbandes
	 */
	public int getLastConveyorbeltFieldRange() {
		return lastConveyorbeltFieldRange;
	}

	/**
	 * Setzt die letzte Reichweite eines Förderbandes
	 * @param lastConveyorbeltFieldRange
	 * 		die letzte Reichweite eines Förderbandes
	 */
	public void setLastConveyorbeltFieldRange(int lastConveyorbeltFieldRange) {
		this.lastConveyorbeltFieldRange = lastConveyorbeltFieldRange;
	}

	/**
	 * Liefert die letzten Eingangsrichtungen eines Förderbandes
	 * @return
	 * 		die letzten Eingangsrichtungen eines Förderbandes
	 */
	public Set<Direction> getLastConveyorbeltFieldInDirections() {
		return lastConveyorbeltFieldInDirections;
	}

	/**
	 * Setzt die letzten Eingangsrichtungen eines Förderbandes
	 * @param lastConveyorbeltFieldInDirections
	 * 		die letzten Eingangsrichtungen eines Förderbandes
	 */
	public void setLastConveyorbeltFieldInDirections(
			Set<Direction> lastConveyorbeltFieldInDirections) {
		this.lastConveyorbeltFieldInDirections = lastConveyorbeltFieldInDirections;
	}

	/**
	 * Liefert die letzte Ausgangsrichtung eines Förderbandes
	 * @return
	 * 		die letzte Ausgangsrichtung eines Förderbandes
	 */
	public Direction getLastConveyorbeltFieldOutDirection() {
		return lastConveyorbeltFieldOutDirection;
	}

	/**
	 * Setzt die letzte Ausgangsrichtung eines Förderbandes
	 * @param lastConveyorbeltFieldOutDirection
	 * 		die letzte Ausgangsrichtung eines Förderbandes
	 */
	public void setLastConveyorbeltFieldOutDirection(
			Direction lastConveyorbeltFieldOutDirection) {
		this.lastConveyorbeltFieldOutDirection = lastConveyorbeltFieldOutDirection;
	}



	/**
	 * Liefert die letzte Restartrichtung eines Startfeldes
	 * @return
	 * 		die letzte Restartrichtung eines Startfeldes
	 */
	public Direction getLastStartFieldRestartDirection() {
		return lastStartFieldRestartDirection;
	}

	/**
	 * Setzt die letzte Restartrichtung eines Startfeldes
	 * @param lastStartFieldRestartDirection
	 * 		die letzte Restartrichtung eines Startfeldes
	 */
	public void setLastStartFieldRestartDirection(
			Direction lastStartFieldRestartDirection) {
		this.lastStartFieldRestartDirection = lastStartFieldRestartDirection;
	}
	
	
	
	
	
}
