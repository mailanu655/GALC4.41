package com.honda.galc.client.schedule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.ProductionLotMbpnSequenceDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLotMbpnSequence;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
/**
 * <h3>Class description</h3>
 * This class displays information about current lot being processed. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jan 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130122</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class CurrentLotPanel extends JPanel implements EventSubscriber<SchedulingEvent>{

	private static final long serialVersionUID = 8713086501834048046L;
	private LabeledTextField lotNumberField = new LabeledTextField("Production Lot");
	private LabeledTextField lotSpecField = new LabeledTextField("SPEC");
	private LabeledTextField lotPositionField = new LabeledTextField("Position");
	private LabeledTextField carrierField = new LabeledTextField("Carrier");
	private LabeledTextField quantityField = new LabeledTextField("Quantity");
	protected ArrayList<ExpectedProductPanel> partSNList = new ArrayList<ExpectedProductPanel>();
	
	private boolean isMoveByKdLot;
	private boolean isProcessProduct;
	private boolean isShowProductLot;
	private boolean isChangeLotSize;
	private MultiValueObject<PreProductionLot> currentLot;
	private ObjectTablePane<MultiValueObject<PreProductionLot>> lotPanel;
	private ExpectedProductPanel productPane;
	private BuildAttributeCache buildAttribute;
	private String productType;
	private String mbpnProductTypes;
	private boolean isProcessMultiProduct;
	protected int maxNumOfPart = 4;
	
	public CurrentLotPanel(Map<String, Object> properties, BuildAttributeCache buildAttribute, String title) {
		super();
		this.setBorder(new TitledBorder(title));
		this.isMoveByKdLot = (Boolean)properties.get(DefaultScheduleClientProperty.IS_MOVE_BY_KD_LOT);
		isChangeLotSize = (Boolean)properties.get(DefaultScheduleClientProperty.CHANGE_LOT_SIZE);
		this.buildAttribute = buildAttribute;
		EventBus.subscribe(SchedulingEvent.class, this);
		initializePanels(properties);
		ViewUtil.setPreferredHeight(this, (Integer)properties.get(DefaultScheduleClientProperty.PANEL_HIGHT));
	}
	

	public MultiValueObject<PreProductionLot> getCurrentLot() {
		return currentLot;
	}

	public void setCurrentLot(MultiValueObject<PreProductionLot> currentLot) {
		this.currentLot = currentLot;
	}
	
	public List<PreProductionLot> getCurrentLots(){
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();
		
		if(this.isMoveByKdLot) {
			for (MultiValueObject<PreProductionLot> lot : lotPanel.getItems()) {
				lots.add(lot.getKeyObject());
			}
		} else {
			if(currentLot != null)
				lots.add(currentLot.getKeyObject());
		}
		return lots;
	}
	
	public void lotInfoChanged(List<MultiValueObject<PreProductionLot>> lots) {
		populateLotInformation(lots.size() == 0? null : lots.get(0));

		if(lotPanel != null) lotPanel.reloadData(lots);
	}
	
	public void populateLotInformation(MultiValueObject<PreProductionLot> lot) {
		currentLot = lot;
		if(lot != null) {
			if(!isShowProductLot){
				//show "Starting VIN" in the current lot panel
				lotNumberField.getComponent().setText(lot.getKeyObject().getStartProductId());
			}else{
				lotNumberField.getComponent().setText(lot.getKeyObject().getProductionLot());
			}
			lotSpecField.getComponent().setText(lot.getKeyObject().getProductSpecCode());
			if(isChangeLotSize()){
				lotPositionField.getComponent().setText(getLotPosition(lot.getKeyObject()));
			}else{
				//position for 2SD product is shown as "stampedCount/lotSize" 
				String lotPosition = "" + lot.getKeyObject().getStampedCount() + "/" + lot.getKeyObject().getLotSize();
				lotPositionField.getComponent().setText(lotPosition);
			}
		}
	}


	private String getLotPosition(PreProductionLot preProductionLot) {
		if(ProductType.KNUCKLE.name().equals(productType))
			return preProductionLot.getLotPosition();	
		else if(ProductTypeUtil.isSubProduct((ProductTypeCatalog.getProductType(productType)))){
			return (preProductionLot.getLotPosition(buildAttribute.findAttributeValue(preProductionLot.getProductSpecCode(),
					BuildAttributeTag.SUB_IDS, productType)));
			
		}else if(ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productType))){
			return	StringUtils.isEmpty(mbpnProductTypes) ? preProductionLot.getLotPosition() : preProductionLot.getLotPosition(mbpnProductTypes);
		} else 
			return preProductionLot.getLotPosition();
	}
	


	private void initializePanels(Map<String, Object> properties) {
		setLayout(new BorderLayout());
		add(createCurrentProductPanel(),BorderLayout.NORTH);
		JPanel prodPanel  = new JPanel();
		prodPanel.setLayout(new BoxLayout(prodPanel, BoxLayout.Y_AXIS));
		prodPanel.add(createLotPane(properties));
			
		isProcessProduct = (Boolean)properties.get(DefaultScheduleClientProperty.PROCESS_PRODUCT);
		isProcessMultiProduct =(Boolean)properties.get(DefaultScheduleClientProperty.PROCESS_MULTI_PRODUCT) ;
		productType = (String)properties.get(TagNames.PRODUCT_TYPE.name());
		mbpnProductTypes = (String)properties.get(TagNames.MBPN_PRODUCT_TYPES.name());
		if(isProcessProduct) {
			if(isProcessMultiProduct) {
				
				prodPanel.add(createCarrierPanel());
				for(int i=0 ;i<4 ;i++) {
					ExpectedProductPanel expectedProductPane =  (ExpectedProductPanel) createProductPane(properties);
					expectedProductPane.setVisible(false);
					partSNList.add(expectedProductPane);
					prodPanel.add(expectedProductPane);
				}
				
			}else {
				prodPanel.add(createProductPane(properties));
			}
		}
		add(prodPanel, BorderLayout.CENTER);
		isShowProductLot = (Boolean)properties.get(DefaultScheduleClientProperty.SHOW_CURRENT_PRODUCT_LOT);
		if(!isShowProductLot)
			lotNumberField.getLabel().setText("Starting VIN");
	}
	

	private JPanel createCurrentProductPanel() {
		initTextField(lotNumberField, 18);
		initTextField(lotSpecField,18);
		initTextField(lotPositionField,5);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(lotNumberField);
		panel.add(lotSpecField);
		panel.add(lotPositionField);
		return panel;
	}
	
	private void initTextField(LabeledTextField comp, int columnSize) {
		comp.setFont(Fonts.DIALOG_BOLD_22);
		comp.getComponent().setColumns(columnSize);
		comp.getComponent().setEditable(false);
		comp.getComponent().setBackground(Color.green);
		comp.getComponent().setHorizontalAlignment(JLabel.CENTER);
		comp.setInsets(0, 5, 5, 5);
	}
	
	private ObjectTablePane<MultiValueObject<PreProductionLot>> createLotPane(Map<String, Object> properties) {

		ColumnMappings columnMappings = ColumnMappings.with(
				(String[])properties.get(DefaultScheduleClientProperty.COLUMN_HEADINGS));
		
		lotPanel = new ObjectTablePane<MultiValueObject<PreProductionLot>>(
				(String)properties.get(DefaultScheduleClientProperty.PANEL_NAME),columnMappings.get());
	
		lotPanel = new ObjectTablePane<MultiValueObject<PreProductionLot>>(columnMappings.get());
		lotPanel.setAlignment(SwingConstants.CENTER);
		lotPanel.getTable().setFont((Font)properties.get(DefaultScheduleClientProperty.FONT));
		lotPanel.getTable().setRowHeight((Integer)properties.get(DefaultScheduleClientProperty.ROW_HEIGHT));
		lotPanel.getTable().setName("CurrentLotTable");
		return lotPanel;
	}
	
	@SuppressWarnings("unchecked")
	public void onEvent(SchedulingEvent event) {

		if(event.getEventType() == SchedulingEventType.CURRENT_ORDER_CHANGED){
			if(!(event.getTargetObject() instanceof List)) return;
			final List<MultiValueObject<PreProductionLot>> preProductionLots = (List<MultiValueObject<PreProductionLot>>) event.getTargetObject();
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
	//				lotInfoChanged(preProductionLots);
					if(isProcessMultiProduct ) {
						carrierSnTextSetFocus();
						resetProdScanPanel();
						populateProdScanPanel(true);
					}
				}
			});
		}else if(event.getEventType() == SchedulingEventType.EXPECTED_PRODUCT_CHANGED){
			if(isProcessProduct && productPane!=null){
				BaseProduct product = (BaseProduct) event.getTargetObject();
				if(product!=null){
					productPane.setExpectedProduct(product);
					productPane.populateData();
				}
			}
		}
	}

	public ObjectTablePane<MultiValueObject<PreProductionLot>> getLotPanel() {
		return lotPanel;
	}

	public void setLotPanel(ObjectTablePane<MultiValueObject<PreProductionLot>> lotPanel) {
		this.lotPanel = lotPanel;
	}
	

	private JPanel createProductPane(Map<String, Object> properties) {
		productPane = new ExpectedProductPanel((Integer)properties.get(TagNames.RESET.name()));
		Boolean checkDuplicateProductId = (Boolean)properties.get(DefaultScheduleClientProperty.CHECK_DUPLICATE_EXPECTED_PRODUCT_ID);
		checkDuplicateProductId = checkDuplicateProductId == null ? Boolean.FALSE: checkDuplicateProductId;
		productPane.setCheckDuplicateProductId(checkDuplicateProductId);
		return productPane;
	
	}

	public ExpectedProductPanel getProductPane() {
		return productPane;
	}

	public boolean isShowProductLot() {
		return isShowProductLot;
	}

	public boolean isChangeLotSize() {
		return isChangeLotSize;
	}

	private JPanel createCarrierPanel() {
		initTextField(carrierField, 18);
		initTextField(quantityField, 18);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		this.carrierField.setEnabled(true);
		this.carrierField.getComponent().setEditable(true);
		this.quantityField.getComponent().setText("4");
		
		panel.add(carrierField);
		panel.add(quantityField);
		return panel;
	}


	public LabeledTextField getCarrierField() {
		return carrierField;
	}


	public void setCarrierField(LabeledTextField carrierField) {
		this.carrierField = carrierField;
	}


	public LabeledTextField getQuantityField() {
		return quantityField;
	}


	public void setQuantityField(LabeledTextField quantityField) {
		this.quantityField = quantityField;
	}
	
	public List<ExpectedProductPanel> getPartSn() {
		return this.partSNList;
	} 
	
	
	private void populateProdScanPanel(boolean enable) {
		if(this.getCurrentLot()!= null) {
			String prodLot = this.getCurrentLot().getKeyObject().getProductionLot();
			List<ProductionLotMbpnSequence> expectedProductionLotMbpnSequences = ServiceFactory.getDao(ProductionLotMbpnSequenceDao.class).findAllByProductionLot(prodLot);
			
			int count = expectedProductionLotMbpnSequences.size();
			
			getQuantityField().getComponent().setText(Integer.toString(count));
			
			for(int i=0;i<count;i++) {
				renderFieldBeanInit(partSNList.get(i).getProductIdTextField(), enable);
				partSNList.get(i).setVisible(true);
				
				partSNList.get(i).getProductIdLabel().setText(getMbpnDesc(expectedProductionLotMbpnSequences.get(i).getMbpn()));
			}
			partSnTextSetFocus(partSNList.get(0).getProductIdTextField());
		}
	}
	
	private void resetProdScanPanel() {
				
		for(int i=0;i< maxNumOfPart;i++) {
			partSNList.get(i).setVisible(false);
			partSNList.get(i).getProductIdLabel().setText("");
		}
	}
	
	protected void renderFieldBeanInit(LengthFieldBean bean, boolean enable) {
		bean.setColor(Color.blue);
		bean.setBackground(Color.blue);
		bean.setDisabledTextColor( Color.black);
		bean.setForeground(Color.white);
		bean.setText("");
		bean.setEnabled(enable);
		bean.setEditable(enable);
		bean.setVisible(true);
	}
	
	protected void partSnTextSetFocus(LengthFieldBean partTextField) {
		partTextField.setText(" ");
		partTextField.setSelectionStart(0);
		partTextField.setSelectionEnd(partSNList.get(0).getProductIdTextField().getText().length());
		partTextField.requestFocus();
	}
	
	protected String getMbpnDesc(String mbnString) {
		String desc = mbnString;
		Mbpn mbpn = ServiceFactory.getDao(MbpnDao.class).findByKey(mbnString);
		if(mbpn != null)desc = StringUtils.isEmpty(mbpn.getDescription())?mbpn.getMbpn():mbpn.getDescription();
		
		return desc;
	}
	
	protected void carrierSnTextSetFocus() {		
		this.carrierField.getComponent().setText("");
		this.carrierField.getComponent().selectAll();
		this.carrierField.getComponent().requestFocus();
	}
	

}
