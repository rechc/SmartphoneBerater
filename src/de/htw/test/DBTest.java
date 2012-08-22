package de.htw.test;

import java.util.List;

import de.htw.berater.db.DBException;
import de.htw.berater.db.Smartphone;
import de.htw.berater.db.SQLClient;

public class DBTest {
	
	// quick and dirty
	
	public static void main(String[] args){
		testOutput("Select * from Smartphones;");
		System.out.println("Ende");
	}
	
	public static void testOutput(String sql){
		try {
			List<Smartphone> list = SQLClient.getInstance().getSmartphones(sql);
			System.out.println(list);
			System.out.println(list.size());
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
