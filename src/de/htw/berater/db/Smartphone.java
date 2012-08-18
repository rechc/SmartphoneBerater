package de.htw.berater.db;

public class Smartphone {
	private int ID;
	private String name;
	private String brand;
	private double price;
	private double weight;
	private String color;
	private String material;
	private double batteryRuntime;
	private double displaysize;
	private String resolution;
	private int internalMemory;
	private int ram;
	private String os;
	private double megapixel;
	private boolean wlan;
	private boolean bluetooth;
	private boolean msexchange;
	private boolean splashWaterProof;;
	private boolean hardwarekeyboard;
	private boolean gps;
	private int mhz;
	private int cores;

	// private boolean outDoor;
	// private int aufloesung;
	// private boolean appStore;

	// ----- Getter + Setter -----

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public double getBatteryRuntime() {
		return batteryRuntime;
	}

	public void setBatteryRuntime(double batteryRuntime) {
		this.batteryRuntime = batteryRuntime;
	}

	public double getDisplaysize() {
		return displaysize;
	}

	public void setDisplaysize(double displaysize) {
		this.displaysize = displaysize;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public int getInternalMemory() {
		return internalMemory;
	}

	public void setInternalMemory(int internalMemory) {
		this.internalMemory = internalMemory;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public double getMegapixel() {
		return megapixel;
	}

	public void setMegapixel(double megapixel) {
		this.megapixel = megapixel;
	}

	public boolean isWlan() {
		return wlan;
	}

	public void setWlan(boolean wlan) {
		this.wlan = wlan;
	}

	public boolean isBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(boolean bluetooth) {
		this.bluetooth = bluetooth;
	}

	public boolean isMsexchange() {
		return msexchange;
	}

	public void setMsexchange(boolean msexchange) {
		this.msexchange = msexchange;
	}

	public boolean isSplashWaterProof() {
		return splashWaterProof;
	}

	public void setSplashWaterProof(boolean splashWaterProof) {
		this.splashWaterProof = splashWaterProof;
	}

	public boolean isHardwarekeyboard() {
		return hardwarekeyboard;
	}

	public void setHardwarekeyboard(boolean hardwarekeyboard) {
		this.hardwarekeyboard = hardwarekeyboard;
	}

	public boolean isGps() {
		return gps;
	}

	public void setGps(boolean gps) {
		this.gps = gps;
	}

	public int getMhz() {
		return mhz;
	}

	public void setMhz(int mhz) {
		this.mhz = mhz;
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}

	@Override
	public String toString() {
		return "ResultData [ID=" + ID + ", name=" + name + ", brand=" + brand
				+ ", price=" + price + ", weight=" + weight + ", color="
				+ color + ", material=" + material + ", batteryRuntime="
				+ batteryRuntime + ", displaysize=" + displaysize
				+ ", resolution=" + resolution + ", internalMemory="
				+ internalMemory + ", ram=" + ram + ", os=" + os
				+ ", megapixel=" + megapixel + ", wlan=" + wlan
				+ ", bluetooth=" + bluetooth + ", msexchange=" + msexchange
				+ ", splashWaterProof=" + splashWaterProof
				+ ", hardwarekeyboard=" + hardwarekeyboard + ", gps=" + gps
				+ ", mhz=" + mhz + ", cores=" + cores + "]";
	}
}
