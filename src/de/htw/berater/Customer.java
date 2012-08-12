package de.htw.berater;

public class Customer {
	public static final int SEHBEHINDERT = 1;
	public static final int SPORTLER = 2;
	public static final int SUDOKUHENGST = 4;
	public static final int SPIELEFREAK = 8;
	private int customer;
	
	public void addCustomerInfo(int customer) {
		this.customer |= customer;
	}
	
	public boolean isCustomer(int info) {
		return (customer & info) == info;
	}

	public void removeCustomerInfo(int info) {
		info = ~info;
		customer &= info;
	}
}
