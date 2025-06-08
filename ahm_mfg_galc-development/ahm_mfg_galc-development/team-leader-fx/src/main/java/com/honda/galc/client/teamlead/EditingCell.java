package com.honda.galc.client.teamlead;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;

import com.honda.galc.client.utils.UiFactory;

class EditingCell<T> extends TableCell<T, String> {

	private TextField textField;
	static DataFormat dragFormat = new DataFormat("draggedItem");
	private boolean isTextWrap;

	public EditingCell() {
		// setOnDragDetected();
		// setOnDragOver();
		// setOnDragDropped();
	}

	public EditingCell(boolean isTextWrap) {
		this.isTextWrap = isTextWrap;
	}

	@Override
	public void startEdit() {
		if (!isEmpty()) {
			super.startEdit();
			createTextField("startEdit");
			setText(null);
			setGraphic(textField);
			textField.selectAll();
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();

		setText((String) getItem());
		setGraphic(null);
	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);
			} else {
				if (isTextWrap) {
					Text text = new Text(getString());
					text.wrappingWidthProperty().bind(
							getTableColumn().widthProperty());
					setGraphic(text);
				} else {
					setText(getString());
					setGraphic(null);
				}
			}
		}
	}

	private void createTextField(String id) {
		textField = UiFactory.createTextField(id, getString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				if (!arg2) {
					commitEdit(textField.getText());
				}

			}

		});
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}

	public void setOnMouseClicked() {

		final TableRow<T> row = getTableRow();
		row.setOnDragOver(new EventHandler<DragEvent>() {

			public void handle(DragEvent event) {
				// data is dragged over the target
				Dragboard db = event.getDragboard();
				if (event.getDragboard().hasContent(dragFormat)) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}

		});
	}

	// public void setOnDragDetected() {
	//
	// final TableRow<T> row = getTableRow();
	// row.setOnDragDetected(new EventHandler<MouseEvent>() {
	//
	// public void handle(MouseEvent event) {
	// // drag was detected, start drag-and-drop gesture
	// Dragboard db = EditingCell<T>.this.startDragAndDrop(TransferMode.MOVE);
	// ClipboardContent content = new ClipboardContent();
	// content.putString(String.valueOf(cell.getIndex()));
	// db.setContent(content);
	// }
	//
	// });
	// }
	//

	public void setOnDragOver() {

		final TableRow<T> row = getTableRow();
		row.setOnDragOver(new EventHandler<DragEvent>() {

			public void handle(DragEvent event) {
				// data is dragged over the target
				Dragboard db = event.getDragboard();
				if (event.getDragboard().hasContent(dragFormat)) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}

		});
	}

	public void setOnDragDropped() {

		final TableRow<T> row = getTableRow();
		row.setOnDragDropped(new EventHandler<DragEvent>() {

			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (event.getDragboard().hasContent(dragFormat)) {

					// data.add(text);
					// table.setItems(data);
					success = true;
				}
				event.getGestureTarget().getClass();
				event.setDropCompleted(success);
				event.consume();
			}

		});
	}
}
