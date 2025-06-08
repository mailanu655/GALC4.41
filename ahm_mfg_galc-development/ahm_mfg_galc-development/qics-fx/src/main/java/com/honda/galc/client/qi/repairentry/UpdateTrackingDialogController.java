package com.honda.galc.client.qi.repairentry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.qi.base.AbstractQiDialogController;
import com.honda.galc.client.qi.homescreen.BulkProductHomeScreenController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.StatusMessagePane;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * <h3>UpdateTrackingDialogController Class description</h3>
 * <p> UpdateTrackingDialogController description </p>
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
 * @author Vivek Bettada<br>
 * Oct 1, 2021
 *
 *
 */

public class UpdateTrackingDialogController extends AbstractQiDialogController<RepairEntryModel, UpdateTrackingDialog> {
	
	
	private BulkProductHomeScreenController parentController;
	private String productType = "";
	List<Line> lineList;
	RepairEntryModel repairModel = null;
	List<ProductSearchResult> productList;
	volatile int numProcessed = 0;
	ExecutorService myExecutor = Executors.newSingleThreadExecutor();
	private volatile boolean isClose = false;
	private volatile boolean isRunning = false;
	private volatile boolean isDone = false;

	public UpdateTrackingDialogController(RepairEntryModel model, UpdateTrackingDialog updateTrackingDialog, List<ProductSearchResult> selectedList,List<Line> newLines, String pType,
			BulkProductHomeScreenController prevController) {
		super();
		setModel(model);
		parentController = prevController;
		lineList = newLines;
		productList = selectedList;
		productType = pType;
		setDialog(updateTrackingDialog);
	}
 
	public ObservableValue<? extends String> getProductListSize()  {
		StringProperty s = new SimpleStringProperty(String.valueOf(productList.size()));
		return s;
	}
	
	public ObservableValue<? extends String> getNumProcessed()  {
		StringProperty s = new SimpleStringProperty(String.valueOf(numProcessed));
		return s;
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton button = (LoggedButton) actionEvent.getSource();
			if (com.honda.galc.client.utils.QiConstant.UPDATE.equals(button.getText()) || com.honda.galc.client.utils.QiConstant.ACCEPT.equals(button.getText())) {
				updateTracking();
			} else {
				cancelBtnAction(actionEvent);
			}
		} 
	}

	@Override
	public void initListeners() {
		getDialog().getLineComboBox().valueProperty().addListener(lineChangeListener);
		getDialog().getProcessPointPanel().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProcessPoint>() {
			public void changed(ObservableValue<? extends ProcessPoint> observable, ProcessPoint oldValue, ProcessPoint newValue) {
				if(getDialog().getProcessPointPanel().getTable().getSelectionModel().getSelectedIndex() >= 0
					|| getDialog().getProcessPointPanel().getTable().getSelectionModel().getSelectedItem() != null)  {
					disableAndResetAll(false);
				}
			}
		});
	}
	
	private final ChangeListener<? super Line> lineChangeListener = new ChangeListener<Line>() {
		public void changed(ObservableValue ov, Line t, Line newValue) {
			if(null!=getDialog().getLineComboBox().getItems()){
				populateProcessPointPanel();
			}
		}
	};

	/**
	 * populates Entry Model combobox
	 */
	public void populateProcessPointPanel() {
		getDialog().getUpdateBtn().setDisable(true);
		getDialog().getProcessPointPanel().setDisable(false);
		getDialog().getProcessPointPanel().getTable().getItems().clear();
		Line newLine = getDialog().getLineComboBox().getSelectionModel().getSelectedItem();
		getDialog().getProcessPointPanel().getTable().getItems().addAll(FXCollections.observableArrayList(
				RepairEntryModel.findAllTrackingPointsByLineAndProductType(productType, newLine.getLineId())));
	}
	
	/** This method will disable and reset components.
	 *  
	 */
	public void disableAndResetAll(boolean isDisable) {
		clearDisplayMessage();
		getDialog().getProcessPointPanel().setDisable(isDisable);
		getDialog().getUpdateBtn().setDisable(isDisable);
	}
	
	private void updateTracking()  {
		
		ProcessPoint selectedProcPt = getDialog().getProcessPointPanel().getSelectedItem();
		Line selectedLine = getDialog().getLineComboBox().getSelectionModel().getSelectedItem();
		List<String> productIds = new ArrayList<String>();
		String comment = getDialog().getCommentArea().getText();
		if(StringUtils.isBlank(comment))  {
			getDialog().setErrorMessage("Please enter reason for change");
			return;
		}

		@Table(name="UPDATE_TRACKING")
		class UpdateTrackingAudit  {
			UpdateTrackingAudit(String pId, String lId, String ppId)  {
				productId = pId;
				lineId = lId;
				processPointId = ppId;
			}
			@Id
			String productId = "";
			@Auditable @Column(name="LINE_ID")
			private String lineId = "";
			@Auditable @Column(name="PROCESS_POINT_ID")
			private String processPointId = "";
		}
		
		StringBuilder sb1 = new StringBuilder();
		sb1.append(String.format("Confirm update : %d product(s) to Line: %s, Process point : %s", getProductList().size(), selectedLine.getLineId(),selectedProcPt.getProcessPointId()));
		if (!MessageDialog.confirm(getDialog(), sb1.toString())) {
		   	return;
		}

		long t1 = System.currentTimeMillis();
		Runnable r = () -> {
			try {
				getDialog().getUpdateBtn().setDisable(true);
				setRunning(true);
				getDialog().getCancelBtn().setDisable(true);
				for(ProductSearchResult myProduct : getProductList())  {
					productIds.add(myProduct.getProduct().getProductId());
					if(!isDone)  {
						ServiceFactory.getService(TrackingService.class).track(myProduct.getProduct(), selectedProcPt.getProcessPointId());
						UpdateTrackingAudit audit1 = new UpdateTrackingAudit(myProduct.getProduct().getProductId(),
										myProduct.getTrackingStatusName(), myProduct.getSearchProcessPointId());
						UpdateTrackingAudit audit2 = new UpdateTrackingAudit(myProduct.getProduct().getProductId(),
								selectedProcPt.getLineId(), selectedProcPt.getProcessPointId());
						StringBuilder sb = new StringBuilder();
						sb.append("User updated tracking status, ")
						.append("RFC: ").append(comment.substring(0, comment.length() <= 256 ? comment.length() : 256));
						AuditLoggerUtil.logAuditInfo(
								audit1,
								audit2,
								sb.toString(),
								"NAQICS Update Tracking",
								myProduct.getProduct().getProductId(),
								getUserId());
						getLogger().info(sb.toString());
						String message = String.format("Processed %d", ++numProcessed);
			  		   	Platform.runLater(() -> getDialog().getStatusMessagePane().setMessageBig(message, Color.LIGHTYELLOW, Color.DARKGREEN)
			  		   			);	
					}
				}
				if(!productIds.isEmpty()) {
					long tSec = (System.currentTimeMillis() - t1)/1000;					
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("Updated tracking status for %d products", numProcessed));
					sb.append(String.format("\nTotal time: %d min, %d sec: COMPLETED", tSec/60, tSec%60));
					StatusMessageEvent evStatus = new StatusMessageEvent(sb.toString(),StatusMessageEventType.DIALOG_INFO);
					evStatus.addArgument(StatusMessagePane.Size, StatusMessagePane.Big);
					EventBusUtil.publish(evStatus);
					ProductEvent pEv = new ProductEvent(productIds, ProductEventType.PRODUCT_UPDATE_TRACKING);
					pEv.addArgument(ObservableListChangeEventType.CLEAR, true);
					EventBusUtil.publish(pEv);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				getLogger().error(ex);
				getDialog().setErrorMessage("Failed to update" + ex.getMessage());
				return;
			}
			finally  {
				setDone(true);
				setRunning(false);
				numProcessed = 0;
				getDialog().getCancelBtn().setDisable(false);
			}
		};
		try  {
			setClose(false);
			getDialog().getUpdateBtn().setDisable(true);
			myExecutor.submit(r);
		}
		catch(Exception ex)  {
			getLogger().error(ex);
			getDialog().getCancelBtn().setDisable(false);
		}
			
	}

	/**
	 * When user clicks on close button in the popup screen closeBtnAction method gets called.
	 */
	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}
	
	public RepairEntryModel getRepairModel() {
		return repairModel;
	}


	public void setRepairModel(RepairEntryModel repairModel) {
		this.repairModel = repairModel;
	}


	public List<ProductSearchResult> getProductList() {
		return productList;
	}


	public void setProductList(List<ProductSearchResult> productList) {
		this.productList = productList;
	}


	public BulkProductHomeScreenController getParentController() {
		return parentController;
	}


	public void setParentController(BulkProductHomeScreenController parentController) {
		this.parentController = parentController;
	}


	public String getProductType() {
		return productType;
	}


	public void setProductType(String productType) {
		this.productType = productType;
	}

	public void setNumProcessed(int numProcessed) {
		this.numProcessed = numProcessed;
	}

	public boolean isClose() {
		return isClose;
	}

	public void setClose(boolean isClose) {
		this.isClose = isClose;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public boolean onClose()  {
		if(!isRunning())  return true;
		else  {
			StringBuilder sb = new StringBuilder();
			sb.append("If you cancel, the update will be partially complete.  Are you sure you wish to cancel?");
    		if (!MessageDialog.confirm(getDialog(), sb.toString())) {
    		   	return false;
    		}
    		else  {
    			setDone(true);
    			setClose(true);
    			return true;
    		}
		}
	}

}
