package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.gts.figure.ShapeFigure;
import com.honda.galc.client.gts.view.MessageWindow;
import com.honda.galc.entity.enumtype.GtsLineStyle;
import com.honda.galc.entity.enumtype.GtsShapeType;
import com.honda.galc.entity.gts.GtsShape;

public class EditShapeDialog extends JDialog implements  ActionListener{

    private static final long serialVersionUID = 1L;
   
    private ShapeFigure figure;
    private JPanel mainPanel = new JPanel();
    private JPanel upperPanel = new JPanel();
    private JPanel middlePanel = new JPanel(); 
    private JPanel lowerPanel = new JPanel(); 
    private JPanel typePanel = new JPanel();
    private JPanel lineStylePanel = new JPanel();
    private JPanel lineWidthPanel = new JPanel();
    private JPanel lineColorPanel = new JPanel();
    private JPanel fillColorPanel = new JPanel();
    private JLabel lineColorLabel = new JLabel();
    private JLabel fillColorLabel = new JLabel();
    private ButtonGroup typeGroup = new ButtonGroup();
    private JRadioButton[] typeButtons = new JRadioButton[]{new JRadioButton("Rectangle"),
                                                            new JRadioButton("Round Rectangle"),
                                                            new JRadioButton("Triangle"),
                                                            new JRadioButton("Ellipse"),
                                                            new JRadioButton("Line")};
    private ButtonGroup lineStyleGroup= new ButtonGroup();
    private JRadioButton[] lineStyleButtons = new JRadioButton[]{new JRadioButton("Sold"),
                                                                 new JRadioButton("Dashed")};
    private JSpinner lineWidthSpinner = new JSpinner();
    private JButton lineColorButton =    new JButton("...");
    private JButton fillColorButton = new JButton("...");
    private JButton noFillButton = new JButton("No Fill");
    private JButton applyButton = new JButton("Apply");
    private JButton okButton = new JButton("Ok");
    private JButton cancelButton = new JButton("    Cancel  ");
    
    
    public EditShapeDialog(ShapeFigure figure){
    	super(figure.getDrawing().getController().getWindow(),true);
        this.figure = figure;
        setTitle("Edit Shape Window");
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(figure.getDrawing().getController().getWindow());
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        setUpperPanel();
        setMiddlePanel();
        setLowerPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(upperPanel,BorderLayout.NORTH);
        mainPanel.add(middlePanel,BorderLayout.CENTER);
        mainPanel.add(lowerPanel,BorderLayout.SOUTH);
 
    }
   
    
    
    private void setUpperPanel(){
        setTypePanel();
        upperPanel.add(typePanel);
    }
    
    private void setMiddlePanel(){

        setLineStylePanel();
        setLineWidthPanel();
        setLineColorPanel();
        setFillColorPanel();
        middlePanel.setLayout(new GridLayout(2,2));
        middlePanel.add(lineStylePanel);
        middlePanel.add(lineWidthPanel);
        middlePanel.add(lineColorPanel);
        middlePanel.add(fillColorPanel);
 
    }
    
    private void setLowerPanel(){
 
        lowerPanel.setLayout(new BoxLayout(lowerPanel,BoxLayout.X_AXIS));
        lowerPanel.setPreferredSize(new Dimension(300,60));
        lowerPanel.add(Box.createHorizontalGlue());
        lowerPanel.add(applyButton);
        lowerPanel.add(Box.createHorizontalStrut(10));
        lowerPanel.add(okButton);
        lowerPanel.add(Box.createHorizontalStrut(10));
        lowerPanel.add(cancelButton);
        lowerPanel.add(Box.createHorizontalStrut(60));
 
    }
    
    private void setTypePanel(){
 
        typePanel.setBorder(new TitledBorder("Type"));
        for(int i= 0;i<typeButtons.length;i++){
            typePanel.add(typeButtons[i]);
            typeGroup.add(typeButtons[i]);
            typeGroup.getSelection();
        }
        int ord = figure.getShape().getShapeType().getId();
        if(ord>=0 && ord <typeButtons.length)
            typeButtons[ord].setSelected(true);
 
    }
    
       
    private void setLineStylePanel(){
 
        lineStylePanel.setBorder(new TitledBorder("Line Style"));
        for(int i= 0;i<lineStyleButtons.length;i++){
            lineStylePanel.add(lineStyleButtons[i]);
            lineStyleGroup.add(lineStyleButtons[i]);
        }
        int ord = figure.getShape().getLineStyle().ordinal();
        if(ord>=0 && ord <lineStyleButtons.length)
            lineStyleButtons[ord].setSelected(true);
 
    }
    
    private void setLineWidthPanel(){
 
        lineWidthPanel.setBorder(new TitledBorder("Line Width"));
        SpinnerModel model = new SpinnerNumberModel((int)figure.getShape().getLineWidth(), 1,30,1);  
        lineWidthSpinner.setModel(model);
        lineWidthPanel.add(lineWidthSpinner);
 
    }
    
    private void setLineColorPanel(){
 
        lineColorPanel.setBorder(new TitledBorder("Line Color"));
        lineColorLabel.setPreferredSize(new Dimension(80,30));
        lineColorPanel.add(lineColorLabel);
        lineColorPanel.add(lineColorButton);
        Color color = figure.getShape().getLineColor();
        lineColorLabel.setOpaque(color != null);
        lineColorLabel.setBackground(color);
 
    }
    
    private void setFillColorPanel(){
 
        fillColorPanel.setBorder(new TitledBorder("Fill Color"));
        fillColorLabel.setPreferredSize(new Dimension(80,30));
        fillColorPanel.add(fillColorLabel);
        fillColorPanel.add(fillColorButton);
        fillColorPanel.add(noFillButton);
        Color color = figure.getShape().getFillColor();
        fillColorLabel.setOpaque(color != null);
        fillColorLabel.setBackground(color);
 
    }
    
    private void addActionListeners(){
 
        applyButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        lineColorButton.addActionListener(this);
        fillColorButton.addActionListener(this);
        noFillButton.addActionListener(this);
 
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == applyButton){
            applyChange();
        }else if(e.getSource() == okButton){
            done();
        }else if(e.getSource() == cancelButton){
            close();
        }else if(e.getSource() == lineColorButton){
            selectLineColor(e);
        }else if(e.getSource() == fillColorButton){
            selectFillColor(e);
        }else if(e.getSource() == noFillButton){
            setNoFillColor();
        }
        
    }
    
    private void applyChange(){
 
        boolean changed = false;
        GtsShape shape = figure.getShape();
        // line color
        if(this.applyLineColorChange(shape)) changed = true;
        // fill color
        if(this.applyFillColorChange(shape)) changed = true;
        // type
        GtsShapeType type = GtsShapeType.getType(getSelectedType());
        if(!shape.getShapeType().equals(type)){
            shape.setShapeTypeValue(type.getId());
            changed = true;
        }
        // line style
        GtsLineStyle lineStyle = GtsLineStyle.getType(this.getSelectedLineStyle());
        if(!shape.getLineStyle().equals(lineStyle)){
            shape.setLineStyleValue(lineStyle.getId());
            changed = true;
        }
        // line width
        int lineWidth = (Integer)lineWidthSpinner.getValue();
        if(shape.getLineWidth()!= lineWidth){
            shape.setLineWidth(lineWidth);
            changed = true;
        }
        // update database and figures
        if(changed){
            GtsShape newShape = figure.getDrawing().getModel().updateShape(shape);
            figure.getDrawing().getController().getTrackingPanel().getDrawingView().removeFromSelection(figure);
            figure.setShape(newShape);
            figure.getDrawing().getController().getTrackingPanel().getDrawingView().addToSelection(figure);
        }
 
    }
    
    private boolean applyLineColorChange(GtsShape shape){
 
        if(shape.getLineColor()== null && !lineColorLabel.isOpaque())return false;
        if(shape.getLineColor() != null && lineColorLabel.isOpaque()&& 
           shape.getLineColor().equals(lineColorLabel.getBackground())) return false;
        if(shape.getLineColor()!=null && !lineColorLabel.isOpaque()){
            shape.setLineColor((Color)null);
            return true;
        }
        shape.setLineColor(lineColorLabel.getBackground());
        return true;

    }
    
    private boolean applyFillColorChange(GtsShape shape){

        if(shape.getFillColor()== null && !fillColorLabel.isOpaque())return false;
        if(shape.getFillColor() != null && fillColorLabel.isOpaque()&& 
           shape.getFillColor().equals(fillColorLabel.getBackground())) return false;
        if(shape.getFillColor()!=null && !fillColorLabel.isOpaque()){
            shape.setFillColor((Color)null);
            return true;
        }
        shape.setFillColor(fillColorLabel.getBackground());
        return true;
    }
    
    private void done(){
        applyChange();
        close();
    }
    
    private void close(){
        this.setVisible(false);
        this.dispose();
    }
    
    private void selectLineColor(ActionEvent e){
 
        Color chosenColor = JColorChooser.showDialog((Component) e.getSource(), "Please select color", lineColorLabel.getBackground());
        if (chosenColor != null) {
            lineColorLabel.setOpaque(true);
            lineColorLabel.setBackground(chosenColor);
        }
    }
    
    private void selectFillColor(ActionEvent e){
 
        Color chosenColor = JColorChooser.showDialog((Component) e.getSource(), "Please select color", fillColorLabel.getBackground());
        if (chosenColor != null) {
            fillColorLabel.setOpaque(true);
            fillColorLabel.setBackground(chosenColor);
        }

    }
    
    private void setNoFillColor(){

        fillColorLabel.setOpaque(false);
        fillColorLabel.setBackground(null);
 
    }
    
    private int getSelectedType(){
 
        for(int i=0;i<typeButtons.length;i++){
            if(typeButtons[i].isSelected()) return i;
        }
        return 0;
 
    }
    
    private int getSelectedLineStyle(){
 
        for(int i=0;i<lineStyleButtons.length;i++){
            if(lineStyleButtons[i].isSelected()) return i;
        }
        return 0;
 
    }
    
        
    public void handleException(Exception e) {
 
        MessageWindow.showExceptionDialog(this, e.getMessage());

    }
}
