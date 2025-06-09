package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.messages.Transaction;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.service.FrameShipConfirmationService;
import com.honda.ahm.lc.service.InProcessProductService;
import com.honda.ahm.lc.service.ParkChangeService;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.service.ShippingTransactionService;
import com.honda.ahm.lc.util.PropertyUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShipmentConfirmMessageHandlerTest extends TestCase {

    @Mock
    private ShippingStatusService shippingStatusService;

    @Mock
    private PropertyUtil propertyUtil;

    @Mock
    private ShippingTransactionService shippingTransactionService;

    @Mock
    private ParkChangeService parkChangeService;

    @Mock
    private InProcessProductService inProcessProductService;

    @Mock
    private FrameShipConfirmationService frameShipConfirmationService;

    @InjectMocks
    private ShipmentConfirmMessageHandler shipmentConfirmMessageHandler;


    @Test
    public void testHandlerWhenShippingStatusIsAH_SHIP() {

        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_SHIP.getType());
        transaction.setLine_id("1");

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(vin);
        statusVehicle.setTimestamp("2023-10-02T12:00:00");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(vin);
        shippingStatus.setStatus(StatusEnum.AH_SHIP.getStatus());
        when(shippingStatusService.getGalcUrl(vin, "1")).thenReturn("testGalc");
        Mockito.when(shippingStatusService.findByProductId("testGalc", vin)).thenReturn(shippingStatus);
        Mockito.when(shippingStatusService.saveShippingStatus(null, shippingStatus)).thenReturn(shippingStatus);
        Mockito.doNothing().when(shippingTransactionService).deleteShippingTransaction("testGalc", vin);
        Mockito.doNothing().when(inProcessProductService).deleteInProcess("testGalc", vin);
        Mockito.doNothing().when(parkChangeService).deleteParkChange("testGalc", vin);
        when(propertyUtil.getProcessPoint(StatusEnum.AH_SHIP.getType())).thenReturn(processPointId);
        Mockito.doNothing().when(frameShipConfirmationService).processFrameShipConfirmation("testGalc", vin,processPointId,"231002","120000", StatusEnum.AH_SHIP);
        shipmentConfirmMessageHandler.handle(statusMessage,StatusEnum.AH_SHIP);
        Mockito.verify(frameShipConfirmationService,times(1)).processFrameShipConfirmation("testGalc",vin,processPointId,"231002","120000", StatusEnum.AH_SHIP);
        Mockito.verify(shippingStatusService,times(1)).trackProduct("testGalc",propertyUtil.getProcessPoint(StatusEnum.AH_SHIP.getType()), vin);
    }

    @Test
    public void testHandlerWhenShippingStatusIsNull(){
        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_SHIP.getType());
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(vin);
        statusVehicle.setTimestamp("2023-10-02T12:00:00");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);
        when(shippingStatusService.getGalcUrl(vin, "1")).thenReturn("testGalc");
       shipmentConfirmMessageHandler.handle(statusMessage,StatusEnum.AH_SHIP);
        Mockito.verify(shippingStatusService,times(1)).findByProductId("testGalc", "1HGCY2F79RA058411");
        Mockito.verify(frameShipConfirmationService,times(0)).processFrameShipConfirmation("testGalc",vin,processPointId,"231002","120000", StatusEnum.AH_SHIP);
        Mockito.verify(shippingStatusService,times(0)).trackProduct("testGalc",propertyUtil.getProcessPoint(StatusEnum.AH_SHIP.getType()), vin);
    }

    @Test
    public void testHandlerWhenShippingStatusIsGreaterThanAH_SHIP() {

        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_PCHG.getType());
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(vin);
        statusVehicle.setTimestamp("2023-10-02T12:00:00");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(vin);
        shippingStatus.setStatus(StatusEnum.AH_PCHG.getStatus());

        Mockito.when(shippingStatusService.findByProductId(null, vin)).thenReturn(shippingStatus);
        shipmentConfirmMessageHandler.handle(statusMessage,StatusEnum.AH_SHIP);
        Mockito.verify(frameShipConfirmationService,times(0)).processFrameShipConfirmation(null,vin,processPointId,"231002","120000", StatusEnum.AH_PCHG);
        Mockito.verify(shippingStatusService,times(0)).trackProduct(null,propertyUtil.getProcessPoint(StatusEnum.AH_PCHG.getType()), vin);
    }

    @Test
    public void testHandlerWhenShippingStatusIsINIT() {

        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.INIT.getType());
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(vin);
        statusVehicle.setTimestamp("2023-10-02T12:00:00");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(vin);
        shippingStatus.setStatus(StatusEnum.INIT.getStatus());

        Mockito.when(shippingStatusService.findByProductId(null, vin)).thenReturn(shippingStatus);
        shipmentConfirmMessageHandler.handle(statusMessage,StatusEnum.AH_SHIP);
        Mockito.verify(frameShipConfirmationService,times(0)).processFrameShipConfirmation(null,vin,processPointId,"231002","120000", StatusEnum.INIT);
        Mockito.verify(shippingStatusService,times(0)).trackProduct(null,propertyUtil.getProcessPoint(StatusEnum.INIT.getType()), vin);
    }



}
