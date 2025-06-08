package com.honda.galc.client.teamleader.tracking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.let.util.TrackingMaintenanceUtil;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ReuseProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ReuseProductResult;
import com.honda.galc.entity.product.ReuseProductResultId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TrackingMaintenanceController</code> is ... .
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
 * @created Aug 2, 2013
 */
public class TrackingMaintenanceController extends BaseListener<TrackingMaintenancePanel> implements ActionListener, TreeSelectionListener {

	// === model === //
	private BaseProduct product;
	private InProcessProduct inProcessProduct;
	private boolean trackingStatusValid;

	private Map<String, ProcessPoint> processPointCache;
	private Map<String, Line> lineCache;
	private Map<String, Division> divisionCache;
	private Map<ProcessPoint, ProductType> processPointProductTypeMapping;
	private Map<ProductType, List<Division>> productTypeDivisionMapping;

	private List<Object> trackingPath;

	public TrackingMaintenanceController(TrackingMaintenancePanel view) {
		super(view);
		this.divisionCache = new HashMap<String, Division>();
		this.lineCache = new HashMap<String, Line>();
		this.processPointCache = new HashMap<String, ProcessPoint>();
		this.productTypeDivisionMapping = new LinkedHashMap<ProductType, List<Division>>();
		this.processPointProductTypeMapping = new HashMap<ProcessPoint, ProductType>();
		this.trackingPath = new ArrayList<Object>();
		loadData();
	}

	// === action hanling === //
	@Override
	protected void executeActionPerformed(ActionEvent ae) {

		if (ae.getSource().equals(getView().getProductTypeComboBox())) {
			productTypeSelected();
			return;
		}

		if (ae.getSource().equals(getView().getInputNumberTextField())) {
			findProduct();
			return;
		}

		if (ae.getSource().equals(getView().getSubmitButton())) {
			moveProduct();
			return;
		}

		if (ae.getSource().equals(getView().getDoneButton())) {
			toIdle();
			return;
		}
	}

	// === tree selection handling === //
	public void valueChanged(TreeSelectionEvent te) {
		if (te.getSource().equals(getView().getPlantStrucureTree())) {
			getView().getSubmitButton().setEnabled(false);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) getView().getPlantStrucureTree().getLastSelectedPathComponent();
			if (getProduct() == null) {
				return;
			}
			if (node == null) {
				return;
			}

			getView().getMainWindow().clearMessage();

			if (TextFieldState.ERROR.isInState(getView().getMoveReasonTextField())) {
				TextFieldState.EDIT.setState(getView().getMoveReasonTextField());
			}

			if (node.getUserObject() instanceof ProcessPoint) {
				ProcessPoint pp = (ProcessPoint) node.getUserObject();
				if (!pp.isTrackingPoint()) {
					return;
				}
				if (isTrackingStatusValid() && pp.getProcessPointId().equals(getInProcessProduct().getLastPassingProcessPointId())) {
					return;
				} else {
					getView().getSubmitButton().setEnabled(true);
					return;
				}
			}
		}

	}

	// === events implementation === //
	protected void productTypeSelected() {
		getView().getInputNumberTextField().setText("");
		ProductType productType = (ProductType) getView().getProductTypeComboBox().getSelectedItem();
		TreeModel model = createTreeModel(productType);
		getView().getPlantStrucureTree().setModel(model);
		getView().getInputNumberTextField().requestFocus();
	}

	@SuppressWarnings("unchecked")
	protected void findProduct() {
		ProductType productType = (ProductType) getView().getProductTypeComboBox().getSelectedItem();
		if (productType == null) {
			return;
		}

		String inputNumber = getView().getInputNumberTextField().getText();
		inputNumber = StringUtils.trim(inputNumber);
		getView().getInputNumberTextField().setText(inputNumber);

		BaseProduct product = null;
		ProductDao<?> productDao = ProductTypeUtil.getProductDao(productType);
		if (productDao instanceof DiecastDao) {
			DiecastDao<?> diecastDao = (DiecastDao<?>) productDao;
			product = diecastDao.findByMCDCNumber(inputNumber);
		} else {
			product = productDao.findByKey(inputNumber);
		}

		if (product == null) {
			String msg = String.format("%s does not exist for input number %s.", productType.getProductName(), inputNumber);
			getView().getInputNumberTextField().selectAll();
			TextFieldState.ERROR.setState(getView().getInputNumberTextField());
			getView().getMainWindow().setErrorMessage(msg);
			return;
		}

		List<ProductHistory> history = (List<ProductHistory>) ProductTypeUtil.getProductHistoryDao(productType).findAllByProductId(product.getProductId());
		List<Map<String, Object>> data = assembleHistoryData(history);
		getView().getProductHistoryPane().reloadData(data);

		String productLineId = product.getTrackingStatus();
		String productProcessPointId = product.getLastPassingProcessPointId();

		Line productLine = lookupLine(productLineId);
		ProcessPoint productProcessPoint = lookupProcessPoint(productProcessPointId);

		getView().getProductLineTextField().setText(format(productLine, productLineId));
		getView().getProductProcessPointTextField().setText(format(productProcessPoint, productProcessPointId));

		InProcessProduct inProcessProduct = ServiceFactory.getDao(InProcessProductDao.class).findByKey(product.getProductId());

		setProduct(product);
		setInProcessProduct(inProcessProduct);
		toProcessing();

		validateTrackingStatus(product, inProcessProduct);
	}

	protected void moveProduct() {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getView().getPlantStrucureTree().getLastSelectedPathComponent();
		ProcessPoint processPoint = (ProcessPoint) node.getUserObject();

		if (getInProcessProduct() != null && getInProcessProduct().getLineId() != null && getInProcessProduct().getLineId().equals(processPoint.getLineId())) {
			String msg = String.format("Product is already on selected line (%s).", processPoint.getLineName());
			JOptionPane.showMessageDialog(getView(), msg);
			return;
		}

		String reason = StringUtils.trim(getView().getMoveReasonTextField().getText());
		getView().getMoveReasonTextField().setText(reason);
		if (StringUtils.isBlank(reason)) {
			getView().getMainWindow().setErrorMessage("Move Reason is required !");
			TextFieldState.ERROR.setState(getView().getMoveReasonTextField());
			getView().getMoveReasonTextField().requestFocus();
			return;
		}
		String msg = "";
		String message1 = PropertyService.getProperty(ApplicationContext.getInstance().getApplicationId(), "MOVE_PRODUCT_MSG{"+processPoint.getProcessPointId()+"}");
		
		if(null != message1 && message1.length() > 1) {
			msg = message1+"\n";
		}

		
		msg = msg + "Are you sure You want to update product location ?";
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Move Product", JOptionPane.YES_NO_OPTION);
		if (retCode == JOptionPane.NO_OPTION) {
			return;
		}

		ServiceFactory.getService(TrackingService.class).track(getProduct(), processPoint.getProcessPointId());
		Logger.getLogger().info(String.format("Product %s has been moved to location %s.", getProduct(), processPoint));

		Timestamp now = new Timestamp(System.currentTimeMillis());
		ReuseProductResultId id = new ReuseProductResultId();
		id.setProductId(getProduct().getProductId());
		id.setActualTimestamp(now);
		ReuseProductResult result = new ReuseProductResult();
		result.setId(id);
		result.setAssociateNo(getView().getMainWindow().getUserId());
		result.setProductionDate(getProductionDate(processPoint.getDivisionId(), now));
		result.setReuseVinReason(reason);
		result.setCreateTimestamp(now);
		ServiceFactory.getDao(ReuseProductResultDao.class).save(result);
		logUserAction(SAVED, result);
		getView().getMoveReasonTextField().setText("");

		getView().getPlantStrucureTree().setSelectionPath(null);
		resetModel();
		findProduct();
		
		// After updating all tracking tables, update SmartEye table (i.e. GAL228TBX)
		String serviceName = PropertyService.getProperty(ApplicationContext.getInstance().getApplicationId(), "SERVICE_NAMES{"+processPoint.getProcessPointId()+"}");
		if(null != serviceName && serviceName.length() > 0) {
			DataContainer dc = new DefaultDataContainer();
			dc.put("PRODUCT", product);
			TrackingMaintenanceUtil trackingMaintenanceUtil = new TrackingMaintenanceUtil();
				try {
					dc =  (DataContainer) trackingMaintenanceUtil.getClass().getMethod(serviceName,  DataContainer.class).invoke(trackingMaintenanceUtil, dc);
				} catch (Exception e) {
					Logger.getLogger().error(e, e.getMessage());
				} 
			String returnMessage = (String)dc.get("MESSAGE");
			if (returnMessage != null && returnMessage.length() > 0) {
				getView().getMainWindow().setWarningMessage(returnMessage);
				TextFieldState.ERROR.setState(getView().getMoveReasonTextField());
				getView().getMoveReasonTextField().requestFocus();
			}
		}
		getView().getPlantStrucureTree().repaint();
	}

	// === state api === //
	protected void toProcessing() {
		TextFieldState.READ_ONLY.setState(getView().getInputNumberTextField());
		TextFieldState.READ_ONLY.setState(getView().getProductLineTextField());
		TextFieldState.READ_ONLY.setState(getView().getProductProcessPointTextField());

		TextFieldState.EDIT.setState(getView().getMoveReasonTextField());
		TextFieldState.READ_ONLY.setState(getView().getAssociateIdTextField());
		getView().getAssociateIdTextField().setText(getView().getMainWindow().getUserId());

		getView().getProductTypeComboBox().setEnabled(false);

		getView().getDoneButton().setEnabled(true);
		getView().getDoneButton().requestFocus();

	}

	protected void toIdle() {
		resetModel();
		resetUi();
		productTypeSelected();
		getView().getInputNumberTextField().requestFocus();
	}

	protected void resetModel() {
		setInProcessProduct(null);
		setProduct(null);
		setTrackingStatusValid(false);
		getTrackingPath().clear();
	}

	protected void resetUi() {
		getView().getInputNumberTextField().setText("");
		getView().getProductLineTextField().setText("");
		getView().getProductProcessPointTextField().setText("");
		getView().getMoveReasonTextField().setText("");
		getView().getAssociateIdTextField().setText("");
		getView().getProductHistoryPane().removeData();

		TextFieldState.EDIT.setState(getView().getInputNumberTextField());
		TextFieldState.DISABLED.setState(getView().getProductLineTextField());
		TextFieldState.DISABLED.setState(getView().getProductProcessPointTextField());
		TextFieldState.DISABLED.setState(getView().getMoveReasonTextField());
		TextFieldState.DISABLED.setState(getView().getAssociateIdTextField());

		getView().getProductTypeComboBox().setEnabled(true);
		getView().getDoneButton().setEnabled(false);
	}

	// === validation === //
	protected void validateTrackingStatus(BaseProduct product, InProcessProduct inProcessProduct) {

		setTrackingStatusValid(false);
		StringBuilder sb = new StringBuilder();

		ProcessPoint productProcessPoint = lookupProcessPoint(product.getLastPassingProcessPointId());
		Line productLine = lookupLine(product.getTrackingStatus());

		ProcessPoint inProcessProductProcessPoint = null;
		Line inProcessProductLine = null;
		Division inProcessProductDivision = null;

		if (inProcessProduct != null) {
			inProcessProductProcessPoint = lookupProcessPoint(inProcessProduct.getLastPassingProcessPointId());
			inProcessProductLine = lookupLine(inProcessProduct.getLineId());
		}
		inProcessProductDivision = lookupDivision(inProcessProductLine, inProcessProductProcessPoint);

		if (productProcessPoint != null && productLine != null && inProcessProductProcessPoint != null && inProcessProductLine != null) {
			if (productLine.getId().equals(productProcessPoint.getLineId()) && inProcessProductLine.getId().equals(inProcessProductProcessPoint.getLineId())) {
				if (productProcessPoint.getProcessPointId().equals(inProcessProductProcessPoint.getProcessPointId())) {
					setTrackingStatusValid(true);
				}
			}
		}

		if (isTrackingStatusValid()) {
			getTrackingPath().add(inProcessProductProcessPoint);
			getTrackingPath().add(inProcessProductLine);
			getTrackingPath().add(inProcessProductDivision);
			expandTree(inProcessProductDivision);
			expandTree(inProcessProductLine);
			expandTree(inProcessProductProcessPoint);
			return;
		}

		sb.append("Inconsistent Tracking Status.");
		TextFieldState.ERROR_READ_ONLY.setState(getView().getProductLineTextField());
		TextFieldState.ERROR_READ_ONLY.setState(getView().getProductProcessPointTextField());

		if (inProcessProduct != null) {
			getTrackingPath().add(inProcessProductProcessPoint);
			getTrackingPath().add(inProcessProductLine);
			getTrackingPath().add(inProcessProductDivision);
			expandTree(inProcessProductDivision);
			expandTree(inProcessProductLine);
			if (inProcessProductProcessPoint != null) {
				expandTree(inProcessProductProcessPoint);
			} else {
				expandTree(productProcessPoint);
			}
		} else if (productProcessPoint != null) {
			expandTree(productProcessPoint);
		}

		if (inProcessProduct == null) {
			appendSeparator(sb, " ");
			sb.append("Missing InProcessProduct.");
		}

		if (sb.length() > 0) {
			getView().getMainWindow().setErrorMessage(sb.toString());
		}
	}

	// === supporting api === //
	protected TreeModel createTreeModel(ProductType productType) {
		TreeModel model = null;
		if (productType == null) {
			return new DefaultTreeModel(new DefaultMutableTreeNode());
		}
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(productType);
		model = new DefaultTreeModel(root);
		List<Division> divisions = getProductTypeDivisionMapping().get(productType);
		if (divisions == null || divisions.isEmpty()) {
			return model;
		}

		for (Division division : divisions) {
			if (division == null || division.getId() == null) {
				continue;
			}
			DefaultMutableTreeNode divisionNode = new DefaultMutableTreeNode(division);
			root.add(divisionNode);
			if (division.getLines() == null || division.getLines().isEmpty()) {
				continue;
			}

			for (Line line : division.getLines()) {
				if (line == null || line.getId() == null) {
					continue;
				}

				if (!isOfProductType(line, productType)) {
					continue;
				}

				DefaultMutableTreeNode lineNode = new DefaultMutableTreeNode(line);
				divisionNode.add(lineNode);

				if (line.getProcessPoints() == null || line.getProcessPoints().isEmpty()) {
					continue;
				}
				for (ProcessPoint pp : line.getProcessPoints()) {
					if (pp == null || pp.getProcessPointId() == null) {
						continue;
					}

					if (isOfProductType(pp, productType)) {
						DefaultMutableTreeNode ppNode = new DefaultMutableTreeNode(pp);
						lineNode.add(ppNode);
					}
				}
			}
		}
		return model;
	}

	protected void expandTree(Object userObject) {
		if (userObject == null) {
			return;
		}
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getView().getPlantStrucureTree().getModel().getRoot();
		DefaultMutableTreeNode node = search(root, userObject);
		if (node == null) {
			return;
		}
		TreePath path = new TreePath(node.getPath());
		getView().getPlantStrucureTree().setSelectionPath(path);
		getView().getPlantStrucureTree().scrollPathToVisible(path);
	}

	protected DefaultMutableTreeNode search(DefaultMutableTreeNode root, Object userObject) {
		if (root == null || userObject == null) {
			return null;
		}
		Enumeration<?> enumeration = root.breadthFirstEnumeration();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
			if (node != null && node.getUserObject() != null && node.getUserObject().equals(userObject)) {
				return node;
			}
		}
		return null;
	}

	protected StringBuilder appendSeparator(StringBuilder sb, String separator) {
		if (sb.length() > 0) {
			sb.append(separator);
		}
		return sb;
	}

	protected String format(Line entity, String id) {
		String patern = "%s : %s";
		String str = "";
		if (entity != null) {
			str = String.format(patern, entity == null ? "" : entity.getLineName(), entity.getId());
		} else if (id != null) {
			str = id;
		}
		return str;
	}

	protected String format(ProcessPoint entity, String id) {
		String patern = "%s : %s";
		String str = "";
		if (entity != null) {
			str = String.format(patern, entity == null ? "" : entity.getProcessPointName(), entity.getId());
		} else if (id != null) {
			str = id;
		}
		return str;
	}

	// === data api === //
	protected void loadData() {

		List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAll();
		if (processPoints == null) {
			return;
		}

		PropertyComparator<ProcessPoint> comparator = new PropertyComparator<ProcessPoint>(ProcessPoint.class, "sequenceNumber");
		Collections.sort(processPoints, comparator);

		ProductType defaultProductType = getView().getMainWindow().getProductType();

		for (ProcessPoint pp : processPoints) {
			if (pp == null) {
				continue;
			}
			ProductType productType = null;
			String productTypeName = PropertyService.getProperty(pp.getProcessPointId(), "PRODUCT_TYPE");
			if (StringUtils.isNotBlank(productTypeName)) {
				productType = ProductType.getType(productTypeName);
			}
			if (productType == null) {
				productType = defaultProductType;
			}

			getProcessPointProductTypeMapping().put(pp, productType);

			Line line = lookupLine(pp.getLineId());
			if (line == null) {
				continue;
			}
			if (!line.getProcessPoints().contains(pp)) {
				line.getProcessPoints().add(pp);
			}

			Division division = lookupDivision(line.getDivisionId());
			if (division == null) {
				continue;
			}
			if (!division.getLines().contains(line)) {
				division.getLines().add(line);
			}

			List<Division> divisions = getProductTypeDivisionMapping().get(productType);
			if (divisions == null) {
				divisions = new ArrayList<Division>();
				getProductTypeDivisionMapping().put(productType, divisions);
			}
			if (!divisions.contains(division)) {
				divisions.add(division);
			}
		}
	}

	protected List<Map<String, Object>> assembleHistoryData(List<ProductHistory> history) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (history == null || history.isEmpty()) {
			return data;
		}
		for (ProductHistory ph : history) {
			if (ph == null) {
				continue;
			}
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("history", ph);
			ProcessPoint pp = lookupProcessPoint(ph.getProcessPointId());
			item.put("processPoint", pp);
			if (pp != null) {
				item.put("line", lookupLine(pp.getLineId()));
			}
			data.add(item);
		}
		return data;
	}

	protected ProcessPoint lookupProcessPoint(String id) {
		if (id == null) {
			return null;
		}
		ProcessPoint pp = getProcessPointCache().get(id);
		if (pp == null) {
			pp = ServiceFactory.getDao(ProcessPointDao.class).findByKey(id);
			getProcessPointCache().put(id, pp);
		}
		return pp;
	}

	protected Line lookupLine(String id) {
		if (id == null) {
			return null;
		}
		Line line = getLineCache().get(id);
		if (line == null) {
			line = ServiceFactory.getDao(LineDao.class).findByKey(id);
			if (line != null) {
				getLineCache().put(id, line);
				if (line.getProcessPoints() == null) {
					line.setProcessPoints(new ArrayList<ProcessPoint>());
				}
			}
		}
		return line;
	}

	protected Division lookupDivision(String id) {
		Division division = getDivisionCache().get(id);
		if (division == null) {
			division = ServiceFactory.getDao(DivisionDao.class).findByKey(id);
			if (division != null) {
				getDivisionCache().put(id, division);
			}
		}
		return division;
	}

	protected Division lookupDivision(Line line, ProcessPoint processPoint) {
		Division division = null;
		if (line != null) {
			division = lookupDivision(line.getDivisionId());
		}

		if (division == null && processPoint != null) {
			division = lookupDivision(processPoint.getDivisionId());
		}

		return division;
	}

	protected void sortLines() {
		PropertyComparator<Line> comparator = new PropertyComparator<Line>(Line.class, "lineSequenceNumber");
		for (ProductType pt : getProductTypeDivisionMapping().keySet()) {
			List<Division> divisions = getProductTypeDivisionMapping().get(pt);
			if (divisions == null || divisions.isEmpty()) {
				continue;
			}
			for (Division div : divisions) {
				if (div.getLines() == null || div.getLines().isEmpty()) {
					continue;
				}
				Collections.sort(div.getLines(), comparator);
			}
		}
	}

	protected boolean isOfProductType(Line line, ProductType productType) {
		if (line == null || productType == null || line.getProcessPoints() == null) {
			return false;
		}
		for (ProcessPoint pp : line.getProcessPoints()) {
			if (isOfProductType(pp, productType)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isOfProductType(ProcessPoint processPoint, ProductType productType) {
		if (processPoint == null || productType == null) {
			return false;
		}
		return productType.equals(getProcessPointProductTypeMapping().get(processPoint));
	}

	protected Date getProductionDate(String divisionId, Timestamp timestamp) {
		DailyDepartmentSchedule sched = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).find(divisionId, timestamp);
		if (sched != null && sched.getId() != null && sched.getId().getProductionDate() != null) {
			return sched.getId().getProductionDate();
		}
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date productionDate = new Date(cal.getTime().getTime());
		return productionDate;
	}

	// === get/set === //
	protected Map<String, ProcessPoint> getProcessPointCache() {
		return processPointCache;
	}

	protected Map<String, Line> getLineCache() {
		return lineCache;
	}

	protected BaseProduct getProduct() {
		return product;
	}

	protected void setProduct(BaseProduct product) {
		this.product = product;
	}

	protected Map<ProductType, List<Division>> getProductTypeDivisionMapping() {
		return productTypeDivisionMapping;
	}

	protected Map<String, Division> getDivisionCache() {
		return divisionCache;
	}

	protected Map<ProcessPoint, ProductType> getProcessPointProductTypeMapping() {
		return processPointProductTypeMapping;
	}

	protected InProcessProduct getInProcessProduct() {
		return inProcessProduct;
	}

	protected void setInProcessProduct(InProcessProduct inProcessProduct) {
		this.inProcessProduct = inProcessProduct;
	}

	protected boolean isTrackingStatusValid() {
		return trackingStatusValid;
	}

	protected void setTrackingStatusValid(boolean trackingStatusValid) {
		this.trackingStatusValid = trackingStatusValid;
	}

	protected List<Object> getTrackingPath() {
		return trackingPath;
	}
}
