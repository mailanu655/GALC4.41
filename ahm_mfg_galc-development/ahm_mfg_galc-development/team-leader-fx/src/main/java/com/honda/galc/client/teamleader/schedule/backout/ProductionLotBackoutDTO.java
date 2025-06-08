package com.honda.galc.client.teamleader.schedule.backout;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductionLotBackoutDTO {

	public ProductionLotBackoutDTO(final String table, final int rows, final String info) {
		setTable(table);
		setRows(rows);
		setInfo(info);
	}

	private StringProperty table;
	public void setTable(String value) { tableProperty().set(value); }
	public String getTable() { return tableProperty().get(); }
	public StringProperty tableProperty() {
		if (table == null) table = new SimpleStringProperty(this, "table");
		return table;
	}

	private IntegerProperty rows;
	public void setRows(int value) { rowsProperty().set(value); }
	public int getRows() { return rowsProperty().get(); }
	public IntegerProperty rowsProperty() {
		if (rows == null) rows = new SimpleIntegerProperty(this, "rows");
		return rows;
	}

	private StringProperty info;
	public void setInfo(String value) { infoProperty().set(value); }
	public String getInfo() { return infoProperty().get(); }
	public StringProperty infoProperty() {
		if (info == null) info = new SimpleStringProperty(this, "info");
		return info;
	}
}
