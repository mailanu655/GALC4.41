package com.honda.galc.client.teamleader.qi.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.ImageMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ImageMaintenancePanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImageMaintenanceController </code> is the Controller class for Image Maintenance.
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
public class ImageMaintenanceController extends AbstractQiController<ImageMaintenanceModel, ImageMaintenancePanel> implements EventHandler<ActionEvent> {

	private QiImage qiImage;
	private QiImageSectionDto dto;
	private File sourceFile;
	private byte[] imageData;

	public ImageMaintenanceController(ImageMaintenanceModel model,
			ImageMaintenancePanel view) {
		 super(model, view);
				
	}

	public void handle(ActionEvent actionEvent) {

		if (actionEvent.getSource().equals(getView().getBtnImageFileName())) 
			imageFileNameBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getView().getBtnCreate())) 
			createBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getView().getBtnUpdate())) 
			updateBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getView().getBtnReplaceImage())) 
			replaceImageBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getView().getBtnInactivate())) 
		{
			if(isImageInUse()){
				updateImageStatus(actionEvent, false);
			}
		}
		else if (actionEvent.getSource().equals(getView().getBtnReactivate())){
			updateImageStatus(actionEvent,true);
		}
		else if (actionEvent.getSource().equals(getView().getBtnReset())) {
			resetBtnAction(actionEvent);
		}else if (actionEvent.getSource() instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) actionEvent.getSource();
			if(StringUtils.trim("Show Grid").equals(checkBox.getText())) 
				showGridAction(actionEvent);
		}else if(actionEvent.getSource() instanceof LoggedRadioButton){
			getView().reload(getView().getFilterTextData());
			clearData();
			getView().disableButtons();
		}else if(actionEvent.getSource() instanceof UpperCaseFieldBean)
			getView().reload(getView().getFilterTextData());
		
	}

	/**
	 * This method gets call when user clicks on Image File Name button.
	 * 
	 * @param event
	 */
	private void imageFileNameBtnAction(ActionEvent event) {
		/** Browse the Image and load the file object */
		browseImage();
		if (sourceFile != null) {
			try {
				BufferedImage bufferedImage = ImageIO.read(sourceFile);
				/** Get Image Dimensions */
				int imageWidth = bufferedImage.getWidth();
				int imageHeight = bufferedImage.getHeight();
				if (imageWidth == QiConstant.IMAGE_DIMENSION && imageHeight == QiConstant.IMAGE_DIMENSION) {
					String imageFileName;
					imageFileName = sourceFile.getName();
					getView().getImageFileNameTextField().setText(imageFileName.toUpperCase());
					getView().getImage().setVisible(true);
					getView().getBtnCreate().setDisable(false);
					/** set Image to ImageView after browse */
					setImage();
					if(getView().getImageTablePane().getTable().getSelectionModel().getSelectedItem()!=null){
						getView().getImageNameTextField().clear();
						getView().getImageDescriptionTextField().clear();
					}
					clearDisplayMessage();
					getView().getImageTablePane().getTable().getSelectionModel().clearSelection();
					getView().getBtnInactivate().setDisable(true);
					getView().getBtnReactivate().setDisable(true);
					getView().getBtnUpdate().setDisable(true);
					getView().getBtnReplaceImage().setDisable(true);
				} else {
					displayErrorMessage("Please upload an Image with  " +QiConstant.IMAGE_DIMENSION+ "*"+QiConstant.IMAGE_DIMENSION+ " resolution");
				}
			} catch (IOException e) {
				handleException("An error occured in imageFileNameAction method ","Failed to load image ", e);
			}
		}
	}

	/**
	 * This method is used to browse the Image
	 */
	private void browseImage() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg");
		fileChooser.getExtensionFilters().add(extFilter);
		sourceFile = fileChooser.showOpenDialog(null);
	}

	/**
	 * This method is used to set the Image after browse
	 * 
	 * @param FileNotFoundException
	 *            if no file is found
	 * @param IOException
	 *            for interrupted I/O Operations
	 */
	private void setImage() {
		Image image = new Image(sourceFile.toURI().toString());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(sourceFile));
			long length = sourceFile.length();
			imageData = new byte[(int) length];
			in.read(imageData);
			getView().getImage().setImage(image);
		} catch (FileNotFoundException e1) {
			handleException("An error occured to upload the image.","Failed to upload the image.", e1);
		} catch (IOException e) {
			handleException("An error occured to upload the image.","Failed to upload the image.", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				handleException("An error occured to process the image.","Failed to process image", e);
			}
		}
	}


	/**This method is used to create an image
	 * @param event
	 */
	private void createBtnAction(ActionEvent event) {
		/** check mandatory fields for Image name and Image File Name */
		if (QiCommonUtil.isMandatoryFieldEmpty(getView().getImageNameTextField()))
			displayErrorMessage("Please enter "+ getView().getImageNameLabel().getText() + "");
		else if (QiCommonUtil.isMandatoryFieldEmpty(getView().getImageFileNameTextField()))
			displayErrorMessage("Please enter "+ getView().getBtnImageFileName().getText() + "");
		else{
			try {
				if (getModel().isImageFileNameExists(StringUtils.trim(getView().getImageFileNameTextField().getText()))) {
					displayErrorMessage("Failed to add new image as the image file name "+ StringUtils.trim(getView().getImageFileNameTextField().getText())+ " already exists!");
				} else if (getModel().isImageNameExists(StringUtils.trim(getView().getImageNameTextField().getText()))) {
					displayErrorMessage("Failed to add new image as the image name "+ StringUtils.trim(getView().getImageNameTextField().getText())+ " already exists!");
				} else {
					qiImage =new QiImage();

					qiImage.setImageName(QiCommonUtil.delMultipleSpaces(getView().getImageNameTextField()).trim());
					qiImage.setImageDescription(QiCommonUtil.delMultipleSpaces(getView().getImageDescriptionTextField()).trim());
					qiImage.setBitmapFileName(QiCommonUtil.delMultipleSpaces(getView().getImageFileNameTextField()).trim());
					qiImage.setActive(true);
					qiImage.setCreateUser(getUserId());
					qiImage.setProductKind(getModel().getProductKind());
					qiImage.setImageData(imageData);
					qiImage.setUpdateUser(StringUtils.EMPTY);
					getModel().createImage(qiImage);
					getView().onTabSelected();
					EventBusUtil.publish(new StatusMessageEvent("Image created successfully.", StatusMessageEventType.INFO));
				}

			} catch (Exception e) {
				handleException("An error occured in createImageAction method ","Failed to create an Image ", e);
			}
		}
	}


	/**This method is used to update an image
	 * @param event
	 */
	private void updateBtnAction(ActionEvent event) {
		/** check mandatory fields for Image name */
		if (QiCommonUtil.isMandatoryFieldEmpty(getView().getImageNameTextField()))
			displayErrorMessage("Please enter "+ getView().getImageNameLabel().getText() + "");
		else
			try {
				if (qiImage.getImageName().equalsIgnoreCase(getView().getImageNameTextField().getText())&& qiImage.getImageDescription().equalsIgnoreCase(getView().getImageDescriptionTextField().getText()))
					displayErrorMessage("No change to update the Image.");
				else if (getModel().isImageNameExists(StringUtils.trim(getView().getImageNameTextField().getText()))&& qiImage.getImageDescription().equalsIgnoreCase(getView().getImageDescriptionTextField().getText()))
					displayErrorMessage("Failed to update the image as the image name "+ StringUtils.trim(getView().getImageNameTextField().getText())+ " already exists!");
				else if (!qiImage.getImageDescription().equalsIgnoreCase(getView().getImageDescriptionTextField().getText())) {
					if (getModel().isImageNameExists(StringUtils.trim(getView().getImageNameTextField().getText()))&& !qiImage.getImageName().equalsIgnoreCase(getView().getImageNameTextField().getText()))
						displayErrorMessage("Failed to update the image as the image name "+ StringUtils.trim(getView().getImageNameTextField().getText())+ " already exists!");
					else {
						/** check mandatory field Reason for Change */
						ReasonForChangeDialog dialog=new ReasonForChangeDialog(getApplicationId());
						if(dialog.showReasonForChangeDialog(null)){
							if (isUpdated(qiImage)) {
								return;
							}
							setImageObject(dialog.getReasonForChangeText());
							getView().onTabSelected();
							EventBusUtil.publish(new StatusMessageEvent("Image details updated successfully.", StatusMessageEventType.INFO));
						}else{
							return;
						}
					}
				} else {
					/** check mandatory field Reason for Change */
					ReasonForChangeDialog dialog=new ReasonForChangeDialog(getApplicationId());
					if(dialog.showReasonForChangeDialog(null)){
						if (isUpdated(qiImage)) {
							return;
						}						
						setImageObject(dialog.getReasonForChangeText());
						getView().onTabSelected();
						EventBusUtil.publish(new StatusMessageEvent("Image details updated successfully.", StatusMessageEventType.INFO));
					}else{
						return;
					}
				}
			} catch (Exception e) {
				handleException("An error occured in updateImageAction method ","Failed to update an Image.", e);
			}
	}

	/**
	 * This method gets call when user has to updated set Image object.
	 */
	private void setImageObject(String reasonforchange) {
		String oldImageName=null;
		String newImageName=null;
		if(!qiImage.getImageName().equalsIgnoreCase(getView().getImageNameTextField().getText())){
			oldImageName = qiImage.getImageName().trim();
			newImageName = QiCommonUtil.delMultipleSpaces(getView().getImageNameTextField()).trim();
		}
		QiImage qiImageCloned = (QiImage)qiImage.deepCopy();
		
		qiImage.setImageName(QiCommonUtil.delMultipleSpaces(getView().getImageNameTextField()).trim());
		qiImage.setImageDescription(QiCommonUtil.delMultipleSpaces(getView().getImageDescriptionTextField()).trim());
		qiImage.setUpdateUser(getUserId());
			
		getModel().updateImage(qiImage.getImageName(),qiImage.getImageDescription(),getUserId(),qiImage.getBitmapFileName());
		if(oldImageName != null && !oldImageName.isEmpty() && newImageName!=null){
			getModel().updateImageNameForEntryScreen(newImageName, getUserId(), oldImageName);
			getModel().updateImageNameForImageSection(newImageName, getUserId(), oldImageName);
		}
		
		//-----Start Audit log
		//call Auditlog utility to capture old and new data	
		qiImageCloned.setImageData(null);
		qiImage.setImageData(null);
		AuditLoggerUtil.logAuditInfo(qiImageCloned, qiImage, reasonforchange,  getView().getScreenName(),getUserId());
		qiImage.setImageData(imageData);
		//-----End of Audit Log
		clearDisplayMessage();
	}


	/**
	 * This method gets call when user clicks on Replace Image button.
	 * 
	 * @param event
	 */
	private void replaceImageBtnAction(ActionEvent event) {
		clearDisplayMessage();
		/** check mandatory field Reason for Change */
		List<String> imageSectionIdList=getModel().findAllImageSectionsByImageName(qiImage.getImageName().trim());
		if(imageSectionIdList.size() == 0){
			replaceImage();
		}
		else{
			if (MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), "The image being replaced is associated with "+imageSectionIdList.size()+" image section(s). Do you still want to continue?")){
				replaceImage();
			}
		}
	}

	/**
	 * 
	 */
	private void replaceImage() {
		ReasonForChangeDialog dialog=new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(null)){
			/** Browse the Image */
			browseImage();
			if (sourceFile != null) {
				try {
					BufferedImage bufferedImage = ImageIO.read(sourceFile);
					int imageWidth = bufferedImage.getWidth();
					int imageHeight = bufferedImage.getHeight();
					String imageFileName = sourceFile.getName();
					if (imageWidth == QiConstant.IMAGE_DIMENSION && imageHeight == QiConstant.IMAGE_DIMENSION) {
						if (qiImage.getBitmapFileName().equalsIgnoreCase(imageFileName)) {
							/** set Image to ImageView after browse */
							setImage();
							qiImage.setImageData(imageData);
							qiImage.setUpdateUser(getUserId());
							if (isUpdated(qiImage)) {
								return;
							}
							getModel().updateImage(imageData, getUserId(), qiImage.getBitmapFileName());
							QiImage qiImageCloned = (QiImage)qiImage.deepCopy();
							qiImage.setImageData("image data".getBytes());
							qiImageCloned.setImageData("IMAGE DATA".getBytes());
							//-----Start Audit log
							//call Auditlog utility to capture old and new data		 				
							   AuditLoggerUtil.logAuditInfo(qiImageCloned, qiImage, dialog.getReasonForChangeTextArea().getText(),  getView().getScreenName(),getUserId());
							//-----End of Audit Log
							qiImage.setImageData(imageData);
							getView().onTabSelected();
							EventBusUtil.publish(new StatusMessageEvent("Image replaced successfully.", StatusMessageEventType.INFO));
						} else {
							displayErrorMessage("Please upload an Image with same Image File Name.");
						}
					}else {
						displayErrorMessage("Please upload an Image with " +QiConstant.IMAGE_DIMENSION+ "*"+QiConstant.IMAGE_DIMENSION+ " resolution");
					}
				} catch (IOException e) {
					handleException("An error occured in replaceImageAction method","Failed to replace Image.", e);
				}
			}
		}
		else{
			return;
		}
	}


	/**
	 * This method is to inactivate/reactivate Image
	 * @param actionEvent
	 * @param status
	 */
	private void updateImageStatus(ActionEvent actionEvent, boolean isActive){
		clearDisplayMessage();
		try{
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
			if(dialog.showReasonForChangeDialog(null))
			{
				try{
					if (isUpdated(qiImage)) {
						return;
					}
					
					getModel().updateImageStatus(qiImage.getBitmapFileName(),isActive? (short) 1:(short) 0);
					
					QiImage qiImageCloned = (QiImage)qiImage.deepCopy();
					//-----Start Audit log
					// set Acitive status true or false in setActive
						qiImage.setActive(isActive);
					//call Auditlog utility to capture old and new data
						AuditLoggerUtil.logAuditInfo(qiImageCloned, qiImage, dialog.getReasonForChangeText(),  getView().getScreenName(),getUserId());
					//-----End of Audit Log
					
					getView().onTabSelected();
				}
				catch (Exception e) {
					handleException("An error occured in activate/inactivate method ", "Failed to activate/inactivate image.", e);
				}
			}
			else
				return;
		}catch (Exception e) {
			handleException("An error occured in updateImageStatus method ", "Failed to update image status", e);
			}
	}

	/** This method returns true if image is not in use by any local or regional screen.
	 * 
	 * @return
	 */
	private boolean isImageInUse() {
		final String imageName = qiImage.getImageName().trim();
		if(getModel().findAllImageSectionsByImageName(imageName).size() > 0 ) {
			MessageDialog.showError(getView().getStage(),"Inactivate is not allowed as image is associated with "+ getUsageMessage() +".");
			return false;
		}
		else{
			String returnValue=isLocalSiteImpacted(imageName);
			if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
				return false;
			}
			else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
				displayErrorMessage("Inactivate is not allowed as local site(s) impacted.")	;
				return false;
			}
		}
			

		return true;
	}
	
	/** This method return the error message stating at how many places entry screen is being in use. 
	 * 
	 * @return
	 */
	private String getUsageMessage() {
		StringBuilder msg = new StringBuilder();
		final String imageName = qiImage.getImageName().trim();
		long imageSectionCount = getModel().findAllImageSectionsByImageName(imageName).size();
		
		if(imageSectionCount > 0) 
			msg.append(msg.length() > 0 ? ", "+ imageSectionCount +" image section(s)" : imageSectionCount +" image section(s)");
		
		return msg.toString();
	}

	/**
	 * This method gets call when user clicks on Show Grid checkbox.
	 * @param event
	 */
	private void showGridAction(ActionEvent event){
		clearDisplayMessage();
		try {
			if (getView().getChkBox().isSelected()) {
				drawGridLines();
				getView().getAnchorImage().getChildren().addAll(getView().getGridLines());
			} else
				getView().getAnchorImage().getChildren().removeAll(getView().getGridLines());
		} catch (Exception e) {
			handleException("An error occurred while selecting checkbox ","Failed to select checkbox ", e);
		}
	}
	/**
	 * This method is used to reset page
	 * 
	 * @param event
	 */
	private void resetBtnAction(ActionEvent event) {
		clearData();
		/** initial load of screen */
		getView().onTabSelected();
		getView().getImageFilterTextField().clear();
		clearDisplayMessage();
		getView().reload();
	}

	/**
	 * This method is used to clear data
	 */
	private void clearData() {
		getView().getImageTablePane().getTable().getSelectionModel().clearSelection();
		getView().getImageNameTextField().clear();
		getView().getImageDescriptionTextField().clear();
		getView().getImageFileNameTextField().clear();
		getView().getImage().setVisible(false);
		getView().getAnchorImage().getChildren().removeAll(getView().getGridLines());
		getView().getChkBox().setSelected(false);
	}

	@Override
	public void addContextMenuItems() {
	}

	@Override
	public void initEventHandlers() {
		addImageListener();
		getView().getImageNameTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(20));
		getView().getImageDescriptionTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(30));
		setTextFieldListener(getView().getImageNameTextField());
		setTextFieldListener(getView().getImageDescriptionTextField());
		setTextFieldListener(getView().getImageFileNameTextField());
		addFieldListener(getView().getImageNameTextField());
		addFieldListener(getView().getImageDescriptionTextField());
	}



	/**
	 * This method is for Image table listener
	 */
	private void addImageListener() {

		getView().getImageTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiImageSectionDto>() {
			public void changed(ObservableValue<? extends QiImageSectionDto> arg0,QiImageSectionDto arg1, QiImageSectionDto arg2) {
				if (getView().getImageTablePane().getTable().getSelectionModel().getSelectedItem() != null) {
					try {
						clearDisplayMessage();
						dto = getView().getImageTablePane().getSelectedItem();
						setImageData();
						getView().getImage().setVisible(true);
						getView().getBtnCreate().setDisable(true);
						if (isFullAccess()) {
							getView().getBtnUpdate().setDisable(false);
							getView().getBtnReplaceImage().setDisable(false);
							boolean isActive = getView().getImageTablePane().getTable().getSelectionModel().getSelectedItem().getActive()==1?true:false;
							getView().getBtnInactivate().disableProperty().setValue(!isActive);
							getView().getBtnReactivate().disableProperty().setValue(isActive);
						} else {
							getView().getBtnUpdate().setDisable(true);
							getView().getBtnReplaceImage().setDisable(true);
							getView().getBtnInactivate().disableProperty().setValue(true);
							getView().getBtnReactivate().disableProperty().setValue(true);
						}
					} catch (Exception e) {
						handleException("An error occurred while selecting image ","Failed to select image ", e);
					}
				}
			}
		});
	}

	/**
	 * This method is used to load Image along with Image Name,Description,Image
	 * File Name
	 * 
	 * @param IOException
	 *  for interrupted I/O Operations
	 */
	private void setImageData() {
		qiImage=getModel().findQiImageByImageName(dto.getImageName());
		getView().getImageNameTextField().setText(qiImage.getImageName());
		getView().getImageDescriptionTextField().setText(qiImage.getImageDescription());
		getView().getImageFileNameTextField().setText(qiImage.getBitmapFileName());
		Image image = new Image(new ByteArrayInputStream(qiImage.getImageData()));
		getView().getImage().setImage(image);
	}

	/**
	 * This method is used to add vertical and horizontal lines for the grid
	 */
	private void drawGridLines() {
		double gridFactorHeight = getView().getImage().getFitHeight()/ (QiConstant.NUMBER_OF_GRIDLINES);
		double gridFactorWidth = getView().getImage().getFitWidth()/ (QiConstant.NUMBER_OF_GRIDLINES);
		for (int i = 0; i <= QiConstant.NUMBER_OF_GRIDLINES; i++) {
			getView().getGridLines().add(new Line(i * gridFactorWidth, 0, i * gridFactorWidth,getView().getImage().getFitHeight()));
			getView().getGridLines().add(new Line(0, i * gridFactorHeight,getView().getImage().getFitWidth(), i* gridFactorHeight));
		}
	}
	
	private String isLocalSiteImpacted(String imageName) {
		List<String> imageNameList=new ArrayList<String>();
		imageNameList.add(imageName);
		return isLocalSiteImpacted(imageNameList,getView().getStage());
		
	}
	
	protected boolean isUpdated(AuditEntry entity) {
		if (getModel().isUpdated(entity)) {
			String logMsg = entity.getClass().getSimpleName() + "(" + entity.getId() + ")-" + QiConstant.CONCURRENT_UPDATE_MSG_TMPL;
			String userMsg = QiConstant.CONCURRENT_UPDATE_MSG;
			displayErrorMessage(logMsg, userMsg);
			return true;
		}
		return false;
	}
}