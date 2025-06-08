package com.honda.galc.client.qi.ipptagentry;


import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.SUBMIT;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.IPPTagId;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.ServerInfoUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>IPPTagEntryPanelController </code> is the Controller class for IPP Tag Entry
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
 * <TD>L&T Infotech</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */

public class IppTagEntryPanelController extends AbstractQiProcessController<IppTagEntryModel, IppTagEntryPanel> implements EventHandler<ActionEvent>  {

	private static final String UPDATE_IPP_TAG ="Update Tag Number";
	private static final String DELETE_IPP_TAG ="Delete Tag Number";


	public IppTagEntryPanelController(IppTagEntryModel model,IppTagEntryPanel ippTagEntryPanel) {
		super(model, ippTagEntryPanel);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedTextField){
			String ippNumber = StringUtils.trim(getView().getIppTagNumberTextField().getText());
			if(StringUtils.isEmpty(ippNumber)){
				displayErrorMessage("Please enter Ipp Tag Number");
				return;
			} else if (getModel().isIppTagNumberNumeric() && !QiCommonUtil.isNumericInput(ippNumber)) {
				getView().getIppTagNumberTextField().setText(StringUtils.EMPTY);
				displayErrorMessage("Invalid input [" + ippNumber + "]. IPP Tag Number must be numeric.");
				return;
			}
			submitIPP(ippNumber);
			getView().reload();
			getView().getIppTagNumberTextField().setText(StringUtils.EMPTY);
			clearMessage();
		}
		if(actionEvent.getSource() instanceof MenuItem) {
			IPPTag ippTag = getView().getIppTagTablePane().getSelectedItem();
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if(UPDATE_IPP_TAG.equals(menuItem.getText())) updateIPPTagNumber(actionEvent,ippTag);
			else if(DELETE_IPP_TAG.equals(menuItem.getText())) deleteIPPTagNumber(actionEvent);
		}
	}

	/**
	 * This method is to save ipp tag
	 */
	public void submitIPP(String ippTagNo) {

		IPPTag ippTag = new IPPTag();
		IPPTagId id = new IPPTagId();
		id.setIppTagNo(ippTagNo);
		id.setDivisionId(getModel().getDivisionId());
		id.setProductId(getModel().getProductId());
		ippTag.setId(id);
		ippTag.setActualTimestamp(CommonUtil.getTimestampNow());
		ippTag.setProcessPointId(getProcessPointId());
		getModel().submitIPP(ippTag);
	}

	/**
	 * This method is to open Popup dialog for update IPP tag number
	 */
	private void updateIPPTagNumber(ActionEvent actionEvent, IPPTag ippTag){
		try{
			if(ippTag == null)
				return;
			IppTagEntryUpdateDialog ippTagEntryUpdateDialog = new IppTagEntryUpdateDialog(UPDATE_IPP_TAG, ippTag,getView(), getModel(), getApplicationId());
			ippTagEntryUpdateDialog.showDialog();
			getView().reload();
		}catch (Exception e) {
			handleException("An error occurred in updateIPPTagNumber method ", "Failed to open updateIPPTagNumber popup ", e);
		}
	}

	/**
	 * This method is to delete IPP tag number
	 */
	private void deleteIPPTagNumber(ActionEvent actionEvent){
		boolean ippTagNo = false;
		IPPTag ippTag = getView().getIppTagTablePane().getSelectedItem();
		if (ippTag == null) {
			return;
		}
		ippTagNo = MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()),"Are you sure you want to delete IPPTag ?");
		if(!ippTagNo)
			return;
		getModel().deleteIPPTag(ippTag);
		getView().reload();
	}

	@Override
	public void initializeListeners() {

	}

	@Override
	public void initEventHandlers() {
		addIppTagTableListener();
		addTextFieldListener();
	}

	/**
	 * This method is for IPP tag Table Listeners
	 */
	private void addIppTagTableListener() {
		getView().getIppTagTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IPPTag>() {
			public void changed(
					ObservableValue<? extends IPPTag> arg0,
					IPPTag arg1,
					IPPTag arg2) {
				clearMessage();
				addContextMenu();
			}
		});
		getView().getIppTagTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenu();
			}
		});
	}


	/**
	 * This method is for enable or disable contextmenu
	 */
	public void addContextMenu(){
		if(isIppTagEditEnabled())
		{
			IPPTag tag = getView().getIppTagTablePane().getSelectedItem();

			boolean isEnabled = false;
			boolean isSelected = tag != null;
			if (isSelected) {
				boolean isEditable = isIppTagEditable(tag);
				isEnabled = isSelected && isEditable;
			}
			ContextMenu contextMenu = new ContextMenu();
			MenuItem updateMenuItem = createMenuItem(UPDATE_IPP_TAG,false, this);
			MenuItem deleteMenuItem = createMenuItem(DELETE_IPP_TAG,false, this);
			contextMenu.getItems().addAll(updateMenuItem,deleteMenuItem);

			updateMenuItem.setDisable(!isEnabled);
			deleteMenuItem.setDisable(!isEnabled);

			if(getView().getIppTagTablePane().getSelectedItem() != null){

				getView().getIppTagTablePane().getTable().setContextMenu(contextMenu);
			}
			else{
				getView().getIppTagTablePane().getTable().getContextMenu().getItems().clear();
			}
		}
	}


	/**
	 * This method is to create menuitem
	 */
	private MenuItem createMenuItem(String name,boolean enabled, EventHandler<ActionEvent> eventHandler) {
		MenuItem menuItem = new MenuItem(name);
		menuItem.setText(name);
		if (eventHandler != null) {
			menuItem.setOnAction(eventHandler);
		}
		menuItem.setDisable(!enabled);
		return menuItem;
	}


	public boolean isIppTagEditEnabled(){
		return PropertyService.getPropertyBean((QiPropertyBean.class), ApplicationContext.getInstance().getProcessPointId()).isIppTagEditEnabled();
	}

	public String getIPPInfoTaskName(){
		return PropertyService.getPropertyBean((QiPropertyBean.class),ApplicationContext.getInstance().getProcessPointId()).getIppInfoOifTaskName();
	}

	public String getIppInfoOifTaskServerUrl(){
		return PropertyService.getPropertyBean((QiPropertyBean.class),ApplicationContext.getInstance().getProcessPointId()).getIppInfoOifTaskServerUrl();
	}

	/**
	 * This method is to check whether IPP tag is editable or no
	 */
	private boolean isIppTagEditable(IPPTag ippTag) {
		if (ippTag == null) {
			return false;
		}
		String oifIppTaskName = getIPPInfoTaskName();
		if (StringUtils.isBlank(oifIppTaskName)) {
			String msg = "QICS IPP Tag Edit - IPP_INFO_OIF_TASK_NAME property not configured";
			getLogger().warn(msg);
			return false;
		}
		String oifServerUrl = getIppInfoOifTaskServerUrl();
		if (StringUtils.isNotBlank(oifServerUrl)) {
			oifServerUrl = oifServerUrl.trim();
			oifServerUrl = oifServerUrl + "/"  + ServerInfoUtil.SERVICE_HANDLER;
		}

		Timestamp lastExecutionTimestamp = getOifLastProcessingTime(oifServerUrl, oifIppTaskName);
		if (lastExecutionTimestamp == null) {
			String msg = "QICS IPP Tag Edit - OIF IPP Info Task - Last Execution time not set";
			getLogger().warn(msg);
			return false;
		}

		Timestamp tagTs = ippTag.getActualTimestamp();
		if (tagTs == null && ippTag.getCreateTimestamp() != null) {
			tagTs = new Timestamp(ippTag.getCreateTimestamp().getTime());
		}

		String offProcessPointIdsString = getOffProcessPointId(oifServerUrl, oifIppTaskName);
		if (StringUtils.isBlank(offProcessPointIdsString)) {
			return tagTs.after(lastExecutionTimestamp);
		}

		if (tagTs.after(lastExecutionTimestamp)) {
			return true;
		}

		String[] ppIds =  offProcessPointIdsString.split(Delimiter.COMMA);
		getModel().getProductHistory();
		List<ProductHistory> historyList = new ArrayList<ProductHistory>();
		for (String processPointId : ppIds) {
			List<? extends ProductHistory> list = getModel().getAllByProductAndProcessPoint(ippTag.getId().getProductId(), processPointId);
			if (list == null || list.isEmpty()) {
				continue;
			}
			historyList.addAll(list);
		}
		if (historyList.isEmpty()) {
			return true;
		}
		boolean editable = true;
		for (ProductHistory h : historyList) {
			if (editable && h.getActualTimestamp().before(lastExecutionTimestamp)) {
				editable = false;
				break;
			}
		}
		return editable;
	}

	private Timestamp getOifLastProcessingTime(String oifServerUrl, String componentId) {
		String statusKey = ApplicationConstants.LAST_PROCESS_TIMESTAMP;
		if (StringUtils.isBlank(oifServerUrl)) {
			getModel().getComponentStatusDao();
		} else {
			getModel().getHttpServiceProvider(oifServerUrl);
		}
		ComponentStatus status = getModel().getAllByComponentIdAndStatusKey(componentId, statusKey);
		if (status == null || StringUtils.isBlank(status.getStatusValue())) {
			return null;
		}
		Timestamp ts = null;
		try {
			ts = Timestamp.valueOf(status.getStatusValue().trim());
		} catch(Exception e) {
			getLogger().error(e, "Error parsing to Timestamp " + status.getStatusValue());
		}
		return ts;
	}

	private String getOffProcessPointId(String oifServerUrl, String componentId) {
		String offProcessPointId = null;
		if (StringUtils.isBlank(oifServerUrl)) {
			offProcessPointId = PropertyService.getProperty(componentId, ApplicationConstants.PROCESS_POINT_OFF_SHORT);
		} else {
			getModel().getComponentProperty(oifServerUrl);
			ComponentPropertyId id = new ComponentPropertyId(componentId, ApplicationConstants.PROCESS_POINT_OFF_SHORT);
			ComponentProperty property = getModel().findById(id);
			if (property != null) {
				offProcessPointId = property.getPropertyValue();
			}
		}
		return offProcessPointId;
	}

	/**
	 * This method is used to create Product Panel Buttons
	 */
	public ProductActionId[] getProductActionIds(){
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{SUBMIT};
		} else 
			return new ProductActionId[]{CANCEL, SUBMIT};
	}

	/**
	 * This method is textfield listener of Ipp tag entry panel
	 */
	private void addTextFieldListener(){
		getView().getIppTagNumberTextField().textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String arg1, String arg2) {
				clearMessage();
				String ippNumber = getView().getIppTagNumberTextField().getText();
				if(getModel().isIppTagNumberNumeric() && !QiCommonUtil.isNumericInput(ippNumber)){
					displayErrorMessage("Invalid input [" + ippNumber + "]. IPP Tag Number must be numeric.");
				}
			}
		});
	}

}
