package com.honda.galc.client.teamleader.qi.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.ImageToEntryScreenMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ImageToEntryScreenPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class ImageToEntryScreenController extends AbstractQiController<ImageToEntryScreenMaintenanceModel, ImageToEntryScreenPanel> implements EventHandler<ActionEvent> {
	boolean isRefresh=false;
	
	private final ChangeListener<? super String> productTypeChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(!isRefresh){
				clearDisplayMessage();
				getView().getEntryModelCombobox().getItems().clear();
				getView().getEntryScreenDetailsTable().getTable().getItems().clear();
				loadEntryModelComboboxList();
				clear();
			}
		}
	};
	
	private final ChangeListener<? super String> entryModelChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
			if(!isRefresh){
				clearDisplayMessage();
				if(null != newValue && !newValue.isEmpty()){
					getView().getEntryScreenDetailsTable().setData(findAllEntryScreensByEntryModel(StringUtils.trim(newValue)));
					setButtonState(3);
				}
				clear();
			}
		}
	};

	private final ChangeListener<? super QiEntryScreenDto> entryScreenDetailsTableChangeListener = new ChangeListener<QiEntryScreenDto>() {

		public void changed(ObservableValue<? extends QiEntryScreenDto> observable, QiEntryScreenDto oldValue, QiEntryScreenDto newValue) {
if(!isRefresh){
			clearDisplayMessage();
			getView().getImageTablePane().getTable().getSelectionModel().clearSelection();
			QiEntryScreenDto dto = getView().getEntryScreenDetailsTable().getTable().getSelectionModel().getSelectedItem();
			if(dto != null) {
				Logger.getLogger().check("Entry screen : " + dto.getEntryScreen() + " selected");
				if (null != dto.getImageName() && !dto.getImageName().isEmpty()) {
					QiImage image = getModel().findImageByImageName(StringUtils.trimToEmpty(dto.getImageName()));
					getView().getSelectedImageView().setImage(new Image(new ByteArrayInputStream(image.getImageData())));
					getView().getselectedImageNameLabel().setText(StringUtils.trimToEmpty(dto.getImageName()));
					if (isFullAccess()) {
						if(dto.getIsUsedVersion() == (short) 1 && getModel().isVersionCreated(dto.getEntryModel()))
							setButtonState(3);
						else
							setButtonState(1);
					}
						
				} else {
					if (isFullAccess()) {
						if(dto.getIsUsedVersion() == (short) 1 && getModel().isVersionCreated(dto.getEntryModel()))
							setButtonState(3);
						else
							setButtonState(2);
					}
					getView().getSelectedImageView().setImage(null);
					getView().getselectedImageNameLabel().setText(null);
				}
			}
		}
}	};
	
	
	public ImageToEntryScreenController(ImageToEntryScreenMaintenanceModel model, ImageToEntryScreenPanel view) {
		super(model, view);
	}

	/**
	 * This method is event handler for the ImageToEntryScreenPanel.
	 * 
	 * @param actionEvent
	 */
	public void handle(ActionEvent event) {
		if (event.getSource().equals(getView().getAssignBtn()))
			assignBtnAction(event);
		else if (event.getSource().equals(getView().getUpdateBtn()))
			updateBtnAction(event);
		else if (event.getSource().equals(getView().getDeassignBtn()))
			deassignBtnAction(event);
		else if (event.getSource().equals(getView().getRefreshBtn())){
			refreshBtnAction(getView().getImageTablePane().getSelectedItem());
			EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
			
		}
		if (event.getSource() instanceof UpperCaseFieldBean)
			getView().reload(getView().getImageFilterTextField().getText());
	}

	/**
	 * This method is event handler for the Assign Button.
	 * 
	 * @param actionEvent
	 */
	private void assignBtnAction(ActionEvent event) {
		String selectedImageName = getView().getselectedImageNameLabel().getText();
		QiEntryScreenDto entryScreen = getView().getEntryScreenDetailsTable().getTable().getSelectionModel().getSelectedItem();
		if (entryScreen != null && entryScreen.getImageName().isEmpty() && getView().getImageTablePane().getSelectedItem() != null) {
			try {
				if (isUpdated(entryScreen)) { //check update timestamp to see if anyone else updated it
					return;
				}
				QiEntryScreen qiEntryScreen = getModel().findEntryScreenDetails(entryScreen);
				QiEntryScreen qiEntryScreenCloned = (QiEntryScreen) qiEntryScreen.deepCopy();
				qiEntryScreen.setImageName(selectedImageName);
				qiEntryScreen.setUpdateUser(getUserId());
				getModel().updateAssignImage(qiEntryScreen);
				//call to prepare and insert audit data
				AuditLoggerUtil.logAuditInfo(qiEntryScreenCloned, qiEntryScreen, QiConstant.ASSIGN_REASON_FOR_AUDIT, getView().getScreenName(),getUserId());
				getView().getEntryScreenDetailsTable().setData(findAllEntryScreensByEntryModel(getView().getEntryModelCombobox().getSelectionModel().getSelectedItem().toString()));
				entryScreen.setImageName(selectedImageName);
				getView().getEntryScreenDetailsTable().getTable().getSelectionModel().select(entryScreen);

				setButtonState(1);
				getView().getImageTablePane().getTable().getSelectionModel().clearSelection();
				EventBusUtil.publish(new StatusMessageEvent("Image assigned successfully.", StatusMessageEventType.INFO));
			} catch (Exception e) {
				handleException("An error occurred while assigning Image to Entry Screen. ", "Failed to assign image to Entry Screen.", e);
			}
		} else {
			clearMessage();
			if (entryScreen == null) {
				displayErrorMessage("Please select entry screen from the table.");
			} else if (!entryScreen.getImageName().isEmpty()) {
				displayErrorMessage("Entry screen has image assigned. Please deassign the image first.");
			} else if (getView().getImageTablePane().getSelectedItem() == null) {
				displayErrorMessage("Please select image from the table.");
			}
		}
	}

	/**
	 * This method is to refresh the Dropdowns
	 */
	@SuppressWarnings("unused")
	public void refreshBtnAction(QiImageSectionDto selectedImageIndex){
		isRefresh=true;
		
		String model = null;
		String productType=null;
		String filter= StringUtils.trim(getView().getImageFilterTextField().getText());
		QiEntryScreenDto selectedEntryScreen = getView().getEntryScreenDetailsTable().getSelectedItem();

		if(null !=getView().getEntryModelCombobox().getSelectionModel().getSelectedItem()){
			model = getView().getEntryModelCombobox().getSelectionModel().getSelectedItem().toString();
		}
		refreshProductType();
		refreshEntryModel(model,filter,selectedEntryScreen,selectedImageIndex);
		isRefresh=false;
		
	}

	/**
	 * @param model
	 */
	private void refreshEntryModel(String model, String filter, QiEntryScreenDto selectedEntryScreen, QiImageSectionDto selectedImageIndex) {
		String productType;
		if (null != model) {
			productType = getView().getProductTypeCombobox().getValue().toString();
			if (productType != null) {
				getView().getEntryModelCombobox().getItems().clear();
				List<String> entryModelList = getModel().findEntryModelsByProductType(StringUtils.trim(productType));
				if (entryModelList.size() > 0) {
					getView().getEntryModelCombobox().getItems().addAll(entryModelList);
					if(entryModelList.contains(model))
						getView().getEntryModelCombobox().setValue(model);
				}                    }
			if(null != model && !model.isEmpty()){
				getView().getEntryScreenDetailsTable().setData(findAllEntryScreensByEntryModel(StringUtils.trim(model)));
				setButtonState(3);
			}
			getView().getEntryScreenDetailsTable().getTable().getSelectionModel().select(selectedEntryScreen);
			if(null != filter && !filter.isEmpty())
				getView().getImageTablePane().setData(getModel().findImageByFilter(StringUtils.trim(filter)));
			getView().getImageTablePane().getTable().getSelectionModel().select(selectedImageIndex);
			
			QiEntryScreenDto dto = getView().getEntryScreenDetailsTable().getTable().getSelectionModel().getSelectedItem();
			if (null!=dto && null != dto.getImageName() && !dto.getImageName().isEmpty()) {
				if (isFullAccess())
					setButtonState(1);
			} else {
				if (isFullAccess())
					setButtonState(2);
			}
		}
	}

	/**
	 * This method is to refresh the Product Type Dropdown
	 */
	private void refreshProductType() {
		String productType;
		List<String> productTypeList = getModel().findAllProductTypes();
		if (null != getView().getProductTypeCombobox().getSelectionModel().getSelectedItem()) {
			productType = getView().getProductTypeCombobox().getValue().toString();
			getView().getProductTypeCombobox().getItems().clear();
			if (productTypeList.size() > 0) {
				getView().getProductTypeCombobox().getItems().addAll(productTypeList);
				if(productTypeList.contains(productType))
					getView().getProductTypeCombobox().setValue(productType);
			}                   
		}
		else{
			getView().getProductTypeCombobox().getItems().clear();
			if (productTypeList.size() > 0)
				getView().getProductTypeCombobox().getItems().addAll(productTypeList);
		}
	}
	/**
	 * This method is event handler for the Deassign Button.
	 * 
	 * @param actionEvent
	 */
	private void deassignBtnAction(ActionEvent event) {
		clearDisplayMessage();
		QiEntryScreenDto entryScreen = getView().getEntryScreenDetailsTable().getTable().getSelectionModel().getSelectedItem();
		
		if (entryScreen != null) {

			if (!isImageOrScreenInUse(entryScreen.getEntryScreen().trim())) {
				if (!entryScreen.getEntryScreen().isEmpty()) {
					ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
					if (dialog.showReasonForChangeDialog(null)) {
						try {
							if (isUpdated(entryScreen)) {
								return;
							}			
							QiEntryScreen qiEntryScreen = getModel().findEntryScreenDetails(entryScreen);
							QiEntryScreen qiEntryScreenCloned = (QiEntryScreen) qiEntryScreen.deepCopy();
							qiEntryScreen.setImageName(null);
							qiEntryScreen.setUpdateUser(getUserId());
							getModel().updateAssignImage(qiEntryScreen);
							
							// call to prepare and insert audit data
							AuditLoggerUtil.logAuditInfo(qiEntryScreenCloned, qiEntryScreen,
									dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(), getUserId());
							clear();
							getView().getImageTablePane().getTable().getSelectionModel().clearSelection();
							getView().getEntryScreenDetailsTable().setData(findAllEntryScreensByEntryModel(getView()
									.getEntryModelCombobox().getSelectionModel().getSelectedItem().toString()));
							entryScreen.setImageName(null);
							getView().getEntryScreenDetailsTable().getTable().getSelectionModel().select(entryScreen);

							setButtonState(2);
							EventBusUtil.publish(new StatusMessageEvent("Image deassigned successfully.",StatusMessageEventType.INFO));
						} catch (Exception e) {
							handleException("An error occurred while deassigning Image to Entry Screen. ",
									"Failed to deassign image to Entry Screen.", e);
						}
					} else {
						return;
					}
				}
			} else {
				MessageDialog.showError(ClientMainFx.getInstance().getStage(getApplicationId()), "The selected entry screen has "
								+ getModel().getPdcCountByEntryScreenName(entryScreen.getEntryScreen())
								+ " Part defect combination(s) assigned. Hence, deassign operation is not allowed.");
			}
		} else
		displayErrorMessage("Please select entry screen from the table.");
	}

	/**
	 * This method is event handler for the Update Button.
	 * 
	 * @param actionEvent
	 */
	private void updateBtnAction(ActionEvent event) {
		QiImageSectionDto imageSection = getView().getImageTablePane().getSelectedItem();
		QiEntryScreenDto entryScreen = getView().getEntryScreenDetailsTable().getTable().getSelectionModel().getSelectedItem();
		clearMessage();
		if (imageSection != null && entryScreen != null) {
			QiImage image=getModel().findImageByImageName(imageSection.getImageName());
			if (!entryScreen.getImageName().equals(image.getImageName())) {
				ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
				if (dialog.showReasonForChangeDialog(null)) {
					try {
						if (isUpdated(entryScreen)) {
							return;
						}
						QiEntryScreen qiEntryScreen = getModel().findEntryScreenDetails(entryScreen);
						QiEntryScreen qiEntryScreenCloned = (QiEntryScreen) qiEntryScreen.deepCopy();
						qiEntryScreen.setImageName(image.getImageName());
						qiEntryScreen.setUpdateUser(getUserId());
						getModel().updateAssignImage(qiEntryScreen);
						// call to prepare and insert audit data
						AuditLoggerUtil.logAuditInfo(qiEntryScreenCloned, qiEntryScreen, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
						getView().getEntryScreenDetailsTable().setData(findAllEntryScreensByEntryModel(getView().getEntryModelCombobox().getSelectionModel().getSelectedItem().toString()));
						entryScreen.setImageName(image.getImageName());
						getView().getEntryScreenDetailsTable().getTable().getSelectionModel().select(entryScreen);
						
						getView().getImageTablePane().getTable().getSelectionModel().clearSelection();
						EventBusUtil.publish(new StatusMessageEvent("Image updated successfully.", StatusMessageEventType.INFO));
					} catch (Exception e) {
						handleException("An error occurred while updating Image assigned to Entry Screen.", "Failed to update image for Entry Screen. ", e);
					}
				} else {
					return;
				}
			} else {
				displayErrorMessage("The selected image already assigned to the screen.");
			}
		} else {
			displayErrorMessage("Please select image/screen from the table.");
		}
	}

	/** This method returns true if entry model for the selected entry screen is active.
	 * 
	 * @return
	 */
	private boolean isEntryModelActive() {
		QiEntryModelId qiEntryModelId= new QiEntryModelId();
		qiEntryModelId.setEntryModel(StringUtils.trimToEmpty(getView().getEntryScreenDetailsTable().getSelectedItem().getEntryModel()));
		qiEntryModelId.setIsUsed((short)0);
		QiEntryModel  qiEntryModel = getModel().findEntryModelByName(qiEntryModelId);
		if(qiEntryModel.isActive()) 
			return true;
		return false;
	}

	/**
	 * This method is used to initiate event handlers for productTypeCombobox,
	 * entryModelCombobox, imageEntryScreenCombobox, imageTablePane and
	 * imageFilterTextField
	 */
	@Override
	public void initEventHandlers() {
		clearDisplayMessage();
		addProductTypeComboboxListener();
		addEntryModelComboboxListenter();
		addImageTablePaneListener();
		addEntryScreenDetailsTableListener();
	}

	private void addEntryScreenDetailsTableListener() {
		getView().getEntryScreenDetailsTable().getTable().getSelectionModel().selectedItemProperty()
		.addListener(entryScreenDetailsTableChangeListener );
	}

	/**
	 * This method is event listener for imageEntryScreenCombobox
	 */
	private void addEntryModelComboboxListenter() {
		getView().getEntryModelCombobox().valueProperty().addListener(entryModelChangeListener);
	}

	private List<QiEntryScreenDto> findAllEntryScreensByEntryModel(String entryModel) {
		if(null != entryModel){
			List<QiEntryScreenDto> qiEntryScreenDtoList = getModel().findAllEntryScreensByEntryModel(StringUtils.trim(entryModel));
			Map<String, QiEntryScreenDto> qiEntryScreenDtosTempMap = new HashMap<String, QiEntryScreenDto>();
			for (int i = 0; i < qiEntryScreenDtoList.size(); i++) {
				if (qiEntryScreenDtosTempMap.get(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel() +qiEntryScreenDtoList.get(i).getIsUsedVersion()) == null) {
					if (qiEntryScreenDtoList.get(i).getImageName() == null)
						qiEntryScreenDtoList.get(i).setImageName("");
					qiEntryScreenDtosTempMap.put(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel() +qiEntryScreenDtoList.get(i).getIsUsedVersion(), qiEntryScreenDtoList.get(i));
					continue;
				} else {
					QiEntryScreenDto dto = qiEntryScreenDtosTempMap.get(qiEntryScreenDtoList.get(i).getEntryScreen()+qiEntryScreenDtoList.get(i).getEntryModel() +qiEntryScreenDtoList.get(i).getIsUsedVersion());
					dto.setDivisionId(dto.getDivisionId().trim() + ", " + qiEntryScreenDtoList.get(i).getDivisionId().trim());
				}
			}

			ArrayList<QiEntryScreenDto> list = new ArrayList<QiEntryScreenDto>(qiEntryScreenDtosTempMap.values());
			Collections.sort(list);
			return list;
		}
		return null;
	}


	/**
	 * This method is used to load Product Type in productTypeCombobox
	 */
	private void addProductTypeComboboxListener() {
		getView().getProductTypeCombobox().valueProperty().addListener(productTypeChangeListener);

		getView().getProductTypeCombobox().setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				if (getView().getProductTypeCombobox().getValue() == null) {
					try {
						clearDisplayMessage();
						getView().getProductTypeCombobox().getItems().clear();
						List<String> productTypeList = getModel().findAllProductTypes();
						getView().getProductTypeCombobox().getItems().addAll(productTypeList);
					} catch (Exception e) {
						handleException("An error occurred while fetching Product Type list.", "Failed to get Product Type.", e);
					}
				}
			}
		});
	}

	/**
	 * This method is used to load Entry Model list in entryModelCombobox
	 * 
	 */
	private void loadEntryModelComboboxList() {
		String productType = null;
		try {
			productType = getView().getProductTypeCombobox().getValue().toString();
			if (productType != null) {
				List<String> entryModelList = getModel().findEntryModelsByProductType(StringUtils.trim(productType));
				if (entryModelList.size() > 0)
					getView().getEntryModelCombobox().getItems().addAll(entryModelList);
			}
			setButtonState(3);
		} catch (Exception e) {
			handleException("An error occurred while fetching Entry Model list.", "Failed to get Entry Model.", e);
		}
	}

	/**
	 * This method is used to handle change event on imageTable
	 */
	private void addImageTablePaneListener() {

		getView().getImageTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiImageSectionDto>() {
			public void changed(ObservableValue<? extends QiImageSectionDto> observableValue, QiImageSectionDto oldValue, QiImageSectionDto newValue) {
				if(observableValue != null && observableValue.getValue() != null)
					Logger.getLogger().check("Image : " + observableValue.getValue().getImageName() + " selected");	
				displayImage();
			}
		});

		getView().getImageTablePane().getTable().setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				displayImage();
			}
		});
	}

	/**
	 * This method is used to display selected Image and its name
	 */
	private void displayImage() {
		clearDisplayMessage();
		if (getView().getImageTablePane().getSelectedItem() != null) {
			String imageName = getView().getImageTablePane().getSelectedItem().getImageName();
			QiImage qiImage=getModel().findImageByImageName(imageName);
			Image img = null;
			if (qiImage != null) {
				try {
					img = new Image(new ByteArrayInputStream(qiImage.getImageData()));
					getView().getSelectedImageView().setImage(img);
					getView().getselectedImageNameLabel().setText(qiImage.getImageName());
				} catch (Exception e) {
					handleException("An error occurred while loading image.", "Failed to load image. ", e);
				}
			}
		}
	}

	/**
	 * This method is used to clear the fields
	 */
	private void clear() {
		clearDisplayMessage();
		getView().getselectedImageNameLabel().setText("");
		getView().getSelectedImageView().setImage(null);
	}

	/**
	 * This method is used to set button state either enabled or disabled
	 * 
	 * @param buttons
	 *            - 1 to disable assignBtn, 2 to disable updateBtn and
	 *            deassignBtn, 3 to disable assignBtn, updateBtn and deassignBtn
	 * 
	 */
	private void setButtonState(int buttons) {
		switch (buttons) {
		case 1:
			getView().getAssignBtn().setDisable(true);
			getView().getUpdateBtn().setDisable(false);
			getView().getDeassignBtn().setDisable(false);
			break;
		case 2:
			getView().getAssignBtn().setDisable(false);
			getView().getUpdateBtn().setDisable(true);
			getView().getDeassignBtn().setDisable(true);
			break;
		case 3:
			getView().getAssignBtn().setDisable(true);
			getView().getUpdateBtn().setDisable(true);
			getView().getDeassignBtn().setDisable(true);
			break;
		}
	}


	@Override
	public void addContextMenuItems() {

	}

	/**  Check whether the image or entry screen already in use.
	 *
	 * @param entryScreen
	 * @return
	 */
	private boolean isImageOrScreenInUse(String entryScreen) {
		if(getModel().getPdcCountByEntryScreenName(entryScreen) > 0)
			return true;
		return false;
	}
	
	protected boolean isUpdated(QiEntryScreenDto dto) {
		QiEntryScreen entity = new QiEntryScreen(dto.getEntryScreen(), dto.getEntryModel(), dto.getIsUsedVersion());
		if (dto.getUpdateTimestamp() != null) {
			entity.setUpdateTimestamp(dto.getUpdateTimestamp());
		}

		boolean isUpdated = getModel().isUpdated(entity);
		if (isUpdated) {
			String entryScreen = StringUtils.trim(dto.getEntryScreen());
			String entryModel = StringUtils.trim(dto.getEntryModel());
			String logMsg = "EntryScreen(" + entryScreen + "," + entryModel + ")-" + QiConstant.CONCURRENT_UPDATE_MSG_TMPL;
			String userMsg = QiConstant.CONCURRENT_UPDATE_MSG;
			displayErrorMessage(logMsg, userMsg);
		}
		return isUpdated;
	}
}
