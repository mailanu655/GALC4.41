package com.honda.galc.system.config.web.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.SiteDao;
import com.honda.galc.dao.qi.QiPlantDao;
import com.honda.galc.dto.GpcsDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.GpcsDivision;
import com.honda.galc.entity.conf.Site;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.system.config.web.forms.GpcsForm;
import com.honda.galc.system.config.web.plugin.InitializationPugin;

public class GpcsConfigurationAction extends ConfigurationAction {
	private static final String ERRORS_GROUP = "gPCSErrors";
	
	protected Logger logger= InitializationPugin.getLogger();


	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		boolean isView = false;
		boolean isAdd = false;
		boolean isUpdate = false;
		boolean isDelete = false;
		ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		GpcsForm gpcsForm = (GpcsForm) form;

		try {

			if(gpcsForm.getButtonName()!=null ) {

				String divisionData[] = gpcsForm.getSelectedDivision().split("-");

				String divisionId =divisionData[0];
			    logger.info("divisionId  is "+divisionId);
				
				GpcsDivision gpcsDivision = new GpcsDivision();
				gpcsDivision.setDivisionId(divisionId);
				gpcsDivision.setGpcsLineNo(gpcsForm.getgPCSLineNo());
				logger.info("gpcs line no "+gpcsForm.getgPCSLineNo());
				gpcsDivision.setGpcsPlantCode(gpcsForm.getgPCSPlantCode());
				logger.info("gpcs plant code "+gpcsForm.getgPCSPlantCode());
				gpcsDivision.setGpcsProcessLocation(gpcsForm.getgPCSProcessLocation());
				logger.info("gpcs process location "+gpcsForm.getgPCSProcessLocation());
				
				logger.info("gpcsDivision  is "+gpcsDivision);

				if("Add".equalsIgnoreCase(gpcsForm.getButtonName())) {
					 logger.info("Adding the gpcs");
					getDao(GpcsDivisionDao.class).saveDivisionLine(gpcsDivision);
					updateDivision(gpcsDivision,gpcsForm,divisionData[1]);
				} else if("Update".equalsIgnoreCase(gpcsForm.getButtonName()))  {
					 logger.info("updating the gpcs");
					getDao(GpcsDivisionDao.class).updateDivisionLine(gpcsDivision);
					updateDivision(gpcsDivision,gpcsForm,divisionData[1]);

				} else if("Delete".equalsIgnoreCase(gpcsForm.getButtonName())) { 
					 logger.info("Deleting the gpcs");
					 
					 logger.info("fetching division by id: "+gpcsDivision.getDivisionId());
					 gpcsDivision =  getDao(GpcsDivisionDao.class).findByKey(gpcsDivision.getDivisionId());
					 logger.info("Division data for division Id is:  "+gpcsDivision);
					 getDao(GpcsDivisionDao.class).deleteDivisionLine(gpcsDivision);
				}
			}

			List<GpcsDto> gPCSData = new ArrayList<GpcsDto>();
			GpcsDto gpcsData=new GpcsDto();

			List<Object[]> gPCSDatDept = getDao(GpcsDivisionDao.class).getGpcsDeptAndLine();
			Map<String,String> siteNameMap = new LinkedHashMap<String,String>();
			Map<String,String> plantNameMap = new LinkedHashMap<String,String>();
			Map<String,String> divisionIdMap = new LinkedHashMap<String,String>();
			Map<String,String> divisionNameMap = new LinkedHashMap<String,String>(); 
			Map<String,String> gpcsPlantCodeMap = new LinkedHashMap<String,String>();
			Map<String,String> gpcsProcessLocationMap = new LinkedHashMap<String,String>();
			Map<String,String> gpcsLineNoMap = new LinkedHashMap<String,String>();
			Map<String,String> divisionIdAndNameMap = new LinkedHashMap<String,String>();
			
			List<Division> divisionList = getDao(DivisionDao.class).findAll();
			for(Division division:divisionList) {
				divisionIdAndNameMap.put(division.getDivisionId() + "-"+division.getDivisionName(), division.getDivisionId() + "-"+division.getDivisionName());
			}
			
			List<Site> siteList = getDao(SiteDao.class).findAll();
			
			for(Site site : siteList) {
				siteNameMap.put(site.getSiteName(), site.getSiteName());
			}

			List<QiPlant> qiPlantList = getDao(QiPlantDao.class).findAll();
			
			for(QiPlant qiPlant:qiPlantList) {
				plantNameMap.put(qiPlant.getPlant(), qiPlant.getPlant());
			}

			for(Object obj[]:gPCSDatDept) {
				gpcsData=new GpcsDto();
				gpcsData.setSiteName((String) obj[0]);
				gpcsData.setPlantName((String) obj[1]);
				gpcsData.setDivisionId(String.valueOf(obj[2]));
				gpcsData.setDivisionName((String) obj[3]);
		 		divisionNameMap.put((String) obj[3], (String) obj[3]);
				gpcsData.setgPCSPlantCode(String.valueOf(obj[4]));
				gpcsPlantCodeMap.put((String) obj[4],(String) obj[4]);
				gpcsData.setgPCSProcessLocation(String.valueOf(obj[5]));
				gpcsProcessLocationMap.put((String) obj[5], (String) obj[5]);
				gpcsData.setgPCSLineNo(String.valueOf(obj[6]));
				gpcsLineNoMap.put((String) obj[6], (String) obj[6]);
				gpcsData.setPlanCode(String.valueOf(obj[7]));
	            gpcsData.setDivisionIdAndName(String.valueOf(obj[2]).trim()+"-"+String.valueOf(obj[3]).trim());
				gPCSData.add(gpcsData);
			}

			gpcsForm.setSiteNameMap(siteNameMap);
			gpcsForm.setDivisionIdAndMap(divisionIdAndNameMap);
			gpcsForm.setPlantNameMap(plantNameMap);
			
			gpcsForm.setgPCSData(gPCSData);
			
			List<Object[]> plantForSite = getDao(PlantDao.class).findPlantForSite();	
			gpcsForm.setSiteWithPlantNameList(getDataMap(plantForSite,false));
			
			List<Object[]> divisionForPlant = getDao(DivisionDao.class).findDivisionForPlant();
			gpcsForm.setPlantWithDivisionNameList(getDataMap(divisionForPlant,true));
						
			request.setAttribute("gpcsForm",gpcsForm);
			
		} 
		catch (Exception e) {

			e.printStackTrace();
			errors.add(ERRORS_GROUP, new ActionError("CFGW0005",e.toString()));
		}          
		return forward(mapping,request,errors,messages);
	}
	
	private Map<String,List<String>> getDataMap(List<Object[]> dataList,boolean isPlant) {
		Map<String,List<String>> sitePlantMapping = new LinkedHashMap<String,List<String>>();
		for(Object[] obj:dataList) {
			String key = (String) obj[0];
			
			String value= (String) obj[1];
			if(isPlant) {
				value = ((String) obj[2]).trim()+"-"+value;
			}
			
			if(sitePlantMapping.containsKey(key)) {
				List<String> list = sitePlantMapping.get(key);
				list.add(value);
			} else {
				List<String> list  = new ArrayList<String>();
				list.add(value);
				sitePlantMapping.put(key, list);
			}
			
		}
		return sitePlantMapping;
	}
	
	
	private void updateDivision(GpcsDivision gpcsDivision, GpcsForm gpcsForm, String divisionName) {
		logger.info("preparing the division object");
		Division division = new Division();
		logger.info("division id: "+gpcsDivision.getDivisionId());
		division.setDivisionId(gpcsDivision.getDivisionId());
		logger.info("division name: "+divisionName);
		division.setDivisionName(divisionName);
		logger.info("plant name: "+gpcsForm.getSelectedPlantName());
		division.setPlantName(gpcsForm.getSelectedPlantName());
		logger.info("site name: "+gpcsForm.getSelectedSiteName());
		division.setSiteName(gpcsForm.getSelectedSiteName());
		getDao(DivisionDao.class).save(division);
	}
}

