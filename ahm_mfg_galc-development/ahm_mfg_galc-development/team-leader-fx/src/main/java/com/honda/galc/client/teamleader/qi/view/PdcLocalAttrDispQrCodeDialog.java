package com.honda.galc.client.teamleader.qi.view;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import org.tbee.javafx.scene.layout.fxml.MigPane;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.controller.PdcLocalAttrDispQrCodeDialogController;
import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.IDto;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;

/**
 * 
 * <h3>PdcLocalAttrDispQrCodeDialog Class description</h3>
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
 * @author LnTInfotech<br>
 * 
 */

public class PdcLocalAttrDispQrCodeDialog extends QiFxDialog<PdcLocalAttributeMaintModel> {

	private LoggedButton cancelButton;
	private LoggedButton exportButton;
	private PdcLocalAttrDispQrCodeDialogController displayQrCodeDialogController;
	private String entryScreen;
	private Label doneLabel;
	private Label directPassLabel;
	private Label voidLastLabel;
	private Label repairedLabel;
	private Label notRepairedLabel;
	private Label nonRepairableLabel;
	private CheckBox doneCheckBox;
	private CheckBox directPassCheckBox;
	private CheckBox voidLastCheckBox;
	private CheckBox repairedCheckBox;
	private CheckBox notRepairedCheckBox;
	private CheckBox nonRepairableCheckBox;
	private ObjectTablePane<PdcRegionalAttributeMaintDto> displayQrCodePane;

	public PdcLocalAttrDispQrCodeDialog(String title, PdcLocalAttributeMaintModel model, String entryScreen, ObservableList<PdcRegionalAttributeMaintDto> dataList) {
		super(title, ClientMainFx.getInstance().getStage(), model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.entryScreen=entryScreen;
		this.displayQrCodeDialogController = new PdcLocalAttrDispQrCodeDialogController(model, this);
		initComponents();
		loadData(dataList);
		displayQrCodeDialogController.initListeners();
	}

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<PdcRegionalAttributeMaintDto> createDefectTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Part Defect Description", "partDefectDesc");

		Double[] columnWidth = new Double[] {
				0.25
		};
		ObjectTablePane<PdcRegionalAttributeMaintDto> panel = new ObjectTablePane<PdcRegionalAttributeMaintDto>(columnMappingList,columnWidth);
		
		LoggedTableColumn<PdcRegionalAttributeMaintDto,PdcRegionalAttributeMaintDto> qrCodeColumn = new LoggedTableColumn<PdcRegionalAttributeMaintDto,PdcRegionalAttributeMaintDto>();
		createQrCodeColumn(qrCodeColumn);
		panel.getTable().getColumns().add(1, qrCodeColumn);
		panel.getTable().getColumns().get(1).setText("QR Code");
		panel.getTable().getColumns().get(1).setResizable(true);
		panel.getTable().getColumns().get(1).setPrefWidth(155);
		
		panel.getTable().setPrefHeight(550);
		panel.getTable().setPrefWidth(550);
		return panel;
	}

	/**
	 * This method is used to create dto binding on the QR code column
	 * @param column
	 */
	public void createQrCodeColumn(LoggedTableColumn<PdcRegionalAttributeMaintDto, PdcRegionalAttributeMaintDto> column){
		column.setCellValueFactory(new Callback<CellDataFeatures<PdcRegionalAttributeMaintDto, PdcRegionalAttributeMaintDto>, ObservableValue<PdcRegionalAttributeMaintDto>>() {
			@Override
			public ObservableValue<PdcRegionalAttributeMaintDto> call(CellDataFeatures<PdcRegionalAttributeMaintDto, PdcRegionalAttributeMaintDto> param) {
				return new ReadOnlyObjectWrapper<PdcRegionalAttributeMaintDto>(param.getValue());
			}

		});
		createImage(column);
	}


	/**
	 * This method is used to set QR Code image in column
	 * @param column
	 */
	@SuppressWarnings("unchecked")
	public  void createImage(LoggedTableColumn column) {
		column.setCellFactory( new Callback<LoggedTableColumn<IDto, PdcRegionalAttributeMaintDto>, LoggedTableCell<IDto,PdcRegionalAttributeMaintDto>>()
		{
			public LoggedTableCell<IDto,PdcRegionalAttributeMaintDto> call(LoggedTableColumn<IDto,PdcRegionalAttributeMaintDto> p)
			{	
				return new LoggedTableCell<IDto,PdcRegionalAttributeMaintDto>()
				{
					@Override
					public void updateItem( PdcRegionalAttributeMaintDto item, boolean empty )
					{
						super.updateItem( item, empty ); 
						if(empty){
							setGraphic(null);	
						}else{
							Integer localAttributeId = item.getLocalAttributeId();
							String textMenu = item.getTextEntryMenu();
							ImageView imageView = displayQrCodeDialogController.getQRCode(localAttributeId+"-"+entryScreen+"-"+textMenu);
							setGraphic(imageView);
						}
					}
				};
			}
		});
	}

	
	/**
	 * This method is used to initialize the components of Dialog screen.
	 */
	private void initComponents() {
		displayQrCodePane = createDefectTablePane();
		MigPane migPane = new MigPane();
		migPane.setPrefWidth(550);
		migPane.setPrefHeight(600);
		doneCheckBox=new CheckBox();
		directPassCheckBox=new CheckBox();
		voidLastCheckBox=new CheckBox();
		repairedCheckBox=new CheckBox();
		notRepairedCheckBox=new CheckBox();
		nonRepairableCheckBox=new CheckBox();
		doneLabel = UiFactory.createLabel("doneLabel","Done",Fonts.SS_DIALOG_BOLD(10),60);
		directPassLabel = UiFactory.createLabel("doneLabel","Direct Pass",Fonts.SS_DIALOG_BOLD(10),80);
		voidLastLabel = UiFactory.createLabel("doneLabel","Void Last",Fonts.SS_DIALOG_BOLD(10),90);
		repairedLabel = UiFactory.createLabel("doneLabel","Repaired",Fonts.SS_DIALOG_BOLD(10),60);
		notRepairedLabel = UiFactory.createLabel("doneLabel","Not Repaired",Fonts.SS_DIALOG_BOLD(10),80);
		nonRepairableLabel = UiFactory.createLabel("doneLabel","Non Repairable",Fonts.SS_DIALOG_BOLD(10),90);
		doneCheckBox.setSelected(true);
		directPassCheckBox.setSelected(true);
		repairedCheckBox.setSelected(true);
		notRepairedCheckBox.setSelected(true);
		createSerialNumberColumn();
		migPane.add(displayQrCodePane,"span6 , wrap");
		migPane.add(doneCheckBox);
		migPane.add(doneLabel );
		migPane.add(directPassCheckBox);
		migPane.add(directPassLabel);
		migPane.add(voidLastCheckBox);
		migPane.add(voidLastLabel,"wrap");
		migPane.add(repairedCheckBox);
		migPane.add(repairedLabel);
		migPane.add(notRepairedCheckBox);
		migPane.add(notRepairedLabel);
		migPane.add(nonRepairableCheckBox);
		migPane.add(nonRepairableLabel,"wrap");
		migPane.add(createButtonContainer(),"span6,center");
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}

	/**
	 * This method is used to create button container.
	 */
	private MigPane createButtonContainer(){
		MigPane pane = new MigPane();
		exportButton = createBtn(QiConstant.EXPORT, getDisplayQDCodeDialogController());
		cancelButton = createBtn(QiConstant.CANCEL, getDisplayQDCodeDialogController());
		pane.add(exportButton);
		pane.add(cancelButton);
		return pane;
	}

	/**
	 * This method is used to set data on initial load
	 */
	public void loadData(ObservableList<PdcRegionalAttributeMaintDto> dataList){
		displayQrCodePane.setData(dataList);
	}
	
	/**
	 * This method is used to create serial number column
	 */
	private void createSerialNumberColumn() {
		LoggedTableColumn<PdcRegionalAttributeMaintDto, Integer> column = new LoggedTableColumn<PdcRegionalAttributeMaintDto, Integer>();
		createSerialNumber(column);
		displayQrCodePane.getTable().getColumns().add(0, column);
		displayQrCodePane.getTable().getColumns().get(0).setText("#");
		displayQrCodePane.getTable().getColumns().get(0).setResizable(true);
		displayQrCodePane.getTable().getColumns().get(0).setMaxWidth(100);
		displayQrCodePane.getTable().getColumns().get(0).setMinWidth(1);
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public PdcLocalAttrDispQrCodeDialogController getDisplayQDCodeDialogController() {
		return displayQrCodeDialogController;
	}

	public LoggedButton getExportButton() {
		return exportButton;
	}

	public ObjectTablePane<PdcRegionalAttributeMaintDto> getDisplayQrCodePane() {
		return displayQrCodePane;
	}

	public CheckBox getDoneCheckBox() {
		return doneCheckBox;
	}


	public CheckBox getRepairedCheckBox() {
		return repairedCheckBox;
	}

	public CheckBox getNotRepairedCheckBox() {
		return notRepairedCheckBox;
	}

	public CheckBox getNonRepairableCheckBox() {
		return nonRepairableCheckBox;
	}
	
	public CheckBox getDirectPassCheckBox() {
		return directPassCheckBox;
	}
	
	public CheckBox getVoidLastCheckBox() {
		return voidLastCheckBox;
	}
	
	public String getEntryScreen() {
		return entryScreen;
	}

}