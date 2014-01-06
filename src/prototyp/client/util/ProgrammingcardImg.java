package prototyp.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import prototyp.client.view.round.PlayerStatusArea;
import prototyp.shared.programmingcard.Programmingcard;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.DragRepositionStartEvent;
import com.smartgwt.client.widgets.events.DragRepositionStartHandler;

/**
 * Klasse für ein drag- und dropbares Programmierkartenbild
 * 
 * @author Marcus
 * 
 */
public class ProgrammingcardImg extends Img {

	/** der Kartenslot, auf der das Image liegt */
	private CardSlot cardSlot;

	/** die Programmierkarte, die zum Image gehört */
	private Programmingcard programmingcard;

	/** Label für die Priorität */
	private final Label priority = new Label();;

	/** der Platz für die gesetzten Programmierkarten */
	private final List<CardSlot> cardSetSlots;

	/** der Platz für die ausgeteilten Programmierkarten */
	private final List<CardSlot> cardGivenSlots;
	

	/**
	 * Konstruktor
	 * 
	 * @param width
	 *            die Weite des Bildes
	 * @param height
	 *            die Höhe des Bildes
	 * @param programmingcard
	 *            die dazugehörige Programmierkarte
	 * @param cardSlot
	 *            der Kartenslot, auf der das Bild liegt
	 */
	public ProgrammingcardImg(final Programmingcard programmingcard,
			CardSlot cardSlot, int width, int height,
			final CardSlot[] cardSetSlots, final CardSlot[] cardGivenSlots) {

		/*
		 * SuperKonstruktor aufrufen
		 */
		super(programmingcard.getImagePath(), width, height);

		this.setStyleName("programmincCard");

		// Programmierkarte zuweisen
		this.programmingcard = programmingcard;

		// Kartenslot zuweisen
		this.cardSlot = cardSlot;

		// Layout setzen
		this.setLayoutAlign(Alignment.CENTER);

		// Man kann dieses Bild draggen...
		setCanDragReposition(true);

		// ...und droppen
		setCanDrop(true);

		// beim Dragen wird ein Schatten angezeigt
		setShowDragShadow(true);

		// der Cursor ist ein Handcursor
		setCursor(Cursor.HAND);
		setDragRepositionCursor(Cursor.HAND);

		setDragAppearance(DragAppearance.TARGET);

		// Die Priorität im Label anzeigen
		this.priority.setAutoFit(true);
		this.priority.setStyleName("programmingCardPriority");
		this.priority.setCursor(Cursor.HAND);
		if (programmingcard.getPriority() < 10) {
			this.priority.setContents("00" + programmingcard.getPriority());
		} else if (programmingcard.getPriority() < 100) {
			this.priority.setContents("0" + programmingcard.getPriority());
		} else {
			this.priority.setContents("" + programmingcard.getPriority());
		}

		
		
		this.addChild(this.priority);

		Collections.addAll(this.cardGivenSlots = new ArrayList<CardSlot>(
				cardGivenSlots.length), cardGivenSlots);
		Collections.addAll(this.cardSetSlots = new ArrayList<CardSlot>(
				cardSetSlots.length), cardSetSlots);

		addDragRepositionStartHandler(new DragRepositionStartHandler() {
			@Override
			public void onDragRepositionStart(DragRepositionStartEvent event) {
				DraggedObject.setDragObject(ProgrammingcardImg.this);
			}
		});

		addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {

				if (!ProgrammingcardImg.this.cardSlot.isLocked()) {
					if (ProgrammingcardImg.this.cardGivenSlots
							.contains(ProgrammingcardImg.this.cardSlot)) {
						for (final CardSlot cardSlot : ProgrammingcardImg.this.cardSetSlots) {
							if (cardSlot.isFree() && !cardSlot.isLocked()) {
								cardSlot.getPresenter().sendOneCardSet(
										cardSlot.getSlotNumber(), 0);
								ProgrammingcardImg.this.cardSlot.removeProgrammingcardImg();
								cardSlot.setProgrammingcardImg(ProgrammingcardImg.this);
								break;
							}
						}
					} else {
						for (final CardSlot cardSlot : ProgrammingcardImg.this.cardGivenSlots) {
							if (cardSlot.isFree() && !cardSlot.isLocked()) {
								cardSlot.getPresenter().sendOneCardSet(
												ProgrammingcardImg.this.cardSlot.getSlotNumber(), 1);
								ProgrammingcardImg.this.cardSlot.removeProgrammingcardImg();
								cardSlot.setProgrammingcardImg(ProgrammingcardImg.this);
								break;
							}
						}
					}
					
					/*
					 * den ReadyButton enablen oder disablen
					 */
					((PlayerStatusArea)cardSetSlots[0].getPresenter().getPage().getRobotStatusArea()).getReadyButton().setDisabled(
							!((PlayerStatusArea)cardSetSlots[0].getPresenter().getPage().getRobotStatusArea()).allCardsSet());
				}
			}
		});
	}

	/**
	 * Liefert den Kartenslot, auf dem das Bild liegt
	 * 
	 * @return the cardSlot
	 */
	public CardSlot getCardSlot() {
		return this.cardSlot;
	}

	/**
	 * Liefert die Programmierkarte, die zu dem Bild gehört
	 * 
	 * @return the programmingcard
	 */
	public Programmingcard getProgrammingcard() {
		return this.programmingcard;
	}

	@Override
	public void setCanDragReposition(Boolean canDragReposition) {
		super.setCanDragReposition(canDragReposition);
		if (canDragReposition) {
			setCursor(Cursor.HAND);
			setDragRepositionCursor(Cursor.HAND);
			this.priority.setCursor(Cursor.HAND);
		} else {
			setCursor(Cursor.DEFAULT);
			setDragRepositionCursor(Cursor.DEFAULT);
			this.priority.setCursor(Cursor.DEFAULT);
		}
	}

	/**
	 * Setzt den Kartenslot, auf dem das Bild liegt
	 * 
	 * @param cardSlot
	 *            the cardSlot to set
	 */
	public void setCardSlot(final CardSlot cardSlot) {
		this.cardSlot = cardSlot;
	}


	/**
	 * Setzt die Visibilität des Labels
	 */
	public void setPriorityVisible(boolean visible) {
		this.priority.setVisible(visible);
	}
}
