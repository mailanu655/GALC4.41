package com.honda.mfg.stamp.conveyor.storageClient;

import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.connection.processor.*;
import com.honda.mfg.connection.watchdog.WatchdogAdapterInterface;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageImpl;
import com.honda.mfg.stamp.conveyor.manager.StoreInManager;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.DeviceReceiveMessageProcessor;
import com.honda.mfg.stamp.conveyor.processor.StorageReceiveMessageProcessor;
import com.honda.mfg.stamp.conveyor.rules.store_in.StoreInManagerImpl;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * User: vcc30690
 * Date: 5/12/11
 */
public class StorageClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(StorageClientTest.class);

    private final String GOOD_HOST = "localhost";
    private final int GOOD_PORT = 65535;
    private final int GOOD_CONNECT_TIMEOUT = 5;

    private TestableMesModuleServer server;
    private AdvancedConnection advancedConnection;
    private WatchdogAdapterInterface adapter;
    private DeviceReceiveMessageProcessor receiveMessageProcessor;
    private StorageReceiveMessageProcessor storageReceiveMessageProcessor;
    //    private DeviceSendMessageProcessor sendMessageProcessor;
    private Storage storage;


    private ConnectionDevice initializeDevice() {
        SocketFactory socketFactory = new SocketFactory(GOOD_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
        SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
        ConnectionProcessorPair processorPair = new StreamConnectionProcessorPair(streamPair);

        // Perform test
        ConnectionInitializer initializer = new ConnectionInitializer(processorPair, 5);
        while (!initializer.isInitialized()) {
        }

        ConnectionDevice mes = new BasicConnection(processorPair, initializer, 5);
//        DevicePing devicePing = new ConnectionPing(mes);
//        Watchdog watchDog = new Watchdog(streamPair);
//        adapter = new WatchdogAdapter(watchDog, devicePing, 5);
//
//        // 2)  Setup device client and start
//        advancedConnection = new AdvancedConnection(mes, adapter);


        return mes;
    }

    private void initializeStampStorageClientProcessors() {
        receiveMessageProcessor = new DeviceReceiveMessageProcessor();

        storageReceiveMessageProcessor = new StorageReceiveMessageProcessor(storage);
        // sendMessageProcessor = new DeviceSendMessageProcessor(advancedConnection);
    }

    private void initializeStorage() {
        StoreInManager storeInManager = new StoreInManagerImpl(null);
        storage = new StorageImpl(storeInManager, null, null, null);
    }

    // TODO Fix storageClientTest once storage state factory is functional
//    @Test
    public void successfullyReadFromMesModuleServer() throws IOException, URISyntaxException {
        // Pre-condition
        String fileName = "carrierArrivedAtStoreInSequence.properties";
        String stopFileName = "stopInformationMessage.properties";

        // 1)  Setup server for listening on a port with test file
        server = new TestableMesModuleServer(GOOD_PORT, fileName, stopFileName);
//        server.run();
        int hi = 1;

        initializeDevice();
        initializeStorage();
        initializeStampStorageClientProcessors();
        AnnotationProcessor.process(this);
        // 3)  Wait for test to be over
        while (server.performingTest() == true) {
        }
        LOG.info("#######################   server stopped");

//        while (storageReceiveMessageProcessor.getVerifyStorageState() == null) {
//        }
//
//        assertNotNull(storageReceiveMessageProcessor.getVerifyStorageState());
        //assertEquals(true, storageReceiveMessageProcessor.verifyStorageState());
        //  assertNotNull(sendMessageProcessor.getMessage());

        AnnotationProcessor.unprocess(this);
    }

    @EventSubscriber(eventClass = CarrierUpdateMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void carrierUpdateStorageMessageListener(CarrierUpdateMessage infoMessage) {
        Message message = infoMessage;
        LOG.error("********************* received Carrier updateMessage \n");
    }
}
