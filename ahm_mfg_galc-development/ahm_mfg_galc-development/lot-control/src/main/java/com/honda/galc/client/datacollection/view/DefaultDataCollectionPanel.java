package com.honda.galc.client.datacollection.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.utils.ColorUtil;

/**
 * 
 * <h3>DefaultDataCollectionPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DefaultDataCollectionPanel description </p>
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
 * @author Paul Chou
 * Nov 15, 2010
 *
 */

@SuppressWarnings("serial")
public class DefaultDataCollectionPanel extends DataCollectionPanel {
	protected SingleColumnTablePanel skipedProductPanel = null;
	protected ProcessedCounterTablePanel processedCounterTablePanel = null;

	public DefaultDataCollectionPanel(DefaultViewProperty property, int winWidth, int winHeight) {
		super(property, winWidth, winHeight);
	}


	@Override
	public void setProductSpecBackGroudColor(String colorName) {

		if(!StringUtils.isEmpty(colorName))
			getTextFieldExpPidOrProdSpec().setBackground(ColorUtil.getColor(colorName));
		else
			super.setProductSpecBackGroudColor(colorName);
	}

	public JComponent getSkipedProductPanel() {
		if(skipedProductPanel == null)
		{
			try {
				skipedProductPanel = new SingleColumnTablePanel("Skipped Product");
				skipedProductPanel.initialize(viewProperty);

			} catch (Exception e) {
				handleException(e);
			}			
		}

		return skipedProductPanel;

	}

	@Override
	protected void initPanel() {
		super.initPanel();
		
		if(viewProperty.isMonitorSkippedProduct()){
			add(getSkipedProductPanel());
			this.addSkippedProductListeners();
		}
		
		if(viewProperty.isShowProcessedCounter())
			add(getProcessedCounterTable());
			
	}
	
	private void addSkippedProductListeners(){
		this.skipedProductPanel.getTable().addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if(e.getClickCount() == 2){
					getCurrentField().requestFocus();
				}
			}
		});
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
