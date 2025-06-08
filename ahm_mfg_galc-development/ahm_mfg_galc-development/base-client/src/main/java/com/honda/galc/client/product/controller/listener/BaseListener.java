package com.honda.galc.client.product.controller.listener;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EventObject;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.HoldParmDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.qics.InspectionModelDao;
import com.honda.galc.dao.qics.InspectionPartDescriptionDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BaseListener</code> is ... .
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

public abstract class BaseListener<T extends JPanel> implements ActionListener, ItemListener {

	protected static final String SAVED = "saved";
	protected static final String UPDATED = "updated";
	protected static final String INSERTED = "inserted";
	protected static final String REMOVED = "removed";

	private T view;
	protected Logger logger;

	public BaseListener(T view) {
		this.view = view;
	}

	// === Event Handlers Stubs === //
	// === ActionListener === //
	public void actionPerformed(ActionEvent ae) {
		try {
			clearMsg();
			executeActionPerformed(ae);
		} catch (Exception e) {
			handleException(e, ae);
		}
	}

	protected void executeActionPerformed(ActionEvent ae) {
		JOptionPane.showMessageDialog(null, getCallerInfo());
	}

	// === ItemListener === //
	public void itemStateChanged(ItemEvent ie) {
		try {
			clearMsg();
			executeItemStateChanged(ie);
		} catch (Exception e) {
			handleException(e, ie);
		}
	}

	protected void executeItemStateChanged(ItemEvent ie) {
		if (ItemEvent.DESELECTED == ie.getStateChange()) {
			JOptionPane.showMessageDialog(null, String.format("%s:deselected", getCallerInfo()));
		} else if (ItemEvent.SELECTED == ie.getStateChange()) {
			JOptionPane.showMessageDialog(null, String.format("%s:selected", getCallerInfo()));
		}
	}

	// === ListSelectionListener === //
	public void valueChanged(ListSelectionEvent lse) {
		try {
			clearMsg();
			executeValueChanged(lse);
		} catch (Exception e) {
			handleException(e, lse);
		}
	}

	protected void executeValueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting()) {
			return;
		}
		ListSelectionModel model = (ListSelectionModel) lse.getSource();
		if (model.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(null, String.format("%s:deselected", getCallerInfo()));
		} else {
			JOptionPane.showMessageDialog(null, String.format("%s:selected", getCallerInfo()));
		}
	}

	// === DocumentListener === //
	public void changedUpdate(DocumentEvent de) {
		try {
			clearMsg();
			executeDocumentListener(de);
		} catch (Exception ex) {
			handleException(ex, null);
		}
	}

	public void insertUpdate(DocumentEvent de) {
		try {
			executeDocumentListener(de);
		} catch (Exception ex) {
			handleException(ex, null);
		}
	}

	public void removeUpdate(DocumentEvent de) {
		try {
			executeDocumentListener(de);
		} catch (Exception ex) {
			handleException(ex, null);
		}
	}

	protected void executeDocumentListener(DocumentEvent e) {
		JOptionPane.showMessageDialog(null, String.format("%s:selected", getCallerInfo()));
	}

	// === utility api === //
	protected void clearMsg() {
		if (getMainWindow() != null) {
			getMainWindow().clearMessage();
		}
	}

	protected void handleException(Exception ex, EventObject eo) {
		if (ex == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		Throwable t = ex;
		while (t != null) {
			String msg = t.getMessage();
			if (msg != null && msg.trim().length() > 0) {
				sb.append("Exc : ").append(t.getClass().getName());
				int maxLenght = 120;
				if (msg.trim().length() > maxLenght) {
					sb.append("\nPlease see a log file for detailed error description.\nMsg : ").append(msg.substring(0, maxLenght)).append("...");
				} else {
					sb.append("\nMsg : ").append(msg);
				}
			} else {
				sb.append(t.toString()).append("\n");
			}
			t = t.getCause();
		}
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));

		if (eo != null) {
			if (eo.getSource() instanceof JTextField) {
				TextFieldState.ERROR.setState((JTextField) eo.getSource());
			}
		}

		if (getView() instanceof ApplicationMainPanel) {
			ApplicationMainPanel view = (ApplicationMainPanel) getView();
			view.getMainWindow().setErrorMessage(sw.toString());
			view.getMainWindow().getLogger().error(sw.toString());
		} else {
			Logger.getLogger(getClass().getName()).error(sw.toString());
			JOptionPane.showMessageDialog(getView(), sb.toString());
		}
	}

	protected boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	protected String trim(String str) {
		if (str != null) {
			return str.trim();
		}
		return str;
	}

	// === get / services === //
	public DivisionDao getDivisionDao() {
		return ServiceFactory.getDao(DivisionDao.class);
	}

	public ProcessPointDao getProcessPointDao() {
		return ServiceFactory.getDao(ProcessPointDao.class);
	}

	public HoldResultDao getHoldResultDao() {
		return ServiceFactory.getDao(HoldResultDao.class);
	}

	public ProductDao<? extends BaseProduct> getProductDao(ProductType productType) {
		return ProductTypeUtil.getProductDao(productType);
	}

	public ProductHistoryDao<? extends ProductHistory, ?> getProductHistoryDao(ProductType productType) {
		return ProductTypeUtil.getProductHistoryDao(productType);
	}

	public InspectionModelDao getInspectionModelDao() {
		return ServiceFactory.getDao(InspectionModelDao.class);
	}

	public InspectionPartDescriptionDao getInspectionPartDescriptionDao() {
		return ServiceFactory.getDao(InspectionPartDescriptionDao.class);
	}

	public DefectDescriptionDao getDefectDescriptionDao() {
		return ServiceFactory.getDao(DefectDescriptionDao.class);
	}

	public DefectResultDao getDefectResultDao() {
		return ServiceFactory.getDao(DefectResultDao.class);
	}

	public DailyDepartmentScheduleDao getDailyDepartmentScheduleDao() {
		return ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
	}

	public GpcsDivisionDao getGpcsDivisionDao() {
		return ServiceFactory.getDao(GpcsDivisionDao.class);
	}

	public UserDao getUserDao() {
		return ServiceFactory.getDao(UserDao.class);
	}

	public HoldParmDao getHoldParamDao() {
		return ServiceFactory.getDao(HoldParmDao.class);
	}

	public QsrDao getQsrDao() {
		return ServiceFactory.getDao(QsrDao.class);
	}

	// === get/set === //
	public T getView() {
		return view;
	}

	public MainWindow getMainWindow() {
		Container container = getView().getTopLevelAncestor();
		if (container != null && container instanceof MainWindow) {
			return (MainWindow) container;
		} else {
			return null;
		}
	}

	public static String getCallerInfo() {
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		int ix = 3;
		String str = String.format("%s.%s()", stes[ix].getClassName(), stes[ix].getMethodName());
		return str;
	}
	
	protected Logger getLogger() {
		if(logger == null) {
			logger = Logger.getLogger(view.getClass().getSimpleName());
		}
		return logger;
	}
	
	protected void logUserAction(String message) {
		getLogger().info(getUserInfo(), message);
	}
	
	protected void logUserAction(String message, Object object) {
		if(object != null) {
			getLogger().info(getUserInfo(), message, " ", object.getClass().getSimpleName(), ": ", object.toString());
		}
	}

	protected void logUserAction(String message, List<?> items) {
		for(Object object : items) {
			logUserAction(message, object);
		}
	}
	
	protected String getUserInfo() {
		return "User " + ClientMain.getInstance().getAccessControlManager().getUserName() + " ";
	}
}
