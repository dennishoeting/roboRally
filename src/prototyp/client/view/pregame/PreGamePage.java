package prototyp.client.view.pregame;

import prototyp.client.presenter.pregame.PreGamePagePresenter;
import prototyp.client.util.Button;
import prototyp.client.view.Page;

/**
 * Definiert das Verhalten der Buttons etc.
 * 
 * @author Dennis, Jannik (Verantwortlicher), Mischa, Timo (Verantwortlicher)
 * @version 1.0
 * @version 1.1 Abbrechen-Button --Jannik
 * @version 1.2 (20.9.10) Preview Area komplettiert --Jannik
 * @version 1.3 2. Konstruktor gelöscht, wird nicht gebraucht 25.10.10(Mischa)
 * @version 1.4 Properties hinzugefügt (Marina)
 * @version 1.5 Konstruktur verändert 30.12.10 Mischa
 * 
 * @see PreGamePagePresenter
 */
public class PreGamePage extends PreGameAbstractPage {
	// Attribute
	private Button buttonReady;

	/**
	 * Konstruktor
	 */
	public PreGamePage() {
		super(Page.props.preGamePage_title());
		this.buttonReady = new Button(Page.props.global_title_buttonReady(),
				Integer.valueOf(Page.props.global_buttonWidth()));
		this.buttonArea.addMembers(this.buttonReady, this.buttonCloseTab);
		getPlayerOptionsArea().setHeight(246);
		getInternalChatArea().setHeight(246);
	}

	/**
	 * Liefert den Ready-Button
	 * 
	 * @return the buttonReady
	 */
	public Button getButtonReady() {
		return this.buttonReady;
	}

}
