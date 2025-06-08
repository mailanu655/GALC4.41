package com.honda.galc.client.ui;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.honda.galc.client.product.view.UiFactory;

public class ErrorMessageDialog extends JDialog  {

	private static final long serialVersionUID = 1L;

	public enum ButtonOptions {
		REFRESH_ONLY, SKIP_ONLY, REFRESH_SKIP, YES_NO, CANCEL_SKIP_REFRESH, YES_NO_REFRESH;
	}
	public static final String defaultRefreshLabel="REFRESH"; 
	public static final String defaultSkipLabel="SKIP"; 
	public static final String defaultYesLabel="YES"; 
	public static final String defaultNoLabel="NO"; 
	public static final String defaultCancelLabel="CANCEL"; 
	
	private String message;
	private String backGroundColor;
	private String foreGroundColor;
	private String errorCode = null;
	private Map<String, String> options;
	private boolean allowScan = false;
	private ButtonOptions buttonOptions;
	
	private JButton skipButton, refreshButton, noButton, yesButton, cancelButton;;
	private JTextField scan;

	public ErrorMessageDialog(JFrame owner, String title, String message, String backgroundColor, String forgroundColor,
			String errorCode, boolean allowScan, ButtonOptions buttonList, Map<String, String> options) {
		super(owner, true);
		setTitle(title);
		setLocationRelativeTo(owner);
		this.message = message;
		this.backGroundColor = backgroundColor;
		this.foreGroundColor = forgroundColor;
		this.errorCode = errorCode;
		this.allowScan = allowScan;
		this.buttonOptions = buttonList;
		this.options = options;
		initComponents();
		getLogger().info(this.toString());
	}

	public ErrorMessageDialog(JFrame owner, String title, String message, boolean allowScan, ButtonOptions buttonList) {
		super(owner, true);
		setTitle(title);
		setLocationRelativeTo(owner);
		this.message = message;
		this.allowScan = allowScan;
		this.buttonOptions = buttonList;
		initComponents();
		getLogger().info(this.toString());
	}

	public ErrorMessageDialog(JFrame owner, String title, String message) {
		super(owner, true);
		setTitle(title);
		setLocationRelativeTo(owner);
		this.message = message;
		initComponents();
		getLogger().info(this.toString());
	}

	@Override
	public String toString() {
		return ErrorMessageDialog.class.getSimpleName()
				+ " [message=" + this.message
				+ ", backGroundColor=" + this.backGroundColor
				+ ", foreGroundColor=" + this.foreGroundColor
				+ ", errorCode=" + this.errorCode
				+ ", allowScan=" + this.allowScan
				+ ", buttonOptions=" + this.buttonOptions
				+ ", options=" + this.options
				+ "]";
	}

	private void initComponents() {

		JPanel areaPanel = getDefaultPanel(null);
		areaPanel.setLayout(new BoxLayout(areaPanel, BoxLayout.Y_AXIS));

		if(errorCode!= null && errorCode.length()> 0)
			areaPanel.add(getErrorCodePanel());
		
		areaPanel.add(getMessageArea());
		
		if (allowScan)
			areaPanel.add(getScanTextBoxPanel());
		areaPanel.add(getButtonPanel());
		
		setContentPane(areaPanel);
		setSize(800, 400);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		if (buttonOptions != null) {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}
		
		 WindowAdapter adapter = new WindowAdapter() {
	            private boolean gotFocus = false;
	            public void windowClosing(WindowEvent we) {
	              
	            }
	            public void windowGainedFocus(WindowEvent we) {
	                // Once window gets focus, set initial focus
	                if (!gotFocus) {
	                   gotFocus = true;
	                   if(allowScan) {
	                	   getScanTextBox().requestFocus();
	                   }else{
	                	   getRefreshButton().requestFocus();
	                   }
	                }
	            }
	        };
	        addWindowListener(adapter);
	        addWindowFocusListener(adapter);
	}

	private JPanel getButtonPanel() {
		JPanel buttonPanel = getDefaultPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
			
		if (buttonOptions != null) {
			switch (buttonOptions) {
			case SKIP_ONLY:
				buttonPanel.add(getSkipButton());
				break;
			case REFRESH_ONLY:
				buttonPanel.add(getRefreshButton());
				break;
			case REFRESH_SKIP:
				buttonPanel.add(getSkipButton());
				buttonPanel.add(getRefreshButton());
				break;
			case YES_NO:
				buttonPanel.add(getYesButton());
				buttonPanel.add(getNoButton());
				break;
			case YES_NO_REFRESH:
				buttonPanel.add(getYesButton());
				buttonPanel.add(getNoButton());
				buttonPanel.add(getRefreshButton());
				break;
			case CANCEL_SKIP_REFRESH:
				buttonPanel.add(getCancelButton());
				buttonPanel.add(getSkipButton());
				buttonPanel.add(getRefreshButton());
				break;
			}
		}

		return buttonPanel;
	}

	private JButton getSkipButton() {
		if (skipButton == null) {
			skipButton = UiFactory.getInfo().createButton(getSkipLabel(), true);
			skipButton.addActionListener(getButtonLogActionListener(getSkipLabel()));
		}

		return skipButton;
	}

	private JButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton = UiFactory.getInfo().createButton(getRefreshLabel(), true);
			refreshButton.addActionListener(getButtonLogActionListener(getRefreshLabel()));
		}

		return refreshButton;
	}
	
	private JButton getNoButton() {
		if (noButton == null) {
			noButton = UiFactory.getInfo().createButton(getNoLabel(), true);
			noButton.addActionListener(getButtonLogActionListener(getNoLabel()));
		}

		return noButton;
	}

	private JButton getYesButton() {
		if (yesButton == null) {
			yesButton = UiFactory.getInfo().createButton(getYesLabel(), true);
			yesButton.addActionListener(getButtonLogActionListener(getYesLabel()));
		}

		return yesButton;
	}

	private JPanel getScanTextBoxPanel() {
		JPanel scanTextPanel = getDefaultPanel(new FlowLayout(FlowLayout.CENTER));
		scanTextPanel.add(getScanTextBox());

		return scanTextPanel;
	}

	public JTextField getScanTextBox() {
		if (scan == null) {
			scan = new JTextField();
			scan.setFont(UiFactory.getIdle().getInputFont());
			scan.setPreferredSize(new Dimension(700, 60));
		}

		return scan;
	}

	private JPanel getErrorCodePanel() {
		JPanel errorCodePanel = getDefaultPanel(new GridLayout(1, 5, 20, 10));
		errorCodePanel.add(getErrorCodeLabel());
		errorCodePanel.add(UiFactory.getDefault().createLabel(""));
		errorCodePanel.add(UiFactory.getDefault().createLabel(""));
		errorCodePanel.add(UiFactory.getDefault().createLabel(""));
		errorCodePanel.add(getErrorCodeLabel());

		return errorCodePanel;
	}

	private JLabel getErrorCodeLabel() {
		JLabel errorCodeLabel = UiFactory.getIdle().createLabel(errorCode, SwingConstants.CENTER);
		errorCodeLabel.setForeground(getForeGroundColor());
		return errorCodeLabel;
	}

	private JPanel getMessageArea() {

		JTextArea commentTextArea = new JTextArea(message, 5, 40);
		commentTextArea.setBackground(getBackGroundColor());
		commentTextArea.setForeground(getForeGroundColor());
		commentTextArea.setFont(UiFactory.getInfo().getLabelFont());
		commentTextArea.setEditable(false);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scrollPane.setBorder(emptyBorder);

		JPanel messagePanel = getDefaultPanel(new FlowLayout(FlowLayout.CENTER));
		messagePanel.add(scrollPane);

		return messagePanel;
	}

	private JPanel getDefaultPanel(LayoutManager layout) {
		JPanel panel = new JPanel();
		panel.setBackground(getBackGroundColor());
		panel.setLayout(layout);
		return panel;
	}

	private Color getBackGroundColor() {
		Color color = getColor(backGroundColor);
		if (color != null) 	return color;
		return Color.YELLOW;
	}

	private Color getForeGroundColor() {
		Color color = getColor(foreGroundColor);
		if (color != null) 	return color;
		return Color.BLACK;
	}

	private Color getColor(String color) {
		if (color != null) {
			return Color.decode(color);
		}
		return null;
	}

	public void setActionListener(ActionListener actionListener) {
		getSkipButton().addActionListener(actionListener);
		getRefreshButton().addActionListener(actionListener);
		getYesButton().addActionListener(actionListener);
		getNoButton().addActionListener(actionListener);
		getCancelButton().addActionListener(actionListener);
	}
	
	public void setKeyListener(KeyListener keyListener){
		if(allowScan)getScanTextBox().addKeyListener(keyListener);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}
	
	public String getSkipLabel(){
		String skipLabel = defaultSkipLabel;
		if(options != null && options.size()>0){
			skipLabel = options.containsKey(defaultSkipLabel)?options.get(defaultSkipLabel):defaultSkipLabel;
		}
		return skipLabel;
	}
	
	public String getRefreshLabel(){
		String refreshLabel = defaultRefreshLabel;
		if(options != null && options.size()>0){
			refreshLabel = options.containsKey(defaultRefreshLabel)?options.get(defaultRefreshLabel):defaultRefreshLabel;
		}
		return refreshLabel;
	}
	
	public String getNoLabel(){
		String noLabel = defaultNoLabel;
		if(options != null && options.size()>0){
			noLabel = options.containsKey(defaultNoLabel)?options.get(defaultNoLabel):defaultNoLabel;
		}
		return noLabel;
	}
	
	public String getYesLabel(){
		String yesLabel = defaultYesLabel;
		if(options != null && options.size()>0){
			yesLabel = options.containsKey(defaultYesLabel)?options.get(defaultYesLabel):defaultYesLabel;
		}
		return yesLabel;
	}
	
	public ButtonOptions getButtonOptions(){
		return buttonOptions;
	}
	
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = UiFactory.getInfo().createButton(getCancelLabel(), true);
			cancelButton.addActionListener(getButtonLogActionListener(getCancelLabel()));
		}

		return cancelButton;
	}
	public String getCancelLabel(){
		String cancelLabel = defaultCancelLabel;
		if(options != null && options.size()>0){
			cancelLabel = options.containsKey(defaultCancelLabel)?options.get(defaultCancelLabel):defaultCancelLabel;
		}
		return cancelLabel;
	}
	
	private ActionListener getButtonLogActionListener(final String buttonLabel) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getLogger().info("Error Dialog - " + buttonLabel + " button clicked");
			}
		};
	}
}
