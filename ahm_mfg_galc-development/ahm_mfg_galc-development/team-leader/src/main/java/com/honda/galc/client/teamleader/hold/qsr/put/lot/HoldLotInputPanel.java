package com.honda.galc.client.teamleader.hold.qsr.put.lot;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.InputPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.FilteredLabeledComboBox;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.entity.product.Product;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

public class HoldLotInputPanel extends InputPanel {

	private static final long serialVersionUID = 1L;

	private LabeledComboBox inProcDptComboBox;
	private LabeledComboBox prodLotSearchComboBox;
	private FilteredLabeledComboBox prodLotComboBox;
	private ProductDao productDao;
	private LineDao lineDao;
	private Map<String,List<String>> trackingStatusByDptMap;
	private HoldLotPanel parentPanel;
	private ProductType selectedProductType;
	private String selectedInProcessDpt;
	
	private static final String DEFAULT_IN_PROC_DPT_VALUE = "-- Any --"; 
	
	public HoldLotInputPanel(HoldLotPanel parentPanel) {
		super(parentPanel);
		this.parentPanel = parentPanel;
	}

	@Override
	protected void initView() {
		super.initView();
		remove(getDepartmentElement());
		remove(getProductTypeElement());
		setLayout(new MigLayout("insets 3", "[fill]5[fill]5[fill]5[fill]", ""));
		add(getDepartmentElement(), "width 200::, height 30");
		add(getProductTypeElement(), "width 200::, height 30");
		add(getInProcDptComboBox(), "width 170::, height 30");
		add(getProdLotComboBox(), "cell 3 0, width 400::, height 30, hidemode 3");
		add(getProdLotSearchComboBox(), "cell 3 0, width 400::, height 30, hidemode 3");
	}
	
	protected LabeledComboBox getInProcDptComboBox() {
		if (inProcDptComboBox == null) {
			inProcDptComboBox = new LabeledComboBox("In Dpt: ");
			inProcDptComboBox.setEnabled(false);
		}
		return inProcDptComboBox;
	}
	
	protected FilteredLabeledComboBox getProdLotComboBox() {
		if (prodLotComboBox == null) {
			prodLotComboBox = new FilteredLabeledComboBox("Production Lot: ");
			prodLotComboBox.getComponent().setName("ProdLotComboBox");
			prodLotComboBox.setFont(Fonts.DIALOG_BOLD_14);
			prodLotComboBox.setEnabled(false);
			prodLotComboBox.setVisible(false);
		}
		return prodLotComboBox;
	}
	
	protected LabeledComboBox getProdLotSearchComboBox() {
		if (prodLotSearchComboBox == null) {
			prodLotSearchComboBox = new LabeledComboBox("Production Lot: ");
			prodLotSearchComboBox.getComponent().setName("ProdLotSearchComboBox");
			prodLotSearchComboBox.getComponent().setEditable(true);
			prodLotSearchComboBox.setFont(Fonts.DIALOG_BOLD_14);
			prodLotSearchComboBox.setEnabled(false);
			prodLotSearchComboBox.setVisible(true);
		}
		return prodLotSearchComboBox;
	}
	
	@Override
	protected void mapActions() {
		super.mapActions();
		getProductTypeComboBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedProductType = (ProductType)getProductTypeComboBox().getSelectedItem();
				if (selectedProductType == null) {
					getInProcDptComboBox().getComponent().setSelectedIndex(-1);
					return;
				}
				parentPanel.getLogger().info(selectedProductType.toString() + " product type selected");
				loadInProcDivisions();
				resetPanel();
			}
		});
		getInProcDptComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedInProcessDpt = (String)getInProcDptComboBox().getComponent().getSelectedItem();
				setProdLotElementVisibility();
				if (selectedInProcessDpt == null) return;
				parentPanel.getLogger().info(selectedInProcessDpt.toString() + " in-process department selected");
				resetPanel();	
			}
		});	
	}
	
	@SuppressWarnings("unchecked")
	private void loadInProcDivisions() {
		ArrayList<String> divisionIds = new ArrayList<String>();
		if (getProductTypeComboBox() == null || selectedProductType == null) {
			getInProcDptComboBox().setEnabled(false);
			return;
		}
		List<Line> trackingLines = getLineDao().findAllTrackingLinesByProductType(selectedProductType.toString());
		Map<String,Division> divisionMap = new HashMap<String,Division>();
		for (Line trackingLine : trackingLines) {
			String divisionId = trackingLine.getDivisionId();
			if (divisionMap.containsKey(divisionId)) continue;
			Division division = ServiceFactory.getDao(DivisionDao.class).findByDivisionId(divisionId);
			if (division != null) divisionMap.put(divisionId, division);
		}
		
		List<Division> divisionList = new ArrayList<Division>(divisionMap.values());
		Collections.sort(divisionList, new Comparator<Division>(){
			public int compare(Division o1, Division o2){
				return o1.getSequenceNumber() - o2.getSequenceNumber();
			}
		});
		for (Division division : divisionList) {
			divisionIds.add(division.getDivisionId());
		}
		divisionIds.add(0,DEFAULT_IN_PROC_DPT_VALUE);
		
		getInProcDptComboBox().getComponent().setModel(new DefaultComboBoxModel<String>(new Vector<String>(divisionIds)));
		getInProcDptComboBox().getComponent().setSelectedIndex(0);
		getInProcDptComboBox().setEnabled(true);
	}
	
	@SuppressWarnings("unchecked")
	protected void loadSearchProductionLots(String searchValue) {
		if (StringUtils.isBlank(searchValue)) return;
		List<String> preProdLotList = getProductDao().findProductionLotNumbersBySubstring(searchValue.trim());
		Collections.sort(preProdLotList, Collections.reverseOrder());
		getProdLotSearchComboBox().getComponent().setModel(new DefaultComboBoxModel<String>(new Vector<String>(preProdLotList)));
	}
	
	protected void resetPanel() {
		if (selectedInProcessDpt == null) return;
		if (selectedInProcessDpt.contentEquals(DEFAULT_IN_PROC_DPT_VALUE)){
			getProdLotSearchComboBox().getComponent().getEditor().setItem("");
			getProdLotSearchComboBox().getComponent().removeAllItems();
		} else {
			try {
				getParentPanel().getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Set<String> currentLots = getProdLotNumbersByDept(selectedInProcessDpt);
				parentPanel.getLogger().info(currentLots.size() + " lots found in department " + selectedInProcessDpt);
				getProdLotComboBox().setModel(new ComboBoxModel<String>(new Vector<String>(currentLots)));
				getProdLotComboBox().getComponent().requestFocus();
				getProdLotComboBox().getComponent().showPopup();
			} catch (Exception e) {
				String errorMsg = "Production Lot data could not be loaded for department " + selectedInProcessDpt;
				parentPanel.getMainWindow().setErrorMessage(errorMsg);
				parentPanel.getLogger().error(e, errorMsg + ".\n");
			} finally {
				getParentPanel().getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				getProdLotComboBox().getComponent().showPopup();
			}
		}
		getParentPanel().getProductPanel().removeData();
		parentPanel.getLogger().info("Product panel reset");
	}
	
	protected void setProdLotElementVisibility() {
		Boolean dptSelected = selectedInProcessDpt != null && !selectedInProcessDpt.equals(DEFAULT_IN_PROC_DPT_VALUE);
		getProdLotComboBox().setVisible(dptSelected);
		getProdLotComboBox().setEnabled(dptSelected);
		getProdLotSearchComboBox().setVisible(!dptSelected);
		getProdLotSearchComboBox().setEnabled(!dptSelected);
	}
	
	@SuppressWarnings("unchecked")
	private Set<String> getProdLotNumbersByDept(String divisionId){
		TreeSet<String> prodLotNumbers = new TreeSet<String>();
		if (divisionId != null) {
			List<String> trackingStatuses = getTrackingStatusByDptMap().get(divisionId);
			List<Product> productsInDpt = getProductDao().findByTrackingStatus(trackingStatuses);
			prodLotNumbers = new TreeSet<String>(productsInDpt.stream().map(Product::getProductionLot).collect(Collectors.toSet()));
		}
		return prodLotNumbers.descendingSet();
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,List<String>> getTrackingStatusByDptMap(){
		if (trackingStatusByDptMap == null) {
			trackingStatusByDptMap = new HashMap<String,List<String>>();
			List<String> trackingStatuses = getProductDao().findAllTrackingStatus();
			for (String trackingStatus : trackingStatuses) {
				try{
					Line trackedLine = getLineDao().findByKey(trackingStatus);
					String divisionId = trackedLine.getDivisionId();
					List<String> trackingStatusList = trackingStatusByDptMap.containsKey(divisionId.trim()) 
							? trackingStatusByDptMap.get(divisionId.trim()) : new ArrayList<String>();
					trackingStatusList.add(trackedLine.getLineId());
					trackingStatusByDptMap.put(divisionId.trim(), trackingStatusList);
				} catch (Exception e) {
					parentPanel.getLogger().info(e, "Failed to retrieve line data for tracking status " + trackingStatus);
				}
			}
		}
		return trackingStatusByDptMap;
	}
	
	@SuppressWarnings("rawtypes")
	private ProductDao getProductDao() {
		if (productDao == null) productDao = ProductTypeUtil.getProductDao(selectedProductType);
		return productDao;
	}
	
	private LineDao getLineDao() {
		if (lineDao == null) lineDao = ServiceFactory.getDao(LineDao.class);
		return lineDao;
	}
}