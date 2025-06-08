package com.honda.galc.client.datacollection.view;


import javax.swing.text.*;

import com.honda.galc.client.ui.component.LengthFieldBean;

public class NumericFieldBean extends LengthFieldBean {
  
  protected class NumericCaseDocument extends LengthControlDocument {
	
	public void insertString(int offs,String str,AttributeSet a) throws BadLocationException{
	  String oldString = getText(0,getLength());
	  String newString = oldString.substring(0,offs) + str + oldString.substring(offs);
	  try{
			for(int i = 0; i <= str.length() - 1; i++){
				if (Character.isDigit(str.charAt(i)) == false) return;
			}	
			super.insertString(offs,str,a);
	  }
	  catch(BadLocationException e){
	  }

	}
  }
  
  protected Document createDefaultModel() {
		return new NumericCaseDocument();
  }


	public void setText(String t) {
		String text;
		text = t;
		try{
			for(int i = 0; i <= text.length() - 1; i++){
				if (Character.isDigit(text.charAt(i)) == false) return;
			}	
			super.setText(text);
		}
		catch(NumberFormatException ne){
		} 				
		catch(IndexOutOfBoundsException e){
		}
	}
}
