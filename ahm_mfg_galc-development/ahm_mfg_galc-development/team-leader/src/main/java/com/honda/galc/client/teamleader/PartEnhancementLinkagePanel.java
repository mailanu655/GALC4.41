package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.data.PartLinkTableModel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.ParentPartNameSelectionPanel;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel.YmtocColumns;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PartLinkDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.entity.product.PartLink;
import com.honda.galc.entity.product.PartLinkId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.AuditLoggerUtil;

public class PartEnhancementLinkagePanel extends TabbedPanel
		implements ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;
	protected ProductSpecSelectionBase productSpecSelectionPanel;
	protected PartNameSelectionPanel childPartNameSelectionPanel;
	protected TablePane partLinkSelectionPanel;
	protected ParentPartNameSelectionPanel parentPartNamePanel;
	private PartLinkTableModel partLinkTableModel;
	private static final String ADD_PART = "Add Part";
	private static final String DELETE_PART = "Delete Part";
	private int midPanelHeight = 250;
	private Dimension screenDimension;

	public PartEnhancementLinkagePanel() {
		super("Part Enhancement Linkage Panel", KeyEvent.VK_L);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}

	public PartEnhancementLinkagePanel(TabbedMainWindow mainWindow) {
		super("Part Enhancement Linkage Panel", KeyEvent.VK_L, mainWindow);
		screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		AnnotationProcessor.process(this);
	}

	protected void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);

		Box box1 = Box.createHorizontalBox();
		box1.setBorder(border);
		box1.add(createParentPartNameSelectionPanel());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		add(box1);

		Box box2 = Box.createHorizontalBox();
		box2.setBorder(border);
		box2.add(createProductSpecSelectionPanel());
		box2.add(createChildPartNamePanel());
		add(box2);

		Box box3 = Box.createHorizontalBox();
		box3.setBorder(border);
		box3.add(createPartLinkSelectionPanel());
		add(box3);
	}

	private ParentPartNameSelectionPanel createParentPartNameSelectionPanel() {
		parentPartNamePanel = new ParentPartNameSelectionPanel(getProductType());
		return parentPartNamePanel;
	}

	private ProductSpecSelectionBase createProductSpecSelectionPanel() {
		if (isMbpnProduct())
			productSpecSelectionPanel = new MbpnSelectionPanel(getApplicationProductTypeName());
		else
			productSpecSelectionPanel = new ProductSpecSelectionPanel(getApplicationProductTypeName());
		productSpecSelectionPanel.setSize(screenDimension.width / 2, midPanelHeight);
		Dimension dim = new Dimension(screenDimension.width / 2, screenDimension.height / 2);
		productSpecSelectionPanel.setPreferredSize(dim);
		productSpecSelectionPanel.setMaximumSize(dim);
		return productSpecSelectionPanel;
	}

	public String getProductType() {
		return getApplicationProductTypeName();
	}

	private PartNameSelectionPanel createChildPartNamePanel() {
		childPartNameSelectionPanel = new PartNameSelectionPanel(screenDimension.width / 4, midPanelHeight,
				new Dimension(screenDimension.width / 4, screenDimension.height / 2));
		return childPartNameSelectionPanel;
	}

	private JScrollPane createPartLinkSelectionPanel() {
		partLinkSelectionPanel = new TablePane("Part Link");
		JScrollPane scrollPane = new JScrollPane(partLinkSelectionPanel);
		scrollPane.setSize((int) (screenDimension.getWidth() / 2), midPanelHeight);
		Dimension dim = new Dimension(screenDimension.width / 2, screenDimension.height / 4);
		scrollPane.setPreferredSize(dim);
		scrollPane.setMaximumSize(dim);
		scrollPane.setVisible(true);
		return scrollPane;
	}

	@Override
	public void onTabSelected() {
		if (isInitialized) {
			return;
		}
		initComponents();
		productSpecSelectionPanel.registerParentPartNameSelectionPanel(parentPartNamePanel);
		loadData();
		addListeners();
		isInitialized = true;
	}

	private void addListeners() {
		MouseListener listener = createMouseListener();
		if (isMbpnProduct()) {
			for (LabeledListBox lbox : productSpecSelectionPanel.getColumnBoxsList()) {
				lbox.getComponent().addMouseListener(listener);
			}
		} else {
			productSpecSelectionPanel.getPanel(YmtocColumns.Year.name()).getComponent().addMouseListener(listener);
			productSpecSelectionPanel.getPanel(YmtocColumns.Model.name()).getComponent().addMouseListener(listener);
			productSpecSelectionPanel.getPanel(YmtocColumns.Model_Type.name()).getComponent()
					.addMouseListener(listener);
		}
		childPartNameSelectionPanel.getPartSelectionPanel().getTable().addMouseListener(listener);
		childPartNameSelectionPanel.getPartSelectionPanel().addListSelectionListener(this);

		partLinkSelectionPanel.addListSelectionListener(this);
		partLinkSelectionPanel.addMouseListener(deletePartLinkMouseListener());
		partLinkSelectionPanel.getTable().addMouseListener(deletePartLinkMouseListener());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			Exception exception = null;
			try {
				JMenuItem menuItem = (JMenuItem) e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if (menuItem.getName().equals(ADD_PART))
					addPart();
				else if (menuItem.getName().equals(DELETE_PART))
					deletePart();

			} catch (Exception ex) {
				exception = ex;
			}
			handleException(exception);
		}
	}

	@EventSubscriber(eventClass = ProductSpecSelectionEvent.class)
	public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
		if (event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel)
				|| event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel)
				|| event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)) {
			showPartLinkResult();
		}
	}

	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting())
			return;
		Exception exception = null;
		try {
			if (e.getSource() == (childPartNameSelectionPanel.getPartSelectionPanel().getTable().getSelectionModel())) {
				showPartLinkResult();
			}

		} catch (Exception ex) {
			exception = ex;
		}
		handleException(exception);
	}

	private MouseListener deletePartLinkMouseListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showDeletePartPopupMenu(e);
			}
		});
	}

	private void showCreateRulePopupMenu(MouseEvent e) {
		Logger.getLogger().info("PartSpecSelection Panel CreateRulePopupMenu enabled");
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(ADD_PART, areAllSelected()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		Logger.getLogger().info("create rule existed");
	}

	private void showDeletePartPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(DELETE_PART, isDeleteEnabled()));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private MouseListener createMouseListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCreateRulePopupMenu(e);
			}
		});
	}

	private void loadData() {
		updatePartSelectionModel();
	}

	private void updatePartSelectionModel() {
		if (childPartNameSelectionPanel == null)
			return;
		List<PartName> partNames = new ArrayList<PartName>();
		partNames = loadPartNames();
		childPartNameSelectionPanel.update(partNames);
	}

	private List<PartName> loadPartNames() {
		return ServiceFactory.getDao(PartNameDao.class).findAll();
	}

	private void addPart() {
		String childpartName = getChildPartName();
		if (childpartName == null)
			return;
		PartLink partLink = new PartLink();
		PartLinkId partLinkId = new PartLinkId();
		String  parentPart = getParentPartName();
		List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
		for (String productSpecCode : specCodes) {
			String specCode = (ProductSpec.trimWildcard(productSpecCode)).trim();
			partLinkId.setChildPartName(childpartName);
			partLinkId.setParentPartName(parentPart.trim());
			partLinkId.setProductSpecCode(specCode);
			partLink.setId(partLinkId);
		}

		PartLink partResult = ServiceFactory.getDao(PartLinkDao.class).findByKey(partLinkId);
		if (partResult != null)
			return;

		ServiceFactory.getDao(PartLinkDao.class).insert(partLink);
		logUserAction(SAVED, partLink);
		AuditLoggerUtil.logAuditInfo(null, partLink, "save", getScreenName(), getUserName().toUpperCase(), "GALC",
				"GALC_Maintenance");
		showPartLinkResult();
	}

	private void deletePart() {
		PartLink link = partLinkTableModel.getSelectedItem();
		if (link == null)
			return;
		if (!MessageDialog.confirm(this, "Are you sure that you want to delete this part ?"))
			return;
		PartLinkId id = new PartLinkId();
		id.setParentPartName(link.getId().getParentPartName());
		id.setProductSpecCode(link.getId().getProductSpecCode());
		id.setChildPartName(link.getId().getChildPartName());
		ServiceFactory.getDao(PartLinkDao.class).removeByKey(id);
		logUserAction(REMOVED, link);
		AuditLoggerUtil.logAuditInfo(link, null, "delete", getScreenName(), getUserName().toUpperCase(), "GALC",
				"GALC_Maintenance");
		showPartLinkResult();
	}

	private void showPartLinkResult() {
		Exception exception = null;
		try {
			List<PartLink> partRepairLinks = retrieveSelectedPartLinks();
			partLinkTableModel = new PartLinkTableModel(partLinkSelectionPanel.getTable(), partRepairLinks, false,
					false, true);
			partLinkTableModel.pack();
			partLinkSelectionPanel.addListSelectionListener(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			exception = ex;
		}
		handleException(exception);
	}

	private List<PartLink> retrieveSelectedPartLinks() {
		List<String> specCodes = productSpecSelectionPanel.buildSelectedProductSpecCodes();
		String parentPartName =  getParentPartName();
		List<PartLink> links = new ArrayList<PartLink>();

		List<PartLink> linkList;
		for (String productSpecCode : specCodes) {
			PartName childPartName = (childPartNameSelectionPanel == null
					|| childPartNameSelectionPanel.getPartNameTableModel() == null) ? null
							: childPartNameSelectionPanel.getPartNameTableModel().getSelectedItem();
			if (childPartName != null) {
				PartLinkId id = new PartLinkId();
				id.setParentPartName(parentPartName);
				id.setProductSpecCode(productSpecCode);
				id.setChildPartName(childPartName.getPartName());
				linkList = ServiceFactory.getDao(PartLinkDao.class).findAllById(parentPartName, productSpecCode,
						childPartName.getPartName());
			} else {
				linkList = ServiceFactory.getDao(PartLinkDao.class)
						.findAllByParentPartNameAndProductSpecCode(parentPartName, productSpecCode);
			}
			links.addAll(linkList);
		}
		return links;
	}

	private boolean areAllSelected() {
		return isParentPartNameSelected() && productSpecSelectionPanel.isProductSpecSelected()
				&& isSamePartNameNotSelected();
	}

	private boolean isSamePartNameNotSelected() {
		String childpartName = getChildPartName();
		PartName parentPart = parentPartNamePanel.getSelectedPartName();
		String parentPartName = parentPart.getPartName().trim();
		return !childpartName.equals(parentPartName);
	}

	private String getParentPartName() {
		return parentPartNamePanel.getParentPartName();
	}
	
	private String getChildPartName() {
		return  childPartNameSelectionPanel.getPartNameTableModel().getSelectedItem().getPartName().trim();
	}

	public boolean isParentPartNameSelected() {
		return getParentPartName() != null;
	}

	private boolean isDeleteEnabled() {
		return partLinkTableModel.getTable().getSelectedRowCount() > 0;
	}
}