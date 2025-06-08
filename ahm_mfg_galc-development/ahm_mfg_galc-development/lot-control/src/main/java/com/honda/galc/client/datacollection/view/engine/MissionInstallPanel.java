package com.honda.galc.client.datacollection.view.engine;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JLabel;

import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.view.DefaultDataCollectionPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;

public class MissionInstallPanel extends DefaultDataCollectionPanel {
	private static final long serialVersionUID = 1L;
	public static int maxMissingPart = 1;
	
	public MissionInstallPanel(DefaultViewProperty property, int winWidth,
			int winHeight) {
		super(property, winWidth, winHeight);
		
		
	}

	@Override
	protected void initComponents() {
		super.initComponents();
		
		int x = -10;
		Rectangle bounds = getTextFieldProdId().getBounds();
		
		for(int i = 0; i < maxMissingPart; i++){
			JLabel label = getMissingReqPartLabel();
			label.setBounds(x, bounds.y + (i +2)*(bounds.height + gap), bounds.x, bounds.height);
			missingRequiredPartLabelList.add(label);
			
			
			UpperCaseFieldBean bean = getMissingPartField();
			bean.setBounds(bounds.x, bounds.y + (i + 2) * (bounds.height + gap), 
					bounds.width -viewProperty.getStatusLabelWidth() - gap, bounds.height);
			missingRequiredPartList.add(bean);
		}
	}
	
	
	@Override
	protected void initPanel() {
		super.initPanel();
		
		for(int i = 0; i < maxMissingPart; i++){
			add(getMissingReqPartLabel(i), getMissingReqPartLabel(i).getName());
			add(getMissingPartField(i), getMissingPartField(i).getName());
		}
	}

	public JLabel getMissingReqPartLabel(int index) {
		return missingRequiredPartLabelList.get(index);
	}
	
	public UpperCaseFieldBean getMissingPartField(int index){
		return missingRequiredPartList.get(index);
	}
	
	private JLabel getMissingReqPartLabel() {
		JLabel labelMissingRequiredPart = new JLabel("missingPartLabel");
		labelMissingRequiredPart.setFont(Fonts.DIALOG_PLAIN_18);
		labelMissingRequiredPart.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
		labelMissingRequiredPart.setForeground(Color.RED);
		labelMissingRequiredPart.setVisible(true);
		return labelMissingRequiredPart;
	}

	private UpperCaseFieldBean getMissingPartField() {
		UpperCaseFieldBean textFieldMissingRequiredPart = new UpperCaseFieldBean();
		textFieldMissingRequiredPart.setName("JTextFieldRequredPart" );
		textFieldMissingRequiredPart.setEditable(false);
		textFieldMissingRequiredPart.setEnabled(true);
		textFieldMissingRequiredPart.setFont(Fonts.DIALOG_PLAIN_36);
		textFieldMissingRequiredPart.setBackground(Color.white);
		textFieldMissingRequiredPart.setVisible(true);
		return textFieldMissingRequiredPart;
	}
	
	

}
