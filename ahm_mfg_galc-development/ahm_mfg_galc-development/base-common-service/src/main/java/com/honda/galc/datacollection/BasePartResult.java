package com.honda.galc.datacollection;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;
/**
 * 
 * <h3>PartResult</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartResult description </p>
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
 * Aug 18, 2010
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 03, 2016
 */
public class BasePartResult implements Serializable{
	private static final long serialVersionUID = 1L;
	protected LotControlRule lotControlRule;
	protected List<LotControlRule> lotControlRules = new ArrayList<LotControlRule>();
	protected ProcessPoint processPoint;
	protected String partName;
	protected String processPointId;
	protected ProductBuildResult buildResult;
	protected boolean headLess = false;
	protected Boolean quickFix = null;
	
	public BasePartResult() {
		super();
	}

	public BasePartResult(LotControlRule lotControlRule, ProductBuildResult installedPart) {
		super();
		this.lotControlRule = lotControlRule;
		this.buildResult = installedPart;
		init();
	}

	public BasePartResult(LotControlRule lotControlRule, ProcessPoint processPoint) {
		this(lotControlRule, processPoint, false);
		init();
	}
	
	public BasePartResult(LotControlRule lotControlRule, ProcessPoint processPoint, boolean headless) {
		this.processPointId = lotControlRule.getId().getProcessPointId();
		this.partName = lotControlRule.getId().getPartName();
		this.processPoint = processPoint;
		this.lotControlRule = lotControlRule;
		lotControlRules.add(lotControlRule);
		setHeadLess(headless);
	}

	private void init() {
		setHeadLess(isHeadLessLotControlTorque());
	}

	private boolean isHeadLessLotControlTorque() {
		TerminalDao terminalDao = ServiceFactory.getService(TerminalDao.class);
		return terminalDao.findFirstByProcessPointId(lotControlRule.getId().getProcessPointId()) == null;
	}

	public InstalledPartStatus getStatus(){
		return buildResult == null ? InstalledPartStatus.NC : buildResult.getInstalledPartStatus();
	}
	
	public MeasurementStatus getStatusMeasure(){
		if(isHeadLess() && isQuickFix()) return null;
		
		int measurementCount = getEffectiveMeasurementCount();
		
		if(measurementCount == 0){
			return null;
		}else if (buildResult == null)
			return null;
		else if(buildResult.getMeasurements() != null){
			
			if(buildResult.getMeasurements().size() < measurementCount)
				return MeasurementStatus.NG;
			for(int i = 0; i < measurementCount; i++){
				MeasurementStatus mstatus = buildResult.getMeasurements().get(i).getMeasurementStatus();
				if(mstatus != MeasurementStatus.OK)
					return mstatus == MeasurementStatus.REMOVED ? MeasurementStatus.REMOVED :  MeasurementStatus.NG;
			}
			
			return MeasurementStatus.OK;
		}

		return MeasurementStatus.NG;
	}
	
	public int getEffectiveMeasurementCount() {
		int count = getMeasurementCount();
		if(isDummyRule()){
			int strValueCount = 0;
			if (getStrValueMeasurements() != null) {
				strValueCount = getStrValueMeasurements().size();
			}
			return count + strValueCount;
		} else 
			return count;
	}

	public int getMeasurementCount() {
		if(lotControlRule.getPartName() != null && 
				lotControlRule.getParts() != null &&
				lotControlRule.getParts().size() > 0)
		{
			PartSpec partSpec = lotControlRule.getParts().get(0);
			return (partSpec.getStringMeasurementSpecs().size() > 0) ? partSpec.getNumberMeasurementSpecs().size() : partSpec.getMeasurementCount();
		}
		
		return 0;
	}
	
	public List<MeasurementSpec> getStrValueMeasurements() {
		
		if(lotControlRule.getPartName() != null && 
				lotControlRule.getParts() != null &&
				lotControlRule.getParts().size() > 0)
		{
			PartSpec partSpec = lotControlRule.getParts().get(0);
			
			return partSpec.getStringMeasurementSpecs();
		}
		
		return null;
	}

	public List<MeasurementSpec> getNumberMeasurements() {

		if(lotControlRule.getPartName() != null && 
				lotControlRule.getParts() != null &&
				lotControlRule.getParts().size() > 0)
		{
			PartSpec partSpec = lotControlRule.getParts().get(0);

			return partSpec.getNumberMeasurementSpecs();
		}

		return null;
	}

	public String getMeasurementResult(){
		int measurementCount = getMeasurementCount();
		StringBuilder result = new StringBuilder();
		if(measurementCount > 0){
			if(buildResult != null && buildResult.getMeasurements() != null && 
					buildResult.getMeasurements().size() > 0){
				for( Measurement measurement: buildResult.getMeasurements()){
					if(result.length() > 0) result.append(",");
					result.append(measurement.getMeasurementValue());
				}
			} else {
				result.append("*****");
			}
		}
		return result.toString();
	}
	
	public String getPartNameWindowLabel(){
		return lotControlRule.getPartName().getWindowLabel();
	}
	
	public String getPartSerialNumber(){
		String result = "";
		if(lotControlRule.getSerialNumberScanType() != PartSerialNumberScanType.NONE)
			return buildResult == null? "*****" : buildResult.getPartSerialNumber();
		
		return result;
	}
	
	public String getAssociate(){
		return buildResult == null ? "" : buildResult.getAssociateNo();
	}
	
	public String getRepaired(){
		return buildResult == null ? "" : buildResult.getInstalledPartReason();
	}
	
	public Timestamp getTimeStamp(){
		return buildResult == null ? null : buildResult.getActualTimestamp();
	}
	
	public int getTorqueNgNo() {

		if(buildResult == null || buildResult.getMeasurements() == null) return -1;

		for(int i = 0 ; i < buildResult.getMeasurements().size(); i++){
			if(buildResult.getMeasurements().get(i).getMeasurementStatus() != MeasurementStatus.OK)
				return i;
		}
		return -1;
	}

	public LotControlRule getLotControlRule() {
		return lotControlRule;
	}
	
	public void add(LotControlRule r) {
		lotControlRules.add(r);
	}
	
	//Getters & Setters
	public InstalledPart getInstalledPart() {
		return (InstalledPart)buildResult;
	}
	
	public ProductBuildResult getBuildResult() {
		return buildResult;
	}

	public void setBuildResult(ProductBuildResult buildResult) {
		this.buildResult = buildResult;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getPartName() {
		return partName;
	}
	
	public void setPartName(String partName) {
		this.partName = partName;
		
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}
	
	public String getProcessPointName() {
		return processPoint.getProcessPointName();
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public List<LotControlRule> getLotControlRules() {
		return lotControlRules;
	}

	public void setLotControlRule(LotControlRule lotControlRule) {
		this.lotControlRule = lotControlRule;
	}
	
	public boolean isPartMark(){
		return !isHeadLess() && 
		lotControlRule.getSerialNumberScanType() == PartSerialNumberScanType.NONE && 
		lotControlRule.getParts().size() > 0 &&
		lotControlRule.getParts().get(0).getMeasurementCount() <= 0 &&
		!StringUtils.isEmpty(lotControlRule.getParts().get(0).getPartSerialNumberMask());
	}

	public boolean isHeadLess() {
		return headLess;
	}

	public void setHeadLess(boolean headLess) {
		this.headLess = headLess;
	}
	
	public boolean isDummyRule(){
		if(lotControlRule.getPartName() != null && 
				lotControlRule.getParts() != null &&
				lotControlRule.getParts().size() > 0)
		{
			PartSpec partSpec = lotControlRule.getParts().get(0);
			return (partSpec.getStringMeasurementSpecs().size() > 0);
		}
		
		return true;
	}
	
	public boolean isQuickFix() {
		return quickFix == null ? true : quickFix;
	}
	
	public String getActualDate() {
		return CommonUtil.formatDate(getTimeStamp());
	}

	@Override
	public String toString() {
		return "BasePartResult [lotControlRule=" + lotControlRule + ", lotControlRules=" + lotControlRules
				+ ", processPoint=" + processPoint + ", partName=" + partName + ", processPointId=" + processPointId
				+ ", buildResult=" + buildResult + ", headLess=" + headLess + ", quickFix=" + quickFix + "]";
	}
	
	
}
