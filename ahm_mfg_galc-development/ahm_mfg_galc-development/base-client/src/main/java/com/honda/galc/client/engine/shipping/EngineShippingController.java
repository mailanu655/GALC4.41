package com.honda.galc.client.engine.shipping;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.EngineShippingQuorum;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.notification.service.IEngineShippingNotification;
import com.honda.global.galc.common.data.DataContainer;
import com.honda.global.galc.common.data.DataContainerTag;
import com.honda.global.galc.common.data.LegacyDataContainerListener;
import com.honda.global.galc.system.orb.Request;

/**
 * 
 * 
 * <h3>MCShippingController Class description</h3>
 * <p> MCShippingController description </p>
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
 * @author Jeffray Huang<br>
 * Sep 10, 2014
 *
 *
 */
public class EngineShippingController extends AbstractController<EngineShippingModel, EngineShippingView> 
	implements ActionListener, ListSelectionListener,LegacyDataContainerListener,IEngineShippingNotification{

	
	public EngineShippingController(EngineShippingModel model, EngineShippingView view) {
		super(model, view);
		AnnotationProcessor.process(this);
		registerDataContainerListener();
		registerEiDeviceListener();
	}

	private void registerDataContainerListener() {
		if(model.getPropertyBean().isSupportLegacyRequest()) {
			view.getMainWindow().getApplicationContext().
				getRequestDispatcher().registerListner(this);
		}
	}
	
	private void registerEiDeviceListener() {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if(eiDevice != null && eiDevice.isEnabled()){
			DeviceDataConverter.getInstance().registerOutputDeviceData(getDeviceOutputDataList());
		}
	}
	
	private List<IDeviceData> getDeviceOutputDataList() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new EngineShippingQuorum());
		return list;
	}

	public void activate() {
		getView().mapActions();
	}

	public void actionPerformed(ActionEvent e) {
		clearMessage();
		try{
			if(isEvent(e,view.assignButton)) assignButtonClicked();
			else if(isEvent(e,view.deassignButton)) deassignButtonClicked();
			else if(isEvent(e,view.changeTrailerButton)) changeTrailerButtonClicked();
			else if(isEvent(e,view.manualLoadButton)) manualLoadButtonClicked();
			else if(isEvent(e,view.completeTrailerButton)) completeTrailerButtonClicked();
			else if(isEvent(e,view.printButton)) printButtonClicked();
			else if(isEvent(e,view.loadQuorumButton)) loadQuorumButtonClicked();
			else if(isEvent(e,view.resizeQuorumButton)) resizeQuorumButtonClicked();
			else if(isEvent(e,view.repairQuorumButton)) repairQuorumButtonClicked();
			else if(isEvent(e,view.createQuorumButton)) createQuorumButtonClicked();
			else if(isEvent(e,view.completeQuorumButton)) completeQuorumButtonClicked();
			else if(isEvent(e,view.toDelayButton)) toDelayButtonClicked();
			else if(isEvent(e,view.fromDelayBUtton)) fromDelayButtonClicked();
			else if(isEvent(e,view.refreshScheduleButton)) refreshScheduleButtonClicked();
		}catch(Exception ex) {
			showAndLogErrorMessage("Exception occured : " + ex.getMessage());
		}
		view.setButtonStates();
	}

	private void assignButtonClicked() {
		if(!view.quorumListPane.getItems().isEmpty() && view.quorumListPane.getItems().get(0).getStatus().equals(ShippingQuorumStatus.LOADING)) {
			showErrorMessage("When the quorum is in LOADING status, it is not allowed to assign a trailer");
			return;
		}
		
		AssignTrailerDialog assignDialog = new AssignTrailerDialog(this,view.vanningScheduleListPane.getItems());
		assignDialog.setVisible(true);
		List<ShippingVanningSchedule> schedules = assignDialog.getSelectedVanningSchedules();
		if(schedules != null && !schedules.isEmpty()){
			model.assignTrailer(assignDialog.getSelectedTrailerNumber(), schedules,assignDialog.getSplitSchedule());
			view.reload();
			showAndLogInfo("Trailer " + assignDialog.getSelectedTrailerNumber() + " is assigned");
		}
	}
	
	private void deassignButtonClicked() {
		if(view.quorumListPane.getItems().get(0).getStatus().equals(ShippingQuorumStatus.LOADING)) {
			showErrorMessage("When the quorum is in LOADING status, it is not allowed to de-assign a trailer");
			return;
		}
		
		if(view.trailerListPane.getTable().getSelectedRow() != view.trailerListPane.getItems().size() -1){
			showErrorMessage("Only last trailer is allowed to be deassigned ! \n Please select last trailer.");
			return;
		};
		ShippingTrailerInfo trailerInfo = view.trailerListPane.getSelectedItem();
		if(trailerInfo == null) return;
		if(trailerInfo.getStatus()!=ShippingTrailerInfoStatus.WAITING){
			showErrorMessage("The selected trailer is not an empty trailer!");
			return;
		}
		if(!MessageDialog.confirm(view.getMainWindow(), "Are you sure to deassign the trailer " + trailerInfo.getTrailerId())) return;
		if(model.deassignTrailer(trailerInfo.getTrailerId())){
			view.reload();
			showInfo("trailer " + trailerInfo.getTrailerId() + " is deassigned successfully");
		}
	}

	private void changeTrailerButtonClicked() {
		ShippingTrailerInfo trailerInfo = view.trailerListPane.getSelectedItem();
		if(trailerInfo == null) return;
		String selectedTrailerNumber = (String)JOptionPane.showInputDialog(
                view.getMainWindow(),
                "Please select a trailer to be assigned",
                "Change Trailer", JOptionPane.PLAIN_MESSAGE,
                null,
                model.findAvailableTrailerNumbers(),
                "");
		if(selectedTrailerNumber != null){
			model.changeTrailerNumber(trailerInfo.getTrailerId(), selectedTrailerNumber);
			showAndLogInfo("Trailer " + trailerInfo + " is changed to trailer number " +  selectedTrailerNumber);
			model.reloadShippingTrailerInfoList();
			view.trailerListPane.reloadData(model.getAllShippingTrailers());
			model.reloadActiveQuorums();
			view.quorumListPane.reloadData(model.findAllScheduledQuorums());
			view.delayedQuorumListPane.reloadData(model.findAllDelayedQuorums());
		}
			
	}

	private void manualLoadButtonClicked() {
		ManualLoadDialog dialog = new ManualLoadDialog(this);
		dialog.setVisible(true);
		if(!dialog.isOk()) return;
		view.reload();
	}

	private void completeTrailerButtonClicked() {
		ShippingTrailerInfo trailerInfo = view.trailerListPane.getSelectedItem();
		if(trailerInfo == null)  {
			showErrorMessage("Please select trailer to complete.");
			return;
		}
		
		if (trailerInfo.getStatus() == ShippingTrailerInfoStatus.LOADING ||
				trailerInfo.getStatus() == ShippingTrailerInfoStatus.WAITING){
			showErrorMessage("You can not complete a trailer with WAITING or LOADING status.");
			return;
		}
		if(trailerInfo.getTrailerNumber().startsWith("N/A")){
			showErrorMessage("You can not complete the dummy trailer, please change it to a real trailer first.");
			return;
		}
		
		List<ShippingQuorumDetail> details = model.checkEngineModels(trailerInfo.getTrailerId());
		if(!details.isEmpty()) {
			ShippingQuorumDetail detail = details.get(0);
			showErrorMessage("Engine " + detail.getEngineNumber() + "'s  MTOC " + detail.getKdLot() +  
					" does not match required MTOC : " + detail.getYmto() + ". Trailer: " + trailerInfo.getTrailerNumber());
			return;
		}
		
		String message = model.performProductCheck(trailerInfo);
		if(!StringUtils.isEmpty(message)){
			
		}
		
		if(trailerInfo.getActQty() < trailerInfo.getSchQty()){
			boolean flag = MessageDialog.confirm(view.getMainWindow(), 
					"Trailer " + trailerInfo.getTrailerNumber() + " is not fully loaded. Are you sure to complete the trailer?");
			if(!flag) return;
			if(model.getPropertyBean().isOverrideEnabled()){
				if(!login()) return;
			}
		}
		
		view.setWaitCursor();
		
		try{
			model.completeTrailer(trailerInfo);
			view.reload();
			showInfo("Completed trailer " + trailerInfo.getTrailerNumber() + " successfully");
		}catch(Exception e){
			getLogger().error(e, "Exception to complete trailer" + trailerInfo.getTrailerId());
		}finally{
			view.restoreDefaultCursor();
		}
	}
	
	private void printButtonClicked() {
		ShippingTrailerInfo trailerInfo = view.trailerListPane.getSelectedItem();
		if(trailerInfo == null)  {
			showErrorMessage("Please select trailer to print");
			return;
		}
		
		List<ShippingVanningSchedule> schedules = 
			model.findVanningSchedules(view.vanningScheduleListPane.getItems(), trailerInfo.getTrailerId());

		model.printVanningScheduleSheet(trailerInfo,schedules);
		showInfo("sent vanning schedule sheet data successfully");
	}

	private void loadQuorumButtonClicked() {
		ShippingQuorum quorum = view.quorumListPane.getSelectedItem();
		if(quorum == null || !quorum.getStatus().equals(ShippingQuorumStatus.ALLOCATED)) return;
		if(!MessageDialog.confirm(view.getMainWindow(), "Are you sure to start to load the allocated quorum?"))
			return;
		
		EngineShippingQuorum engineQuorum = new EngineShippingQuorum();
		engineQuorum.setPalletType(quorum.getPalletType());
		engineQuorum.setQuorumSize(quorum.getQuorumSize());
		engineQuorum.setRowNumber(quorum.getTrailerRow());
		String trailerNumber = quorum.getTrailerNumber();
		engineQuorum.setTrailerNumber(
		    StringUtils.substring(trailerNumber, trailerNumber.length()> 6 ? trailerNumber.length()-6 : 0));
		engineQuorum.setQuorumType(quorum.getQuorumType());
		
		    try {
			DeviceManager.getInstance().getEiDevice().send(engineQuorum);
			getLogger().info("sent quorum data " + quorum + " to PLC successfully");
		}catch (Exception ex) {
			showAndLogErrorMessage("Could not send Shipping Quorum to PLC due to " + ex.getMessage() );
			return;
		}
		
		model.syncVanningSchedules();
		
		quorum.setStatus(ShippingQuorumStatus.LOADING);
		model.updateQuorumStatus(quorum, ShippingQuorumStatus.LOADING);
		getView().quorumListPane.refresh();
		showAndLogInfo("loading the current Quorum  " + quorum);
	}
	
	protected boolean login() {
		if(LoginDialog.login() != LoginStatus.OK) return false;

		if (!ClientMain.getInstance().getAccessControlManager().isAuthorized(model.getPropertyBean().getAuthorizationGroup())) {
			JOptionPane.showMessageDialog(null, "You have no access permission to execute this action.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void resizeQuorumButtonClicked() {
		ShippingQuorum quorum = view.quorumListPane.getSelectedItem();
		if(quorum == null) return;
		
		if (quorum.isLoading()){
			int option = JOptionPane.showConfirmDialog(getView().getMainWindow(), 
					"Are you sure to resize the quorum that is in the LOADING status?",
					"Resize Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ;
			if (option != 0) {        
				//user choose "no", then do nothing.
				return;
			}
		}
		
		Integer[] quorumSizes = new Integer[quorum.getActualQuorumSize()];
		for(int i = 1;i<=quorum.getActualQuorumSize();i++) quorumSizes[i-1] = i;
		
		
		Integer size = (Integer)JOptionPane.showInputDialog(
                getView().getMainWindow(),
                "Please select the number you want to resize to",
                "Resize", JOptionPane.PLAIN_MESSAGE,
                null,
                quorumSizes,
                5);
		if(size == null || size == quorum.getQuorumSize()) return;
		
		String resizeMessage = "Are you sure to resize the quorum size to:"+size+"?";
		if(size==0){
			resizeMessage = "Are you sure to resize the quorum size to 0?, if you choose YES, the quorum will be finished without loading any engine!";
		}
		int option = JOptionPane.showConfirmDialog(getView().getMainWindow(), 
				resizeMessage,
				"Resize Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ;
		if (option != 0) {        
			//user choose "no", then do nothing.
			return;
		}
		
		if (quorum.isLoading()){
			if(size < quorum.getLoadedCount()){
				showErrorMessage("You can not select the number less than "+quorum.getLoadedCount()+" that we have loaded.");
				return;
			}
		}
		
		int oldSize = quorum.getQuorumSize();
		getModel().resizeQuorum(quorum, size);
		getView().quorumListPane.refresh();
		getView().quorumDetailListPane.reloadData(model.getActualShippingQuorumDetails(quorum));
		getLogger().info("Resized Quorum : " + quorum + " size from " + oldSize + " to " + size);
		showAndLogInfo("Resized Quorum size from " + oldSize + " to " + size);

	}

	private void repairQuorumButtonClicked() {
		RepairQuorumDialog dialog = new RepairQuorumDialog(this);
		ShippingQuorum quorum = dialog.getShippingQuorum();
		dialog.dispose();
		if(quorum == null)return;
		
		ShippingQuorum beforeQuorum = view.quorumListPane.getSelectedItem();
		quorum = model.createRepairQuorum(beforeQuorum, quorum.getQuorumSize(), quorum.getPalletType());
		model.reloadActiveQuorums();
		view.quorumListPane.reloadData(model.findAllScheduledQuorums());
		showAndLogInfo("created repair quorum " + quorum);
	}

	private void createQuorumButtonClicked() {
		ExceptionalQuorumDialog dialog = new ExceptionalQuorumDialog(this);
		ShippingQuorum quorum = dialog.getShippingQuorum();
		dialog.dispose();
		if(quorum == null) return;
		ShippingQuorum beforeQuorum = view.quorumListPane.getSelectedItem();
		model.createExceptionalQuorum(beforeQuorum, quorum);
		model.reloadActiveQuorums();
		view.quorumListPane.reloadData(model.findAllScheduledQuorums());
		showAndLogInfo("created exceptional quorum " + quorum);
	}

	private void completeQuorumButtonClicked() {
		int index = view.quorumListPane.getTable().getSelectedRow();
		
		if(index != 0) showErrorMessage("You can only select the first row to complete");
		else {
			ShippingQuorum quorum = view.quorumListPane.getSelectedItem();
			if(quorum == null) return;
			if(quorum.getStatus() == ShippingQuorumStatus.LOADING){
                // the current quorum is in loading status
                // check TL security
				if(model.getPropertyBean().isOverrideEnabled()){
					if(!login()) return;
				}
            }   
			if(MessageDialog.confirm(view.getMainWindow(),"Are you sure to complete the quorum you selected? if choose YES, the quorum will be completed without fully loaded!")){
				model.completeQuorum(quorum);
				model.reloadActiveQuorums();
				view.trailerListPane.reloadData(model.getAllShippingTrailers());
				view.quorumListPane.reloadData(model.findAllScheduledQuorums());
				view.delayedQuorumListPane.reloadData(model.findAllDelayedQuorums());
				view.quorumListPane.clearSelection();
				view.quorumListPane.getTable().getSelectionModel().setSelectionInterval(index, index);
				
				getLogger().info("Quorum " + quorum + " is short completed");
				showInfo("Quorum is completed successfuly");
			}
		}
			
		
	}

	private void toDelayButtonClicked() {
		ShippingQuorum quorum = view.quorumListPane.getSelectedItem();
		if(quorum == null) return;
		if(quorum.getStatus().equals(ShippingQuorumStatus.LOADING)) 
		     showErrorMessage("You can not choose a quorum with loading status to be delayed.");
		else if(MessageDialog.confirm(view.getMainWindow(), "Are you sure to delay the selected quorum?")){
			
			getLogger().info("Move Quorum to delayed list : " + quorum);
			
			model.updateQuorumStatus(quorum, ShippingQuorumStatus.DELAYED);
			model.reloadActiveQuorums();
			reloadQuorums();
		}
	}

	private void fromDelayButtonClicked() {
		ShippingQuorum afterQuorum = view.quorumListPane.getSelectedItem();
		ShippingQuorum delayedQuorum = view.delayedQuorumListPane.getSelectedItem();
		
		if(delayedQuorum == null) return;
		
		if(MessageDialog.confirm(view.getMainWindow(), "Are you sure to put the delayed quorum back to scheduled list?")){
			if(afterQuorum == null)afterQuorum = model.findLastShippingQuorum();
			getLogger().info("Move Delayed quorum " + delayedQuorum + " to after quroum : " + " scheduled list : " + afterQuorum);
			
			model.releaseDelayedQuorum(delayedQuorum, afterQuorum);
			reloadQuorums();
		}
	}
	
	protected void reloadQuorums() {
		int scheduleQuorumIndex = Math.max(view.quorumListPane.getTable().getSelectedRow(),0);
		int delayedQuorumIndex = Math.max(view.delayedQuorumListPane.getTable().getSelectedRow(),0);
		model.reloadActiveQuorums();
		view.quorumListPane.clearSelection();
		view.delayedQuorumListPane.clearSelection();
		view.quorumListPane.reloadData(model.findAllScheduledQuorums());
		view.delayedQuorumListPane.reloadData(model.findAllDelayedQuorums());
		view.quorumListPane.getTable().getSelectionModel().setSelectionInterval(scheduleQuorumIndex, scheduleQuorumIndex);
		view.delayedQuorumListPane.getTable().getSelectionModel().setSelectionInterval(delayedQuorumIndex, delayedQuorumIndex);
	}

	private void refreshScheduleButtonClicked() {
		model.syncVanningSchedules();
		view.vanningScheduleListPane.reloadData(model.findAllActiveVanningSchedules());
		showAndLogInfo("vanning schedules are refreshed successfully");
	}

	public void valueChanged(ListSelectionEvent e) {
		clearMessage();
		if(isEvent(e,view.trailerListPane.getTable().getSelectionModel()))
				trailerSelected();
		else if(isEvent(e,view.quorumListPane.getTable().getSelectionModel()))
			quorumSelected();
		else if(isEvent(e,view.delayedQuorumListPane.getTable().getSelectionModel()))
			delayedQuorumSelected();
	}

	private void trailerSelected() {
		ShippingTrailerInfo trailerInfo = view.trailerListPane.getSelectedItem();
		if(trailerInfo == null) return;
		
		List<ShippingVanningSchedule> schedules = 
			model.findVanningSchedules(view.vanningScheduleListPane.getItems(), trailerInfo.getTrailerId());
		view.vanningScheduleListPane.setSelectedItems(schedules);
		int rowIndex = view.vanningScheduleListPane.getTable().getSelectedRow();
		view.vanningScheduleListPane.scrollToCenter(rowIndex, 0);
	}

	private void quorumSelected() {
		ShippingQuorum quorum = view.quorumListPane.getSelectedItem();
		view.quorumDetailListPane.reloadData(quorum == null ? null : model.getActualShippingQuorumDetails(quorum)); 
		view.setButtonStates();
	}

	private void delayedQuorumSelected() {
		ShippingQuorum delayedQuorum = view.delayedQuorumListPane.getSelectedItem();
		view.delayedQuorumDetailListPane.reloadData(delayedQuorum == null ? null : delayedQuorum.getShippingQuorumDetails());
		view.setButtonStates();
	}
	
	public void received(DataContainer dc) {
		List<Request> requests = getRequests(dc);
		for(Request request :requests) {
			if(request.getCommand().equals("updateQuorumList") ||
			   request.getCommand().equals("updateQuorumStatus") ||
			   request.getCommand().equals("displayErrorMessageFromServer"))
				try {
					request.invoke(this);
				} catch (Exception e) {
					showErrorMessage("Exception Occured: " + e.getMessage());
				}
		}
	}
	
	@SuppressWarnings("unchecked")
    private List<Request> getRequests(DataContainer dc) {
        
        List<Request> requests = new ArrayList<Request>();
        Object object = dc.get(DataContainerTag.DATA);
        
        if(object instanceof Request) {
            requests.add((Request)object);
        }else if(object instanceof List<?>){
            requests.addAll((List<Request>)object);
        }
        
        return requests;
        
    }
	
	/**
	 * notification from legacy shipping process
	 * @param errorMessage
	 */
	public void displayErrorMessageFromServer(String errorMessage){
		getLogger().info("Received legacy error message " + errorMessage); 
		showErrorMessage(errorMessage);
	}
	/**
	 * notification from legacy shipping process
	 * quorum is allocating or allocated
	 * @param quorum
	 */
	public void updateQuorumStatus(com.honda.global.galc.bl.common.data.shipping.ShippingQuorum quorum){
		getLogger().info("Received legacy quorum " + quorum); 
		view.reload();
	}
	
	/**
	 * notification from legacy shipping process
	 * engine loaded into quorum
	 * @param quorum
	 * @param engineNumber
	 * @param ymto
	 */
	public void updateQuorumList(com.honda.global.galc.bl.common.data.shipping.ShippingQuorum quorum, String engineNumber, String ymto){
		getLogger().info("Received legacy quorum " + quorum + " engine: " + engineNumber + " ymto: " + ymto);
		model.updateOtherTrailerInfo(quorum.getTrailerID());
		view.reload();
	}
	
	/**
	 * notification about last passed product at a process Point
	 * @param processPointId
	 * @param productId
	 */
	@EventTopicSubscriber(topic="IEngineShippingNotification")
	public void onEngineShippingNotificationEvent(String event, com.honda.galc.net.Request request) {
        try {
			request.invoke(this);
		} catch (Exception e) {
			getLogger().error(e, "Could not process shipping notification event " + request);
		}
   }
	
	/**
	 * This is notification from regional shipping tracking client
	 */
	public void engineLoaded(int trailerId, ShippingQuorumDetail quorumDetail) {
		getLogger().info("Received engine loaded : " + quorumDetail);
		view.reload();
	}

	/* 
	 * update quorum status -> ALLOCATING, ALLOCATED
	 * (non-Javadoc)
	 * @see com.honda.galc.notification.service.IEngineShippingNotification#shippingQuorumUpdated(com.honda.galc.entity.product.ShippingQuorum)
	 */
	public void shippingQuorumUpdated(ShippingQuorum quorum) {
		getLogger().info("Received quorum update : " + quorum);
		model.reloadActiveQuorums();
		view.quorumListPane.reloadData(model.findAllScheduledQuorums());
		view.setButtonStates();
	}	
	
}
