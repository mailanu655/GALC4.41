package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ListModel<T> extends  AbstractListModel implements ListCellRenderer{
    
    
    private static final long serialVersionUID = 1L;
    
    protected List<T> objects = null;
    protected Comparator<? super T> comparator = null;
    protected String methodName;
    private int textAlignment = JLabel.LEFT;
    
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    
    public ListModel(List<T> objects) {
        this(objects, null);
    }
    
    public ListModel(List<T> objects,String methodName) {
        this.objects = objects;
        this.methodName = methodName;
    }
    
    
    public T getElementAt(int index) {
        if(index >= getSize()) return null;
        return objects.get(index);
    }
    
    public void removeAll(List<T> objectList) {
    	if (objectList == null) return;
    	for (T object : objectList) {
    		remove(object);
    	}
    }
    
    public void remove(T object) {
    	if(objects == null) return;
    	int index = objects.indexOf(object);
    	if(index <0) return;
    	objects.remove(index);
    	fireIntervalRemoved(this, index, index);
    }
    
    public void addAll(List<T> objList) {
    	if (objList == null) return;
    	for (T obj : objList) {
    		add(obj);
    	}
    }
    
    public void add(T obj) {
    	
    	if(objects == null) return;
    	int index = objects.size();
    	objects.add(obj);
    	fireIntervalAdded(this, index, index);
    }
    

    public int getSize() {
        return objects == null ? 0 : objects.size();
    }
    
	public void setTextAlignment(int textAlignment) {
		this.textAlignment = textAlignment;
	}

	public int getTextAlignment() {
		return textAlignment;
	}

    protected  String getDisplayObject(T object) {
        try {
            if(methodName == null) return object == null ? null : object.toString();
            Method method = object.getClass().getMethod(methodName, new Class[]{});
            return (String)method.invoke(object, new Object[]{});
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    
    @SuppressWarnings("unchecked")
	public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
      
        JLabel renderer = (JLabel) defaultRenderer
            .getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        if (value instanceof Icon) {
            renderer.setIcon((Icon)value);
        }
       
        if (isSelected) {
            renderer.setBackground(list.getSelectionBackground());
            renderer.setForeground(list.getSelectionForeground());
        }else{
            renderer.setBackground(list.getBackground());
            renderer.setForeground(list.getForeground());
        }
        if(value != null) {
            renderer.setText((String)getDisplayObject((T)value));
            renderer.setHorizontalAlignment(textAlignment);
            renderer.setFont(list.getFont());
        }
        return renderer;
    }
    
    /**
     * Sets the Comparator used for sorting the list.
     */
    public void setComparator(Comparator<? super T> comparator) {
    	this.comparator = comparator;
    }
    
    /**
     * Sorts the backing list using the model's comparator.<br>
     * Has no effect if the model's comparator is not specified.
     */
    public void sort() {
    	if (this.comparator != null) {
    		Collections.sort(this.objects, this.comparator);
    		fireContentsChanged(this, 0, 0);
    	}
    }

}
