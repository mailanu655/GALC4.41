package com.honda.galc.client.teamleader.qi.view;

import java.math.BigDecimal;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.UpdatePddaModelYearController;
import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UpdatePddaModelYearDialog</code> is the Dialog  class for updating Pdda Model Year.
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
 * <TD>3/10/2017</TD>

 * </TABLE>
*/
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
 public class UpdatePddaModelYearDialog extends QiFxDialog<PdcLocalAttributeMaintModel>{ 

	private LoggedButton updateButton;
	private LoggedButton cancelButton;
	private LabeledComboBox<BigDecimal> modelYearComboBox;
	private UpdatePddaModelYearController controller;
	private volatile boolean isCancel = false;


	public UpdatePddaModelYearDialog(String title,String applicationId,PdcLocalAttributeMaintModel model, List<PdcRegionalAttributeMaintDto> selectedDataList) {
		super(title, applicationId, model);
		this.controller = new UpdatePddaModelYearController(model, this,selectedDataList);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		controller.loadInitialData();
		controller.initListeners();
	}

	/**
	 * This method is used to create layout of Update Pdda Model Year Dialog
	 */
	private void initComponents() {
		double screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane pane = new MigPane("insets 10 10 10 10", "center", "");
		modelYearComboBox=  createLabeledComboBox("Model Year", true, new Insets(10), true, true);
		modelYearComboBox.getControl().setMinHeight(35.0);
		modelYearComboBox.getControl().setMinWidth(100.0);
		modelYearComboBox.getControl().setId("modelYearComboBox");
		modelYearComboBox.getLabel().setPadding(new Insets(0,10,0,0));
		updateButton = createBtn(QiConstant.DONE,getController());
		updateButton.setDisable(true);
        cancelButton = createBtn(QiConstant.CANCEL, getController());
		pane.add(modelYearComboBox,"wrap");
		pane.add(updateButton,"split2");
		pane.add(cancelButton);
		pane.setPrefWidth(screenWidth/5);
		pane.setPrefHeight(screenHeight/5);
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
		
	}
	
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	private LabeledComboBox<BigDecimal> createLabeledComboBox(String label,boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandaotry) {
		LabeledComboBox<BigDecimal> comboBox = new LabeledComboBox<BigDecimal>(label,isHorizontal,insets,isLabelBold,isMandaotry);
		return comboBox;
	}

	public LoggedButton getUpdateButton() {
		return updateButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public LabeledComboBox<BigDecimal> getModelYearComboBox() {
		return modelYearComboBox;
	}

	public UpdatePddaModelYearController getController() {
		return controller;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	
	 
 }