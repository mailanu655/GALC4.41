package com.honda.galc.dao.oif;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.entity.oif.Invoice;
import com.honda.galc.entity.product.HostMtoc;
import com.honda.galc.entity.product.HostMtocId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>HostMtocDao Class description</h3>
 * <p> HostMtocDao description </p>
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
 * @author Jeffray Huang<br>
 * Dec 1, 2010
 *
 *
 */
public interface InvoiceDao extends IDaoService<Invoice, Integer> {

	public List<Invoice> findByInvoiceDate(Timestamp invDate);

}
