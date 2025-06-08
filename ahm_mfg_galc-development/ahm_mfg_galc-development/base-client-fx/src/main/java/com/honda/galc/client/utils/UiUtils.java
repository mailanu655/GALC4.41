package com.honda.galc.client.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javafx.application.Platform;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.util.BeanUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UiUtils</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class UiUtils {

	public static void setText(Collection<TextField> fields, String text) {
		if (fields == null) {
			return;
		}
		for (TextField tf : fields) {
			tf.setText(text);
		}
	}

	public static void setText(Map<?, TextField> textFields, Map<?, String> values) {
		if (textFields == null || values == null) {
			return;
		}
		for (Object ix : textFields.keySet()) {
			TextField field = textFields.get(ix);
			String value = values.get(ix);
			if (field != null) {
				field.setText(value);
			}
		}
	}

	public static void setText(Map<String, TextField> textFields, Object model) {
		if (textFields == null || textFields.isEmpty()) {
			return;
		}
		setText(textFields.values(), "");
		if (model == null) {
			return;
		}
		for (String propertyName : textFields.keySet()) {
			TextField field = textFields.get(propertyName);
			if (field == null) {
				continue;
			}
			Object value = BeanUtils.getNestedPropertyValue(model, propertyName);
			String text = value == null ? "" : StringUtils.trim(value.toString());
			field.setText(text);
		}
	}

	public static <T> Map<T, String> getText(Map<T, TextField> textFields) {
		Map<T, String> values = new HashMap<T, String>();
		if (textFields == null) {
			return values;
		}
		for (T ix : textFields.keySet()) {
			TextField field = textFields.get(ix);
			String value = field.getText();
			value = StringUtils.trim(value);
			values.put(ix, value);
		}
		return values;
	}

	public static void setEnabled(Collection<? extends Control> controls, boolean enabled) {
		if (controls == null) {
			return;
		}
		for (Control comp : controls) {
			comp.setDisable(!enabled);
		}
	}

	public static void setEditable(Collection<TextField> components, boolean editable) {
		if (components == null) {
			return;
		}
		for (TextField comp : components) {
			comp.setEditable(editable);
		}
	}

	public static void setState(Collection<?> components, TextFieldState state) {
		if (state == null || components == null) {
			return;
		}
		for (Object comp : components) {
			if (comp instanceof TextField) {
				state.setState((TextField) comp);
			}
		}
	}

	public static void requestFocus(final Control component) {
		if (component == null) {
			return;
		}
		Platform.runLater(new Runnable() {
			public void run() {
				component.requestFocus();
				if (component instanceof TextField) {
					((TextField) component).selectAll();
				}
			}
		});
	}

	public static TextField getNextFocusableTextField(List<TextField> list, TextField currentTextField) {
		if (null == list || list.isEmpty()) {
			return null;
		}
		int startIdx = list.indexOf(currentTextField);
		if (startIdx < 0) {
			startIdx = 0;
		}
		int len = list.size();
		TextField tf = null;
		for (int i = startIdx; i < startIdx + len; i++) {
			tf = list.get((i + 1) % len);
			if (!tf.isDisabled() && tf.isEditable()) {
				return tf;
			}
		}
		return null;
	}

	public static String toString(List<Integer> ixSequence, Map<Integer, TextField> textFields) {
		StringBuilder sb = new StringBuilder();
		if (ixSequence == null || textFields == null) {
			return sb.toString();
		}
		for (Integer ix : ixSequence) {
			TextField textField = textFields.get(ix);
			String text = textField.getText();
			try { //convert to hex code in case of two digits value 
				if (text.length() > 1  && StringUtils.isNumeric(text)) {
					text = Integer.toHexString(Integer.parseInt(text));
				}
			} catch (NumberFormatException e) {
			}
			sb.append(text);
		}
		return sb.toString();
	}

	public static Map<Integer, String> toIxMap(List<Integer> ixSequence, String string, boolean isNumeric) {
		Map<Integer, String> ixMap = new HashMap<Integer, String>();
		if (ixSequence == null || string == null) {
			return ixMap;
		}
		string = string.trim();
		for (int i = 0; i < ixSequence.size() && i < string.length(); i++) {
			if (isNumeric) {
				ixMap.put(ixSequence.get(i), "" + Integer.parseInt("" + string.charAt(i), 16)); //convert hex back
			} else {
				ixMap.put(ixSequence.get(i), String.valueOf(string.charAt(i)));
			}
		}
		return ixMap;
	}

	public static List<TextField> toList(List<Integer> ix, Map<Integer, TextField> textFields) {
		List<TextField> list = new ArrayList<TextField>();
		if (ix == null || textFields == null) {
			return list;
		}
		for (Integer key : ix) {
			TextField tf = textFields.get(key);
			if (tf != null) {
				list.add(tf);
			}
		}
		return list;
	}

	public static String dateTimePatternToRegExpPattern(String datePattern) {
		if (datePattern == null) {
			return null;
		}
		return datePattern.replaceAll("[A-Za-z]", "[0-9]");
	}
	
	public static Label createNewEmptyLabel(String id) {
		return UiFactory.createLabel(id, "               ", Fonts.SS_DIALOG_BOLD(35), 250.0);
	}
	
	public static List<String> validate(LoggedTextField textField) {
		if (textField == null || getValidator(textField) == null) {
			return new ArrayList<String>();
		}
		return getValidator(textField).execute(textField.getText());
	}

	
	public static ChainCommand getValidator(LoggedTextField textField) {
		if (textField != null) {
			Object obj = textField.getProperties().get("validator");
			if (obj instanceof ChainCommand) {
				return (ChainCommand) obj;
			}
		}
		return null;
	}

	public static void mapValidator(LoggedTextField textField,ChainCommand validator) {
		if (textField != null && validator != null) {
			textField.getProperties().put("validator", validator);
		}
	}

	public static void setText(List<LoggedTextField> valueFields, String text) {
		if (valueFields != null) {
			for (LoggedTextField tf : valueFields) {
				tf.setText(text);
			}		
		}
		
	}
}
