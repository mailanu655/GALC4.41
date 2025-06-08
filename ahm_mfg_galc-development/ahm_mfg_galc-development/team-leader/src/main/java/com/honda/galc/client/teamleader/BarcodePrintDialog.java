package com.honda.galc.client.teamleader;

import java.awt.*;

public class BarcodePrintDialog extends javax.swing.JDialog  {

	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel ivjJDialogContentPane = null;
	private javax.swing.JButton ivjJButtonOK = null;
	private javax.swing.JLabel ivjOutputMessage = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private java.lang.String ID = null;
	private javax.swing.JPanel ivjButtonArea = null;
	private javax.swing.JPanel ivjMessageArea = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == BarcodePrintDialog.this.getJButtonOK()) 
				connEtoC1(e);
		};
	};
/**
 * BarcodePrintDialog ??
 */
public BarcodePrintDialog() {
	super();
	initialize();
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Dialog
 */
public BarcodePrintDialog(java.awt.Dialog owner) {
	super(owner);
	initialize();
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
public BarcodePrintDialog(java.awt.Dialog owner, String title) {
	super(owner, title);
	initialize();
}
/**
 * Create confirmation dialog with OK button and one message.After call this constructor, developer needs to call show() method to display the dialog.
 * 
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param message java.lang.String
 */
public BarcodePrintDialog(java.awt.Dialog owner, String title, String message) {
	super(owner);

	ID = "BarcodePrintDialog";
	setDialogTitle(title);
	getOutputMessage().setText(message);
	initialize();	

	setWindowWidth(message);

	int iX = (owner.getX() + owner.getWidth()) / 2 - getWidth() / 2;
	int iY = (owner.getY() + owner.getHeight()) / 2 - getHeight() / 2;
	setLocation(new Point(iX, iY));
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
public BarcodePrintDialog(java.awt.Dialog owner, String title, boolean modal) {
	super(owner, title, modal);
	initialize();
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
public BarcodePrintDialog(java.awt.Dialog owner, boolean modal) {
	super(owner, modal);
	initialize();
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Frame
 */
public BarcodePrintDialog(java.awt.Frame owner) {
	super(owner);
	initialize();
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public BarcodePrintDialog(java.awt.Frame owner, String title) {
	super(owner, title);
	initialize();
}
/**
 * Create confirmation dialog with OK button and one message.After call this constructor, developer needs to call show() method to display the dialog.
 * 
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param message java.lang.String
 */
public BarcodePrintDialog(java.awt.Frame owner, String title, String message) {
	super(owner);

	ID = "BarcodePrintDialog";
	setDialogTitle(title);
	getOutputMessage().setText(message);
	initialize();

	setWindowWidth(message);

	int iX = (owner.getX() + owner.getWidth()) / 2 - getWidth() / 2;
	int iY = (owner.getY() + owner.getHeight()) / 2 - getHeight() / 2;
	setLocation(new Point(iX, iY));
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
public BarcodePrintDialog(java.awt.Frame owner, String title, boolean modal) {
	super(owner, title, modal);
	initialize();
}
/**
 * BarcodePrintDialog ??
 * @param owner java.awt.Frame
 * @param modal boolean
 */
public BarcodePrintDialog(java.awt.Frame owner, boolean modal) {
	super(owner, modal);
	initialize();
}
/**
 * \???????????????
 * ??? : (1/29/01 16:52:11)
 * @param title java.lang.String
 * @param message java.lang.String
 */
public BarcodePrintDialog(String title, String message) {
	super();

	ID = "BarcodePrintDialog";
	setDialogTitle(title);
	getOutputMessage().setText(message);
	initialize();
	
	setWindowWidth(message);
}
/**
 * connEtoC1:  (JButtonOK.action.actionPerformed(java.awt.event.ActionEvent) --> BarcodePrintDialog.JButtonOK_ActionPerformed1(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* ?? : ??????????????? */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.JButtonOK_ActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * ButtonArea2 ??????????????
 * @return javax.swing.JPanel
 */
/* ?? : ??????????????? */
private javax.swing.JPanel getButtonArea() {
	if (ivjButtonArea == null) {
		try {
			ivjButtonArea = new javax.swing.JPanel();
			ivjButtonArea.setName("ButtonArea");
			ivjButtonArea.setPreferredSize(new java.awt.Dimension(100, 40));
			ivjButtonArea.setLayout(new java.awt.FlowLayout());
			ivjButtonArea.setMinimumSize(new java.awt.Dimension(90, 30));
			getButtonArea().add(getJButtonOK(), getJButtonOK().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjButtonArea;
}
/**
 * \???????????????
 * ??? : (1/30/01 10:03:13)
 * @return java.lang.String
 */
public java.lang.String getID() {
	return ID;
}
/**
 * JButtonOK ??????????????
 * @return javax.swing.JButton
 */
/* ?? : ??????????????? */
private javax.swing.JButton getJButtonOK() {
	if (ivjJButtonOK == null) {
		try {
			ivjJButtonOK = new javax.swing.JButton();
			ivjJButtonOK.setName("JButtonOK");
			ivjJButtonOK.setPreferredSize(new java.awt.Dimension(90, 30));
			ivjJButtonOK.setFont(new java.awt.Font("dialog", 0, 14));
			ivjJButtonOK.setText("OK");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJButtonOK;
}
/**
 * JDialogContentPane ??????????????
 * @return javax.swing.JPanel
 */
/* ?? : ??????????????? */
private javax.swing.JPanel getJDialogContentPane() {
	if (ivjJDialogContentPane == null) {
		try {
			ivjJDialogContentPane = new javax.swing.JPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
			getJDialogContentPane().add(getMessageArea(), "North");
			getJDialogContentPane().add(getButtonArea(), "South");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane;
}
/**
 * MessageArea2 ??????????????
 * @return javax.swing.JPanel
 */
/* ?? : ??????????????? */
private javax.swing.JPanel getMessageArea() {
	if (ivjMessageArea == null) {
		try {
			ivjMessageArea = new javax.swing.JPanel();
			ivjMessageArea.setName("MessageArea");
			ivjMessageArea.setPreferredSize(new java.awt.Dimension(200, 48));
			ivjMessageArea.setLayout(getMessageAreaCardLayout());
			ivjMessageArea.setMinimumSize(new java.awt.Dimension(1454, 24));
			getMessageArea().add(getOutputMessage(), getOutputMessage().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMessageArea;
}
/**
 * MessageAreaCardLayout ??????????????
 * @return java.awt.CardLayout
 */
/* ?? : ??????????????? */
private java.awt.CardLayout getMessageAreaCardLayout() {
	java.awt.CardLayout ivjMessageAreaCardLayout = null;
	try {
		/* ?????? */ 
		ivjMessageAreaCardLayout = new java.awt.CardLayout();
		ivjMessageAreaCardLayout.setHgap(20);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjMessageAreaCardLayout;
}
/**
 * OutputMessage ??????????????
 * @return javax.swing.JLabel
 */
/* ?? : ??????????????? */
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
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOutputMessage;
}
/**
 * ??????? throw ?????????????
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ?????????????????????????????????????? */
	// System.out.println("--------- ???????????? ---------");
	// exception.printStackTrace(System.out);
}
/**
 * ??????
 * @exception java.lang.Exception ????
 */
/* ?? : ??????????????? */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getJButtonOK().addActionListener(ivjEventHandler);
}
/**
 * ???????????
 */
/* ?? : ??????????????? */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("BarcodePrintDialog");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(380, 120);
		setModal(true);
		setContentPane(getJDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * Comment
 */
public void JButtonOK_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	this.dispose();
	return;
}
/**
 * ??
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		BarcodePrintDialog aBarcodePrintDialog;
		aBarcodePrintDialog = new BarcodePrintDialog();
		aBarcodePrintDialog.setModal(true);
		aBarcodePrintDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		//aBarcodePrintDialog.show();
		java.awt.Insets insets = aBarcodePrintDialog.getInsets();
		aBarcodePrintDialog.setSize(aBarcodePrintDialog.getWidth() + insets.left + insets.right, aBarcodePrintDialog.getHeight() + insets.top + insets.bottom);
		aBarcodePrintDialog.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("javax.swing.JDialog ? main() ??????????");
		exception.printStackTrace(System.out);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (1/2/2003 8:53:14 PM)
 */
public void setAllPanesBackground(Color color) {
	getContentPane().setBackground(color);
	getMessageArea().setBackground(color);
	getButtonArea().setBackground(color);
}
/**
 * \???????????????
 * ??? : (1/29/01 18:47:56)
 * @param stitle java.lang.String
 */
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
/**
 * \???????????????
 * ??? : (1/30/01 10:03:13)
 * @param newID java.lang.String
 */
public void setID(java.lang.String newID) {
	ID = newID;
}
/**
 * set width of window.
 * 
 * @param messageString java.lang.String
 */
private void setWindowWidth(String messageString) {
	int width = 380;
	
	if (messageString == null) {
		setSize(width, 120);
		return;
	}
	java.awt.FontMetrics metrics =  ivjOutputMessage.getFontMetrics(ivjOutputMessage.getFont());
	System.out.println(metrics);
	width = metrics.stringWidth( messageString )+70;
	
	
	if (width < 380) {
		width = 380;
	}
	else if (width > 1024) {
		width = 1024;
	}
	setSize(width, 120);
	return;
}
/*
 * Set the focus to the lable on message box
 * other than the ok button.
 * 
 * this was created by wallace
 * 
 * 
 */
public void setFocus(){
	this.ivjOutputMessage.requestFocus();	
}
}
