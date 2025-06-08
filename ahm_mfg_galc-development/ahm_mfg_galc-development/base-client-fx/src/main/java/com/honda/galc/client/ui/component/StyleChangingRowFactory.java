package com.honda.galc.client.ui.component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * 
 * @author vc035303
 *
 * http://stackoverflow.com/questions/20350099/programmatically-change-the-tableview-row-appearance#20409924
 */
public class StyleChangingRowFactory<T> implements Callback<TableView<T>, TableRow<T>> {

	private final ObservableList<String> rowStyles;
	private final Callback<TableView<T>, TableRow<T>> baseFactory;

	public StyleChangingRowFactory(Callback<TableView<T>, TableRow<T>> baseFactory) {
		this.baseFactory = baseFactory;
		this.rowStyles = FXCollections.observableArrayList();
	}

	@Override
	public TableRow<T> call(TableView<T> tableView) {

		final TableRow<T> row;
		if (baseFactory == null) {
			row = new TableRow<T>();
		} else {
			row = baseFactory.call(tableView);
		}

		row.indexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obs,
					Number oldValue, Number newValue) {
				updateStyle(row);
			}
		});

		rowStyles.addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> change) {
				updateStyle(row);
			}
		});

		return row;
	}

	public ObservableList<String> getRowStyles() {
		return rowStyles;
	}

	private void updateStyle(TableRow<T> row) {
		int index = row.getIndex();
		if (index > -1 && index < rowStyles.size()) {
			row.setStyle(rowStyles.get(index));
		} else {
			row.setStyle("");
		}
	}

}