# ShippingTransactionTask.java Documentation

## Purpose

The `ShippingTransactionTask` is a critical component in the Honda AHM LC Sales Interface Service that handles the outbound flow of vehicle shipping information from the GALC (Global Assembly Line Control) system to the YMS (Yard Management System). It acts as a bridge between these two systems, ensuring that vehicle data is properly formatted and transferred.

## How It Works

This task follows a simple but powerful workflow:

1. **Read Messages**: It reads messages from the GALC queue containing information about vehicles ready for shipping
2. **Process Data**: It retrieves additional vehicle details from the GALC database and formats them into shipping messages
3. **Send Messages**: It sends the formatted messages to the YMS queue
4. **Update Status**: It updates the GALC database with shipping status information

The task is triggered in two ways:
- Automatically by the `ShippingMessageScheduler` on a schedule defined in application properties
- Manually through the REST endpoint in `LCSalesInterfaceController`

## Key Components

### Main Method: execute()

The `execute()` method is the heart of this class, implementing the `ITransactionTask` interface. This method:

1. Reads a message from the GALC queue
2. Parses it into a `DataContainer` object
3. Determines the appropriate data population method based on status type:
   - `VQ_SHIP` status uses `populateShippingData()`
   - Other statuses use `populateAFOffData()`
4. Generates a shipping message using the populated data
5. Sends the message to the YMS queue
6. Updates the GALC database with the new shipping status
7. Handles errors and sends email notifications if needed

### Helper Methods

The class contains several helper methods that handle specific parts of the process:

- **generateMessage()**: Creates a formatted `ShippingMessage` object with vehicle data
- **populateShippingData()**: Retrieves shipping data for vehicles with VQ_SHIP status
- **populateAFOffData()**: Retrieves data for vehicles with assembly-off status
- **updateGalc()**: Updates the GALC database with shipping status
- **getTransactionTimeStamp()**: Formats transaction timestamps
- **getProdLotNumber()** and **getKdLotNumber()**: Format lot numbers for production and KD (Knocked Down) parts

## Data Flow

```
┌─────────────────┐
│                 │
│  GALC Queue     │──────┐
│                 │      │
└─────────────────┘      │
                         ▼
┌─────────────────────────────────────────┐
│                                         │
│         ShippingTransactionTask         │
│                                         │
│  1. Read message from GALC queue        │
│  2. Parse into DataContainer            │
│  3. Retrieve additional vehicle data    │
│  4. Format into ShippingMessage         │
│  5. Send to YMS queue                   │
│  6. Update GALC database                │
│                                         │
└───────────────┬─────────────────────────┘
                │
                ▼
┌─────────────────┐    ┌─────────────────┐
│                 │    │                 │
│   YMS Queue     │    │  GALC Database  │
│                 │    │                 │
└─────────────────┘    └─────────────────┘
```

## Database Interactions

The `ShippingTransactionTask` interacts with the GALC database through several service classes:

### Read Operations

- **ShippingTransactionService**:
  - `get50ATransactionVin()`: Retrieves shipping transactions with specific status
  - `getMaxActualTs()`: Gets the assembly-off timestamp for a vehicle
  - `getPartSerialNumber()`: Gets part serial numbers (like key numbers)
  - `getFIFCodeBySpecCode()`: Retrieves FIF codes for vehicle specifications

- **FrameService**:
  - `getFrame()`: Retrieves frame information for a product ID

- **FrameSpecService**:
  - `getFrameSpec()`: Retrieves frame specifications for a product spec code

- **ShippingStatusService**:
  - `getGalcUrl()`: Determines the appropriate GALC URL for a product ID
  - `findByProductId()`: Retrieves shipping status for a product ID

### Write Operations

- **ShippingTransactionService**:
  - `saveShippingTransaction()`: Updates shipping transaction records

- **ShippingStatusService**:
  - `saveShippingStatus()`: Updates shipping status records

### Database Queries

The actual database queries are handled by the `BaseGalcService` class, which:

1. Constructs REST API calls to the GALC system
2. Formats the request payload with the appropriate parameters
3. Makes the REST call using Spring's `RestTemplate`
4. Processes the response into the appropriate model objects

## Integration with Other Files

The `ShippingTransactionTask` integrates with several other components in the system:

### Direct Dependencies

- **ITransactionTask.java**: Interface that defines the `execute()` method
- **ShippingMessageScheduler.java**: Schedules the execution of this task
- **LCSalesInterfaceController.java**: Provides REST endpoints to manually trigger this task

### Service Dependencies

- **IQueueManagerService**: Handles JMS queue operations
- **ShippingStatusService**: Manages shipping status records
- **ShippingTransactionService**: Manages shipping transaction records
- **FrameService**: Retrieves frame information
- **FrameSpecService**: Retrieves frame specifications

### Utility Dependencies

- **PropertyUtil**: Provides access to configuration properties
- **JSONUtil**: Handles JSON serialization/deserialization
- **EmailSender**: Sends email notifications for errors

### Message Models

- **DataContainer**: Represents the message received from GALC
- **ShippingMessage**: Represents the formatted message sent to YMS
- **ShippingVehicle**: Contains vehicle details in the shipping message
- **Transaction**: Contains transaction details in the shipping message

## Code Breakdown

### Class Declaration and Dependencies

```java
@Service(value = "shippingTransactionTask")
public class ShippingTransactionTask implements ITransactionTask {
    // Logger and autowired dependencies
}
```

The class is annotated with `@Service` to make it a Spring-managed bean, and it implements the `ITransactionTask` interface.

### execute() Method

The `execute()` method follows this logic:

1. Initialize an error message list
2. Try to read a message from the GALC queue
3. If a message is found:
   - Parse it into a `DataContainer`
   - Extract the product ID and line ID
   - Get the GALC URL for this product
   - Populate shipping data based on status type
   - Generate a shipping message
   - Send the message to the YMS queue
   - Update the GALC database
4. Handle any errors and send email notifications if needed

### Data Population Methods

The class has two main methods for populating shipping data:

- **populateShippingData()**: For VQ_SHIP status
  - Retrieves shipping transactions with status 0
  - Validates required fields (price, engine number, key number)
  - Gets assembly-off date if not already present

- **populateAFOffData()**: For other statuses
  - Retrieves frame and frame spec information
  - Validates required fields
  - Gets assembly-off date if not already present

### Message Generation

The `generateMessage()` method creates a `ShippingMessage` object with:

- Vehicle information (VIN, model, color, engine number, etc.)
- Transaction details (line ID, plant ID, transaction code, etc.)
- Timestamps and lot numbers

### GALC Database Updates

The `updateGalc()` method updates two tables in the GALC database:

- **Shipping Transaction Table**: Updates the send flag to 'Y'
- **Shipping Status Table**: Changes the status from 0 to 1 (indicating it was sent to AH)

## Common Issues and Troubleshooting

### Message Queue Issues

- **No message in queue**: The task will log "No Message in Queue to read" and exit gracefully
- **Invalid message format**: The task will log an error and send an email notification
- **Queue connection problems**: These will be caught in the try-catch block and reported via email

### Data Validation Issues

- **Missing product ID**: The task will log an error and send an email notification
- **Missing required fields**: The task validates several required fields (engine number, key number, etc.) and logs errors if they're missing
- **Missing frame or frame spec**: The task checks for these and logs specific error messages

### Database Connection Issues

- **GALC connection problems**: These will be caught in the service layer and reported via email
- **Failed database updates**: The task checks the result of database operations and logs errors if they fail

## Summary

The `ShippingTransactionTask` is a crucial component in the Honda AHM LC Sales Interface Service that handles the outbound flow of vehicle shipping information. It reads messages from GALC, processes them, and sends them to YMS, ensuring that vehicle data is properly transferred between these systems. The task is designed to be robust, with comprehensive error handling and reporting, making it a reliable part of the vehicle shipping process.