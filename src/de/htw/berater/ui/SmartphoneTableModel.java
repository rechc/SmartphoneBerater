package de.htw.berater.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.htw.berater.db.Smartphone;

public final class SmartphoneTableModel extends AbstractTableModel {

	private List<Smartphone> phones;

	private String[] columnNames = {
			"Marke", "Name", "Preis", "Gewicht", "Farbe", "Material", "Gesprächszeit (2G)", "Display",
			"Auflösung", "interner Speicher", "RAM", "OS", "MegaPixel", "WLAN", "Bluetooth", "MSExchange",
			"Spritzwasserfest", "Hardwaretastatur", "Mhz", "Kerne", "GPS", "Kamera"
	};

	public SmartphoneTableModel() {
		this.phones = new ArrayList<Smartphone>();
	}

	@Override
	public int getRowCount() {
		return phones.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Smartphone phone = phones.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return phone.getBrand();
		case 1:
			return phone.getName();
		case 2:
			return phone.getPrice();
		case 3:
			return phone.getWeight();
		case 4:
			return phone.getColor();
		case 5:
			return phone.getMaterial();
		case 6:
			return phone.getBatteryRuntime();
		case 7:
			return phone.getDisplaysize();
		case 8:
			return phone.getResolution();
		case 9:
			return phone.getInternalMemory();
		case 10:
			return phone.getRam();
		case 11:
			return phone.getOs();
		case 12:
			return phone.getMegapixel();
		case 13:
			return phone.isWlan();
		case 14:
			return phone.isBluetooth();
		case 15:
			return phone.isMsexchange();
		case 16:
			return phone.isSplashWaterProof();
		case 17:
			return phone.isHardwarekeyboard();
		case 18:
			return phone.getMhz();
		case 19:
			return phone.getCores();
		case 20:
			return phone.isGps();
		case 21:
			return phone.isHatKameraEigenschaft();
		default:
			return "TODO";
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {//TODO fix warning
		return Object.class;
	}

	public void setPhoneList(List<Smartphone> phones) {
		this.phones = phones;
		fireTableDataChanged();
	}

}
