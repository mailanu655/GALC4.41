package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.IPPTagDao;
import com.honda.galc.dto.oif.IPPTagDTO;
import com.honda.galc.entity.product.IPPTag;
import com.honda.galc.entity.product.IPPTagId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;
/**
 * 
 * <h3>IPPTagDaoImpl Class description</h3>
 * <p> IPPTagDaoImpl description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Aug 22, 2011
 *
 *
 */
public class IPPTagDaoImpl extends BaseDaoImpl<IPPTag, IPPTagId> implements IPPTagDao{
	
	private static final String UPDATE_TAG_NUMBER = "update IPPTag e set e.id.ippTagNo = :newIppTagNo " +
			"where e.id.productId = :productId AND e.id.ippTagNo = :oldIppTagNo AND e.id.divisionId = :divisionId";
	
	private static final String SELECT_IPP_INFO =
			"SELECT " +
					"d.GPCS_PROCESS_LOCATION, "+
					"b.PRODUCT_ID, "+ 
					"b.IPP_TAG_NO, "+
					"b.ACTUAL_TIMESTAMP, "+
					"c.LOT_NUMBER, "+
					"a.KD_LOT_NUMBER, "+
					"substr(a.PRODUCT_SPEC_CODE,1,4), "+
					"substr(a.PRODUCT_SPEC_CODE,5,3), "+
					"substr(a.PRODUCT_SPEC_CODE,8,3), "+
					"substr(a.PRODUCT_SPEC_CODE,11,10), "+
					"substr(a.PRODUCT_SPEC_CODE,21,1), "+
					"a.ENGINE_SERIAL_NO, "+
					"a.AF_ON_SEQUENCE_NUMBER, "+
					"b.ACTUAL_TIMESTAMP, "+
					"c.START_PRODUCT_ID, "+
					"CURRENT_TIMESTAMP "+
			"FROM "+
					"galadm.GAL143TBX a, galadm.GAL191TBX b, galadm.GAL217TBX c, galadm.GAL238TBX d "+
			"WHERE "+
					"a.Product_ID=b.Product_ID AND "+
					"b.Division_ID=d.Division_ID AND "+
					"c.PRODUCTION_LOT = a.PRODUCTION_LOT AND "+
					"DATE(b.Actual_Timestamp)=CURRENT DATE";
	
	private static final String SELECT_IPP_PARAMS = new StringBuilder ()
		.append( " Select distinct	" )
		.append( " 		productionLot.line_no as LINE_NUMBER,						" )
		.append( " 		productionLot.plant_code as PLANT_CODE,						" )
		.append( "		division.gpcs_process_location as SCANNED,					" )
		.append( "		ipp.product_id as PSN_NUMBER,								" )
		.append( "		ipp.ipp_tag_no as IPP_TAG_NUMBER,							" )
		.append( "		to_char(ipp.actual_timestamp,'YYYY-MM-DD') as BUSINESS_DATE," )
		.append( "		productionLot.lot_number as PROD_ORDER_LOT_NUMBER,			" )
		.append( "		productionLot.kd_lot_number as EOS_LOT_NUMBER,				" )
		.append( "		productionLot.production_lot as LOT_NUMBER,				" )
		.append( "		substr(productionLot.PRODUCT_SPEC_CODE,1,4) as MTC_MODEL,		" )
		.append( "		substr(productionLot.PRODUCT_SPEC_CODE,5,3) as MTC_TYPE,		" )
		.append( "		substr(productionLot.PRODUCT_SPEC_CODE,8,3) as MTC_OPTION,		" )
		.append( "		substr(productionLot.PRODUCT_SPEC_CODE,11,10) as MTC_COLOR,		" )
		.append( "		substr(productionLot.PRODUCT_SPEC_CODE,21,1) as MTC_INT_COLOR,	" )
		.append( "		nvl(frame.ENGINE_SERIAL_NO, ' ') as EIN_NUMBER,				" )
		.append( "		lpad(trim(right(cast((nvl(frame.AF_ON_SEQUENCE_NUMBER,0)) as varchar(10)),5)),5,'0')  " )
		.append( "		as ASSY_SEQ_NO,											" )
		.append( "		to_char(ipp.ACTUAL_TIMESTAMP,'YYYY-MM-DD-HH24.MI.SS.NNNNNN') as PROCESS_TSTP," )
		.append( "		productionLot.START_PRODUCT_ID as PSN_NO_CALC,				" )
		.append( "		to_char(CURRENT_TIMESTAMP,'YYYY-MM-DD-HH24.MI.SS.NNNNNN') as CREATE_DTTS" )
		.append( " from		" )
		.append( "		GAL191TBX ipp	" )
		.append( "		join GAL143TBX frame on frame.Product_ID = ipp.Product_ID	" )
		.append( "		join GAL212TBX productionLot on frame.PRODUCTION_LOT = productionLot.PRODUCTION_LOT	" )
		.append( "		join GAL238TBX division on ipp.Division_ID = division.Division_ID	" )
		.append( " where ipp.ACTUAL_TIMESTAMP between ?1 and ?2					" )
		.toString();
	
	private static final String SELECT_IPP_IPU_PARAMS = new StringBuilder ()
		.append( " Select distinct	" )
		.append( " 		productionLot.line_no as LINE_NUMBER,						" )
		.append( " 		productionLot.plant_code as PLANT_CODE,						" )
		.append( "		division.gpcs_process_location as SCANNED,					" )
		.append( "		ipp.product_id as PSN_NUMBER,								" )
		.append( "		ipp.ipp_tag_no as IPP_TAG_NUMBER,							" )
		.append( "		to_char(ipp.actual_timestamp,'YYYY-MM-DD') as BUSINESS_DATE," )
		.append( "		productionLot.lot_number as PROD_ORDER_LOT_NUMBER,			" )
		.append( "		productionLot.kd_lot_number as EOS_LOT_NUMBER,				" )
		.append( "		productionLot.production_lot as LOT_NUMBER,				" )
		.append( "		to_char(ipp.ACTUAL_TIMESTAMP,'YYYY-MM-DD-HH24.MI.SS.NNNNNN') as PROCESS_TSTP," )
		.append( "		productionLot.START_PRODUCT_ID as PSN_NO_CALC,				" )
		.append( "		to_char(CURRENT_TIMESTAMP,'YYYY-MM-DD-HH24.MI.SS.NNNNNN') as CREATE_DTTS" )
		.append( " from		" )
		.append( "		GAL191TBX ipp	" )
		.append( "		join MBPN_PRODUCT_TBX mbpn on mbpn.PRODUCT_ID = ipp.Product_ID	" )
		.append( "		join GAL212TBX productionLot on mbpn.CURRENT_ORDER_NO = productionLot.PRODUCTION_LOT	" )
		.append( "		join GAL238TBX division on ipp.Division_ID = division.Division_ID	" )		
		.append( " where ipp.ACTUAL_TIMESTAMP between ?1 and ?2					" )
		.toString();
			
	private static final String SELECT_FIRST_VIN_PER_LOT = new StringBuilder ()
	.append( " Select " )
	.append( " 		productionLot.line_no as LINE_NUMBER,						" )
	.append( " 		productionLot.plant_code as PLANT_CODE,						" )
	.append( "		'' as SCANNED,					" )
	.append( "		history.product_id as PSN_NUMBER,								" )
	.append( "		'',															" )
	.append( "		to_char(history.actual_timestamp,'YYYY-MM-DD') as BUSINESS_DATE," )
	.append( "		productionLot.lot_number as PROD_ORDER_LOT_NUMBER,			" )
	.append( "		productionLot.kd_lot_number as EOS_LOT_NUMBER,				" )
	.append( "		productionLot.production_lot as LOT_NUMBER,				" )
	.append( "		substr(history.PRODUCT_SPEC_CODE,1,4) as MTC_MODEL,			" )
	.append( "		substr(history.PRODUCT_SPEC_CODE,5,3) as MTC_TYPE,			" )
	.append( "		substr(history.PRODUCT_SPEC_CODE,8,3) as MTC_OPTION,		" )
	.append( "		substr(history.PRODUCT_SPEC_CODE,11,10) as MTC_COLOR,		" )
	.append( "		substr(history.PRODUCT_SPEC_CODE,21,1) as MTC_INT_COLOR,	" )
	.append( "		nvl(frame.ENGINE_SERIAL_NO, ' ') as EIN_NUMBER,				" )
	.append( "		lpad(trim(right(cast((nvl(frame.AF_ON_SEQUENCE_NUMBER,0)) as varchar(10)),5)),5,'0')  " )
	.append( "		as ASSY_SEQ_NO,											" )
	.append( "		to_char(history.ACTUAL_TIMESTAMP,'YYYY-MM-DD-HH24.MI.SS.NNNNNN') as PROCESS_TSTP," )
	.append( "		productionLot.START_PRODUCT_ID as PSN_NO_CALC,				" )
	.append( "		to_char(CURRENT_TIMESTAMP,'YYYY-MM-DD-HH24.MI.SS.NNNNNN') as CREATE_DTTS" )
	.append( " from		" )
	.append( " ( " )
	.append( " select  " )
	.append( " process_point_id,  production_date, production_lot, min(actual_timestamp) as actual_timestamp " )
	.append( " from gal215tbx " )
	.append( " where process_point_id in (:PROCESS_POINT_OFF) and actual_timestamp between ?1 and ?2 " )
	.append( " group by process_point_id, production_date, production_lot " )
	.append( " ) fh	 " )
	.append( "		join gal215tbx history on fh.process_point_id = history.process_point_id and fh.production_date = history.production_date " )
	.append( "		and fh.actual_timestamp = history.actual_timestamp and fh.production_lot = history.PRODUCTION_LOT " )
	.append( "		join GAL217TBX productionLot on history.PRODUCTION_LOT = productionLot.PRODUCTION_LOT	" )
	.append( "		join GAL214TBX pp on history.process_point_id = pp.process_point_id	" )
	.append( "		join GAL238TBX division on pp.Division_ID = division.Division_ID	" )
	.append( "		left join gal143tbx frame on frame.product_id = history.product_id		" )
	.toString();
	
	public List<IPPTag> findAllByProductId(String productId){
		
		return findAll(Parameters.with("id.productId", productId),new String[]{"actualTimestamp"},true);
	}

	public List<Object[]> getIPPInfo() {
		List<Object[]> ippInfoList = null; 
		ippInfoList = findAllByNativeQuery(SELECT_IPP_INFO, null, Object[].class);
		return ippInfoList;
	}
	
	/**
	 * Method to get the IPP tags info to send the CPCS system.
	 * IPP interface was for frame products, but new requirements is necessary send the I
	 * PP for other product types like transmissions and the specifics frame attribues will 
	 * send empty
	 * This is able to work for all the products type
	 * @param dateFilter
	 * @param processPoints
	 * @return
	 */
	public List<IPPTagDTO> getIPPInfo( final String startDate, final String endDate )
	{
		List<IPPTagDTO> ippList	=	null;
		Parameters params	=	Parameters.with( "1", startDate );
		params.put( "2", endDate );
		ippList				=	findAllByNativeQuery( SELECT_IPP_PARAMS, params, IPPTagDTO.class );
		return ippList;
	}
	
	/**
	 * Method to get the IPP IPU tags info to send the CPCS system.
	 * This is able to work for all the products type
	 * @param dateFilter
	 * @param processPoints
	 * @return
	 */
	public List<IPPTagDTO> getIPPIPUInfo( final String startDate, final String endDate )
	{
		List<IPPTagDTO> ippList	=	null;
		Parameters params	=	Parameters.with( "1", startDate );
		params.put( "2", endDate );
		ippList				=	findAllByNativeQuery( SELECT_IPP_IPU_PARAMS, params, IPPTagDTO.class );
		return ippList;
	}
	
	/**
	 * For each production lot, retrieve first VIN built for that lot during given time period
	 * This will be run every day, so above translates to providing first VIN for each lot build that day
	 * @param dateFilter
	 * @param processPoints
	 * @return
	 */
	public List<IPPTagDTO> getFirstForEachLot( final String startDate, final String endDate, final String processPoints )
	{
		List<IPPTagDTO> ippList	=	null;
		String paramPPOff	=	StringUtil.toSqlInString( processPoints );
		Parameters params	=	Parameters.with( "1", startDate );
		params.put( "2", endDate );
		String query		=	SELECT_FIRST_VIN_PER_LOT.replace( ":PROCESS_POINT_OFF", paramPPOff );
		ippList				=	findAllByNativeQuery( query, params, IPPTagDTO.class );
		return ippList;
	}
	
	@Transactional
	public IPPTag update(IPPTag ippTag, String ippTagNo) {
		Parameters params = Parameters.with("productId", ippTag.getId().getProductId());
		params.put("oldIppTagNo", ippTag.getId().getIppTagNo());
		params.put("divisionId", ippTag.getId().getDivisionId());
		params.put("newIppTagNo", ippTagNo);
		executeUpdate(UPDATE_TAG_NUMBER, params);
		IPPTagId id = new IPPTagId(ippTag.getProductId(), ippTagNo, ippTag.getDivisionId()); 
		return findByKey(id);
	}
}
