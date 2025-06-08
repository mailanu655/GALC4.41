package com.honda.galc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * 
 * @author Jeffray Huang
 *
 * @param <E>
 */
public class SortedArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;
    
    private String methodName;
    private boolean inverseSort = false;
    
    public SortedArrayList() {
        
    }
    
    public SortedArrayList(Collection<? extends E> c) {
        this.addAll(c);
    }
    
    public SortedArrayList(String methodName) {
        this.methodName = methodName;
    }
    
    public SortedArrayList(String methodName, boolean inverseSort) {
        this(methodName);
        this.inverseSort = inverseSort;
    }
    
    public SortedArrayList(Collection<? extends E> c,String methodName) {
        this(methodName);
        this.addAll(c);
    }
    
    public SortedArrayList(Collection<? extends E> c,String methodName, boolean inverseSort) {
        this(methodName, inverseSort);
        this.addAll(c);
    }
    
    public boolean add(E o) {
        super.add(o);
        sort();
        return true;
    }
    
    public void add(int index, E element) {
        super.add(index,element);
        sort();
    }
    
    public boolean addAll(int index, Collection<? extends E> c) {
    	if(c == null) return false;
        boolean flag = super.addAll(index,c);
        sort();
        return flag;
    }    
    
    public boolean addAll(Collection<? extends E> c) {
    	if(c == null) return false;
        boolean flag = super.addAll(c);
        sort();
        return flag;
    }    
    
    private void sort() {
        Collections.sort(this,new ArrayComparator());
    }
    
    private class ArrayComparator implements Comparator<E> {

    	int invert = 1;
    	
        public int compare(E o1, E o2) {
        	if (inverseSort)
        		invert = -1;
        	
            Object obj1 = getFieldValue(o1);
            Object obj2 = getFieldValue(o2);
            if(obj1 instanceof String){
                if(obj1 == null) return -1 * invert;
                if(obj2 == null) return 1 * invert;
                return ((String)obj1).compareTo((String)obj2) * invert;
            }
            else if(obj1 instanceof Integer) {
                if((Integer) obj1 > (Integer) obj2) return 1 * invert;
                if((Integer) obj1 < (Integer) obj2) return -1 * invert;
            }
            else if(obj1 instanceof Double) {
                if((Double) obj1 > (Double) obj2) return 1 * invert;
                if((Double) obj1 < (Double) obj2) return -1 * invert;
            }
            else if(obj1 instanceof Date) {
                if(obj1 == null) return -1 * invert;
                if(obj2 == null) return 1 * invert;
                return ((Date)obj1).compareTo((Date)obj2) * invert;
            }
            return 0;
        }
        
        private Object getFieldValue(E o){
            if(methodName == null || methodName.length() == 0) return o;
            Method method = null;
                try {
                    method = o.getClass().getMethod(methodName, new Class[]{});
                } catch (SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (NoSuchMethodException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
           
            Object obj = null;
            if(method != null)
                try {
                    obj = method.invoke(o,new Object[]{});
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
                 
            return obj;
        }
        
    }
    
}
