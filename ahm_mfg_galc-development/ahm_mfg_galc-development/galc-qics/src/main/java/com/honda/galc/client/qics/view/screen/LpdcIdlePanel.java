package com.honda.galc.client.qics.view.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.MultipleProductInputPanel;
import com.honda.galc.client.qics.view.fragments.UnitInfoIdlePanel;
import com.honda.galc.client.qics.view.fragments.MultipleProductInputPanel.Style;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.frame.QicsLpdcFrame;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>LpdcIdlePanel</code>
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
 * <TD>Pankaj Gopal</TD>
 * <TD>Apr 14, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Pankaj Gopal
 */

public class LpdcIdlePanel extends QicsPanel implements ActionListener,DataContainerListener {

	private static final long serialVersionUID = 1L;
	private static final int LABEL_WIDTH_SHORT = 340;
	private static final int TEXT_FIELD_WIDTH_SHORT = 140;
	private static final int ELEMENT_HEIGHT_SHORT = 40;
	private MultipleProductInputPanel multipleProductInputPanel1;
	private MultipleProductInputPanel multipleProductInputPanel2;
	private UnitInfoIdlePanel totalInspectedPanel;
	private UnitInfoIdlePanel directPassedPanel;
	private UnitInfoIdlePanel scrappedPanel;
	private UnitInfoIdlePanel preheatPanel;
	private JButton directPassAllButton;
	private JPanel calculationsGroupPanel;

	public LpdcIdlePanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	public void actionPerformed(ActionEvent e) {
		// process "Direct pass all" 
		getQicsFrame().clearMessage();
		getQicsFrame().clearStatusMessage();

		String field1 = getProductInputField(1).getText();
		String field2 = getProductInputField(2).getText();
		String field3 = getProductInputField(3).getText();
		String field4 = getProductInputField(4).getText();

		if(submitProduct(field1))
			getMultipleProductInputPanel1().directPassDataHandler1();

		if(submitProduct(field2))
			getMultipleProductInputPanel1().directPassDataHandler2();

		if(submitProduct(field3))
			getMultipleProductInputPanel2().directPassDataHandler1();

		if(submitProduct(field4))
			getMultipleProductInputPanel2().directPassDataHandler2();

		getQicsFrame().createDataCollectionCompleteDataContainer();
		getQicsFrame().displayIdleView();
		toggleDirectPassAllButton();
	}

	private boolean submitProduct(String productId) {
		try {
			getQicsController().submitLpdcProductForProcessing(productId);
			return true;
		} catch (Exception e) {
			getLogger().error(e, "Exception to submit product:" + productId);
			setErrorMessage(e.getMessage());
			return false;
		}
	}

	protected void mapAction() {
		getDirectPassAllButton().addActionListener(this);
	}

	protected void initialize() {
		setLayout(null);
		setSize(1024, 620);
		setEnabled(true);
		add(getClientNameLabel());
		add(getStationOneNameLabel());
		add(getMultipleProductInputPanel1());
		add(getStationTwoNameLabel());
		add(getMultipleProductInputPanel2());
		add(getCalculationsGroupPanel());
		add(getDirectPassAllButton());
		mapAction();
		getDirectPassAllButton().setEnabled(false);
	}

	protected JLabel getClientNameLabel() {
		JLabel clientNameLabel = new JLabel();
		clientNameLabel.setBounds(350, 10, 500, 50);
		clientNameLabel.setFont(new Font("Dialog", Font.BOLD, 36));
		clientNameLabel.setText("Initial Inspection");
		return clientNameLabel;
	}

	protected JLabel getStationOneNameLabel() {
		return setLabel("<----- Heads from LP 1", 50, 0);
	}

	protected JLabel getStationTwoNameLabel() {
		return setLabel("Heads from LP 2 ----->", 440, 730);
	}

	protected JLabel setLabel(String message, int coordinate, int hgap) {
		JLabel stationNameLabel = new JLabel();
		stationNameLabel.setBounds(10 + hgap, coordinate, 600, 50);
		stationNameLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		stationNameLabel.setText(message);
		return stationNameLabel;
	}


	protected MultipleProductInputPanel getMultipleProductInputPanel1() {
		if (multipleProductInputPanel1 == null) {
			multipleProductInputPanel1 = new MultipleProductInputPanel(this, 1, 0, Style.Two_Row, false);
			multipleProductInputPanel1.setName("mulitProductPanel1");
			multipleProductInputPanel1.setBounds(10, 100, 1000, 150);
		}
		return multipleProductInputPanel1;
	}

	protected MultipleProductInputPanel getMultipleProductInputPanel2() {
		if (multipleProductInputPanel2 == null) {
			multipleProductInputPanel2 = new MultipleProductInputPanel(this, 2, 1,Style.Two_Row, true);
			multipleProductInputPanel2.setName("mulitProductPanel2");
			multipleProductInputPanel2.setBounds(10, 480, 1000, 150);
		}
		return multipleProductInputPanel2;
	}
	protected JPanel getCalculationsGroupPanel() {
		if (calculationsGroupPanel == null) {
			calculationsGroupPanel = new JPanel();
			calculationsGroupPanel.setLayout(new GridLayout(4, 1));
			calculationsGroupPanel.setBounds(10, 260, 500, 190);
			calculationsGroupPanel.add(getTotalInspectedPanel());
			calculationsGroupPanel.add(getDirectPassedPanel());
			calculationsGroupPanel.add(getScrappedPanel());
			calculationsGroupPanel.add(getPreheatPanel());
		}
		return calculationsGroupPanel;
	}

	public JButton getDirectPassAllButton() {
		if (directPassAllButton == null) {
			directPassAllButton = new JButton("DIRECT PASS ALL");
			directPassAllButton.setBounds(550, 275, 400, 150);
			directPassAllButton.setFont(new Font("Dialog", Font.BOLD, 28));
		}
		return directPassAllButton;
	}

	protected void setStationInfoValues() {
		StationResult result = getQicsController().getClientModel().getStationResult();
		if (result == null) {
			return;
		}
		getTotalInspectedPanel().setText(toString(result.getProductIdInspect()));
		getDirectPassedPanel().setText(toString(result.getProductIdDirectPassed()));
		getScrappedPanel().setText(toString(result.getScrap()));
		getPreheatPanel().setText(toString(result.getPreheat()));
	}

	public void toggleDirectPassAllButton() {
		if (!getProductInputField(1).isEditable()) {
			if (!getProductInputField(2).isEditable()) {
				if (!getProductInputField(3).isEditable()) {
					if (!getProductInputField(4).isEditable()) {
						if(getQicsPropertyBean().isLpdcDirectPassAllButtonEnabled())
							getDirectPassAllButton().setEnabled(true);
						return;
					}
				}
			}
		}
		getDirectPassAllButton().setEnabled(false);
	}

	public boolean isReadyForInput(JTextField field) {
		// TODO consider using bool for controlling input ready state
		Color color = field.getBackground();
		return (color.equals(Color.BLUE) || color.equals(Color.YELLOW)) ? true : false;
	}

	protected UnitInfoIdlePanel getTotalInspectedPanel() {
		if (totalInspectedPanel == null) {
			String labelText = getQicsController().getProductTypeData().getProductTypeLabel() + "s Inspected";
			totalInspectedPanel = createShortUnitInfoIdlePanel(labelText);
		}
		return totalInspectedPanel;
	}

	protected UnitInfoIdlePanel getDirectPassedPanel() {
		if (directPassedPanel == null) {
			String labelText = getQicsController().getProductTypeData().getProductTypeLabel() + "s Direct Passed";
			directPassedPanel = createShortUnitInfoIdlePanel(labelText);
		}
		return directPassedPanel;
	}

	protected UnitInfoIdlePanel getScrappedPanel() {
		if (scrappedPanel == null) {
			String labelText = getQicsController().getProductTypeData().getProductTypeLabel() + "s Scrapped";
			scrappedPanel = createShortUnitInfoIdlePanel(labelText);
		}
		return scrappedPanel;
	}

	protected UnitInfoIdlePanel getPreheatPanel() {
		if (preheatPanel == null) {
			String labelText = getQicsController().getProductTypeData().getProductTypeLabel() + "s Preheat";
			preheatPanel = createShortUnitInfoIdlePanel(labelText);
		}
		return preheatPanel;
	}

	protected UnitInfoIdlePanel createShortUnitInfoIdlePanel(String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH_SHORT, TEXT_FIELD_WIDTH_SHORT, ELEMENT_HEIGHT_SHORT);
	}

	protected String toString(String str) {
		return str == null ? "" : str.trim();
	}

	protected String toString(Object anObject) {
		return anObject == null ? "" : anObject.toString();
	}

	public JTextField getProductInputField(int id) {
		
		switch(id) {
		case 1: return getMultipleProductInputPanel1().getSingleProductInputField1();
		case 2: return getMultipleProductInputPanel1().getSingleProductInputField2();
		case 3: return getMultipleProductInputPanel2().getSingleProductInputField1();
		case 4: return getMultipleProductInputPanel2().getSingleProductInputField2();
		}
		
		return null;
		
	}

	// public SubmitButtonsPanel getDirectPassButton() {
	// return getSubmitButtonsPanel();
	// }

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.IDLE;
	}

	@Override
	public void startPanel() {
		setStationInfoValues();
	}

	@Override
	public void setButtonsState() {
	}

	// received product ids from PLC
	public DataContainer received(DataContainer dc) {

		Logger.getLogger().info("Receive data container from device:" + dc);
		DataContainer retDC = new DefaultDataContainer();
		getQicsLpdcFrame().setProcesssingMultipleProduct(true);
		
		boolean result = populateProductInputs(dc);
		
		if(!result){
			dc = convertPlcDataTag(dc);
			populateProductInputs(dc);
		}
		
		getQicsLpdcFrame().setProcesssingMultipleProduct(false);
		
		return retDC;
	}

	private boolean  populateProductInputs(DataContainer dc) {
		boolean populated = false;
		for(int i = 1;i<=4;i++){
			String productId = StringUtils.trim(dc.getString("PRODUCT_ID_" + i));
			if(!StringUtils.isEmpty(productId)&& isReadyForInput(getProductInputField(i))) {
				getProductInputField(i).setText(productId);
				getProductInputField(i).postActionEvent();
				populated = true;
			}
		}
		
		return populated;
	}

	private DataContainer convertPlcDataTag(DataContainer dc) {
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(dc.getClientID());
		return device.convertDeviceTag(dc);
	
	}
	
	protected QicsLpdcFrame getQicsLpdcFrame(){
		return (QicsLpdcFrame)window;
	}
}
