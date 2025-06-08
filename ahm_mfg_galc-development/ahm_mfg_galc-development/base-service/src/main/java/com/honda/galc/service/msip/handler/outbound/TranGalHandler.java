package com.honda.galc.service.msip.handler.outbound;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.TranGalDto;
import com.honda.galc.service.msip.property.outbound.TranGalPropertyBean;
import com.honda.galc.service.property.PropertyService;
/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class TranGalHandler extends BaseMsipOutboundHandler<TranGalPropertyBean> { 
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TranGalDto> fetchDetails()  {
		int totalRecord = 0;
		ArrayList<TranGalDto> listOfTranGal = new ArrayList<TranGalDto>();
		try {
			getLogger().info("Inside TranGal Handler");
			
			
			List<Object[]> warrantyData  = getListOfTransmissionWarrantyDetails();
			
			getLogger().info( "Initialize OutputFormaterHelper for Transmission Warranty Task" );
			if ( warrantyData != null && !warrantyData.isEmpty( ) )
			{
				totalRecord = warrantyData.size();
			}
			
			final String warrantyHeaerRecord	= new StringBuilder( PropertyService.getSiteName() + " PRODATA ")
												.append( String.format( "%09d", totalRecord ) )
												.append( String.format( "%179s", " ") )
												.toString();
			
			getLogger().info("Inside TranGal Handler warrantyHeaerRecord :: " + warrantyHeaerRecord);
			
			exportData(warrantyData, listOfTranGal, warrantyHeaerRecord);
			return listOfTranGal;
		} catch (Exception e) {
			getLogger().error(e);
			listOfTranGal.clear();
			TranGalDto dto = new TranGalDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			listOfTranGal.add(dto);
			return listOfTranGal;
		}
	}
	private List<Object[]> getListOfTransmissionWarrantyDetails() {
			final String lastDateFilter = getPropertyBean().getTmWarLastDateFilter();
			getLogger().info("lastDateFilter :: " + lastDateFilter);
			//Transmission warranty properties
			final String tmOff = getPropertyBean().getTmWarTmppOff();
			final String trackingStatus = getPropertyBean().getTmScrapExceptional();
			final String torquePartName = getPropertyBean().getTmWarTorquePartName();
			final String casePartName = getPropertyBean().getTmWarCasePartName();
			
			MissionDao warrantyDao = ServiceFactory.getDao(MissionDao.class);
			List<Object[]> warrantyData = warrantyDao.queryTransmissionWarranty(tmOff, trackingStatus, torquePartName,
					casePartName, lastDateFilter);
			return warrantyData;
	}
	
	private void exportData(List<Object[]> warrantyData, List<TranGalDto> listOfTranGal, String warrantyHeaerRecord){
		for (Object[] data : warrantyData)
		{
			TranGalDto tranGalDto = new TranGalDto();
			tranGalDto.setWarrantyHeaderRecord(warrantyHeaerRecord);
			tranGalDto.setProductId			( data[0] == null ? "" : data[0].toString()  );
			tranGalDto.setBuildDate			( data[1] == null ? "" : data[1].toString()  );
			tranGalDto.setBuildTime			( data[2] == null ? "" : data[2].toString()  );
			//just to send number line without zero. the line number format is 01 and warranty just need the 1
			tranGalDto.setLineNumber			( data[3] == null ? "" : data[3].toString().length() > 1 ?  data[3].toString().substring(1,2) : data[3].toString());
			tranGalDto.setPlantCode			( data[4] == null ? "" : data[4].toString()  );
			tranGalDto.setShift				( data[5] == null ? "" : data[5].toString()  );
			tranGalDto.setTeamCd				( data[6] == null ? "" : data[6].toString()  );
			tranGalDto.setTypeCd				( data[7] == null ? "" : data[7].toString()  );
			tranGalDto.setCastLotNumber		( data[8] == null ? "" : data[8].toString()  );
			tranGalDto.setMachLotNumber		( data[9] == null ? "" : data[9].toString()  );
			tranGalDto.setTorqueCastLotNumber	( data[10] == null ? "" : data[10].toString() );
			tranGalDto.setTorqueMachLotNumber	( data[11] == null ? "" : data[11].toString() );
			tranGalDto.setKdLotNumber			( data[12] == null ? "" : data[12].toString() );
			tranGalDto.setProdLotKd			( data[13] == null ? "" : data[13].toString() );
			
			listOfTranGal.add( tranGalDto );				
		}	
	}
	
}
