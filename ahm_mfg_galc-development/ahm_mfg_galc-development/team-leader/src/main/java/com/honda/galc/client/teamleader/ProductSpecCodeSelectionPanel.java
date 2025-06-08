package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.event.ProductSpecCodeSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>ProductSpecCodeSelectionPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSpecCodeSelectionPanel description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Mar 31, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Mar 31, 2012
 */
public class ProductSpecCodeSelectionPanel  extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;

	private LabeledComboBox productTypeComboBox;
	private LabeledComboBox modelYearComboBox;
	private LabeledComboBox modelComboBox;
	private LabeledComboBox modelTypeComboBox;
	private LabeledComboBox optionComboBox;
	private LabeledComboBox extColorComboBox;
	private LabeledComboBox intColorComboBox;
	
	private List<ProductTypeData> productTypes;
	private ProductSpecData productSpecData;
	
	private String currentProductType;
	private String currentYear;
	private String currentModel;
	private String currentModelType;
	private String currentOptions;
	private String currentExtColor;
	private String currentIntColor;
	
	
	public ProductSpecCodeSelectionPanel() {
		super();

		initialize();
		initComponent();
		addActionListeners();
	}

	private void initialize() {
		try{
			productTypes = ServiceFactory.getDao(ProductTypeDao.class).findAll();
			filterProductTypes();
        }catch(Exception e){
           Logger.getLogger().error(e, " exception to find product types.");
        }
	}

	private void filterProductTypes() {
		// Engine, Frame and Knuckles all have their own product spec table and data is populated by 
		// OIF so should not touch those data
		List<ProductTypeData> removeTypes = new ArrayList<ProductTypeData>();
		for(ProductTypeData type: productTypes){
			if(StringUtils.isEmpty(type.getOwnerProductTypeName()) || 
					type.getProductTypeName().trim().equals(ProductType.KNUCKLE.toString()))
				removeTypes.add(type);
		}
		
		productTypes.removeAll(removeTypes);
	}

	private void initComponent() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 1, 5));
        add(getProductTypeComboBox());
        add(getModelYearComboBox());
        add(getModelComboBox());
        add(getModelTypeComboBox());
        add(getOptionComboBox());
        add(getExtColorComboBox());
        add(getIntColorComboBox());
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
	}

	private void addActionListeners() {
		getProductTypeComboBox().getComponent().addActionListener(this);
		getModelYearComboBox().getComponent().addActionListener(this);
		getModelComboBox().getComponent().addActionListener(this);
		getModelTypeComboBox().getComponent().addActionListener(this);
		getOptionComboBox().getComponent().addActionListener(this);
		getExtColorComboBox().getComponent().addActionListener(this);
		getIntColorComboBox().getComponent().addActionListener(this);
		
	}


	public LabeledComboBox getProductTypeComboBox() {
		if(productTypeComboBox == null){
			productTypeComboBox = new LabeledComboBox("Product Type");
            ComboBoxModel<ProductTypeData> model = new ComboBoxModel<ProductTypeData>(productTypes,"getProductTypeName");
            productTypeComboBox.getComponent().setModel(model);
            productTypeComboBox.getComponent().setSelectedIndex(-1);
            productTypeComboBox.getComponent().setRenderer(model);
            productTypeComboBox.getComponent().setPreferredSize(new Dimension(100, 25));
		}
		return productTypeComboBox;
	}

	public LabeledComboBox getModelYearComboBox() {
		if(modelYearComboBox == null){
			modelYearComboBox = new LabeledComboBox("Year");
			modelYearComboBox.getComponent().setPreferredSize(new Dimension(40,25));
		}
		return modelYearComboBox;
	}

	public LabeledComboBox getModelComboBox() {
		if(modelComboBox == null){
			modelComboBox = new LabeledComboBox("Model");
			modelComboBox.getComponent().setPreferredSize(new Dimension(60,25));
		}
		return modelComboBox;
	}

	public LabeledComboBox getModelTypeComboBox() {
		if(modelTypeComboBox == null){
			modelTypeComboBox = new LabeledComboBox("Model Type");
			modelTypeComboBox.getComponent().setPreferredSize(new Dimension(50,25));
		}
		return modelTypeComboBox;
	}

	public LabeledComboBox getOptionComboBox() {
		if(optionComboBox == null){
			optionComboBox = new LabeledComboBox("Option");
			optionComboBox.getComponent().setPreferredSize(new Dimension(50,25));
		}
		return optionComboBox;
	}

	public LabeledComboBox getExtColorComboBox() {
		if(extColorComboBox == null){
			extColorComboBox = new LabeledComboBox("Ext Color");
			extColorComboBox.getComponent().setPreferredSize(new Dimension(90,25));
		}
		return extColorComboBox;
	}

	public LabeledComboBox getIntColorComboBox() {
		if(intColorComboBox == null){
			intColorComboBox = new LabeledComboBox("Int Color");
			intColorComboBox.getComponent().setPreferredSize(new Dimension(50,25));
		}
		return intColorComboBox;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(getProductTypeComboBox().getComponent())) productTypeChanged();
        else if(e.getSource().equals(getModelYearComboBox().getComponent())) modelYearChanged();
        else if(e.getSource().equals(getModelComboBox().getComponent())) modelChanged();
        else if(e.getSource().equals(getModelTypeComboBox().getComponent())) modelTypeChanged();
        else if(e.getSource().equals(getOptionComboBox().getComponent())) optionChanged();
        else if(e.getSource().equals(getExtColorComboBox().getComponent())) extColorChanged();
        else if(e.getSource().equals(getIntColorComboBox().getComponent())) intColorChanged();
		
	}

	private void productTypeChanged() {
		
		 ProductTypeData productType = (ProductTypeData) getProductTypeComboBox().getComponent().getSelectedItem();
		 if(productType == null) return;
		 
		 currentProductType = productType.getProductTypeName();
		 loadProductSpecs();
		 
		 if(productType != null && !StringUtils.isEmpty(currentProductType)){
			 ComboBoxModel<String> years = new ComboBoxModel<String>(productSpecData.getModelYearCodes());
			 getModelYearComboBox().getComponent().setModel(years);
			 getModelYearComboBox().getComponent().setSelectedIndex(-1);
			 getModelYearComboBox().getComponent().setRenderer(years);
			 
			 sendProductSpecSelectingEvent(SelectionEvent.MODEL_YEAR_SELECTED);
		 } 
	}

	public void loadProductSpecs() {
		
		productSpecData= new ProductSpecData(currentProductType);
		productSpecData.loadProductSpec();
	}

	private void modelYearChanged() {
		 String selectedYear = (String) getModelYearComboBox().getComponent().getSelectedItem();
		 
		 if (!StringUtils.isEmpty(selectedYear)){
			 if(!StringUtils.equals(selectedYear, currentYear)) {
				 currentYear = selectedYear;
				 currentModel = null;
				 currentModelType = null;
				 currentOptions = null;
				 currentExtColor = null;
				 currentIntColor = null;
				 
				 ComboBoxModel<String> models = new ComboBoxModel<String>(getModelCodes(selectedYear));
				 getModelComboBox().getComponent().setModel(models);
				 getModelComboBox().getComponent().setSelectedIndex(-1);
				 getModelComboBox().getComponent().setRenderer(models);

				 sendProductSpecSelectingEvent(SelectionEvent.MODEL_YEAR_SELECTED);
			 }
		 } else {
			 currentYear = null;
			 resetModelComboBox();
			 resetModelTypeComboBox();
			 resetOptionComboBox();
			 resetExtColorComboBox();
			 resetIntColorComboBox();
		 }
		
	}

	private List<String> getModelCodes(String selectedYear) {
		List<String> modelCodes = productSpecData.getModelCodes(selectedYear);
		modelCodes.add(" ");
		return modelCodes;
	}


	private void modelChanged() {
		String selectedYear = (String) getModelYearComboBox().getComponent().getSelectedItem();
		String selectedModel = (String) getModelComboBox().getComponent().getSelectedItem();
		
		if(!StringUtils.isEmpty(selectedModel)){
			if(!StringUtils.equals(selectedModel, currentModel)){

				currentModel = selectedModel;
				currentModelType = null;
				currentOptions = null;
				currentExtColor = null;
				currentIntColor = null;
				ComboBoxModel<String> modelTypes = new ComboBoxModel<String>(getModelTypeCodes(selectedYear, selectedModel));
				getModelTypeComboBox().getComponent().setModel(modelTypes);
				getModelTypeComboBox().getComponent().setSelectedIndex(-1);
				getModelTypeComboBox().getComponent().setRenderer(modelTypes);

				sendProductSpecSelectingEvent(SelectionEvent.MODEL_SELECTED);
			}
		} else {
			resetModelTypeComboBox();
			resetOptionComboBox();
			resetExtColorComboBox();
			resetIntColorComboBox();
		}
	}

	private List<String> getModelTypeCodes(String selectedYear, String selectedModel) {
		  List<String> modelTypeCodes = productSpecData.getModelTypeCodes(selectedYear, selectedModel);
		  modelTypeCodes.add(" ");
		  return modelTypeCodes;
	}


	private void modelTypeChanged() {
		String selectedYear = (String) getModelYearComboBox().getComponent().getSelectedItem();
		String selectedModel = (String) getModelComboBox().getComponent().getSelectedItem();
		String selectedModelType = (String)getModelTypeComboBox().getComponent().getSelectedItem();
		
		if(!StringUtils.isEmpty(selectedModelType)){
			if(!StringUtils.equals(selectedModelType, currentModelType)){
				currentModelType = selectedModelType;
				currentOptions = null;
				currentExtColor = null;
				currentIntColor = null;
				ComboBoxModel<String> options = new ComboBoxModel<String>(getOptionCodes(selectedYear, selectedModel, selectedModelType)); 
				getOptionComboBox().getComponent().setModel(options);
				getOptionComboBox().getComponent().setSelectedIndex(-1);
				getOptionComboBox().getComponent().setRenderer(options);

				sendProductSpecSelectingEvent(SelectionEvent.MODEL_TYPE_SELECTED);
			}
		} else {
			resetOptionComboBox();
			resetExtColorComboBox();
			resetIntColorComboBox();
		}
	}

	private List<String> getOptionCodes(String selectedYear, String selectedModel, String selectedModelType) {
		List<String> modelOptionCodes = productSpecData.getModelOptionCodes(selectedYear, selectedModel, new String[]{selectedModelType});
		modelOptionCodes.add(" ");
		return modelOptionCodes;
	}


	private void optionChanged() {
		String selectedYear = (String) getModelYearComboBox().getComponent().getSelectedItem();
		String selectedModel = (String) getModelComboBox().getComponent().getSelectedItem();
		String selectedModelType = (String)getModelTypeComboBox().getComponent().getSelectedItem();
		String option = (String)getOptionComboBox().getComponent().getSelectedItem();
		
		if(!StringUtils.isEmpty(option)){
			if(!StringUtils.equals(option, currentOptions)){
				currentOptions = option;
				currentExtColor = null;
				currentIntColor = null;
				ComboBoxModel<String> extColors = new ComboBoxModel<String>(getExtColorCodes(selectedYear, selectedModel,
						selectedModelType, option));
				getExtColorComboBox().getComponent().setModel(extColors);
				getExtColorComboBox().getComponent().setSelectedIndex(-1);
				getExtColorComboBox().getComponent().setRenderer(extColors);

				sendProductSpecSelectingEvent(SelectionEvent.MODEL_OPTION_SELECTED);
			}
		} else {
			resetExtColorComboBox();
			resetIntColorComboBox();
		}
		
	}

	private List<String> getExtColorCodes(String selectedYear, String selectedModel, String selectedModelType, String option) {
		 List<String> modelExtColorCodes = productSpecData.getModelExtColorCodes(selectedYear, selectedModel, 
				 new String[]{selectedModelType}, new String[]{option});
		 modelExtColorCodes.add(" ");
		 return modelExtColorCodes;
	}



	private void extColorChanged() {
		String selectedYear = (String) getModelYearComboBox().getComponent().getSelectedItem();
		String selectedModel = (String) getModelComboBox().getComponent().getSelectedItem();
		String selectedModelType = (String)getModelTypeComboBox().getComponent().getSelectedItem();
		String option = (String)getOptionComboBox().getComponent().getSelectedItem();
		String extColor = (String)getExtColorComboBox().getComponent().getSelectedItem();
		
		if(!StringUtils.isEmpty(extColor)){
			if(!StringUtils.equals(extColor, currentExtColor)){

				currentExtColor = extColor;
				currentIntColor = null;
				ComboBoxModel<String> intColors = new ComboBoxModel<String>(getIntColors(selectedYear, selectedModel, selectedModelType,
						option, extColor));
				getIntColorComboBox().getComponent().setModel(intColors);
				getIntColorComboBox().getComponent().setSelectedIndex(-1);
				getIntColorComboBox().getComponent().setRenderer(intColors);

				sendProductSpecSelectingEvent(SelectionEvent.EXT_COLOR_SELECTED);
			}
		} else {
			resetIntColorComboBox();
		}
		
	}

	private List<String> getIntColors(String selectedYear, String selectedModel, String selectedModelType, 
			String option,	String extColor) {
		 List<String> modelIntColorCodes = productSpecData.getModelIntColorCodes(selectedYear, selectedModel, 
				new String[]{selectedModelType},new String[]{option}, new String[]{extColor});
		 modelIntColorCodes.add(" ");
		 
		 return modelIntColorCodes;
	}



	private void intColorChanged() {
		String intColor = (String)getIntColorComboBox().getComponent().getSelectedItem();
		if(!StringUtils.isEmpty(intColor)){
			if(!StringUtils.equals(intColor, currentIntColor)){
				currentIntColor = intColor;
				sendProductSpecSelectingEvent(SelectionEvent.INT_COLOR_SELECTED);
			}
		} else {
			currentIntColor = null;
		}

	}
	
	public List<ProductSpecCode> getSelectedProductSepecCode(){
		return getSelectedProductSepecCode(false);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ProductSpecCode> getSelectedProductSepecCode(boolean reload){
		ProductTypeData productType = (ProductTypeData) getProductTypeComboBox().getComponent().getSelectedItem();
		String selectedYear = (String) getModelYearComboBox().getComponent().getSelectedItem();
		String selectedModel = (String) getModelComboBox().getComponent().getSelectedItem();
		String selectedModelType = (String)getModelTypeComboBox().getComponent().getSelectedItem();
		String selectedOption = (String)getOptionComboBox().getComponent().getSelectedItem();
		String selectedExtColor = (String)getExtColorComboBox().getComponent().getSelectedItem();
		String selectedIntColor = (String)getIntColorComboBox().getComponent().getSelectedItem();
		
		
		if(reload)
			loadProductSpecs();
		
		
		List<ProductSpecCode> selectedSpecs = (List<ProductSpecCode>)productSpecData.getProductSpecs();
		
		if(StringUtils.isEmpty(productType.getProductTypeName())) return selectedSpecs;
		selectedSpecs = filterBySelecteProductType(productType.getProductTypeName(), selectedSpecs);
		
		if(StringUtils.isEmpty(selectedYear)) return selectedSpecs;

		selectedSpecs = filterBySelectedYear(selectedYear, selectedSpecs);
		if(StringUtils.isEmpty(selectedModel)) return selectedSpecs;
		
		selectedSpecs = filterBySelectedModel(selectedModel, selectedSpecs);
		if(StringUtils.isEmpty(selectedModelType)) return selectedSpecs;
		
		selectedSpecs = filterBySelectedModelType(selectedModelType, selectedSpecs);
		if(StringUtils.isEmpty(selectedOption)) return selectedSpecs;
		
		selectedSpecs = filterBySelectedOption(selectedOption, selectedSpecs);
		if(StringUtils.isEmpty(selectedExtColor)) return selectedSpecs;
		
		selectedSpecs = filterBySelectedExtColor(selectedExtColor, selectedSpecs);
		if(StringUtils.isEmpty(selectedIntColor)) return selectedSpecs;
	
		selectedSpecs = filterBySelectedIntColor(selectedIntColor, selectedSpecs);
		
		return selectedSpecs;
	}

	private List<ProductSpecCode> filterBySelectedIntColor(String selectedIntColor, List<ProductSpecCode> selectedSpecs) {
		
		if(isWildCard(selectedIntColor)) return selectedSpecs;
		
		List<ProductSpecCode> list = new ArrayList<ProductSpecCode>();
		for(ProductSpecCode spec : (List<ProductSpecCode>)selectedSpecs){
			if(selectedIntColor.equals(spec.getIntColorCode()))
				list.add((ProductSpecCode)spec);
		}
		return list;
	}

	private List<ProductSpecCode> filterBySelectedExtColor(String selectedExtColor, List<ProductSpecCode> selectedSpecs) {
		
		if(isWildCard(selectedExtColor)) return selectedSpecs;
		
		List<ProductSpecCode> list = new ArrayList<ProductSpecCode>();
		for(ProductSpecCode spec : (List<ProductSpecCode>)selectedSpecs){
			if(selectedExtColor.equals(spec.getExtColorCode()))
				list.add((ProductSpecCode)spec);
		}
		return list;
	}

	private List<ProductSpecCode> filterBySelectedOption(String selectedOption, List<ProductSpecCode> selectedSpecs) {
		
		if(isWildCard(selectedOption)) return selectedSpecs;
		
		List<ProductSpecCode> list = new ArrayList<ProductSpecCode>();
		for(ProductSpec spec : selectedSpecs){
			if(selectedOption.equals(spec.getModelOptionCode()))
				list.add((ProductSpecCode)spec);
		}
		return list;
	}

	private List<ProductSpecCode> filterBySelectedModelType(String selectedModelType, List<ProductSpecCode> selectedSpecs) {
		
		if(isWildCard(selectedModelType)) return selectedSpecs;
		
		List<ProductSpecCode> list = new ArrayList<ProductSpecCode>();
		for(ProductSpec spec : selectedSpecs){
			if(selectedModelType.equals(spec.getModelTypeCode()))
				list.add((ProductSpecCode)spec);
		}
		return list;
	}

	private List<ProductSpecCode> filterBySelectedModel(String selectedModel, List<ProductSpecCode> selectedSpecs) {
		
		if(isWildCard(selectedModel)) return selectedSpecs;
		
		List<ProductSpecCode> list = new ArrayList<ProductSpecCode>();
		for(ProductSpec spec : selectedSpecs){
			if(selectedModel.equals(spec.getModelCode()))
				list.add((ProductSpecCode)spec);
		}
		return list;
	}

	private List<ProductSpecCode> filterBySelecteProductType(String selectedProdType, List<ProductSpecCode> selectedSpecs) {

		if(isWildCard(selectedProdType)) return (List<ProductSpecCode>)selectedSpecs;

		List<ProductSpecCode> list = new ArrayList<ProductSpecCode>();
		if(selectedSpecs != null){
			for(ProductSpecCode spec : selectedSpecs){
				if(StringUtils.equals(selectedProdType.trim(), spec.getId().getProductType().trim()))
					list.add((ProductSpecCode)spec);
			}
		}
		return list;
	}
	

	private List<ProductSpecCode> filterBySelectedYear(String selectedYear, List<ProductSpecCode> selectedSpecs) {
		
		if(isWildCard(selectedYear)) return (List<ProductSpecCode>)selectedSpecs;
		
		List<ProductSpecCode> list = new ArrayList<ProductSpecCode>();
		for(ProductSpec spec : selectedSpecs){
			if(selectedYear.equals(spec.getModelYearCode()))
				list.add((ProductSpecCode)spec);
		}
		return list;
	}
	
	
	private void resetModelComboBox() {
		ComboBoxModel<String> empty = new ComboBoxModel<String>(new String[]{});
		getModelComboBox().getComponent().setModel(empty);
		currentModel = null;
	}
	
	private void resetModelTypeComboBox() {
		ComboBoxModel<String> empty = new ComboBoxModel<String>(new String[]{});
		getModelTypeComboBox().getComponent().setModel(empty);
		currentModelType = null;
	}
	
	private void resetOptionComboBox() {
		ComboBoxModel<String> empty = new ComboBoxModel<String>(new String[]{});
		getOptionComboBox().getComponent().setModel(empty);
		currentOptions = null;
	}
	
	private void resetExtColorComboBox() {
		ComboBoxModel<String> empty = new ComboBoxModel<String>(new String[]{});
		getExtColorComboBox().getComponent().setModel(empty);
		currentExtColor = null;
	}
	
	private void resetIntColorComboBox() {
		ComboBoxModel<String> empty = new ComboBoxModel<String>(new String[]{});
		getIntColorComboBox().getComponent().setModel(empty);
		currentIntColor = null;
	}
	
	private boolean isWildCard(String selected) {
		return "*".equals(selected);
	}
	
	private void sendProductSpecSelectingEvent(int eventType) {
    	EventBus.publish(new ProductSpecCodeSelectionEvent(this,eventType));
    }

	public String getModelYearCode() {
		return (String)getModelYearComboBox().getComponent().getSelectedItem();
	}
	
	public String getProductType() {
		ProductTypeData productTypeData = (ProductTypeData)getProductTypeComboBox().getComponent().getSelectedItem();
		return productTypeData == null ? null : productTypeData.getProductTypeName();
	}

	public String getModelCode() {
		return (String)getModelComboBox().getComponent().getSelectedItem();
	}

	public String getModelTypeCode() {
		return (String)getModelTypeComboBox().getComponent().getSelectedItem();
	}

	public String getModelOptionCode() {
		return (String)getOptionComboBox().getComponent().getSelectedItem();
	}

	public String getExtColorCode() {
		return (String)getExtColorComboBox().getComponent().getSelectedItem();
	}

	public String getIntColorCode() {
		return (String)getIntColorComboBox().getComponent().getSelectedItem();
	}
	

}
