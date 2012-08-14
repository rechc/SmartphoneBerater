package de.htw.test;

import java.util.List;

import de.htw.berater.db.ResultData;
import de.htw.berater.db.SQLClient;

public class DBTest {
	
	// quick and dirty
	
	public static void main(String[] args){
		SQLClient sq =SQLClient.getInstance();
		List<ResultData> xlist = sq.getResultData("Select * from Smartphones;");
		
		for(ResultData rd : xlist){
			System.out.println(rd.toString());
		}
		
		
		System.out.println("Ende");
	}

}
