package com.honda.galc.oif.web.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.oif.values.OifServiceConfigValue;
import com.honda.galc.oif.values.OifStartMenuItem;
import com.honda.galc.oif.values.OifStartMenuPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.OifServiceConstants;
import com.honda.galc.system.oif.svc.common.OifServiceFactory;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class OifWebUtility {

	private static final String EXCLUDE_DEF_PACKAGE = "DEF_PACKAGE";
	private static final String OIF_START_MENU_COMP_ID = "OIF_START_MENU";
	private static final String OIF_START_DISTRIBUTION_MENU_COMP_ID = "OIF_START_DISTRIBUTION_MENU";

	/**
	 * 
	 */
	private OifWebUtility() {
		// Make a utility class - no instance is allowed
	}

	/**
	 * @return
	 */
	public static List<OifStartMenuItem> listOifStartMenuOptions()  {
		List<OifStartMenuItem> options = new ArrayList<OifStartMenuItem>();
		OifStartMenuItem lv0 = new OifStartMenuItem("", "");
		options.add(lv0);
		
		List<ComponentProperty> props= PropertyService.getComponentProperty(OIF_START_MENU_COMP_ID);
		
		OifStartMenuPropertyBean propBean = PropertyService.getPropertyBean(OifStartMenuPropertyBean.class);
		
		Map<String, Boolean> tsReqFlags = propBean.tsReqFlags(Boolean.class);
		Map<String, String> warnings = propBean.warnings();
		
		List<ComponentProperty> tasks = new SortedArrayList<ComponentProperty>("getPropertyKey");
		
		// filter out commented out elements		
		for(ComponentProperty item : props) {

			String key = item.getPropertyKey();
			if(!key.startsWith("#") && StringUtils.isNumeric(key)) {
				tasks.add(item);
			}
			
		}
		
		for (ComponentProperty item : tasks) {
			String propValue = item.getPropertyValue();
			if(propValue != null && propValue.length() > 0) {
				String[] elems = propValue.split("\\s*:\\s*");
				final String name = elems[0];
				OifStartMenuItem menuOption = new OifStartMenuItem(elems[1], name);
				
				if (tsReqFlags != null) {
					// Get additional properties
					Boolean tsReqFlag = tsReqFlags.get(item.getPropertyKey());
					if (tsReqFlag != null) {
						menuOption.setStartTsRequired(tsReqFlag);
					}
				}		
				
				if (warnings != null) {
					// 
					String warning = warnings.get(item.getPropertyKey());
					if (warning != null) {
						menuOption.setWarning(warning);
					}
				}	
				
				options.add(menuOption);
			}
		}
		return options;
	}
	
	/**
	 * @return
	 */
	public static List<OifServiceConfigValue> listOifServices() {
		
		List<OifServiceConfigValue> list = new ArrayList<OifServiceConfigValue>();
		PropertyService.refreshComponentProperties(OifServiceFactory.OIF_SERVICES_COMPONENT_ID);
		List<ComponentProperty> props = PropertyService.getComponentProperty(OifServiceFactory.OIF_SERVICES_COMPONENT_ID);
		
		List<ComponentProperty> sortedServices = new SortedArrayList<ComponentProperty>(props,"getPropertyKey");
		
		for (ComponentProperty item: sortedServices) {
			String name = item.getPropertyKey();
			if(!name.equals(EXCLUDE_DEF_PACKAGE)) {
				OifServiceConfigValue e = new OifServiceConfigValue(name);
				list.add(e);
				populateRecordDefinitions(e);
			}
		}
		
		return list;
	}

	/**
	 * @param e
	 */
	private static void populateRecordDefinitions(OifServiceConfigValue e) {
		String strParseLineDefs = PropertyService.getProperty(e.getInterfaceName(), OifServiceConstants.PARSE_LINE_DEFS);
		e.setMessageDefinitionsKey(strParseLineDefs);
		if(strParseLineDefs == null) {
			String strOutputDefs = PropertyService.getProperty(e.getInterfaceName(), OifServiceConstants.OUTPUT_FORMAT_DEFS);
			e.setMessageDefinitionsKey(strOutputDefs);
		}
		String strDistribution = PropertyService.getProperty(e.getInterfaceName(), OifServiceConstants.DISTRIBUTION_PARAM);
		e.setDistributionName(strDistribution);
		
		String recDefsString = PropertyService.getProperty(e.getInterfaceName(), OifServiceConstants.LOAD_RECORD_DEFS);
		populateRecordDefinitions(e, recDefsString);
		recDefsString = PropertyService.getProperty(e.getInterfaceName(), OifServiceConstants.FILTER_LOAD_RECORD_DEFS);
		populateRecordDefinitions(e, recDefsString);
	}

	/**
	 * @param e
	 * @param recDefsString
	 */
	private static void populateRecordDefinitions(OifServiceConfigValue e, String recDefsString) {
		if(recDefsString != null) {
			String[] recDefs = recDefsString.split("\\s*,\\s*");
			for (String recDef : recDefs) {
				String[] recDefStrings = recDef.split("\\s*:\\s*");
				
				if (recDefStrings.length > 1) {
					e.addRecordDef(recDefStrings[0], recDefStrings[1]);
				}				
			}
		}
	}

	/**
	 * @return
	 */
	public static List<OifStartMenuItem> listDistributionMenuOptions()  {
		List<OifStartMenuItem> options = new ArrayList<OifStartMenuItem>();
		options.add(new OifStartMenuItem("", ""));
		List<ComponentProperty> props= PropertyService.getComponentProperty(OIF_START_DISTRIBUTION_MENU_COMP_ID);
		List<ComponentProperty> tasks = new SortedArrayList<ComponentProperty>("getPropertyKey");
		
		// filter out commented out elements		
		for(ComponentProperty item : props) {
			String key = item.getPropertyKey();
			if(!key.startsWith("#") && StringUtils.isNumeric(key)) {
				tasks.add(item);
			}
		}
		
		for (ComponentProperty item : tasks) {
			String propValue = item.getPropertyValue();
			if(propValue != null && propValue.length() > 0) {
				String[] elems = propValue.split("\\s*:\\s*");
				final String name = elems[0];
				OifStartMenuItem menuOption = new OifStartMenuItem(elems[1], name);
				options.add(menuOption);
			}
		}
		return options;
	}
	
	public static boolean isComponentDefined(String componentId) {
		return PropertyService.isComponentDefined(componentId);
	}
}
