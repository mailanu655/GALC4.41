## Purpose

The Sales Interface Service is a Java-based microservice that facilitates communication between Honda's GALC (Global Assembly Line Control) system and YMS (Yard Management System). It handles vehicle shipping and status tracking throughout the sales process.

## How It Works

### Core Functionality

This service performs two main operations:

1. **Shipping Message Processing**: Retrieves vehicle data from GALC, formats it, and sends it to YMS
2. **Status Message Processing**: Receives status updates from YMS and updates the GALC system accordingly

### Workflow

1. **Scheduled Tasks**: Two schedulers run at regular intervals:
   - `ShippingMessageScheduler`: Processes vehicles ready for shipping
   - `StatusMessageScheduler`: Processes status updates from YMS
2. **Message Flow**:
   - **Outbound**: GALC → Sales Interface Service → YMS
   - **Inbound**: YMS → Sales Interface Service → GALC
3. **Status Tracking**: The service tracks various vehicle statuses including:
   - VQ-SHIP (Vehicle ready for shipping)
   - AH-RCVD (Vehicle received at American Honda)
   - DLR-ASGN (Vehicle assigned to dealer)
   - AH-SHIP (Vehicle shipped from American Honda)
   - AH-RTN (Vehicle returned to factory)
   - And several others

## Key Components

### 1. Controllers

**LCSalesInterfaceController**: Provides REST endpoints for:

- Testing message sending/receiving
- Manually triggering shipping and status processing
- Tracking vehicle status

### 2. Schedulers

- **ShippingMessageScheduler**: Runs on a configurable schedule to process vehicles ready for shipping
- **StatusMessageScheduler**: Runs on a configurable schedule to process status updates from YMS

### 3. Tasks

- **ShippingTransactionTask**: Handles the core logic for retrieving vehicle data from GALC, formatting it, and sending it to YMS
- **ReceivingTransactionTask**: Processes status messages from YMS and updates GALC accordingly

### 4. Message Handlers

- **StatusMessageHandlerFactory**: Routes incoming status messages to the appropriate handler based on the status type
- Various handlers (AhReceiveMessageHandler, DealerAssignMessageHandler, etc.): Process specific types of status messages

### 5. Services

- **ShippingStatusService**: Manages vehicle shipping status information
- **ShippingTransactionService**: Handles shipping transaction data
- **QueueManagerService**: Manages message queue operations
- Various other services for specific data types (Frame, FrameSpec, etc.)

### 6. Models

- **ShippingStatus**: Represents the current status of a vehicle in the shipping process
- **ShippingTransaction**: Contains shipping transaction details
- **Frame**: Represents vehicle frame information
- Various other models for specific data types

### 7. Utilities

- **EmailSender**: Sends email notifications for errors
- **JSONUtil**: Handles JSON serialization/deserialization
- **PropertyUtil**: Manages application properties

## Database Interactions

The service doesn't directly interact with a database but uses REST calls to the GALC system to:

- Retrieve vehicle information
- Update vehicle status
- Track vehicle processing

## Integration Points

1. **IBM MQ**: Used for message queuing between systems
2. **GALC REST API**: Used to retrieve and update vehicle information
3. **Email System**: Used for error notifications

## Configuration

The application is configured through:

- **application.properties**: Contains queue names, URLs, process IDs, and other configuration
- **application.yml**: Contains logging configuration

## Error Handling

- Errors are logged and email notifications are sent to configured recipients
- Transactions are tracked to ensure data integrity

## Deployment

- The application is packaged as a JAR file
- It can be containerized using the jib-maven-plugin
- It runs on Java 8

This Sales Interface Service plays a critical role in Honda's vehicle sales process by ensuring accurate and timely communication between manufacturing systems (GALC) and sales systems (YMS), tracking vehicles throughout their journey from production to dealer delivery.