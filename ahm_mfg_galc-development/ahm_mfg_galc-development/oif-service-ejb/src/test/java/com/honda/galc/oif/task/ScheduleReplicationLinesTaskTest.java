package com.honda.galc.oif.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.oif.property.ReplicateScheduleProperty;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class, PropertyService.class})
public class ScheduleReplicationLinesTaskTest{

	@Mock
	PreProductionLotDao preProductionLotDaoMock =PowerMockito.mock(PreProductionLotDao.class);

	@Mock
	ComponentPropertyDao componentPropertyDaoMock =PowerMockito.mock(ComponentPropertyDao.class);

	@Mock
	ReplicateScheduleProperty replicateSchedulePropertyMock = PowerMockito.mock(ReplicateScheduleProperty.class);

	@Mock
	ComponentStatusDao componentStatusDaoMock =PowerMockito.mock(ComponentStatusDao.class);

	private ScheduleReplicationLinesTask testTask;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.mockStatic(PropertyService.class);

		PowerMockito.when(PropertyService.getPropertyBean(ReplicateScheduleProperty.class, "OIF_REPLICATION")).thenReturn(replicateSchedulePropertyMock);
		PowerMockito.when(ServiceFactory.getDao(PreProductionLotDao.class)).thenReturn(preProductionLotDaoMock);
		PowerMockito.when(ServiceFactory.getDao(ComponentPropertyDao.class)).thenReturn(componentPropertyDaoMock);
		PowerMockito.when(ServiceFactory.getDao(ComponentStatusDao.class)).thenReturn(componentStatusDaoMock);


		testTask  = new ScheduleReplicationLinesTask("OIF_REPLICATION");	
	}


	/**
	 * @author Lalit Shahani, HMA
	 * @date Jul 11, 2019
	 * 
	 * Tests first lot by linked list
	 * 
	 */
	@Test	
	public void getFirstLot_getFirstLotByLinkedList(){

		PowerMockito.when(replicateSchedulePropertyMock.isCurrentLotOrderBySeqNum()).thenReturn(false);
		PowerMockito.when(preProductionLotDaoMock.findCurrentLotByPlanCodeOrderByLinkedList(Matchers.anyString())).thenReturn(getFirstLotByLinkedList());
		PreProductionLot firstLot = testTask.getFirstLot("HMA 02AF");
		assertEquals(getFirstLotByLinkedList(),firstLot);
	}
	/**
	 * @author Lalit Shahani, HMA
	 * @date Jul 11, 2019
	 * 
	 * Tests first lot by linked list
	 * 
	 */
	@Test	
	public void getFirstLot_getFirstLotBySeqNum(){
		PowerMockito.when(replicateSchedulePropertyMock.isCurrentLotOrderBySeqNum()).thenReturn(true);
		PowerMockito.when(preProductionLotDaoMock.findCurrentPreProductionLotByPlanCode(Matchers.anyString())).thenReturn(getFirstLotBySeqNum());
		PreProductionLot firstLot = testTask.getFirstLot("HMA 02AF");
		assertEquals(getFirstLotBySeqNum(),firstLot);
	}


	/**
	 * @author Lalit Shahani, HMA
	 * @date Jul 11, 2019
	 * 
	 * Tests first lot by linked list
	 * 
	 */
	@Test	
	public void doesPlanCodeHaveTail_lastLotFound(){
		PowerMockito.when(preProductionLotDaoMock.findLastLotByPlanCode("HMA 02AF")).thenReturn(getTestLot());
		boolean hasTail = testTask.doesPlanCodeHaveTail("HMA 02AF");
		assertTrue(hasTail);

	}	


	/**
	 * @author Lalit Shahani, HMA
	 * @date Jul 11, 2019
	 * 
	 * Tests first lot by linked list
	 * 
	 */
	@Test	
	public void doesPlanCodeHaveTail_lastLotNotFound(){
		PowerMockito.when(preProductionLotDaoMock.findLastLotByPlanCode("HMA 02AF")).thenReturn(null);
		List<PreProductionLot> testLotList = new ArrayList<PreProductionLot>();
		testLotList.add(getTestLot());
		PowerMockito.when(preProductionLotDaoMock.findAllByPlanCode("HMA 02AF")).thenReturn(testLotList);

		boolean hasTail = testTask.doesPlanCodeHaveTail("HMA 02AF");
		assertFalse(hasTail);

	}
	


	private PreProductionLot getTestLot() {
		PreProductionLot testLot = new PreProductionLot();
		testLot.setProductionLot("Test Lot");
		return testLot;
	}


	private PreProductionLot getFirstLotByLinkedList() {
		PreProductionLot preProductionLot=new PreProductionLot("TestLot1");
		return preProductionLot;
	}

	private PreProductionLot getFirstLotBySeqNum() {
		PreProductionLot preProductionLot=new PreProductionLot("TestLot2");
		return preProductionLot;
	}

}
