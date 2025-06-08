package com.honda.galc.dao.jpa.conf;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.SortedArrayList;
/**
 * 
 * <h3>OpcConfigEntryDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OpcConfigEntryDaoImpl description </p>
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
 * @author Jeffray Huang
 * Nov 26, 2009
 *
 */

public class OpcConfigEntryDaoImpl extends BaseDaoImpl<OpcConfigEntry,Long> implements OpcConfigEntryDao {

    private static final long serialVersionUID = 1L;
	private final String FIND_BY_PROCESS_POINT_ID = "select o from OpcConfigEntry o, Device d where d.ioProcessPointId = :processPointId and o.deviceId = d.clientId";

    public List<OpcConfigEntry> findAllByOpcInstance(String opcInsName) {
        return findAll(Parameters.with("opcInstanceName", opcInsName));
    }

    public List<String> findAllOpcInstanceNames() {
        Set<String> names = new HashSet<String>();
        for(OpcConfigEntry item: findAll()) {
           names.add(item.getOpcInstanceName().trim());
        }
        return new SortedArrayList<String>(names);
        
    }

    public OpcConfigEntry findByDeviceId(String opcInsName, String deviceId) {
        return findFirst(Parameters.with("opcInstanceName", opcInsName).put("deviceId", deviceId));
    }

	public List<OpcConfigEntry> findAllByDeviceId(String deviceId) {
		return findAll(Parameters.with("deviceId", deviceId));
	}

	public List<OpcConfigEntry> findAllByProcessPointId(String processPointId) {
		return this.findAllByQuery(FIND_BY_PROCESS_POINT_ID, Parameters.with("processPointId", processPointId));
	}
	 
	/**
	 * Overrides the default insert to set default ids to null for compatibility with the @GeneratedValue tag.
	 * Also retrieves the generated value after persisting.
  	 */
	@Transactional
	public OpcConfigEntry insert(OpcConfigEntry config) {
		if (config.getId()<=0) config.setId(null);
		// The persist, flush, refresh combination is required becuase the generated value type is identity
		entityManager.persist(config);
		entityManager.flush();
		entityManager.refresh(config);
		Logger.getLogger().check("Inserted entity : " + config.toString());
		return config;
	}

 

}
