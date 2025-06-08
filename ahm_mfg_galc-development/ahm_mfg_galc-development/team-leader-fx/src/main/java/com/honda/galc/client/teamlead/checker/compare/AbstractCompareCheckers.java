package com.honda.galc.client.teamlead.checker.compare;

import java.util.List;

import com.honda.galc.dto.AbstractCheckerDto;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
/**
 * 
 * <h3>AbstractCompareCheckers Class description</h3>
 * <p> AbstractCompareCheckers: Abstract Class for Checker Comparison</p>
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
 * </TABLE>
 *   
 * @author Hemant Kumar<br>
 * Apr 30, 2018
 *
 */
public abstract class AbstractCompareCheckers<M extends AbstractDtoMapper<? extends AbstractCheckerDto>> {

	private TableView<M> table = new TableView<M>();
	private TableColumn<M, HBox> diffCol;

	public AbstractCompareCheckers() {
		constructTable();
	}

	/**
	 * This method is used to create parent table
	 */
	@SuppressWarnings("unchecked")
	public void constructTable() {
		TableColumn<M, String> keyCol = getKeyColumn();
		keyCol.setSortable(false);
		keyCol.setResizable(false);
		keyCol.minWidthProperty().bind(table.widthProperty().multiply(0.08));

		diffCol = getDifferenceColumn();
		diffCol.minWidthProperty().bind(table.widthProperty().multiply(0.92));
		diffCol.setSortable(false);
		diffCol.setResizable(false);
		table.setSelectionModel(null);
		table.getColumns().addAll(keyCol, diffCol);
		Label placeHolder = new Label("No Difference Found!");
		placeHolder.setTextFill(Color.RED);
		placeHolder.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		table.setPlaceholder(placeHolder);
	}

	public void populateTable(List<M> mapper) {
		table.getItems().clear();
		table.getItems().addAll(mapper);
		table.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
	}
	
	/**
	 * This method is used to get Difference Column
	 * @return
	 */
	public abstract TableColumn<M, HBox> getDifferenceColumn();
	
	/**
	 * This method is used to get Key Column
	 * @return
	 */
	public abstract TableColumn<M, String> getKeyColumn();
	
	/**
	 * This method is used to get checker table
	 * @return
	 */
	public TableView<M> getCheckerTable() {
		return table;
	}
	
	/**
	 * This method is used to get difference column 
	 * @return
	 */
	public TableColumn<M, HBox> getDiffCol() {
		return diffCol;
	}
}
