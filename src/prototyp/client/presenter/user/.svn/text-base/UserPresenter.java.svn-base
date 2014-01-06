package prototyp.client.presenter.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prototyp.client.service.LobbyService;
import prototyp.client.service.LobbyServiceAsync;
import prototyp.client.service.RoundManagerService;
import prototyp.client.service.RoundManagerServiceAsync;
import prototyp.client.service.UserService;
import prototyp.client.service.UserServiceAsync;
import prototyp.shared.useradministration.User;
import prototyp.shared.util.events.user.UserEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * Presenter für das User Model des jeweiligen Benutzers Erstellt eine Instanz mit einem User und kann auch den jeweiligen DB
 * Eintrag ändern.
 * 
 * @author Timo (Verantwortlicher)
 * @version 1.0
 * @version 1.1 Hier ist jetzt der ClosingHandler drin
 * @version 1.2 Gibt jetzt jede Minute ein Signal an den Server, dass der User noch da ist.
 */
public class UserPresenter {

	/** die UserPresenter-Instanz */
	private static UserPresenter userPresenter = null;

	/**
	 * Wenn der UserPresenter schon einmalig erzeugt wurde kann er mittels dieser Methode statisch geholt werden.
	 * 
	 * @return userPresenter
	 */
	public static UserPresenter getInstance() {
		return UserPresenter.userPresenter;
	}

	/**
	 * Erstellt einen neuen UserPresenter
	 * 
	 * @param user
	 *            Eingeloggte User
	 */
	public static void putUser(User user) {
		UserPresenter.userPresenter = new UserPresenter(user);
	}

	/** Alle Chat Domains, für die Nachricht beim Verlassen der Seite. */
	private List<Domain> chatServiceDomains = new ArrayList<Domain>();

	/** Liste mit allen aktiven RoundIDs des Users */
	private List<Integer> roundIDs = new ArrayList<Integer>();

	/** RemoteEventService */
	private RemoteEventService theEventService = RemoteEventServiceFactory.getInstance().getRemoteEventService();

	/** Attribute */
	private User user;

	/** Async-Objekt */
	private final UserServiceAsync userService = GWT.create(UserService.class);

	/** RoundManagerService */
	private final RoundManagerServiceAsync roundManagerService = GWT.create(RoundManagerService.class);

	/** LobbyService */
	private final LobbyServiceAsync lobbyService = GWT.create(LobbyService.class);

	/**
	 * Enthält alle Aktiven RemoteEventListener für den Unlistener Listener wichtig
	 */
	private Map<String, RemoteEventListener> eventListeners;

	/**
	 * Konstruktor im SingleTon-Pattern.
	 * 
	 * @param user
	 *            Eingeloggte User
	 */
	private UserPresenter(User user) {
		this.user = user;
		UserPresenter.userPresenter = this;

		this.eventListeners = new HashMap<String, RemoteEventListener>();

		// Nach dem LogIn eine ChatNachricht verfassen
		this.userService.sendChatLogIn(user.getAccountData().getNickname(), new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				// Dann ist auch nicht schlimm ;)
			}

			@Override
			public void onSuccess(Boolean result) {
				// Brauch' nichts passieren
			}
		});

		/*
		 * Wenn der Browser geschlossen wird oder die Seite verlassen wird
		 */
		Window.addWindowClosingHandler(new ClosingHandler() {
			@Override
			public void onWindowClosing(ClosingEvent event) {
				onClosing();
			}
		});

		// Alle 60sek ein Lebenszeichnen geben
		giveAliveSignal();
		
		addListeners();
	}

	/**
	 * Wird immer aufgerufen, wenn ein Chat erstellt wird.
	 * 
	 * @param domain
	 *            Neue Domain
	 * @return true, falls alles geklappt hat
	 */
	public boolean addNewChatServiceDomain(Domain domain) {
		this.chatServiceDomains.add(domain);
		return true;
	}

	/**
	 * Fügt einen RemoteEventListener hinzu. Falls schon einer zu der Domain existiert, wird dieser ersetzt.
	 * 
	 * @param domain
	 * @param listener
	 * @return
	 */
	public boolean addRemoteEventListener(String domain, RemoteEventListener listener) {
		this.eventListeners.put(domain, listener);
		return true;
	}

	/**
	 * Fügt eine neue aktive Round(ID) hinzu.
	 * 
	 * @param round
	 *            ID, der neuen aktiven Runde
	 * @return true, falls es geklappt hat.
	 */
	public boolean addRound(int roundID) {
		return this.roundIDs.add(roundID);
	}

	/**
	 * Löscht eine ChatServiceDomain wieder. Sollte gemacht werden, wenn ein Chat nicht mehr existiert.
	 * 
	 * @param domain
	 *            Domain, die gelöscht werden soll
	 * @return true, falls alles geklappt hat.
	 */
	public boolean deleteChatServiceDomain(Domain domain) {
		return this.chatServiceDomains.remove(domain);
	}

	/**
	 * Löscht alle angemeldeten Listener der RemoteEventServices (sollte beim ausloggen aufgerufen werden)
	 * 
	 * @return true, falls alles geklappt hat.
	 */
	public boolean deleteRemoteEventServiceListeners() {
		this.theEventService.removeListeners();
		return true;
	}

	/**
	 * Löscht eine (nicht mehr aktive) RoundID.
	 * 
	 * @param round
	 *            ID der Spielrunde, die gelöscht werden soll
	 * @return true, falls alles geklappt hat.
	 */
	public boolean deleteRound(int roundID) {
		return this.roundIDs.remove(this.roundIDs.indexOf(roundID)) != null;
	}

	/**
	 * Liefert alle RoundIDs, in denen der User/Player/Watcher sich befindet.
	 * 
	 * @return ArrayList mit den aktiven Spielrunden des Spielers
	 */
	public List<Integer> getActiveRounds() {
		return this.roundIDs;
	}

	/**
	 * Liefert den Nickname des Users
	 * 
	 * @return nickname
	 */
	public String getNickname() {
		return this.user.getAccountData().getNickname();
	}

	/**
	 * Liefert das Userobjekt
	 * 
	 * @return user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Liefert den UserService
	 * 
	 * @return userService
	 */
	public UserServiceAsync getUserService() {
		return this.userService;
	}

	/**
	 * Gibt dem Server jede Minute ein Lebenszeichen.
	 * 
	 * @return
	 */
	private void giveAliveSignal() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				UserPresenter.this.lobbyService.giveAliveSignal(UserPresenter.this.user.getId(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// Seite neuladen -> Frontpage
						Window.Location.reload();
					}

					@Override
					public void onSuccess(Boolean result) {
						// Wieder aufrufen. Damit die Zeit besser
						// hinkommt nicht im onSuccess.
						UserPresenter.this.giveAliveSignal();
					}

				});
			}

		};
		// Alle 30sek eine Server Anfrage stellen.
		timer.schedule(30000);
	}
	
	/**
	 * Fügt die Listener hinzu
	 * @return
	 */
	private boolean addListeners() {
		
		theEventService.addListener(DomainFactory.getDomain("user:" +user.getId()), new RemoteEventListener() {
			
			@Override
			public void apply(Event anEvent) {
				//Nur ausführen wenn es ein UserEvent ist
				if(anEvent instanceof UserEvent) {
					((UserEvent)anEvent).apply();
				}
			}
		});
		
		return true;
	}
	

	/**
	 * Überprüft, ob es sich um einen Administrator handelt.
	 * 
	 * @return true, wenn es sich um einen Administrator handelt.
	 */
	public boolean isAdmin() {
		return this.user.isAdmin();
	}

	/**
	 * Alles was passieren soll wenn der Browser geschlossen wird oder die Seite verlassen wird, kommt hier rein.
	 * 
	 * @return true
	 */
	public boolean onClosing() {
		// Alle EventServiceListener löschen
		deleteRemoteEventServiceListeners();

		// Alles was Serverseitig noch passieren soll:
		this.userService.onClosing(this.user, this.chatServiceDomains, this.roundIDs, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				// Interessiert niemanden mehr :D
			}

			@Override
			public void onSuccess(Boolean result) {
				// Nichts: Der Browser ist geschlossen oder die Seite
				// ist verlassen.
			}
		});

		// Aus allen Rounds löschen und erstellte Rounds löschen
		this.roundManagerService.removeFromAllRounds(this.roundIDs, this.user, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// Interessiert niemanden mehr :D
			}

			@Override
			public void onSuccess(Boolean result) {
				// Interessiert niemanden mehr :D
			}

		});

		return true;
	}

	/**
	 * Löscht einen RemoteEventListener aus der Liste.
	 * 
	 * @param domain
	 * @return true, wenn der RemoteEventListener gelöscht wurde
	 */
	public boolean removeRemoteEventListener(String domain) {
		return this.eventListeners.remove(domain) != null;
	}

	/**
	 * Setzt ein neues Userobjekt
	 * 
	 * @param user
	 *            Neues Userobjekt
	 * @return true
	 */
	public boolean setUser(User user) {
		this.user = user;
		return true;
	}
}
