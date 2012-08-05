package de.htw.berater.db;

public class ResultData {
	private boolean wlan;
	private String marke;
	private boolean outDoor;
	private int aufloesung;
	private boolean appStore;
	private double price;
	/* und alle die hier fehlen*/
	
	
	
	public boolean isWlan() {
		return wlan;
	}
	public void setWlan(boolean wlan) {
		this.wlan = wlan;
	}
	public String getMarke() {
		return marke;
	}
	public void setMarke(String marke) {
		this.marke = marke;
	}
	public boolean isOutDoor() {
		return outDoor;
	}
	public void setOutDoor(boolean outDoor) {
		this.outDoor = outDoor;
	}
	public int getAufloesung() {
		return aufloesung;
	}
	public void setAufloesung(int aufloesung) {
		this.aufloesung = aufloesung;
	}
	public boolean isAppStore() {
		return appStore;
	}
	public void setAppStore(boolean appStore) {
		this.appStore = appStore;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
