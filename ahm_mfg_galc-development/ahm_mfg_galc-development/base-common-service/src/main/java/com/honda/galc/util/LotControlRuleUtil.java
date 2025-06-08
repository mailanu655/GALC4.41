package com.honda.galc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;

/**
 * 
 * <h3>LotControlRuleUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlRuleUtil description </p>
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
 * <TD>Mar 18, 2015</TD>
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
 * @since Mar 18, 2015
 */
public class LotControlRuleUtil {
	private static final String DATA_MAPPING_PATTERN = "([a-zA-Z0-9_\\-\\s]*)(\\.)([a-zA-Z0-9_\\-\\s]*)";
	private static final String MULTI_DATA_MAPPING_PATTERN = "([a-zA-Z0-9_\\-\\s]*)(\\.)([a-zA-Z0-9_\\-\\s]*)(\\.)([a-zA-Z0-9_\\-\\s]*)";
	private static final String MEASUREMENT_PATTERN = "([a-zA-Z_\\-]+)([0-9]+)";
	public enum SnTag{SN,PART_SERIAL_NUMBER};
	public enum StrvalueTag{MEASUREMENT_STRING_VALUE,STRING_VALUE};
	public enum MnameTag{MEASUREMENT_NAME,MNAME};

	
	public static List<LotControlRule> deduceLotControlRules(Device device, String productType, String productName, Logger logger) {
		Map<String, LotControlRule> ruleMap = new LinkedHashMap<String, LotControlRule>();
		Map<String, Integer> countMap = new HashMap<String, Integer>();

		Pattern patern = Pattern.compile(!StringUtils.isEmpty(productName) ? MULTI_DATA_MAPPING_PATTERN : DATA_MAPPING_PATTERN );
		Pattern mpatern = Pattern.compile(MEASUREMENT_PATTERN);
		for(DeviceFormat df : device.getDeviceDataFormats()){
			Matcher matcher = patern.matcher(df.getTag());
			if(matcher.matches()) {
				String partName = matcher.group(matcher.groupCount() -2);
				if(!ruleMap.keySet().contains(partName)){
					LotControlRule rule = new LotControlRule();
					rule.setId(new LotControlRuleId(partName, device.getIoProcessPointId()));
					rule.setPartName(new PartName(partName, productType));
					rule.getPartName().setWindowLabel(partName);
					
					ruleMap.put(partName, rule);
					
				}
				Integer mcount = deduceMeasurement(ruleMap.get(partName), mpatern, matcher.group(matcher.groupCount()), df, logger);
				
				if(mcount != null){
				   if(countMap.get(partName) == null) countMap.put(partName, mcount);
				   else countMap.put(partName, Math.max(countMap.get(partName), mcount));
				}  
			}
			
		}
		

		return new ArrayList<LotControlRule>(ruleMap.values());
	}
	
	private static Integer deduceMeasurement(LotControlRule rule, Pattern mpatern, String mName, DeviceFormat df, Logger logger) {
		Matcher matcher;
		try {
			matcher = mpatern.matcher(mName);
			if(matcher.matches()){
				
				String token = matcher.group(1);
				int count=Integer.parseInt(matcher.group(2));
				
				if(rule.getParts() == null || rule.getParts().size() == 0){
					PartSpec pspec = new PartSpec();
					PartSpecId partSpecId = new PartSpecId();
					partSpecId.setPartName(rule.getPartNameString());
					partSpecId.setPartId("A0000");
					pspec.setId(partSpecId);
					pspec.setMeasurementCount(count);
					List<PartSpec> specs = new ArrayList<PartSpec>();
					specs.add(pspec);
					rule.setParts(specs);
				} else {
					if(rule.getParts().get(0).getMeasurementCount() < count)
						rule.getParts().get(0).setMeasurementCount(count);
				}
				
				MeasurementSpec mspec = getMeasurementSpec(rule, count); 
				if(mspec == null) addMeasurementSpec(rule, count);
				
				if(MnameTag.MEASUREMENT_NAME.name().equals(token) || MnameTag.MNAME.name().equals(token))
					getMeasurementSpec(rule, count).setMeasurementName(df.getTagValue()); //must be a static field
				
				if(StrvalueTag.MEASUREMENT_STRING_VALUE.name().equals(token) ||StrvalueTag.STRING_VALUE.name().equals(token))
					if(getMeasurementSpec(rule, count).getMeasurementName() == null) getMeasurementSpec(rule, count).setMeasurementName("");
				
				
			} else {
				if(SnTag.SN.name().equals(mName) || SnTag.PART_SERIAL_NUMBER.name().equals(mName)){
					rule.setScan(true);
				}
				
				return null;
			}
		} catch (Exception e) {
			logger.warn(e, " Invalid measurement sequence number.");

		}
		return null;

	}

	private static void addMeasurementSpec(LotControlRule rule, int count) {
		MeasurementSpec ms = new MeasurementSpec();
		MeasurementSpecId msId = new MeasurementSpecId(rule.getId().getPartName(), null, count);//PartID????
		ms.setId(msId);
		
		rule.getParts().get(0).getMeasurementSpecs().add(ms);
		
	}

	private static MeasurementSpec getMeasurementSpec(LotControlRule rule, int count) {
		PartSpec partSpec = rule.getParts().get(0);
		List<MeasurementSpec> measurementSpecs = partSpec.getMeasurementSpecs();
		if(measurementSpecs == null) return null;
		
		for(MeasurementSpec ms : measurementSpecs)
			if(ms.getId().getMeasurementSeqNum() == count) return ms;
		
		return null;
	}
}
