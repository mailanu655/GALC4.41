package com.honda.galc.dao.conf;

import com.honda.galc.dto.qi.QiTerminalDetailDto;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.service.IDaoService;

import java.util.List;

public interface TerminalDao extends IDaoService<Terminal, String> {
    public List<Terminal> findAllBySiteName(String siteName);

    public List<Terminal> findAllByPlantName(String plantName);

    /**
     * Finds all terminals ordered by HOST_NAME.
     */
    public List<Terminal> findAllOrderByHostName();

    public List<Terminal> findAllByDivisionId(String divId);
    
    public List<Terminal> findAllByProcessPointId(String processPointId);
    
    public Terminal findFirstByProcessPointId(String processPointId);
    
    public List<Terminal> findAllLotControlTerminals();
    
    public List<Terminal> findAllByApplicationId(String applicationId);

	List<QiTerminalDetailDto> findAllTerminalDetailByApplicationId(String applicationId);
	
	public void updateTerminalApplication(String hostName, String processPointId);

}
