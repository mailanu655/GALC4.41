package com.honda.galc.service.msip.handler.inbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductShippingDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductShipping;
import com.honda.galc.entity.product.ProductShippingId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.msip.dto.inbound.RckaeDto;
import com.honda.galc.service.msip.property.inbound.RckaePropertyBean;
import com.honda.galc.service.property.PropertyService;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class RckaeHandler extends BaseMsipInboundHandler<RckaePropertyBean, RckaeDto> {
	
	String strProductType;
	String siteLineId;
	
	public RckaeHandler() {}

	public boolean execute(List<RckaeDto> dtoList) {
	
		try {
			String ppId = getPropertyBean().getShippingRecvPpid();
			List<String> enginesToVerify = new ArrayList<String>(); 
			SystemPropertyBean systemBean = PropertyService.getPropertyBean(SystemPropertyBean.class);
			SimpleDateFormat sdf = new SimpleDateFormat(getPropertyBean().getTimestampFormat());
			strProductType = systemBean.getProductType();
			siteLineId = getPropertyBean().getLineNo();
			//update or insert data
			for(RckaeDto dto:dtoList){
				int lineNumber = dto.getLineNumber();
				String kdLotNumber = dto.getKdLotNumber();
				String model = dto.getModel();
				String type = dto.getType();
				String option = dto.getOption();
				String rackID = dto.getRackId();
				String ein = dto.getEin();
				String trailerId = dto.getTrailerId();
				String timestampStr = dto.getTimestamp();
				Timestamp timestamp = new java.sql.Timestamp(sdf.parse(timestampStr).getTime());
				
	
				if(strProductType.equals(ProductType.FRAME.toString()) && Integer.parseInt(siteLineId) == lineNumber) { // for assembly lines i.e. Line 1 or Line 2 at HMA
					if(checkStatusBeforeUpdate(ein)) { 
						try {
							createEngine(ein, kdLotNumber.trim(), model.trim() + type.trim() + option.trim(), trailerId, ppId);
							createEngineHistory(ProductType.ENGINE, ein, model.trim() + type.trim() + option.trim(), ppId);
						} catch (SQLException sqlExcep) {
							getLogger().error("SQLException occured in Line " + lineNumber + " while processing Engine: " + ein + "\n" + ExceptionUtils.getFullStackTrace(sqlExcep));
							try {
								getLogger().error("Retrying tracking Engine: " + ein);
								createEngine(ein, kdLotNumber.trim(), model.trim() + type.trim() + option.trim(), trailerId, ppId);
								createEngineHistory(ProductType.ENGINE, ein, model.trim() + type.trim() + option.trim(), ppId);
							} catch (Exception e) {
								getLogger().error("Exception occured in Line " + lineNumber + " while retrying processing Engine: " + ein + "\n" + ExceptionUtils.getFullStackTrace(e));
							}
						} catch (Exception e) {
							getLogger().error("Exception occured in Line " + lineNumber + " while processing Engine: " + ein + "\n" + ExceptionUtils.getFullStackTrace(e));
						}
						enginesToVerify.add(ein);
					}
				}
				if(strProductType.equals(ProductType.ENGINE.toString())){ // for Engine Line
					saveData(ein, lineNumber, rackID, timestamp, trailerId);
				}
			}
			//		This verification is here because of deadlocks while executing track method in TrackingService
			//		can result in failure of inserting records into GAL131TBX (Engine) 
			//		without any exception thrown
			for (String ein : enginesToVerify) {
				if(ServiceFactory.getDao(EngineDao.class).findByKey(ein) == null) {
					getLogger().error("The engine is missing: " + ein);
				}
			}
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void saveData(String ein, int lineNumber, String rackID,Timestamp timestamp, String trailerId){
		// track the product
		try {
			TrackingService ts = ServiceFactory.getService(TrackingService.class);
			String shipLinePpid = PropertyService.getProperty(getComponentId(), "SHIPPING_RECV_PPID{L" + lineNumber + "}", "");
			ts.track(ProductType.ENGINE, ein, shipLinePpid); // line number from the file
		} catch (Exception e) {
			getLogger().error("Exception occured in Line " + lineNumber + " while processing Engine: " + ein + "\n" + ExceptionUtils.getFullStackTrace(e));
		}
		
		// update OFF_TIMESTAMP of PRODUCT_CARRIER_TBX TABLE
		ProductCarrierDao pcd = ServiceFactory.getDao(ProductCarrierDao.class);
		List<ProductCarrier> prodCarrList = pcd.findAll(ein, rackID);
		if(prodCarrList != null && !prodCarrList.isEmpty()) {
			ProductCarrier pc = prodCarrList.get(0);
			pc.setOffTimestamp(timestamp);
			pcd.update(pc);
		}
		ProductShipping ps = new ProductShipping();
		ProductShippingId id = new ProductShippingId();
		id.setProductId(ein);
		id.setTrailerNumber(trailerId);
		ps.setProductTypeString(ProductType.ENGINE.toString());
		ps.setId(id);
		ps.setDunnage(rackID);
		ps.setTrailerStatus(1);
		Date date = new Date(timestamp.getTime());
		ps.setShipDate(date);
		getDao(ProductShippingDao.class).save(ps);
	}
	public void createEngineHistory(ProductType productType, String productId, String productSpecCode, String processPointId) {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		ProductResult productResult = new ProductResult(productId, processPointId, currentTimestamp);
		productResult.setProductionDate(new java.sql.Date(new java.util.Date().getTime()));
		ProductHistory productHistory = (ProductHistory)productResult;
		productHistory.setAssociateNo("");
		productHistory.setApproverNo("");
		productHistory.setActualTimestamp(currentTimestamp);
		productHistory.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		productHistory.setProcessCount(1);
		ServiceFactory.getDao(ProductResultDao.class).save((ProductResult) productHistory);
	}
	

	public void createEngine(String ein, String kdLotNumber, String productSpecCode, String trailerId, String processPointId) throws SQLException {
		Engine engine = new Engine();
		engine.setKdLotNumber(kdLotNumber.trim());
		engine.setProductSpecCode(productSpecCode);
		engine.setPlantCode(PropertyService.getSiteName());
		engine.setProductId(ein);
		engine.setProductionLot("");
		engine.setEngineFiringFlag((short)0);
		engine.setMissionSerialNo(trailerId);
		engine.setLastPassingProcessPointId(processPointId);
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(processPointId);
		engine.setTrackingStatus(processPoint.getLineId());
		getDao(EngineDao.class).saveEngine(engine);
		getLogger().info("Engine created:" + engine.getProductId());
	}

//	Allow data update only if 
//	1. Engine does not exist or 
//	2. Engine exists but TrackingStatus is not set 
//		or TrackingStatus is in set of parameters from EIN_TRACK_STATUS_EXCLUSIONS
	private boolean checkStatusBeforeUpdate(String ein) {
		boolean result = false;
		Engine eng = getDao(EngineDao.class).findByKey(ein);
		List<String> lineIds = getPropertyBean().getEinTrackStatusExclusions();
		if (eng == null || eng.getTrackingStatus() == null) {
			result = true;
		} else {
			if(lineIds != null && lineIds.size() > 0) {
				if (lineIds.contains(eng.getTrackingStatus())) {
					result = true;
				} else {
					String errorMessage = "The engine is currently online or installed to frame. EngineId: " + ein + ", TrackingStatus: " + eng.getTrackingStatus() + "; Data is not updated.";
					getLogger().error(errorMessage);
				}
			} else {
				String errorMessage = "Expecting EIN_TRACK_STATUS_EXCLUSIONS set for ";// + this.componentId;
				getLogger().error(errorMessage);
			}
		}
		return result;
	}

}
