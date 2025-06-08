package com.honda.galc.client.teamlead.checker.compare;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.OperationCheckerDto;
import com.honda.galc.entity.conf.MCOperationChecker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
/**
 * 
 * <h3>CompareCheckerUtil Class description</h3>
 * <p> CompareCheckerUtil: Util class for Checker Comparison</p>
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
public class CompareCheckerUtil {
	
	/**
	 * This method is used to get Scroll bar for a given table
	 * @param orientation
	 * @param tableView
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static ScrollBar getScrollbar(Orientation orientation, ObjectTablePane tableView) {
		for (Node n : tableView.lookupAll(".scroll-bar")) {
			if (n instanceof ScrollBar) {
				ScrollBar bar = (ScrollBar) n;
				if (bar.getOrientation().equals(orientation)) {
					return bar;
				}
			}
		}
		return null;
	}
	
	/**
	 * This method is used to synchronize scroll bars of two tables
	 * @param fromTableView
	 * @param toTableView
	 */
	@SuppressWarnings("rawtypes")
	public static void syncScrollBar(ObjectTablePane fromTableView, ObjectTablePane toTableView) {

		final ScrollBar currentPaneHScroll = getScrollbar(Orientation.HORIZONTAL,fromTableView);
		final ScrollBar currentPaneVScroll = getScrollbar(Orientation.VERTICAL,fromTableView);
		final ScrollBar updatPaneHScroll = getScrollbar(Orientation.HORIZONTAL,toTableView);
		final ScrollBar updatePaneVScroll = getScrollbar(Orientation.VERTICAL,toTableView);
		ChangeListener<Number> currentHScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				updatPaneHScroll.setValue(currentPaneHScroll.getValue());
			}
		};
		ChangeListener<Number> updateHScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				currentPaneHScroll.setValue(updatPaneHScroll.getValue());
			}
		};
		ChangeListener<Number> currentVScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				updatePaneVScroll.setValue(currentPaneVScroll.getValue());
			}
		};
		ChangeListener<Number> updateVScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				currentPaneVScroll.setValue(updatePaneVScroll.getValue());
			}
		};
		if(currentPaneVScroll != null) {
			currentPaneVScroll.valueProperty().addListener(currentVScrollChangeListener);
		}
		if(currentPaneHScroll != null) {
			currentPaneHScroll.valueProperty().addListener(currentHScrollChangeListener);
		}
		if(updatPaneHScroll != null) {
			updatPaneHScroll.valueProperty().addListener(updateHScrollChangeListener);
		}
		if(updatePaneVScroll != null) {
			updatePaneVScroll.valueProperty().addListener(updateVScrollChangeListener);
		}
	}
	
	/**
	 * This method is used to convert Entity List to Dto List for Operation Checkers
	 * @param opCheckerList
	 * @return
	 */
	public static List<OperationCheckerDto> getOpCheckerDtoListFromEntityList(List<MCOperationChecker> opCheckerList) {
		List<OperationCheckerDto> opCheckerDtoList = new ArrayList<OperationCheckerDto>();
		for(MCOperationChecker opChecker : opCheckerList) {
			OperationCheckerDto opCheckerDto = new OperationCheckerDto(
					opChecker.getId().getOperationName(), opChecker.getId().getOperationRevision(), opChecker.getId().getCheckPoint(), opChecker.getId().getCheckSeq(), 
					opChecker.getCheckName(), opChecker.getChecker(), opChecker.getReactionType().name(), null, null, 
					StringUtils.EMPTY, false);
			opCheckerDtoList.add(opCheckerDto);
		}
		return opCheckerDtoList;
	}
}
