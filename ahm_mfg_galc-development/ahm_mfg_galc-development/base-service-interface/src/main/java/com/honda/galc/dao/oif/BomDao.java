package com.honda.galc.dao.oif;

import java.util.List;

import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.fif.BomId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * <h3>BomDao.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BomDao.java description </p>
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
 * <TD>Jiamei Li</TD>
 * <TD>Feb 26, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 26, 2015 
 * pdda partmark task changes
 */
public interface BomDao extends IDaoService<Bom, BomId> {

	public List<Object[]> getAllBomtbxPapidData();
	public List<Bom> findAllBy(String partNo, String mtcModel, String mtcType);
	public List<Object[]> findEffectiveDate(String specCodeMask , String operationName , String partNo);
	public List<Bom> getPartList(String partNo, String partSectionCode, String partItemNo, String mtcModel, 
			String mtcType, String mtcOption, String mtcColor, String intColorCode);
	public List<Object[]> getUpdatedPapidData(String plantCode, String  lastRunTimestamp);
	public List<Object[]> findImageByPartId(Integer partIdentificationId);
	public List<Bom> findAllValidModels(String plantCode, String partNumber);
	public List<Bom> findAllByPartNo(String partNo, String plantCode);
	public List<Bom> findAllByPartNoAndMtc(String partNo, String plantCode, String mtcModel, String mtcType);
	public List<Object[]> findModelAndTypeByModelYearCodeAndPartNoPrefix(String modelYearCode, String partNoPrefix);
	public List<Object[]> findModelAndTypeBySystemModelGroupAndPartNoPrefix(String system, String modelGroup, String partNoPrefix);
	public List<Bom> findAllByModelYearCodePartNoPrefixAndPartColorCode(String modelYearCode, String partNoPrefix, String partColorCode);
	public List<Bom> findAllBySystmMdlGrpPrtPrfxNPrtClrCd(String system, String modelGroup, String partNoPrefix, String partColorCode);
	public Bom findProductSpecToBeReplicate(String productionLot, List<String> notLikeParts, List<String> likeParts);
	public String getBomPartNo(String parentProductSpec, String mbpnProductSpec);
	public boolean isBomPartNoValid(String partNo);                      

}
