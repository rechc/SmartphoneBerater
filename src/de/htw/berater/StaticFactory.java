package de.htw.berater;

import java.util.List;

import de.htw.berater.controller.Controller;
import de.htw.berater.db.ResultData;
import de.htw.berater.ui.BeraterUI;

public class StaticFactory {
	public static BeraterUI getNewBeraterUI(Berater berater1, Berater berater2) {
		return new BeraterUI(berater1, berater2){

			@Override
			public void onFalseQuestion(String string) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNewQuestion(String newQuestion) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void show(List<ResultData> resultData) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void show() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNewData(List<ResultData> resultData) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setController(Controller controller) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}


	public static Berater getNewBerater1(String rdfPath, String namespace) {
		return new Berater1(rdfPath, namespace);
	}


	public static Berater getNewBerater2(String rdfPath, String namespace) {
		return new Berater2(rdfPath, namespace);
	}
}
