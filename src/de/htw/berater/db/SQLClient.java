package de.htw.berater.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SQLClient {

	private static SQLClient instance;

	// for properties
	private static final String PROPERTY_FILE_PATH = "./SQLClientDB.properties";
	private static final String PROPERTY_FILE_NAME_SERVER_URL = "serverUrl";
	private static final String PROPERTY_FILE_NAME_DB = "dbName";
	private static final String PROPERTY_FILE_NAME_SERVER_USER_NAME = "userName";
	private static final String PROPERTY_FILE_NAME_SERVER_USER_PW = "userPassword";

	// for connection
	private String serverUrl;
	private String dbName;
	private String serverUserName;
	private String serverUserPw;

	// the connection
	private Connection connection = null;

	private SQLClient() throws DBException {

		Properties dbprop = new Properties();

		try {

			dbprop.load(new FileInputStream(PROPERTY_FILE_PATH));
			this.serverUrl = dbprop.getProperty(PROPERTY_FILE_NAME_SERVER_URL);
			this.dbName = dbprop.getProperty(PROPERTY_FILE_NAME_DB);
			this.serverUserName = dbprop
					.getProperty(PROPERTY_FILE_NAME_SERVER_USER_NAME);
			this.serverUserPw = dbprop
					.getProperty(PROPERTY_FILE_NAME_SERVER_USER_PW);

		} catch (FileNotFoundException e) {
			throw new DBException("Property FileNotFoundException " + e);
		} catch (IOException e) {
			throw new DBException("Property IOException " + e);
		}

	}

	/**
	 * Singelton
	 * 
	 * @return
	 * @throws DBException 
	 */
	public static SQLClient getInstance() throws DBException {
		synchronized (SQLClient.class) {
			if (instance == null) {
				instance = new SQLClient();
			}
			return instance;
		}
	}

	public void initialConnection() throws DBException {
		System.out.println("connection opened");
		try {
			Class.forName("com.mysql.jdbc.Driver");

			this.connection = DriverManager.getConnection(serverUrl + dbName,
					serverUserName, serverUserPw);

		} catch (ClassNotFoundException e) {
			throw new DBException(
					"ClassNotFoundException: MySQL Treiber installieren oder .jar einbinden "
							+ e);
		} catch (SQLException e) {
			throw new DBException("DB-Connection-Error " + e);
		}
	}

	public void closeConnection() throws DBException {
		if (connection != null) {
			try {
				connection.close();
				System.out.println("connection closed");
			} catch (SQLException e) {
				throw new DBException("DB-Connection-Close-Error " + e);
			}
			connection = null;
		}
	}

	private void checkAndOpenConnection() throws DBException {
		if (connection == null) {
			initialConnection();
		}
	}

	/**
	 * 
	 * @param sql
	 *            erwartet select Befehl auf Smartphones
	 * @return List<ResultData>
	 * @throws DBException
	 */
	public List<Smartphone> getSmartphones(String sql) throws DBException {
		checkAndOpenConnection();

		ArrayList<Smartphone> rdl = new ArrayList<Smartphone>();
		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = this.connection.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			if (statement.execute(sql)) {
				rs = statement.getResultSet();
				rdl = parseResultData(rdl, rs);
				statement.close();
				rs.close();
			}
		} catch (SQLException e) {
			throw new DBException("DB Exception: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (statement != null)
					statement.close();
			} catch (SQLException ex) {
				// Ignore.
			}
		}
		return rdl;
	}

	private ArrayList<Smartphone> parseResultData(ArrayList<Smartphone> rdl,
			ResultSet rs) throws SQLException {
		if (!rs.next()) {
			return new ArrayList<Smartphone>();
		}
		Smartphone temprd;

		do {
			temprd = new Smartphone();

			temprd.setID(rs.getInt(1));
			temprd.setName(rs.getString(2));
			temprd.setBrand(rs.getString(3));
			temprd.setPrice(rs.getDouble(4));
			temprd.setWeight(rs.getDouble(5));
			temprd.setColor(rs.getString(6));
			temprd.setMaterial(rs.getString(7));
			temprd.setBatteryRuntime(rs.getDouble(8));
			temprd.setDisplaysize(rs.getDouble(9));
			temprd.setResolution(rs.getString(10));
			temprd.setInternalMemory(rs.getInt(11));
			temprd.setRam(rs.getInt(12));
			temprd.setOs(rs.getString(13));
			temprd.setMegapixel(rs.getDouble(14));
			temprd.setWlan(rs.getBoolean(15));
			temprd.setBluetooth(rs.getBoolean(16));
			temprd.setMsexchange(rs.getBoolean(17));
			temprd.setSplashWaterProof(rs.getBoolean(18));
			temprd.setHardwarekeyboard(rs.getBoolean(19));
			temprd.setMhz(rs.getInt(20));
			temprd.setCores(rs.getInt(21));
			temprd.setGps(rs.getBoolean(22));
			temprd.setHatKameraEigenschaft(rs.getBoolean(23));

			rdl.add(temprd);

		} while (rs.next());
		return rdl;
	}

	public List<String> getBrands() throws DBException {
		checkAndOpenConnection();

		List<String> brands = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = this.connection
					.prepareStatement("select distinct(marke) from Smartphones order by 1");
			rs = pstmt.executeQuery();

			while (rs.next())
				brands.add(rs.getString(1));
		} catch (SQLException e) {
			throw new DBException("DB Exception: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException ex) {
				// Ignore.
			}
		}
		return brands;
	}

	public boolean doesColumnExist(String localName) throws DBException {
		checkAndOpenConnection();

		PreparedStatement pstmt = null;
		try {
			pstmt = connection
					.prepareStatement("select * from Smartphones where "
							+ localName + " = 1");
			pstmt.executeQuery();
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException ex) {
				// Ignore.
			}
		}
	}

}
