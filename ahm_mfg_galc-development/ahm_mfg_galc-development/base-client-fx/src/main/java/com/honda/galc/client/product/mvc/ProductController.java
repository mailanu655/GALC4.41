package com.honda.galc.client.product.mvc;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.RelaunchUtil;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.enumtype.KeyboardEventType;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.AbstractProductIdProcessor;
import com.honda.galc.client.product.IProductIdProcessor;
import com.honda.galc.client.product.ProcessEvent;
import com.honda.galc.client.product.ProductDeviceListener;
import com.honda.galc.client.product.pane.AbstractProductIdlePane;
import com.honda.galc.client.product.pane.AbstractProductInputPane;
import com.honda.galc.client.product.pane.ProductIdlePaneContainer;
import com.honda.galc.client.product.pane.ProductInputPane;
import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.product.process.IProcessController;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.schedule.KeyboardEvent;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.schedule.SchedulingEvent;
import com.honda.galc.client.schedule.SchedulingEventType;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.LoginDialogWithoutPassword;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.ProductCancelledEvent;
import com.honda.galc.client.ui.event.ProductFinishedEvent;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiInspectionUtils;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.MdrsUserTrainingCache;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.notification.service.IProductPassedNotification;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.LDAPService;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductController</code> is ... .
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
 * @author Jeffray Huang
 */
public class ProductController implements IProductPassedNotification{

	protected enum State {
		NOT_INITIALIZED, IDLE, VALIDATING, PROCESSING
	};

	protected AbstractProductClientPane view;
	protected ProductModel model;

	protected State state;

	protected ProductDeviceListener deviceListener;

	protected AbstractProductIdProcessor productIdProcessor;

    protected ClientAudioManager audioManager;

	String warningMessage = "";

	private MdrsUserTrainingCache mdrsUserTrainingCache = null;

	public final String TRAINING_VALIDITY_CHECK_ENABLED = "TRAINING_VALIDITY_CHECK_ENABLED";
	boolean isTrained = false;
	boolean removeI = false;

	public static final String PROCESS_POINT_LIST = "PROCESS_POINT_LIST";
	
	protected KeyBoardPopup keyboardPopup;
	private ProductSequenceDao productSequenceDao;

	private ProductInputPane productInputPane;
	
	private int inputPaneHeight;
	private String stationId;
	
	public ProductController(AbstractProductClientPane view,ProductPropertyBean productPropertyBean) {
		this.view = view;
		String applicationId = "";
		if(getView().getMainWindow().getApplication() != null)  {
			applicationId = getView().getMainWindow().getApplication().getApplicationId();
		}
		this.model = new ProductModel(getView().getMainWindow().getApplicationContext(), applicationId);
		if(productPropertyBean != null) this.model.setProperty(productPropertyBean);
		this.state = State.NOT_INITIALIZED;
		this.deviceListener = new ProductDeviceListener(this, view.getLogger());
		this.productIdProcessor = createProductIdProcessor();
		this.audioManager = createAudioManager();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				createKeyBoardPopup();
			}
		});
		EventBusUtil.register(this);
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	protected void setAudioManager(ClientAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	public void processInputNumberInvoke(String inputNumber) {
		getView().getInputPane().setProductId(inputNumber);
		productIdProcessor.processInputNumber(null);
	}

	/**
	 * received the product id refresh signal from PLC
	 */
	public void productIdRefresh() {
		String productId = getModel().getExpectedProductId();
		if (!StringUtils.isEmpty(productId))
			processInputNumberInvoke(productId);
	}

	public void clickNextButtonInvoke() {
	}

	public void cycleComplete() {
		if (!getModel().isTrainingMode()) {
			getModel().invokeTracking();

		}
		if (getModel().getProperty().isCheckExpectedProductId()) {
			getModel().getExpectedProductManager().saveNextExpectedProduct();
			getView().getInputPane().getExpectedProductIdField()
					.setText(getModel().getExpectedProductManager().getExpectedProductId());
		}
		EventBusUtil.publish(new ProductFinishedEvent(getModel()));
		toIdle();
	}


	public void overRide(String prodSpecCode, String message) {
		LoginStatus status = LoginDialogWithoutPassword
				.login(message + ". Press cancel or have TC/TL (or above) scan in to grant override.", true);

		if (status.equals(LoginStatus.OK)) {
			String tmtcUserid = ClientMainFx.getInstance().getAccessControlManager().getUserName();
			String assoUserid = getModel().getApplicationContext().getUserId();
			String tmtcUserGroup = getModel().getApplicationContext().getApplicationPropertyBean().getTmtcUserGroup();

			List<UserSecurityGroupId> userSecGroup = LDAPService.getInstance().getMemberOf(tmtcUserid);
			boolean tmtcIsPartOfGroup = false;
			for (UserSecurityGroupId secGrp : userSecGroup) {
				if (secGrp.getSecurityGroup().trim().equalsIgnoreCase(tmtcUserGroup.trim())) {
					tmtcIsPartOfGroup = true;
					break;
				}
			}

			if (tmtcIsPartOfGroup) {
				// cache user detail
				if (!isTrained) {
					getMdrsUserTrainingCache().put(assoUserid.trim() + "-" + prodSpecCode,
							new String(assoUserid + "," + tmtcUserid + "," + prodSpecCode));
					getMdrsUserTrainingCache().getCache().dispose();
				}
			} else {
				MessageDialog.showError("Associate does not have authority to grant override.");
				return;
			}
		} else {
			MessageDialog.showError("User chosen cancel, initiating restart");
			try {
				RelaunchUtil.restart();
			} catch (IOException ex) {
				getLogger().error("Failed to restart the application " + ex.getMessage());
			}
		}
	}

	public Boolean checkInputNumber(String inputNumber) {

		Map<String, Object> messages = productIdProcessor.validate(inputNumber);

		if (!messages.isEmpty())
			return false;
		return true;
	}

	public void startTracking(String inputNumber) {
		String trackingPP = getView().getMainWindow().getApplicationProperty("TRACKING_PROCESSPOINTID");
		if (StringUtils.isEmpty(trackingPP))
			return;

		getLogger().info("Start first time tracking");
		ProductHistory productHistory = ProductTypeUtil.createProductHistory(inputNumber, trackingPP,
				getModel().getProductType());

		if (productHistory != null) {
			productHistory.setAssociateNo(model.getApplicationContext().getUserId());
			// TODO::productHistory.setApproverNo(model.getApplicationContext().getApproverNo());
			productHistory.setApproverNo("");
			productHistory.setProcessPointId(trackingPP);
			productHistory.setProductId(inputNumber);
		}

		getService(TrackingService.class).track(ProductType.getType(getModel().getProductType()), productHistory);

		String COMPLETE_FLG = "3";// No model changed
		if (getModel().isDisplayCCP())
			COMPLETE_FLG = "2";// model changed

		getModel().broadcastDataContainer(COMPLETE_FLG);
	}

	public void startProduct(BaseProduct product) {
		getLogger().info(String.format("Start Processing product: %s ", product));
		getModel().setProduct(product);
		EventBusUtil.publish(new ProductStartedEvent(getModel()));

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("product", product);

		setWarningMessage(product.getProductId());
		if (!getModel().isTrainingMode() && getModel().getProperty().isAutoTracking())
			startTracking(product.getProductId());

		setState(State.VALIDATING);
		getView().getInfoPane().setInfo(model);
		getView().showInfoPane();

		getView().getProductProcessPane().prepareProcessViews(product);
		getView().getProductProcessPane().setFirstEnabledProcessViewSelected();

		getView().showProcessPane();
		
		if (!getModel().isTrainingMode()) {
			getModel().invokeBroadcastService(CheckPoints.BEFORE_PRODUCT_PROCESSED);
		}
		
		setState(State.PROCESSING);
	}

	private void setWarningMessage(String inputNumber) {

		String trackingPP = getView().getMainWindow().getApplicationProperty("TRACKING_PROCESSPOINTID");
		if (StringUtils.isEmpty(trackingPP))
			return;

		String headlessProcessPoint = getView().getMainWindow().getApplicationProperty("HEADLESS_PROCESSPOINTID");
		if (!ProductTypeUtil
				.getProductHistoryDao(
						getView().getMainWindow().getApplicationContext().getProductTypeData().getProductType())
				.hasProductHistory(inputNumber, getModel().getProcessPointId())
				&& ProductTypeUtil
						.getProductHistoryDao(
								getView().getMainWindow().getApplicationContext().getProductTypeData().getProductType())
						.hasProductHistory(inputNumber, trackingPP)) {
			if (StringUtils.isEmpty(headlessProcessPoint) || ProductTypeUtil
					.getProductHistoryDao(
							getView().getMainWindow().getApplicationContext().getProductTypeData().getProductType())
					.hasProductHistory(inputNumber, headlessProcessPoint)) {
				warningMessage = "Process complete - Release the pallet";
			} else {
				warningMessage = "Process Incomplete - Perform data collection";
			}
		}

		if (warningMessage.length() > 0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					getView().setMessage(warningMessage, Color.YELLOW);
				}
			});
		}
	}

	public void finishProduct() {

		BaseProduct product = getModel().getProduct();
		List<AbstractProcessView<?, ?>> processes = getView().getProductProcessPane().getActiveProcessViews();
		if (!processes.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("The following process are not finished and are required.");
			sb.append("\nPlease complete them before finishing product.\n");
			for (int i = 0; i < processes.size(); i++) {
				sb.append(" - ").append(processes.get(i).getController().getProcessName());
				if (i < processes.size() - 1) {
					sb.append("\n");
				}
			}

			getView().getProductProcessPane().selectProcessView(0);
			return;
		}

		getLogger().info(String.format("Finish Processing product: %s ", product));

		EventBusUtil.publish(new ProductFinishedEvent(getModel()));
		if (!getModel().isTrainingMode()) {
			getModel().invokeTracking();
			getModel().invokeBroadcastService(CheckPoints.AFTER_PRODUCT_PROCESSED);
		}

		if (getModel().getProperty().isSendDataCollectionComplete()) {
			getDeviceListener().sendDataCollectionComplete();
		}

		toIdle();
	}

	public void cancel() {
		BaseProduct product = getModel().getProduct();
		getLogger().check(String.format("Cancel Processing product: %s ", product));
		getModel().setSkipped(true);
		getModel().setLastProductSkipped(true);
		EventBusUtil.publish(new ProductCancelledEvent(getModel()));
		toIdle();
		if (getModel().getProperty().isSendDataCollectionComplete()) {
			getDeviceListener().sendDataCollectionInComplete();
		}
	}

	// === transition api === //
	public void toIdle() {
		updateSequenceAndNextExpectedProduct();
		getView().getProductProcessPane().resetProcessViews();
		resetModel();
		resetUi();
		TextFieldState.EDIT.setState(getView().getInputPane().getProductIdField());
		getView().showInputPane();
		getView().showIdlePane();
		getView().getProductProcessPane().setFirstEnabledProcessViewSelected();
		
		getView().getProductIdlePaneContainer().toIdle();
		UiUtils.requestFocus(getView().getInputPane().getProductIdField());
		setState(State.IDLE);
	}
	
	public void toIdleAfterProcess() {
		getView().getProductProcessPane().resetProcessViews();
		TextFieldState.EDIT.setState(getView().getInputPane().getProductIdField());
		getView().showInputPane();
		getView().showIdlePane();
		getView().getProductProcessPane().setFirstEnabledProcessViewSelected();
		getView().getProductIdlePaneContainer().toIdle();
		UiUtils.requestFocus(getView().getInputPane().getProductIdField());
		setState(State.IDLE);
	}

	protected void resetModel() {
		getModel().setProduct(null);
	}

	protected void resetUi() {
		getView().getInfoPane().setInfo(null);
		getView().getInputPane().setProductId(null);
		getView().clearErrorMessage();
	}

	// === events === //
	@Subscribe()
	public void onProcessEvent(ProcessEvent event) {
		if (event == null) {
			return;
		}
		if (IProcessController.ProcessState.FINISHED.equals(event.getProcessState())) {
			if (getView().getProductProcessPane().getActiveProcessViews().isEmpty()) {
				finishProduct();
			} else {
				getView().getProductProcessPane().selectNextEnabledView();
			}
		} else if ((IProcessController.ProcessState.CANCELLED.equals(event.getProcessState()))) {
			cancel();
		}
	}

	// === config api === //
	public boolean isInIdleState() {
		return ProductController.State.IDLE.equals(getState());
	}

	public boolean isInProcessingState() {
		return ProductController.State.PROCESSING.equals(getState());
	}
	
	public boolean isDcStation() {
		return PropertyService.getPropertyBean(QiPropertyBean.class, getModel().getProcessPointId()).isDcStation();
	}
	
	public boolean isDcProduct() {
		return ProductTypeUtil.isDieCast(getModel().getProductTypeData().getProductType());
	}

	// === get/set === //
	public AbstractProductClientPane getView() {
		return view;
	}

	public ProductTypeData getProductTypeData() {
		return getView().getMainWindow().getApplicationContext().getProductTypeData();
	}

	protected ProductDeviceListener getDeviceListener() {
		return deviceListener;
	}
	

	protected AbstractProductIdProcessor createProductIdProcessor() {
		Class<?> clazz;
		String className = getModel().getProperty().getProductIdProcessor();
		AbstractProductIdProcessor processor = null;
		try {
			clazz = Class.forName(getModel().getProperty().getProductIdProcessor());
			if (!IProductIdProcessor.class.isAssignableFrom(clazz))
				getLogger().error(className + " is not a Product ID processor");
			Class<?>[] parameterTypes = { this.getClass() };
			Object[] parameters = { this };
			Constructor<?> constructor = clazz.getConstructor(parameterTypes);
			processor = (AbstractProductIdProcessor) constructor.newInstance(parameters);
		} catch (Throwable e) {
			getLogger().error("Failed to create observer:" + className);
		}
		return processor;
	}

	protected ClientAudioManager createAudioManager() {
		AudioPropertyBean property = PropertyService.getPropertyBean(AudioPropertyBean.class,
				getModel().getProcessPointId());
		return new ClientAudioManager(property);
	}

	public State getState() {
		synchronized (state) {
			return state;
		}
	}

	public void setState(State state) {
		synchronized (state) {
			this.state = state;
		}
	}

	public ProductModel getModel() {
		return model;
	}

	public Logger getLogger() {
		return getView().getLogger();
	}

	public MdrsUserTrainingCache getMdrsUserTrainingCache() {
		if (mdrsUserTrainingCache == null)
			mdrsUserTrainingCache = new MdrsUserTrainingCache();

		return mdrsUserTrainingCache;
	}

	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if (event == null) {
			return;
		} else if (event.getEventType().equals(ProductEventType.PRODUCT_INPUT_RECIEVED)) {
			productIdProcessor.processInputNumber(event);
		} else if (event.getEventType().equals(ProductEventType.PRODUCT_RESET)) {
			productIdProcessor.productReset(event);
		}
	
	}
	
	protected void updateSequenceAndNextExpectedProduct() {
		if (getModel().getProduct() != null) {

			Boolean checkExpectedProductId = getModel().getProperty().isCheckExpectedProductId();
			if (checkExpectedProductId) {
				//BAK - 2015-12-28 - Do not update when skipped or in bypass
				if (getModel().isBypassExpectedProduct() || getModel().isSkipped()) {
					return;
				}
				
				//BAK - 2015-12-28 - Need to make sure that there is an expected product
				if (StringUtils.isBlank(getModel().getExpectedProductId())) {
					return;
				}
				
				//BAK - 2015-12-28 - Do not update when the expected product is a previous product
				if (!getModel().getExpectedProductId().equals(getModel().getProductId())) {
					if (!getModel().getExpectedProductManager().isProductIdAheadOfExpectedProductId(
							getModel().getExpectedProductId(), getModel().getProductId())) {
						return;
					}
				}
				
				getModel().getExpectedProductManager().saveNextExpectedProduct();
				getView().getInputPane().getExpectedProductIdField().setText(getModel().getExpectedProductManager().getExpectedProductId());
				if (getModel().getProperty().isShowProductSequence()) {
					getView().getInputPane().setProductSequence();
				}
			}
		}
	}
	
	@Subscribe()
	public void onSchedulingEvent(SchedulingEvent event) {
		if (null!=event && event.getEventType().equals(SchedulingEventType.SCHEDULE_CLIENT_PROCESSED)){
			productIdProcessor.processInputNumber(null);
	}

	}
	public void resetQuantity(){
		AbstractProductInputPane  clientPane =  getView().getInputPane();
		getModel().getUpcQuantity(clientPane);
	}
	public String getProcessPointId(){
		return ClientMainFx.getInstance().currentApplicationId;
				
	}
	
	/**
	 * This method is used to create Virtual Keyboard PopUp
	 */
	protected void createKeyBoardPopup() {
		Scene primaryScene = this.getView().getMainWindow().getScene();
		primaryScene.getStylesheets().add(this.getClass().getResource(ClientConstants.KEYBOARD_CSS_PATH).toExternalForm());
		String fontUrl = this.getClass().getResource(ClientConstants.KEYBOARD_FONT_URL).toExternalForm();
		Font.loadFont(fontUrl, -1);
		keyboardPopup = KeyBoardPopupBuilder.create().initLayout("numblock").initScale(1.6).initLocale(Locale.ENGLISH).addIRobot(RobotFactory.createFXRobot()).build();
		keyboardPopup.setX(Screen.getPrimary().getVisualBounds().getWidth()/2);
		keyboardPopup.setY(Screen.getPrimary().getVisualBounds().getHeight());
		keyboardPopup.getKeyBoard().setOnKeyboardCloseButton(new EventHandler<Event>() {
			public void handle(Event event) {
				CommonUtil.setPopupVisible(false, null,keyboardPopup);
			}
		});
		keyboardPopup.setOwner(primaryScene);
	}
	
	/**
	 * This method is used to show and hide Virutal Keyboard
	 */
	public void setKeyboardPopUpVisible(boolean isVisible) {
		CommonUtil.setPopupVisible(isVisible, (Node)getView().getMainWindow(), keyboardPopup);
	}
	
	/**
	 * This method is used to check weather Keyboard is visible or not
	 */
	public boolean isKeyboardPopVisible() {
		return keyboardPopup.isVisible();
	}
	
	/**
	 * This method is used to Subscribe Keyboard Events
	 */
	@Subscribe()
	public void onKeyboardEvent(KeyboardEvent event) {
		if (event == null) {
			return;
		} else if(event.getEventType().equals(KeyboardEventType.HIDE_KEYBOARD)) {
			setKeyboardPopUpVisible(false);
		} else if(event.getEventType().equals(KeyboardEventType.SHOW_KEYBOARD)) {
			setKeyboardPopUpVisible(true);
		}
	}

	/*	  
	 * 	NAQICS-1415 Do not update GDP if the product passed current station an hour ago
	 */
	public void updateGdpDefects() {
		String[] vqGdpProcessPointIds = PropertyService.getPropertyBean(QiPropertyBean.class).getVqGdpProcessPointId();
		List<String> ids = Arrays.asList(vqGdpProcessPointIds);
		int oneHour = 60 * 60 * 1000;  // One hour in milliseconds
		if(!ids.isEmpty() && ids.contains(getProcessPointId()) && QiInspectionUtils.isGdpProcessPoint(getProcessPointId())) {
			Timestamp initialVqGdpTimestamp = ServiceFactory.getDao(ProductResultDao.class).getInitialProcessTimestamp(getModel().getProductId(), ids);
			if(initialVqGdpTimestamp == null || (new Timestamp(System.currentTimeMillis() - oneHour).after(initialVqGdpTimestamp))) {
				return;
			}
		}
		getModel().updateGdpDefects();
	}

	public void cancelWithPrompt() {	

		BaseProduct product = getModel().getProduct();
		getLogger().check(String.format("Cancel Processing product: %s ", product));
		getModel().setSkipped(true);
		getModel().setLastProductSkipped(true);
		EventBusUtil.publish(new ProductCancelledEvent(getModel()));
		toIdle();
		if (getModel().getProperty().isSendDataCollectionComplete()) {
			getDeviceListener().sendDataCollectionInComplete();
		}
	}
	
	public ProductInputPane getProductInputPane() {
		return productInputPane;
	}

	public void setProductInputPane(ProductInputPane productInputPane) {
		this.productInputPane = productInputPane;
	}
	
	public AbstractProductIdlePane getFirstIdlePane()  {
		AbstractProductIdlePane idlePane = null;
		ProductIdlePaneContainer idleContainer = getView().getProductIdlePaneContainer();
		if(idleContainer != null && idleContainer.getProductIdlePanes() != null)  {
			List<AbstractProductIdlePane> idlePaneList = idleContainer.getProductIdlePanes();
			if(idlePaneList != null && !idlePaneList.isEmpty())  {
				idlePane = idlePaneList.get(0);
			}
		}
		return idlePane;

	}
	
	@Override
	public void execute(String processPointId, String productId) {
		StringBuilder msg = new StringBuilder("Received notification:").append(processPointId).append(" - ")
				            .append(productId).append(Delimiter.DOT);
		
		if(isShowProductSequenceTab()) {//we may lost Vin after cancel if don't add to the queue
			ProductSequenceId sequenceId = new ProductSequenceId(productId, getStationId());
			ProductSequence sequence = new ProductSequence(sequenceId);
			sequence.setReferenceTimestamp(new Timestamp(System.currentTimeMillis()));
			getProductSequenceDao().save(sequence);
			getLogger().info(msg.toString(), "added Product Sequence:", sequence.toString());
			return;
		}
		
		if(this.state == State.IDLE) {
			ProductEvent productEvt = new ProductEvent(productId, ProductEventType.PRODUCT_INPUT_RECIEVED);
			EventBusUtil.publish(productEvt);
			getLogger().info(msg.toString()," Product was sent to processe.");
		} else {
			getLogger().info(msg.toString(), " Client is not in Idle state and notification was ingnored.");
		}
		
		
	}

	private String getStationId() {
		if(stationId == null)
			stationId = PropertyService.getProperty(getProcessPointId(), QiConstant.RFID_STATION_PROP_KEY,getProcessPointId());

		return stationId;
	}

	private ProductSequenceDao getProductSequenceDao() {
		if(productSequenceDao == null)
			productSequenceDao = getDao(ProductSequenceDao.class);
		return productSequenceDao;
	}


	public boolean isShowProductSequenceTab()  {
		boolean isProductSequenceTab = PropertyService.getPropertyBoolean(getProcessPointId(), "SHOW_PRODUCT_SEQUENCE_TAB", false);
		boolean isProductSequence = PropertyService.getPropertyBoolean(getProcessPointId(), "IS_PRODUCT_SEQUENCE_STATION", false);
		return (isProductSequence && isProductSequenceTab);
	}
	
	public int getInputPaneHeight() {
		return this.inputPaneHeight;
	}

	public void setInputPaneHeight(int inputPaneHeight) {
		this.inputPaneHeight = inputPaneHeight;
	}
}
