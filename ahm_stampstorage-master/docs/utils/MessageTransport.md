# MessageTransport

## Purpose
The MessageTransport class handles the low-level communication between client and server in the StampStorage system. It manages the reading and writing of messages over socket streams, processes incoming messages, and routes them to the appropriate message sink for further processing. This class serves as the bridge between the network layer and the application's message processing logic.

## Logic/Functionality
- Manages input and output streams for socket communication
- Reads and processes incoming messages from clients
- Writes outgoing messages to clients
- Detects and handles PING messages for connection health monitoring
- Routes other message types to the configured message sink
- Handles various IO exceptions and connection issues
- Supports both character and byte-based message reading

## Flow
1. The class is instantiated with input and output streams from a socket connection
2. It sets up buffered readers and writers for efficient stream handling
3. The process() method is called to begin processing messages
4. It reads data from the input stream and accumulates it in a buffer
5. When a complete message is detected (using a message separator), it extracts the message
6. PING messages are handled directly with an immediate response
7. Other messages are parsed into appropriate objects and passed to the message sink
8. The process continues until the connection is closed or an error occurs

## Key Elements
- **Stream Management**: Handles input and output streams for socket communication
- **Message Parsing**: Extracts and parses messages from the input stream
- **PING Handling**: Special handling for PING messages to maintain connection health
- **Message Routing**: Routes messages to the appropriate message sink
- **Error Handling**: Comprehensive error handling for IO exceptions and other issues
- **Buffer Management**: Efficiently manages buffers for reading and accumulating message data

## Usage
The MessageTransport is typically created by the StampServiceSocketConnection when a new client connection is established:

```java
// Example of creating a MessageTransport
MessageTransport transport = new MessageTransport(
    clientSocket.getInputStream(),
    clientSocket.getOutputStream(),
    socketConnection
);
transport.setMessageSink(messageSink);
```

It can be started directly or through the run method:

```java
// Example of starting message processing
transport.process();
// or
transport.start();
```

Messages can be sent to clients using the writeObject method:

```java
// Example of sending a message
MessageRequest req = new ConnectionRequest(message);
transport.writeObject(req);
```

## Debugging and Production Support

### Common Issues
1. **Message Parsing Errors**: Issues with extracting complete messages from the input stream
2. **Stream Corruption**: Corrupted data in the input or output streams
3. **Connection Timeouts**: Timeouts during read operations
4. **Resource Leaks**: Failure to properly close streams
5. **Message Format Issues**: Problems with message format or encoding

### Debugging Steps
1. **Check Message Separators**: Verify that message separators are correctly defined and detected
2. **Inspect Stream State**: Check the state of input and output streams
3. **Review Timeout Settings**: Ensure timeout settings are appropriate for the network environment
4. **Monitor Resource Usage**: Check for resource leaks related to streams
5. **Examine Message Formats**: Verify that messages conform to expected formats
6. **Analyze Error Logs**: Review logs for exceptions or errors related to message transport

### Resolution
1. **Message Parsing Improvements**: Enhance message parsing logic to handle edge cases
2. **Stream Handling Enhancements**: Improve stream handling to better detect and recover from corruption
3. **Timeout Adjustments**: Tune timeout settings based on network performance
4. **Resource Management**: Ensure proper closing of resources in all scenarios
5. **Format Validation**: Add validation for message formats

### Monitoring
1. **Message Throughput**: Track the number of messages processed per second
2. **Error Rates**: Monitor for exceptions during message processing
3. **Connection Duration**: Track how long connections remain active
4. **Buffer Usage**: Monitor buffer usage to detect potential memory issues
5. **Message Sizes**: Track the sizes of messages being processed