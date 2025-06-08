package com.honda.galc.dao.jpa.oif;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.entity.fif.SalesOrderFif;
import com.honda.galc.entity.fif.SalesOrderFifId;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>SalesOrderFifDaoImpl.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SalesOrderFifDaoImpl.java description </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>Feb 16, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */

public class SalesOrderFifDaoImpl extends
		BaseDaoImpl<SalesOrderFif, SalesOrderFifId> implements SalesOrderFifDao {
	
	private static final String FIF_CODE_BY_SPEC_CODE	=	new StringBuilder()
			.append (" select fif.fif_Codes,	"	)
			.append (" fif.order_Seq_No	")
			.append (" from SALES_ORDER_FIF_TBX fif	"	)
			.append (" where fif.frm_Model_Year_Cd = ?1 and "	)
			.append (" fif.frm_Model_Cd		=	?2 and	"	)
			.append (" fif.frm_Type_Cd		=	?3 and	")
			.append (" fif.frm_Ext_Clr_Cd	=	?4 and	")
			.append (" fif.frm_Int_Clr_Cd	=	?5 and	")
			.append (" fif.frm_Plant_Cd		=	?6	and	")
			.append (" fif.frm_Option_Cd	=	?7	and	")
			.append (" fif.sales_Int_Clr_Cd	=	?8	and	")
			.append (" fif.sales_Ext_Clr_Cd	=	?9	and	")
			.append (" fif.sales_Model_Cd	=	?10		")
			.append (" order by fif.order_Seq_No desc "	)
			.toString();
	
	/**
	 * Method to get the FIF Code for a specific Frame product spec code 
	 * select the correct FIF_CODES value based on the greatest ORDER_SEQ_NO value
	 * @param productSpecCode
	 * @return
	 */
	public String getFIFCodeByProductSpec ( final FrameSpec productSpecCode )
	{
		Parameters parameters	=	Parameters.with( "1", productSpecCode.getModelYearCode() );
		parameters.put( "2", productSpecCode.getModelCode() 		);
		parameters.put( "3", productSpecCode.getModelTypeCode()		);
		parameters.put( "4", productSpecCode.getExtColorCode() 		);
		parameters.put( "5", productSpecCode.getIntColorCode()		);
		parameters.put( "6", productSpecCode.getPlantCodeGpcs()		);
		parameters.put( "7", productSpecCode.getModelOptionCode()	);
		parameters.put( "8", productSpecCode.getSalesIntColorCode()	);
		parameters.put( "9", productSpecCode.getSalesExtColorCode()	);
		parameters.put( "10",productSpecCode.getSalesModelCode()	);
		
		Object[] fifCodes = findFirstByNativeQuery( FIF_CODE_BY_SPEC_CODE, parameters, Object[].class );
		
		String code	=	null;
		if ( fifCodes != null )
		{
			code	= fifCodes[0] != null ? fifCodes[0].toString() : null;	
		}
		return code;
	}
	/**
	 * Method to get the FIF Code for a specific Frame product spec code 
	 * select the correct FIF_CODES value based on the greatest ORDER_SEQ_NO value
	 * @param productSpecCode
	 * @return List<SalesOrderFif>
	 */
	public List<SalesOrderFif> getSalesOrderFifBySpecCode ( final SalesOrderFifId salesOrderId )
	{
		Parameters parameters	=	Parameters.with( "id.frmModelYearCd", salesOrderId.getFrmModelYearCd());
		parameters.put( "id.frmModelCd", salesOrderId.getFrmModelCd());
		parameters.put( "id.frmTypeCd", salesOrderId.getFrmTypeCd());
		parameters.put( "id.frmExtClrCd", salesOrderId.getFrmExtClrCd());
		parameters.put( "id.frmIntClrCd", salesOrderId.getFrmIntClrCd());
		parameters.put( "id.frmPlantCd", salesOrderId.getFrmPlantCd());
		parameters.put( "id.frmOptionCd", salesOrderId.getFrmOptionCd());
		parameters.put( "id.salesIntClrCd", salesOrderId.getSalesIntClrCd());
		parameters.put( "id.salesExtClrCd", salesOrderId.getSalesExtClrCd());
		parameters.put( "id.salesModelCd",salesOrderId.getSalesModelCd());
		
		String[] orderBy = {"id.orderSeqNo"};
		
		List<SalesOrderFif> all = findAll(parameters, orderBy, false);
		
		
		return all;
	}
}
