package com.honda.galc.client.teamleader.qics.iqscatregressionmode;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Cursor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.dao.qics.RegressionDao;
import com.honda.galc.entity.qics.Regression;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 30, 2014
 */
public class RegressionDialog extends JDialog implements java.awt.event.ActionListener, java.awt.event.KeyListener {

	private static final long serialVersionUID = 1L;
	private JButton addRegressionBtn = null;
	private JButton cancelRegressionBtn = null;
	private JPanel regressionDialogPanel = null;
	private JLabel regressionCodeLbl = null;
	private LengthFieldBean regressionCodeLengthBean = null;
	private IQSCatRegressionModeMaintPanel parentWindow = null;
	private boolean isCancel = false;		

	public RegressionDialog() {
		super();
		initialize();
	}

	public RegressionDialog(IQSCatRegressionModeMaintPanel owner) {
		super();
		parentWindow = owner;
		initialize();
		startDialog();
	}

	public void checkTextField() {
		if(!(getRegressionCodeLengthBean().getText() == null) && !(getRegressionCodeLengthBean().getText().equals(""))) {
			getAddRegressionBtn().setEnabled(true);
		} else {
			getAddRegressionBtn().setEnabled(false);
		}		
		return;
	}

	public void add() {	
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Regression regression=new Regression(getRegressionCodeLengthBean().getText());
		getDao(RegressionDao.class).save(regression);	
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		isCancel = false;
		this.dispose();	
		return;
	}

	public void cancel() {	
		isCancel = true;
		this.dispose();
		return;
	}

	private JButton getAddRegressionBtn() {
		if (addRegressionBtn == null) {
			try {
				addRegressionBtn = new JButton();
				addRegressionBtn.setName("addRegressionBtn");
				addRegressionBtn.setFont(new java.awt.Font("dialog", 0, 18));
				addRegressionBtn.setText("Add");
				addRegressionBtn.setBounds(50, 75, 98, 25);
				addRegressionBtn.setEnabled(false);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return addRegressionBtn;
	}

	private JButton getCancelRegressionBtn() {
		if (cancelRegressionBtn == null) {
			try {
				cancelRegressionBtn = new JButton();
				cancelRegressionBtn.setName("cancelRegressionBtn");
				cancelRegressionBtn.setFont(new java.awt.Font("dialog", 0, 18));
				cancelRegressionBtn.setText("Cancel");
				cancelRegressionBtn.setBounds(199, 75, 98, 25);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return cancelRegressionBtn;
	}

	private JPanel getRegressionDialogPanel() {
		if (regressionDialogPanel == null) {
			try {
				regressionDialogPanel = new JPanel();
				regressionDialogPanel.setName("regressionDialogPanel");
				regressionDialogPanel.setLayout(null);
				getRegressionDialogPanel().add(getRegressionCodeLbl(), getRegressionCodeLbl().getName());
				getRegressionDialogPanel().add(getRegressionCodeLengthBean(), getRegressionCodeLengthBean().getName());
				getRegressionDialogPanel().add(getAddRegressionBtn(), getAddRegressionBtn().getName());
				getRegressionDialogPanel().add(getCancelRegressionBtn(), getCancelRegressionBtn().getName());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return regressionDialogPanel;
	}

	private JLabel getRegressionCodeLbl() {
		if (regressionCodeLbl == null) {
			try {
				regressionCodeLbl = new JLabel();
				regressionCodeLbl.setName("regressionCodeLbl");
				regressionCodeLbl.setFont(new java.awt.Font("dialog", 0, 18));
				regressionCodeLbl.setText("Regression Code");
				regressionCodeLbl.setBounds(16, 20, 158, 28);
				regressionCodeLbl.setForeground(java.awt.Color.black);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return regressionCodeLbl;
	}


	private LengthFieldBean getRegressionCodeLengthBean() {
		if (regressionCodeLengthBean == null) {
			try {
				regressionCodeLengthBean = new LengthFieldBean();
				regressionCodeLengthBean.setName("regressionCodeUpperCaseBean");
				regressionCodeLengthBean.setFont(new java.awt.Font("dialog", 0, 14));
				regressionCodeLengthBean.setBounds(177, 20, 191, 25);
				regressionCodeLengthBean.setMaximumLength(32);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return regressionCodeLengthBean;
	}
	private void handleException(Exception e) {
		if(e == null) 
			parentWindow.clearMessage();
		else {
			parentWindow.getLogger().error(e, "unexpected exception occurs: " + e.getMessage());
			parentWindow.setError(e.getMessage());	
		}
	}

	private void initConnections() throws java.lang.Exception {
		getRegressionCodeLengthBean().addKeyListener(this);
		getAddRegressionBtn().addActionListener(this);
		getCancelRegressionBtn().addActionListener(this);
	}

	private void initialize() {
		try {
			setName("Add Regression");
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle("Add Regression");
			setModal(true);
			setSize(380, 133);
			setResizable(false);
			setContentPane(getRegressionDialogPanel());
			initConnections();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource().equals(getAddRegressionBtn())) 
			add();
		if (e.getSource().equals(getCancelRegressionBtn()) )
			cancel();
	};
	public void keyPressed(java.awt.event.KeyEvent e) {};
	public void keyReleased(java.awt.event.KeyEvent e) {
		if (e.getSource().equals(getRegressionCodeLengthBean())) 
			checkTextField();
	};
	public void keyTyped(java.awt.event.KeyEvent e) {};

	public void startDialog() {	
		setLocationRelativeTo(parentWindow);
		this.setVisible(true);
		return;
	}
}
