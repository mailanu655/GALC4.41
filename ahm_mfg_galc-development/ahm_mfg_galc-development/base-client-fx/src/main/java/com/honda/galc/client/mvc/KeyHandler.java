package com.honda.galc.client.mvc;


/**
 * 
 * <h3>KeyHandler Class description</h3>
 * <p>KeyHandler description </p>
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
 * @author Suriya Sena<br>
 * Oct 23, 2014
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.property.KeypadPropertyBean;
import com.honda.galc.service.property.PropertyService;

public  class KeyHandler {
	public enum VirtualKey { LEFT, RIGHT, COMPLETE, REJECT, TOGGLEPANE, SKIPTASK, SKIPOPERATION, SKIPPRODUCT, PREVTASK, PREVOPERATION, UNDEFINED };
    private static HashMap<KeyCodeCombination,VirtualKey> keyMap; 
    
	public static VirtualKey getVirtualKey(KeyEvent keyEvent) {
		loadKeyMapTable();
		VirtualKey vk = VirtualKey.UNDEFINED;
		for (KeyCodeCombination combo :  keyMap.keySet()) {
	    	if (combo.match(keyEvent)) {
	    		vk=keyMap.get(combo);
	    		return vk;
    	    }	
		}
		return vk;
	}
    
	private static void loadKeyMapTable() {
		if (keyMap == null) {
		   keyMap = new HashMap<KeyCodeCombination,VirtualKey>();
		   
		   KeypadPropertyBean property = PropertyService.getPropertyBean(KeypadPropertyBean.class);
		   
		   KeyCodeCombination keyCombo1 = parseKeySeq(property.getLeft());
		   KeyCodeCombination keyCombo2 = parseKeySeq(property.getRight());
		   KeyCodeCombination keyCombo3 = parseKeySeq(property.getComplete());
		   KeyCodeCombination keyCombo4 = parseKeySeq(property.getTogglePane());
		   KeyCodeCombination keyCombo5 = parseKeySeq(property.getSkipOperation());
		   KeyCodeCombination keyCombo6 = parseKeySeq(property.getSkipTask());
		   KeyCodeCombination keyCombo7 = parseKeySeq(property.getSkipProduct());
		   KeyCodeCombination keyCombo8 = parseKeySeq(property.getPreviousTask());
		   KeyCodeCombination keyCombo9 = parseKeySeq(property.getPreviousOperation());
		   KeyCodeCombination keyCombo10 = parseKeySeq(property.getReject());
		   
		   keyMap.put(keyCombo1,  KeyHandler.VirtualKey.LEFT);
		   keyMap.put(keyCombo2,  KeyHandler.VirtualKey.RIGHT);
		   keyMap.put(keyCombo3,  KeyHandler.VirtualKey.COMPLETE);
		   keyMap.put(keyCombo4,  KeyHandler.VirtualKey.TOGGLEPANE);
		   keyMap.put(keyCombo5,  KeyHandler.VirtualKey.SKIPOPERATION);
		   keyMap.put(keyCombo6,  KeyHandler.VirtualKey.SKIPTASK);
		   keyMap.put(keyCombo7,  KeyHandler.VirtualKey.SKIPPRODUCT);
		   keyMap.put(keyCombo8,  KeyHandler.VirtualKey.PREVTASK);
		   keyMap.put(keyCombo9,  KeyHandler.VirtualKey.PREVOPERATION);
		   keyMap.put(keyCombo10,  KeyHandler.VirtualKey.REJECT);
		}
	}
	 
	private static KeyCodeCombination parseKeySeq(String value) {
		KeyCodeCombination keyCombinationRc = null;
		List<KeyCodeCombination.Modifier> modifierList = new ArrayList<KeyCombination.Modifier>();

		String codeList[] = value.split(",");

		if (codeList.length > 6 || codeList.length < 1) {
			throw new IllegalArgumentException("Incorrect format of code string");
		}

		for (int i = 1; i < codeList.length; i++) {
			String modifier = codeList[i];

			if (modifier.equals("SHIFT_DOWN")) {
				modifierList.add(KeyCombination.SHIFT_DOWN);
			}
			if (modifier.equals("CONTROL_DOWN")) {
				modifierList.add(KeyCombination.CONTROL_DOWN);
			}
			if (modifier.equals("ALT_DOWN")) {
				modifierList.add(KeyCombination.ALT_DOWN);
			}
			if (modifier.equals("META_DOWN")) {
				modifierList.add(KeyCombination.META_DOWN);
			}
			if (modifier.equals("SHORTCUT_DOWN")) {
				modifierList.add(KeyCombination.SHORTCUT_DOWN);
			}
		}

		codeList[0] = StringUtils.capitalize(codeList[0].toLowerCase());
		KeyCode keyCode = KeyCode.getKeyCode(codeList[0]);

		if (keyCode != null) {
			keyCombinationRc = new KeyCodeCombination(keyCode,modifierList.toArray(new KeyCombination.Modifier[0]));
		}

		return keyCombinationRc;
	}
}
