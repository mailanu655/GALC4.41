# StatusMessageScheduler Documentation

## Purpose

The `StatusMessageScheduler` is a critical component in the AHM LC Sales Interface Service that periodically checks for and processes status messages from the YMS (Yard Management System) queue. It acts as a scheduled job that runs at regular intervals to retrieve messages about vehicle status changes and process them accordingly.

## How It Works

The scheduler uses Spring's scheduling capabilities to run a job at specified intervals (defined by a cron expression in the application properties). When triggered, it:

1. Checks if the status job is enabled in the application configuration
2. If enabled, it executes the `ReceivingTransactionTask` which:
   - Reads messages from a designated queue
   - Processes these messages based on their transaction type
   - Handles any errors that occur during processing

## Visual Workflow

```
┌─────────────────────────┐
│ StatusMessageScheduler  │
│  (Runs on cron schedule)│
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐     ┌─────────────────────┐
│ Check if job is enabled ├────►│ Log that job is not │
│ (propertyUtil.statusJob │ No  │ enabled and exit    │
│  Enable())              │     └─────────────────────┘
└───────────┬─────────────┘
            │ Yes
            ▼
┌─────────────────────────┐
│ Execute Receiving       │
│ Transaction Task        │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐     ┌─────────────────────┐
│ Read message from       │     │ If no message,      │
│ sales.receiving.queue   ├────►│ log and exit        │
└───────────┬─────────────┘     └─────────────────────┘
            │ Message found
            ▼
┌─────────────────────────┐
│ Parse JSON to           │
│ StatusMessage object    │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│ Determine message type  │
│ using StatusEnum        │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│ Route to appropriate    │
│ handler based on type   │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐     ┌─────────────────────┐
│ Process message with    │     │ If errors occur,    │
│ specific handler        ├────►│ send email alert    │
└─────────────────────────┘     └─────────────────────┘
```

## Key Components

### Class Structure
```java
@Component
public class StatusMessageScheduler {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    ReceivingTransactionTask receivingTransactionTask;
    
    @Autowired
    PropertyUtil propertyUtil;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private boolean isEnabled = false;

    @Scheduled(cron = "${status.scheduledTasks.cron.expression}")
    public void cronJobSch() {
        // Method implementation
    }
}
```

### Important Methods

#### `cronJobSch()`
- **Purpose**: The main scheduled method that runs according to the cron expression
- **Functionality**: 
  - Logs the execution time
  - Checks if the job is enabled via configuration
  - If enabled, calls the `receivingTransactionTask.execute()` method
  - If disabled, logs that the job is not enabled

## Interactions with Other Components

The `StatusMessageScheduler` interacts with several other components in the system:

1. **PropertyUtil**
   - Used to check if the status job is enabled via `propertyUtil.statusJobEnable()`
   - Retrieves configuration values from application.properties

2. **ReceivingTransactionTask**
   - The main task that is executed when the scheduler runs
   - Handles the actual message processing logic

3. **IQueueManagerService (via ReceivingTransactionTask)**
   - Used to read messages from the queue
   - Implemented by `QueueManagerService` which uses Spring's `JmsTemplate`

4. **StatusMessageHandlerFactory (via ReceivingTransactionTask)**
   - Factory that determines which handler to use based on the message type
   - Routes messages to the appropriate handler implementation

## Database Interactions

The `StatusMessageScheduler` itself does not directly interact with any database. However, the handlers that process the messages (invoked through the chain of calls) may perform database operations depending on the message type.

The database interactions would happen in the specific handler implementations:
- `AhReceiveMessageHandler`
- `AhParkingChangeMessageHandler`
- `DealerAssignMessageHandler`
- `FactoryReturnMessageHandler`
- `ShipmentConfirmMessageHandler`
- `SimpleStatusMessageHandler`

## Configuration

The scheduler is configured through the application.properties file:

```properties
# Enable/disable the status message job
lc.status.msg.job.enable=TRUE

# Cron expression for the status message scheduler (runs every minute)
status.scheduledTasks.cron.expression=0 */1 * * * ?

# Queue name for receiving messages
sales.receiving.queue.name=LQ.YMS_GALC_MAP_INFO_REQ.001
```

## Examples

### Real-world Scenario

1. A vehicle's status changes in the Yard Management System (YMS)
2. YMS sends a status update message to the queue (e.g., "AH-RCVD" for vehicle received at AHM)
3. The `StatusMessageScheduler` runs at its scheduled time
4. It checks that the job is enabled and executes the `ReceivingTransactionTask`
5. The task reads the message from the queue
6. The message is parsed into a `StatusMessage` object
7. The appropriate handler is selected based on the transaction code
8. The handler processes the message, updating the necessary systems
9. If any errors occur, an email alert is sent to the configured recipients

## Line-by-Line Code Breakdown

```java
@Component  // Marks this class as a Spring component for dependency injection
public class StatusMessageScheduler {
    // Logger for this class
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    // Autowired dependency for the task that will be executed
    @Autowired
    ReceivingTransactionTask receivingTransactionTask;
    
    // Autowired dependency for accessing configuration properties
    @Autowired
    PropertyUtil propertyUtil;

    // Date formatter for logging timestamps
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    // Flag to track if the job is enabled
    private boolean isEnabled = false;

    // Scheduled method that runs according to the cron expression in properties
    @Scheduled(cron = "${status.scheduledTasks.cron.expression}")
    public void cronJobSch() {
        // Get current date and time
        Date now = new Date();
        // Format the date for logging
        String strDate = sdf.format(now);

        // Log that the scheduled task is executing
        logger.info("status.scheduledTasks.cron.expression executed at {}", strDate);

        // Check if the job is enabled in the configuration
        isEnabled = propertyUtil.statusJobEnable();

        // If enabled, execute the task
        if (isEnabled) {
            receivingTransactionTask.execute();
        } else {
            // Log that the job is not enabled
            logger.info("Scheduled Jobs Not Enabled - Property JOBS_ENABLED = " + isEnabled);
        }
    }
}
```

## Summary

The `StatusMessageScheduler` is a crucial component that enables the AHM LC Sales Interface Service to process status messages from the YMS queue in a scheduled manner. It acts as the entry point for the message processing workflow, delegating the actual processing to specialized handlers based on the message type. The scheduler is configurable through application properties, allowing for easy enabling/disabling and schedule adjustments without code changes.