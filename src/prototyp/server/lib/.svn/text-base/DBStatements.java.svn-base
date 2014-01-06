package prototyp.server.lib;

/**
 * Hilfsklasse, die alle SQL-Statements als Enum beinhaltet
 * 
 * @author Andreas Rehfeldt
 * @version 1.13 (23.09.2010)
 * @version 1.14 (30.09.2010, Award-Statements, Robert)
 * 
 * @see DBConnection
 */
public enum DBStatements {
	/*
	 * 1. Account 2. Statistik 3. Login-Logout 4. HP-Inhalte
	 */

	// 3. Login-Logout Statements
	/**
	 * Liefert alle UserIDs zur übergebenen Email. Parameter für das Statement ist (1) email.
	 */
	CHECK_EMAIL_CONSTRAINT("SELECT userID FROM game_user WHERE email = ?"),

	/**
	 * Liefert die letzte Zeit, wann der Nutzer ein neues Passwort angefordert hat. Parameter: (1) email
	 */
	CHECK_NEWPASSWORDTIME(
			"SELECT sendNewPasswordTime FROM game_user WHERE email = ? AND (CURDATE() > sendNewPasswordTime OR sendNewPasswordTime IS NULL)"),

	/**
	 * Liefert alle UserIDs zum übergebenen Nickname. Parameter für das Statement ist (1) nickname.
	 */
	CHECK_NICKNAME_CONSTRAINT("SELECT userID FROM game_user WHERE nickname = ?"),

	/**
	 * Fügt eine neuen Award in game_award für einen User hinzu. Parameter sind (1) userID, (2) awardType
	 */
	INSERT_NEW_ARCHIEVED_AWARD("INSERT INTO game_award (userID, awardType) VALUES (?,?)"),

	/**
	 * Fügt eine neue Statistic für einen Nutzer in die game_statistic_lastrounds Tabelle ein. Parameter ist (1) userID.
	 */
	INSERT_NEW_LASTROUNDS("INSERT INTO game_statistic_lastrounds (userID) VALUES (?)"),

	/**
	 * Fügt in die Datenbank ein neues PlayingBoard ein. Parameter: name, description, width, height, numberOfCheckpoints,
	 * maxPlayers, imageFileName
	 */
	// INSERT_NEW_PLAYINGBOARD("INSERT INTO playingboard (name, description, width, height, numberOfCheckpoints, maxPlayers, imageFileName) VALUES (?, ?, ?, ?, ?, ?, ?)"),
	INSERT_NEW_PLAYINGBOARD(
			"INSERT INTO game_playingboard (name, description, width, height, numberOfCheckpoints, maxPlayers, imageFileName, userID, difficulty, numberOfConveyorBelts, numberOfCompactors, numberOfPushers, numberOfGears, numberOfLaserCannons)" +
			" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"),

	SELECT_USERID_FOR_PLAYINGBOARD("SELECT userID FROM game_playingboard WHERE name = ?"),
			
	/**
	 * Fügt eine neue Statistic für einen Nutzer in die game_statistic Tabelle ein. Parameter ist (1) userID.
	 */
	INSERT_NEW_STATISTIC("INSERT INTO game_statistic (userID, wins, abortedRounds, playedRounds) VALUES (?, 0, 0, 0)"),

	/**
	 * Fügt einen neuen User in die game_statistic_award ein. Parameter ist (1) userID.
	 */
	INSERT_NEW_STATISTIC_AWARD("INSERT INTO game_statistic_award (userID) VALUES (?)"),

	// 1.2 Nach-Registration-Statements; die direkt nach der Registration
	// ausgeführt werden
	/**
	 * Fügt einen neuen User in die game_user Tabelle ein. Parameter ist (1) nickname, (2) email, (3) password, (4) firstname, (5)
	 * surname.
	 */
	INSERT_NEW_USER("INSERT INTO game_user (nickname, email, password, firstname, surname) VALUES (?, ?, ?, ?, ?)"),

	/**
	 * Löscht ein Spielbrett aus der datenbank
	 * Als Variable(1) ist nur die Spielbrett-Id anzugeben
	 */
	DELETE_PLAYINGBOARD("DELETE FROM game_playingboard WHERE playingBoardID = ?"),
	
	/**
	 * Sperrt einen Nutzer. Parameter für das Statement ist (1) userID.
	 */
	LOCK_USER("UPDATE game_user SET locked = 1 WHERE userID = ?"),

	/**
	 * Liefert die Anzahl der User. Keine Parameter sind vorhanden.
	 */
	NUMBER_OF_USERS("SELECT COUNT(*) FROM game_user"),

	/**
	 * Liefert zu einer UserID alle Accountdaten: nickname, email,firstname,surname,locked, isAdmin, password. Parameter für das
	 * Statement ist (1) userID.
	 */
	SELECT_ACCOUNTDATA_TO_EDIT("SELECT nickname,email,firstname,surname,locked,isAdmin, password FROM game_user WHERE userID = ?"),

	/**
	 * Liefert zu einem Nickname: userID, email, firstname, surname, locked, isAdmin, password. Parameter ist (1) nickname.
	 */
	SELECT_ACCOUNTDATA_TO_EDIT_BY_NICKNAME(
			"SELECT userID, email, firstname, surname, locked, isAdmin, password FROM game_user WHERE nickname = ?"),

	// 1. Relevante Account Statements
	// 1.1 Vergleichs-Statements; Werden bei Accountänderungen oder bei der
	// Registrierung verwendet
	/**
	 * Liefert alle Nicknames und Email Adressen, die schon von anderen User verwendet werden. Parameter: (1) nickname, (2) email,
	 * (3) userID.
	 */
	SELECT_CHECK_UNIQUE_NICK_AND_MAIL("SELECT nickname, email FROM game_user WHERE (nickname = ? or email = ?) AND userID != ?"),

	/**
	 * Liefert eine gesuchte e-Mail-Adresse und den Gesperrt-Status des Nutzers. Parameter für das Statement ist (1) email.
	 */
	SELECT_EMAIL_AND_LOCKED_BY_EMAIL("SELECT email, locked FROM game_user WHERE email = ?"),

	/**
	 * Liefert eine gesuchte e-Mail-Adresse. Parameter für das Statement ist (1) email.
	 */
	SELECT_EMAIL_BY_EMAIL("SELECT email FROM game_user WHERE email = ?"),

	/**
	 * Liefert eine gesuchte e-Mail-Adresse von einem User, der nicht gesperrt ist. Parameter für das Statement ist (1) email.
	 */
	SELECT_EMAIL_UNLOCKED_BY_EMAIL("SELECT email FROM game_user WHERE email = ? AND locked = 0"),

	/**
	 * Liefert das locked Kennzeichen des zugehörigen Nicknamen. Parameter für das Statement ist (1) nickname.
	 */
	SELECT_LOCKED_BY_NICKNAME("SELECT locked FROM game_user WHERE nickname = ?"),

	/**
	 * Selektiert alle Daten, die zum einloggen benötigt werden. Parameter: (1) nickname
	 */
	SELECT_LOGIN_DATA(
			"SELECT userID, firstname, locked, failedLogInCount, AddTime(failedLogInTime, '00:15:00') failedLogInTime, password from game_user where nickname = ?"),

	/**
	 * Liefert den Nicknamen einer zugehörigen E-Mail Adresse. Parameter für das Statement ist (1) email.
	 */
	SELECT_NICKNAME_AND_USERID_BY_EMAIL("SELECT nickname, userID FROM game_user WHERE email = ?"),

	/**
	 * Liefert den Nicknamen und überprüft das Passwort zu einem zugehörigen Nutzer. Parameter für das Statement ist (1) password
	 * und (2) userID.
	 */
	SELECT_NICKNAME_BY_PASSWORD("SELECT nickname FROM game_user WHERE password = ? AND userID = ?"),

	/**
	 * Liefert das Passwort von einem zugehörigen Nicknamen. Parameter für das Statement ist (1) nickname.
	 */
	SELECT_PASSWORD_BY_NICKNAME("SELECT password,userID FROM game_user WHERE nickname = ?"),

	/**
	 * Liefert ein PlayingBoard. Als Parameter ist nur der Name anzugeben. Außerdem die Userdaten des PlayingBoard Erstellers.
	 * Liefert: playingBoardID ,name,description,width,height,difficulty,numberOfCheckpoints ,maxPlayers,imageFileName,userID,
	 * nickname
	 */
	SELECT_PLAYINGBOARD(
			"SELECT playingBoardID,name,description,width,height,difficulty,numberOfCheckpoints,maxPlayers,imageFileName,userID, nickname FROM game_playingboard NATURAL JOIN game_user WHERE name = ?"),

	/**
	 * Liefert alle Spielbretter. Sie werden nach der maximalen Anzahl der Spieler sortiert. Es sind keine weiteren Parameter
	 * gebraucht. Außerdem die User Daten des Erstellers.
	 */
	SELECT_PLAYINGBOARDS(
			"SELECT playingBoardID,name,description,width,height,difficulty,numberOfCheckpoints,maxPlayers,imageFileName,userID, nickname, numberOfConveyorBelts, numberOfCompactors, numberOfPushers, numberOfGears, numberOfLaserCannons FROM game_playingboard NATURAL JOIN game_user ORDER BY maxPlayers,difficulty ASC"),

			/**
			 * Liefert alle Spielbretter einer bestimmten UserID. Sie werden nach der maximalen Anzahl der Spieler sortiert. Es sind keine weiteren Parameter
			 * gebraucht. Außerdem die User Daten des Erstellers.
			 */
			SELECT_MY_PLAYINGBOARDS(
					"SELECT playingBoardID,name,description,width,height,difficulty,numberOfCheckpoints,maxPlayers,imageFileName,userID, nickname, numberOfConveyorBelts, numberOfCompactors, numberOfPushers, numberOfGears, numberOfLaserCannons FROM game_playingboard NATURAL JOIN game_user WHERE userID = ? ORDER BY maxPlayers,difficulty ASC"),		
			
	/**
	 * Liefert zu einer AwardID einen Award, der seinen Namen, die Beschreibung und seinen ImagePfad beinhaltet. Parameter (1) ist
	 * die AwardID.
	 */
	SELECT_SINGLE_AWARD("SELECT name, description, imageFileName FROM game_awardtype WHERE awardTypeID = ?"),

	/**
	 * Liefert zu einer UserID alle höchsten Profil-Awards sowie deren Name, Beschreibung und Pfad. Parameter für das Statement
	 * ist (1) userID.
	 */
	SELECT_USER_STATISTICAWARDS(
			"SELECT g.awardTypeID, g.name, g.description, g.imageFileName FROM game_statistic_awardtype as g"
				+ " WHERE g.awardTypeID IN (SELECT MAX(atype.awardTypeID) as statisticAwardID FROM game_statistic_award "
				+ " as e JOIN game_statistic_awardtype as atype ON e.statisticAwardID = atype.awardTypeID WHERE e.userID = ? GROUP BY atype.grouptype)"),

	/**
	 * Liefert alle User mit ihren IDs, Nicknames, Vorname, Nachname, E-Mailadresse und Sperrstatus. Keine weiteren Parameter
	 * werden benötigt.
	 */
	SELECT_USERS_TO_EDIT("SELECT userID,nickname,firstname, surname,email,locked FROM game_user ORDER BY nickname ASC"),

	// 4. HP-Inhalt Statements
	/**
	 * Liefert alle User mit ihren dazugehörigen Statistiken sortiert nach wins. Es sind keine weiteren Parameter notwendig. Es
	 * werden folgende Spalten zurückgeliefert: userID, locked, isAdmin, nickname, email, firstname, surname, wins, playedRounds,
	 * abortedRounds, points, roundName1, roundName2, roundName3, rankname
	 */
	SELECT_USERS_WITH_STATISTIC(
			"SELECT a.userID, a.locked, a.isAdmin, a.nickname, a.email, a.firstname, a.surname, b.wins, b.playedRounds, b.abortedRounds, b.points,"
					+ " (SELECT name FROM game_rank WHERE b.points>=pointsFrom AND b.points<=pointTo) AS rankname"
					+ " FROM game_user AS a NATURAL JOIN game_statistic AS b NATURAL JOIN game_statistic_lastrounds AS c"
					+ " ORDER BY b.points DESC"),

	/**
	 * Entsperrt einen Nutzer. Parameter für das Statement ist (1) userID.
	 */
	UNLOCK_USER("UPDATE game_user SET locked = 0 WHERE userID = ?"),

	/**
	 * Ändert die Accountdaten eines Nutzers. Parameter für das Statement sind (1) nickname, (2) email, (3) firstname, (4) surname
	 * und (5) userID.
	 */
	UPDATE_ACCOUNTDATA("UPDATE game_user SET nickname = ?,email = ?,firstname = ?,surname = ? WHERE userID = ?"),

	/**
	 * Erhöht die Anzahl der Fehlversuche Parameter: (1) userID
	 */
	UPDATE_FAILED_LOGIN_COUNT("UPDATE game_user SET failedLogInCount = ?, failedLogInTime = sysdate() where userID = ?"),

	/**
	 * Aktualisiert game_statistic für einen User. Parameter sind (1) = 1 falls Spiel gewonnen, sonst 0; (2)=Anzahl der im Spiel
	 * erworbenen Punkte; (3) für die userID
	 */
	UPDATE_GAME_STATISTIC("UPDATE game_statistic SET wins=wins+?, playedRounds=playedRounds+1, points=points+? WHERE userID=?"),

	/**
	 * Erhöht die game_statistic für einen user um 1 abgebrochenes Spiel; Parameter: (1) UserID
	 */
	UPDATE_GAME_STATISTIC_ABORTED_ROUNDS("UPDATE game_statistic SET playedRounds=playedRounds+1, abortedRounds=abortedRounds+1 WHERE userID=?"),

	/**
	 * Ermittelt die Anzahl der erreichten Awards eines Users. Parameter (1) userID, (2) awardType
	 */
	SELECT_AWARD_COUNT("select count(*) from game_award where userID = ? and awardType = ?"),

	/**
	 * Liefert den erreichten Statistic Award; Parameter (1) grouptype, (2) anzahl der erhaltenden Awards
	 */
	SELECT_STATISTIC_AWARD4USER(
			"select awardtypeID from game_statistic_awardtype a where a.grouptype = ? and a.value <= ? order by value desc LIMIT 1"),

	/**
	 * Prüft ob der user den Award bereits erhalten hat; Parameter (1) userID, (2) statisticAwardID
	 */
	SELECT_REACHED_AWARD_FOR_USER(
			"select g.statisticAwardID from game_statistic_award g where g.userID = ? and g.statisticAwardID = ?"),

	/**
	 * Fügt einen neuen game_statistic_award für einen Benutzer ein: Parameter (1) userID, (2) statisticAward
	 */
	INSERT_NEW_GAME_STATIC_AWARD("INSERT INTO game_statistic_award (userID, statisticAwardID) VALUES (?,?)"),

	// // 2.2 Award Statements
	// /**
	// * Aktualisiert die game_statistic_award, wird angewendet falls nach
	// * CHECK_GAME_AWARD für einen User ein neuer Award freizuschalten ist.
	// * Parameter sind (1) für den Award- type, (2) für die AwardTypeID und (3)
	// * für die userID
	// */
	// UPDATE_GAME_STATISTIC_AWARD("UPDATE game_statistic_award SET ? = ? WHERE userID = ?"),

	/**
	 * Setzt das Datum der letzten Passwortänderung Parameter: (1) email
	 */
	UPDATE_NEWPASSWORDTIME("UPDATE game_user set sendNewPasswordTime = CURDATE() WHERE email = ?"),

	/**
	 * Ändert das Passwort eines Nutzers. Parameter für das Statement ist (1) password, (2) userID.
	 */
	UPDATE_PASSWORD_BY_USERID("UPDATE game_user SET password = ? WHERE userID = ?"),

	// 1.3 AccountänderungsStatements
	/**
	 * Aktualisiert die Profildaten eines Users. Parameter: (1) firstname, (2) surname, (3) email, (4) nickname, (5) userID.
	 */
	UPDATE_PROFILE_DATA("UPDATE game_user SET firstname = ?, surname = ?, email = ?, nickname = ? WHERE userID = ?"),

	/**
	 * Setzt die fehlerhaften LogIn-Versuche zurück Parameter: (1) userID
	 */
	UPDATE_RESET_FAILED_LOGIN_COUNT("UPDATE game_user set failedLogInCount = 0, failedLogInTime = sysDate() where userID = ?"),

	/**
	 * Aktualisiert das Password eines Users. Parameter: (1) das verschlüsselte Password, (2) userID.
	 */
	UPDATE_USER_PASSWORD("UPDATE game_user SET password = ? WHERE userID = ?"),

	/**
	 * Updatet ein Playingboard. Parameter: description, width, height, numberOfCheckpoints, maxPlayers, imageFileName, name
	 */
	UPDATE_PLAYINGBOARD(
			"UPDATE game_playingboard set description = ?, width = ?, height = ?, numberOfCheckpoints = ?, maxPlayers = ?, imageFileName = ?, lastChangedUserID = ?, difficulty = ?, numberOfConveyorBelts = ?" +
			" ,numberOfCompactors = ?, numberOfPushers = ?, numberOfGears = ?, numberOfLaserCannons = ?"+
			" WHERE name = ?"),

	/**
	 * Aktualisiert die zuletzt gespielten Runden eines Users Parameter: (1) roundID, (2) userID
	 * 
	 * Achtung: Round muss vorher existieren, sonst FK Verletzung!
	 */
	UPDATE_LAST_ROUNDS("update game_statistic_lastrounds set round3 = round2, round2 = round1, round1 = ? where userID = ?"),

	/**
	 * Liefert die zuletzt eingefügte ID
	 */
	SELECT_LAST_ROUND_ID("select LAST_INSERT_ID() from game_round"),

	/**
	 * Fügt die Runde in die Tabelle game_round ein. Parameter: (1)`playingboardID`, (2)`name`, (3)`startingTime`,
	 * (4)`numberOfPlayers`, (5)`robotsShots`, (6)`presserOn`, (7)`pusherOn`, (8)`laserOn`
	 * 
	 */
	INSERT_ROUND(

			"INSERT INTO game_round (`playingboardID`,`name`,`startingTime`,`numberOfPlayers`,`robotsShots`,`presserOn`,`pusherOn`,`laserOn`) VALUES (?,?,?,?,?,?,?,?)"),

	/**
	 * Gibt die 3 zuletzt gespielten Map-Namen eines Useres zurück Parameter: (1) userID, (2) userID, (3) userID
	 */
	SELECT_LASTROUNDS(
			"(SELECT e.round1, f.name FROM game_round d , game_statistic_lastrounds e, game_playingboard f WHERE e.round1 = d.roundID AND d.playingboardID = f.playingBoardID"
					+ " and e.userID = ?) UNION (SELECT e.round2, f.name FROM game_round d , game_statistic_lastrounds e, game_playingboard f WHERE e.round2 = d.roundID AND"
					+ " d.playingboardID = f.playingBoardID and e.userID = ?) UNION (SELECT e.round3, f.name FROM game_round d , game_statistic_lastrounds e, game_playingboard f"
					+ " WHERE e.round3 = d.roundID AND d.playingboardID = f.playingBoardID and e.userID = ?)"), 
					

	/**
	 * Liefert die Anzahl aller erhaltenen Awards eines Users; Parameter: (1) userID
	 */
	SELECT_COUNT_OF_AWARDS("select count(*) from game_statistic_award where userID = ?")
	; // Enum
	

	// Ende

	/**
	 * SQL String für das zugehörige Enum.
	 */
	private String stmt;

	/**
	 * Konstruktor
	 * 
	 * @param stmt
	 *            String
	 */
	DBStatements(String stmt) {
		this.stmt = stmt;
	}

	/**
	 * Liefert den SQL String
	 * 
	 * @return stmt
	 */
	public String getStmt() {
		return this.stmt;
	}

	/**
	 * Schreibt den SQL-String
	 * 
	 * @return stmt
	 */
	@Override
	public String toString() {
		return this.stmt;
	}
}
