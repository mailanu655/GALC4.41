package com.honda.galc.oif.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.oif.property.ReplicateScheduleProperty;
import com.honda.galc.oif.task.ReplicateScheduleToLinesTask;

import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ReplicateScheduleProperty.class,PreProductionLotDao.class})
public class ReplicateScheduleToLinesTaskTest  {
	

	private ReplicateScheduleToLinesTask testTask=null;
	
	/**
	 * @author Gangadhararao Gadde, HMIN
	 * @date Sept 26, 2018
	 * 
	 * Tests first lot by linked list
	 * 
	 */
	@Test	
	public void getFirstLot_getFirstLotByLinkedList(){
		ReplicateScheduleProperty replicateSchedulePropertyMock = PowerMockito.mock(ReplicateScheduleProperty.class);
		PreProductionLotDao preProductionLotDaoMock =mock(PreProductionLotDao.class);
		PowerMockito.when(replicateSchedulePropertyMock.isCurrentLotOrderBySeqNum()).thenReturn(false);
		PowerMockito.when(preProductionLotDaoMock.findCurrentLotByPlanCodeOrderByLinkedList(Matchers.anyString())).thenReturn(getFirstLotByLinkedList());
		testTask= PowerMockito.mock(ReplicateScheduleToLinesTask.class);		
		assertNotSame(testTask.getFirstLot(Matchers.anyString()),getFirstLotBySeqNum());
	}
	/**
	 * @author Gangadhararao Gadde, HMIN
	 * @date Sept 26, 2018
	 * 
	 * Tests first lot by Seq Number
	 * 
	 */
	@Test	
	public void getFirstLot_getFirstLotBySeqNum(){
		ReplicateScheduleProperty replicateSchedulePropertyMock = PowerMockito.mock(ReplicateScheduleProperty.class);
		PreProductionLotDao preProductionLotDaoMock =mock(PreProductionLotDao.class);
		PowerMockito.when(replicateSchedulePropertyMock.isCurrentLotOrderBySeqNum()).thenReturn(true);
		PowerMockito.when(preProductionLotDaoMock.findCurrentPreProductionLotByPlanCode(Matchers.anyString())).thenReturn(getFirstLotBySeqNum());
		testTask= PowerMockito.mock(ReplicateScheduleToLinesTask.class);		
		assertNotSame(testTask.getFirstLot(Matchers.anyString()),getFirstLotByLinkedList());
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
