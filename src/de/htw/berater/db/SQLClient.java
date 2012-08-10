package de.htw.berater.db;

import java.util.List;
import java.util.Set;

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
	
	public List<ResultData> getResultData(Set<SQLConstraint> sqlConstraints) {
		//TODO
		return null;
	}
}
