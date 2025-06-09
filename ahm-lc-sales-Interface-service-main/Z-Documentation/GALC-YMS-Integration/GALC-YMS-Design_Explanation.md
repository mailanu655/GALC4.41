# GALC-YMS Integration Design

## What is the GALC-YMS-Design.pdf?

The GALC-YMS-Design.pdf document outlines the architectural design and integration approach between Honda's Global Assembly Line Control (GALC) system and the Yard Management System (YMS). This document serves as the blueprint for how these two critical systems communicate with each other through the Sales Interface Service.

## The Big Picture: How These Systems Work Together

Think of the vehicle sales process as a relay race where information about each vehicle needs to be passed from one system to another as the vehicle moves through different stages:

```
Manufacturing → Shipping → Dealer Assignment → Delivery
    (GALC)        (YMS)        (YMS)           (YMS)
```

The Sales Interface Service acts as the translator and messenger between these systems, ensuring that:

1. GALC knows when a vehicle has been shipped, received, or returned
2. YMS knows which vehicles are ready for shipping and their specifications
3. Both systems maintain accurate, synchronized information about each vehicle

## Key Components of the Integration

### 1. Message Queues

The integration uses IBM MQ message queues to reliably transfer information between systems:

- **LC Receiving Queue**: Where GALC places messages about vehicles ready for shipping
- **Sales Shipping Queue**: Where the Sales Interface sends formatted messages for YMS
- **Sales Receiving Queue**: Where YMS sends status updates back to the Sales Interface

### 2. Data Transformation

The Sales Interface transforms data between GALC and YMS formats:

- **GALC → YMS**: Converts vehicle manufacturing data into shipping information
- **YMS → GALC**: Converts status updates into process point tracking information

### 3. Process Points

Process points are unique identifiers that mark specific stages in a vehicle's journey:

| Process Point ID | What It Means | System Interaction |
|------------------|---------------|-------------------|
| AAH1RE1P00101 | Vehicle received at American Honda | YMS → GALC |
| AAH1SC1P00101 | Vehicle assigned to dealer | YMS → GALC |
| AAH1DC1P00101 | Vehicle shipped from American Honda | YMS → GALC |
| AAH1PP1P00101 | Vehicle enters post-production options | YMS → GALC |
| AAH1SC1P00103 | Vehicle loaded onto train | YMS → GALC |

### 4. Status Flow

The integration follows a defined status flow that represents the vehicle's journey:

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  AF-OFF     │────>│  VQ-SHIP    │────>│  AH-RCVD    │────>│  DLR-ASGN   │
│ (Assembly)  │     │ (Ready for  │     │ (Received   │     │ (Dealer     │
│             │     │  Shipping)  │     │  at Honda)  │     │ Assignment) │
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
                                                                    │
                                                                    ▼
                                                            ┌─────────────┐
                                                            │  AH-SHIP    │
                                                            │ (Shipped    │
                                                            │  from Honda)│
                                                            └─────────────┘
```

## How the Sales Interface Implements This Design

### Outbound Flow (GALC → YMS)

1. **Scheduled Polling**: The `ShippingMessageScheduler` runs at configured intervals
2. **Message Retrieval**: Reads messages from the GALC queue about vehicles ready for shipping
3. **Data Enrichment**: Retrieves additional vehicle details from GALC
4. **Message Formatting**: Creates a properly formatted message for YMS
5. **Message Delivery**: Sends the message to the YMS queue
6. **Status Update**: Updates GALC that the message has been sent

### Inbound Flow (YMS → GALC)

1. **Scheduled Polling**: The `StatusMessageScheduler` runs at configured intervals
2. **Message Retrieval**: Reads status update messages from the YMS queue
3. **Message Routing**: The `StatusMessageHandlerFactory` routes messages to the appropriate handler
4. **Status Processing**: The specific handler (e.g., `AhReceiveMessageHandler`) processes the status update
5. **GALC Update**: Updates the vehicle status in GALC
6. **Process Point Tracking**: Sends the appropriate process point to GALC to track the vehicle's progress

## Real-World Example

Let's follow a vehicle through the integration process:

1. **Manufacturing Completion**:
   - Vehicle completes assembly (AF-OFF status)
   - GALC records this and marks it ready for shipping (VQ-SHIP)
   - GALC places a message in the LC Receiving Queue

2. **Shipping Preparation**:
   - Sales Interface retrieves the message
   - It gathers vehicle details (VIN, model, specifications) from GALC
   - It formats this information for YMS
   - It sends the formatted message to the Sales Shipping Queue

3. **Yard Management**:
   - YMS receives the message and processes the vehicle
   - When the vehicle is received at American Honda, YMS sends an AH-RCVD status update
   - This message goes to the Sales Receiving Queue

4. **Status Update Processing**:
   - Sales Interface retrieves the status update
   - It routes the message to the AhReceiveMessageHandler
   - The handler updates the vehicle status in GALC
   - It sends the process point AAH1RE1P00101 to GALC to track this milestone

5. **Continued Tracking**:
   - This process continues as the vehicle moves through dealer assignment, shipping, etc.
   - Each status change triggers a message from YMS
   - Each message is processed and results in a GALC update with the appropriate process point

## Technical Implementation Details

### Message Structure

The integration uses JSON messages with specific structures:

**GALC → YMS Message Example**:
```json
{
  "vin": "1HGCY2F83RA046588",
  "model": "ACCORD",
  "color": "NH-883P",
  "destination": "MIDWEST",
  "options": ["SUNROOF", "NAVIGATION"]
}
```

**YMS → GALC Message Example**:
```json
{
  "vin": "1HGCY2F83RA046588",
  "statusType": "AH-RCVD",
  "timestamp": "2023-06-15T14:30:00",
  "location": "EAST_LIBERTY"
}
```

### REST API Integration

The Sales Interface uses REST API calls to interact with GALC:

1. **Retrieving Vehicle Information**:
   ```
   GET /galc/api/frame/findByKey?productId=1HGCY2F83RA046588
   ```

2. **Updating Status**:
   ```
   POST /galc/api/shippingStatus/save
   ```

3. **Tracking Process Points**:
   ```
   POST /galc/api/trackingService/track
   ```

## Benefits of This Integration Design

1. **Reliable Communication**: Message queues ensure no updates are lost
2. **Decoupled Systems**: GALC and YMS can operate independently
3. **Standardized Tracking**: Process points provide consistent status tracking
4. **Scalability**: The design handles high volumes of vehicles
5. **Error Handling**: Failed messages are logged and can be retried

## Troubleshooting the Integration

When issues occur in the integration, check:

1. **Queue Connectivity**: Ensure IBM MQ connections are working
2. **Message Format**: Verify messages follow the expected structure
3. **Process Point Configuration**: Confirm process point IDs match between systems
4. **Status Flow**: Check if vehicles are in the correct status for updates
5. **API Availability**: Verify GALC APIs are accessible

## Summary

The GALC-YMS-Design.pdf document provides the architectural blueprint for how Honda's manufacturing system (GALC) and yard management system (YMS) communicate through the Sales Interface Service. This integration enables seamless tracking of vehicles from production through delivery, ensuring accurate information is maintained across systems.

The Sales Interface Service implements this design through scheduled tasks, message queues, status handlers, and process point tracking, creating a robust bridge between these critical systems in Honda's vehicle sales process.