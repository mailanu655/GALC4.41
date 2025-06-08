package com.honda.galc.dao.jpa.conf;


import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.PlantId;
import com.honda.galc.service.Parameters;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public class PlantDaoImpl extends BaseDaoImpl<Plant,PlantId> implements PlantDao {
	
	private static final String FIND_PLANT_FOR_SITE = "select distinct site_name,plant_name from GAL211TBX";
	
	public Plant findById(String siteName, String plantName) {
		PlantId id = new PlantId();
		id.setPlantName(plantName);
		id.setSiteName(siteName);
		return findByKey(id);
	}
	

	@Transactional
	public void removeById(String siteName) {

		delete(Parameters.with("siteName",siteName));

	}

	@Transactional
	public void removeById(String siteName, String plantName) {

		Parameters params = Parameters.with("siteName", siteName).put("plantName", plantName);
		delete(params);

	}


	/** This method is used to find all Plant based on site.
	 * @param siteName.
	 * @return List of Plant.
	 **/

	public List<Plant> findAllBySite(String siteName) {
		return findAll(Parameters.with("id.siteName", siteName), new String[]{"id.plantName"}, true);

	}
	
	@Override
	public List<Object[]> findPlantForSite() {
		
		List<Object[]> plantForSite = findAllByNativeQuery(FIND_PLANT_FOR_SITE,null, Object[].class);
		return plantForSite;	
		
	}


}
