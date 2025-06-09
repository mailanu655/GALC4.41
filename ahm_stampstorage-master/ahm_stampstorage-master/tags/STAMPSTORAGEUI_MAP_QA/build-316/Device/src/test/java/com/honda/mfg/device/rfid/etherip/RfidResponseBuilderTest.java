package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.messages.MessageBase;
import com.honda.mfg.device.rfid.etherip.messages.*;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 24, 2010
 */
public class RfidResponseBuilderTest extends MessageBase {
    Map<Class, Integer> eventCounter = new HashMap<Class, Integer>();
    Map<Class, Object> eventMap = new Hashtable<Class, Object>();
    private final long PUBLISH_EVENT_WAIT_TIMEOUT = 1000L;
    private static final int INSTANCE_COUNTER = 5;
    private static final int NODE_ID = 6;
    private static final Date RESPONSE_DATE = new Date();

    public RfidResponseBuilderTest() {
        AnnotationProcessor.process(this);
    }

    @Before
    public void before() {
        eventCounter.clear();
        eventMap.clear();
    }

    @After
    public void after() {
        eventCounter.clear();
        eventMap.clear();
    }

    @Test
    public void buildsGoodRfidWriteTagResponse() {
        RfidWriteTagResponse expectedResponse = new RfidWriteTagResponse(INSTANCE_COUNTER, NODE_ID, RESPONSE_DATE);
        assertResponse(expectedResponse);
        EventBus.publish(expectedResponse);
        assertPublishedEvents(RfidWriteTagResponse.class, 1);
    }

    @Test
    public void buildsRfidClearPendingResponsesResponse() {
        RfidClearPendingResponsesResponse expectedResponse = new RfidClearPendingResponsesResponse(1, 2, RESPONSE_DATE);
        assertResponse(expectedResponse);
    }

    @Test
    public void buildsRfidGetControllerConfigurationResponse() {
        RfidGetControllerConfigurationResponse expectedResponse = new RfidGetControllerConfigurationResponse(3, 4, RESPONSE_DATE);
        assertResponse(expectedResponse);
    }

    @Test
    public void buildsRfidGetControllerInfoResponse() {
        RfidGetControllerInfoResponse expectedResponse = new RfidGetControllerInfoResponse(INSTANCE_COUNTER, NODE_ID, RESPONSE_DATE);
        assertResponse(expectedResponse);
    }

    @Test
    public void buildsGoodRfidReadTagIdResponse() {
        String expectedTagId = "FFFFFFFFFFFFFFFF";
        RfidReadTagIdResponse expectedResponse = new RfidReadTagIdResponse(INSTANCE_COUNTER, NODE_ID, RESPONSE_DATE, expectedTagId);
        RfidReadTagIdResponse actual = (RfidReadTagIdResponse) assertResponse(expectedResponse);
        Assert.assertEquals("Invalid tag id number!", expectedTagId, actual.getTagId());
    }

    @Test
    public void buildsBadRfidReadTagIdResponseWithEmptyTagList() {
        String tagId = null;
        RfidReadTagIdResponse expectedResponse = new RfidReadTagIdResponse(INSTANCE_COUNTER, NODE_ID, RESPONSE_DATE, tagId);
        assertResponse(expectedResponse);
    }

    @Test
    public void buildsGoodRfidReadTagResponse() {
        String tagValue = "abcxyz";
        RfidReadTagResponse expectedResponse = new RfidReadTagResponse(INSTANCE_COUNTER, NODE_ID, RESPONSE_DATE, tagValue);
        assertResponse(expectedResponse);
        Assert.assertEquals("RFID tag read value does not match.", tagValue, expectedResponse.getTagValue());
    }

    @Test
    public void buildsInvalidRfidReadTagResponseWithNullTagValue() {
        String tagValue = null;
        RfidReadTagResponse expectedResponse = new RfidReadTagResponse(INSTANCE_COUNTER, NODE_ID, RESPONSE_DATE, tagValue);
        assertResponse(expectedResponse);
        Assert.assertEquals("RFID tag read value does not match.", tagValue, expectedResponse.getTagValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildsInvalidRfidWriteTagResponse() {
        new RfidWriteTagResponse(-1, NODE_ID, RESPONSE_DATE);
    }

    @Test(expected = UnknownResponseException.class)
    public void throwsUnknownResponseExceptionAttemptingToProcessInvalidResponse() {
        RfidResponseBuilder builder = new RfidResponseBuilder(new RfidResponseParser());
        builder.buildRfidResponse("trash..");
    }

    private RfidResponse assertResponse(RfidResponse expected) {
        RfidResponse actual = build(expected);
        Assert.assertEquals("RFID response classes do not match.", expected.getClass(), actual.getClass());
        Assert.assertEquals("Instance counters do not match.", expected.getInstanceCounter(), actual.getInstanceCounter());
        Assert.assertEquals("Node IDs do not match.", expected.getNodeId(), actual.getNodeId());
        Assert.assertEquals("Response dates do not match.", expected.getResponseDate().toString(), actual.getResponseDate().toString());
        Assert.assertEquals("Invalid length.", expected.getLengthInWords(), actual.getLengthInWords());
        return actual;
    }

    private RfidResponse build(RfidResponse response) {
        RfidResponseBuilder responseBuilder = new RfidResponseBuilder(new RfidResponseParser());
        return responseBuilder.buildRfidResponse(response.getRfidResponse());
    }

    @EventSubscriber(eventClass = Object.class, referenceStrength = ReferenceStrength.STRONG)
    public void countEvents(Object event) {
        eventCounter.put(event.getClass(), eventCount(event.getClass()) + 1);
        eventMap.put(event.getClass(), event);
    }

    int eventCount(Class eventClass) {
        Integer events = eventCounter.get(eventClass);
        return events == null ? 0 : events;

    }

    void assertPublishedEvents(Class eventClass, int eventCount) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < PUBLISH_EVENT_WAIT_TIMEOUT) {
        }
        assertEquals("Did not publish expected number of events of class " + eventClass, eventCount, eventCount(eventClass));
    }
}
