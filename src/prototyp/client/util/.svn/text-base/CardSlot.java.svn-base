package prototyp.client.util;

import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.view.round.PlayerStatusArea;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.events.DropOutEvent;
import com.smartgwt.client.widgets.events.DropOutHandler;
import com.smartgwt.client.widgets.events.DropOverEvent;
import com.smartgwt.client.widgets.events.DropOverHandler;
import com.smartgwt.client.widgets.layout.Layout;

/**
 * Ein Kartenslot in der Roundplayerpage
 * 
 * @author Marcus
 * 
 */
public class CardSlot extends Layout {

	/** der RoundplayerpagePresenter */
	private final RoundPlayerPagePresenter presenter;

	/** die Nummer des Kartenslots */
	private final int slotNumber;
	
	private ProgrammingcardImg programmingcardImg;

	/**
	 * Konstruktor
	 * 
	 * @param edgeImage
	 * @param dropTypes
	 *            string welche bilder gedropped werden können
	 */
	public CardSlot(int slotNumber, final RoundPlayerPagePresenter presenter) {

		this.slotNumber = slotNumber;
		this.presenter = presenter;

		setWidth(35);
		setHeight(50);

		setBackgroundColor("#ffffff");
		if (this.slotNumber != -1) {
			setBackgroundImage("ui/cardSlotSet_Image.png");
		} else {
			setBackgroundImage("ui/cardSlotGiven_Image.png");
		}
		setBorder("1px solid #0E0");
		setOverflow(Overflow.HIDDEN);
		setCanAcceptDrop(true);
		setAnimateMembers(true);
		setDropLineThickness(0);

		addDropOverHandler(new DropOverHandler() {
			@Override
			public void onDropOver(DropOverEvent event) {
				if (willAcceptDrop()) {
					setBackgroundColor("#ffff80");
				}
			}
		});

		/*
		 * für Animation(Farbe)
		 */
		addDropOutHandler(new DropOutHandler() {
			@Override
			public void onDropOut(DropOutEvent event) {
				setBackgroundColor("#ffffff");
			}
		});

		/*
		 * Ereignissbehandlung, die beim Droppen eines Bildes passiert
		 */
		addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {

				if (!CardSlot.this.isFree()) {
					event.cancel();
					return;
				}

				final ProgrammingcardImg img = (ProgrammingcardImg) DraggedObject.getDragObject();
				
				if (CardSlot.this.slotNumber != -1) {
					/*
					 * Karte wurde hinzugefügt
					 */
					// Serveranfrage senden
					CardSlot.this.presenter.sendOneCardSet(
							CardSlot.this.slotNumber, 0);
					final int sN;
					if ((sN = img.getCardSlot().slotNumber) != -1) {
						CardSlot.this.presenter.sendOneCardSet(sN, 1);
					}
					
					/*
					 * Prüfen, ob readyButton aktiviert werden kann
					 */
				} else {
					/*
					 * Karte wurde entfernt aus den oberen Kartenslots
					 */
					CardSlot.this.presenter.sendOneCardSet(img.getCardSlot().slotNumber, 1);
				}

				/*
				 * den CardSlot neu setzen
				 */
				programmingcardImg = img;
				img.getCardSlot().programmingcardImg = null;
				img.setCardSlot(CardSlot.this);
				
				/*
				 * den ReadyButton enablen oder disablen
				 */
				((PlayerStatusArea)presenter.getPage().getRobotStatusArea()).getReadyButton().setDisabled(
						!((PlayerStatusArea)presenter.getPage().getRobotStatusArea()).allCardsSet());
			}
		});
	}

	/**
	 * Liefert den RoundPlayerPagePresenter
	 * 
	 * @return presenter
	 */
	public RoundPlayerPagePresenter getPresenter() {
		return this.presenter;
	}

	/**
	 * Liefert die Slotnummer
	 * 
	 * @return slotNumber
	 */
	public int getSlotNumber() {
		return this.slotNumber;
	}
	
	
	/**
	 * Entfernt das Programmierkartenbild aus dem Kartenslot
	 */
	public void removeProgrammingcardImg() {
		if(this.programmingcardImg != null) {
			this.removeMembers(this.getMembers());
			this.programmingcardImg = null;
		}
	}
	
	/**
	 * Setzt ein Programmierkartenbild in den Kartenslot
	 * @param programmingcardImg
	 */
	public void setProgrammingcardImg(ProgrammingcardImg programmingcardImg) {
		this.addMember(programmingcardImg);
		programmingcardImg.setCardSlot(this);
		this.programmingcardImg = programmingcardImg;
	}
	
	/**
	 * Liefert das Programmierkartenbild das sich auf diesem Kartenslot befindet
	 * @return
	 */
	public ProgrammingcardImg getProgrammingcardImg() {
		return this.programmingcardImg;
	}

	/**
	 * Gibt an, ob sich eine Programmierkarte im Kartenslot befindet
	 * 
	 * @return true, wenn sich keine Karte im Slot befindet false, wenn sich
	 *         eine Karte drin befindet
	 */
	public boolean isFree() {
		return this.programmingcardImg == null;
	}

	/**
	 * gibt an, ob der KartenSlot gesperrt ist.
	 * Wenn er gesperrt ist, können keine Karten gedropped oder gedragged werden
	 * @return
	 */
	public boolean isLocked() {
		return !getCanAcceptDrop();
	}

	/**
	 * deaktiviert den KartenSlot sodass er keine Karten mehr annehmen kann und 
	 */
	public void setIsLocked(boolean locked) {
		setCanAcceptDrop(!locked);
		if(this.getProgrammingcardImg() != null) {
			this.getProgrammingcardImg().setCanDragReposition(!locked);
		}
	}
}
