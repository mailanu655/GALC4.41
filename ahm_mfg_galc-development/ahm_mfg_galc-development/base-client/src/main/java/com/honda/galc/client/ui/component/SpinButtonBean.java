package com.honda.galc.client.ui.component;

import java.awt.Font;

import javax.swing.event.DocumentListener;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> This class is Java Bean which can be used being able to stick on Visual.<br>
 *     It has created combining the text box and the scroll bar.<br>
 *     The following items can be set up as an initial value.<br>
 *     Max Value.<br>
 *     Minimum Value.<br>
 *     Incremental Value.<br>
 * 	   Initial Value.<br>
 *  <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 *  <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 *  <TH>Updated by</TH>
 *  <TH>Update date</TH>
 *  <TH>Version</TH>
 *  <TH>Mark of Update</TH>
 *  <TH>Reason</TH>
 *  </TR>
 *  <TR>
 *  <TD>K.Ishibe</TD>
 *  <TD>(07/21/01 09:28:07)</TD>
 *  <TD>0.1.0</TD>
 *  <TD>(none)</TD>
 *  <TD>Initial Release</TD>
 *  </TR>
 *  
 *  <TR>
 *  <TD>R.Lasenko</TD>
 *  <TD>Jun 19, 2009</TD>
 *  <TD>&nbsp;</TD>
 *  <TD>@RL060</TD>
 *  <TD>HEAT:01086754 - Make number of printout copies configurable with 1 as default
 *  </TD>
 *  </TR>
 *  
 *  </TABLE>
 * @see
 * @ver 0.1
 * @author K.Ishibe
 */

public class SpinButtonBean extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	Increase and decrease of a value are set up.
	*/
	private int fieldIncNum 	= 1;
	/**
	An Initial value is set up.
	*/
	private int fieldInitNum 	= 0;
	/**
	An Max value is set up.
	*/
	private int fieldMaxNum 	= 999;
	/**
	An Minimum value is set up.
	*/
	private int fieldMinNum 	= 0;
	/**
	The present value is saved.
	*/
	private int fieldNum 		= 0;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JScrollBar ivjJScrollBar1 = null;
	private NumericFieldBean ivjJTextField1 = null;

class IvjEventHandler implements java.awt.event.AdjustmentListener, java.awt.event.KeyListener {
		public void adjustmentValueChanged(java.awt.event.AdjustmentEvent e) {
			if (e.getSource() == SpinButtonBean.this.getJScrollBar1()) 
				connEtoC1(e);
		};
		public void keyPressed(java.awt.event.KeyEvent e) {
			if (e.getSource() == SpinButtonBean.this.getJTextField1()) 
				connEtoC2();
		};
		public void keyReleased(java.awt.event.KeyEvent e) {
			if (e.getSource() == SpinButtonBean.this.getJTextField1()) 
				connEtoC2();
		};
		public void keyTyped(java.awt.event.KeyEvent e) {
			if (e.getSource() == SpinButtonBean.this.getJTextField1()) 
				connEtoC2();
		};
	};
/**
 * SpinButtonBean constructor comment.
 */
public SpinButtonBean() {
	super();
	initialize();
}
/**
 * SpinButtonBean constructor comment.
 * @param layout java.awt.LayoutManager
 */
public SpinButtonBean(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * SpinButtonBean constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public SpinButtonBean(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * SpinButtonBean constructor comment.
 * @param isDoubleBuffered boolean
 */
public SpinButtonBean(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * connEtoC1:  (JScrollBar1.adjustment.adjustmentValueChanged(java.awt.event.AdjustmentEvent) --> SpinButtonBean.jScrollBar1_AdjustmentValueChanged(Ljava.awt.event.AdjustmentEvent;)V)
 * @param arg1 java.awt.event.AdjustmentEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.AdjustmentEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jScrollBar1_AdjustmentValueChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (JTextField1.key. --> SpinButtonBean.jTextField1_KeyEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.jTextField1_KeyEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Gets the incNum property (java.lang.String) value.
 * @return The incNum property value.
 * @see #setIncNum
 */
public int getIncNum() {
	return fieldIncNum;
}
/**
 * Gets the initNum property (int) value.
 * @return The initNum property value.
 * @see #setInitNum
 */
public int getInitNum() {
	return fieldInitNum;
}
/**
 * Return the JScrollBar1 property value.
 * @return javax.swing.JScrollBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollBar getJScrollBar1() {
	if (ivjJScrollBar1 == null) {
		try {
			ivjJScrollBar1 = new javax.swing.JScrollBar();
			ivjJScrollBar1.setName("JScrollBar1");
			ivjJScrollBar1.setUnitIncrement(-1);
			ivjJScrollBar1.setMaximum(2);
			ivjJScrollBar1.setVisibleAmount(0);
			ivjJScrollBar1.setValue(1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollBar1;
}
/**
 * Return the JTextField1 property value.
 * @return com.honda.global.galc.client.common.NumericFieldBean
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private NumericFieldBean getJTextField1() {
	if (ivjJTextField1 == null) {
		try {
			ivjJTextField1 = new NumericFieldBean();
			ivjJTextField1.setName("JTextField1");
			ivjJTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJTextField1;
}
/**
 * Gets the maxNum property (java.lang.String) value.
 * @return The maxNum property value.
 * @see #setMaxNum
 */
public int getMaxNum() {
	return fieldMaxNum;
}
/**
 * Gets the minNum property (java.lang.String) value.
 * @return The minNum property value.
 * @see #setMinNum
 */
public int getMinNum() {
	return fieldMinNum;
}
/**
 * Gets the num property (java.lang.String) value.
 * @return The num property value.
 * @see #setNum
 */
public int getNum() {
	return fieldNum;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getJScrollBar1().addAdjustmentListener(ivjEventHandler);
	getJTextField1().addKeyListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SpinButtonBean");
		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
		setSize(120, 25);
		add(getJTextField1(), getJTextField1().getName());
		add(getJScrollBar1());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	this.fieldNum	=	fieldInitNum;
	ivjJTextField1.setText(String.valueOf(this.fieldNum));
	// user code end
}
/**
 * Comment
 */
public void jScrollBar1_AdjustmentValueChanged(java.awt.event.AdjustmentEvent adjustmentEvent) {
	if(ivjJScrollBar1.getValue() > 1) {
		ivjJTextField1.setText(String.valueOf(this.fieldNum=(this.fieldNum+this.fieldIncNum)<=this.fieldMaxNum?this.fieldNum+=this.fieldIncNum:this.fieldNum));
	} else if(ivjJScrollBar1.getValue() < 1) {
		ivjJTextField1.setText(String.valueOf(this.fieldNum=(this.fieldNum-this.fieldIncNum)>=this.fieldMinNum?this.fieldNum-=this.fieldIncNum:this.fieldNum));
	}
	ivjJScrollBar1.setValue(1);
	return; 
}
/**
 * Comment
 */
public void jTextField1_KeyEvents() {
	if((ivjJTextField1.getText()!=null) && !((ivjJTextField1.getText()).equals(""))) {
		int	tmpNum	=	Integer.parseInt(ivjJTextField1.getText());
		if(fieldMaxNum <= tmpNum) {
			setNum(this.fieldMaxNum);
		} else if(fieldMinNum >= tmpNum) {
			setNum(this.fieldMinNum);
		} else {				
			setNum(Integer.parseInt(ivjJTextField1.getText()));
		}
	}
	return;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		SpinButtonBean aSpinButtonBean;
		aSpinButtonBean = new SpinButtonBean();
		frame.setContentPane(aSpinButtonBean);
		frame.setSize(aSpinButtonBean.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JPanel");
		exception.printStackTrace(System.out);
	}
}
/**
 * Sets the incNum property (java.lang.String) value.
 * @param incNum The new value for the property.
 * @see #getIncNum
 */
public void setIncNum(int incNum) {
	int oldValue = fieldIncNum;
	fieldIncNum = incNum;
	firePropertyChange("incNum", oldValue, incNum);
}
/**
 * Sets the initNum property (int) value.
 * @param initNum The new value for the property.
 * @see #getInitNum
 */
public void setInitNum(int initNum) {
	int oldValue = fieldInitNum;
	fieldInitNum = initNum;
	firePropertyChange("initNum", new Integer(oldValue), new Integer(initNum));
	this.fieldNum	=	fieldInitNum;
	ivjJTextField1.setText(String.valueOf(this.fieldNum));
}
/**
 * Sets the maxNum property (java.lang.String) value.
 * @param maxNum The new value for the property.
 * @see #getMaxNum
 */
public void setMaxNum(int maxNum) {
	int oldValue = fieldMaxNum;
	fieldMaxNum = maxNum;
	firePropertyChange("maxNum", oldValue, maxNum);
}
/**
 * Sets the minNum property (java.lang.String) value.
 * @param minNum The new value for the property.
 * @see #getMinNum
 */
public void setMinNum(int minNum) {
	int oldValue = fieldMinNum;
	fieldMinNum = minNum;
	firePropertyChange("minNum", oldValue, minNum);
}
/**
 * Sets the num property (java.lang.String) value.
 * @param num The new value for the property.
 * @see #getNum
 */
public void setNum(int num) {
	int oldValue = fieldNum;
	fieldNum = num;
	firePropertyChange("num", oldValue, num);
}

	public void reset() {
		setNum(0);
		getJTextField1().setText(String.valueOf(0));
	}

	@Override
	public void setFont(Font font) {
		getJTextField1().setFont(font);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		getJTextField1().setEnabled(enabled);
		getJScrollBar1().setEnabled(enabled);
	}
	/**
	 * Control enablement of the text field
	 * @param enabled
	 */
	// @RL060
	public void setTextEnabled(boolean enabled) {
		getJTextField1().setEnabled(enabled);
	}
	
	public void addDocumentListener(DocumentListener listener) {
		getJTextField1().getDocument().addDocumentListener(listener);
	}
}
