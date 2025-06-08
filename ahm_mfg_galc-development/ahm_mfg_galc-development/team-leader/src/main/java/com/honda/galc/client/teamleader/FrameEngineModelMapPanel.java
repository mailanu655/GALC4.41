package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang.StringUtils;
import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameEngineModelMapDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.SortedArrayList;

public class FrameEngineModelMapPanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final String YMTO_FIELDS_WIDTH_PARAM = "width 50:300:300";
	private ProductSpecSelectionPanel ymtoSelectPanel;
	private List<FrameEngineModelMap> frmEngModelMapRecords;
	private TreeMap<String,HashSet<String>> frameEnginesYmtoMap;
	private ObjectTablePane<FrameEngineModelMap> origYmtoTable;
    private ObjectTablePane<FrameEngineModelMap> altEngineYmtoTable;
    private ArrayList<FrameEngineModelMap> altEngineMtoRecords;
    private LabeledTextField enterYearTextField;
    private LabeledTextField enterModelTextField;
    private LabeledTextField enterTypeTextField;
    private LabeledTextField enterOptionTextField;
    private JButton saveButton;
   
	public FrameEngineModelMapPanel(TabbedMainWindow mainWindow){
		super("FrameEngineModelMap", KeyEvent.VK_M, mainWindow);
		this.getMainWindow().setSize(1200, 800);
	}

	@Override
	public void onTabSelected() {
		try {
			if (this.isInitialized)	return;
			this.initComponents();
			this.addListeners();
			this.loadData();
			this.isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start FrameEngineModelMap.");
			setErrorMessage("Exception to start FrameEngineModelMap." + e.toString());
		}	
	}
	
	private void initComponents() {
		setLayout(new MigLayout());
		this.add(this.createYmtoSelectionPanel(), "spanx, center, wrap");
		this.add(this.createOrigYmtoTable(), "spany2, width 200:1920:1920, grow, growy");
		this.add(this.createAltEngineYmtoTable(), "width 300:400:400, grow, growy, wrap");
		this.add(this.createEnterYmtoPanel(), "width 300:400:400, growx, wrap");
	}
	
	private JPanel createYmtoSelectionPanel() {
		JPanel panel = new JPanel(new MigLayout());
		int height = this.getMainWindow().getHeight()/3;
		panel.setSize(panel.getWidth(), height);
		this.ymtoSelectPanel = new ProductSpecSelectionPanel(ProductType.FRAME.getProductName());
		this.ymtoSelectPanel.remove(4);
		this.ymtoSelectPanel.remove(4);
		this.ymtoSelectPanel.setBorder(new TitledBorder("Frame YMTO Filter"));
		for(LabeledListBox lbox : ymtoSelectPanel.getColumnBoxsList()){
			((BorderLayout)lbox.getLayout()).setVgap(0);
			lbox.getLabel().setHorizontalAlignment(JLabel.CENTER);
		}
		this.ymtoSelectPanel.getPanel("Model_Type").getLabel().setText("Type");
		panel.add(this.ymtoSelectPanel,"width 600:600:600, height 200:200:200");
		return panel;
	}
	
	private ObjectTablePane<FrameEngineModelMap> createOrigYmtoTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Frm Year", "id.frmModelYearCode");
		mapping.put("Frm Model", "id.frmModelCode");
		mapping.put("Frm Type", "id.frmModelTypeCode");
		mapping.put("Frm Option", "id.frmModelOptionCode");
		mapping.put("Eng Year", "id.engModelYearCode");
		mapping.put("Eng Model", "id.engModelCode");
		mapping.put("Eng Type", "id.engModelTypeCode");
		mapping.put("Eng Option", "id.engModelOptionCode");
		this.origYmtoTable = new ObjectTablePane<FrameEngineModelMap>(mapping.get(), true, false);
		this.origYmtoTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.origYmtoTable.getTable().setCellSelectionEnabled(false);
		this.origYmtoTable.getTable().setDefaultRenderer(Object.class, new CellRenderer());
		this.origYmtoTable.getTable().setName("OriginalYmtoTable");
		this.origYmtoTable.setBorder(new TitledBorder("Original YMTOs"));
		this.origYmtoTable.getTable().setRowSelectionAllowed(true);
		return origYmtoTable;
	}
	
	private ObjectTablePane<FrameEngineModelMap> createAltEngineYmtoTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Year", "id.engModelYearCode");
		mapping.put("Model", "id.engModelCode");
		mapping.put("Type", "id.engModelTypeCode");
		mapping.put("Option", "id.engModelOptionCode");
		this.altEngineYmtoTable = new ObjectTablePane<FrameEngineModelMap>(mapping.get(), true, false);
		this.altEngineYmtoTable.getTable().setFocusable(false);
		this.altEngineYmtoTable.getTable().setName("AltEngineYmtoTable");
		this.altEngineYmtoTable.setBorder(new TitledBorder("Alternative Engine YMTOs"));
		this.altEngineYmtoTable.getTable().setRowSelectionAllowed(false);
		return altEngineYmtoTable;
	}
	
	private void refreshFrameYmtoTable() {
		String selectedYear = this.ymtoSelectPanel.getSelectedModelYearCode();
		String selectedModel = this.ymtoSelectPanel.getSelectedModelCode();
		String selectedType = this.ymtoSelectPanel.getSelectedModelTypeCode();
		String selectedOption = this.ymtoSelectPanel.getSelectedModelOptionCode();
		List<FrameEngineModelMap> filteredRecords = getFilteredRecords(selectedYear, selectedModel, selectedType, selectedOption);
		this.origYmtoTable.reloadData(filteredRecords);
	}

	private void refreshAltEngineYmtoTable() {
		FrameEngineModelMap selectedFrameYmto= this.origYmtoTable.getSelectedItem();
		if (selectedFrameYmto == null) return;
		this.altEngineMtoRecords = new SortedArrayList<FrameEngineModelMap>();
		for (FrameEngineModelMap record : this.frmEngModelMapRecords) {
			HashSet<String> engModels = new HashSet<String>();
			if (this.frameEnginesYmtoMap.containsKey(record.getFrameYmto()))
				engModels = this.frameEnginesYmtoMap.get(record.getFrameYmto());
			
			if (selectedFrameYmto.compareFrameYmto(record.getFrameYmto()) &&
				(!(engModels.contains(record.getEngineYmto())))) {
				altEngineMtoRecords.add(record);
			}
		}
		this.altEngineYmtoTable.reloadData(altEngineMtoRecords);
	}
	
	protected JPanel createEnterYmtoPanel(){
		this.createYmtoTextFields();
		JPanel enterYmtoPanel = new JPanel(new MigLayout("align 50% 50%","[grow,fill]"));		
		enterYmtoPanel.add(this.enterYearTextField, YMTO_FIELDS_WIDTH_PARAM);
		enterYmtoPanel.add(this.enterModelTextField, YMTO_FIELDS_WIDTH_PARAM);
		enterYmtoPanel.add(this.enterTypeTextField, YMTO_FIELDS_WIDTH_PARAM);
		enterYmtoPanel.add(this.enterOptionTextField, YMTO_FIELDS_WIDTH_PARAM + ", wrap");
		enterYmtoPanel.add(this.createAddMtoButton(), "spanx, center, width 70:70:70");
		enterYmtoPanel.setBorder(new TitledBorder("Add Alternative YMTO"));
		return enterYmtoPanel;
	}
	
	protected void createYmtoTextFields() {
		this.enterYearTextField = this.createTextField("Year", 1, false);
		this.enterModelTextField = this.createTextField("Model", 3, false);
		this.enterTypeTextField = this.createTextField("Type", 3, false);
		this.enterOptionTextField = this.createTextField("Option", 2, false);
	}
	
    protected LabeledTextField createTextField(String title, final int maxChar, Boolean horizontal){
    	LabeledTextField labeledTextField = new LabeledTextField(title,horizontal);
    	final JTextField textField = labeledTextField.getComponent();
    	textField.setName(title + "TextField");
    	textField.setHorizontalAlignment(JTextField.CENTER);
    	textField.addKeyListener(new KeyAdapter() {
    		@Override
    	    public void keyTyped(KeyEvent e) { 
    	        if (textField.getText().length() >= maxChar)
    	            e.consume(); 
    	    }  
    	});
    	labeledTextField.getLabel().setFont(new Font("Dialog", Font.BOLD, 12));
    	((BorderLayout)labeledTextField.getLayout()).setVgap(0);
    	if (!horizontal) labeledTextField.getLabel().setAlignmentX(CENTER_ALIGNMENT);
    	labeledTextField.getLabel().setHorizontalAlignment(SwingConstants.CENTER);
    	labeledTextField.setEnabled(false);
    	labeledTextField.setBorder(null);
    	return labeledTextField;
    }
	private JButton createAddMtoButton(){
		this.saveButton = new JButton("Save");
		this.saveButton.setName("SaveButton");
		this.saveButton.setEnabled(false);
		return this.saveButton;
	}
	
	private void loadData() {
		StringBuilder errors = new StringBuilder();
		SortedArrayList<FrameSpec> frameSpecs = new SortedArrayList<FrameSpec>();
		frameSpecs.addAll(ServiceFactory.getDao(FrameSpecDao.class).findAll());
		this.frameEnginesYmtoMap = new TreeMap<String, HashSet<String>>();
		for (FrameSpec frameSpec : frameSpecs) {
			StringBuilder frameYmto = new StringBuilder();
			try {
				frameYmto.append(frameSpec.getModelYearCode().trim());
				frameYmto.append(frameSpec.getModelCode().trim());
				frameYmto.append(frameSpec.getModelTypeCode().trim());
				frameYmto.append(frameSpec.getModelOptionCode().trim());
			} catch (NullPointerException e) {
				String errMsg = "FrameSpec [" + frameSpec.getProductSpecCode() + "] record skipped. Engine or frame YMTOs contain \"null\" values. ";
				Logger.getLogger().error(errMsg);
				errors.append(errMsg + "\n");
				continue;
			}
			String engineYmto = frameSpec.getEngineMto().trim();
			if (this.frameEnginesYmtoMap.containsKey(frameYmto.toString())) { 
				this.frameEnginesYmtoMap.get(frameYmto.toString()).add(engineYmto);
			} else {
				HashSet<String> engineYmtos = new HashSet<String>();
				engineYmtos.add(engineYmto);
				this.frameEnginesYmtoMap.put(frameYmto.toString(), engineYmtos);
			}
		}
		this.frmEngModelMapRecords = new SortedArrayList<FrameEngineModelMap>();
		this.frmEngModelMapRecords.addAll(ServiceFactory.getDao(FrameEngineModelMapDao.class).findAll());
		this.setErrorMessage(errors.toString());
	}
	
	private List<FrameEngineModelMap> getFilteredRecords(String year, String model, String type, String option){
		List<FrameEngineModelMap> filteredRecords = new SortedArrayList<FrameEngineModelMap>();
		for (Map.Entry<String, HashSet<String>> frameEnginesYmto : this.frameEnginesYmtoMap.entrySet()) {
			String frmYear = ProductSpecUtil.extractModelYearCode(frameEnginesYmto.getKey());
			String frmModel = ProductSpecUtil.extractModelCode(frameEnginesYmto.getKey());
			String frmType = ProductSpecUtil.extractModelTypeCode(frameEnginesYmto.getKey());
			String frmOption = ProductSpecUtil.extractModelOptionCode(frameEnginesYmto.getKey());
			
			Object[] engineYmtos = frameEnginesYmto.getValue().toArray();
			String engYear = ""; 
			String engModel = "";
			String engType = "";
			String engOption = "";
			if (engineYmtos.length == 1) {
				String engineYmto = (String)engineYmtos[0];
				engYear = ProductSpecUtil.extractModelYearCode(engineYmto);
				engModel = ProductSpecUtil.extractModelCode(engineYmto);
				engType = ProductSpecUtil.extractModelTypeCode(engineYmto);
				engOption = ProductSpecUtil.extractModelOptionCode(engineYmto);
			} else if (engineYmtos.length > 1 ) {
				engYear = "Mult. models";
			}
			
			if ((year == null || year.equals("*") || frmYear.equals(year)) &&
				(model == null || model.equals("*") || frmModel.equals(model)) &&
				(type == null || type.equals("*") || frmType.equals(type)) &&
				(option == null || option.equals("*") || frmOption.equals(option))){
				FrameEngineModelMap validRecord = new FrameEngineModelMap(	frmYear,frmModel,frmType,frmOption,
																	engYear,engModel,engType,engOption);
				filteredRecords.add(validRecord);
			}
		}	
		return filteredRecords;
	}
	
	private void addListeners() {
		for(LabeledListBox lbox : ymtoSelectPanel.getColumnBoxsList()){
			lbox.getComponent().addListSelectionListener(
				new ListSelectionListener(){
					public void valueChanged(ListSelectionEvent e){
						getMainWindow().clearMessage();
						origYmtoTable.clearSelection();
						refreshFrameYmtoTable();
					}
				}
			);
		}
		this.origYmtoTable.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				frameSelected();
            }
		});
		this.saveButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.saveButton)) this.saveAltYmto();
	}
	
	private void frameSelected() {
		FrameEngineModelMap record;
		if ((record = origYmtoTable.getSelectedItem()) == null) {
    		enterYearTextField.setEnabled(false);
    		enterModelTextField.setEnabled(false);
    		enterTypeTextField.setEnabled(false);
    		enterOptionTextField.setEnabled(false);
    		saveButton.setEnabled(false);
    		getMainWindow().clearMessage();
    	} else {
    		enterYearTextField.setEnabled(true);
    		enterModelTextField.setEnabled(true);
    		enterTypeTextField.setEnabled(true);
    		enterOptionTextField.setEnabled(true);
    		saveButton.setEnabled(true);
    		refreshAltEngineYmtoTable();
    		HashSet<String> engModels = frameEnginesYmtoMap.get(record.getFrameYmto());
    		if (engModels.size() > 1){ 
    			getMainWindow().setErrorMessage("Multiple ENGINE YMTOs exist for FRAME YMTO [" + record.getFrameYmto() + 
    											"] in frame model master table: " + engModels.toString());
    		} else if (StringUtils.isBlank(record.getEngineYmto())) {
    			getMainWindow().setErrorMessage("Original ENGINE YMTO is missing in frame model master table for " +
    											"FRAME YMTO [" + record.getFrameYmto() + "].");
    		} else {
    			getMainWindow().clearMessage();
    		}
    	}
	}
	
	public void saveAltYmto() {
		String enteredYear = this.enterYearTextField.getComponent().getText().trim().toUpperCase();
		String enteredModel = this.enterModelTextField.getComponent().getText().trim().toUpperCase();
		String enteredType = this.enterTypeTextField.getComponent().getText().trim().toUpperCase();
		String enteredOption = this.enterOptionTextField.getComponent().getText().trim().toUpperCase();
		if (!this.altYmtoIsValid(enteredYear, enteredModel, enteredType, enteredOption)) return;
		
		FrameEngineModelMap selectedFrameYmto = (FrameEngineModelMap) this.origYmtoTable.getSelectedItem().deepCopy();
		selectedFrameYmto.getId().setEngModelYearCode(enteredYear);
		selectedFrameYmto.getId().setEngModelCode(enteredModel);
		selectedFrameYmto.getId().setEngModelTypeCode(enteredType);
		selectedFrameYmto.getId().setEngModelOptionCode(enteredOption);
		selectedFrameYmto.setUpdtUserId(this.getMainWindow().getUserId());
		
		if (!MessageDialog.confirm(this, "Assign ENGINE YMTO [" + 
				enteredYear + " " + enteredModel + " " + enteredType + " " + enteredOption +
				"] to FRAME YMTO [" +
				selectedFrameYmto.getId().getFrmModelYearCode().toUpperCase() + 
				" " + selectedFrameYmto.getId().getFrmModelCode().toUpperCase() +
				" " + selectedFrameYmto.getId().getFrmModelTypeCode().toUpperCase() +
				" " + selectedFrameYmto.getId().getFrmModelOptionCode().toUpperCase() + "] ?")) return;
		ServiceFactory.getDao(FrameEngineModelMapDao.class).save(selectedFrameYmto);
		this.loadData();
		
		this.enterYearTextField.clear();
		this.enterModelTextField.clear();
		this.enterTypeTextField.clear();
		this.enterOptionTextField.clear();
		
		this.getMainWindow().setMessage("ENGINE YMTO [" + 
				enteredYear + " " + enteredModel + " " + enteredType + " " + enteredOption +
				"] assigned to FRAME YMTO [" +
				selectedFrameYmto.getId().getFrmModelYearCode() + 
				" " + selectedFrameYmto.getId().getFrmModelCode() +
				" " + selectedFrameYmto.getId().getFrmModelTypeCode() +
				" " + selectedFrameYmto.getId().getFrmModelOptionCode() + "].");
		this.refreshFrameYmtoTable();
		this.refreshAltEngineYmtoTable();
	}
	
	private Boolean altYmtoIsValid(String enteredYear, String enteredModel, String enteredType, String enteredOption) {
		if (StringUtils.isBlank(enteredYear)) {
			this.getMainWindow().setErrorMessage("Engine model year code is missing.");
			return false;
		} else if (StringUtils.isBlank(enteredModel)) {
			this.getMainWindow().setErrorMessage("Engine model code is missing.");
			return false;
		} else if (StringUtils.isBlank(enteredType)) {
			this.getMainWindow().setErrorMessage("Engine model type code is missing.");
			return false;
		} else if (StringUtils.isBlank(enteredOption)) {
			this.getMainWindow().setErrorMessage("Engine model option code is missing.");
			return false;
		}
		
		if (enteredYear.length() != 1) {
			this.getMainWindow().setErrorMessage("Engine model YEAR code must be 1 character long.");
			return false;
		} else if (enteredModel.length() != 3) {
			this.getMainWindow().setErrorMessage("Engine MODEL code must be 3 characters long.");
			return false;
		} else if (enteredType.length() != 3) {
			this.getMainWindow().setErrorMessage("Engine model TYPE code must be 3 characters long.");
			return false;
		} else if (enteredOption.length() != 2) {
			this.getMainWindow().setErrorMessage("Engine model OPTION code must be 2 characters long.");
			return false;
		} 
		else if (this.origYmtoTable.getSelectedItem().compareEngineYmto(enteredYear,enteredModel,enteredType,enteredOption)) {
			this.getMainWindow().setErrorMessage("Alternative ENGINE YMTO [" + 
					enteredYear + " " + enteredModel + " " + enteredType + " " + enteredOption + 
					"] equals original ENGINE YMTO.");
			return false;
		}
		for (FrameEngineModelMap record : this.altEngineMtoRecords) {
			if (record.compareEngineYmto(enteredYear,enteredModel,enteredType,enteredOption)){
				this.getMainWindow().setErrorMessage("Alternative ENGINE YMTO [" + 
						enteredYear + " " + enteredModel + " " + enteredType + " " + enteredOption + 
						"] already exists.");
				return false;
			}
		}
		return true;
	}
	
	public class CellRenderer extends DefaultTableCellRenderer {    
		private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			String cell = (String)table.getValueAt(row, 4);
			if(StringUtils.isBlank(cell)) {       
				if (isSelected)	this.setBackground(Color.red);
				else this.setBackground(Color.pink);
				this.setForeground(table.getForeground());
			} else if (isSelected) {
	            this.setBackground(table.getSelectionBackground());
	            this.setForeground(table.getSelectionForeground());
			} else if (column > 3){
				this.setBackground(new Color(245,245,220));
				this.setForeground(table.getForeground());
			} else {
				this.setBackground(table.getBackground());
				this.setForeground(table.getForeground());
			}
			return this;
		}
	}
}
