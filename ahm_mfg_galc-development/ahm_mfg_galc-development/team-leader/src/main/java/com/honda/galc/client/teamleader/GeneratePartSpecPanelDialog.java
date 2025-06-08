package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.teamleader.model.PartSpecFromBomTableModel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.AuditLoggerUtil;

public class GeneratePartSpecPanelDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private JPanel partNumberSearchPanel, partNumberFilterPanel;
	private JPanel buttonPanel;
	private JButton saveButton;
	private JButton cancelButton;
	private UpperCaseFieldBean partNumberSearch, partNumberFilter;
	private JCheckBox selectAllCheckBox;
	private JCheckBox showPartColorCheckBox;
	private JComboBox modelComboBox;
	private ObjectTablePane partSpecPanel = new ObjectTablePane("Part Id Selection");
	private JButton loadButton, filterButton;
	private PartSpecFromBomTableModel partSpecFromBomTableModel = new PartSpecFromBomTableModel(
			partSpecPanel.getTable(), null);

	private String partName;
	private String plantCode;
	private List<Bom> bomList;
	private List<PartSpec> partSpecs;
	private List<Bom> partNumberList;

	private JLabel msgLabel;

	public GeneratePartSpecPanelDialog(Frame owner, String partName, String plant, List<PartSpec> partSpecs) {
		super(owner, "Generate Part Specs");
		this.partName = partName;
		this.plantCode = plant;
		this.partSpecs = partSpecs;
		this.partNumberList = new ArrayList<Bom>();
		setSize(800, 800);
		initComponents();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void initComponents() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		msgLabel = UiFactory.getDefault().createLabel("   ", SwingConstants.CENTER);
		msgLabel.setForeground(Color.RED);
		labelPanel.add(msgLabel);

		JPanel boxPanel1 = new JPanel();
		boxPanel1.setLayout(new BoxLayout(boxPanel1, BoxLayout.Y_AXIS));
		JSplitPane splitPanel1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getPartNumberSearchPanel(),
				getPartNumberFilterPanel());
		splitPanel1.setDividerLocation(100);

		boxPanel1.add(splitPanel1);

		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, boxPanel1, getPartIdSelectionPanel());
		splitPanel.setDividerLocation(200);

		boxPanel.add(splitPanel);

		JPanel selectPanel = new JPanel();
		selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.Y_AXIS));
		selectPanel.add(getSelectAllCheckBox());
		selectPanel.add(getShowPartColorCheckBox());
		boxPanel.add(selectPanel);

		panel.add(labelPanel, BorderLayout.NORTH);
		panel.add(boxPanel, BorderLayout.CENTER);
		panel.add(getButtonPanel(), BorderLayout.SOUTH);

		add(panel, BorderLayout.CENTER);
		getPartNumberSearchText().addKeyListener(this);

		getLoadButton().addActionListener(this);
		partSpecFromBomTableModel.showPartColor(false);
	}

	private JCheckBox getSelectAllCheckBox() {
		if (selectAllCheckBox == null) {
			selectAllCheckBox = new JCheckBox();
			selectAllCheckBox.setText("Select All Part Numbers");
			selectAllCheckBox.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (selectAllCheckBox.isSelected()) {
						List<Bom> selectedBomList = new ArrayList<Bom>();
						List<Bom> bomList = partSpecFromBomTableModel.getItems();
						for (Bom bom : bomList) {
							bom.setApply(true);
							selectedBomList.add(bom);
						}
						partSpecFromBomTableModel.refresh(selectedBomList);
					} else {
						List<Bom> selectedBomList = new ArrayList<Bom>();
						List<Bom> bomList = partSpecFromBomTableModel.getItems();
						for (Bom bom : bomList) {
							bom.setApply(false);
							selectedBomList.add(bom);
						}
						partSpecFromBomTableModel.refresh(selectedBomList);
					}

				}

			});
			selectAllCheckBox.setEnabled(false);
		}
		return selectAllCheckBox;
	}

	private JCheckBox getShowPartColorCheckBox() {
		if (showPartColorCheckBox == null) {
			showPartColorCheckBox = new JCheckBox();
			showPartColorCheckBox.setText("Show Part Color");
			showPartColorCheckBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean usePartColor = showPartColorCheckBox.isSelected();
					partSpecFromBomTableModel.showPartColor(usePartColor);
					filterData(usePartColor);
					filterPartNumberList();
					enableGui(partNumberList.size() > 0);
				}
			});
		}
		return showPartColorCheckBox;
	}

	private Component getPartNumberSearchPanel() {
		if (partNumberSearchPanel == null) {
			partNumberSearchPanel = new JPanel();
			partNumberSearchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
			partNumberSearchPanel
			.add(UiFactory.getInfo().createLabel("Base 5 Part ", UiFactory.getDefault().getLabelFont()));
			partNumberSearchPanel.add(getPartNumberSearchText());
			partNumberSearchPanel.add(getLoadButton());
		}
		return partNumberSearchPanel;
	}

	private Component getPartNumberFilterPanel() {
		if (partNumberFilterPanel == null) {
			partNumberFilterPanel = new JPanel();
			partNumberFilterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
			JPanel panel1 = new JPanel();
			panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			panel1.add(UiFactory.getInfo().createLabel("ModelYear ", UiFactory.getDefault().getLabelFont()));
			panel1.add(getModelYearComboBox());
			partNumberFilterPanel.add(panel1);
			JPanel panel2 = new JPanel();
			panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			panel2.add(UiFactory.getInfo().createLabel("Part# Filter ", UiFactory.getDefault().getLabelFont()));
			panel2.add(getPartNumberFilterText());
			partNumberFilterPanel.add(panel2);
			JPanel panel3 = new JPanel();
			panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			panel3.add(getFilterButton());
			partNumberFilterPanel.add(panel3);

		}
		return partNumberFilterPanel;
	}

	private Component getModelYearComboBox() {
		modelComboBox = new JComboBox();
		modelComboBox.setName("JModelList");
		modelComboBox.setEnabled(false);
		modelComboBox.addActionListener(this);
		modelComboBox.setMaximumSize(new java.awt.Dimension(100, 30));
		modelComboBox.setPreferredSize(new java.awt.Dimension(100, 30));
		modelComboBox.setModel(new ComboBoxModel<String>(new ArrayList<String>()));
		return modelComboBox;
	}

	private UpperCaseFieldBean getPartNumberSearchText() {

		if (partNumberSearch == null) {
			partNumberSearch = new UpperCaseFieldBean();
			partNumberSearch.setName("PartNumberJText");
			partNumberSearch.setFont(UiFactory.getInput().getInputFont());
			partNumberSearch.setMaximumLength(5);
			partNumberSearch.setMaximumSize(new java.awt.Dimension(100, 30));
			partNumberSearch.setPreferredSize(new java.awt.Dimension(100, 30));
		}
		return partNumberSearch;
	}

	private UpperCaseFieldBean getPartNumberFilterText() {

		if (partNumberFilter == null) {
			partNumberFilter = new UpperCaseFieldBean();
			partNumberFilter.setName("PartFilterJText");
			partNumberFilter.setFont(UiFactory.getInput().getInputFont());
			partNumberFilter.addKeyListener(this);
			partNumberFilter.setMaximumLength(13);
			partNumberFilter.setMaximumSize(new java.awt.Dimension(150, 30));
			partNumberFilter.setPreferredSize(new java.awt.Dimension(150, 30));
			partNumberFilter.setEnabled(false);
		}
		return partNumberFilter;
	}

	private Component getPartIdSelectionPanel() {
		return partSpecPanel;
	}

	private Component getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
			buttonPanel.add(getSaveButton());
			buttonPanel.add(getCancelButton());
		}
		return buttonPanel;
	}

	public JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton("Cancel");
			cancelButton.setName("Cancel");
		}
		return cancelButton;
	}

	public JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton("Save");
			saveButton.setName("Save");
		}
		return saveButton;
	}

	public JButton getLoadButton() {
		if (loadButton == null) {
			loadButton = new JButton("Load");
			loadButton.setName("Load");
		}
		return loadButton;
	}

	public JButton getFilterButton() {
		if (filterButton == null) {
			filterButton = new JButton("Filter");
			filterButton.setName("Filter");
			filterButton.setEnabled(false);
			filterButton.addActionListener(this);
		}
		return filterButton;
	}

	public JLabel getMsgLabel() {
		return msgLabel;
	}

	public List<Bom> getPartNumberList() {
		boolean isSelectAll = getSelectAllCheckBox().isSelected();
		List<Bom> refreshedList = new ArrayList<Bom>();
		if (partNumberList != null && !partNumberList.isEmpty()) {
			for(Bom bom: partNumberList){
				bom.setApply(isSelectAll);
				refreshedList.add(bom);
			}
		}
		return refreshedList;
	}

	public void setPartNumberList(List<Bom> partNumberList) {
		this.partNumberList = partNumberList;
	}

	private void loadData(String partNumber) {
		BomDao bomDao = ServiceFactory.getDao(BomDao.class);
		bomList = bomDao.findAllByPartNo(partNumber, plantCode);
		if (bomList.size() == 0)
			getMsgLabel().setText(" No Matching PartNumber Found ");
		filterData(showPartColorCheckBox.isSelected());
		enableGui(partNumberList.size() > 0);
	}

	/**
	 * Filters loaded data so that only records without a part spec are available.
	 */
	private void filterData(boolean usePartColor) {
		if (bomList != null && !bomList.isEmpty()) {
			List<String> modelYearList = new ArrayList<String>();
			modelYearList.add("");
			List<Bom> partNumberList = new ArrayList<Bom>();
			List<PartSpec> partSpecs = ServiceFactory.getDao(PartSpecDao.class).findAllByPartName(partName);

			if (usePartColor) {
				Map<String, Bom> uniqueModelYearPartNoPartColorBom = new HashMap<String, Bom>();
				for (Bom bom : bomList) {
					String partNo = bom.getId().getPartNo().trim();
					String modelYear = bom.getId().getMtcModel().substring(0, 1);
					String partColor = bom.getId().getPartColorCode();
					if (StringUtils.isBlank(partColor)) {
						partColor = "";
					}
					boolean partSpecExists = partSpecForPartNoPartColorExists(partNo, partSpecs, modelYear, partColor);
					if (!partSpecExists) {
						String key = modelYear + "-" + partNo + "-" + partColor;
						if (!uniqueModelYearPartNoPartColorBom.keySet().contains(key)) {
							uniqueModelYearPartNoPartColorBom.put(key, bom);
							partNumberList.add(bom);
							if (!modelYearList.contains(modelYear))
								modelYearList.add(modelYear);
						}
					}
				}
			} else {
				Map<String, Bom> uniqueModelYearPartNoBom = new HashMap<String, Bom>();
				for (Bom bom : bomList) {
					String partNo = bom.getId().getPartNo().trim();
					String modelYear = bom.getId().getMtcModel().substring(0, 1);
					boolean partSpecExists = partSpecForPartNoExists(partNo, partSpecs, modelYear);
					if (!partSpecExists) {
						String key = modelYear + "-" + partNo;
						if (!uniqueModelYearPartNoBom.keySet().contains(key)) {
							uniqueModelYearPartNoBom.put(key, bom);
							partNumberList.add(bom);
							if (!modelYearList.contains(modelYear))
								modelYearList.add(modelYear);
						}
					}
				}
			}
			setPartNumberList(partNumberList);
			partSpecFromBomTableModel.refresh(partNumberList);
			Object selectedModel = modelComboBox.getSelectedItem();
			modelComboBox.setModel(new ComboBoxModel<String>(modelYearList));
			modelComboBox.setSelectedItem(selectedModel);
		}
	}

	private void enableGui(boolean enable) {
		if (enable) {
			getSelectAllCheckBox().setEnabled(true);
			getSelectAllCheckBox().setSelected(false);
			modelComboBox.setEnabled(true);
			getPartNumberFilterText().setEnabled(true);
			getFilterButton().setEnabled(true);
		} else {
			getSelectAllCheckBox().setEnabled(false);
			getSelectAllCheckBox().setSelected(false);
			modelComboBox.setEnabled(false);
			getPartNumberFilterText().setEnabled(false);
			getFilterButton().setEnabled(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getPartNumberSearchText() || e.getSource() == getLoadButton()) {
			String partNumber = getPartNumberSearchText().getText();
			if (partNumber.trim().length() > 4) {
				getMsgLabel().setText("");
				loadData(partNumber);
			} else {
				partSpecFromBomTableModel.refresh(new ArrayList<Bom>());
				getMsgLabel().setText("PartNumber Search String has to be atleast 5 characters");
				getSelectAllCheckBox().setEnabled(false);
			}
		} else if ((e.getSource() == modelComboBox) || (e.getSource() == getFilterButton())) {
			filterPartNumberList();
		}
	}

	public void createPartSpecsFromBom() {
		boolean usePartColor = showPartColorCheckBox.isSelected();
		List<Bom> partNumberList = partSpecFromBomTableModel.getItems();
		PartSpecDao partSpecDao = ServiceFactory.getDao(PartSpecDao.class);
		for (Bom partNumber : partNumberList) {
			if (partNumber.isApply()) {
				String modelYear = "";
				for (Bom bom : bomList) {
					modelYear = bom.getId().getMtcModel().substring(0, 1);
					if (usePartColor) {
						if (bom.getId().getPartNo().trim().equalsIgnoreCase(partNumber.getId().getPartNo().trim())
								&& StringUtils.equalsIgnoreCase(bom.getId().getPartColorCode(), partNumber.getId().getPartColorCode())
								&& partNumber.getModelYear().equalsIgnoreCase(modelYear)) {
							if (!partSpecForPartNoPartColorExists(partNumber.getId().getPartNo(), partSpecs, modelYear, partNumber.getId().getPartColorCode())) { // add only if partNo does not exist
								PartSpec partSpec = createPartSpec(partName, getNextPartId(partSpecs, modelYear), bom.getId().getPartNo(), bom.getId().getPartColorCode());
								PartSpec pspec = getPartSpecWithSamePartNoPartColorDiffModelYear(partNumber.getId().getPartNo(),
										partSpecs, modelYear, partNumber.getId().getPartColorCode());
								if (pspec != null) {
									partSpec.setPartMark(pspec.getPartMark());
									partSpec.setPartSerialNumberMask(pspec.getPartSerialNumberMask());
								}
								partSpec = partSpecDao.save(partSpec);
								AuditLoggerUtil.logAuditInfo(null,partSpec,"save", "Part",ApplicationContext.getInstance().getUserId().toUpperCase(),"GALC", "GALC_Maintenance");
								partSpecs.add(partSpec);
							}
						}
					} else {
						if (bom.getId().getPartNo().trim().equalsIgnoreCase(partNumber.getId().getPartNo().trim())
								&& partNumber.getModelYear().equalsIgnoreCase(modelYear)) {
							if (!partSpecForPartNoExists(partNumber.getId().getPartNo(), partSpecs, modelYear)) { // add only if partNo does not exist
								PartSpec partSpec = createPartSpec(partName, getNextPartId(partSpecs, modelYear), bom.getId().getPartNo());
								PartSpec pspec = getPartSpecWithSamePartNoDiffModelYear(partNumber.getId().getPartNo(),
										partSpecs, modelYear);
								if (pspec != null) {
									partSpec.setPartMark(pspec.getPartMark());
									partSpec.setPartSerialNumberMask(pspec.getPartSerialNumberMask());
								}
								partSpec = partSpecDao.save(partSpec);
								AuditLoggerUtil.logAuditInfo(null,partSpec,"save", "Part",ApplicationContext.getInstance().getUserId().toUpperCase(),"GALC", "GALC_Maintenance");
								partSpecs.add(partSpec);
							}
						}
					}
				}
			}
		}
	}

	private PartSpec createPartSpec(String partName, String partId, String partNumber) {
		return createPartSpec(partName, partId, partNumber, "");
	}

	private PartSpec createPartSpec(String partName, String partId, String partNumber, String partColor) {
		PartSpecId id = new PartSpecId();
		id.setPartName(partName);
		id.setPartId(partId);
		PartSpec partSpec = new PartSpec();
		partSpec.setId(id);
		partSpec.setPartNumber(partNumber);
		partSpec.setPartColorCode(partColor);
		return partSpec;
	}

	private String getNextPartId(List<PartSpec> partSpecs, String modelYear) {
		String partId = null;

		for (PartSpec partSpec : partSpecs) {
			if (partId == null) {
				if (partSpec.getId().getPartId().substring(0, 1).equalsIgnoreCase(modelYear)) {
					partId = partSpec.getId().getPartId();
				}
			} else if (partSpec.getId().getPartId().substring(0, 1).equalsIgnoreCase(modelYear)
					&& partSpec.getId().getPartId().compareTo(partId) > 0) {
				partId = partSpec.getId().getPartId();
			}
		}

		int num = 0;
		if (partId == null) {
			return modelYear + "~" + "001";
		} else {
			num = Integer.parseInt(partId.substring(2, 5)) + 1;
		}

		return partId.substring(0, 1) + "~" + new DecimalFormat("000").format(num);
	}

	private boolean partSpecForPartNoExists(String partNo, List<PartSpec> partSpecs, String modelYear) {
		boolean partSpecExists = false;
		for (PartSpec pSpec : partSpecs) {
			String partSpecModel = pSpec.getId().getPartId().substring(0, 1);
			if (pSpec.getPartNumber() != null && StringUtils.isNotEmpty(pSpec.getPartNumber().trim())
					&& pSpec.getPartNumber().trim().equalsIgnoreCase(partNo)
					&& partSpecModel.equalsIgnoreCase(modelYear)) {
				partSpecExists = true;
			}
		}

		return partSpecExists;
	}

	private boolean partSpecForPartNoPartColorExists(String partNo, List<PartSpec> partSpecs, String modelYear, String partColor) {
		boolean partSpecExists = false;
		for (PartSpec pSpec : partSpecs) {
			String partSpecModel = pSpec.getId().getPartId().substring(0, 1);
			if (pSpec.getPartNumber() != null && StringUtils.isNotEmpty(pSpec.getPartNumber().trim())
					&& pSpec.getPartNumber().trim().equalsIgnoreCase(partNo)
					&& StringUtils.equalsIgnoreCase(pSpec.getPartColorCode(), partColor)
					&& partSpecModel.equalsIgnoreCase(modelYear)) {
				partSpecExists = true;
			}
		}

		return partSpecExists;
	}

	private PartSpec getPartSpecWithSamePartNoDiffModelYear(String partNo, List<PartSpec> partSpecs, String modelYear) {
		List<PartSpec> partSpecList = new ArrayList<PartSpec>();
		for (PartSpec pSpec : partSpecs) {
			String partSpecModel = pSpec.getId().getPartId().substring(0, 1);
			if (pSpec.getPartNumber() != null && StringUtils.isNotEmpty(pSpec.getPartNumber().trim())
					&& pSpec.getPartNumber().trim().equalsIgnoreCase(partNo)
					&& !partSpecModel.equalsIgnoreCase(modelYear)) {
				partSpecList.add(pSpec);
			}
		}
		if (!partSpecList.isEmpty())
			return partSpecList.get(partSpecList.size() - 1);
		else
			return null;
	}

	private PartSpec getPartSpecWithSamePartNoPartColorDiffModelYear(String partNo, List<PartSpec> partSpecs, String modelYear, String partColor) {
		for (PartSpec pSpec : partSpecs) {
			String partSpecModel = pSpec.getId().getPartId().substring(0, 1);
			if (pSpec.getPartNumber() != null && StringUtils.isNotEmpty(pSpec.getPartNumber().trim())
					&& pSpec.getPartNumber().trim().equalsIgnoreCase(partNo)
					&& StringUtils.equalsIgnoreCase(pSpec.getPartColorCode(), partColor)
					&& !partSpecModel.equalsIgnoreCase(modelYear)) {
				return pSpec;
			}
		}
		return null;
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == getPartNumberSearchText()) {
			String partNumber = getPartNumberSearchText().getText();
			if (e.getKeyCode() == KeyEvent.VK_ENTER && partNumber.length() > 0) {
				if (partNumber.trim().length() > 4) {
					getMsgLabel().setText("");
					loadData(partNumber);
				} else {
					partSpecFromBomTableModel.refresh(new ArrayList<Bom>());
					getMsgLabel().setText("PartNumber Search String has to be atleast 5 characters");
					getSelectAllCheckBox().setEnabled(false);
				}
			}
		} else if (e.getSource() == getPartNumberFilterText()) {
			String partNumber = getPartNumberSearchText().getText();
			if (e.getKeyCode() == KeyEvent.VK_ENTER && partNumber.length() > 0) {
				filterPartNumberList();
			}
		}
	}

	private void filterPartNumberList() {
		String modelYear = (String) modelComboBox.getSelectedItem();
		String partNumberFilter = getPartNumberFilterText().getText();
		List<Bom> filteredList = null;
		boolean filterOnlyByModelYear = StringUtils.isNotEmpty(modelYear) && StringUtils.isEmpty(partNumberFilter);
		boolean filterOnlyByPartNumber = StringUtils.isEmpty(modelYear) && StringUtils.isNotEmpty(partNumberFilter);
		boolean filterByModelYearAndPartNumber = StringUtils.isNotEmpty(modelYear)
				&& StringUtils.isNotEmpty(partNumberFilter);
		if (filterOnlyByModelYear || filterOnlyByPartNumber || filterByModelYearAndPartNumber) {
			filteredList = new ArrayList<Bom>();
			for (Bom part : getPartNumberList()) {

				if ((filterOnlyByModelYear && part.getModelYear().trim().equalsIgnoreCase(modelYear.trim()))
						|| (filterOnlyByPartNumber && part.getId().getPartNo().trim().contains(partNumberFilter.trim()))) {
					filteredList.add(part);
					continue;
				} else if (filterByModelYearAndPartNumber) {
					if (part.getModelYear().trim().equalsIgnoreCase(modelYear.trim())
							&& part.getId().getPartNo().trim().contains(partNumberFilter.trim())) {
						filteredList.add(part);
						continue;
					}
				} else {
					continue;
				}

			}
		}
		if (filteredList != null){
			partSpecFromBomTableModel.refresh(filteredList);
		}else{
			partSpecFromBomTableModel.refresh(getPartNumberList());
		}
	}

}
