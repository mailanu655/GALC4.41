package com.honda.ahm.lc.task;

import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.service.IQueueManagerService;
import com.honda.ahm.lc.util.PropertyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReceivingTransactionTaskTest {

    @Mock
    public IQueueManagerService queueManagerService;

    @Mock
    public PropertyUtil propertyUtil;

    @InjectMocks
    private ReceivingTransactionTask receivingTransactionTask;

    @Test
    public void testWhenMessageRecievedFromYmsQueue() {
        StatusMessage statusMessage = new StatusMessage();

        Mockito.when(queueManagerService.recv(propertyUtil.getSalesReceivingQueueName())).thenReturn(createAhReceivedStatusMessageString());

        receivingTransactionTask.execute();

        Mockito.verify(queueManagerService,Mockito.times(1)).recv(propertyUtil.getSalesReceivingQueueName());
    }

    @Test
    public void testWhenMessageNotRecievedFromYmsQueue(){
        StatusMessage statusMessage = new StatusMessage();

        Mockito.when(queueManagerService.recv(propertyUtil.getSalesReceivingQueueName())).thenReturn(null);

        receivingTransactionTask.execute();

        Mockito.verify(queueManagerService,Mockito.times(1)).recv(propertyUtil.getSalesReceivingQueueName());
    }


    private String createAhReceivedStatusMessageString() {
        return /*"{ \"transaction\": { \"destination_environment\": \"TEST\", \"destination_site\": \"GALC\", " +
                "\"plant_id\": \"A\", \"transaction_code\": \"AH-RCVD\", \"description\": \"AH received\", " +
                "\"transaction_timestamp\": \"2023-10-02T12:00:00\" }, \"vehicle\": { \"flag\": \"\", " +
                "\"vin\": \"1HGCY2F83RA046588\", \"ship_date\": \"2023-12-23\", \"parking_bay\": \"A84\", " +
                "\"parking_space\": \"008\", \"control_number\": \"174231\", \"dealer_number\": \"208095\", " +
                "\"ship_type\": \"1\", \"odc_code\": \"\", \"timestamp\": \"2023-10-02T12:00:00\" } }";*/
        
        "{"+"\"TRANSACTION\":{\"DESTINATION_ENVIRONMENT\":\"TEST\",\"DESTINATION_SITE\":\"GALC\",\"PLANT_ID\":\"B\",\"LINE_ID\":NULL,\"TRANSACTION_CODE\":\"AH-PCHG\","
		+ "\"DESCRIPTION\":\"PARK CHANGE\",\"TRANSACTION_TIMESTAMP\":\"2024-05-29T19:54:28\"},\"VEHICLE\":{\"FLAG\":NULL,\"VIN\":\"5FPYK3650PB900200\",\"SHIP_DATE\":NULL,"
		+ "\"PARKING_BAY\":\"002\",\"PARKING_SPACE\":\"102\",\"CONTROL_NUMBER\":0,\"DEALER_NUMBER\":NULL,\"SHIP_TYPE\":NULL,\"TIMESTAMP\":\"2024-05-29T19:54:28\"}}";

    }

}