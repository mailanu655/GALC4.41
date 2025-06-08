package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Screen;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ImageMaintenanceController;
import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.teamleader.qi.model.ImageMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiImageSectionDto;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImageMaintenancePanel</code> is the Panel class for Image Maintenance.
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
public class ImageMaintenancePanel extends QiAbstractTabbedView<ImageMaintenanceModel, ImageMaintenanceController> {

	private ObjectTablePane<QiImageSectionDto> imageTablePane;

	private UpperCaseFieldBean imageFilterTextField;
	private UpperCaseFieldBean imageFileNameTextField;
	private UpperCaseFieldBean imageNameTextField;
	private UpperCaseFieldBean imageDescriptionTextField;

	private ImageView image;

	private List<Line> gridLines;

	private LoggedButton btnImageFileName;
	private LoggedButton btnCreate;
	private LoggedButton btnUpdate;
	private LoggedButton btnReplaceImage;
	private LoggedButton btnInactivate;
	private LoggedButton btnReactivate;
	private LoggedButton btnReset;

	private CheckBox showGridChkBox;

	private AnchorPane anchorImage;

	private LoggedLabel imageNameLabel;
	private LoggedLabel imageDescLabel;
	
	private HBox imageNameContainer;
	private HBox imageFileNameContainer;
	
	public ImageMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox outerVboxContainer=new VBox();
		HBox outerContainer=new HBox();
		HBox mainPane = new HBox();
		HBox imageDescContainer=new HBox();
		HBox chkBoxBtnContainer=new HBox();
		
		createImageViewContainer();

		mainPane.getChildren().add(createTitledPane("Loaded Images", createImagePanel()));
		
		LoggedTableColumn<QiImageSectionDto, Integer> column = new LoggedTableColumn<QiImageSectionDto, Integer>();
		createSerialNumber(column);
		imageTablePane.getTable().getColumns().add(0, column);
		imageTablePane.getTable().getColumns().get(0).setText("#");
		imageTablePane.getTable().getColumns().get(0).setResizable(true);
		imageTablePane.getTable().getColumns().get(0).setMaxWidth(50);
		imageTablePane.getTable().getColumns().get(0).setMinWidth(5);

		imageFileNameContainer = createImageFileNameContainer();
		
		imageNameContainer = createImageNameContainer();

		imageDescContainer=createImageDescriptionContainer();
		
		chkBoxBtnContainer.getChildren().add(createTitledPane("Actions", createButtonsPanel()));
		
		outerVboxContainer.getChildren().addAll(mainPane,imageFileNameContainer,imageNameContainer,imageDescContainer,chkBoxBtnContainer);
		outerVboxContainer.setSpacing(20);
		outerVboxContainer.setPadding(new Insets(0,0,0,10));

		outerContainer.setPadding(new Insets(10));
		outerContainer.getChildren().addAll(anchorImage,outerVboxContainer);
		this.setCenter(outerContainer);
	}

	@SuppressWarnings("static-access")
	private void createImageViewContainer() {
		anchorImage=new AnchorPane();
		image=new ImageView();
		anchorImage.setTopAnchor(image, 0.01);
		anchorImage.setLeftAnchor(image, 0.01);
		image.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth()/2);
		image.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight()-120);
		image.setPickOnBounds(true);
		image.setPreserveRatio(false);
		anchorImage.setStyle("-fx-border-color: grey;");
		anchorImage.getChildren().add(image);
	}
	
	/**creates image description label and textfield
	 * @param imageDescriptionBox
	 */
	private HBox createImageDescriptionContainer() {
		HBox imageDescriptionBox=new HBox();
		imageDescLabel=UiFactory.createLabelWithStyle("imageDescLabel","Image Description","display-label-14");
		imageDescriptionTextField= UiFactory.createUpperCaseFieldBean("imageDescriptionTextField", 20, Fonts.SS_DIALOG_PLAIN(14), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		if (!isFullAccess()) {
			imageDescriptionTextField.setDisable(true);
		}
		imageDescriptionBox.getChildren().addAll(imageDescLabel,imageDescriptionTextField);
		imageDescriptionBox.setSpacing(37);
		return imageDescriptionBox;
	}

	
	/**creates image name label and textfield
	 * @param imageNameLabelBox
	 * @return 
	 */
	private HBox createImageNameContainer() {
		HBox imageNameLabelBox=new HBox();
		imageNameLabel=UiFactory.createLabelWithStyle("imageNameLabel","Image Name","display-label-14");
		imageNameLabel.setWrapText(true);
		LoggedLabel asteriskimageName=UiFactory.createLabelWithStyle("asteriskimageName","*","display-label-14");
		asteriskimageName.setStyle("-fx-text-fill: red");
		asteriskimageName.setPadding(new Insets(0,0,0,3));
		imageNameLabelBox.getChildren().addAll(imageNameLabel,asteriskimageName);
		imageNameTextField= UiFactory.createUpperCaseFieldBean("imageNameTextField", 20, Fonts.SS_DIALOG_PLAIN(14), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		if (!isFullAccess()) {
			imageNameTextField.setDisable(true);
		}
		imageNameContainer=new HBox();
		imageNameContainer.getChildren().addAll(imageNameLabelBox,imageNameTextField);
		imageNameContainer.setSpacing(66);
		return imageNameContainer;
	}

	
	/**creates image file name button with textfield
	 * @param imageFileNameLabelBox
	 * @return 
	 */
	private HBox createImageFileNameContainer() {
		HBox imageFileNameLabelBox=new HBox();
		btnImageFileName=createImageBtn(QiConstant.BROWSE_IMAGE,getController(),"imageSection-btn");
		btnImageFileName.setStyle("-fx-font-size: 14;-fx-font-weight: bold;");
		
		if (!isFullAccess()) {
			btnImageFileName.setDisable(true);
		}
		LoggedLabel fileNameAsterisk=UiFactory.createLabelWithStyle("fileNameAsterisk","*","display-label-14");
		fileNameAsterisk.setStyle("-fx-text-fill: red");
		fileNameAsterisk.setPadding(new Insets(0,0,0,3));
		imageFileNameLabelBox.getChildren().addAll(btnImageFileName,fileNameAsterisk);
		imageFileNameTextField= UiFactory.createUpperCaseFieldBean("imageFileNameTextField", 20, Fonts.SS_DIALOG_PLAIN(14), TextFieldState.DISABLED, Pos.BASELINE_LEFT);
		imageFileNameContainer=new HBox();
		imageFileNameContainer.getChildren().addAll(imageFileNameLabelBox,imageFileNameTextField);
		imageFileNameContainer.setSpacing(12);
		return imageFileNameContainer;
	}

	/**
	 * This method is used to create Image panel.
	 * @return
	 */
	private MigPane createImagePanel() {
		MigPane pane = new MigPane("insets 5 5 0 5", "[center,grow,fill]", "");
		HBox radioButtonContainer =createFilterRadioButtons(getController(),Screen.getPrimary().getVisualBounds().getWidth()/4);
		HBox filterContainer=new HBox();
		HBox outerContainer=new HBox();
		LoggedLabel imageFilterLabel=UiFactory.createLabelWithStyle("imageFilterLabel","Filter","display-label-14");
		imageFilterTextField = createFilterTextField("imageFilterTextField", 12, getController());
		filterContainer.getChildren().addAll(imageFilterLabel,imageFilterTextField);
		filterContainer.setSpacing(5);
		filterContainer.setAlignment(Pos.BASELINE_RIGHT);
		outerContainer.getChildren().addAll(radioButtonContainer,filterContainer);
		pane.add(outerContainer,"span,wrap");
		imageTablePane=createImageTablePane();
		pane.add(imageTablePane,"span,wrap");	

		return pane;
	}

	/**
	 * This method is used to create Image panel.
	 * @return
	 */
	private MigPane createButtonsPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox btnMainContainer=new HBox();
		HBox btnContainer=new HBox();
		HBox btnContainer2=new HBox();
		VBox btnVboxContainer=new VBox();
		
		createActionButtons();
		
		gridLines = new ArrayList<Line>();
		showGridChkBox=createCheckBox("Show Grid",getController(),"checkBox-ImageSection");
		showGridChkBox.setStyle("-fx-font-size: 12;-fx-font-weight: bold;");
		showGridChkBox.setWrapText(true);
		btnContainer.getChildren().addAll(showGridChkBox,btnCreate,btnUpdate,btnReset);
		btnContainer.setSpacing(20);
		btnContainer2.getChildren().addAll(btnInactivate,btnReactivate,btnReplaceImage);
		btnContainer2.setSpacing(15);
		btnVboxContainer.getChildren().addAll(btnContainer,btnContainer2);
		btnVboxContainer.setSpacing(10);
		btnMainContainer.getChildren().addAll(btnVboxContainer);
		btnMainContainer.setAlignment(Pos.CENTER);
		pane.add(btnMainContainer,"span,wrap");

		return pane;
	}

	private void createActionButtons() {
		btnCreate= createImageBtn(QiConstant.CREATE,getController(),"imageSection-btn");
		btnUpdate=createImageBtn(QiConstant.UPDATE,getController(),"imageSection-btn");
		btnReplaceImage=createImageBtn(QiConstant.REPLACE_IMAGE,getController(),"imageSection-btn");
		btnInactivate=createImageBtn(QiConstant.INACTIVATE,getController(),"imageSection-btn");
		btnReactivate=createImageBtn(QiConstant.REACTIVATE,getController(),"imageSection-btn");
		btnReset=createImageBtn(QiConstant.RESET,getController(),"imageSection-btn");
		
		if (!isFullAccess()) {
			disableButtons();
		}
		
	}
	/**
	 * This method is used to create TitledPane for Image panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		if(title.equalsIgnoreCase("Loaded Images"))
			titledPane.setPrefSize(700, 300);
		else
			titledPane.setPrefSize(700, 200);
		return titledPane;
	}
	private ObjectTablePane<QiImageSectionDto> createImageTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Image Name", "imageName").put("Status","status");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.30,0.113
		}; 
		ObjectTablePane<QiImageSectionDto> panel = new ObjectTablePane<QiImageSectionDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	public void onTabSelected() {
		reload(StringUtils.trim(imageFilterTextField.getText()));	
		getController().clearDisplayMessage();
		disableButtons();
	}

	public void disableButtons() {
		btnCreate.setDisable(true);
		btnUpdate.setDisable(true);
		btnReplaceImage.setDisable(true);
		btnInactivate.setDisable(true);
		btnReactivate.setDisable(true);
	}

	public String getScreenName() {
		return QiRegionalScreenName.IMAGE_MAINTENANCE.getScreenName();
	}

	@Override
	public void reload() {
		imageTablePane.setData(getModel().findImageByFilter("",getSelectedRadioButtonValue()));
	}

	public void reload(String filter) {
		imageTablePane.setData(getModel().findImageByFilter(filter,getSelectedRadioButtonValue()));
	}
	
	public String getFilterTextData()
	{
		return StringUtils.trimToEmpty(imageFilterTextField.getText());
	}
	
	/**
	 * This method return a list of values based on selected radio buttons (e.g. Active - 1, Inactive - 0, All - 0 & 1)
	 * @return
	 */
	private List<Short> getSelectedRadioButtonValue() {
		List<Short> statusList = new ArrayList<Short>();
		if(getAllRadioBtn().isSelected()) {
			statusList.add((short)1);
			statusList.add((short)0);
		} else {
			if(getActiveRadioBtn().isSelected())
				statusList.add((short)1);
			else
				statusList.add((short)0);
			statusList.add((short)2);
		}
		return statusList;
	}
	
	@Override
	public void start() {}

	public ObjectTablePane<QiImageSectionDto> getImageTablePane() {
		return imageTablePane;
	}

	public UpperCaseFieldBean getImageFilterTextField() {
		return imageFilterTextField;
	}

	public void setImageFilterTextField(UpperCaseFieldBean imageFilterTextField) {
		this.imageFilterTextField = imageFilterTextField;
	}

	public LoggedLabel getImageDescLabel() {
		return imageDescLabel;
	}

	public LoggedButton getBtnUpdate() {
		return btnUpdate;
	}

	public LoggedButton getBtnReplaceImage() {
		return btnReplaceImage;
	}

	public LoggedButton getBtnInactivate() {
		return btnInactivate;
	}

	public LoggedButton getBtnReactivate() {
		return btnReactivate;
	}

	public LoggedButton getBtnReset() {
		return btnReset;
	}

	public LoggedLabel getImageNameLabel() {
		return imageNameLabel;
	}

	public LoggedButton getBtnImageFileName() {
		return btnImageFileName;
	}

	public AnchorPane getAnchorImage() {
		return anchorImage;
	}

	public CheckBox getChkBox() {
		return showGridChkBox;
	}

	public LoggedButton getBtnCreate() {
		return btnCreate;
	}

	public List<Line> getGridLines() {
		return gridLines;
	}

	public UpperCaseFieldBean getImageFileNameTextField() {
		return imageFileNameTextField;
	}


	public UpperCaseFieldBean getImageDescriptionTextField() {
		return imageDescriptionTextField;
	}

	public UpperCaseFieldBean getImageNameTextField() {
		return imageNameTextField;
	}

	public ImageView getImage() {
		return image;
	}
	
	
}
