# JSONUtil Documentation

## Purpose
The JSONUtil class is a utility component that handles all JSON conversion operations in the AHM LC Sales Interface Service. It acts as a translator between Java objects and JSON text, enabling the application to communicate with external systems through standardized message formats.

## How It Works
JSONUtil works like a language interpreter for the application. When data needs to be sent to or received from external systems, JSONUtil converts between the application's internal data structures (Java objects) and the common language of integration (JSON).

### Step-by-Step Process

#### For Incoming Messages:
1. **Message Reception**: The application receives a JSON string from an external system or queue
2. **Parsing**: JSONUtil converts the JSON string into appropriate Java objects:
   - `getDataContainerFromJSON()`: Converts JSON to DataContainer objects
   - `getStatusMessageFromJSON()`: Converts JSON to StatusMessage objects
3. **Object Handling**: The application processes the converted objects

#### For Outgoing Messages:
1. **Object Creation**: The application creates message objects (like ShippingMessage)
2. **Serialization**: JSONUtil converts these objects to JSON strings using `convertShippingMessageToJSON()`
3. **Message Transmission**: The JSON string is sent to external systems or queues

## Key Components

### Fields
- `logger`: Records information about JSON conversion operations

### Methods
- `getDataContainerFromJSON(String jsonString)`: Converts JSON to DataContainer objects
  - Used when processing messages from the LC system
  
- `getStatusMessageFromJSON(String jsonString)`: Converts JSON to StatusMessage objects
  - Uses a custom type adapter to handle the Vehicle/StatusVehicle inheritance
  - Used when processing messages from the YMS system
  
- `convertShippingMessageToJSON(ShippingMessage dc)`: Converts ShippingMessage objects to JSON
  - Used when sending messages to the YMS system
  
- `getGson()`: Creates a configured Gson instance with specific serialization settings
  - Handles null values, pretty printing, and date formatting

## Interactions
The JSONUtil class interacts with several components in the system:

### Direct Dependencies
- **Jackson ObjectMapper**: Used for some JSON parsing operations
- **Gson**: Used for more complex JSON serialization/deserialization with custom type adapters
- **Message Classes**: Works with DataContainer, StatusMessage, and ShippingMessage classes

### Used By
- **ReceivingTransactionTask**: Uses JSONUtil to parse incoming JSON messages from queues
- **ShippingTransactionTask**: Uses JSONUtil to both parse incoming messages and create outgoing JSON messages

## Visual Workflow

### Incoming Message Flow
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  External       │────>│  Queue Manager  │────>│  JSONUtil       │────>│  Application    │
│  System/Queue   │     │  Service        │     │  Parser         │     │  Logic          │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
      JSON Text              JSON String           Converts to             Uses Java
                                                  Java Objects             Objects
```

### Outgoing Message Flow
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Application    │────>│  JSONUtil       │────>│  Queue Manager  │────>│  External       │
│  Logic          │     │  Serializer     │     │  Service        │     │  System/Queue   │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
   Java Objects           Converts to             JSON String             JSON Text
                          JSON
```

## Data Flow

```
┌─────────────────┐                                  ┌─────────────────┐
│  JSON String    │                                  │  Java Objects   │
│  from External  │                                  │  for Internal   │
│  Systems        │                                  │  Processing     │
└────────┬────────┘                                  └────────▲────────┘
         │                                                    │
         │                                                    │
         │                                                    │
         │         ┌─────────────────────────────┐            │
         └────────>│                             │────────────┘
                   │          JSONUtil           │
         ┌────────>│                             │────────────┐
         │         └─────────────────────────────┘            │
         │                                                    │
         │                                                    │
         │                                                    │
┌────────▼────────┐                                  ┌────────┴────────┐
│  Java Objects   │                                  │  JSON String    │
│  for Outgoing   │                                  │  for External   │
│  Messages       │                                  │  Systems        │
└─────────────────┘                                  └─────────────────┘
```

## Example Use Cases

### Example 1: Parsing an Incoming Status Message

When the application receives a status update from the YMS system:

```java
// In ReceivingTransactionTask.java
String message = queueManagerService.recv(propertyUtil.getSalesReceivingQueueName());

if (StringUtils.isNotBlank(message)) {
    logger.info("Message read from Queue-" + message);
    // Convert JSON to StatusMessage object
    StatusMessage statusMessage = JSONUtil.getStatusMessageFromJSON(message.toLowerCase());
    logger.info("Received YMS message", statusMessage.toString());
    // Process the message
    errorMessages.addAll(statusMessageHandlerFactory.handleMessage(statusMessage));
}
```

The incoming JSON might look like:
```json
{
  "TRANSACTION": {
    "LINE_ID": "A1",
    "PLANT_ID": "MAP",
    "TRANSACTION_CODE": "DLR-ASGN",
    "DESCRIPTION": "Dealer Assignment",
    "TRANSACTION_TIMESTAMP": "2023-05-15 10:30:45.123"
  },
  "VEHICLE": {
    "VIN": "1HGCM82633A123456",
    "MODEL_ID": "CM8",
    "COLOR_CODE": "B92M",
    "DEALER_CODE": "12345"
  }
}
```

JSONUtil converts this to a StatusMessage object with a StatusVehicle inside it.

### Example 2: Creating an Outgoing Shipping Message

When the application needs to send shipping information to YMS:

```java
// In ShippingTransactionTask.java
// Create and populate a ShippingMessage object
ShippingMessage shippingMessage = generateMessage(galcUrl, shippingTransaction, dataContainer);

// Convert the object to JSON
String outPutMessage = JSONUtil.convertShippingMessageToJSON(shippingMessage);
if (StringUtils.isNotBlank(outPutMessage)) {
    logger.info(" sending the message to MQ -" + outPutMessage);
    // Send the JSON message to the queue
    String result = queueManagerService.send(propertyUtil.getSalesShippingQueueName(), outPutMessage);
}
```

The resulting JSON might look like:
```json
{
  "transaction": {
    "line_id": "A1",
    "plant_id": "MAP",
    "transaction_code": "VQ_SHIP",
    "description": "Vehicle Shipping",
    "transaction_timestamp": "2023-05-15 14:22:33.456",
    "destination_site": "YMS",
    "destination_environment": "test"
  },
  "vehicle": {
    "vin": "1HGCM82633A123456",
    "model_id": "CM8",
    "model_type": "SEDAN",
    "model_option": "EXL",
    "color_code": "B92M",
    "engine_number": "K24Z123456",
    "key_number": "0123456",
    "ccc_number": "ABC123456",
    "assembly_off_date": "230515",
    "adc_process_code": "50A",
    "price": "000025000",
    "fif_codes": []
  }
}
```

## Debugging Production Issues

### Common Issues and Solutions

#### 1. JSON Parsing Errors

**Symptoms:**
- Error logs showing JsonParseException or similar exceptions
- Messages not being processed correctly

**Debugging Steps:**
1. Check the raw JSON message format in the logs:
   ```sql
   -- If using a database for logging:
   SELECT message, timestamp 
   FROM application_logs 
   WHERE message LIKE '%Message read from Queue-%' 
   ORDER BY timestamp DESC 
   LIMIT 10;
   ```
2. Verify the JSON structure against the expected format for the target class
3. Look for common JSON syntax errors (missing commas, unbalanced brackets)
4. Check for case sensitivity issues (note that some methods convert to lowercase)

#### 2. Missing or Null Fields in Converted Objects

**Symptoms:**
- NullPointerExceptions when accessing fields after conversion
- Incomplete data in processed messages

**Debugging Steps:**
1. Check if all required fields are present in the JSON:
   ```sql
   -- If using a database for logging:
   SELECT message 
   FROM application_logs 
   WHERE message LIKE '%Message read from Queue-%' 
   AND timestamp > (CURRENT_TIMESTAMP - INTERVAL '1 HOUR')
   ORDER BY timestamp DESC;
   ```
2. Verify field names in the JSON match the expected property names (including case)
3. Check the Java class definitions to ensure they match the JSON structure
4. Add debug logging to print the converted object's toString() output

#### 3. JSON Serialization Issues

**Symptoms:**
- Empty or malformed JSON being sent to external systems
- Error logs in the JSONUtil.convertShippingMessageToJSON method

**Debugging Steps:**
1. Check if the source object has all required data before conversion:
   ```java
   logger.debug("ShippingMessage before conversion: " + shippingMessage);
   ```
2. Verify that no circular references exist in the object structure
3. Check for special characters or formatting issues in string fields
4. Test the serialization with a simplified object to isolate the issue

### Visual Debugging Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ 1. Identify     │────>│ 2. Check Raw    │────>│ 3. Verify Class │
│    JSON Issue   │     │    JSON Message │     │    Definitions  │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
┌─────────────────┐     ┌─────────────────┐            ▼
│ 6. Fix Issue &  │<────│ 5. Test with    │<────┌─────────────────┐
│    Verify       │     │    Sample Data  │     │ 4. Add Debug    │
└─────────────────┘     └─────────────────┘     │    Logging      │
                                                └─────────────────┘
```

## Debugging Queries

### Check for Recent JSON Parsing Errors
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE '%JsonParseException%' 
   OR message LIKE '%JsonMappingException%'
   OR message LIKE '%JSON%error%'
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check Raw Messages Being Processed
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE 'Message read from Queue-%' 
ORDER BY timestamp DESC 
LIMIT 10;
```

### Check for Successful JSON Conversions
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE 'sending the message to MQ -%' 
ORDER BY timestamp DESC 
LIMIT 10;
```

## Summary

The JSONUtil class is a critical component in the AHM LC Sales Interface Service that enables communication between different systems by translating between Java objects and JSON format. It handles both the parsing of incoming JSON messages and the creation of outgoing JSON messages.

Think of JSONUtil as the application's translator - it ensures that all systems can understand each other by converting between their different "languages" (Java objects and JSON text). Without this translation service, the different components of the system would not be able to communicate effectively.

While JSONUtil doesn't directly interact with the database, it plays a crucial role in the data flow between systems, making it an essential part of the integration architecture.