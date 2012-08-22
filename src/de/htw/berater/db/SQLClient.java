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
	 */
	public static SQLClient getInstance() {
		synchronized (SQLClient.class) {
			if (instance == null) {
				try {
					instance = new SQLClient();
				} catch (DBException e) {
					// TODO Auto-generated catch block
					// TODO weiterschmeißen
					e.printStackTrace();
				}
			}
			return instance;
		}
	}

	private Connection getConnection() throws DBException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			return DriverManager.getConnection(serverUrl + dbName,
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
	 * @param sql
	 *            erwartet select Befehl auf Smartphones
	 * @return List<ResultData>
	 * @throws DBException
	 */
	public List<Smartphone> getSmartphones(String sql) throws DBException {
		ArrayList<Smartphone> rdl = new ArrayList<Smartphone>();
		ResultSet rs = null;
		Statement stmnt = null;
		Connection con = null;
		try {
			con = getConnection();
			stmnt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			if (stmnt.execute(sql)) {
				rs = stmnt.getResultSet();
				rdl = parseResultData(rdl, rs);
				rs.close();
			}
			stmnt.close();
			con.close();
		} catch (SQLException e) {
			throw new DBException("DB Exception: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmnt != null)
					stmnt.close();
				if (con != null)
					con.close();
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
		List<String> brands = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn
					.prepareStatement("select distinct(marke) from Smartphones order by 1");
			rs = stmt.executeQuery();
			while (rs.next())
				brands.add(rs.getString(1));
		} catch (SQLException e) {
			throw new DBException("DB Exception: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				// Ignore.
			}
		}
		return brands;
	}

}
