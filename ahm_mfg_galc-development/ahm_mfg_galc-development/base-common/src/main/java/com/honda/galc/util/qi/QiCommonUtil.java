package com.honda.galc.util.qi;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.honda.galc.qi.constant.QiConstant;
/**
 * 
 * <h3>QiCommonUtil</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QiCommonUtil description </p>
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
 * @author LnT Infotech
 * May 4, 2016
 *
 */
public class QiCommonUtil {
	public static String format(Date date) {
		if(date == null) return "";
		SimpleDateFormat df = new SimpleDateFormat(QiConstant.TIME_STAMP_FORMAT);
		return df.format(date);
	}
}
