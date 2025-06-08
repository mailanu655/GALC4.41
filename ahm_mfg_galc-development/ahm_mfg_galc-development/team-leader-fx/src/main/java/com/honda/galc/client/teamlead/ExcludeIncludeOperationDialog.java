package com.honda.galc.client.teamlead;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationMatrixId;
import com.honda.galc.service.ServiceFactory;

public class ExcludeIncludeOperationDialog extends FxDialog {


	ListView<Item> specCodeCheckView = new ListView<Item>();
	private MCOperationMatrixDTO selectedOperation = null;
	private boolean listModified = false;
	private List<MCOperationMatrix> opMatrixLst = null;
	private HashSet<MCOperationMatrix> specCodeLst = null;
	private HashSet<MCOperationMatrix> excludedLst = null;
	static String MBPN = "MBPN";
	static String NON_MBPN = "NON_MBPN";
	static String PRODUCT = "PRODUCT";
	int NON_MBPN_SPEC_CODE_TOTAL_COUNTS = 0;
	int NON_MBPN_SPEC_CODE_SELECTED_COUNTS = 0;
	
	public ExcludeIncludeOperationDialog(MCOperationMatrixDTO selectedRow ) {
		super("Exclude or Include Operations", ClientMainFx.getInstance().getStage());
		this.setSelectedOperation(selectedRow);
	}

	public boolean constructStage() {
	
		this.setScene(constructScene(getSelectedOperation()));
		this.setHeight(800);
		this.setWidth(600);
		this.toFront();
		this.centerOnScreen();
		this.showAndWait();
		return isListModified();
	}

	private Scene constructScene(MCOperationMatrixDTO selectedRow) {
		
		VBox regSpecCodeVBox = createRegSpecCodeVBox(selectedRow);
		HBox controlHBox = createControlHBox();
		
		VBox componentVBox = new VBox(15);
		componentVBox.setAlignment(Pos.CENTER);
		componentVBox.getChildren().add(regSpecCodeVBox);
		
		componentVBox.getChildren().add(controlHBox);
		
		this.getScene().setRoot(componentVBox); 

		return this.getScene();
		
	}

	private HBox createControlHBox() {

		HBox ctrlHbox =  new HBox(30);
		String confirmImgUrl = "/resource/images/common/confirm.png";
		String cancelImgUrl = "/resource/images/common/reject.png";
		
		Button confirmButton = createButton("  Confirm", "confirmBtn", confirmImgUrl);
		confirmButton.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings({ "unused" })
			public void handle(ActionEvent arg0) {
				if(isListModified()){
					 FXOptionPane.Response response = FXOptionPane.showConfirmDialog(null, "Are you sure to apply the changes? Please confirm !!!", "Manage Exclusion/Inclusion", FXOptionPane.Type.CONFIRM);
					 if(response.equals(FXOptionPane.Response.YES)){
						MCOperationMatrixDao opMatDao = ServiceFactory.getDao(MCOperationMatrixDao.class);
						List<MCOperationMatrix> newExclucedSpecCodeLst = new ArrayList<MCOperationMatrix>();
						List<MCOperationMatrix> newExclucedNonMbpnSpecCodeLst = new ArrayList<MCOperationMatrix>();	
						for(Item item : specCodeCheckView.getItems()){
							
							if(item.getSpecCodeFor().equalsIgnoreCase(MBPN)){
								if(!item.isOn()){
									newExclucedSpecCodeLst.add(createOpMatObj(item.getName()));
								}
							}else{
								if(!item.isOn()){
									NON_MBPN_SPEC_CODE_SELECTED_COUNTS ++;
									newExclucedNonMbpnSpecCodeLst.add(createOpMatObj(item.getName()));
								}
							}						
						}
						
						if(NON_MBPN_SPEC_CODE_SELECTED_COUNTS == NON_MBPN_SPEC_CODE_TOTAL_COUNTS){
							MCOperationMatrix nonMpbn = newExclucedNonMbpnSpecCodeLst.get(0);
							String nonMbpnSpecCodeMask = nonMpbn.getId().getSpecCodeMask().trim().substring(0, 4) + "*";
							nonMpbn.getId().setSpecCodeMask(nonMbpnSpecCodeMask);
							newExclucedNonMbpnSpecCodeLst.clear();
							newExclucedNonMbpnSpecCodeLst.add(nonMpbn);
							newExclucedSpecCodeLst.addAll(newExclucedNonMbpnSpecCodeLst);
						}else{
							newExclucedSpecCodeLst.addAll(newExclucedNonMbpnSpecCodeLst);
						}
							
						if(newExclucedSpecCodeLst != null && newExclucedSpecCodeLst.size()>0){
							int opMatrixDao = opMatDao.deleteExcludedOperations(getSelectedOperation().getOperationName().trim(), getSelectedOperation().getOperationRev(), getSelectedOperation().getPddaPlatFormId());
							
							opMatDao.saveAll(newExclucedSpecCodeLst);
						}else if(newExclucedSpecCodeLst != null && newExclucedSpecCodeLst.size() == 0){
							int opMatrixDao = opMatDao.deleteExcludedOperations(getSelectedOperation().getOperationName().trim(), getSelectedOperation().getOperationRev(), getSelectedOperation().getPddaPlatFormId());
						}
							
						closeExclueIncludeOperScreen();
					 }
				}else{
					FXOptionPane.Response response = FXOptionPane.showConfirmDialog(null, "Observed that you haven't done any operation, Click Yes if you want to stay or Click No to Exit !!!", "Manage Exclusion/Inclusion", FXOptionPane.Type.CONFIRM);
					if(response.equals(FXOptionPane.Response.NO)){
						closeExclueIncludeOperScreen();
					}
				}
			}
		});
		
		Button cancelButton = createButton("  Cancel", "cancelBtn", cancelImgUrl);
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				closeExclueIncludeOperScreen();
			}
		});
		
		ctrlHbox.intersects(30, 30, 30, 30);
		ctrlHbox.setAlignment(Pos.CENTER);
		ctrlHbox.getChildren().add(confirmButton);
		ctrlHbox.getChildren().add(cancelButton);
		return ctrlHbox;
	}

	private VBox createRegSpecCodeVBox(MCOperationMatrixDTO selectedRow) {
		
		opMatrixLst = ServiceFactory.getDao(MCOperationMatrixDao.class).findAllMatrixForOperationAndRevId(selectedRow.getOperationName(), selectedRow.getOperationRev());
		specCodeLst = new LinkedHashSet<MCOperationMatrix>();
		excludedLst = new LinkedHashSet<MCOperationMatrix>();
		
		for(MCOperationMatrix opMatrix : opMatrixLst){
			
			if(opMatrix.getId().getSpecCodeType().equalsIgnoreCase(PRODUCT)){
				specCodeLst.add(opMatrix);
			}else
				excludedLst.add(opMatrix);
		}
		
		VBox regSpecCodeVBox = new VBox(10);
		regSpecCodeVBox.intersects(50, 50, 50, 50);
		specCodeCheckView.editableProperty().set(false);
		specCodeCheckView.setPrefWidth(200);
		specCodeCheckView.setMaxWidth(200);
		specCodeCheckView.setPrefHeight(500);
		specCodeCheckView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		specCodeCheckView.setCursor(Cursor.HAND);
		regSpecCodeVBox.setAlignment(Pos.CENTER);
		
		Label regSpecCodeLbl = UiFactory.createLabel("regSpecCodeLbl", "Spec Code Masks for " + selectedRow.getOperationName().trim(), Fonts.SS_DIALOG_BOLD(20));
		Label infoSpecCodeLbl = UiFactory.createLabel("infoSpecCodeLbl", "Select to Include or Deselect to Exclude", Fonts.SS_DIALOG_PLAIN(15));
	
		for(MCOperationMatrix opMatrix :specCodeLst){
			
			Item item;
			
			  if(opMatrix.getId().getSpecCodeMask().trim().substring(0, 5).matches("[0-9]+")){
				  item = new Item(opMatrix.getId().getSpecCodeMask(), !isInExlucdedLst(opMatrix), MBPN);
			  }else{
				  NON_MBPN_SPEC_CODE_TOTAL_COUNTS++;
				  item = new Item(opMatrix.getId().getSpecCodeMask(), !isInExlucdedLst(opMatrix), NON_MBPN);
			  }
			
			item.onProperty().addListener(new ChangeListener<Boolean>() {
				public void changed(ObservableValue<? extends Boolean> arg0,
						Boolean arg1, Boolean arg2) {
					setListModified(true);
					
				}
			});
			
			specCodeCheckView.getItems().add(item);
		}

		specCodeCheckView.setCellFactory(CheckBoxListCell.forListView(new Callback<Item, ObservableValue<Boolean>>() {
            public ObservableValue<Boolean> call(Item item) {
                return item.onProperty();
            }
        }));
		
		regSpecCodeVBox.setStyle("-fx-border-color: black;-fx-border-width: 2px;-fx-padding: 10;-fx-spacing: 8;");
		regSpecCodeVBox.getChildren().add(regSpecCodeLbl);
		regSpecCodeVBox.getChildren().add(infoSpecCodeLbl);
		regSpecCodeVBox.getChildren().add(specCodeCheckView);
		regSpecCodeVBox.getChildren().add(createSelectDeselectBtn());
		return regSpecCodeVBox;
	}
	
	private HBox createSelectDeselectBtn(){
		
		HBox selectDeselectHBox = new HBox(10);
		
		Button selectBtn = UiFactory.createButton("Select All", "selectBtn");
		Button deselectBtn = UiFactory.createButton("Deselect All", "deselectBtn");
		
		selectBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				for(Item item : specCodeCheckView.getItems()){
					item.setOn(true);
				}
			}
		});
		
		deselectBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				for(Item item : specCodeCheckView.getItems()){
					item.setOn(false);
				}
			}
		});
		
		selectDeselectHBox.getChildren().add(selectBtn);
		selectDeselectHBox.getChildren().add(deselectBtn);
		selectDeselectHBox.setAlignment(Pos.CENTER);
		return selectDeselectHBox;
		
	}

	private void closeExclueIncludeOperScreen() {
		this.close();
	}
	
	private ImageView getImageView(String url){
		
		Image image = new Image(url);
		ImageView imgView = new ImageView(image);
		imgView.setFitHeight(35.0);
		imgView.setFitWidth(35.0);
		imgView.setPreserveRatio(true);
		return imgView;
		
	}

	private Button createButton(String caption, String buttonId, String imgUrl){
		
		final Button button = UiFactory.createButton(caption, buttonId);
		button.setGraphic(getImageView(imgUrl));
		button.setFont(Fonts.DIALOG_BOLD_20);
		button.setCursor(Cursor.HAND);
		button.setStyle("-fx-base: #BDBDBD;");
		
		final DropShadow shadow = new DropShadow();
		shadow.blurTypeProperty().set(BlurType.THREE_PASS_BOX);
		button.addEventHandler(MouseEvent.MOUSE_ENTERED, 
		    new EventHandler<MouseEvent>() {
		        public void handle(MouseEvent e) {
		            button.setEffect(shadow);
		        }
		});

		button.addEventHandler(MouseEvent.MOUSE_EXITED, 
		    new EventHandler<MouseEvent>() {
		        public void handle(MouseEvent e) {
		            button.setEffect(null);
		        }
		});
		return button;
	}
	
	
    private MCOperationMatrixDTO getSelectedOperation() {
		return selectedOperation;
	}

	private void setSelectedOperation(MCOperationMatrixDTO selectedOperation) {
		this.selectedOperation = selectedOperation;
	}

	public boolean isListModified() {
		return listModified;
	}

	public void setListModified(boolean listModified) {
		this.listModified = listModified;
	}

	private boolean isInExlucdedLst(MCOperationMatrix opMat){
		String checkingOpMatSpecCode = opMat.getId().getSpecCodeMask().trim();
		checkingOpMatSpecCode = checkingOpMatSpecCode.substring(0, checkingOpMatSpecCode.length()-1);
		for(MCOperationMatrix excluded : excludedLst){
			String excludedSpecCode = excluded.getId().getSpecCodeMask().trim();
			excludedSpecCode = excludedSpecCode.substring(0, excludedSpecCode.length()-1);
			if(StringUtils.startsWithAny(checkingOpMatSpecCode, new String[]{excludedSpecCode})){
				return true;
			}
			excludedSpecCode = null;
		}
		return false;
	}
	
	private MCOperationMatrix createOpMatObj(String specCodeMask){
		MCOperationMatrix opMatrix = new MCOperationMatrix();
		MCOperationMatrixId opMatrixId = new MCOperationMatrixId(getSelectedOperation().getOperationName(), getSelectedOperation().getOperationRev(), getSelectedOperation().getPddaPlatFormId(), "EXCLUDE" , specCodeMask);
		opMatrix.setId(opMatrixId);
		return opMatrix;
	}
	
	public static class Item {
        private final StringProperty name = new SimpleStringProperty();
        private final BooleanProperty on = new SimpleBooleanProperty();
        private final StringProperty specCodeFor = new SimpleStringProperty();
        
        public Item(String name, boolean on, String specCodeFor) {
            setName(name);
            setOn(on);
            setSpecCodeFor(specCodeFor);
        }

        public final StringProperty nameProperty() {
            return this.name;
        }

        public final String getName() {
            return this.nameProperty().get();
        }

        public final void setName(final String name) {
            this.nameProperty().set(name);
        }

        public final BooleanProperty onProperty() {
            return this.on;
        }

        public final boolean isOn() {
            return this.onProperty().get();
        }

        public final void setOn(final boolean on) {
            this.onProperty().set(on);
        }

        public final String getSpecCodeFor(){
        	return this.specCodeFor.get();
        }
        
        public final void setSpecCodeFor(String spceCodeFor){
        	this.specCodeFor.set(spceCodeFor);
        }
        
        public String toString() {
            return getName();
        }

    }
	
}
