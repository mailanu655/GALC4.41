package com.honda.galc.client.teamleader.let;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.MessageType;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.device.let.HandHeldDeviceXmlSendThread;
import com.honda.galc.entity.enumtype.UploadStatusType;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ExtensionFileFilter;

public class LoadXmlFileDialog extends JDialog implements ActionListener {
	
	private File xmlFile;

	private static final long serialVersionUID = 1L;
	private static final Font font = new Font("Verdana", Font.BOLD, 12);

	private JTextField jHostTxtField;
	private JTextField jPortTxtField;
	private JTextField jMfgIDTxtField;
	private JTextField jTargetTxtField;
	private JTextField jTerminalTxtField;
	private JTextField jDataTxtField;
	private JProgressBar jProgressBar;
	private JPanel jPanel;
	
	private JLabel jHostLbl;
	private JLabel jPortLbl;
	private JLabel jMfgIDLbl;
	private JLabel jTargetLbl;
	private JLabel jTerminalLbl;
	private JLabel jMessageLbl;
	private JLabel jDataLbl;
	private JLabel jProgressBarLbl;
	
	private JButton jSendBtn;
	private JButton jDoneBtn;
	private JButton jBrowseBtn;
	
	private JComboBox  jMessageComboBox = null;
	private int _port = 9000;					// default port
	private Socket _socket = null;
	private MainWindow window;
	private int portNumber;
	private String uploadStatus  ="";
	
	public LoadXmlFileDialog(Frame owner,MainWindow window,int portNumber ,String title, String message) {
		super(owner, title, true);
		this.window=window;
		this.portNumber = portNumber;
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		getMainWindow().addWindowListener(new WindowAdapter() {			
			public void windowOpened(WindowEvent e) {}
		});	
		init();
	}

	public LoadXmlFileDialog() {
		super();
		init();
	}
	
	private MainWindow getMainWindow() {
		return window;
	}

	private void init() {
		setAlwaysOnTop(true);
		setSize(800, 250);
		initLayout();
	}

	private void initLayout() {
		jPanel = new JPanel();
		jPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		jHostLbl = new JLabel("Host");
		jHostLbl.setFont(font);
		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 20, 0);
		c.weightx = 0;
		jPanel.add(jHostLbl, c);

		jHostTxtField = new JTextField(10);
		jHostTxtField.setText(String.valueOf(getLetPropertyBean().getHostName()));
		c.gridheight = 2;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 60, 20, 0);
		jPanel.add(jHostTxtField, c);

		jPortLbl = new JLabel("Port");
		jPortLbl.setFont(font);
		c = new GridBagConstraints();
		c.gridheight = 2;
		c.gridx = 3;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 20, 0);
		c.weightx = 0;
		jPanel.add(jPortLbl, c);

		jPortTxtField = new JTextField(10);
		jPortTxtField.setText(String.valueOf(portNumber));
		c.gridheight = 2;
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 60, 20, 0);
		jPanel.add(jPortTxtField, c);

		jMfgIDLbl = new JLabel("MfgID");
		jMfgIDLbl.setFont(font);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.insets = new Insets(0, 20, 10, 0);
		jPanel.add(jMfgIDLbl, c);

		jMfgIDTxtField = new JTextField(10);
		jMfgIDTxtField.setText(String.valueOf(getLetPropertyBean().getDefaultMfgId()));
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 60, 10, 0);
		jPanel.add(jMfgIDTxtField, c);

		jTargetLbl = new JLabel("Target/Source");
		jTargetLbl.setFont(font);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 10, 0);
		c.weightx = 0.2;
		jPanel.add(jTargetLbl, c);

		jTargetTxtField = new JTextField(20);
		jTargetTxtField.setText(String.valueOf(getLetPropertyBean().getDefaultUploadTarget()));
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 120, 10, 0);
		jPanel.add(jTargetTxtField, c);

		jMessageLbl = new JLabel("Message");
		jMessageLbl.setFont(font);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridx = 3;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 10, 0);
		c.weightx = 0.2;
		jPanel.add(jMessageLbl, c);

		jMessageComboBox = new JComboBox();
		jMessageComboBox.addItem(MessageType.DIAG.toString());
		jMessageComboBox.addItem(MessageType.TRES.toString());
		jMessageComboBox.addItem(MessageType.RINP.toString());
		jMessageComboBox.setSelectedIndex(1);
		c.gridheight = 1;
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 80, 10, 0);
		jPanel.add(jMessageComboBox, c);

		jTerminalLbl = new JLabel("Terminal");
		jTerminalLbl.setFont(font);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 10, 0);
		c.weightx = 0.2;
		jPanel.add(jTerminalLbl, c);

		jTerminalTxtField = new JTextField(20);
		jTerminalTxtField.setText(String.valueOf(getLetPropertyBean().getDefaultUploadTerminal()));
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 80, 10, 0);
		jPanel.add(jTerminalTxtField, c);

		jDataLbl = new JLabel("data(XML)");
		jDataLbl.setFont(font);

		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.insets = new Insets(0, 20, 10, 0);
		jPanel.add(jDataLbl, c);

		jDataTxtField = new JTextField(20);
		jDataTxtField.setText(String.valueOf(getLetPropertyBean().getDefaultUploadXmlLocation()));
		c.gridheight = 1;
		c.gridwidth = 3;
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 0.2;
		c.insets = new Insets(0, 0, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		jPanel.add(jDataTxtField, c);

		jBrowseBtn = getBrowseButton();
		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.insets = new Insets(0, 10, 10, 0);
		jPanel.add(jBrowseBtn, c);

		jSendBtn = getSendButton();
		c.gridheight = 1;
		c.gridx = 5;
		c.gridy = 3;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 20, 10, 0);
		jPanel.add(jSendBtn, c);
		add(jPanel);
		
		jDoneBtn = getDoneButton();
		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridx = 6;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.insets = new Insets(0, 10, 10, 0);
		jPanel.add(jDoneBtn, c);
		
		jProgressBarLbl = new JLabel("File sending and processing in Progress.......");
		jProgressBarLbl.setFont(font);
		jProgressBarLbl.setVisible(false);
		c = new GridBagConstraints();
		c.gridheight = 1;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.insets = new Insets(0, 10, 10, 0);
		jPanel.add(jProgressBarLbl, c);
		
		jProgressBar = getProgressBar();
		c = new GridBagConstraints();
		c.gridheight = 2;
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.2;
		c.insets = new Insets(0, 10, 10, 0);
		jPanel.add(jProgressBar, c);
	}

	public JProgressBar getProgressBar() {
		if(jProgressBar == null){
			jProgressBar = new JProgressBar(0, 100);
			jProgressBar.setSize(200, 35);
			jProgressBar.setOpaque(true);
			jProgressBar.setVisible(false);
			jProgressBar.setStringPainted(true);
			jProgressBar.setForeground(Color.GREEN);
			jProgressBar.setFont(new Font("Dialog", Font.BOLD, 15));

		}
		return jProgressBar;
	}

	private JButton getSendButton() {
		jSendBtn = new JButton("Send");
		jSendBtn.setFont(font);
		jSendBtn.setLocation(140, 10);
		jSendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSend();
			}
		});
		return jSendBtn;
	}
	
	public void onSend() {
		setUploadStatus(UploadStatusType.PASS.toString());
		final String filePath = jDataTxtField.getText();
		String portNumber = jPortTxtField.getText();
		String hostName = jHostTxtField.getText();
		final String header = jMfgIDTxtField.getText()+jTargetTxtField.getText()+jTerminalTxtField.getText()+jMessageComboBox.getSelectedItem(); 
		if(header.length()!=13){
			JOptionPane.showMessageDialog(this, "Total Header should be equal to 13 characters");

		}else if(StringUtils.isEmpty(filePath)){
			JOptionPane.showMessageDialog(this, "Please select a file to upload");
		}else if(StringUtils.isEmpty(portNumber)){
			JOptionPane.showMessageDialog(this, "Please enter port number");
		}else if(StringUtils.isEmpty(hostName)){
			JOptionPane.showMessageDialog(this, "please enter host name");
		}
		else{
			
			try {
				HandHeldDeviceXmlSendThread connectionThread = new HandHeldDeviceXmlSendThread( getClientSocket(),header.trim(),filePath.trim());
				connectionThread.start();
			} catch (Exception e) {
				setUploadStatus(UploadStatusType.FAIL.toString());
				LoadXmlFileDialog.this.dispose();
			}
			if(uploadStatus.equalsIgnoreCase(UploadStatusType.PASS.toString())){
				jProgressBarLbl.setVisible(true);
				jProgressBar.setVisible(true);
				jSendBtn.setEnabled(false);
				jDoneBtn.setEnabled(false);
				new Thread(new Runnable(){
					public void run(){
						int x = 0;
						while(x<=100) {
							x++;
							jProgressBar.setValue(x);       
							try{
								Thread.sleep(150);
							}catch(Exception ex){ }
							if (x >=  100 ){
								jProgressBarLbl.setVisible(false);
								jProgressBar.setVisible(false);
								jSendBtn.setEnabled(true);
								jDoneBtn.setEnabled(true);

							}
						}
					}
				}).start();
			}
			
		}
	}
	
	public Socket getSocket() {
		return _socket;
	}
	
	private Socket getClientSocket() {
		if (_socket == null || _socket.isClosed() || !_socket.isConnected()) {
			try {
				_port = Integer.parseInt(jPortTxtField.getText().trim());
				_socket = TCPSocketFactory.getSocket(jHostTxtField.getText().trim(), _port, 2000);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _socket;
	}
	
	private JButton getBrowseButton() {
		jBrowseBtn = new JButton("Browse");
		jBrowseBtn.setFont(font);
		jBrowseBtn.setLocation(140, 10);
		jBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSelection();
			}
		});
		return jBrowseBtn;
	}
	
	private JButton getDoneButton() {
		jDoneBtn = new JButton("Done");
		jDoneBtn.setFont(font);
		jDoneBtn.setLocation(140, 10);
		jDoneBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoadXmlFileDialog.this.dispose();
			}
		});
		return jDoneBtn;
	}
	
	private void fileSelection() {
		JFileChooser fc = new JFileChooser("");
		 FileFilter filter = new ExtensionFileFilter("XML","xml");
		fc.setFileFilter(filter);
		fc.addChoosableFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		fc.requestFocus();
		fc.requestFocusInWindow();
		int returnVal  = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			xmlFile = fc.getSelectedFile();
			jDataTxtField.setText(xmlFile.getAbsolutePath());
		}
	}

	public void actionPerformed(ActionEvent arg0) {}
	
	public static LetPropertyBean getLetPropertyBean() {
		return PropertyService.getPropertyBean(LetPropertyBean.class);
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	
	
	
	
	
}
