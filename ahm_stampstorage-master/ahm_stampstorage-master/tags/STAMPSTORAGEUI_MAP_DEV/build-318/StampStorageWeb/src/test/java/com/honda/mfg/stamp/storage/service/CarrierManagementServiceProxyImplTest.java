package com.honda.mfg.stamp.storage.service;

import com.honda.mfg.connection.messages.MessageRequest;
import com.honda.mfg.connection.processor.ConnectionDevice;
import com.honda.mfg.connection.processor.ConnectionMessageSeparators;
import com.honda.mfg.connection.processor.messages.ConnectionRequest;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
import com.honda.mfg.stamp.conveyor.messages.BulkStatusUpdateRequestMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateOperations;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateRequestMessage;
import com.honda.mfg.stamp.conveyor.messages.EmptyCarrierReleaseRequest;
import com.honda.mfg.stamp.conveyor.messages.JsonServiceMessageTypes;
import com.honda.mfg.stamp.conveyor.messages.JsonServiceWrapperMessage;
import com.honda.mfg.stamp.conveyor.messages.LaneUpdateRequestMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.messages.ReleaseCarriersRequestMessage;
import com.honda.mfg.stamp.conveyor.messages.ServiceRequestMessage;
import com.honda.mfg.stamp.conveyor.messages.StatusUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.UpdateLaneCarrierListRequestMessage;
import com.honda.mfg.stamp.storage.service.messagebuilders.MesMessageBuilder;
import com.honda.mfg.stamp.storage.service.messagebuilders.JSonResponseParser;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.SwingEventService;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;

/**
 * @author VCC44349
 * 2013-01-30
 * The tests in this class have the purpose of ensuring that a ServiceRequestMessage is sent
 * through the device wrapper to the service component.  Calls are made to the
 * CarrierManagementServiceProxy which creates the service request and wraps it in a
 * JsonServiceWrapperMessage before publishing it, which is then picked up over
 * EventBus by ServiceSendMessageProcessor.
 * {@link ServiceSendMessageProcessor}
 * {@link com.honda.mfg.stamp.storage.service.CarrierManagementServiceProxy}
 * {@link ServiceRequestMessage}
 * {@link JsonServiceWrapperMessage}
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class CarrierManagementServiceProxyImplTest {

    EventService e;
    int count = 0;

    @Before
    public void before() throws EventServiceExistsException {

        if (e != null && !(e instanceof SwingEventService)) {
            e = new SwingEventService();
            EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS,
                    e);
        }
    }

    @Test(timeout = 5000L)
    public void successfullySaveCarrier() {
    	
        loadCarrierMesTable();
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        Carrier c = getCarrier(1234);
        c.setDestination(new Stop(1201l));
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        service.saveCarrier(c);
        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

    @Test
    public void successfullyStoreReworkCarrier() {
        loadCarrierMesTable();
        loadStopTable();
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        service.storeReworkCarrier(getCarrier(1234));
        //verify(storage, times(1)).store(Matchers.<Carrier>any());
    }


    @Test(timeout = 5000L)
    public void successfullySaveCarriersInToLane() {
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();

        List<Integer> carrierNumbers = new ArrayList<Integer>();
        carrierNumbers.add(1234);

        Stop laneStop = new Stop();
        laneStop.setName("ST12-01");
        laneStop.setId(1201L);
        service.saveCarriersInToRow(carrierNumbers, laneStop);
        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

    @Test(timeout = 5000L)
    public void successfullyReleaseMultipleCarriers() {
        loadCarrierMesTable();
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        StorageRow lane = new StorageRow(1L, "Row1", 10,1);
        lane.setStop(new Stop(1201L));
        lane.store(new Carrier(10));
        lane.store(new Carrier(11));
        lane.store(new Carrier(12));
        Carrier c = new Carrier(13);
        c.setCurrentLocation(new Stop(803));
        lane.store(c);
        lanes.add(lane);
        service.releaseCarriers(lane, 6, new Stop(500), "user");
        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

  //  @Test(timeout = 5000L)
    public void successfullyAddCarrierToRow() {
        loadCarrierMesTable();
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        StorageRow lane = new StorageRow(1L, "Row1", 10,1);
        lane.setStop(new Stop(1201L));
        lane.store(new Carrier(10));
        lane.store(new Carrier(11));
        lane.store(new Carrier(12));
        Carrier c = new Carrier(13);
        c.setCurrentLocation(new Stop(803));
        lane.store(c);
        lanes.add(lane);

        service.addCarrierToRow(new Integer(9), new Integer(2), new Stop(1201L));

        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());

    }

    @Test(timeout = 5000L)
    public void successfullyRemoveCarrierFromRow() {
        loadStopTable();
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        StorageRow lane = new StorageRow(1L, "Row1", 10,1);
        lane.setStop(new Stop(1201L));
        lane.store(new Carrier(10));
        lane.store(new Carrier(11));
        lane.store(new Carrier(12));
        Carrier c = new Carrier(13);
        c.setCurrentLocation(new Stop(803));
        lane.store(c);
        lanes.add(lane);
        service.removeCarrierFromRow(13);

        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }
    
    @Test(timeout = 5000L)
    public void successfullyRecalculateDestination() {
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        service.recalculateCarrierDestination(getCarrier(1234));
        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

    @Test(timeout = 5000L)
    public void successFullySendBulkCarrierStatusUpdate() {
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();

        List<Carrier> carrierList = new ArrayList<Carrier>();

        for (int i = 0; i < 12; i++) {
            carrierList.add(getCarrier(i));
        }

        service.sendBulkCarrierStatusUpdate(carrierList, CarrierStatus.SHIPPABLE, "user");

        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

    @Test(timeout = 5000L)
    public void releaseEmptyCarriersFromRowsOnDemand() {
        loadCarrierMesTable();
        loadStopTable();
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        List<StorageRow> lanes = new ArrayList<StorageRow>();
        StorageRow lane = new StorageRow(1L, "Row1", 10,1);
        lane.setStop(new Stop(1201L));
        lane.store(new Carrier(10));
        lane.store(new Carrier(11));
        lane.store(new Carrier(12));
        Carrier c = new Carrier(13);
        c.setCurrentLocation(new Stop(803));
        lane.store(c);
        lanes.add(lane);
        service.releaseEmptyCarriersFromRows(StorageArea.C_HIGH, true, "user");
        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

    @Test(timeout = 5000L)
    public void releaseEmptyCarriersFromRowsOnDemandNoEmptyDeliveryStopFound() {
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        service.releaseEmptyCarriersFromRows(StorageArea.C_HIGH, true, "user");
        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

    @Test(timeout = 5000L)
    public void releaseEmptyCarriersFromRowsOnDemandNoRowFound() {
        loadStopTable();
        ConnectionDevice mesDev = mock(ConnectionDevice.class);
        ServiceSendMessageProcessor sender = new ServiceSendMessageProcessor(mesDev);
        //constructor already call process()
        //AnnotationProcessor.process(sender.getClass());
        CarrierManagementServiceProxy service = new CarrierManagementServiceProxyImpl();
        service.releaseEmptyCarriersFromRows(StorageArea.C_HIGH, true, "user");
        verify(mesDev, timeout(1000)).sendMessage(Matchers.<ConnectionMessage>any());
        AnnotationProcessor.unprocess(sender.getClass());
    }

    Carrier getCarrier(Integer carrierNumber) {
        Timestamp productionRunTimestamp = new Timestamp(System.currentTimeMillis());
        Integer prodRunNo = 108;
        Stop currentLocation = new Stop(513);
        Stop destination = new Stop(513);
        CarrierStatus carrierStatus = CarrierStatus.ON_HOLD;
        Carrier carrier = new Carrier(1, productionRunTimestamp, prodRunNo, currentLocation, destination, carrierStatus, carrierNumber, null);

        return carrier;
    }
    
   // @Test
	public void testCarrierUpdateRequestMessage()  {
		
		Carrier c = getCarrier(7);
        JSonResponseParser jSonParser = new JSonResponseParser();
        ServiceRequestMessage carrierUpdateReqMsg = new CarrierUpdateRequestMessage(c,CarrierUpdateOperations.SAVE);
        JsonServiceWrapperMessage jsonWrapperIn = new JsonServiceWrapperMessage(carrierUpdateReqMsg, JsonServiceMessageTypes.CarrierUpdateReqeustMessage);
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(jsonWrapperIn, JsonServiceWrapperMessage.class);
        
        MessageRequest msgRequest = new ConnectionRequest(mesMsg, ConnectionMessageSeparators.DEFAULT.getSeparator());
        String request = msgRequest.getMessageRequest();
		Message thisMessage = (Message) jSonParser.parse(
				request, JsonServiceWrapperMessage.class); //CarrierUpdateRequestMessage.class);
		JsonServiceWrapperMessage jsonWrapperOut = (JsonServiceWrapperMessage)thisMessage;
		ServiceRequestMessage serviceRequest = jsonWrapperOut.getContent();
		
        CarrierUpdateOperations cOp = serviceRequest.getTargetOp();
        assertEquals(CarrierUpdateOperations.SAVE, cOp);
		CarrierUpdateRequestMessage req = (CarrierUpdateRequestMessage)serviceRequest;
		Carrier c1 = req.getCarrier();
		assertEquals(7, c1.getCarrierNumber().intValue());

	}

  //  @Test
	public void testUpdateLaneCarrierListRequestMessage()  {
		
    	loadStopTable();
		Carrier c = getCarrier(7);
        JSonResponseParser jSonParser = new JSonResponseParser();
        List<Integer> cList = Arrays.asList(101, 102, 103);
        ServiceRequestMessage updateLaneListReqMsg = new UpdateLaneCarrierListRequestMessage(cList, 1201L, CarrierUpdateOperations.ROW_RESEQUENCE);
        JsonServiceWrapperMessage jsonWrapperIn = new JsonServiceWrapperMessage(updateLaneListReqMsg, JsonServiceMessageTypes.UpdateLaneCarrierListMessage);
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(jsonWrapperIn, JsonServiceWrapperMessage.class);
        
        MessageRequest msgRequest = new ConnectionRequest(mesMsg, ConnectionMessageSeparators.DEFAULT.getSeparator());
        String request = msgRequest.getMessageRequest();
		Message thisMessage = (Message) jSonParser.parse(
				request, JsonServiceWrapperMessage.class); //CarrierUpdateRequestMessage.class);
		JsonServiceWrapperMessage jsonWrapperOut = (JsonServiceWrapperMessage)thisMessage;
		ServiceRequestMessage serviceRequest = jsonWrapperOut.getContent();
		
        CarrierUpdateOperations cOp = serviceRequest.getTargetOp();
        assertEquals(CarrierUpdateOperations.ROW_RESEQUENCE, cOp);
        UpdateLaneCarrierListRequestMessage req = (UpdateLaneCarrierListRequestMessage)serviceRequest;
		List<Integer> cListOut = req.getCarrierNumberList();
		java.util.Iterator<Integer> litOut = cListOut.iterator();
		for(Integer i : cList)  {
			assertEquals(i, litOut.next());
		}
		assertEquals(1201L, req.getLaneStop().getId().longValue());

	}

   // @Test
	public void testLaneUpdateRequestMessage()  {
		
		Carrier c = getCarrier(7);
        JSonResponseParser jSonParser = new JSonResponseParser();
        ServiceRequestMessage laneUpdateReqMsg = new LaneUpdateRequestMessage(7, CarrierUpdateOperations.REMOVE_FROM_ROW);
        JsonServiceWrapperMessage jsonWrapperIn = new JsonServiceWrapperMessage(laneUpdateReqMsg, JsonServiceMessageTypes.LaneUpdateRequestMessage);
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(jsonWrapperIn, JsonServiceWrapperMessage.class);
        
        MessageRequest msgRequest = new ConnectionRequest(mesMsg, ConnectionMessageSeparators.DEFAULT.getSeparator());
        String request = msgRequest.getMessageRequest();
		Message thisMessage = (Message) jSonParser.parse(
				request, JsonServiceWrapperMessage.class); //LaneUpdateRequestMessage.class);
		JsonServiceWrapperMessage jsonWrapperOut = (JsonServiceWrapperMessage)thisMessage;
		ServiceRequestMessage serviceRequest = jsonWrapperOut.getContent();
		
        CarrierUpdateOperations cOp = serviceRequest.getTargetOp();
        assertEquals(CarrierUpdateOperations.REMOVE_FROM_ROW, cOp);
        LaneUpdateRequestMessage req = (LaneUpdateRequestMessage)serviceRequest;
		assertEquals(7, req.getCarrierNumber().intValue());

	}

    //@Test
	public void testLaneUpdateRequestMessage_AddCarrier()  {
		
    	loadStopTable();
		Carrier c = getCarrier(7);
        JSonResponseParser jSonParser = new JSonResponseParser();
        ServiceRequestMessage laneUpdateReqMsg = new LaneUpdateRequestMessage(7, 3, 1201L, CarrierUpdateOperations.ADD_TO_ROW);
        JsonServiceWrapperMessage jsonWrapperIn = new JsonServiceWrapperMessage(laneUpdateReqMsg, JsonServiceMessageTypes.LaneUpdateRequestMessage);
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(jsonWrapperIn, JsonServiceWrapperMessage.class);
        
        MessageRequest msgRequest = new ConnectionRequest(mesMsg, ConnectionMessageSeparators.DEFAULT.getSeparator());
        String request = msgRequest.getMessageRequest();
		Message thisMessage = (Message) jSonParser.parse(
				request, JsonServiceWrapperMessage.class); //LaneUpdateRequestMessage.class);
		JsonServiceWrapperMessage jsonWrapperOut = (JsonServiceWrapperMessage)thisMessage;
		ServiceRequestMessage serviceRequest = jsonWrapperOut.getContent();
		
        CarrierUpdateOperations cOp = serviceRequest.getTargetOp();
        assertEquals(CarrierUpdateOperations.ADD_TO_ROW, cOp);
        LaneUpdateRequestMessage req = (LaneUpdateRequestMessage)serviceRequest;
		assertEquals(7, req.getCarrierNumber().intValue());
		assertEquals(1201L, req.getLaneStop().getId().longValue());
		assertEquals(3, req.getPosition().intValue());

	}

  //  @Test
	public void testReleaseCarrierRequestMessage()  {
		
    	loadStopTable();
        JSonResponseParser jSonParser = new JSonResponseParser();
        ServiceRequestMessage releaseCarriersReq = new ReleaseCarriersRequestMessage(
				1201L, 4, 5200L,
				CarrierUpdateOperations.RELEASE_CARRIERS);
        JsonServiceWrapperMessage jsonWrapperIn = new JsonServiceWrapperMessage(releaseCarriersReq, JsonServiceMessageTypes.ReleaseCarriersRequest);
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(jsonWrapperIn, JsonServiceWrapperMessage.class);
        
        MessageRequest msgRequest = new ConnectionRequest(mesMsg, ConnectionMessageSeparators.DEFAULT.getSeparator());
        String request = msgRequest.getMessageRequest();
		Message thisMessage = (Message) jSonParser.parse(
				request, JsonServiceWrapperMessage.class); //LaneUpdateRequestMessage.class);
		JsonServiceWrapperMessage jsonWrapperOut = (JsonServiceWrapperMessage)thisMessage;
		ServiceRequestMessage serviceRequest = jsonWrapperOut.getContent();
		
        CarrierUpdateOperations cOp = serviceRequest.getTargetOp();
        assertEquals(CarrierUpdateOperations.RELEASE_CARRIERS, cOp);
        ReleaseCarriersRequestMessage req = (ReleaseCarriersRequestMessage)serviceRequest;
		assertEquals(1201L, req.getLaneStopId().longValue());
		assertEquals(5200L, req.getDestinationStopId().longValue());
		assertEquals(4, req.getReleaseCount().intValue());

	}
    
  //  @Test
	public void testBulkStatusUpdateRequestMessage()  {
		
        JSonResponseParser jSonParser = new JSonResponseParser();
        List<Integer> cList = Arrays.asList(201, 202, 203);
        ServiceRequestMessage bulkReq = new BulkStatusUpdateRequestMessage(cList, CarrierStatus.ON_HOLD, "XXX", CarrierUpdateOperations.GROUP_HOLD);
        JsonServiceWrapperMessage jsonWrapperIn = new JsonServiceWrapperMessage(bulkReq, JsonServiceMessageTypes.BulkStatusUpdateRequest);
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(jsonWrapperIn, JsonServiceWrapperMessage.class);
        
        MessageRequest msgRequest = new ConnectionRequest(mesMsg, ConnectionMessageSeparators.DEFAULT.getSeparator());
        String request = msgRequest.getMessageRequest();
		Message thisMessage = (Message) jSonParser.parse(
				request, JsonServiceWrapperMessage.class); //CarrierUpdateRequestMessage.class);
		JsonServiceWrapperMessage jsonWrapperOut = (JsonServiceWrapperMessage)thisMessage;
		ServiceRequestMessage serviceRequest = jsonWrapperOut.getContent();
		
        CarrierUpdateOperations cOp = serviceRequest.getTargetOp();
        assertEquals(CarrierUpdateOperations.GROUP_HOLD, cOp);
        BulkStatusUpdateRequestMessage req = (BulkStatusUpdateRequestMessage)serviceRequest;
		List<Integer> cListOut = req.getCarrierNumberList();
		java.util.Iterator<Integer> litOut = cListOut.iterator();
		for(Integer i : cList)  {
			assertEquals(i, litOut.next());
		}
		assertEquals(CarrierStatus.ON_HOLD, req.getNewStatus());
		assertEquals("XXX", req.getUser());

	}

    //@Test
	public void testEmptyCarrierReleaseRequestMessage()  {
		
        JSonResponseParser jSonParser = new JSonResponseParser();
        ServiceRequestMessage releaseEmptyFromRowsReq = new EmptyCarrierReleaseRequest(StorageArea.C_HIGH, true, CarrierUpdateOperations.RELEASE_EMPTIES_FROM_ROW);
        JsonServiceWrapperMessage jsonWrapperIn = new JsonServiceWrapperMessage(releaseEmptyFromRowsReq, JsonServiceMessageTypes.ReleaseEmptiesFromRowRequest);
        MesMessageBuilder mesMessageBuilder = new MesMessageBuilder();
        GeneralMessage mesMsg = mesMessageBuilder.buildMesMessage(jsonWrapperIn, JsonServiceWrapperMessage.class);
        
        MessageRequest msgRequest = new ConnectionRequest(mesMsg, ConnectionMessageSeparators.DEFAULT.getSeparator());
        String request = msgRequest.getMessageRequest();
		Message thisMessage = (Message) jSonParser.parse(
				request, JsonServiceWrapperMessage.class); //EmptyCarrierReleaseRequest.class);
		JsonServiceWrapperMessage jsonWrapperOut = (JsonServiceWrapperMessage)thisMessage;
		ServiceRequestMessage serviceRequest = jsonWrapperOut.getContent();
		
        CarrierUpdateOperations cOp = serviceRequest.getTargetOp();
        assertEquals(CarrierUpdateOperations.RELEASE_EMPTIES_FROM_ROW, cOp);
        EmptyCarrierReleaseRequest req = (EmptyCarrierReleaseRequest)serviceRequest;
		assertEquals(StorageArea.C_HIGH, req.getWhichArea());
		assertTrue(req.isReleaseManager());
	}

   void loadCarrierMesTable() {
        CarrierMes carrier = new CarrierMes();
        carrier.setCarrierNumber(10);
        carrier.setDieNumber(101);
        carrier.setQuantity(1);
        carrier.setCurrentLocation(1201L);
        carrier.setDestination(1201L);
        carrier.setBuffer(new Integer(1));
        carrier.setOriginationLocation(0);
        carrier.setStatus(0);
        carrier.setProductionRunNumber(100);

        CarrierMes carrier1 = new CarrierMes();
        carrier1.setCarrierNumber(11);
        carrier1.setDieNumber(999);
        carrier1.setQuantity(0);
        carrier1.setCurrentLocation(1201L);
        carrier1.setDestination(1201L);
        carrier1.setBuffer(new Integer(0));
        carrier1.setProductionRunNumber(100);

        CarrierMes carrier2 = new CarrierMes();
        carrier2.setCarrierNumber(12);
        carrier2.setDieNumber(999);
        carrier2.setQuantity(0);
        carrier2.setCurrentLocation(1201L);
        carrier2.setDestination(1201L);
        carrier2.setBuffer(new Integer(0));
        carrier2.setProductionRunNumber(100);

        CarrierMes carrier3 = new CarrierMes();
        carrier3.setCarrierNumber(13);
        carrier3.setDieNumber(999);
        carrier3.setQuantity(0);
        carrier3.setCurrentLocation(803L);
        carrier3.setDestination(1201L);
        carrier3.setBuffer(new Integer(0));

        CarrierMes carrier4 = new CarrierMes();
        carrier4.setCarrierNumber(1234);
        carrier4.setDieNumber(999);
        carrier4.setQuantity(0);
        carrier4.setCurrentLocation(1201L);
        carrier4.setDestination(1201L);
        carrier4.setBuffer(new Integer(0));

        carrier.persist();
        carrier1.persist();
        carrier2.persist();
        carrier3.persist();
        carrier4.persist();
    }
    void loadStopTable() {
        Stop stop = new Stop();
        stop.setId(1201l);
        stop.setStopArea(StopArea.ROW);
        stop.setStopType(StopType.NO_ACTION);
        stop.setName("ST 12-1");

        stop.persist();

        Stop stop1 = new Stop();
        stop1.setId(0l);
        stop1.setStopArea(StopArea.UNDEFINED);
        stop1.setStopType(StopType.MAINTENANCE);
        stop1.setName("ST 0");

        stop1.persist();

        Stop stop2 = new Stop();
        stop2.setId(500l);
        stop2.setStopArea(StopArea.WELD_LINE_1);
        stop2.setStopType(StopType.FULL_CARRIER_DELIVERY);
        stop2.setName("ST 0");

        stop2.persist();

        Stop stop3 = new Stop();
        stop3.setId(5200l);
        stop3.setStopArea(StopArea.OLD_WELD_LINE);
        stop3.setStopType(StopType.EMPTY_CARRIER_DELIVERY);
        stop3.setName("ST 0");

        stop3.persist();
    }
}
