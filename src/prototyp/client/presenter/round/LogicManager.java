package prototyp.client.presenter.round;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;

import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.ChatService;
import prototyp.client.service.ChatServiceAsync;
import prototyp.client.util.ProgrammingcardImg;
import prototyp.client.view.Page;
import prototyp.client.view.round.BackupWindow;
import prototyp.client.view.round.InformationWindow;
import prototyp.client.view.round.PlayerStatusArea;
import prototyp.server.model.Round;
import prototyp.shared.animation.Animate;
import prototyp.shared.field.BackupField;
import prototyp.shared.field.CheckpointField;
import prototyp.shared.field.CompactorField;
import prototyp.shared.field.ConveyorBeltField;
import prototyp.shared.field.Field;
import prototyp.shared.field.GearField;
import prototyp.shared.field.HoleField;
import prototyp.shared.field.LaserCannonField;
import prototyp.shared.field.PusherField;
import prototyp.shared.field.RepairField;
import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.round.Robot;
import prototyp.shared.round.RoundOption;
import prototyp.shared.util.Direction;
import prototyp.shared.util.Position;
import prototyp.shared.util.RobotMovement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

/**
 * Klasse, die für die Verwaltung der Roboter in einer Spielrunde zuständig ist.
 * 
 * @author Marcus, Andreas
 * @version 1.0
 * @version 1.1 Konstruktoren und Methoden hinzugefügt, Serializable implementiert (Marcus)
 * 
 * @see {@link Robot}, {@link Round}
 */
public class LogicManager implements ManagerInterface {

	
	
	/**
	 * Der Animationstimer
	 * 
	 * @author Marcus
	 * @version 1.1
	 */
	public class AnimationTimer extends AnimationTimerAbstract {

		/**
		 * Private Klasse
		 * 
		 * @author Marcus
		 * @version 1.0
		 */
		private class AnimationListener {

			/** der neuzustartende Timer */
			protected Timer t;

			/** die Anzahl der Animationen, auf deren Ende gewartet wird */
			protected int n = 0;

			/** die Liste mit den Animationen */
			protected List<Animate> animateList;

			/** Wartezeit, bis die nächste Animation gestartet wird */
			protected int duration;

			/**
			 * Konstruktor
			 * 
			 * @param t
			 *            der Timer, der neugestartet werden soll, wenn alle Animationen beendet sind
			 * @param n
			 *            die Anzahl der Animationen, auf deren Ende gewartet wird
			 */
			public AnimationListener(Timer t, List<Animate> animateList, int duration) {
				this.t = t;
				this.animateList = animateList;
				this.duration = duration;
			}

			/**
			 * Wird aufgerufen, um zu melden, dass eine Animation beendet ist.
			 */
			public boolean animationReady() {
				if (++this.n == this.animateList.size()) {
					this.t.schedule(this.duration);
					return true;
				}
				return false;
			}
		}

		/**
		 * Klasse die die Laseranimationen steuert
		 * 
		 * @author Marcus
		 * @version 1.0
		 */
		private class LaserAnimationListener extends AnimationListener {

			private Set<Robot> hittenRobots;

			public LaserAnimationListener(Timer t, List<Animate> animateList, int duration, Set<Robot> hittenRobots) {
				super(t, animateList, duration);
				this.hittenRobots = hittenRobots;
			}

			public boolean animationReady() {
				if (++this.n == this.animateList.size()) {

					for (final Robot hit : this.hittenRobots) {
						if (hit.getDamageToken() >= 10) {

							hit.setDeadForTurn(true);
							hit.setDamageToken(0);
							hit.setLifePoints(hit.getLifePoints() - 1);

							/*
							 * Sound abspielen
							 */
							RoundPlayerPagePresenter.SOUND_MANAGER.play("robo-boom_1.mp3");

							/*
							 * Für Awards
							 */
							hit.setNumberOfDeaths(hit.getNumberOfDeaths() + 1);

							if (hit == LogicManager.this.myRobot) {
								/*
								 * Anzeigen, dass man tot ist
								 */
								presenter.setAreaState(RoundPlayerPagePresenter.DEAD);

								((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea()).setLifeToken(hit
										.getLifePoints());
								((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea()).setDamageToken(0);

								if (hit.getLifePoints() == 0) {
									/*
									 * man selbst ist GANZ tot
									 */

									LogicManager.this.robotState = 2;
									hit.setDead(true);

									// Karten werden versteckt
									LogicManager.this.presenter.setAreaState(RoundPlayerPagePresenter.DEAD);

									// Window anzeigen, dass man selbst tot ist
									InformationWindow.showWindow(LogicManager.this.presenter.getPage(),
											Page.props.informationWindow_dead_title(), Page.props.informationWindow_dead_me_text(), "ui/robot_Lost.png");

									/*
									 * ChatNachricht schreiben
									 */
									sendMessage(hit.getPlayer().getUser().getAccountData().getNickname() + " "
											+ Page.props.roundPlayerPage_chat_dead());
								}
							} else {

								LogicManager.this.presenter.getOthersStateRecords().get(hit.getPlayer().getUser().getId())
										.updateDamageToken(hit.getDamageToken());
								LogicManager.this.presenter.getOthersStateRecords().get(hit.getPlayer().getUser().getId())
										.setNumberOfLifeToken(hit.getLifePoints() + 1);
								LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
								LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersStateGrid().redraw();

								/*
								 * Roboter ist tot
								 */
								if (hit.getLifePoints() == 0) {
									hit.setDead(true);

									// Window anzeigen, dass ein anderer Roboter tot ist
									InformationWindow.showWindow(
											LogicManager.this.presenter.getPage(),
											Page.props.informationWindow_dead_title(),
											"" + Page.props.informationWindow_dead_other_text_part1()
													+ hit.getPlayer().getUser().getAccountData().getNickname()
													+ Page.props.informationWindow_dead_other_text_part2(), "ui/robot_Lost.png");

									LogicManager.this.presenter.getOthersStateRecords().get(hit.getPlayer().getUser().getId())
											.setDead();
									LogicManager.this.presenter.getOthersReadyRecords().get(hit.getPlayer().getUser().getId())
											.setDead();
									LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
									LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersStateGrid().redraw();
								}
							}

							/*
							 * Explosion abspielen
							 */
							playExplosion(hit);

							hit.setI(-1);
							hit.setJ(-1);
							LogicManager.this.presenter.getPage().getDrawingArea()
									.remove(LogicManager.this.presenter.getRobotsImageList().get(hit));
						}
					}

					this.t.schedule(this.duration);
					return true;
				}
				return false;
			}

		}

		/** der Spielschritt */
		private int i = -1;

		/** die Liste der Programmierkarten der Spieler */
		private final Map<Integer, List<Programmingcard>> programmingcardsMap;

		/**
		 * Konstruktor
		 * 
		 * @param programmingcardsMap
		 */
		public AnimationTimer(Map<Integer, List<Programmingcard>> programmingcardsMap) {
			this.programmingcardsMap = programmingcardsMap;
		}

		/**
		 * Spielt eine Explosionsanimation
		 * 
		 * @param r
		 */
		private void playExplosion(final Robot r) {
			/*
			 * Explosionsanimationenen
			 */
			final Image img = new Image(r.getJ() * 50 + 25, r.getI() * 50 + 25, 0, 0, "images/robots/explosion.png");
			presenter.getPage().getDrawingArea().add(img);

			new Animate(img, "width", 0, 50, 1000).start();
			new Animate(img, "height", 0, 50, 1000).start();
			new Animate(img, "x", r.getJ() * 50 + 25, r.getJ() * 50, 1000).start();
			new Animate(img, "y", r.getI() * 50 + 25, r.getI() * 50, 1000) {
				public void onComplete() {
					presenter.getPage().getDrawingArea().remove(img);
				}
			}.start();
		}

		/**
		 * Lässt einen Roboter auf einer Presse shrumpen
		 * 
		 * @param r
		 */
		private void shrinkRobot(final Robot r) {
			final Image img = presenter.getRobotsImageList().get(r);

			new Animate(img, "width", 50, 10, 500) {
				public void onStart() {
					new Animate(img, "x", r.getJ() * 50, r.getJ() * 50 + 20, 500).start();
				}

				public void onComplete() {
					new Animate(img, "height", 50, 10, 500) {
						public void onStart() {
							new Animate(img, "y", r.getI() * 50, r.getI() * 50 + 20, 500) {
								public void onComplete() {
									playExplosion(r);
								}
							}.start();
						}
					}.start();
				}
			}.start();
		}

		/**
		 * Erstellt eine Animationsliste mit Presserfeldern und fügt diese der Animationsliste hinzu
		 * 
		 * @param animateList
		 * @param timer
		 */
		private void addCompactors(final List<List<Animate>> animateList, final Timer timer) {

			/*
			 * die AnimationsListe
			 */
			final List<Animate> subList = new ArrayList<Animate>();

			/*
			 * der AnimationListener
			 */
			final AnimationListener animationListener = new AnimationListener(timer, subList, 10);

			/*
			 * DummyAnimation erstellen, die die Pressen aktiv macht
			 */
			subList.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 1000) {
				public void onStart() {
					/*
					 * Aktive Bilder
					 */
					for (CompactorField beltField : LogicManager.this.playingBoard.getCompactorFieldList()) {
						if (!beltField.isActiveInFirstAndFifth() || (AnimationTimer.this.i == 0 || AnimationTimer.this.i == 4)) {
							final Image img = LogicManager.this.presenter.getFieldsImageList()[beltField.getI()][beltField.getJ()]
									.get(beltField.getImagePathOff());
							img.setHref(beltField.getImagePathOn());
						}
					}
				}

				public void onComplete() {
					/*
					 * Passive Bilder
					 */
					for (CompactorField beltField : LogicManager.this.playingBoard.getCompactorFieldList()) {
						if (!beltField.isActiveInFirstAndFifth() || (AnimationTimer.this.i == 0 || AnimationTimer.this.i == 4)) {
							final Image img = LogicManager.this.presenter.getFieldsImageList()[beltField.getI()][beltField.getJ()]
									.get(beltField.getImagePathOff());
							img.setHref(beltField.getImagePathOff());
						}
					}
					animationListener.animationReady();
				}
			});

			/*
			 * Roboter durchgehen und schauen, ob sie auf einem CompactorFeld stehen
			 */
			for (final Robot robot : LogicManager.this.robots.values()) {
				if (!(robot.isDead() || robot.isDeadForTurn() || robot.getI() == -1)) {

					/*
					 * Prüfen, ob der Roboter auf einem Compactorfield steht und dieser aktiv ist
					 */
					if (LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()] instanceof CompactorField
							&& (((CompactorField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()])
									.isActiveInFirstAndFifth() == false || (AnimationTimer.this.i == 0 || AnimationTimer.this.i == 4))) {

						/*
						 * DummyAnimation erstellen
						 */
						subList.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 1100) {

							public void onStart() {
								shrinkRobot(robot);
							}

							public void onComplete() {

								/*
								 * Roboter sterben lassen
								 */
								setRobotDead(robot);

								/*
								 * Auf Award prüfen
								 */
								if (!robot.isHasRobotBeenPressed()) {
									robot.setHasRobotBeenPressed(true);

									if (robot == LogicManager.this.myRobot) {
										/*
										 * Sound abspielen
										 */
										RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-award-1.mp3");

										/*
										 * ChatNachricht senden
										 */
										sendMessage(robot.getPlayer().getUser().getAccountData().getNickname() + " "
												+ Page.props.roundPlayerPage_chat_presseraward());

										/*
										 * Animation für Award erstellen
										 */
										final Image awardImage = new Image(robot.getJ() * 50 + 25, robot.getI() * 50 + 25, 0, 0,
												"images/awards/reif_fuer_die_schrottpresse.png");
										LogicManager.this.presenter.getPage().getDrawingArea().add(awardImage);
										new Animate(awardImage, "rotation", 0, 360, 800).start();
										new Animate(awardImage, "x", robot.getJ() * 50 + 25, robot.getJ() * 50, 800).start();
										new Animate(awardImage, "y", robot.getI() * 50 + 25, robot.getI() * 50, 800).start();
										new Animate(awardImage, "width", 0, 50, 800).start();
										new Animate(awardImage, "height", 0, 50, 800) {
											public void onComplete() {
												new Timer() {
													@Override
													public void run() {
														LogicManager.this.presenter.getPage().getDrawingArea().remove(awardImage);
													}

												}.schedule(1000);
											}
										}.start();
									}
								}
								animationListener.animationReady();
							}
						});
					}
				}
			}

			/*
			 * Zur Hauptanimationsliste hinzufügen
			 */
			animateList.add(subList);
		}

		/**
		 * Erstellt Animationen für einfache Fließbänder
		 * 
		 * @param animateList
		 * @param timer
		 */
		private void addConveyorBelts(final List<List<Animate>> animateList, final boolean isFirstTurn, final Timer timer) {

			/*
			 * Speichert die Roboterpositionen auf Förderbändern
			 */
			final Map<Robot, Position> robotsOnConveyor = new HashMap<Robot, Position>();

			/*
			 * Förderbandliste erstellen
			 */
			final List<ConveyorBeltField> conveyorBeltFieldList = new ArrayList<ConveyorBeltField>(
					LogicManager.this.playingBoard.getConveyorBeltFieldRangeTwoList());

			/*
			 * Schauen, ob Förderbänder mit Reichweite 1 auch gestartet werden müssen
			 */
			if (!isFirstTurn) {
				/*
				 * Einer-Förderbänder hinzufügen
				 */
				conveyorBeltFieldList.addAll(LogicManager.this.playingBoard.getConveyorBeltFieldRangeOneList());
			}

			/*
			 * Alle Roboter ermitteln, die Auf einem Förderband stehen
			 */
			for (final ConveyorBeltField conveyor : conveyorBeltFieldList) {
				final Robot robot = getRobotByPosition(conveyor.getI(), conveyor.getJ());
				if (robot != null) {
					robotsOnConveyor.put(robot, robot.getPosition());
				}
			}

			/*
			 * Alle Roboter ermitteln, die Auf einer Presse stehen
			 */
			for (final CompactorField compactor : playingBoard.getCompactorFieldList()) {
				final Robot robot = getRobotByPosition(compactor.getI(), compactor.getJ());
				if (robot != null) {
					robotsOnConveyor.put(robot, robot.getPosition());
				}
			}
			
			
			/*
			 * Speichert die Eingangsrichtungen für Roboter
			 */
			final Map<Robot, Direction> robotsMoveIntoDirection = new HashMap<Robot, Direction>();

			/*
			 * Speichert die neuen Positionen für jeden veränderten Roboter
			 */
			final Map<Robot, List<Position>> movedPositionMap = new HashMap<Robot, List<Position>>();
						
			/*
			 * Verschobene Positionen ermitteln
			 */
			final Set<Robot> removeSet = new HashSet<Robot>();
			for (final Robot robot : robotsOnConveyor.keySet()) {

				final Direction outDirection;

				if (LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()] instanceof ConveyorBeltField) {
					outDirection = ((ConveyorBeltField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()])
							.getArrowOutDirection();
				} else {
					outDirection = ((CompactorField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()])
							.getDirection();
				}
				
				final List<Position> positionStack = new LinkedList<Position>();
				movedPositionMap.put(robot, positionStack);

				switch (outDirection) {
				case NORTH:
					
					for(int i = robot.getI(); ; i--) {
						
						positionStack.add(new Position(i, robot.getJ()));
						final Robot toPush = getRobotByPosition(i, robot.getJ());
						if(toPush == null || (toPush != robot && robotsOnConveyor.containsKey(toPush))) {
							//ersten entfernen
							positionStack.remove(0);
							break;
						}
	
						if(playingBoard.getFields()[i][robot.getJ()].getNorth() == null) {
							removeSet.add(robot);
							break;
						}
					}
					robotsMoveIntoDirection.put(robot, Direction.SOUTH);
					
					break;
				case EAST:
					for(int j = robot.getJ(); ; j++) {
						
						positionStack.add(new Position(robot.getI(), j));
						final Robot toPush = getRobotByPosition(robot.getI(), j);
						if(toPush == null || (toPush != robot && robotsOnConveyor.containsKey(toPush))) {
							//ersten entfernen
							positionStack.remove(0);
							break;
						}
	
						if(playingBoard.getFields()[robot.getI()][j].getEast() == null) {
							removeSet.add(robot);
							break;
						}
					}
					robotsMoveIntoDirection.put(robot, Direction.WEST);
					break;
				case SOUTH:
					for(int i = robot.getI(); ; i++) {
						
						positionStack.add(new Position(i, robot.getJ()));
						final Robot toPush = getRobotByPosition(i, robot.getJ());
						if(toPush == null || (toPush != robot && robotsOnConveyor.containsKey(toPush))) {
							//ersten entfernen
							positionStack.remove(0);
							break;
						}
	
						if(playingBoard.getFields()[i][robot.getJ()].getSouth() == null) {
							removeSet.add(robot);
							break;
						}
					}
					robotsMoveIntoDirection.put(robot, Direction.NORTH);
					break;
				case WEST:
					for(int j = robot.getJ(); ; j--) {
						
						positionStack.add(new Position(robot.getI(), j));
						
						final Robot toPush = getRobotByPosition(robot.getI(), j);
						if(toPush == null || (toPush != robot && robotsOnConveyor.containsKey(toPush))) {
							//ersten entfernen
							positionStack.remove(0);
							break;
						}
	
						if(playingBoard.getFields()[robot.getI()][j].getWest() == null) {
							removeSet.add(robot);
							break;
						}
					}
					robotsMoveIntoDirection.put(robot, Direction.EAST);
					break;
				}

			}
			
			/*
			 * Konflikte testen und Roboter enfernen, die sich nicht bewegen dürfen
			 */
			final List<Robot> currentR = new ArrayList<Robot>(movedPositionMap.keySet());
			for(int i=0; i<currentR.size()-1; i++) {
				
				final List<Position> posList1 = movedPositionMap.get(currentR.get(i));
				
				for(int j = i+1; j<currentR.size(); j++) {
					
					final List<Position> posList2 = movedPositionMap.get(currentR.get(j));
				
					for(final Position pos : posList1) {
						if(posList2.contains(pos)) {
							removeSet.add(currentR.get(i));
							removeSet.add(currentR.get(j));
						}
					}
				}
			}
			
			Set<Robot> swappedSet;
			do {
				/*
				 * Alle Roboter entfernen, die sich nicht bewegen dürfen
				 */
				robotsOnConveyor.keySet().removeAll(removeSet);
				
				
				swappedSet = new HashSet<Robot>();
				
				for(final Robot robot : robotsOnConveyor.keySet()) {
					for(final Robot removed : removeSet) {
						if(movedPositionMap.get(robot).contains(removed.getPosition())) {
							swappedSet.add(robot);
						}
					}
				}
				removeSet.addAll(swappedSet);
				
			} while(swappedSet.size() > 0);

			
			/*
			 * Alle Roboter, die vom Förderband geschoben werden dürfen, schieben
			 */
			for (final Robot robot : robotsOnConveyor.keySet()) {
				
				/*
			    * bewegen
				*/
				Field.NeighbourDelegate del = null;
					switch (robotsMoveIntoDirection.get(robot)) {
					case NORTH:
						del = new Field.NeighbourDelegate() {
							@Override
							public Field invoke(Field field) {
								return field.getSouth();
							}
						};
						break;
					case EAST:
						del = new Field.NeighbourDelegate() {
							@Override
							public Field invoke(Field field) {
								return field.getWest();
							}
						};
						break;
					case SOUTH:
						del = new Field.NeighbourDelegate() {
							@Override
							public Field invoke(Field field) {
								return field.getNorth();
							}
						};
						break;
					default:
						del = new Field.NeighbourDelegate() {
							@Override
							public Field invoke(Field field) {
								return field.getEast();
							}
						};
						break;
					}

					/*
					 * Roboter werden rekursiv geschoben
					 */
					pushRobots(robot, robotsOnConveyor, del);
			}
				
			/*
			 * Animationen werden nun erstellt
			 */
			final List<Animate> conveyorList = new ArrayList<Animate>();
			final AnimationListener animationListener = new AnimationListener(timer, conveyorList, 10);

			/*
			 * DummyAnimation hinzufügen, die die FörderBänder animiert
			 */
			conveyorList.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 1500) {

				public void onComplete() {
					animationListener.animationReady();
				}

				public void onStart() {

					RoundPlayerPagePresenter.SOUND_MANAGER.play("foerderband-1.mp3");

					for (ConveyorBeltField beltField : LogicManager.this.playingBoard.getConveyorBeltFieldRangeTwoList()) {
						final Image img = LogicManager.this.presenter.getFieldsImageList()[beltField.getI()][beltField.getJ()]
								.get(beltField.getImagePathOff());
						img.setHref(beltField.getImagePathOn());
					}
					if (!isFirstTurn) {
						for (ConveyorBeltField beltField : LogicManager.this.playingBoard.getConveyorBeltFieldRangeOneList()) {
							final Image img = LogicManager.this.presenter.getFieldsImageList()[beltField.getI()][beltField.getJ()]
									.get(beltField.getImagePathOff());
							img.setHref(beltField.getImagePathOn());
						}
					}
				}
			});

			for (final Robot robot : robotsOnConveyor.keySet()) {
				/*
				 * Animation ertellen
				 */
				final Position oldPos = robotsOnConveyor.get(robot);
				if (robot.getI() != oldPos.getI()) {
					conveyorList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot), "y",
							oldPos.getI() * 50, robot.getI() * 50, 1500) {

						public void onComplete() {
							animationListener.animationReady();
						}

					});
				} else if (robot.getJ() != oldPos.getJ()) {
					conveyorList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot), "x",
							oldPos.getJ() * 50, robot.getJ() * 50, 1500) {

						public void onComplete() {
							animationListener.animationReady();
						}

					});
				}
			}


			/*
			 * Drehanmitaionen
			 */
			final List<Animate> turnList = new ArrayList<Animate>();
			final AnimationListener turnAnimationListener = new AnimationListener(timer, turnList, 100);

			/*
			 * DummyAnimation hinzufügen, die die FörderBänder animiert
			 */
			turnList.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 1000) {
				public void onComplete() {

					for (ConveyorBeltField beltField : LogicManager.this.playingBoard.getConveyorBeltFieldRangeTwoList()) {
						final Image img = LogicManager.this.presenter.getFieldsImageList()[beltField.getI()][beltField.getJ()]
								.get(beltField.getImagePathOff());
						img.setHref(beltField.getImagePathOff());
					}
					if (!isFirstTurn) {
						for (ConveyorBeltField beltField : LogicManager.this.playingBoard.getConveyorBeltFieldRangeOneList()) {
							final Image img = LogicManager.this.presenter.getFieldsImageList()[beltField.getI()][beltField.getJ()]
									.get(beltField.getImagePathOff());
							img.setHref(beltField.getImagePathOff());
						}
					}

					turnAnimationListener.animationReady();
				}
			});

			for (final Robot robot : robotsOnConveyor.keySet()) {
				if (robotsMoveIntoDirection.get(robot) != null && LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()] instanceof ConveyorBeltField
						&& ((ConveyorBeltField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()])
								.isMerging()) {

					final RobotMovement robotMovement;
					if ((robotMovement = Direction.getRobotMovement(robotsMoveIntoDirection.get(robot),
							((ConveyorBeltField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()])
									.getArrowOutDirection())) != null) {

						robot.setDirection(Direction.getDirection(robot.getDirection(), robotMovement));

						turnList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot), "rotation", 0,
								robotMovement == RobotMovement.TURN_RIGHT ? 90 : -90, 1000) {

							public void onComplete() {
								final Image curr = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50,
										robot.getImagePath());
								final Image old = LogicManager.this.presenter.getRobotsImageList().get(robot);
								LogicManager.this.presenter.getPage().getDrawingArea().add(curr);
								old.setVisible(false);
								old.setRotation(0);
								old.setHref(robot.getImagePath());
								old.setVisible(true);					
								LogicManager.this.presenter.getPage().getDrawingArea().remove(curr);
								
								turnAnimationListener.animationReady();
							}
						});
					}
				}
			}

			/*
			 * Lochanimationen
			 */
			final List<Animate> fallenRobotList = new ArrayList<Animate>();

			/*
			 * Animationlistener erstellen
			 */
			final AnimationListener robotFallAnimationListener = new AnimationListener(timer, fallenRobotList, 100);

			for (final Robot fallen : LogicManager.this.robots.values()) {
				if (fallen.getI() != -1
						&& LogicManager.this.playingBoard.getFields()[fallen.getI()][fallen.getJ()] instanceof HoleField) {

					fallenRobotList.addAll(createFallAnimation(timer, fallen, robotFallAnimationListener));

					fallen.setI(-1);
					fallen.setJ(-1);
				}
			}

			/*
			 * Listen der Animationsliste hinzufügen
			 */
			animateList.add(conveyorList);
			animateList.add(turnList);

			if (!fallenRobotList.isEmpty()) {
				animateList.add(fallenRobotList);
			}
		}

		private void addGears(final List<List<Animate>> animateList, final Timer timer) {
			/*
			 * Zahnradfeldanimationen erstellen, wenn sich diese auf dem Spielfeld befinden
			 */
			if (LogicManager.this.playingBoard.getGearFieldList().size() > 0) {

				// Die Animationsliste für Zahnradfelder
				final List<Animate> gearFieldList = new ArrayList<Animate>();

				// Der Animationslistener, der das Ende aller Zahnradanimationen
				// wartet
				final AnimationListener gearFieldAnimationListener = new AnimationListener(timer, gearFieldList, 100);

				/*
				 * Dummy-Sound Animation
				 */
				gearFieldList.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 1000) {
					public void onComplete() {
						gearFieldAnimationListener.animationReady();
					}

					public void onStart() {
						/*
						 * Sound abspielen
						 */
						RoundPlayerPagePresenter.SOUND_MANAGER.play("zahnrad-1.mp3");
					}
				});

				for (GearField gearField : LogicManager.this.playingBoard.getGearFieldList()) {

					/*
					 * Alle Zahnräder, die sich im Uhrzeigersinn drehen sollen animiert werden.
					 */
					if (gearField.isClockwiseDirection()) {

						/*
						 * Wenn sich ein Roboter auf dem Zahnradfeld steht befindet, diesen auch animieren
						 */
						final Robot robot;
						if ((robot = getRobotByPosition(gearField.getI(), gearField.getJ())) != null) {
							robot.setDirection(Direction.getDirection(robot.getDirection(), RobotMovement.TURN_RIGHT));
							gearFieldList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot),
									"rotation", 0, 90, 1000) {

								public void onComplete() {
									final Image curr = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50,
											robot.getImagePath());

									LogicManager.this.presenter.getPage().getDrawingArea().add(curr);

									LogicManager.this.presenter.getRobotsImageList().get(robot).setVisible(false);
									LogicManager.this.presenter.getPage().getDrawingArea()
											.remove(LogicManager.this.presenter.getRobotsImageList().get(robot));
									LogicManager.this.presenter.getRobotsImageList().put(robot, curr);

									gearFieldAnimationListener.animationReady();
								}

							});
						}

						/*
						 * Zahnradanimation hinzufügen
						 */
						// Großes Zahnrad
						Image img = LogicManager.this.presenter.getFieldsImageList()[gearField.getI()][gearField.getJ()]
								.get("images/fields/gear_east_part1.png");
						gearFieldList.add(new Animate(img, "rotation", img.getRotation(), img.getRotation() + 90, 1000) {

							public void onComplete() {
								gearFieldAnimationListener.animationReady();
							}

						});
						// kleines Zahnrad
						img = LogicManager.this.presenter.getFieldsImageList()[gearField.getI()][gearField.getJ()]
								.get("images/fields/gear_part2.png");
						gearFieldList.add(new Animate(img, "rotation", img.getRotation(), img.getRotation() - 90, 1000) {

							public void onComplete() {
								gearFieldAnimationListener.animationReady();
							}

						});

						/*
						 * Zahnräder, die sich entgegen dem Uhrzeigersinn drehen, werden animiert
						 */
					} else {

						/*
						 * Wenn sich ein Roboter auf dem Zahnradfeld steht befindet, diesen auch animieren
						 */
						final Robot robot;
						if ((robot = getRobotByPosition(gearField.getI(), gearField.getJ())) != null) {
							robot.setDirection(Direction.getDirection(robot.getDirection(), RobotMovement.TURN_LEFT));
							gearFieldList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot),
									"rotation", 0, -90, 1000) {

								public void onComplete() {
									final Image curr = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50,
											robot.getImagePath());

									LogicManager.this.presenter.getPage().getDrawingArea().add(curr);

									LogicManager.this.presenter.getRobotsImageList().get(robot).setVisible(false);
									LogicManager.this.presenter.getPage().getDrawingArea()
											.remove(LogicManager.this.presenter.getRobotsImageList().get(robot));
									LogicManager.this.presenter.getRobotsImageList().put(robot, curr);

									gearFieldAnimationListener.animationReady();
								}
							});
						}

						/*
						 * Zahnradanimation hinzufügen
						 */
						// Großes Zahnrad
						Image img = LogicManager.this.presenter.getFieldsImageList()[gearField.getI()][gearField.getJ()]
								.get("images/fields/gear_west_part1.png");
						gearFieldList.add(new Animate(img, "rotation", img.getRotation(), img.getRotation() - 90, 1000) {

							public void onComplete() {
								gearFieldAnimationListener.animationReady();
							}

						});
						// kleines Zahnrad
						img = LogicManager.this.presenter.getFieldsImageList()[gearField.getI()][gearField.getJ()]
								.get("images/fields/gear_part2.png");
						gearFieldList.add(new Animate(img, "rotation", img.getRotation(), img.getRotation() + 90, 1000) {

							public void onComplete() {
								gearFieldAnimationListener.animationReady();
							}

						});
					}
				}
				animateList.add(gearFieldList);
			}
		}

		private void addPusher(final List<List<Animate>> animateList, final Timer timer) {
			/*
			 * 
			 * Schieber animieren, wenn sich welche auf dem Spielbrett befinden
			 */
			if (LogicManager.this.playingBoard.getPusherFieldList().size() > 0) {

				/*
				 * Pusheranimationen erstellen, Drücker drücken und gehen danach zurück in ihre Ausgangslage
				 */
				// Die Animationsliste der Schieberanimationen fürs Drücken
				final List<Animate> pusherFieldPushList = new ArrayList<Animate>();
				// Der Animationslistener, der das Ende aller
				// Pusher-Push-Animationen registriert
				final AnimationListener pusherFieldPushAnimationListener = new AnimationListener(timer, pusherFieldPushList, 100);

				// Die Animationsliste der Schieberanimationen fürs Zurückgehen
				// in die Ausgangslage
				final List<Animate> pusherFieldBackList = new ArrayList<Animate>();
				// Der Animationslistener, der das Ende aller
				// Pusher-Back-Animationen registriert
				final AnimationListener pusherFieldBackAnimationListener = new AnimationListener(timer, pusherFieldBackList, 100);

				// Liste für in Löcher gefallene Roboter
				final List<Animate> robotFallenList = new ArrayList<Animate>();

				final boolean[] moved = { true };

				for (PusherField pusherField : LogicManager.this.playingBoard.getPusherFieldList()) {

					moved[0] = true;
					/*
					 * Schauen, ob Pusher bewegt werden darf
					 */
					if (!pusherField.isActiveInUneven() || this.i % 2 == 0) {

						/*
						 * Ein Roboter, der eventuell von dem Schieber beeinflusst wird
						 */
						Robot robot;

						Image img;

						/*
						 * die Richtung des Schiebers bestimmen
						 */
						switch (pusherField.getDirection()) {

						case NORTH:

							/*
							 * Wenn ein Roboter auf dem Pusherfield steht
							 */
							if ((robot = getRobotByPosition(pusherField.getI(), pusherField.getJ())) != null) {

								final List<List<Animate>> robotPushList = move(robot, Direction.SOUTH, timer, moved, false);

								pusherFieldPushList.addAll(robotPushList.get(0));

								if (robotPushList.size() == 2) {
									if (moved[0]) {
										robotFallenList.addAll(robotPushList.get(1));
									} else {
										pusherFieldPushList.addAll(robotPushList.get(1));
									}
								}
							}

							/*
							 * Pusher-Vorwärtsanimation erstellen
							 */
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_north_part1.png");
							pusherFieldPushList.add(new Animate(img, "height", 8, moved[0] ? 34 : 13, 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_north_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldPushList.add(new Animate(img, "y", pusherField.getI() * 50, pusherField.getI() * 50
									+ (moved[0] ? 26 : 5), 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});

							/*
							 * Pusher-Rückwärtssanimation erstellen
							 */
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_north_part1.png");
							pusherFieldBackList.add(new Animate(img, "height", moved[0] ? 34 : 13, 8, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_north_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldBackList.add(new Animate(img, "y", pusherField.getI() * 50 + (moved[0] ? 26 : 5),
									pusherField.getI() * 50, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});

							break;

						case EAST:

							/*
							 * Wenn ein Roboter auf dem Pusherfield steht
							 */
							if ((robot = getRobotByPosition(pusherField.getI(), pusherField.getJ())) != null) {

								final List<List<Animate>> robotPushList = move(robot, Direction.WEST, timer, moved, false);

								pusherFieldPushList.addAll(robotPushList.get(0));

								if (robotPushList.size() == 2) {
									if (moved[0]) {
										robotFallenList.addAll(robotPushList.get(1));
									} else {
										pusherFieldPushList.addAll(robotPushList.get(1));
									}
								}
							}

							/*
							 * Pusher-Vorwärtsanimation erstellen
							 */
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_east_part1.png");
							pusherFieldPushList.add(new Animate(img, "width", 8, moved[0] ? 34 : 13, 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});
							pusherFieldPushList.add(new Animate(img, "x", pusherField.getJ() * 50 + 35, pusherField.getJ() * 50
									+ (moved[0] ? 9 : 30), 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_east_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldPushList.add(new Animate(img, "x", pusherField.getJ() * 50, pusherField.getJ() * 50
									- (moved[0] ? 26 : 5), 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});

							/*
							 * Pusher-Rückwärtsanimation erstellen
							 */
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_east_part1.png");
							pusherFieldBackList.add(new Animate(img, "width", (moved[0] ? 34 : 13), 8, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});
							pusherFieldBackList.add(new Animate(img, "x", pusherField.getJ() * 50 + (moved[0] ? 9 : 30),
									pusherField.getJ() * 50 + 35, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});

							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_east_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldBackList.add(new Animate(img, "x", pusherField.getJ() * 50 - (moved[0] ? 26 : 5),
									pusherField.getJ() * 50, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});

							break;

						case SOUTH:

							/*
							 * Wenn ein Roboter auf dem Pusherfield steht
							 */
							if ((robot = getRobotByPosition(pusherField.getI(), pusherField.getJ())) != null) {

								final List<List<Animate>> robotPushList = move(robot, Direction.NORTH, timer, moved, false);

								pusherFieldPushList.addAll(robotPushList.get(0));

								if (robotPushList.size() == 2) {
									if (moved[0]) {
										robotFallenList.addAll(robotPushList.get(1));
									} else {
										pusherFieldPushList.addAll(robotPushList.get(1));
									}
								}
							}

							/*
							 * Pusher-Vorwärtsanimation erstellen
							 */
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_south_part1.png");
							pusherFieldPushList.add(new Animate(img, "height", 8, moved[0] ? 34 : 13, 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});
							pusherFieldPushList.add(new Animate(img, "y", pusherField.getI() * 50 + 35, pusherField.getI() * 50
									+ (moved[0] ? 9 : 30), 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_south_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldPushList.add(new Animate(img, "y", pusherField.getI() * 50, pusherField.getI() * 50
									- (moved[0] ? 26 : 5), 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});

							/*
							 * Pusher-Rückwärtsanimation erstellen
							 */
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_south_part1.png");
							pusherFieldBackList.add(new Animate(img, "height", moved[0] ? 34 : 13, 8, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});
							pusherFieldBackList.add(new Animate(img, "y", pusherField.getI() * 50 + (moved[0] ? 9 : 30),
									pusherField.getI() * 50 + 35, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});

							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_south_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldBackList.add(new Animate(img, "y", pusherField.getI() * 50 - (moved[0] ? 26 : 5),
									pusherField.getI() * 50, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});

							break;

						case WEST:

							/*
							 * Wenn ein Roboter auf dem Pusherfield steht
							 */
							if ((robot = getRobotByPosition(pusherField.getI(), pusherField.getJ())) != null) {

								final List<List<Animate>> robotPushList = move(robot, Direction.EAST, timer, moved, false);

								pusherFieldPushList.addAll(robotPushList.get(0));

								if (robotPushList.size() == 2) {
									if (moved[0]) {
										robotFallenList.addAll(robotPushList.get(1));
									} else {
										pusherFieldPushList.addAll(robotPushList.get(1));
									}
								}
							}

							// Pusher-Vorwärtsanimation erstellen
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_west_part1.png");
							pusherFieldPushList.add(new Animate(img, "width", 8, moved[0] ? 34 : 13, 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_west_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldPushList.add(new Animate(img, "x", pusherField.getJ() * 50, pusherField.getJ() * 50
									+ (moved[0] ? 26 : 5), 1000) {
								public void onComplete() {
									pusherFieldPushAnimationListener.animationReady();
								}
							});

							/*
							 * Pusher-Rückwärtsanimation erstellen
							 */
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_west_part1.png");
							pusherFieldBackList.add(new Animate(img, "width", moved[0] ? 34 : 13, 8, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});
							img = LogicManager.this.presenter.getFieldsImageList()[pusherField.getI()][pusherField.getJ()]
									.get("images/fields/pusher_west_part2" + (pusherField.isActiveInUneven() ? "_uneven" : "")
											+ ".png");
							pusherFieldBackList.add(new Animate(img, "x", pusherField.getJ() * 50 + (moved[0] ? 26 : 5),
									pusherField.getJ() * 50, 1000) {
								public void onComplete() {
									pusherFieldBackAnimationListener.animationReady();
								}
							});

							break;
						}
					}
				}

				if (!pusherFieldPushList.isEmpty()) {

					/*
					 * Dummy-Sound Animation
					 */
					pusherFieldPushList.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 1000) {
						public void onComplete() {
							pusherFieldPushAnimationListener.animationReady();
						}

						public void onStart() {
							RoundPlayerPagePresenter.SOUND_MANAGER.play("schieber-1.mp3");
						}
					});

					/*
					 * Die Vorwärts und Rückwärtsbewegungen der Animationsliste hinzufügen
					 */
					animateList.add(pusherFieldPushList);
					animateList.add(pusherFieldBackList);

					if (!robotFallenList.isEmpty()) {
						animateList.add(robotFallenList);
					}
				}
			}
		}

		/**
		 * Fügt der Animationlist Roboterlaser hinzu
		 * 
		 * @param animateList
		 * @param timer
		 */
		private List<Animate> addRobotLaser(final Timer timer) {

			final List<Animate> robotLaserAnimation = new ArrayList<Animate>();

			/*
			 * Roboterlaser
			 */
			if (LogicManager.this.roundOption.isRobotShootsOn()) {
				final Set<Robot> hittenRobots = new HashSet<Robot>();
				final LaserAnimationListener laserAnimationListener = new LaserAnimationListener(timer, robotLaserAnimation, 100,
						hittenRobots);

				robotLaserShot: for (final Robot robot : LogicManager.this.robots.values()) {

					if (robot.getI() != -1 && robot.getPowerDown() != 2) {
						Field hangler = LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()];

						String axis = "";
						int start = 0;
						int end = 0;
						int robotHit = 0;

						switch (robot.getDirection()) {
						case NORTH:
							hangler = hangler.getNorth();
							axis = "y1";
							start = robot.getI() * 50 + 25;
							end = robot.getI() * 50;
							robotHit = -10;
							break;
						case EAST:
							hangler = hangler.getEast();
							axis = "x1";
							start = robot.getJ() * 50 + 25;
							end = robot.getJ() * 50 + 50;
							robotHit = 10;
							break;
						case SOUTH:
							hangler = hangler.getSouth();
							axis = "y1";
							start = robot.getI() * 50 + 25;
							end = robot.getI() * 50 + 50;
							robotHit = 10;
							break;
						case WEST:
							hangler = hangler.getWest();
							axis = "x1";
							start = robot.getJ() * 50 + 25;
							end = robot.getJ() * 50;
							robotHit = -10;
							break;
						}

						/*
						 * die Reichweite des Lasers
						 */
						int range = 0;

						while (hangler != null) {

							/*
							 * Laser wird einen anderen Roboter treffen
							 */
							final Robot hit;
							if ((hit = getRobotByPosition(hangler.getI(), hangler.getJ())) != null) {

								/*
								 * dem Set hinzufügen
								 */
								hittenRobots.add(hit);

								/*
								 * Punkte vergeben
								 */
								robot.setPoints(robot.getPoints() + 250);

								/*
								 * für Awards
								 */
								hit.setNumberOfLaserHits(robot.getNumberOfLaserHits() + 1);
								robot.setNumberOfOtherRobotHit(robot.getNumberOfOtherRobotHit() + 1);
								/*
								 * Laser ist gegen einen Roboter getroffen
								 */
								final Line laser = new Line(robot.getJ() * 50 + 25, robot.getI() * 50 + 25,
										robot.getJ() * 50 + 25, robot.getI() * 50 + 25);
								LogicManager.this.presenter.getPage().getDrawingArea().add(laser);
								laser.setStrokeWidth(2);
								laser.setStrokeColor("red");

								robotLaserAnimation.add(new Animate(laser, axis, start, robotHit + end + range * 50, (Math
										.abs(range) + 1) * 200) {
									public void onComplete() {

										/*
										 * Schadenspunkte verteilen
										 */
										hit.setDamageToken(hit.getDamageToken() + 1);

										/*
										 * Sound abspielen
										 */
										RoundPlayerPagePresenter.SOUND_MANAGER.play("robotreffer_1.mp3");

										if (hit != LogicManager.this.myRobot) {

											LogicManager.this.presenter.getOthersStateRecords()
													.get(hit.getPlayer().getUser().getId())
													.updateDamageToken(hit.getDamageToken());
											LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersStateArea()
													.redraw();

											if (hit.getDamageToken() >= 10) {

												/*
												 * Punkte vergeben
												 */
												robot.setPoints(robot.getPoints() + 500);
											}
										} else {
											((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea())
													.setDamageToken(LogicManager.this.myRobot.getDamageToken());

										}

										/*
										 * nach 400 millisekunden wird der Laser wieder entfernt
										 */
										new Timer() {
											@Override
											public void run() {
												LogicManager.this.presenter.getPage().getDrawingArea().remove(laser);
												laserAnimationListener.animationReady();
											}

										}.schedule(400);
									}

								});
								continue robotLaserShot;
							}

							switch (robot.getDirection()) {
							case NORTH:
								hangler = hangler.getNorth();
								range = range - 1;
								break;
							case EAST:
								hangler = hangler.getEast();
								range = range + 1;
								break;
							case SOUTH:
								hangler = hangler.getSouth();
								range = range + 1;
								break;
							case WEST:
								hangler = hangler.getWest();
								range = range - 1;
								break;
							}
						}

						/*
						 * Laser ist gegen eine Wand getroffen
						 */
						final Line laser = new Line(robot.getJ() * 50 + 25, robot.getI() * 50 + 25, robot.getJ() * 50 + 25,
								robot.getI() * 50 + 25);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser);
						laser.setStrokeWidth(2);
						laser.setStrokeColor("red");
						robotLaserAnimation.add(new Animate(laser, axis, start, end + range * 50, (Math.abs(range) + 1) * 200) {
							public void onComplete() {
								new Timer() {
									@Override
									public void run() {
										LogicManager.this.presenter.getPage().getDrawingArea().remove(laser);
										laserAnimationListener.animationReady();
									}

								}.schedule(400);
							}

						});
					}
				}

				/*
				 * DummyAnimation für LaserSound
				 */
				if(robotLaserAnimation.size() > 0) {
					robotLaserAnimation.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 10) {
						public void onComplete() {
							laserAnimationListener.animationReady();
						}

						public void onStart() {
							RoundPlayerPagePresenter.SOUND_MANAGER.play("laser-1.mp3");
						}
					});
				}

			}

			/*
			 * Schauen, ob auch wirklich Roboter geschossen haben, ...
			 */
			if (robotLaserAnimation.size() <= 1) {
				timer.schedule(200);
			}

			// .. dann die Animationen hinzufügen
			return robotLaserAnimation;
		}

		/**
		 * Erstellt die Wandlaseranimationen
		 * 
		 * @param animateList
		 * @param timer
		 */
		private void addWallLaser(final List<List<Animate>> animateList, final Timer timer) {
			final List<Animate> wallLaserList = new ArrayList<Animate>();
			final Set<Robot> hittenRobots = new HashSet<Robot>();
			final LaserAnimationListener laserAnimationListener = new LaserAnimationListener(timer, wallLaserList, 100,
					hittenRobots);
			for (final LaserCannonField laserCannonField : LogicManager.this.playingBoard.getLaserCannonFieldList()) {

				final int i = laserCannonField.getI();
				final int j = laserCannonField.getJ();

				/*
				 * Nördliche Laser
				 */
				if (laserCannonField.getLaserCannonInfo().getCannonsNorth() > 0) {

					Field hangler = laserCannonField;

					// Brechnen bis zum nächsten roboter oder wand
					int gab = 0;

					// Der Roboter
					Robot r = null;

					while (hangler != null) {
						if ((r = getRobotByPosition(hangler.getI(), hangler.getJ())) != null) {
							if (!r.isDeadForTurn()) {
								hittenRobots.add(r);
								break;
							}
							r = null;
						}
						hangler = hangler.getSouth();

						gab++;
					}

					final Robot hit = r;

					final Line laser1;
					final Line laser2;
					final Line laser3;

					switch (laserCannonField.getLaserCannonInfo().getCannonsNorth()) {
					case 1: // Anzahl der Laser 1
						laser1 = new Line(j * 50 + 25, i * 50 + 15, j * 50 + 25, i * 50 + 15);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "y1", laser1.getY1(), laser1.getY1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					case 2: // Anzahl der Laser 2
						laser1 = new Line(j * 50 + 17, i * 50 + 15, j * 50 + 17, i * 50 + 15);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "y1", laser1.getY1(), laser1.getY1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});

						laser2 = new Line(j * 50 + 33, i * 50 + 15, j * 50 + 33, i * 50 + 15);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");

						wallLaserList.add(new Animate(laser2, "y1", laser2.getY1(), laser2.getY1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;

					case 3: // Anzahl der Laser 3
						laser1 = new Line(j * 50 + 13, i * 50 + 15, j * 50 + 13, i * 50 + 15);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "y1", laser1.getY1(), laser1.getY1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser2 = new Line(j * 50 + 25, i * 50 + 15, j * 50 + 25, i * 50 + 15);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");

						wallLaserList.add(new Animate(laser2, "y1", laser2.getY1(), laser1.getY2() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser3 = new Line(j * 50 + 37, i * 50 + 15, j * 50 + 37, i * 50 + 15);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser3);
						laser3.setStrokeWidth(2);
						laser3.setStrokeColor("red");

						wallLaserList.add(new Animate(laser3, "y1", laser3.getY1(), laser3.getY1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser3);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser3);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					}

				}

				/*
				 * Östliche Laser
				 */
				if (laserCannonField.getLaserCannonInfo().getCannonsEast() > 0) {

					Field hangler = laserCannonField;

					// Brechnen bis zum nächsten roboter oder wand
					int gab = 0;

					// Der Roboter
					Robot r = null;

					while (hangler != null) {
						if ((r = getRobotByPosition(hangler.getI(), hangler.getJ())) != null) {
							if (!r.isDeadForTurn()) {
								hittenRobots.add(r);
								break;
							}
							r = null;
						}
						hangler = hangler.getWest();

						gab++;
					}

					final Robot hit = r;

					final Line laser1;
					final Line laser2;
					final Line laser3;

					switch (laserCannonField.getLaserCannonInfo().getCannonsEast()) {
					case 1:
						laser1 = new Line(j * 50 + 35, i * 50 + 25, j * 50 + 35, i * 50 + 24);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "x1", laser1.getX1(), laser1.getX1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					case 2:
						laser1 = new Line(j * 50 + 35, i * 50 + 17, j * 50 + 35, i * 50 + 17);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "x1", laser1.getX1(), laser1.getX1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});

						laser2 = new Line(j * 50 + 35, i * 50 + 33, j * 50 + 35, i * 50 + 33);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");

						wallLaserList.add(new Animate(laser2, "x1", laser2.getX1(), laser2.getX1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;

					case 3:
						laser1 = new Line(j * 50 + 35, i * 50 + 13, j * 50 + 35, i * 50 + 13);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "x1", laser1.getX1(), laser1.getX1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser2 = new Line(j * 50 + 35, i * 50 + 25, j * 50 + 35, i * 50 + 25);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");
						;

						wallLaserList.add(new Animate(laser2, "x1", laser2.getX1(), laser1.getX2() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser3 = new Line(j * 50 + 35, i * 50 + 37, j * 50 + 35, i * 50 + 37);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser3);
						laser3.setStrokeWidth(2);
						laser3.setStrokeColor("red");

						wallLaserList.add(new Animate(laser3, "x1", laser3.getX1(), laser3.getX1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser3);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser3);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					}

				}
				/*
				 * Südliche Laser
				 */
				if (laserCannonField.getLaserCannonInfo().getCannonsSouth() > 0) {

					Field hangler = laserCannonField;

					// Brechnen bis zum nächsten roboter oder wand
					int gab = 0;

					// Der Roboter
					Robot r = null;

					while (hangler != null) {
						if ((r = getRobotByPosition(hangler.getI(), hangler.getJ())) != null) {
							if (!r.isDeadForTurn()) {
								hittenRobots.add(r);
								break;
							}
							r = null;
						}
						hangler = hangler.getNorth();

						gab++;
					}

					final Robot hit = r;

					final Line laser1;
					final Line laser2;
					final Line laser3;

					switch (laserCannonField.getLaserCannonInfo().getCannonsSouth()) {
					case 1:
						laser1 = new Line(j * 50 + 25, i * 50 + 35, j * 50 + 25, i * 50 + 35);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "y1", laser1.getY1(), laser1.getY1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					case 2:
						laser1 = new Line(j * 50 + 17, i * 50 + 35, j * 50 + 17, i * 50 + 35);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "y1", laser1.getY1(), laser1.getY1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});

						laser2 = new Line(j * 50 + 33, i * 50 + 35, j * 50 + 33, i * 50 + 35);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");

						wallLaserList.add(new Animate(laser2, "y1", laser2.getY1(), laser2.getY1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;

					case 3:
						laser1 = new Line(j * 50 + 13, i * 50 + 35, j * 50 + 13, i * 50 + 35);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "y1", laser1.getY1(), laser1.getY1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser2 = new Line(j * 50 + 25, i * 50 + 35, j * 50 + 25, i * 50 + 35);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");

						wallLaserList.add(new Animate(laser2, "y1", laser2.getY1(), laser2.getY2() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser3 = new Line(j * 50 + 37, i * 50 + 35, j * 50 + 37, i * 50 + 35);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser3);
						laser3.setStrokeWidth(2);
						laser3.setStrokeColor("red");

						wallLaserList.add(new Animate(laser3, "y1", laser3.getY1(), laser3.getY1() - gab * 50
								+ (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser3);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser3);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					}
				}
				/*
				 * Westliche Laser
				 */
				if (laserCannonField.getLaserCannonInfo().getCannonsWest() > 0) {

					Field hangler = laserCannonField;

					// Brechnen bis zum nächsten roboter oder wand
					int gab = 0;

					// Der Roboter
					Robot r = null;

					while (hangler != null) {
						if ((r = getRobotByPosition(hangler.getI(), hangler.getJ())) != null) {
							if (!r.isDeadForTurn()) {
								hittenRobots.add(r);
								break;
							}
							r = null;

						}
						hangler = hangler.getEast();

						gab++;
					}

					final Robot hit = r;

					final Line laser1;
					final Line laser2;
					final Line laser3;

					switch (laserCannonField.getLaserCannonInfo().getCannonsWest()) {
					case 1:
						laser1 = new Line(j * 50 + 15, i * 50 + 25, j * 50 + 15, i * 50 + 25);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "x1", laser1.getX1(), laser1.getX1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					case 2:
						laser1 = new Line(j * 50 + 15, i * 50 + 17, j * 50 + 15, i * 50 + 17);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "x1", laser1.getX1(), laser1.getX1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
								;
							}
						});

						laser2 = new Line(j * 50 + 15, i * 50 + 33, j * 50 + 15, i * 50 + 33);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");

						wallLaserList.add(new Animate(laser2, "x1", laser2.getX1(), laser2.getX1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;

					case 3:
						laser1 = new Line(j * 50 + 15, i * 50 + 13, j * 50 + 15, i * 50 + 13);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser1);
						laser1.setStrokeWidth(2);
						laser1.setStrokeColor("red");

						wallLaserList.add(new Animate(laser1, "x1", laser1.getX1(), laser1.getX1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser1);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser1);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser2 = new Line(j * 50 + 15, i * 50 + 25, j * 50 + 15, i * 50 + 25);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser2);
						laser2.setStrokeWidth(2);
						laser2.setStrokeColor("red");

						wallLaserList.add(new Animate(laser2, "x1", laser2.getX1(), laser1.getX2() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser2);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser2);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						laser3 = new Line(j * 50 + 15, i * 50 + 37, j * 50 + 15, i * 50 + 37);
						LogicManager.this.presenter.getPage().getDrawingArea().add(laser3);
						laser3.setStrokeWidth(2);
						laser3.setStrokeColor("red");

						wallLaserList.add(new Animate(laser3, "x1", laser3.getX1(), laser3.getX1() + gab * 50
								- (r == null ? 16 : 0), (Math.abs(gab) + 1) * 200) {
							public void onComplete() {
								if (hit != null) {
									setDamageToRobot(hit, laserAnimationListener, laser3);
								} else {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(laser3);
											laserAnimationListener.animationReady();
										}

									}.schedule(400);
								}
							}
						});
						break;
					}

				}
			}
			if (wallLaserList.size() > 0) {
				/*
				 * Dummy-Animation für den Sound
				 */
				wallLaserList.add(new Animate(new Line(0, 0, 0, 0), "", 0, 0, 10) {
					public void onComplete() {
						laserAnimationListener.animationReady();
					}

					public void onStart() {
						// Sound anspielen
						RoundPlayerPagePresenter.SOUND_MANAGER.play("laser-2.mp3");
					}
				});
				animateList.add(wallLaserList);
			}
		}

		/**
		 * Erstellt eine Animation für das Fallen eines Roboters und liefert die Liste Zurück
		 * 
		 * @return
		 */
		private List<Animate> createFallAnimation(final Timer timer, final Robot fallen,
				final AnimationListener fallAnimationListener) {

			/*
			 * Liste, die die Animation für den fallenden Roboter enthält
			 */
			final List<Animate> fallenRobotList = new ArrayList<Animate>(5);

			/*
			 * Im folgenden werden alle benötoigten Animationen für den Fall des Roboters erstellt und der in der
			 * "fallenRobot"-Liste gespeichert
			 */
			fallenRobotList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(fallen), "width", 50, 0, 1000) {
				public void onComplete() {
					fallAnimationListener.animationReady();
				}
			});
			fallenRobotList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(fallen), "height", 50, 0, 1000) {
				public void onComplete() {
					fallAnimationListener.animationReady();
				}
			});

			fallenRobotList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(fallen), "rotation", 0, 360,
					1000) {
				public void onComplete() {
					fallAnimationListener.animationReady();
				}
			});

			fallenRobotList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(fallen), "x",
					fallen.getJ() * 50, fallen.getJ() * 50 + 25, 1000) {
				public void onComplete() {
					fallAnimationListener.animationReady();
				}
			});

			fallenRobotList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(fallen), "y",
					fallen.getI() * 50, fallen.getI() * 50 + 25, 1000) {

				public void onComplete() {

					LogicManager.this.presenter.getPage().getDrawingArea()
							.remove(LogicManager.this.presenter.getRobotsImageList().get(fallen));

					/*
					 * Werte der Roboter anpassen
					 */
					setRobotDead(fallen);

					fallAnimationListener.animationReady();
				}

				public void onStart() {
					/*
					 * Sound abspielen
					 */
					RoundPlayerPagePresenter.SOUND_MANAGER.play("fallen-1.mp3");
				}
			});

			return fallenRobotList;
		}

		/**
		 * Hier wird EIN Roboter vorwärts bzw. rückwärts bewegt. Dabei werden eventuel andere Roboter auch geschoben.
		 * 
		 * @param robot
		 *            der zu bewegende Roboter
		 * @param steps
		 *            die Anzahl der Schritte in die Blickrichtung
		 * @return Informationen über die bewegten Roboter
		 */
		private List<List<Animate>> move(final Robot robot, final Direction direction, final Timer timer, final boolean[] moved,
				final boolean isActive) {

			/*
			 * Die AnimationsAnimations liste
			 */
			final List<List<Animate>> animateList = new ArrayList<List<Animate>>();

			/*
			 * Der Roboter darf noch nicht in ein Loch gefallen sein
			 */
			if (!robot.isDeadForTurn() && robot.getI() != -1) {

				/*
				 * Eine Liste, mit den Robotern, die bewegt werden
				 */
				final List<Robot> robotList = new ArrayList<Robot>();

				/*
				 * Eine Liste, mit Feldern, die Durchlaufen wurden.
				 */
				final List<Field> wayList = new ArrayList<Field>();

				/*
				 * die Achse auf der sich der Roboter bewegt, entweder "x" oder "y"
				 */
				final String movement = direction == Direction.SOUTH || direction == Direction.NORTH ? "y" : "x";

				/*
				 * Das Feld mit dem sich durchgehangelt wird, bis man auf eine Wand stößt oder kein Roboter mehr gefunden wurde.
				 */
				Field hanglerField = LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()];

				/*
				 * Delegate
				 */
				Field.NeighbourDelegate del = null;

				/*
				 * Das Delegate mit dr richtigen Methode verknüpfen
				 */
				switch (direction) {
				case NORTH:
					del = new Field.NeighbourDelegate() {
						@Override
						public Field invoke(Field field) {
							return field.getNorth();
						}
					};
					break;
				case EAST:
					del = new Field.NeighbourDelegate() {
						@Override
						public Field invoke(Field field) {
							return field.getEast();
						}
					};
					break;
				case SOUTH:
					del = new Field.NeighbourDelegate() {
						@Override
						public Field invoke(Field field) {
							return field.getSouth();
						}
					};
					break;
				case WEST:
					del = new Field.NeighbourDelegate() {
						@Override
						public Field invoke(Field field) {
							return field.getWest();
						}
					};
					break;
				}

				/*
				 * Felder durchlaufen und die Roboter speichern, die nachher eventuell geschoben werden
				 */
				while (hanglerField != null) {

					/*
					 * der aktuelle Roboter auf dem Feld
					 */
					final Robot r;

					/*
					 * kein Roboter steht auf dem Feld. somit können die Roboter bewegt werden
					 */
					if ((r = getRobotByPosition(hanglerField.getI(), hanglerField.getJ())) == null || r.isDeadForTurn()) {

						moved[0] = true;

						List<Animate> moveList = new ArrayList<Animate>();

						/*
						 * Wartet auf das ende aller Vorwärtsbewegungen des Roboters und startet den Timer dann neu
						 */
						final AnimationListener moveAnimationListener = new AnimationListener(timer, moveList, 100);

						for (int n = 0; n < wayList.size(); n++) {

							moveList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robotList.get(n)),
									movement, movement.equals("y") ? robotList.get(n).getI() * 50 : robotList.get(n).getJ() * 50,
									movement.equals("y") ? wayList.get(n).getI() * 50 : wayList.get(n).getJ() * 50, 1000) {

								public void onComplete() {
									moveAnimationListener.animationReady();
								}

							});
							robotList.get(n).setI(wayList.get(n).getI());
							robotList.get(n).setJ(wayList.get(n).getJ());
						}

						/*
						 * Die RobotermoveList der AnmiantionList hinzufügen
						 */
						animateList.add(moveList);

						break;
					}

					/*
					 * den aktuellen Roboter der Roboterliste hinzufügen
					 */
					robotList.add(r);

					/*
					 * Das Delegate aufrufen und nextField neu setzen
					 */
					wayList.add(hanglerField = del.invoke(hanglerField));

				}

				/*
				 * Schauen, ob der letzte Roboter in ein Loch gefallen ist
				 */
				final Robot fallenRobot = robotList.get(robotList.size() - 1);
				boolean didRobotFallInHole = false;
				if (LogicManager.this.playingBoard.getFields()[fallenRobot.getI()][fallenRobot.getJ()] instanceof HoleField) {

					didRobotFallInHole = true;

					/*
					 * Punkte vergeben
					 */
					if (robot != fallenRobot && isActive) {
						/*
						 * Punkte vergeben
						 */
						robot.setPoints(robot.getPoints() + 500);

						/*
						 * für Awards
						 */
						robot.setNumberOfRobotsPushedInHole(robot.getNumberOfRobotsPushedInHole() + 1);

						if (robot.getNumberOfRobotsPushedInHole() == 1 && robot == LogicManager.this.myRobot) {

							/*
							 * Sound abspielen
							 */
							RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-award-1.mp3");

							/*
							 * ChatNachricht senden
							 */
							sendMessage(robot.getPlayer().getUser().getAccountData().getNickname() + " "
									+ Page.props.roundPlayerPage_chat_darksideaward());

							/*
							 * Animation starten
							 */
							final Image awardImage = new Image(robot.getJ() * 50 + 25, robot.getI() * 50 + 25, 0, 0,
									"images/awards/dark_side_normal.png");
							LogicManager.this.presenter.getPage().getDrawingArea().add(awardImage);
							new Animate(awardImage, "rotation", 0, 360, 800).start();
							new Animate(awardImage, "x", robot.getJ() * 50 + 25, robot.getJ() * 50, 800).start();
							new Animate(awardImage, "y", robot.getI() * 50 + 25, robot.getI() * 50, 800).start();
							new Animate(awardImage, "width", 0, 50, 800).start();
							new Animate(awardImage, "height", 0, 50, 800) {
								public void onComplete() {
									new Timer() {
										@Override
										public void run() {
											LogicManager.this.presenter.getPage().getDrawingArea().remove(awardImage);
										}

									}.schedule(1000);
								}
							}.start();
						}
					}

					final List<Animate> fallenRobotList;

					/*
					 * Animationlistener erstellen
					 */
					final AnimationListener robotFallAnimationListener = new AnimationListener(timer, null, 100);

					/*
					 * die Teilanimationsliste für den gefallenen Roboter wird der Animationsliste hinzugefügt
					 */
					animateList.add(fallenRobotList = createFallAnimation(timer, fallenRobot, robotFallAnimationListener));

					/*
					 * Animationsliste setzen
					 */
					robotFallAnimationListener.animateList = fallenRobotList;
				}

				/*
				 * Wenn keine Bewegung stattgefunden hat, soll es so ausehen, als ob die Roboter versuchen die Wand wegzuschieben
				 */
				if (animateList.size() == 0) {

					moved[0] = false;

					/*
					 * Vorschubdaten ermitteln
					 */
					// Der Roboter soll sich 5 pixel in richtung wand bewegen
					final int stepWall;
					if (direction == Direction.EAST || direction == Direction.SOUTH) {
						stepWall = 5;
					} else {
						stepWall = -5;
					}

					/*
					 * Eine Liste, die Vorwartsanimationen gegen eine Wand enthält.
					 */
					final List<Animate> forwardList = new ArrayList<Animate>();

					/*
					 * Der AnimationsLister, der auf das Ende der Animationen wartet
					 */
					final AnimationListener forwardAnimationListener = new AnimationListener(timer, forwardList, 100);

					/*
					 * Die erste Animation enthält den Sound
					 */
					forwardList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robotList.get(0)), movement,
							movement.equals("y") ? robotList.get(0).getI() * 50 : robotList.get(0).getJ() * 50, movement
									.equals("y") ? robotList.get(0).getI() * 50 + stepWall : robotList.get(0).getJ() * 50
									+ stepWall, 200) {

						public void onComplete() {
							RoundPlayerPagePresenter.SOUND_MANAGER.play("gegen_wand-1.mp3");
							forwardAnimationListener.animationReady();
						}

					});

					/*
					 * Erstellen der Animationen und hinzufügen zur forwardList
					 */
					for (int i = 1; i < robotList.size() - 1; i++) {
						forwardList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robotList.get(i)),
								movement, movement.equals("y") ? robotList.get(i).getI() * 50 : robotList.get(i).getJ() * 50,
								movement.equals("y") ? robotList.get(i).getI() * 50 + stepWall : robotList.get(i).getJ() * 50
										+ stepWall, 200) {

							public void onComplete() {
								forwardAnimationListener.animationReady();
							}

						});
					}

					/*
					 * Eine Liste, die die Animationen für Roboter zurück auf ihre Ausgangspositionen speichert.
					 */
					final List<Animate> backwardList = new ArrayList<Animate>();

					/*
					 * Der AnimationsLister, der auf das Ende der Animationen wartet
					 */
					final AnimationListener backwardAnimationListener = new AnimationListener(timer, backwardList, 100);

					/*
					 * Erstellen der Animationen und hinzufügen zur backwardList
					 */
					for (int i = 0; i < robotList.size(); i++) {
						backwardList.add(new Animate(LogicManager.this.presenter.getRobotsImageList().get(robotList.get(i)),
								movement, movement.equals("y") ? robotList.get(i).getI() * 50 + stepWall : robotList.get(i)
										.getJ() * 50 + stepWall, movement.equals("y") ? robotList.get(i).getI() * 50 : robotList
										.get(i).getJ() * 50, 200) {

							public void onComplete() {
								backwardAnimationListener.animationReady();
							}

						});
					}

					/*
					 * die beiden Listen der Animationsliste hinzufügen
					 */
					animateList.add(forwardList);
					animateList.add(backwardList);
				} else if (isActive) {
					/*
					 * Punkte vergeben, da Roboter geschoben wurden
					 */
					robot.setPoints(robot.getPoints() + (animateList.size() - 1) * 50);

					/*
					 * für Awards
					 */
					robot.setNumberOfPushedRobots(robot.getNumberOfPushedRobots() + animateList.size() - 1);
					robot.setNumberOfSteps(robot.getNumberOfSteps() + 1);

					if (!robot.isHasPushedOnRepairfield()) {
						for (int i = 1; i < robotList.size(); i++) {
							if (LogicManager.this.playingBoard.getFields()[robotList.get(i).getI()][robotList.get(i).getJ()] instanceof RepairField) {
								robot.setHasPushedOnRepairfield(true);

								if (robot == LogicManager.this.myRobot) {
									/*
									 * Sound abspielen
									 */
									RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-award-1.mp3");

									/*
									 * ChatNachricht senden
									 */
									sendMessage(robot.getPlayer().getUser().getAccountData().getNickname() + " "
											+ Page.props.roundPlayerPage_chat_samaritanaward());

									/*
									 * Animation starten
									 */
									final Image awardImage = new Image(robot.getJ() * 50 + 25, robot.getI() * 50 + 25, 0, 0,
											"images/awards/barmherziger_samariter.png");
									LogicManager.this.presenter.getPage().getDrawingArea().add(awardImage);
									new Animate(awardImage, "rotation", 0, 360, 800).start();
									new Animate(awardImage, "x", robot.getJ() * 50 + 25, robot.getJ() * 50, 800).start();
									new Animate(awardImage, "y", robot.getI() * 50 + 25, robot.getI() * 50, 800).start();
									new Animate(awardImage, "width", 0, 50, 800).start();
									new Animate(awardImage, "height", 0, 50, 800) {
										public void onComplete() {
											new Timer() {
												@Override
												public void run() {
													LogicManager.this.presenter.getPage().getDrawingArea().remove(awardImage);
												}

											}.schedule(1000);
										}
									}.start();
								}
							}
						}
					}
				}
				if (didRobotFallInHole) {
					fallenRobot.setDeadForTurn(true);
				}
			}

			/*
			 * Liefern der Liste
			 */
			return animateList;
		}

		
		/**
		 * Verschiebt Roboter
		 * @param robot
		 * @param map
		 * @param del
		 * @return
		 */
		private boolean pushRobots(final Robot robot, Map<Robot, Position> map, final Field.NeighbourDelegate del) {
			if (robot != null) {

				final Field next = del.invoke(LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()]);
				if(next == null) {
					return false;
				}

				final Robot nextBot = getRobotByPosition(next.getI(), next.getJ());
				if(!map.containsKey(nextBot)) {
					pushRobots(getRobotByPosition(next.getI(), next.getJ()), map, del);
				}
				
				map.put(robot, robot.getPosition());
				robot.setI(next.getI());
				robot.setJ(next.getJ());
				return true;
			}
			return true;
		}

		@Override
		public void run() {
			try {
				/*
				 * Animation kann beendet werden
				 */
				if ((this.i = this.i + 1) >= 5) {

					if (this.i > 5) {
						return;
					}

					LogicManager.this.animationTimer = null;

					if (LogicManager.this.roundFinished == false) {
						/*
						 * Gestorbene Roboter zurück auf ihr Backupfeld setzen
						 */
						setRobotOnRestartField();

						/*
						 * Roboter, die einen PowerDown angekündigt haben, abschalten. oder wieder anschalten, die den Powerdown
						 * durchegührt haben
						 */
						for (Robot r : LogicManager.this.robots.values()) {
							if (!r.isDead()) {

								if (r.getPowerDown() == 1) {
									if (!r.isDeadForTurn()) {
										r.setPowerDown(2);
										if (r == LogicManager.this.myRobot) {
											presenter.setAreaState(RoundPlayerPagePresenter.POWER_DOWN_ACTIVE);

											sendMessage(LogicManager.this.myRobot.getPlayer().getUser().getAccountData()
													.getNickname()
													+ " " + Page.props.roundPlayerPage_chat_powerdown_is());

										} else {
											LogicManager.this.presenter.getOthersReadyRecords()
													.get(r.getPlayer().getUser().getId()).setPowerDown();
										}
									} else {
										if (r == LogicManager.this.myRobot) {
											sendMessage(UserPresenter.getInstance().getNickname() + " "
													+ Page.props.roundPlayerPage_chat_powerdown_abort());
										}
									}
								} else if (r.getPowerDown() == 2) {

									r.setPowerDown(0);
									r.setDamageToken(0);

									/*
									 * den Button wieder aktivieren
									 */
									if (r == LogicManager.this.myRobot) {
										/*
										 * PowerDOwn-Anzeige weg
										 */
										presenter.resetAreaState();

										/*
										 * PowerdownButton wieder aktivieren
										 */
										((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea())
												.getPowerDownButton().setDisabled(false);
										
										sendMessage(LogicManager.this.myRobot.getPlayer().getUser().getAccountData()
												.getNickname()
												+ " " + Page.props.roundPlayerPage_chat_powerdown_end());

										/*
										 * Visualisieren
										 */
										((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea())
												.setDamageToken(0);
										
										
									} else {

										LogicManager.this.presenter.getOthersStateRecords().get(r.getPlayer().getUser().getId())
												.updateDamageToken(0);
										LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersStateArea().redraw();
									}

								}
							}
						}

						/*
						 * neue Programmierkarten werden geholt
						 */
						if (!LogicManager.this.myRobot.isDead()) {
							LogicManager.this.presenter.receiveProgrammingCards();
						}

					}
					/*
					 * der Timer wird beendet und der nächste Spielzug kann beginnen
					 */
					LogicManager.this.animationTimer = null;
					cancel();
					return;
				}

				/*
				 * die i-ten Programmierkarten in eine Liste einfügen
				 */
				final List<Programmingcard> cardsList = new ArrayList<Programmingcard>();
				for (List<Programmingcard> programmingcards : this.programmingcardsMap.values()) {
					if (programmingcards.size() > this.i) {
						cardsList.add(programmingcards.get(this.i));
					}
				}

				/*
				 * die Kartenliste nach dem Prioritätswert absteigend sortieren
				 */
				Collections.sort(cardsList, new Comparator<Programmingcard>() {
					@Override
					public int compare(Programmingcard o1, Programmingcard o2) {
						return o2.getPriority() - o1.getPriority();
					}
				});

				/**
				 * Roboter Bewegungen in einem Timer ausführen
				 */
				new Timer() {

					/** der Zähler */
					private int i = -1;

					public void run() {

						/*
						 * Alle Karten des i-ten Spielschritts sind abgearbeitet den aktuellen Timer verlassen und die nächsten
						 * Programmierkarten der Spieler ausführen
						 */
						if (++this.i == cardsList.size()) {

							/**
							 * Timer um Maschinenelemente zu animieren
							 */
							new Timer() {

								private int i = -1;
								private final List<List<Animate>> animateList = startMachines(this);
								private boolean robotLaserAnimated = false;

								/**
								 * Schaut nach, ob alle Roboter für den Spielzug tot sind
								 * 
								 * @return
								 */
								private boolean allRobotsDead() {
									for (Robot robot : LogicManager.this.robots.values()) {
										if (!robot.isDeadForTurn()) {
											return false;
										}
									}
									return true;
								}

								/**
								 * prüft, ob ein Roboter gewonnen hat oder eine BackupKarte abgelegt werden kann
								 * 
								 * @return
								 */
								private BackupField checkGameStatus() {

									/*
									 * das BackupField, das zurückgeliefert werden soll
									 */
									BackupField backupField = null;

									/*
									 * Roboter durchlaufen
									 */
									for (final Robot robot : LogicManager.this.robots.values()) {

										if (!robot.isDeadForTurn() && robot.getPowerDown() != 2) {

											/*
											 * prüfen, ob der Roboter auf dem Checkpointfield steht, den er als nächstes erreichen
											 * müsste
											 */
											if (LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()] == LogicManager.this.playingBoard
													.getCheckpointFieldList().get(robot.getNumberOfReachedCheckpoints())) {

												/*
												 * Punkte vergeben
												 */
												robot.setPoints(robot.getPoints() + 500);

												
												/*
												 * Sound abspielen
												 */
												RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-checkpoint-1.mp3");

												if (robot == LogicManager.this.myRobot) {
													
													/*
													 * erreichten Checkpoint oben rechts anzeigen
													 */
													presenter.getMyCheckpointImage().setSrc("robotStatus/checkpoint_" + 
															(robot.getNumberOfReachedCheckpoints() + 1) 
															+ "v2.png");
													
													/*
													 * Bild des Checkpoints ändern
													 */
													LogicManager.this.presenter.getFieldsImageList()[LogicManager.this.myRobot
															.getI()][LogicManager.this.myRobot.getJ()].get(
															"images/fields/checkpoint_"
																	+ LogicManager.this.playingBoard
																			.getCheckpointFieldList()
																			.get(LogicManager.this.myRobot
																					.getNumberOfReachedCheckpoints())
																			.getNumberOfCheckpoint() + ".png").setHref(
															"images/fields/checkpoint_"
																	+ LogicManager.this.playingBoard
																			.getCheckpointFieldList()
																			.get(LogicManager.this.myRobot
																					.getNumberOfReachedCheckpoints())
																			.getNumberOfCheckpoint() + "v2.png");

													/*
													 * Backupkarte ist vorhanden
													 */
													if (LogicManager.this.myRobot.getNumberOfBackupCards() > 0) {

														backupField = (BackupField) LogicManager.this.playingBoard.getFields()[LogicManager.this.myRobot
																.getI()][LogicManager.this.myRobot.getJ()];

													}

													/*
													 * Chatnachricht an alle, dass ich einen Checkpoint erreicht habe
													 */
													sendMessage(LogicManager.this.myRobot.getPlayer().getUser().getAccountData()
															.getNickname()
															+ " "
															+ Page.props.roundPlayerPage_chat_checkpoint_reached_part1()
															+ " "
															+ (LogicManager.this.myRobot.getNumberOfReachedCheckpoints() + 1)
															+ " " + Page.props.roundPlayerPage_chat_checkpoint_reached_part2());
												} else {
													/*
													 * Listgrids updaten
													 */
													presenter.getOthersReadyRecords().get(robot.getPlayer().getUser().getId()).setNewCheckpoint(
															robot.getNumberOfReachedCheckpoints());
													presenter.getOthersStateRecords().get(robot.getPlayer().getUser().getId()).setNewCheckpoint(
															robot.getNumberOfReachedCheckpoints());
													presenter.getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
													presenter.getPage().getRobotStatusArea().getOthersStateGrid().redraw();
												}

												/*
												 * Anzahl der erreichten Checkpoints inkrementieren
												 */
												robot.setNumberOfReachedCheckoints(robot.getNumberOfReachedCheckpoints() + 1);
															

												/*
												 * Schauen, ob der Roboter gewonnen hat
												 */
												if (robot.getNumberOfReachedCheckpoints() == LogicManager.this.playingBoard
														.getNumberOfCheckpoints()) {

													/*
													 * Gewinnersound abspielen
													 */
													RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-gewinn-1.mp3");

													/*
													 * Ein Roboter hat gewonnen
													 */

													/*
													 * Punkte verteilen
													 */
													robot.setPoints(robot.getPoints() + 2500);
													if (robot == LogicManager.this.myRobot) {

														LogicManager.this.presenter.getPage().addChild(
																new WinnerWindowPresenter(presenter.getRoundInfo().getRoundId(),
																		LogicManager.this.presenter, robot.getPreviewImagePath(),
																		Page.props.roundPlayerPage_winner_you(), Page.props
																				.winnerwindow_information_self(), new ArrayList<Robot>(robots.values()), robot)
																		.getPage());

													} else {
														LogicManager.this.presenter.getPage().addChild(
																new WinnerWindowPresenter(presenter.getRoundInfo().getRoundId(),
																		LogicManager.this.presenter, robot.getPreviewImagePath(),
																		robot.getPlayer().getUser().getAccountData()
																				.getNickname(), Page.props
																				.winnerwindow_information_other(),new ArrayList<Robot>(robots.values()), robot)
																		.getPage());
													}

													LogicManager.this.roundFinished = true;
													AnimationTimer.this.i = 4;
													AnimationTimer.this.cancel();
													cancel();

													return null;
												}
											}

											/*
											 * prüfen, ob der Roboter auf einem RepairField steht
											 */
											if (LogicManager.this.playingBoard.getFields()[robot.getI()][robot.getJ()] instanceof RepairField) {

												/*
												 * Sound abspielen
												 */
												RoundPlayerPagePresenter.SOUND_MANAGER
														.play("reparatur-"
																+ ((RepairField) LogicManager.this.playingBoard.getFields()[robot
																		.getI()][robot.getJ()]).getNumberOfWrench() + ".mp3");

												/*
												 * Roboter reparieren
												 */
												robot.setDamageToken(robot.getDamageToken()
														- ((RepairField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot
																.getJ()]).getNumberOfWrench());

												/*
												 * Punkte vergeben
												 */
												robot.setPoints(robot.getPoints()
														+ ((RepairField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot
																.getJ()]).getNumberOfWrench() * 100);

												/*
												 * Für Awards
												 */
												robot.setNumberOfRepairs(robot.getNumberOfRepairs()
														+ ((RepairField) LogicManager.this.playingBoard.getFields()[robot.getI()][robot
																.getJ()]).getNumberOfWrench());

												if (robot == LogicManager.this.myRobot) {

													((PlayerStatusArea) LogicManager.this.presenter.getPage()
															.getRobotStatusArea()).setDamageToken(robot.getDamageToken());

													if (LogicManager.this.myRobot.getNumberOfBackupCards() > 0) {
														backupField = (BackupField) LogicManager.this.playingBoard.getFields()[LogicManager.this.myRobot
																.getI()][LogicManager.this.myRobot.getJ()];
													}
												} else {

													LogicManager.this.presenter.getOthersStateRecords()
															.get(robot.getPlayer().getUser().getId())
															.updateDamageToken(robot.getDamageToken());
													LogicManager.this.presenter.getPage().getRobotStatusArea()
															.getOthersStateArea().redraw();
												}
											}
										}
									}

									return backupField;
								}

								@Override
								public void run() {

									/*
									 * Wenn noch ein Roboter am Leben ist, sollen die Machinen arbeiten
									 */
									if (!allRobotsDead()) {

										/*
										 * Alle Maschinenelemente wurde animiert
										 */
										if (++this.i >= this.animateList.size()) {

											/*
											 * 
											 * RoboterLaser
											 */
											if (this.robotLaserAnimated == false) {
												for (Animate animate : addRobotLaser(this)) {
													animate.start();
												}
												this.robotLaserAnimated = true;
												return;
											}

											/*
											 * hier schauen, ob jmd gewonnen hat, oder ob mein Roboter auf einem Backupfield
											 * steht.
											 */
											final BackupField backupField;
											if ((backupField = checkGameStatus()) != null) {

												/*
												 * Chatnachricht an alle, dass ich meine Backupkarte gelegt habe
												 */
												sendMessage(LogicManager.this.myRobot.getPlayer().getUser().getAccountData()
														.getNickname()
														+ " " + Page.props.roundPlayerPage_chat_backup_announcement());

												/*
												 * Dialog öffnen
												 */
												BackupWindow.showBackupWindow(LogicManager.this.presenter.getPage(),
														new BooleanCallback() {

															/**
															 * Der Dialog wurde geklickt oder die Zeit ist abgelaufen
															 */
															@Override
															public void execute(Boolean value) {
																if (value) {
																	
																	
																	LogicManager.this.presenter.sendRestartField(
																			backupField.getI(), backupField.getJ());

																	LogicManager.this.myRobot
																			.setNumberOfBackupCards(LogicManager.this.myRobot
																					.getNumberOfBackupCards() - 1);

																	/*
																	 * Chatnachricht an alle , dass ich meine Backupkarte gelegt
																	 * habe
																	 */
																	sendMessage(LogicManager.this.myRobot.getPlayer().getUser()
																			.getAccountData().getNickname()
																			+ " "
																			+ Page.props.roundPlayerPage_chat_backup_success());

																	/*
																	 * BackupkartenBild anzeigen
																	 */

																	LogicManager.this.presenter
																			.getPage()
																			.getDrawingArea()
																			.add(new Image(backupField.getJ() * 50, backupField
																					.getI() * 50, 50, 50,
																					"images/robotStatus/repairfield_startpoint.png"));

																	// Alle
																	// Roboter
																	// wieder
																	// zur
																	// Front
																	// bringen
																	for (final Robot robot : LogicManager.this.robots.values()) {
																		final Image img = LogicManager.this.presenter
																				.getRobotsImageList().get(robot);
																		if (img.getParent() == LogicManager.this.presenter
																				.getPage().getDrawingArea()) {
																			LogicManager.this.presenter.getPage()
																					.getDrawingArea().bringToFront(img);
																		}
																	}
																} else {

																	/*
																	 * Chatnachricht an alle , dass ich meine Backupkarte nicht
																	 * gelegt habe
																	 */
																	sendMessage(LogicManager.this.myRobot.getPlayer().getUser()
																			.getAccountData().getNickname()
																			+ " " + Page.props.roundPlayerPage_chat_backup_fail());

																}

																if (!LogicManager.this.myRobotDead) {
																	LogicManager.this.presenter
																			.setStepReady(LogicManager.this.robotState);
																} else {
																	// animationTimer
																	// =
																	// null;
																}
																LogicManager.this.robotState = 0;
															}
														});
											} else {

												/*
												 * die nächsten Programmierkarten aller Spieler ausführen
												 */
												if (!LogicManager.this.myRobotDead) {
													LogicManager.this.presenter.setStepReady(LogicManager.this.robotState);
												}
											}

											return;
										}

										/*
										 * 
										 * Maschinenanimation ausführen
										 */
										for (Animate animate : this.animateList.get(this.i)) {
											animate.start();
										}
									} else {
										if (!LogicManager.this.myRobotDead) {
											LogicManager.this.presenter.setStepReady(LogicManager.this.robotState);
										}
									}
								}

							}.schedule(100);
							return;
						}

						/*
						 * den aktiven Roboter(der bewegende) speichern
						 */
						final Robot robot = LogicManager.this.robots.get(cardsList.get(this.i).getPlayerId());

						/*
						 * Der Roboter darf nicht schon gestorben sein
						 */
						if (!robot.isDeadForTurn() && robot.getPowerDown() != 2) {

							/*
							 * Programmierkarte umdrehen
							 */
							if (!LogicManager.this.myRobot.isDeadForTurn() && LogicManager.this.myRobot.getPowerDown() != 2) {
								ProgrammingcardImg programmingcardImg = (ProgrammingcardImg) ((PlayerStatusArea) LogicManager.this.presenter
										.getPage().getRobotStatusArea()).getCardSetSlots()[AnimationTimer.this.i].getMember(0);
								if (programmingcardImg != null) {
									programmingcardImg.setSrc(programmingcardImg.getProgrammingcard().getImagePath());
									programmingcardImg.setPriorityVisible(true);
								}
							}

							/*
							 * **************************
							 * Den Roboterbefehl ausführen **************************
							 */
							final Timer stepTimer = this;
							switch (cardsList.get(this.i).getRobotMovement()) {
							case TURN_LEFT:
								/*
								 * Für Awards
								 */
								robot.setNumberOfTurns(robot.getNumberOfTurns() + 1);
								if (!robot.isHasAwardHamster() && AnimationTimer.this.i > 1) {
									if (AnimationTimer.this.programmingcardsMap.get(robot.getPlayer().getUser().getId())
											.get(AnimationTimer.this.i - 1).getRobotMovement() == RobotMovement.TURN_LEFT
											&& AnimationTimer.this.programmingcardsMap.get(robot.getPlayer().getUser().getId())
													.get(AnimationTimer.this.i - 2).getRobotMovement() == RobotMovement.TURN_LEFT) {

										robot.setHasAwardHamster(true);

										if (robot == LogicManager.this.myRobot) {
											/*
											 * Sound abspielen
											 */
											RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-award-1.mp3");

											/*
											 * ChatNachricht senden
											 */
											sendMessage(robot.getPlayer().getUser().getAccountData().getNickname() + " "
													+ Page.props.roundPlayerPage_chat_hamsteraward());

											/*
											 * Animation starten
											 */
											final Image awardImage = new Image(robot.getJ() * 50 + 25, robot.getI() * 50 + 25, 0,
													0, "images/awards/hamster.png");
											LogicManager.this.presenter.getPage().getDrawingArea().add(awardImage);
											new Animate(awardImage, "rotation", 0, 360, 800).start();
											new Animate(awardImage, "x", robot.getJ() * 50 + 25, robot.getJ() * 50, 800).start();
											new Animate(awardImage, "y", robot.getI() * 50 + 25, robot.getI() * 50, 800).start();
											new Animate(awardImage, "width", 0, 50, 800).start();
											new Animate(awardImage, "height", 0, 50, 800) {
												public void onComplete() {
													new Timer() {
														@Override
														public void run() {
															LogicManager.this.presenter.getPage().getDrawingArea()
																	.remove(awardImage);
														}

													}.schedule(1000);
												}
											}.start();
										}
									}
								}

								robot.setDirection(Direction.getDirection(robot.getDirection(), RobotMovement.TURN_LEFT));
								new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot), "rotation", 0, -90, 1000) {

									public void onComplete() {
										final Image curr = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50,
												robot.getImagePath());

										LogicManager.this.presenter.getPage().getDrawingArea().add(curr);

										LogicManager.this.presenter.getRobotsImageList().get(robot).setVisible(false);
										LogicManager.this.presenter.getPage().getDrawingArea()
												.remove(LogicManager.this.presenter.getRobotsImageList().get(robot));
										LogicManager.this.presenter.getRobotsImageList().put(robot, curr);

										schedule(100);
									}

								}.start();
								return;
							case TURN_RIGHT:
								/*
								 * Für Awards
								 */
								robot.setNumberOfTurns(robot.getNumberOfTurns() + 1);

								robot.setDirection(Direction.getDirection(robot.getDirection(), RobotMovement.TURN_RIGHT));

								new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot), "rotation", 0, 90, 1000) {

									public void onComplete() {
										final Image curr = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50,
												robot.getImagePath());

										LogicManager.this.presenter.getPage().getDrawingArea().add(curr);

										LogicManager.this.presenter.getRobotsImageList().get(robot).setVisible(false);
										LogicManager.this.presenter.getPage().getDrawingArea()
												.remove(LogicManager.this.presenter.getRobotsImageList().get(robot));
										LogicManager.this.presenter.getRobotsImageList().put(robot, curr);

										schedule(100);
									}

								}.start();
								return;
							case TURN_AROUND:
								/*
								 * Für Awards
								 */
								robot.setNumberOfTurns(robot.getNumberOfTurns() + 2);

								robot.setDirection(Direction.getDirection(robot.getDirection(), RobotMovement.TURN_AROUND));
								new Animate(LogicManager.this.presenter.getRobotsImageList().get(robot), "rotation", 0, 180, 1000) {

									public void onComplete() {
										final Image curr = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50,
												robot.getImagePath());

										LogicManager.this.presenter.getPage().getDrawingArea().add(curr);

										LogicManager.this.presenter.getRobotsImageList().get(robot).setVisible(false);
										LogicManager.this.presenter.getPage().getDrawingArea()
												.remove(LogicManager.this.presenter.getRobotsImageList().get(robot));
										LogicManager.this.presenter.getRobotsImageList().put(robot, curr);

										schedule(100);
									}

								}.start();
								return;
							case MOVE_FORWARDS:

								new Timer() {

									final List<List<Animate>> list = move(robot, robot.getDirection(), this, new boolean[1], true);

									int index = -1;

									@Override
									public void run() {

										if ((this.index += 1) == this.list.size()) {
											LogicManager.this.animateReplayList.addAll(this.list);
											stepTimer.schedule(100);
											return;
										}

										for (Animate animate : this.list.get(this.index)) {
											animate.start();
										}
									}
								}.schedule(100);
								return;
							case MOVE_TWO_FORWARDS:

								new Timer() {

									List<List<Animate>> list = move(robot, robot.getDirection(), this, new boolean[1], true);

									int index = -1;

									@Override
									public void run() {

										if (this.index == -1) {
											this.list.addAll(move(robot, robot.getDirection(), this, new boolean[1], true));
										}

										if ((this.index += 1) == this.list.size()) {
											LogicManager.this.animateReplayList.addAll(this.list);
											stepTimer.schedule(100);
											return;
										}

										for (Animate animate : this.list.get(this.index)) {
											animate.start();
										}
									}
								}.schedule(100);
								return;
							case MOVE_THREE_FORWARDS:
								new Timer() {

									List<List<Animate>> list = move(robot, robot.getDirection(), this, new boolean[1], true);

									int index = -1;

									@Override
									public void run() {

										if (this.index == -1) {
											this.list
													.addAll(move(robot, robot.getDirection(), this, new boolean[1], true));
											this.list.addAll(move(robot, robot.getDirection(), this, new boolean[1], true));
										}

										if ((this.index += 1) == this.list.size()) {
											LogicManager.this.animateReplayList.addAll(this.list);
											stepTimer.schedule(100);
											return;
										}

										for (Animate animate : this.list.get(this.index)) {
											animate.start();
										}
									}
								}.schedule(100);
								return;
							case MOVE_BACKWARDS:
								new Timer() {

									List<List<Animate>> list = move(robot,
											Direction.getDirection(robot.getDirection(), RobotMovement.TURN_AROUND), this,
											new boolean[1], true);

									int index = -1;

									@Override
									public void run() {

										if ((this.index += 1) == this.list.size()) {
											stepTimer.schedule(100);
											return;
										}

										for (Animate animate : this.list.get(this.index)) {
											animate.start();
										}
									}
								}.schedule(100);
								return;
							}
						} else {
							schedule(100);
						}
					}
				}.schedule(100);
			} catch (Exception e) {
				/*
				 * Wird verschluckt
				 */
			}
		}

		/**
		 * Versieht einen Roboter mit Laserschaden
		 * 
		 * @param hit
		 * @param laserAnimationListener
		 * @param laser
		 */
		private void setDamageToRobot(final Robot hit, final LaserAnimationListener laserAnimationListener, final Line laser) {

			/*
			 * Schadenspunkte verteilen
			 */
			hit.setDamageToken(hit.getDamageToken() + 1);

			/*
			 * Sound abspielen
			 */
			RoundPlayerPagePresenter.SOUND_MANAGER.play("robotreffer_1.mp3");

			/*
			 * für Awards
			 */
			hit.setNumberOfLaserHits(hit.getNumberOfLaserHits() + 1);

			if (hit != LogicManager.this.myRobot) {

				LogicManager.this.presenter.getOthersStateRecords().get(hit.getPlayer().getUser().getId())
						.updateDamageToken(hit.getDamageToken());
				LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersStateArea().redraw();

			} else {
				((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea())
						.setDamageToken(LogicManager.this.myRobot.getDamageToken());

			}

			/*
			 * nach 400 millisekunden wird der Laser wieder entfernt
			 */
			new Timer() {
				@Override
				public void run() {
					LogicManager.this.presenter.getPage().getDrawingArea().remove(laser);
					laserAnimationListener.animationReady();
				}

			}.schedule(400);
		}

		/**
		 * Lässt einen Roboter STERBEN
		 */
		private void setRobotDead(final Robot robot) {
			// Werte der Roboter anpassen
			robot.setLifePoints(robot.getLifePoints() - 1);
			robot.setDamageToken(0);

			robot.setI(-1);
			robot.setJ(-1);
			robot.setDeadForTurn(true);

			/*
			 * Für Awards
			 */
			robot.setNumberOfDeaths(robot.getNumberOfDeaths() + 1);

			/*
			 * Sound abspielen
			 */
			RoundPlayerPagePresenter.SOUND_MANAGER.play("robo-boom_1.mp3");

			if (robot != LogicManager.this.myRobot) {
				
				// Lifepoints decrementieren
				LogicManager.this.presenter.getOthersStateRecords().get(robot.getPlayer().getUser().getId())
						.setNumberOfLifeToken(robot.getLifePoints() + 1);

				// damagetoken wieder auf 0 setzten
				LogicManager.this.presenter.getOthersStateRecords().get(robot.getPlayer().getUser().getId()).updateDamageToken(0);

				if (robot.getLifePoints() == 0) {
					robot.setDead(true);

					// Window anzeigen, dass ein anderer Roboter tot ist
					InformationWindow.showWindow(
							LogicManager.this.presenter.getPage(),
							Page.props.informationWindow_dead_title(),
							"" + Page.props.informationWindow_dead_other_text_part1()
									+ robot.getPlayer().getUser().getAccountData().getNickname()
									+ Page.props.informationWindow_dead_other_text_part2(), "ui/robot_Lost.png");

					LogicManager.this.presenter.getOthersStateRecords().get(robot.getPlayer().getUser().getId()).setDead();
					LogicManager.this.presenter.getOthersReadyRecords().get(robot.getPlayer().getUser().getId()).setDead();

					LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
				}

				LogicManager.this.presenter.getPage().getRobotStatusArea().getOthersStateGrid().redraw();
			} else {
				/*
				 * Anzeigen, dass man tot ist
				 */
				presenter.setAreaState(RoundPlayerPagePresenter.DEAD);
				
				((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea()).setLifeToken(robot
						.getLifePoints());

				((PlayerStatusArea) LogicManager.this.presenter.getPage().getRobotStatusArea())
						.setDamageToken(LogicManager.this.myRobot.getDamageToken());

				if (robot.getLifePoints() == 0) {

					LogicManager.this.robotState = 2;
					robot.setDead(true);

					// Window anzeigen, dass man selbst tot ist
					InformationWindow.showWindow(LogicManager.this.presenter.getPage(), Page.props.informationWindow_dead_title(),
							Page.props.informationWindow_dead_me_text(), "ui/robot_Lost.png");

					// Bilder werden versteckt
					LogicManager.this.presenter.setAreaState(RoundPlayerPagePresenter.DEAD);

					/*
					 * ChatNachricht schreiben
					 */
					sendMessage(robot.getPlayer().getUser().getAccountData().getNickname() + " "
							+ Page.props.roundPlayerPage_chat_dead());
				}
			}

			LogicManager.this.presenter.getPage().getDrawingArea()
					.remove(LogicManager.this.presenter.getRobotsImageList().get(robot));
		}

		/**
		 * Liefert eine Animationsliste zu Machinen
		 * 
		 * @param timer
		 * @return
		 */
		private List<List<Animate>> startMachines(final Timer timer) {

			/*
			 * die Haupliste, die alle Animationen erhält
			 */
			final List<List<Animate>> animateList = new ArrayList<List<Animate>>();

			/*
			 * Einfache Förderbänder
			 */
			if (LogicManager.this.roundOption.isAlwaysConveyorBeltsOn()) {

				/*
				 * Förderbänder mit Reichweite 2 werden um 1 bewegt.
				 */
				if (!LogicManager.this.playingBoard.getConveyorBeltFieldRangeTwoList().isEmpty()) {
					addConveyorBelts(animateList, true, timer);
				}

				/*
				 * Förderbänder mit Reichweite 1 UND 2 werden um 1 bewegt.
				 */
				if (!LogicManager.this.playingBoard.getConveyorBeltFieldRangeOneList().isEmpty()
						|| !LogicManager.this.playingBoard.getConveyorBeltFieldRangeTwoList().isEmpty()) {

					addConveyorBelts(animateList, false, timer);
				}
			}

			/*
			 * Pressen
			 */
			if (LogicManager.this.roundOption.isAlwaysCompactorsOn()) {
				addCompactors(animateList, timer);
			}

			/*
			 * Pusher
			 */
			if (LogicManager.this.roundOption.isAlwaysPushersOn()) {
				addPusher(animateList, timer);
			}

			/*
			 * Zahnräder
			 */
			if (LogicManager.this.roundOption.isAlwaysGearsOn()) {

				addGears(animateList, timer);
			}

			/*
			 * Wandlaseranimationen erstellen
			 */
			if (LogicManager.this.roundOption.isLasersOn()) {
				addWallLaser(animateList, timer);
			}

			/*
			 * 
			 * die Animationen zurückliefern
			 */
			return animateList;
		}
	}

	/**
	 * der Chat, in dem auch Infos gepostet werden
	 */
	private final ChatServiceAsync chatService = GWT.create(ChatService.class);

	/** Seriennummer */
	private static final long serialVersionUID = 9134377438961759886L;

	/** Zum Bewegen der Robots muss der RobotManager das PlayingBoard kennen */
	private PlayingBoard playingBoard;

	/** der LogicManager muss die Spielregeln kennen */
	private RoundOption roundOption;

	/** Liste mit den Robotern einer Spielrunde als key die UserId */
	private Map<Integer, Robot> robots;

	/** Presenter */
	private RoundPlayerPagePresenter presenter;

	/** AnimationTimer */
	private AnimationTimer animationTimer;

	/** der Roboter, der mir gehört */
	private Robot myRobot;

	/** speichert Prioritäten für BackUp-Felder */
	private Map<Position, Stack<Integer>> backupFieldStack = new HashMap<Position, Stack<Integer>>();

	/** gibt an, ob die Spielrunde beendet ist */
	private boolean roundFinished = false;

	private boolean myRobotDead = false;

	/** Stauts der Roboter (für Readystatus... sonst Marcus fragen) */
	private int robotState = 0;

	/**
	 * 
	 * die ReplayListe
	 * 
	 */
	private final List<List<Animate>> animateReplayList = new ArrayList<List<Animate>>();

	/**
	 * Default-Konstruktor
	 */
	public LogicManager(RoundPlayerPagePresenter presenter) {
		this.presenter = presenter;

	}

	/**
	 * 
	 */
	public void createBackupFieldMap() {
		/*
		 * Backupfelder hinzufügen
		 */
		for (final Robot robot : this.robots.values()) {
			final Stack<Integer> stack = new Stack<Integer>();
			stack.add(robot.getPlayer().getUser().getId());
			robot.setStartField((BackupField) playingBoard.getFields()[robot.getStartField().getI()][robot.getStartField().getJ()]);
			this.backupFieldStack.put(new Position(robot.getStartField().getI(), robot.getStartField().getJ()), stack);
		}

		for (final BackupField field : this.playingBoard.getCheckpointFieldList()) {
			this.backupFieldStack.put(new Position(field.getI(), field.getJ()), new Stack<Integer>());
		}

		for (final BackupField field : this.playingBoard.getRepairFieldList()) {
			this.backupFieldStack.put(new Position(field.getI(), field.getJ()), new Stack<Integer>());
		}
	}

	public List<List<Animate>> getAnimateReplayList() {
		return this.animateReplayList;
	}

	/**
	 * @return the animationTimer
	 */
	public AnimationTimerAbstract getAnimationTimer() {
		return this.animationTimer;
	}

	public Map<Position, Stack<Integer>> getBackupFieldStack() {
		return this.backupFieldStack;
	}

	public Robot getMyRobot() {
		return this.myRobot;
	}


	/**
	 * @return the playingBoard
	 */
	public PlayingBoard getPlayingBoard() {
		return this.playingBoard;
	}

	/**
	 * Schaut, ob auf der besagten Position ein Roboter steht und liefert diesen zurück
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Robot getRobotByPosition(final int i, final int j) {
		for (final Robot r : this.robots.values()) {
			if (r.getI() == i && r.getJ() == j) {
				return r;
			}
		}
		return null;
	}


	/**
	 * Liefert die angemeldeten Roboter
	 * 
	 * @return die Roboterliste
	 */
	public Map<Integer, Robot> getRobots() {
		return this.robots;
	}

	/**
	 * @return the roundOption
	 */
	public RoundOption getRoundOption() {
		return this.roundOption;
	}

	/**
	 * Wird ausgeführt, wenn das Spiel zu Ende ist.
	 */
	public boolean isGameFinished() {

		int numberOfRobots = 0;
		Robot winner = null;
		for (final Robot r : this.robots.values()) {
			if (!r.isDead()) {
				numberOfRobots += 1;
				winner = r;
			}
		}

		if (numberOfRobots == 1) {
			LogicManager.this.roundFinished = true;

			/*
			 * die Round-Domain entfernen
			 */
			this.presenter.unlistenRemoteListener();

			// Gewinnersound abspielen
			RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-gewinn-1.mp3");

			/*
			 * Ein Roboter hat gewonnen
			 */
			winner.setPoints(winner.getPoints() + 2500);
			if (winner == this.myRobot) { // Wenn du gewonnen hast
				this.presenter.getPage().addChild(
						new WinnerWindowPresenter(presenter.getRoundInfo().getRoundId(), this.presenter, winner
								.getPreviewImagePath(), Page.props.roundPlayerPage_winner_you(), Page.props
								.winnerwindow_information_self(), new ArrayList<Robot>(robots.values()), winner).getPage());
			} else { // Wenn ein anderer gewonnen hat
				this.presenter.getPage().addChild(
						new WinnerWindowPresenter(presenter.getRoundInfo().getRoundId(), this.presenter, winner
								.getPreviewImagePath(), winner.getPlayer().getUser().getAccountData().getNickname(), Page.props
								.winnerwindow_information_other(), new ArrayList<Robot>(robots.values()), winner).getPage());
			}

			return true;

		} else if (numberOfRobots == 0) {
			LogicManager.this.roundFinished = true;

			/*
			 * die Round-Domain entfernen
			 */
			this.presenter.unlistenRemoteListener();

			// Gewinnersound abspielen
			RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-gewinn-1.mp3");

			// Window anzeigen
			this.presenter.getPage().addChild(
					new WinnerWindowPresenter(presenter.getRoundInfo().getRoundId(), this.presenter, "ui/robot_Nobody_Won.png",
							Page.props.roundPlayerPage_winner_nobody(), Page.props.winnerwindow_information_other(), new ArrayList<Robot>(robots.values()), null)
							.getPage());

			
			return true;
		}

		return false;
	}

	/**
	 * Mit dieser Methode werden alle angemeldeten Roboter bewegt.
	 * 
	 * @param ranking
	 *            die reihenfolge der Roboter
	 * @param movments
	 *            die bewegungen der Roboter
	 * @return
	 */
	public void moveAllRobots(final Map<Integer, List<Programmingcard>> programmingcardsMap) {

		this.animationTimer = new AnimationTimer(programmingcardsMap);
		this.animationTimer.schedule(500);

	}

	/**
	 * Entfernt einen Roboter aus dem RobotManager.
	 * 
	 * @param robot
	 *            der zu entfernende Roboter
	 * @return true, wenn das Entfernen geklappt hat
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn der übergebene Roboter nicht existiert.
	 */


	/**
	 * Chatnachricht über bestimmte Erreignisse meines Roboters senden
	 * 
	 * @param message
	 */
	public void sendMessage(final String message) {
		this.chatService.sendGeneratedRoundMessage(message, this.myRobot.getColor().toString().toLowerCase(),
				"internalChatRound:" + this.presenter.getRoundInfo().getRoundId(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						/*
						 * Ausgeben der Fehlermeldung
						 */
						SC.say(Page.props.global_title_error(), caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						/*
						 * Nix
						 */
					}
				});
	}

	public void setMeLeft(boolean left) {
		this.myRobotDead = left;
	}

	/**
	 * setzt
	 * 
	 * @return
	 */
	public Robot setMyRobot() {
		return this.myRobot = this.robots.get(UserPresenter.getInstance().getUser().getId());
	}

	/**
	 * @param playingBoard
	 *            the playingBoard to set
	 */
	public void setPlayingBoard(final PlayingBoard playingBoard) {
		// playingboard zuweisen
		this.playingBoard = playingBoard;
	}

	/**
	 * macht alle Roboter wieder lebendig und setzt sie auf ihr Restartfeld
	 */
	public void setRobotOnRestartField() {
	
		for (final Position position : this.backupFieldStack.keySet()) {
			
			outer: for (final int playerId : this.backupFieldStack.get(position)) {
				final Robot robot = this.robots.get(playerId);
				if (robot.isDeadForTurn() && !robot.isDead()) {
					final Field field = this.playingBoard.getFields()[position.getI()][position.getJ()];
					for (int n = 0;; n++) {
						for (int i = field.getI() - n; i <= field.getI() + n; i++) {
							for (int j = field.getJ() - n; j <= field.getJ() + n; j++) {
								if (i >= 0 && i < this.playingBoard.getHeight() && j >= 0 && j < this.playingBoard.getWidth()
										&& !(playingBoard.getFields()[i][j] instanceof HoleField) && !(playingBoard.getFields()[i][j] instanceof CheckpointField)
										&& getRobotByPosition(i, j) == null) {

									robot.setDeadForTurn(false);

									robot.setI(i);
									robot.setJ(j);

									/*
									 * Bild neu zeichnen
									 */
									final Image robotImage = this.presenter.getRobotsImageList().get(robot);
									robotImage.setRotation(0);
									robotImage.setWidth(50);
									robotImage.setHeight(50);
									robotImage.setX(j * 50);
									robotImage.setY(i * 50);
									robotImage.setHref(robot.getImagePath());
									this.presenter.getPage().getDrawingArea().remove(robotImage);
									this.presenter.getPage().getDrawingArea().add(robotImage);

									continue outer;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param robots
	 *            the robots to set
	 */
	public void setRobots(final Map<Integer, Robot> robots) {
		this.robots = robots;
	}

	/**
	 * @param roundOption
	 *            the roundOption to set
	 */
	public void setRoundOption(final RoundOption roundOption) {
		this.roundOption = roundOption;
	}
}