package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrDao</code> is ... .
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
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public interface QsrDao extends IDaoService<Qsr, Integer> {

	public Qsr insert(Qsr qsr, HoldParm holdParam);
	public List<Qsr> findAll(String processLocation, int qsrStatus);
	public List<Qsr> findAll(String processLocation, String productType, int qsrStatus);
	public Qsr holdProducts(ProductType productType, List<HoldResult> holdResults, Qsr qsr);
	public void updateHoldResults(ProductType productType, List<HoldResult> holdResults, Qsr qsr);
	public void scrapHoldProducts(ProductType productType, Qsr qsr, List<HoldResult> holdResults, HoldResult releaseInfo, DefectDescription defectDescription, ProcessPoint processPoint);
	public void massScrapProducts(ProductType productType, Qsr qsr,	List<HoldResult> assembleScrapResults,DefectDescription defectDescriptionInput, ProcessPoint processPoint);
	public List<Qsr> findAll(String processLocation, String productType, int qsrStatus, String holdAccessType);
}
