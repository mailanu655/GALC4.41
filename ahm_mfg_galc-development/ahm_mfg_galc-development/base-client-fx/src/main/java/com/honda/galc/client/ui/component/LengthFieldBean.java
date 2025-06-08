package com.honda.galc.client.ui.component;

//import javax.swing.JTextField;
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.event.*;
//import javax.swing.text.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;

//import java.awt.AWTEvent;
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
public class LengthFieldBean extends LoggedTextField {

	private static final long serialVersionUID = 1L;
	private int maximumLength = 0;
	private boolean fixedLength = false;
	public LengthFieldBean rootClass = null;
	protected ITextFieldRender render;
	
	private Color color = Color.WHITE; 
	public LengthFieldBean(String id) {
		super(id);
		rootClass	= this;
		this.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent event) {
	        	doEnterText(event);
	        }
	    });	
	}
	
	public LengthFieldBean(String id, ITextFieldRender fieldRender) {
		this(id);
		render = fieldRender;

		if(render != null){
			render.setField(this);
			render.init();
		}
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
	
	protected void doEnterText(Event e) {
		String colorStyle = "";
		if (checkFixedLength()){
			if (getColor() != null) {
				colorStyle = "-fx-background-color: "
						+ getColor().toString().substring(2) + ";";
			}
		}else{
			colorStyle = "-fx-background-color: red;";
		}
		//Only change background color and leave other style unchanged.
		String existingStyle=this.getStyle();
		if(StringUtils.isBlank(existingStyle)){
			this.setStyle(colorStyle);
		}
		else{
			String[] styles = existingStyle.split(";");
			StringBuilder sb = new StringBuilder(colorStyle);
			for(String s: styles){
				if(!s.contains("-fx-background-color")){
					sb.append(";");
					sb.append(s);
				}
			}
			this.setStyle(sb.toString());
		}
	}
	protected void doLostFocus(Event e) {
	
	}  

	protected void doGainedFocus(Event e) {
		Logger.getLogger().check(this.rootClass.getScene()+" gained focus");
	}  
	public Color getColor() {
		return color;
	}

	public int getMaximumLength(){
		return maximumLength;
	}  
	
	
	public boolean isFixedLength() {
		return fixedLength;
	}
	
	/**
	 * Set color to FixedLengthBeen.
	 * 
	 * @param newColor javafx.scene.paint.Color
	 * @see javafx.scene.paint.Color
	 */
	public void setColor(Color newColor) {
	   Logger.getLogger().check(this.rootClass.getScene()+" color changed to "+newColor);
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
	public void settext(String t) {
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
