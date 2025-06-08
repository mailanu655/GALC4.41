package com.honda.galc.client.utils;

import java.awt.Color;
import java.lang.reflect.Field;

import com.honda.galc.common.logging.Logger;

public class ColorUtil {
	public static Color getColor(String colorName) {
        try {
            // Find the field and value of colorName
           Field field = Class.forName("java.awt.Color").getField(colorName);
            return (Color)field.get(null);
        } catch (Exception e) {
        	Logger.getLogger().warn("ColorUtil::getColor() failed to get color:" + colorName);
            return null;
        }
    }
}
