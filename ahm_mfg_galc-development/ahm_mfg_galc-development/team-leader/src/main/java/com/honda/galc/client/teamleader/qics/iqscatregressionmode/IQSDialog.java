package com.honda.galc.client.teamleader.qics.iqscatregressionmode;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Cursor;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.dao.qics.IqsDao;
import com.honda.galc.entity.qics.Iqs;
import com.honda.galc.entity.qics.IqsId;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 30, 2014
 */
public class IQSDialog extends JDialog implements java.awt.event.ActionListener, java.awt.event.KeyListener {

	private static final long serialVersionUID = 1L;
	private JButton iqsAddBtn = null;
	private JButton iqsCancelBtn = null;
	private JLabel iqsCategoryNameLbl = null;
	private JLabel iqsCoefficientLbl = null;
	private JLabel iqsItemNameLbl = null;
	private LengthFieldBean iqsCoefficientLengthBean = null;
	private LengthFieldBean iqsCategoryLengthBean = null;
	private LengthFieldBean iqsItemNameLengthBean = null;
	private JPanel iqsDialogPanel = null;
	private IQSCatRegressionModeMaintPanel parentWindow = null;
	private boolean isEdit = false;
	private boolean isCancel = true;


	public IQSDialog() {
		super();
		initialize();
	}

	public IQSDialog(IQSCatRegressionModeMaintPanel owner) {
		super();
		parentWindow = owner;
		isEdit = false;	
		initialize();
		startDialog(null);
	}

	public IQSDialog(IQSCatRegressionModeMaintPanel owner,Iqs iqs) {
		super();
		parentWindow = owner;
		isEdit = true;	
		initialize();
		startDialog(iqs);

	}

	private void checkTextFields() {
		if(iqsCategoryLengthBean.getText().trim().equals("") || iqsItemNameLengthBean.getText().trim().equals("") || iqsCoefficientLengthBean.getText().trim().equals("")) {
			iqsAddBtn.setEnabled(false);
		}else{
			iqsAddBtn.setEnabled(true);
		}
		return;
	}

	private void cancel() {
		try {
			isCancel = true;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			this.dispose();

		} catch (Exception e) {
			handleException(e);
		}
	}

	private void edit() {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));	
			IqsId id=new IqsId(getIqsCategoryLengthBean().getText(), getIqsItemNameLengthBean().getText());
			Iqs iqs=new Iqs(id,getCoefficientLengthBean().getText());
			if (isEdit == true)
			{
				getDao(IqsDao.class).update(iqs);
			}
			else
			{
				getDao(IqsDao.class).save(iqs);
			}
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			isCancel = false;
			this.dispose();
		} catch (Exception e) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			handleException(e);
		}
	}

	private JButton getJButtonAdd() {
		if (iqsAddBtn == null) {
			try {
				iqsAddBtn = new JButton();
				iqsAddBtn.setName("JButtonAdd");
				iqsAddBtn.setText("Edit");
				iqsAddBtn.setMaximumSize(new java.awt.Dimension(59, 25));
				iqsAddBtn.setActionCommand("Add IQS");
				iqsAddBtn.setFont(new java.awt.Font("dialog", 0, 18));
				iqsAddBtn.setBounds(90, 140, 170, 25);
				iqsAddBtn.setMinimumSize(new java.awt.Dimension(59, 25));
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsAddBtn;
	}

	private JButton getJButtonCancel() {
		if (iqsCancelBtn == null) {
			try {
				iqsCancelBtn = new JButton();
				iqsCancelBtn.setName("JButtonCancel");
				iqsCancelBtn.setText("Cancel");
				iqsCancelBtn.setMaximumSize(new java.awt.Dimension(81, 25));
				iqsCancelBtn.setActionCommand("CANCEL");
				iqsCancelBtn.setFont(new java.awt.Font("dialog", 0, 18));
				iqsCancelBtn.setBounds(370, 140, 170, 25);
				iqsCancelBtn.setMinimumSize(new java.awt.Dimension(81, 25));
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsCancelBtn;
	}

	private JLabel getIqsCategoryNameLbl() {
		if (iqsCategoryNameLbl == null) {
			try {
				iqsCategoryNameLbl = new JLabel();
				iqsCategoryNameLbl.setName("iqsCategoryNameLbl");
				iqsCategoryNameLbl.setText("IQS Category");
				iqsCategoryNameLbl.setMaximumSize(new java.awt.Dimension(76, 16));
				iqsCategoryNameLbl.setForeground(new java.awt.Color(0,0,0));
				iqsCategoryNameLbl.setFont(new java.awt.Font("dialog", 0, 18));
				iqsCategoryNameLbl.setBounds(10, 20, 150, 20);
				iqsCategoryNameLbl.setMinimumSize(new java.awt.Dimension(76, 16));
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsCategoryNameLbl;
	}

	private JLabel getIqsCoefficientLbl() {
		if (iqsCoefficientLbl == null) {
			try {
				iqsCoefficientLbl = new JLabel();
				iqsCoefficientLbl.setName("iqsCoefficientLbl");
				iqsCoefficientLbl.setText("IQS Coefficient");
				iqsCoefficientLbl.setMaximumSize(new java.awt.Dimension(85, 16));
				iqsCoefficientLbl.setForeground(new java.awt.Color(0,0,0));
				iqsCoefficientLbl.setFont(new java.awt.Font("dialog", 0, 18));
				iqsCoefficientLbl.setBounds(10, 88, 150, 20);
				iqsCoefficientLbl.setMinimumSize(new java.awt.Dimension(85, 16));
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsCoefficientLbl;
	}

	private JLabel getIqsItemNameLbl() {
		if (iqsItemNameLbl == null) {
			try {
				iqsItemNameLbl = new JLabel();
				iqsItemNameLbl.setName("iqsItemNameLbl");
				iqsItemNameLbl.setText("IQS Item");
				iqsItemNameLbl.setMaximumSize(new java.awt.Dimension(51, 16));
				iqsItemNameLbl.setForeground(new java.awt.Color(0,0,0));
				iqsItemNameLbl.setFont(new java.awt.Font("dialog", 0, 18));
				iqsItemNameLbl.setBounds(10, 54, 150, 20);
				iqsItemNameLbl.setMinimumSize(new java.awt.Dimension(51, 16));
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsItemNameLbl;
	}

	private LengthFieldBean getCoefficientLengthBean() {
		if (iqsCoefficientLengthBean == null) {
			try {
				iqsCoefficientLengthBean = new LengthFieldBean();
				iqsCoefficientLengthBean.setName("LengthFieldBeanCoefficient");
				iqsCoefficientLengthBean.setBackground(java.awt.Color.white);
				iqsCoefficientLengthBean.setFixedLength(false);
				iqsCoefficientLengthBean.setForeground(java.awt.Color.black);
				iqsCoefficientLengthBean.setMaximumLength(30);
				iqsCoefficientLengthBean.setFont(new java.awt.Font("dialog", 0, 14));
				iqsCoefficientLengthBean.setColor(java.awt.Color.white);
				iqsCoefficientLengthBean.setBounds(170, 88, 432, 25);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsCoefficientLengthBean;
	}

	private JPanel getIqsDialogPanel() {
		if (iqsDialogPanel == null) {
			try {
				iqsDialogPanel = new JPanel();
				iqsDialogPanel.setName("iqsDialogPanel");
				iqsDialogPanel.setLayout(null);
				iqsDialogPanel.setMinimumSize(new java.awt.Dimension(0, 0));
				getIqsDialogPanel().add(getIqsCategoryNameLbl(), getIqsCategoryNameLbl().getName());
				getIqsDialogPanel().add(getIqsItemNameLbl(), getIqsItemNameLbl().getName());
				getIqsDialogPanel().add(getIqsCoefficientLbl(), getIqsCoefficientLbl().getName());
				getIqsDialogPanel().add(getIqsCategoryLengthBean(), getIqsCategoryLengthBean().getName());
				getIqsDialogPanel().add(getIqsItemNameLengthBean(), getIqsItemNameLengthBean().getName());
				getIqsDialogPanel().add(getCoefficientLengthBean(), getCoefficientLengthBean().getName());
				getIqsDialogPanel().add(getJButtonAdd(), getJButtonAdd().getName());
				getIqsDialogPanel().add(getJButtonCancel(), getJButtonCancel().getName());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsDialogPanel;
	}

	private LengthFieldBean getIqsCategoryLengthBean() {
		if (iqsCategoryLengthBean == null) {
			try {
				iqsCategoryLengthBean = new LengthFieldBean();
				iqsCategoryLengthBean.setName("iqsCategoryLengthBean");
				iqsCategoryLengthBean.setFont(new java.awt.Font("dialog", 0, 14));
				iqsCategoryLengthBean.setText("");
				iqsCategoryLengthBean.setBounds(170, 18, 432, 25);
				iqsCategoryLengthBean.setMaximumLength(32);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsCategoryLengthBean;
	}

	private LengthFieldBean getIqsItemNameLengthBean() {
		if (iqsItemNameLengthBean == null) {
			try {
				iqsItemNameLengthBean = new LengthFieldBean();
				iqsItemNameLengthBean.setName("iqsItemNameLengthBean");
				iqsItemNameLengthBean.setFont(new java.awt.Font("dialog", 0, 14));
				iqsItemNameLengthBean.setBounds(170, 54, 432, 25);
				iqsItemNameLengthBean.setMaximumLength(64);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return iqsItemNameLengthBean;
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
		getJButtonCancel().addActionListener(this);
		getIqsCategoryLengthBean().addKeyListener(this);
		getIqsItemNameLengthBean().addKeyListener(this);
		getCoefficientLengthBean().addKeyListener(this);
		getJButtonAdd().addActionListener(this);
	}

	private void initialize() {
		try {
			setName("IQS Add/Update");
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle("IQS Add/Update");
			setModal(true);
			setSize(650, 240);
			setResizable(false);
			setContentPane(getIqsDialogPanel());
			initConnections();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public boolean isCancel() {
		return isCancel;
	}

	private void startDialog(Iqs iqs) {
		if(isEdit == false || iqs==null) {
			iqsAddBtn.setEnabled(false);
			getJButtonAdd().setText("Add");
		} else {
			iqsAddBtn.setEnabled(true);
			getIqsCategoryLengthBean().setEditable(false);
			getIqsCategoryLengthBean().setEnabled(false);
			getIqsItemNameLengthBean().setEditable(false);
			getIqsItemNameLengthBean().setEnabled(false);
			getIqsCategoryLengthBean().setText(iqs.getIqsCategoryName().trim());
			getIqsItemNameLengthBean().setText(iqs.getIqsItemName().trim());
			getCoefficientLengthBean().setText(iqs.getCoefficient()==null?"":iqs.getCoefficient().trim());
			getJButtonAdd().setText("Edit");
		}			
		setLocationRelativeTo(parentWindow);
		setVisible(true);
		return;
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		if (e.getSource().equals(getJButtonCancel())) 
			cancel();
		if (e.getSource().equals(getJButtonAdd())) 
			edit();
	};

	public void keyPressed(java.awt.event.KeyEvent e) {};

	public void keyReleased(java.awt.event.KeyEvent e) {
		if (e.getSource().equals(getIqsCategoryLengthBean())) 
			checkTextFields();
		if (e.getSource().equals(getIqsItemNameLengthBean())) 
			checkTextFields();
		if (e.getSource().equals(getCoefficientLengthBean()) )
			checkTextFields();
	};

	public void keyTyped(java.awt.event.KeyEvent e) {};

}
