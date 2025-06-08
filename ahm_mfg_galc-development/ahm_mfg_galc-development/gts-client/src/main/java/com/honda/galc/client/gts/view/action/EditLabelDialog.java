package com.honda.galc.client.gts.view.action;

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
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.gts.figure.LabelFigure;
import com.honda.galc.client.gts.view.MessageWindow;
import com.honda.galc.entity.enumtype.GtsBorderType;
import com.honda.galc.entity.gts.GtsLabel;

public class EditLabelDialog extends JDialog implements  ActionListener{

    private static final long serialVersionUID = 1L;

    private LabelFigure figure;
    private JPanel mainPanel = new JPanel();
    private JPanel labelPanel = new JPanel();
    private JPanel borderTypePanel = new JPanel();
    private JPanel borderColorPanel = new JPanel();
    private JPanel textColorPanel = new JPanel();
    private JPanel backColorPanel = new JPanel();
    private JPanel commandPanel = new JPanel();
    private ButtonGroup typeGroup = new ButtonGroup();
    private JRadioButton[] typeButtons = new JRadioButton[]{new JRadioButton("Rectangle"),
                                                            new JRadioButton("Round Rectangle"),
                                                            new JRadioButton("Ellipse")};
    private JLabel borderColorLabel = new JLabel();
    private JLabel textColorLabel = new JLabel();
    private JLabel backColorLabel = new JLabel();
    
    private JTextField labelField = new JTextField();
    private JButton fontButton = new JButton("Font");
    private JButton borderColorButton = new JButton("...");
    private JButton noBorderButton = new JButton("No Border");
    private JButton textColorButton = new JButton("...");
    private JButton backColorButton = new JButton("...");
    private JButton noFillButton = new JButton("No Fill");
    private JButton applyButton = new JButton("Apply");
    private JButton okButton = new JButton("Ok");
    private JButton cancelButton = new JButton("    Cancel  ");
    
    
    public EditLabelDialog(LabelFigure figure){
        super(figure.getDrawing().getController().getWindow(),true);
        this.figure = figure;
        setTitle("Edit Label Window");
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(figure.getDrawing().getController().getWindow());
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        setLabelPanel();
        setBorderTypePanel();
        setBorderColorPanel();
        setTextColorPanel();
        setBackColorPanel();
        setCommandPanel();
        mainPanel.setLayout(new GridLayout(6,1));
        mainPanel.add(labelPanel);
        mainPanel.add(borderTypePanel);
        mainPanel.add(borderColorPanel);
        mainPanel.add(textColorPanel);
        mainPanel.add(backColorPanel);
        mainPanel.add(commandPanel);
   }
   
    
    private void setLabelPanel(){
        labelPanel.setBorder(new TitledBorder("Label"));
        labelField.setPreferredSize(new Dimension(160,40));
        labelField.setText(figure.getLabel().getLabelText());
        labelField.setFont(figure.getLabel().getFont());
        labelPanel.add(labelField);
        labelPanel.add(fontButton);
         
    }
    private void setBorderTypePanel(){
        borderTypePanel.setBorder(new TitledBorder("Border Type"));
        for(int i= 0;i<typeButtons.length;i++){
            borderTypePanel.add(typeButtons[i]);
            typeGroup.add(typeButtons[i]);
            typeGroup.getSelection();
        }
        int ord = figure.getLabel().getBorderType().ordinal();
        if(ord>=0 && ord <typeButtons.length)
            typeButtons[ord].setSelected(true);
    }
    
       
    
    
    private void setBorderColorPanel(){
        borderColorPanel.setBorder(new TitledBorder("Border Color"));
        borderColorLabel.setPreferredSize(new Dimension(160,25));
        borderColorPanel.add(borderColorLabel);
        borderColorPanel.add(borderColorButton);
        borderColorPanel.add(noBorderButton);
        Color color = figure.getLabel().getBorderColor();
        borderColorLabel.setOpaque(color != null);
        borderColorLabel.setBackground(color);
    }
    
    private void setTextColorPanel(){
        textColorPanel.setBorder(new TitledBorder("Text Color"));
        textColorLabel.setPreferredSize(new Dimension(160,25));
        textColorPanel.add(textColorLabel);
        textColorPanel.add(textColorButton);
        Color color = figure.getLabel().getTextColor();
        textColorLabel.setOpaque(color != null);
        textColorLabel.setBackground(color);
    }
    
    private void setBackColorPanel(){
        backColorPanel.setBorder(new TitledBorder("Background Color"));
        backColorLabel.setPreferredSize(new Dimension(160,25));
        backColorPanel.add(backColorLabel);
        backColorPanel.add(backColorButton);
        backColorPanel.add(noFillButton);
        Color color = figure.getLabel().getBackColor();
        backColorLabel.setOpaque(color != null);
        backColorLabel.setBackground(color);
    }
    
    private void setCommandPanel(){
        commandPanel.setLayout(new BoxLayout(commandPanel,BoxLayout.X_AXIS));
        commandPanel.setPreferredSize(new Dimension(300,60));
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.add(applyButton);
        commandPanel.add(Box.createHorizontalStrut(10));
        commandPanel.add(okButton);
        commandPanel.add(Box.createHorizontalStrut(10));
        commandPanel.add(cancelButton);
        commandPanel.add(Box.createHorizontalStrut(60));
    }
    
    private void addActionListeners(){
        applyButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        fontButton.addActionListener(this);
        borderColorButton.addActionListener(this);
        textColorButton.addActionListener(this);
        backColorButton.addActionListener(this);
        noBorderButton.addActionListener(this);
        noFillButton.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == fontButton){
            changeFont(e);
        }else if(e.getSource() == borderColorButton){
            selectBorderColor(e);
        }else if(e.getSource() == textColorButton){
            selectTextColor(e);
        }else if(e.getSource() == backColorButton){
            selectBackColor(e);
        }else if(e.getSource() == noBorderButton){
            setNoBorder();
        }else if(e.getSource() == noFillButton){
            setNoFill();
        }else if(e.getSource() == applyButton){
            applyChange();
        }else if(e.getSource() == okButton){
            done();
        }else if(e.getSource() == cancelButton){
            close();
        }
        
    }
    
    private void applyChange(){
        boolean changed = false;
        GtsLabel label = figure.getLabel();
        if(!label.getLabelText().equals(labelField.getText())){
            label.setLabelText(labelField.getText());
            changed = true;
        }
        if(label.getFont() == null || !label.getFont().equals(labelField.getFont())){
            label.setFont(labelField.getFont());
            changed = true;
        }
        // border color
        if(this.applyBorderColorChange(label)) changed = true;
        // text color
        if(this.applyTextColorChange(label)) changed = true;
        // background color
        if(this.applyBackColorChange(label)) changed = true;
        // border type
        GtsBorderType type = GtsBorderType.getType(getSelectedType());
        if(!label.getBorderType().equals(type)){
            label.setBorderType(type);
            changed = true;
        }
        // update database and figures
        if(changed){
            GtsLabel newLable = figure.getDrawing().getModel().updateLabel(label);
  //          figure.getDrawing().getController().getTrackingPanel().getDrawingView().removeFromSelection(figure);
            figure.setLabel(newLable);
  //          figure.getDrawing().getController().getTrackingPanel().getDrawingView().addToSelection(figure);
         }
    }
    
    private boolean applyBorderColorChange(GtsLabel label){
        if(label.getBorderColor()== null && !borderColorLabel.isOpaque())return false;
        if(label.getBorderColor() != null && borderColorLabel.isOpaque()&& 
           label.getBorderColor().equals(borderColorLabel.getBackground())) return false;
        if(label.getBorderColor()!=null && !borderColorLabel.isOpaque()){
            label.setBorderColor((Color)null);
            return true;
        }
        label.setBorderColor(borderColorLabel.getBackground());
        return true;
    }
    
    private boolean applyTextColorChange(GtsLabel label){
        if(label.getTextColor()== null && !textColorLabel.isOpaque())return false;
        if(label.getTextColor() != null && textColorLabel.isOpaque()&& 
           label.getTextColor().equals(textColorLabel.getBackground())) return false;
        if(label.getTextColor()!=null && !textColorLabel.isOpaque()){
            label.setTextColor((Color)null);
            return true;
        }
        label.setTextColor(textColorLabel.getBackground());
        return true;
    }
    
    private boolean applyBackColorChange(GtsLabel label){
        if(label.getBackColor()== null && !backColorLabel.isOpaque())return false;
        if(label.getBackColor() != null && backColorLabel.isOpaque()&& 
           label.getBackColor().equals(backColorLabel.getBackground())) return false;
        if(label.getBackColor()!=null && !backColorLabel.isOpaque()){
            label.setBackColor((Color)null);
            return true;
        }
        label.setBackColor(backColorLabel.getBackground());
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
    
    
    private void selectBorderColor(ActionEvent e){
        Color chosenColor = JColorChooser.showDialog((Component) e.getSource(), "Please select color", borderColorLabel.getBackground());
        if (chosenColor != null) {
            borderColorLabel.setOpaque(true);
            borderColorLabel.setBackground(chosenColor);
        }
    }
    
    private void selectTextColor(ActionEvent e){
        Color chosenColor = JColorChooser.showDialog((Component) e.getSource(), "Please select color", textColorLabel.getBackground());
        if (chosenColor != null) {
            textColorLabel.setBackground(chosenColor);
        }
    }
    
    private void selectBackColor(ActionEvent e){
        Color chosenColor = JColorChooser.showDialog((Component) e.getSource(), "Please select color", backColorLabel.getBackground());
        if (chosenColor != null) {
            backColorLabel.setOpaque(true);
            backColorLabel.setBackground(chosenColor);
        }
    }
    
    /**
     * set the border color as null --- no border will be drawn
     *
     */
    
    private void setNoBorder(){
        borderColorLabel.setOpaque(false);
        borderColorLabel.setBackground(null);
    }
    
    /**
     * set the back color as null --- no background will be displayed
     *
     */
    
    private void setNoFill(){
        backColorLabel.setOpaque(false);
        backColorLabel.setBackground(null);
    }
    
    private void changeFont(ActionEvent e){
        JFontChooser fontChooser = new JFontChooser(labelField.getFont());
        if(fontChooser.showDialog((Component)e.getSource(), "Choose Font")){
            labelField.setFont(fontChooser.getFont());
        }
    }
    
    private int getSelectedType(){
        for(int i=0;i<typeButtons.length;i++){
            if(typeButtons[i].isSelected()) return i;
        }
        return 0;
    }
        
    public void handleException(Exception e) {
        
        MessageWindow.showExceptionDialog(this, e.getMessage());

    }
}
