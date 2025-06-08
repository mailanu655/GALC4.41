package com.honda.galc.enumtype;


/**
 * @author Jeff Lutz
 * @date Nov 16, 2009
 */
public class EnumUtil {

    public static <T extends IdEnum<?>> T getType(Class<T> type, int id) {
        for (T t : type.getEnumConstants()) {
            if (t.getId() == id) return t;
        }
        return null;
    }
    
    public static <T extends IdEnum<?>> T getType(Class<T> type, String name) {
        for (T t : type.getEnumConstants()) {
            if (t.toString().equals(name)) 
            	return t;
        }
        return null;
    }
}
