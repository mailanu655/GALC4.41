# CommandProcessor

## Purpose
The CommandProcessor class serves as a central dispatcher for client requests in the StampStorage system. It receives messages from client connections, processes them based on their type, and routes them to the appropriate methods in the StorageStateUpdateService for execution. This class acts as a bridge between the socket-based client communication layer and the business logic layer of the application.

## Logic/Functionality
- Implements both MessageSinkInterface and CommandProcessorInterface
- Uses a singleton pattern to ensure only one instance exists throughout the application
- Processes incoming JsonServiceWrapperMessage objects and extracts the ServiceRequestMessage content
- Routes requests to appropriate methods in the StorageStateUpdateService based on the CarrierUpdateOperations value
- Handles various carrier operations including saving, recalculating destinations, adding/removing from rows, resequencing, releasing carriers, and more
- Maintains a reference to the StorageStateUpdateService (carrierManager) for executing operations

## Flow
1. The MessageTransport class receives raw data from socket connections and converts it to Message objects
2. These Message objects are passed to the CommandProcessor via the put() method
3. If the message is a JsonServiceWrapperMessage, the content is extracted as a ServiceRequestMessage
4. The processCommand() method identifies the operation type from the message
5. Based on the operation type, the appropriate method in the StorageStateUpdateService is called
6. Any exceptions during processing are caught and logged

## Key Elements
- **Singleton Pattern**: The class uses a static instance variable and getInstance() method to ensure only one instance exists
- **MessageSinkInterface Implementation**: Allows the class to receive messages from the MessageTransport
- **CommandProcessorInterface Implementation**: Defines the contract for command processing
- **processCommand() Method**: The core method that routes requests to the appropriate handler based on operation type
- **CarrierUpdateOperations Handling**: Extensive switch-like logic to handle different operation types

## Usage
The CommandProcessor is primarily used by the MessageTransport class when processing incoming socket messages:

```java
// Example of how MessageTransport uses CommandProcessor
MessageSinkInterface cmdProc = CommandProcessor.getInstance();
cmdProc.setCarrierManager(carrierManager);
stampServiceSocketConnection.setObjectSink(cmdProc);
```

When a complete message is received, it's passed to the CommandProcessor:

```java
// Inside MessageTransport.process()
if (messageSink != null) {
    messageSink.put(objectSource, thisMessage);
}
```

## Debugging and Production Support

### Common Issues
1. **Null Carrier Manager**: If the StorageStateUpdateService reference is not properly set, NullPointerExceptions will occur
2. **Invalid Message Format**: If the incoming message doesn't match the expected format, ClassCastExceptions may occur
3. **Incomplete Message Processing**: If exceptions occur during processing, operations may be partially completed
4. **Thread Safety Issues**: Since the class is a singleton used by multiple threads, concurrent modification issues may arise

### Debugging Steps
1. **Check Log Files**: Examine the application logs for exceptions or error messages from CommandProcessor
2. **Verify Message Format**: Ensure incoming messages conform to the expected JsonServiceWrapperMessage format
3. **Inspect CarrierManager State**: Verify that the StorageStateUpdateService is properly initialized and functioning
4. **Review Client Connections**: Check if client connections are being established and maintained properly
5. **Trace Message Flow**: Add debug logging to track the flow of messages through the system

### Resolution
1. **Null Carrier Manager**: Ensure the StorageStateUpdateService is properly initialized and set before processing messages
2. **Invalid Message Format**: Validate messages before processing and handle format exceptions gracefully
3. **Incomplete Processing**: Implement transaction-like behavior to ensure operations are atomic
4. **Thread Safety**: Add synchronization where needed to prevent concurrent modification issues

### Monitoring
1. **Log Message Counts**: Track the number of messages processed to identify unusual patterns
2. **Monitor Error Rates**: Watch for increases in exceptions or error logs
3. **Track Response Times**: Monitor how long it takes to process different types of messages
4. **Check Resource Usage**: Monitor memory and CPU usage to identify potential resource constraints
5. **Audit Operation Counts**: Keep track of how many operations of each type are being processed