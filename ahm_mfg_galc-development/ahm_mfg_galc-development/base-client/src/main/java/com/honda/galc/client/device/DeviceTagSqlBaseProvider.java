/**
 * 
 */
package com.honda.galc.client.device;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.plc.PlcDataCollectionBean;
import com.honda.galc.client.device.plc.omron.PlcDataField;
import com.honda.galc.device.dataformat.BitArray;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;

/**
 * @author Subu Kathiresan
 * @date Dec 14, 2012
 */
public abstract class DeviceTagSqlBaseProvider extends DeviceTagBaseProvider {

	public boolean populateFields(PlcDataCollectionBean bean, String deviceID) {
		addDefaultSubstitutions(bean);
		return true;
	}
	
	public StringBuilder convertBitArray(DeviceFormat deviceFormat, String qryResult) {
		if (deviceFormat.getDeviceDataType().equals(DeviceDataType.BIT_ARRAY)) {
			try {
				return new BitArray(qryResult).getValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new StringBuilder(StringUtils.trimToEmpty(qryResult));
	}
	
	public String replaceParams(PlcDataCollectionBean bean, String sql) {
	      Pattern p = Pattern.compile("\\{[\\w]*\\}");
	      Matcher m = p.matcher(sql);
	      while (m.find()) {
	    	 String placeHolderName = m.group().replace("{", "").replace("}", "");
	         if (bean.getSubstitutionList().containsKey(placeHolderName)) {
	        	sql = sql.replace("{" + placeHolderName + "}", bean.getSubstitutionList().get(placeHolderName)); 
	         }
	      }

		return sql;
	}
	
	public void addFieldToWrite(PlcDataCollectionBean bean, String fieldName, StringBuilder val) {
		try {
			bean.getPlcDataFields().put(fieldName, new PlcDataField(fieldName, val));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public String executeSql(String sql) {
		String qryResult = "";
		try {
			qryResult = (String) getDeviceFormatDao().executeSqlQuery(sql);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return qryResult;
	}
	
	private void addDefaultSubstitutions(PlcDataCollectionBean bean) {
		try {
			bean.getSubstitutionList().put("PRODUCT_ID", new StringBuilder(bean.getProductId()));
			bean.getSubstitutionList().put("PRODUCT_SPEC_CODE", new StringBuilder(bean.getProductSpecCode()));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
