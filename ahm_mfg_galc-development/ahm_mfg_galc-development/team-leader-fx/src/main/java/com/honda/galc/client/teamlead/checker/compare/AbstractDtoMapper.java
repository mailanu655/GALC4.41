package com.honda.galc.client.teamlead.checker.compare;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.AbstractCheckerDto;
import com.honda.galc.util.StringUtil;

import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
/**
 * 
 * <h3>AbstractDtoMapper Class description</h3>
 * <p> AbstractDtoMapper: Abstract Class for Checker Dto Mappers</p>
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
public abstract class AbstractDtoMapper<E extends AbstractCheckerDto> {

	public abstract String getKey();

	public abstract List<E> getFromCheckers();

	public abstract void setFromCheckers(List<E> fromCheckers);

	public abstract List<E> getToCheckers();

	public abstract void setToCheckers(List<E> toCheckers);

	/**
	 * This method is used to create inner tables
	 * @param isFromTable
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ObjectTablePane<E> createSubTable(boolean isFromTable) {

		int maxHeight = Math.max(getFromCheckers().size(), getToCheckers().size());

		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Check Point", "checkPoint")
				.put("Check\nSeq", "checkSeqAsString")
				.put("Check Name","checkName")
				.put("Checker","checker")
				.put("Reaction Type","reactionType");
		Double[] columnWidth = new Double[] {
				0.07, 0.04, 0.07, 0.15, 0.07
		}; 
		ObjectTablePane<E> subTable = new ObjectTablePane<E>(columnMappingList,columnWidth);

		for(TableColumn col : subTable.getTable().getColumns()) {
			if(!isFromTable) {
				changeCellColor(col);
			}
			col.setResizable(false);
			col.setSortable(false);
		}

		if(isFromTable) {
			subTable.setData(FXCollections.observableArrayList(getFromCheckers()));
		} else {
			subTable.setData(FXCollections.observableArrayList(getToCheckers()));
		}
		subTable.setPrefHeight(50+(maxHeight*30));
		subTable.setConstrainedResize(false);
		subTable.getTable().setSelectionModel(null);
		return subTable;
	}

	/**
	 * This method is used to change cell color to show the differences
	 * @param column
	 */
	protected void changeCellColor(final TableColumn<E, String> column) {
		column.setCellFactory(new Callback<TableColumn<E,String>, TableCell<E,String>>() {
			@Override
			public TableCell<E, String> call(TableColumn<E, String> param) {
				TableCell<E, String> cell = new TableCell<E, String>() {
					@SuppressWarnings("unchecked")
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null) {
							String columnName = StringUtil.toCamelCase(column.getText(), false);
							E result = (E) this.getTableRow().getItem();
							if(StringUtils.isEmpty(item.toString()) || (result!= null && isDataDifferent(item, result.getCheckSeq(), columnName))) {
								this.setStyle("-fx-background-color: YELLOW; -fx-border-color: orange;");
							} else {
								this.setStyle("");
							}
							setText(item);
							setGraphic(null);
							setTooltip(new Tooltip(item.toString()));
						}
					}
				};
				return cell;
			}
		});
	}

	/**
	 * This method is used to check if the data is different or not
	 * @param item
	 * @param checkSeq
	 * @param propertyName
	 * @return
	 */
	private boolean isDataDifferent(String item, int checkSeq, String propertyName) {
		boolean isPresent = false;
		for (E checkerDto : getFromCheckers()) {
			try {
				if(checkerDto.getCheckSeq() == checkSeq){
					isPresent = true;
					String value = String.valueOf(new PropertyDescriptor(propertyName, getDtoClass()).getReadMethod().invoke(checkerDto));
					return !item.equals(value);
				}
			} catch (Exception e) {
				Logger.getLogger().error(e, new LogRecord("An exception occured in isDataDifferent() method"));
			}
		}
		return !isPresent;
	}

	@SuppressWarnings("unchecked")
	private Class<E> getDtoClass() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<E>) genericSuperclass.getActualTypeArguments()[0];
	}
}
