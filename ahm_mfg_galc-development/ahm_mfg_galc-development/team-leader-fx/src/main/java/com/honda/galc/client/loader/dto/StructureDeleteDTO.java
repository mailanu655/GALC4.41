package com.honda.galc.client.loader.dto;

import javafx.beans.property.SimpleStringProperty;

public class StructureDeleteDTO {
	private SimpleStringProperty col1;
    private SimpleStringProperty col2;
    private SimpleStringProperty col3;
    private SimpleStringProperty col4;
    private SimpleStringProperty col5;
    private SimpleStringProperty col6;
    
    public StructureDeleteDTO(String col1, String col2, String col3, String col4, String col5, String col6) {
        this.col1 = new SimpleStringProperty(col1);
        this.col2 = new SimpleStringProperty(col2);
        this.col3 = new SimpleStringProperty(col3);
        this.col4 = new SimpleStringProperty(col4);
        this.col5 = new SimpleStringProperty(col5);
        this.col6 = new SimpleStringProperty(col6);
    }
    
    public SimpleStringProperty col1Property() {
        if (col1 == null) {
        	col1 = new SimpleStringProperty(this, "col1");
        }
        return col1;
    }
    public SimpleStringProperty col2Property() {
        if (col2 == null) {
        	col2 = new SimpleStringProperty(this, "col2");
        }
        return col2;
    }
    public SimpleStringProperty col3Property() {
        if (col3 == null) {
        	col3 = new SimpleStringProperty(this, "col3");
        }
        return col3;
    }
    public SimpleStringProperty col4Property() {
        if (col4 == null) {
        	col4 = new SimpleStringProperty(this, "col4");
        }
        return col4;
    }
    public SimpleStringProperty col5Property() {
        if (col5 == null) {
        	col5 = new SimpleStringProperty(this, "col5");
        }
        return col5;
    }
    public SimpleStringProperty col6Property() {
        if (col6 == null) {
        	col6 = new SimpleStringProperty(this, "col6");
        }
        return col6;
    }
   
    public String getCol1() {
        return col1.get();
    }
    public void setCol1(String col1) {
    	this.col1.set(col1);
    }
    public String getCol2() {
        return this.col2.get();
    }
    public void setCol2(String col2) {
    	this.col2.set(col2);
    }
    public String getCol3() {
        return col3.get();
    }
    public void setCol3(String col3) {
    	this.col3.set(col3);
    }
    public String getCol4() {
        return this.col4.get();
    }
    public void setCol4(String col4) {
    	this.col4.set(col4);
    }
    public String getCol5() {
        return col5.get();
    }
    public void setCol5(String col5) {
    	this.col5.set(col5);
    }
    public String getCol6() {
        return this.col6.get();
    }
    public void setCol6(String col6) {
    	this.col6.set(col6);
    }
    
    
}
