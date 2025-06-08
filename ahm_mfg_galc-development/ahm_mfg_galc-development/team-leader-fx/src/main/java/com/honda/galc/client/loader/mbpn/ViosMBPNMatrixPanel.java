package com.honda.galc.client.loader.mbpn;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamlead.vios.AbstractViosTabbedView;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosPlatform;
import com.honda.galc.client.teamlead.vios.ViosPlatformPane;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.PddaPlatformUtil;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ViosMBPNMatrixPanel extends AbstractViosTabbedView<ViosMBPNMatrixModel, ViosMBPNMatrixController>{

	private MCViosMasterPlatform platform;
	private ObjectTablePane<MCViosMasterMBPNMatrixData> mbpnMasterDataList;
	private ViosPlatformPane platformTitledPane;
	private VBox mainBox;
	private LabeledTextField filterTextField;
	private LoggedButton fileChooserButton;
	private LoggedButton loadDataButton;
	private LoggedButton resetButton;
	private Text filePath;
	private TextArea templateSelectedText;
	private ProgressBar progressBar;
	
	private LoggedButton uploadButton;
	
	private LoggedButton addButton;
	private LoggedButton updateButton;
	private LoggedButton deleteButton;
	
	public ViosMBPNMatrixPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public void onScreenSelected() {
		PddaPlatformDto platformDto = ViosPlatform.getInstance().getPddaPlatformDto();
		if(!PddaPlatformUtil.isBlankPddaPlatform(platformDto)) {
			MCViosMasterPlatform selectedPlatform = new MCViosMasterPlatform(platformDto);
			platformTitledPane.selectPlatform(selectedPlatform);
			platformTitledPane.setDisable(true);
		}
	}

	@Override
	public String getScreenName() {
		return "VIOS MBPN Matrix";
	}

	@Override
	public void reload() {
		filterTextField.clear();
		reload(StringUtils.EMPTY);
	}


	public void reload(String filter) {
		if(platform != null) {	
			List<MCViosMasterMBPNMatrixData> itemList = ServiceFactory.getService(MCViosMasterMBPNMatrixDataDao.class).findAllData(platform.getGeneratedId(), filter);
			mbpnMasterDataList.setData(itemList);
			
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getTopBody() {
		platformTitledPane = new ViosPlatformPane(this) {
			@Override
			public void vmcComboboxListener(String value) {
				addVMCComboBoxListener(value);
			}
		};
		BorderPane.setMargin(platformTitledPane, new Insets(0, 0, 10, 0));
		return platformTitledPane;
	}

	
	private void addVMCComboBoxListener(String value) {
		clearErrorMessage();
		if(value != null) {
			mainBox.setDisable(false);
			platform = new MCViosMasterPlatform(platformTitledPane.getPlantCombobox().getSelectionModel().getSelectedItem(), 
					platformTitledPane.getDeptCombobox().getSelectionModel().getSelectedItem(), 
					new BigDecimal(platformTitledPane.getModelYearCombobox().getSelectionModel().getSelectedItem()), 
					new BigDecimal(platformTitledPane.getProdRateCombobox().getSelectionModel().getSelectedItem()), 
					platformTitledPane.getLineNoCombobox().getSelectionModel().getSelectedItem(), 
					platformTitledPane.getVmcCombobox().getSelectionModel().getSelectedItem());
			ApplicationContext applicationContext = ClientMainFx.getInstance().getApplicationContext();
			platform.setUserId(applicationContext.getUserId());
			reload();
		} else {
			mainBox.setDisable(true);
			filterTextField.getControl().clear();
			mbpnMasterDataList.getTable().getItems().clear();
			platform = null;
		}
	}

	@Override
	public Node getCenterBody() {
		VBox mbpnDetailsBox = new VBox();
		mbpnDetailsBox.setAlignment(Pos.CENTER);
		mbpnDetailsBox.setSpacing(10);
		mbpnDetailsBox.setPadding(new Insets(10));

		HBox filterBox = new HBox();
		filterBox.setAlignment(Pos.CENTER_RIGHT);
		filterBox.setSpacing(20);

		filterTextField = new LabeledTextField("Search", true, new Insets(0), true, false);
		filterTextField.getControl().setMinWidth(250);
		filterTextField.getControl().setMinHeight(25);
		BorderPane.setMargin(filterTextField.getControl(), new Insets(0, 0, 0, 10));

		filterBox.getChildren().addAll(filterTextField);

		mbpnMasterDataList = createMBPNDetailsTable();

		mbpnDetailsBox.getChildren().addAll(filterBox, mbpnMasterDataList);

		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(10);

		uploadButton = createBtn(ViosConstants.UPLOAD, getController());
		addButton = createBtn(ViosConstants.ADD, getController());
		updateButton = createBtn(ViosConstants.UPDATE, getController());
		deleteButton = createBtn(ViosConstants.DELETE, getController());
		

		buttonContainer.getChildren().addAll(uploadButton);
		buttonContainer.getChildren().addAll(addButton);
		buttonContainer.getChildren().addAll(updateButton);
		buttonContainer.getChildren().addAll(deleteButton);
		

		TitledPane partTitledPane = createTitiledPane("MBPN Master Details", mbpnDetailsBox);

		VBox detailsBox = new VBox();
		detailsBox.setAlignment(Pos.CENTER);
		detailsBox.setSpacing(10);
		detailsBox.getChildren().addAll(partTitledPane);

		mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setDisable(true);

		progressBar = new ProgressBar();
		progressBar.setPrefWidth(150);
		progressBar.setVisible(false);
		
		mainBox.getChildren().addAll(detailsBox,progressBar, buttonContainer);
		return mainBox;
	}
	

	private ObjectTablePane<MCViosMasterMBPNMatrixData> createMBPNDetailsTable() {

		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("ASM Process Number", "id.asmProcNo")
				.put("MBPN Mask", "id.mbpnMask")
				.put("MTC Model", "id.mtcModel")
				.put("MTC Type", "id.mtcType");

		Double[] columnWidth = new Double[] { 0.10,  0.10, 0.10, 0.70}; 
		ObjectTablePane<MCViosMasterMBPNMatrixData> subTable = new ObjectTablePane<MCViosMasterMBPNMatrixData>(columnMappingList,columnWidth,true);
		subTable.setConstrainedResize(false);
		return subTable;
	}
	
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.setOnAction(handler);
		btn.getStyleClass().add("action-btn");
		btn.setPrefWidth(150);
		btn.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.45 * btn.getWidth())));
		return btn;
	}

	private TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font(StringUtils.EMPTY, FontWeight.BOLD, 12));
		titledPane.setContent(content);
		titledPane.setCollapsible(false);
		titledPane.setContentDisplay(ContentDisplay.CENTER);
		return titledPane;
	}
	
	public LabeledTextField getFilterTextField() {
		return filterTextField;
	}
	
	public LoggedButton getUploadButton() {
		return uploadButton;
	}
	
	public LoggedButton getAddButton() {
		return addButton;
	}
	
	public LoggedButton getUpdateButton() {
		return updateButton;
	}
	
	public LoggedButton getFileChooserButton() {
		return fileChooserButton;
	}

	public void setFileChooserButton(LoggedButton fileChooserButton) {
		this.fileChooserButton = fileChooserButton;
	}

	public LoggedButton getLoadDataButton() {
		return loadDataButton;
	}

	public void setLoadDataButton(LoggedButton loadDataButton) {
		this.loadDataButton = loadDataButton;
	}

	public LoggedButton getResetButton() {
		return resetButton;
	}

	public void setResetButton(LoggedButton resetButton) {
		this.resetButton = resetButton;
	}

	public Text getFilePath() {
		return filePath;
	}

	public void setFilePath(Text filePath) {
		this.filePath = filePath;
	}

	public TextArea getTemplateSelectedText() {
		return templateSelectedText;
	}

	public void setTemplateSelectedText(TextArea templateSelectedText) {
		this.templateSelectedText = templateSelectedText;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	public MCViosMasterPlatform getPlatform() {
		return platform;
	}
	
	public ObjectTablePane<MCViosMasterMBPNMatrixData> getmbpnMasterDataList() {
		return mbpnMasterDataList;
	}

	public LoggedButton getDeleteButton() {
		return deleteButton;
	}
	
}


