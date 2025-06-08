package com.honda.galc.service.msip.handler.inbound;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.entity.fif.SalesOrderFif;
import com.honda.galc.entity.fif.SalesOrderFifId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.inbound.Gmm098Dto;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/*
 * 
 * @author Sivakumar Ponnusamy
 * @date Nov 17, 2017
 */

public class Gmm098Handler extends BaseMsipInboundHandler<BaseMsipPropertyBean, Gmm098Dto> {

	public Gmm098Handler() {
	}

	public boolean execute(List<Gmm098Dto> dtoList) {
		try {
			processSalesOrderFif(dtoList);
		} catch (Exception ex) {
			getLogger().error("Unexpected Error Occured: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	private void processSalesOrderFif(List<Gmm098Dto> salesOrderFifDtoList) throws ParseException {
		getLogger().info("start to process SalesOrderFif");

		// Process files(s)
		List<Gmm098Dto> salesOrderFifDtoListNew = new ArrayList<Gmm098Dto>();
		for (Gmm098Dto receivedRecord : salesOrderFifDtoList) {
			if (receivedRecord.getUniqueId() == null || receivedRecord.getSalesModelCd() == null
					|| receivedRecord.getSalesVehDestCd() == null || receivedRecord.getSalesOptionCd() == null
					|| receivedRecord.getSalesExtClrCd() == null || receivedRecord.getSalesIntClrCd() == null
					|| receivedRecord.getOrderType() == null || receivedRecord.getFrmPlantCd() == null
					|| receivedRecord.getFrmModelYearCd() == null || receivedRecord.getFrmModelCd() == null
					|| receivedRecord.getFrmDevSeqCd() == null || receivedRecord.getFrmTypeCd() == null
					|| receivedRecord.getFrmOptionCd() == null || receivedRecord.getFrmExtClrCd() == null
					|| receivedRecord.getFrmIntClrCd() == null) {
				getLogger().emergency("The primary key is missing for this record: " + receivedRecord);

			} else {
				salesOrderFifDtoListNew.add(receivedRecord);
			}
		}
		// Update or insert data
		for (Gmm098Dto salesOrderFifDto : salesOrderFifDtoListNew) {
			SalesOrderFif salesOrderFif = deriveSalesOrderFif(salesOrderFifDto);
			ServiceFactory.getDao(SalesOrderFifDao.class).save(salesOrderFif);
			getLogger().info("Sales Order FIF record saved" + salesOrderFif);
		}
		getLogger().info("Sales Order FIF record saved; file processed: " + salesOrderFifDtoList);
	}
	

	public SalesOrderFif deriveSalesOrderFif(Gmm098Dto dto) {
		SalesOrderFif salesOrderFif = new SalesOrderFif();
		salesOrderFif.setId(deriveID(dto));
		salesOrderFif.setBuildByDt(dto.getBuildByDt());
		salesOrderFif.setEfctEndDt(dto.getEfctEndDt());
		salesOrderFif.setFifCodes(dto.getFifCodes());
		salesOrderFif.setLocProdMonth(dto.getLocProdMonth());
		salesOrderFif.setModelYear(dto.getModelYear());
		salesOrderFif.setProdDiv(dto.getProdDiv());
		salesOrderFif.setProdQty(dto.getProdQty());
		salesOrderFif.setSalesDiv(dto.getSalesDiv());
		salesOrderFif.setSalesKdLotNo(dto.getSalesKdLotNo());
		salesOrderFif.setShipperId(dto.getShipperId());
		return salesOrderFif;
	}

	private SalesOrderFifId deriveID(Gmm098Dto dto) {
		SalesOrderFifId salesOrderFifId = new SalesOrderFifId();
		salesOrderFifId.setFrmDevSeqCd(dto.getFrmDevSeqCd());
		salesOrderFifId.setFrmExtClrCd(dto.getFrmExtClrCd());
		salesOrderFifId.setFrmIntClrCd(dto.getFrmIntClrCd());
		salesOrderFifId.setFrmModelCd(dto.getFrmModelCd());
		salesOrderFifId.setFrmModelYearCd(dto.getFrmModelYearCd());
		salesOrderFifId.setFrmOptionCd(dto.getFrmOptionCd());
		salesOrderFifId.setFrmPlantCd(dto.getFrmPlantCd());
		salesOrderFifId.setFrmTypeCd(dto.getFrmTypeCd());
		salesOrderFifId.setOrderSeqNo(dto.getOrderSeqNo());
		salesOrderFifId.setOrderType(dto.getOrderType());
		salesOrderFifId.setSalesExtClrCd(dto.getSalesExtClrCd());
		salesOrderFifId.setSalesIntClrCd(dto.getSalesIntClrCd());
		salesOrderFifId.setSalesModelCd(dto.getSalesModelCd());
		salesOrderFifId.setSalesOptionCd(dto.getSalesOptionCd());
		salesOrderFifId.setSalesVehDestCd(dto.getSalesVehDestCd());
		salesOrderFifId.setUniqueId(dto.getUniqueId());
		return salesOrderFifId;
	}
}