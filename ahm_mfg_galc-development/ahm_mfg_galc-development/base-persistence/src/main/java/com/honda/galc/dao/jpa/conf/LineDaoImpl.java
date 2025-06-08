package com.honda.galc.dao.jpa.conf;


import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.PreviousLineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.PreviousLine;
import com.honda.galc.entity.product.FactoryNewsCurrent;
import com.honda.galc.service.Parameters;

/**
 * added new method findEligibleRepairLines for Qics Repair In Panel
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public class LineDaoImpl extends BaseDaoImpl<Line,String> implements LineDao {

    @Autowired
    private ProcessPointDao processPointDao;
    
    @Autowired
    private PreviousLineDao PreviousLineDao; 
    
    private static final String FIND_LINE_USING_ENTRYPROCESSPOINTID = 
    	"select al.* from GALADM.GAL214TBX pp " +
    	"join GALADM.GAL195TBX l on pp.LINE_ID = l.LINE_ID " +
    		"join GALADM.GAL128TBX d on l.DIVISION_ID = d.DIVISION_ID " +
    		"join GALADM.GAL128TBX ad on d.SEQUENCE_NUMBER >= ad.SEQUENCE_NUMBER " +
    		"join GALADM.GAL195TBX al on ad.DIVISION_ID = al.DIVISION_ID and ((d.DIVISION_ID=al.DIVISION_ID and al.LINE_SEQUENCE_NUMBER<=l.LINE_SEQUENCE_NUMBER) or (d.DIVISION_ID <> al.DIVISION_ID)) and al.ENTRY_PROCESS_POINT_ID <> '' " +
    		"join GALADM.GAL214TBX app on al.LINE_ID = app.LINE_ID and ((l.LINE_ID=al.LINE_ID and app.SEQUENCE_NUMBER<=pp.SEQUENCE_NUMBER) or (l.LINE_ID<>al.LINE_ID)) and al.ENTRY_PROCESS_POINT_ID=app.PROCESS_POINT_ID " +
    		"where pp.PROCESS_POINT_ID=?1 " +
    		"order by ad.SEQUENCE_NUMBER desc, al.LINE_SEQUENCE_NUMBER desc " +
    		"fetch first 1 row only with UR for read only";
    
    private static final String FIND_LINE_USING_LINENAME = "select a from Line a where a.lineName = :lineName";
    
    private static final String GET_PRODUCTION_PROGRESS = "SELECT INV.PP_ALL_SEQ_NUM,INV.SITE_NAME,INV.SITE_DESCRIPTION,INV.DIVISION_ID,INV.DIV_NAME,INV.DIV_SEQ_NUM,INV.LINE_ID,INV.LINE_NAME, " +
		    "INV.LINE_SEQ_NUM,INV.MINIMUM_INVENTORY,INV.STD_INVENTORY,INV.MAXIMUM_INVENTORY,INV.CURRENT_INVENTORY,PROD_SHIFT_PLAN.PRODUCTION_DATE,PROD_SHIFT_PLAN.SHIFT, " +
		    "PROD_SHIFT_PLAN.SHIFT_PLAN, PROD_SHIFT_PLAN.SHIFT_TARGET,MIN(PERIOD.START_TIMESTAMP) SHIFT_START,MAX(PERIOD.END_TIMESTAMP) SHIFT_END, " +
		    //Need to use Min because multiple tracking points per line is possible
		    "COUNT(DISTINCT HIST.PRODUCT_ID) AS SHIFT_ACTUAL FROM (SELECT((coalesce(DIV.SEQUENCE_NUMBER,0)+1)*1000000+(coalesce(LINE.LINE_SEQUENCE_NUMBER,0)+1)*1000) AS PP_ALL_SEQ_NUM, " + 
		    "SITE.SITE_NAME,TRIM(SITE.SITE_DESCRIPTION) AS SITE_DESCRIPTION,DIV.DIVISION_ID, TRIM(DIV.DIVISION_NAME) AS DIV_NAME,DIV.SEQUENCE_NUMBER AS DIV_SEQ_NUM, " +
		    //Look up the current inventory by tracking status of VINs in 143
		    "trim(LINE.LINE_ID) AS LINE_ID ,TRIM(LINE.LINE_NAME) AS LINE_NAME,LINE.LINE_SEQUENCE_NUMBER AS LINE_SEQ_NUM,LINE.MINIMUM_INVENTORY,LINE.STD_INVENTORY,LINE.MAXIMUM_INVENTORY, " + 
		    "COALESCE(COUNT(DISTINCT INPP.PRODUCT_ID),0) AS CURRENT_INVENTORY FROM GALADM.GAL117TBX SITE " +  
		    "LEFT JOIN GALADM.GAL128TBX DIV ON SITE.SITE_NAME = DIV.SITE_NAME " +  
		    "LEFT JOIN GALADM.GAL195TBX LINE ON DIV.DIVISION_ID = LINE.DIVISION_ID " +  
		    "LEFT JOIN GALADM.GAL214TBX PP ON LINE.LINE_ID = PP.LINE_ID " +
		    "LEFT JOIN GALADM.GAL176TBX INPP ON INPP.LINE_ID=LINE.LINE_ID " +
		    //Use tracking point flag to get Lines but need to group by LINE since could have multiple tracking process points
		    //Only return factory news data for the plant provided
		    "WHERE PP.TRACKING_POINT_FLAG = 1 AND DIV.PLANT_NAME = ?1 " + 
		    "GROUP BY SITE.SITE_NAME,SITE.SITE_DESCRIPTION,DIV.DIVISION_ID, DIV.DIVISION_NAME,DIV.SEQUENCE_NUMBER,LINE.LINE_ID,LINE.LINE_NAME,LINE.LINE_SEQUENCE_NUMBER, " +
		    //This represents Lines with inventory
		    //Join to get current production day's plan and target by shift
		    "LINE.MINIMUM_INVENTORY,LINE.STD_INVENTORY,LINE.MAXIMUM_INVENTORY) AS INV " +
		    "LEFT JOIN (SELECT LINE_PROD_DATE.LINE_ID,LINE_PROD_DATE.LINE_NAME,LINE_PROD_DATE.PROCESS_POINT_TYPE,LINE_PROD_DATE.LINE_NO,LINE_PROD_DATE.PROCESS_LOCATION, " +
		    //Use PROCESS POINT TYPE to determine if we should use ON or OFF capacity in 226
		    "LINE_PROD_DATE.PRODUCTION_DATE,PROD_SHIFT.SHIFT, " +
		    //Use PROCESS POINT TYPE and current timestamp to calculate target as of now
		    "CASE WHEN LINE_PROD_DATE.PROCESS_POINT_TYPE IN (1,9) THEN SUM(PROD_SHIFT.CAPACITY_ON) WHEN LINE_PROD_DATE.PROCESS_POINT_TYPE IN (2,8,13) THEN SUM(PROD_SHIFT.CAPACITY) END AS SHIFT_PLAN, " +
		    //This is a previous period so sum entire capacity
		    "CASE WHEN LINE_PROD_DATE.PROCESS_POINT_TYPE IN (1,9) THEN SUM(case when PROD_SHIFT.PLAN = 'Y' and PROD_SHIFT.START_TIMESTAMP <= current_timestamp and current_timestamp >= PROD_SHIFT.END_TIMESTAMP " +
		    //This is the current period so sum partial amount based on percentage of time
		    "then PROD_SHIFT.CAPACITY_ON when PROD_SHIFT.PLAN = 'Y' and current timestamp >= PROD_SHIFT.START_TIMESTAMP and current timestamp <= PROD_SHIFT.END_TIMESTAMP " +
		    //All other periods should not be counted
		    "then timestampdiff(2, char(current timestamp - PROD_SHIFT.START_TIMESTAMP)) * (decimal(PROD_SHIFT.CAPACITY_ON,20,15)/(TIMESTAMPDIFF(2,char(PROD_SHIFT.END_TIMESTAMP-PROD_SHIFT.START_TIMESTAMP )))) " +
		    "else 0 end) WHEN LINE_PROD_DATE.PROCESS_POINT_TYPE IN (2,8,13) THEN SUM(case when PROD_SHIFT.PLAN = 'Y' and PROD_SHIFT.START_TIMESTAMP <= current_timestamp " + 
		    //This is a previous period so sum entire capacity
		    "and current_timestamp >= PROD_SHIFT.END_TIMESTAMP " +
		    //This is the current period so sum partial amount based on percentage of time
		    "then PROD_SHIFT.CAPACITY when PROD_SHIFT.PLAN = 'Y' and current timestamp >= PROD_SHIFT.START_TIMESTAMP and current timestamp <= PROD_SHIFT.END_TIMESTAMP " +
		    "then timestampdiff(2, char(current timestamp - PROD_SHIFT.START_TIMESTAMP)) * (decimal(PROD_SHIFT.CAPACITY,20,15)/(TIMESTAMPDIFF(2,char(PROD_SHIFT.END_TIMESTAMP-PROD_SHIFT.START_TIMESTAMP )))) " +
		    //All other periods should not be counted 
		    "else 0 end) END AS SHIFT_TARGET FROM (SELECT LINE.LINE_ID,LINE.LINE_NAME,PP.PROCESS_POINT_ID,PP.PROCESS_POINT_TYPE,PROD_DATE.LINE_NO,PROD_DATE.PROCESS_LOCATION,PROD_DATE.PRODUCTION_DATE " +
		    "FROM GALADM.GAL214TBX PP INNER JOIN GALADM.GAL195TBX LINE ON PP.LINE_ID=LINE.LINE_ID INNER JOIN GALADM.GAL128TBX DIV ON DIV.DIVISION_ID=LINE.DIVISION_ID INNER JOIN GALADM.GAL238TBX MAP " + 
		    //Include Plant Code so we use index, need to have this column in GAL238TBX
		    "ON (MAP.DIVISION_ID=DIV.DIVISION_ID) INNER JOIN GALADM.GAL226TBX PROD_DATE ON PROD_DATE.PLANT_CODE=?2 " +
		    //Find current period and use this to determine the current production date 
		    "AND PROD_DATE.LINE_NO=MAP.GPCS_LINE_NO AND PROD_DATE.PROCESS_LOCATION=MAP.GPCS_PROCESS_LOCATION AND PROD_DATE.START_TIMESTAMP <= current_timestamp AND current_timestamp <= PROD_DATE.END_TIMESTAMP " + 
		    //This returns the current production date
		    "WHERE PP.TRACKING_POINT_FLAG = 1 GROUP BY LINE.LINE_ID,LINE.LINE_NAME,PP.PROCESS_POINT_ID,PP.PROCESS_POINT_TYPE,PROD_DATE.LINE_NO,PROD_DATE.PROCESS_LOCATION,PROD_DATE.PRODUCTION_DATE) AS LINE_PROD_DATE " +
		    //Include Plant Code so we use index, need to have this column in GAL238TBX
		    "LEFT JOIN GALADM.GAL226TBX PROD_SHIFT ON PROD_SHIFT.PLANT_CODE=?3 " +
		    //Find all periods for current production date
		    "AND LINE_PROD_DATE.LINE_NO=PROD_SHIFT.LINE_NO AND LINE_PROD_DATE.PROCESS_LOCATION=PROD_SHIFT.PROCESS_LOCATION AND LINE_PROD_DATE.PRODUCTION_DATE = PROD_SHIFT.PRODUCTION_DATE " +
		    //Group by shift to calculate plan and target
		    "GROUP BY LINE_PROD_DATE.LINE_ID,LINE_PROD_DATE.LINE_NAME,LINE_PROD_DATE.PROCESS_POINT_TYPE,LINE_PROD_DATE.LINE_NO,LINE_PROD_DATE.PROCESS_LOCATION,LINE_PROD_DATE.PRODUCTION_DATE,PROD_SHIFT.SHIFT) AS PROD_SHIFT_PLAN ON INV.LINE_ID=PROD_SHIFT_PLAN.LINE_ID " +
		    //Include Plant Code so we use index, need to have this column in GAL238TBX
		    "LEFT JOIN GALADM.GAL226TBX PERIOD ON PERIOD.PLANT_CODE=?4 " +
		    //Find all periods for the given production date and shift
		    "AND PERIOD.LINE_NO = PROD_SHIFT_PLAN.LINE_NO AND PERIOD.PROCESS_LOCATION = PROD_SHIFT_PLAN.PROCESS_LOCATION AND PERIOD.PRODUCTION_DATE=PROD_SHIFT_PLAN.PRODUCTION_DATE AND PERIOD.SHIFT=PROD_SHIFT_PLAN.SHIFT " +
		    //Find all tracking points for given LINE_ID since can be multiple
		    "LEFT JOIN GALADM.GAL214TBX PP ON INV.LINE_ID=PP.LINE_ID AND PP.TRACKING_POINT_FLAG=1 " +
		    //Find all products that went through Process Point during the given period
		    "LEFT JOIN GALADM.GAL215TBX HIST ON PP.PROCESS_POINT_ID=HIST.PROCESS_POINT_ID AND HIST.ACTUAL_TIMESTAMP BETWEEN PERIOD.START_TIMESTAMP AND PERIOD.END_TIMESTAMP " +
		    "GROUP BY INV.PP_ALL_SEQ_NUM,INV.SITE_NAME,INV.SITE_DESCRIPTION,INV.DIVISION_ID,INV.DIV_NAME,INV.DIV_SEQ_NUM,INV.LINE_ID,INV.LINE_NAME,INV.LINE_SEQ_NUM,INV.MINIMUM_INVENTORY,INV.STD_INVENTORY,INV.MAXIMUM_INVENTORY, " +
		    "INV.CURRENT_INVENTORY,PROD_SHIFT_PLAN.PRODUCTION_DATE,PROD_SHIFT_PLAN.SHIFT, PROD_SHIFT_PLAN.SHIFT_PLAN, PROD_SHIFT_PLAN.SHIFT_TARGET ORDER BY INV.PP_ALL_SEQ_NUM,MIN(PERIOD.START_TIMESTAMP) WITH CS FOR READ ONLY";
 
     private static final String FIND_ELIGIBLE_REPAIR_LINES = "select e from Line e where e.lineTypeId=2 and e.divisionId = :divisionId and e.entryProcessPointId is not null"; 
     
     private static final String GET_AGED_INVENTORY_FOR_LINE = "select prodHist.product_id, prodHist.actual_timestamp, " +
    		"TIMESTAMPDIFF (4, CURRENT_TIMESTAMP - prodHist.actual_timestamp) as MINS_ON_LINE " +
    		"FROM gal215tbx prodHist, gal176tbx prodSeq " +
    		"WHERE prodHist.product_id = prodSeq.PRODUCT_ID " +
    		"AND TIMESTAMPDIFF (4, CURRENT_TIMESTAMP - prodHist.actual_timestamp) > ?2 " +
    		"AND ProdSeq.line_id = ?1 " +
    		"AND prodHist.process_point_id in (SELECT process_point_id " + 
    	                                 "FROM gal195tbx line, gal214tbx pp " +
    	                                 "WHERE line.line_id = pp.line_id " +
    	                                 "AND line.line_id = ?1 " +
    	                                 "AND pp.tracking_point_flag = 1) " +
    	    "ORDER BY prodHist.actual_timestamp DESC FOR READ ONLY";
     
     private static final String FIND_ALL_LINES_PARENTS = "select line.LINE_ID,line.LINE_NAME,line.DIVISION_ID,line.SITE_NAME,line.PLANT_NAME,count(processpoint.PROCESS_POINT_ID) from GAL195TBX line "+ 
     		" left join GAL214TBX processpoint on line.LINE_ID = processpoint.LINE_ID "+
    		" and line.SITE_NAME = processpoint.SITE_NAME "+ 
    		" and line.PLANT_NAME = processpoint.PLANT_NAME  "+
    		" and line.DIVISION_ID = processpoint.DIVISION_ID  "+
    		" group by line.LINE_ID,line.LINE_NAME,line.DIVISION_ID,line.SITE_NAME,line.PLANT_NAME ";

     private static final String FIND_NEXT_LINE_SEQ = "select a.LINE_SEQUENCE_NUMBER from gal195tbx a,gal195tbx b "+
      		" where a.SITE_NAME = b.SITE_NAME  "+
     		" and   a.PLANT_NAME= b.PLANT_NAME "+
     		" and   a.DIVISION_NAME=b.DIVISION_NAME "+
     		" and   a.LINE_SEQUENCE_NUMBER>b.LINE_SEQUENCE_NUMBER "+ 
     		" and   b.LINE_ID=?1 "+
     		" order by a.LINE_SEQUENCE_NUMBER fetch first 1 rows only";

     private static final String FIND_FIRST_LINE_SEQ_OF_NEXT_DIV = 
    		" select min(LINE_SEQUENCE_NUMBER) "+ 
      		" from gal195tbx  "+
      		" where division_id in ( "+
      		"   select e.division_id  "+
      		"   from gal128tbx e, gal195tbx f "+ 
      		"   where e.SEQUENCE_NUMBER = ( "+
      		"     select min(c.SEQUENCE_NUMBER) "+ 
      		"     from gal128tbx c, gal195tbx d  "+
      		"     where c.SEQUENCE_NUMBER> ( "+
      		"       select a.SEQUENCE_NUMBER  "+
      		"       from gal128tbx a,gal195tbx b "+ 
      		"       where b.line_id=?1  "+
      		"       and a.division_id=b.division_id "+ 
      		"       and a.plant_name = b.plant_name  "+
      		"       and a.site_name = b.site_name "+
      		"  )    "+
      		"  and d.line_id=?1 "+ 
      		"  and c.plant_name = d.plant_name "+ 
      		"  and c.site_name = d.site_name "+
      		" )  "+
      		" and f.line_id=?1 "+ 
      		" and e.plant_name = f.plant_name "+ 
      		" and e.site_name = f.site_name "+
      		" )";

     private static final String FIND_LAST_LINE_SEQ = "select a.LINE_SEQUENCE_NUMBER from gal195tbx a,gal195tbx b "+
        		" where a.SITE_NAME = b.SITE_NAME  "+
       		" and   a.PLANT_NAME= b.PLANT_NAME "+
       		" and   a.DIVISION_NAME=b.DIVISION_NAME "+
       		" and   a.LINE_SEQUENCE_NUMBER<b.LINE_SEQUENCE_NUMBER "+ 
       		" and   b.LINE_ID=?1 "+
       		" order by a.LINE_SEQUENCE_NUMBER desc fetch first 1 rows only";

       private static final String FIND_LAST_LINE_SEQ_OF_PREVIOUS_DIV = 
      		" select max(LINE_SEQUENCE_NUMBER) "+ 
        		" from gal195tbx  "+
        		" where division_id in ( "+
        		"   select e.division_id  "+
        		"   from gal128tbx e, gal195tbx f "+ 
        		"   where e.SEQUENCE_NUMBER = ( "+
        		"     select max(c.SEQUENCE_NUMBER) "+ 
        		"     from gal128tbx c, gal195tbx d  "+
        		"     where c.SEQUENCE_NUMBER < ( "+
        		"       select a.SEQUENCE_NUMBER  "+
        		"       from gal128tbx a,gal195tbx b "+ 
        		"       where b.line_id=?1  "+
        		"       and a.division_id=b.division_id "+ 
        		"       and a.plant_name = b.plant_name  "+
        		"       and a.site_name = b.site_name "+
        		"  )    "+
        		"  and d.line_id=?1 "+ 
        		"  and c.plant_name = d.plant_name "+ 
        		"  and c.site_name = d.site_name "+
        		" )  "+
        		" and f.line_id=?1 "+ 
        		" and e.plant_name = f.plant_name "+ 
        		" and e.site_name = f.site_name "+
        		" )";
       
       private static final String FIND_NEXT_LINE_ID = "select a.LINE_ID from gal195tbx a,gal195tbx b "+
         	" where a.SITE_NAME = b.SITE_NAME  "+
        		" and   a.PLANT_NAME= b.PLANT_NAME "+
        		" and   a.DIVISION_NAME=b.DIVISION_NAME "+
        		" and   a.LINE_SEQUENCE_NUMBER>b.LINE_SEQUENCE_NUMBER "+ 
        		" and   b.LINE_ID=?1 "+
        		" order by a.LINE_SEQUENCE_NUMBER fetch first 1 rows only";

        private static final String FIND_FIRST_LINE_ID_OF_NEXT_DIV = 
     		   " select LINE_ID " +
     				   " from gal195tbx g " +
     				   " where division_id in ( " +
     				   "   select e.division_id " +
     				   "   from gal128tbx e, gal195tbx f " +
     				   "   where e.SEQUENCE_NUMBER = ( " +
     				   "     select min(c.SEQUENCE_NUMBER) " +
     				   "     from gal128tbx c, gal195tbx d " +
     				   "     where c.SEQUENCE_NUMBER> ( " +
     				   "       select a.SEQUENCE_NUMBER " +
     				   "       from gal128tbx a,gal195tbx b " +
     				   "       where b.line_id=?1 " +
     				   "       and a.division_id=b.division_id " +
     				   "       and a.plant_name = b.plant_name " +
     				   "       and a.site_name = b.site_name " +
     				   "  ) " +
     				   "  and d.line_id=?1 " +
     				   "  and c.plant_name = d.plant_name " +
     				   "  and c.site_name = d.site_name " +
     				   " ) " +
     				   " and f.line_id=?1 " +
     				   " and e.plant_name = f.plant_name " +
     				   " and e.site_name = f.site_name " +
     				   " ) " +
     				   " and LINE_SEQUENCE_NUMBER = (select min(LINE_SEQUENCE_NUMBER) from gal195tbx h where g.plant_name = h.plant_name " +
     				   " and g.site_name = h.site_name) fetch first 1 rows only; ";

        private final String FIND_TRACKING_LINES_BY_PRODUCT_TYPE = "select distinct r from ProcessPoint p, ComponentProperty q, Line r "
        		+ " where p.lineId = r.lineId and p.processPointId = q.id.componentId and q.id.propertyKey='PRODUCT_TYPE' and q.propertyValue=:product_type " 
    			+ " and p.trackingPointFlag=1";
        
    	private static final String FIND_ALL_BY_LINE_IDS = "select e from Line e where e.lineId in (:lineIds)";

        
     @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.NOT_SUPPORTED)
    public List<Line> findAllByDivisionId(Division division,boolean withChildren) {
          Parameters params = Parameters.with("siteName", division.getSiteName())
                                .put("plantName", division.getPlantName())
                                .put("divisionId",division.getDivisionId());
          List<Line> lines =  findAll(params);
          
          if(withChildren) {
              for(Line line : lines) {
                  line.setProcessPoints(processPointDao.findAllByLine(line));
              }
          }
          return lines;
    }
    
    @SuppressWarnings("unchecked")
    public List<Line> findAlLinesParents() {
        List<Line> lines =  new ArrayList<Line>();
        
        List<Object[]> lineLstObjs = null;
        lineLstObjs = findResultListByNativeQuery(FIND_ALL_LINES_PARENTS, new Parameters());
        for(Object[] operObj : lineLstObjs){
        	Line line = new Line();
        	line.setLineId(operObj[0].toString());
        	line.setLineName(operObj[1]==null?"":operObj[1].toString());
        	line.setDivisionId(operObj[2]==null?"":operObj[2].toString());
        	line.setSiteName(operObj[3]==null?"":operObj[3].toString());
        	line.setPlantName(operObj[4]==null?"":operObj[4].toString());
        	line.setProcessPointListCount(Integer.parseInt(operObj[5].toString()));
        	lines.add(line);
        }
        return lines;
  }

    public Line findByLineName(String lineName) {
    	return findFirstByQuery(FIND_LINE_USING_LINENAME, Parameters.with("lineName", lineName));
    }
    
    public int getAgedInventory(String lineId, int ageInMins) {
		getLogger().info("About to execute getAgedInventory() query");
		Parameters parameters = Parameters.with("1", lineId).put("2", ageInMins);
		List<Object[]> agedInventory = executeNative(parameters, GET_AGED_INVENTORY_FOR_LINE);
		getLogger().info("Finished executing getAgedInventory() query");
		return agedInventory.size();
    }
    
    public int findNextLineSeq(String lineId) {
		Parameters parameters = Parameters.with("1", lineId);
		Integer seq = findFirstByNativeQuery(FIND_NEXT_LINE_SEQ, parameters, Integer.class);
		if (seq == null) {
			seq = findFirstByNativeQuery(FIND_FIRST_LINE_SEQ_OF_NEXT_DIV, parameters, Integer.class);
		}
		return (seq == null)? -1 : seq.intValue();
    }

    public ArrayList<FactoryNewsCurrent> getFactoryNews(String plantName, String gpcsPlantCode) {
    	Hashtable<String, FactoryNewsCurrent> factoryNewsMap = new Hashtable<String, FactoryNewsCurrent>();
    	ArrayList<FactoryNewsCurrent> factoryNewsList = new ArrayList<FactoryNewsCurrent>();
    	
    	try {
    		List<Object[]> productionPlan = getProductionProgress(plantName, gpcsPlantCode);		
    		Date asOfDate = getDatabaseTimeStamp();
    		
    		for(Object[] result: productionPlan) {
    			String divisionName = ((String)result[4]).trim();
    			String lineName = ((String)result[7]).trim();
    			
    			// there could be duplicates in the list returned, hence the map
    			if (!factoryNewsMap.containsKey(lineName)) {
    				FactoryNewsCurrent factoryNewsCurr = new FactoryNewsCurrent();
    				if (factoryNewsList.size() > 0)
    					factoryNewsList.get(factoryNewsList.size() - 1).setNextLineName(lineName);
    				
    				factoryNewsCurr.setSequenceNumber(getInt(result[0]));
    				factoryNewsCurr.setDivisionName(divisionName);
    				factoryNewsCurr.setDivisionId(getString(result[3]));
    				factoryNewsCurr.setLineName(lineName);
    				factoryNewsCurr.setLineId(getString(result[6]));
    				factoryNewsCurr.setPlan(getInt(result[15]));
    				factoryNewsCurr.setTarget(getInt(result[16]));
    				factoryNewsCurr.setAsOfDate(asOfDate);
    				factoryNewsCurr.setCurrentInventory(getInt(result[12]));	
    				factoryNewsList.add(factoryNewsCurr);
    				populateShiftActual(result, factoryNewsCurr);
    				factoryNewsMap.put(lineName, factoryNewsCurr);
    			} else {
    				FactoryNewsCurrent fnCurr = factoryNewsMap.get(lineName);
    				fnCurr.setPlan(fnCurr.getPlan() + getInt(result[15]));
    				fnCurr.setTarget(fnCurr.getTarget() + getInt(result[16]));
    				populateShiftActual(result, fnCurr);
    			}
    		}
    		
    		// set Difference
    		for(FactoryNewsCurrent fnCur: factoryNewsList) {
    			fnCur.setDifference(fnCur.getActualTotal() - fnCur.getTarget());
    		}
    		
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	return factoryNewsList;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	private List<Object[]> getProductionProgress(String plantName, String gpcsPlantCode) {

		getLogger().info("About to execute getProductionProgress() query");
		Parameters parameters = Parameters.with("1", plantName).put("2", gpcsPlantCode).put("3",gpcsPlantCode).put("4", gpcsPlantCode);
		List<Object[]> productionPlan = executeNative(parameters, GET_PRODUCTION_PROGRESS);
		getLogger().info("Finished executing getProductionProgress() query");

		return productionPlan;
	}

	private void populateShiftActual(Object[] result, FactoryNewsCurrent factoryNewsCurr) {
		int actual = (getInt(result[19]));
		switch(getInt(result[14])) {
			case 1:
				factoryNewsCurr.setActual1st(actual);
				break;
			case 2:
				factoryNewsCurr.setActual2nd(actual);
				break;
			case 3:
				factoryNewsCurr.setActual3rd(actual);
				break;
			default:
				break;
		}
		factoryNewsCurr.setActualTotal(factoryNewsCurr.getActualTotal() + actual);
    }

	public Line getByEntryProcessPointId(String entryProcessPointId) {
		return findFirstByNativeQuery(FIND_LINE_USING_ENTRYPROCESSPOINTID, Parameters.with("1", entryProcessPointId));
	}
	
	@Transactional
	public Line save(Line line) {
		//Remark : jpa 1.x does not persist deletion of children from collection - have to do this manually
		List<PreviousLine> linesToDelete = new ArrayList<PreviousLine>();
		for (PreviousLine pl : getPreviousLineDao().findAllByLineId(line.getId())) {
			if (!line.getPreviousLines().contains(pl)) {
				linesToDelete.add(pl);
			}
		}
		if (!linesToDelete.isEmpty()) {
			getPreviousLineDao().removeAll(linesToDelete);
		}
		return super.save(line);
	}

	// === get/set === //
	public PreviousLineDao getPreviousLineDao() {
		return PreviousLineDao;
	}

	public void setPreviousLineDao(PreviousLineDao previousLineDao) {
		PreviousLineDao = previousLineDao;
	}
	
	public List<Line> findEligibleRepairLines(String divisionId) {
		return findAllByQuery(FIND_ELIGIBLE_REPAIR_LINES, Parameters.with("divisionId", divisionId));
	}
	
    public String findNextLineId(String lineId) {
		Parameters parameters = Parameters.with("1", lineId);
		String nextLineId = findFirstByNativeQuery(FIND_NEXT_LINE_ID, parameters, String.class);
		if (nextLineId == null) {
			nextLineId = findFirstByNativeQuery(FIND_FIRST_LINE_ID_OF_NEXT_DIV, parameters, String.class);
		}
		return (nextLineId == null)? "" : nextLineId;
    }

	@Override
	public List<Line> findAllTrackingLinesByProductType(String productType) {
		if(StringUtils.isBlank(productType))  return null;
       	Parameters params = Parameters.with("product_type", productType);
		return findAllByQuery(FIND_TRACKING_LINES_BY_PRODUCT_TYPE, params);
	}	

    public List<Line> findAllByLineIds(List<String> lineIds) {
		return findAllByQuery(FIND_ALL_BY_LINE_IDS, Parameters.with("lineIds", lineIds));
    }

}
