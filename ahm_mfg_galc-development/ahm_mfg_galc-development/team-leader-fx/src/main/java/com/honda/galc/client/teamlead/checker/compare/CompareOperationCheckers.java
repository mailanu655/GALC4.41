package com.honda.galc.client.teamlead.checker.compare;

import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.OperationCheckerDto;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
/**
 * 
 * <h3>CompareOperationCheckers Class description</h3>
 * <p> CompareOperationCheckers: Operation Checker Comparison</p>
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
public class CompareOperationCheckers extends AbstractCompareCheckers<OperationCheckerDtoMapper> {

	public CompareOperationCheckers() {
		super();
	}

	@Override
	public TableColumn<OperationCheckerDtoMapper, HBox> getDifferenceColumn() {
		TableColumn<OperationCheckerDtoMapper, HBox> diffCol = new TableColumn<OperationCheckerDtoMapper, HBox>("Difference");
		diffCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OperationCheckerDtoMapper,HBox>, ObservableValue<HBox>>() {
			@Override
			public ObservableValue<HBox> call(final CellDataFeatures<OperationCheckerDtoMapper, HBox> param) {
				ObservableValue<HBox> obs = new ObservableValue<HBox>() {
					@Override
					public void addListener(InvalidationListener listener) {
					}

					@Override
					public void removeListener(InvalidationListener listener) {
					}

					@Override
					public void addListener(ChangeListener<? super HBox> listener) {
					}

					@Override
					public void removeListener(ChangeListener<? super HBox> listener) {
					}

					@Override
					public HBox getValue() {
						HBox container = new HBox();
						ObjectTablePane<OperationCheckerDto> fromTable = param.getValue().createSubTable(true);
						ObjectTablePane<OperationCheckerDto> toTable = param.getValue().createSubTable(false);
						CompareCheckerUtil.syncScrollBar(fromTable, toTable);
						container.getChildren().addAll(fromTable, toTable);
						container.setAlignment(Pos.CENTER);
						container.setSpacing(10);
						return container;
					}

				};
				return obs;
			}
		});
		return diffCol;
	}

	@Override
	public TableColumn<OperationCheckerDtoMapper, String> getKeyColumn() {
		TableColumn<OperationCheckerDtoMapper, String> unitNumberCol = new TableColumn<OperationCheckerDtoMapper, String>("Unit Number");
		unitNumberCol.setMinWidth(100);
		unitNumberCol.setCellValueFactory(new PropertyValueFactory<OperationCheckerDtoMapper, String>("key"));
		unitNumberCol.setCellFactory(new Callback<TableColumn<OperationCheckerDtoMapper,String>, TableCell<OperationCheckerDtoMapper,String>>() {
			@Override
			public TableCell<OperationCheckerDtoMapper, String> call(TableColumn<OperationCheckerDtoMapper, String> param) {
				TableCell<OperationCheckerDtoMapper, String> cell = new TableCell<OperationCheckerDtoMapper, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null) {
							setText(item);
							setGraphic(null);
						}
					}
				};
				cell.setAlignment(Pos.CENTER);
				return cell;
			}
		});
		return unitNumberCol;
	}
}
