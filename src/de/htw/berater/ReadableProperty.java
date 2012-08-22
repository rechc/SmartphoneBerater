package de.htw.berater;

public class ReadableProperty {
	
	private String key;
	private String value;
	private boolean booleanValue;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isBooleanValue() {
		return booleanValue;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value, boolean booleanValue) {
		this.value = value;
		this.booleanValue = booleanValue;
	}
	
	@Override
	public String toString() {
		return key + ":" + value;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ReadableProperty)
		
		if (((ReadableProperty)other).key.equals(key) && ((ReadableProperty)other).value.equals(value)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (key + value).hashCode();
	}
}
