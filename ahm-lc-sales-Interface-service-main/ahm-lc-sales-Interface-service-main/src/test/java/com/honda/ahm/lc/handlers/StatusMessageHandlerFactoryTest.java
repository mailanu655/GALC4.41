package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.messages.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatusMessageHandlerFactoryTest {

    @Mock
    private IStatusMessageHandler ahReceiveMessageHandler;

    @Mock
    private IStatusMessageHandler ahParkingChangeMessageHandler;

    @Mock
    private IStatusMessageHandler dealerAssignMessageHandler;

    @Mock
    private IStatusMessageHandler factoryReturnMessageHandler;

    @Mock
    private IStatusMessageHandler shipmentConfirmMessageHandler;


    @InjectMocks
    private StatusMessageHandlerFactory statusMessageHandlerFactory;


    @Test
    public void handleMessageTestWhenStatusIsAH_RCVD() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code("AH-RCVD");
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        StatusEnum statusEnum = StatusEnum.getStatusByType("AH-RCVD");

        statusMessageHandlerFactory.handleMessage(statusMessage);

        Mockito.verify(ahReceiveMessageHandler, Mockito.times(1)).handle(statusMessage,statusEnum);
    }

    @Test
    public void handleMessageTestWhenStatusIsAH_PCHG() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code("AH-PCHG");
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        statusMessageHandlerFactory.handleMessage(statusMessage);

        Mockito.verify(ahParkingChangeMessageHandler, Mockito.times(1)).handle(statusMessage,StatusEnum.AH_PCHG);
    }

    @Test
    public void handleMessageTestWhenStatusIsDLR_ASGN() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code("DLR-ASGN");
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        statusMessageHandlerFactory.handleMessage(statusMessage);

        Mockito.verify(dealerAssignMessageHandler, Mockito.times(1)).handle(statusMessage,StatusEnum.DLR_ASGN);
    }

    @Test
    public void handleMessageTestWhenStatusIsAH_RTN() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code("AH-RTN");
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        statusMessageHandlerFactory.handleMessage(statusMessage);

        Mockito.verify(factoryReturnMessageHandler, Mockito.times(1)).handle(statusMessage,StatusEnum.AH_RTN);
    }

    @Test
    public void handleMessageTestWhenStatusIsAH_SHIP() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code("AH-SHIP");
        transaction.setLine_id("1");
        
        StatusVehicle statusVehicle = new StatusVehicle();

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        statusMessageHandlerFactory.handleMessage(statusMessage);

        Mockito.verify(shipmentConfirmMessageHandler, Mockito.times(1)).handle(statusMessage,StatusEnum.AH_SHIP);
    }

}