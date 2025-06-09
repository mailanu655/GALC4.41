# ReceivingTransactionTask.java Documentation

## Purpose

The `ReceivingTransactionTask` class is a critical component in the Honda AHM LC Sales Interface Service that handles the processing of status update messages received from the YMS (Yard Management System). It serves as a bridge between the YMS and GALC (Global Assembly Line Control) systems, ensuring that vehicle status changes in the yard are properly reflected in the manufacturing system.

This class is responsible for:
1. Reading messages from a designated JMS queue
2. Parsing these messages into structured objects
3. Routing them to the appropriate handlers based on the status type
4. Handling any errors that occur during processing
5. Sending email notifications for any issues

## Visual Workflow

```
┌─────────────────────┐     ┌─────────────────────┐     ┌─────────────────────┐
│                     │     │                     │     │                     │
│  YMS System         │────▶│  JMS Queue          │────▶│  ReceivingTransaction│
│  (Status Updates)   │     │  (Message Storage)  │     │  Task (Processing)  │
│                     │     │                     │     │                     │
└─────────────────────┘     └─────────────────────┘     └──────────┬──────────┘
                                                                   │
                                                                   ▼
┌─────────────────────┐     ┌─────────────────────┐     ┌─────────────────────┐
│                     │     │                     │     │                     │
│  GALC Database      │◀────│  Status Message     │◀────│  StatusMessage      │
│  (Updated Records)  │     │  Handlers           │     │  HandlerFactory     │
│                     │     │                     │     │                     │
└─────────────────────┘     └─────────────────────┘     └─────────────────────┘
```

## Data Flow

1. **Input**: JSON messages from YMS queue containing vehicle status updates
2. **Processing**: 
   - Parse JSON into StatusMessage objects
   - Determine the appropriate handler based on status type
   - Execute the handler's business logic
3. **Output**: 
   - Updated records in GALC database
   - Error messages (if any) sent via email

## Code Breakdown

### Class Declaration and Dependencies

```java
@Service("ReceivingTransactionTask")
public class ReceivingTransactionTask implements ITransactionTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IQueueManagerService queueManagerService;

    @Autowired
    private PropertyUtil propertyUtil;

    @Autowired
    private StatusMessageHandlerFactory statusMessageHandlerFactory;
    
    @Autowired
    private EmailSender emailSender;
```

The class is annotated with `@Service` to make it a Spring-managed bean with the name "ReceivingTransactionTask". It implements the `ITransactionTask` interface, which defines a single `execute()` method.

Key dependencies:
- **IQueueManagerService**: Used to read messages from the JMS queue
- **PropertyUtil**: Provides access to configuration properties
- **StatusMessageHandlerFactory**: Factory that determines which handler to use based on message type
- **EmailSender**: Utility for sending error notifications

### execute() Method

```java
@Override
public void execute() {
    List<String> errorMessages = new ArrayList<String>();
    try {
        
        // Read and parse message received from YMS Queue
        String message = queueManagerService.recv(propertyUtil.getSalesReceivingQueueName());
                    
        if (Strings.isNotBlank(message)) {
            logger.info("Message read from Queue-"+message);
            StatusMessage statusMessage = JSONUtil.getStatusMessageFromJSON(message.toLowerCase());
            logger.info("Received YMS message", statusMessage.toString());
            errorMessages.addAll(statusMessageHandlerFactory.handleMessage(statusMessage));
        } else {
            logger.info("No Message in Queue to read");
        }
    } catch (Exception ex) {
        logger.error("Error processing message from YMS", ex.getMessage());
        errorMessages.add("Error processing message from YMS-"+ ex.getMessage());
    }
    
    if(!errorMessages.isEmpty()) {
        emailSender.sendEmail(getClass().getName()+" : ", errorMessages);
    }
}
```

The `execute()` method follows this logic:
1. Initialize an error message list
2. Try to read a message from the YMS queue using the configured queue name
3. If a message is found:
   - Log the message
   - Parse it into a `StatusMessage` object using `JSONUtil.getStatusMessageFromJSON()`
   - Pass the message to the `StatusMessageHandlerFactory` to route it to the appropriate handler
   - Collect any error messages returned by the handler
4. If no message is found, log that information
5. If any errors occurred during processing, send an email notification

## Integration with Other Components

### Direct Dependencies

1. **ITransactionTask Interface**
   - Defines the contract that ReceivingTransactionTask implements
   - Provides a standard `execute()` method for all transaction tasks

2. **StatusMessageScheduler**
   - Periodically triggers the `execute()` method based on a cron expression
   - Controls whether the task is enabled or disabled

3. **LCSalesInterfaceController**
   - Provides a REST endpoint to manually trigger the task
   - Allows on-demand processing of messages

4. **IQueueManagerService**
   - Abstracts the JMS queue operations
   - Implemented by `QueueManagerService` which uses Spring's `JmsTemplate`

5. **StatusMessageHandlerFactory**
   - Routes messages to the appropriate handler based on status type
   - Manages a collection of specialized handlers for different status types

### Message Handlers

The task delegates the actual processing to specialized handlers through the `StatusMessageHandlerFactory`:

1. **AhReceiveMessageHandler**: Handles "AH-RCVD" (AHM Receiving) status
2. **AhParkingChangeMessageHandler**: Handles "AH-PCHG" (Parking Change) status
3. **DealerAssignMessageHandler**: Handles "DLR-ASGN" (Dealer Assigned) status
4. **FactoryReturnMessageHandler**: Handles "AH-RTN" (Factory Return) status
5. **ShipmentConfirmMessageHandler**: Handles "AH-SHIP" (Shipment Confirmed) status
6. **SimpleStatusMessageHandler**: Handles various other statuses (PPO-ON, PPO-OFF, ON-TRN, SHIPPER, DLR-RCPT, DLR-RTN)

## Database Interactions

The `ReceivingTransactionTask` itself doesn't directly interact with any database. However, the handlers it invokes through the `StatusMessageHandlerFactory` perform database operations via service classes.

### Indirect Database Interactions (via handlers)

The handlers use service classes that extend `BaseGalcService` to interact with the GALC database through REST API calls:

1. **ShippingStatusService**:
   - `getGalcUrl()`: Determines the appropriate GALC URL for a VIN
   - `findByProductId()`: Retrieves shipping status for a VIN
   - `saveShippingStatus()`: Updates shipping status in GALC
   - `trackProduct()`: Updates tracking information in GALC

2. **Other specialized services**:
   - `FrameShipConfirmationService`
   - `ShippingTransactionService`
   - `ParkChangeService`
   - `InProcessProductService`

These services construct REST API calls to the GALC system rather than using direct SQL queries.

## Configuration

The task is configured through the application.properties file:

```properties
# Queue name for receiving messages
sales.receiving.queue.name=LQ.YMS_GALC_MAP_INFO_REQ.001

# Enable/disable the status message job
lc.status.msg.job.enable=TRUE

# Process IDs for different status types
AH-RCVD.process.id=AAH1RE1P00101
DLR-ASGN.process.id=AAH1SC1P00101
AH-SHIP.process.id=AAH1DC1P00101
AH-RTN.process.id=AVQ1FR1P00101
PPO-ON.process.id=AAH1PP1P00101
PPO-OFF.process.id=AAH1PP1P00102
ON-TRN.process.id=AAH1SC1P00103
SHIPPER.process.id=AAH1SC1P00102
DLR-RCPT.process.id=AAH1DL1P00102
DLR-RTN.process.id=AAH1DL1P00101
```

## Debugging Production Issues

### Common Issues

1. **Message Queue Connection Issues**:
   - Check IBM MQ connection settings in application.properties
   - Verify network connectivity to MQ server
   - Check queue names and permissions

2. **Message Parsing Errors**:
   - Examine the raw message format in the logs
   - Verify that the message structure matches what `JSONUtil.getStatusMessageFromJSON()` expects
   - Check for case sensitivity issues (note that the code converts to lowercase)

3. **Handler Processing Errors**:
   - Check which handler is being invoked based on the status type
   - Examine the handler's logic for potential issues
   - Verify that the GALC URL is being correctly determined

### Useful Queries for Debugging

Since the application uses REST API calls rather than direct SQL queries, debugging typically involves:

1. **Checking Application Logs**:
   - Look for "Message read from Queue" entries to see raw messages
   - Check for error messages related to parsing or processing
   - Examine handler-specific logs for more details

2. **Monitoring JMS Queue**:
   - Use IBM MQ Explorer or similar tools to check queue depth
   - Examine message contents directly in the queue
   - Verify that messages are being consumed

3. **Verifying GALC REST API Calls**:
   - Check network traffic between the application and GALC
   - Examine request payloads and response codes
   - Verify that the GALC endpoints are accessible

## Summary

The `ReceivingTransactionTask` is a crucial component in the Honda AHM LC Sales Interface Service that processes status update messages from the YMS system. It follows a clean separation of concerns by:

1. Reading messages from a queue
2. Delegating processing to specialized handlers
3. Managing errors and notifications

This design makes the code maintainable and extensible, allowing new status types to be added by simply implementing new handlers without modifying the core task logic.
