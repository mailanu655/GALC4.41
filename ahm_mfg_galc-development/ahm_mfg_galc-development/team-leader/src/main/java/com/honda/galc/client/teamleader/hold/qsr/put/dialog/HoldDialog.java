package com.honda.galc.client.teamleader.hold.qsr.put.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.fragments.HoldInputPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.dao.product.HoldAccessTypeDao;
import com.honda.galc.dao.product.QsrDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.HoldAccessTypeId;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LDAPService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldDialog</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
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
public class HoldDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private HoldProductPanel parentPanel;
	private HoldInputPanel inputPanel;

	private List<BaseProduct> products;
	private List<BaseProduct> filteredProducts = new ArrayList<BaseProduct>();
	private QsrMaintenancePropertyBean propertyBean;
	private List<Qsr> qsrs;
	private List<Map<String,Object>> records;

	public HoldDialog(HoldProductPanel parentPanel, String title, List<Map<String, Object>> records, List<Qsr> qsrs) {
		super(parentPanel.getMainWindow(), title, true);
		this.parentPanel = parentPanel;
		this.records = records;
		this.products = parentPanel.extractProducts(records);
		this.qsrs = qsrs;
		this.propertyBean = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, parentPanel.getApplicationId());
		this.filteredProducts.addAll(getProducts());
		
		setSize(730, 400);
		initView();
		mapActions();
	}
	
	public HoldInputPanel getInputPanel() {
		return inputPanel;
	}

	@SuppressWarnings("unchecked")
	protected void initView() {
		inputPanel = new HoldInputPanel(getParentPanel());
		initProcessPointPanel();
	
		add(getInputPanel());

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		resetHoldOptions(false);
		
		if(!propertyBean.isShowProcessPoint()) {
			getInputPanel().getProcessPointPanel().setVisible(false);
		}
		
		if(!propertyBean.isShowSpindleInput()) {
			getInputPanel().getSpindleInput().setVisible(false);
			getInputPanel().getSpindleLabel().setVisible(false);
		}
			
		getInputPanel().getHoldAccessTypeInput().setModel(new DefaultComboBoxModel(new Vector<HoldAccessType>(getHoldAccessTypes())));
		getInputPanel().getHoldAccessTypeInput().setSelectedIndex(-1);
		
		getInputPanel().getQsrInput().setModel(new DefaultComboBoxModel(new Vector<Qsr>(getQsrs())));
		getInputPanel().getQsrInput().setSelectedIndex(-1);

		getRootPane().setDefaultButton(getInputPanel().getCancelButton());
	}
	
	protected void initProcessPointPanel() {

		if (getInputPanel().getProcessPointPanel().isReasonsFromProperties()) {
			getInputPanel().getProcessPointPanel().setVisible(false);
		} else {
			getInputPanel().getProcessPointPanel().setComponentVisibility(getInputPanel().getQCAction());
		}
		inputPanel.getDepartmentComboBoxComponent().setSelectedItem(getParentPanel().getDivision());
		getInputPanel().getQSRReason().populateReasonsByDiv(getInputPanel().getQCAction());
	}
	
	// === action mappings ===//
	private void mapActions() {
		getInputPanel().getHoldNowInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputPanel().setQCAction(QCAction.QCHOLD);
				getInputPanel().getProcessPointPanel().setQCAction(QCAction.QCHOLD);

				if (getInputPanel().getProcessPointPanel().isReasonsFromProperties()) {
					getInputPanel().getProcessPointPanel().setVisible(false);
				}else {
					getInputPanel().getProcessPointPanel().setComponentVisibility(getInputPanel().getQCAction());
				}
				getInputPanel().getProcessPointPanel().initModel();
				getInputPanel().getQSRReason().populateReasonsByDiv(getInputPanel().getQCAction());
				getInputPanel().getOriginDptComboBox().setEnabled(!getInputPanel().getProcessPointPanel().isVisible());
				}
			});

		getInputPanel().getHoldAtShippingInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputPanel().setQCAction(QCAction.QCHOLD);
				getInputPanel().getProcessPointPanel().setQCAction(QCAction.QCHOLD);

				if (getInputPanel().getProcessPointPanel().isReasonsFromProperties()) {
					getInputPanel().getProcessPointPanel().setVisible(false);
				}else {
					getInputPanel().getProcessPointPanel().setComponentVisibility(getInputPanel().getQCAction());
				}
				getInputPanel().getProcessPointPanel().clearSelection();
				getInputPanel().getProcessPointPanel().initModel();
				getInputPanel().getQSRReason().populateReasonsByDiv(getInputPanel().getQCAction());
				getInputPanel().getOriginDptComboBox().setEnabled(!getInputPanel().getProcessPointPanel().isVisible());
			}
		});
		
		getInputPanel().getKickoutInput().addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	getInputPanel().setQCAction(QCAction.KICKOUT);
				getInputPanel().getProcessPointPanel().setQCAction(QCAction.QCHOLD);
				
	    		getInputPanel().getProcessPointPanel().setComponentVisibility(getInputPanel().getQCAction());
				getInputPanel().getProcessPointPanel().initModel();
				getInputPanel().getQSRReason().populateReasonsByDiv(getInputPanel().getQCAction());
				getInputPanel().getOriginDptComboBox().setEnabled(!getInputPanel().getProcessPointPanel().isVisible());
	        }
	    });
		
		getInputPanel().getDepartmentComboBoxComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( getInputPanel().getLineComboBoxComponent().getSelectedItem() == null) {
					getInputPanel().getProcessPointComboBoxComponent().setModel(new DefaultComboBoxModel());
				}
				getInputPanel().getQSRReason().populateReasonsByDiv(getInputPanel().getQCAction());
				if (getInputPanel().getProcessPointPanel().isVisible()) {
					getInputPanel().getOriginDptComboBox().getComponent().setSelectedItem(
						getInputPanel().getDepartmentComboBoxComponent().getSelectedItem());
				}
			}
		});
		
		getInputPanel().getOriginDptComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputPanel().getAddToQsrInput().setSelected(false);
				getInputPanel().getQsrInput().removeAllItems();
				getInputPanel().getQsrInput().setEnabled(false);
				getInputPanel().getReasonInput().setSelectedIndex(-1);
			}
		});
				
		getInputPanel().getHoldAccessTypeInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HoldAccessType selectedAccessType = (HoldAccessType) getInputPanel().getHoldAccessTypeInput().getSelectedItem();
				if (selectedAccessType != null) setHoldTypeByAccessType(selectedAccessType.getId().getTypeId());
				getInputPanel().getAddToQsrInput().setSelected(false);
			}
		});
		
		getInputPanel().getProcessPointPanel().getLineComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputPanel().getQSRReason().populateReasonsByDivAndLine(getInputPanel().getQCAction());				
			}
		});

		final JTextComponent reasonTextField = (JTextComponent) getInputPanel().getReasonInput().getEditor().getEditorComponent();
		HoldInputListener editReasonInputListener = new HoldInputListener(this) {
			@Override
			protected String getReason() {
				return reasonTextField.getText();
			}
		};
		reasonTextField.getDocument().addDocumentListener(editReasonInputListener);

		final JTextComponent kickoutTextField = (JTextComponent) getInputPanel().getProcessPointComboBoxComponent().getEditor().getEditorComponent();
		HoldInputListener editKickoutInputListener = new HoldInputListener(this) {
			@Override
			protected String getKickoutProcessPoint() {
				return kickoutTextField.getText();
			}
		};
		
		kickoutTextField.getDocument().addDocumentListener(editKickoutInputListener);
		
		getInputPanel().getRespDptComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!getInputPanel().getAddToQsrInput().isSelected()) return;
				Division selectedDpt = (Division)getInputPanel().getRespDptComboBox().getComponent().getSelectedItem();
				Qsr selectedQsr = (Qsr)getInputPanel().getQsrInput().getSelectedItem();
				reloadExistingQsrs();
				if (	selectedDpt != null &&
						StringUtils.isNotBlank(selectedDpt.getId()) &&
						selectedQsr != null && 
						selectedQsr.getResponsibleDepartment() != null && 
						selectedQsr.getResponsibleDepartment().trim().equals(selectedDpt.getId().trim())) {
					getInputPanel().getQsrInput().setSelectedItem(selectedQsr);
				} else if (
						(selectedDpt == null || StringUtils.isBlank(selectedDpt.getId())) &&
						StringUtils.isBlank(selectedQsr.getResponsibleDepartment())){
					getInputPanel().getQsrInput().setSelectedItem(selectedQsr);
				} else {
					getInputPanel().getReasonInput().setSelectedIndex(-1);
				}
			}
		});
		
		getInputPanel().getAddToQsrInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadExistingQsrs();
			}
		});
		
		HoldInputListener inputListener = new HoldInputListener(this);
		getInputPanel().getReasonInput().addActionListener(inputListener);
		getInputPanel().getHoldNowInput().addActionListener(inputListener);
		getInputPanel().getHoldAtShippingInput().addActionListener(inputListener);
		getInputPanel().getKickoutInput().addActionListener(inputListener);
		getInputPanel().getProcessPointComboBoxComponent().addActionListener(inputListener);
		getInputPanel().getAssociateNameInput().getDocument().addDocumentListener(inputListener);
		getInputPanel().getPhoneInput().getDocument().addDocumentListener(inputListener);
		getInputPanel().getAddToQsrInput().addActionListener(inputListener);
		getInputPanel().getQsrInput().addActionListener(inputListener);
		getInputPanel().getHoldAccessTypeInput().addActionListener(inputListener);
		getInputPanel().getQsrInput().addActionListener(new QsrSelectionListener(this));

		getInputPanel().getSubmitButton().addActionListener(new HoldProductsAction(this));

		getInputPanel().getHoldAccessTypeInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputPanel().getAddToQsrInput().setSelected(false);
				getInputPanel().getReasonInput().setSelectedItem(null);
				getInputPanel().getQsrInput().setEnabled(false);
				getInputPanel().getQsrInput().setSelectedIndex(-1);
				
			}
		});
		
		getInputPanel().getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public List<BaseProduct> getProducts() {
		return products;
	}

	public HoldProductPanel getParentPanel() {
		return parentPanel;
	}

	public List<Qsr> getQsrs() {
		return qsrs;
	}

	public List<BaseProduct> getFilteredProducts() {
		return filteredProducts;
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
	
	private List<HoldAccessType> getHoldAccessTypes() {
		List<HoldAccessType> holdAccessTypes = new ArrayList<HoldAccessType>();
		List<HoldAccessType> accessTypes = new ArrayList<HoldAccessType>();
		if(propertyBean.isLoadHoldAccessBySecurityGroups()) {
			List<String> userSecurityGroupList = LDAPService.getInstance().getMemberList(parentPanel.getMainWindow().getUserId().trim());
			 accessTypes = ServiceFactory.getDao(HoldAccessTypeDao.class).findAllByMatchingSecurityGroups(userSecurityGroupList);	
		}else {
			accessTypes =  ServiceFactory.getDao(HoldAccessTypeDao.class).findAll();
		}
		holdAccessTypes.addAll(filterHoldAccessTypes(accessTypes));
		return holdAccessTypes;
	}
	
	private List<HoldAccessType> filterHoldAccessTypes(List<HoldAccessType> accessTypes) {
		List<HoldAccessType> holdAccessTypes = new ArrayList<>();
		List<String> typeIds = new ArrayList<>();
		
		//get selected product type from panel
		String selectedProductType = getParentPanel().getProductType().getProductName();
		
		for (HoldAccessType accessType : accessTypes) {
			HoldAccessTypeId accessTypeId = accessType.getId();
			if (!typeIds.contains(accessTypeId.getTypeId()) 
					&& (accessTypeId.getProductType().equals(selectedProductType) 
							|| accessTypeId.getProductType().equals(""))) {
				typeIds.add(accessTypeId.getTypeId());
				holdAccessTypes.add(accessType);
			}
		}
		
		return holdAccessTypes;
	}
	
	private void resetHoldOptions(Boolean setEnabled) {
		Enumeration<AbstractButton> enumeration = this.getInputPanel().getButtonGroup().getElements();
		while (enumeration.hasMoreElements()) {
			enumeration.nextElement().setEnabled(setEnabled);
		}
		this.getInputPanel().getHoldAtShippingInput().setSelected(true);
	}
			
	private void setHoldTypeByAccessType(String accessTypeId) {
		this.resetHoldOptions(true);
		Map<String,String> holdToAccessTypeMap = this.getInputPanel().getProperty().isHoldToAccessTypeMap(String.class);
		if (holdToAccessTypeMap != null && holdToAccessTypeMap.containsKey(accessTypeId)) {
			String holdType = holdToAccessTypeMap.get(accessTypeId);
			if (holdType.equalsIgnoreCase(HoldResultType.HOLD_NOW.toString())) {
				this.getInputPanel().getHoldNowInput().doClick();
			} else if (holdType.equalsIgnoreCase(HoldResultType.HOLD_AT_SHIPPING.toString())) {
				this.getInputPanel().getHoldAtShippingInput().doClick();
			} else if (holdType.equalsIgnoreCase(QCAction.KICKOUT.toString())) {
				this.getInputPanel().getKickoutInput().doClick();
			} 		
			Enumeration<AbstractButton> enumeration = this.getInputPanel().getButtonGroup().getElements();
			while (enumeration.hasMoreElements()) {
				AbstractButton button = enumeration.nextElement();
				button.setEnabled(button.isSelected());
			}
		} else this.setDefaultHoldType();
	}
	
	private void setDefaultHoldType() {
		if (propertyBean.isHoldNowDisabled()) {
			this.getInputPanel().getHoldNowInput().setEnabled(false);
			this.getInputPanel().getHoldAtShippingInput().doClick();
		} else if (propertyBean.isHoldAtShippingDisabled()) {
			this.getInputPanel().getHoldAtShippingInput().setEnabled(false);
			this.getInputPanel().getHoldNowInput().doClick();
		}
		getInputPanel().getKickoutInput().setEnabled(propertyBean.isKickOutEnabled());
	}
	
	private void reloadExistingQsrs() {
		Division division = (Division) this.getInputPanel().getOriginDptComboBox().getComponent().getSelectedItem();
		ProductType productType = this.getParentPanel().getProductType();
		HoldAccessType selectedAccessType = (HoldAccessType) getInputPanel().getHoldAccessTypeInput().getSelectedItem();
		
		List<Qsr> qsrList = new ArrayList<Qsr>();
		
		if (division != null && productType != null && selectedAccessType != null) {
			qsrList = ServiceFactory.getDao(QsrDao.class).findAll(
				division.getDivisionId(), 
				productType.name(), 
				QsrStatus.ACTIVE.getIntValue(),
				selectedAccessType.getId().getTypeId());
		}
		
		qsrList = this.filterQsrsByRespDpt(qsrList);
		
		Comparator<Qsr> c = new Comparator<Qsr>() {
			public int compare(Qsr o1, Qsr o2) {
				return -o1.getId().compareTo(o2.getId());
			}
		};
		Collections.sort(qsrList, c);
		this.inputPanel.getQsrInput().setModel(new DefaultComboBoxModel(qsrList.toArray()));
		this.inputPanel.getQsrInput().setSelectedIndex(-1);
	}
	
	private List<Qsr> filterQsrsByRespDpt(List<Qsr> qsrList){
		List<Qsr> filteredQsrList = new ArrayList<Qsr>();
		Division selectedRespDpt = (Division)this.getInputPanel().getRespDptComboBox().getComponent().getSelectedItem();
		if (qsrList == null) return filteredQsrList;
		if (selectedRespDpt != null && StringUtils.isNotBlank(selectedRespDpt.getId())) {
			for (Qsr qsr : qsrList) {
				if (StringUtils.isNotBlank(qsr.getResponsibleDepartment()) && 
					(qsr.getResponsibleDepartment().trim()).equals(selectedRespDpt.getId().trim())) {
					filteredQsrList.add(qsr);
				}	
			}
		} else {
			filteredQsrList = qsrList;
		}
		return filteredQsrList;
	}
}