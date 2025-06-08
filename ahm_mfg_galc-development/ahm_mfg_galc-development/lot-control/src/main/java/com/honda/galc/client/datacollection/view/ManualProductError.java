package com.honda.galc.client.datacollection.view;
import java.awt.*;

public class ManualProductError extends javax.swing.JDialog {
	private javax.swing.JPanel ivjJDialogContentPane = null;
	private javax.swing.JButton ivjJButtonOK = null;
	private javax.swing.JLabel ivjOutputMessage = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private java.lang.String ID = null;
	private javax.swing.JPanel ivjButtonArea = null;
	private javax.swing.JPanel ivjMessageArea = null;
	private java.awt.CardLayout ivjMessageAreaCardLayout = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ManualProductError.this.getJButtonOK()) 
				connEtoC1(e);
		};
	};

public ManualProductError() {
	super();
	initialize();
}

public ManualProductError(java.awt.Dialog owner) {
	super(owner);
	initialize();
}

public ManualProductError(java.awt.Dialog owner, String title) {
	super(owner, title);
	initialize();
}

public ManualProductError(java.awt.Dialog owner, String title, String message) {
	super(owner);

	ID = "SCOM010";
	setDialogTitle(title);
	getOutputMessage().setText(message);
	initialize();	

	setWindowWidth(message);

	int iX = (owner.getX() + owner.getWidth()) / 2 - getWidth() / 2;
	int iY = (owner.getY() + owner.getHeight()) / 2 - getHeight() / 2;
	setLocation(new Point(iX, iY));
}

public ManualProductError(java.awt.Dialog owner, String title, boolean modal) {
	super(owner, title, modal);
	initialize();
}

public ManualProductError(java.awt.Dialog owner, boolean modal) {
	super(owner, modal);
	initialize();
}

public ManualProductError(java.awt.Frame owner) {
	super(owner);
	initialize();
}

public ManualProductError(java.awt.Frame owner, String title) {
	super(owner, title);
	initialize();
}

public ManualProductError(java.awt.Frame owner, String title, String message) {
	super(owner);

	ID = "SCOM010";
	setDialogTitle(title);
	getOutputMessage().setText(message);
	initialize();

	setWindowWidth(message);

	int iX = (owner.getX() + owner.getWidth()) / 2 - getWidth() / 2;
	int iY = (owner.getY() + owner.getHeight()) / 2 - getHeight() / 2;
	setLocation(new Point(iX, iY));
}

public ManualProductError(java.awt.Frame owner, String title, boolean modal) {
	super(owner, title, modal);
	initialize();
}

public ManualProductError(java.awt.Frame owner, boolean modal) {
	super(owner, modal);
	initialize();
}

public ManualProductError(String title, String message) {
	super();

	ID = "SCOM010";
	setDialogTitle(title);
	getOutputMessage().setText(message);
	initialize();
	
	setWindowWidth(message);
}

private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		
		this.JButtonOK_ActionPerformed(arg1);
		
	} catch (java.lang.Throwable ivjExc) {
		
		handleException(ivjExc);
	}
}

private javax.swing.JPanel getButtonArea() {
	if (ivjButtonArea == null) {
		try {
			ivjButtonArea = new javax.swing.JPanel();
			ivjButtonArea.setName("ButtonArea");
			ivjButtonArea.setPreferredSize(new java.awt.Dimension(100, 40));
			ivjButtonArea.setMinimumSize(new java.awt.Dimension(90, 30));
			getButtonArea().add(getJButtonOK(), getJButtonOK().getName());
			
		} catch (java.lang.Throwable ivjExc) {
			
			handleException(ivjExc);
		}
	}
	return ivjButtonArea;
}

public java.lang.String getID() {
	return ID;
}

private javax.swing.JButton getJButtonOK() {
	if (ivjJButtonOK == null) {
		try {
			ivjJButtonOK = new javax.swing.JButton();
			ivjJButtonOK.setName("JButtonOK");
			ivjJButtonOK.setPreferredSize(new java.awt.Dimension(90, 30));
			ivjJButtonOK.setFont(new java.awt.Font("dialog", 0, 14));
			ivjJButtonOK.setText("OK");
			
		} catch (java.lang.Throwable ivjExc) {
			
			handleException(ivjExc);
		}
	}
	return ivjJButtonOK;
}

private javax.swing.JPanel getJDialogContentPane() {
	if (ivjJDialogContentPane == null) {
		try {
			ivjJDialogContentPane = new javax.swing.JPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
			getJDialogContentPane().add(getMessageArea(), "North");
			getJDialogContentPane().add(getButtonArea(), "South");
			
		} catch (java.lang.Throwable ivjExc) {
			
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane;
}

private javax.swing.JPanel getMessageArea() {
	if (ivjMessageArea == null) {
		try {
			ivjMessageArea = new javax.swing.JPanel();
			ivjMessageArea.setName("MessageArea");
			ivjMessageArea.setPreferredSize(new java.awt.Dimension(200, 40));
			ivjMessageArea.setLayout(getMessageAreaCardLayout());
			ivjMessageArea.setMinimumSize(new java.awt.Dimension(1454, 24));
			getMessageArea().add(getOutputMessage(), getOutputMessage().getName());
			
		} catch (java.lang.Throwable ivjExc) {
			
			handleException(ivjExc);
		}
	}
	return ivjMessageArea;
}

private java.awt.CardLayout getMessageAreaCardLayout() {
	java.awt.CardLayout ivjMessageAreaCardLayout = null;
	try {
	 
		ivjMessageAreaCardLayout = new java.awt.CardLayout();
		ivjMessageAreaCardLayout.setHgap(20);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjMessageAreaCardLayout;
}

private javax.swing.JLabel getOutputMessage() {
	if (ivjOutputMessage == null) {
		try {
			ivjOutputMessage = new javax.swing.JLabel();
			ivjOutputMessage.setName("OutputMessage");
			ivjOutputMessage.setOpaque(false);
			ivjOutputMessage.setText("");
			ivjOutputMessage.setMaximumSize(new java.awt.Dimension(1454, 24));
			ivjOutputMessage.setForeground(new java.awt.Color(0,0,0));
			ivjOutputMessage.setPreferredSize(new java.awt.Dimension(380, 20));
			ivjOutputMessage.setFont(new java.awt.Font("dialog", 0, 18));
			ivjOutputMessage.setMinimumSize(new java.awt.Dimension(1454, 24));
			
		} catch (java.lang.Throwable ivjExc) {
			
			handleException(ivjExc);
		}
	}
	return ivjOutputMessage;
}

private void handleException(java.lang.Throwable exception) {

	
}

private void initConnections() throws java.lang.Exception {
	
	getJButtonOK().addActionListener(ivjEventHandler);
}

private void initialize() {
	try {
		
		setName("SCOM010");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(380, 120);
		setModal(true);
		setContentPane(getJDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	
}

public void JButtonOK_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	this.dispose();
	return;
}

public static void main(java.lang.String[] args) {
	try {
		ManualProductError aSCOM010;
		aSCOM010 = new ManualProductError();
		aSCOM010.setModal(true);
		aSCOM010.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		//aSCOM010.show();
		java.awt.Insets insets = aSCOM010.getInsets();
		aSCOM010.setSize(aSCOM010.getWidth() + insets.left + insets.right, aSCOM010.getHeight() + insets.top + insets.bottom);
		aSCOM010.show();
	} catch (Throwable exception) {
		System.err.println("javax.swing.JDialog ? main() ??????????");
		exception.printStackTrace(System.out);
	}
}

public void setAllPanesBackground(Color color) {
	getContentPane().setBackground(color);
	getMessageArea().setBackground(color);
	getButtonArea().setBackground(color);
}

public void setDialogTitle(String stitle) {
	String TitleStr = new String();
	if (ID == null) {
		TitleStr = " : ";
	}
	else {
		TitleStr = ID + " : ";
	}
	if (stitle != null) {
		TitleStr = TitleStr + stitle;
	}
	setTitle(TitleStr);
}

public void setID(java.lang.String newID) {
	ID = newID;
}

private void setWindowWidth(String messageString) {
	int width = 380;
	
	if (messageString == null) {
		setSize(width, 120);
		return;
	}
	java.awt.FontMetrics metrics =  ivjOutputMessage.getFontMetrics(ivjOutputMessage.getFont());
	System.out.println(metrics);
	width = metrics.stringWidth( messageString )+50;
	
	
	if (width < 380) {
		width = 380;
	}
	else if (width > 1024) {
		width = 1024;
	}
	setSize(width, 120);
	return;
}

public void setFocus(){
	this.ivjOutputMessage.requestFocus();	
}
}
