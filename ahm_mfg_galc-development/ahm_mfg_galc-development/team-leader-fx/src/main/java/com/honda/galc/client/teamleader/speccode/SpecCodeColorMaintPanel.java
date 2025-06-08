package com.honda.galc.client.teamleader.speccode;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.FrameSpecDto;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class SpecCodeColorMaintPanel extends AbstractTabbedView<SpecCodeColorMaintModel, SpecCodeColorMaintController>{

	private TextField productIdTextField;
	final private String font_style="-fx-font: 15 arial;";
	private Button productIdBtn;
	private ObjectTablePane<FrameSpecDto> frameSpecDtoDataList;
	private Button editButton;
	private List<FrameSpecDto> frameSpecDtoList;
	
	public SpecCodeColorMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	@Override
	public void onTabSelected() {
		initComponents();
	}

	@Override
	public String getScreenName() {
		return "Prod Spec Code Maint";
	}

	
	public void initComponents() {
		BorderPane pane = new BorderPane();
		TitledPane titlePane =  new  TitledPane();
		
		titlePane.setText("Product Spec Code Maintainance");
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 20, 10, 20));
		vbox.setSpacing(10);

		frameSpecDtoDataList = createSpecCodeDetailsPanel();
		editButton  = UiFactory.createButton("Edit Result", font_style, true);
		editButton.setPrefSize(200, 30);
		editButton.setDisable(true);
		
		TableColumn<FrameSpecDto, String> columnToStyle = (TableColumn<FrameSpecDto, String>) frameSpecDtoDataList.getTable().getColumns().get(3);
		columnToStyle.setCellFactory(new Callback<TableColumn<FrameSpecDto, String>, TableCell<FrameSpecDto, String>>() {
			@Override
			public LoggedTableCell<FrameSpecDto, String> call(
					TableColumn<FrameSpecDto, String> param) {

				final LoggedTableCell<FrameSpecDto, String> cell = new LoggedTableCell<FrameSpecDto, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty) {
							String style = "";
							if(item.contains(PreProductionLotSendStatus.DONE.name())) {
								style = "-fx-font-weight: bold;-fx-text-fill: red;";
							} else if(item.contains(PreProductionLotSendStatus.WAITING.name())){
								style = "-fx-font-weight: bold;-fx-text-fill: black;";
							} 
							setStyle(style);	
						} 
						setText(item);
					}
				};
				return cell;
			}
		});
		
		
		vbox.getChildren().addAll(titlePane, createProductPanel(), frameSpecDtoDataList, editButton);
		pane.setCenter(vbox);
		setCenter(pane);
		mapActions();
		
	}

	private void mapActions() {
		getProductIdTextField().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	getProductIdTextField().setText(newValue);
		        reload();
		    }
		});
		
		getProductIdBtn().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				getController().productIdButtonClick();
			}
		});
		
		getEditButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				clearErrorMessage();
				getController().editButtonClicked();
			}
		});
		
	}

	private ObjectTablePane<FrameSpecDto> createSpecCodeDetailsPanel() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "productId")
				.put("Production Lot", "productionLot").put("Product Spec Code","productSpecCode")
				.put("Send Status", "sendStatusString").put("Ext Color Desc", "extColorDescription")
				.put("Int Color Code","intColorCode").put("Int Color Desc", "intColorDescription")
				.put("Sales Model Type Code", "salesModelTypeCode").put("Sales Model Code", "salesModelCode")
				.put("Sales Ext Color Code", "salesExtColorCode");

		Double[] columnWidth = new Double[] {
				0.10, 0.15, 0.15,0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10
		}; 
		ObjectTablePane<FrameSpecDto> panel = new ObjectTablePane<FrameSpecDto>(columnMappingList,columnWidth);
		panel.setPrefSize(100, 200);
		panel.setConstrainedResize(false);
		return panel;
	}


	
	private Node createProductPanel() {
		HBox hbox = new HBox();
		//Product/VIN Text field
		productIdTextField = UiFactory.createTextField("productIdTextField", 17,TextFieldState.EDIT);
		productIdTextField.setMaxWidth(200);
		productIdTextField.setFocusTraversable(true);
		productIdTextField.requestFocus();
		productIdTextField.setStyle(font_style);
		productIdTextField.setPrefSize(200, 30);
		
		productIdBtn = UiFactory.createButton("VIN", font_style, true);
		productIdBtn.setPrefSize(100, 30);
		
		HBox row = new HBox();
		row.setPadding(new Insets(5, 10, 5, 10));
		row.setAlignment(Pos.BASELINE_LEFT);
		row.setSpacing(10);
		row.getChildren().add(productIdBtn);
		row.getChildren().add(productIdTextField);
		
		hbox.getChildren().add(row);
		return hbox;
	}

	@Override
	public void reload() {
		clearErrorMessage();
		if(productIdTextField.getText() != null && !StringUtil.isNullOrEmpty(productIdTextField.getText() )) {
			frameSpecDtoList = getModel().getFramSpecList(productIdTextField.getText());
			frameSpecDtoDataList.setData(frameSpecDtoList);
			if(frameSpecDtoList.size()>=1)
				editButton.setDisable(false);
		} else {
			frameSpecDtoList = new ArrayList<FrameSpecDto>();
			frameSpecDtoDataList.setData(frameSpecDtoList);
			editButton.setDisable(true);
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initView() {
		onTabSelected();
	}

	public ObjectTablePane<FrameSpecDto> getFrameSpecDtoDataList() {
		return frameSpecDtoDataList;
	}

	public void setFrameSpecDtoDataList(ObjectTablePane<FrameSpecDto> frameSpecDtoDataList) {
		this.frameSpecDtoDataList = frameSpecDtoDataList;
	}

	public TextField getProductIdTextField() {
		return productIdTextField;
	}

	public void setProductIdTextField(TextField productIdTextField) {
		this.productIdTextField = productIdTextField;
	}

	public Button getProductIdBtn() {
		return productIdBtn;
	}

	public void setProductIdBtn(Button productIdBtn) {
		this.productIdBtn = productIdBtn;
	}

	public Button getEditButton() {
		return editButton;
	}

	public void setEditButton(Button editButton) {
		this.editButton = editButton;
	}

	public List<FrameSpecDto> getFrameSpecDtoList() {
		return frameSpecDtoList;
	}

	public void setFrameSpecDtoList(List<FrameSpecDto> frameSpecDtoList) {
		this.frameSpecDtoList = frameSpecDtoList;
	}

}
