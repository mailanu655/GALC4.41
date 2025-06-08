package com.honda.galc.dao.jpa.product;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import junit.framework.Assert;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductDaoImplTest</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Feb 19, 2019
 */
public class ProductDaoImplTest {

	@Test
	public void testGetIdFieldName() {

		Map<ProductDaoImpl<?>, String> daoMap = new LinkedHashMap<ProductDaoImpl<?>, String>();
		daoMap.put(new EngineDaoImpl(), "productId");
		daoMap.put(new FrameDaoImpl(), "productId");
		daoMap.put(new BlockDaoImpl(), "blockId");
		daoMap.put(new HeadDaoImpl(), "headId");
		daoMap.put(new MissionDaoImpl(), "productId");
		daoMap.put(new MissionCaseDaoImpl(), "productId");
		daoMap.put(new TorqueConverterCaseDaoImpl(), "productId");
		daoMap.put(new MovablePulleyDriveDaoImpl(), "productId");
		daoMap.put(new MovablePulleyDrivenDaoImpl(), "productId");
		daoMap.put(new PulleyShaftDriveDaoImpl(), "productId");
		daoMap.put(new PulleyShaftDrivenDaoImpl(), "productId");
		daoMap.put(new IpuDaoImpl(), "productId");
		daoMap.put(new KnuckleDaoImpl(), "productId");
		daoMap.put(new MbpnProductDaoImpl(), "productId");
		daoMap.put(new CrankshaftDaoImpl(), "crankshaftId");
		daoMap.put(new ConrodDaoImpl(), "conrodId");
		daoMap.put(new BumperDaoImpl(), "productId");
		daoMap.put(new WeldDaoImpl(), "productId");
		
		for (Map.Entry<ProductDaoImpl<?>, String> entry : daoMap.entrySet()) {
			ProductDaoImpl<?> dao = entry.getKey();
			String expected = entry.getValue();
			String actual = dao.getIdFieldName();
			Assert.assertEquals(dao.getClass().getSimpleName(), expected, actual);
		}
	}
}
