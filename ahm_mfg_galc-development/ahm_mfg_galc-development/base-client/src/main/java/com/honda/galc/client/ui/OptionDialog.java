/**
 *
 */
package com.honda.galc.client.ui;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Subu Kathiresan
 * @date Jan 31, 2012
 */
public class OptionDialog extends JDialog implements Runnable {

	private static final long serialVersionUID = -3846333769148032575L;

	private static final String YES_BUTTON = "Yes";
	private static final String NO_BUTTON = "No";
	private static final String TITLE_CONFIRMATION = "Confirmation";

	private String _message;
	private JFrame _owner;
	private JOptionPane _optionPane;
	private volatile int _selectedOption = -1;

	public OptionDialog(JFrame owner){
		super(owner, true);
		setOwner(owner);
		this.addWindowListener(new WindowAdapter() { // window adapter to log when the user clicks the 'X' button to close the dialog
			@Override
			public void windowClosing(WindowEvent we) {
				if (getSelectedOption() < 0) {
					getLogger().info(getMessage() + " dialog was presented and the user chose to close the dialog");
				}
			}
		});
	}

	public OptionDialog(JFrame owner, String title){
		this(owner);
		setTitle(title);
	}

	public OptionDialog(JFrame owner, String title, String message){
		this(owner, title);
		setMessage(message);
	}

	public void run() {
		makeButtonsNonFocusable();
		setVisible(true);
	}

	private void disposeDialog() {
		getOptionPane().setEnabled(false);
		getOptionPane().setVisible(false);
		dispose();
	}

	private void start(){
		try {
			if (SwingUtilities.isEventDispatchThread())
				run();
			else
				SwingUtilities.invokeAndWait(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean confirm(JFrame owner, String message) {
		return new OptionDialog(owner).confirm(TITLE_CONFIRMATION, message);
	}
	
	public static boolean confirm(JFrame owner, String title, String message) {
		return new OptionDialog(owner).confirm(title, message);
	}

	private boolean confirm(String title, String message) {
		setTitle(title);
		setMessage(message);
		setSelectedOption(-1);

		Object buttons[] = {YES_BUTTON, NO_BUTTON};
		setOptionPane(new JOptionPane(getMessage(), JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, buttons));
		add(getOptionPane());
		pack();
		setLocationRelativeTo(getOwner());

		getOptionPane().addPropertyChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
						String prop = e.getPropertyName();
						if ((e.getSource() == getOptionPane()) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
							if (e.getNewValue().equals(YES_BUTTON)) {
								setSelectedOption(0);
								getLogger().info(getMessage() + " dialog was presented and the user chose 'Yes'");
							}
							else if (e.getNewValue().equals(NO_BUTTON)) {
								setSelectedOption(1);
								getLogger().info(getMessage() + " dialog was presented and the user chose 'No'");
							}
							disposeDialog();
						}
					}
				});

		start();
		return getSelectedOption() == 0 ? true : false;
	}

	/**
	 * make all option buttons non-focusable.  This is to force
	 * the user to acknowledge the dialog and not dispose the
	 * dialog box using a scanner or by pressing the Enter key
	 */
	private void makeButtonsNonFocusable() {
		for(Component panel: getOptionPane().getComponents()) {
			if (panel instanceof JPanel) {
				for(Component button: ((JPanel) panel).getComponents()) {
					if (button instanceof JButton) {
						button.setFocusable(false);
					}
				}
			}
		}
	}

	public void setOwner(JFrame owner) {
		_owner = owner;
	}

	public JFrame getOwner() {
		return _owner;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public String getMessage() {
		return _message;
	}

	private void setOptionPane(JOptionPane optionPane) {
		_optionPane = optionPane;
	}

	private JOptionPane getOptionPane() {
		return _optionPane;
	}

	private void setSelectedOption(int selectedOption) {
		_selectedOption = selectedOption;
	}

	private int getSelectedOption() {
		return _selectedOption;
	}
}