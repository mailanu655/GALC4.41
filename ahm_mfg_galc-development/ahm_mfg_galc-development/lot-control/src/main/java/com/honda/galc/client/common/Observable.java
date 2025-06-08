package com.honda.galc.client.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Observable</code> is implements observable pattern. It's 
 * guarantee the notify the observers in the same order the observers were added. 
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
 * <TD>P.Chou</TD>
 * <TD>Nov 13, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
public class Observable {
	Map<String, IObserver> observers = new LinkedHashMap<String, IObserver>();

	public void addObserver(IObserver observer) {
        if (observer == null) {
            //OK, not configured. 
        	return;
        }
        synchronized (this) {
            if (!observers.containsKey(observer.getClass().getSimpleName()))
                observers.put(observer.getClass().getSimpleName(), observer);
        }
    }
	
	@SuppressWarnings("unchecked")
	public <T extends IObserver> T getObserver(Class<T> clzz){
		return (T)observers.get(clzz.getSimpleName());
	}
	
	public synchronized void notifyObservers(Object data) {
        synchronized (this) {
        	for(IObserver observer : observers.values())
        		observer.update(this, data);
        }
    }
	
	public synchronized IObserver getObserver(IObserver observer) {
           return observers.get(observer.getClass().getSimpleName());
    }
	
	public synchronized void deleteObserver(IObserver observer){
		observers.remove(observer.getClass().getSimpleName());
	}
	
	public synchronized void deleteObservers(){
		observers.clear();
	}
	
	public synchronized Map<String, IObserver> getObservers() {
		return observers;
	}

	public synchronized void setObservers(Map<String, IObserver> observers) {
		this.observers = observers;
	}
	
	public synchronized int count(){
		return observers.size();
	}
	
	public void cleanUp(){
		for(IObserver observer : observers.values()){
			observer.cleanUp();
		}
	}
}
