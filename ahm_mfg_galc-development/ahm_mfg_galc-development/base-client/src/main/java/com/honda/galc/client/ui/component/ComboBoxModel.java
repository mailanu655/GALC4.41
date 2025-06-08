package com.honda.galc.client.ui.component;

import java.util.Arrays;
import java.util.List;


public class ComboBoxModel<T> extends ListModel<T> implements javax.swing.ComboBoxModel{

    
    private static final long serialVersionUID = 1L;
    
    private T selectedObject;
    
    public ComboBoxModel(List<T> objects) {
    	this(objects,null);
    }
    
    public ComboBoxModel(List<T> objects,String methodName) {
        super(objects,methodName);
    }
    
    public ComboBoxModel(T[] values) {
    	this(Arrays.asList(values),null);
    }
    
    public Object getSelectedItem() {
         return selectedObject;
    }

    @SuppressWarnings("unchecked")
	public void setSelectedItem(Object anItem) {
       	if ((selectedObject != null && !selectedObject.equals( anItem )) ||
    		    selectedObject == null && anItem != null) {
    		    selectedObject = (T)anItem;
    		    fireContentsChanged(this, -1, -1);
    	 }
 
    }
    
    public T getSelectedModel() {
        for(T item : this.objects) {
            Object obj = getDisplayObject(item);
            if(obj.equals(selectedObject)) return item;
        }
        return null;
    }

}
