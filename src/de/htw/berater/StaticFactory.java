package de.htw.berater;

import java.util.List;

import de.htw.berater.db.ResultData;
import de.htw.berater.szenario.Szenario;
import de.htw.berater.ui.BeraterUI;

public class StaticFactory {
	public static BeraterUI getNewBeraterUI() {
		return new BeraterUI() {
			
			@Override
			public void onStart(String string) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onReset() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNewQuestion(String newQuestion) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish(String string) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFalseQuestion(String string) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void addSzenario(Szenario szenario) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void startSzenario(Szenario szenario) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void followSzenario(Szenario szenario) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void addSzenarios(Szenario... sz) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void show(List<ResultData> resultData) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}


	public static Berater getNewBerater(String rdfPath, String namespace) {
		return null;
	}
}
