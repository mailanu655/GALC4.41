package com.honda.galc.client.datacollection.view;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.IViewObserver;
import com.honda.galc.client.datacollection.property.DefaultViewProperty;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.datacollection.view.action.CreateEmptyCarrierButtonAction;
import com.honda.galc.client.datacollection.view.action.ProductIdInputAction;
import com.honda.galc.client.datacollection.view.action.ResetAFSequenceButtonAction;
import com.honda.galc.client.datacollection.view.action.SwitchAFOnModeAction;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.DestinationType;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.net.ConnectionStatusListener;
import com.honda.galc.net.ServiceMonitor;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

public class AFOnViewManager extends ViewManagerBase
implements IViewObserver, ConnectionStatusListener {

	protected AFOnView view;
	protected JPanel viewPanel;
	protected DefaultViewProperty viewProperty;
	private Sequence nextSeq;
	private String argument = "initialize";
	private boolean initialize= true;
	
	public AFOnViewManager(ClientContext context) {
		super(context);
		init();
	}

	private void init() {
		try {
			
			createDataCollectionPanel();
			getViewPanel();
			
			initConnections();
			refreshScreen();			
			ServiceMonitor.getInstance().registerHttpServiceListener(this);
			
			//Exceptions may exist before client start, for example disable gun failed 
			if(getCurrentState().getErrorList().size() > 0){
				setErrorMessage(getCurrentState().getErrorList().get(0).getMessage().getDescription());
			}else{
				
			}
			
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to init ViewManager.");
		}
	}

	
	private AFOnView createDataCollectionPanel() {
		if(view == null){
			view =  new AFOnView(getViewProperty());
		}
		return view;
	}

	public DataCollectionPanelBase getView() {
		return view;
	}
	
	protected DefaultViewProperty getViewProperty() {
		if(viewProperty == null)
			viewProperty = PropertyService.getPropertyBean(DefaultViewProperty.class, context.getProcessPointId());
		return viewProperty;
	 }

	protected JPanel getViewPanel() {
		if(viewPanel == null){
			viewPanel = new JPanel();
			viewPanel.setName("ViewContentPanel");
			viewPanel.setLayout(new BorderLayout());
			viewPanel.add(view, BorderLayout.CENTER);
						
			messageArea = getMessagePanel();
			
		}
		return viewPanel;
		
	}
	protected void initConnections() throws Exception {
		
		super.initConnections();
		view.getTextFieldProdId().setAction(new ProductIdInputAction(context, "ProductIdInput"));
		view.getSwitchModeButton().setAction(new SwitchAFOnModeAction(context,"SwitchMode"));
		view.getResetSequenceButton().setAction(new ResetAFSequenceButtonAction(context,"ResetSequence"));
		view.getCreateEmptyButton().setAction(new CreateEmptyCarrierButtonAction(context,"CreateEmpty"));
		renderOpModeLabel(getOpMode());
		
		
	}
	
	public DataCollectionState getCurrentState(){
		return DataCollectionController.getInstance().getState();
	}

	@Override
	public void update(Observable o, Object arg) {

		showErrorMessage(arg);
		
		super.update(o, arg);
	}

	protected void showErrorMessage(Object arg) {
		Message errorMsg = getErrorMessage((DataCollectionState)arg);
		if(errorMsg != null && !errorMsg.isEmpty()){
			setErrorMessage(errorMsg);
			if(errorMsg.getType().equals(MessageType.ERROR) || errorMsg.getType().equals(MessageType.EMERGENCY)) 
				playNGSound();
		}
	}

	@Override
	public void setProductInputFocused() {
	
		
	}

	@Override
	public void initProductId(ProcessProduct state) {
		
	}

	@Override
	public void receivedProductId(ProcessProduct state) {
		view.getTextFieldProdId().setText(state.getProductId());
	}

	@Override
	public void productIdOk(ProcessProduct state) {
	
		renderFieldBeanOk(view.getTextFieldProdId(), state.getProductId());
		renderExpPidOrProdSpec(view.getProdSpecLabel(), state.getProduct().getProductSpec());
	
		clearMessageArea(state);
		
	}

	protected void refreshScreen() {
	
		view.getLabelExpPIDOrProdSpec().setVisible(false);	
		view.getTextFieldExpPidOrProdSpec().setVisible(false);
		ViewControlUtil.refreshObject(view.getLabelAfOnSeqNum(), view.getAfOnSeqNumLabel());
		ViewControlUtil.refreshObject(view.getLabelAfOnSeqNumValue(),	"",	ViewControlUtil.VIEW_COLOR_INPUT,false);
		nextSeq = getSequenceDao().getNextSequence(getSequenceName());
		String seqNumber = nextSeq!= null?String.valueOf(nextSeq.getCurrentSeq()):"00000";
		renderSeqNo(view.getAfOnSeqNumLabel(),seqNumber);
		if(isInAuto()){
			renderFieldBeanInit(view.getTextFieldProdId(), true);
			view.getTextFieldProdId().setEditable(false);
			view.getTextFieldProdId().setEnabled(false);
		}							
		else{
			renderFieldBeanInit(view.getTextFieldProdId(), true);
			view.getTextFieldProdId().setEditable(true);
			view.getTextFieldProdId().setEnabled(true);
		}
		renderOpModeLabel(getOpMode());
		messageArea.setErrorMessageArea(null);
		if(getController().getProperty().isShowUpcomingProducts()) {
			view.getUpcomingSequenceTableModel().refresh(getUpcomingSequenceVins());
		}
		if(getController().getProperty().isProcessEmptyEnabled()){
			if(!isInAuto()){
				view.getCreateEmptyButton().setEnabled(true);
			}
			else {
				view.getCreateEmptyButton().setEnabled(false);
			}
		}
		List<AssemblyOnModel> previouslyScannedVins = getPreviouslyScannedVins();
		view.getPreviouslyScannedTableModel().refresh(previouslyScannedVins);
		if(previouslyScannedVins.size() > 0){
			view.getPreviouslyScannedTableModel().getTable().setRowSelectionAllowed(true);
			view.getPreviouslyScannedTableModel().getTable().setRowSelectionInterval(0, 0);
		}
		
		if(isInAuto() && initialize) {requestTrigger();initialize=false;}
	}
	
	@Override
	public void productIdNg(ProcessProduct state) {
		renderFieldBeanNg(view.getTextFieldProdId(), state.getProductId());
		view.getTextFieldProdId().setEditable(true);
		if(isInAuto()) 	broadcast(view.getTextFieldProdId().getText(), 0,1);
	}

	@Override
	public void completeProductId(ProcessRefresh state) {
		if(isInAuto()) 	broadcast(view.getTextFieldProdId().getText(), 1,0);
		refreshScreen(viewManagerProperty.getScreenRefreshingDelay());
		notifyFinishProduct();
	}

	@Override
	public void initPartSn(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partSnOk(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partSnNg(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receivedPartSn(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completePartSerialNumber(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTorque(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void torqueOk(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void torqueNg(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completeCollectTorques(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initRefreshDelay(ProcessRefresh state) {
			
	}

	@Override
	public void skipPart(DataCollectionState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skipCurrentInput(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skipProduct(DataCollectionState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshScreen(DataCollectionState state) {
		refreshScreen(0);
		notifyFinishProduct();
	}

	@Override
	public void notifyError(DataCollectionState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void message(DataCollectionState state) {
		// TODO Auto-generated method stub
		
	}

	public void refreshScreen(int refreshingDelay) {
		try
		{
					
			if(refreshingDelay > 0)
				refreshScreenWithDelay(refreshingDelay);
			else
				refreshScreen();

		}
		catch (Exception e)
		{
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::refreshScreen() exception.");
		}

	}
	
	private void refreshScreenWithDelay(final int screenRefreshingDelay) {
		try {
			for (int i = screenRefreshingDelay; i > 0; i--) {
				if(!messageArea.isError())
					messageArea.setErrorMessageArea("Refreshing Screen in " + i + " seconds.");
				
				Thread.sleep(1000);
			}

			refreshScreen();
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + "::refreshScreenWithDelay() exception.");
		}

	}

	protected void renderFieldBeanNg(UpperCaseFieldBean bean, String text) {
		bean.setEnabled(true);
		bean.setText(" ");
		bean.setColor(ViewControlUtil.VIEW_COLOR_NG);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_NG);
		bean.setSelectionStart(0);
		bean.setSelectionEnd(bean.getText().length());
		bean.requestFocus();
	}
	
	protected void renderFieldBeanInit(UpperCaseFieldBean bean, boolean requestFocus) {
		bean.setColor(ViewControlUtil.VIEW_COLOR_CURRENT);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_CURRENT);
		bean.setDisabledTextColor(ViewControlUtil.VIEW_COLOR_FONT);
		bean.setForeground(ViewControlUtil.VIEW_COLOR_INPUT);
		bean.setText("");
		bean.setEnabled(true);
		bean.setEditable(true);
		bean.setVisible(true);
		
		if(requestFocus) bean.requestFocus();
	}
	
	protected void renderFieldBeanOk(UpperCaseFieldBean bean, String serialNumber) {
		bean.setText(serialNumber);
		bean.setColor(ViewControlUtil.VIEW_COLOR_OK);
		bean.setBackground(ViewControlUtil.VIEW_COLOR_OK);
		bean.setDisabledTextColor(ViewControlUtil.VIEW_COLOR_FONT);
		bean.setForeground(ViewControlUtil.VIEW_COLOR_FONT);
		bean.setEditable(false);
		bean.setEnabled(true);
	}
	
	protected void renderExpPidOrProdSpec(String label, String text) {
		view.getLabelExpPIDOrProdSpec().setText(label);
		view.getLabelExpPIDOrProdSpec().setVisible(true);
		view.getTextFieldExpPidOrProdSpec().setText(text);
		view.getTextFieldExpPidOrProdSpec().setVisible(true);
		view.getLabelExpPIDOrProdSpec().repaint();
	}
		
	protected void renderSeqNo(String label, String text) {
		view.getLabelAfOnSeqNum().setText(label);
		view.getLabelAfOnSeqNumValue().setText(text!= null?StringUtil.padLeft(text,5,'0'):"N/A");
		view.getLabelAfOnSeqNum().setVisible(true);
		view.getLabelAfOnSeqNumValue().setVisible(true);
	}
	
	protected void renderOpModeLabel(String text) {
		view.getOperationModeLabel().setText(text);
		if(text.equalsIgnoreCase(OperationMode.AUTO_MODE.getName())){
			view.getOperationModeLabel().setForeground(ViewControlUtil.VIEW_COLOR_CURRENT);
		}else{
			view.getOperationModeLabel().setForeground(ViewControlUtil.VIEW_COLOR_NG);
		}
		
	}
	private String getOpMode() {
		ComponentStatus componentStatus = ServiceFactory.getDao(ComponentStatusDao.class).findByKey(context.getProcessPointId(), "OPERATION_MODE");
		if(componentStatus != null){
			String operationMode = componentStatus.getStatusValue();
			return operationMode;
		}
		return "N/A";
	}
	
	private ArrayList<AssemblyOnModel> getUpcomingSequenceVins(){
		
		Integer lastProcessedSeq = (getSequenceDao().findByKey(getSequenceName())).getCurrentSeq();
		
		List<ProcessProductDto> ppsList = ServiceFactory.getDao(ProductSequenceDao.class).findAllUpcoming(getProductCheckPropertyBean().getLastPassingProcessPoint(),lastProcessedSeq,context.getProperty().getProcessChangeDisplayRows());
		
		ArrayList<AssemblyOnModel> modelList= new ArrayList<AssemblyOnModel>();
		
		for(ProcessProductDto dto: ppsList){
			AssemblyOnModel model = new AssemblyOnModel();
		
			model.setSequence(String.valueOf(dto.getSequence()));
			model.setMtoci(dto.getMtoci());
			model.setKdLot(dto.getKdLot());
			modelList.add(model);
		}
		
		return modelList;
	}
	
	private ArrayList<AssemblyOnModel> getPreviouslyScannedVins(){
		List<ProcessProductDto> results = getAllProcessedProducts(getController().getProperty().isProcessEmptyEnabled());
		ArrayList<AssemblyOnModel> modelList= new ArrayList<AssemblyOnModel>();
		
		for(ProcessProductDto dto: results){
			
				AssemblyOnModel model = new AssemblyOnModel();
				
				model.setVin(dto.getVin());
				model.setMtoci(dto.getMtoci());
				model.setSequence(String.valueOf(dto.getSequence()));
				model.setKdLot(dto.getKdLot());
				
				modelList.add(model);
		}
		return modelList;
	}
	
	private List<ProcessProductDto> getAllProcessedProducts(boolean handleEmptyCarriers){
		if(handleEmptyCarriers) {
			return ServiceFactory.getDao(ProductCarrierDao.class).findAllProcessedProductsForProcessPoint(context.getProcessPointId(),context.getProperty().getProcessChangeDisplayRows());
		}
		return ServiceFactory.getDao(FrameDao.class).findAllProcessedProductsForProcessPoint(context.getProcessPointId(),context.getProperty().getProcessChangeDisplayRows());
	}
	
	private boolean isInAuto() {
		return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(getOpMode());
	}
	
	protected void requestTrigger() {
		List<BroadcastDestination > broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(context.getProcessPointId());
		for (BroadcastDestination broadcastDestination : broadcastDestinations) {
			if (!broadcastDestination.getDestinationType().equals(DestinationType.DEVICE_WISE)) continue;
			if(broadcastDestination.getArgument().equalsIgnoreCase(argument)){
				DataContainer dataContainer = new DefaultDataContainer();
				getService(BroadcastService.class).broadcast(context.getProcessPointId(), broadcastDestination.getSequenceNumber(), dataContainer);
			}
		}
	}
	
	protected void broadcast(String productId, Integer unitRelease, Integer error) {
		List<BroadcastDestination > broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(context.getProcessPointId());
		for (BroadcastDestination broadcastDestination : broadcastDestinations) {
			if (!broadcastDestination.getDestinationType().equals(DestinationType.DEVICE_WISE)) continue;
			if(broadcastDestination.getArgument().equalsIgnoreCase("release")){
				DataContainer dataContainer = new DefaultDataContainer();
				dataContainer.put(DataContainerTag.UNIT_RELEASE,unitRelease);
				dataContainer.put(DataContainerTag.IS_ERROR, error);
				getService(BroadcastService.class).broadcast(context.getProcessPointId(), broadcastDestination.getSequenceNumber(), dataContainer);
			}
		}
	}
	
	public void logErrorMessage(String message) {
		Logger.getLogger().error(message);
		getController().getFsm().error(new Message(message));
	}

	public void logException(Exception ex) {
		ex.printStackTrace();
		ProductBean productBean = new ProductBean();
		productBean.setProductId(view.getTextFieldProdId().getText());
		String msg = StringUtils.trimToEmpty(ex.getMessage());
		getController().getFsm().productIdNg(productBean, "PRODUCT", msg);
		Logger.getLogger().error(msg);
	}
	
	public DataCollectionController getController() {
		return DataCollectionController.getInstance(context.getAppContext().getApplicationId());
	}
	
	private String getSequenceName(){
		return getController().getProperty().getSequenceName();
	}

	@Override
	public JPanel getClientPanel() {
		return view;
	}
	
	private SequenceDao getSequenceDao(){
		return ServiceFactory.getDao(SequenceDao.class);
	}
	
	private ProductCheckPropertyBean getProductCheckPropertyBean() {
		return PropertyService.getPropertyBean(ProductCheckPropertyBean.class, context.getProcessPointId());
	}

	public void partSnOkButWait(ProcessPart state) {}

	public void receivedBypass(ProcessPart state) {}

	public void receivedAuto(ProcessPart state) {}

}
