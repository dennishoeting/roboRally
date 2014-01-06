package prototyp.client.view.round;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Klasse für die BackUp-Karten
 * 
 * @author Marcus, Andreas
 * @version 1.0
 * @version 1.1 Umgestaltet und den Timer verändert
 * 
 */
public final class BackupWindow extends Window {
	/**
	 * Zeigt das BackUp-Window an, d.h. diese Klasse wird initialisiert.
	 * 
	 * @param parent
	 *            Vater-Canvas
	 * @param callback
	 *            Callback-Objekt
	 */
	public static void showBackupWindow(final Canvas parent,
			final BooleanCallback callback) {
		parent.addChild(new BackupWindow(callback));
	}

	/** Labels */
	private final StandardLabel labelQuestion, labelTimer;

	/** Buttons */
	private final Button buttonYes, buttonNo;

	/** Bild */
	private Img card;

	/** Callback -Objekt */
	private final BooleanCallback callback;

	/** der Timer */
	private final Timer timer;

	/**
	 * Konstruktor
	 * 
	 * @param callback
	 *            Callback - Objekt
	 */
	private BackupWindow(final BooleanCallback callback) {
		// Callback speichern
		this.callback = callback;

		setTitle(Page.props.backupwindow_title()); // Titel angegeben
		setHeaderControls(HeaderControls.HEADER_LABEL); // Schließen und
														// Minimieren ist dann
														// weg

		// Einzelne Objekte

		this.labelQuestion = new StandardLabel(
				Page.props.backupwindow_question());
		this.labelQuestion.setWrap(false);
		this.labelQuestion.setAlign(Alignment.CENTER);
		this.labelQuestion.setWidth(220);

		this.labelTimer = new StandardLabel();
		this.labelTimer.setAlign(Alignment.CENTER);
		this.labelTimer.setWidth(100);
		this.labelTimer.setHeight(30);
		this.labelTimer.setStyleName("timer");

		this.card = new Img("ui/restart_point_50x50.png");
		this.card.setWidth(50);
		this.card.setHeight(50);
		this.card.setAlign(Alignment.CENTER);

		this.buttonYes = new Button(Page.props.global_title_yes());
		this.buttonYes.setAlign(Alignment.CENTER);

		this.buttonNo = new Button(Page.props.global_title_no());
		this.buttonNo.setAlign(Alignment.CENTER);

		// Areas
		HorizontalStack objects = new HorizontalStack(
				Integer.valueOf(Page.props.global_marginInStackAreas()),
				Integer.valueOf(Page.props.global_paddingInStackAreas()));
		objects.setWidth(220);
		objects.setHeight(40);
		objects.setAlign(Alignment.CENTER);
		objects.setMembers(this.labelTimer, this.card);

		HorizontalStack buttons = new HorizontalStack(
				Integer.valueOf(Page.props.global_marginInStackAreas()),
				Integer.valueOf(Page.props.global_paddingInStackAreas()));
		buttons.setWidth(220);
		buttons.setAlign(Alignment.CENTER);
		buttons.setMembers(this.buttonYes, this.buttonNo);

		VerticalStack vStack = new VerticalStack(Integer.valueOf(Page.props
				.global_marginInStackAreas()), Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		vStack.setTop(20);
		vStack.setAlign(Alignment.CENTER);
		vStack.setMembers(this.labelQuestion, objects, buttons);

		// Hinzufügen
		this.addChild(vStack);

		// Eigenschaften des Windows
		this.setWidth(250);
		this.setHeight(180);
		this.setAutoCenter(true);
		setCanDrag(false);
		setCanDragReposition(false);

		// Listener hinzufügen
		addListener();

		/*
		 * Timer starten
		 */
		this.timer = new Timer() {
			/** Zeitangabe */
			private int i = 5;

			@Override
			public void run() {
				final String time = this.i >= 10 ? this.i + "" : "0" + this.i;
				BackupWindow.this.labelTimer.setContents("00:" + time);

				if (this.i == 0) {
					BackupWindow.this.destroy();
					callback.execute(false);
					cancel();
					return;
				}

				this.i -= 1;

				schedule(1000);
			}
		};
		this.timer.schedule(1);
	}

	/**
	 * Fügt die Listener hinzu (Muss eigentlich in einen Presenter)
	 */
	private void addListener() {
		// Nein
		this.buttonNo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				BackupWindow.this.timer.cancel();

				// Fenster löschen
				BackupWindow.this.destroy();

				// Callback senden
				BackupWindow.this.callback.execute(false);
			}
		});

		// Ja
		this.buttonYes.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				BackupWindow.this.timer.cancel();

				// Fenster löschen
				BackupWindow.this.destroy();

				// Callback senden
				BackupWindow.this.callback.execute(true);

			}
		});
	}
}
