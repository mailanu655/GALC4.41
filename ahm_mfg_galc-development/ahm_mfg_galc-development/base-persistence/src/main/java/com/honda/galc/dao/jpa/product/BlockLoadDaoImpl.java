package com.honda.galc.dao.jpa.product;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BlockLoadDao;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>BlockLoadDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> BlockLoadDaoImpl description </p>
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
 * <TD>May 26, 2011</TD>
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
 * @since May 26, 2011
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class BlockLoadDaoImpl extends BaseDaoImpl<BlockLoad, String> implements BlockLoadDao{
	private static final long serialVersionUID = 1L;
	
	private static final String DELETE_BY_PRODUCTION_DATE = "DELETE FROM GALADM.BLOCK_LOAD_TBX WHERE STATUS = '9' AND DATE(UPDATE_TIMESTAMP) <= ?";
		
	private static final String FIND_ALL_NON_STAMPED = 
		"SELECT a.MC_NUMBER,a.PRODUCT_SPEC_CODE,a.PRODUCTION_LOT,a.LOT_SIZE,a.REFERENCE_NUMBER,a.STATUS,a.CREATE_TIMESTAMP,a.UPDATE_TIMESTAMP,b.KD_LOT_NUMBER " +
		"FROM GALADM.BLOCK_LOAD_TBX a, GALADM.GAL217TBX b " +
		"WHERE a.PRODUCTION_LOT = b.PRODUCTION_LOT and a.STATUS <> '9' " + 
		"ORDER BY a.PRODUCTION_LOT desc,a.REFERENCE_NUMBER desc";

	private static final String FIND_LAST_BLOCK_LOAD =
		"SELECT a.* " +
		"FROM GALADM.BLOCK_LOAD_TBX a " +
		"WHERE a.STATUS <> 3 " +
		"ORDER BY a.CREATE_TIMESTAMP DESC";
	
	@Transactional
    public int deleteAllByProductionDate(Date productionDate) {
		return executeNativeUpdate(DELETE_BY_PRODUCTION_DATE,Parameters.with("1", productionDate));
	}


	public List<BlockLoad> findAllNonStampedBlocks() {
		List<BlockLoad> blockLoadList = new ArrayList<BlockLoad>();
		List<Object[]> results = findAllByNativeQuery(FIND_ALL_NON_STAMPED, null, Object[].class);
		for(Object[] objects:results) {
			BlockLoad blockLoad = new BlockLoad();
			blockLoad.setMcNumber((String)objects[0]);
			blockLoad.setProductSpecCode((String)objects[1]);
			blockLoad.setProductionLot((String)objects[2]);
			blockLoad.setLotSize((Integer)objects[3]);
			blockLoad.setReferenceNumber((Integer)objects[4]);
			blockLoad.setStatusId(((String)objects[5]));
			blockLoad.setCreateTimestamp((Timestamp)objects[6]);
			blockLoad.setUpdateTimestamp((Timestamp)objects[7]);
			blockLoad.setKdLotNumber((String)objects[8]);
			blockLoadList.add(blockLoad);
		}
		return blockLoadList;
	}


	@Transactional
    public int updateStatus(String mcNumber, BlockLoadStatus status) {
		return update(Parameters.with("statusId", Character.forDigit(status.getId(), 10)), Parameters.with("mcNumber", mcNumber));
	}


	public BlockLoad findLastBlockLoad() {
		return findFirstByNativeQuery(FIND_LAST_BLOCK_LOAD, null);
	}


	public long countByProductionLot(String productionLot) {
		return count(Parameters.with("productionLot", productionLot));
	}

}
