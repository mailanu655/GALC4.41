package com.honda.galc.client.ui.component;

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;

/**
 * 
 * <h3>LabeledComboBox Class description</h3>
 * <p> LabeledComboBox description </p>
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
 * @author Jeffray Huang<br>
 * May 17, 2010
 *
 * @author Suriya Sena
 * Jan 29, 2014 JavaFx Migration
 * @param <T>
 *
 */
public class LabeledComboBox<T> extends LabeledControl<ComboBox<T>> {
	private static final long serialVersionUID = 1L;
	
	public LabeledComboBox(String labelName) {
		this(labelName,true);
	}
	
	public LabeledComboBox(String labelName, boolean isHorizontal) {
		super(labelName, new ComboBox<T>(),isHorizontal);
		this.setInsets(10, 10, 10, 10);
	}
	
	public LabeledComboBox(String labelName, boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandaotry) {
		super(labelName, new ComboBox<T>(),isHorizontal,false,insets,isLabelBold,isMandaotry);
	}
	
	public void setSelectedIndex( int selectionIndex) {
		int size = getControl().getItems().size();
		int index = selectionIndex < size? selectionIndex : size -1;
		getControl().getSelectionModel().clearAndSelect(index);
	}
	
	public void setItems(ObservableList<T> list) {
		getControl().setItems(list);
	}	
	
	public void setItems(List<T> list) {
		ObservableList<T> observableList= FXCollections.observableArrayList(list);
		this.setItems(observableList);
	}
	
	public void setItems(T [] array) {
		List<T> list =  Arrays.asList(array);
		this.setItems(list);
	}
}