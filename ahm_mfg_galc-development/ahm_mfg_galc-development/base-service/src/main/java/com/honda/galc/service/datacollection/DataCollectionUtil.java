package com.honda.galc.service.datacollection;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.ProductResultUtil;

/**
 * 
 * <h3>DataCollectionUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCollectionUtil description </p>
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
 * <TD>Dec 11, 2014</TD>
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
 * @since Dec 11, 2014
 */
public class DataCollectionUtil {
	private HeadlessDataCollectionContext context;
	private ProductTypeUtil productTypeUtil;
	public static final String TimeStampDelimiter  = "-|:|\\.|\\s";

	public DataCollectionUtil(HeadlessDataCollectionContext context) {
		super();
		this.context = context;
	}
	
	public ProductBuildResult createBuildResult(String partName, String value, int status){
		 ProductBuildResult buildResult = getProductTypeUtil().createBuildResult(context.getCurrentProductId(), partName);
		 buildResult.setResultValue(value);
		 buildResult.setInstalledPartStatusId(status);
		 
		 return buildResult;
	}
	
	public ProductBuildResult addBuildResult(String partName){
		return addBuildResult(partName, null, 1);
	}
	
	public ProductBuildResult addBuildResult(String partName, String value, int status){
		ProductBuildResult buildResult = createBuildResult(partName, value, status);
		context.getBuildResults().add(buildResult);
		
		return buildResult;
	}
	
	public ProductBuildResult addDefectBuildResult(String partName,String defectName){
		return addDefectBuildResult(partName, null, 0, defectName);
	}
	
	public ProductBuildResult addDefectBuildResult(String partName, String value, int status, String defectName){
		ProductBuildResult buildResult = createBuildResult(partName, value, status);
		buildResult.setDefectName(defectName);
		context.getBuildResults().add(buildResult);
		
		return buildResult;
	}
	
	public ProductBuildResult getBuildResultDb(String partName){
		return getProductTypeUtil().getProductBuildResultDao().findById(context.getCurrentProductId(), partName);
	}
	
	public int incrResultIntValueDb(String partName){
		ProductBuildResult buildResultDb = getBuildResultDb(partName);
		if(buildResultDb == null) 
			buildResultDb = createBuildResult(partName, "0", null);
		
		ProductBuildResult increaseValue = incrResultIntValue(buildResultDb);
		ProductResultUtil.saveBuildResult(context.getProcessPointId(), increaseValue);
		return Integer.valueOf(increaseValue.getResultValue());
	}

	public ProductBuildResult createBuildResult(String partName, String value, Integer status) {
		ProductBuildResult buildResult = getProductTypeUtil().createBuildResult(context.getCurrentProductId(), partName);
		buildResult.setResultValue(value);
		buildResult.setInstalledPartStatusId(status);
		
		if(!StringUtils.isEmpty(context.getAssociateNoValue()))
			buildResult.setAssociateNo(context.getAssociateNoValue());
		
		return buildResult;
	}
	
	public ProductBuildResult incrResultIntValue(ProductBuildResult result){
		int intValue = getIntValue(result.getPartName(), result.getResultValue());
		result.setResultValue("" + (intValue +1));
		return result;
	}
	
	public ProductBuildResult saveResultValue(String partName, String value, int status){
		ProductBuildResult buildResult = createBuildResult(partName, value, status);
		return ProductResultUtil.saveBuildResult(context.getProcessPointId(), buildResult);
	}
	
	public String getResultValueDb(String partName){
		ProductBuildResult buildResultDb = getBuildResultDb(partName);
		return buildResultDb != null ? buildResultDb.getResultValue() : null;
	}
	
	public int getResultIntValueDb(String partName){
		ProductBuildResult buildResultDb = getBuildResultDb(partName);
		String value = buildResultDb == null ? "0" : buildResultDb.getResultValue();
		return getIntValue(partName, value);
	}

	public int getIntValue(String partName, String value) {
		if(StringUtils.isEmpty(value))
			return 0;
		else {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				context.getLogger().error(e, "Exception to get integer value for:", partName, context.getCurrentProductId());
				return 0;
			}
		}
	}

	public String getCurrentTimeStamp(){
		return CommonUtil.format(CommonUtil.getTimestampNow());

	}
	
	public String getCurrentTimeStamp(String dateFormat){
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(CommonUtil.getTimestampNow());
	}
	
	public String getCurrentTimeStamp(String dateFormat, Timestamp timeStamp){
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(timeStamp);
	}
	
	public void getCurrentTimeStampBcd(){
		DeviceFormat timeStampFormat = context.getDevice().getDeviceFormat(TagNames.CURRENT_TIMESTAMP_FORMAT.name());
		if(timeStampFormat != null && !StringUtils.isEmpty(timeStampFormat.getTagValue())){
			String timeStamp = getCurrentTimeStamp(timeStampFormat.getTagValue());
			getCurrentTimeStampBcd(timeStampFormat.getTagValue(), timeStamp);
		}
	}
	
	public void getCurrentTimeStampBcd(String timeStampFormat, String timeStamp){
		if(!StringUtils.isEmpty(timeStampFormat) && !StringUtils.isEmpty(timeStamp)){
			String[] tokens = timeStampFormat.split(TimeStampDelimiter);
			String[] elements = timeStamp.split(TimeStampDelimiter);
			boolean isBigEndian = isBigEndian();
						
			for(int i = 0; i < tokens.length; i++){
				String number = elements[i];
				Integer bcd = 0;
				
				//BCD always big endian, so reverse if it's small endian 
				if(!isBigEndian)
					number = reverseBcd(number);
				
				for(int j = 0; j < number.length(); j++){
					if(bcd > 0) bcd = bcd<<4;
					bcd += Integer.valueOf("" + number.charAt(j));
				}
				context.getLogger().debug(tokens[i], " : ", "" + bcd);
				context.put(tokens[i], bcd);
			}
			
			context.getLogger().debug("Time Stamp Format:", timeStampFormat, " Time Stamp:", timeStamp, " Big Endian:" + isBigEndian);
		}
	}

	private String reverseBcd(String number) {
		//if(number.length() == 2) return number;
		if(number != null && number.length() >= 2){
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< number.length(); i +=2)
				sb.insert(0, number.substring(i, i+2));
			return sb.toString();
		} else {
			context.getLogger().error("invalide number:", number);
			return number;
		}
		
	}

	public boolean isBigEndian() {
		boolean bigEndian = false;
		DeviceFormat isBigEndian = context.getDevice().getDeviceFormat(TagNames.BIG_ENDIAN.name());
		if(isBigEndian != null)	bigEndian = (Boolean) isBigEndian.getValue();
		
		return bigEndian;
	}

	//----------getters & setters ----------------
	public ProductTypeUtil getProductTypeUtil() {
		if(productTypeUtil == null)
			productTypeUtil = context.getProductTypeUtil();
		
		return productTypeUtil;
	}

	
}
