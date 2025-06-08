package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.msip.dto.outbound.QicsDeftDetailDto;
import com.honda.galc.service.msip.dto.outbound.QicsDeftHeaderDto;
import com.honda.galc.service.msip.property.outbound.QicsDeftPropertyBean;
/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class QicsDeftHandler extends BaseMsipOutboundHandler<QicsDeftPropertyBean> {
	QicsDeftHeaderDto hDto;

	@Override
	@SuppressWarnings("unchecked")
	public List<QicsDeftDetailDto> fetchDetails(Date startTimestamp, int duration) {
		List<QicsDeftDetailDto> dtoList = new ArrayList<QicsDeftDetailDto>();
		try {
			return sendDefectOfVehicle(startTimestamp, duration);
		}
		catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			QicsDeftDetailDto dto = new QicsDeftDetailDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}

	private List<QicsDeftDetailDto> sendDefectOfVehicle(Date startTimestamp, int duration) {
		List<QicsDeftDetailDto> detailsDtoList = new ArrayList<QicsDeftDetailDto>();
		hDto = new QicsDeftHeaderDto();
		getLogger().info("Inside QicsDeft Handler");
		getLogger().info("Componenet Id :: " + getComponentId());
		String lines = getPropertyBean().getActiveLinesUrls();
		getLogger().info("lines :: " +lines);
		Calendar now = GregorianCalendar.getInstance();
		Timestamp nowTs = new Timestamp(now.getTimeInMillis());

		// check if the needed configurations(report's file name and path, the process
		// point and lines which the data is generated from) are set
		if (StringUtils.isBlank(lines)) {
			getLogger().error("Needed configuration is missing [" + "Counting  active lines: " + lines + "]");
			return null;
		}
		// retrieve and merge data
		String[] activeLines = lines.split(",");

		for (String line : activeLines) {
			DefectResultDao coreMQDao = HttpServiceProvider.getService(line + getPropertyBean().getHttpServiceUrlPart(),
					DefectResultDao.class);

			Timestamp startTs = null, endTs = null;
			// get the configured start timestamp, if not set/null then get the component
			// last run timestamp
			startTs = new Timestamp(startTimestamp.getTime());
			
			// get the configured start timestamp, if not set/null then default to now
			endTs = getTimestamp(startTs, duration);
			if (endTs == null) {
				endTs = nowTs;
			}
			// if the start > end, then reset to defaults
			if (startTs.after(endTs)) {
				startTs = getStartOfToday();
				endTs = nowTs;
			}
			List<Object[]> resultSetList = coreMQDao.getAllCoreMQ(startTs, endTs);
			updateDetails(resultSetList, detailsDtoList);
			hDto.setTotalRocordsSize(detailsDtoList.size());

		}

		updateLastProcessTimestamp(nowTs);
		getLogger().info("Finished Core MQ(Market Quality) Task");

		return detailsDtoList;
	}

	private void updateDetails(List<Object[]> resultSetList,List<QicsDeftDetailDto> detailsDtoList){
		QicsDeftDetailDto dto = null;

		for (int i = 0; resultSetList != null && i < resultSetList.size(); i++) {
			Object[] lineObj = resultSetList.get(i);

			dto = new QicsDeftDetailDto();
			dto.setVin(ProductNumberDef.justifyJapaneseVIN(lineObj[0].toString(),
					getPropertyBean().getJapanVINLeftJustified()));
			int defectResultId = (Integer) lineObj[1];
			String sVal = String.format("%015d", defectResultId);
			dto.setReportedDefectID(sVal);
			dto.setEntryTimestamp(lineObj[2].toString());
			dto.setPartName(lineObj[3].toString());
			dto.setPartLocationName(lineObj[4].toString());
			dto.setDefectTypeName(lineObj[5].toString());
			dto.setSecondaryPartName(lineObj[6].toString());
			dto.setSecondaryPartLoc(lineObj[7].toString());
			dto.setResponsiblePlant(getPropertyBean().getPlantName());
			dto.setResponsibleDepartment(lineObj[8].toString());
			dto.setFiller(" ");

			detailsDtoList.add(dto);

		}

	}

}
