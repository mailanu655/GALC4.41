package com.honda.galc.client.product.validator;

import java.util.List;

import com.honda.galc.client.product.process.AbstractProcessModel;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UniqueInstalledPartValidator</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class UniqueInstalledPartValidator extends AbstractValidator {

	private String partName;
	private AbstractProcessModel model;

	public UniqueInstalledPartValidator(String partName, AbstractProcessModel model) {
		this.partName = partName;
		this.model = model;
		setDetailedMessageTemplate(String.format("part %s is already associated with product", getPartName()));
	}

	public boolean execute(String value) {

		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		List<InstalledPart> installedParts = installedPartDao.findAllByPartNameAndSerialNumber(getPartName(), value);
		if (installedParts != null && installedParts.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (InstalledPart ip : installedParts) {
				if (ip.getProductId().equals(getModel().getProductModel().getProduct().getProductId())) {
					continue;
				}
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(ip.getProductId());
			}
			if (sb.length() > 0) {
				String msg = String.format("Part %s: %s is already associated with product: %s", getPartName(), value, sb.toString());
				setDetailedMessageTemplate(msg);
				return false;
			}
		}
		return true;
	}

	// === get/set === //
	protected AbstractProcessModel getModel() {
		return model;
	}

	protected String getPartName() {
		return partName;
	}
}
