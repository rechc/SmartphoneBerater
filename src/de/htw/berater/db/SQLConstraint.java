package de.htw.berater.db;

public class SQLConstraint {
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return key + ":" + value;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof SQLConstraint)
		
		if (((SQLConstraint)other).key.equals(key) && ((SQLConstraint)other).value.equals(value)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (key + value).hashCode();
	}
}
