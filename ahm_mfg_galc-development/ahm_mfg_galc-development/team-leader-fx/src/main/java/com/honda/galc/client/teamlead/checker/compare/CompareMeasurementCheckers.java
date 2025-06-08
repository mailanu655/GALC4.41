package com.honda.galc.client.teamlead.checker.compare;

import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.MeasurementCheckerDto;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
/**
 * 
 * <h3>CompareMeasurementCheckers Class description</h3>
 * <p> CompareMeasurementCheckers: Measurement Checker Comparison</p>
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
public class CompareMeasurementCheckers extends AbstractCompareCheckers<MeasurementCheckerDtoMapper> {

	public CompareMeasurementCheckers() {
		super();
	}

	@Override
	public TableColumn<MeasurementCheckerDtoMapper, HBox> getDifferenceColumn() {
		TableColumn<MeasurementCheckerDtoMapper, HBox> diffCol = new TableColumn<MeasurementCheckerDtoMapper, HBox>("Difference");
		diffCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MeasurementCheckerDtoMapper,HBox>, ObservableValue<HBox>>() {
			@Override
			public ObservableValue<HBox> call(final CellDataFeatures<MeasurementCheckerDtoMapper, HBox> param) {
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
						ObjectTablePane<MeasurementCheckerDto> fromTable = param.getValue().createSubTable(true);
						ObjectTablePane<MeasurementCheckerDto> toTable = param.getValue().createSubTable(false);
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
	public TableColumn<MeasurementCheckerDtoMapper, String> getKeyColumn() {
		TableColumn<MeasurementCheckerDtoMapper, String> keyCol = new TableColumn<MeasurementCheckerDtoMapper, String>("Unit Number\nMeas Seq No\nPart No\nPart Item No\nPart Section Code\nPart Type");
		keyCol.setMinWidth(120);
		keyCol.setCellValueFactory(new PropertyValueFactory<MeasurementCheckerDtoMapper, String>("key"));
		keyCol.setCellFactory(new Callback<TableColumn<MeasurementCheckerDtoMapper,String>, TableCell<MeasurementCheckerDtoMapper,String>>() {
			@Override
			public TableCell<MeasurementCheckerDtoMapper, String> call(TableColumn<MeasurementCheckerDtoMapper, String> param) {
				TableCell<MeasurementCheckerDtoMapper, String> cell = new TableCell<MeasurementCheckerDtoMapper, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null) {
							setText(item);
							setGraphic(null);
						}
					}
				};
				cell.setAlignment(Pos.BASELINE_LEFT);
				cell.setPadding(new Insets(0, 0, 0, 10));
				return cell;
			}
		});
		return keyCol;
	}
}
