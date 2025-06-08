package com.honda.galc.client.teamleader.transfer;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.property.PropertyService;
import net.miginfocom.swing.MigLayout;

public class RequiredPartTransferPanel extends TransferPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	protected RequiredPartDisplayPanel partSrcPanel;
	protected RequiredPartDisplayPanel partDstPanel;
	private static final String ASTERISK = "*";
	
	protected void initComponents() {
		this.setLayout(new MigLayout());
		this.add(this.getplDivProdPanel(), "span3, center, wrap");
		this.add(this.partSrcPanel = new RequiredPartDisplayPanel(this, true, "SrcPanel"),"span, wrap");
		this.add(this.getCopyButton(),"span, center, wrap");
		this.add(this.partDstPanel = new RequiredPartDisplayPanel(this, false, "DstPanel"),"span");
	}
	
	public RequiredPartTransferPanel() {
		super("Required Part Transfer", KeyEvent.VK_L);
		AnnotationProcessor.process(this);
	}

	public RequiredPartTransferPanel(TabbedMainWindow mainWindow) {
		super("Required Part Transfer", KeyEvent.VK_L, mainWindow);
		AnnotationProcessor.process(this);
	}
	
	protected void addListeners() {
		this.getPlantComboBox().getComponent().addActionListener(this);
		this.getDivisionComboBox().getComponent().addActionListener(this);
		this.getProductComboBox().getComponent().addActionListener(this);
		this.partSrcPanel.getModelComboBox().getComponent().addActionListener(this);
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
	            if (e.getSource().equals(copyButton)) this.transferRequiredParts();
			} catch (Exception ex) {
	        	System.out.println(ex.getStackTrace());
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
			
			this.partSrcPanel.resetPanel(this.getProcPointTags());
			this.partDstPanel.resetPanel(this.getProcPointTags());		
		}
		else{
			this.partSrcPanel.resetPanel(null);
			this.partDstPanel.resetPanel(null);
		}
	}

	private void transferRequiredParts() {
		List<RequiredPart> srcParts = this.partSrcPanel.getRequiredPartTableModel().getSelectedItems();
		if (srcParts == null || srcParts.isEmpty()) 
			return;
		else if (!MessageDialog.confirm(this, "Transfer " + srcParts.size() + " selected part(s)?"))
			return;
		
		List<String> dstSpecCodes = ProductSpec.trimWildcard(partDstPanel.buildSelectedProductSpecCodes());
		if (dstSpecCodes.size() == 0)
			return;

		this.productSpecData = new ProductSpecData(this.getSelectedProductType());

		ArrayList<RequiredPart> dstParts = new ArrayList<RequiredPart>();

		RequiredPartDao partDao = getDao(RequiredPartDao.class);

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
			
			for (RequiredPart srcPart : srcParts) {
				HashMap<RequiredPart,Integer> result = 
					this.convertRequiredPart((RequiredPart)srcPart.deepCopy(),yearCode,modelCode,typeCode,optionCode,extColorCode,intColorCode,productSpecData,modelCodes,skip);	
				RequiredPart dstPart = new RequiredPart();
				int option = JOptionPane.DEFAULT_OPTION;
				for (Map.Entry<RequiredPart, Integer> entry : result.entrySet()) {
					dstPart = entry.getKey();
					option = entry.getValue();
				}
				if (option == JOptionPane.YES_OPTION) {
					skip = true;
					continue;
				} else if (option == JOptionPane.NO_OPTION) {
					continue;
				} else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
					dstParts.clear();
					break;
				}
				
				if (this.requiredPartExists(dstPart)) {
					dstParts.clear();
					break;
				} 
				
				int response = this.relatedRequiredPartCheck(dstPart, displayWarn);
				if (response == JOptionPane.CANCEL_OPTION) {
					dstParts.clear();
					break;
				} else if (response == JOptionPane.YES_OPTION){
					displayWarn = false;
				}
				dstParts.add(dstPart);
			}
		}
		dstParts = this.mergeDuplicateRequiredParts(dstParts);
		
		if (this.duplicateIdsExist(dstParts)) dstParts.clear();
		
		int count = 0;
		CommonTlPropertyBean propertyBean = PropertyService.getPropertyBean(CommonTlPropertyBean.class, getApplicationId());
		int batchSize = propertyBean.getBatchSize();		
		count = batchSave(partDao, dstParts, batchSize);
		
		String msg = count + " new part(s) transfered to YMTOC: " + partDstPanel.getSelectedYmtoc();	
		this.getMainWindow().setMessage(msg);
		Logger.getLogger().info(msg);
		this.partDstPanel.showRequiedPartResult();
	}
	
	protected HashMap<RequiredPart,Integer> convertRequiredPart(RequiredPart part, String modelYearCode, String modelCode, String modelTypeCode, String modelOptionCode,
											String extColorCode, String intColorCode, ProductSpecData productSpecData, List<String> modelCodes, Boolean skip) {
		HashMap<RequiredPart,Integer> result = new  HashMap<RequiredPart,Integer>();
		int option = JOptionPane.NO_OPTION;
		String errMsg =	"Required part [" + part.getId().toString().replaceAll("\\s+(,)", ",").trim() + "] can not be transfered.";
		
		String destProcPoint = this.partDstPanel.getSelectedProcPoint().trim();
		if (!destProcPoint.equals(ProductSpec.WILDCARD)) {
			destProcPoint = destProcPoint.split(" - ")[0].trim();
			part.getId().setProcessPointId(destProcPoint);
		}
		
		part.getId().setModelYearCode(modelYearCode);
		
		if (StringUtils.isEmpty(modelCode)) {
			if (!modelCodes.contains(part.getId().getModelCode())) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + part.getId().getModelCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else part.getId().setModelCode(modelCode);
		
		List<String> modelTypeCodes = null;
		if (StringUtils.isEmpty(modelTypeCode)) {
			modelTypeCodes = productSpecData.getModelTypeCodes(modelYearCode, part.getId().getModelCode());
			if (!modelTypeCodes.contains(part.getId().getModelTypeCode())) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + part.getId().getModelTypeCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else part.getId().setModelTypeCode(modelTypeCode);
		
		List<String> modelOptionCodes = null;
		if (StringUtils.isEmpty(modelOptionCode)) {
			modelOptionCodes = productSpecData.getModelOptionCodes(modelYearCode, part.getId().getModelCode(), new Object[] {part.getId().getModelTypeCode()});
			if (!modelOptionCodes.contains((part.getId().getModelOptionCode()))) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + modelTypeCode + part.getId().getModelOptionCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else part.getId().setModelOptionCode(modelOptionCode);
		
		List<String> extColorCodes = null;
		if (StringUtils.isEmpty(extColorCode)) {
			extColorCodes = productSpecData.getModelExtColorCodes(modelYearCode, part.getId().getModelCode(), new Object[] {part.getId().getModelTypeCode()}, new Object[] {part.getId().getModelOptionCode()});
			if (!extColorCodes.contains((part.getId().getExtColorCode()))) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + modelTypeCode + modelOptionCode + part.getId().getExtColorCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else part.getId().setExtColorCode(extColorCode);
		
		List<String> intColorCodes = null;
		if (StringUtils.isEmpty(intColorCode)) {
			intColorCodes = productSpecData.getModelIntColorCodes(modelYearCode, part.getId().getModelCode(), new Object[] {part.getId().getModelTypeCode()}, new Object[] {part.getId().getModelOptionCode()}, new Object[] {part.getId().getExtColorCode()});
			if (!intColorCodes.contains((part.getId().getIntColorCode()))) {
				errMsg = errMsg + "\nDestination SpecCode [" + modelYearCode + modelCode + modelTypeCode + modelOptionCode + extColorCode + part.getId().getIntColorCode() + "] does not exist.";
				getLogger().info(errMsg);
				if (!skip)
					option = JOptionPane.showConfirmDialog(this, errMsg + "\n\nSkip similar notifications?\n", "Invalid destination spec code", JOptionPane.YES_NO_CANCEL_OPTION);
				result.put(null,option);
				return result;
			}
		} else part.getId().setIntColorCode(intColorCode);
		
		String newSpecCode= part.getId().getModelYearCode()+ProductSpec.padModelCode((part.getId().getModelCode()).equals(ProductSpec.WILDCARD)?"":part.getId().getModelCode());
		newSpecCode += ProductSpec.padModelTypeCode((part.getId().getModelTypeCode()).equals(ProductSpec.WILDCARD)?"":part.getId().getModelTypeCode());
		newSpecCode += ProductSpec.padModelOptionCode((part.getId().getModelOptionCode()).equals(ProductSpec.WILDCARD)?"":part.getId().getModelOptionCode());
		newSpecCode += ProductSpec.padExtColorCode((part.getId().getExtColorCode()).equals(ProductSpec.WILDCARD)?"":part.getId().getExtColorCode());
		newSpecCode += ProductSpec.padIntColorCode((part.getId().getIntColorCode()).equals(ProductSpec.WILDCARD)?"":part.getId().getIntColorCode());
		part.getId().setProductSpecCode(newSpecCode.trim());
		
		if (!StringUtils.isBlank(extColorCode)) {
			part.getId().setExtColorCode(extColorCode);
		}
		if (!StringUtils.isBlank(intColorCode)) {
			part.getId().setIntColorCode(intColorCode);
		}
		
		part.setUpdateTimestamp(null);
		
		result.put(part,-999);
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
	
	private boolean requiredPartExists(RequiredPart part) {
		RequiredPart partFound= getDao(RequiredPartDao.class).findByKey(part.getId());
		if (partFound == null) return false;
		String msg = "Required part ["+ part.getId().toString().replaceAll("\\s+(,)", ",").trim() + "] already exists.";
		JOptionPane.showMessageDialog(this, msg, "TRANSFER FAILED", JOptionPane.ERROR_MESSAGE);
		Logger.getLogger().info(msg);
		return true;
	}
	
	private Integer relatedRequiredPartCheck(RequiredPart part, Boolean displayWarn) {
		List<RequiredPart> partsFound= getDao(RequiredPartDao.class).findAllByProcessPoint(part.getId().getProcessPointId());
		for (RequiredPart partFound : partsFound) {
			if (this.functionEquals(partFound, part)) {
				if (this.isParentMTOC(part, partFound) || this.isParentMTOC(partFound, part)) {
					String msg = 	"Required part:\n["+ part.getId().toString().replaceAll("\\s+(,)", ",").trim() + "]\n" +
									"has an existing related record:\n[" + partFound.getId().toString().replaceAll("\\s+(,)", ",").trim() + "]." +
									"\n\nIgnore all similar warnings?";
					Logger.getLogger().info(msg);
					int response = this.relatedRequiredPartWarning(part, partFound, msg, "RELATED REQUIRED PART EXISTS IN DESTINATION", displayWarn);
					if (response == JOptionPane.YES_OPTION) 
						return JOptionPane.YES_OPTION;
					else if (response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION) 
						return JOptionPane.CANCEL_OPTION;
				}
			}
		}
		return -999;
	}
	
	private boolean functionEquals(RequiredPart part1, RequiredPart part2) {
		return 	part1.getId().getProcessPointId().equals(part2.getId().getProcessPointId())
				&& part1.getId().getPartName().equals(part2.getId().getPartName())
				&& StringUtils.equals(part1.getProductType(), part2.getProductType())
				&& StringUtils.equals(part1.getSubId(), part2.getSubId())
				&& (this.isParentMTOC(part1, part2) || this.isParentMTOC(part2, part1));
	}
	
	public boolean isParentMTOC(RequiredPart partOne, RequiredPart partTwo) {
		return (partOne.getId().getModelYearCode().equals(partTwo.getId().getModelYearCode()) || partTwo.getId().getModelYearCode().equals(ASTERISK))
			&& (partOne.getId().getModelCode().equals(partTwo.getId().getModelCode()) || partTwo.getId().getModelCode().equals(ASTERISK))
			&& (partOne.getId().getModelTypeCode().equals(partTwo.getId().getModelTypeCode()) || partTwo.getId().getModelTypeCode().equals(ASTERISK))
			&& (partOne.getId().getModelOptionCode().equals(partTwo.getId().getModelOptionCode()) || partTwo.getId().getModelOptionCode().equals(ASTERISK))
			&& (partOne.getId().getExtColorCode().equals(partTwo.getId().getExtColorCode()) || partTwo.getId().getExtColorCode().equals(ASTERISK))
			&& (partOne.getId().getIntColorCode().equals(partTwo.getId().getIntColorCode()) || partTwo.getId().getIntColorCode().equals(ASTERISK));
	}
	
	private boolean duplicateIdsExist(ArrayList<RequiredPart> parts) {
		ArrayList<RequiredPart> partsTmp = new ArrayList<RequiredPart>();
		for (RequiredPart part : parts) {
			for(RequiredPart partTmp : partsTmp) {
				if (part.getId().equals(partTmp.getId())) {
					String errMsg = "Can't create required parts with duplicate IDs [" + part.getId().toString().replaceAll("\\s+(,)", ",").trim() + "].";
					JOptionPane.showMessageDialog(this, errMsg, "TRANSFER FAILED", JOptionPane.ERROR_MESSAGE);
					Logger.getLogger().info(errMsg);
					return true;
				}
			}
			partsTmp.add(part);
		}
		return false;
	}
	
	private ArrayList<RequiredPart> mergeDuplicateRequiredParts(ArrayList<RequiredPart> parts) {
		ArrayList<RequiredPart> partsTmp = new ArrayList<RequiredPart>();
		boolean displayWarn = true;
		for (RequiredPart part : parts) {
			boolean duplicate = false;
			for(RequiredPart partTmp : partsTmp) {
				if (this.functionEquals(partTmp, part)) {
					String msg = 	"The following related required parts were generated in the transfer:\n" + 
									part.getId().toString().replaceAll("\\s+(,)", ",").trim() + "\n" + 
									partTmp.getId().toString().replaceAll("\\s+(,)", ",").trim() + 
									"\n\nIgnore all similar warnings?";
					int response = this.relatedRequiredPartWarning(part, partTmp, msg, "RELATED REQUIRED PARTS GENERATED", displayWarn);
					if (response == JOptionPane.YES_OPTION) 
						displayWarn = false;
					if (response == JOptionPane.NO_OPTION) 
						continue;
					else if (response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION)
						return new ArrayList<RequiredPart>();
					else 
						duplicate = true;
				}
			}
			if (!duplicate) partsTmp.add(part);
		}
		if (parts.size() > partsTmp.size())	parts = new ArrayList<RequiredPart>(partsTmp);
		return parts;
	}
	
	private Integer relatedRequiredPartWarning(RequiredPart part1, RequiredPart part2, String msg, String title, Boolean displayWarn) {
		if (	(this.functionEquals(part1, part2)) &&
				(this.isParentMTOC(part1, part2) || this.isParentMTOC(part2, part1))) {
			Logger.getLogger().info(msg);
			if (displayWarn)
				return JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_CANCEL_OPTION);
		}
		return -999;
	}
	
	protected Boolean isReadyToCopy() {
		return 	(this.partSrcPanel.getRequiredPartTableModel() != null) &&
				(this.partSrcPanel.getRequiredPartTableModel().getSelectedItems().size() > 0) &&
				(this.partDstPanel.getSelectedProcPoint() != null) &&
				(this.partDstPanel.getSelectedIntColorCode() != null);
	}
}