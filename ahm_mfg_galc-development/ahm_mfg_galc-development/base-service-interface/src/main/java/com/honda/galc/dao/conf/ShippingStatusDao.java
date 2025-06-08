package com.honda.galc.dao.conf;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.service.IDaoService;

public interface ShippingStatusDao extends IDaoService<ShippingStatus, String> {

	List<ShippingStatus> getVinsWithOpenStatus(Timestamp cutoff_timestamp);
	List<ShippingStatus> getSelectedVins(List<String> vins);
	List<ShippingStatus> getfactoryReturns();
	List<ShippingStatus> getVinByStatus(final String status, final List<String> vin, final Date dateTime);
	List<ShippingStatus> getVinInOtherStatus(String status, List<String> vin, final Date dateTime);
	Integer countVinOpenStatus(final String status, final Date dateTime);
	
	public List<ShippingStatus> findNotInvoicedByShippingStatus(int shippingStatus);
	public List<ShippingStatus> findNotInvoicedBy75ATimestamp(int shippingStatus, Timestamp startTs, Timestamp endTs, String pp);
	
	public List<Object[]> getInvoicedVindDetails(String lastSuccessfulRun, String toTimeStamp, String partNames);
	
	public List<Object[]> getInvoicedVindDetailsWithSubPart(String lastSuccessfulRun, String toTimeStamp, String partNames, String subPartNames);
	
	public List<Object[]> getInvoicedVindDetailsWithExtRequired(String fromTimeStamp, String toTimeStamp, String partNames, List<String> framePrefix);

}
