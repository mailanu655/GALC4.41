package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.service.GenericDaoService;

/**
 * 
 * <h3>PruneMbpnTask</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PruneMbpnTask description </p>
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
 * <TD>Oct 30, 2015</TD>
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
 * @since Oct 30, 2015
 */
public class PruneMbpnTask extends PrunningAbstractTask{

	String trackingProcessPointId;
	
	public PruneMbpnTask(String name) {
		super(name);
		init();
	}

	private void init() {
		trackingProcessPointId = getPropertyBean().getTrackingProcessPointId();
	}

	@Override
	public void performPrunning() {
		logger.info("start to prune Mbpn data");
		pruneMbpnData();
		logger.info("finished to prune Mbpn data");
		
	}

	private void pruneMbpnData() {
		List<Date> dates = findMbpnProductionDates(trackingProcessPointId, startProductionDate);

		int index = 0;
		for(Date date : dates) {
			logger.info("start to prune Mbpn data of production date " + date);
			pruneMbpnByProductionDate(date);
			logger.info("finish to prune Mbpn data of production date " + date);
			if(++index >= getBatchSize()) break;
		}
		
	}

	private void pruneMbpnByProductionDate(Date date) {
		for(TableDefinitions.TableDefinition table : TableDefinitions.mbpnTables.getTables()) {
			int count = getService(GenericDaoService.class).deleteAllByProductionDate(table.getTableName(), trackingProcessPointId, date);
			logger.info("deleted " + count + " " + table.getTableName() + " " + 
					 table.getTableDescription() +  " data of production date " + date);
		}	
		
		// prune history and product
		int count = getService(GenericDaoService.class).deleteMbpnProductsAndHistory(trackingProcessPointId, date);
		logger.info("deleted " + count + " MBPN_PRODUCT_TBX and GAL215TBX(History) " + 
				 "Mbpn Product" +  " data of production date " + date);
		
	}

	private List<Date> findMbpnProductionDates(String processPointId, Date date) {
		return getDao(MbpnDao.class).findAllProductionDates(processPointId, date);
	}

	@Override
	protected void checkConfiguration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Set<String> getAllPrunningTables() {
		// TODO Auto-generated method stub
		return null;
	}


}