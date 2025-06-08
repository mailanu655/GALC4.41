package com.honda.galc.client.teamleader.hold.qsr.put.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.teamleader.ReturnToFactoryPanel;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.fragments.ReturnToFactoryInputPanel;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductHoldUtil;

public class ReturnToFactoryDialog extends JDialog implements ActionListener, DocumentListener{

	private static final long serialVersionUID = 1L;

	private ReturnToFactoryPanel parentPanel;
	private ReturnToFactoryInputPanel inputPanel;
	private List<Frame> products;
	private QsrMaintenancePropertyBean propertyBean = null;
	private List<Map<String,Object>> records;
	
	private String reason;
	private String id;
	private String name;
	private String phone;
	private String respDpt;
	private Qsr qsr;
	private boolean completeStatus = false;
	private boolean isSelectingQSR = false;

	public ReturnToFactoryDialog(ReturnToFactoryPanel parentPanel, String title, List<Frame> products) {
		super(parentPanel.getMainWindow(), title, true);
		this.parentPanel = parentPanel;
		this.products = products;
		this.setSize(730, 320);
		this.propertyBean=PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getParentPanel().getApplicationId());
		this.initView();
		mapActions();
	}
	
	public ReturnToFactoryPanel getParentPanel() {
		return this.parentPanel;
	}
	
	protected void initView() {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.add(getInputPanel());
		this.getRootPane().setDefaultButton(getInputPanel().getSubmitButton());
	}
	
	public ReturnToFactoryInputPanel getInputPanel() {
		if (this.inputPanel == null) {
			this.inputPanel = new ReturnToFactoryInputPanel(getParentPanel());
			this.inputPanel.getHoldAtShippingInput().setSelected(true);
			this.inputPanel.getHoldAtShippingInput().setEnabled(false);
			
			Component base = this.inputPanel.getHoldLabel();
			for (String holdReason : this.inputPanel.getProperty().getHoldReasons()) {
				this.inputPanel.getReasonInput().addItem(holdReason);
			}
			this.reloadExistingQsrs();
		}
		return this.inputPanel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void reloadExistingQsrs() {
		this.inputPanel.getQsrInput().setModel(new DefaultComboBoxModel(this.getQsrs().toArray()));
		this.inputPanel.getQsrInput().setSelectedIndex(-1);
	}
	
	private void mapActions() {
		getInputPanel().getHoldAtShippingInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputPanel().setQCAction(QCAction.QCHOLD);
			}
		});
		
		getInputPanel().getOriginDptComboBoxComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputPanel().getRespDptComboBoxComponent().setSelectedIndex(-1);
				getInputPanel().getQsrInput().removeAllItems();
				getInputPanel().getAddToQsrInput().setSelected(false);
			}
		});
		
		getInputPanel().getRespDptComboBoxComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				respDpt = (String)getInputPanel().getRespDptComboBoxComponent().getSelectedItem();
				reloadExistingQsrs();
			}
		});

		getInputPanel().getReasonInput().addActionListener(this);
		getInputPanel().getAssociateNameInput().getDocument().addDocumentListener(this);
		getInputPanel().getPhoneInput().getDocument().addDocumentListener(this);
		getInputPanel().getAddToQsrInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getInputPanel().getAddToQsrInput().isSelected()) {
					getInputPanel().getQsrInput().setEnabled(true);
					reloadExistingQsrs();
				} else {
					getInputPanel().getQsrInput().setEnabled(false);
					getInputPanel().getQsrInput().setSelectedIndex(-1);
				}
			}
		});
		getInputPanel().getQsrInput().addActionListener(this);

		getInputPanel().getSubmitButton().addActionListener(this);
		getInputPanel().getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getInputPanel().getQsrInput())) {	
			this.isSelectingQSR = true;
			Qsr selectedQsr = (Qsr)getInputPanel().getQsrInput().getSelectedItem();
			if (selectedQsr != null) {
				String selectedDptID = selectedQsr.getResponsibleDepartment();
				if (StringUtils.isBlank(selectedDptID))
					this.getInputPanel().getDepartmentComboBoxComponent().setSelectedIndex(-1);
				else
					this.getInputPanel().getDepartmentComboBoxComponent().setSelectedItem(selectedDptID);
			}
			this.isSelectingQSR = false;
		}
		if (	e.getSource().equals(getInputPanel().getReasonInput()) ||
				e.getSource().equals(getInputPanel().getAddToQsrInput()) ||
				e.getSource().equals(getInputPanel().getQsrInput())){
			this.enableSubmit();
		} else if (e.getSource().equals(getInputPanel().getSubmitButton())){
			this.completeStatus = true;
			this.dispose();
		}
	}
	
	protected List<Qsr> getQsrs() {
		List<Qsr> qsrList = new ArrayList<Qsr>();
		List<Qsr> filteredQsrList = new ArrayList<Qsr>();
		if (this.parentPanel.getDivision() != null) { 
			qsrList = ServiceFactory.getDao(QsrDao.class).findAll(
					getInputPanel().getOriginDptComboBox().getComponent().getSelectedItem().toString(),
					ProductType.FRAME.name(), 
					QsrStatus.ACTIVE.getIntValue(),
					ProductHoldUtil.getDefaultHoldAccessType(this.getParentPanel().getApplicationId())
				);
		}
		
		if (this.respDpt != null){
			for (Qsr qsr : qsrList) {
				if (StringUtils.isNotBlank(qsr.getResponsibleDepartment()) && 
					(qsr.getResponsibleDepartment().trim()).equals(respDpt.trim())) {
					filteredQsrList.add(qsr);
				}	
			}
		} else {
			filteredQsrList = qsrList;
		}
		
		if (!filteredQsrList.isEmpty()) {
			Comparator<Qsr> c = new Comparator<Qsr>() {
				public int compare(Qsr o1, Qsr o2) {
					return -o1.getId().compareTo(o2.getId());
				}
			};
			Collections.sort(filteredQsrList, c);
		}
		return filteredQsrList;
	}

	public List<Frame> getProducts() {
		return this.products;
	}
	
	public QsrMaintenancePropertyBean getProperty() {
		return propertyBean;
	}
	
	public List<Map<String,Object>> getRecords(){
		return this.records;
	}
	
	public void setRecords(List<Map<String,Object>> records) {
		this.records = records;
	}

	public void insertUpdate(DocumentEvent e) {
		this.enableSubmit();	
	}

	public void removeUpdate(DocumentEvent e) {
		this.enableSubmit();
	}

	public void changedUpdate(DocumentEvent e) {
		this.enableSubmit();
	}
	
	protected void enableSubmit() {
		this.reason = (String) getInputPanel().getReasonInput().getSelectedItem();
		this.id = getInputPanel().getAssociateIdInput().getText();
		this.name = getInputPanel().getAssociateNameInput().getText();
		this.phone = getInputPanel().getPhoneInput().getText();
		this.qsr = (Qsr) this.getInputPanel().getQsrInput().getSelectedItem();
		boolean addToExisting = getInputPanel().getAddToQsrInput().isSelected();
		boolean submitEnabled = false;
		if ((!getProperty().isInputAssociateInfo() && !StringUtils.isEmpty(this.getReason())) ||
			(!StringUtils.isEmpty(this.getReason()) && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(phone))) {
			submitEnabled = true;
		}
		if (addToExisting && this.getQsr() == null) {
			submitEnabled = false;
		}
		this.getInputPanel().getSubmitButton().setEnabled(submitEnabled);
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getRespDpt() {
		return this.respDpt;
	}
	
	public Qsr getQsr() {
		return this.qsr;
	}
	
	public Boolean getAddToExisting() {
		return this.getInputPanel().getAddToQsrInput().isSelected();
	}
	
	public boolean getCompleteStatus() {
		return this.completeStatus;
	}
}