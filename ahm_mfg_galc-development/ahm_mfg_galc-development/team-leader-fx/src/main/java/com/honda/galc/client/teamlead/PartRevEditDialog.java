package com.honda.galc.client.teamlead;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.PartType;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationPartRevisionId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.client.ui.component.MessageBox;

public class PartRevEditDialog {

	private Stage dialog = null;
	private Stage owner = null;
	private TableView<MCOperationPartRevisionDTO> tblView;
	private Button okButton;
	private Button cancelButton;
	private TextField partMaskTextField, opProcessor, opView, deviceMsgTextField;
	private Label partMaskOptions, partMaskOptionsHeader;
	private ComboBox<PartCheck> partCheckComboBox;
	private MCOperationPartRevisionDTO rec;
	private CheckBox partMaskCheckBox;

	public PartRevEditDialog(TableView<MCOperationPartRevisionDTO> tblView, MCOperationPartRevisionDTO rec) {
		this.tblView = tblView;
		this.rec = rec;
		constructStage();
	}

	public void closeMessage() {
		dialog.close();
	}

	public void constructStage() {
		dialog = new Stage();
		dialog.initStyle(StageStyle.DECORATED);
		if (owner != null)
			dialog.initOwner(owner);
		dialog.initModality(Modality.APPLICATION_MODAL);
		Scene scene = constructScene();

		dialog.setHeight(400);
		dialog.setWidth(600);

		dialog.setScene(scene);
		dialog.centerOnScreen();
		dialog.sizeToScene();

		dialog.toFront();
		dialog.showAndWait();
	}

	private Scene constructScene() {
		String cssPath = "/resource/css/mfgmaintscreen.css";

		if (ClientMainFx.class.getResource(cssPath) == null) {
			getLogger().warn(
					String.format("Unable to load stylesheet [%s]. Using default",
							cssPath));
		}
		
		boolean enableMFGPartfield = rec.getPartType().equals(PartType.MFG.name()) ? true : false;
		
		opView = UiFactory.createTextField("opView");
		Label opViewLabel = UiFactory.createLabel("opViewLabel", "Enter Part View");
		opView.setText(rec.getView());
		opView.setDisable(enableMFGPartfield ? true : false);

		opProcessor = UiFactory.createTextField("opProcessor");
		Label opProcessorLabel = UiFactory.createLabel("opProcessorLabel", "Enter Part Processor");
		opProcessor.setText(rec.getProcessor());

		opProcessor.setDisable(enableMFGPartfield ? true : false);
		
		//bak - 2015-07-15 - Added new Part Mask Options Key
		partMaskOptionsHeader = UiFactory.createLabel("partMaskOptionsHeader");
		partMaskOptionsHeader.setText("Part Mask Wildcard Options");
		
		partMaskOptions = UiFactory.createLabel("partMaskOptionsLabel");
		CommonPartUtility.PartMaskFormat maskFormat = CommonPartUtility.PartMaskFormat.getFormat(PropertyService.getPartMaskWildcardFormat());	
		String label = "<<Product>> (Product Id),<<Destination>> (Destination ie KH), <<Model>> (Model Year and Code), <<ModelYear>> (Year Code), <<ModelCode>> (Model Code), \n" +
			"<<ModelType>>; (Type Code), <<ModelOption>> (Option Code), <<IntColor>> (Interior Color Code), <<ExtColor>> (Exterior Color Code),<<INTCOLORCODE|A;B>>";
		if (maskFormat == CommonPartUtility.PartMaskFormat.MSEXEL) {
			partMaskOptions.setText("# (Single Digit), ? (Single Character), * (One or More Characters), \n"+ label);			
		}
		else if (maskFormat == CommonPartUtility.PartMaskFormat.MSEXEL_ENHANCED) {
			partMaskOptions.setText("? (Single Letter), # (Single Digit), % (Single Character), * (One or More Characters), \n" + 
									"\\? ( ? ), \\#  ( # ), \\%  ( % ), \\*  ( * ), \\\\  ( \\ ), << % ; n >> (n Characters), \n"+label);
		}
		else {
			partMaskOptions.setText("# (Single Character), * (One or More Characters), \n" + label);
		}
		

		partMaskTextField = UiFactory.createTextField("partMaskTextField");
		//FIF Part Masking
		partMaskTextField.setText(rec.getPartMask());
		partMaskTextField.setDisable(enableMFGPartfield ? false : true);
		Label partMaskLabel = UiFactory.createLabel("partMaskLabel", "Enter Part Mask");
		
		partCheckComboBox = new ComboBox<PartCheck>();
		partCheckComboBox.getItems().setAll(PartCheck.values());
		if(StringUtils.isNotEmpty(rec.getPartCheck())) {
			partCheckComboBox.getSelectionModel().select(PartCheck.get(rec.getPartCheck()));
		} else {
			partCheckComboBox.getSelectionModel().selectFirst();
		}
		partCheckComboBox.setDisable(enableMFGPartfield ? false : true);
		Label partCheckLabel = UiFactory.createLabel("partCheckLabel", "Select Part Check");
		
		//FIF Part Masking
		Label partMaskCheckBoxLabel = UiFactory.createLabel("partMaskCheckBoxLabel", "Use same Part Mask");
		
		partMaskCheckBox = new CheckBox();
		partMaskCheckBox.setVisible(enableMFGPartfield ? true : false);
		partMaskCheckBoxLabel.setVisible(enableMFGPartfield ? true : false);
		partMaskCheckBox.setSelected(true);
		if(!rec.getPartMaskCheckbox()){
			partMaskCheckBox.setVisible(false);
			partMaskCheckBox.setSelected(false);
			partMaskCheckBoxLabel.setVisible(false);
		}
		
		deviceMsgTextField = UiFactory.createTextField("deviceMsgTextField");
		deviceMsgTextField.setText(rec.getDeviceMsg());
		deviceMsgTextField.setDisable(enableMFGPartfield ? true : false);
		Label deviceMsgLabel = UiFactory.createLabel("deviceMsgLabel", "Enter Device Msg");
		
		okButton = UiFactory.createButton("Save");
		cancelButton = UiFactory.createButton("Cancel");

		okButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
			
				if (rec.getPartMask()!=null  && !rec.getPartMask().isEmpty() 
						&& !partMaskTextField.getText().equalsIgnoreCase(rec.getPartMask()) && partMaskCheckBox.isSelected())
				{
					new MessageBox("You cannot enter a different Part Mask value if the Use same Part Mask checkbox is selected").showMessage();
					return;
				}
				rec.setView(opView.getText());
				rec.setProcessor(opProcessor.getText());
				rec.setPartMask(partMaskTextField.getText());
				rec.setPartCheck(partCheckComboBox.getSelectionModel().getSelectedItem().name());
				rec.setDeviceMsg(deviceMsgTextField.getText());
				
				String[] partrev=StringUtils.trimToEmpty(rec.getPartMaskTextboxVal()).split("\\^");
				String partMask="";
				MCOperationPartRevision entity = DTOConverter.convertMcoperationPartRevisionDTO(rec);
				MCOperationPartRevisionId recEntityId = new MCOperationPartRevisionId(rec.getOperationName(), rec.getPartId(), Integer.parseInt(rec.getPartRev()));
				MCOperationPartRevision recEntity = ServiceFactory.getDao(MCOperationPartRevisionDao.class).findByKey(recEntityId);
				
				if(recEntity != null && recEntity.getApproved() != null) {
					entity.setApproved(new Timestamp(recEntity.getApproved().getTime()));
				}
				if(recEntity != null && recEntity.getDeprecated() != null) {
					entity.setDeprecated(new Timestamp(recEntity.getDeprecated().getTime()));
				}
				if(partMaskCheckBox.isSelected() && partrev.length>1)
				{
					for(int i=0; i<partrev.length; i++)
					{
						String[] partrevitem=partrev[i].split("\\:");
					 	rec.setPartRev(partrevitem[0]);
						rec.setPartItemNo(partrevitem[1]);
						partMask=partrevitem[2];
						if(StringUtils.isEmpty(partMaskTextField.getText()) || partMask.equals("#")){
							ServiceFactory.getDao(MCOperationPartRevisionDao.class)
								.save(entity);
						}
					}
				}else{
					ServiceFactory.getDao(MCOperationPartRevisionDao.class)
							.save(entity);
				}

				List<MCOperationPartRevisionDTO> items = new ArrayList<MCOperationPartRevisionDTO>(tblView.getItems());
				tblView.getItems().setAll(items);
				
				closeMessage();
				
			}

		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				closeMessage();
			}
		});
		
		GridPane grid = new GridPane();
		GridPane.setColumnSpan(opView, 3);
		GridPane.setColumnSpan(opProcessor, 3);
		GridPane.setColumnSpan(opView, 3);
		GridPane.setColumnSpan(partMaskOptions, 3);
		GridPane.setColumnSpan(partMaskTextField, 3);
		GridPane.setColumnSpan(deviceMsgTextField, 3);
		GridPane.setColumnSpan(okButton, 3);

		
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(30, 30, 30, 30));

		grid.add(opViewLabel, 0, 2);
		grid.add(opView, 1, 2);

		grid.add(opProcessorLabel, 0, 3);
		grid.add(opProcessor, 1, 3);

		grid.add(partMaskOptionsHeader, 0, 4);
		grid.add(partMaskOptions, 1, 4);
		
		grid.add(partMaskLabel, 0, 5);
		grid.add(partMaskTextField, 1, 5);
		
		grid.add(partCheckLabel, 0, 6);
		grid.add(partCheckComboBox, 1, 6);
		//FIF Part Masking
		grid.add(partMaskCheckBox, 2, 6);
		grid.add(partMaskCheckBoxLabel, 3, 6);
		
		grid.add(deviceMsgLabel, 0, 7);
		grid.add(deviceMsgTextField, 1, 7);

		grid.add(okButton, 1, 8);
		grid.add(cancelButton, 0, 8);

		Scene scene = new Scene(grid);
		scene.getStylesheets().add(cssPath);
		return scene;
	}
}
