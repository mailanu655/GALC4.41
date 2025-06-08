package com.honda.galc.client.teamleader.let;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.LetPassCriteriaMtoDao;
import com.honda.galc.dao.product.LetProgramCategoryDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.LETDataTag;
import com.honda.galc.entity.product.LetProgramCategory;
import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 25, 2013
 */

public class LetUtil
{

	public DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	public DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
	public DateFormat format3 = new SimpleDateFormat("yyyyMMddHHmmss");
	public Vector vecPgmComboDatas =null;
	public Hashtable pgmComboTypeData =null;
	public Hashtable pgmComboAttrData = null;
	public Hashtable pgmComboColorData = null;
	public Vector pgmData = null;

	public LetUtil() {
		super();
	}

	public  String getStartTime(String prodDate,String processLocation)  {
		String ansDate = "";
		try {	           
			Date date = (Date) format2.parse(prodDate);
			Timestamp minStartTimestamp = getDao(DailyDepartmentScheduleDao.class).findMinStartTimestamp(format1.format(date), processLocation);
			if (minStartTimestamp == null) {
				Object[] scheduleData=getDao(DailyDepartmentScheduleDao.class).findStartTimeNextDayInfo(processLocation);
				String lastTime = ((Time) scheduleData[0]).toString();
				ansDate = format1.format(date) + " " + lastTime + ".000000";

			} else {

				ansDate = ((Timestamp) minStartTimestamp).toString();
			}	           
		} catch (ParseException e) {
			Logger.getLogger().error(e,"An error Occurred while processing the LET Download Results screen");
			e.printStackTrace();
		} 
		return ansDate;
	}

	public  String getEndTime(String prodDate,String processLocation)  {
		String ansDate = "";
		try {	           
			Date date = (Date) format2.parse(prodDate);
			Timestamp maxEndTimestamp = getDao(DailyDepartmentScheduleDao.class).findMaxEndTimestamp(format1.format(date), processLocation);
			if (maxEndTimestamp == null) {
				List<Object[]> scheduleList=getDao(DailyDepartmentScheduleDao.class).findEndTimeNextDayInfo(processLocation);		               
				Object[] firstSchedule=scheduleList.get(0);
				String lastTime = ((Time) firstSchedule[0]).toString();
				for (Object[] schedule:scheduleList)
				{        			
					if ((Integer) schedule[1] != null) 
					{
						Integer iNextDay = (Integer) schedule[1];
						if (iNextDay.equals(Integer.valueOf("1"))) {
							Calendar cal = new GregorianCalendar();
							cal.setTime(date);
							cal.add(Calendar.DATE, +1);
							date = cal.getTime();
						}
					}	        			
				}
				ansDate = format1.format(date) + " " + lastTime + ".999999";
			} else {
				ansDate = ((Timestamp) maxEndTimestamp).toString();
			}
		} catch (ParseException e) {
			Logger.getLogger().error(e,"An error Occurred while processing the End Time");
			e.printStackTrace();			
		} 
		return ansDate;
	}

	public void getProgram(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,String effectiveDt,String effectiveTime)
	{
		pgmData = new Vector();
		Hashtable htData;
		HashMap pgmNameMap = null;
		try {
			List<LetProgramCategory> letProgCatgList= getDao(LetProgramCategoryDao.class).getColorExplanation("");
			int rowCount = letProgCatgList.size();
			if (rowCount == 0) {
				Logger.getLogger().error("Criteria program not found.");
				return ;
			}
			vecPgmComboDatas = new Vector(rowCount);
			pgmComboTypeData = new Hashtable(rowCount);
			pgmComboAttrData = new Hashtable(rowCount);
			pgmComboColorData = new Hashtable(rowCount);
			pgmNameMap = new HashMap(rowCount);
			for (LetProgramCategory letProgram:letProgCatgList) {
				String pgmName =  letProgram.getPgmCategoryName();
				vecPgmComboDatas.add(pgmName);
				pgmComboTypeData.put(pgmName, letProgram.getId().getInspectionDeviceType());
				pgmComboAttrData.put(pgmName, letProgram.getId().getCriteriaPgmAttr());
				pgmComboColorData.put(pgmName, letProgram.getBgColor());
				pgmNameMap.put(letProgram.getId().getInspectionDeviceType()+ letProgram.getId().getCriteriaPgmAttr(),pgmName);
			}		
			Vector vecParam = new Vector(5);
			List<Object[]> letPassCriteriaList=null;
			if (modelYearCode == null|| modelCode == null|| modelTypeCode == null|| modelOptionCode == null|| effectiveDt == null||effectiveTime== null) {
				letPassCriteriaList=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaForMtoEffTime("","","","", new Timestamp(format3.parse("99991231235959").getTime()));
			} else {
				letPassCriteriaList=getDao(LetPassCriteriaMtoDao.class).findLetPassCriteriaForMtoEffTime( modelYearCode, modelCode, modelTypeCode, modelOptionCode,   new Timestamp(format3.parse(effectiveDt.trim() + effectiveTime.trim()).getTime()));
			}			
			for (Object[] letPassCriteria:letPassCriteriaList) {
				htData = new Hashtable();
				htData.put(LETDataTag.CRITERIA_PGM_ID,ObjectUtils.defaultIfNull(letPassCriteria[0], ""));
				htData.put(LETDataTag.CRITERIA_PGM_NAME,ObjectUtils.defaultIfNull(letPassCriteria[1], ""));
				String deviceType =StringUtils.trimToEmpty((String)letPassCriteria[2]);
				String pgmAttr =StringUtils.trimToEmpty((String)letPassCriteria[3]);
				String pgmCate =StringUtils.trimToEmpty((String)pgmNameMap.get(deviceType + pgmAttr));
				htData.put(LETDataTag.PGM_CATEGORY, pgmCate);
				pgmData.add(htData);
			}
		}  catch (Exception e) {
			Logger.getLogger().error(e,"An error Occurred while processing the Let Shipping Judgement Setting Panel screen");
			e.printStackTrace();
		}			
	}

	public void getProgramCategoryData()
	{
		try {
			List<LetProgramCategory> letProgCatgList= getDao(LetProgramCategoryDao.class).getColorExplanation("");
			int rowCount = letProgCatgList.size();
			if (rowCount == 0) {
				Logger.getLogger().error("Criteria program not found.");
				return ;
			}
			vecPgmComboDatas = new Vector(rowCount);
			pgmComboTypeData = new Hashtable(rowCount);
			pgmComboAttrData = new Hashtable(rowCount);
			pgmComboColorData = new Hashtable(rowCount);
			for (LetProgramCategory letProgram:letProgCatgList)
			{
				String pgmName =  letProgram.getPgmCategoryName();
				vecPgmComboDatas.add(pgmName);
				pgmComboTypeData.put(pgmName, letProgram.getId().getInspectionDeviceType());
				pgmComboAttrData.put(pgmName, letProgram.getId().getCriteriaPgmAttr());
				pgmComboColorData.put(pgmName, letProgram.getBgColor());
			}				
		} catch (Exception e) {
			Logger.getLogger().error(e,"An error Occurred while processing the Let Shipping Judgement Setting Panel screen");
			e.printStackTrace();
		}
	}

	public Vector getVecPgmComboDatas() {
		return vecPgmComboDatas;
	}

	public void setVecPgmComboDatas(Vector vecPgmComboDatas) {
		this.vecPgmComboDatas = vecPgmComboDatas;
	}

	public Hashtable getPgmComboTypeData() {
		return pgmComboTypeData;
	}

	public void setPgmComboTypeData(Hashtable pgmComboTypeData) {
		this.pgmComboTypeData = pgmComboTypeData;
	}

	public Hashtable getPgmComboAttrData() {
		return pgmComboAttrData;
	}

	public void setPgmComboAttrData(Hashtable pgmComboAttrData) {
		this.pgmComboAttrData = pgmComboAttrData;
	}

	public Hashtable getPgmComboColorData() {
		return pgmComboColorData;
	}

	public void setPgmComboColorData(Hashtable pgmComboColorData) {
		this.pgmComboColorData = pgmComboColorData;
	}

	public Vector getPgmData() {
		return pgmData;
	}

	public void setPgmData(Vector pgmData) {
		this.pgmData = pgmData;
	}

	public Calendar createCalendarWithoutParsePostion(String vstrDate)
	{
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar newObj = new GregorianCalendar();
			sdf.setLenient(false);
			Date d = sdf.parse(vstrDate);
			newObj.setTime(d);
			return newObj;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Calendar createCalendar(String vstrDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		Calendar newObj = new GregorianCalendar();
		sdf.setLenient(false);
		Date d = sdf.parse(vstrDate, new ParsePosition(0));
		if (d == null) {
			return null;
		}
		newObj.setTime(d);

		return newObj;
	}

	public Timestamp createTimestamp(String yyyyMMddHHmmss, int microSec) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = sdf.parse(yyyyMMddHHmmss, new ParsePosition(0));

		Timestamp ts = new Timestamp(date.getTime());

		ts.setNanos(microSec);

		return ts;

	}

	public Timestamp createTimestamp(String dateStr) {
		return createTimestamp(dateStr, 0);
	}

}