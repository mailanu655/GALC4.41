package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.messages.Transaction;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.service.FrameShipConfirmationService;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.util.PropertyUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DealerAssignMessageHandlerTest extends TestCase {

    @Mock
    private ShippingStatusService shippingStatusService;

    @Mock
    private PropertyUtil propertyUtil;

    @Mock
    private FrameShipConfirmationService frameShipConfirmationService;

    @InjectMocks
    private DealerAssignMessageHandler dealerAssignMessageHandler;


    @Test
    public void testHandlerWhenShippingStatusIsDLR_ASGN() {
        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.DLR_ASGN.getType());
        transaction.setLine_id("1");

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(vin);
        statusVehicle.setTimestamp("2023-10-02T12:00:00");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(vin);
        shippingStatus.setStatus(StatusEnum.DLR_ASGN.getStatus());
        when(shippingStatusService.getGalcUrl(vin, "1")).thenReturn("testGalc");
        Mockito.when(shippingStatusService.findByProductId("testGalc", vin)).thenReturn(shippingStatus);
        Mockito.when(propertyUtil.getProcessPoint(StatusEnum.DLR_ASGN.getType())).thenReturn(processPointId);
        Mockito.doNothing().when(frameShipConfirmationService).processFrameShipConfirmation("testGalc", vin,processPointId,"231002","120000", StatusEnum.DLR_ASGN);
        Mockito.when(shippingStatusService.saveShippingStatus("testGalc", shippingStatus)).thenReturn(shippingStatus);

        dealerAssignMessageHandler.handle(statusMessage,StatusEnum.DLR_ASGN);

        Mockito.verify(frameShipConfirmationService,times(1)).processFrameShipConfirmation("testGalc",vin,processPointId,"231002","120000", StatusEnum.DLR_ASGN);
        Mockito.verify(shippingStatusService,times(1)).trackProduct("testGalc",propertyUtil.getProcessPoint(StatusEnum.DLR_ASGN.getType()), vin);
    }

    @Test
    public void testHandlerWhenShippingStatusIsNull(){
        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.DLR_ASGN.getType());
        transaction.setLine_id("1");

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(vin);
        statusVehicle.setTimestamp("2023-10-02T12:00:00");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        when(shippingStatusService.getGalcUrl(vin, "1")).thenReturn("testGalc");
        
        dealerAssignMessageHandler.handle(statusMessage,StatusEnum.DLR_ASGN);
        
        Mockito.verify(shippingStatusService,times(1)).findByProductId("testGalc", "1HGCY2F79RA058411");
        Mockito.verify(frameShipConfirmationService,times(0)).processFrameShipConfirmation("testGalc",vin,processPointId,"231002","120000", StatusEnum.DLR_ASGN);
        Mockito.verify(shippingStatusService,times(0)).trackProduct("testGalc",propertyUtil.getProcessPoint(StatusEnum.DLR_ASGN.getType()), vin);
    }

    @Test
    public void testHandlerWhenShippingStatusIsGreaterThanDLR_ASGN() {

        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_PCHG.getType());

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
        dealerAssignMessageHandler.handle(statusMessage,StatusEnum.AH_PCHG);
        Mockito.verify(frameShipConfirmationService,times(0)).processFrameShipConfirmation(null,vin,processPointId,"231002","120000", StatusEnum.AH_PCHG);
        Mockito.verify(shippingStatusService,times(0)).trackProduct(null,propertyUtil.getProcessPoint(StatusEnum.AH_PCHG.getType()), vin);
    }

    @Test
    public void testHandlerWhenShippingStatusIs_INIT() {

        String processPointId = "AAH1DC1P00101";
        String vin= "1HGCY2F79RA058411";
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.INIT.getType());

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
        dealerAssignMessageHandler.handle(statusMessage,StatusEnum.INIT);
        Mockito.verify(frameShipConfirmationService,times(0)).processFrameShipConfirmation(null,vin,processPointId,"231002","120000", StatusEnum.INIT);
        Mockito.verify(shippingStatusService,times(0)).trackProduct(null,propertyUtil.getProcessPoint(StatusEnum.INIT.getType()), vin);
    }



}
