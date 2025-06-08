package com.honda.galc.client.qi.defectentry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImageSectionPolygonSelectionDialog</code> is the class for Image Section polygon selection
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
 * <TD>14/07/2016</TD>
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
public class ImageSectionPolygonSelectionDialog extends FxDialog{
	
	private LoggedButton okButton;
	private LoggedButton cancelButton;
	private ObjectTablePane<QiImageSectionDto> imageSectionTablePane;
	private String buttonClickedname;
	Map<String, Node> polygonMap = new HashMap<String, Node>();
	private DefectEntryModel model;
	private HBox messageBox;
	private String part1Filter;
	private QiPropertyBean property;
	
	public ImageSectionPolygonSelectionDialog(DefectEntryModel model, Map<String, Node> polygonMap, String part1Filter) {
		super("Image Section Polygon Selection", ClientMainFx.getInstance().getStage());
		this.model = model;
		this.polygonMap = polygonMap;
		this.part1Filter = part1Filter;
		initComponents();
		addImageSectionTablePaneListener();
	}
	
	public ImageSectionPolygonSelectionDialog(String applicationId, DefectEntryModel model) {
		super("Image Section Polygon Selection", applicationId);
		this.model = model;
	}
	
	public boolean showPolygonSelectionDialog(String entryScreenName, String defect1, String defect2) {
		loadData(entryScreenName, defect1, defect2);
		showDialog();
		if(getButtonClickedname().equalsIgnoreCase("Ok"))
			return true;
		else
			return false;
	}
	
	public QiPropertyBean getProperty() {
		if(property == null) {
			property= PropertyService.getPropertyBean(QiPropertyBean.class, ClientMainFx.getInstance().currentApplicationId);
		}
		return property;
	}
	
	private void initComponents(){
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		
		VBox mainBox = new VBox();
		imageSectionTablePane = createImageSectionTablePane();
		HBox btnContainer = new HBox();
		okButton = createBtn("Ok");
		cancelButton = createBtn(QiConstant.CANCEL);
		btnContainer.getChildren().addAll(okButton, cancelButton);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.setPadding(new Insets(10, 10, 10, 10));
		btnContainer.setSpacing(10);
		messageBox = createMessageBox("Please select a Part Location Combination", "error-message");
		messageBox.setVisible(false);
		mainBox.getChildren().addAll(imageSectionTablePane, btnContainer, messageBox);
		((BorderPane) this.getScene().getRoot()).setCenter(mainBox);
		resizeImageSectionDialog();
	}

	private void resizeImageSectionDialog() {
		if (getProperty().getImageSectionListSize() <= 5)
			// Backward compatible for the original dialog size of 5 items
			this.setMaxHeight(265);	
		else 
			// Dynamically expand the dialog box based on the data size in the pane
			// Dialog max out at 15 items on the list with scroll bar 
			VBox.setVgrow(imageSectionTablePane, Priority.ALWAYS);
	}
	
	private void loadData(String entryScreenName, String defect1, String defect2) {
		List<Integer> imageSectionIdList = new ArrayList<Integer>();
		for (Map.Entry<String, Node> entry : polygonMap.entrySet()) {
			imageSectionIdList.add(Integer.parseInt(entry.getKey()));
		}
		List<QiImageSectionDto> dtoList =model.findAllPartLocationCombByImageSections(entryScreenName, imageSectionIdList, defect1, defect2, part1Filter);
		imageSectionTablePane.setData(dtoList);
	}
	
	private LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("popup-btn");
		return btn;
	}
	
	private EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

		public void handle(ActionEvent event) {
			Object source = event.getSource();
			if (source instanceof LoggedButton) {
				LoggedButton btn = (LoggedButton) source;
				buttonClickedname = btn.getText();
				QiImageSectionDto imageSectionDto = imageSectionTablePane.getTable().getSelectionModel().getSelectedItem();
				if(imageSectionDto == null && getButtonClickedname().equalsIgnoreCase("Ok"))
				{
					messageBox.setVisible(true);
				}
				else
				{
					Stage stage = (Stage) btn.getScene().getWindow();
					stage.close();
				}
			}
		}
	};
	
	public void addImageSectionTablePaneListener() {
		imageSectionTablePane.getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiImageSectionDto>() {
			@Override
			public void changed(
					ObservableValue<? extends QiImageSectionDto> arg0,
					QiImageSectionDto oldValue, QiImageSectionDto newValue) {
				messageBox.setVisible(false);
			}
		});
	}
	
	private HBox createMessageBox(String message, String styleClass){
		HBox messageBox = new HBox();
		messageBox.setPadding(new Insets(10, 10, 10, 10));
		LoggedLabel messageLabel = UiFactory.createLabel("messageLabel");
		messageLabel.setText(message);
		messageLabel.setWrapText(true);
		messageLabel.setPrefWidth(300);
		messageLabel.setAlignment(Pos.CENTER);
		Tooltip tooltip = new Tooltip(message);
		tooltip.getStyleClass().add("display-label");
		messageLabel.setTooltip(tooltip);
		messageLabel.getStyleClass().add(styleClass);
		messageBox.getChildren().add(messageLabel);
		messageBox.setAlignment(Pos.CENTER);
		return messageBox;
	}
	
	private ObjectTablePane<QiImageSectionDto> createImageSectionTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("QICS Full Part Name","fullPartDesc")
							.put("Sec Id", "imageSectionId");
		
		Double[] columnWidth = new Double[] {
				0.6,0.05
			}; 
		ObjectTablePane<QiImageSectionDto> panel = new ObjectTablePane<QiImageSectionDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(true);
		panel.setId("assignedPartNameTablePane");
		panel.getStyleClass().add(".table-view. table-cell");
		for (TableColumn tableColumn : panel.getTable().getColumns()) {
			tableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
				@Override
				public TableCell call(TableColumn param) {
	                return new TableCell<QiImageSectionDto, String>() {
	                    @Override
	                    public void updateItem(String item, boolean empty) {
	                        super.updateItem(item, empty);
		                    this.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.01 * getScreenWidth())));
		                    if (!isEmpty()) {
		                        setText(item);
		                    }
	                    }
	                };
	            }
			});
		}
		
		return panel;
	}
	
	public String getButtonClickedname() {
		return StringUtils.trimToEmpty(buttonClickedname);
	}
	public LoggedButton getCancelBtn() {
		return cancelButton;
	}
	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelButton = cancelBtn;
	}
	public ObjectTablePane<QiImageSectionDto> getImageSectionTablePane() {
		return imageSectionTablePane;
	}
	public void setImageSectionTablePane(
			ObjectTablePane<QiImageSectionDto> imageSectionTablePane) {
		this.imageSectionTablePane = imageSectionTablePane;
	}
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
}
