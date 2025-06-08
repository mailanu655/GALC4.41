package com.honda.galc.client.teamleader.hold.qsr.release;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.HoldPanel;
import com.honda.galc.client.teamleader.hold.InputPanel;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.listener.ExportAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.PopUpRemovePartsDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.PopupDefectDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.PopupReleaseDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.PopupScrapDialogAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.QsrEventListener;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.SelectByListAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.SelectHoldsAction;
import com.honda.galc.client.teamleader.hold.qsr.release.listener.UnreleaseAction;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.ProductSelectionDialog;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.PropertyMapping;
import com.honda.galc.dao.product.HoldAccessTypeDao;
import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.HoldStatus;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.HoldAccessTypeId;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.report.TableReport;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LDAPService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ReleasePanel</code> is ... .
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
public class ReleasePanel extends HoldPanel {

	private static final long serialVersionUID = 1L;

	private Qsr cachedQsr;
	private HoldResult cachedHoldResultInput;
	private List<HoldAccessType> holdAccessTypes;

	private QsrEventListener qsrEventListener;

	public ReleasePanel(QsrMaintenanceFrame mainWindow) {
		super("Release", KeyEvent.VK_R, mainWindow);
		this.qsrEventListener = new QsrEventListener(this);
	}

	public ReleasePanel(QsrMaintenanceFrame mainWindow,String title) {
		super(title, KeyEvent.VK_E, mainWindow);
	}

	@Override
	protected void defineProductTypeColumnsMapping() {
		super.defineProductTypeColumnsMapping();
		for (PropertiesMapping mapping : getProductTypeColumnsMapping().values()) {
			if (Config.isDisableProductIdCheck(this.getProductTypeString())){
				mapping.get().remove(1);
				mapping.get().add(1, new PropertyMapping("Product ID", "holdResult.id", createHoldIdFormat()));
			}
			putQsrColumnsMapping(mapping);
		}
	}

	protected PropertiesMapping putQsrColumnsMapping(PropertiesMapping mapping) {	
		
		mapping.put("QSR ID", "holdResult.qsrId");
		mapping.put("QSR Status", "holdResult.releaseFlag", createQsrReleaseFlagFormat());
		mapping.put("Product Status", "product.holdStatus", createProductHoldStatusFormat());
		mapping.put("Hld Timestamp", "holdResult.createTimestamp");
		mapping.put("Hld Type", "holdResult.id.holdType", this.createHoldResultTypeFormat());
		mapping.put("Hld Assoc Name", "holdResult.holdAssociateName");
		mapping.put("Rls Timestamp", "holdResult.releaseTimestamp");
		mapping.put("Rls Assoc Name", "holdResult.releaseAssociateName");
		mapping.put("Rls Assoc Phone", "holdResult.releaseAssociatePhone");
		mapping.put("Defect Status", "product.defectStatus", createDefectStatusFormat());
		mapping.put("Dunnage #", "product.dunnage");
		mapping.put("Kickout", "holdProcessPointName");
		return mapping;
	}
	
	@Override
	protected PropertiesMapping createDefaultColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Product Id", "product.productId");
		mapping.put("QSR ID", "holdResult.qsrId");
		return mapping;
	}

	// === factory methods === //
	@Override
	protected ReleaseInputPanel createInputPanel() {
		ReleaseInputPanel panel = new ReleaseInputPanel(this);
		return panel;
	}

	protected JPopupMenu createProductPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		popup.add("Select By List").addActionListener(new SelectByListAction(this));
		popup.addSeparator();
		popup.add("Release Selected").addActionListener(new PopupReleaseDialogAction(this));
		popup.add("Inline Defect").addActionListener(new PopupDefectDialogAction(this));
		popup.add("Scrap Selected").addActionListener(new PopupScrapDialogAction(this));
		popup.addSeparator();
		popup.add("Remove Parts").addActionListener(new PopUpRemovePartsDialogAction(this));
		popup.addSeparator();
		popup.add("Unrelease Selected").addActionListener(new UnreleaseAction(this));
		popup.addSeparator();
	
		popup.add("Export Selected").addActionListener(new ExportAction(this, false));
		popup.add("Export All").addActionListener(new ExportAction(this, true));
		return popup;
	}
	
	public Format createHoldIdFormat() {
		Format format = new Format() {
			private static final long serialVersionUID = 1L;

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				HoldResultId id = (HoldResultId) obj;
				
				if (obj == null) {
					return toAppendTo;
				}
				else {
					return toAppendTo.append(id.getProductId());
				}
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		};
		return format;
	}
	
	public Format createQsrReleaseFlagFormat() {
		Format format = new Format() {
			private static final long serialVersionUID = 1L;

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				if (obj == null) {
					return toAppendTo;
				}
				if (obj.equals(1)) {
					return toAppendTo.append("Released");
				} else {
					return toAppendTo.append("On-Hold");
				}
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		};
		return format;
	}

	public Format createProductHoldStatusFormat() {
		Format format = new Format() {
			private static final long serialVersionUID = 1L;

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				if (obj == null) {
					return toAppendTo;
				}
				if (obj.equals(HoldStatus.ON_HOLD.getId())) {
					return toAppendTo.append("On-Hold");
				}
				if (obj.equals(2)) {
					return toAppendTo.append("Scrapped");
				} else {
					return toAppendTo.append("Released");
				}
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		};
		return format;
	}

	public Format createDefectStatusFormat() {
		Format format = new Format() {
			private static final long serialVersionUID = 1L;

			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				if (obj == null) {
					return toAppendTo;
				}
				DefectStatus ds = (DefectStatus) obj;
				if (ds == null) {
					return toAppendTo.append("Unknown");
				}
				return toAppendTo.append(ds.getName());
			}

			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		};
		return format;
	}
	
	public Format createHoldResultTypeFormat() {
		return new Format() {
			private static final long serialVersionUID = 1L;
			@Override
			public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
				if (obj == null) {
					return toAppendTo;
				} else if (obj.equals(HoldResultType.HOLD_NOW.getId())) {
					return toAppendTo.append(HoldResultType.HOLD_NOW.getTypeString());
				} else if (obj.equals(HoldResultType.HOLD_AT_SHIPPING.getId())) {
					return toAppendTo.append(HoldResultType.HOLD_AT_SHIPPING.getTypeString());
				} else if (obj.equals(HoldResultType.GENERIC_HOLD.getId())){
					return toAppendTo.append(HoldResultType.GENERIC_HOLD.getTypeString());
				} else {
					return toAppendTo.append("unknown");
				}
			}
			@Override
			public Object parseObject(String source, ParsePosition pos) {
				return null;
			}
		};
	}
	
	protected void mapActions() {
		
		getInputPanel().getHoldTypeElement().getComponent().addActionListener(new BaseListener<ReleasePanel>(this) {
			@Override
			public void executeActionPerformed(ActionEvent e) {
				getInputPanel().getQsrComboBox().removeAllItems();
				ProductType productType = getProductType();
				HoldAccessType holdAccessType = getHoldAccessType();
				if (productType != null && holdAccessType != null) {
					String selectedDptRadButton = getInputPanel().getSelectedDptRadButtonText();
					Division division = getDivision();
					List<Qsr> qsrs = new ArrayList<Qsr>();
					if (selectedDptRadButton.equals(InputPanel.ORIGIN_DPT_TEXT)) {
						qsrs = getQsrDao().findAll(division.getDivisionId(), productType.name(), QsrStatus.ACTIVE.getIntValue(), holdAccessType.getId().getTypeId());
					} else {
						for (Qsr qsr : getQsrDao().findAll()) {
							if (	qsr.getResponsibleDepartment() != null && 
									division.getDivisionId().equalsIgnoreCase(qsr.getResponsibleDepartment().trim()) &&
									productType.name().trim().equalsIgnoreCase(qsr.getProductType().trim()) &&
									qsr.getStatus() == QsrStatus.ACTIVE.getIntValue() &&
									holdAccessType.getId().getTypeId().trim().equalsIgnoreCase(qsr.getHoldAccessType().trim())) {
								qsrs.add(qsr);
							}
						}
					}
					Comparator<Qsr> c = new Comparator<Qsr>() {
						public int compare(Qsr o1, Qsr o2) {
							return -o1.getId().compareTo(o2.getId());
						}
					};
					Collections.sort(qsrs, c);
					qsrs = permissionFilter(qsrs,getHoldResultDao());
					getInputPanel().getQsrComboBox().setModel(new DefaultComboBoxModel(new Vector<Qsr>(qsrs)));
					getInputPanel().getQsrComboBox().setSelectedIndex(-1);
				}
				
			}

			
		});
		getInputPanel().getProductTypeComboBox().addActionListener(new BaseListener<ReleasePanel>(this) {
			@Override
			public void executeActionPerformed(ActionEvent e) {
				getInputPanel().getQsrComboBox().removeAllItems();
				getInputPanel().getHoldTypeComboBox().removeAllItems();
				ProductType productType = getProductType();
				if (productType != null) {
					getInputPanel().getHoldTypeElement().getComponent().setModel(new DefaultComboBoxModel(new Vector<HoldAccessType>(getHoldAccessTypes())));
					getInputPanel().getHoldTypeElement().getComponent().setSelectedIndex(0);
				}
			}
		});
		getInputPanel().getQsrComboBox().addItemListener(new BaseListener<ReleasePanel>(this) {
			@Override
			public void executeItemStateChanged(ItemEvent e) {
				getView().getProductPanel().removeData();
				if (ItemEvent.DESELECTED == e.getStateChange()) {
					getView().getInputPanel().getCommandButton().setEnabled(false);
				} else if (ItemEvent.SELECTED == e.getStateChange()) {
					getView().getInputPanel().getCommandButton().setEnabled(true);
				}
			}
		});
		getInputPanel().getCommandButton().addActionListener(new SelectHoldsAction(this));
		MouseListener mouseListener = getMouseListener();
		getProductPanel().addMouseListener(mouseListener);
		getProductPanel().getTable().addMouseListener(mouseListener);
		
		KeyListener kl = new KeyListener() {

			public void keyTyped(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent arg0) {
			}

			public void keyPressed(KeyEvent arg0) {
				if(arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_F)
					searchProduct();

			}
		};
		
		getInputPanel().getCommandButton().addKeyListener(kl);
		getProductPanel().getTable().addKeyListener(kl);
	}
	
	 private void searchProduct() {
		 ProductSelectionDialog productSelect = new ProductSelectionDialog(getProductList());
		 productSelect.setModal(true);
		 productSelect.setVisible(true);
		 int selectedIndex = productSelect.getSelectedIndex();
		 if(selectedIndex != -1)
			 getProductPanel().getTable().setRowSelectionInterval(selectedIndex, selectedIndex);
	 }


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<BaseProduct> getProductList() {
			List<BaseProduct> list = new ArrayList<BaseProduct>();
			BaseTableModel model = (BaseTableModel)getProductPanel().getTable().getModel();
			for(Map<String, Object> map : (List<Map<String, Object>>)model.getItems()){
				list.add((BaseProduct)map.get("product"));
			}
			return list;
		}

	protected List<Qsr> permissionFilter(List<Qsr> qsrs,HoldResultDao holdResultDao) {
		List<Qsr> filterQsrs = new ArrayList<Qsr>();
		for (Qsr qsr : qsrs) {
			List<HoldResult> holdResults = holdResultDao.findAllByQsrId(qsr.getId());
			boolean filterFlg =  false;
			for (HoldResult holdResult : holdResults) {
				if (holdResult.getReleasePermission() > this.getQsrMaintPropertyBean().getHoldBy() && holdResult.getEquipmentFlg() == 0) 
					filterFlg =  true;  
			}
			if (!filterFlg) filterQsrs.add(qsr);
		}
		
		return filterQsrs;
	}
	
	@Override
	public TableReport getReport() {
		TableReport report = super.getReport();
		ProductType productType = getProductType();
		Qsr qsr = (Qsr) getInputPanel().getQsrComboBox().getSelectedItem();
		String fileName = String.format("%s-RELEASE-%s.xlsx", productType, qsr.getName());
		String headerLine = String.format("%s-RELEASE-%s-%s", productType, qsr.getName(), qsr.getDescription());
		String sheetName = String.format("%s-RELEASE-%s", productType, qsr.getName());
		report.setFileName(fileName);
		report.setTitle(headerLine);
		report.setReportName(sheetName);
		return report;
	}

	@Override
	public void defineReports() {
		super.defineReports();
		for (TableReport report : getReports().values()) {
			report.addColumn("holdResult.qsrId", "QSR ID", 4500);
			report.addColumn("holdResult.releaseFlag", "QSR Status", 4500, createQsrReleaseFlagFormat());
			report.addColumn("product.holdStatus", "Product Status", 4500, createProductHoldStatusFormat());
			report.addColumn("product.defectStatus", "Defect Status", 4500, createDefectStatusFormat());
			report.addColumn("holdResult.createTimestamp", "Hold Timestamp", 4500);
			report.addColumn("holdResult.id.holdType", "Hold Type", 4500, this.createHoldResultTypeFormat());
			report.addColumn("holdResult.holdReason", "Hold Reason", 4500);
			report.addColumn("holdResult.holdAssociateNo", "Hold Assoc #", 4500);
			report.addColumn("holdResult.holdAssociateName", "Hold Assoc Name", 4500);
			report.addColumn("holdResult.releaseTimestamp", "Release Timestamp", 4500);
			report.addColumn("holdResult.releaseReason", "Release Reason", 4500);
			report.addColumn("holdResult.releaseAssociateNo", "Release Assoc #", 4500);
			report.addColumn("holdResult.releaseAssociateName", "Release Assoc Name", 4500);
			report.addColumn("holdResult.releaseAssociatePhone", "Release Assoc Phone", 4500);
			report.addColumn("product.dunnage", "Dunnage", 4500);
			report.addColumn("holdResult.holdProcessPoint", "Kickout", 4500);
		}
	}

	public Qsr getCachedQsr() {
		return cachedQsr;
	}

	public void setCachedQsr(Qsr cachedQsr) {
		this.cachedQsr = cachedQsr;
	}

	public HoldResult getCachedHoldResultInput() {
		return cachedHoldResultInput;
	}

	public void setCachedHoldResultInput(HoldResult cachedHoldResultInput) {
		this.cachedHoldResultInput = cachedHoldResultInput;
	}

	@Override
	public ReleaseInputPanel getInputPanel() {
		return (ReleaseInputPanel) super.getInputPanel();
	}

	protected QsrEventListener getQsrEventListener() {
		return qsrEventListener;
	}
	
	public HoldAccessType getHoldAccessType() {
		return (HoldAccessType) getInputPanel().getHoldTypeElement().getComponent().getSelectedItem();
	}

	public List<HoldAccessType> getHoldAccessTypes() {
		this.holdAccessTypes =  filterHoldAccessTypes(ServiceFactory.getDao(HoldAccessTypeDao.class).findAll());
		
		return holdAccessTypes;
	}

	public void setHoldAccessTypes(List<HoldAccessType> holdAccessTypes) {
		this.holdAccessTypes = holdAccessTypes;
	}
	
	protected MouseListener getMouseListener() {
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					boolean selected = getProductPanel().getSelectedValue() != null ;
					boolean listed = getProductPanel().getTable().getRowCount() > 0;
					boolean accessible = getQsrMaintPropertyBean().isLoadHoldAccessBySecurityGroups() ? isUserHasAccess() : true;
					
					MenuElement[] menuElements = getProductPopupMenu().getSubElements();		
					menuElements[0].getComponent().setEnabled(listed && getQsrMaintPropertyBean().isSelectByListEnabled()); // Select By List
					menuElements[1].getComponent().setEnabled(selected && accessible); // Release Selected
					menuElements[2].getComponent().setEnabled(selected && getQsrMaintPropertyBean().isInlineDefectEnabled() && accessible); // Inline defect
					menuElements[3].getComponent().setEnabled(selected && !disableScrapMenuItem() && accessible); // Scrap Selected
					menuElements[4].getComponent().setEnabled(selected && getQsrMaintPropertyBean().isMassPartRemovalEnabled() && accessible); // Remove Parts
					menuElements[5].getComponent().setEnabled(selected && getQsrMaintPropertyBean().isUnreleaseSelectedEnabled() && accessible); // Unrelease Selected
					menuElements[6].getComponent().setEnabled(selected); // Export Selected
					menuElements[7].getComponent().setEnabled(listed); // Export All
					getProductPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}	
		};
		
		return mouseListener;
		
	}
	
	private List<HoldAccessType> getHoldAccessTypesBySecurityGroup(){
		List<HoldAccessType> holdAccessTypes = new ArrayList<HoldAccessType>();
		List<String> userSecurityGroupList = LDAPService.getInstance().getMemberList(getMainWindow().getUserId().trim());
		List<HoldAccessType> accessTypes = ServiceFactory.getDao(HoldAccessTypeDao.class).findAllByMatchingSecurityGroups(userSecurityGroupList);
		holdAccessTypes = filterHoldAccessTypes(accessTypes);
		
		return holdAccessTypes;
	}
	
	protected List<HoldAccessType> filterHoldAccessTypes(List<HoldAccessType> accessTypes) {
		List<HoldAccessType> holdAccessTypes = new ArrayList<>();
		List<String> typeIds = new ArrayList<>();
		
		//get selected product type from panel
		String selectedProductType = getProductType().getProductName();
		
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
	
	protected boolean isUserHasAccess() {
		HoldAccessType seletedHoldAccessType = getHoldAccessType();
		if (seletedHoldAccessType != null) {
			for (HoldAccessType accessType : getHoldAccessTypesBySecurityGroup()){
				if (seletedHoldAccessType.getId().getTypeId().trim().equals(accessType.getId().getTypeId().trim())) 
					return true;
			}
		}
		return false;
	}
}