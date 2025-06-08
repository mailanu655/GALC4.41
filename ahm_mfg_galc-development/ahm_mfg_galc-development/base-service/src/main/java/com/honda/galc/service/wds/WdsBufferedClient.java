package com.honda.galc.service.wds;

import java.util.ArrayList;
import java.util.List;

import com.WdsWebDataServerInterface.www.WdsHttpValue;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>WdsBufferedClient</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>May 8, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class WdsBufferedClient extends WdsClient {
    
    private List<WdsHttpValue> values = new ArrayList<WdsHttpValue>();
    
    private String plantName = "";
    
    private final static String  COMPONENT_ID = "Data Server";
   
    
    public WdsBufferedClient(Logger logger, String plantName,String wsURL){
        
        super(wsURL,logger);
        
        this.plantName = plantName;
        
    }
    
    public WdsBufferedClient(Logger logger){
        super(PropertyService.getProperty(COMPONENT_ID,"WEB SERVICE URL"),logger);
        this.plantName = PropertyService.getProperty(COMPONENT_ID,"SERVER IDENTIFIER");
    }
    
    
     public void updateValue(String name,int value){
        addValue(createHttpValue(plantName + "\\" +  name,"NUM",String.valueOf(value)));
    }
    
    public void updateValue(String name,float value){
        
        addValue(createHttpValue(plantName + "\\" +  name,"NUM",String.valueOf(value)));
        
    }
    
    public void updateValue(String name,String value) {
        
        addValue(createHttpValue(plantName + "\\" +  name,"STR",value));
        
    }
    
    private void addValue(WdsHttpValue value){
        
        WdsHttpValue aValue = this.getWdsValue(value.getName());
        
        if(aValue == null) values.add(value);
        else {
            aValue.setType(value.getType());
            aValue.setValueString(value.getValueString());
        }
        
    }
    
    private WdsHttpValue createHttpValue(String name, String type,String valueString) {
        
        WdsHttpValue httpValue = new WdsHttpValue();
        
        httpValue.setName(name);
        httpValue.setType(type);
        httpValue.setValueString(valueString);
        
        return httpValue;
        
    }
    
    private WdsHttpValue getWdsValue(String name){
        
        for(WdsHttpValue value : values) {
            
            if(value.getName().equals(name)) return value;
        }
        
        return null;
    }
    
    /**
     * 
     *
     */
    
    public void flush() {
        if(wsURL != null && wsURL.length() != 0)
            updateValues(values);
        
        values.clear();
        
    }
    
    
}
