package de.htw.berater.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
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

	};

	/**
	 * Singelton
	 * 
	 * @return
	 */
	public static SQLClient getInstance() {
		synchronized (SQLClient.class) {
			if (instance == null) {
				try {
					instance = new SQLClient();
				} catch (DBException e) {
					// TODO Auto-generated catch block
					// TODO weiterschmeiﬂen
					e.printStackTrace();
				}
			}
			return instance;
		}
	}

	private Connection getConnection() throws DBException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			return (Connection) DriverManager.getConnection(serverUrl + dbName,
					serverUserName, serverUserPw);

		} catch (ClassNotFoundException e) {
			throw new DBException(
					"ClassNotFoundException: MySQL Treiber installieren oder .jar einbinden "
							+ e);
		} catch (SQLException e) {
			throw new DBException("DB-Connection-Error " + e);
		}
	}

	/**
	 * 
	 * @param sql erwartet select Befehl auf Smartphones
	 * @return List<ResultData>
	 */
	public List<ResultData> getResultData(String sql) {
		LinkedList<ResultData> rdl = new LinkedList<ResultData>();
		try {
			Connection con = getConnection();
			Statement stmnt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			if (stmnt.execute(sql)) {
				ResultSet rs = stmnt.getResultSet();
				rdl = parseResultData(rdl, rs);
				rs.close();
			}
			stmnt.close();
			con.close();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rdl;
	}

	private LinkedList<ResultData> parseResultData(LinkedList<ResultData> rdl,
			ResultSet rs) throws SQLException {
		if (!rs.next()) {
			return new LinkedList<ResultData>();
		}
		ResultData temprd;

		do {
			temprd = new ResultData();

			temprd.setID(rs.getInt("Smartphone_id"));
			temprd.setName(rs.getString("Name"));
			temprd.setBrand(rs.getString("Marke"));
			temprd.setPrice(rs.getDouble("Preis"));
			temprd.setWeight(rs.getDouble("Gewicht"));
			temprd.setColor(rs.getString("Farbe"));
			temprd.setMaterial(rs.getString("Material"));
			temprd.setBatteryRuntime(rs.getDouble("Akkulaufzeit"));
			temprd.setDisplaysize(rs.getDouble("Displaygroesse"));
			temprd.setResolution(rs.getString("Aufloesung"));
			temprd.setInternalMemory(rs.getInt("Interner Speicher"));
			temprd.setRam(rs.getInt("RAM"));
			temprd.setOs(rs.getString("Os"));
			temprd.setMegapixel(rs.getDouble("Megapixel"));
			temprd.setWlan(rs.getBoolean("WLAN"));
			temprd.setBluetooth(rs.getBoolean("Bluetooth"));
			temprd.setMsexchange(rs.getBoolean("MSExchange"));
			temprd.setSplashWaterProof(rs.getBoolean("Spritzwassergeschuetzt"));
			temprd.setHardwarekeyboard(rs.getBoolean("Hardwaretastatur"));
			temprd.setMhz(rs.getInt("Mhz"));
			temprd.setCores(rs.getInt("cores"));
			temprd.setGps(rs.getBoolean("GPS"));

			rdl.add(temprd);

		} while (rs.next());

		return rdl;
	}

}
