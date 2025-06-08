package com.honda.galc.dao.jpa.conf;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.service.Parameters;

import java.util.List;

/**
 * 
 * <h3>DivisionDaoImpl Class description</h3>
 * <p> DivisionDaoImpl description </p>
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
 * Mar 29, 2010
 *
 */
public class DivisionDaoImpl extends BaseDaoImpl<Division,String> implements DivisionDao {
    
    @Autowired
    private LineDao lineDao;
    
    @Autowired
    private TerminalDao terminalDao;
    
    @Autowired
    private ZoneDao zoneDao;
    
    @Autowired
    private DeviceDao deviceDao;
    
	private static final String FIND_DISTINCT_PLANT = "SELECT DISTINCT PLANT_NAME FROM GALADM.GAL128TBX where SITE_NAME  = ?1";	
	private static final String FIND_DIVISION_CHILDREN = "select division.division_id,division.division_name,division.plant_name, "+
			" (select count(terminal.HOST_NAME) from GAL234TBX terminal where terminal.division_id=division.division_id ) as clientCount, "+
			" (select count(device.CLIENT_ID) from GAL253TBX device where device.division_id=division.division_id ) as deviceCount  "+
			" from GAL128TBX division  "+
			" where division.division_id = ?1";	
	private static final String FIND_PLANT_BY_SITE = "SELECT DISTINCT PLANT_NAME FROM GALADM.GAL128TBX JOIN GALADM.QI_EXTERNAL_SYSTEM_DATA_TBX on SITE_NAME = ENTRY_SITE where SITE_NAME  = ?1";
	
	private static final String UPDATE_DIVISION_ID = "update Division d set d.siteName = :siteName, d.plantName = :plantName where d.divisionId = :divisionId";
	private static final String UPDATE_LINE_ID = "update Line l set l.siteName = :siteName, l.plantName = :plantName where l.divisionId = :divisionId";
	private static final String UPDATE_PROCESS_POINT_ID = "update ProcessPoint p set p.siteName = :siteName, p.plantName = :plantName where p.divisionId = :divisionId";
	
	private static final String FIND_DISTINCT_DIVISION_ID = "select e.divisionId from Division e";
	
	private static final String FIND_DISTINCT_DIVISION_ID_AND_NAME = "select e.DIVISION_ID,e.DIVISION_NAME from GAL128TBX e";

	private static final String FIND_DIVISION_FOR_PLANT = "select distinct gal.plant_name, gal.division_name,gal.division_id from gal128tbx gal";
	
	private static final String FIND_DIVISION_ID = "select distinct gal.division_id from gal128tbx gal where gal.division_name=?1";
	
	private static final String FIND_ALL_BY_DIVISION_IDS = "select d from Division d where d.divisionId in (:divisionIds)";

	@SuppressWarnings("unchecked")
    public List<Division> findDept() {
        Query q = entityManager.createQuery(
                "select distinct dept.name, dept.sequence from Division as dept order by dept.sequence");
        return (List<Division>) q.getResultList();
    }

    public Division findByDivisionId(String divisionId) {
    	return findByKey(divisionId);
    }

    public List<Division> findById(String siteName) {
    	
    		return findAll(Parameters.with("siteName", siteName));

    }

    public List<Division> findById(String siteName, String plantName) {
    	Parameters params = Parameters.with("siteName", siteName)
    						.put("plantName", plantName);
    	
    	return findAll(params);
    }

    public List<Division> findAllByDivisionIds(List<String> divisionIds) {
		return findAllByQuery(FIND_ALL_BY_DIVISION_IDS, Parameters.with("divisionIds", divisionIds));
    }

    public Division findByKeyWithChildren(String divisionId) {
    	
    	Division division = findByKey(divisionId);
    	
    	division.setTerminals(terminalDao.findAllByDivisionId(divisionId));
    	division.setDevices(deviceDao.findAllByDivisionId(divisionId));
        division.setLines(lineDao.findAllByDivisionId(division,false));
    	division.setZones(zoneDao.findAllByDivisionId(divisionId));
    	
    	return division;

    }

    @SuppressWarnings("unchecked")
	public Division findWithChildren(String divisionId) {

    	Parameters params = Parameters.with("1", divisionId);
        List<Object[]> divisionLstObjs = null;
        divisionLstObjs = findResultListByNativeQuery(FIND_DIVISION_CHILDREN, params);
        
        Division division = new Division();
        for(Object[] operObj : divisionLstObjs){
        	division.setDivisionId(operObj[0].toString());
        	division.setDivisionName(operObj[1]==null?"":operObj[1].toString());
        	division.setPlantName(operObj[2]==null?"":operObj[2].toString());
        	division.setTerminalCount(Integer.parseInt(operObj[3].toString()));
        	division.setDeviceCount(Integer.parseInt(operObj[4].toString()));
        }
    	division.setZones(zoneDao.findAllByDivisionId(divisionId));
    	
    	return division;

    }

    @Transactional
    public Division save(Division division) {
        return super.save(division);
    }

    @Transactional
    public void save(List<Division> divisions) {
    	for(Division division : divisions) {
    		save(division);
    	}
    }
    
    @Transactional
    public void removeByDivisionId(String divisionId) {
    	
    	remove(findByDivisionId(divisionId));
    }

    @Transactional
    public void removeById(String siteName) {
        delete(Parameters.with("siteName", siteName));
    }
    
    @Transactional
    public void removeById(String siteName, String plantName) {
        delete(Parameters.with("siteName", siteName).put("plantName", plantName));
    }

    @Transactional
    public void removeById(String siteName, String plantName, String divisionId) {
        delete(Parameters.with("siteName", siteName)
        		.put("plantName", plantName).put("divisionId",divisionId));
        
    }
    
    @Transactional
    public void updateId(String siteName, String plantName, String divisionId) {
    	executeUpdate(UPDATE_DIVISION_ID, Parameters.with("siteName", siteName).put("plantName", plantName).put("divisionId", divisionId));
    	executeUpdate(UPDATE_LINE_ID, Parameters.with("siteName", siteName).put("plantName", plantName).put("divisionId", divisionId));
    	executeUpdate(UPDATE_PROCESS_POINT_ID, Parameters.with("siteName", siteName).put("plantName", plantName).put("divisionId", divisionId));
    }
    
	public List<String> findPlantBySite(String siteName) {
		Parameters params = Parameters.with("1", siteName);
		return findAllByNativeQuery(FIND_DISTINCT_PLANT, params, String.class);
	}
	
	/** This method finds all Plant by site name for which headless error data exists
	 * @return List<String>
	*/
	public List<String> findAllPlantBySiteAndExternalSystemData(String site) {
		Parameters params = Parameters.with("1", site);
		return findAllByNativeQuery(FIND_PLANT_BY_SITE, params, String.class);
	}
	
	public List<String> findAllDivisionId() {
		return findByQuery(FIND_DISTINCT_DIVISION_ID, String.class);
	}

	@Override
	public List<Object[]> findDivisionIdAndName() {
			 @SuppressWarnings("unchecked")
			 List<Object[]> divisionLstObjs = findResultListByNativeQuery(FIND_DISTINCT_DIVISION_ID_AND_NAME,null);
			 return divisionLstObjs;
		      
    }	
	
	@Override
	public List<Object[]> findDivisionForPlant() {
		List<Object[]> divisionForPlant = findAllByNativeQuery(FIND_DIVISION_FOR_PLANT,null, Object[].class);
		return divisionForPlant;	
	}

	@Override
	public String getDivisionID(String divisionName) {
		Parameters params = Parameters.with("1", divisionName);
		List<String> divisionId = findAllByNativeQuery(FIND_DIVISION_ID,params, String.class);
		return divisionId.get(0);
	}
}


