package com.honda.galc.dao.jpa.product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.BearingSelectResultDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.product.BearingSelectResult;
import com.honda.galc.entity.product.BearingSelectResultId;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>IPPTagDaoImpl Class description</h3>
 * <p>
 * IPPTagDaoImpl description
 * </p>
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
 *         Aug 22, 2011
 * 
 * 
 */
public class BearingSelectResultDaoImpl extends BaseDaoImpl<BearingSelectResult, BearingSelectResultId> implements BearingSelectResultDao {

	@Autowired
	private InstalledPartDao installedPartDao;
	
	@Autowired
	private BearingPartDao bearingPartDao;
	
	private static final String START_DATETIME = "START_DATETIME";
	private static final String END_DATETIME = "END_DATETIME";
	private static final String BEARING_PART = "BEARING_PART";
	private static final String BEARING_COLOR = "BEARING_COLOR";
	private static final String COUNT = "COUNT";
	private static final String STARTDATE = "startDate";
	private static final String ENDDATE = "endDate";
	private static final String COMMONDATE = "commonDate";
	private static final String PRODUCT_ID = "PRODUCT_ID";
	private static final String ACTUAL_TIMESTAMP = "ACTUAL_TIMESTAMP";
	private static final String LOCATION = "LOCATION";
	private static final String PROCESSPOINTS = "processPoints";
	private static final String PLANCODE = "planCode";

	private static final String BEARING_SELECT_RESULTS = "select b from BearingSelectResult b where b.id.actualTimestamp > :startDate and b.id.actualTimestamp < :endDate and b.id.actualTimestamp > :commonDate";
	
	private static final String BEARING_SELECT_RESULTS_PROCESS = "select b from " +
			"BearingSelectResult b , ProductResult r  where b.id.productId = r.id.productId and r.id.actualTimestamp > :startDate and" +
			" r.id.actualTimestamp < :endDate and b.id.actualTimestamp > :commonDate and" +
			" r.id.processPointId in (:processPoints) ";
	
	
	private static final String BEARING_SELECT_RESULTS_PROCESS_PLAN_CODE = "select b from " +
			"BearingSelectResult b , ProductResult r , PreProductionLot p where b.id.productId = r.id.productId and r.id.actualTimestamp > :startDate and" +
			" r.id.actualTimestamp < :endDate and b.id.actualTimestamp > :commonDate and p.productionLot = r.productionLot and p.planCode = :planCode and" +
			" r.id.processPointId in (:processPoints) ";
	
	public List<BearingSelectResult> findAllByProductId(String productId) {
		List<BearingSelectResult> list = super.findAll(Parameters.with("id.productId", productId)); 
		return list;
	}

	public BearingSelectResult findByProductId(String productId) {
		List<BearingSelectResult> list = findAllByProductId(productId);
		if (list == null || list.isEmpty()) {
			return null;
		}
		if (list.size() == 1) {
			return list.get(0);
		}
		Comparator<BearingSelectResult> c = new Comparator<BearingSelectResult>() {
			public int compare(BearingSelectResult br, BearingSelectResult br2) {
				return -br.getId().getActualTimestamp().compareTo(br2.getId().getActualTimestamp());
			}
		};
		list = new ArrayList<BearingSelectResult>(list);
		Collections.sort(list, c);
		return list.get(0);
	}
	
	@Transactional
	public void save(BearingSelectResult bearingSelectResult, List<InstalledPart> installedParts) {
		getInstalledPartDao().saveAll(installedParts);
		bearingSelectResult.getId().setActualTimestamp(getDatabaseTimeStamp());
		save(bearingSelectResult);
	}
	
	public List<HashMap<String, Object>> getBearingUsageData(DefaultDataContainer dc) throws ParseException {

		Parameters params = new Parameters();
		SimpleDateFormat modifiedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
		
		Date comandDate = modifiedFormat.parse("2010-01-01 00:00:00.0");
		params.put(COMMONDATE, new java.sql.Timestamp(comandDate.getTime()));			
		String processPoints = (dc.get("PROCESS_POINTS")!=null)?dc.get("PROCESS_POINTS").toString():"  ";
		
		params.put(STARTDATE, new java.sql.Timestamp(modifiedFormat.parse(dc.get(START_DATETIME).toString()).getTime()));
		params.put(ENDDATE, new java.sql.Timestamp (modifiedFormat.parse(dc.get(END_DATETIME).toString()).getTime()));
		
		String planCode = (String) dc.get("PLAN_CODE");		
		List<BearingSelectResult> qresults =null;

		if(!StringUtils.isEmpty(processPoints.trim()) && !StringUtils.isEmpty(planCode)){

			params.put(PLANCODE, planCode);
			String[] processPoint = processPoints.split(",");
			ArrayList<String> processPointList = new ArrayList<String>();

			for(String value : processPoint){
				processPointList.add(value);
			}

			params.put(PROCESSPOINTS,processPointList);
			qresults = findAllByQuery(BEARING_SELECT_RESULTS_PROCESS_PLAN_CODE, params);

		}else if(!StringUtils.isEmpty(processPoints.trim())){

			String[] processPoint = processPoints.split(",");
			ArrayList<String> processPointList = new ArrayList<String>();

			for(String value : processPoint){
				processPointList.add(value);
			}

			params.put(PROCESSPOINTS,processPointList);
			qresults = findAllByQuery(BEARING_SELECT_RESULTS_PROCESS, params);
		}else{	
			qresults = findAllByQuery(BEARING_SELECT_RESULTS, params);
		}
				
		List<HashMap<String, Object>> unionList = new ArrayList<HashMap<String,Object>>();
		if(qresults != null && qresults.size() > 0) {
			for (BearingSelectResult bsr : qresults) {
				String productId = bsr.getId().getProductId();
				Date actualTimestamp = bsr.getId().getActualTimestamp();
				
				HashMap<String, Object> singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalUpperBearing01());
				singleUnionRec.put(LOCATION, "jub1");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalUpperBearing02());
				singleUnionRec.put(LOCATION, "jub2");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalUpperBearing03());
				singleUnionRec.put(LOCATION, "jub3");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalUpperBearing04());
				singleUnionRec.put(LOCATION, "jub4");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalUpperBearing05());
				singleUnionRec.put(LOCATION, "jub5");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalUpperBearing06());
				singleUnionRec.put(LOCATION, "jub6");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalLowerBearing01());
				singleUnionRec.put(LOCATION, "jlb1");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalLowerBearing02());
				singleUnionRec.put(LOCATION, "jlb2");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalLowerBearing03());
				singleUnionRec.put(LOCATION, "jlb3");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalLowerBearing04());
				singleUnionRec.put(LOCATION, "jlb4");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalLowerBearing05());
				singleUnionRec.put(LOCATION, "jlb5");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getJournalLowerBearing06());
				singleUnionRec.put(LOCATION, "jlb6");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodUpperBearing01());
				singleUnionRec.put(LOCATION, "cub1");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodUpperBearing02());
				singleUnionRec.put(LOCATION, "cub2");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodUpperBearing03());
				singleUnionRec.put(LOCATION, "cub3");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodUpperBearing04());
				singleUnionRec.put(LOCATION, "cub4");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodUpperBearing05());
				singleUnionRec.put(LOCATION, "cub5");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodUpperBearing06());
				singleUnionRec.put(LOCATION, "cub6");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodLowerBearing01());
				singleUnionRec.put(LOCATION, "clb1");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodLowerBearing02());
				singleUnionRec.put(LOCATION, "clb2");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodLowerBearing03());
				singleUnionRec.put(LOCATION, "clb3");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodLowerBearing04());
				singleUnionRec.put(LOCATION, "clb4");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodLowerBearing05());
				singleUnionRec.put(LOCATION, "clb5");
				unionList.add(singleUnionRec);
				
				singleUnionRec = new HashMap<String, Object>();
				singleUnionRec.put(PRODUCT_ID, productId);
				singleUnionRec.put(ACTUAL_TIMESTAMP, actualTimestamp);
				singleUnionRec.put(BEARING_PART, bsr.getConrodLowerBearing06());
				singleUnionRec.put(LOCATION, "clb6");
				unionList.add(singleUnionRec);
			}
		}
		
		List<Object[]> qqRes = bearingPartDao.getAllIdAndColorRecords();
		
		List<HashMap<String, Object>> retList = new ArrayList<HashMap<String,Object>>();
		
		for (Object[] bearingPartRec : qqRes) {
			
			String bearingSerialNo = bearingPartRec[0].toString().trim();
			String bearingColour = bearingPartRec[1].toString();
			int prodCount = 0;
			
			for(HashMap<String, Object> singleUnionRec: unionList) {
				if(bearingSerialNo.equals(singleUnionRec.get(BEARING_PART) != null ? singleUnionRec.get(BEARING_PART).toString().trim() : null)) {
					prodCount++; 
				}
			}
			if(prodCount > 0) {
				HashMap<String, Object> rowMap = new HashMap<String, Object>();
				rowMap.put(BEARING_PART, bearingSerialNo);
				rowMap.put(BEARING_COLOR, bearingColour);
				rowMap.put(COUNT, prodCount);
				retList.add(rowMap);
			}
		}
		return retList;
	}

	

	public InstalledPartDao getInstalledPartDao() {
		return installedPartDao;
	}

	public void setInstalledPartDao(InstalledPartDao installedPartDao) {
		this.installedPartDao = installedPartDao;
	}

	public BearingPartDao getBearingPartDao() {
		return bearingPartDao;
	}

	public void setBearingPartDao(BearingPartDao bearingPartDao) {
		this.bearingPartDao = bearingPartDao;
	}
	
	
}
