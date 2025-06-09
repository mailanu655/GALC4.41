package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.InstalledPartStatus;
import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.messages.Transaction;
import com.honda.ahm.lc.model.InRepairArea;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.model.ShippingTransaction;
import com.honda.ahm.lc.service.InRepairAreaService;
import com.honda.ahm.lc.service.InstalledPartService;
import com.honda.ahm.lc.service.NaqDefectAndParkingService;
import com.honda.ahm.lc.service.ParkChangeService;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.service.ShippingTransactionService;
import com.honda.ahm.lc.util.PropertyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FactoryReturnMessageHandlerTest {

    @Mock
    private ShippingStatusService shippingStatusService;

    @Mock
    private PropertyUtil propertyUtil;

    @Mock
    private InstalledPartService installedPartService;

    @Mock
    private NaqDefectAndParkingService naqDefectAndParkingService;

    @Mock
    private ShippingTransactionService shippingTransactionService;

    @Mock
    private InRepairAreaService inRepairAreaService;

    @Mock
    private ParkChangeService parkChangeService;

    @InjectMocks
    private FactoryReturnMessageHandler factoryReturnMessageHandler;

    String PROCESS_POINT_ID = "AVQ1FR1P00101";
    String VIN = "1HGCY2F79RA058411";
    Character SENDED_FLAG = 'R';
    String RESPONSIBLE_DEPARTMENT = "VQ";
    String TIME_STAMP = "2023-10-02T12:00:00";
    String LINE_ID = "1";
    String GALC_URL = "testGalc";
    String NAQ_DEFECT_NAME = "AH_RETURN";
    String REPAIR_AREA = "VQ_FACTORY_RETURN";
    String PARTS_TO_BACKOUT = "INJ_MOLDID";


    @Test
    public void testHandlerWhenGalcIsBlank() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_RTN.getType());
        transaction.setLine_id(LINE_ID);

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(VIN);
        statusVehicle.setTimestamp(TIME_STAMP);

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(VIN);
        shippingStatus.setStatus(StatusEnum.AH_RTN.getStatus());
        shippingStatus.setActualTimestamp(TIME_STAMP);

        Mockito.when(shippingStatusService.getGalcUrl(VIN, LINE_ID)).thenReturn(null);

        factoryReturnMessageHandler.handle(statusMessage, StatusEnum.AH_RTN);

        Mockito.verify(shippingStatusService, times(1)).getGalcUrl(VIN, LINE_ID);
    }

    @Test
    public void testHandlerWhenShippingStatusIsAH_RTN() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_RTN.getType());
        transaction.setLine_id(LINE_ID);

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(VIN);
        statusVehicle.setTimestamp(TIME_STAMP);

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(VIN);
        shippingStatus.setStatus(StatusEnum.AH_RTN.getStatus());
        shippingStatus.setActualTimestamp(TIME_STAMP);

        when(shippingStatusService.getGalcUrl(VIN, LINE_ID)).thenReturn(GALC_URL);
        when(shippingStatusService.findByProductId(GALC_URL, VIN)).thenReturn(shippingStatus);

        factoryReturnMessageHandler.handle(statusMessage, StatusEnum.AH_RTN);

        Mockito.verify(shippingStatusService, times(1)).findByProductId(GALC_URL, VIN);
        Mockito.verify(shippingStatusService, times(1)).getGalcUrl(VIN, LINE_ID);

    }


    @Test
    public void testHandlerWhenShippingStatusIsNull() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_RTN.getType());
        transaction.setLine_id(LINE_ID);

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(VIN);
        statusVehicle.setTimestamp(TIME_STAMP);

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(VIN);
        shippingStatus.setStatus(StatusEnum.AH_RTN.getStatus());
        shippingStatus.setActualTimestamp(TIME_STAMP);

        Mockito.when(shippingStatusService.getGalcUrl(VIN, LINE_ID)).thenReturn(GALC_URL);
        Mockito.when(shippingStatusService.findByProductId(GALC_URL, VIN)).thenReturn(null);

        factoryReturnMessageHandler.handle(statusMessage, StatusEnum.AH_RTN);

        Mockito.verify(shippingStatusService, times(1)).getGalcUrl(VIN, LINE_ID);
        Mockito.verify(shippingStatusService, times(1)).findByProductId(GALC_URL, VIN);
    }

    @Test
    public void testHandlerWhenShippingStatusIsGreaterThanAH_SHIP() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.AH_PCHG.getType());
        transaction.setLine_id(LINE_ID);

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(VIN);
        statusVehicle.setTimestamp(TIME_STAMP);

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(VIN);
        shippingStatus.setStatus(StatusEnum.AH_PCHG.getStatus());

        Mockito.when(shippingStatusService.getGalcUrl(VIN, transaction.getLine_id())).thenReturn(GALC_URL);
        Mockito.when(shippingStatusService.findByProductId(GALC_URL, VIN)).thenReturn(shippingStatus);

        factoryReturnMessageHandler.handle(statusMessage, StatusEnum.AH_PCHG);
        Mockito.verify(shippingStatusService, times(1)).getGalcUrl(VIN, LINE_ID);
        Mockito.verify(shippingStatusService, times(1)).findByProductId(GALC_URL, VIN);
    }

    @Test
    public void testHandlerWhenShippingStatusIs_INIT() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.INIT.getType());
        transaction.setLine_id(LINE_ID);

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(VIN);
        statusVehicle.setTimestamp(TIME_STAMP);

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(VIN);
        shippingStatus.setStatus(StatusEnum.INIT.getStatus());

        Mockito.when(shippingStatusService.getGalcUrl(VIN, transaction.getLine_id())).thenReturn(GALC_URL);
        Mockito.when(shippingStatusService.findByProductId(GALC_URL, VIN)).thenReturn(shippingStatus);

        factoryReturnMessageHandler.handle(statusMessage, StatusEnum.INIT);
        Mockito.verify(shippingStatusService, times(1)).getGalcUrl(VIN, LINE_ID);
        Mockito.verify(shippingStatusService, times(1)).findByProductId(GALC_URL, VIN);
    }

    @Test
    public void testHandlerWhenShippingStatusIsNotAH_RTN() {
        Transaction transaction = new Transaction();
        transaction.setTransaction_code(StatusEnum.DLR_ASGN.getType());
        transaction.setLine_id(LINE_ID);

        StatusVehicle statusVehicle = new StatusVehicle();
        statusVehicle.setVin(VIN);
        statusVehicle.setTimestamp(TIME_STAMP);

        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setTransaction(transaction);
        statusMessage.setVehicle(statusVehicle);

        ShippingStatus shippingStatus = new ShippingStatus();
        shippingStatus.setVin(VIN);
        shippingStatus.setStatus(StatusEnum.DLR_ASGN.getStatus());

        ShippingTransaction shippingTransaction = new ShippingTransaction();
        shippingTransaction.setSendFlag(SENDED_FLAG);

        InRepairArea inRepairArea = new InRepairArea();
        inRepairArea.setProductId(VIN);
        inRepairArea.setRepairAreaName(REPAIR_AREA);
        inRepairArea.setActualTimestamp(TIME_STAMP);
        inRepairArea.setResponsibleDept(RESPONSIBLE_DEPARTMENT);

        when(shippingStatusService.getGalcUrl(VIN, transaction.getLine_id())).thenReturn(GALC_URL);
        when(shippingStatusService.findByProductId(GALC_URL, VIN)).thenReturn(shippingStatus);

        when(propertyUtil.getBackoutPartList()).thenReturn(Collections.singletonList(PARTS_TO_BACKOUT));
        Mockito.doNothing().when(installedPartService).updateInstalledPartStatus(GALC_URL, VIN, Collections.singletonList(PARTS_TO_BACKOUT), InstalledPartStatus.BLANK);

        when(shippingStatusService.saveShippingStatus(GALC_URL, shippingStatus)).thenReturn(shippingStatus);

        when(propertyUtil.getProcessPoint(StatusEnum.AH_RTN.getType())).thenReturn(PROCESS_POINT_ID);
        Mockito.doNothing().when(shippingStatusService).trackProduct(GALC_URL, PROCESS_POINT_ID, VIN);

        when(shippingTransactionService.findByProductId(GALC_URL, VIN)).thenReturn(shippingTransaction);

        when(shippingTransactionService.saveShippingTransaction(GALC_URL, shippingTransaction)).thenReturn(shippingTransaction);

        Mockito.doNothing().when(parkChangeService).deleteParkChange(GALC_URL, VIN);
//
//        Mockito.when(inRepairAreaService.saveinRepairArea("testGalc",inRepairArea)).thenReturn(inRepairArea);

        when(propertyUtil.getPropertyByDefectName()).thenReturn(NAQ_DEFECT_NAME);
        when(propertyUtil.updateNaqEnable()).thenReturn(true);
        when(propertyUtil.getPropertyByRepairName()).thenReturn(REPAIR_AREA);
        when(propertyUtil.getProcessPoint(StatusEnum.AH_RTN.getType())).thenReturn(PROCESS_POINT_ID);
        Mockito.doNothing().when(naqDefectAndParkingService).createNaqDefectAndParking(GALC_URL, VIN, NAQ_DEFECT_NAME, PROCESS_POINT_ID, REPAIR_AREA);

        factoryReturnMessageHandler.handle(statusMessage, StatusEnum.AH_RTN);

        Mockito.verify(shippingStatusService, times(1)).getGalcUrl(VIN, LINE_ID);
        Mockito.verify(shippingStatusService, times(2)).findByProductId(GALC_URL, VIN);
        Mockito.verify(installedPartService, times(1)).updateInstalledPartStatus(GALC_URL, VIN, Collections.singletonList(PARTS_TO_BACKOUT), InstalledPartStatus.BLANK);
        Mockito.verify(shippingStatusService, times(1)).trackProduct(GALC_URL, PROCESS_POINT_ID, VIN);
        Mockito.verify(shippingTransactionService, times(1)).findByProductId(GALC_URL, VIN);
        Mockito.verify(shippingTransactionService, times(1)).saveShippingTransaction(GALC_URL, shippingTransaction);
        Mockito.verify(parkChangeService, times(1)).deleteParkChange(GALC_URL, VIN);
        Mockito.verify(naqDefectAndParkingService, times(1)).createNaqDefectAndParking(GALC_URL, VIN, NAQ_DEFECT_NAME, PROCESS_POINT_ID, REPAIR_AREA);

    }
}
