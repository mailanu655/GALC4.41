package com.honda.galc.client.ui.component;

import java.awt.Font;

import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * 
 * <h3>LabeledComponent Class description</h3>
 * <p> LabeledComponent description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Apr 21, 2011
 * 
 * @author Suriya Sena
 * Jan 29, 2014 JavaFx Migration
 *
 */

public class LabeledControl<T extends javafx.scene.control.Control> extends BorderPane {
	
	private static final long serialVersionUID = 1L;
	
	private Label label = UiFactory.createLabel("");
	//private JScrollPane scrollPane;
	private T control;
	
	public LabeledControl(String labelName, T control){
	    this(labelName,control,true,false);
	}
	public LabeledControl(String labelName,T control, boolean isHorizontal) {
	    this(labelName,control,isHorizontal,false);
	}
	
	public LabeledControl(String labelName, T control,boolean isHorizontal, boolean isScrollable){

		// set horizontal and vertical gap between components as 10
		this.setPadding(new javafx.geometry.Insets(10));
        
        label.setText(labelName);
        label.setId(labelName.toLowerCase()+" Label");
        control.setId(labelName.toLowerCase()+" Field");
        this.control = control;
        
        if(isScrollable){
        	System.out.println("SCROLLABLE NOT IMPLEMENTED YET");
//            scrollPane =  new JScrollPane(control);
//            scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        }
        
        if(isHorizontal) {
        	this.setLeft(label);
//            this.add(Box.createHorizontalStrut(10));
//            this.add(isScrollable ? scrollPane:control,BorderLayout.CENTER);
        	this.setCenter(control);
        }else{
            this.setTop(label);
 //           this.add(Box.createVerticalStrut(10));
 //           this.add(isScrollable ? scrollPane:control,BorderLayout.CENTER);
            this.setCenter(control);
        }
        setWidth(100);
	}
	
	/**
	 * This method is used to create a Labeled Control with Bold Label and fixed alignment
	 * @param labelName
	 * @param control
	 * @param isHorizontal
	 * @param isScrollable
	 * @param insets
	 * @param isLabelBold
	 */
	public LabeledControl(String labelName, T control,boolean isHorizontal, boolean isScrollable, Insets insets, boolean isLabelBold, boolean isMandatory){

		this.setPadding(insets);
        
        label.setText(labelName);
        label.setId(labelName.toLowerCase()+" Label");
        control.setId(labelName.toLowerCase()+" Field");
        this.control = control;
        if(isLabelBold) {
        	label.setStyle("-fx-font-weight: bold;");
        }
        
        if(isScrollable){
        	//TODO: SCROLLABLE NOT IMPLEMENTED YET
        	Logger.getLogger().error("Scrollable yet to be implemented");
        }
        if(isMandatory) {
        	markFieldMandatory(label);
        }
        if(isHorizontal) {
        	this.setLeft(label);
        	this.setCenter(control);
        	BorderPane.setAlignment(getLabel(), Pos.CENTER_RIGHT);
            BorderPane.setAlignment(getControl(), Pos.CENTER_RIGHT);
        }else{
            this.setTop(label);
            this.setCenter(control);
            BorderPane.setAlignment(getLabel(), Pos.CENTER);
            BorderPane.setAlignment(getControl(), Pos.CENTER);
        }
        setWidth(100);
	}
	
	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public T getControl() {
		return control;
	}

	public void setComponent(T component) {
		this.control = component;
	}
	
	public void setFont(Font font) {
//TODO use stylesheet instead of Font
	//	if(label != null) label.setFont(font);
	//	if(control != null) control.setFont(font);
	}
	
	public void setWidth(int width) {
		control.setMinSize(width,control.getHeight());
	}
	
	public void setHeight(int height) {
		control.setMinSize(control.getWidth(),height);
	}
	
	public void setMaxHeight(int height) {
		control.setMaxSize(control.getWidth(),height);
	}
	
	public void setMaxWidth(int width) {
		control.setMaxSize(width,control.getHeight());
	}
	
	public void setPreferredHeight(int height) {
		control.setPrefSize(control.getWidth(), height);
	}
	
	public void setPreferredWidth(int width) {
		control.setPrefSize(width, control.getHeight());
	}
	
	public void setLabelPreferredHeight(int height) {
		label.setPrefSize(label.getWidth(),height);
	}
	
	public void setLabelPreferredWidth(int width) {
		label.setPrefSize(width,label.getHeight());
	}

	@SuppressWarnings("serial")
	public void setInsets(final int top,final int left,final int bottom, final int right) {
		//TODO
//		this.setBorder(new AbstractBorder() {
//			public Insets getBorderInsets(Component c) { 
//		        return new Insets(top, left, bottom, right);
//		    }
//		});
	}

	
	public void setEnable(boolean flag) {
		label.setDisable(!flag);
		control.setDisable(!flag);
	}
	
	/**
	 * This method is used to create asterisk label
	 */
	public Label markFieldMandatory(final Label label) {
		final LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-asterisk-mark");
		label.setContentDisplay(ContentDisplay.RIGHT);
		label.setGraphic(asterisk);
		return label;
	}
	
}
