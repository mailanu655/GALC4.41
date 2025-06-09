package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.messages.Transaction;
import com.honda.ahm.lc.messages.Vehicle;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.util.PropertyUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AhReceiveMessageHandlerTest{

    @Mock
    private ShippingStatusService shippingStatusService;

    @Mock
    private PropertyUtil propertyUtil;

    @InjectMocks
    private AhReceiveMessageHandler ahReceiveMessageHandler;
    
    

    @Test
    public void testHandlerWhenProductStatusIsVQ_SHIP(){
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.VQ_SHIP.getType());
        transaction.setLine_id("1");

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin("5FNYG2H71PB036908");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin("5FNYG2H71PB036908");
        shippingStatus.setStatus(StatusEnum.VQ_SHIP.getStatus());
        
        when(shippingStatusService.getGalcUrl("5FNYG2H71PB036908", "1")).thenReturn("testGalc");
        when(shippingStatusService.findByProductId("testGalc", "5FNYG2H71PB036908")).thenReturn(shippingStatus);

        // Invoke the AH_RCVD message handler
        ahReceiveMessageHandler.handle(statusMessage,StatusEnum.AH_RCVD);

        // verify track is getting invoke for this VIN status update
        Mockito.verify(shippingStatusService,times(1)).trackProduct("testGalc", propertyUtil.getProcessPoint(StatusEnum.AH_RCVD.getType()), "5FNYG2H71PB036908");
    }

    @Test
    public void testHandlerWhenProductStatusIsNotVQ_SHIP(){
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_SHIP.getType());

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin("5FNYG2H71PB036908");

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin("5FNYG2H71PB036908");
        shippingStatus.setStatus(StatusEnum.AH_SHIP.getStatus());
        when(shippingStatusService.getGalcUrl("5FNYG2H71PB036908", "1")).thenReturn("testGalc");
        when(shippingStatusService.findByProductId("testGalc", "5FNYG2H71PB036908")).thenReturn(shippingStatus);

        // Invoke the AH_RCVD message handler
        ahReceiveMessageHandler.handle(statusMessage,StatusEnum.AH_SHIP);

        // verify track is getting invoke for this VIN status update
        Mockito.verify(shippingStatusService,times(0)).trackProduct("testGalc", propertyUtil.getProcessPoint(StatusEnum.AH_RCVD.getType()), "5FNYG2H71PB036908");
    }

    @Test
    public void testHandlerWhenProductStatusIsNull(){
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.VQ_SHIP.getType());

        StatusVehicle statusVehicle = new StatusVehicle();

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ahReceiveMessageHandler.handle(statusMessage,StatusEnum.AH_RCVD);

        // verify track is getting invoke for this VIN status update
        Mockito.verify(shippingStatusService,times(0)).trackProduct("testGalc", propertyUtil.getProcessPoint(StatusEnum.AH_RCVD.getType()), "5FNYG2H71PB036908");
    }


}