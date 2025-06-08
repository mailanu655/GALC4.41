package com.honda.galc.client.ui.component;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class ManualProductEntryDialog extends JDialog {

	private enum ProductEntryRadioButtonType {
		PROD_ID ("prodIdRdBtn"),
		VIN("vinRdBtn"),
		SHORT_VIN ("shortVinRdBtn"),
		TRACK_STS ("trackStsRdBtn"),
		PROD_LOT ("prodLotRdBtn"),
		SEQ_NO ("seqNoRdBtn"),
		SEQ_RANGE ("seqRangeRdBtn");
		private final String actionCommand;
		private ProductEntryRadioButtonType(String actionCommand) {
			this.actionCommand = actionCommand;
		}
		public String getActionCommand() {
			return this.actionCommand;
		}
		public static ProductEntryRadioButtonType forName(String name) {
			for (ProductEntryRadioButtonType type : values()) {
				if (type.name().equals(name)) {
					return type;
				}
			}
			return null;
		}
	}

	private enum ShipEntryRadioButtonType {
		NOT_SHIPPED ("notShippedBtn"),
		SHIPPED ("shippedBtn");
		
		private final String actionCommand;
		private ShipEntryRadioButtonType(String actionCommand) {
			this.actionCommand = actionCommand;
		}
		public String getActionCommand() {
			return this.actionCommand;
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int OVERSIZED_FONT_SIZE = 16;
	private static Font font = null;

	private final ProductTypeData productTypeData;
	private final ProductPropertyBean productPropertyBean;
	private final List<String> productTrackingStatusFilter;

	private final int searchFieldSize;

	private String returnProductId = "";
	private CardComponent<SearchField> searchFieldPrimary;
	private CardComponent<SearchField> searchFieldSecondary;
	private CardComponent<IconButton> searchBtn;

	private JRadioButton prodIdRdBtn;
	private JRadioButton vinRdBtn;
	private JRadioButton shortVinRdBtn;
	private JRadioButton trackStsRdBtn;
	private JRadioButton prodLotRdBtn;
	private JRadioButton seqNoRdBtn;
	private JRadioButton seqRangeRdBtn;
	private JRadioButton notShippedBtn;
	private JRadioButton shippedBtn;

	private ButtonGroup toggleGroup;
	private ButtonGroup shipToggleGroup;;
	private String[] shippingLines;

	private ObjectTablePane<BaseProduct> productsTblView;

	private JButton selectBtn;
	private JCheckBox filterChkBx;

	public ManualProductEntryDialog(java.awt.Frame owner, String productType, String name) {
		super(owner);
		this.productTypeData = ServiceFactory.getDao(ProductTypeDao.class).findByKey(productType.toUpperCase());
		this.productPropertyBean = PropertyService.getPropertyBean(ProductPropertyBean.class, ApplicationContext.getInstance().getApplicationId());
		this.productTrackingStatusFilter = findProductTrackingStatusFilter(ApplicationContext.getInstance().getApplicationId());
		if (this.productTypeData == null) {
			MessageDialog.showError(this, "Product Type " + productType + " not found");
		}
		this.searchFieldSize = this.productTypeData.getProductType().getProductIdLength();
		init(name, "S-COM020:Manual Product Entry");
	}

	public ManualProductEntryDialog(java.awt.Frame owner, String title, ProductTypeData productTypeData) {
		super(owner);
		this.productTypeData = productTypeData;
		this.productPropertyBean = PropertyService.getPropertyBean(ProductPropertyBean.class, ApplicationContext.getInstance().getApplicationId());
		this.productTrackingStatusFilter = findProductTrackingStatusFilter(ApplicationContext.getInstance().getApplicationId());
		this.searchFieldSize = this.productTypeData.getProductType().getProductIdLength();
		init("SCOM020", title);
	}

	/**
	 * Returns the whitelist of TRACKING_STATUSes that will not be filtered out,
	 * or returns null if filtering is not enabled.
	 */
	private List<String> findProductTrackingStatusFilter(String applicationId) {
		if (productPropertyBean.isProductSearchFilterEnabled()) { // check if filtering is enabled
			List<PreviousLine> previousLines = ServiceFactory.getDao(PreviousLineDao.class).findAllByProcessPointId(applicationId);
			if (previousLines != null && !previousLines.isEmpty()) {
				List<String> previousLineIds = new ArrayList<String>();
				for (PreviousLine previousLine : previousLines) {
					previousLineIds.add(previousLine.getId().getPreviousLineId());
				}
				return previousLineIds;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public void setVisible(boolean b) {
		Logger.getLogger().info(ManualProductEntryDialog.class.getSimpleName() + " : dialog " + (b ? "shown" : "hidden"));
		super.setVisible(b);
	}

	@Override
	public void dispose() {
		Logger.getLogger().info(ManualProductEntryDialog.class.getSimpleName() + " : dialog disposed");
		super.dispose();
	}

	private void init(String name, String title) {
		setName(name);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setSize(1024, 550);
		setTitle(title);
		final boolean isProduct = isProduct(productTypeData.getProductType());
		final boolean isFrame = isProduct && productTypeData.getProductType().equals(ProductType.FRAME);
		final boolean isEngine = isProduct && productTypeData.getProductType().equals(ProductType.ENGINE);
		List<ProductEntryRadioButtonType> types = getProductEntryRadioButtonTypes(isProduct, isFrame, isEngine);
		SystemPropertyBean property = PropertyService.getPropertyBean(SystemPropertyBean.class, ApplicationContext.getInstance().getApplicationId());
		shippingLines = property.getProductSearchShippedLineIds();
		initComponents(types, isProduct, isFrame, isEngine);
		initConnections(types);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { searchFieldPrimary.requestFocusInWindow(); }
		});
	}

	private void initComponents(final List<ProductEntryRadioButtonType> types, final boolean isProduct, final boolean isFrame, final boolean isEngine) {
		// create the radio buttons
		initRadioButtons(types);
		initShipEntryRadioButtons();
		// group the radio buttons
		initRadioButtonsGroup(types);
		

		// initialize the search fields and buttons
		final Icon searchFieldIcon = new ImageIcon(ManualProductEntryDialog.class.getResource("/resource/images/common/magnifier.png"));
		final Icon searchBtnIcon = new ImageIcon(ManualProductEntryDialog.class.getResource("/resource/images/common/search.png"));
		final ActionListener searchBtnActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				doSearch();
			}
		};
		searchBtn = new CardComponent<IconButton>(new IconButton(searchBtnIcon, searchBtnActionListener), true);
		searchFieldPrimary = new CardComponent<SearchField>(new SearchField(searchFieldSize, searchFieldIcon, true, getSearchAction(), searchBtnIcon.getIconHeight()), true);
		searchFieldSecondary = new CardComponent<SearchField>(new SearchField(searchFieldSize, searchFieldIcon, false, getSearchAction(), searchBtnIcon.getIconHeight()), false);

		// initialize the table
		final ColumnMappings columnMappings = new ColumnMappings();
		if (isProduct) {
			if (isFrame) {
				columnMappings.put("Af On Seq", "afOnSequenceNumber");
				columnMappings.put("Short VIN", "shortVin");
			}
			columnMappings.put(productTypeData.getProductIdLabel(), "productId");
			if (isEngine) {
				columnMappings.put("VIN", "vin");
			}
			columnMappings.put("Prod Spec Code", "productSpecCode")
			.put("Production Date", "formattedProductionDate")
			.put("Production Lot", "productionLot");
		} else if (ProductTypeUtil.isDieCast(productTypeData.getProductType())) {
			columnMappings.put(productTypeData.getProductIdLabel(), "productId")
			.put("DC Serial Number", "dcSerialNumber")
			.put("MC Serial Number", "mcSerialNumber")
			.put("Prod Spec Code", "productSpecCode");
		}
		else {
			columnMappings.put(productTypeData.getProductIdLabel(), "productId")
			.put("Prod Spec Code", "productSpecCode");
		}

		productsTblView = new ObjectTablePane<BaseProduct>(columnMappings.get(), true, true);
		productsTblView.setPreferredWidth(600);
		productsTblView.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (event.getClickCount() == 2) {
					Point point = event.getPoint();
					int rowIndex = productsTblView.getTable().rowAtPoint(point);
					int columnIndex = productsTblView.getTable().columnAtPoint(point);
					if (rowIndex != -1 && columnIndex != -1) {
						productsTblView.getTable().changeSelection(rowIndex, columnIndex, false, false);
						doSelect();
					}
				}
			}
		});
		if (productTrackingStatusFilter != null) {
			// add cell renderer to highlight filtered rows
			for (int i = 0; i < productsTblView.getTable().getColumnModel().getColumnCount(); i++) {
				productsTblView.getTable().getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
					@SuppressWarnings("unchecked")
					@Override
					public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
						Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
						if (!productTrackingStatusFilter.contains(((BaseTableModel<BaseProduct>) table.getModel()).getItem(row).getTrackingStatus())) {
							cellComponent.setBackground(productPropertyBean.getProductSearchFilterBackgroundColor());
						} else {
							cellComponent.setBackground(Color.WHITE);
						}
						return cellComponent;
					}
				});
			}
			// filter out products with a TRACKING_STATUS that is not in the whitelist
			getTableModel().setFilter(new TableModelFilter<BaseProduct>() {
				@Override
				public boolean include(BaseProduct item) {
					if (filterChkBx.isVisible() && !filterChkBx.isSelected()) {
						if (productTrackingStatusFilter != null && productTrackingStatusFilter.contains(item.getTrackingStatus())) {
							return true;
						}
						return false;
					}
					return true;
				}
			});
		}

		selectBtn = new JButton("Select");
		selectBtn.setFont(getOversizedFont());
		filterChkBx = new JCheckBox("Show filtered results");
		filterChkBx.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		filterChkBx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getTableModel().filter();
			}
		});
		filterChkBx.setVisible(false);

		initLayout(types);
	}

	private List<ProductEntryRadioButtonType> getProductEntryRadioButtonTypes(final boolean isProduct, final boolean isFrame, final boolean isEngine) {
		List<ProductEntryRadioButtonType> productEntryRadioButtonTypes = new ArrayList<ProductEntryRadioButtonType>();
		Map<String, String> productEntryRadioButtonMap = this.productPropertyBean.getProductSearchRadioButtons();
		if (productEntryRadioButtonMap != null) {
			String productEntryRadioButtonCsv = productEntryRadioButtonMap.get(this.productTypeData.getProductTypeName());
			if (productEntryRadioButtonCsv != null) {
				String[] productEntryRadioButtonNames = productEntryRadioButtonCsv.split(Delimiter.COMMA);
				for (String productEntryRadioButtonName : productEntryRadioButtonNames) {
					ProductEntryRadioButtonType type = ProductEntryRadioButtonType.forName(productEntryRadioButtonName);
					if (type != null && !productEntryRadioButtonTypes.contains(type)) {
						productEntryRadioButtonTypes.add(type);
					}
				}
			}
		}
		if (productEntryRadioButtonTypes.isEmpty()){
			// create default configuration
			productEntryRadioButtonTypes.add(ProductEntryRadioButtonType.PROD_ID);
			if (isFrame) {
				productEntryRadioButtonTypes.add(ProductEntryRadioButtonType.SHORT_VIN);
			} else if (isEngine) {
				productEntryRadioButtonTypes.add(ProductEntryRadioButtonType.VIN);
			}
			productEntryRadioButtonTypes.add(ProductEntryRadioButtonType.TRACK_STS);
			if (isProduct) {
				productEntryRadioButtonTypes.add(ProductEntryRadioButtonType.PROD_LOT);
				if (isFrame) {
					productEntryRadioButtonTypes.add(ProductEntryRadioButtonType.SEQ_NO);
					productEntryRadioButtonTypes.add(ProductEntryRadioButtonType.SEQ_RANGE);
				}
			}
		}
		return productEntryRadioButtonTypes;
	}

	private SortableTableModel<BaseProduct> getTableModel() {
		return (SortableTableModel<BaseProduct>) productsTblView.getTableModel();
	}

	@SuppressWarnings("serial")
	private Action getSearchAction() {
		return (new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				doSearch();
			}
		});
	}

	private Action getUpArrowAction() {
		return (new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				final int buttonCount = toggleGroup.getButtonCount();
				if (buttonCount <= 1) return;
				final Enumeration<AbstractButton> buttons = toggleGroup.getElements();

				int index = 1;
				boolean firstSelected = false;
				AbstractButton previousButton = null;
				while (buttons.hasMoreElements()) {
					AbstractButton currentButton = buttons.nextElement();
					if (currentButton.isSelected()) {
						if (index == 1) {
							// the first button of the group is currently selected, so the last button should become selected
							firstSelected = true;
						} else {
							previousButton.setSelected(true);
							previousButton.doClick(0);
							break;
						}
					} else if (firstSelected) {
						if (index == buttonCount) {
							// currentButton is the last button in the group, and the first button is currently selected
							currentButton.setSelected(true);
							currentButton.doClick(0);
							break;
						}
					} else {
						previousButton = currentButton;
					}
					index++;
				}
			}
		});
	}

	private Action getDownArrowAction() {
		return (new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				final int buttonCount = toggleGroup.getButtonCount();
				if (buttonCount <= 1) return;
				final Enumeration<AbstractButton> buttons = toggleGroup.getElements();

				int index = 1;
				boolean selectNext = false;
				AbstractButton firstButton = null;
				while (buttons.hasMoreElements()) {
					AbstractButton currentButton = buttons.nextElement();
					if (index == 1) {
						// save a reference to the first button in case the last button of the group is currently selected
						firstButton = currentButton;
					}
					if (currentButton.isSelected()) {
						if (index == buttonCount) {
							// the last button of the group is currently selected, so the first button should become selected
							firstButton.setSelected(true);
							firstButton.doClick(0);
							break;
						} else {
							selectNext = true;
						}
					} else if (selectNext) {
						currentButton.setSelected(true);
						currentButton.doClick(0);
						break;
					}
					index++;
				}
			}
		});
	}

	private boolean isProduct(ProductType type) {
		try {
			return Product.class.isAssignableFrom(ProductTypeUtil.getTypeUtil(type).getProductClass());
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

	private void initRadioButtons(List<ProductEntryRadioButtonType> types) {
		boolean first = true;
		for (ProductEntryRadioButtonType type : types) {
			switch (type) {
			case PROD_ID:
				this.prodIdRdBtn = makeRadioButton(productTypeData.getProductIdLabel(), ProductEntryRadioButtonType.PROD_ID.getActionCommand(), first);
				break;
			case TRACK_STS:
				this.trackStsRdBtn = makeRadioButton("Tracking Status", ProductEntryRadioButtonType.TRACK_STS.getActionCommand(), first);
				break;
			case PROD_LOT:
				this.prodLotRdBtn = makeRadioButton("Production Lot", ProductEntryRadioButtonType.PROD_LOT.getActionCommand(), first);
				break;
			case SHORT_VIN:
				this.shortVinRdBtn = makeRadioButton("Short VIN", ProductEntryRadioButtonType.SHORT_VIN.getActionCommand(), first);
				break;
			case SEQ_NO:
				this.seqNoRdBtn = makeRadioButton("SEQ Number", ProductEntryRadioButtonType.SEQ_NO.getActionCommand(), first);
				break;
			case SEQ_RANGE:
				this.seqRangeRdBtn = makeRadioButton("SEQ Range", ProductEntryRadioButtonType.SEQ_RANGE.getActionCommand(), first);
				break;
			case VIN:
				this.vinRdBtn = makeRadioButton("VIN", ProductEntryRadioButtonType.VIN.getActionCommand(), first);
				break;
			}
			first = false;
		}
	}

	private void initShipEntryRadioButtons() {
		this.notShippedBtn = makeRadioButton("Not Shipped", ShipEntryRadioButtonType.NOT_SHIPPED.getActionCommand(), true);
		this.shippedBtn = makeRadioButton("Shipped", ShipEntryRadioButtonType.SHIPPED.getActionCommand(), false);		
	}
	private JRadioButton makeRadioButton(String text, String actionCommand, boolean selected) {
		JRadioButton radioButton = new JRadioButton(text);
		radioButton.setFont(getOversizedFont());
		radioButton.setActionCommand(actionCommand);
		radioButton.setSelected(selected);
		radioButton.setFocusable(false);
		return radioButton;
	}

	private void initRadioButtonsGroup(List<ProductEntryRadioButtonType> types) {
		this.toggleGroup = new ButtonGroup();
		for (ProductEntryRadioButtonType type : types) {
			switch (type) {
			case PROD_ID:
				this.toggleGroup.add(this.prodIdRdBtn);
				break;
			case TRACK_STS:
				this.toggleGroup.add(this.trackStsRdBtn);
				break;
			case PROD_LOT:
				this.toggleGroup.add(this.prodLotRdBtn);
				break;
			case SHORT_VIN:
				this.toggleGroup.add(this.shortVinRdBtn);
				break;
			case SEQ_NO:
				this.toggleGroup.add(this.seqNoRdBtn);
				break;
			case SEQ_RANGE:
				this.toggleGroup.add(this.seqRangeRdBtn);
				break;
			case VIN:
				this.toggleGroup.add(this.vinRdBtn);
				break;
			}
		}
		//shipping status buttons group
		this.shipToggleGroup = new ButtonGroup();
		this.shipToggleGroup.add(this.notShippedBtn);
		this.shipToggleGroup.add(this.shippedBtn);
	}

	private void initLayout(final List<ProductEntryRadioButtonType> types) {
		final int padding = 2;
		final Insets searchFieldInsets = new Insets(0,padding,0,0);
		final Insets searchBtnInsets = new Insets(0,0,0,padding);
		final Insets buttonInsets = new Insets(padding,0,0,0);
		final JLabel searchOptionLabel = new JLabel("Search Option");
		searchOptionLabel.setFont(getOversizedFont());
		this.setLayout(new GridBagLayout());

		ViewUtil.setGridBagConstraints(this, this.searchFieldPrimary, 0, 0, null, null, GridBagConstraints.HORIZONTAL, null, null, searchFieldInsets, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this, this.searchBtn, 1, 0, null, null, GridBagConstraints.NONE, null, null, searchBtnInsets, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this, this.searchFieldSecondary, 0, 1, null, null, GridBagConstraints.HORIZONTAL, null, null, searchFieldInsets, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this, searchOptionLabel, 0, 2, null, null, null, null, null, null, GridBagConstraints.LINE_START, null, null);
		// add the radio buttons
		int gridY = 3;
		for (int i = 0; i < types.size(); i++) {
			int anchor = i == types.size() - 1 ? GridBagConstraints.NORTHWEST : GridBagConstraints.LINE_START;
			ViewUtil.setGridBagConstraints(this, getRadioButtonForType(types.get(i)), 0, gridY, null, null, null, null, null, null, anchor, null, null);
			gridY++;
		}
		//Ship option
		if(shippingLines != null && shippingLines.length > 0){
				final JLabel shipOptionLabel = new JLabel("Shipping Option");
				shipOptionLabel.setFont(getOversizedFont());
				ViewUtil.setGridBagConstraints(this, shipOptionLabel, 0, gridY++, null, null, null, null, null, null, GridBagConstraints.NORTHWEST, null, null);
				ViewUtil.setGridBagConstraints(this, this.notShippedBtn, 0, gridY++, null, null, null, null, null, null, GridBagConstraints.NORTHWEST, null, null);
				ViewUtil.setGridBagConstraints(this, this.shippedBtn, 0, gridY++, null, null, null, null, null, null, GridBagConstraints.NORTHWEST, null, null);
		}
		
		ViewUtil.setGridBagConstraints(this, this.productsTblView, 3, 0, 2, gridY, GridBagConstraints.BOTH, null, null, null, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this, this.selectBtn, 3, gridY, null, null, null, null, null, buttonInsets, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this, this.filterChkBx, 4, gridY, null, null, null, null, null, buttonInsets, GridBagConstraints.LINE_END, null, null);
	}

	private void initConnections(final List<ProductEntryRadioButtonType> types) {
		// set up action for the X (close) button
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				doCancel();
			}
		});

		// set up action for the Esc key
		final String closeWindowActionMap = "closeWindowAction";
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), closeWindowActionMap);
		getRootPane().getActionMap().put(closeWindowActionMap, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				doCancel();
			}
		});

		// set up the tab order
		setTabOrder(searchFieldPrimary.getComponent(), new JComponent[] { searchFieldSecondary.getComponent(), productsTblView.getTable() });
		setTabOrder(searchFieldSecondary.getComponent(), new JComponent[] { productsTblView.getTable(), searchFieldPrimary.getComponent() });
		setTabOrder(productsTblView.getTable(), new JComponent[] { searchFieldPrimary.getComponent() });

		// set up actions for the radio buttons
		ActionListener rdBtnListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				searchFieldPrimary.getComponent().selectAll();
				searchFieldSecondary.getComponent().setText("");
				showInputSearchField(event.getActionCommand());
			}
		};
		for (ProductEntryRadioButtonType type : types) {
			getRadioButtonForType(type).addActionListener(rdBtnListener);
		}

		// set up radio button selection using arrow keys
		final String upArrowActionMap = "upArrowAction";
		final String downArrowActionMap = "downArrowAction";
		searchFieldPrimary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), upArrowActionMap);
		searchFieldPrimary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, 0), upArrowActionMap);
		searchFieldPrimary.getComponent().getActionMap().put(upArrowActionMap, getUpArrowAction());
		searchFieldPrimary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), downArrowActionMap);
		searchFieldPrimary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, 0), downArrowActionMap);
		searchFieldPrimary.getComponent().getActionMap().put(downArrowActionMap, getDownArrowAction());
		searchFieldSecondary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), upArrowActionMap);
		searchFieldSecondary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, 0), upArrowActionMap);
		searchFieldSecondary.getComponent().getActionMap().put(upArrowActionMap, getUpArrowAction());
		searchFieldSecondary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), downArrowActionMap);
		searchFieldSecondary.getComponent().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, 0), downArrowActionMap);
		searchFieldSecondary.getComponent().getActionMap().put(downArrowActionMap, getDownArrowAction());

		// set up product table to select a product using the Enter key
		final String enterKeyActionMap = "enterKeyAction";
		productsTblView.getTable().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enterKeyActionMap);
		productsTblView.getTable().getActionMap().put(enterKeyActionMap, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				doSelect();
			}
		});

		// set up action for the Select button
		ActionListener selectBtnListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				doSelect();
			}
		};
		selectBtn.addActionListener(selectBtnListener);
	}

	private JRadioButton getRadioButtonForType(ProductEntryRadioButtonType type) {
		switch (type) {
		case PROD_ID:
			return this.prodIdRdBtn;
		case TRACK_STS:
			return this.trackStsRdBtn;
		case PROD_LOT:
			return this.prodLotRdBtn;
		case SHORT_VIN:
			return this.shortVinRdBtn;
		case SEQ_NO:
			return this.seqNoRdBtn;
		case SEQ_RANGE:
			return this.seqRangeRdBtn;
		case VIN:
			return this.vinRdBtn;
		default:
			return null;
		}
	}

	/**
	 * Function to be performed when the user searches for a product.
	 */
	@SuppressWarnings("unchecked")
	private void doSearch() {
		getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					productsTblView.getTable().clearSelection();
					String selectedToggle = toggleGroup.getSelection().getActionCommand();
					List<BaseProduct> productList = null;
					if (validateInput(selectedToggle)) {
						if (selectedToggle.equals(ProductEntryRadioButtonType.PROD_ID.getActionCommand())) {
							String productId = searchFieldPrimary.getComponent().getText().trim();
							long startTime = System.currentTimeMillis();
							productList = (List<BaseProduct>) findByProductId(productId);
							Logger.getLogger().info(System.currentTimeMillis() - startTime, ManualProductEntryDialog.class.getSimpleName() + " : " + ProductEntryRadioButtonType.PROD_ID.getActionCommand() + " searched for " + productId);
						} else if (selectedToggle.equals(ProductEntryRadioButtonType.VIN.getActionCommand())) {
							String vin = searchFieldPrimary.getComponent().getText().trim();
							long startTime = System.currentTimeMillis();
							productList = (List<BaseProduct>) findByVin(vin);
							Logger.getLogger().info(System.currentTimeMillis() - startTime, ManualProductEntryDialog.class.getSimpleName() + " : " + ProductEntryRadioButtonType.VIN.getActionCommand() + " searched for " + vin);
						} else if (selectedToggle.equals(ProductEntryRadioButtonType.SHORT_VIN.getActionCommand())) {
							String shortVin = searchFieldPrimary.getComponent().getText().trim();
							long startTime = System.currentTimeMillis();
							productList = (List<BaseProduct>) findByShortVin(shortVin);
							Logger.getLogger().info(System.currentTimeMillis() - startTime, ManualProductEntryDialog.class.getSimpleName() + " : " + ProductEntryRadioButtonType.SHORT_VIN.getActionCommand() + " searched for " + shortVin);
						} else if (selectedToggle.equals(ProductEntryRadioButtonType.TRACK_STS.getActionCommand())) {
							String trackingStatus = searchFieldPrimary.getComponent().getText().trim();
							long startTime = System.currentTimeMillis();
							productList = (List<BaseProduct>) findByTrackingStatus(trackingStatus);
							Logger.getLogger().info(System.currentTimeMillis() - startTime, ManualProductEntryDialog.class.getSimpleName() + " : " + ProductEntryRadioButtonType.TRACK_STS.getActionCommand() + " searched for " + trackingStatus);
						} else if (selectedToggle.equals(ProductEntryRadioButtonType.PROD_LOT.getActionCommand())) {
							String productLot = searchFieldPrimary.getComponent().getText().trim();
							long startTime = System.currentTimeMillis();
							productList = (List<BaseProduct>) findByProductLot(productLot);
							Logger.getLogger().info(System.currentTimeMillis() - startTime, ManualProductEntryDialog.class.getSimpleName() + " : " + ProductEntryRadioButtonType.PROD_LOT.getActionCommand() + " searched for " + productLot);
						} else if (selectedToggle.equals(ProductEntryRadioButtonType.SEQ_NO.getActionCommand())) {
							String seq = searchFieldPrimary.getComponent().getText().trim();
							long startTime = System.currentTimeMillis();
							productList = (List<BaseProduct>) findBySeq(seq);
							Logger.getLogger().info(System.currentTimeMillis() - startTime, ManualProductEntryDialog.class.getSimpleName() + " : " + ProductEntryRadioButtonType.SEQ_NO.getActionCommand() + " searched for " + seq);
						} else if (selectedToggle.equals(ProductEntryRadioButtonType.SEQ_RANGE.getActionCommand())) {
							String seqStart = searchFieldPrimary.getComponent().getText().trim();
							String seqEnd = searchFieldSecondary.getComponent().getText().trim();
							long startTime = System.currentTimeMillis();
							productList = (List<BaseProduct>) findBySeqRange(seqStart, seqEnd);
							Logger.getLogger().info(System.currentTimeMillis() - startTime, ManualProductEntryDialog.class.getSimpleName() + " : " + ProductEntryRadioButtonType.SEQ_RANGE.getActionCommand() + " searched for " + seqStart + "-" + seqEnd);
						}
						
						//shipping logic
						productList = findByShippedStatus(productList);
						productsTblView.reloadData(productList);
						checkFilter(productList);
					}
				} finally {
					getRootPane().setCursor(Cursor.getDefaultCursor());
				}
			}
		});
	}
	private List<BaseProduct> findByShippedStatus(List<BaseProduct> productList) {
		if(productList == null || productList.isEmpty()) return new ArrayList<BaseProduct>();				
		if(shippingLines == null || shippingLines.length == 0 ) 		return productList;
		
		//check if the products are shipped in the shipping line or not.
		List<String> shippingLinesList = Arrays.asList(shippingLines);
		String shipToggle = shipToggleGroup.getSelection().getActionCommand();
		List<BaseProduct> filteredProducts = new ArrayList<BaseProduct>();
		for(BaseProduct product : productList) {
			if(product.getTrackingStatus() != null && shippingLinesList.contains(product.getTrackingStatus().trim())) {
				if(shipToggle.equals(ShipEntryRadioButtonType.SHIPPED.getActionCommand()))  filteredProducts.add(product);
			}else {
				if(!shipToggle.equals(ShipEntryRadioButtonType.SHIPPED.getActionCommand()))  filteredProducts.add(product);
			}
		}
					
		return filteredProducts;
	}

	/**
	 * Function to be performed when the user selects a product.
	 */
	private void doSelect() {
		if (productsTblView.getSelectedItem() == null) {
			MessageDialog.showError(ManualProductEntryDialog.this, "Please Select a Product");
			return;
		}
		returnProductId = productsTblView.getSelectedItem().getProductId();
		Logger.getLogger().info(ManualProductEntryDialog.class.getSimpleName() + " : product id " + returnProductId + " selected");
		ManualProductEntryDialog.this.dispose();
	}

	/**
	 * Function to be performed when the user cancels product selection.
	 */
	private void doCancel() {
		returnProductId = "";
		Logger.getLogger().info(ManualProductEntryDialog.class.getSimpleName() + " : operation cancelled");
		ManualProductEntryDialog.this.dispose();
	}

	private void showInputSearchField(String actionCommand) {
		searchFieldSecondary.setVisible(false);

		searchFieldPrimary.setVisible(true);
		searchBtn.setVisible(true);
		if (actionCommand.equals(ProductEntryRadioButtonType.SEQ_RANGE.getActionCommand())) {
			searchFieldSecondary.setVisible(true);
		}
		searchFieldPrimary.requestFocusInWindow();
	}

	private boolean validateInput(String selectedToggle){
		boolean isValid = true;
		String msgWarning = "";
		
		boolean prodID_selected = selectedToggle.equals(ProductEntryRadioButtonType.PROD_ID.getActionCommand());
		boolean vin_selected = selectedToggle.equals(ProductEntryRadioButtonType.VIN.getActionCommand());

		if (prodID_selected || vin_selected) {
			String content = searchFieldPrimary.getComponent().getText().trim();	
			int searchTextMinLength = getSearchMinLength();
			if (content.length() < searchTextMinLength && prodID_selected){
				isValid =  false;
				msgWarning = productTypeData.getProductIdLabel() + " search field must have at least " + searchTextMinLength + " characters.";
			} else if (content.length() < searchTextMinLength && vin_selected) {
				isValid = false;
				msgWarning = "VIN search field must have at least " + searchTextMinLength + " characters.";
			}
		}
		if (!isValid){
			MessageDialog.showError(this, msgWarning);
		}
		return isValid;
	}

	public String getResultProductId() {
		return returnProductId;
	}

	/**
	 * Sets the tab order for fromComponent.<br>
	 * The given array of toComponents sets the priority of which component will be focused when the TAB key is pressed.<br>
	 * If the first toComponent can be focused then it will be focused, else the next toComponent will be checked and so on.
	 */
	@SuppressWarnings("serial")
	private void setTabOrder(final JComponent fromComponent, final JComponent[] toComponents) {
		final String tabMap = "tabOrder";
		fromComponent.setFocusTraversalKeysEnabled(false);
		fromComponent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), tabMap);
		fromComponent.getActionMap().put(tabMap, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				for (JComponent toComponent : toComponents) {
					if (focusComponent(toComponent)) {
						break;
					}
				}
			}
		});
	}

	private boolean focusComponent(JComponent jComponent) {
		if (jComponent != null && jComponent.isVisible()) {
			if (jComponent.equals(productsTblView.getTable())) {
				if (productsTblView.getTable().getRowCount() > 0) {
					productsTblView.getTable().requestFocusInWindow();
					if (productsTblView.getTable().getSelectedRow() == -1) {
						productsTblView.getTable().setRowSelectionInterval(0, 0);
					}
					return true;
				}
			} else {
				jComponent.requestFocusInWindow();
				if (jComponent instanceof JTextField) {
					((JTextField) jComponent).selectAll();
				}
				return true;
			}
		}
		return false;
	}

	private List<? extends BaseProduct> findByProductId(String sn) {
		try {
			if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(sn))
				return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findAllBySN(sn);
			else
				return new ArrayList<BaseProduct>();
		} catch (Exception e) {
			return new ArrayList<BaseProduct>();
		}
	}
	
	private List<? extends BaseProduct> findByVin(String vin) {
		try {
			if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(vin)) {
				List<Frame> frames = new ArrayList<>();
				frames = (List<Frame>) ProductTypeUtil.getProductDao("FRAME").findAllBySN(vin);
				
				List<Engine> engines = new ArrayList<>();
				for (int i = 0; i < frames.size(); i++) {
					engines.add(ServiceFactory.getDao(EngineDao.class).findEngineByVin(frames.get(i).getId()));
				}
				return engines;
			}
			else
				return new ArrayList<BaseProduct>();
		} catch (Exception e) {
			return new ArrayList<BaseProduct>();
		}
	}

	private List<? extends BaseProduct> findByShortVin(String shortVin) {
		try {
			if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(shortVin))
				return ServiceFactory.getDao(FrameDao.class).findAllByShortVin(shortVin);
			else
				return new ArrayList<BaseProduct>();
		} catch (Exception e) {
			return new ArrayList<BaseProduct>();
		}
	}

	private List<? extends BaseProduct> findByProductLot(String sn) {
		try {
			if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(sn))
				return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findAllByProductionLot(sn);
			else
				return new ArrayList<BaseProduct>();
		} catch (Exception e) {
			return new ArrayList<BaseProduct>();
		}
	}

	private List<? extends BaseProduct> findByTrackingStatus(String sn) {
		try {
			if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(sn))
				return ProductTypeUtil.getProductDao(productTypeData.getProductType()).findByTrackingStatus(sn);
			else
				return new ArrayList<BaseProduct>();
		} catch (Exception e) {
			return new ArrayList<BaseProduct>();
		}
	}

	private List<? extends BaseProduct> findBySeq(String sn) {
		try {
			if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(sn))
				return ((FrameDao)ProductTypeUtil.getProductDao(productTypeData.getProductType())).findByAfOnSequenceNumber(Integer.parseInt(sn));
			else
				return new ArrayList<BaseProduct>();
		} catch (Exception e) {
			return new ArrayList<BaseProduct>();
		}
	}

	private List<? extends BaseProduct> findBySeqRange(String start, String end) {
		try {
			if(!StringUtils.isEmpty(productTypeData.getProductType().name()) && !StringUtils.isEmpty(start) && !StringUtils.isEmpty(end))
				return ((FrameDao)ProductTypeUtil.getProductDao(productTypeData.getProductType())).findAllByAfOnSequenceNumber(Integer.parseInt(start),Integer.parseInt(end));
			else
				return new ArrayList<BaseProduct>();
		} catch (Exception e) {
			return new ArrayList<BaseProduct>();
		}
	}

	/**
	 * Shows the filtering check box if required.<br>
	 * Returns true iff filtering is required.
	 */
	private boolean checkFilter(List<BaseProduct> productList) {
		if (productTrackingStatusFilter != null) {
			if (productList != null) {
				for (BaseProduct product : productList) {
					if (!productTrackingStatusFilter.contains(product.getTrackingStatus())) {
						filterChkBx.setSelected(false);
						filterChkBx.setVisible(true);
						getTableModel().filter();
						return true;
					}
				}
			}
			filterChkBx.setVisible(false);
		}
		return false;
	}

	public Font getOversizedFont() {
		if (font == null) {
			if (getFont() == null) {
				font = Fonts.DIALOG_PLAIN_16;
			} else {
				font = getFont().deriveFont((float) OVERSIZED_FONT_SIZE);
			}
		}
		return font;
	}

	private static class CardComponent<T extends JComponent> extends JPanel {

		private static final long serialVersionUID = 1L;
		private static final String VISIBLE = "visible";
		private static final String INVISIBLE = "invisible";
		private final CardLayout cardLayout;
		private final T component;

		public CardComponent(T component, boolean visible) {
			this.cardLayout = new CardLayout();
			this.component = component;
			setLayout(this.cardLayout);
			add(this.component, VISIBLE);
			add(new JPanel(), INVISIBLE);

			setVisible(visible);
		}

		public T getComponent() {
			return this.component;
		}

		@Override
		public void setVisible(boolean visible) {
			if (visible) {
				this.cardLayout.show(this, VISIBLE);
			} else {
				this.cardLayout.show(this, INVISIBLE);
			}
		}

		@Override
		public void requestFocus() {
			this.component.requestFocus();
		}

		@Override
		public boolean requestFocusInWindow() {
			return this.component.requestFocusInWindow();
		}

		@Override
		public boolean hasFocus() {
			return this.component.hasFocus();
		}
	}

	@SuppressWarnings("serial")
	private class SearchField extends JTextField {
		private final Icon icon;
		private final boolean iconVisible;
		private final Insets textFieldInsets;

		/**
		 * Creates a new SearchField for searching products.
		 * @param columns - number of columns used to calculate preferred width of the search bar
		 * @param icon - icon to display at the left edge of the search bar
		 * @param iconVisible - flag indicating if the icon should be visible
		 * @param searchAction - action to perform when the Enter key is pressed in the search bar
		 * @param height - height of the search bar
		 */
		public SearchField(int columns, Icon icon, boolean iconVisible, Action searchAction, int height) {
			super(columns);
			this.icon = icon;
			this.iconVisible = iconVisible;
			this.textFieldInsets = UIManager.getBorder("TextField.border").getBorderInsets(new JTextField());
			this.setFont(getOversizedFont());
			final String searchActionMap = "searchAction";
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), searchActionMap);
			this.getActionMap().put(searchActionMap, searchAction);
			Dimension dimensions = getPreferredSize();
			dimensions.height = height;
			setPreferredSize(dimensions);
		}

		public Icon getIcon() {
			return this.icon;
		}
		protected boolean isIconVisible() {
			return this.iconVisible;
		}
		protected Insets getTextFieldInsets() {
			return this.textFieldInsets;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int leftMargin = getTextFieldInsets().left;
			if (getIcon() != null) {
				int iconWidth = getIcon().getIconWidth();
				int x = getTextFieldInsets().left + 5;
				leftMargin = leftMargin + x + iconWidth;
				if (isIconVisible()) {
					int iconHeight = getIcon().getIconHeight();
					int y = (this.getHeight() - iconHeight)/2;
					getIcon().paintIcon(this, g, x, y);
				}
			}
			setMargin(new Insets(getTextFieldInsets().top, leftMargin, getTextFieldInsets().bottom, getTextFieldInsets().right));
		}
	}

	@SuppressWarnings("serial")
	private class IconButton extends JButton {
		private final Icon icon;

		public IconButton(Icon icon, ActionListener actionListener) {
			this.icon = icon;
			addActionListener(actionListener);
			setIcon(icon);
			setPreferredSize(new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight()));
			final String enterMap = "enterAction";
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enterMap);
			this.getActionMap().put(enterMap, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent event) {
					doClick();
				}
			});
		}

		public Icon getIcon() {
			return this.icon;
		}
	}

	protected int getSearchMinLength(){
		return PropertyService.getPropertyBean(ProductPropertyBean.class, ApplicationContext.getInstance().getApplicationId()).getProductSearchMinLength();
	}
}
