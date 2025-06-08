package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.model.ImageMaintenanceModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImageSectionCopyFromDialog</code> is the dialog to select Image for copy sections. 
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
 * <TD>27/04/2017</TD>
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
public class ImageSectionCopyFromDialog extends QiFxDialog<ImageMaintenanceModel> {
	
	private LoggedButton okButton;
	private LoggedButton cancelButton;
	private String buttonClickedname;
	private LabeledComboBox<String> imageNameComboBox;
	List<String> imageNameList;
	
	public ImageSectionCopyFromDialog(String applicationId, List<String> imageNameList , ImageMaintenanceModel model) {
		super("Image Section Copy From Dialog", applicationId, model);
		this.imageNameList = imageNameList;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
	}
	
	public boolean showCopyFromDialog() {
		initComponents();
		showDialog();
		if (getButtonClickedname().equalsIgnoreCase("Ok")) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void initComponents() {
		MigPane migPane = new MigPane("insets 0 5 0 5", "[left,grow]", "[]20[]");
		migPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 4);
		migPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() / 4);
		imageNameComboBox = createLabeledComboBox("Image Name");
		okButton = createBtn("Ok");
		okButton.setDisable(true);
		cancelButton = createBtn(QiConstant.CANCEL);
		
		migPane.add(imageNameComboBox, "dock north");
		migPane.add(okButton, "cell 1 2 1 1");
		migPane.add(cancelButton, "cell 2 2 1 1");
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}
	
	private LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
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
				Stage stage = (Stage) btn.getScene().getWindow();
				stage.close();
			}
		}
	};
	
	private ChangeListener<String> listener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
			if (new_val != null)
				okButton.setDisable(false);
			else
				okButton.setDisable(true);
		}
	};
	
	private LabeledComboBox<String> createLabeledComboBox(String label) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(label, true, new Insets(10), true, false);
		comboBox.getLabel().setPadding(new Insets(10, 0, 10, 30));
		comboBox.setId(label);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setMinHeight(35.0);
		comboBox.getControl().getItems().addAll(imageNameList);
		comboBox.getControl().setPrefHeight(35);
		comboBox.getControl().getSelectionModel().selectedItemProperty().addListener(listener);
		return comboBox;
	}
	
	public String getButtonClickedname() {
		return StringUtils.trimToEmpty(buttonClickedname);
	}

	public LabeledComboBox<String> getImageNameComboBox() {
		return imageNameComboBox;
	}
}
