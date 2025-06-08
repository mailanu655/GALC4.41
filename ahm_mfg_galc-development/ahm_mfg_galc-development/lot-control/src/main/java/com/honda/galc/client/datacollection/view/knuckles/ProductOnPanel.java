package com.honda.galc.client.datacollection.view.knuckles;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JLabel;

import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.view.DefaultDataCollectionPanel;
import com.honda.galc.client.datacollection.view.SingleColumnTablePanel;
import com.honda.galc.client.datacollection.view.SkippedProductTablePanel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
/**
 * 
 * <h3>ProductOnPanel1</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductOnPanel1 description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jan 25, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 25, 2012
 */
public class ProductOnPanel extends DefaultDataCollectionPanel{
	private static final long serialVersionUID = 1L;
	
	private PreProductionLotPanel preProductionLotScreen;
	private JLabel kdLotLabel;
	private UpperCaseFieldBean kdLotField;
	
	
	public ProductOnPanel(DefaultViewProperty property, int winWidth, int winHeight) {
		super(property, winWidth, winHeight);
	}

	@Override
	protected void initPanel() {
		// TODO Auto-generated method stub
		super.initPanel();
		add(getKdLotLabel(), getKdLotLabel().getName());
		add(getKdLotField(), getKdLotField().getName());
		add(getPreProductionLotScreen(), getPreProductionLotScreen().getName());
		Rectangle bounds = getButton(2).getBounds();
		getButton(2).setBounds(new Rectangle(bounds.x -18, bounds.y, bounds.width + 26, bounds.height));
	}

	public PreProductionLotPanel getPreProductionLotScreen() {
		if(preProductionLotScreen == null){
			preProductionLotScreen = new PreProductionLotPanel();
			Rectangle expPidBounds = getTextFieldExpPidOrProdSpec().getBounds();
			preProductionLotScreen.initScreen((int)expPidBounds.getY()+2);
		}
		return preProductionLotScreen;
	} 
	
	public UpperCaseFieldBean getKdLotField() {
		if(kdLotField == null){
			Rectangle bounds = getTextFieldExpPidOrProdSpec().getBounds();
			kdLotField = new UpperCaseFieldBean();
			kdLotField.setName("KnuckleMtocField");
			kdLotField.setColor(Color.white);
			kdLotField.setBackground(Color.white);
			kdLotField.setFont(new java.awt.Font("dialog", 0, viewProperty.getPartSnFontSize()));
			kdLotField.setColumns(30);
			kdLotField.setBounds(bounds.x, bounds.y - bounds.height, 
					bounds.width, bounds.height);
			kdLotField.setEditable(false);
		}
		return kdLotField;
	}

	public JLabel getKdLotLabel() {
		
		if(kdLotLabel == null){
			Rectangle bounds = getLabelExpPIDOrProdSpec().getBounds();
			kdLotLabel = new JLabel();
			kdLotLabel.setName("KnuckleMtocLabel");
			kdLotLabel.setFont(new java.awt.Font("dialog", 0, viewProperty.getPartLabelFontSize()));
			kdLotLabel.setText("KD LOT:");
			kdLotLabel.setBounds(bounds.x, bounds.y - bounds.height, bounds.width, bounds.height);
			kdLotLabel.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			kdLotLabel.setForeground(java.awt.Color.black);
		}
		return kdLotLabel;
	}
	
	

	@Override
	public SingleColumnTablePanel getSkipedProductPanel() {
		
		if(skipedProductPanel == null)
		{
			try {
				skipedProductPanel = new SkippedProductTablePanel("Skipped Product");
				skipedProductPanel.initialize(viewProperty);

			} catch (Exception e) {
				handleException(e);
			}			
		}

		return skipedProductPanel;
	}

	@Override
	protected void setTextFieldSubIdBounds() {
		Rectangle refbounds = getKdLotField().getBounds();
		Rectangle prodBounds = getTextFieldProdId().getBounds();
		this.textFieldSubId.setBounds(refbounds.x + refbounds.width + 2*gap, refbounds.y, prodBounds.width - refbounds.width - 2*gap, refbounds.height);
	}

	@Override
	protected Rectangle getLabelExpPidOrProdSpecRefrenceBounds() {
		 Rectangle bounds = getLabelProdId().getBounds();
		 return new Rectangle(bounds.x, bounds.y + 46, bounds.width, bounds.height);
	}

	@Override
	protected Rectangle getTextFieldExpPidOrProdSpecRefrenceBounds() {
		Rectangle bounds = getTextFieldProdId().getBounds();
		return new Rectangle(bounds.x, bounds.y +46, bounds.width, bounds.height);
	}

}
