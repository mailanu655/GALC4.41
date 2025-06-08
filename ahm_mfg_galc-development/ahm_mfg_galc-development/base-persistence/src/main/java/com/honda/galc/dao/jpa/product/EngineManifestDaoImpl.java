package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.constant.EngineManifestPlant;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.EngineManifestDao;
import com.honda.galc.dao.product.EngineManifestHisDao;
import com.honda.galc.entity.product.EngineManifest;
import com.honda.galc.entity.product.EngineManifestHis;
import com.honda.galc.entity.product.EngineManifestHisId;
import com.honda.galc.entity.product.EngineManifestId;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>EngineManifestDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineManifestDaoImpl description </p>
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
 * <TD>Mar 17, 2017</TD>
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
 * @since Mar 17, 2017
 */
public class EngineManifestDaoImpl extends BaseDaoImpl<EngineManifest,EngineManifestId> implements EngineManifestDao {
	
	private static final String FIND_ALL_ENGINE_MANIFEST = "SELECT p FROM EngineManifest p" +
    		" WHERE p.id.plant = :plant AND p.id.engineNo in (:engineNos)";

	@Autowired
	private EngineManifestHisDao histDao;
	 

	private EngineManifestHis createEngineManifestHistory(EngineManifest em) {
		EngineManifestHis emh = new EngineManifestHis();
		 EngineManifestHisId id = new EngineManifestHisId();
		 id.setEngineNo(em.getId().getEngineNo());
		 id.setCompany(em.getCompany());
		 id.setPlant(em.getId().getPlant());
		 id.setActTimestamp(new Date());
		 EngineManifestPlant emp = EngineManifestPlant.getByPlant(em.getId().getPlant());
		 id.setProcess(emp.getProcess());
		 id.setDepartment(emp.getDepartment());
		 id.setLine(emp.getLine());
		 
		 emh.setId(id);
		 emh.setVin(em.getVin());
		 emh.setEngineSource(em.getEngineSource());
		 emh.setEngineFiredInd(em.getEngineFiredInd());
		 emh.setProductionDate(new Date());
		 emh.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		 
		return emh;
	}

	@Transactional
	public EngineManifest save(EngineManifest entity) {
		super.save(entity);
		getEngineManifestHisDao().save(createEngineManifestHistory(entity));
		
		return entity;
	}

	public List<EngineManifest> findAllByEngineList(String plant, List<String> engines) {
		return findAllByQuery(FIND_ALL_ENGINE_MANIFEST, Parameters.with("plant", plant).put("engineNos", engines));
		
	}
	
	
	public EngineManifestHisDao getEngineManifestHisDao() {
		if(histDao == null)
			histDao = ServiceFactory.getService(EngineManifestHisDao.class);
		
		return histDao;
	}
}

