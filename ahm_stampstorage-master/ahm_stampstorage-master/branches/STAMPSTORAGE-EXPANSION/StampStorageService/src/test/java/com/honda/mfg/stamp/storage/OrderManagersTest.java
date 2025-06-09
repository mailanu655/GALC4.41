package com.honda.mfg.stamp.storage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */

import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class OrderManagersTest {

	@Test
	public void successfullyTestOrderManagers() {
		loadTestData();
		OrderManagers.getWeldLine1();
		OrderManagers.getWeldLine2();
	}

	public void loadTestData() {

		Stop stop3 = new Stop(500L);
		stop3.setName("stop3");
		stop3.setStopType(StopType.NO_ACTION);
		stop3.setStopArea(StopArea.UNDEFINED);

		stop3.persist();

		OrderMgr orderMgr1 = new OrderMgr();
		orderMgr1.setId(1L);
		orderMgr1.setLineName("line1");
		orderMgr1.setMaxDeliveryCapacity(new Integer(12));
		orderMgr1.setDeliveryStop(stop3);
		orderMgr1.setLeftConsumptionExit(stop3);
		orderMgr1.setLeftConsumptionStop(stop3);
		orderMgr1.setRightConsumptionExit(stop3);
		orderMgr1.setRightConsumptionStop(stop3);
		orderMgr1.persist();

		OrderMgr orderMgr2 = new OrderMgr();
		orderMgr2.setId(2L);
		orderMgr2.setLineName("line2");
		orderMgr2.setMaxDeliveryCapacity(new Integer(12));
		orderMgr2.setDeliveryStop(stop3);
		orderMgr2.setLeftConsumptionExit(stop3);
		orderMgr2.setLeftConsumptionStop(stop3);
		orderMgr2.setRightConsumptionExit(stop3);
		orderMgr2.setRightConsumptionStop(stop3);
		orderMgr2.persist();

	}
}