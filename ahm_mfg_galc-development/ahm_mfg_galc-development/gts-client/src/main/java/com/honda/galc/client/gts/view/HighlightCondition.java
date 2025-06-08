package com.honda.galc.client.gts.view;

import java.awt.Color;

public class HighlightCondition {
     
    private Type type;
    private String expected_value;
    private Color color;
    
    public HighlightCondition(Type type,String value,Color color){
        this.type = type;
        this.expected_value = value;
        this.color = color;
    }
    
    public Type getType(){
        return type;
    }
    
    public String getTypeName() {
    	return type.getTypeName();
    }
    
    public String getExpectedValue(){
        return expected_value;
    }
    
    public Color getColor(){
        return color;
    }
    
    public void setColor(Color color){
        this.color = color;
    }
    
    public enum Type {
    	TYPE_NONE("Type None"),
    	TYPE_PROD_LOT("Type Production Lot"),
    	TYPE_PROD_NUMBER("Type Product ID"),
    	TYPE_CARRIER("Type Carrier"),
    	TYPE_MTO("Type MTO"),
    	TYPE_COLOR("Type Color");
    	
    	private String typeName;
    	
    	private  Type(String typeName) {
    		this.typeName = typeName;
    	}
    	
    	public String getTypeName() {
    		return typeName;
    	}
    	
    }
}
