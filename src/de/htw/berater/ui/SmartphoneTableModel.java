package de.htw.berater.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.htw.berater.db.Smartphone;

public final class SmartphoneTableModel extends AbstractTableModel {

	private List<Smartphone> phones;

	private String[] columnNames = {
			"Marke", "Name", "TODO", "TODO", "TODO", "TODO", "TODO", "TODO",
			"TODO", "TODO", "TODO", "TODO", "TODO", "TODO", "TODO", "TODO",
			"TODO", "TODO", "TODO", "TODO", "TODO", "TODO"
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
		default:
			return "TODO";
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	public void setPhoneList(List<Smartphone> phones) {
		this.phones = phones;
		fireTableDataChanged();
	}

}
