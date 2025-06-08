package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JViewport;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import com.honda.galc.client.gts.view.GtsDrawingView;
import com.honda.galc.client.gts.view.MessageWindow;
import com.honda.galc.client.gts.view.Preference;
import com.honda.galc.entity.enumtype.GtsCarrierDisplayType;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>PreferenceDialog</code> is for user to set the user preference
 * The user can select the drawing view option - none, fit all or set
 * custom location. The user can select to maximize the window at start up.
 * Also the user can select the type of information to display at start up
 * for the carrier
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Apr 8, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class PreferenceDialog extends JDialog implements  ActionListener {

    private static final long serialVersionUID = 1L;

    
    private JPanel mainPanel = new JPanel();
    private JPanel startupPanel = new JPanel();
    private JPanel typePanel = new JPanel();
    private JPanel sizePanel = new JPanel();
    private JPanel customSizePanel = new JPanel();
    
    private JPanel commandPanel = new JPanel();
    
    private JFormattedTextField locationX = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField locationY = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JFormattedTextField scaleFactor = null;
    
    private JCheckBox windowMaxmizedBox = new JCheckBox("Window maxmized at  start up");
    
    private ButtonGroup typeGroup = new ButtonGroup();
    
    private JButton loadButton = new JButton("Load Current Location");
    private JButton okButton = new JButton("Ok");
    private JButton cancelButton = new JButton("    Cancel  ");
    
    private ButtonGroup sizeGroup = new ButtonGroup();
    private JRadioButton noneRadioButton =  new JRadioButton("None");
    private JRadioButton fitRadioButton = new JRadioButton("Automatically fit layout into the window");
    private JRadioButton csRadioButton =  new JRadioButton("Set customized size and location");
    
    private GtsDrawingView view;
    
    public PreferenceDialog(GtsDrawingView view){
        super(view.getDrawing().getController().getWindow(),"Preference Window",true);
        this.view = view;
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(view.getDrawing().getController().getWindow());
    }
    
    private void initComponent(){
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        setSizePanel();
        setTypePanel();
        setStartupPanel();
        
        setCommandPanel();
        
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        
        mainPanel.add(startupPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(commandPanel);
        
        contentPane.add(mainPanel);
        
        
        
    }
    
    private void setStartupPanel(){
        
        startupPanel.setBorder(new TitledBorder("Start Up Settings"));
        startupPanel.setLayout(new BorderLayout());
        
        windowMaxmizedBox.setSelected(Preference.isWindowMaximized());
        
        startupPanel.add(windowMaxmizedBox,BorderLayout.NORTH);
        startupPanel.add(sizePanel,BorderLayout.CENTER);
        startupPanel.add(typePanel,BorderLayout.SOUTH);
        
    }
    
    private void setSizePanel(){
        
        sizePanel.setBorder(new TitledBorder("Set the display size "));
        sizePanel.setLayout(new BorderLayout());
        
        JPanel aPanel = new JPanel(new GridLayout(3,1));
        
        noneRadioButton.setSelected(!Preference.isAutoFitAll() && !Preference.isCustomizedSize());
        sizeGroup.add(noneRadioButton);
        aPanel.add(noneRadioButton);
        
        fitRadioButton.setSelected(Preference.isAutoFitAll());
        sizeGroup.add(fitRadioButton);
        aPanel.add(fitRadioButton);
        
        csRadioButton.setSelected(Preference.isCustomizedSize());
        sizeGroup.add(csRadioButton);
        aPanel.add(csRadioButton);
 
        
        setCustomSizePanel();
        
        sizePanel.add(aPanel,BorderLayout.NORTH);
        sizePanel.add(customSizePanel,BorderLayout.SOUTH);
        
        
    }
    
    private void setCustomSizePanel(){
        
        this.customSizePanel.setLayout(new BoxLayout(customSizePanel,BoxLayout.Y_AXIS));
        
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setMinimum(10);
        formatter.setMaximum(800);
        scaleFactor = new JFormattedTextField(formatter);
        
        locationX.setColumns(10);
        locationY.setColumns(10);
        scaleFactor.setColumns(10);
        
        Point point = Preference.getLocation();
        
        locationX.setValue(point.x);
        locationY.setValue(point.y);

        scaleFactor.setValue((new Double(Preference.getDrawingScaleFactor()*100)).intValue());
        
        JPanel aPanel1 = new JPanel(new BorderLayout());
        aPanel1.add(new JLabel("      Top Left Location x:"),BorderLayout.WEST);
        aPanel1.add(locationX,BorderLayout.EAST);
        
        JPanel aPanel2 = new JPanel(new BorderLayout());
        aPanel2.add(new JLabel("      Top Left Location y:"),BorderLayout.WEST);
        aPanel2.add(locationY,BorderLayout.EAST);
        
        JPanel aPanel3 = new JPanel(new BorderLayout());
        aPanel3.add(new JLabel("      Scale Factor(%): "),BorderLayout.WEST);
        aPanel3.add(scaleFactor,BorderLayout.EAST);
        
        JPanel aPanel4 = new JPanel(new BorderLayout());
        aPanel4.add(loadButton,BorderLayout.EAST);
        customSizePanel.add(aPanel1);
        customSizePanel.add(aPanel2);
        customSizePanel.add(aPanel3);
        customSizePanel.add(aPanel4);
        
    }
    
    private void setTypePanel(){
        
        typePanel.setBorder(new TitledBorder("Carrier Display Type"));
        
        
        typePanel.setLayout(new GridLayout(GtsCarrierDisplayType.values().length,1));
        
        JRadioButton button;
        
        for (GtsCarrierDisplayType type : GtsCarrierDisplayType.values()){
            
            typeGroup.add( button = new JRadioButton(type.name()));
            button.setActionCommand(type.name());
            typePanel.add( button);
            
            if(Preference.getCarrierDisplayType().equals(type.name())) {
                button.setSelected(true);
            }
            
        }
        
        
        
        
    }
    
    private void setCommandPanel(){
        commandPanel.setLayout(new BoxLayout(commandPanel,BoxLayout.X_AXIS));
        commandPanel.setPreferredSize(new Dimension(300,60));
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.add(okButton);
        commandPanel.add(Box.createHorizontalStrut(10));
        commandPanel.add(cancelButton);
        commandPanel.add(Box.createHorizontalStrut(60));
    }
    
    private void addActionListeners(){
        
        loadButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
    }
    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == loadButton){
            loadCurrentLocation();
        }else if(e.getSource() == okButton){
            done();
        }else if(e.getSource() == cancelButton){
            close();
        }
        
    }
    
    private void loadCurrentLocation(){
        
        Point point = ((JViewport)view.getParent()).getViewPosition();
        
        locationX.setValue(point.x);
        locationY.setValue(point.y);
        
        scaleFactor.setValue(((Number)(view.getScaleFactor()*100)).intValue());
        
    }
    
    private void applyChange(){
        
        Preference.setWindowMaximized(windowMaxmizedBox.isSelected());
        
        // set the drawing view option - none, fit all or set custom location
        Preference.setDrawingViewOption(getSizeGroupIndex());
        
        if(Preference.isCustomizedSize()){
            Preference.setDrawingScaleFactor(((Number)scaleFactor.getValue()).intValue()/100.0);
            view.setScaleFactor(Preference.getDrawingScaleFactor());
            Preference.setLocation(((Number)locationX.getValue()).intValue(), ((Number)locationY.getValue()).intValue());
        }
        
        Preference.setCarrierDisplayType(typeGroup.getSelection().getActionCommand());
        
        if(Preference.isAutoFitAll()) view.fitAll();
        
    }
    
    private int getSizeGroupIndex(){
        
        if(this.fitRadioButton.isSelected()) return 1;
        else if(this.csRadioButton.isSelected()) return 2;
        return 0;
 
    }
    
    
    private void done(){
        
        applyChange();
        close();
        
    }
    
    private void close(){
        this.setVisible(false);
        this.dispose();
    }
    
    
    public void handleException(Exception e) {
        
        MessageWindow.showExceptionDialog(this, e.getMessage());

    }

  
}
