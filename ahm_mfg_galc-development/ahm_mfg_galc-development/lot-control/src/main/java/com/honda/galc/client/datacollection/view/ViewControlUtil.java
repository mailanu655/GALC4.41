package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.honda.galc.client.ui.component.UpperCaseFieldBean;


public class ViewControlUtil {
	public final static Color VIEW_COLOR_INPUT = Color.white;
	public final static Color VIEW_COLOR_OUTPUT = new Color(204,204,204);
	public final static Color VIEW_COLOR_CURRENT = Color.blue;
	public final static Color VIEW_COLOR_OK = Color.green;
	public final static Color VIEW_COLOR_NG = Color.red;
	public final static Color VIEW_COLOR_WAIT = Color.yellow;
	public final static Color VIEW_COLOR_FONT = Color.black;
	public final static Color VIEW_COLOR_PROMPT = new Color(204, 204, 255);
	public final static Color VIEW_COLOR_SKIP = new Color(204, 204, 255);
	public final static int[] productSpecPosition ={20, 10, 7, 4, 1, 0};

	
	public static void refreshObject(JComponent component, String aText) {
		if (component instanceof JTextField) {
			if (aText != null)
				((JTextField) component).setText(aText);
		}
		if (component instanceof JLabel) {
			if (aText != null)
				((JLabel) component).setText(aText);
		}		
	}
	
	public static void refreshObject(Object aObj,String aText,Color aColor,boolean aEnable) {

		if (aObj instanceof UpperCaseFieldBean || aObj instanceof JTextField){
			//((UpperCaseFieldBean)aObj).setColor(aColor);
			((JTextField)aObj).setBackground(aColor);
			((JTextField)aObj).setEditable(aEnable);
			
			if (aEnable == false){
				((JTextField)aObj).setDisabledTextColor(VIEW_COLOR_FONT);
			}
			
			if (aText != null) ((JTextField)aObj).setText(aText);
			
			((JTextField)aObj).setSelectionStart(0);
			((JTextField)aObj).setSelectionEnd( ((JTextField)aObj).getColumns());
		}
		
		
	}
	
	public static void setSelection(JTextField bean, Color color) {
		bean.setSelectionStart(0);
		bean.setSelectionEnd(bean.getText().length());
		
		if(color != null)
		   bean.setSelectionColor(color);
	}
	
	public static String getColor(Map<String, String> modelColorMap, 
			String productSpec, boolean excludeYearCode){
		
		if(modelColorMap == null) return null;
		
		for(int i = 0; i < productSpecPosition.length; i++){
			int position = productSpecPosition[i];
			if((excludeYearCode && position == 1) || position == 0) break;
			if(productSpec.length() <= productSpecPosition[i +1]) continue;

			String spec = productSpec.substring(excludeYearCode ? 1 : 0, 
					(productSpec.length() < position)? productSpec.length() : (position));

			for(String key : modelColorMap.keySet()){
				if(spec.trim().equals(key.trim()))
					return modelColorMap.get(key);
			}
		}
		
		return null;
	}
	
	public static String getColor(Map<String, String> modelColorMap, String productSpec){
		
		return getColor( modelColorMap, productSpec, false);
	}


}
