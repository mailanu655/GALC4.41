package com.honda.galc.service.utils;

import static com.honda.galc.data.ProductType.*;

import java.util.LinkedHashMap;
import java.util.Map;



import org.junit.Test;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.Bumper;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Head;
import com.honda.galc.entity.product.Ipu;
import com.honda.galc.entity.product.Knuckle;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Mission;
import com.honda.galc.entity.product.MissionCase;
import com.honda.galc.entity.product.MovablePulleyDrive;
import com.honda.galc.entity.product.MovablePulleyDriven;
import com.honda.galc.entity.product.PulleyShaftDrive;
import com.honda.galc.entity.product.PulleyShaftDriven;
import com.honda.galc.entity.product.TorqueConverterCase;
import com.honda.galc.entity.product.Weld;

import junit.framework.Assert;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>ProductTypeUtilTest</code> is ... .
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
public class ProductTypeUtilTest {

	@Test
	public void testIsDunnagableProductClass() {
		Map<Class<?>, Boolean> daoMap = new LinkedHashMap<Class<?>, Boolean>();
		daoMap.put(Engine.class, Boolean.FALSE);
		daoMap.put(Frame.class, Boolean.FALSE);
		daoMap.put(Block.class, Boolean.TRUE);
		daoMap.put(Head.class, Boolean.TRUE);
		daoMap.put(Mission.class, Boolean.FALSE);
		daoMap.put(MissionCase.class, Boolean.TRUE);
		daoMap.put(TorqueConverterCase.class, Boolean.TRUE);
		daoMap.put(MovablePulleyDrive.class, Boolean.TRUE);
		daoMap.put(MovablePulleyDriven.class, Boolean.TRUE);
		daoMap.put(PulleyShaftDrive.class, Boolean.TRUE);
		daoMap.put(PulleyShaftDriven.class, Boolean.TRUE);
		daoMap.put(Ipu.class, Boolean.TRUE);
		daoMap.put(Knuckle.class, Boolean.TRUE);
		daoMap.put(MbpnProduct.class, Boolean.TRUE);
		daoMap.put(Crankshaft.class, Boolean.TRUE);
		daoMap.put(Conrod.class, Boolean.TRUE);
		daoMap.put(Bumper.class, Boolean.TRUE);
		daoMap.put(Weld.class, Boolean.TRUE);
		
		for (Map.Entry<Class<?>, Boolean> entry : daoMap.entrySet()) {
			Class<?> entityClass = entry.getKey();
			Boolean expected = entry.getValue();
			Boolean actual = ProductTypeUtil.isDunnagable(entityClass);
			Assert.assertEquals(entityClass.getSimpleName(), expected, actual);
		}
	}
	
	@Test
	public void testIsDunnagableProductType() {
		Map<ProductType, Boolean> daoMap = new LinkedHashMap<ProductType, Boolean>();
		daoMap.put(ENGINE, Boolean.FALSE);
		daoMap.put(FRAME, Boolean.FALSE);
		daoMap.put(FRAME_JPN, Boolean.FALSE);
		daoMap.put(HEAD, Boolean.TRUE);		
		daoMap.put(BLOCK, Boolean.TRUE);
		daoMap.put(MISSION, Boolean.FALSE);
		daoMap.put(MCASE, Boolean.TRUE);
		daoMap.put(TCCASE, Boolean.TRUE);
		daoMap.put(MPDR, Boolean.TRUE);
		daoMap.put(MPDN, Boolean.TRUE);
		daoMap.put(PSDR, Boolean.TRUE);
		daoMap.put(PSDN, Boolean.TRUE);
		daoMap.put(KNUCKLE, Boolean.TRUE);
		daoMap.put(MBPN, Boolean.TRUE);
		daoMap.put(PLASTICS, Boolean.TRUE);
		daoMap.put(WELD, Boolean.TRUE);
		daoMap.put(IPU, Boolean.TRUE);
		daoMap.put(CRANKSHAFT, Boolean.TRUE);
		daoMap.put(CONROD, Boolean.TRUE);
		daoMap.put(BUMPER, Boolean.TRUE);
		daoMap.put(IPU_MBPN, Boolean.TRUE);
		daoMap.put(TDU, Boolean.TRUE);
		daoMap.put(BMP_MBPN, Boolean.TRUE);
		daoMap.put(SUBFRAME, Boolean.TRUE);
		daoMap.put(KNU_MBPN, Boolean.TRUE);
		daoMap.put(MBPN_PART, Boolean.TRUE);
		
		for (Map.Entry<ProductType, Boolean> entry : daoMap.entrySet()) {
			ProductType productType = entry.getKey();
			Boolean expected = entry.getValue();
			Boolean actual = ProductTypeUtil.isDunnagable(productType);
			Assert.assertEquals(productType.name(), expected, actual);
		}
	}
}
