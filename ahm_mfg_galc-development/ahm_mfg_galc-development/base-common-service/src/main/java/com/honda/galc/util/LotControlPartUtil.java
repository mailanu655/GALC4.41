package com.honda.galc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>LotControlPartUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlPartUtil description </p>
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
 * @author Paul Chou
 * Mar 26, 2010
 *
 */
public class LotControlPartUtil {

	/**
	 * Select Lot Control Rule for Frame Product
	 * @param spec
	 * @param allRules
	 * @return
	 */
	public static List<LotControlRule> getLotControlRuleByProductSpec(BaseProductSpec spec, List<LotControlRule> allRules) {
		return getTheMostMatchedRulesByIndex(spec, allRules);
	}
	
	private static List<LotControlRule> getTheMostMatchedRulesByIndex(BaseProductSpec spec, List<LotControlRule> rulesOfSameYearModelOrMainNoClassNo) {
		//Same part name may cross multi-index - get most suitable rule out each indexed rule
		int maxRuleIndex = getMaxRuleIndex(rulesOfSameYearModelOrMainNoClassNo);
		List<LotControlRule> resultRules = new ArrayList<LotControlRule>();
		
		for(int i = 1; i < maxRuleIndex +1; i++){
				LotControlRule bestMatchRule =findBestMatchedRulesOfSameRuleSequenceByPartNameAndSpec(spec, 
					getRulesOfSameRuleSequence(rulesOfSameYearModelOrMainNoClassNo, i));
				if(bestMatchRule != null) resultRules.add(bestMatchRule);
		
		}
		
		return resultRules;
	}

	private static List<LotControlRule> getRulesOfSameRuleSequence(List<LotControlRule> rulesOfSameYearModelOrMainNoClassNo, int seq) {
		List<LotControlRule> list = new ArrayList<LotControlRule>();
		for(LotControlRule rule : rulesOfSameYearModelOrMainNoClassNo)
			if(rule.getSequenceNumber() == seq)
				list.add(rule);
		
		return list;
	}


	private static int getMaxRuleIndex(List<LotControlRule> rulesOfSameYearModel) {
		int index = 0;
		for(LotControlRule rule : rulesOfSameYearModel){
			if(rule.getSequenceNumber() > index)
				index = rule.getSequenceNumber();
		}
		return index;
	}

	public static List<LotControlRule> getTheMostMatchedRules(BaseProductSpec spec, List<LotControlRule> rulesOfSameYearModel) {
		//Should do the same Lot Control Rule selection as PartUtility
		List<String> partNames = getPartNames(rulesOfSameYearModel);
		List<LotControlRule> resultRules = new ArrayList<LotControlRule>();
		
		for(String partName: partNames){
			resultRules.addAll(findBestMatchedRulesByPartNameAndSpec(spec, 
					getRulesOfSamePartName(rulesOfSameYearModel, partName)));
		}
		
		return resultRules;
	}

	private static List<LotControlRule> getRulesOfSamePartName(List<LotControlRule> rules, String partName) {
		List<LotControlRule> list = new ArrayList<LotControlRule>();
		
		for(LotControlRule rule: rules)
			if(partName.equals(rule.getId().getPartName())) list.add(rule);
		
		return list;
	}

	
	
	public static List<LotControlRule> findBestMatchedRulesByPartNameAndSpec(BaseProductSpec spec, 
			List<LotControlRule> rulesOfSamePart) {
		return  ProductSpecUtil.getMatchedList(spec.getProductSpecCode(), rulesOfSamePart, LotControlRule.class);
	}
	
	public static LotControlRule findBestMatchedRulesOfSameRuleSequenceByPartNameAndSpec(BaseProductSpec spec, 
			List<LotControlRule> rulesOfSamePart) {
		if(spec instanceof Mbpn){
			return ProductSpecUtil.getMatchedItem(spec.getProductSpecCode(), rulesOfSamePart, LotControlRule.class, true);
		}else{
			return ProductSpecUtil.getMatchedItem(spec.getProductSpecCode(), rulesOfSamePart, LotControlRule.class, false);
		}
	}
	
	private static List<String> getPartNames(List<LotControlRule> rules) {
		List<String> list = new ArrayList<String>();
		for(LotControlRule rule: rules){
			if(!list.contains(rule.getId().getPartName().trim()))
				list.add(rule.getId().getPartName().trim());
		}
		return list;
	}
	
	public static String toString(Measurement measurement){
		StringBuilder sb = new StringBuilder();
		if (measurement != null) {
			sb.append(" sequencenumber=").append(measurement.getId().getMeasurementSequenceNumber()).append(",");
			sb.append(" measurement=").append(measurement.getMeasurementValue()).append(",");
			sb.append(" measurementstatus=").append(measurement.getMeasurementValueStatus()).append(",");
			sb.append(" angle=").append(measurement.getMeasurementAngle()).append(",");
			sb.append(" angleStatus=").append(measurement.getMeasurementAngleStatus());
		}
		return sb.toString();
	}

	public static void loadMeasurementsForPart(InstalledPart part) {
		if (part == null) {
			return;
		}
		List<Measurement> measurements = ServiceFactory.getDao(MeasurementDao.class).findAll(part.getProductId(), part.getPartName());
		if (measurements != null && !measurements.isEmpty()) {
			for(Measurement measurement:measurements) {
				if(measurement.getMeasurementStatus().getId() != MeasurementStatus.REMOVED.getId()) {
					part.getMeasurements().add(measurement);
				}
			}
		}
	}

	public static void filterOutExcludePart(List<InstalledPart> partList, String excludes) {
		
		List<InstalledPart> excludeParts = new ArrayList<InstalledPart>();
		for(InstalledPart part : partList){
			if(isExcludedToSave(part.getId().getPartName(), excludes))
				excludeParts.add(part);
		}
		
		if(excludeParts.size() > 0)
			partList.removeAll(excludeParts);
		
	}

	public static boolean isExcludedToSave(String partName, String excludes) {
		if(StringUtils.isEmpty(excludes)) return false;
		
		String[] split = excludes.split(",");
		for(int i = 0; i < split.length; i++){
			if(split[i].trim().equals(partName))
				return true;
		}
		
		return false;
	}
	
	public static void sortRulesBySequence(List<LotControlRule> resultRules) {
		Collections.sort(resultRules, new RuleComparator());
	}
	
	public static List<String> getPartNamesOfSamePartNumber(PartSpec spec) {
		String partNumber = spec.getPartNumber();
		List<String> partList = new ArrayList<String>();
		if(!StringUtils.isEmpty(partNumber) && partNumber.length() >= 5) {
			//The Part Spec Team Leader make sure the part number at least includes base5
			partList = ServiceFactory.getDao(PartSpecDao.class).findAllPartNamesByPartNumberBase5(partNumber.substring(0, 5));
		} else {
			partList.add(spec.getId().getPartName());
		}
		
		List<String> trimmedList = new ArrayList<String>();
		for(String s : partList)
			trimmedList.add(StringUtils.trimToEmpty(s));
		
		return trimmedList;
	}

}

class RuleComparator implements Comparator<LotControlRule>{

	public int compare(LotControlRule o1, LotControlRule o2) {
		if(o1.getSequenceNumber() == o2.getSequenceNumber()) return 0;
		return o1.getSequenceNumber() < o2.getSequenceNumber() ? -1 : 1;
	}
	
}
