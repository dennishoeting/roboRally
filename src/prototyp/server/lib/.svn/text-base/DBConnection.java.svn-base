package prototyp.server.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Verbindung zur Datenbank und alle bereits kompilierten Statements in einer
 * Hashmap
 * 
 * @author Kamil Knefel, Andreas Rehfeldt
 * @version 1.1
 * 
 */
public final class DBConnection {

	/**
	 * Datenbankverbindung
	 */
	private static Connection connection;

	/**
	 * verwendete Datenbank
	 */
	private static final String DB_DATABASE = "roborally";

	/**
	 * Datenbanktreiber
	 */
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

	/**
	 * Datenbankpasswort
	 */
	private static final String DB_PASSWORD = "we_will_rock_with_our_fucking_game!";

	/**
	 * Port zur Datenbank
	 */
	private static final String DB_PORT = "40020";

	/**
	 * Datenbankservername
	 */
	private static final String DB_SERVERNAME = "hurrikan.informatik.uni-oldenburg.de";

	/**
	 * Datenbankbenutzer
	 */
	private static final String DB_USERNAME = "roborally";

	/**
	 * Hashmap mit allen PreparedStatements
	 */
	private static HashMap<String, PreparedStatement> statements = null;

	/**
	 * Schließt die Datenbankverbindung, PreparedStatements und löscht alle
	 * PreparedStatements aus der HashMap
	 */
	public static void closeConnection() {
		if (DBConnection.connection != null) {
			try {
				closePstmts();
				DBConnection.connection.close();
				DBConnection.statements.clear();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Schließt alle PreparedStatements
	 * 
	 * @throws SQLException
	 *             Falls ein Datenbankfehler auftritt
	 */
	private static void closePstmts() throws SQLException {
		if (DBConnection.statements != null
				&& !DBConnection.statements.isEmpty()) {
			for (PreparedStatement pstmt : DBConnection.statements.values()) {
				pstmt.close();
			}
		}
	}

	/**
	 * Baut die Verbindung zur Datenbank auf, falls noch nicht vorhanden. Prüft
	 * bei vorhandener Verbindung, ob diese noch gültig ist, falls nicht wird
	 * eine neue erstellt; Kamil, 27.10.2010
	 * 
	 * @return Connection zur Datenbank
	 */
	public static Connection getConnection() {
		if (DBConnection.connection == null) {
			new DBConnection();
			// System.out.println(DBConnection.getTime()+"Verbindung aufgebaut! (Fkt getConnection)");
		} else {
			DBConnection.isConnectionValid();
		}
		return DBConnection.connection;
	}

	/**
	 * Liefert das SQL-Statement zum übergebenen SQL-Query. Ist das
	 * SQL-Statement noch nicht vorbereitet, so wird es kompiliert und in die
	 * HashMap mit den PreparedStatments hinzugefügt.
	 * 
	 * @param sql
	 *            SQL-Statement, das vorbereitet werden soll.
	 * @return PreparedStatement des übergebenen SQL-Query
	 * 
	 * @throws SQLException
	 *             Falls bspw. die Verbindung nicht vorhanden ist oder das
	 *             Statement syntaktisch falsch ist.
	 */
	public static PreparedStatement getPstmt(String sql) {
		PreparedStatement pstmt;
		if (DBConnection.connection != null) {
			DBConnection.isConnectionValid();
		}
		if (DBConnection.statements != null
				&& DBConnection.statements.containsKey(sql)) {
			pstmt = DBConnection.statements.get(sql);
		} else {
			pstmt = prepareStatement(sql);
			DBConnection.statements.put(sql, pstmt);
		}

		return pstmt;
	}

	/**
	 * Prüft ob die Verbindung noch gültig ist
	 */
	private static boolean isConnectionValid() {
		boolean result = false;
		try {
			// Prüft ob die Verbindung noch gültig ist. Timeout=100
			if (!DBConnection.connection.isValid(100)) {
				DBConnection.closeConnection();
				DBConnection.connection = null;
				new DBConnection();
			}
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * Allgemeine Funktion zum compilieren eines SQL's
	 * 
	 * @param sql
	 *            SQL String, der im PreparedStatementobjekt compiliert wird
	 * 
	 * @return das compilierte Statement
	 */
	private static PreparedStatement prepareStatement(String sql) {
		PreparedStatement pstmt = null;
		try {
			if (DBConnection.connection == null) {
				DBConnection.getConnection();
			} else {
				DBConnection.isConnectionValid();
			}
			pstmt = DBConnection.connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pstmt;
	}

	/**
	 * Standard-Konstruktor, der eine Verbindung zur Datenbank aufbaut, falls
	 * diese noch nicht vorhanden ist.
	 */
	private DBConnection() {
		if (DBConnection.connection == null) {
			try {
				Class.forName(DBConnection.DB_DRIVER).newInstance(); // Um
																		// korrekten
																		// Treiber
																		// für
																		// die
																		// Connection
																		// zu
																		// laden
				DBConnection.connection = DriverManager
						.getConnection(getConnectionURL());
				DBConnection.statements = new HashMap<String, PreparedStatement>();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Liefert die Connection String zur Datenbank
	 * 
	 * @return Connection String zur Datenbank
	 */
	private String getConnectionURL() {
		return "jdbc:mysql://" + DBConnection.DB_SERVERNAME + ":"
				+ DBConnection.DB_PORT + "/" + DBConnection.DB_DATABASE
				+ "?user=" + DBConnection.DB_USERNAME + "&password="
				+ DBConnection.DB_PASSWORD;// JDBC url
	}
}
