package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.MbpnSpecData;
import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.data.SpecData;
import com.honda.galc.client.teamleader.property.BuildAttributeMaintenancePropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.BuildAttributeTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BuildAttributeTransferPanel</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Nov 18, 2016
 */
public class BuildAttributeTransferPanel extends TabbedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static final String YEAR_CODE_LABEL = "Year";
	public static final String MODEL_CODE_LABEL = "Model";
	public static final String MAIN_NO_LABEL = "Main No";
	public static final String CLASS_NO_LABEL = "Class No";

	private LabeledComboBox productTypeSelect;

	private LabeledComboBox sourceSpecCode1Select;
	private LabeledComboBox sourceSpecCode2Select;

	private LabeledComboBox targetSpecCode1Select;
	private LabeledComboBox targetSpecCode2Select;

	private JButton transferButton;

	private BuildAttributeTableModel buildAttributeTableModel;
	private TablePane buildAttributePanel;

	private Map<String, SpecData> specDataMap = new HashMap<String, SpecData>();

	private AtomicInteger actionCounter;

	private BuildAttributeMaintenancePropertyBean propertyBean;

	public BuildAttributeTransferPanel(TabbedMainWindow mainWindow) {
		super("Build Attribute Transfer", KeyEvent.VK_T, mainWindow);
		initUi();
		initData();
		mapListeners();
		initUiState();
	}

	// === initialization === //
	protected void initUi() {
		this.productTypeSelect = new LabeledComboBox("Product Type");
		this.sourceSpecCode1Select = new LabeledComboBox(YEAR_CODE_LABEL);
		this.sourceSpecCode2Select = new LabeledComboBox(MODEL_CODE_LABEL);

		this.targetSpecCode1Select = new LabeledComboBox("Year");
		this.targetSpecCode2Select = new LabeledComboBox("Model");

		this.transferButton = new JButton("Transfer");

		setLayout(new MigLayout("insets 0 0 0 0", "", ""));
		add(createSelectionPanel(), "wrap");
		add(createAttributePanel(), "width max, height max, growx, growy, wrap");
		add(createTransferPanel());

		getProductTypeSelect().getComponent().setRenderer(new PropertyComboBoxRenderer<ProductTypeData>(ProductTypeData.class, "productType"));
	}

	protected void initData() {
		setActionCounter(new AtomicInteger(0));
		ProductTypeDao dao = getDao(ProductTypeDao.class);
		List<ProductTypeData> list = dao.findAll();
		List<ProductTypeData> supportedProducts = new ArrayList<ProductTypeData>();

		String[] productTypeNames = getPropertyBean().getProductTypeNames();
		if (productTypeNames == null || productTypeNames.length == 0) {
			supportedProducts.addAll(list);
		} else {
			List<String> names = Arrays.asList(productTypeNames);
			for (ProductTypeData ptd : list) {
				if (names.contains(ptd.getProductTypeName())) {
					supportedProducts.add(ptd);
				}
			}
		}
		getProductTypeSelect().getComponent().setModel(new DefaultComboBoxModel(supportedProducts.toArray()));
	}

	protected void mapListeners() {
		getProductTypeSelect().getComponent().addActionListener(this);
		getSourceSpecCode1Select().getComponent().addActionListener(this);
		getSourceSpecCode2Select().getComponent().addActionListener(this);

		getTargetSpecCode1Select().getComponent().addActionListener(this);
		getTargetSpecCode2Select().getComponent().addActionListener(this);
		getTransferButton().addActionListener(this);
	}

	protected void initUiState() {
		getProductTypeSelect().getComponent().setSelectedIndex(-1);
	}

	// === ui factory methods === //
	protected JPanel createSelectionPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0", "[][][]", ""));
		panel.add(getProductTypeSelect(), "width max, growx");
		panel.add(getSourceSpecCode1Select(), "width max, growx");
		panel.add(getSourceSpecCode2Select(), "width max, growx");
		panel.setBorder(new TitledBorder(""));
		return panel;
	}

	protected JPanel createTransferPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0", "[max][max][200!]", ""));
		panel.add(getTargetSpecCode1Select(), "growx");
		panel.add(getTargetSpecCode2Select(), "growx");
		panel.add(getTransferButton(), "growx, height 40!");
		getTransferButton().setFont(Fonts.DIALOG_BOLD_16);
		panel.setBorder(new TitledBorder("New Product Spec"));
		return panel;
	}

	protected TablePane createAttributePanel() {
		this.buildAttributePanel = new TablePane(null, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.buildAttributeTableModel = new BuildAttributeTableModel(buildAttributePanel.getTable(), true, new ArrayList<BuildAttribute>());
		return buildAttributePanel;
	}

	// === ui utility === //
	protected void resetComboBox(JComboBox comboBox, List<String> list, boolean enabled) {
		if (list != null) {
			comboBox.setModel(new DefaultComboBoxModel(list.toArray()));
		} else {
			comboBox.setModel(new DefaultComboBoxModel(new String[] {}));
		}
		comboBox.setSelectedIndex(-1);
		comboBox.setEnabled(enabled);
	}

	protected void setUiToProductType(String productTypeName) {
		boolean productSpecType = !ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productTypeName));
		if (productSpecType && getBuildAttributeTableModel().isProductSpec() || !productSpecType && !getBuildAttributeTableModel().isProductSpec()) {
			return;
		}
		setBuildAttributeTableModel(new BuildAttributeTableModel(getBuildAttributePanel().getTable(), productSpecType, new ArrayList<BuildAttribute>()));
		if (!productSpecType) {
			getSourceSpecCode1Select().getLabel().setText(MAIN_NO_LABEL);
			getSourceSpecCode2Select().getLabel().setText(CLASS_NO_LABEL);
			getTargetSpecCode1Select().getLabel().setText(MAIN_NO_LABEL);
			getTargetSpecCode2Select().getLabel().setText(CLASS_NO_LABEL);
		} else {
			getSourceSpecCode1Select().getLabel().setText(YEAR_CODE_LABEL);
			getSourceSpecCode2Select().getLabel().setText(MODEL_CODE_LABEL);
			getTargetSpecCode1Select().getLabel().setText(YEAR_CODE_LABEL);
			getTargetSpecCode2Select().getLabel().setText(MODEL_CODE_LABEL);
		}
	}

	protected void setWaitCursor() {
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getActionCounter().incrementAndGet();
	}

	protected void setDefaultCursor() {
		getActionCounter().decrementAndGet();
		if (getActionCounter().get() < 1) {
			getMainWindow().setCursor(Cursor.getDefaultCursor());
		}
	}

	@Override
	public void onTabSelected() {

	}

	// === main action handler === //
	public void actionPerformed(ActionEvent e) {
		try {
			setWaitCursor();
			clearErrorMessage();
			ProductTypeData productTypeData = (ProductTypeData) getProductTypeSelect().getComponent().getSelectedItem();
			String productTypeName = null;
			if (productTypeData != null) {
				productTypeName = productTypeData.getProductTypeName();
			}
			if (e.getSource().equals(getProductTypeSelect().getComponent())) {
				getActionCounter().set(1);
				productTypeSelected(productTypeName);
			} else if (e.getSource().equals(getSourceSpecCode1Select().getComponent())) {
				sourceSpecCode1Selected(productTypeName);
			} else if (e.getSource().equals(getSourceSpecCode2Select().getComponent())) {
				sourceSpecCode2Selected(productTypeName);
			} else if (e.getSource().equals(getTargetSpecCode1Select().getComponent())) {
				targetSpecCode1Selected(productTypeName);
			} else if (e.getSource().equals(getTargetSpecCode2Select().getComponent())) {
				targetSpecCode2Selected();
			} else if (e.getSource().equals(getTransferButton())) {
				transferButtonClicked(productTypeName);
			}
		} catch (Exception ex) {
			setErrorMessage("Exception:" + ex.getMessage());
			getLogger().error(ex, "Exception occured : ");
		} finally {
			setDefaultCursor();
		}
	}

	// === event handlers === //
	protected void productTypeSelected(String productTypeName) {
		List<String> list = null;
		if (productTypeName != null) {
			list = getSpecCode1Values(productTypeName);
			setUiToProductType(productTypeName);
		}
		resetComboBox(getSourceSpecCode1Select().getComponent(), list, list != null);
	}

	protected void sourceSpecCode1Selected(String productTypeName) {
		String sourceSpecCode1 = (String) getSourceSpecCode1Select().getComponent().getSelectedItem();
		List<String> list = null;
		if (sourceSpecCode1 != null) {
			list = getSpecCode2Values(productTypeName, sourceSpecCode1);
		}
		resetComboBox(getSourceSpecCode2Select().getComponent(), list, list != null);
	}

	protected void sourceSpecCode2Selected(String productTypeName) {
		List<String> list = getSpecCode1Values(productTypeName);
		resetComboBox(getTargetSpecCode1Select().getComponent(), list, true);
		displayBuildAttributes(productTypeName);
	}

	protected void targetSpecCode1Selected(String productTypeName) {
		String targetSpecCode1 = (String) getTargetSpecCode1Select().getComponent().getSelectedItem();
		List<String> list = null;
		if (targetSpecCode1 != null) {
			list = getSpecCode2Values(productTypeName, targetSpecCode1);
		}
		resetComboBox(getTargetSpecCode2Select().getComponent(), list, list != null);
	}

	protected void targetSpecCode2Selected() {

		String sourceSpecCode1 = (String) getSourceSpecCode1Select().getComponent().getSelectedItem();
		String sourceSpecCode2 = (String) getSourceSpecCode2Select().getComponent().getSelectedItem();
		String targetSpecCode1 = (String) getTargetSpecCode1Select().getComponent().getSelectedItem();
		String targetSpecCode2 = (String) getTargetSpecCode2Select().getComponent().getSelectedItem();

		boolean selectionValid = isTransferSelectionValid(sourceSpecCode1, sourceSpecCode2, targetSpecCode1, targetSpecCode2);
		boolean transferEnabled = selectionValid;
		transferEnabled = transferEnabled && getBuildAttributeTableModel().getRowCount() > 0;
		getTransferButton().setEnabled(transferEnabled);
	}

	protected void transferButtonClicked(String productTypeName) {
		List<BuildAttribute> sourceAttributes = getBuildAttributeTableModel().getItems();
		if (sourceAttributes == null || sourceAttributes.isEmpty()) {
			return;
		}

		String sourceSpecCode = getSelectedSourceSpecCode(productTypeName);
		String targetSpecCode = getSelectedTargetSpecCode(productTypeName);

		if (isTargetAttributeExist(productTypeName)) {
			return;
		}

		List<BuildAttribute> targetAttributes = assembleAttributes(productTypeName, sourceAttributes, targetSpecCode);

		if (targetAttributes == null || targetAttributes.isEmpty()) {
			String msg = "There is no valid Build Attribute(s) for selected \n New Product Spec";
			MessageDialog.showError(this, msg);
			return;
		}
		int totalCount = sourceAttributes.size();
		int validCount = targetAttributes.size();
		int skippedCount = totalCount - validCount;

		String msg = validCount + " Build Attribute(s) will be copied from '" + sourceSpecCode + "' to '" + targetSpecCode + "'";
		if (skippedCount > 0) {
			msg = msg + "\n" + skippedCount + " Build Attribute(s) will be skipped as New Product Spec is invalid.";
		}
		msg = msg + "\nAre you sure ?";
		if (!MessageDialog.confirm(this, msg)) {
			return;
		}

		int savedCount = saveAttributes(targetAttributes);
		msg = savedCount + " Build Attribute(s) have been saved : for New Product Spec: '" + targetSpecCode + "'";
		MessageDialog.showInfo(this, msg);
		Logger.getLogger().info(msg);
	}

	// === handlers impl === //
	protected boolean isTargetAttributeExist(String productTypeName) {
		String specCode = getSelectedTargetSpecCode(productTypeName);
		BuildAttributeDao dao = ServiceFactory.getDao(BuildAttributeDao.class);
		long count = dao.count(specCode, null, productTypeName);
		if (count > 0) {
			String msg = count + " Build Attributes(s) already exist for selected Product Spec : '" + specCode + "'";
			MessageDialog.showError(this, msg);
			Logger.getLogger().info(msg);
			return true;
		}
		return false;
	}

	protected List<BuildAttribute> assembleAttributes(String productTypeName, List<BuildAttribute> sourceAttributes, String targetSpecCode) {

		SpecData specData = getSpecData(productTypeName);
		Map<String, List<BuildAttribute>> attributeIxByProductSpec = createProductSpecIx(sourceAttributes);
		List<BuildAttribute> targetAttributes = new ArrayList<BuildAttribute>();

		for (String sourceProductSpec : attributeIxByProductSpec.keySet()) {

			List<BuildAttribute> attributesBySpec = attributeIxByProductSpec.get(sourceProductSpec);
			if (attributesBySpec == null || attributesBySpec.isEmpty()) {
				continue;
			}
			String targetProductSpec = assembleProductSpec(sourceProductSpec, targetSpecCode);

			if (!specData.isValid(targetProductSpec)) {
				if (Logger.getLogger().isDebugEnabled()) {
					String msg = "Build Attributes can not be copied from '" + sourceProductSpec + "' to '" + targetProductSpec + "' as new Product Spec is invalid";
					Logger.getLogger().debug(msg);
				}
				continue;
			}
			for (BuildAttribute ba : attributesBySpec) {
				BuildAttribute newBa = assembleBuildAttribute(ba, targetProductSpec);
				targetAttributes.add(newBa);
			}
		}
		return targetAttributes;
	}

	protected int saveAttributes(List<BuildAttribute> attributes) {

		int count = 0;
		int batchSize = getPropertyBean().getBatchSize();
		BuildAttributeDao dao = ServiceFactory.getDao(BuildAttributeDao.class);
		if (!attributes.isEmpty()) {
			count = batchSave(dao, attributes, batchSize);
		}
		return count;
	}

	protected void displayBuildAttributes(String productTypeName) {
		getBuildAttributeTableModel().refresh(new ArrayList<BuildAttribute>());
		if (productTypeName == null) {
			return;
		}
		String specCode = getSelectedSourceSpecCode(productTypeName);
		if (specCode == null) {
			return;
		}
		BuildAttributeDao dao = ServiceFactory.getDao(BuildAttributeDao.class);
		int maxSize = getPropertyBean().getBuildAttributeResultsetMaxSize();
		if (maxSize > 0) {
			long count = dao.count(specCode, null, productTypeName);
			if (count > maxSize) {
				String msg = "Resultset: %s exceeds max size : %s for: %s";
				msg = String.format(msg, count, maxSize, ProductSpec.trimWildcard(specCode));
				setErrorMessage(msg);
				return;
			}

		}
		List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();
		buildAttributes.addAll(dao.findAllMatchId(specCode, null, productTypeName));
		getBuildAttributeTableModel().refresh(buildAttributes);
	}

	// === model api === //
	protected BuildAttribute assembleBuildAttribute(BuildAttribute sourceBa, String targetProductSpec) {
		BuildAttribute targetBa = (BuildAttribute) sourceBa.deepCopy();
		targetBa.setUpdateTimestamp(null);
		targetBa.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		targetBa.getId().setProductSpecCode(targetProductSpec);
		targetBa.setUpdateUser(getUserName());
		return targetBa;
	}

	protected String assembleProductSpec(String sourceSpecCode, String targetSpecCode) {
		StringBuilder sb = new StringBuilder();
		targetSpecCode = targetSpecCode.trim();
		sb.append(targetSpecCode);
		if (targetSpecCode.length() < sourceSpecCode.length()) {
			sb.append(sourceSpecCode.substring(targetSpecCode.length()));
		}
		return sb.toString();
	}

	protected Map<String, List<BuildAttribute>> createProductSpecIx(List<BuildAttribute> buildAttributes) {
		Map<String, List<BuildAttribute>> ix = new TreeMap<String, List<BuildAttribute>>();
		for (BuildAttribute ba : buildAttributes) {
			putAttributeOnIx(ix, ba);
		}
		return ix;
	}

	protected void putAttributeOnIx(Map<String, List<BuildAttribute>> ix, BuildAttribute buildAttribute) {
		if (ix == null || buildAttribute == null || buildAttribute.getProductSpecCode() == null) {
			return;
		}
		String productSpec = buildAttribute.getProductSpecCode();
		List<BuildAttribute> list = ix.get(productSpec);
		if (list == null) {
			list = new ArrayList<BuildAttribute>();
			ix.put(productSpec, list);
		}
		list.add(buildAttribute);
	}

	protected SpecData getSpecData(String productTypeName) {
		if (productTypeName == null) {
			return null;
		}
		SpecData data = getSpecDataMap().get(productTypeName);
		if (data == null) {
			if (ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productTypeName))) {
				data = new MbpnSpecData(productTypeName);
			} else {
				data = new ProductSpecData(productTypeName);
			}
			getSpecDataMap().put(productTypeName, data);
		}
		return data;
	}

	protected MbpnSpecData getMbpnSpecData(String productTypeName) {
		SpecData data = getSpecData(productTypeName);
		if (data instanceof MbpnSpecData) {
			return (MbpnSpecData) data;
		}
		return null;
	}

	protected ProductSpecData getProductSpecData(String productTypeName) {
		SpecData data = getSpecData(productTypeName);
		if (data instanceof ProductSpecData) {
			return (ProductSpecData) data;
		}
		return null;
	}

	protected String getSelectedSourceSpecCode(String productTypeName) {
		String specCode1 = (String) getSourceSpecCode1Select().getComponent().getSelectedItem();
		String specCode2 = (String) getSourceSpecCode2Select().getComponent().getSelectedItem();
		return buildProductSpecCode(productTypeName, specCode1, specCode2);
	}

	protected String getSelectedTargetSpecCode(String productTypeName) {
		String specCode1 = (String) getTargetSpecCode1Select().getComponent().getSelectedItem();
		String specCode2 = (String) getTargetSpecCode2Select().getComponent().getSelectedItem();
		return buildProductSpecCode(productTypeName, specCode1, specCode2);
	}

	protected String buildProductSpecCode(String productTypeName, String specCode1, String specCode2) {

		String productSpecCode = null;
		if (ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productTypeName))) {
			productSpecCode = buildMbpnCode(getMbpnSpecData(productTypeName), specCode1, specCode2);
		} else {
			productSpecCode = buildProductSpecCode(getProductSpecData(productTypeName), specCode1, specCode2);
		}
		if (productSpecCode != null) {
			productSpecCode = ProductSpec.trimWildcard(productSpecCode);
		}
		return productSpecCode;
	}

	public String buildProductSpecCode(ProductSpecData specData, String yearCode, String modelCode) {
		if (specData == null) {
			return null;
		}
		List<String> list = specData.getProductSpecData(yearCode, modelCode);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	protected String buildMbpnCode(MbpnSpecData mbpnData, String mainNo, String classNumber) {
		if (mbpnData == null) {
			return null;
		}
		Object[] ar = {};
		List<String> list = mbpnData.getProductSpecData(mainNo, classNumber, ar, ar, ar, ar, ar);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	protected List<String> getSpecCode1Values(String productTypeName) {
		List<String> list = new ArrayList<String>();
		if (productTypeName == null) {
			return list;
		}
		if (ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productTypeName))) {
			MbpnSpecData mbpnData = getMbpnSpecData(productTypeName);
			list = mbpnData.getMainNos();
		} else {
			ProductSpecData specData = getProductSpecData(productTypeName);
			list = specData.getModelYearCodes();
		}
		return list;
	}

	protected List<String> getSpecCode2Values(String productTypeName, String specCode1) {
		List<String> list = new ArrayList<String>();
		if (productTypeName == null || specCode1 == null) {
			return list;
		}
		if (ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(productTypeName))) {
			MbpnSpecData mbpnData = getMbpnSpecData(productTypeName);
			list = mbpnData.getClassNos(specCode1);
		} else {
			ProductSpecData specData = getProductSpecData(productTypeName);
			list = specData.getModelCodes(specCode1);
		}
		return list;
	}

	protected <E, K, T extends IDaoService<E, K>> int batchSave(IDaoService<E, K> dao, List<E> items, int batchSize) {
		if (items == null || items.isEmpty()) {
			return 0;
		}
		int totalSize = items.size();
		int totalSaved = 0;
		List<E> batch = new ArrayList<E>();
		for (int i = 0; i < totalSize; i++) {
			batch.add(items.get(i));
			if (batch.size() >= batchSize || i == (totalSize - 1)) {
				dao.saveAll(batch);
				logUserAction(SAVED, batch);
				totalSaved += batch.size();
				batch.clear();
			}
		}
		return totalSaved;
	}

	// === validation === //
	protected boolean isTransferSelectionValid(String sourceSpecCode1, String sourceSpecCode2, String targetSpecCode1, String targetSpecCode2) {

		boolean selectionValid = false;
		if (isWildCardOrBlank(sourceSpecCode1) || isWildCardOrBlank(targetSpecCode1)) {
			selectionValid = false;
		} else if (isSpecEquals(sourceSpecCode1, targetSpecCode1) && isSpecEquals(sourceSpecCode2, targetSpecCode2)) {
			selectionValid = false;
		} else if (isSpecEquals(sourceSpecCode1, targetSpecCode1)) {
			if (!isWildCardOrBlank(sourceSpecCode2) && !isWildCardOrBlank(targetSpecCode2)) {
				selectionValid = true;
			}
		} else if (isWildCardOrBlank(sourceSpecCode2) && isWildCardOrBlank(targetSpecCode2)) {
			selectionValid = true;
		} else if (!isWildCardOrBlank(sourceSpecCode2)) {
			selectionValid = true;
		}
		return selectionValid;
	}

	protected boolean isSpecEquals(String str1, String str2) {
		if (StringUtils.equals(str1, str2)) {
			return true;
		}
		if (isWildCardOrBlank(str1) && isWildCardOrBlank(str2)) {
			return true;
		}
		return false;
	}

	protected boolean isWildCardOrBlank(String str) {
		if (StringUtils.isBlank(str)) {
			return true;
		}
		if (ProductSpecUtil.isWildCard(str)) {
			return true;
		}
		return false;
	}

	// === get/set === //
	protected LabeledComboBox getProductTypeSelect() {
		return productTypeSelect;
	}

	protected JButton getTransferButton() {
		return transferButton;
	}

	protected TablePane getBuildAttributePanel() {
		return buildAttributePanel;
	}

	protected Map<String, SpecData> getSpecDataMap() {
		return specDataMap;
	}

	protected BuildAttributeTableModel getBuildAttributeTableModel() {
		return buildAttributeTableModel;
	}

	protected LabeledComboBox getSourceSpecCode1Select() {
		return sourceSpecCode1Select;
	}

	protected LabeledComboBox getSourceSpecCode2Select() {
		return sourceSpecCode2Select;
	}

	protected LabeledComboBox getTargetSpecCode1Select() {
		return targetSpecCode1Select;
	}

	protected LabeledComboBox getTargetSpecCode2Select() {
		return targetSpecCode2Select;
	}

	protected void setBuildAttributeTableModel(BuildAttributeTableModel buildAttributeTableModel) {
		this.buildAttributeTableModel = buildAttributeTableModel;
	}

	protected AtomicInteger getActionCounter() {
		return actionCounter;
	}

	protected void setActionCounter(AtomicInteger actionCounter) {
		this.actionCounter = actionCounter;
	}

	protected BuildAttributeMaintenancePropertyBean getPropertyBean() {
		if (propertyBean == null) {
			propertyBean = PropertyService.getPropertyBean(BuildAttributeMaintenancePropertyBean.class, getApplicationId());
		}
		return propertyBean;
	}
}
