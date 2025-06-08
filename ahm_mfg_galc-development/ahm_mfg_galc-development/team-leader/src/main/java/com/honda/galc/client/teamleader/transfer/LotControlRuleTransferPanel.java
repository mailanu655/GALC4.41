package com.honda.galc.client.teamleader.transfer;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.PartByProductSpecCodeDao;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.property.PropertyService;
import net.miginfocom.swing.MigLayout;

public class LotControlRuleTransferPanel extends TransferPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	protected LotControlRuleDisplayPanel lcSrcPanel;
	protected LotControlRuleDisplayPanel lcDstPanel;
	
	public LotControlRuleTransferPanel() {
		super("Lot Control Rule Transfer", KeyEvent.VK_L);
		AnnotationProcessor.process(this);
	}

	public LotControlRuleTransferPanel(TabbedMainWindow mainWindow) {
		super("Lot Control Rule Transfer", KeyEvent.VK_L, mainWindow);
		AnnotationProcessor.process(this);
	}
	
	protected void initComponents() {
		this.setLayout(new MigLayout());
		this.add(this.getplDivProdPanel(), "span3, center, wrap");
		this.add(this.lcSrcPanel = new LotControlRuleDisplayPanel(this, true, "SrcPanel"),"span, wrap");
		this.add(this.getCopyButton(),"span, center, wrap");
		this.add(this.lcDstPanel = new LotControlRuleDisplayPanel(this, false, "DstPanel"),"span");
	}
		
	protected void addListeners() {
		this.getPlantComboBox().getComponent().addActionListener(this);
		this.getDivisionComboBox().getComponent().addActionListener(this);
		this.getProductComboBox().getComponent().addActionListener(this);
		this.lcSrcPanel.getModelComboBox().getComponent().addActionListener(this);
		this.copyButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {	
		this.getMainWindow().clearMessage();
		if (e.getSource().equals(this.getPlantComboBox().getComponent())) {
			this.plantChanged();
		} else if (e.getSource().equals(this.getDivisionComboBox().getComponent())) {
			this.divisionChanged();
		} else if (e.getSource().equals(this.getProductComboBox().getComponent())) {
			this.productTypeChanged();
		} else {
			try {
	        	setWaitCursor();
	            if (e.getSource().equals(copyButton)) transferRules();
	        } finally {
	        	setDefaultCursor();
	        }
		}
	}
	
	public void productTypeChanged() {
		Logger.getLogger().info(this.getSelectedProductType() + " is selected");
		if (this.getSelectedProductType() != null) {
			this.productSpecData = this.getProductSpecCache().get(this.getSelectedProductType());
			if (this.productSpecData == null) {
				this.productSpecData = new ProductSpecData(this.getSelectedProductType());
				this.getProductSpecCache().put(this.getSelectedProductType(), this.productSpecData);
			}
			this.loadProcPointTags(this.processPoints.get(this.getSelectedProductType()));
			
			this.lcSrcPanel.resetPanel(this.getProcPointTags());
			this.lcDstPanel.resetPanel(this.getProcPointTags());		
		}
		else{
			this.lcSrcPanel.resetPanel(null);
			this.lcDstPanel.resetPanel(null);
		}
	}

	private void transferRules() {
		List<LotControlRule> srcRules = this.lcSrcPanel.getLotControlRuleTableModel().getSelectedItems();
		if (srcRules == null || srcRules.isEmpty()) 
			return;
		else if (!MessageDialog.confirm(this, "Transfer " + srcRules.size() + " selected rule(s)?"))
			return;
		
		List<String> dstSpecCodes = ProductSpec.trimWildcard(lcDstPanel.buildSelectedProductSpecCodes());
		if (dstSpecCodes.size() == 0)
			return;

		this.productSpecData = new ProductSpecData(this.getSelectedProductType());

		Set<PartByProductSpecCode> parts = new HashSet<PartByProductSpecCode>();
		ArrayList<LotControlRule> dstRules = new ArrayList<LotControlRule>();

		LotControlRuleDao ruleDao = getDao(LotControlRuleDao.class);
		PartByProductSpecCodeDao pbpsDao = getDao(PartByProductSpecCodeDao.class);

		for (String dstSpecCode : dstSpecCodes) {
			dstSpecCode = ProductSpec.trimWildcard(dstSpecCode);

			String yearCode = ProductSpec.extractModelYearCode(dstSpecCode);
			String modelCode = ProductSpec.extractModelCode(dstSpecCode);
			String typeCode = ProductSpec.extractModelTypeCode(dstSpecCode);
			String optionCode = ProductSpec.extractModelOptionCode(dstSpecCode);
			String extColorCode = ProductSpec.extractExtColorCode(dstSpecCode);
			String intColorCode = ProductSpec.extractIntColorCode(dstSpecCode);

			List<String> modelCodes = productSpecData.getModelCodes(yearCode);
			Boolean displayWarn = true;
			Boolean skip = false;
			
			for (LotControlRule srcRule : srcRules) {
				HashMap<LotControlRule,Integer> result = 
					this.convertRule((LotControlRule)srcRule.deepCopy(),yearCode,modelCode,typeCode,optionCode,extColorCode,intColorCode,productSpecData,modelCodes,skip);	
				LotControlRule dstRule = new LotControlRule();
				int option = JOptionPane.DEFAULT_OPTION;
				for (Map.Entry<LotControlRule, Integer> entry : result.entrySet()) {
					dstRule = entry.getKey();
					option = entry.getValue();
				}
				if (option == JOptionPane.YES_OPTION) {
					skip = true;
					continue;
				} else if (option == JOptionPane.NO_OPTION) {
					continue;
				} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
					dstRules.clear();
					break;
				}
				
				if (this.ruleExists(dstRule)) {
					dstRules.clear();
					break;
				} 
				
				int response = this.relatedRulesCheck(dstRule, displayWarn);
				if (response == JOptionPane.CANCEL_OPTION) {
					dstRules.clear();
					break;
				} else if (response == JOptionPane.YES_OPTION){
					displayWarn = false;
				}
				
				List<PartByProductSpecCode> validParts = setPartData(dstRule);
				if (validParts != null && !validParts.isEmpty()) {
					parts.addAll(validParts);
				}
				dstRules.add(dstRule);
				dstRule.getPartByProductSpecs().clear();
			}
		}
		dstRules = this.mergeDuplicateRules(dstRules);
		
		if (this.duplicateIdsExist(dstRules)) dstRules.clear();
		
		int count = 0;
		CommonTlPropertyBean propertyBean = PropertyService.getPropertyBean(CommonTlPropertyBean.class, getApplicationId());
		int batchSize = propertyBean.getBatchSize();		
		count = batchSave(ruleDao, dstRules, batchSize);
		if (!parts.isEmpty()) {
			parts = this.mergeDuplicateParts(parts);
			batchSave(pbpsDao, new ArrayList<PartByProductSpecCode>(parts), batchSize);
		}
		
		String msg = count + " new rule(s) transfered to YMTOC: " + lcDstPanel.getSelectedYmtoc();	
		this.getMainWindow().setMessage(msg);
		Logger.getLogger().info(msg);
		this.lcDstPanel.showLotControlRuleResult();
	}
	
	protected HashMap<LotControlRule,Integer> convertRule(LotControlRule rule, String modelYearCode, String modelCode, String modelTypeCode, String modelOptionCode,
											String extColorCode, String intColorCode, ProductSpecData productSpecData, List<String> modelCodes, Boolean skip) {
		HashMap<LotControlRule,Integer> result = new  HashMap<LotControlRule,Integer>();
		int option = JOptionPane.NO_OPTION;
		String errMsg =	"Rule [" + rule.getId().toString().replaceAll("\\s+(,)", ",").trim() + "] can not be transfered.";
		
		String destProcPoint = this.lcDstPanel.getSelectedProcPoint().trim();
		if (!destProcPoint.equals(ProductSpec.WILDCARD)) {
			destProcPoint = destProcPoint.split(" - ")[0].trim();
			rule.getId().setProcessPointId(destProcPoint);
		}
		
		rule.getId().setModelYearCode(modelYearCode);
		
		if (StringUtils.isEmpty(modelCode)) {
			if (!modelCodes.contains(rule.getId().getModelCode())) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + rule.getId().getModelCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else rule.getId().setModelCode(modelCode);
		
		List<String> modelTypeCodes = null;
		if (StringUtils.isEmpty(modelTypeCode)) {
			modelTypeCodes = productSpecData.getModelTypeCodes(modelYearCode, rule.getId().getModelCode());
			if (!modelTypeCodes.contains(rule.getId().getModelTypeCode())) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + rule.getId().getModelTypeCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else rule.getId().setModelTypeCode(modelTypeCode);
		
		List<String> modelOptionCodes = null;
		if (StringUtils.isEmpty(modelOptionCode)) {
			modelOptionCodes = productSpecData.getModelOptionCodes(modelYearCode, rule.getId().getModelCode(), new Object[] {rule.getId().getModelTypeCode()});
			if (!modelOptionCodes.contains((rule.getId().getModelOptionCode()))) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + modelTypeCode + rule.getId().getModelOptionCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else rule.getId().setModelOptionCode(modelOptionCode);
		
		List<String> extColorCodes = null;
		if (StringUtils.isEmpty(extColorCode)) {
			extColorCodes = productSpecData.getModelExtColorCodes(modelYearCode, rule.getId().getModelCode(), new Object[] {rule.getId().getModelTypeCode()}, new Object[] {rule.getId().getModelOptionCode()});
			if (!extColorCodes.contains((rule.getId().getExtColorCode()))) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + modelTypeCode + modelOptionCode + rule.getId().getExtColorCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else rule.getId().setExtColorCode(extColorCode);
		
		List<String> intColorCodes = null;
		if (StringUtils.isEmpty(intColorCode)) {
			intColorCodes = productSpecData.getModelIntColorCodes(modelYearCode, rule.getId().getModelCode(), new Object[] {rule.getId().getModelTypeCode()}, new Object[] {rule.getId().getModelOptionCode()}, new Object[] {rule.getId().getExtColorCode()});
			if (!intColorCodes.contains((rule.getId().getIntColorCode()))) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + modelTypeCode + modelOptionCode + extColorCode + rule.getId().getIntColorCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else rule.getId().setIntColorCode(intColorCode);
		
		String newSpecCode= rule.getId().getModelYearCode()+ProductSpec.padModelCode((rule.getId().getModelCode()).equals(ProductSpec.WILDCARD)?"":rule.getId().getModelCode());
		newSpecCode += ProductSpec.padModelTypeCode((rule.getId().getModelTypeCode()).equals(ProductSpec.WILDCARD)?"":rule.getId().getModelTypeCode());
		newSpecCode += ProductSpec.padModelOptionCode((rule.getId().getModelOptionCode()).equals(ProductSpec.WILDCARD)?"":rule.getId().getModelOptionCode());
		newSpecCode += ProductSpec.padExtColorCode((rule.getId().getExtColorCode()).equals(ProductSpec.WILDCARD)?"":rule.getId().getExtColorCode());
		newSpecCode += ProductSpec.padIntColorCode((rule.getId().getIntColorCode()).equals(ProductSpec.WILDCARD)?"":rule.getId().getIntColorCode());
		rule.getId().setPartName(rule.getId().getPartName());
		rule.getPartName().setPartName(rule.getPartName().getPartName());
		rule.getId().setProductSpecCode(newSpecCode.trim());
		
		if (!StringUtils.isBlank(extColorCode)) {
			rule.getId().setExtColorCode(extColorCode);
		}
		if (!StringUtils.isBlank(intColorCode)) {
			rule.getId().setIntColorCode(intColorCode);
		}
		
		rule.setUpdateTimestamp(null);
		rule.setUpdateUser(getUserName());
		
		rule.getId().setPartName(rule.getId().getPartName().trim());
		result.put(rule,-999);
		return result;
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
	
	private boolean ruleExists(LotControlRule rule) {
		List<LotControlRule> rulesFound= getDao(LotControlRuleDao.class).findAllById(rule.getId());
		if (rulesFound == null || rulesFound.size() == 0) return false;
		String msg = "Rule ["+ rule.getId().toString().replaceAll("\\s+(,)", ",").trim() + "] already exists.";
		JOptionPane.showMessageDialog(this, msg, "TRANSFER FAILED", JOptionPane.ERROR_MESSAGE);
		Logger.getLogger().info(msg);
		return true;
	}
	
	private Integer relatedRulesCheck(LotControlRule rule, Boolean displayWarn) {
		List<LotControlRule> rulesFound= getDao(LotControlRuleDao.class).findAllByProcessPoint(rule.getId().getProcessPointId());
		for (LotControlRule ruleFound : rulesFound) {
			if (ruleFound.functionEquals(rule) && ruleFound.getSequenceNumber() == rule.getSequenceNumber()) {
				if ((rule.isParentMTOC(ruleFound) || ruleFound.isParentMTOC(rule))) {
					String msg = 	"Rule:\n["+ rule.getId().toString().replaceAll("\\s+(,)", ",").trim() + "]\n" +
									"has an existing related rule:\n[" + ruleFound.getId().toString().replaceAll("\\s+(,)", ",").trim() + "]." +
									"\n\nIgnore all similar warnings?";
					Logger.getLogger().info(msg);
					int response = this.relatedRuleWarning(rule, ruleFound, msg, "RELATED RULE EXISTS IN DESTINATION", displayWarn);
					if (response == JOptionPane.YES_OPTION) 
						return JOptionPane.YES_OPTION;
					else if (response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION) 
						return JOptionPane.CANCEL_OPTION;
				}
			}
		}
		return -999;
	}
	
	private boolean duplicateIdsExist(ArrayList<LotControlRule> rules) {
		ArrayList<LotControlRule> rulesTmp = new ArrayList<LotControlRule>();
		for (LotControlRule rule : rules) {
			for(LotControlRule ruleTmp : rulesTmp) {
				if (rule.getId().equals(ruleTmp.getId())) {
					String errMsg = "Can't create rules with duplicate IDs [" + rule.getId().toString().replaceAll("\\s+(,)", ",").trim() + "].";
					JOptionPane.showMessageDialog(this, errMsg, "TRANSFER FAILED", JOptionPane.ERROR_MESSAGE);
					Logger.getLogger().info(errMsg);
					return true;
				}
			}
			rulesTmp.add(rule);
		}
		return false;
	}
	
	private ArrayList<LotControlRule> mergeDuplicateRules(ArrayList<LotControlRule> rules) {
		ArrayList<LotControlRule> rulesTmp = new ArrayList<LotControlRule>();
		boolean displayWarn = true;
		for (LotControlRule rule : rules) {
			boolean duplicate = false;
			for(LotControlRule ruleTmp : rulesTmp) {
				if (ruleTmp.functionEquals(rule) &&	ruleTmp.getSequenceNumber() == rule.getSequenceNumber()) {
					String msg = 	"The following related rules were generated in the transfer:\n" + 
									rule.getId().toString().replaceAll("\\s+(,)", ",").trim() + "\n" + 
									ruleTmp.getId().toString().replaceAll("\\s+(,)", ",").trim() + 
									"\n\nIgnore all similar warnings?";
					int response = this.relatedRuleWarning(rule, ruleTmp, msg, "RELATED RULES GENERATED", displayWarn);
					if (response == JOptionPane.YES_OPTION) 
						displayWarn = false;
					if (response == JOptionPane.NO_OPTION) 
						continue;
					else if (response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION)
						return new ArrayList<LotControlRule>();
					else 
						duplicate = true;
				}
			}
			if (!duplicate) rulesTmp.add(rule);
		}
		if (rules.size() > rulesTmp.size())	rules = new ArrayList<LotControlRule>(rulesTmp);
		return rules;
	}
	
	private Integer relatedRuleWarning(LotControlRule rule1, LotControlRule rule2, String msg, String title, Boolean displayWarn) {
		if (	(rule1.functionEquals(rule2)) &&
				(rule1.getSequenceNumber() == rule2.getSequenceNumber()) &&
				(rule1.isParentMTOC(rule2) || rule2.isParentMTOC(rule1))) {
			Logger.getLogger().info(msg);
			if (displayWarn)
				return JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
		}
		return -999;
	}
	
	private HashSet<PartByProductSpecCode> mergeDuplicateParts(Set<PartByProductSpecCode> parts) {
		ArrayList<PartByProductSpecCode> partsTmp = new ArrayList<PartByProductSpecCode>();
		for (PartByProductSpecCode part : parts) {
			boolean duplicate = false;
			for(PartByProductSpecCode partTmp : partsTmp) {
				if (part.getId().equals(partTmp.getId())) duplicate = true;
			}
			if (!duplicate) partsTmp.add(part);
		}
		if (parts.size() > partsTmp.size()) parts = new HashSet<PartByProductSpecCode>(partsTmp);
		return (HashSet<PartByProductSpecCode>) parts;
	}

	private List<PartByProductSpecCode> setPartData(LotControlRule rule) {

		List<PartByProductSpecCode> parts = rule.getPartByProductSpecs();
		List<PartByProductSpecCode> items = new ArrayList<PartByProductSpecCode>();
		for (PartByProductSpecCode part : parts) {
			
			String partId = part.getId().getPartId();
			
			if ( part.getPartSpec() == null) {
				String msg = "PartByProductSpec " + part + " is invalid and will be skipped as PartSpec does not exist.";
				getLogger().error(msg);
				continue;
			}
			
			String partnumber = part.getPartSpec().getPartNumber();
			String modelYearCode = rule.getId().getModelYearCode();
			
			if(partId.substring(0, 1).equalsIgnoreCase(part.getId().getModelYearCode())	&& partId.substring(1, 2).equalsIgnoreCase("~")){
				List<PartSpec> partSpecs = rule.getPartName().getAllPartSpecs();
				PartSpec matchingPartSpec = null;
				for(PartSpec partSpec:partSpecs){
					String nPartId = partSpec.getId().getPartId();
					if(partSpec.getPartNumber() != null && partSpec.getPartNumber().substring(0, 11)
							.equalsIgnoreCase(partnumber.substring(0, 11))&& nPartId.substring(0, 1).equalsIgnoreCase(modelYearCode)
							&& nPartId.substring(1, 2).equalsIgnoreCase("~")){
						matchingPartSpec = partSpec;
						break;
					}
				}
				if(matchingPartSpec !=null){
					part.setPartSpec(matchingPartSpec);
					part.getId().setPartId(matchingPartSpec.getId().getPartId());
					part.getId().setYmtoc(rule.getId());
					part.getId().setProductSpecCode(rule.getId().getProductSpecCode());
					part.setUpdateTimestamp(null);
					part.setUpdateUser(getUserName());
					items.add(part);
				}
			
			}else{
				part.getId().setYmtoc(rule.getId());
				part.getId().setProductSpecCode(rule.getId().getProductSpecCode());
				part.setUpdateTimestamp(null);
				part.setUpdateUser(getUserName());
				items.add(part);
			}
		}
		return items;
	}
	
	protected Boolean isReadyToCopy() {
		return 	(this.lcSrcPanel.getLotControlRuleTableModel() != null) &&
				(this.lcSrcPanel.getLotControlRuleTableModel().getSelectedItems().size() > 0) &&
				(this.lcDstPanel.getSelectedProcPoint() != null) &&
				(this.lcDstPanel.getSelectedIntColorCode() != null);
	}
}