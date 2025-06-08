package com.honda.galc.client.ui;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.StringTokenizer;
import javax.swing.JComponent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;

/**
 * 
 * <h3>MessageDialog Class description</h3>
 * <p> MessageDialog description </p>
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
 * @author Jeffray Huang<br>
 * Apr 22, 2010
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class MessageDialog {

	private static int CHAR_COUNT = 50;
	private static int COLUMN = 80;
	private static int ROW = 30;
	private static  String inputText;
	private static boolean isSetTextInInputDialog;


	public static void showError(String message) {
		showError(null,message);
	}

	public static void showError(Component parentComp,String message){

		showError(parentComp,message,CHAR_COUNT);

	}

	public static void showError(Component parentComp,String message,String title){

		showError(parentComp,message,title,CHAR_COUNT);

	}

	public static void showError(Component parentComp,String message,int countPerLine){

		showError(parentComp,message,"Error Occured!",countPerLine);
	}

	public static void showInfo(Component parentComp,String message){

		showInfo(parentComp,message,CHAR_COUNT);

	}
	public static void showInfo(Component parentComp,String message,String title){

		showInfo(parentComp,message,title,CHAR_COUNT);

	}

	public static void showInfo(Component parentComp,String message,int countPerLine){

		showInfo(parentComp,message,"Information",countPerLine);
	}

	public static void showError(Component parentComp,String message,String title,int countPerLine) {
		JOptionPane.showMessageDialog(parentComp, 
				new JLabel(getHtmlText(message,countPerLine)), 
				title, JOptionPane.ERROR_MESSAGE);
		getLogger().info("Showed \"" + title + "\" error dialog with message \"" + message + "\"");
	}

	public static void showInfo(Component parentComp,String message,String title,int countPerLine) {
		JOptionPane.showMessageDialog(parentComp, 
				new JLabel(getHtmlText(message,countPerLine)), 
				title, JOptionPane.INFORMATION_MESSAGE);
		getLogger().info("Showed \"" + title + "\" info dialog with message \"" + message + "\"");
	}

	/**
	 * Show a colored message dialog box.
	 * @param parentComp - the parent component
	 * @param message - the message to be displayed
	 * @param messageType - one of JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.ERROR_MESSAGE, JOptionPane.QUESTION_MESSAGE or JOptionPane.PLAIN_MESSAGE
	 * @param color - the background color of the message
	 * @param title - the title of the dialog
	 */
	public static void showColoredMessageDialog(Component parentComp, String message, String title, int messageType, Color color) {
		showColoredDialog(0, parentComp, message, title, -1, messageType, color);
	}

	/**
	 * Show a colored message dialog box.
	 * @param parentComp - the parent component
	 * @param message - the message to be displayed
	 * @param optionType - one of JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_CANCEL_OPTION or JOptionPane.OK_CANCEL_OPTION
	 * @param messageType - one of JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.ERROR_MESSAGE, JOptionPane.QUESTION_MESSAGE or JOptionPane.PLAIN_MESSAGE
	 * @param color - the background color of the message
	 * @param title - the title of the dialog
	 */
	public static int showColoredConfirmDialog(Component parentComp, String message, String title, int optionType, int messageType, Color color) {
		return showColoredDialog(1, parentComp, message, title, optionType, messageType, color);
	}

	/**
	 * Show a colored message dialog box.
	 * @param category - the category of dialog
	 * @param parentComp - the parent component
	 * @param message - the message to be displayed
	 * @param optionType - one of JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_CANCEL_OPTION or JOptionPane.OK_CANCEL_OPTION
	 * @param messageType - one of JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.ERROR_MESSAGE, JOptionPane.QUESTION_MESSAGE or JOptionPane.PLAIN_MESSAGE
	 * @param color - the background color of the message
	 * @param title - the title of the dialog
	 */
	private static int showColoredDialog(int category, Component parentComp, String message, String title, int optionType, int messageType, Color color) {
		int result = JOptionPane.CLOSED_OPTION;
		JTextArea commentTextArea = new JTextArea(message, 10, 50);
		commentTextArea.setBackground(color);
		commentTextArea.setFont(UiFactory.getInfo().getLabelFont());
		commentTextArea.setEditable(false);
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setBackground(color);

		switch (category) {
		case 0:
			final JOptionPane pane = new JOptionPane(scrollPane, JOptionPane.WARNING_MESSAGE, JOptionPane.OK_OPTION, null, new String[] {"OK"});
			JDialog dialog = pane.createDialog(null, title);
			commentTextArea.requestFocus();
		    dialog.setVisible(true);
		    if (pane.getValue() != null) {
		    	result = JOptionPane.OK_OPTION;
		    	getLogger().info("Showed \"" + title + "\" colored (" + color + ") dialog with message \"" + message + "\" and the user chose 'OK'");
		    } else {
		    	getLogger().info("Showed \"" + title + "\" colored (" + color + ") dialog with message \"" + message + "\" and the user closed the dialog");
		    }
			return result;
		case 1:
			result = JOptionPane.showConfirmDialog(parentComp, scrollPane, title, optionType, messageType);
			switch (result) {
			case JOptionPane.YES_OPTION: // YES_OPTION and OK_OPTION are equal
				if (optionType == JOptionPane.OK_CANCEL_OPTION) {
					getLogger().info("Showed \"" + title + "\" colored (" + color + ") dialog with message \"" + message + "\" and the user chose 'OK'");
				} else {
					getLogger().info("Showed \"" + title + "\" colored (" + color + ") dialog with message \"" + message + "\" and the user chose 'YES'");
				}
				break;
			case JOptionPane.NO_OPTION:
				getLogger().info("Showed \"" + title + "\" colored (" + color + ") dialog with message \"" + message + "\" and the user chose 'NO'");
				break;
			case JOptionPane.CANCEL_OPTION:
				getLogger().info("Showed \"" + title + "\" colored (" + color + ") dialog with message \"" + message + "\" and the user chose 'CANCEL'");
				break;
			case JOptionPane.CLOSED_OPTION:
				getLogger().info("Showed \"" + title + "\" colored (" + color + ") dialog with message \"" + message + "\" and the user closed the dialog");
				break;
			}
			return result;
		}
		return -1;
	}
	
	/**
	 * Show an error message dialog box with an unfocused "OK" button.
	 * @param message - the message to be displayed
	 * @param title - the title of the dialog
	 */
	public static int showErrorUnfocused(String message, String title){
		int result = JOptionPane.CLOSED_OPTION;
		String dialogTitle = "ERROR";
		if (title != null) dialogTitle = title;
		JLabel msgLabel = new JLabel("<html>" + message.replaceAll("\n", "<br/>") + "</html>");
        final JOptionPane pane = new JOptionPane(msgLabel, JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION, null, new String[] {"OK"});
        JDialog dialog = pane.createDialog(null, dialogTitle);
        msgLabel.requestFocus();
        dialog.setVisible(true);
        if (pane.getValue() != null) {
        	result = JOptionPane.OK_OPTION;
        	getLogger().info("Showed \"" + title + "\" unfocused error dialog with message \"" + message + "\" and the user chose 'OK'");
        } else {
        	getLogger().info("Showed \"" + title + "\" unfocused error dialog with message \"" + message + "\" and the user closed the dialog");
        }
        return result;
	}

	public static void showScrollingInfo(Component parentComp,String message){

		showScrollingInfo(parentComp,message,"Information");

	}

	public static void showScrollingInfo(Component parentComp,String message,int column){

		showScrollingInfo(parentComp,message,"Information",ROW,column);

	}

	public static void showScrollingInfo(Component parentComp,String message,String title){

		showScrollingInfo(parentComp,message,title,ROW,COLUMN);

	}

	public static void showScrollingInfo(Component parentComp,String message,int row, int column){

		showScrollingInfo(parentComp,message,"Information",row,column);

	}

	public static String showInputDialog(String title,String label,int maxLength, boolean isUpperCase) {
		return showInputDialog(null,title, label, maxLength, isUpperCase);
	}

	public static String showInputDialog(Component parentComp,String title,String label,int maxLength, boolean isUpperCase) {
		return showInputDialog(parentComp,title,label,maxLength, isUpperCase, null, null);
	}

	public static String showInputDialog(Component parentComp,String title,String label,int maxLength, boolean isUpperCase, Font font, Dimension dimension) {
		LengthFieldBean lengthFieldBean = ( isUpperCase ? new UpperCaseFieldBean() : new LengthFieldBean());
		lengthFieldBean.setName(label);
		lengthFieldBean.setMaximumLength(maxLength);
		if(font != null) lengthFieldBean.setFont(font);
		if(dimension != null) lengthFieldBean.setPreferredSize(dimension);
		if(isSetTextInInputDialog) {
			lengthFieldBean.setText(inputText);	
		}
		// ensure lengthFieldBean has focus
		lengthFieldBean.addAncestorListener( new AncestorListener() {


			public void ancestorRemoved(AncestorEvent event) {
			}


			public void ancestorMoved(AncestorEvent event) {
			}


			public void ancestorAdded(AncestorEvent event) {
				JComponent component =  event.getComponent();
				component.requestFocusInWindow();
				component.removeAncestorListener(this);
			}
		});

		JLabel jLabel = new JLabel(label);
		if(null != font) jLabel.setFont(font);

		Object[] ob = {jLabel,lengthFieldBean}; 

		int result = JOptionPane.showConfirmDialog(parentComp, ob, title, JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String input = lengthFieldBean.getText();
			getLogger().info(label + " dialog was presented and the user entered \'" + input + "\'");
			return input;
		} else {
			getLogger().info(label + " dialog was presented and the user chose to cancel");
			return null;
		}
	}

	public static String showInputDialog(Component parentComp,String title,String label,String text,int maxLength, boolean isUpperCase) {
		   inputText = text;
		   isSetTextInInputDialog = true;
		   return showInputDialog(parentComp,title,label,maxLength,isUpperCase);
	}
	
	public static void showScrollingInfo(Component parentComp,String message,String title,int row, int column) {

		// create a JTextArea
		JTextArea textArea = new JTextArea(row, column);
		textArea.setText(message);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		// wrap a scrollpane around it
		JScrollPane scrollPane = new JScrollPane(textArea);

		// display them in a message dialog
		JOptionPane.showMessageDialog(parentComp, scrollPane,title,JOptionPane.INFORMATION_MESSAGE);

		getLogger().info("Showed \"" + title + "\" scrolling info dialog with message \"" + message + "\"");
	}


	private static String getHtmlText(String str, int count){
		if(str == null) return "";
		int length = Math.max(calculateCount(str),count);
		StringTokenizer st = new StringTokenizer(str, "\n");
		StringBuilder buf = new StringBuilder();
		buf.append("<html>");
		while(st.hasMoreTokens()){
			appendHtmlText(buf,st.nextToken(),length);
		};  
		buf.append("</html>");
		return buf.toString();
	}

	private static int calculateCount(String str) {
		int count = 0;
		StringTokenizer st = new StringTokenizer(str, " \n");
		while(st.hasMoreTokens()) {
			int len = st.nextToken().length();
			if(len > count) count = len;
		}

		return count;
	}

	private static void appendHtmlText(StringBuilder buffer, String str,int count) {

		StringTokenizer st = new StringTokenizer(str, " ");
		int k = 0;
		for(int i=0; st.hasMoreTokens();i++){
			String s = st.nextToken();
			if(k + s.length() >= count && k > 0) {
				buffer.append("<br>");
				k = 0;
			}
			buffer.append(s);
			buffer.append(" ");
			k += s.length();
		}
		if(k>0) buffer.append("<br>");

	}

	public static boolean confirm(Component parentComp, String message) {
		return confirm(parentComp, message, true);
	}

	/**
	 * this confirm dialog will not have a default option
	 * button selected, which will force the user to 
	 * acknowledge the dialog and not dispose it
	 * using a scanner or by pressing the Enter key
	 * 
	 * @param frame
	 * @param message
	 * @return
	 */
	public static boolean confirm(JFrame frame, String message) {
		return OptionDialog.confirm(frame, message);
	}

	/**
	 * Option dialog which allows us to choose which option button
	 * has the focus when shown
	 * 
	 * @param parentComp
	 * @param message
	 * @param selectYes
	 * @return
	 */
	public static boolean confirm(Component parentComp, String message, boolean selectYes) {
		Object buttons[] = {"Yes", "No"};
		int selectedOption = JOptionPane.showOptionDialog(
				parentComp                 			// Position - center to component
				, message				       		// Message
				, "Confirmation"             		// Title
				, JOptionPane.YES_NO_OPTION  		// Option type
				, JOptionPane.QUESTION_MESSAGE 		// MessageType
				, null                     			// Icon (none)
				, buttons                    		// Option buttons
				, buttons[selectYes? 0: 1]			// Button that'll have the focus
				);

		if (selectedOption == JOptionPane.YES_OPTION) {
			getLogger().info(message + " dialog was presented and the user chose 'Yes'");
			return true;
		} else {
			getLogger().info(message + " dialog was presented and the user chose 'No'"); 
			return false;
		}
	}
}
