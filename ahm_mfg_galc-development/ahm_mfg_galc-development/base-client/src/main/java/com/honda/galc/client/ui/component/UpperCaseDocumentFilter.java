package com.honda.galc.client.ui.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class UpperCaseDocumentFilter extends DocumentFilter {
	private static final long serialVersionUID = 1L;
//	public void insertString(int offs,String str,AttributeSet a) throws BadLocationException{
//	try{
//	super.insertString(offs,str.toUpperCase(),a);
//	}
//	catch(BadLocationException e){
//	Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::insertString() exception.");
//	}
//	}
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
			AttributeSet attr) throws BadLocationException {
		fb.insertString(offset, text.toUpperCase(), attr);
	}
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
			AttributeSet attr) throws BadLocationException {
		fb.replace(offset, length, text.toUpperCase(), attr);
	}



}
