package com.honda.galc.service.wds;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.WdsWebDataServerInterface.www.WdsHttpValue;
import com.WdsWebDataServerInterface.www.WdsValueDefinition;
import com.WdsWebDataServerInterface.www.WdsWebDataServerInterfaceLocator;
import com.WdsWebDataServerInterface.www.WdsWebDataServerInterfacePort;
import com.honda.galc.common.logging.Logger;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>WdsClient</code> is ...
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

public class WdsClient implements IWdsClient{
    
    WdsWebDataServerInterfacePort port;
    
    protected String wsURL = "";
    
    protected Logger logger;
    
    public WdsClient(String wsURL, Logger logger) {
        
        this.wsURL = wsURL;
        this.logger = logger;
        
        initPort();
        
    }
    
    private void initPort() {
        
        WdsWebDataServerInterfaceLocator locator = new WdsWebDataServerInterfaceLocator();
        
        
        
        try {
            
            if(wsURL != null && wsURL.length() != 0)
                port = locator.getWdsWebDataServerInterfacePort(new URL(wsURL));
            
        } catch (MalformedURLException e) {
                
            getLogger().error("Data Server Web service URL is incorrect. URL : " + wsURL);
                
        } catch (ServiceException e) {
            getLogger().error(e,"Could not inititialize Data Server web service");
        }
        
    }
    
    public void updateValue(String cat,String name,int value) {
        
        try {
            if(port != null)
                port.updateIntegerValue(cat + "\\" + name,BigInteger.valueOf(value));
            
        } catch (RemoteException e) {
            getLogger().error(e,"Data Server Web service invocation problem when executing \"updatingIntegerValue\"");
        }
        
    }
    
    public void updateValue(String cat, String name, float value) {
        
        try {
            if(port != null)
                port.updateFloatValue(cat + "\\" + name,value);
        } catch (RemoteException e) {
            getLogger().error(e,"Data Server Web service invocation problem when executing \"updatingFloatValue\"");
        }
        
    }
    
    public void updateValue(String cat, String name, String value){
        try {
            if(port != null)
                port.updateStringValue(cat + "\\" + name,value);
        } catch (RemoteException e) {
            getLogger().error(e,"Data Server Web service invocation problem when executing \"updatingStringValue\"");
        }
    }
    
    public String currentValue(String cat, String name) {
        
        try {
            if(port != null){
                WdsHttpValue[] values = port.currentValue(cat + "\\" + name);
                return values[0].getValueString();
            }
        } catch (RemoteException e) {
            getLogger().error(e,"Data Server Web service invocation problem when executing \"currentValue\"");
        }
        return null;
    }
    
    public WdsValueDefinition[] availableValues(){
        try{
            if(port != null)
                return port.availableValues();
        }catch(RemoteException e) {
            getLogger().error(e,"Data Server Web service invocation problem when executing \"availableValues\"");
        }
        return null;
        
    }
    
    public void updateValues(WdsHttpValue[] values) {
        
        try {
            if(port != null)
                port.updateValues(values);
        } catch (RemoteException e) {
            getLogger().error(e,"Data Server Web service invocation problem when executing \"updateValues\"");
        }
    }
    
   
    
    public void updateValues(List<WdsHttpValue> values){
        
        WdsHttpValue[] httpValues = new WdsHttpValue[values.size()];
        for(int i = 0; i<values.size();i++){
            httpValues[i] = values.get(i);
        }
        if (httpValues.length > 0) {
        	updateValues(httpValues);
        } 
    }
    
    protected Logger getLogger() {
        return logger;
    }
}
