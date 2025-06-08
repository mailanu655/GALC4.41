package com.honda.galc.client.teamleader.history;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.property.ProductionInfoPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductHistoryMaintenanceController</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jul 16, 2013
 */
public class ProductHistoryMaintenanceController extends BaseListener<ProductHistoryMaintenancePanel> implements ActionListener, ListSelectionListener {

	private BaseProduct product;
	private Map<String, ProcessPoint> processPoints;
	private final String ACTUAL_TIMESTAMP_INVALID_DATE_ERROR_MSG = "Invalid date for input Actual Timestamp, please correct";

	public ProductHistoryMaintenanceController(ProductHistoryMaintenancePanel view) {
		super(view);
		this.processPoints = new HashMap<String, ProcessPoint>();
		initProcessPoints();
	}

	// === action event dispatching === //
	@Override
	protected void executeActionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(getView().getProductIdTextField())) {
			findProductData();
			return;
		}
		if (ae.getSource().equals(getView().getResetButton())) {
			toIdle();
			return;
		}
		if (ae.getSource().equals(getView().getDeleteButton())) {
			deleteHistory();
			return;
		}
		if (ae.getSource().equals(getView().getUpdateButton())) {
			updateHistory();
			return;
		}
		if (ae.getSource().equals(getView().getNewEntryButton())) {
			saveNewEntry();
			return;
		}
	}

	// === list selection event === //
	@Override
	protected void executeValueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting()) {
			return;
		}
		ListSelectionModel model = (ListSelectionModel) lse.getSource();
		if (model.isSelectionEmpty()) {
			historyDeseleced();
		} else {
			historySelected();
		}
	}

	// === controlling api === //
	public void toIdle() {
		setProduct(null);
		resetForm();
		getView().getProductHistoryPane().removeData();
		getView().getProductIdTextField().setText("");
		TextFieldState.EDIT.setState(getView().getProductIdTextField());
		getView().getResetButton().setEnabled(false);
		getView().getProductIdTextField().requestFocus();
	}

	public void toProcessing() {
		TextFieldState.READ_ONLY.setState(getView().getProductIdTextField());
		getView().getDeleteButton().setEnabled(false);
		getView().getUpdateButton().setEnabled(false);
		getView().getResetButton().setEnabled(true);
		getView().getResetButton().requestFocus();
	}

	protected void resetForm() {
		getView().getProductionDateTextField().setText("");
		getView().getActualTimestampTextField().setText("");
		getView().getProcessPointTextField().setText("");
		TextFieldState.DISABLED.setState(getView().getProcessPointTextField());
		TextFieldState.DISABLED.setState(getView().getProductionDateTextField());
		TextFieldState.DISABLED.setState(getView().getActualTimestampTextField());
	}

	// === implementation === //
	protected void findProductData() {

		if (getProcessPoints().isEmpty()) {
			getView().getMainWindow().setErrorMessage("No Process Points defined (property : PROCESS_POINT_IDS).");
			return;
		}

		ProductType productType = getView().getMainWindow().getProductType();
		String productId = getView().getProductIdTextField().getText();
		productId = StringUtils.trim(productId);
		getView().getProductIdTextField().setText(productId);

		if (!UiUtils.isValid(getView().getProductIdTextField(), getView().getMainWindow())) {
			getView().getProductIdTextField().requestFocus();
			return;
		}

		BaseProduct product = ProductTypeUtil.getProductDao(productType).findBySn(productId);
		if (product == null) {
			getView().getProductIdTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getProductIdTextField());
			getView().getMainWindow().setErrorMessage(String.format("Product does not exist for %s.", productId));
			return;
		}
		
		List<ExceptionalOut> exceptionalList = getDao(ExceptionalOutDao.class).findAllByProductId(productId);
		
		if (!(exceptionalList.isEmpty())) {
			getView().getMainWindow().setWarningMessage("This Product is either Scrapped or Exceptional out");
			return;
		}
		
		if(isProcessPointShipping(product.getLastPassingProcessPointId(), productType)){
			getView().getMainWindow().setWarningMessage("This Product is Shipped");
			return;
		}
		
		
		setProduct(product);
		reloadProductHistory();
		toProcessing();
	}

	protected PreProductionLot findProductionLot(String productionLotNumber) {
		PreProductionLot productionLot = null;
		if (StringUtils.isBlank(productionLotNumber)) {
			return productionLot;
		}
		productionLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(productionLotNumber);
		return productionLot;
	}
	
	protected boolean isProcessPointShipping(String lastPassingProcessPointId, ProductType productType) {
		if(lastPassingProcessPointId == null) return false;
		
		List<String> shippingProcessPoints = getShippingProcessPoints(productType);
		return shippingProcessPoints != null && !shippingProcessPoints.isEmpty() &&
				shippingProcessPoints.contains(lastPassingProcessPointId);
	}

	private List<String> getShippingProcessPoints(ProductType productType) {
		ProductionInfoPropertyBean property = PropertyService.getPropertyBean(ProductionInfoPropertyBean.class);
		String productTypeName = productType.name();
		try {
			String[] shippingProcessPointIds = property.getShippedPpIds(String[].class).get(productTypeName);
			if(shippingProcessPointIds != null) {
				return shippingProcessPointIds.length == 0 ? null : Arrays.asList(shippingProcessPointIds);
			} else {
				return null;
			}
		}catch (NullPointerException e) {
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	protected void deleteHistory() {
		int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Delete History", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		ProductHistory productHistory = getView().getProductHistoryPane().getSelectedItem();
		ProductType productType = getView().getMainWindow().getProductType();
		((ProductHistoryDao) ProductTypeUtil.getProductHistoryDao(productType)).remove(productHistory);
		logUserAction(REMOVED, productHistory);
		reloadProductHistory();
	}
	
	protected Timestamp getActualTimestampValue(){
		return getView().getActualTimestampValue();
	}
	
	protected void showInvalidActualTimestampError(){
		getView().getMainWindow().setErrorMessage(ACTUAL_TIMESTAMP_INVALID_DATE_ERROR_MSG);
	}

	@SuppressWarnings("unchecked")
	protected void updateHistory() {
		Timestamp actualTimestamp = getActualTimestampValue();
		if(actualTimestamp == null){
			showInvalidActualTimestampError();
			return;
		}
		
		int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Update History", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		
		ProductHistory productHistory = getView().getProductHistoryPane().getSelectedItem();
		
		if(actualTimestamp.equals(productHistory.getActualTimestamp())){
			getView().getMainWindow().setWarningMessage("No Update made in existing entry");
			return;
		}
		
		
		if (productHistory.getActualTimestamp() == null) {
			productHistory.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
			productHistory.setProcessCount(1);
			if (productHistory instanceof ProductResult && getProduct() instanceof Product) {
				ProductResult productResult = (ProductResult) productHistory;
				Product product = (Product) getProduct();
				productResult.setProductionLot(product.getProductionLot());
				productResult.setProductSpecCode(product.getProductSpecCode());
			}
		} else {
			productHistory.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		}

		BaseProduct product = (BaseProduct) getProduct();
		PreProductionLot productionLotData = findProductionLot(product.getProductionLot());

		DailyDepartmentSchedule dailyDepartmentSchedule = null;
		if(null != productionLotData) {
			String processLocation = productionLotData.getProcessLocation();
			String lineNo = productionLotData.getLineNo();
			String plantCode = productionLotData.getPlantCode();
			dailyDepartmentSchedule = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).findByActualTime(lineNo, processLocation, plantCode, actualTimestamp);
		}
		
		if (productHistory instanceof ProductResult) {
			ProductResult productResult = (ProductResult) productHistory;
			productResult.setProductionDate(getView().getProductionDateValue());
			if(dailyDepartmentSchedule != null)
				productResult.setProductionDate(dailyDepartmentSchedule.getId().getProductionDate());
		}

		ProductType productType = getView().getMainWindow().getProductType();
		try {
			if (productHistory.getActualTimestamp() != null && !actualTimestamp.equals(productHistory.getActualTimestamp())) {
				((ProductHistoryDao) ProductTypeUtil.getProductHistoryDao(productType))
					.updateActualTimestamp(actualTimestamp, productHistory.getProductId(), productHistory.getProcessPointId(), productHistory.getActualTimestamp());
				logUserAction(UPDATED, productHistory);
			}
			productHistory.setActualTimestamp(actualTimestamp);
			((ProductHistoryDao) ProductTypeUtil.getProductHistoryDao(productType)).save(productHistory);
			logUserAction(SAVED, productHistory);
			getView().getProductHistoryPane().getTable().getSelectionModel().clearSelection();
		} finally {
			List<ProductHistory> history = selectProductHistory();
			getView().getProductHistoryPane().reloadData(history);
		}
	}
	
	public void saveNewEntry() {
		Timestamp actualTimestamp = getActualTimestampValue();
		if(actualTimestamp == null){
			showInvalidActualTimestampError();
			return;
		}
		
		ProductHistory productHistory = getView().getProductHistoryPane().getSelectedItem();
		int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Create History", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}	
		
		if (productHistory.getActualTimestamp() == null) {
			productHistory.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
			productHistory.setProcessCount(1);
			if (productHistory instanceof ProductResult && getProduct() instanceof Product) {
				ProductResult productResult = (ProductResult) productHistory;
				Product product = (Product) getProduct();
				productResult.setProductionLot(product.getProductionLot());
				productResult.setProductSpecCode(product.getProductSpecCode());
			}
		} else {
			productHistory.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		}
		ProductType productType = getView().getMainWindow().getProductType();
		
		BaseProduct product = (BaseProduct) getProduct();
		PreProductionLot productionLotData = findProductionLot(product.getProductionLot());
		
		DailyDepartmentSchedule dailyDepartmentSchedule = null;
 		if(null != productionLotData) {
 			String processLocation = productionLotData.getProcessLocation();
 			String lineNo = productionLotData.getLineNo();
 			String plantCode = productionLotData.getPlantCode();
 	 		dailyDepartmentSchedule = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).findByActualTime(lineNo, processLocation, plantCode, actualTimestamp);
 		}
		
		if (productHistory instanceof ProductResult) {
			ProductResult productResult = (ProductResult) productHistory;
			productResult.setProductionDate(getView().getProductionDateValue());
			if(dailyDepartmentSchedule != null)
				productResult.setProductionDate(dailyDepartmentSchedule.getId().getProductionDate());
		}
		
		productHistory.setActualTimestamp(actualTimestamp);
		try{
			((ProductHistoryDao) ProductTypeUtil.getProductHistoryDao(productType)).save(productHistory);
			logUserAction(SAVED, productHistory);
			getView().getProductHistoryPane().getTable().getSelectionModel().clearSelection();
		}finally{
			List<ProductHistory> history = selectProductHistory();
			getView().getProductHistoryPane().reloadData(history);
		}
	
	}
	
	public void historySelected() {
		historyDeseleced();

		TextFieldState.READ_ONLY.setState(getView().getProcessPointTextField());
		TextFieldState.EDIT.setState(getView().getActualTimestampTextField());

		ProductHistory productHistory = getView().getProductHistoryPane().getSelectedItem();
		boolean productionDateEditable = productHistory instanceof ProductResult;
		TextFieldState.READ_ONLY.setState(getView().getProductionDateTextField());
		/*if (productionDateEditable) {
			TextFieldState.EDIT.setState(getView().getProductionDateTextField());
		}*/
		getView().getProcessPointTextField().setText(productHistory.getProcessPointName());

		if (productHistory.getActualTimestamp() == null) {
			getView().getUpdateButton().setEnabled(false);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			getView().setActualTimestampValue(now);
			if (productionDateEditable) {
				getView().setProductionDateValue(now);
			}
		} else {
			getView().getUpdateButton().setEnabled(true);
			getView().setActualTimestampValue(productHistory.getActualTimestamp());
			if (productionDateEditable) {
				ProductResult pr = (ProductResult) productHistory;
				getView().setProductionDateValue(pr.getProductionDate());
			}
			getView().getDeleteButton().setEnabled(true);
		}
		getView().getNewEntryButton().setEnabled(true);
	}

	public void historyDeseleced() {
		resetForm();
		getView().getDeleteButton().setEnabled(false);
		getView().getUpdateButton().setEnabled(false);
		getView().getNewEntryButton().setEnabled(false);
	}

	protected void reloadProductHistory() {
		List<ProductHistory> history = selectProductHistory();
		getView().getProductHistoryPane().getTable().getSelectionModel().clearSelection();
		getView().getProductHistoryPane().reloadData(history);
	}

	protected List<ProductHistory> selectProductHistory() {

		ProductType productType = getView().getMainWindow().getProductType();
		List<ProductHistory> history = new ArrayList<ProductHistory>();

		ProductHistoryDao<? extends ProductHistory, ?> historyDao = ProductTypeUtil.getProductHistoryDao(productType);
		String productId = getProduct().getProductId();

		for (String processPointId : getProcessPoints().keySet()) {

			List<? extends ProductHistory> list = historyDao.findAllByProductAndProcessPoint(productId, processPointId);
			if (list != null && !list.isEmpty()) {
				history.addAll(list);
			} else {
				ProductHistory ph = ProductTypeUtil.createProductHistory(productId, processPointId, productType.getProductName());
				ph.setActualTimestamp(null);
				history.add(ph);
			}
		}
		lookupProcessPointName(history);
		return history;
	}

	protected void lookupProcessPointName(List<? extends ProductHistory> list) {
		for (ProductHistory ph : list) {
			if (StringUtils.isBlank(ph.getProcessPointName())) {
				ProcessPoint processPoint = getProcessPoints().get(ph.getProcessPointId());
				if (processPoint == null) {
					continue;
				}
				ph.setProcessPointName(processPoint.getProcessPointName());
			}
		}
	}

	// === get/set === //
	protected BaseProduct getProduct() {
		return product;
	}

	protected void setProduct(BaseProduct product) {
		this.product = product;
	}

	protected Map<String, ProcessPoint> getProcessPoints() {
		return processPoints;
	}

	protected Collection<String> getProcessPointIds() {
		return getProcessPoints().keySet();
	}

	protected void initProcessPoints() {
		String propertyName = "PROCESS_POINT_IDS";
		String property = getView().getMainWindow().getApplicationProperty(propertyName);
		property = StringUtils.trim(property);
		if (StringUtils.isBlank(property)) {
			return;
		}
		String[] ar = property.split(Delimiter.COMMA);
		StringBuilder sb = new StringBuilder();
		for (String processPointId : ar) {
			if (StringUtils.isBlank(processPointId)) {
				continue;
			}
			processPointId = StringUtils.trim(processPointId);
			ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
			if (processPoint == null) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(processPointId);
			} else {
				getProcessPoints().put(processPointId, processPoint);
			}
		}
		if (sb.length() > 0) {
			getView().getMainWindow().setErrorMessage(String.format("Invalid process point ids are configured and will be ignored : %s. Please verify property %s.", sb, propertyName));
		}
	}
}
