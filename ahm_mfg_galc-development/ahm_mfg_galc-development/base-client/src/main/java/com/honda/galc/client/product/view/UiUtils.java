package com.honda.galc.client.product.view;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.command.ChainCommand;
import com.honda.galc.client.ui.MainWindow;
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

	public static void setText(Collection<JTextField> fields, String text) {
		if (fields == null) {
			return;
		}
		for (JTextField tf : fields) {
			tf.setText(text);
		}
	}

	public static void setText(Map<?, JTextField> textFields, Map<?, String> values) {
		if (textFields == null || values == null) {
			return;
		}
		for (Object ix : textFields.keySet()) {
			JTextField field = textFields.get(ix);
			String value = values.get(ix);
			if (field != null) {
				field.setText(value);
			}
		}
	}

	public static void setText(Map<String, JTextField> textFields, Object model) {
		if (textFields == null || textFields.isEmpty()) {
			return;
		}
		setText(textFields.values(), "");
		if (model == null) {
			return;
		}
		for (String propertyName : textFields.keySet()) {
			JTextField field = textFields.get(propertyName);
			if (field == null) {
				continue;
			}
			Object value = BeanUtils.getNestedPropertyValue(model, propertyName);
			String text = value == null ? "" : StringUtils.trim(value.toString());
			field.setText(text);
		}
	}

	public static <T> Map<T, String> getText(Map<T, JTextField> textFields) {
		Map<T, String> values = new HashMap<T, String>();
		if (textFields == null) {
			return values;
		}
		for (T ix : textFields.keySet()) {
			JTextField field = textFields.get(ix);
			String value = field.getText();
			value = StringUtils.trim(value);
			values.put(ix, value);
		}
		return values;
	}

	public static void setEnabled(Collection<? extends Component> components, boolean enabled) {
		if (components == null) {
			return;
		}
		for (Component comp : components) {
			comp.setEnabled(enabled);
		}
	}

	public static void setEditable(Collection<JTextField> components, boolean editable) {
		if (components == null) {
			return;
		}
		for (JTextField comp : components) {
			comp.setEditable(editable);
		}
	}

	public static void setSelected(Collection<? extends AbstractButton> components, boolean selected) {
		if (components == null) {
			return;
		}
		for (AbstractButton comp : components) {
			comp.setSelected(selected);
		}
	}

	public static void setState(Collection<?> components, TextFieldState state) {
		if (state == null || components == null) {
			return;
		}
		for (Object comp : components) {
			if (comp instanceof JTextField) {
				state.setState((JTextField) comp);
			}
		}
	}

	public static void requestFocus(final JComponent component) {
		if (component == null) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// component.requestFocusInWindow();
				component.requestFocus();
				if (component instanceof JTextField) {
					((JTextField) component).selectAll();
				}
			}
		});
	}

	public static String toString(List<Integer> ixSequence, Map<Integer, JTextField> textFields) {
		StringBuilder sb = new StringBuilder();
		if (ixSequence == null || textFields == null) {
			return sb.toString();
		}
		for (Integer ix : ixSequence) {
			JTextField textField = textFields.get(ix);
			// if (textField == null) {
			// TODO review, should it be safe/null check ?
			// } else {
			sb.append(textField.getText());
			// }
		}
		return sb.toString();
	}

	public static Map<Integer, String> toIxMap(List<Integer> ixSequence, String string) {
		Map<Integer, String> ixMap = new HashMap<Integer, String>();
		if (ixSequence == null || string == null) {
			return ixMap;
		}
		string = string.trim();
		for (int i = 0; i < ixSequence.size() && i < string.length(); i++) {
			int key = ixSequence.get(i);
			ixMap.put(key, String.valueOf(string.charAt(key - 1)));
		}
		return ixMap;
	}

	public static List<JTextField> toList(List<Integer> ix, Map<Integer, JTextField> textFields) {
		List<JTextField> list = new ArrayList<JTextField>();
		if (ix == null || textFields == null) {
			return list;
		}
		for (Integer key : ix) {
			JTextField tf = textFields.get(key);
			if (tf != null) {
				list.add(tf);
			}
		}
		return list;
	}

	public static boolean isValid(JTextField textField, ChainCommand validator, MainWindow view) {
		if (textField == null) {
			return false;
		}
		if (validator == null) {
			return true;
		}
		String input = textField.getText();
		List<String> messages = validator.execute(input);
		if (messages != null && !messages.isEmpty()) {
			if (view != null) {
				view.setErrorMessage(messages.get(0));
			}
			TextFieldState.ERROR.setState(textField);
			textField.selectAll();
			return false;
		}
		return true;
	}

	public static boolean isValid(JTextField textField, MainWindow view) {
		ChainCommand validator = (ChainCommand) textField.getClientProperty("validator");
		return (isValid(textField, validator, view));
	}
	
	public static List<String> validate(JTextField textField) {
		if (textField == null || getValidator(textField) == null) {
			return new ArrayList<String>();
		}
		return getValidator(textField).execute(textField.getText());
	}

	public static void mapValidator(JTextField textField, ChainCommand validator) {
		if (textField == null || validator == null) {
			return;
		}
		textField.putClientProperty("validator", validator);
	}
	
	public static ChainCommand getValidator(JTextField textField) {
		if (textField == null) {
			return null;
		}
		Object obj = textField.getClientProperty("validator");
		if (obj instanceof ChainCommand) {
			return (ChainCommand) obj;
		}
		return null;
	}

	public static String dateTimePatternToRegExpPattern(String datePattern) {
		if (datePattern == null) {
			return null;
		}
		return datePattern.replaceAll("[A-Za-z]", "[0-9]");
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getComponents(Container container, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (container == null || clazz == null) {
			return list;
		}
		for (Component comp : container.getComponents()) {
			if (comp == null) {
				continue;
			}
			if (clazz.isAssignableFrom(comp.getClass())) {
				list.add((T) comp);
			} else if (Container.class.isAssignableFrom(comp.getClass())) {
				list.addAll(getComponents((Container) comp, clazz));
			}
		}
		return list;
	}

	public static JTextField getFirstTextField(Container container, TextFieldState state) {
		if (container == null || state == null) {
			return null;
		}
		List<JTextField> list = getComponents(container, JTextField.class);
		for (JTextField field : list) {
			if (state.isInState(field)) {
				return field;
			}
		}
		return null;
	}
}
