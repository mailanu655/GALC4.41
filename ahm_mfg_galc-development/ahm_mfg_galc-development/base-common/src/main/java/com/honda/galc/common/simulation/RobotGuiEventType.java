/**
 * 
 */
package com.honda.galc.common.simulation;


import com.honda.galc.common.simulation.ButtonClickEvent;
import com.honda.galc.common.simulation.CheckBoxSelectEvent;
import com.honda.galc.common.simulation.ComboBoxSelectEvent;
import com.honda.galc.common.simulation.ComboBoxTextEntryEvent;
import com.honda.galc.common.simulation.KeyInsertEvent;
import com.honda.galc.common.simulation.KeyPressEvent;
import com.honda.galc.common.simulation.ListBoxSelectEvent;
import com.honda.galc.common.simulation.MenuSelectEvent;
import com.honda.galc.common.simulation.MouseClickEvent;
import com.honda.galc.common.simulation.RadioButtonSelectEvent;
import com.honda.galc.common.simulation.TabbedPaneSelectEvent;
import com.honda.galc.common.simulation.TableCellTextEntryEvent;
import com.honda.galc.common.simulation.TableSelectEvent;

/**
 * @author Subu Kathiresan
 * @date March 18, 2013
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 */
public enum RobotGuiEventType implements ISimulationEventType {
	
	MOUSE_CLICK_EVENT(MouseClickEvent.class),
	KEY_INSERT_EVENT(KeyInsertEvent.class),
	KEY_PRESS_EVENT(KeyPressEvent.class),
	TABBED_PANE_SELECT_EVENT(TabbedPaneSelectEvent.class),
	TABLE_SELECT_EVENT(TableSelectEvent.class),
	BUTTON_CLICK_EVENT(ButtonClickEvent.class),
	RADIO_BUTTON_SELECT_EVENT(RadioButtonSelectEvent.class),
	CHECK_BOX_SELECT_EVENT(CheckBoxSelectEvent.class),
	MENU_SELECT_EVENT(MenuSelectEvent.class),
	COMBO_BOX_SELECT_EVENT(ComboBoxSelectEvent.class),
	COMBO_BOX_TEXT_ENTRY_EVENT(ComboBoxTextEntryEvent.class),
	LIST_BOX_SELECT_EVENT(ListBoxSelectEvent.class),
	TABLE_CELL_TEXT_ENTRY_EVENT(TableCellTextEntryEvent.class),
	FIND_TEXT_AND_CLICK_EVENT(FindTextAndClickEvent.class),
	CLEAR_TEXTBOX_EVENT (ClearTextBoxEvent.class);
	
	private Class<? extends RobotGuiEvent> eventClass; 

	private RobotGuiEventType(Class<? extends RobotGuiEvent> eventClass) {
		this.eventClass = eventClass;
	}
	
	public Class<? extends RobotGuiEvent> getEventClass() {
		return eventClass;
	}
	
	public static boolean isValid(String eventName) {
		try {
			if (RobotGuiEventType.valueOf(eventName) != null) {
				return true;
			}
		} catch (Exception ex) {}
		return false;
	}
}
