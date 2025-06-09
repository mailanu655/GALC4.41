package com.honda.mfg.stamp.storage.service.clientmgr;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.messages.*;
import com.honda.mfg.stamp.conveyor.service.StorageStateUpdateService;
import com.honda.mfg.stamp.storage.service.utils.MessageSinkInterface;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CommandProcessorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPut() {
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		try {
			mSink.put(this, mockMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		verify(mockMessage).getContent();
	}

	@Test
	public void testProcessCommandCarrierUpdateRequest() {
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		
		CarrierUpdateRequestMessage mockRequest = mock(CarrierUpdateRequestMessage.class);
		
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		
		try {
			when(mockMessage.getContent()).thenReturn(mockRequest);
			when(mockRequest.getTargetOp()).thenReturn(
					CarrierUpdateOperations.SAVE,
					CarrierUpdateOperations.STORE_REWORK,
					CarrierUpdateOperations.RECALC_DEST);
			
			mSink.put(this, mockMessage);
			verify(mockManager).saveCarrier(any(Carrier.class));
			mSink.put(this, mockMessage);
			verify(mockManager).storeReworkCarrier(any(Carrier.class));
			mSink.put(this, mockMessage);
			verify(mockManager).recalculateCarrierDestination(any(Carrier.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testProcessLaneUpdateRequest() {
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		
		LaneUpdateRequestMessage mockLaneReq = mock(LaneUpdateRequestMessage.class);
		
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		try {
			when(mockMessage.getContent()).thenReturn(mockLaneReq);
			when(mockLaneReq.getTargetOp()).thenReturn(
				CarrierUpdateOperations.REMOVE_FROM_ROW,
				CarrierUpdateOperations.ADD_TO_ROW,
				CarrierUpdateOperations.REORDER_CARRIERS_IN_ROW);
			
			mSink.put(this, mockMessage);
			verify(mockManager).removeCarrierFromRow(anyInt());
			mSink.put(this, mockMessage);
			verify(mockManager).addCarrierToRow(anyInt(), anyInt(), any(Stop.class));
			mSink.put(this, mockMessage);
			verify(mockManager).reorderCarriersInRow(anyLong());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testProcessCommandReleaseCarrierRequest() {
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		
		ReleaseCarriersRequestMessage mockReleaseCarrierReq = mock(ReleaseCarriersRequestMessage.class);
		
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		try {
			
			when(mockMessage.getContent()).thenReturn(mockReleaseCarrierReq);
            when(mockReleaseCarrierReq.getLaneStop()).thenReturn(new Stop(513l));
            when(mockReleaseCarrierReq.getDestination()).thenReturn(new Stop(1201l));
			when(mockReleaseCarrierReq.getTargetOp()).thenReturn(
				CarrierUpdateOperations.RELEASE_CARRIERS);
			mSink.put(this, mockMessage);
			verify(mockManager).releaseCarriers(any(Stop.class), anyInt(), any(Stop.class), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testProcessCommandEmptyReleaseRequest() {
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		
		EmptyCarrierReleaseRequest mockReleaseEmptyReq = mock(EmptyCarrierReleaseRequest.class);
		
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		try {
			
			when(mockMessage.getContent()).thenReturn(mockReleaseEmptyReq);
			when(mockReleaseEmptyReq.getTargetOp()).thenReturn(
				CarrierUpdateOperations.RELEASE_EMPTIES_FROM_ROW);
			mSink.put(this, mockMessage);
			verify(mockManager).releaseEmptyCarriersFromRows(any(StorageArea.class), anyBoolean(), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testProcessCommandRowResequence() {
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		
		UpdateLaneCarrierListRequestMessage mockReq = mock(UpdateLaneCarrierListRequestMessage.class);
		List<Integer> carrierNumbers = Arrays.asList(1,2,3);
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		try {
			
			when(mockMessage.getContent()).thenReturn(mockReq);
			when(mockReq.getTargetOp()).thenReturn(
				CarrierUpdateOperations.ROW_RESEQUENCE);
			when(mockReq.getCarrierNumberList()).thenReturn(carrierNumbers);
			mSink.put(this, mockMessage);
			verify(mockManager).saveCarriersInToRow(anyList(), any(Stop.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testProcessCommandGroupHold() {
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		
		BulkStatusUpdateRequestMessage mockReq = mock(BulkStatusUpdateRequestMessage.class);
		List<Integer> carrierNumbers = Arrays.asList(1,2,3);
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		try {
			
			when(mockMessage.getContent()).thenReturn(mockReq);
			when(mockReq.getTargetOp()).thenReturn(
				CarrierUpdateOperations.GROUP_HOLD);
			when(mockReq.getCarrierNumberList()).thenReturn(carrierNumbers);
			mSink.put(this, mockMessage);
			verify(mockManager).sendBulkCarrierStatusUpdate(anyList(), any(CarrierStatus.class), anyString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetInstance() {
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		assertNotNull(mSink);
	}

	@Test
	public void testGetCommandProcessorInstance() {
		CommandProcessorInterface cmdProc = CommandProcessor.getCommandProcessorInstance();
		assertNotNull(cmdProc);
	}

//	@Test
//	public void testGetMesConnectionStatus() {
//		Object o = CommandProcessor.getCommandProcessorInstance().getMesConnectionStatus();
//		assertNotNull(o);
//	}
//
//	@Test
//	public void testGetDevicePong() {
//		Object o = CommandProcessor.getCommandProcessorInstance().getDevicePong();
//		assertNotNull(o);
//	}

	@Test
	public void testGetCarrierManager() {
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		assertNotNull(mSink.getCarrierManager());
	}

	@Test
	public void testSetCarrierManager() {
		
		JsonServiceWrapperMessage mockMessage = mock(JsonServiceWrapperMessage.class);
		CarrierUpdateRequestMessage mockRequest = mock(CarrierUpdateRequestMessage.class);
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		MessageSinkInterface mSink = CommandProcessor.getInstance();
		mSink.setCarrierManager(mockManager);
		
		try {
			when(mockMessage.getContent()).thenReturn(mockRequest);
			when(mockRequest.getTargetOp()).thenReturn(
					CarrierUpdateOperations.SAVE);
			
			mSink.put(this, mockMessage);
			verify(mockManager).saveCarrier(any(Carrier.class));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
