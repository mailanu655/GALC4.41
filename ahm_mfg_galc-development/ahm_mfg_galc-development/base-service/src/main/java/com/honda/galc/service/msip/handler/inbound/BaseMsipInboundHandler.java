package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.oif.FifCodeChoicesDao;
import com.honda.galc.dao.oif.FifCodeGroupsDao;
import com.honda.galc.dao.oif.FrameShipConfirmationDao;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.dao.product.MissionSpecDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Mission;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gpp305Dto;
import com.honda.galc.service.msip.dto.inbound.IMsipInboundDto;
import com.honda.galc.service.msip.dto.inbound.PreProductionLotDto;
import com.honda.galc.service.msip.dto.inbound.ProductionLotDto;
import com.honda.galc.service.msip.handler.BaseMsipHandler;
import com.honda.galc.service.msip.handler.inbound.IMsipInboundHandler;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.service.msip.util.MsipUtil;

/**
 * @author Subu Kathiresan
 * @date Mar 27, 2017
 */
public abstract class BaseMsipInboundHandler<P extends BaseMsipPropertyBean, D extends IMsipInboundDto> 
 extends BaseMsipHandler<P> implements IMsipInboundHandler<D> {
	
	public static final Integer CHARACTER_SET = 819;
	public static final Integer ENCODING_ASCII = 279;
	public static final String JAPAN_VIN_LEFT_JUSTIFIED = "JAPAN_VIN_LEFT_JUSTIFIED";
	public static final String LAST_PROCESS_TIMESTAMP = "LAST_PROCESS_TIMESTAMP";
	
	public boolean execute(String inputParameter) {
		return true;
	}
	
	public boolean checkDigitValidity(String productId) {
		String[] validDigits = getPropertyBean().getValidCheckDigits();
		for(String validDigit:validDigits){
			if(validDigit.trim().equalsIgnoreCase(Character.toString(productId.charAt(8)))) return true;
		}
		return false;
	}

	public Frame createFrame(ProductionLot pl, String productId, boolean isJapaneseVIN) {
		Frame frame = new Frame(productId);
		frame.setProductionLot(pl.getProductionLot());
		frame.setKdLotNumber(pl.getKdLotNumber());
		frame.setProductSpecCode(pl.getProductSpecCode());
		frame.setPlanOffDate(pl.getPlanOffDate());
		frame.setProductionDate(pl.getProductionDate());
		if(isJapaneseVIN) {
			frame.setShortVin(productId); 
		} else {
			frame.setShortVin(	ProductNumberDef.VIN.getModel(productId) +
								ProductNumberDef.VIN.getYear(productId) + 
								ProductNumberDef.VIN.getPlant(productId) +
								ProductNumberDef.VIN.getSequence(productId));
		}
		return frame;
	}
	
	protected Engine createEngine(ProductionLot pl, String productId) {
		 Engine engine = new Engine();
		 engine.setProductId(productId);
		 engine.setProductionLot(pl.getProductionLot());
		 engine.setKdLotNumber(pl.getKdLotNumber());
		 engine.setProductSpecCode(pl.getProductSpecCode());
		 EngineSpec engineSpec = getDao(EngineSpecDao.class).findByKey(pl.getProductSpecCode());
		 engine.setActualMissionType(engineSpec.getActualMissionType());
		 
		 engine.setPlanOffDate(pl.getPlanOffDate());
		 engine.setProductionDate(pl.getProductionDate());
		 engine.setLastPassingProcessPointId(getPropertyBean().getOffLineId());
		 engine.setTrackingStatus(getPropertyBean().getOffProcessPoint());
		 return engine;
	}

	public ProductStampingSequence createStampingSequence(ProductionLot pl, int i, String productId) {
		ProductStampingSequenceId stampingSequenceId = new ProductStampingSequenceId(pl.getProductionLot(), productId);
		ProductStampingSequence stampingSequence = new ProductStampingSequence();
		stampingSequence.setId(stampingSequenceId);
		stampingSequence.setStampingSequenceNumber(i+1);
		stampingSequence.setSendStatus(0);
		return stampingSequence;
	}
	
	public ProductionLotDto addPLDTO(Gpp305Dto plan305) { 
		ProductionLotDto pldto = deriveProductionLotDto(plan305);
		String strProductionLot = plan305.generateProductionLot();
		pldto.setProductionLot(strProductionLot);
		pldto.setProdLotKd(strProductionLot);
		return pldto;
	}
	
	protected Map<String, PreProductionLotDto> getTailsByProcessLocation() { 
		Map<String, PreProductionLotDto> tailsByLocation = new HashMap<String, PreProductionLotDto>(); 
		for(String siteProcessLocation : getPropertyBean().getProcessLocations()) {
			List<PreProductionLot> tails = getDao(PreProductionLotDao.class).
					getTailsByPlantLineLocation(getPropertyBean().getSiteName(), 
							getPropertyBean().getLineNo(), siteProcessLocation);
//		 	There should be only one "tail" i.e. a record in PreProduction Plan table
//			with next_production_plan set to null for plantCode, lineNo and processLocation 	
			switch(tails.size()) {
				case 0:
					StringBuffer sb = new StringBuffer("Unable to find the last preproduction lot record with ")
							.append(" PlantCode = ").append(getPropertyBean().getSiteName())
							.append("; LineNo = ").append(getPropertyBean().getLineNo())
							.append("; ProcessLocation = " + siteProcessLocation);
					getLogger().info(sb.toString());
					return null;
				case 1: 	// OK
					break;
				default:
					sb = new StringBuffer("Found more than one production lot with null in the next_production_lot with ")
						.append(" PlantCode = ").append(getPropertyBean().getSiteName())
						.append("; LineNo = ").append(getPropertyBean().getLineNo())
						.append("; ProcessLocation = " + siteProcessLocation);
					getLogger().info(sb.toString());
					return null;
			};
			PreProductionLotDto tailDto = new PreProductionLotDto(tails.get(0));
			tailsByLocation.put(siteProcessLocation, tailDto);
		}
		return tailsByLocation;
	}
	
	/**
	 * Method to generate all the missions start product id
	 * @param productionLotDTO
	 */
	protected void insertTransmissions ( final ProductionLotDto productionLotDto )
	{
		MissionDao 					missionDao			= ServiceFactory.getDao( MissionDao.class );
		ProductStampingSequenceDao productStampingDao	=	ServiceFactory.getDao( ProductStampingSequenceDao.class );
		//get the start product id
		final String startProduct		= productionLotDto.getStartProductId ();
		final Integer	lotSize			= productionLotDto.getLotSize ();
		//get the product id base data
		final String	productIdBase	= startProduct.substring ( 0, 5 );
		//get the base sequence to start the sequence calculation
		final Integer sequenceBase	= Integer.valueOf( startProduct.substring( 5, 11 ) );
		//get initialization properties, 
		//the initial process point is the material service off process point
		//the initial line id (tracking status) is the material service 
		final String initialPassingProcessPoint	=	getPropertyBean().getOffProcessPoint();
		final String initialTrackingStatus		=	getPropertyBean().getOffLineId();
		for (int i = 0; i < lotSize; i++)
		{
			final Integer sequence	=	sequenceBase + i;
			Mission mission	=	new Mission();
			mission.setProductId( new StringBuilder( productIdBase ).append( String.format( "%06d", sequence )).toString() );
			mission.setProductionLot	( productionLotDto.getProductionLot () 	);
			mission.setProductSpecCode	( productionLotDto.getProductSpecCode ());
			mission.setPlanOffDate		( productionLotDto.getPlanOffDate ()	);
			mission.setKdLotNumber		( productionLotDto.getKdLotNumber ()	);
			mission.setProductionDate	( productionLotDto.getProductionDate ()	);
			mission.setLastPassingProcessPointId	( initialPassingProcessPoint );
			mission.setTrackingStatus	( initialTrackingStatus );
			missionDao.save( mission );
			
			//creating the product stamping sequence (table gal216tbx)
			//create Id
			ProductStampingSequenceId productStampingSequeneId	=	new ProductStampingSequenceId ();
			productStampingSequeneId.setProductID		(	mission.getId()				);
			productStampingSequeneId.setProductionLot	(	mission.getProductionLot()	);
			//create the prdocut sequence
			ProductStampingSequence	productStampingSequence = new ProductStampingSequence ();
			productStampingSequence.setId			(	productStampingSequeneId	);
			productStampingSequence.setSendStatus	(	0	);
			productStampingSequence.setStampingSequenceNumber( i + 1 );
			productStampingDao.save(productStampingSequence);
			
		}
	}
	
	protected Map<String, PreProductionLotDto> getTailsByPlanCode() {
		Map<String, PreProductionLotDto> tailsByPlanCode = new HashMap<String, PreProductionLotDto>();
		
		for(String planCode : getPropertyBean().getPlanCodes()) {
			List<PreProductionLot> tails = getDao(PreProductionLotDao.class).getTailsByPlanCode(planCode);
			
//		 	There should be only one "tail" i.e. a record in PreProduction Plan table
//			with next_production_plan set to null for plantCode, lineNo and processLocation 	
			switch(tails.size()) {
				case 0:
					StringBuffer sb = new StringBuffer("Unable to find the last preproduction lot record with ")
							.append(" PlanCode = ").append(planCode);
					
					getLogger().info(sb.toString());
					
					return null;
				case 1: 	// OK
					break;
				default:
					sb = new StringBuffer("Found more than one production lot with null in the next_production_lot with ")
						.append(" PlanCode = ").append(planCode);

					getLogger().info(sb.toString());					
					return null;
			};

			tailsByPlanCode.put(planCode, new PreProductionLotDto(tails.get(0)));
		}

		return tailsByPlanCode;
	}
	
	/**
	 * @param compTs: ComponentStatus
	 * @return Timestamp
	 * given component status, convert the status value to a Timestamp
	 */
	protected Timestamp getComponentTimestamp(ComponentStatus compTs) {
		Timestamp ts = null;
		if (compTs != null && !StringUtils.isBlank(compTs.getStatusValue())) {
			try {
				String strTS = StringUtils.trim(compTs.getStatusValue());
				ts = Timestamp.valueOf(strTS);
			} catch (Exception e) {
				getLogger().error(e, "Error parsing timestamp" + compTs.getStatusValue());
			}
		}
		return ts;
	}

	/**
	 * @param key:String
	 * @return Timestamp
	 * find timestamp value given the status key, for the current component id
	 */
	protected Timestamp getComponentTimestamp(String key) {
		Timestamp ts = null;
		if(!StringUtils.isEmpty(key))  {
			ComponentStatus compTs = getComponentStatus(LAST_PROCESS_TIMESTAMP);
			if(compTs != null)  {
				ts = getComponentTimestamp(compTs);
			}
		}
		return ts;
	}
	
	protected ComponentStatus getComponentStatus(String key)  {
		ComponentStatusId id = new ComponentStatusId(getComponentId(), key);
		ComponentStatus cStat = getCompStatusDao().findByKey(id);
		return cStat;
	}

	/**
	 * @return Timestamp
	 * get timestamp for key=LAST_PROCESS_TIMESTAMP
	 */
	protected Timestamp getLastProcessTimestamp() {
		return getComponentTimestamp(LAST_PROCESS_TIMESTAMP);
	}

	/**
	 * @param currentTimestamp
	 * update the timestamp for key=LAST_PROCESS_TIMESTAMP to the given Timestamp
	 */
	protected void updateLastProcessTimestamp(Timestamp currentTimestamp) {
		
		if(currentTimestamp == null)  return;
		
		ComponentStatus cStat = getComponentStatus(LAST_PROCESS_TIMESTAMP);
		if(cStat == null)  {
			cStat = new ComponentStatus(getComponentId(), LAST_PROCESS_TIMESTAMP, currentTimestamp.toString());			
		} else {
			cStat.setStatusValue(currentTimestamp.toString());
		}
		getCompStatusDao().save(cStat);
	}
	
    public ProductionLot deriveProductionLot(ProductionLotDto plDto) {
		ProductionLot pl = new ProductionLot();
	   	pl.setProductionLot(plDto.getProductionLot());
	   	pl.setLotSize(plDto.getLotSize());
	   	pl.setStartProductId(plDto.getStartProductId());
	   	pl.setDemandType(plDto.getDemandType());
	   	pl.setKdLotNumber(plDto.getKdLotNumber());
	   	pl.setPlantCode(plDto.getPlantCode());
	   	pl.setLineNo(plDto.getLineNo());
	   	pl.setProcessLocation(plDto.getProcessLocation());
	   	pl.setProductSpecCode(plDto.getProductSpecCode());
	   	pl.setPlanCode(plDto.getPlanCode());
	   	pl.setLotNumber(plDto.getLotNumber());
		pl.setPlanOffDate(plDto.getPlanOffDate());
		pl.setProductionDate(plDto.getProductionDate());
		pl.setProdLotKd(plDto.getProdLotKd());
		pl.setDemandType(plDto.getDemandType());
	   	pl.setWeLineNo(plDto.getLineNoWE());
	   	pl.setWeProcessLocation(plDto.getProcessLocationWE());
	   	pl.setPaLineNo(plDto.getLineNoPA());
	   	pl.setPaProcessLocation(plDto.getProcessLocationPA());
		return pl; 
	}
    
    public ProductionLotDto deriveProductionLotDto(Gpp305Dto dto305) {
		ProductionLotDto pl = new ProductionLotDto();
		Date sqlDate;
		try {
			sqlDate = new java.sql.Date(MsipUtil.sdf1.parse(dto305.getPlanOffDate()).getTime());
			pl.setProductionDate(sqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
			getLogger().error("Unable to parse GPP305 request");
		} 
		pl.setProductionLot(dto305.getProductionLot());
		pl.setPlanCode(dto305.getPlanCode());
		pl.setPlantCode(dto305.getPlantCode());
		pl.setLotSize(dto305.getProductionQuantity());
		pl.setStartProductId(dto305.getStartProductId());
		pl.setLotNumber(dto305.getLotNumber());
		pl.setLineNo(dto305.getLineNo());
		pl.setProcessLocation(dto305.getProcessLocation());
		pl.setProductSpecCode(dto305.getProductSpecCode());
		pl.setKdLotNumber(dto305.getKdLotNumber());
		pl.setDemandType(dto305.getDemandType());
		pl.setProcessFlag(dto305.getProcessFlag());
		pl.setLineNoWE(dto305.getLineNoWE());
    	pl.setProcessLocationWE(dto305.getProcessLocationWE());
    	pl.setLineNoPA(dto305.getLineNoPA());
    	pl.setProcessLocationPA(dto305.getProcessLocationPA());
    	pl.setModelYearCode(dto305.getModelYearCode());
    	pl.setModelCode(dto305.getModelCode());
    	pl.setModelTypeCode(dto305.getModelTypeCode());
    	pl.setModelOptionCode(dto305.getModelOptionCode());
    	pl.setExteriorColorCode(dto305.getExteriorColorCode());
    	pl.setInternalColorCode(dto305.getInternalColorCode());
    	pl.setRemakeFlag(dto305.getRemakeFlag());
    	pl.setStampingFlag(dto305.getStampingFlag());
    	pl.setCarryInOutFlag(dto305.getCarryInOutFlag());
    	pl.setNumberOfUnitsCarryInOut(dto305.getNumberOfUnitsCarryInOut());
		return pl; 
	}
    
    public MissionSpecDao getMissionSpecDao() {
    	return ServiceFactory.getDao(MissionSpecDao.class);
    }
       
    public BomDao getBomDao() {
    	return ServiceFactory.getDao(BomDao.class);	
    }
    
    public FrameShipConfirmationDao getframeShipConfirmationDao() {
    	return ServiceFactory.getDao(FrameShipConfirmationDao.class);
    }
    
    public FifCodeGroupsDao getFifCodeGroupsDao() {
    	return ServiceFactory.getDao(FifCodeGroupsDao.class);
    }
    
    public FifCodeChoicesDao getFifCodeChoicesDao() {
    	return ServiceFactory.getDao(FifCodeChoicesDao.class);
    }
    public SalesOrderFifDao getSalesOrderFifDao() {
    	return ServiceFactory.getDao(SalesOrderFifDao.class);
    }
    
	public static void writeToFile(List<?> printList, String aFilePath) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(aFilePath)));
		for (Object output : printList) {
			bufferedWriter.write(output.toString());
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}

}
