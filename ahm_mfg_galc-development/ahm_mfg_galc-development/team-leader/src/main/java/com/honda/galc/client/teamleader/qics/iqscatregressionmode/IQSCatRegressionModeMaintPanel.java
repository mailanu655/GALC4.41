package com.honda.galc.client.teamleader.qics.iqscatregressionmode;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.qics.DefectDescriptionDao;
import com.honda.galc.dao.qics.IqsDao;
import com.honda.galc.dao.qics.RegressionDao;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.Iqs;
import com.honda.galc.entity.qics.IqsId;
import com.honda.galc.entity.qics.Regression;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 30, 2014
 */
public class IQSCatRegressionModeMaintPanel extends QicsMaintenanceTabbedPanel implements  ListSelectionListener  {

	private static final long serialVersionUID = 1L;
	private JButton addIqsBtn = null;	
	private JButton deleteIqsBtn = null;
	private JButton updateIqsBtn = null;
	private JButton addRegressionBtn = null;
	private JButton deleteRegressionBtn = null;
	private ObjectTablePane<Iqs> iqsTablePane = null;
	private ObjectTablePane<Regression> regressionTablePane =null;
	private IQSDialog iqsDialog = null;
	private RegressionDialog regressionDialog = null;
	protected WindowIconifiedEventHandler wieh = new WindowIconifiedEventHandler();

	public IQSCatRegressionModeMaintPanel(QicsMaintenanceFrame mainWindow) {
		super("IQS Cat Regression Mode",KeyEvent.VK_R);
		setMainWindow(mainWindow);
		initialize();
	}

	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			isInitialized = true;
		}
	}

	@Override
	public void deselected(ListSelectionModel model) {

	}

	@Override
	public void selected(ListSelectionModel model) {
	}

	class WindowIconifiedEventHandler extends java.awt.event.WindowAdapter {																					
		public void windowIconified(java.awt.event.WindowEvent e) {																				
			IQSCatRegressionModeMaintPanel.this.requestFocus();																			
		};																				
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {		
		if (e.getSource().equals(getAddIqsBtn())) 
			addIqs();
		if (e.getSource().equals(getDeleteIqsBtn())) 
			deleteIqs();
		if (e.getSource().equals(getUpdateIqsBtn())) 
			updateIqs();
		if (e.getSource().equals(getAddRegressionBtn())) 
			addRegression();
		if (e.getSource().equals(getDeleteRegressionBtn())) 
			deleteRegression();
	};		
	public void valueChanged(ListSelectionEvent e) {				 
		if (e.getSource().equals(getIqsTablePane().getTable().getSelectionModel())) 
			iqsTableSelected();
		if (e.getSource().equals(getRegressionTablePane().getTable().getSelectionModel())) 
			regressionTableSelected();
	};

	public void clearMessage() {
		setErrorMessage("");
	}

	public void deleteIqs() {
		clearMessage();	
		if (MessageDialog.confirm(this, "Do you want to delete?")) {
			int row = getIqsTablePane().getTable().getSelectedRow();
			if(row<0)
			{
				setErrorMessage("Please select a row to delete");
				return;
			}
			Iqs iqs = (Iqs)getIqsTablePane().getItems().get(row);
			String iqsCategory = iqs.getId().getIqsCategoryName();
			String iqsItem = iqs.getId().getIqsItemName();
			List<DefectDescription> defectDescList=getDao(DefectDescriptionDao.class).findAllByIqsCategoryItemName(iqsCategory,iqsItem);
			if(defectDescList.size()>0)
			{
				setErrorMessage("Please remove relation with Part Defect Combination");
				return;
			}
			IqsId key = new IqsId(iqsCategory,iqsItem);
			getDao(IqsDao.class).removeByKey(key);
			logUserAction("removed Iqs by key: " + key.toString());
			resetScreen();
		}	
		return;
	}

	public void deleteRegression() {
		clearMessage();
		if (MessageDialog.confirm(this, "Do you want to delete?"))  {
			int row = getRegressionTablePane().getTable().getSelectedRow();	
			if(row<0)
			{
				setErrorMessage("Please select a row to delete");
				return;
			}
			Regression iqs = (Regression)getRegressionTablePane().getItems().get(row);
			String regressionCode = iqs.getRegressionCode();
			List<DefectDescription> defectDescList=getDao(DefectDescriptionDao.class).findAllByRegressionCode(regressionCode);
			if(defectDescList.size()>0)
			{
				setErrorMessage("Please remove relation with Part Defect Combination");
				return;
			}
			getDao(RegressionDao.class).removeByKey(regressionCode);	
			logUserAction("removed Regression by key: " + regressionCode);
			resetScreen();	
		}
		return;
	} 

	public void updateIqs() {
		clearMessage();
		int row = getIqsTablePane().getTable().getSelectedRow();
		Iqs iqs = (Iqs)getIqsTablePane().getItems().get(row);
		if(row<0)
		{
			setErrorMessage("Please select a row to update");
			return;
		}
		iqsDialog = new IQSDialog(this, iqs);
		resetScreen();		
		return;
	}

	public void addIqs() {
		clearMessage();
		iqsDialog = new IQSDialog(this,null);
		if(iqsDialog.isCancel()) {
			return;
		}
		resetScreen();				
		return;
	}

	public void addRegression() {
		clearMessage();		
		regressionDialog = new RegressionDialog(this);
		if(regressionDialog.isCancel()) {
			return;
		}
		resetScreen();
		return;
	}

	private javax.swing.JButton getAddIqsBtn() {
		if (addIqsBtn == null) {
			try {
				addIqsBtn = new javax.swing.JButton();
				addIqsBtn.setName("addIqsBtn");
				addIqsBtn.setFont(new java.awt.Font("dialog", 0, 18));
				Component base = getIqsTablePane();
				addIqsBtn.setSize(100, 50);
				addIqsBtn.setText("Add");
				addIqsBtn.setFont(Fonts.DIALOG_PLAIN_18);
				addIqsBtn.setLocation(getLeftMargin()+25, base.getY() + base.getHeight()+30);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return addIqsBtn;
	}

	private javax.swing.JButton getDeleteIqsBtn() {
		if (deleteIqsBtn == null) {
			try {
				deleteIqsBtn = new javax.swing.JButton();
				deleteIqsBtn.setName("deleteIqsBtn");
				deleteIqsBtn.setFont(new java.awt.Font("dialog", 0, 18));
				Component base = getAddIqsBtn();
				deleteIqsBtn.setSize(100, 50);
				deleteIqsBtn.setText("Delete");
				deleteIqsBtn.setFont(Fonts.DIALOG_PLAIN_18);
				deleteIqsBtn.setLocation(base.getX()+base.getWidth()+30, base.getY() );
			} catch (Exception e) {
				handleException(e);
			}
		}
		return deleteIqsBtn;
	}

	private javax.swing.JButton getUpdateIqsBtn() {
		if (updateIqsBtn == null) {
			try {
				updateIqsBtn = new javax.swing.JButton();
				updateIqsBtn.setName("updateIqsBtn");
				updateIqsBtn.setFont(new java.awt.Font("dialog", 0, 18));
				Component base = getDeleteIqsBtn();
				updateIqsBtn.setSize(100, 50);
				updateIqsBtn.setText("Update");
				updateIqsBtn.setFont(Fonts.DIALOG_PLAIN_18);
				updateIqsBtn.setLocation(base.getX()+base.getWidth()+30, base.getY());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return updateIqsBtn;
	}


	private javax.swing.JButton getAddRegressionBtn() {
		if (addRegressionBtn == null) {
			try {
				addRegressionBtn = new javax.swing.JButton();
				addRegressionBtn.setName("addRegressionBtn");
				addRegressionBtn.setFont(new java.awt.Font("dialog", 0, 18));
				Component base = getUpdateIqsBtn();
				addRegressionBtn.setSize(100, 50);
				addRegressionBtn.setText("Add");
				addRegressionBtn.setFont(Fonts.DIALOG_PLAIN_18);
				addRegressionBtn.setLocation(base.getX()+base.getWidth()+200, base.getY() );
			} catch (Exception e) {
				handleException(e);
			}
		}
		return addRegressionBtn;
	}

	private javax.swing.JButton getDeleteRegressionBtn() {
		if (deleteRegressionBtn == null) {
			try {
				deleteRegressionBtn = new javax.swing.JButton();
				deleteRegressionBtn.setName("deleteRegressionBtn");
				deleteRegressionBtn.setFont(new java.awt.Font("dialog", 0, 18));
				Component base = getAddRegressionBtn();
				deleteRegressionBtn.setSize(100, 50);
				deleteRegressionBtn.setText("Delete");
				deleteRegressionBtn.setFont(Fonts.DIALOG_PLAIN_18);
				deleteRegressionBtn.setLocation(base.getX()+base.getWidth()+30, base.getY());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return deleteRegressionBtn;
	}

	protected ObjectTablePane<Iqs> getIqsTablePane() {
		if(iqsTablePane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("IQS CATEGORY","iqsCategoryName").put("COEFFICIENT", "coefficient").put("IQS ITEM", "iqsItemName"); 			
			iqsTablePane = new ObjectTablePane<Iqs>(columnMappings.get(),true);
			int height = 500;
			iqsTablePane.setSize(400, height);
			iqsTablePane.setLocation(getLeftMargin() + 5, getTopMargin() + 10);
			iqsTablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			iqsTablePane.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return iqsTablePane;
	}

	protected ObjectTablePane<Regression> getRegressionTablePane() {
		if(regressionTablePane==null)
		{
			ColumnMappings columnMappings = ColumnMappings.with("Regression Code","regressionCode"); 			
			regressionTablePane = new ObjectTablePane<Regression>(columnMappings.get(),true);
			Component base = getIqsTablePane();
			int height = base.getHeight();
			regressionTablePane.setSize(400, height);
			regressionTablePane.setLocation(base.getX() + base.getWidth()+100, base.getY());
			regressionTablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			regressionTablePane.setFont(new java.awt.Font("dialog", 0, 14));
		}
		return regressionTablePane;
	}

	public void handleException(Exception e) {
		if(e == null) this.clearErrorMessage();
		else {
			getLogger().error(e, "unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			this.setErrorMessage(e.getMessage());	
		}
	}

	private void initConnections()  {
		getIqsTablePane().getTable().getSelectionModel().addListSelectionListener(this);
		getRegressionTablePane().getTable().getSelectionModel().addListSelectionListener(this);
		getDeleteRegressionBtn().addActionListener(this);
		getUpdateIqsBtn().addActionListener(this);
		getDeleteIqsBtn().addActionListener(this);
		getAddIqsBtn().addActionListener(this);
		getAddRegressionBtn().addActionListener(this);
	}

	private void initialize() {
		try {
			setName("IqsRegressionCodeMaintenancePanel");
			setSize(1024, 768);
			setLayout(null);
			add(getIqsTablePane());
			add(getRegressionTablePane());
			add(getAddIqsBtn());		
			add(getUpdateIqsBtn());
			add(getDeleteIqsBtn());
			add(getAddRegressionBtn());
			add(getDeleteRegressionBtn());
			initConnections();
			startFrame();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void resetComponent() {
		getAddIqsBtn().setEnabled(true);
		getUpdateIqsBtn().setEnabled(false);
		getDeleteIqsBtn().setEnabled(false);
		getAddRegressionBtn().setEnabled(true);
		getDeleteRegressionBtn().setEnabled(false);

	}

	public void resetScreen() {
		List<Iqs> iqsList=getDao(IqsDao.class).findAll();
		List<Regression> regressionList=getDao(RegressionDao.class).findAll();
		getIqsTablePane().reloadData(iqsList);
		getRegressionTablePane().reloadData(regressionList);
		resetComponent();
		return;
	}       

	public void iqsTableSelected()  {
		clearMessage();	
		if(getIqsTablePane().getTable().getSelectedRowCount() <= 0) {
			getAddIqsBtn().setEnabled(true);
			getUpdateIqsBtn().setEnabled(false);
			getDeleteIqsBtn().setEnabled(false);
		} else {
			getAddIqsBtn().setEnabled(false);
			getUpdateIqsBtn().setEnabled(true);
			getDeleteIqsBtn().setEnabled(true);
			getRegressionTablePane().clearSelection();
		}
		return;
	}

	public void regressionTableSelected () {
		clearMessage();	
		if(getRegressionTablePane().getSelectedItems().size() <= 0) {
			getAddRegressionBtn().setEnabled(true);
			getDeleteRegressionBtn().setEnabled(false);

		} else {
			getAddRegressionBtn().setEnabled(false);
			getDeleteRegressionBtn().setEnabled(true);
			getIqsTablePane().getTable().clearSelection();
		}
		return;
	} 

	public void startFrame() {
		try {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));		
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			resetScreen();
		} catch (Exception e) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			handleException(e);
		}
	}

	public void setError(String message) {
		setErrorMessage(message);		
	}


}