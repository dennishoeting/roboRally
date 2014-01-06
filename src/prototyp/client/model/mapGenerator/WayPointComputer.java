package prototyp.client.model.mapGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prototyp.client.presenter.mapgenerator.MapGeneratorPresenter;
import prototyp.client.view.mapgenerator.MapGeneratorPage;
import prototyp.shared.exception.playingboard.CheckpointFieldNotReachableException;
import prototyp.shared.exception.playingboard.RepairFieldNotReachableExeption;
import prototyp.shared.field.CheckpointField;
import prototyp.shared.field.Field;
import prototyp.shared.field.HoleField;
import prototyp.shared.field.RepairField;
import prototyp.shared.field.StartField;
import prototyp.shared.field.WallField;
import prototyp.shared.util.Position;

/**
 * Dijkstra-Algorithmus. Berechnet den kürzesten Weg für den MapGenerator.
 * 
 * @author Marcus
 * @version 1.0
 * 
 * @see MapGeneratorPage
 */
public class WayPointComputer {

	/**
	 * Innere Klasse
	 * 
	 * @author Marcus
	 * @version 1.0
	 * 
	 */
	private static class Node {
		private final Position position;
		private int distance = Integer.MAX_VALUE;
		private Node precursor = null;
		private final ArrayList<Node> nodes = new ArrayList<Node>();

		private Node(Position p) {
			this.position = p;
		}
	}

	/** Array mit Knoten */
	private final Node[][] array;

	/** MapGeneratorPresenter */
	private final MapGeneratorPresenter observer;

	/** Liste mit den Startfeldern */
	private final List<StartField> startFieldList = new ArrayList<StartField>();

	/** Liste mit den Checkpointfeldern */
	private final List<CheckpointField> checkpointList = new ArrayList<CheckpointField>();

	/** Liste mit den Repairfeldern */
	private final List<RepairField> repairList = new ArrayList<RepairField>();

	/**
	 * Konstruktor
	 * 
	 * @param field
	 *            Spielbrett
	 * @param observer
	 *            MapGeneratorPresenter
	 */
	public WayPointComputer(final Field[][] field, final MapGeneratorPresenter observer) {

		this.observer = observer;

		this.array = new Node[field.length][field[0].length];

		for (int i = 0; i < this.array.length; i++) {
			for (int j = 0; j < this.array[i].length; j++) {
				this.array[i][j] = new Node(new Position(i, j));
			}
		}

		/*
		 * Felder mit Nachbarfelder verlinken
		 */
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {

				field[i][j].setI(i);
				field[i][j].setJ(j);

				if (field[i][j] instanceof StartField) {
					this.startFieldList.add((StartField) field[i][j]);
				} else if (field[i][j] instanceof CheckpointField) {
					this.checkpointList.add((CheckpointField) field[i][j]);
				} else if (field[i][j] instanceof RepairField) {
					this.repairList.add((RepairField) field[i][j]);
				}

				if (j + 1 < field[i].length) {
					if (!(field[i][j + 1] instanceof HoleField) && !((WallField) field[i][j + 1]).getWallInfo().isWallWest()
							&& !(field[i][j] instanceof HoleField) && !((WallField) field[i][j]).getWallInfo().isWallEast()) {
						this.array[i][j].nodes.add(this.array[i][j + 1]);
					}
				}
				if (j - 1 > -1) {
					if (!(field[i][j - 1] instanceof HoleField) && !((WallField) field[i][j - 1]).getWallInfo().isWallEast()
							&& !(field[i][j] instanceof HoleField) && !((WallField) field[i][j]).getWallInfo().isWallWest()) {
						this.array[i][j].nodes.add(this.array[i][j - 1]);
					}
				}
				if (i + 1 < field.length) {
					if (!(field[i + 1][j] instanceof HoleField) && !((WallField) field[i + 1][j]).getWallInfo().isWallNorth()
							&& !(field[i][j] instanceof HoleField) && !((WallField) field[i][j]).getWallInfo().isWallSouth()) {
						this.array[i][j].nodes.add(this.array[i + 1][j]);
					}
				}
				if (i - 1 > -1) {
					if (!(field[i - 1][j] instanceof HoleField) && !((WallField) field[i - 1][j]).getWallInfo().isWallSouth()
							&& !(field[i][j] instanceof HoleField) && !((WallField) field[i][j]).getWallInfo().isWallNorth()) {
						this.array[i][j].nodes.add(this.array[i - 1][j]);
					}
				}
			}
		}

		// Startfelder sortieren
		Collections.sort(this.startFieldList, new Comparator<StartField>() {
			@Override
			public int compare(StartField o1, StartField o2) {
				return o1.getStartNumber() - o2.getStartNumber();
			}
		});

		// Checkpointfelder sortieren
		Collections.sort(this.checkpointList, new Comparator<CheckpointField>() {
			@Override
			public int compare(CheckpointField o1, CheckpointField o2) {
				return o1.getNumberOfCheckpoint() - o2.getNumberOfCheckpoint();
			}
		});
	}

	/**
	 * Findet einen Weg zwischen zwei Punkten
	 * 
	 * @param from
	 *            Startposition
	 * @param to
	 *            Endposition
	 * @return List mit Positionen
	 */
	private List<Position> findWay(final Position from, final Position to) {

		final Set<Node> Q = new HashSet<Node>();

		for (int i = 0; i < this.array.length; i++) {
			for (int j = 0; j < this.array[i].length; j++) {
				this.array[i][j].distance = Integer.MAX_VALUE;
				Q.add(this.array[i][j]);
			}
		}

		this.array[from.getI()][from.getJ()].distance = 0;

		final ArrayList<Node> nextNodes = new ArrayList<Node>();
		for (Node nodes : this.array[from.getI()][from.getJ()].nodes) {
			nodes.distance = 1;
			nodes.precursor = this.array[from.getI()][from.getJ()];
			nextNodes.add(nodes);
		}

		Collections.sort(nextNodes, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				if (o2.position.equals(to)) {
					return Integer.MIN_VALUE;
				}
				if (o1.position.equals(to)) {
					return Integer.MAX_VALUE;
				}
				return o1.distance - o2.distance;
			}
		});

		while (!nextNodes.get(0).position.equals(to)) {

			final Node u = nextNodes.get(0);

			Q.remove(u);
			nextNodes.remove(u);

			for (Node nB : u.nodes) {
				if (Q.contains(nB) && !nextNodes.contains(nB)) {
					nB.distance = u.distance + 1;
					nB.precursor = u;
					nextNodes.add(nB);
				}
			}

			Collections.sort(nextNodes, new Comparator<Node>() {
				@Override
				public int compare(Node o1, Node o2) {
					if (o2.position.equals(to)) {
						return Integer.MIN_VALUE;
					}
					if (o1.position.equals(to)) {
						return Integer.MAX_VALUE;
					}
					return o1.distance - o2.distance;
				}
			});
		}

		Node hangler = nextNodes.get(0);
		final List<Position> list = new ArrayList<Position>();
		do {
			list.add(hangler.position);
			hangler = hangler.precursor;
		} while (!hangler.position.equals(from));

		list.add(hangler.position);

		return list;
	}

	/**
	 * Sucht den kürzesten Weg von den Startfeldern zu den Checkpoints
	 * 
	 * @throws CheckpointFieldNotReachableException
	 *             Wird geworfen, wenn Checkpointfelder nicht erreicht werden können
	 */
	public void findWaysToCheckpoints() throws CheckpointFieldNotReachableException {

		if (this.observer != null) {
			this.observer.removeWayPointList();
		}

		final List<List<Position>> list = new ArrayList<List<Position>>();

		/*
		 * StartFelder und checkpoints suchen
		 */
		for (final StartField startField : this.startFieldList) {
			try {
				list.add(findWay(new Position(startField.getI(), startField.getJ()), new Position(this.checkpointList.get(0)
						.getI(), this.checkpointList.get(0).getJ())));
			} catch (Exception e) {
				throw new CheckpointFieldNotReachableException(1, startField.getStartNumber(), true);
			}

		}

		for (int i = 0; i < this.checkpointList.size() - 1; i++) {
			try {
				list.add(findWay(new Position(this.checkpointList.get(i).getI(), this.checkpointList.get(i).getJ()),
						new Position(this.checkpointList.get(i + 1).getI(), this.checkpointList.get(i + 1).getJ())));
			} catch (Exception e) {
				throw new CheckpointFieldNotReachableException((i + 1), i, false);
			}
		}

		// Anzeigen
		if (this.observer != null) {
			this.observer.drawWayPointList(list);
		}
	}

	/**
	 * Findet den Weg zu den Reparaturfeldern
	 * 
	 * @throws RepairFieldNotReachableExeption
	 *             Wird geworfen, wenn Reparaturfelder nicht erreicht werden können.
	 */
	public void findWaysToRepairFields() throws RepairFieldNotReachableExeption {
		/*
		 * StartFelder und checkpoints suchen
		 */
		for (final StartField startField : this.startFieldList) {
			try {
				findWay(new Position(startField.getI(), startField.getJ()), new Position(this.repairList.get(0).getI(),
						this.repairList.get(0).getJ()));
			} catch (Exception e) {
				throw new RepairFieldNotReachableExeption(this.repairList.get(0).getJ(), this.repairList.get(0).getI());
			}

		}

		for (int i = 0; i < this.repairList.size() - 1; i++) {
			try {
				findWay(new Position(this.repairList.get(i).getJ(), this.repairList.get(i).getI()), new Position(this.repairList
						.get(i + 1).getJ(), this.repairList.get(i + 1).getI()));
			} catch (Exception e) {
				throw new RepairFieldNotReachableExeption(this.repairList.get(i + 1).getJ(), this.repairList.get(i + 1).getI());
			}
		}
	}
}
