package com.honda.galc.client.ui.component;

import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.honda.galc.common.logging.Logger;

import java.awt.AWTEvent;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Text Field to restrict the length of an input string.
 * <h4>Usage and Example</h4>
 * 
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Y.Kawada</TD>
 * <TD>(Nov/02/2001 10:04:35)</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Modify Javadoc</TD>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.5 2010</TD>
 * <TD>1.0.2</TD>
 * <TD>(none)</TD>
 * <TD>added render</TD>
 * </TR>
 * </TR>
 * </TABLE>
 * @see 
 * @author M.Satoh
 * @version 1.0
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LengthFieldBean extends JTextField implements ItemChange {

	private static final long serialVersionUID = 1L;
	private int maximumLength = 0;
	private boolean fixedLength = false;
	public LengthFieldBean rootClass = null;
	protected ITextFieldRender render;

	protected class LengthControlDocument extends PlainDocument {

		private static final long serialVersionUID = 1L;

		public void insertString(int offs,String str,AttributeSet a) throws BadLocationException{
			String insertStr=null; 
			String oldString = rootClass.getText();
			String newString = oldString.substring(0,offs) + str + oldString.substring(offs);

			try{
				if (str==null) return;
				if (maximumLength <= 0){
					insertStr = str;
				}
				if (maximumLength > 0){
					if (newString.length() <= maximumLength){
						insertStr = str;
					}else{
						if (getLength() < maximumLength){
							insertStr = str.substring(0,maximumLength-getLength());
						}
					}
				}
				if (str.length() >= 1){
					super.insertString(offs,insertStr,a);
				}
			}catch(BadLocationException e){
				Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::insertString() exception.");
			}
		}
	}
	
	private EventListenerList listenerList = new EventListenerList();
	private java.awt.Color color = Color.white; 
	public LengthFieldBean() {
		rootClass	= this;
		this.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doEnterText(e);
			}
		});
		this.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if (!e.isTemporary()){
					doLostFocus(e);
				}
			}
			public void focusGained(FocusEvent e){
				if (!e.isTemporary()){
					doGainedFocus(e);
				}
			}
		});
	}
	
	public LengthFieldBean(ITextFieldRender fieldRender) {
		this();
		render = fieldRender;

		if(render != null){
			render.setField(this);
			render.init();
		}
	}

	/**
	 * Add ItemChangeLinstener to listen ActionEvent and FocusEvent from this LengthFieldBeen.
	 * 
	 *
	 * @param l ItemChangeListener????
	 * @see ItemChangeListener
	 */
	public void addItemChangeListener(ItemChangeListener l) {
		listenerList.add(ItemChangeListener.class, l);
	}  
	
	/**
	 * If this is specified as fixed, check if the string of the text field is the same length as specified.
	 * 
	 * @return false - If the length of input string is not equal to the specified length.
	 * <BR>    true  - If the length of input string is equal to the specified length.
	 */
	public boolean checkFixedLength() {
		
		return !fixedLength || maximumLength == getText().length();

	}  
	
	protected Document createDefaultModel() {
		return new LengthControlDocument();
	}
	
	protected void doEnterText(AWTEvent e) {
		if (checkFixedLength()){
			this.setBackground(getColor());			
		}else{
			this.setBackground(Color.red);
		}
		fireItemChange(e);
	}
	protected void doLostFocus(AWTEvent e) {
	
	}  

	protected void fireItemChange(AWTEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==ItemChangeListener.class) {
				((ItemChangeListener)listeners[i+1]).itemChanged(e);
			}
		}
	}
	protected void doGainedFocus(AWTEvent e) {
		Logger.getLogger().check(this.rootClass.getName()+" gained focus");
	}  
	public java.awt.Color getColor() {
		return color;
	}

	public int getMaximumLength(){
		return maximumLength;
	}  
	
	
	public boolean isFixedLength() {
		return fixedLength;
	}  
	
	/**
	 * Remove ItemChangeListener from FixedLengthBeen.
	 * 
	 * @param l ItemChangeListener
	 * @see ItemChangeListener
	 */
	public void removeItemChangeListener(ItemChangeListener l) {
		listenerList.remove(ItemChangeListener.class, l);
	}
	
	/**
	 * Set color to FixedLengthBeen.
	 * 
	 * @param newColor java.awt.Color
	 * @see java.awt.Color
	 */
	public void setColor(java.awt.Color newColor) {
	   Logger.getLogger().check(this.rootClass.getName()+" color changed to "+newColor);
		color = newColor;
	}
	
	/**
	 * If this field is specified as fixed lengh, any input the lengh of which is not equal to the specified length is considered to be an error and the background of this field turned to be red.
	 * 
	 * @param fixedLengthFlg true-Specify as fixed length field. false -Specify as not fixed length field.
	 */
	public void setFixedLength(boolean fixedLengthFlg){
		fixedLength = fixedLengthFlg;
		checkFixedLength();
	}  
	
	/**
	 * Set maximum length to the specified length, user cannot input longer than this. The maximum length is used when checkFixedLength() is called.
	 * @param Length 
	 */
	public void setMaximumLength(int Length){
		maximumLength = Length;
		this.setText(this.getText());
	}  
	
	/**
	 * Set the specified text to text field but those characters which are over the specified maximum length are removed.
	 * @param t String 
	 */
	public void setText(String t) {
		String text;
		text = (t != null) ? t: "";
		try{
			if(getMaximumLength() != 0){
				if(text.length() > getMaximumLength()) text = text.substring(0,getMaximumLength()) ;
			}
			super.setText(text);
			checkFixedLength();
		}		
		catch(IndexOutOfBoundsException e){
			Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::setText() exception.");
		}
	}
	
	public void setText(Text t) {
		if(t.getValue() != null)
			super.setText(t.getValue());
		
		if(render != null)
			render.renderField(t);
	}
	
	public void setStatus(boolean status) {
		if(render != null)
			render.renderField(status);
	}

	public ITextFieldRender getRender() {
		return render;
	}

	public void setRender(ITextFieldRender render) {
		this.render = render;
		if(render != null)
			this.render.setField(this);
	}
}
