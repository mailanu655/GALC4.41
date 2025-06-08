package com.honda.galc.client.teamleader.knuckle;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.KnucklePreProductionLotTableModel;
import com.honda.galc.client.ui.tablemodel.SubProductTableModel;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.util.SortedArrayList;


public class KsnRecreationPanel extends ApplicationMainPanel implements ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private TablePane preProductionLotPane;
	private TablePane subProductPane;
	
	private JButton recreateButton = new JButton("Recreate");
	
	
	private KnucklePreProductionLotTableModel preProdLotTableModel;
	private SubProductTableModel subProductTableModel;
	
	private List<PreProductionLot> preProductionLots = new ArrayList<PreProductionLot>();
	private List<SubProduct> subProducts = new ArrayList<SubProduct>();
	
	BuildAttributeCache buildAttributeCache;
	
	public KsnRecreationPanel(MainWindow window) {
		super(window);
		
		initComponents();
		
		addListeners();
		loadData();
	}

	private void initComponents() {
		
		setLayout(new BorderLayout());
		
		add(createKdLotPane(),BorderLayout.NORTH);
		add(createSubProductPanel(),BorderLayout.CENTER);
		add(createButtonPanel(),BorderLayout.SOUTH);
		
	}
	

	private void addListeners() {
		
		preProductionLotPane.addListSelectionListener(this);
		recreateButton.addActionListener(this);
		
	}


	
	private TablePane createKdLotPane() {
		
		preProductionLotPane = new TablePane("Pre-Production KD Lot List(KSNs To Be Recreated)");
		preProdLotTableModel = new KnucklePreProductionLotTableModel(preProductionLotPane.getTable(),true,preProductionLots);
		preProductionLotPane.setPreferredHeight(80);
		preProductionLotPane.setMaxHeight(80);
		return preProductionLotPane;
		
	}
	
	private TablePane createSubProductPanel() {
		
		subProductPane = new TablePane("Knucle Serial Number List",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		subProductTableModel = new SubProductTableModel(subProductPane.getTable(),subProducts);
		return subProductPane;
	}
	
	private JPanel createButtonPanel() {
		
		JPanel panel = new JPanel();
		recreateButton.setFont(Fonts.DIALOG_BOLD_20);
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalGlue());
		panel.add(recreateButton);
		panel.add(Box.createHorizontalGlue());
		return panel;
		
	}
	
	private void loadData() {
		
		preProductionLots = getDao(PreProductionLotDao.class).findAllWithIncorrectKsns();
		subProducts = new ArrayList<SubProduct>();
		setKnucklePartData(preProductionLots);
		
		subProductTableModel.refresh(subProducts);
		preProdLotTableModel.refresh(preProductionLots);
		if(!preProductionLots.isEmpty())preProductionLotPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
		
	}
	
	private void setKnucklePartData(List<PreProductionLot> prodLots) {
	
		if(prodLots.isEmpty()) return;
		
		buildAttributeCache = new BuildAttributeCache(
				BuildAttributeTag.KNUCKLE_LEFT_SIDE,
				BuildAttributeTag.KNUCKLE_RIGHT_SIDE,
				BuildAttributeTag.KNUCKLE_PART_MARK_LEFT,
				BuildAttributeTag.KNUCKLE_PART_MARK_RIGHT);
		
		for(PreProductionLot item : prodLots) {
			
			String partNumberLeft  = buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_LEFT_SIDE);
			String partNumberRight =  buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_RIGHT_SIDE);
			String partMarkLeft  = buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_PART_MARK_LEFT);
			String partMarkRight =  buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_PART_MARK_RIGHT);
			
			item.setPartNumber(partNumberLeft + " , " + partNumberRight);
			item.setPartMark(partMarkLeft + " , " + partMarkRight);
		}
		
		
	}

	public void valueChanged(ListSelectionEvent e) {
		
		List<PreProductionLot> selectedLots= preProdLotTableModel.getSelectedProductionLots();
		
		subProducts.clear();
		if(selectedLots.isEmpty()) return;
		for(PreProductionLot preProdLot : selectedLots) {
			subProducts.addAll(findAll(preProdLot.getProductionLot()));
		}
		subProductTableModel.refresh(subProducts);
	
	}
	
	private List<SubProduct> findAll(String productionLot) {
		
		List<SubProduct> allSubProducts = getDao(SubProductDao.class).findAllByProductionLot(productionLot);
		List<SubProduct> leftItems = new SortedArrayList<SubProduct>("getProductId");
		List<SubProduct> rightItems = new SortedArrayList<SubProduct>("getProductId");
		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		for(SubProduct item : allSubProducts) {
			if(item.getSubId().equals(Product.SUB_ID_LEFT))
				leftItems.add(item);
			else rightItems.add(item);
		}
		
		int count = leftItems.size()>= rightItems.size() ? leftItems.size() : rightItems.size();
		
		for(int i = 0; i<count;i++) {
			
			if(i<leftItems.size())
				subProducts.add(leftItems.get(i));
			if(i<rightItems.size())
				subProducts.add(rightItems.get(i));
		}
		return subProducts;
	}

	public void actionPerformed(ActionEvent e) {
		
		doRecreate();
		
	}

	private void doRecreate() {
		
		List<PreProductionLot> selectedLots= preProdLotTableModel.getSelectedProductionLots();
		
		if(selectedLots.isEmpty()) return;
		
		for(PreProductionLot item : selectedLots) {
			
			recreateLot(item);
			
		}
		
		loadData();
		
	}

	private void recreateLot(PreProductionLot item) {
		
		SubProduct subProductLeft = testPartNumberMatch(item, Product.SUB_ID_LEFT);
		if(subProductLeft != null) 
			recreateLot(item,subProductLeft);
		
		SubProduct subProductRight = testPartNumberMatch(item, Product.SUB_ID_RIGHT);
		if(subProductRight != null) 
			recreateLot(item,subProductRight);
			
	}
	
	private void recreateLot(PreProductionLot item, SubProduct subProduct) {
		
		BuildAttribute buildAttribute = buildAttributeCache.findById(item.getProductSpecCode(), getPartNumberTag(subProduct.getSubId()));
		if(buildAttribute == null){
			setErrorMessage("build attribute (" + item.getProductSpecCode() + "," +getPartNumberTag(subProduct.getSubId()) + ") does not exist");
			return;
		}
		
		String yearCode = ProductSpec.excludeToModelYearCode(item.getProductSpecCode());
		
		try{
			getDao(PreProductionLotDao.class).recreateKnuckleSerialNumbers(subProduct, buildAttribute.getAttributeValue()+yearCode);
			setMessage("recreate ksns for production lot " + item.getProductionLot() + " successfully");
			getLogger().info("recreate ksns for production lot " + item.getProductionLot() + " successfully");
		}catch(Exception e) {
			setErrorMessage("Could not recreate ksns due to " + e.getMessage());
			
			getLogger().error(e,"Could not recreate ksns");
		}
		
		
	}

	private SubProduct testPartNumberMatch(PreProductionLot preProdLot,String side) {
		
		for(SubProduct item : subProducts) {
			
			if(item.getProductionLot().equals(preProdLot.getProductionLot()) && item.getSubId().equals(side)) {
				
				BuildAttribute buildAttribute = buildAttributeCache.findById(preProdLot.getProductSpecCode(), getPartNumberTag(side));
				if(!item.getProductId().substring(0, 10).equals(buildAttribute.getAttributeValue())) return item;
			}
		}
		return null;
	}
	
	
	private String getPartNumberTag(String side) {
		
		return side.equals(Product.SUB_ID_LEFT) ?
				BuildAttributeTag.KNUCKLE_LEFT_SIDE :
				BuildAttributeTag.KNUCKLE_RIGHT_SIDE;
	}
	


}
