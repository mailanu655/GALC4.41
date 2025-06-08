package com.honda.galc.service.msip.handler.inbound;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.apache.commons.lang.ObjectUtils;

import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.fif.BomId;
import com.honda.galc.service.msip.dto.inbound.PmcbomDto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.util.StringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class PmcbomHandler extends BaseMsipInboundHandler<BaseMsipPropertyBean, PmcbomDto> {
	
	public PmcbomHandler() {}

	public boolean execute(List<PmcbomDto> dtoList) {
		int totalCount = 0;
		int insertedCount = 0;
		int updatedCount = 0;
		long startTime = System.currentTimeMillis();
		try {
			//update or insert data
			List<Method> bomColumnGetterMethods = getBomColumnGetterMethods();
			for(PmcbomDto bomDto : dtoList){
				if(bomDto.getIntColorCode() == null || bomDto.getMtcColor() == null || 
						bomDto.getMtcModel() == null || bomDto.getMtcOption() == null || 
						bomDto.getMtcType() == null || bomDto.getPartBlockCode() == null || 
						bomDto.getPartColorCode() == null || bomDto.getPartItemNo() == null || 
						bomDto.getPartNo() == null || bomDto.getPartSectionCode() == null || 
						bomDto.getPlantLocCode() == null || bomDto.getSupplierCatCode() == null || 
						bomDto.getTgtShipToCode() == null || bomDto.getEffBegDate() == null){
					getLogger().emergency("The primary key is missing for this record: " + bomDto);
				}else{				
					Bom bom = deriveBom(bomDto);
					//generate PDDA_FIF_TYPE for each BOM record.  PDDA_FIF_TYPE = SUBSTR(PART_ITEM_NO,6,2)||SUBSTR(MODEL_TYPE_CODE,1,1)
					String partBlock = bomDto.getPartBlockCode().trim();
					String partItemNo = bomDto.getPartItemNo().trim();
					String modelTypeCode = bomDto.getMtcType().trim();
					if(partBlock != null && partBlock.equals("F") && partItemNo.length() >= 7 && modelTypeCode.length() > 1){
						String pddaFifType = modelTypeCode.substring(0, 1) + partItemNo.substring(5,7);
						bom.setPddaFifType(pddaFifType);
					}
					Bom currentBom = getBomDao().findByKey(bom.getId());
					if (currentBom == null) {
						getBomDao().save(bom);
						insertedCount++;
						getLogger().debug("Bom record inserted " + bom);
					} else if (isBomChanged(currentBom, bom, bomColumnGetterMethods)) {
						getBomDao().save(bom);
						updatedCount++;
						getLogger().debug("Bom record updated " + bom);
					} else {
						getLogger().debug("Bom record unmodified " + bom);
					}
					totalCount++;
				}
			}
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			getLogger().info(String.format("BomTask complete.  Received %1$d records total; inserted %2$d records and updated %3$d records in %4$d milliseconds.", 
					totalCount, insertedCount, updatedCount, totalTime));
			
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private List<Method> getBomColumnGetterMethods() {
		Field[] bomFields = Bom.class.getDeclaredFields();
		List<Field> bomColumnFields = new ArrayList<Field>();
		for (Field bomField : bomFields) {
			if (bomField.isAnnotationPresent(Column.class)) {
				bomColumnFields.add(bomField);
			}
		}
		List<Method> bomColumnGetterMethods = new ArrayList<Method>();
		for (Field bomColumnField : bomColumnFields) {
			String bomColumnGetterMethodName = StringUtil.getterName(bomColumnField.getName());
			try {
				Method bomColumnGetterMethod = Bom.class.getMethod(bomColumnGetterMethodName);
				bomColumnGetterMethods.add(bomColumnGetterMethod);
			} catch (SecurityException se) {
				getLogger().error(se, "Unable to get Bom @Column method " + bomColumnGetterMethodName);
			} catch (NoSuchMethodException nsme) {
				getLogger().error(nsme, "Unable to get Bom @Column method " + bomColumnGetterMethodName);
			}
		}
		return bomColumnGetterMethods;
	}
	
	private boolean isBomChanged(Bom currentBom, Bom newBom, List<Method> bomColumnGetterMethods) {
		for (Method bomColumnGetterMethod : bomColumnGetterMethods) {
			Object currentColumn, newColumn;
			try {
				currentColumn = bomColumnGetterMethod.invoke(currentBom);
			} catch (IllegalArgumentException iae) {
				getLogger().error(iae, "Unable to get current Bom @Column value " + bomColumnGetterMethod.getName());
				continue;
			} catch (IllegalAccessException iae) {
				getLogger().error(iae, "Unable to get current Bom @Column value " + bomColumnGetterMethod.getName());
				continue;
			} catch (InvocationTargetException ite) {
				getLogger().error(ite, "Unable to get current Bom @Column value " + bomColumnGetterMethod.getName());
				continue;
			}
			try {
				newColumn = bomColumnGetterMethod.invoke(newBom);
			} catch (IllegalArgumentException iae) {
				getLogger().error(iae, "Unable to get new Bom @Column value " + bomColumnGetterMethod.getName());
				continue;
			} catch (IllegalAccessException iae) {
				getLogger().error(iae, "Unable to get new Bom @Column value " + bomColumnGetterMethod.getName());
				continue;
			} catch (InvocationTargetException ite) {
				getLogger().error(ite, "Unable to get new Bom @Column value " + bomColumnGetterMethod.getName());
				continue;
			}
			if (!ObjectUtils.equals(currentColumn, newColumn)) {
				return true;
			}
		}
		return false;
	}
	
	public Bom deriveBom(PmcbomDto bomDto) {
		Bom bom = new Bom();
		bom.setId(deriveID(bomDto));
		bom.setDataUpdDescText(bomDto.getDataUpdDescText());
		bom.setDcFamClassCode(bomDto.getDcFamClassCode());
		bom.setDcPartNo(bomDto.getDcPartNo());
		bom.setDcPartName(bomDto.getDcPartName());
		bom.setEffEndDate(bomDto.getEffEndDate());
		bom.setPartColorIdCode(bomDto.getPartColorIdCode());
		bom.setPartProdCode(bomDto.getPartProdCode());
		bom.setPartQty(bomDto.getPartQty());
		bom.setPddaFifType(bomDto.getPddaFifType());
		bom.setProclocGpNo(bomDto.getProclocGpNo());
		bom.setProclocGpSeqNo(bomDto.getProclocGpSeqNo());
		bom.setSupplierCatCode(bomDto.getSupplierCatCode());
		bom.setTargetMfgDestNo(bomDto.getTargetMfgDestNo());
		bom.setTgtModelDevCode(bomDto.getTgtModelDevCode());
		bom.setTgtPlantLocCode(bomDto.getTgtPlantLocCode());
		bom.setTimestampDate(bomDto.getTimestampDate());
		return bom;
	}

	private BomId deriveID(PmcbomDto bomDto) {
		BomId bomId = new BomId();
		bomId.setEffBegDate(bomDto.getEffBegDate());
		bomId.setIntColorCode(bomDto.getIntColorCode());
		bomId.setMtcColor(bomDto.getMtcColor());
		bomId.setMtcModel(bomDto.getMtcModel());
		bomId.setMtcOption(bomDto.getMtcOption());
		bomId.setMtcType(bomDto.getMtcType());
		bomId.setPartBlockCode(bomDto.getPartBlockCode());
		bomId.setPartColorCode(bomDto.getPartColorCode());
		bomId.setPartItemNo(bomDto.getPartItemNo());
		bomId.setPartNo(bomDto.getPartNo());
		bomId.setPartSectionCode(bomDto.getPartSectionCode());
		bomId.setPlantLocCode(bomDto.getPlantLocCode());
		bomId.setSupplierNo(bomDto.getSupplierNo());
		bomId.setTgtShipToCode(bomDto.getTgtShipToCode());
		return bomId;
	}
}
