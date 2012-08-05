package de.htw.berater.db;

import java.util.List;

public class SQLClient {
	public static SQLClient instance;
	
	private SQLClient(){};
	
	public static SQLClient getInstance() {
		synchronized(SQLClient.class) {
			if (instance == null) {
				instance = new SQLClient();
			}
			return instance;
		}
	}
	
	public List<ResultData> getResultData(List<SQLConstraint> sqlConstraints) {
		//TODO
		return null;
	}
}
