package com.honda.galc.dao.jpa.oif;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.oif.InvoiceDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.entity.oif.Invoice;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.service.Parameters;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class InvoiceDaoImpl extends BaseDaoImpl<Invoice,Integer> implements InvoiceDao {
	
	private static final String SELECT_INVOICE_BY_DATE = 
			"select I "
			+ "from PurchaseContract I where date(I.invoiceDate) = :invDate "
			+ "order by I.invoiceNo ";
	
	
	public List<Invoice> findByInvoiceDate(Timestamp invDate) {
		Parameters p = Parameters.with("modelCode", new Date(invDate.getTime()));
		return findAllByQuery(SELECT_INVOICE_BY_DATE, p);
	}

}
