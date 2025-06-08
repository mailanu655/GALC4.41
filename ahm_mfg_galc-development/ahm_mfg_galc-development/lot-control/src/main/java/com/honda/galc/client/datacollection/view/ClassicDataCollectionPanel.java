package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.datacollection.property.ClassicViewPropertyBean;

public class ClassicDataCollectionPanel extends DataCollectionPanelBase {

	private static final long serialVersionUID = 1L;
	protected SingleColumnTablePanel skippedProductPanel = null;
	protected ProcessedCounterTablePanel processedCounterTablePanel = null;
	private ArrayList<JTextArea> torqueInfoList = new ArrayList<JTextArea>();
	ClassicViewPropertyBean classicProperty;
	public ClassicDataCollectionPanel(ClassicViewPropertyBean property, int winWidth,
			int winHeight) {
		super(property, winWidth, winHeight);
		this.classicProperty = property;
		
		init();
	}

	@Override
	protected void init() {
		AnnotationProcessor.process(this);
		super.init();
	}

	@Override
	protected void initPanel() {
		super.initPanel();

		if(viewProperty.isMonitorSkippedProduct())
			add(getSkipedProductPanel());

		if(viewProperty.isShowProcessedCounter())
			add(getProcessedCounterTable());

		for (int i = 0; i < maxNumOfTorque; i++) {
			add(getTorqueValueTextArea(i),	getTorqueValueTextArea(i).getName());

		}		

	}
	

	@Override
	protected void initComponents() {
		super.initComponents();
		initTorqueInfoList();
	}

	public JTextArea getTorqueValueTextArea(int ic) {

		JTextArea textArea = null;
		try {
			textArea = torqueInfoList.get(ic);

		} catch (java.lang.Throwable t) {
			handleException(t);
		}
		return textArea;
	}
	
	private void initTorqueInfoList() {
		JTextArea textArea;
		for (int i = 0; i < maxNumOfTorque; i++) {
			try {
				textArea = new JTextArea();
				textArea.setName("JTextAreaTorque0" + i);
				textArea.setText("");
				textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				textArea.setFont(new java.awt.Font("dialog", 0, 10));
				lineUpTorqueArea(textArea, getTorqueValueTextField(i));
				textArea.setEditable(false);
				textArea.setEnabled(false);
				textArea.setVisible(false);
				textArea.setDisabledTextColor(Color.BLACK);
				textArea.setBackground(new Color(238,238,238));
				torqueInfoList.add(textArea);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}	
		
	}
	
	
	private void lineUpTorqueArea(JTextArea torqueValueTextArea,
			JTextField torqueValueTextField) {
		int textAreaH = classicProperty.getTorqueInfoAreaHeight();
		int gap = viewProperty.getTorqueFieldGap();
		Rectangle bounds = torqueValueTextField.getBounds();
		torqueValueTextArea.setBounds((int)bounds.getX(), (int)bounds.getY()+(int)bounds.getHeight() + gap, 
				(int)bounds.getWidth(), textAreaH);
		
	}
	
	@Override
	protected int getTorquePositionY(int row, int height, int gap) {
		int y = viewProperty.getTorqueStartPositionY();
		return y + row * (height + classicProperty.getTorqueInfoAreaHeight() + gap);

	}
	

	
	public JComponent getSkipedProductPanel() {
		if(skippedProductPanel == null)
		{
			try {
				skippedProductPanel = new SingleColumnTablePanel("Skipped Product");
				skippedProductPanel.initialize(viewProperty);

			} catch (Exception e) {
				handleException(e);
			}			
		}

		return skippedProductPanel;

	}
	

	public JComponent getProcessedCounterTable() {
		if(processedCounterTablePanel == null)
		{
			try {
				processedCounterTablePanel = new ProcessedCounterTablePanel();
				processedCounterTablePanel.initialize(viewProperty);

			} catch (Exception e) {
				handleException(e);
			}			
		}
		return processedCounterTablePanel;

	}


}
