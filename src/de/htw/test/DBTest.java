package de.htw.test;

import java.util.List;

import de.htw.berater.db.DBException;
import de.htw.berater.db.Smartphone;
import de.htw.berater.db.SQLClient;

public class DBTest {
	
	// quick and dirty
	
	public static void main(String[] args){
		SQLClient sq =SQLClient.getInstance();
		List<Smartphone> xlist;
		try {
			xlist = sq.getSmartphones("Select * from Smartphones;");
			for(Smartphone rd : xlist){
				System.out.println(rd.toString());
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		System.out.println("Ende");
	}

}
