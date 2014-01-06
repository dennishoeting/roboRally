package prototyp.client.presenter.mapgenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import prototyp.client.model.mapGenerator.FieldPropertySaver;
import prototyp.client.model.mapGenerator.WayPointComputer;
import prototyp.client.presenter.PagePresenter;
import prototyp.client.util.RadioToggleButton;
import prototyp.client.util.ToggleButton;
import prototyp.client.view.Page;
import prototyp.client.view.mapgenerator.MapGeneratorEditWindow.WindowContentStack;
import prototyp.client.view.mapgenerator.MapGeneratorPage;
import prototyp.client.view.mapgenerator.ProgressWindow;
import prototyp.shared.exception.field.ConveyorBeltFieldException;
import prototyp.shared.exception.playingboard.CheckpointFieldNotReachableException;
import prototyp.shared.field.BasicField;
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
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.util.Direction;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.LaserCannonInfo;
import prototyp.shared.util.Position;
import prototyp.shared.util.WallInfo;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.MouseStillDownEvent;
import com.smartgwt.client.widgets.events.MouseStillDownHandler;
import com.smartgwt.client.widgets.events.ScrolledEvent;
import com.smartgwt.client.widgets.events.ScrolledHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Presenter für den MapGenerator
 * 
 * @author Marcus, Timo, Dennis (Verantwortlicher)
 * 
 */
public class MapGeneratorPresenter implements PagePresenter {

	/**
	 * Fürs Scrollen auf der Drawingarea
	 * Hiermit wird das Rechteck auf der Minimap verschoben
	 * @author Marcus
	 *
	 */
	public class DrawingAreaScrollHandler implements ScrolledHandler {

		@Override
		public void onScrolled(ScrolledEvent event) {
			if(!page.getMinimap().isMousePressedOnMinimap()) {
				rectangleMove.setX(page.getDrawingAreaArea().getScrollLeft() / 5);
				rectangleMove.setY(page.getDrawingAreaArea().getScrollTop() / 5);
			}
		}
	}
	
	public class DrawingAreaChooseScrollhandler implements MouseStillDownHandler {

		@Override
		public void onMouseStillDown(MouseStillDownEvent event) {
			if(MapGeneratorPresenter.this.startDraggedX != -1) {
				final int x = event.getX() - page.getDrawingAreaAreaInner().getAbsoluteLeft() - page.getDrawingAreaArea().getScrollLeft();
				final int y = event.getY() - page.getDrawingAreaAreaInner().getAbsoluteTop() - page.getDrawingAreaArea().getScrollTop();
				if(x > 650) {
					final int range = (int)(-0.2*(700-x) + 25);
					page.getDrawingAreaArea().scrollTo(page.getDrawingAreaArea().getScrollLeft() + range, page.getDrawingAreaArea().getScrollTop());
					mouseMoveHandler.chooseFields(event.getX() - page.getDrawingAreaAreaInner().getAbsoluteLeft(),
						event.getY() - page.getDrawingAreaAreaInner().getAbsoluteTop());
				} else if(x < 50) {
					final int range = (int)(-0.2*x + 25);
					page.getDrawingAreaArea().scrollTo(page.getDrawingAreaArea().getScrollLeft() - range, page.getDrawingAreaArea().getScrollTop());
					mouseMoveHandler.chooseFields(event.getX() - page.getDrawingAreaAreaInner().getAbsoluteLeft(),
						event.getY() - page.getDrawingAreaAreaInner().getAbsoluteTop());
				}
				
				if(y > 420) {
					final int range = (int)(-0.2*(420-y) + 25);
					page.getDrawingAreaArea().scrollTo(page.getDrawingAreaArea().getScrollLeft(), page.getDrawingAreaArea().getScrollTop() + range);
					mouseMoveHandler.chooseFields(event.getX() - page.getDrawingAreaAreaInner().getAbsoluteLeft(),
						event.getY() - page.getDrawingAreaAreaInner().getAbsoluteTop());
				} else if(y < 50) {
					final int range = (int)(-0.2*y + 25);
					page.getDrawingAreaArea().scrollTo(page.getDrawingAreaArea().getScrollLeft(), page.getDrawingAreaArea().getScrollTop() - range);
					mouseMoveHandler.chooseFields(event.getX() - page.getDrawingAreaAreaInner().getAbsoluteLeft(),
						event.getY() - page.getDrawingAreaAreaInner().getAbsoluteTop());
				}
			}
		}
		
	}
	
	
	/**
	 * Listener zur Behandlung von MouseDownEvents
	 * 
	 * @see DrawingAreaMouseMoveHandler
	 * @see DrawingAreaMouseOutHandler
	 * @author Marcus
	 */
	private class DrawingAreaMouseDownHandler implements MouseDownHandler {

		@Override
		public void onMouseDown(MouseDownEvent event) {
			if(event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
				
				
				MapGeneratorPresenter.this.startDraggedX = MapGeneratorPresenter.this.currentDraggedX = MapGeneratorPresenter.this.jStart = MapGeneratorPresenter.this.jEnd = event
						.getX() / 50;
				MapGeneratorPresenter.this.startDraggedY = MapGeneratorPresenter.this.currentDraggedY = MapGeneratorPresenter.this.iStart = MapGeneratorPresenter.this.iEnd = event
						.getY() / 50;

				
				MapGeneratorPresenter.this.rectangleChoose.setX(MapGeneratorPresenter.this.startDraggedX * 50);
				MapGeneratorPresenter.this.rectangleChoose.setY(MapGeneratorPresenter.this.startDraggedY * 50);
				MapGeneratorPresenter.this.rectangleChoose.setWidth(50);
				MapGeneratorPresenter.this.rectangleChoose.setHeight(50);
				
				/*
				 * Für das Rechteck auf der Minimap
				 */
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setX(MapGeneratorPresenter.this.startDraggedX * 10);
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setY(MapGeneratorPresenter.this.startDraggedY * 10);
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setWidth(10);
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setHeight(10);
				
			}
		}
	}

	/**
	 * Listener zur Behandlung von MouseMoveEvents (Mehrfachauswahl)
	 * 
	 * @see DrawingAreaMouseDownHandler
	 * @see DrawingAreaMouseOutHandler
	 * @author Marcus
	 */
	private class DrawingAreaMouseMoveHandler implements MouseMoveHandler {

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
				this.chooseFields(event.getX(), event.getY());
			} 
		}
		
		public void chooseFields(final int x, final int y) {
			if (MapGeneratorPresenter.this.startDraggedX != -1 && (MapGeneratorPresenter.this.currentDraggedX != x / 50
					|| MapGeneratorPresenter.this.currentDraggedY != y / 50)) {
			MapGeneratorPresenter.this.currentDraggedX = MapGeneratorPresenter.this.jEnd = x / 50;
			MapGeneratorPresenter.this.currentDraggedY = MapGeneratorPresenter.this.iEnd = y / 50;

			if (MapGeneratorPresenter.this.currentDraggedX < MapGeneratorPresenter.this.startDraggedX
					&& MapGeneratorPresenter.this.currentDraggedY < MapGeneratorPresenter.this.startDraggedY) {
				MapGeneratorPresenter.this.rectangleChoose.setX(MapGeneratorPresenter.this.currentDraggedX * 50);
				MapGeneratorPresenter.this.rectangleChoose.setY(MapGeneratorPresenter.this.currentDraggedY * 50);

				MapGeneratorPresenter.this.rectangleChoose.setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX - 1) * 50));
				MapGeneratorPresenter.this.rectangleChoose.setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY - 1) * 50));
				
				/*
				 * Das Viereck auf der Minimap
				 */
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setX(MapGeneratorPresenter.this.currentDraggedX * 10);
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setY(MapGeneratorPresenter.this.currentDraggedY * 10);

				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX - 1) * 10));
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY - 1) * 10));
				
			} else if (MapGeneratorPresenter.this.currentDraggedX >= MapGeneratorPresenter.this.startDraggedX
					&& MapGeneratorPresenter.this.currentDraggedY >= MapGeneratorPresenter.this.startDraggedY) {
				
				MapGeneratorPresenter.this.rectangleChoose.setX(MapGeneratorPresenter.this.startDraggedX * 50);
				MapGeneratorPresenter.this.rectangleChoose.setY(MapGeneratorPresenter.this.startDraggedY * 50);

				MapGeneratorPresenter.this.rectangleChoose.setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX + 1) * 50));
				MapGeneratorPresenter.this.rectangleChoose.setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY + 1) * 50));
				
				
				/*
				 * Für das Dreieck auf der Minimap
				 */
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setX(MapGeneratorPresenter.this.startDraggedX * 10);
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setY(MapGeneratorPresenter.this.startDraggedY * 10);

				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX + 1) * 10));
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY + 1) * 10));
			} else if (MapGeneratorPresenter.this.currentDraggedY < MapGeneratorPresenter.this.startDraggedY) {
				
				MapGeneratorPresenter.this.rectangleChoose.setX(MapGeneratorPresenter.this.startDraggedX * 50);
				MapGeneratorPresenter.this.rectangleChoose.setY(MapGeneratorPresenter.this.currentDraggedY * 50);

				MapGeneratorPresenter.this.rectangleChoose.setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX + 1) * 50));
				MapGeneratorPresenter.this.rectangleChoose.setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY - 1) * 50));
				
				/*
				 * Für das Dreieck auf der Minimap
				 */
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setX(MapGeneratorPresenter.this.startDraggedX * 10);
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setY(MapGeneratorPresenter.this.currentDraggedY * 10);

				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX + 1) * 10));
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY - 1) * 10));
			} else {
				
				MapGeneratorPresenter.this.rectangleChoose.setX(MapGeneratorPresenter.this.currentDraggedX * 50);
				MapGeneratorPresenter.this.rectangleChoose.setY(MapGeneratorPresenter.this.startDraggedY * 50);

				MapGeneratorPresenter.this.rectangleChoose.setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX - 1) * 50));
				MapGeneratorPresenter.this.rectangleChoose.setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY + 1) * 50));
				
				/*
				 * Für das Dreieck auf der Minimap
				 */
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setX(MapGeneratorPresenter.this.currentDraggedX * 10);
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setY(MapGeneratorPresenter.this.startDraggedY * 10);

				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setWidth(Math.abs((MapGeneratorPresenter.this.currentDraggedX
						- MapGeneratorPresenter.this.startDraggedX - 1) * 10));
				MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose().setHeight(Math.abs((MapGeneratorPresenter.this.currentDraggedY
						- MapGeneratorPresenter.this.startDraggedY + 1) * 10));
			}
			}
		
		}
	}

	/**
	 * Listener zur Behandlung eines MouseOutEvents
	 * 
	 * @see DrawingAreaMouseDownHandler
	 * @see DrawingAreaMouseMoveHandler
	 * @author Marcus
	 */
	private class DrawingAreaMouseOutHandler implements MouseOutHandler {
		@Override
		public void onMouseOut(MouseOutEvent event) {
			
			if(event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
				MapGeneratorPresenter.this.startDraggedX = -1;
				if (testFields()) {
					updateElements();
				} else {
					MapGeneratorPresenter.this.page.getEditingWindow().getMainSectionArea().getMembers()[MapGeneratorPresenter.this.showingWindowContentID]
							.hide();
					MapGeneratorPresenter.this.page.getEditingWindow().getMainSectionArea().getMembers()[MapGeneratorPresenter.this.showingWindowContentID = 0]
							.show();
					MapGeneratorPresenter.this.page.getFieldTypeBox().setValue("");
				}
			} 
		}

	}

	/**
	 * 
	 * @author Marcus
	 * 
	 */
	private class DrawingAreaMouseUpHandler implements MouseUpHandler {

		@Override
		public void onMouseUp(MouseUpEvent event) {
			if(event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
				MapGeneratorPresenter.this.startDraggedX = -1;
				if (testFields()) {
					updateElements();
				} else {
					MapGeneratorPresenter.this.page.getEditingWindow().getMainSectionArea().getMembers()[MapGeneratorPresenter.this.showingWindowContentID]
						.hide();
					MapGeneratorPresenter.this.page.getEditingWindow().getMainSectionArea().getMembers()[MapGeneratorPresenter.this.showingWindowContentID = 0]
						.show();
					MapGeneratorPresenter.this.page.getFieldTypeBox().setValue("");
				}
			} 
		}
	}

	/**
	 * Liefert die FieldTypeID
	 * 
	 * @param value
	 *            der FieldName
	 * @return fieldTypeID
	 */
	private static int getFieldTypeIDByName(final String value) {
		if (value.equals("BasicField")) {
			return 1;
		} else if (value.equals("StartField")) {
			return 2;
		} else if (value.equals("CheckpointField")) {
			return 3;
		} else if (value.equals("GearField")) {
			return 4;
		} else if (value.equals("HoleField")) {
			return 5;
		} else if (value.equals("PusherField")) {
			return 6;
		} else if (value.equals("CompactorField")) {
			return 7;
		} else if (value.equals("RepairField")) {
			return 8;
		} else if (value.equals("ConveyorBeltField")) {
			return 9;
		}

		return 0;
	}

	/** gibt an, ob die Karte geändert wurde */
	private boolean changed = false;

	private int currentDraggedX = -1;

	private int currentDraggedY = -1;

	/** die zu bearbeitende */
	private Field[][] fields;

	/** das ImageListArray für die Felder */
	private ArrayList<Image>[][] fieldsImageList;

	private int iEnd = 0;

	/** der -Index des aktuell ausgewählten Spielfeldes */
	private int iStart = 0;

	private int jEnd = 0;

	/** der X-Index des aktuell ausgewählten Spielfeldes */
	private int jStart = 0;
	
	
	/** Klasse die die letzten Eigenschaften der Felder speichert */
	private final FieldPropertySaver fieldPropertySaver = new FieldPropertySaver(); 
	

	/** der Scrollhandler für die DrawingArea */
	private final DrawingAreaScrollHandler scrollHandler = new DrawingAreaScrollHandler();
	
	/** ChooseScrollhandler für die DrawingArea */
	private final DrawingAreaChooseScrollhandler chooseScrollhandler = new DrawingAreaChooseScrollhandler();
	
	/** der MouseDownHandler für die DrawingArea */
	private final DrawingAreaMouseDownHandler mouseDownHandler = new DrawingAreaMouseDownHandler();

	/** der MouseDownHandler für die DrawingArea */
	private final DrawingAreaMouseMoveHandler mouseMoveHandler = new DrawingAreaMouseMoveHandler();

	/** der MouseDownHandler für die DrawingArea */
	private final DrawingAreaMouseOutHandler mouseOutHandler = new DrawingAreaMouseOutHandler();

	/** der MouseDownHandler für die DrawingArea */
	private final DrawingAreaMouseUpHandler mouseUpHandler = new DrawingAreaMouseUpHandler();

	/** die zum Presenter zugehörige Page */
	private final MapGeneratorPage page;

	/** das Auswahlrechteck */
	private final Rectangle rectangleChoose = new Rectangle(0, 0, 50, 50);

	/** das Bewegungsrechteck */
	private final Rectangle rectangleMove = new Rectangle(0, 0, 140, 94);

	/** gibt an, welche Area der MapGeneratorEditingWindow aktiviert sein soll */
	private int showingWindowContentID;

	/** fürs scrollen auf der kleinen map */
	private int startDraggedX = -1;

	private int startDraggedY = -1;

	private boolean menueIsShown;

	/** Speichern des Namen */
	private String saveName = "";

	/** Speichern der Beschreibung */
	private String saveDescription = "";

	/** Speichern der Schwierigkeit */
	private String saveDifficulty = "";

	/** Speichern des WindowContentStack */
	private WindowContentStack windowContentStack;

	/** die Waypoints */
	private final List<List<VectorObject>> waypoints = new ArrayList<List<VectorObject>>();

	
	
	
	/**
	 * Konstruktor zum Erzeugen eines MapGeneratorPresenters
	 */
	public MapGeneratorPresenter() {
		this.menueIsShown = true;

		this.page = new MapGeneratorPage();
		//page.getDrawingAreaArea().addmou
		this.showingWindowContentID = 0;

		this.rectangleChoose.setFillOpacity(0.0);
		this.rectangleChoose.setStrokeColor("darkred");
		this.rectangleChoose.setStrokeWidth(2);

		this.rectangleMove.setStrokeWidth(1);
		this.rectangleMove.setFillOpacity(0.0);
		this.rectangleMove.setStrokeOpacity(1);
		this.rectangleMove.setStrokeColor("red");
		
		this.page.getEditingWindow().getCheckBox_showWays().setValue(Boolean.valueOf(Cookies.getCookie("waypoints")));
		
		addListeners();
	}

	/**
	 * Diese Methode fügt den Bedienelementen der MapGeneretorPage Listener hinzu
	 * 
	 * @return immer true
	 */
	private boolean addListeners() {

		/*
		 * Listener für die mainSection
		 */
		this.page.getEditingWindow().getButtonMenue().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setMenueVisibility(!MapGeneratorPresenter.this.menueIsShown);
			}
		});

		/*
		 * Hier wird ein ClickHandler für den Erstellen-Button hinzugefügt.
		 */
		this.page.getButtonCreate().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (MapGeneratorPresenter.this.changed) {
					SC.ask(Page.props.global_title_attention(), Page.props.mapGeneratorPresenter_createMap_error_message(),
							new BooleanCallback() {
								@Override
								public void execute(Boolean value) {
									if (value) {
										new MapGeneratorNewMapWindowPresenter(MapGeneratorPresenter.this);
									}
								}
							});
				} else {
					new MapGeneratorNewMapWindowPresenter(MapGeneratorPresenter.this);
				}
			}
		});

		/*
		 * Hier wird ein ClickHandler für den Speichern-Button hinzugefügt.
		 */
		this.page.getButtonSave().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new MapGeneratorSaveWindowPresenter(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this,
						MapGeneratorPresenter.this.saveName, MapGeneratorPresenter.this.saveDescription,
						MapGeneratorPresenter.this.saveDifficulty);
			}
		});

		/*
		 * Hier wird ein ClickHandler für den Laden-Button hinzugefügt.
		 */
		this.page.getButtonLoad().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (MapGeneratorPresenter.this.changed) {
					SC.ask(Page.props.global_title_attention(), Page.props.mapGeneratorPresenter_loadMap_error_message(),
							new BooleanCallback() {
								@Override
								public void execute(Boolean value) {
									if (value) {
										new MapGeneratorLoadWindowPresenter(MapGeneratorPresenter.this);
									}
								}
							});
				} else {
					new MapGeneratorLoadWindowPresenter(MapGeneratorPresenter.this);
				}
			}
		});

		/*
		 * Hier wird ein ClickHandler für den Entladen-Button hinzugefügt.
		 */
		this.page.getButtonUnload().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (MapGeneratorPresenter.this.changed) {
					SC.ask(Page.props.global_title_attention(), Page.props.mapGeneratorPresenter_unloadMap_error_message(),
							new BooleanCallback() {
								@Override
								public void execute(Boolean value) {
									if (value) {
										MapGeneratorPresenter.this.changed = false;
										MapGeneratorPresenter.this.page.getEditingWindow().getButtonMenue().setDisabled(true);
										MapGeneratorPresenter.this.page.getEditingWindow().getMiniMapArea().removeMembers(
												MapGeneratorPresenter.this.page.getEditingWindow().getMiniMapArea().getMembers());
										page.getButtonSave().setDisabled(true);
										page.getButtonUnload().setDisabled(true);
										MapGeneratorPresenter.this.page.getButtonTransform().setDisabled(true);
										page.getMainArea().removeChild(page.getDrawingAreaArea());
									}
								}
							});
				} else {
					MapGeneratorPresenter.this.changed = false;
					MapGeneratorPresenter.this.page.getEditingWindow().getButtonMenue().setDisabled(true);
					MapGeneratorPresenter.this.page.getEditingWindow().getMiniMapArea().removeMembers(
							MapGeneratorPresenter.this.page.getEditingWindow().getMiniMapArea().getMembers());
					page.getButtonSave().setDisabled(true);
					page.getButtonUnload().setDisabled(true);
					MapGeneratorPresenter.this.page.getButtonTransform().setDisabled(true);
					page.getMainArea().removeChild(page.getDrawingAreaArea());
				}
			}
		});
		
		/*
		 * Ereignis behandlung für den Löschen Knopf
		 */
		this.page.getButtonDelete().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new MapGeneratorDeleteWindowPresenter(MapGeneratorPresenter.this);
			}
		});
		
		
		/*
		 * Ereignisbehandlung für den TransformButton
		 */
		this.page.getButtonTransform().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new MapGeneratorTransformWindowPresenter(MapGeneratorPresenter.this);
			}
		});
		
		/*
		 * Wegbrechnung
		 */
		this.page.getEditingWindow().getCheckBox_showWays().addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				if ((Boolean) event.getValue() == true) {
					try {
						new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
								.findWaysToCheckpoints();
					} catch (Exception e) {
						// Nichts passiert
					}
				} else {
					removeWayPointList();
				}
				
				/*
				 * in Cookies speichern
				 */
				Cookies.setCookie("waypoints", ((Boolean)event.getValue()).toString());
			}
		});

		/*
		 * CnangedHandler für die Combobox der MapGeneratorEditingPage Wenn ComboboxItem gewechselt wurde, soll das ein neues Feld
		 * erzeugt werden
		 */
		this.page.getFieldTypeBox().addChangedHandler(new ChangedHandler() {
			public void createNewFields(String fieldTypeName) {

				/*
				 * Karte wurde geändert -> deshalb markieren
				 */
				MapGeneratorPresenter.this.changed = true;

				/*
				 * die richtige Area auf der dem EditingWindow anzeigen
				 */
				MapGeneratorPresenter.this.page.getEditingWindow().getMainSectionArea().getMembers()[MapGeneratorPresenter.this.showingWindowContentID]
						.hide();
				MapGeneratorPresenter.this.showingWindowContentID = getFieldTypeIDByName(fieldTypeName);
				MapGeneratorPresenter.this.page.getEditingWindow().getMainSectionArea().getMembers()[MapGeneratorPresenter.this.showingWindowContentID]
						.show();

				/*
				 * alte Bilder entfernen
				 */
				for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
						MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
					for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
							MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
						for (final Image img : MapGeneratorPresenter.this.fieldsImageList[i][j]) {
							MapGeneratorPresenter.this.page.getPlayingboardArea().remove(img);
						}
						MapGeneratorPresenter.this.fieldsImageList[i][j] = new ArrayList<Image>();
					}
				}
				
				/*
				 * Wallinfo von den selektierten Feldern holen
				 */
				final WallInfo wallInfo = new WallInfo(testWallInfos());

				/*
				 * LaserCannonInfos von den selectierten Feldern holen
				 */
				final LaserCannonInfo laserCannonInfo = new LaserCannonInfo(testLaserCannonInfos());
				
				/*
				 * neues Feld erzeugen
				 */
				if (fieldTypeName.equals("BasicField")) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new BasicField(new WallInfo(wallInfo), 
									new LaserCannonInfo(laserCannonInfo));
						}
					}
					
				} else if (fieldTypeName.equals("StartField")) {
					final int startNumber = getNextStartFieldNumber();
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new StartField(new WallInfo(wallInfo), startNumber);
							((StartField) MapGeneratorPresenter.this.fields[i][j]).setRespawnDirection(
									fieldPropertySaver.getLastStartFieldRestartDirection());
						}
					}

				} else if (fieldTypeName.equals("CheckpointField")) {
					final int checkpointNumber = getNextCheckpointFieldNumber();
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new CheckpointField(new WallInfo(wallInfo), checkpointNumber);
						}
					}
					
				} else if (fieldTypeName.equals("GearField")) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new GearField(new WallInfo(wallInfo), new LaserCannonInfo(laserCannonInfo), 
									fieldPropertySaver.isLastGearFieldClockwise());
						}
					}
					
				} else if (fieldTypeName.equals("HoleField")) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new HoleField();
						}
					}
					
				} else if (fieldTypeName.equals("PusherField")) {
					if(laserCannonInfo.getCannons(fieldPropertySaver.getLastPusherFieldDirection()) > 0) {
						laserCannonInfo.setCannons(fieldPropertySaver.getLastPusherFieldDirection(), 0);
					}
					
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new PusherField(new WallInfo(wallInfo), new LaserCannonInfo(laserCannonInfo),
									fieldPropertySaver.getLastPusherFieldDirection(), 
									fieldPropertySaver.isLastPusherFieldActivity());
						}
					}

				} else if (fieldTypeName.equals("CompactorField")) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new CompactorField(new WallInfo(wallInfo), new LaserCannonInfo(laserCannonInfo),
									fieldPropertySaver.getLastCompactorFieldDirection(),
									fieldPropertySaver.isLastCompactorFieldActivity());
						}
					}
				} else if (fieldTypeName.equals("RepairField")) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new RepairField(new WallInfo(wallInfo), fieldPropertySaver.getLastRepairFieldValue());
						}
					}

				} else if (fieldTypeName.equals("ConveyorBeltField")) {
					try {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								MapGeneratorPresenter.this.fields[i][j] = new ConveyorBeltField(new WallInfo(wallInfo),
										new LaserCannonInfo(laserCannonInfo), fieldPropertySaver.getLastConveyorbeltFieldOutDirection(),
										new HashSet<Direction>(fieldPropertySaver.getLastConveyorbeltFieldInDirections()),
										fieldPropertySaver.getLastConveyorbeltFieldRange());
							}
						}

					} catch (ConveyorBeltFieldException ex) {
						SC.say(Page.props.mapGeneratorPage_title(), ex.getMessage());
					}
				}

				/*
				 * Buttons für Feldeigenschaften richtig setzten
				 */
				updateElements();

				/*
				 * Felder zeichnen
				 */
				drawSelectedImages();

				if (MapGeneratorPresenter.this.page.getEditingWindow().getCheckBox_showWays().getValueAsBoolean()) {
					try {
						new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
								.findWaysToCheckpoints();
					} catch (CheckpointFieldNotReachableException ex) {
						/*
						 * nix passiert
						 */
					}
				}

				/*
				 * Auswahlrahmen wieder aufs oberste Bild setzen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().bringToFront(MapGeneratorPresenter.this.rectangleChoose);
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose());
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.rectangleMove);
			}

			@Override
			public void onChanged(ChangedEvent event) {
				createNewFields((String) event.getValue());
			}
		});

		/*
		 * ButtonHandler für Laser
		 */
		final com.smartgwt.client.widgets.events.ClickHandler laserRadioToggleButtonHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
			/**
			 * 
			 * @param str
			 * @param i
			 *            X-Koordinate
			 * @param j
			 *            Y-Koordinate
			 */
			private void addWall(String str, int i, int j) {
				final Image img = new Image(j * 50, i * 50, 50, 50, str);
				MapGeneratorPresenter.this.page.getPlayingboardArea().add(img);
				MapGeneratorPresenter.this.fieldsImageList[i][j].add(img);
			}

			@Override
			public void onClick(ClickEvent event) {
				final RadioToggleButton source = (RadioToggleButton) event.getSource();
				final Direction direction = Direction.valueOf(source.getName().substring(0, source.getName().length() - 1));
				final int numberOfCannons = Integer.valueOf("" + source.getName().charAt(source.getName().length() - 1));
				setLaser(direction, numberOfCannons);
			}

			public void setLaser(Direction direction, int numberOfCannons) {
				MapGeneratorPresenter.this.changed = true;

				// removen
				if (numberOfCannons == 0) {
					try {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								MapGeneratorPresenter.this.page.getPlayingboardArea().remove(
										removeImage(((LaserCannonField) MapGeneratorPresenter.this.fields[i][j])
												.removeLaserCannons(direction), i, j));
							}
						}
					} catch (Exception ex) {
						/*
						 * Verschlucken
						 */
					}

					// adden
				} else {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {

							String[] result = ((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).addLaserCannons(
									direction, numberOfCannons);

							// Wenn Laser vorher da war
							if (!result[1].equals("")) {
								MapGeneratorPresenter.this.page.getPlayingboardArea().remove(removeImage(result[1], i, j));
							}

							final Image img = new Image(j * 50, i * 50, 50, 50, result[0]);
							MapGeneratorPresenter.this.page.getPlayingboardArea().add(img);
							MapGeneratorPresenter.this.fieldsImageList[i][j].add(img);

							final WindowContentStack windowContentStack = (WindowContentStack) MapGeneratorPresenter.this.page
									.getEditingWindow().getMainSectionArea().getMembers()[MapGeneratorPresenter.this.showingWindowContentID];

							// Wand hinzufügen, falls nötig
							switch (direction) {
							case NORTH:
								if (!((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().isWallNorth()) {
									addWall(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.NORTH), i, j);
									windowContentStack.getWallNorth().setSelected(true);
								}
								break;
							case EAST:
								if (!((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().isWallEast()) {
									addWall(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.EAST), i, j);
									windowContentStack.getWallEast().setSelected(true);
								}
								break;
							case SOUTH:
								if (!((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().isWallSouth()) {
									addWall(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.SOUTH), i, j);
									windowContentStack.getWallSouth().setSelected(true);
								}
								break;
							case WEST:
								if (!((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().isWallWest()) {
									addWall(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.WEST), i, j);
									windowContentStack.getWallWest().setSelected(true);
								}
								break;
							}
						}
					}
				}

				/*
				 * Waypoints
				 */
				if (MapGeneratorPresenter.this.page.getEditingWindow().getCheckBox_showWays().getValueAsBoolean()) {
					try {
						new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
								.findWaysToCheckpoints();
					} catch (CheckpointFieldNotReachableException ex) {
						/*
						 * nix passiert
						 */
					}
				}

				/*
				 * Auswahlrahmen wieder aufs oberste Bild setzen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().bringToFront(MapGeneratorPresenter.this.rectangleChoose);
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose());
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.rectangleMove);
			}
		};

		/*
		 * LaserButtons hinzufügen
		 */
		ArrayList<WindowContentStack> stacks = new ArrayList<WindowContentStack>();
		stacks.add(this.page.getEditingWindow().getMainAreaBasic());
		stacks.add(this.page.getEditingWindow().getMainAreaGear());
		stacks.add(this.page.getEditingWindow().getMainAreaPusher());
		stacks.add(this.page.getEditingWindow().getMainAreaCompactor());
		stacks.add(this.page.getEditingWindow().getMainConveyorBelt());

		for (WindowContentStack wcs : stacks) {
			wcs.getLaserNorth0().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserNorth1().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserNorth2().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserNorth3().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserEast0().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserEast1().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserEast2().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserEast3().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserSouth0().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserSouth1().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserSouth2().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserSouth3().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserWest0().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserWest1().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserWest2().addClickHandler(laserRadioToggleButtonHandler);
			wcs.getLaserWest3().addClickHandler(laserRadioToggleButtonHandler);
		}

		/*
		 * ClickHandler für die WallToggleButtons
		 */
		final ClickHandler wallToggleButtonHandler = new ClickHandler() {
			/**
			 * Prozedur um Image zu adden
			 */
			private void addImage(String str, int i, int j) {
				Image img = new Image(j * 50, i * 50, 50, 50, str);
				MapGeneratorPresenter.this.page.getPlayingboardArea().add(img);
				MapGeneratorPresenter.this.fieldsImageList[i][j].add(img);
			}

			@Override
			public void onClick(ClickEvent event) {
				ToggleButton btn = (ToggleButton) event.getSource();
				setWalls(btn.getName(), btn.isSelected());
			}

			public void setWalls(String name, boolean isSelected) {
				MapGeneratorPresenter.this.changed = true;

				if (name.equals("N")) {
					if (isSelected) {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								addImage(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.NORTH), i, j);
							}
						}
					} else {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								MapGeneratorPresenter.this.page
										.getPlayingboardArea()
										.remove(removeImage(
												((WallField) MapGeneratorPresenter.this.fields[i][j]).removeWall(Direction.NORTH),
												i, j));
								// auch Laser entfernen
								if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof LaserCannonField
										&& ((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.getCannonsNorth() > 0) {
									MapGeneratorPresenter.this.page.getPlayingboardArea().remove(
											removeImage(((LaserCannonField) MapGeneratorPresenter.this.fields[i][j])
													.removeLaserCannons(Direction.NORTH), i, j));
								}
							}
						}
						((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaBasic())
								.getLaserNorth0().setSelected(true);
					}
				} else if (name.equals("O")) {
					if (isSelected) {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								addImage(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.EAST), i, j);
							}
						}
					} else {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								MapGeneratorPresenter.this.page.getPlayingboardArea().remove(
										removeImage(
												((WallField) MapGeneratorPresenter.this.fields[i][j]).removeWall(Direction.EAST),
												i, j));
								// auch Laser entfernen
								if (MapGeneratorPresenter.this.fields[i][j] instanceof LaserCannonField
										&& ((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.getCannonsEast() > 0) {
									MapGeneratorPresenter.this.page.getPlayingboardArea().remove(
											removeImage(((LaserCannonField) MapGeneratorPresenter.this.fields[i][j])
													.removeLaserCannons(Direction.EAST), i, j));
								}
							}
						}
						((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaBasic())
								.getLaserEast0().setSelected(true);
					}
				} else if (name.equals("S")) {
					if (isSelected) {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								addImage(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.SOUTH), i, j);
							}
						}
					} else {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								MapGeneratorPresenter.this.page
										.getPlayingboardArea()
										.remove(removeImage(
												((WallField) MapGeneratorPresenter.this.fields[i][j]).removeWall(Direction.SOUTH),
												i, j));
								// auch Laser entfernen
								if (MapGeneratorPresenter.this.fields[i][j] instanceof LaserCannonField
										&& ((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.getCannonsSouth() > 0) {
									MapGeneratorPresenter.this.page.getPlayingboardArea().remove(
											removeImage(((LaserCannonField) MapGeneratorPresenter.this.fields[i][j])
													.removeLaserCannons(Direction.SOUTH), i, j));
								}
							}
						}
						((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaBasic())
								.getLaserSouth0().setSelected(true);
					}
				} else if (name.equals("W")) {
					if (isSelected) {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								addImage(((WallField) MapGeneratorPresenter.this.fields[i][j]).addWall(Direction.WEST), i, j);
							}
						}
					} else {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
									.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
								MapGeneratorPresenter.this.page.getPlayingboardArea().remove(
										removeImage(
												((WallField) MapGeneratorPresenter.this.fields[i][j]).removeWall(Direction.WEST),
												i, j));
								// auch Laser entfernen
								if (MapGeneratorPresenter.this.fields[i][j] instanceof LaserCannonField
										&& ((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.getCannonsWest() > 0) {
									MapGeneratorPresenter.this.page.getPlayingboardArea().remove(
											removeImage(((LaserCannonField) MapGeneratorPresenter.this.fields[i][j])
													.removeLaserCannons(Direction.WEST), i, j));
								}
							}
						}
						((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaBasic())
								.getLaserWest0().setSelected(true);
					}
				}

				/*
				 * Waypoints
				 */
				if (MapGeneratorPresenter.this.page.getEditingWindow().getCheckBox_showWays().getValueAsBoolean()) {
					try {
						new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
								.findWaysToCheckpoints();
					} catch (CheckpointFieldNotReachableException ex) {
						/*
						 * nix passiert
						 */
					}
					;
				}

				/*
				 * Auswahlrahmen wieder aufs oberste Bild setzen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().bringToFront(MapGeneratorPresenter.this.rectangleChoose);
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose());
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.rectangleMove);
			}
		};

		/*
		 * Die Wall-ToggleButtons werden geaddet
		 */
		stacks.add(this.page.getEditingWindow().getMainAreaStart());
		stacks.add(this.page.getEditingWindow().getMainAreaCheckpoint());
		stacks.add(this.page.getEditingWindow().getMainAreaRepair());

		for (WindowContentStack wcs : stacks) {
			wcs.getWallNorth().addClickHandler(wallToggleButtonHandler);
			wcs.getWallEast().addClickHandler(wallToggleButtonHandler);
			wcs.getWallSouth().addClickHandler(wallToggleButtonHandler);
			wcs.getWallWest().addClickHandler(wallToggleButtonHandler);
		}

		/*
		 * Handler für Änderungen der speziellen Eigenschaften eines Spielfeldes
		 */
		com.smartgwt.client.widgets.events.ClickHandler propertiesRadioToggleButtonHandler = new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setFieldProperties(((ToggleButton) event.getSource()).getName(), ((ImgButton) event.getSource()).getRadioGroup(),
						event);
			}

			public void setFieldProperties(String btnName, String radioGroup, ClickEvent event) {
				MapGeneratorPresenter.this.changed = true;
				/*
				 * Bilder entfernen
				 */
				for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
						MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
					for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
							MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
						for (Image img : MapGeneratorPresenter.this.fieldsImageList[i][j]) {
							MapGeneratorPresenter.this.page.getPlayingboardArea().remove(img);
						}
						MapGeneratorPresenter.this.fieldsImageList[i][j] = new ArrayList<Image>();
					}
				}

				/*
				 * Instanztest
				 */
				if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof StartField) {
					
					if (!radioGroup.equals("RestartGroup")) {
						final Direction respawnDirection = ((StartField) MapGeneratorPresenter.this.fields[iStart][jStart]).getRespawnDirection();
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
									MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
	
								MapGeneratorPresenter.this.fields[i][j] = new StartField(
										((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(),
										Integer.valueOf(btnName));
								((StartField) MapGeneratorPresenter.this.fields[i][j]).setRespawnDirection(respawnDirection);
							}
						}
						
					} else {
						for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
								MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
							for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
									MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {

								((StartField) MapGeneratorPresenter.this.fields[i][j]).setRespawnDirection(Direction
										.valueOf(btnName));

							}
						}
						
						/*
						 * Feldeigenschaften merken
						 */
						fieldPropertySaver.setLastStartFieldRestartDirection(Direction.valueOf(btnName));
					}
					
				} else if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof RepairField) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new RepairField(
									((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(), Integer.valueOf(btnName));
						}
					}
					
					fieldPropertySaver.setLastRepairFieldValue(Integer.valueOf(btnName));
					
				} else if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof CheckpointField) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new CheckpointField(
									((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(), Integer.valueOf(btnName));
						}
					}
				} else if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof GearField) {
					for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math.max(
							MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
						for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math.max(
								MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
							MapGeneratorPresenter.this.fields[i][j] = new GearField(
									((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(),
									((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo(),
									btnName.equals("R"));
						}
					}
					
					fieldPropertySaver.setLastGearFieldClockwise(btnName.equals("R"));
					
				} else if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof LaserCannonField) {
					Direction arrDirection = null;
					switch (btnName.charAt(0)) {
					case 'N':
						arrDirection = Direction.NORTH;
						break;
					case 'O':
						arrDirection = Direction.EAST;
						break;
					case 'S':
						arrDirection = Direction.SOUTH;
						break;
					case 'W':
						arrDirection = Direction.WEST;
						break;
					}

					if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof PusherField) {
						if (radioGroup.equals("PusherDirection")) {
							// Den enstprechenden Knopf für die Wall und Laser
							// deaktivieren und
							switch (((PusherField) MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart])
									.getDirection()) {
							case NORTH:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallNorth().setDisabled(false);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().setWallNorth(false);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth0().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth1().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth2().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth3().setDisabled(false);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth0().setSelected(true);
								break;
							case EAST:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallEast().setDisabled(false);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().setWallEast(false);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast0().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast1().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast2().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast3().setDisabled(false);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast0().setSelected(true);
								break;
							case SOUTH:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallSouth().setDisabled(false);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().setWallSouth(false);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth0().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth1().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth2().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth3().setDisabled(false);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth0().setSelected(true);
								break;
							case WEST:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallWest().setDisabled(false);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo().setWallWest(false);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest0().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest1().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest2().setDisabled(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest3().setDisabled(false);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest0().setSelected(true);
								break;
							}

							// Falls Laser vorhanden -> entfernen
							// Den enstrechenden Knopf für die Wall und Laser
							// aktivieren
							switch (arrDirection) {
							case NORTH:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallNorth().setSelected(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallNorth().setDisabled(true);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.setCannonsNorth(0);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth0().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth1().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth2().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth3().setDisabled(true);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserNorth0().setSelected(true);
								break;
							case EAST:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallEast().setSelected(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallEast().setDisabled(true);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.setCannonsEast(0);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast0().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast1().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast2().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast3().setDisabled(true);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserEast0().setSelected(true);
								break;
							case SOUTH:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallSouth().setSelected(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallSouth().setDisabled(true);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.setCannonsSouth(0);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth0().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth1().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth2().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth3().setDisabled(true);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserSouth0().setSelected(true);
								break;
							case WEST:
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallWest().setSelected(false);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getWallWest().setDisabled(true);
								for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
										.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
									for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
											.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
										((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo()
												.setCannonsWest(0);
									}
								}

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest0().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest1().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest2().setDisabled(true);
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest3().setDisabled(true);

								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainAreaPusher())
										.getLaserWest0().setSelected(true);
								break;
							}

							for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
									.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
								for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
										.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
									MapGeneratorPresenter.this.fields[i][j] = new PusherField(
											((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(),
											((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo(),
											arrDirection,
											((PusherField) MapGeneratorPresenter.this.fields[i][j]).isActiveInUneven());
								}
							}

						} else {
							for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
									.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
								for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
										.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
									MapGeneratorPresenter.this.fields[i][j] = new PusherField(
											((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(),
											((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo(),
											((PusherField) MapGeneratorPresenter.this.fields[i][j]).getDirection(),
											btnName.equals("J"));
								}
							}
						}

						/*
						 *  Eigenschaften merken
						 */
						fieldPropertySaver.setLastPusherFieldDirection(((PusherField) MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart])
								.getDirection());
						fieldPropertySaver.setLastPusherFieldActivity(((PusherField) MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart])
								.isActiveInUneven());

					} else if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof CompactorField) {
						if (radioGroup.equals("CompactorDirection")) {
							for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
									.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
								for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
										.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
									MapGeneratorPresenter.this.fields[i][j] = new CompactorField(
											((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(),
											((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo(),
											arrDirection,
											((CompactorField) MapGeneratorPresenter.this.fields[i][j]).isActiveInFirstAndFifth());
								}
							}
						} else {
							for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
									.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
								for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
										.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
									MapGeneratorPresenter.this.fields[i][j] = new CompactorField(
											((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(),
											((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo(),
											((CompactorField) MapGeneratorPresenter.this.fields[i][j]).getDirection(),
											btnName.equals("J"));
								}
							}
						}
						
						/*
						 *  Eigenschaften merken
						 */
						fieldPropertySaver.setLastCompactorFieldDirection(((CompactorField) MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart])
								.getDirection());
						fieldPropertySaver.setLastCompactorFieldActivity(((CompactorField) MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart])
								.isActiveInFirstAndFifth());

					} else if (MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart] instanceof ConveyorBeltField) {
						Direction outDirection = null;
						if (btnName.equals("outN")) {
							outDirection = Direction.NORTH;

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setDisabled(true);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setDisabled(false);

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setSelected(true);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setSelected(false);

						} else if (btnName.equals("outE")) {
							outDirection = Direction.EAST;

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setDisabled(true);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setDisabled(false);

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setSelected(true);
						} else if (btnName.equals("outS")) {
							outDirection = Direction.SOUTH;

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setDisabled(true);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setDisabled(false);

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setSelected(true);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setSelected(false);
						} else if (btnName.equals("outW")) {
							outDirection = Direction.WEST;

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setDisabled(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setDisabled(true);

							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInNorth().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInEast().setSelected(true);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInSouth().setSelected(false);
							((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
									.getConveyorBeltInWest().setSelected(false);
						} else {
							outDirection = ((ConveyorBeltField) MapGeneratorPresenter.this.fields[MapGeneratorPresenter.this.iStart][MapGeneratorPresenter.this.jStart])
									.getArrowOutDirection();
						}

						final Set<Direction> inDirections = new HashSet<Direction>();

						if (((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
								.getConveyorBeltInNorth().isSelected()) {
							inDirections.add(Direction.NORTH);
						}
						if (((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
								.getConveyorBeltInEast().isSelected()) {
							inDirections.add(Direction.EAST);
						}
						if (((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
								.getConveyorBeltInSouth().isSelected()) {
							inDirections.add(Direction.SOUTH);
						}
						if (((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
								.getConveyorBeltInWest().isSelected()) {
							inDirections.add(Direction.WEST);
						}

						if (inDirections.isEmpty()) {
							if (btnName.equals("inN")) {
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
										.getConveyorBeltInNorth().setSelected(true);
								inDirections.add(Direction.NORTH);
							} else if (btnName.equals("inE")) {
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
										.getConveyorBeltInEast().setSelected(true);
								inDirections.add(Direction.EAST);
							} else if (btnName.equals("inS")) {
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
										.getConveyorBeltInSouth().setSelected(true);
								inDirections.add(Direction.SOUTH);
							} else {
								((WindowContentStack) MapGeneratorPresenter.this.page.getEditingWindow().getMainConveyorBelt())
										.getConveyorBeltInWest().setSelected(true);
								inDirections.add(Direction.WEST);

							}
						}

						final int range;
						switch (btnName.charAt(0)) {
						case '1':
							range = 1;
							break;
						case '2':
							range = 2;
							break;
						default:
							range = ((ConveyorBeltField)fields[iStart][jStart]).getRange();
							break;
						}
						try {
							for (int i = Math.min(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i <= Math
									.max(MapGeneratorPresenter.this.iStart, MapGeneratorPresenter.this.iEnd); i++) {
								for (int j = Math.min(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j <= Math
										.max(MapGeneratorPresenter.this.jStart, MapGeneratorPresenter.this.jEnd); j++) {
									MapGeneratorPresenter.this.fields[i][j] = new ConveyorBeltField(
											((WallField) MapGeneratorPresenter.this.fields[i][j]).getWallInfo(),
											((LaserCannonField) MapGeneratorPresenter.this.fields[i][j]).getLaserCannonInfo(),
											outDirection, new HashSet<Direction>(inDirections), range);
								}
							}
							
							/*
							 * Eigenschaften merken
							 */
							fieldPropertySaver.setLastConveyorbeltFieldOutDirection(outDirection);
							fieldPropertySaver.setLastConveyorbeltFieldInDirections(inDirections);
							fieldPropertySaver.setLastConveyorbeltFieldRange(range);
						} catch (ConveyorBeltFieldException ex) {
							SC.warn(ex.getMessage());
						}
					}
				}

				/*
				 * Images adden
				 */
				drawSelectedImages();

				
				/*
				 * Waypoints
				 */
				if (MapGeneratorPresenter.this.page.getEditingWindow().getCheckBox_showWays().getValueAsBoolean()) {
					try {
						new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
								.findWaysToCheckpoints();
					} catch (CheckpointFieldNotReachableException ex) {
						/*
						 * nix passiert
						 */
					}
					;
				}

				/*
				 * Auswahlrahmen wieder aufs oberste Bild setzen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().bringToFront(MapGeneratorPresenter.this.rectangleChoose);
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.page.getMinimap().getRectangleChoose());
				MapGeneratorPresenter.this.page.getMinimap().bringToFront(MapGeneratorPresenter.this.rectangleMove);
			}
		};

		/*
		 * StartRadioToggleButtons adden
		 */
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart1().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart2().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart3().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart4().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart5().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart6().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart7().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart8().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart9().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart10().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart11().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getStart12().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getRestartNorth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getRestartEast().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getRestartSouth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaStart()).getRestartWest().addClickHandler(
				propertiesRadioToggleButtonHandler);

		/*
		 * Die RepairButtons adden
		 */
		((WindowContentStack) this.page.getEditingWindow().getMainAreaRepair()).getRepairValue1().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaRepair()).getRepairValue2().addClickHandler(
				propertiesRadioToggleButtonHandler);

		/*
		 * die CheckBoxButtons adden
		 */
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck1().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck2().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck3().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck4().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck5().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck6().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck7().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCheckpoint()).getCheck8().addClickHandler(
				propertiesRadioToggleButtonHandler);

		/*
		 * GearFelder adden
		 */
		((WindowContentStack) this.page.getEditingWindow().getMainAreaGear()).getGearRight().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaGear()).getGearLeft().addClickHandler(
				propertiesRadioToggleButtonHandler);
		/*
		 * Pusher und Compactor: richtungen adden
		 */
		((WindowContentStack) this.page.getEditingWindow().getMainAreaPusher()).getPusherNorth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaPusher()).getPusherEast().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaPusher()).getPusherSouth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaPusher()).getPusherWest().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCompactor()).getCompactorNorth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCompactor()).getCompactorEast().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCompactor()).getCompactorSouth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCompactor()).getCompactorWest().addClickHandler(
				propertiesRadioToggleButtonHandler);

		/*
		 * Pusher und Compactor: Always adden
		 */
		((WindowContentStack) this.page.getEditingWindow().getMainAreaPusher()).getPusherAlways().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaPusher()).getPusherUneven().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCompactor()).getCompactorAlways().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainAreaCompactor()).getCompactorFirstLast().addClickHandler(
				propertiesRadioToggleButtonHandler);

		/*
		 * conveyorbelts: richtungen hinzufügen
		 */
		// Ausgangsrichtung
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltNorth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltEast().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltSouth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltWest().addClickHandler(
				propertiesRadioToggleButtonHandler);

		// Eingangrichtungen
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltInNorth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltInEast().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltInSouth().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltInWest().addClickHandler(
				propertiesRadioToggleButtonHandler);

		/*
		 * conveyorbelts: reichweite hinzufügen
		 */
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltRange1().addClickHandler(
				propertiesRadioToggleButtonHandler);
		((WindowContentStack) this.page.getEditingWindow().getMainConveyorBelt()).getConveyorBeltRange2().addClickHandler(
				propertiesRadioToggleButtonHandler);

		return true;
	}

	/**
	 * Setzt CheckpointField
	 */
	private void buildCheckpointField() {
		switch (((CheckpointField) this.fields[this.iStart][this.jStart]).getNumberOfCheckpoint()) {
		case 1:
			this.windowContentStack.getCheck1().setSelected(true);
			break;
		case 2:
			this.windowContentStack.getCheck2().setSelected(true);
			break;
		case 3:
			this.windowContentStack.getCheck3().setSelected(true);
			break;
		case 4:
			this.windowContentStack.getCheck4().setSelected(true);
			break;
		}
	}

	/**
	 * Setzt CompactorField
	 */
	private void buildCompactorField() {
		switch (((CompactorField) this.fields[this.iStart][this.jStart]).getDirection()) {
		case NORTH:
			this.windowContentStack.getCompactorNorth().setSelected(true);
			break;
		case EAST:
			this.windowContentStack.getCompactorEast().setSelected(true);
			break;
		case SOUTH:
			this.windowContentStack.getCompactorSouth().setSelected(true);
			break;
		case WEST:
			this.windowContentStack.getCompactorWest().setSelected(true);
			break;
		}

		if (((CompactorField) this.fields[this.iStart][this.jStart]).isActiveInFirstAndFifth()) {
			this.windowContentStack.getCompactorFirstLast().setSelected(true);
		} else {
			this.windowContentStack.getCompactorAlways().setSelected(true);
		}
	}

	/**
	 * Setzt RotateConveyorBelt
	 */
	private void buildConveyorBelt() {

		final ConveyorBeltField conveyorBeltField = (ConveyorBeltField) this.fields[this.iStart][this.jStart];

		/*
		 * Ausgangsrichtung
		 */
		switch (conveyorBeltField.getArrowOutDirection()) {
		case NORTH:
			this.windowContentStack.getConveyorBeltNorth().setSelected(true);

			this.windowContentStack.getConveyorBeltInNorth().setDisabled(true);
			this.windowContentStack.getConveyorBeltInEast().setDisabled(false);
			this.windowContentStack.getConveyorBeltInSouth().setDisabled(false);
			this.windowContentStack.getConveyorBeltInWest().setDisabled(false);
			break;
		case EAST:
			this.windowContentStack.getConveyorBeltEast().setSelected(true);

			this.windowContentStack.getConveyorBeltInNorth().setDisabled(false);
			this.windowContentStack.getConveyorBeltInEast().setDisabled(true);
			this.windowContentStack.getConveyorBeltInSouth().setDisabled(false);
			this.windowContentStack.getConveyorBeltInWest().setDisabled(false);
			break;
		case SOUTH:
			this.windowContentStack.getConveyorBeltSouth().setSelected(true);

			this.windowContentStack.getConveyorBeltInNorth().setDisabled(false);
			this.windowContentStack.getConveyorBeltInEast().setDisabled(false);
			this.windowContentStack.getConveyorBeltInSouth().setDisabled(true);
			this.windowContentStack.getConveyorBeltInWest().setDisabled(false);
			break;
		case WEST:
			this.windowContentStack.getConveyorBeltWest().setSelected(true);

			this.windowContentStack.getConveyorBeltInNorth().setDisabled(false);
			this.windowContentStack.getConveyorBeltInEast().setDisabled(false);
			this.windowContentStack.getConveyorBeltInSouth().setDisabled(false);
			this.windowContentStack.getConveyorBeltInWest().setDisabled(true);
			break;
		}

		/*
		 * Eingangsrichtung
		 */
		this.windowContentStack.getConveyorBeltInNorth().setSelected(
				conveyorBeltField.getArrowInDirections().contains(Direction.NORTH));
		this.windowContentStack.getConveyorBeltInEast().setSelected(
				conveyorBeltField.getArrowInDirections().contains(Direction.EAST));
		this.windowContentStack.getConveyorBeltInSouth().setSelected(
				conveyorBeltField.getArrowInDirections().contains(Direction.SOUTH));
		this.windowContentStack.getConveyorBeltInWest().setSelected(
				conveyorBeltField.getArrowInDirections().contains(Direction.WEST));

		/*
		 * Reichweite
		 */
		this.windowContentStack.getConveyorBeltRange1().setSelected(conveyorBeltField.getRange() == 1);
		this.windowContentStack.getConveyorBeltRange2().setSelected(conveyorBeltField.getRange() == 2);

	}

	/**
	 * Setzt GearField
	 */
	private void buildGearField() {
		if (((GearField) this.fields[this.iStart][this.jStart]).isClockwiseDirection()) {
			this.windowContentStack.getGearRight().setSelected(true);
		} else {
			this.windowContentStack.getGearLeft().setSelected(true);
		}
	}

	/**
	 * Setzt Laser
	 */
	private void buildLasers() {
		switch (((LaserCannonField) this.fields[this.iStart][this.jStart]).getLaserCannonInfo().getCannonsNorth()) {
		case 0:
			this.windowContentStack.getLaserNorth0().setSelected(true);
			break;
		case 1:
			this.windowContentStack.getLaserNorth1().setSelected(true);
			break;
		case 2:
			this.windowContentStack.getLaserNorth2().setSelected(true);
			break;
		case 3:
			this.windowContentStack.getLaserNorth3().setSelected(true);
			break;
		}
		switch (((LaserCannonField) this.fields[this.iStart][this.jStart]).getLaserCannonInfo().getCannonsEast()) {
		case 0:
			this.windowContentStack.getLaserEast0().setSelected(true);
			break;
		case 1:
			this.windowContentStack.getLaserEast1().setSelected(true);
			break;
		case 2:
			this.windowContentStack.getLaserEast2().setSelected(true);
			break;
		case 3:
			this.windowContentStack.getLaserEast3().setSelected(true);
			break;
		}
		switch (((LaserCannonField) this.fields[this.iStart][this.jStart]).getLaserCannonInfo().getCannonsSouth()) {
		case 0:
			this.windowContentStack.getLaserSouth0().setSelected(true);
			break;
		case 1:
			this.windowContentStack.getLaserSouth1().setSelected(true);
			break;
		case 2:
			this.windowContentStack.getLaserSouth2().setSelected(true);
			break;
		case 3:
			this.windowContentStack.getLaserSouth3().setSelected(true);
			break;
		}
		switch (((LaserCannonField) this.fields[this.iStart][this.jStart]).getLaserCannonInfo().getCannonsWest()) {
		case 0:
			this.windowContentStack.getLaserWest0().setSelected(true);
			break;
		case 1:
			this.windowContentStack.getLaserWest1().setSelected(true);
			break;
		case 2:
			this.windowContentStack.getLaserWest2().setSelected(true);
			break;
		case 3:
			this.windowContentStack.getLaserWest3().setSelected(true);
			break;
		}
	}

	/**
	 * Setzt PusherField
	 */
	private void buildPusherField() {
		switch (((PusherField) this.fields[this.iStart][this.jStart]).getDirection()) {
		case NORTH:
			this.windowContentStack.getPusherNorth().setSelected(true);
			this.windowContentStack.getWallNorth().setSelected(false);

			this.windowContentStack.getWallNorth().setDisabled(true);
			this.windowContentStack.getWallEast().setDisabled(false);
			this.windowContentStack.getWallSouth().setDisabled(false);
			this.windowContentStack.getWallWest().setDisabled(false);
			
			this.windowContentStack.getLaserNorth0().setDisabled(true);
			this.windowContentStack.getLaserNorth1().setDisabled(true);
			this.windowContentStack.getLaserNorth2().setDisabled(true);
			this.windowContentStack.getLaserNorth3().setDisabled(true);
			
			this.windowContentStack.getLaserEast0().setDisabled(false);
			this.windowContentStack.getLaserEast1().setDisabled(false);
			this.windowContentStack.getLaserEast2().setDisabled(false);
			this.windowContentStack.getLaserEast3().setDisabled(false);
			
			this.windowContentStack.getLaserSouth0().setDisabled(false);
			this.windowContentStack.getLaserSouth1().setDisabled(false);
			this.windowContentStack.getLaserSouth2().setDisabled(false);
			this.windowContentStack.getLaserSouth3().setDisabled(false);
			
			this.windowContentStack.getLaserWest0().setDisabled(false);
			this.windowContentStack.getLaserWest1().setDisabled(false);
			this.windowContentStack.getLaserWest2().setDisabled(false);
			this.windowContentStack.getLaserWest3().setDisabled(false);
			
			break;
		case EAST:
			this.windowContentStack.getPusherEast().setSelected(true);
			this.windowContentStack.getWallEast().setSelected(false);

			this.windowContentStack.getWallNorth().setDisabled(false);
			this.windowContentStack.getWallEast().setDisabled(true);
			this.windowContentStack.getWallSouth().setDisabled(false);
			this.windowContentStack.getWallWest().setDisabled(false);
			
			this.windowContentStack.getLaserNorth0().setDisabled(false);
			this.windowContentStack.getLaserNorth1().setDisabled(false);
			this.windowContentStack.getLaserNorth2().setDisabled(false);
			this.windowContentStack.getLaserNorth3().setDisabled(false);
			
			this.windowContentStack.getLaserEast0().setDisabled(true);
			this.windowContentStack.getLaserEast1().setDisabled(true);
			this.windowContentStack.getLaserEast2().setDisabled(true);
			this.windowContentStack.getLaserEast3().setDisabled(true);
			
			this.windowContentStack.getLaserSouth0().setDisabled(false);
			this.windowContentStack.getLaserSouth1().setDisabled(false);
			this.windowContentStack.getLaserSouth2().setDisabled(false);
			this.windowContentStack.getLaserSouth3().setDisabled(false);
			
			this.windowContentStack.getLaserWest0().setDisabled(false);
			this.windowContentStack.getLaserWest1().setDisabled(false);
			this.windowContentStack.getLaserWest2().setDisabled(false);
			this.windowContentStack.getLaserWest3().setDisabled(false);
			
			break;
		case SOUTH:
			this.windowContentStack.getPusherSouth().setSelected(true);
			this.windowContentStack.getWallSouth().setSelected(false);

			this.windowContentStack.getWallNorth().setDisabled(false);
			this.windowContentStack.getWallEast().setDisabled(false);
			this.windowContentStack.getWallSouth().setDisabled(true);
			this.windowContentStack.getWallWest().setDisabled(false);
			
			this.windowContentStack.getLaserNorth0().setDisabled(false);
			this.windowContentStack.getLaserNorth1().setDisabled(false);
			this.windowContentStack.getLaserNorth2().setDisabled(false);
			this.windowContentStack.getLaserNorth3().setDisabled(false);
			
			this.windowContentStack.getLaserEast0().setDisabled(false);
			this.windowContentStack.getLaserEast1().setDisabled(false);
			this.windowContentStack.getLaserEast2().setDisabled(false);
			this.windowContentStack.getLaserEast3().setDisabled(false);
			
			this.windowContentStack.getLaserSouth0().setDisabled(true);
			this.windowContentStack.getLaserSouth1().setDisabled(true);
			this.windowContentStack.getLaserSouth2().setDisabled(true);
			this.windowContentStack.getLaserSouth3().setDisabled(true);
			
			this.windowContentStack.getLaserWest0().setDisabled(false);
			this.windowContentStack.getLaserWest1().setDisabled(false);
			this.windowContentStack.getLaserWest2().setDisabled(false);
			this.windowContentStack.getLaserWest3().setDisabled(false);
			break;
		case WEST:
			this.windowContentStack.getPusherWest().setSelected(true);
			this.windowContentStack.getWallWest().setSelected(false);

			this.windowContentStack.getWallNorth().setDisabled(false);
			this.windowContentStack.getWallEast().setDisabled(false);
			this.windowContentStack.getWallSouth().setDisabled(false);
			this.windowContentStack.getWallWest().setDisabled(true);
			
			this.windowContentStack.getLaserNorth0().setDisabled(false);
			this.windowContentStack.getLaserNorth1().setDisabled(false);
			this.windowContentStack.getLaserNorth2().setDisabled(false);
			this.windowContentStack.getLaserNorth3().setDisabled(false);
			
			this.windowContentStack.getLaserEast0().setDisabled(false);
			this.windowContentStack.getLaserEast1().setDisabled(false);
			this.windowContentStack.getLaserEast2().setDisabled(false);
			this.windowContentStack.getLaserEast3().setDisabled(false);
			
			this.windowContentStack.getLaserSouth0().setDisabled(false);
			this.windowContentStack.getLaserSouth1().setDisabled(false);
			this.windowContentStack.getLaserSouth2().setDisabled(false);
			this.windowContentStack.getLaserSouth3().setDisabled(false);
			
			this.windowContentStack.getLaserWest0().setDisabled(true);
			this.windowContentStack.getLaserWest1().setDisabled(true);
			this.windowContentStack.getLaserWest2().setDisabled(true);
			this.windowContentStack.getLaserWest3().setDisabled(true);
			break;
		}

		if (((PusherField) this.fields[this.iStart][this.jStart]).isActiveInUneven()) {
			this.windowContentStack.getPusherUneven().setSelected(true);
		} else {
			this.windowContentStack.getPusherAlways().setSelected(true);
		}
	}

	/**
	 * Setzt RepairField
	 */
	private void buildRepairField() {
		if (((RepairField) this.fields[this.iStart][this.jStart]).getNumberOfWrench() == 1) {
			this.windowContentStack.getRepairValue1().setSelected(true);
		} else {
			this.windowContentStack.getRepairValue2().setSelected(true);
		}
	}

	/**
	 * Setzt StartField
	 */
	private void buildStartField() {
		switch (((StartField) this.fields[this.iStart][this.jStart]).getStartNumber()) {
		case 1:
			this.windowContentStack.getStart1().setSelected(true);
			break;
		case 2:
			this.windowContentStack.getStart2().setSelected(true);
			break;
		case 3:
			this.windowContentStack.getStart3().setSelected(true);
			break;
		case 4:
			this.windowContentStack.getStart4().setSelected(true);
			break;
		case 5:
			this.windowContentStack.getStart5().setSelected(true);
			break;
		case 6:
			this.windowContentStack.getStart6().setSelected(true);
			break;
		case 7:
			this.windowContentStack.getStart7().setSelected(true);
			break;
		case 8:
			this.windowContentStack.getStart8().setSelected(true);
			break;
		case 9:
			this.windowContentStack.getStart9().setSelected(true);
			break;
		case 10:
			this.windowContentStack.getStart10().setSelected(true);
			break;
		case 11:
			this.windowContentStack.getStart11().setSelected(true);
			break;
		case 12:
			this.windowContentStack.getStart12().setSelected(true);
			break;
		}

		switch (((StartField) this.fields[this.iStart][this.jStart]).getRespawnDirection()) {
		case NORTH:
			this.windowContentStack.getRestartNorth().setSelected(true);
			break;
		case EAST:
			this.windowContentStack.getRestartEast().setSelected(true);
			break;
		case SOUTH:
			this.windowContentStack.getRestartSouth().setSelected(true);
			break;
		case WEST:
			this.windowContentStack.getRestartWest().setSelected(true);
			break;
		}
	}
	
	/**
	 * liefert die nächste verfügbare Startfeldnummer
	 * @return
	 */
	private int getNextStartFieldNumber() {
	
		final Set<Integer> startNumberSet = new HashSet<Integer>();
		
		for(int i=0; i<this.fields.length; i++) {
			for(int j=0; j<this.fields[i].length; j++) {
				
				if(this.fields[i][j] instanceof StartField) {
					startNumberSet.add(((StartField)this.fields[i][j]).getStartNumber());
				}
			}
		}
		
		for(int startNumber = 1; startNumber<=12; startNumber++) {
			if(!startNumberSet.contains(startNumber)) {
				return startNumber;
			}
		}
		
		return 12;
	}
	
	/**
	 * liefert die nächste verfügbare Checkpointnummer
	 * @return
	 */
	private int getNextCheckpointFieldNumber() {
	
		final Set<Integer> checkpointNumberSet = new HashSet<Integer>();
		
		for(int i=0; i<this.fields.length; i++) {
			for(int j=0; j<this.fields[i].length; j++) {
				
				if(this.fields[i][j] instanceof CheckpointField) {
					checkpointNumberSet.add(((CheckpointField)this.fields[i][j]).getNumberOfCheckpoint());
				}
			}
		}
		
		for(int checkpointNumber = 1; checkpointNumber<=8; checkpointNumber++) {
			if(!checkpointNumberSet.contains(checkpointNumber)) {
				return checkpointNumber;
			}
		}
		
		return 8;
	}

	/**
	 * Erstellt die Wände
	 */
	private void buildWalls() {
		if (((WallField) this.fields[this.iStart][this.jStart]).getWallInfo() != null) {
			this.windowContentStack.getWallNorth().setSelected(
					((WallField) this.fields[this.iStart][this.jStart]).getWallInfo().isWallNorth());
			this.windowContentStack.getWallEast().setSelected(
					((WallField) this.fields[this.iStart][this.jStart]).getWallInfo().isWallEast());
			this.windowContentStack.getWallSouth().setSelected(
					((WallField) this.fields[this.iStart][this.jStart]).getWallInfo().isWallSouth());
			this.windowContentStack.getWallWest().setSelected(
					((WallField) this.fields[this.iStart][this.jStart]).getWallInfo().isWallWest());
		}
	}
	
	

	/**
	 * Erstellt ein neues Spielbrett mit Basisfeldern in den angegebenen angegebenen Dimensionen
	 * 
	 * @param width
	 *            die Breite des Spielbretts
	 * @param height
	 *            die Höhe des Spielbretts
	 * @return immer true
	 */
	@SuppressWarnings("unchecked")
	public boolean createNew(final int width, final int height) {
		/*
		 * Fortschrittfenster anzeigen
		 */
		final ProgressWindow progressWindow = new ProgressWindow(Page.props.mapGeneratorPage_progressWindow_create());

		/*
		 * Das Erstellen wird in einem Extra Timer geschehen
		 */
		new Timer() {
			private int i = 0;
			private int a = 0;
			private int percentPerStep;

			@Override
			public void run() {

				if (this.i == 0) {
					MapGeneratorPresenter.this.saveName = "";
					MapGeneratorPresenter.this.saveDescription = "";
					MapGeneratorPresenter.this.saveDifficulty = "";
					MapGeneratorPresenter.this.page.setDrawingArea(width * 50, height * 50);
					MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(false);
					MapGeneratorPresenter.this.page.getMinimap().setVisible(false);
					MapGeneratorPresenter.this.fields = new Field[height][width];
					MapGeneratorPresenter.this.fieldsImageList = new ArrayList[height][width];
					this.i = 1;
					this.percentPerStep = 100 / height;
					schedule(50);
					return;
				}

				if (this.i == 1) {
					while (this.a < height) {
						for (int j = 0; j < width; j++) {
							MapGeneratorPresenter.this.fields[this.a][j] = new BasicField(new WallInfo(), new LaserCannonInfo());
							MapGeneratorPresenter.this.fieldsImageList[this.a][j] = new ArrayList<Image>();
							for (DrawingInfo info : MapGeneratorPresenter.this.fields[this.a][j].getImagePathList()) {
								final Image img = MapGeneratorPresenter.this.page.getPlayingboardArea().add(info, j*50, this.a*50);
								MapGeneratorPresenter.this.fieldsImageList[this.a][j].add(img);
							}
						}

						this.a += 1;
						progressWindow.getProgressbar().setPercentDone(
								progressWindow.getProgressbar().getPercentDone() + this.percentPerStep);

						progressWindow.getLabel().setContents(
								Page.props.mapGeneratorPresenter_progressWindow_progress() + ": "
										+ progressWindow.getProgressbar().getPercentDone() + "%");
						schedule(50);
						return;
					}

					progressWindow.getProgressbar().setPercentDone(100);
					progressWindow.getLabel().setContents(Page.props.mapGeneratorPresenter_progressWindow_progress() + ": 100%");
					this.i = 2;
					schedule(100);
					return;
				}

				MapGeneratorPresenter.this.rectangleChoose.setWidth(50);
				MapGeneratorPresenter.this.rectangleChoose.setHeight(50);
				MapGeneratorPresenter.this.rectangleChoose.setX(0);
				MapGeneratorPresenter.this.rectangleChoose.setY(0);
				MapGeneratorPresenter.this.page.getPlayingboardArea().add(MapGeneratorPresenter.this.rectangleChoose);

				MapGeneratorPresenter.this.rectangleMove.setX(0);
				MapGeneratorPresenter.this.rectangleMove.setY(0);
				if (width > 14) {
					MapGeneratorPresenter.this.rectangleMove.setWidth(143);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setWidth(MapGeneratorPresenter.this.page.getMinimap().getWidth());
				}
				if (height > 9) {
					MapGeneratorPresenter.this.rectangleMove.setHeight(97);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setHeight(MapGeneratorPresenter.this.page.getMinimap().getHeight());
				}

				if (width <= 14 && height <= 9) {
					MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(0);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(1);
				}
				MapGeneratorPresenter.this.page.getMinimap().addRectangle(MapGeneratorPresenter.this.rectangleMove);

				MapGeneratorPresenter.this.iStart = 0;
				MapGeneratorPresenter.this.iEnd = 0;
				MapGeneratorPresenter.this.jStart = 0;
				MapGeneratorPresenter.this.jEnd = 0;

				updateElements();

				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseDownHandler(MapGeneratorPresenter.this.mouseDownHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseMoveHandler(MapGeneratorPresenter.this.mouseMoveHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseUpHandler(MapGeneratorPresenter.this.mouseUpHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseOutHandler(MapGeneratorPresenter.this.mouseOutHandler);
				
				MapGeneratorPresenter.this.page.getDrawingAreaArea().addScrolledHandler(scrollHandler);
				MapGeneratorPresenter.this.page.getDrawingAreaAreaInner().addMouseStillDownHandler(chooseScrollhandler);
				
				// Menue-Schalter einschalten
				MapGeneratorPresenter.this.page.getEditingWindow().getButtonMenue().setDisabled(false);

				// Menue ausblenden
				setMenueVisibility(false);

				MapGeneratorPresenter.this.page.getButtonSave().setDisabled(false);
				MapGeneratorPresenter.this.page.getButtonUnload().setDisabled(false);
				MapGeneratorPresenter.this.page.getButtonTransform().setDisabled(false);
				MapGeneratorPresenter.this.page.getFieldTypeBox().setDisabled(false);

				MapGeneratorPresenter.this.changed = false;

				/*
				 * ProgressWindow zerstören
				 */
				progressWindow.destroy();

				/*
				 * DrawingArea anzeigen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(true);
				MapGeneratorPresenter.this.page.getMinimap().setVisible(true);
			}

		}.schedule(400);

		return true;
	}

	/**
	 * Zeichnet die Wegpunkte auf das Spielbrett
	 * @param waypoints
	 */
	public void drawWayPointList(final List<List<Position>> waypoints) {

		final List<Circle> circleList = new ArrayList<Circle>();
		
		for (final List<Position> sublist : waypoints) {

			final List<VectorObject> lineList = new ArrayList<VectorObject>();

			for (int i = 0; i < sublist.size() - 1; i++) {

				final Line line = new Line(sublist.get(i).getJ() * 50 + 25, sublist.get(i).getI() * 50 + 25, sublist.get(i + 1)
						.getJ() * 50 + 25, sublist.get(i + 1).getI() * 50 + 25);
				line.setStrokeColor("blue");
				line.setStrokeOpacity(1);
				line.setStrokeWidth(1);
				this.page.getPlayingboardArea().add(line);
				lineList.add(line);
			}

			final Circle circle1 = new Circle(sublist.get(0).getJ() * 50 + 25, sublist.get(0).getI() * 50 + 25, 3);
			circle1.setStrokeWidth(2);
			circle1.setStrokeColor("blue");
			this.page.getPlayingboardArea().add(circle1);
			lineList.add(circle1);
			circleList.add(circle1);

			final Circle circle2 = new Circle(sublist.get(sublist.size() - 1).getJ() * 50 + 25, sublist.get(sublist.size() - 1)
					.getI() * 50 + 25, 3);
			circle2.setStrokeWidth(2);
			circle2.setStrokeColor("blue");
			this.page.getPlayingboardArea().add(circle2);
			lineList.add(circle2);
			circleList.add(circle2);

			this.waypoints.add(lineList);
		}
		
		/*
		 * Alle Kreise nach oben bringen
		 */
		for(final Circle circle : circleList) {
			this.page.getPlayingboardArea().bringToFront(circle);
		}
		
		
		/*
		 *  Auf der Minimap den Auswhaldreieck und das Bewegungsdreieck nach oben bringen
		 */
		if(this.page.getMinimap().getRectangleChoose() != null) {
			this.page.getMinimap().bringToFront(this.page.getMinimap().getRectangleChoose());
			this.page.getMinimap().bringToFront(this.rectangleMove);
		}

	}

	/**
	 * Liefert die Page
	 * 
	 * @return page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * @return the saveDescription
	 */
	public String getSaveDescription() {
		return this.saveDescription;
	}

	/**
	 * @return the saveDifficulty
	 */
	public String getSaveDifficulty() {
		return this.saveDifficulty;
	}

	/**
	 * @return the saveName
	 */
	public String getSaveName() {
		return this.saveName;
	}

	/**
	 * Prozedur um Image zu removen
	 * 
	 * @return Das entfernte Image
	 */
	private Image removeImage(String str, int i, int j) {
		for (Image img : this.fieldsImageList[i][j]) {
			if (img.getHref().equals(str)) {
				this.fieldsImageList[i][j].remove(img);
				return img;
			}
		}
		return null;
	}

	public void removeWayPointList() {
		for (List<VectorObject> sublist : this.waypoints) {
			for (VectorObject vectorObject : sublist) {
				this.page.getPlayingboardArea().remove(vectorObject);
			}
		}
		this.waypoints.clear();
	}

	/**
	 * Setzt die changed-flag
	 * 
	 * @param changed
	 *            flag
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * Verbirgt/Zeigt das Menue
	 */
	private void setMenueVisibility(boolean visibility) {
		if (visibility) {
			this.page.getEditingWindow().getSectionStack().expandSection(1);
			this.page.getEditingWindow().getSectionStack().expandSection(2);
			this.page.getEditingWindow().getSectionStack().collapseSection(3);
			this.page.getEditingWindow().getButtonMenue().setTitle(Page.props.mapGeneratorEditingWindow_menueOff());

			this.page.getEditingWindow().getSelectForm().setDisabled(true);
		} else {
			this.page.getEditingWindow().getSectionStack().collapseSection(1);
			this.page.getEditingWindow().getSectionStack().expandSection(2);
			this.page.getEditingWindow().getSectionStack().expandSection(3);
			this.page.getEditingWindow().getButtonMenue().setTitle(Page.props.mapGeneratorEditingWindow_menueOn());

			this.page.getEditingWindow().getSelectForm().setDisabled(false);
		}
		this.menueIsShown = visibility;
	}

	/**
	 * @param saveDescription
	 *            the saveDescription to set
	 */
	public void setSaveDescription(String saveDescription) {
		this.saveDescription = saveDescription;
	}

	/**
	 * @param saveDifficulty
	 *            the saveDifficulty to set
	 */
	public void setSaveDifficulty(String saveDifficulty) {
		this.saveDifficulty = saveDifficulty;
	}

	/**
	 * @param saveName
	 *            the saveName to set
	 */
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	/**
	 * Zeichnet die neuen Bild auf die Drawingarea
	 */
	private void drawSelectedImages() {
		for (int i = Math.min(this.iStart, this.iEnd); i <= Math.max(this.iStart, this.iEnd); i++) {
			for (int j = Math.min(this.jStart, this.jEnd); j <= Math.max(this.jStart, this.jEnd); j++) {
				for (DrawingInfo info : this.fields[i][j].getImagePathList()) {
					final Image img = MapGeneratorPresenter.this.page.getPlayingboardArea().add(info, j*50, i*50);
					this.fieldsImageList[i][j].add(img);
				}
				/*
				 * Falls Startfeld, muss die Restartrichtung hinzugefügt werden
				 */
				if (this.fields[i][j] instanceof StartField) {
					final Image img = new Image(j * 50, i * 50, 50, 50, "images/mapgenerator/restart_"
							+ ((StartField) this.fields[i][j]).getRespawnDirection().name().toLowerCase() + ".png");
					this.page.getPlayingboardArea().add(img);
					this.fieldsImageList[i][j].add(img);
				}
			}
		}
	}

	/**
	 * Testet die Felder
	 * 
	 * @return true, falls Test erfolgreich
	 */
	private boolean testFields() {
		try {
			final Field start = this.fields[this.iStart][this.jStart];
			for (int i = Math.min(this.iStart, this.iEnd); i <= Math.max(this.iStart, this.iEnd); i++) {
				for (int j = Math.min(this.jStart, this.jEnd); j <= Math.max(this.jStart, this.jEnd); j++) {
					if (!start.equals(this.fields[i][j])) {
						return false;
					}

				}
			}
		} catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * Testet, ob bei den ausgewählten Feldern die Wände identisch sind.
	 * @return
	 */
	private WallInfo testWallInfos() {
		if(this.fields[iStart][jStart] instanceof WallField) {
			
			final WallInfo wallInfo = ((WallField)this.fields[iStart][jStart]).getWallInfo();
			
			for (int i = Math.min(this.iStart, this.iEnd); i <= Math.max(this.iStart, this.iEnd); i++) {
				for (int j = Math.min(this.jStart, this.jEnd); j <= Math.max(this.jStart, this.jEnd); j++) {
					if(!(this.fields[i][j] instanceof WallField) || !((WallField)this.fields[i][j]).getWallInfo().equals(wallInfo)) {
						return null;
					}
				}
			}
			return wallInfo;
		} 
		return null;
	}
	
	/**
	 * Testet, ob bei den ausgewählten Feldern die Laserkanonen identisch sind.
	 * @return
	 */
	private LaserCannonInfo testLaserCannonInfos() {
		if(this.fields[iStart][jStart] instanceof LaserCannonField) {
			
			final LaserCannonInfo laserCannonInfo = ((LaserCannonField)this.fields[iStart][jStart]).getLaserCannonInfo();
			
			for (int i = Math.min(this.iStart, this.iEnd); i <= Math.max(this.iStart, this.iEnd); i++) {
				for (int j = Math.min(this.jStart, this.jEnd); j <= Math.max(this.jStart, this.jEnd); j++) {
					if(!(this.fields[i][j] instanceof LaserCannonField) || !((LaserCannonField)this.fields[i][j]).getLaserCannonInfo().equals(laserCannonInfo)) {
						return null;
					}
				}
			}
			return laserCannonInfo;
		} 
		return null;
	}

	/**
	 * Diese Methode bekommt ein Spielbrett Spielbrett und zeichnet es auf die Drawingarea der MapGeneratorPage.
	 * 
	 * @param playingBoard
	 *            das zu zeichnende Spielbrett
	 */
	@SuppressWarnings("unchecked")
	public void transmitPlayingBoard(final PlayingBoard playingBoard) {
		/*
		 * Fortschrittfenster anzeigen
		 */
		final ProgressWindow progressWindow = new ProgressWindow(Page.props.mapGeneratorPage_progressWindow_load());

		/*
		 * Laden im Timer
		 */
		new Timer() {
			private int i = 0;
			private int a = 0;
			private int percentPerStep;

			@Override
			public void run() {

				if (this.i == 0) {
					MapGeneratorPresenter.this.saveName = playingBoard.getName();
					MapGeneratorPresenter.this.saveDescription = playingBoard.getDescription();
					MapGeneratorPresenter.this.saveDifficulty = playingBoard.getDifficulty();

					MapGeneratorPresenter.this.fields = playingBoard.getFields();
					MapGeneratorPresenter.this.page.setDrawingArea(playingBoard.getWidth() * 50, playingBoard.getHeight() * 50);
					MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(false);
					MapGeneratorPresenter.this.page.getMinimap().setVisible(false);
					MapGeneratorPresenter.this.fieldsImageList = new ArrayList[playingBoard.getHeight()][playingBoard.getWidth()];
					this.i = 1;
					this.percentPerStep = 100 / playingBoard.getHeight();
					schedule(50);
					return;
				}

				if (this.i == 1) {
					while (this.a < playingBoard.getHeight()) {
						for (int j = 0; j < MapGeneratorPresenter.this.fields[this.a].length; j++) {
							MapGeneratorPresenter.this.fieldsImageList[this.a][j] = new ArrayList<Image>();
							for (DrawingInfo info : MapGeneratorPresenter.this.fields[this.a][j].getImagePathList()) {
								final Image img = MapGeneratorPresenter.this.page.getPlayingboardArea().add(info, j*50, a*50);
								MapGeneratorPresenter.this.fieldsImageList[this.a][j].add(img);	
							}
							if (MapGeneratorPresenter.this.fields[this.a][j] instanceof StartField) {
								final Image img = new Image(j * 50, this.a * 50, 50, 50, "images/mapgenerator/restart_"
										+ ((StartField) MapGeneratorPresenter.this.fields[this.a][j]).getRespawnDirection()
												.name().toLowerCase() + ".png");
								MapGeneratorPresenter.this.fieldsImageList[this.a][j].add(img);
								MapGeneratorPresenter.this.page.getPlayingboardArea().add(img);
							}
						}
						this.a += 1;
						progressWindow.getProgressbar().setPercentDone(
								progressWindow.getProgressbar().getPercentDone() + this.percentPerStep);

						progressWindow.getLabel().setContents(
								Page.props.mapGeneratorPresenter_progressWindow_progress() + ": "
										+ progressWindow.getProgressbar().getPercentDone() + "%");
						schedule(50);
						return;
					}

					progressWindow.getProgressbar().setPercentDone(100);
					progressWindow.getLabel()
							.setContents(Page.props.mapGeneratorPresenter_progressWindow_progress() + ": : 100%");
					this.i = 2;
					schedule(100);
					return;
				}

				if (MapGeneratorPresenter.this.page.getEditingWindow().getCheckBox_showWays().getValueAsBoolean()) {
					try {
						new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
								.findWaysToCheckpoints();
					} catch (CheckpointFieldNotReachableException ex) {
						/*
						 * nix passiert
						 */
					}
				}

				MapGeneratorPresenter.this.rectangleChoose.setWidth(50);
				MapGeneratorPresenter.this.rectangleChoose.setHeight(50);
				MapGeneratorPresenter.this.rectangleChoose.setX(0);
				MapGeneratorPresenter.this.rectangleChoose.setY(0);
				MapGeneratorPresenter.this.page.getPlayingboardArea().add(MapGeneratorPresenter.this.rectangleChoose);

				MapGeneratorPresenter.this.rectangleMove.setX(0);
				MapGeneratorPresenter.this.rectangleMove.setY(0);
				if (playingBoard.getWidth() > 14) {
					MapGeneratorPresenter.this.rectangleMove.setWidth(143);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setWidth(MapGeneratorPresenter.this.page.getMinimap().getWidth());
				}
				if (playingBoard.getHeight() > 9) {
					MapGeneratorPresenter.this.rectangleMove.setHeight(97);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setHeight(MapGeneratorPresenter.this.page.getMinimap().getHeight());
				}

				if (playingBoard.getWidth() <= 14 && playingBoard.getHeight() <= 9) {
					MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(0);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(1);
				}
				MapGeneratorPresenter.this.page.getMinimap().addRectangle(MapGeneratorPresenter.this.rectangleMove);

				MapGeneratorPresenter.this.iStart = 0;
				MapGeneratorPresenter.this.iEnd = 0;
				MapGeneratorPresenter.this.jStart = 0;
				MapGeneratorPresenter.this.jEnd = 0;

				updateElements();

				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseDownHandler(MapGeneratorPresenter.this.mouseDownHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseMoveHandler(MapGeneratorPresenter.this.mouseMoveHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseUpHandler(MapGeneratorPresenter.this.mouseUpHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseOutHandler(MapGeneratorPresenter.this.mouseOutHandler);

				MapGeneratorPresenter.this.page.getDrawingAreaArea().addScrolledHandler(scrollHandler);
				MapGeneratorPresenter.this.page.getDrawingAreaAreaInner().addMouseStillDownHandler(chooseScrollhandler);

				// Menue-Schalter einschalten
				MapGeneratorPresenter.this.page.getEditingWindow().getButtonMenue().setDisabled(false);

				// Menue ausblenden
				setMenueVisibility(false);

				MapGeneratorPresenter.this.page.getButtonSave().setDisabled(false);
				MapGeneratorPresenter.this.page.getButtonUnload().setDisabled(false);
				MapGeneratorPresenter.this.page.getButtonTransform().setDisabled(false);
				MapGeneratorPresenter.this.page.getFieldTypeBox().setDisabled(false);

				/*
				 * ProgressWindow zerstören
				 */
				progressWindow.destroy();

				
				/*
				 * DrawingArea anzeigen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(true);
				MapGeneratorPresenter.this.page.getMinimap().setVisible(true);
			}
		}.schedule(400);
	}
	
	/**
	 * Diese Methode bekommt Spielfelder übergeben und zeichnet es auf die Drawingarea der MapGeneratorPage.
	 * 
	 * @param playingBoard
	 *            das zu zeichnende Spielbrett
	 */
	@SuppressWarnings("unchecked")
	public void transformFields(final Field[][] transformedFields, final String title) {
		/*
		 * Fortschrittfenster anzeigen
		 */
		final ProgressWindow progressWindow = new ProgressWindow(title);

		/*
		 * Laden im Timer
		 */
		new Timer() {
			private int i = 0;
			private int a = 0;
			private int percentPerStep;

			@Override
			public void run() {

				if (this.i == 0) {
					MapGeneratorPresenter.this.fields = transformedFields;
					MapGeneratorPresenter.this.page.setDrawingArea(transformedFields[0].length * 50, transformedFields.length * 50);
					MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(false);
					MapGeneratorPresenter.this.page.getMinimap().setVisible(false);
					MapGeneratorPresenter.this.fieldsImageList = new ArrayList[transformedFields.length][transformedFields[0].length];
					this.i = 1;
					this.percentPerStep = 100 / transformedFields.length;
					schedule(50);
					return;
				}

				if (this.i == 1) {
					while (this.a < transformedFields.length) {
						for (int j = 0; j < MapGeneratorPresenter.this.fields[this.a].length; j++) {
							MapGeneratorPresenter.this.fieldsImageList[this.a][j] = new ArrayList<Image>();
							for (DrawingInfo info : MapGeneratorPresenter.this.fields[this.a][j].getImagePathList()) {
								final Image img = MapGeneratorPresenter.this.page.getPlayingboardArea().add(info, j*50, a*50);
								MapGeneratorPresenter.this.fieldsImageList[this.a][j].add(img);
							}
							if (MapGeneratorPresenter.this.fields[this.a][j] instanceof StartField) {
								final Image img = new Image(j * 50, this.a * 50, 50, 50, "images/mapgenerator/restart_"
										+ ((StartField) MapGeneratorPresenter.this.fields[this.a][j]).getRespawnDirection()
												.name().toLowerCase() + ".png");
								MapGeneratorPresenter.this.fieldsImageList[this.a][j].add(img);
								MapGeneratorPresenter.this.page.getPlayingboardArea().add(img);
							}
						}
						this.a += 1;
						progressWindow.getProgressbar().setPercentDone(
								progressWindow.getProgressbar().getPercentDone() + this.percentPerStep);

						progressWindow.getLabel().setContents(
								Page.props.mapGeneratorPresenter_progressWindow_progress() + ": "
										+ progressWindow.getProgressbar().getPercentDone() + "%");
						schedule(50);
						return;
					}

					progressWindow.getProgressbar().setPercentDone(100);
					progressWindow.getLabel()
							.setContents(Page.props.mapGeneratorPresenter_progressWindow_progress() + ": : 100%");
					this.i = 2;
					schedule(100);
					return;
				}

				if (MapGeneratorPresenter.this.page.getEditingWindow().getCheckBox_showWays().getValueAsBoolean()) {
					try {
						new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
								.findWaysToCheckpoints();
					} catch (CheckpointFieldNotReachableException ex) {
						/*
						 * nix passiert
						 */
					}
				}

				MapGeneratorPresenter.this.rectangleChoose.setWidth(50);
				MapGeneratorPresenter.this.rectangleChoose.setHeight(50);
				MapGeneratorPresenter.this.rectangleChoose.setX(0);
				MapGeneratorPresenter.this.rectangleChoose.setY(0);
				MapGeneratorPresenter.this.page.getPlayingboardArea().add(MapGeneratorPresenter.this.rectangleChoose);

				MapGeneratorPresenter.this.rectangleMove.setX(0);
				MapGeneratorPresenter.this.rectangleMove.setY(0);
				if (transformedFields[0].length > 14) {
					MapGeneratorPresenter.this.rectangleMove.setWidth(143);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setWidth(MapGeneratorPresenter.this.page.getMinimap().getWidth());
				}
				if (transformedFields.length > 9) {
					MapGeneratorPresenter.this.rectangleMove.setHeight(97);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setHeight(MapGeneratorPresenter.this.page.getMinimap().getHeight());
				}

				if (transformedFields[0].length <= 14 && transformedFields.length <= 9) {
					MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(0);
				} else {
					MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(1);
				}
				MapGeneratorPresenter.this.page.getMinimap().addRectangle(MapGeneratorPresenter.this.rectangleMove);

				MapGeneratorPresenter.this.iStart = 0;
				MapGeneratorPresenter.this.iEnd = 0;
				MapGeneratorPresenter.this.jStart = 0;
				MapGeneratorPresenter.this.jEnd = 0;

				updateElements();

				/*
				 * Eenthandler hinzufügen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseDownHandler(MapGeneratorPresenter.this.mouseDownHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseMoveHandler(MapGeneratorPresenter.this.mouseMoveHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseUpHandler(MapGeneratorPresenter.this.mouseUpHandler);
				MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseOutHandler(MapGeneratorPresenter.this.mouseOutHandler);

				MapGeneratorPresenter.this.page.getDrawingAreaArea().addScrolledHandler(scrollHandler);
				MapGeneratorPresenter.this.page.getDrawingAreaAreaInner().addMouseStillDownHandler(chooseScrollhandler);

				// Menue-Schalter einschalten
				MapGeneratorPresenter.this.page.getEditingWindow().getButtonMenue().setDisabled(false);

				// Menue ausblenden
				setMenueVisibility(false);

				/*
				 * Buttons wieder verfügbar machen
				 */
				MapGeneratorPresenter.this.page.getButtonSave().setDisabled(false);
				MapGeneratorPresenter.this.page.getButtonUnload().setDisabled(false);
				MapGeneratorPresenter.this.page.getButtonTransform().setDisabled(false);
				MapGeneratorPresenter.this.page.getFieldTypeBox().setDisabled(false);

				/*
				 * ProgressWindow zerstören
				 */
				progressWindow.destroy();

				
				/*
				 * DrawingArea anzeigen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(true);
				MapGeneratorPresenter.this.page.getMinimap().setVisible(true);
			}
		}.schedule(400);
	}

	/**
	 * updatet bei Feldwechsel
	 */
	private void updateElements() {

		/*
		 * Hierin wird der Klassenname des Feldes gespeichert
		 */
		final StringBuilder fieldName = new StringBuilder();
		for (int a = this.fields[this.iStart][this.jStart].getClass().getName().length() - 1; a >= 0; a--) {
			if (this.fields[this.iStart][this.jStart].getClass().getName().charAt(a) == '.') {
				break;
			}
			fieldName.insert(0, this.fields[this.iStart][this.jStart].getClass().getName().charAt(a));
		}

		// Die Richtige MainArea anzeigen
		this.page.getEditingWindow().getMainSectionArea().getMembers()[this.showingWindowContentID].hide();
		this.showingWindowContentID = getFieldTypeIDByName(fieldName.toString());
		this.windowContentStack = (WindowContentStack) this.page.getEditingWindow().getMainSectionArea().getMembers()[this.showingWindowContentID];
		this.page.getEditingWindow().getMainSectionArea().getMembers()[this.showingWindowContentID].show();

		/**
		 * Im Folgenden werden die Werte im Editorfenster für das jeweilige Feld richtig gesetzt
		 */
		// Wände
		if (this.fields[this.iStart][this.jStart] instanceof WallField) {
			buildWalls();
		}

		if (this.fields[this.iStart][this.jStart] instanceof LaserCannonField) {
			buildLasers();
		}

		if (fieldName.toString().equals("StartField")) {
			buildStartField();
		} else if (fieldName.toString().equals("RepairField")) {
			buildRepairField();
		} else if (fieldName.toString().equals("CheckpointField")) {
			buildCheckpointField();
		} else if (fieldName.toString().equals("GearField")) {
			buildGearField();
		} else if (fieldName.toString().equals("PusherField")) {
			buildPusherField();
		} else if (fieldName.toString().equals("CompactorField")) {
			buildCompactorField();
		} else if (fieldName.toString().equals("ConveyorBeltField")) {
			buildConveyorBelt();
		}

		// Namen in der ComboBox auswählen
		this.page.getFieldTypeBox().setValue(fieldName.toString());
	}

	/**
	 * 
	 */
	public boolean changePlayingBoardSize(final int width, final int height) {
		/*
		 * Fortschrittfenster anzeigen
		 */
		final ProgressWindow progressWindow = new ProgressWindow(Page.props.mapGeneratorPage_progressWindow_changeSize());

		
		/*
		 * Das Erstellen wird in einem Extra Timer geschehen
		 */
		new Timer() {
			private int i = 0;
			private int a = 0;
			private int percentPerStep;
			
			private HandlerRegistration doubleClickHandler;
			private HandlerRegistration upHandler;
			private HandlerRegistration downHandler;
			private HandlerRegistration moveHandler;
			private HandlerRegistration outHandler;
			
			@SuppressWarnings("unchecked")
			private final ArrayList<Image>[][] minimapImages  = new ArrayList[fieldsImageList.length][fieldsImageList[0].length];
			@SuppressWarnings("unchecked")
			private final ArrayList<Image>[][] newImages = new ArrayList[height][width];
			
			/** speichert die alten Felder */
			private final Field[][] oldFields = fields;

			@Override
			public void run() {

				if (this.i == 0) {
					page.getEditingWindow().setDisabled(true);
					MapGeneratorPresenter.this.page.setDrawingArea(width * 50, height * 50);
					MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(false);
					MapGeneratorPresenter.this.page.getMinimap().setVisible(false);
					MapGeneratorPresenter.this.fields = new Field[height][width];
					this.i = 1;
					this.percentPerStep = 100 / height;
					schedule(50);
					return;
				}

				if (this.i == 1) {
					while (this.a < height) {
						for (int j = 0; j < width; j++) {
							MapGeneratorPresenter.this.fields[this.a][j] = new BasicField(new WallInfo(), new LaserCannonInfo());
							this.newImages[this.a][j] = new ArrayList<Image>();
							for (final DrawingInfo info : MapGeneratorPresenter.this.fields[this.a][j].getImagePathList()) {
								final Image img = MapGeneratorPresenter.this.page.getPlayingboardArea().add(info, j*50, this.a*50);
								this.newImages[this.a][j].add(img);
							}
						}

						this.a += 1;
						progressWindow.getProgressbar().setPercentDone(
								progressWindow.getProgressbar().getPercentDone() + this.percentPerStep);

						progressWindow.getLabel().setContents(
								Page.props.mapGeneratorPresenter_progressWindow_progress() + ": "
										+ progressWindow.getProgressbar().getPercentDone() + "%");
						schedule(50);
						return;
					}

					progressWindow.getProgressbar().setPercentDone(100);
					progressWindow.getLabel().setContents(Page.props.mapGeneratorPresenter_progressWindow_progress() + ": 100%");
					this.i = 2;
					schedule(100);
					return;
				}

				
				/*
				 *  Die Bilder der alten Karte werden auf die Drawingarea gezeichnet
				 *  und somit automatisch zur Minimap hinzugefügt 
				 */
				for(int i=0; i<fieldsImageList.length; i++) {
					for(int j=0; j<fieldsImageList[i].length; j++) {
						minimapImages[i][j] = new ArrayList<Image>();
						Image remove = null;
						for(final Image image : fieldsImageList[i][j]) {
							if(!image.getHref().equals("images/fields/basicfield.png")) {
								page.getPlayingboardArea().add(image);
								minimapImages[i][j].add((Image)page.getPlayingboardArea().getMinimapVecorObject(image));
							} else {
								remove = image;
							}
						}
						if(remove != null) {
							fieldsImageList[i][j].remove(remove);
						}
					}
				}
				
				/*
				 * Werte auf "default" setzen
				 */
				isMoving = false;
				firstOldI = 0;
				firstOldJ = 0;
				currentResizedX = -1;
				currentResizedY = -1;
				mapMoveRect.setFillOpacity(0.0);
				mapMoveRect.setStrokeWidth(2);
				mapMoveRect.setStrokeColor("gray");
				mapMoveRect.setWidth(fieldsImageList[0].length*50);
				mapMoveRect.setHeight(fieldsImageList.length*50);
				mapMoveRect.setX(0);
				mapMoveRect.setY(0);
				page.getPlayingboardArea().add(mapMoveRect);
				mapMoveRectMinimap = (Rectangle)page.getPlayingboardArea().getMinimapVecorObject(mapMoveRect);
				
				/*
				 * für den MausCursor
				 */
				final VStack cursorStack = new VStack();
				cursorStack.setWidth(page.getDrawingAreaAreaInner().getWidth());
				cursorStack.setHeight(page.getDrawingAreaAreaInner().getHeight());
				page.getDrawingAreaAreaInner().addChild(cursorStack);
				
				

				this.downHandler = page.getDrawingAreaAreaInner().addMouseDownHandler(new com.smartgwt.client.widgets.events.MouseDownHandler() {
					@Override
					public void onMouseDown(
							com.smartgwt.client.widgets.events.MouseDownEvent event) {

						final int eventX = (event.getX() - page.getDrawingAreaAreaInner().getAbsoluteLeft()) / 50;
						final int eventY = (event.getY() - page.getDrawingAreaAreaInner().getAbsoluteTop()) / 50;
						
						if((eventX >= firstOldJ && eventX < firstOldJ+fieldsImageList[0].length
								&& eventY >= firstOldI && eventY < firstOldI+fieldsImageList.length)) {
							
							MapGeneratorPresenter.this.currentResizedX = eventX;
							MapGeneratorPresenter.this.currentResizedY = eventY;
						
							isMoving = true;
						}

					}
				});
				
				/*
				 * 
				 */
				this.moveHandler = page.getDrawingAreaAreaInner().addMouseMoveHandler(new com.smartgwt.client.widgets.events.MouseMoveHandler() {
					@Override
					public void onMouseMove(
							com.smartgwt.client.widgets.events.MouseMoveEvent event) {
						
						final int eventX = (event.getX() - page.getDrawingAreaAreaInner().getAbsoluteLeft()) / 50;
						final int eventY = (event.getY() - page.getDrawingAreaAreaInner().getAbsoluteTop()) / 50;
						
						if(isMoving || (eventX >= firstOldJ && eventX < firstOldJ+fieldsImageList[0].length
								&& eventY >= firstOldI && eventY < firstOldI+fieldsImageList.length)) {
							
							cursorStack.setCursor(Cursor.MOVE);
							mapMoveRect.setStrokeColor("white");
							mapMoveRectMinimap.setStrokeColor("white");
							cursorInOldField = true;
						} else {
							cursorStack.setCursor(Cursor.DEFAULT);
							mapMoveRect.setStrokeColor("gray");
							mapMoveRectMinimap.setStrokeColor("gray");
							cursorInOldField = false;
						}

						if (isMoving && (MapGeneratorPresenter.this.currentResizedX != eventX
								|| MapGeneratorPresenter.this.currentResizedY != eventY)
								&& eventX>=0 && eventX<fields[0].length && eventY>=0 && eventY<fields.length) {
							
							final int moveX =  (int)Math.signum(currentResizedX - eventX) * 50;
							final int moveY =  (int)Math.signum(currentResizedY - eventY) * 50;
							MapGeneratorPresenter.this.currentResizedX = eventX;
							MapGeneratorPresenter.this.currentResizedY = eventY;
							
							/*
							 * alte bilder verschieben
							 */
							for(int i=0; i<fieldsImageList.length; i++) {
								for(int j=0; j<fieldsImageList[0].length; j++) {
									for(final Image image : fieldsImageList[i][j]) {
										image.setX(image.getX() - moveX);
										image.setY(image.getY() - moveY);
									}
									for(final Image image : minimapImages[i][j]) {
										image.setX(image.getX() - moveX / 5);
										image.setY(image.getY() - moveY / 5);
									}
								}
							}
							
							mapMoveRect.setX(mapMoveRect.getX() - moveX);
							mapMoveRect.setY(mapMoveRect.getY() - moveY);
							
							mapMoveRectMinimap.setX(mapMoveRectMinimap.getX() - moveX/5);
							mapMoveRectMinimap.setY(mapMoveRectMinimap.getY() - moveY/5);
							
							firstOldJ = mapMoveRect.getX() / 50;
							firstOldI = mapMoveRect.getY() / 50;
						} 
					}
				});
				
				
				this.upHandler = page.getDrawingAreaAreaInner().addMouseUpHandler(new com.smartgwt.client.widgets.events.MouseUpHandler() {					
					@Override
					public void onMouseUp(com.smartgwt.client.widgets.events.MouseUpEvent event) {
						isMoving = false;
					}
				});

				this.outHandler = page.getDrawingAreaAreaInner().addMouseOutHandler(new com.smartgwt.client.widgets.events.MouseOutHandler() {	
					@Override
					public void onMouseOut(
							com.smartgwt.client.widgets.events.MouseOutEvent event) {
						isMoving = false;
						mapMoveRect.setStrokeColor("gray");
						mapMoveRectMinimap.setStrokeColor("gray");
					}
				});
				
				this.doubleClickHandler = page.getDrawingAreaAreaInner().addDoubleClickHandler(new DoubleClickHandler() {
					
					/**
					 * Behandlungsroutine beim Doppelklick
					 */
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						
						/*
						 * Wenn auf das alte Feld geklickt wird
						 */
						if(cursorInOldField) {
						
						/*
						 * Verschiebungsviereck von der drawingArea entfernen
						 */
						page.getPlayingboardArea().remove(mapMoveRect);
						
						/*
						 * Ein neues Spielbrett wird aus dem alten zusammengebastelt
						 * und die fieldsImagelist neu gesetzt
						 */
						for(int i = 0; i<oldFields.length; i++) {
							for(int j = 0; j<oldFields[i].length; j++) {
								if(i+firstOldI >= 0 && i+firstOldI<fields.length
										&& j+firstOldJ >= 0 && j+firstOldJ<fields[0].length) {
									
									fields[i+firstOldI][j+firstOldJ] = oldFields[i][j];
								
									/*
									 * alte Bilder von der DrawingArea entfernen
									 */
									for(final Image oldImg : newImages[i+firstOldI][j+firstOldJ]) {
										if(fields[i+firstOldI][j+firstOldJ] instanceof ConveyorBeltField
											|| fields[i+firstOldI][j+firstOldJ] instanceof HoleField
											|| fields[i+firstOldI][j+firstOldJ] instanceof CompactorField) {
											
											page.getPlayingboardArea().remove(oldImg);
										}
										
									}
									newImages[i+firstOldI][j+firstOldJ] = fieldsImageList[i][j];
								}
							}
						}
						
						/*
						 * ImageArray neu setzen
						 */
						fieldsImageList = newImages;
						
						/*
						 * Karte wurde geändert -> auf true setzen
						 */
						MapGeneratorPresenter.this.changed = true;
						
						/*
						 * Auswahlrechteck wieder setzen
						 */
						MapGeneratorPresenter.this.rectangleChoose.setWidth(50);
						MapGeneratorPresenter.this.rectangleChoose.setHeight(50);
						MapGeneratorPresenter.this.rectangleChoose.setX(0);
						MapGeneratorPresenter.this.rectangleChoose.setY(0);
						MapGeneratorPresenter.this.page.getPlayingboardArea().add(MapGeneratorPresenter.this.rectangleChoose);
						
						/*
						 * Bewegungsrechteck für Minimap wieder setzen
						 */
						MapGeneratorPresenter.this.rectangleMove.setX(page.getDrawingAreaArea().getScrollLeft()/5);
						MapGeneratorPresenter.this.rectangleMove.setY(page.getDrawingAreaArea().getScrollTop()/5);
						if (fields[0].length > 14) {
							MapGeneratorPresenter.this.rectangleMove.setWidth(143);
						} else {
							MapGeneratorPresenter.this.rectangleMove.setWidth(MapGeneratorPresenter.this.page.getMinimap().getWidth());
						}
						if (fields.length > 9) {
							MapGeneratorPresenter.this.rectangleMove.setHeight(97);
						} else {
							MapGeneratorPresenter.this.rectangleMove.setHeight(MapGeneratorPresenter.this.page.getMinimap().getHeight());
						}

						if (fields[0].length <= 14 && fields.length <= 9) {
							MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(0);
						} else {
							MapGeneratorPresenter.this.rectangleMove.setStrokeWidth(1);
						}
						MapGeneratorPresenter.this.page.getMinimap().addRectangle(MapGeneratorPresenter.this.rectangleMove);

						/*
						 * Werte wieder rückgängig machen
						 */
						MapGeneratorPresenter.this.iStart = 0;
						MapGeneratorPresenter.this.iEnd = 0;
						MapGeneratorPresenter.this.jStart = 0;
						MapGeneratorPresenter.this.jEnd = 0;

						/*
						 * aktuelles Feld im Editor anzeigen lassen
						 */
						updateElements();
						
						/*
						 * EventListener wieder entfernen
						 */
						downHandler.removeHandler();
						moveHandler.removeHandler();
						upHandler.removeHandler();
						outHandler.removeHandler();
						doubleClickHandler.removeHandler();
						
						
						/*
						 * normale Eventistener wieder hinzufügen
						 */
						MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseDownHandler(MapGeneratorPresenter.this.mouseDownHandler);
						MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseMoveHandler(MapGeneratorPresenter.this.mouseMoveHandler);
						MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseUpHandler(MapGeneratorPresenter.this.mouseUpHandler);
						MapGeneratorPresenter.this.page.getPlayingboardArea().addMouseOutHandler(MapGeneratorPresenter.this.mouseOutHandler);
						MapGeneratorPresenter.this.page.getDrawingAreaArea().addScrolledHandler(scrollHandler);
						MapGeneratorPresenter.this.page.getDrawingAreaAreaInner().addMouseStillDownHandler(chooseScrollhandler);
						
						/*
						 * CursorArea wieder entfernen
						 */
						page.getDrawingAreaAreaInner().removeChild(cursorStack);
						
						/*
						 * Waypoints setzen, falls ausgewählt
						 */
						if (MapGeneratorPresenter.this.page.getEditingWindow().getCheckBox_showWays().getValueAsBoolean()) {
							try {
								new WayPointComputer(MapGeneratorPresenter.this.fields, MapGeneratorPresenter.this)
										.findWaysToCheckpoints();
							} catch (CheckpointFieldNotReachableException ex) {
								/*
								 * nix passiert
								 */
							}
						}
						
						/*
						 * Das Editierfenster wieder verfügbar machen
						 */
						page.getEditingWindow().setDisabled(false);
						
						/*
						 *  Menue ausblenden
						 */
						setMenueVisibility(false);
					}
					}
				});
				
				
				/*
				 * DrawingArea anzeigen
				 */
				MapGeneratorPresenter.this.page.getPlayingboardArea().setVisible(true);
				MapGeneratorPresenter.this.page.getMinimap().setVisible(true);
				
				/*
				 * ProgressWindow zerstören
				 */
				progressWindow.destroy();
			}

		}.schedule(400);

		return true;
	}
	
	private Rectangle mapMoveRectMinimap = new Rectangle(1, 1, 1, 1);
	private Rectangle mapMoveRect = new Rectangle(1, 1, 1, 1);
	private boolean isMoving = false;
	private int firstOldI = 0;
	private int firstOldJ = 0;
	private int currentResizedX = -1;
	private int currentResizedY = -1;
	private boolean cursorInOldField;
	
	public Field[][] getFields() {
		return fields;
	}
}
