package com.honda.galc.dao.jpa.conf;



import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ApplicationByTerminalDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dto.qi.QiTerminalDetailDto;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.service.Parameters;


public class TerminalDaoImpl extends BaseDaoImpl<Terminal,String> implements TerminalDao {

	public static final String FIND_ALL_LC_TERMINALS = "select t from Terminal t where t.locatedProcessPointId in (select distinct r.id.processPointId from LotControlRule r)";
	public static final String FIND_ALL_BY_APPLICATION_ID = "select t from Terminal t, ApplicationByTerminal at where at.id.hostName = t.hostName and at.id.applicationId = :applicationId";
	public static final String FIND_ALL_TERMINAL_DETAIL_BY_APPLICATION_ID =
			"select T.HOST_NAME,T.TERMINAL_DESCRIPTION,W.COLUMN_LOCATION,W.PHONE_EXTENSION "
			+ " from GAL234TBX T left join WSCLIENTS_TBX W on T.HOST_NAME = W.HOST_NAME "
			+ " where T.LOCATED_PROCESS_POINT_ID = ?1";

	@Autowired
	private ApplicationByTerminalDao applicationByTerminalDao;
	@SuppressWarnings("unchecked")
    public List<Terminal> findAllBySiteName(String siteName) {
        Query q;
        q = entityManager.createQuery(
                "select term from Terminal as term "
                        + "where term.divisionId in (select div from Division as div where div.siteName = :siteName)");
        q.setParameter("siteName", siteName);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Terminal> findAllByPlantName(String plantName) {
    	
    	Query q;
        q = entityManager
                .createQuery(
                        "select term from Terminal as term "
                                + "where term.divisionId in (select div.id from Division as div where div.plantName = :plantName)");
        q.setParameter("plantName", plantName);
        return q.getResultList();
    }

    public List<Terminal> findAllOrderByHostName() {
    	return findAll(null, new String[] {"hostName"});
    }

    public List<Terminal> findAllByDivisionId(String divId) {
    	
    	return findAll(Parameters.with("divisionId", divId), new String[] {"hostName"});
    	
    }

    @Transactional
    public void removeById(String hostName) {
    	applicationByTerminalDao.removeById(hostName);
    	delete(Parameters.with("hostName", hostName));
    }
    
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Terminal findFirstByProcessPointId(String processPointId) {
        return findFirst(Parameters.with("locatedProcessPointId", processPointId));
    }
    
    public List<Terminal> findAllByProcessPointId(String processPointId) {
    	return findAll(Parameters.with("locatedProcessPointId", processPointId), new String[] {"hostName"});
    }
    
    public List<Terminal> findAllLotControlTerminals() {
    	return findAllByQuery(FIND_ALL_LC_TERMINALS);
    }
    
    public List<Terminal> findAllByApplicationId(String applicationId) {
    	return findAllByQuery(FIND_ALL_BY_APPLICATION_ID, Parameters.with("applicationId", applicationId));
    }
    public List<QiTerminalDetailDto> findAllTerminalDetailByApplicationId(String applicationId) {
    	Parameters params = Parameters.with("1", applicationId);
    	List<QiTerminalDetailDto> terminalDtoList = findAllByNativeQuery(FIND_ALL_TERMINAL_DETAIL_BY_APPLICATION_ID, params, QiTerminalDetailDto.class);
    	return terminalDtoList;	
    } 
    
    @Override
	@Transactional
	public void updateTerminalApplication(String hostName, String processPointId) {
		
		applicationByTerminalDao.updateTerminalApplication(hostName, processPointId);
		
		//update processpoint for terminal "hostName"
		Terminal terminal = this.findByKey(hostName);
		if(terminal != null) {
			terminal.setLocatedProcessPointId(processPointId);
		}
		save(terminal);
	} 
}
