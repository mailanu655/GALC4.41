# LCSalesInterfaceController Documentation

## Overview

The `LCSalesInterfaceController` serves as the REST API interface for the Honda AHM LC Sales Interface Service. This controller provides endpoints for testing and manually triggering the core functionalities of the application, including sending shipping messages to YMS (Yard Management System), processing status updates from YMS, and tracking vehicle status.

## Purpose

This controller allows developers and support staff to:

- Test JMS messaging connections
- Manually trigger shipping and status message processing
- Test data retrieval and updates to the GALC system
- Track vehicle status changes

## File Information

- **File Path**: `src/main/java/com/honda/ahm/lc/controller/LCSalesInterfaceController.java`
- **Package**: `com.honda.ahm.lc.controller`
- **Base URL**: `/salesInterface`

## Dependencies

The controller relies on several injected services and components:

| Component | Purpose |
|-----------|---------|
| `PropertyUtil` | Provides access to application properties |
| `IQueueManagerService` | Handles JMS queue operations |
| `ShippingStatusService` | Manages vehicle shipping status |
| `ShippingTransactionService` | Manages shipping transactions |
| `ShippingTransactionTask` | Processes outbound shipping messages |
| `ReceivingTransactionTask` | Processes inbound status messages |

## API Endpoints

### Testing Endpoints

#### 1. Send Test Message

```
GET /salesInterface/send
```

**Purpose**: Sends a test JSON message to a predefined queue.

**Response**:
- `OK` - Message sent successfully
- `FAIL` - Error occurred while sending message

**Notes**:
- Uses a hardcoded queue name `LQ.MQFT_RECEIVE.01`
- Uses a hardcoded JSON message with sample vehicle data

#### 2. Receive Test Message

```
GET /salesInterface/recv
```

**Purpose**: Receives a message from a predefined queue.

**Response**:
- Message content if available
- `FAIL` - Error occurred while receiving message

### Process Endpoints

#### 1. Send Shipping Message

```
GET /salesInterface/sendShippingMessage
```

**Purpose**: Manually triggers the shipping transaction process.

**Process Flow**:
1. Reads messages from GALC queue
2. Processes the messages to extract vehicle information
3. Formats the data into shipping messages
4. Sends the messages to YMS queue
5. Updates GALC with shipping status

**Response**:
- `OK` - Process completed successfully
- `FAIL` - Error occurred during processing

#### 2. Read Status Message

```
GET /salesInterface/readStatusMessage
```

**Purpose**: Manually triggers the status message processing.

**Process Flow**:
1. Reads status messages from YMS queue
2. Parses the messages into status objects
3. Routes the messages to appropriate handlers based on status type
4. Updates GALC with the new status information

**Response**:
- `OK` - Process completed successfully
- `FAIL` - Error occurred during processing

#### 3. Track Vehicle

```
GET /salesInterface/track
```

**Purpose**: Tests the vehicle tracking functionality with a hardcoded VIN.

**Process Flow**:
1. Gets the process point ID for "AH-SHIP" status
2. Gets the GALC URL for the hardcoded VIN
3. Calls the tracking service to update the vehicle's status

**Response**:
- `OK` - Tracking completed successfully
- `FAIL` - Error occurred during tracking

### Data Retrieval Endpoints

#### 1. Test Shipping Status

```
GET /salesInterface/test2
```

**Purpose**: Tests retrieving and updating shipping status.

**Process Flow**:
1. Retrieves the shipping status for a specific VIN
2. Updates the status to 4
3. Saves the updated status back to GALC
4. Logs the before and after status

**Response**:
- `OK` - Test completed successfully
- `FAIL` - Error occurred during test

#### 2. Test Shipping Transaction

```
GET /salesInterface/test1
```

**Purpose**: Tests retrieving shipping transaction data.

**Process Flow**:
1. Retrieves shipping transactions for a specific VIN
2. Gets the assembly-off time for the vehicle
3. Gets the key part serial number
4. Logs all the retrieved information

**Response**:
- `OK` - Test completed successfully
- `FAIL` - Error occurred during test

## Data Flow Diagrams

### Controller Endpoint Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     LCSalesInterfaceController                          │
│                                                                         │
└───────────────────────────────┬─────────────────────────────────────────┘
                                │
            ┌───────────────────┼───────────────────┐
            │                   │                   │
            ▼                   ▼                   ▼
┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐
│                   │  │                   │  │                   │
│ Testing Endpoints │  │ Process Endpoints │  │ Data Retrieval    │
│ - /send           │  │ - /sendShipping   │  │   Endpoints       │
│ - /recv           │  │ - /readStatus     │  │ - /test1          │
│                   │  │ - /track          │  │ - /test2          │
└───────────────────┘  └───────────────────┘  └───────────────────┘
```

### Shipping Message Flow (via /sendShippingMessage)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     LCSalesInterfaceController                          │
│                     /sendShippingMessage endpoint                       │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     ShippingTransactionTask.execute()                   │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Read message from GALC Queue                        │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Parse message into DataContainer                    │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Generate ShippingMessage                            │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Send message to YMS Queue                           │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Update GALC with shipping status                    │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Return "OK" to controller                           │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### Status Message Flow (via /readStatusMessage)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     LCSalesInterfaceController                          │
│                     /readStatusMessage endpoint                         │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     ReceivingTransactionTask.execute()                  │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Read message from YMS Queue                         │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Parse message into StatusMessage                    │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Route message to appropriate handler                │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Update GALC with new status                         │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Return "OK" to controller                           │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## Integration with Other Components

### Service Layer Integration

The controller integrates with several service classes:

- **IQueueManagerService**: Used for JMS queue operations
  - `send()`: Sends messages to specified queues
  - `recv()`: Receives messages from specified queues

- **ShippingStatusService**: Used for managing vehicle shipping status
  - `getGalcUrl()`: Determines the appropriate GALC URL for a VIN
  - `findByProductId()`: Retrieves shipping status for a VIN
  - `saveShippingStatus()`: Updates shipping status in GALC
  - `trackProduct()`: Updates tracking information in GALC

- **ShippingTransactionService**: Used for managing shipping transactions
  - `get50ATransactionVin()`: Retrieves shipping transactions
  - `getMaxActualTs()`: Gets the assembly-off timestamp
  - `getPartSerialNumber()`: Gets part serial numbers

### Task Layer Integration

The controller directly calls the task layer to execute business processes:

- **ShippingTransactionTask**: Processes outbound shipping messages
  - `execute()`: Reads messages from GALC, processes them, and sends them to YMS

- **ReceivingTransactionTask**: Processes inbound status messages
  - `execute()`: Reads status messages from YMS and updates GALC

### Utility Layer Integration

The controller uses utility classes for configuration and helper functions:

- **PropertyUtil**: Provides access to application properties
  - `getProcessPoint()`: Gets process point IDs for different statuses
  - `getAFOffProcessPoint()`: Gets the assembly-off process point ID
  - `getKeyNoPartName()`: Gets the key part name

## Database Interactions

The controller doesn't directly interact with databases. Instead, it uses service classes that make REST API calls to the GALC system. These REST calls effectively serve as the database interaction layer:

### Indirect Database Interactions

1. **ShippingStatusService**:
   - `findByProductId()`: Retrieves shipping status from GALC via REST
   - `saveShippingStatus()`: Updates shipping status in GALC via REST
   - `trackProduct()`: Updates tracking information in GALC via REST

2. **ShippingTransactionService**:
   - `get50ATransactionVin()`: Retrieves shipping transactions from GALC via REST
   - `getMaxActualTs()`: Retrieves timestamp information from GALC via REST
   - `getPartSerialNumber()`: Retrieves part information from GALC via REST

### REST API Calls

The actual REST calls are handled by the `BaseGalcService` class, which:

1. Constructs the appropriate URL for the GALC REST service
2. Creates the request payload
3. Makes the REST call using Spring's `RestTemplate`
4. Processes the response

## Error Handling

The controller implements basic error handling:

1. All endpoints are wrapped in try-catch blocks
2. JMS exceptions are caught and logged
3. Simple "OK" or "FAIL" responses are returned to indicate success or failure
4. Detailed error messages are logged but not exposed in the API response

## Real-World Usage

This controller is primarily used for:

1. **Testing**: Verifying that the JMS connections and GALC REST API calls are working correctly
2. **Manual Operations**: Allowing support staff to manually trigger processes when needed
3. **Troubleshooting**: Providing endpoints to check the status of specific vehicles
4. **Development**: Helping developers understand and test the data flow

## Summary

The `LCSalesInterfaceController` provides a simple but effective REST API interface for the Honda AHM LC Sales Interface Service. While it doesn't contain complex business logic itself, it orchestrates the interaction between the user and the underlying services that handle the actual data processing and communication with GALC and YMS systems.

The controller is primarily designed for testing and manual operation, while the actual scheduled processing of shipping and status messages is handled by the scheduler components that call the same transaction tasks that the controller endpoints use.