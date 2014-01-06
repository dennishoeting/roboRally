package prototyp.client.presenter.chat;

import prototyp.client.model.easterEggs.DavidGuetta;
import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.ChatService;
import prototyp.client.service.ChatServiceAsync;
import prototyp.client.util.SoundManager;
import prototyp.client.view.Page;
import prototyp.client.view.chat.ChatArea;
import prototyp.shared.util.events.chat.ChatEvent;
import prototyp.shared.util.events.chat.PrivateChatEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * Presenter für den Chat Es muss eine Domain angeben werden!
 * 
 * @author timo
 * @version 1.0
 * @version 1.1 übergibt die domains jetzt an den UserPresenter damit eine Abschlussnachricht verfasst werden kann, diese können
 *          am Ende auch wieder gelöscht werden.
 * @version 1.2 Sound hinzugefügt (Andreas)
 */
public class ChatPresenter implements PagePresenter {
	/** Konkrete Domain */
	private final Domain concreteDomain;

	/** Chat-Domain */
	private final String domain;

	/** Zugehörige Area */
	private final ChatArea page;

	/** Chat-Service */
	private final ChatServiceAsync serviceProxy;

	/** EventService */
	private final RemoteEventService theEventService;

	/** Abstrakte Factory */
	private final RemoteEventServiceFactory theEventServiceFactory;

	/** SoundManager */
	private final SoundManager soundManager;

	/**
	 * Konstruktor
	 * 
	 * @param domain
	 *            Domain für die Events
	 */
	public ChatPresenter(String domain) {
		this.domain = domain;

		// SoundManager erstellen
		this.soundManager = new SoundManager();

		// Konkrete Domain erstellen
		this.concreteDomain = DomainFactory.getDomain(domain);

		// Domain an den UserPresenter übergeben
		UserPresenter.getInstance().addNewChatServiceDomain(this.concreteDomain);

		// Service
		this.serviceProxy = GWT.create(ChatService.class);

		/*
		 * Einen EventService mittels Factory holen
		 */
		this.theEventServiceFactory = RemoteEventServiceFactory.getInstance();
		this.theEventService = this.theEventServiceFactory.getRemoteEventService();
		this.page = new ChatArea();

		// Maximale Anzahl an Zeichen
		this.page.getMessageField().setLength(55);

		// Listeners
		addListeners();
	}

	/**
	 * Schickt eine Nachricht, dass das PostGame geschlossen wurde.
	 * 
	 * @param nickname
	 *            Nickname des Spielers
	 * @param domain
	 *            Chatdomain
	 * @return true
	 */
	public boolean sendLeavePostGameMessage(String nickname, String domain) {
		serviceProxy.sendPostGameLeaveMessage(nickname, domain, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// interessiert niemanden -timo
			}

			@Override
			public void onSuccess(Boolean result) {
				// interessiert auch niemanden -timo
			}

		});

		return true;
	}

	/**
	 * Schickt eine Nachricht, dass das PostGame betreten wurde.
	 * 
	 * @param nickname
	 *            Nickname des Spielers
	 * @param domain
	 *            Chatdomain
	 * @return true
	 */
	public boolean sendEnterPostGameMessage(String nickname, String domain) {
		serviceProxy.sendPostGameEnterMessage(nickname, domain, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// interessiert niemanden -Andreas
			}

			@Override
			public void onSuccess(Boolean result) {
				// interessiert auch niemanden -Andreas
			}

		});

		return true;
	}

	/**
	 * Zeigt eine Nachricht an, die nur für sich angezeigt wird. Wird z.B. bei der Runde genommen, damit beim Betreten der
	 * Spielrunde ganz nach unten gescrollt wird.
	 * 
	 * @param message
	 *            Chatnachricht
	 * @return true, wenn die Nachricht korrekt angezeigt wird.
	 */
	public boolean sendSelfMessage(String message) {
		// Nachricht anzeigen
		return this.getMessage("<strong><font color='#ff69b4'>" + Page.props.global_title_game() + ": " + message
				+ " </font></strong>", null);
	}

	/**
	 * Gibt die Domain zurück
	 * 
	 * @return domain
	 */
	public String getDomain() {
		return this.domain;
	}

	/**
	 * Listener hinzufügen
	 * 
	 * @return true
	 */
	private boolean addListeners() {
		// Zwei Listener zum Absenden der Nachricht:

		// Zum Klicken
		this.page.getButtonSendMessage().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if (ChatPresenter.this.page.getMessageField().getValue() != null) {
					ChatPresenter.this.sendMessage();
				}
			}

		});

		// Wenn Enter gedrückt wird
		this.page.getMessageField().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName() != null && event.getKeyName().equals("Enter")
						&& ChatPresenter.this.page.getMessageField().getValue() != null) {
					ChatPresenter.this.sendMessage();
				}
			}

		});

		// Listener um EventService mit Domain zu registrieren
		this.theEventService.addListener(this.concreteDomain, new RemoteEventListener() {
			@Override
			public void apply(Event anEvent) {
				if (anEvent instanceof ChatEvent) {
					ChatEvent event = (ChatEvent) anEvent;
					// Nachricht hinzufügen
					ChatPresenter.this.getMessage(event.getMessage(), event.getSound());

				} else {
					// Private Nachricht
					if (anEvent instanceof PrivateChatEvent && // Darf sie gelesen werden?
							(((PrivateChatEvent) anEvent).getFrom().equals(UserPresenter.getInstance().getNickname()) || ((PrivateChatEvent) anEvent)
									.getTo().equals(UserPresenter.getInstance().getNickname()))) {

						// Nachricht hinzufügen
						ChatPresenter.this.getMessage(((PrivateChatEvent) anEvent).getMessage(), null);

					}
				}

			}

		});

		return true;
	}

	/**
	 * Löscht den EventListener für den ChatPresenter sollte nach Abschluss der Round gemacht werden. Außerdem wird die Domain aus
	 * dem UserPresenter entfernt.
	 * 
	 * @return true
	 */
	public boolean deleteEventListener() {
		this.theEventService.removeListeners(this.concreteDomain);
		UserPresenter.getInstance().deleteChatServiceDomain(this.concreteDomain);
		return true;
	}

	/**
	 * Fügt eine Neue Nachricht hinzu
	 * 
	 * @param message
	 *            Nachricht
	 * @return true
	 */
	private boolean getMessage(String message, String sound) {
		// Alte Nachrichten holen
		String tmpText = ChatPresenter.this.page.getChatTextArea().getContents();

		// Neue Nachricht hinzufügen

		// Falls es schon Nachrichten gibt
		if (!tmpText.equals("")) {
			// Das letzte <br /> löschen
			tmpText = tmpText.substring(0, tmpText.length() - 6);
		}

		// ein <br /> zu viel hinzufügen -> damit immer die letzte Zeile
		// sichtbar ist
		tmpText += message + "<br /><br />";

		// Text in die TextArea setzen
		ChatPresenter.this.page.getChatTextArea().setContents(tmpText);
		ChatPresenter.this.page.getChatTextArea().scrollToBottom();

		// Sound abspielen
		if (sound != null && !sound.equals("")) {
			this.soundManager.play(sound);
		}

		return true;
	}

	/**
	 * Liefert die ChatArea
	 * 
	 * @return ChatArea
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Sendet eine Nachricht
	 */
	private void sendMessage() {
		if (ChatPresenter.this.page.getMessageField().getValue() != null) {
			// Message nach HTML Tags absuchen und ersetzen
			String mes = ChatPresenter.this.page.getMessageField().getValue().toString();
			mes = mes.replace("<", "&lt;");
			mes = mes.replace(">", "&gt;");
			mes = mes.replace("ü", "&uuml;");
			mes = mes.replace("Ü", "&Uuml;");
			mes = mes.replace("ß", "&szlig;");
			mes = mes.replace("ö", "&ouml;");
			mes = mes.replace("Ö", "&Ouml;");
			mes = mes.replace("ä", "&auml;");
			mes = mes.replace("Ä", "&Auml;");
			mes = mes.replace("\"", "&#34;");

			if (domain.equals("global-chat") && mes.equals("getDavid();")) {
				if (!DavidGuetta.isRunning()) {
					DavidGuetta.doIt(ChatPresenter.this.serviceProxy, ChatPresenter.this.domain,
							ChatPresenter.this.page.getChatTextArea(), UserPresenter.getInstance().getNickname());
				} else {
					ChatPresenter.this.serviceProxy.sendMessage("m0wl", UserPresenter.getInstance().getNickname(),
							ChatPresenter.this.domain, new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) { /* nix */
								}

								@Override
								public void onSuccess(Boolean result) { /* nix */
								}
							});
				}
			} else {
				mes = mes.trim();
				// Private Nachricht
				if (mes.charAt(0) == '@' && mes.contains(" ")) {
					// Empfänger ermitteln
					String to = mes.substring(1, mes.indexOf(" "));
					// Nachricht überarbeiten
					mes = mes.substring(mes.indexOf(" "));

					ChatPresenter.this.serviceProxy.sendPrivateMessage(mes, to, UserPresenter.getInstance().getNickname(),
							ChatPresenter.this.domain, new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {
									// Nichts
								}

								@Override
								public void onSuccess(Boolean result) {
									// Nichts
								}
							});

				} else { // Keine Private Nachricht
					ChatPresenter.this.serviceProxy.sendMessage(mes, UserPresenter.getInstance().getNickname(),
							ChatPresenter.this.domain, new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) { /* nix */
								}

								@Override
								public void onSuccess(Boolean result) { /* nix */
								}
							});
				}
			}
			ChatPresenter.this.page.getMessageField().clearValue();
		}
	}
}
