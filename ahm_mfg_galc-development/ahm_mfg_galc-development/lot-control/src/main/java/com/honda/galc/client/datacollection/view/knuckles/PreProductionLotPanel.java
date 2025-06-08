package com.honda.galc.client.datacollection.view.knuckles;

import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.knuckles.KnucklesOnSequenceManager;
import com.honda.galc.client.datacollection.observer.knuckles.LotControlKnucklesPersistenceManagerExt;
import com.honda.galc.client.datacollection.property.KnucklePropertyBean;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.utils.ColorUtil;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>PreProductionLotPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PreProductionLotPanel description </p>
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
 * Nov 27, 2010
 *
 */

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class PreProductionLotPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel lotPlan;
	private JLabel actual = new JLabel("Actual");
	
	private LabeledTextField leftField;
	private LabeledTextField rightField;
	private JTextField lotPlanField;
	private int panelwidth = 230;
	private int panelhight = 116;
	public static final Font FONT = new Font("Dialog", Font.BOLD, 22);
	Dimension fieldDimension = new Dimension(180, 45);
	private SubProductDao subProductDao;
	
	public PreProductionLotPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		setName(this.getClass().getSimpleName());
		setLayout(null);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		setSize(panelwidth, panelhight);
		
		add(getLotPlan());
		add(getActual());
		add(getLotPlanField());
		add(getLeftField());
		add(getRightField());
	}
	
	
	public LabeledTextField getLeftField() {
		if(leftField == null){
			leftField = new LabeledTextField(" Left", true);
			leftField.getComponent().setColumns(4);
			leftField.setInsets(1, 10, 1, 10);
			leftField.setFont(new Font("Courier", Font.BOLD, 16));
			leftField.getComponent().setFont(Fonts.DIALOG_BOLD_20);
			leftField.getComponent().setBackground(ColorUtil.getColor(getKnuckleProperty().getSubidColorMap().get("L")));
		}
		return leftField;
	}
	
	public LabeledTextField getRightField() {
		if(rightField == null){
			rightField = new LabeledTextField("Right", true);
			rightField.getComponent().setColumns(4);
			rightField.setInsets(1, 10, 1, 10);
			rightField.setFont(new Font("Courier", Font.BOLD, 16));
			rightField.getComponent().setFont(Fonts.DIALOG_BOLD_20);
			rightField.getComponent().setBackground(ColorUtil.getColor(getKnuckleProperty().getSubidColorMap().get("R")));
		}
		return rightField;
	}

	public JTextField getLotPlanField() {
		if(lotPlanField == null){
			lotPlanField = new JTextField();
			lotPlanField.setColumns(5);
			lotPlanField.setFont(Fonts.DIALOG_BOLD_24);
		}
		return lotPlanField;
	}

	

	public JLabel getActual() {
		if(actual == null){
			actual = new JLabel("Actual");
			actual.setFont(Fonts.DIALOG_BOLD_14);
		}
		return actual;
	}

	public JLabel getLotPlan() {
		if(lotPlan == null){
			lotPlan = new JLabel("Lot Plan");
			lotPlan.setFont(Fonts.DIALOG_BOLD_14);
		}
		return lotPlan;
	}

	public void initScreen(int y1){
		getLotPlan().setBounds(25, 0, 100, 30);
		getActual().setBounds(120, 0, 100, 30);
		getLotPlanField().setBounds(25, 35, 80, 50);
		getLeftField().setBounds(100, 25, 120, 30);
		getRightField().setBounds(100, 60 , 120, 30);
		setLocation(2, y1);
	}
	
	public void setData(ClientContext context) {
				
		if(context.getCurrentPreProductionLot() == null){
			setData(context.getCurrentPreProductionLot(), "" , "" );
			return;
		}
		
		if(context.isOnLine() && getKnuckleProperty().isUseSubproductPassingCount()){
			List<KeyValue> passingSubProductList = getSubProductDao().findPassingSubProduct(
					context.getCurrentPreProductionLot().getProductionLot(), 
					getKnuckleProperty().getOifLastPassingProcessPoint());
			
			setData(context.getCurrentPreProductionLot(), passingSubProductList);

		} else {
			KnucklesOnSequenceManager knDbMgr = getKnuckleOnSeqManager(context);
			setData(context.getCurrentPreProductionLot(), "" + knDbMgr.getLeftPassingCount(), "" + knDbMgr.getRightPassingCount());
		}
		
	}

	private void setData(PreProductionLot currentPreProductionLot, List<KeyValue> passingSubProductList) {
		String leftCount = "0";
		String rightCount = "0";
		if(passingSubProductList != null){
			for(KeyValue kv : passingSubProductList){
				if(kv.getKey() == null) continue;
				if(Product.SUB_ID_LEFT.equals(kv.getKey().toString().trim()))
					leftCount = kv.getValue().toString();
				else if(Product.SUB_ID_RIGHT.equals(kv.getKey().toString().trim()))
					rightCount = kv.getValue().toString();
			}
		} 
		
		setData(currentPreProductionLot, leftCount, rightCount);
		
	}

	private SubProductDao getSubProductDao() {
		if(subProductDao == null)
			subProductDao = ServiceFactory.getDao(SubProductDao.class);
		
		return subProductDao;
		
	}

	private KnucklesOnSequenceManager getKnuckleOnSeqManager(ClientContext context) {
		LotControlKnucklesPersistenceManagerExt dbmgr = (LotControlKnucklesPersistenceManagerExt)context.getDbManager();
		KnucklesOnSequenceManager knDbMgr = (KnucklesOnSequenceManager)dbmgr.getExpectedProductManger();
		return knDbMgr;
	}
	
	public void setData(PreProductionLot preProductionLot) {
		setData(preProductionLot, "","");
	}
	
	public void setData(PreProductionLot preProductionLot, String leftPassingCount, String rightPassingCount) {
		if(preProductionLot != null)
			lotPlanField.setText(preProductionLot.getLotPosition());
		else
			lotPlanField.setText("");
			
		
		getLeftField().getComponent().setText(leftPassingCount);
		getRightField().getComponent().setText(rightPassingCount);
	}

	public KnucklePropertyBean getKnuckleProperty(){
		return PropertyService.getPropertyBean(KnucklePropertyBean.class, 
				DataCollectionController.getInstance().getClientContext().getProcessPointId());
	}
	
}
