package com.honda.galc.client.codebroadcast;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.ErrorData;
import com.honda.galc.device.dataformat.ErrorDataIdentifier;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.PrintAttributeConvertor;
import com.honda.galc.service.property.PropertyService;

public abstract class CodeBroadcastController {

	private final String applicationId;
	private final String trackingDeviceId;
	private final Logger logger;
	private final Pattern displayPattern;
	private final String productIdLabelText;
	private final String productSpecLabelText;
	private final CodeBroadcastPanel panel;
	private final Timer timeoutTimer;
	private ProductType productType;
	private BroadcastDestinationDao broadcastDestinationDao;
	private BuildAttributeDao buildAttributeDao;
	private ProductionLotDao productionLotDao;
	private PrintAttributeConvertor printAttributeConvertor;
	private boolean errorState;



	/*
	 * --------------------------------------------------
	 * Getters/Setters
	 * --------------------------------------------------
	 */
	protected String getApplicationId() { return this.applicationId; }
	protected String getTrackingDeviceId() { return this.trackingDeviceId; }
	protected Logger getLogger() { return this.logger; }
	protected List<BroadcastDestination> getBroadcastDestinations() { return getBroadcastDestinationDao().findAllByProcessPointId(getApplicationId()); }
	public boolean isPopupErrorCode(String code) {
		if (code == null)
			return false;
		for (String popupErrorCode : getPropertyBean().getPopupErrorCodes()) {
			if (code.equals(popupErrorCode))
				return true;
		}
		return false;
	}
	public String getProductIdLabelText() { return this.productIdLabelText; }
	public String getProductSpecLabelText() { return this.productSpecLabelText; }
	protected CodeBroadcastPanel getPanel() { return this.panel; }
	protected CodeBroadcastPropertyBean getPropertyBean() { return getPanel().getPropertyBean(); }
	public ProductType getProductType() {
		if (productType == null) {
			productType = getPanel().getMainWindow().getProductType();
		}
		return productType;
	}
	protected BroadcastDestinationDao getBroadcastDestinationDao() {
		if (this.broadcastDestinationDao == null) {
			this.broadcastDestinationDao = ServiceFactory.getDao(BroadcastDestinationDao.class);
		}
		return this.broadcastDestinationDao;
	}
	protected BuildAttributeDao getBuildAttributeDao() {
		if (this.buildAttributeDao == null) {
			this.buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		}
		return this.buildAttributeDao;
	}
	protected ProductionLotDao getProductionLotDao() {
		if (this.productionLotDao == null) {
			this.productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
		}
		return this.productionLotDao;
	}
	protected PrintAttributeConvertor getPrintAttributeConvertor() {
		if (this.printAttributeConvertor == null) {
			this.printAttributeConvertor = new PrintAttributeConvertor(Arrays.asList(getStationDisplayFormats()), getLogger());
		}
		return this.printAttributeConvertor;
	}
	public boolean isErrorState() {
		return this.errorState;
	}
	public void setErrorState(boolean errorState) {
		this.errorState = errorState;
	}
	protected abstract String[] getStationJobCodes();
	protected abstract List<CodeBroadcastCode> getCodes();
	protected abstract PrintAttributeFormat[] getStationDisplayFormats();



	/*
	 * --------------------------------------------------
	 * Constructors
	 * --------------------------------------------------
	 */

	public CodeBroadcastController(CodeBroadcastPanel panel) {
		this.panel = panel;
		this.applicationId = getPanel().getMainWindow().getApplication().getApplicationId();
		this.trackingDeviceId = PropertyService.getProperty(getPanel().getMainWindow().getApplicationContext().getTerminal().getHostName(), "TRACKING_DEVICE_ID", null);
		this.logger = getPanel().getLogger();

		{
			ProductTypeData productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(getProductType().getProductName().toUpperCase());
			if (productTypeData != null) {
				this.productIdLabelText = productTypeData.getProductIdLabel();
				this.productSpecLabelText = productTypeData.getProductSpecCodeLabel();
			}
			else {
				this.productIdLabelText = "PROD_ID";
				this.productSpecLabelText = "PROD_SPEC";
			}
		}
		this.errorState = false;

		final int timeoutInterval = getPropertyBean().getTimeoutInterval() * 1000;
		if (timeoutInterval > -1) {
			this.timeoutTimer = new Timer(timeoutInterval, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doRefresh();
				}
			});
			this.timeoutTimer.setRepeats(false);
		} else {
			this.timeoutTimer = null;
		}

		this.displayPattern = Pattern.compile("_?" + getPropertyBean().getStationName() + "_?");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { initialize(); }
		});
	}



	/*
	 * --------------------------------------------------
	 * Initialization methods
	 * --------------------------------------------------
	 */

	protected void initialize() {
		initConnections();
	}

	@SuppressWarnings("serial")
	protected void initConnections() {
		if (!getPropertyBean().isDisplayOnly()) {
			{
				final String functionKey = getPropertyBean().getFunctionKeyConfirm();
				if (StringUtils.isNotBlank(functionKey)) {
					final String clickConfirmButton = "clickConfirmButton";
					getPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(functionKey.toUpperCase()), clickConfirmButton);
					getPanel().getActionMap().put(clickConfirmButton, new AbstractAction() {
						@Override
						public void actionPerformed(ActionEvent actionEvent) {
							getPanel().getConfirmButton().doClick(0);
						}
					});
				}
			}
			getPanel().getConfirmButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					doConfirm();
				}
			});
		}
		if (getPropertyBean().isEnableRefreshButton()) {
			final String functionKey = getPropertyBean().getFunctionKeyRefresh();
			if (StringUtils.isNotBlank(functionKey)) {
				final String clickRefreshButton = "clickRefreshButton";
				getPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(functionKey.toUpperCase()), clickRefreshButton);
				getPanel().getActionMap().put(clickRefreshButton, new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent actionEvent) {
						getPanel().getRefreshButton().doClick(0);
					}
				});
			}
			getPanel().getRefreshButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					doRefresh();
				}
			});
		}
	}

	protected void doRefresh() {
		try {
			Logger.getLogger().info("Refreshing screen");
			getPanel().clearTab();
			stopTimeoutTimer();
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.toString(), e);
		}
	}

	protected abstract void doConfirm();



	/*
	 * --------------------------------------------------
	 * Helper/utility methods
	 * --------------------------------------------------
	 */

	protected void startTimeoutTimer() {
		if (this.timeoutTimer != null) {
			if (!this.timeoutTimer.isRunning()) {
				this.timeoutTimer.start();
			}
		}
	}

	protected void restartTimeoutTimer() {
		if (this.timeoutTimer != null) {
			if (this.timeoutTimer.isRunning()) {
				this.timeoutTimer.restart();
			} else {
				this.timeoutTimer.start();
			}
		}
	}

	protected void stopTimeoutTimer() {
		if (this.timeoutTimer != null) {
			if (this.timeoutTimer.isRunning()) {
				this.timeoutTimer.stop();
			}
		}
	}

	public void handleClearTab() {
		stopTimeoutTimer();
		checkMessage();
	}

	public void checkMessage() {
		if (isErrorState() && !getPanel().isErrorMessage()) {
			requestTrigger();
		} else if (!isErrorState()) {
			getPanel().clearMessage();
		}
	}

	protected void requestTrigger() {
		for (BroadcastDestination broadcastDestination : getBroadcastDestinations()) {
			if (!getPropertyBean().getArgumentRequestTrigger().equals(broadcastDestination.getArgument())) continue;
			DataContainer dataContainer = new DefaultDataContainer();
			dataContainer.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
			if (getTrackingDeviceId() != null) {
				dataContainer.put(DataContainerTag.DEVICE_ID, getTrackingDeviceId());
			}
			getService(BroadcastService.class).broadcast(getApplicationId(), broadcastDestination.getSequenceNumber(), dataContainer);
		}
	}

	public Object received(IDeviceData deviceData) {
		if (deviceData instanceof ErrorData) {
			ErrorData message = (ErrorData) deviceData;
			if (message instanceof ErrorDataIdentifier) {
				ErrorDataIdentifier identifier = (ErrorDataIdentifier) message;
				if (getTrackingDeviceId() != null && !getTrackingDeviceId().equals(identifier.getIdentifier())) {
					return deviceData;
				}
			}
			if (message.isErrorBit()) {
				setErrorState(true);
				if (StringUtils.isEmpty(message.getErrorCode()) && StringUtils.isEmpty(message.getErrorMessage())) {
					getPanel().displayErrorMessage("null");
				}
				else {
					String error = buildInfoMessage(message.getErrorCode(), message.getErrorMessage());
					getPanel().displayErrorMessage(error, isPopupErrorCode(message.getErrorCode()));
				}
			} else {
				setErrorState(false);
				if (getPanel().isErrorMessage()) {
					getPanel().clearMessage();
				}
			}
		}
		return deviceData;
	}

	public ChangeListener getCodePanelConfirmListener(final CodeBroadcastCode code, final JButton codeConfirmField) {
		return (new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if (code.getConfirmed()) {
					codeConfirmField.setBackground(getPropertyBean().getColorConfirmed());
					Logger.getLogger().info("Confirmed code: " + code.getLabel());
					getPanel().getConfirmButton().setEnabled(isCodesConfirmed());
				} else {
					codeConfirmField.setBackground(getPropertyBean().getColorUnconfirmed());
					Logger.getLogger().info("Unconfirmed code: " + code.getLabel());
					getPanel().getConfirmButton().setEnabled(false);
				}
				restartTimeoutTimer();
			}
		});
	}

	protected boolean isCodesConfirmed() {
		return isCodesConfirmed(getCodes());
	}

	protected boolean isCodesConfirmed(List<CodeBroadcastCode> codes) {
		if (codes == null || codes.isEmpty()) return true;
		for (CodeBroadcastCode code : codes) {
			if (!code.getConfirmed()) {
				return false;
			}
		}
		return true;
	}

	protected void unconfirm() {
		unconfirm(getCodes());
	}

	protected void unconfirm(List<CodeBroadcastCode> codes) {
		if (codes == null || codes.isEmpty()) {
			return;
		}
		for (CodeBroadcastCode code : codes) {
			code.setConfirmed(false);
		}
	}

	protected BuildAttribute getSingleBuildAttribute(String attribute) {
		List<BuildAttribute> results = getBuildAttributeDao().findAllByAttribute(attribute);
		if (results == null || results.isEmpty()) return null;
		if (results.size() != 1) throw new RuntimeException(String.format("Multiple attributes named %1$s are defined", attribute));
		BuildAttribute result = results.get(0);
		return result;
	}

	public static String concatWithUnderscore(String... strings) {
		if (ArrayUtils.isEmpty(strings)) return null;
		StringBuilder builder = new StringBuilder();
		final int last = strings.length-1;
		for (int i = 0; i < last; i++) {
			builder.append(strings[i]);
			builder.append("_");
		}
		builder.append(strings[last]);
		return builder.toString();
	}

	protected String validateCodesForProductSpec(List<CodeBroadcastCode> codes, String productSpec) {
		StringBuilder errorBuilder = null;
		if (codes == null)
			return null;
		for (CodeBroadcastCode code : codes) {
			if (code.getValue() == null) {
				if (errorBuilder == null) {
					errorBuilder = new StringBuilder();
					errorBuilder.append("Missing attributes [");
					errorBuilder.append(code.getLabel());
					continue;
				}
				else {
					errorBuilder.append(",");
					errorBuilder.append(code.getLabel());
					continue;
				}
			}
		}
		if (errorBuilder != null) {
			errorBuilder.append("] for product spec code [");
			errorBuilder.append(productSpec);
			errorBuilder.append("];");
			return errorBuilder.toString();
		}
		return null;
	}

	protected List<CodeBroadcastCode> getCodesForProductSpec(final String productSpec, final String[] codeNames) {
		if (ArrayUtils.isEmpty(codeNames)) {
			return null;
		}
		List<CodeBroadcastCode> codes = new ArrayList<CodeBroadcastCode>();
		for (String codeName : codeNames) {
			BuildAttribute codeAttribute = getBuildAttributeDao().findById(codeName, productSpec);
			CodeBroadcastCode code = new CodeBroadcastCode(codeName, getCodeDisplayName(codeName), codeAttribute == null ? null : codeAttribute.getAttributeValue());
			codes.add(code);
		}
		return codes;
	}

	protected List<CodeBroadcastCode> getCodesForPrintAttributes(BaseProduct product) {
		getPrintAttributeConvertor().make(prepareProductDataContainer(product));
		List<Object> values = getPrintAttributeConvertor().getValues();
		List<CodeBroadcastCode> codes = new ArrayList<CodeBroadcastCode>();
		for (int i = 0; i < getStationDisplayFormats().length; i++) {
			final String key = getStationDisplayFormats()[i].getAttribute();
			final String value = ObjectUtils.toString(values.get(i));
			CodeBroadcastCode code = new CodeBroadcastCode(key, key, value);
			codes.add(code);
		}
		return codes;
	}

	private DataContainer prepareProductDataContainer(BaseProduct product) {
		DefaultDataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());
		dc.put(DataContainerTag.PRODUCT_TYPE, product.getProductType());
		dc.put(DataContainerTag.PRODUCTION_LOT, product.getProductionLot());
		dc.put(DataContainerTag.PROCESS_POINT_ID, getApplicationId());
		dc.put(Product.class, product);
		dc.put(product.getClass(), product);
		ProductionLot prodLot = getProductionLotDao().findByKey(product.getProductionLot());
		dc.put(ProductionLot.class, prodLot);
		return dc;
	}

	public String getCodeDisplayName(String stationJobCode) {
		if (stationJobCode == null) return null;
		return displayPattern.matcher(stationJobCode).replaceAll("");
	}

	protected boolean broadcastCodes(List<CodeBroadcastCode> codes, String prodId, String colorCode, final boolean clearTabOnSuccess) {
		if (!isCodesConfirmed(codes)) {
			getPanel().displayWarningMessage(buildInfoMessage(getPropertyBean().getCodeNg(), "CODES NOT CONFIRMED"), isPopupErrorCode(getPropertyBean().getCodeNg()));
			return false;
		}
		List<DataContainer> broadcastResults = new ArrayList<DataContainer>();
		for (BroadcastDestination broadcastDestination : getBroadcastDestinations()) {
			if (getPropertyBean().getArgumentBroadcastCodes().equals(broadcastDestination.getArgument())) {
				DataContainer dataContainer = new DefaultDataContainer();
				if (!StringUtils.isEmpty(prodId)) {
					dataContainer.put(DataContainerTag.PRODUCT_ID, prodId);
				}
				if (!StringUtils.isEmpty(colorCode)) {
					dataContainer.put(DataContainerTag.COLOR_CODE, colorCode);
				}
				if (getTrackingDeviceId() != null) {
					dataContainer.put(DataContainerTag.DEVICE_ID, getTrackingDeviceId());
				}
				if (codes != null) {
					for (CodeBroadcastCode code : codes) {
						dataContainer.put(code.getKey(), code.getValue());
					}
				}
				dataContainer.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());

				DataContainer result = getService(BroadcastService.class).broadcast(getApplicationId(), broadcastDestination.getSequenceNumber(), dataContainer);
				broadcastResults.add(result);
			}
			else if (broadcastDestination.isAutoEnabled() && !StringUtils.isEmpty(prodId)) {
				getService(BroadcastService.class).broadcast(getApplicationId(), broadcastDestination.getSequenceNumber(), prodId);
			}
		}
		if (getPropertyBean().isIgnoreBroadcastResults()) {
			setErrorState(false);
			if (clearTabOnSuccess) {
				getPanel().clearTab();
			}
			getPanel().playOKSound();
			getPanel().displayMessage(getConfirmationSummary(false, codes, prodId, colorCode), false);
			return true;
		}
		return validateBroadcastResults(broadcastResults, clearTabOnSuccess);
	}

	private boolean validateBroadcastResults(List<DataContainer> broadcastResults, final boolean clearTabOnSuccess) {
		StringBuilder successBuilder = new StringBuilder();
		StringBuilder warningBuilder = new StringBuilder();
		StringBuilder errorBuilder = new StringBuilder();
		boolean popup = false;
		for (DataContainer broadcastResult : broadcastResults) {
			String infoCode = null, infoMessage = null;
			if (broadcastResult != null) {
				infoCode = (String) broadcastResult.get(DataContainerTag.INFO_CODE);
				infoMessage = buildInfoMessage(infoCode, (String) broadcastResult.get(DataContainerTag.INFO_MESSAGE));
			}
			if (infoCode == null) {
				popup = true;
				appendInfoMessage(errorBuilder, "Failed to receive response");
				continue;
			}
			if (!popup && isPopupErrorCode(infoCode)) {
				popup = true;
			}
			if (infoCode.equals(getPropertyBean().getCodeOk())) {
				appendInfoMessage(successBuilder, infoMessage);
			}
			else if (infoCode.equals(getPropertyBean().getCodeNg())) {
				appendInfoMessage(warningBuilder, infoMessage);
			}
			else {
				appendInfoMessage(errorBuilder, infoMessage);
			}
		}
		if (errorBuilder.length() != 0) {
			setErrorState(true);
			String errorMessage = errorBuilder.toString();
			getPanel().displayErrorMessage(errorMessage, popup);
			return false;
		} else if (warningBuilder.length() != 0) {
			getPanel().displayWarningMessage(warningBuilder.toString(), popup);
			return false;
		} else {
			setErrorState(false);
			if (clearTabOnSuccess) {
				getPanel().clearTab();
			}
			getPanel().playOKSound();
			getPanel().displayMessage(successBuilder.toString(), popup);
			return true;
		}
	}

	protected String buildInfoMessage(String infoCode, String infoMessage) {
		StringBuilder errorBuilder = new StringBuilder();
		errorBuilder.append("[");
		errorBuilder.append(infoCode);
		errorBuilder.append("]: ");
		errorBuilder.append(infoMessage);
		return errorBuilder.toString();
	}

	private void appendInfoMessage(StringBuilder builder, String infoMessage) {
		if (builder.length() != 0) builder.append(";\n");
		builder.append(infoMessage);
	}

	protected String getConfirmationSummary(final boolean isPrompt, final List<CodeBroadcastCode> codes, final String prodId, final String colorCode) {
		StringBuilder confirmationSummaryBuilder = new StringBuilder();
		if (isBroadcastCodes()) {
			confirmationSummaryBuilder.append(isPrompt ? "Broadcast" : "Broadcasted");
		} else {
			confirmationSummaryBuilder.append(isPrompt ? "Confirm" : "Confirmed");
		}
		if (codes != null) {
			confirmationSummaryBuilder.append(" codes: [");
			confirmationSummaryBuilder.append(getCodesSummary(codes));
			confirmationSummaryBuilder.append("]");
		}
		if (!StringUtils.isEmpty(prodId)) {
			if (codes != null) {
				confirmationSummaryBuilder.append(",");
			}
			confirmationSummaryBuilder.append(" product id: ");
			confirmationSummaryBuilder.append(prodId);
		}
		if (!StringUtils.isEmpty(colorCode)) {
			if (codes != null || !StringUtils.isEmpty(prodId)) {
				confirmationSummaryBuilder.append(",");
			}
			confirmationSummaryBuilder.append(" color code: ");
			confirmationSummaryBuilder.append(colorCode);
		}
		if (isPrompt) {
			confirmationSummaryBuilder.append("?");
		}
		return confirmationSummaryBuilder.toString();
	}

	private boolean isBroadcastCodes() {
		for (BroadcastDestination broadcastDestination : getBroadcastDestinations()) {
			if (getPropertyBean().getArgumentBroadcastCodes().equals(broadcastDestination.getArgument())) {
				return true;
			}
		}
		return false;
	}

	protected String getCodesSummary(List<CodeBroadcastCode> codes) {
		if (codes == null) {
			return null;
		}
		StringBuilder summaryBuilder = new StringBuilder();
		Iterator<CodeBroadcastCode> iter = codes.iterator();
		while (iter.hasNext()) {
			CodeBroadcastCode code = iter.next();
			summaryBuilder.append(code.getLabel());
			summaryBuilder.append(": ");
			summaryBuilder.append(code.getValue());
			if (iter.hasNext()) {
				summaryBuilder.append(", ");
			}
		}
		return summaryBuilder.toString();
	}

}
