package com.honda.galc.client.codebroadcast;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductSpecDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.ErrorData;
import com.honda.galc.device.dataformat.ErrorDataIdentifier;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.dataformat.ProductIdIdentifier;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.StragglerStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.StragglerService;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.check.PartResultData;

public class CodeBroadcastProductController extends CodeBroadcastController {

	private static final String PREVIOUS_PRODUCTION_LOT = "PREVIOUS_PRODUCTION_LOT";

	private final String[] leadingCharsToRemove;
	private final PrintAttributeFormat[] stationDisplayFormats;

	private List<CodeBroadcastCode> codes;
	private String productId;
	private String productSpec;
	private ProcessPointDao processPointDao;
	private ProductDao<?> productDao;
	@SuppressWarnings("rawtypes")
	private ProductSpecDao productSpecDao;
	private ComponentStatusDao componentStatusDao;
	private TrackingService trackingService;
	private String previousProductionLot;

	/*
	 * --------------------------------------------------
	 * Getters/Setters
	 * --------------------------------------------------
	 */
	private CodeBroadcastProductPanel getProductPanel() {
		return (CodeBroadcastProductPanel) getPanel();
	}
	public String getProductId() {
		return this.productId;
	}
	public String getProductSpec() {
		return this.productSpec;
	}
	public void setProductData(String productId, String productSpec) {
		this.productId = productId;
		this.productSpec = productSpec;
	}
	public String[] getStationJobCodes() {
		return getPropertyBean().getStationJobCodes();
	}
	public List<CodeBroadcastCode> getCodes() {
		return this.codes;
	}
	public void setCodes(List<CodeBroadcastCode> codes) {
		this.codes = codes;
	}
	protected String[] getLeadingCharsToRemove() {
		return this.leadingCharsToRemove;
	}
	@Override
	public PrintAttributeFormat[] getStationDisplayFormats() {
		return this.stationDisplayFormats;
	}

	/*
	 * --------------------------------------------------
	 * DAO getters
	 * --------------------------------------------------
	 */
	private ProcessPointDao getProcessPointDao() {
		if (this.processPointDao == null) {
			this.processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return this.processPointDao;
	}
	private ProductDao<?> getProductDao() {
		if (this.productDao == null) {
			this.productDao = ProductTypeUtil.getProductDao(getProductType());
		}
		return this.productDao;
	}
	@SuppressWarnings("rawtypes")
	private ProductSpecDao getProductSpecDao() {
		if (this.productSpecDao == null) {
			final ProductTypeUtil productTypeUtil = ProductTypeUtil.getTypeUtil(getProductType());
			final Class<? extends BaseProductSpecDao<? extends BaseProductSpec, ?>> productSpecDaoClass = productTypeUtil.getProductSpecDaoClass();
			if (ProductSpecDao.class.isAssignableFrom(productSpecDaoClass)) {
				this.productSpecDao = ((ProductSpecDao) productTypeUtil.getProductSpecDao());
			} else {
				throw new RuntimeException("Unable to get ProductSpecDao for product type " + getProductType());
			}
		}
		return this.productSpecDao;
	}
	private ComponentStatusDao getComponentStatusDao() {
		if (this.componentStatusDao == null) {
			this.componentStatusDao = ServiceFactory.getDao(ComponentStatusDao.class);
		}
		return this.componentStatusDao;
	}

	/*
	 * --------------------------------------------------
	 * Service getters
	 * --------------------------------------------------
	 */
	private TrackingService getTrackingService() {
		if (this.trackingService == null) {
			this.trackingService = ServiceFactory.getService(TrackingService.class);
		}
		return this.trackingService;
	}

	/*
	 * --------------------------------------------------
	 * Property getters
	 * --------------------------------------------------
	 */
	private SystemPropertyBean getSystemPropertyBean() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class, getApplicationId());
	}
	private ProductPropertyBean getProductPropertyBean() {
		return PropertyService.getPropertyBean(ProductPropertyBean.class, getApplicationId());
	}
	private ProductCheckPropertyBean getProductCheckPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getApplicationId());
	}



	/*
	 * --------------------------------------------------
	 * Constructors
	 * --------------------------------------------------
	 */

	public CodeBroadcastProductController(CodeBroadcastPanel panel) {
		super(panel);
		// initialize leadingCharsToRemove
		if (getProductPropertyBean().isRemoveIEnabled()) {
			final String leadCharsToRemove = getSystemPropertyBean().getLeadingVinCharsToRemove();
			if (!StringUtils.isBlank(leadCharsToRemove)) {
				this.leadingCharsToRemove = leadCharsToRemove.trim().split(",");
				Arrays.sort(this.leadingCharsToRemove, Collections.reverseOrder());
			} else {
				this.leadingCharsToRemove = null;
			}
		} else {
			this.leadingCharsToRemove = null;
		}
		// initialize displayAttributeFormats
		{
			List<PrintAttributeFormat> printAttributeFormats = ServiceFactory.getDao(PrintAttributeFormatDao.class).findAllByFormId(getPropertyBean().getStationDisplayFormId());
			if (printAttributeFormats != null && !printAttributeFormats.isEmpty()) {
				this.stationDisplayFormats = new PrintAttributeFormat[printAttributeFormats.size()];
				printAttributeFormats.toArray(this.stationDisplayFormats);
			} else {
				this.stationDisplayFormats = null;
			}
		}
	}



	/*
	 * --------------------------------------------------
	 * Initialization methods
	 * --------------------------------------------------
	 */

	@Override
	protected void initialize() {
		try {
			super.initialize();
			if (getPropertyBean().getColorNewLot() != null) {
				ComponentStatus componentStatus = getComponentStatusDao().findByKey(getApplicationId(), PREVIOUS_PRODUCTION_LOT);
				if (componentStatus == null || componentStatus.getStatusValue() == null) {
					this.previousProductionLot = "";
				} else {
					this.previousProductionLot = componentStatus.getStatusValue();
				}
			}
			if (getPropertyBean().isAutoConfirmDeviceData()) {
				setManualProductIdEntryEnabled(false);
				getPanel().getConfirmButton().setEnabled(false);
			} else {
				getProductPanel().requestFocusOnProductId();
			}
			CodeBroadcastDeviceListener.getInstance().registerController(this);
			if (!getPanel().isErrorMessage()) requestTrigger();
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.getMessage(), e);
		}
	}

	@SuppressWarnings("serial")
	@Override
	protected void initConnections() {
		try {
			super.initConnections();
			if (!getPropertyBean().isDisplayOnly()) {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException ie) {
					Logger.getLogger().error(ie, "initConnections() sleep cycle interrupted");
				}
				{
					final String functionKey = getPropertyBean().getFunctionKeyProductSelect();
					if (StringUtils.isNotBlank(functionKey)) {
						final String clickProductIdButton = "clickProductIdButton";
						getPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(functionKey.toUpperCase()), clickProductIdButton);
						getPanel().getActionMap().put(clickProductIdButton, new AbstractAction() {
							@Override
							public void actionPerformed(ActionEvent actionEvent) {
								getProductPanel().getProductIdButton().doClick(0);
							}
						});
					}
				}
				getProductPanel().getProductIdButton().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						doProductSelection();
					}
				});
				getProductPanel().getProductIdTextField().addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						try {
							checkMessage();
							String productId = getProductPanel().getProductIdTextField().getText();
							Logger.getLogger().info("User entered product id: " + productId);
							handleProductSelection(productId, false);
						} catch (Exception e) {
							getPanel().displayErrorMessage(e.getMessage(), e);
						}
					}
				});
			}
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.getMessage(), e);
		}
	}

	@Override
	protected void doConfirm() {
		doConfirm(false);
	}

	@SuppressWarnings("unchecked")
	protected void doConfirm(final boolean autoConfirm) {
		try {
			stopTimeoutTimer();
			checkMessage();
			Logger.getLogger().info("Confirming codes");
			if (!getPropertyBean().isIgnoreErrorStateCheck() && !isErrorState() && !autoConfirm) {
				getPanel().displayWarningMessage("Cannot confirm codes: not in error state", true);
				unconfirm();
				restartTimeoutTimer();
				return;
			}
			// confirm the color code if needed
			final String colorCode;
			if (getPropertyBean().isBroadcastColorCode()) {
				final ProductSpec productSpecData = (ProductSpec) getProductSpecDao().findByKey(getProductSpec());
				colorCode = productSpecData.getExtColorCode();
			} else {
				colorCode = null;
			}
			if (autoConfirm || !getPropertyBean().isConfirmConfirmation() || MessageDialog.confirm(getPanel().getMainWindow(), getConfirmationSummary(true, getCodes(), getProductId(), colorCode))) {
				getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				// identify if the product is a straggler
				if (StringUtils.isNotBlank(getPropertyBean().getStragglerPpDelayedAt())) {
					DataContainer stragglerResult = ServiceFactory.getService(StragglerService.class).identifyStragglers(getPropertyBean().getStragglerPpDelayedAt(), getProductId(), getProductType().name());
					StragglerStatus stragglerStatus = StragglerStatus.getType((Integer) (stragglerResult.get(TagNames.STRAGGLER_STATUS)));
					switch (stragglerStatus) {
					case NO_ACTION:
						break;
					case CREATED:
						Logger.getLogger().info("Straggler created for product id " + getProductId() + " at " + getApplicationId());
						break;
					case RELEASE:
						Logger.getLogger().info("Straggler released for product id " + getProductId() + " at " + getApplicationId());
						break;
					case CREATED_AND_RELEASE:
						Logger.getLogger().info("Straggler created and released for product id " + getProductId() + " at " + getApplicationId());
						break;
					default:
						break;
					}
					
				}
				// track the product if all applicable checks have passed
				if (isTrackingConfigured()) {
					if (getTrackingDeviceId() == null) {
						getTrackingService().track(getProductType(), getProductId(), getApplicationId());
					} else {
						getTrackingService().track(getProductType(), getProductId(), getApplicationId(), getTrackingDeviceId());
					}
				}
				// broadcast the codes
				if (broadcastCodes(getCodes(), getProductId(), colorCode, !getPropertyBean().isAutoConfirmDeviceData())) {
					Logger.getLogger().info("Broadcasted codes " + getCodesSummary(getCodes()) + (colorCode != null ? (", Color Code: " + colorCode) : "") + " for product id: " + getProductId());
					if (getPropertyBean().isAutoConfirmDeviceData()) {
						setManualProductIdEntryEnabled(false);
						getPanel().getConfirmButton().setEnabled(false);
						if (!autoConfirm) {
							for (Map.Entry<String, CodeBroadcastConfirmationField> entry : getPanel().getCodePanelFields().entrySet()) {
								entry.getValue().setConfirmationEnabled(false);
							}
						}
					}
				} else {
					Logger.getLogger().info("Failed to broadcast codes " + getCodesSummary(getCodes()) + (colorCode != null ? (", Color Code: " + colorCode) : "") + " for product id: " + getProductId());
					unconfirm();
					setManualProductIdEntryEnabled(true);
					for (Map.Entry<String, CodeBroadcastConfirmationField> entry : getPanel().getCodePanelFields().entrySet()) {
						entry.getValue().setConfirmationEnabled(true);
					}
				}
				getPanel().setCursor(Cursor.getDefaultCursor());
			} else {
				restartTimeoutTimer();
			}
		} catch (Exception e) {
			getPanel().setCursor(Cursor.getDefaultCursor());
			getPanel().displayErrorMessage(e.getMessage(), e);
		}
	}

	protected void doProductSelection() {
		try {
			checkMessage();
			ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(getPanel().getMainWindow(), getProductType().name(), "Manual Product Entry");
			manualProductEntry.setModal(true);
			manualProductEntry.setVisible(true);
			getProductPanel().getProductIdTextField().requestFocusInWindow();

			String productId = manualProductEntry.getResultProductId();
			Logger.getLogger().info("User selected product id: " + productId);
			handleProductSelection(productId, false);
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.getMessage(), e);
		}
	}

	@Override
	public Object received(IDeviceData deviceData) {
		if (deviceData instanceof ProductId) {
			if (StringUtils.isBlank(getProductPanel().getProductIdTextField().getText()) || getPropertyBean().isAllowProductReplacement()) {
				ProductId message = (ProductId) deviceData;
				if (message instanceof ProductIdIdentifier) {
					ProductIdIdentifier identifier = (ProductIdIdentifier) message;
					if (getTrackingDeviceId() != null && !getTrackingDeviceId().equals(identifier.getIdentifier())) {
						return deviceData;
					}
				}
				stopTimeoutTimer();
				Logger.getLogger().info("Received ProductId device data: " + message.getProductId());
				handleProductSelection(message.getProductId(), getPropertyBean().isAutoConfirmDeviceData());
				return deviceData;
			}
		}
		if (deviceData instanceof ErrorData) {
			ErrorData message = (ErrorData) deviceData;
			if (message instanceof ErrorDataIdentifier) {
				ErrorDataIdentifier identifier = (ErrorDataIdentifier) message;
				if (getTrackingDeviceId() != null && !getTrackingDeviceId().equals(identifier.getIdentifier())) {
					return deviceData;
				}
			}
			if (message.isErrorBit()) {
				getPanel().getConfirmButton().setEnabled(getCodes() != null && !getCodes().isEmpty() && isCodesConfirmed());
				setManualProductIdEntryEnabled(true);
			} else {
				getPanel().getConfirmButton().setEnabled(false);
				setManualProductIdEntryEnabled(false);
			}
		}
		return super.received(deviceData);
	}

	private void handleProductSelection(final String dirtyProductId, final boolean autoConfirm) {
		stopTimeoutTimer();
		final String productId = cleanProductId(dirtyProductId);
		if (StringUtils.isEmpty(productId) || productId.equals(getProductId())) {
			restartTimeoutTimer();
			return;
		}
		getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					if (getPropertyBean().isShowPreviousProduct()) {
						getProductPanel().getPreviousProductIdTextField().setText(getProductId());
						getProductPanel().getPreviousProductSpecTextField().setText(getProductSpec());
					}
					if (productId.equals(getPropertyBean().getDeviceDataErrorProductId())) {
						clearTabWithProductId(productId);
						if (autoConfirm) setErrorState(true);
						getPanel().displayErrorMessage(getPropertyBean().getDeviceDataErrorMessage());
						return;
					}
//					if (productId.length() != getProductType().getProductIdLength()) {
//						clearTabWithProductId(productId);
//						if (autoConfirm) setErrorState(true);
//						getPanel().displayErrorMessage(String.format("Product id %1$s has invalid length: expected %2$d but received %3$d characters", productId, getProductType().getProductIdLength(), productId.length()), true);
//						return;
//					}
					BaseProduct product = getProductDao().findBySn(productId);
					if (product == null) {
						clearTabWithProductId(productId);
						if (autoConfirm) setErrorState(true);
						getPanel().displayErrorMessage(String.format("Invalid product id: %1$s", productId), true);
						return;
					}
					if (!getProductDao().isProductTrackingStatusValidForProcessPoint(product.getProductId(), getApplicationId())) {
						clearTabWithProductId(productId);
						if (autoConfirm) setErrorState(true);
						final String message = String.format("The TRACKING_STATUS of %1$s is %2$s, which is not valid for %3$s. \nValid statuses are %4$s.", productId, getTrackingStatusSummary(product.getTrackingStatus()), getApplicationId(), getValidTrackingStatusesSummary());
						getPanel().getEmailHandler().sendEmail(message);
						getPanel().displayErrorMessage(message, true);
						return;
					}
					// perform product checks, if applicable
					if (ArrayUtils.isNotEmpty(getProductCheckPropertyBean().getProductCheckTypes())) {
						if (!executeCheck(product, getProcessPointDao().findById(getApplicationId()))) {
							clearTabWithProductId(productId);
							if (autoConfirm) setErrorState(true);
							return;
						}
					}

					setProductData(product.getProductId(), product.getProductSpecCode());
					getProductPanel().getProductIdTextField().setText(getProductId());
					getPanel().getProductSpecTextField().setText(getProductSpec());
					// check if the production lot has changed and change background if so
					if (previousProductionLot != null) {
						String productionLot = product.getProductionLot();
						if (!ObjectUtils.equals(productionLot, previousProductionLot)) {
							getPanel().setBackground(getPropertyBean().getColorNewLot());
							previousProductionLot = (productionLot == null ? "" : productionLot);
							ComponentStatus componentStatus = new ComponentStatus(getApplicationId(), PREVIOUS_PRODUCTION_LOT, previousProductionLot);
							getComponentStatusDao().save(componentStatus);
						} else {
							getPanel().setBackground(getPropertyBean().getColorNeutral());
						}
					}
					if (ArrayUtils.isNotEmpty(getPropertyBean().getStationDisplayCodes())) {
						getPanel().populateDisplayFields(getPanel().getDisplayFields(), getCodesForProductSpec(getProductSpec(), getPropertyBean().getStationDisplayCodes()));
					}
					if (getStationDisplayFormats() != null) {
						getPanel().populateDisplayFields(getPanel().getDisplayFields(), getCodesForPrintAttributes(product));
					}
					if (ArrayUtils.isNotEmpty(getStationJobCodes())) {
						loadProductCodes(getProductSpec());
						if (autoConfirm) {
							for (CodeBroadcastCode code : getCodes()) {
								code.setConfirmed(true);
							}
							getPanel().getConfirmButton().setEnabled(false);
							setManualProductIdEntryEnabled(false);
						}
						getPanel().populateCodePanel(getPanel().getCodePanel(), getPanel().getCodePanelFields(), getPanel().getConfirmButton(), getCodes(), autoConfirm || getPropertyBean().isDisplayOnly());
					} else if (!autoConfirm) {
						getPanel().getConfirmButton().setEnabled(true);
					}
					// disable product re-entry if a product cannot replace an on-screen product
					if (!getPropertyBean().isAllowProductReplacement()) {
						setManualProductIdEntryEnabled(false);
					}
					if (autoConfirm) {
						doConfirm(true);
					}
				} catch (Exception e) {
					clearTabWithProductId(productId);
					getPanel().displayErrorMessage("Invalid product id: an error occurred during product processing", e);
				} finally {
					getPanel().setCursor(Cursor.getDefaultCursor());
					restartTimeoutTimer();
				}
			}
		});
	}

	private void clearTabWithProductId(String productId) {
		if (getPropertyBean().isAutoConfirmDeviceData()) {
			getProductPanel().clearCurrentProduct();
			setCodes(null);
		} else {
			getPanel().clearTab();
		}
		getProductPanel().getProductIdTextField().setText(productId);
		getProductPanel().getProductIdTextField().selectAll();
	}

	protected void setManualProductIdEntryEnabled(final boolean enabled) {
		getProductPanel().getProductIdButton().setEnabled(enabled);
		getProductPanel().getProductIdTextField().setEditable(enabled);
	}

	protected String cleanProductId(final String dirtyProductId) {
		if (StringUtils.isEmpty(dirtyProductId)) {
			return dirtyProductId;
		}
		// remove leading and trailing whitespace
		String productId = StringUtils.trim(dirtyProductId);
		// remove unwanted leading characters
		if (getLeadingCharsToRemove() != null) {
			for (String leadingCharToRemove : getLeadingCharsToRemove()) {
				if (productId.toUpperCase().startsWith(leadingCharToRemove)) {
					productId = productId.substring(leadingCharToRemove.length());
					break;
				}
			}
		}
		return productId;
	}

	private String getValidTrackingStatusesSummary() {
		final List<PreviousLine> previousLines = ServiceFactory.getDao(PreviousLineDao.class).findAllByProcessPointId(getApplicationId());
		if (previousLines == null || previousLines.isEmpty()) {
			return null;
		}
		StringBuilder summaryBuilder = new StringBuilder();
		for (PreviousLine previousLine : previousLines) {
			if (summaryBuilder.length() > 0) {
				summaryBuilder.append(", ");
			}
			summaryBuilder.append(getTrackingStatusSummary(previousLine.getId().getPreviousLineId()));
		}
		return summaryBuilder.toString();
	}

	private String getTrackingStatusSummary(String trackingStatus) {
		if (trackingStatus == null) {
			return "null (not statused)";
		}
		Line trackingStatusLine = ServiceFactory.getDao(LineDao.class).findByKey(trackingStatus);
		String trackingStatusName = trackingStatusLine == null ? null : trackingStatusLine.getLineName();
		if (StringUtils.isNotEmpty(trackingStatusName)) {
			StringBuilder summaryBuilder = new StringBuilder();
			summaryBuilder.append(trackingStatus);
			summaryBuilder.append(" (");
			summaryBuilder.append(trackingStatusName);
			summaryBuilder.append(")");
			return summaryBuilder.toString();
		} else {
			return trackingStatus;
		}
	}

	protected boolean isTrackingConfigured() {
		ProcessPoint processPoint = getProcessPointDao().findById(getApplicationId());
		if (processPoint != null) {
			return processPoint.isTrackingPoint() || processPoint.isPassingCount();
		} else {
			return false;
		}
	}

	private void loadProductCodes(final String productSpecCode) {
		try {
			setCodes(getCodesForProductSpec(productSpecCode, getStationJobCodes()));
			String result = validateCodesForProductSpec(getCodes(), productSpecCode);
			if (result != null) getPanel().displayErrorMessage(result, true);
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.getMessage(), e);
			setCodes(null);
		}
	}

	@Override
	public void handleClearTab() {
		super.handleClearTab();
		if (!getPropertyBean().isAutoConfirmDeviceData()) {
			setProductData(null, null);
			setCodes(null);
		}
	}

	private boolean executeCheck(BaseProduct product, ProcessPoint processPoint) {
		List<String> checkResults = executeProductChecks(product, processPoint);
		if (checkResults.size() > 0) {
			StringBuffer msg = new StringBuffer();
			msg.append(product.getProductId() + " failed the following Product Checks : \n");
			for (int i = 0; i < checkResults.size(); i++) {
				msg.append(checkResults.get(i));
				if (i != checkResults.size() - 1) {
					msg.append("\n");
				}
			}
			getPanel().displayErrorMessage(msg.toString(), true);
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<String> executeProductChecks(BaseProduct frame, ProcessPoint processPoint) {
		String[] productCheckTypes = getProductCheckPropertyBean().getProductCheckTypes();
		Map<String, Object> checkResults = ProductCheckUtil.check(frame, processPoint, productCheckTypes);
		List<String> productCheckTypesList = Arrays.asList(productCheckTypes);
		List<String> failedProductCheckList = new ArrayList<String>();
		for (String checkType : productCheckTypesList) {
			if (checkResults.get(checkType) != null) {
				if (checkResults.get(checkType) instanceof List && ((List) (checkResults.get(checkType))).size() > 0) {
					List<Object> resultList = (List<Object>) checkResults.get(checkType);
					String msg = "";
					for (Object obj : resultList) {
						if (obj instanceof String) {
							String s = (String) obj;
							if (s.trim().length() > 0)
								msg = msg + s + ",";
						}
						if (obj instanceof PartResultData) {
							PartResultData s = (PartResultData) obj;
							msg = msg + "\n" + s.part_Name.trim() + "-" + s.part_Reason + ",";
						}
					}
					failedProductCheckList.add(checkType + " : " + msg);
				} else if (checkResults.get(checkType) instanceof Boolean
						&& checkResults.get(checkType).equals(Boolean.FALSE)) {
					failedProductCheckList.add(checkType);
				} else if (checkResults.get(checkType) instanceof Map
						&& ((Map) (checkResults.get(checkType))).size() > 0) {
					Map<String, Object> resultsMap = (Map<String, Object>) checkResults.get(checkType);
					String msg = "";
					for (String key : resultsMap.keySet()) {
						if (resultsMap.get(key) instanceof Boolean && resultsMap.get(key).equals(Boolean.TRUE)) {
							if (key.trim().length() > 0)
								msg = msg + key + ",";
						}
					}
					failedProductCheckList.add(checkType + " : " + msg);
				} else {
					String msg = checkResults.get(checkType).toString();
					if (!StringUtils.isBlank(msg)) {
						failedProductCheckList.add(checkType + " : " + msg);
					}
				}
			}
		}
		return failedProductCheckList;
	}
}
