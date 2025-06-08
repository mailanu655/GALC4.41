package com.honda.galc.client.teamleader.qi.productRecovery;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.dao.product.BlockBuildResultHistoryDao;
import com.honda.galc.dao.product.ConrodBuildResultDao;
import com.honda.galc.dao.product.ConrodBuildResultHistoryDao;
import com.honda.galc.dao.product.CrankshaftBuildResultDao;
import com.honda.galc.dao.product.CrankshaftBuildResultHistoryDao;
import com.honda.galc.dao.product.HeadBuildResultDao;
import com.honda.galc.dao.product.HeadBuildResultHistoryDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * <h3>DiecastManualLtCtrResultEnterViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DiecastManualLtCtrResultEnterViewManager description </p>
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
 * <TD>Mar 29, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author L&T infotech
 * Aug 18, 2017
 */
public class DiecastManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase {

	public DiecastManualLtCtrResultEnterViewManager(ApplicationContext appContext,Application application, ProductType currentProductType) {
		super(appContext, application, currentProductType);
	}
	
	protected List<? extends ProductBuildResult> getCollectedBuildResult() {
		List<? extends ProductBuildResult> buildResults = null;
		if(getProductType().name().equals(ProductType.HEAD.toString()))  {
			buildResults = getHeadBuildResultData();
		}
		else if (getProductType().name().equals(ProductType.BLOCK.toString()))  {
			buildResults = getBlockBuildResultData();
		}
		else if (getProductType().name().equals(ProductType.CONROD.toString()))  {
			buildResults = getConrodBuildResultData();
		}
		else if (getProductType().name().equals(ProductType.CRANKSHAFT.toString()))  {
			buildResults = getCrankshaftBuildResultData();
		}
		return buildResults;
	}


	private List<BlockBuildResult> getBlockBuildResultData() {
		BlockBuildResult result = new BlockBuildResult(parent.getProductId(), getCurrentLotControlRule().getPartNameString());
		result.setInstalledPartStatus(InstalledPartStatus.OK);
		
		result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		ProductBuildResult pbr = partResult.getBuildResult();
		if (pbr != null) {
			result.setDefectRefId(((BlockBuildResult)pbr).getDefectRefId()); 
		}
		if(!partResult.isHeadLess() || !partResult.isQuickFix())  {
			getInputResultValue(result);
		}
		
		if(StringUtils.isEmpty(result.getResultValue()))  {
			result.setResultValue("0.0");
		}
		result.setAssociateNo(getUser());
		result.setProcessPointId(application.getApplicationId());
		partResult.setBuildResult(result);
		List<BlockBuildResult> list = new ArrayList<BlockBuildResult>();
		list.add(result);
		
		return list;
	}

	private List<HeadBuildResult> getHeadBuildResultData() {
		HeadBuildResult result = new HeadBuildResult(parent.getProductId(), getCurrentLotControlRule().getPartNameString());
		result.setInstalledPartStatus(InstalledPartStatus.OK);
		
		result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		ProductBuildResult pbr = partResult.getBuildResult();
		if (pbr != null) {
			result.setDefectRefId(((HeadBuildResult)pbr).getDefectRefId()); 
		}
		
		if(!partResult.isHeadLess() || !partResult.isQuickFix())  {
			getInputResultValue(result);
		}
		
		if(StringUtils.isEmpty(result.getResultValue()))  {
			result.setResultValue("0.0");
		}
		result.setAssociateNo(getUser());
		result.setProcessPointId(application.getApplicationId());
		partResult.setBuildResult(result);
		List<HeadBuildResult> list = new ArrayList<HeadBuildResult>();
		list.add(result);
		return list;
	}
	
	private List<ConrodBuildResult> getConrodBuildResultData() {
		ConrodBuildResult result = new ConrodBuildResult(parent.getProductId(), getCurrentLotControlRule().getPartNameString());
		result.setInstalledPartStatus(InstalledPartStatus.OK);
		
		result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		ProductBuildResult pbr = partResult.getBuildResult();
		if (pbr != null) {
			result.setDefectRefId(((HeadBuildResult)pbr).getDefectRefId()); 
		}
		
		if(!partResult.isHeadLess() || !partResult.isQuickFix())  {
			getInputResultValue(result);
		}
		
		if(StringUtils.isEmpty(result.getResultValue()))  {
			result.setResultValue("0.0");
		}
		result.setAssociateNo(getUser());
		result.setProcessPointId(application.getApplicationId());
		partResult.setBuildResult(result);
		List<ConrodBuildResult> list = new ArrayList<ConrodBuildResult>();
		list.add(result);
		return list;
	}
	
	private List<CrankshaftBuildResult> getCrankshaftBuildResultData() {
		CrankshaftBuildResult result = new CrankshaftBuildResult(parent.getProductId(), getCurrentLotControlRule().getPartNameString());
		result.setInstalledPartStatus(InstalledPartStatus.OK);
		
		result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		
		ProductBuildResult pbr = partResult.getBuildResult();
		if (pbr != null) {
			result.setDefectRefId(((HeadBuildResult)pbr).getDefectRefId()); 
		}
		
		if(!partResult.isHeadLess() || !partResult.isQuickFix())  {
			getInputResultValue(result);
		}
		
		if(StringUtils.isEmpty(result.getResultValue()))  {
			result.setResultValue("0.0");
		}
		result.setAssociateNo(getUser());
		result.setProcessPointId(application.getApplicationId());
		partResult.setBuildResult(result);
		List<CrankshaftBuildResult> list = new ArrayList<CrankshaftBuildResult>();
		list.add(result);
		return list;
	}
	
	private void getInputResultValue(ProductBuildResult result) {
		if (dialog.getPartSnField().isVisible())
			result.setResultValue(dialog.getPartSnField().getText());
	}

	
	@SuppressWarnings("unchecked")
	@Override
	protected void checkDuplicatePart(String partSn) {
		List<String> partNames = new ArrayList<String>();
		if(partSpec != null)
			partNames = LotControlPartUtil.getPartNamesOfSamePartNumber(partSpec);
		else
			partNames.add(getCurrentLotControlRule().getPartName().getPartName());
		
		List<? extends ProductBuildResult> duplicateParts = getProductTypeUtil().getProductBuildResultDao().findAllByPartNameAndSerialNumber(partNames, partSn);
		
		if (duplicateParts != null && !duplicateParts.isEmpty()) {
			final String errorMessage;
			{
				java.util.Iterator<? extends ProductBuildResult> duplicatePartIterator = duplicateParts.iterator();
				StringBuilder errorMessageBuilder = new StringBuilder("Duplicate part # with product");
				errorMessageBuilder.append(duplicateParts.size() > 1 ? "s: " : ": ");
				while (duplicatePartIterator.hasNext()) {
					ProductBuildResult duplicatePart = duplicatePartIterator.next();
					errorMessageBuilder.append(duplicatePart.getProductId());
					if (duplicatePartIterator.hasNext()) {
						errorMessageBuilder.append(", ");
					}
				}
				errorMessage = errorMessageBuilder.toString();
			}
				dialog.displayErrorMessage(errorMessage,errorMessage);
		}
		
	}
	
	public ProductTypeUtil getProductTypeUtil() {
		return ProductTypeUtil.getTypeUtil(getProductType());
	}

	protected void sendToQics(List<? extends ProductBuildResult> installedPartList)  {
		//check QI_DEFECT_FLAG from lot control (GAL246TBX)
		//if it is 1, repair NAQ defect
		ProductBuildResultDao<? extends ProductBuildResult, ?> myDao = ProductTypeUtil.getProductBuildResultDao(getProductType());
		if (partResult.getLotControlRule().isQicsDefect() && installedPartList.size() > 0) {
			List<String> productIdList = new ArrayList<String>();
			List<String> partNameList = new ArrayList<String>();
			for (ProductBuildResult installedPart : (List<? extends ProductBuildResult>)installedPartList) {
				productIdList.add(installedPart.getProductId());
				partNameList.add(installedPart.getPartName());
			}
			
			List<Long> defectRefIds= myDao.findDefectRefIds(productIdList, partNameList);
			
			List<QiRepairDefectDto> existingRepaired = new ArrayList<QiRepairDefectDto>();
			for (int i = 0; i < defectRefIds.size(); i++) {
				QiRepairDefectDto qiRepairDefectDto = new QiRepairDefectDto();
				qiRepairDefectDto.setExternalSystemKey(defectRefIds.get(i));
				qiRepairDefectDto.setAssociateId(getUser());
				qiRepairDefectDto.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
				qiRepairDefectDto.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
				qiRepairDefectDto.setProcessPointId(this.application.getApplicationId());
				existingRepaired.add(qiRepairDefectDto);
			}
			
			if(!existingRepaired.isEmpty())  {
				try {
					ServiceFactory.getService(QiHeadlessDefectService.class).repairDefects(existingRepaired, true);
				} catch (Exception ex) {
					Logger.getLogger().error(ex, "Exception invoking QiHeadlessDefectServiceImpl");
				}		
			}
		}	
	}
		
	protected boolean isHead() {
		return getProductType().name().equals(ProductType.HEAD.toString());
	}
	
	protected boolean isBlock() {
		return getProductType().name().equals(ProductType.BLOCK.toString());
	}
	
	protected boolean isConrod() {
		return getProductType().name().equals(ProductType.CONROD.toString());
	}
	
	protected boolean isCrankshaft() {
		return getProductType().name().equals(ProductType.CRANKSHAFT.toString());
	}
	
	@SuppressWarnings("unchecked")
	protected void doSaveUpdate() {
		try {
			if (isHead())  {
				saveHeadResults((List<HeadBuildResult>) installedPartList);
			}
			else if (isBlock())  {
				saveBlockResults((List<BlockBuildResult>) installedPartList);
			}
			else if (isConrod())  {
				saveConrodResults((List<ConrodBuildResult>) installedPartList);
			}
			else if (isCrankshaft())  {
				saveCrankshaftResults((List<CrankshaftBuildResult>) installedPartList);
			}
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to save ", getProductType().name(), 
					" build result:", installedPartList.toString());
		}
		
		Logger.getLogger().info("Saved data into database by user:" + getUser() + 
				System.getProperty("line.separator") + installedPartList.get(0));
	}


	private void saveBlockResults(List<BlockBuildResult> installedPartList) {
		ServiceFactory.getDao(BlockBuildResultDao.class).saveAll(installedPartList);
		for(BlockBuildResult result : installedPartList) {
			ServiceFactory.getDao(BlockBuildResultHistoryDao.class).saveHistory(result);
		}
		if (partResult.getLotControlRule().isQicsDefect() && installedPartList.size() > 0)  {
			sendToQics(installedPartList);
		}
	}


	private void saveHeadResults(List<HeadBuildResult> installedPartList) {
		ServiceFactory.getDao(HeadBuildResultDao.class).saveAll(installedPartList);
		for(HeadBuildResult result : installedPartList) {
			ServiceFactory.getDao(HeadBuildResultHistoryDao.class).saveHistory(result);
		}
		if (partResult.getLotControlRule().isQicsDefect() && installedPartList.size() > 0)  {
			sendToQics(installedPartList);
		}
	}

	private void saveConrodResults(List<ConrodBuildResult> installedPartList) {
		ServiceFactory.getDao(ConrodBuildResultDao.class).saveAll(installedPartList);
		for(ConrodBuildResult result : installedPartList) {
			ServiceFactory.getDao(ConrodBuildResultHistoryDao.class).saveHistory(result);
		}
		if (partResult.getLotControlRule().isQicsDefect() && installedPartList.size() > 0)  {
			sendToQics(installedPartList);
		}
	}

	private void saveCrankshaftResults(List<CrankshaftBuildResult> installedPartList) {
		ServiceFactory.getDao(CrankshaftBuildResultDao.class).saveAll(installedPartList);
		for(CrankshaftBuildResult result : installedPartList) {
			ServiceFactory.getDao(CrankshaftBuildResultHistoryDao.class).saveHistory(result);
		}
		if (partResult.getLotControlRule().isQicsDefect() && installedPartList.size() > 0)  {
			sendToQics(installedPartList);
		}
	}
	
	protected void loadHeadedResult() {

		dialog.getPartNameTextArea().setText(getPartNameLabel());// +"   "+partmask);

		if(getCurrentLotControlRule().getSerialNumberScanType() != PartSerialNumberScanType.NONE){
			dialog.setVisibleInstalledPart(true);
			if(partResult.getBuildResult() != null && !partResult.getBuildResult().getInstalledPartStatus().equals(InstalledPartStatus.REMOVED)){
				dialog.getPartNameTextArea().setText(getPartNameLabel());
				text = new Text(partResult.getBuildResult().getPartSerialNumber());
				dialog.getPartSnField().setText(text);

				if(getCurrentLotControlRule().isVerify()){
					if(!checkInstalledPartSerialNumber()){
						text = new Text(false);
						partSnNg = true;

						if(resetScreen) dialog.getPartSnField().setStatus(false);
						return;
					} 
				} 

				dialog.enableInstalledPart();
				if(!StringUtils.isBlank(partResult.getBuildResult().getPartSerialNumber()))  {
					dialog.getPartSnField().setStatus(true);
					dialog.getPartSnField().setDisable(true);
				}


			} else {
				LotControlRule lotControlRule =getCurrentLotControlRule();
				String partmask = CommonPartUtility.parsePartMask(lotControlRule.getPartMasks());
				dialog.getPartNameTextArea().setText(getPartNameLabel() +" "+partmask);
				dialog.enableInstalledPart();
				partSnNg = true;
				return;
			}
		} 
	}
	
	private PartSpec getInstaledPartSpec() {
		ProductBuildResult installedPart = partResult.getBuildResult();
		for(PartSpec spec : getPartSepcs()){
			if(isHead())  {
				if(spec.getId().getPartName().equals(((HeadBuildResult)installedPart).getId().getPartName()))  {
					return spec;
				}
			}
			else if(isBlock())  {
				if(spec.getId().getPartName().equals(((BlockBuildResult)installedPart).getId().getPartName()))  {
					return spec;
				}
			}
			else if(isConrod())  {
				if(spec.getId().getPartName().equals(((ConrodBuildResult)installedPart).getId().getPartName()))  {
					return spec;
				}
			}
			else if(isCrankshaft())  {
				if(spec.getId().getPartName().equals(((CrankshaftBuildResult)installedPart).getId().getPartName()))  {
					return spec;
				}
			}
		}
		Logger.getLogger().warn("Failed to find PartSpec for installed part part:" + 
				installedPart.toString());

		return null;
	}

	protected boolean checkInstalledPartSerialNumber() {
		PartSpec partSpec = getInstaledPartSpec();

		if(partSpec == null) return false;			

		if(CommonPartUtility.verification(partResult.getBuildResult().getPartSerialNumber(), 
				partSpec.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat(), product)){
			dialog.getPartSnField().setStatus(true);
			partSnNg = false;
			return true;
		}else {
			dialog.getPartSnField().setStatus(false);
			text = new Text(false);
			partSnNg = true;
			return false;
		}
	}

}
