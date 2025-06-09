# ConnectionEventMessage Technical Documentation

## Purpose
The ConnectionEventMessage class is a simple message class used to communicate connection status changes within the stamp storage system. It serves as a notification mechanism for components that need to be aware of connection state changes, particularly for connections to external systems like MES (Manufacturing Execution System).

## Logic/Functionality
- Encapsulates a boolean flag indicating connection status
- Provides a constructor to set the connection status
- Offers a getter method to retrieve the connection status

### Key Methods:
- **ConnectionEventMessage(boolean connected)**: Constructor that sets the connection status
- **isConnected()**: Getter method that returns the connection status

## Flow
The ConnectionEventMessage is typically used in the following flow:
1. A component detects a connection state change (connected or disconnected)
2. The component creates a new ConnectionEventMessage with the appropriate status
3. The message is published to the EventBus
4. Subscribers receive the message and take appropriate action based on the connection status

## Key Elements
- **connected**: Boolean flag indicating whether the connection is established (true) or not (false)
- **Constructor**: Sets the initial connection status
- **isConnected()**: Method to retrieve the connection status

## Usage
The ConnectionEventMessage is typically used by components that need to notify others about connection state changes, such as the DataStaleEventAdaptor which publishes this message when MES device connections are initialized or uninitialized.

```java
// Example of creating and publishing a connection event message
boolean isConnected = true; // or false for disconnected
ConnectionEventMessage message = new ConnectionEventMessage(isConnected);
EventBus.publish(message);

// Example of subscribing to connection event messages
@EventSubscriber(eventClass = ConnectionEventMessage.class)
public void connectionEventListener(ConnectionEventMessage message) {
    if (message.isConnected()) {
        // Handle connected state
    } else {
        // Handle disconnected state
    }
}
```

## Debugging and Production Support

### Common Issues
1. **Message not being received by subscribers**: Could occur if the EventBus is not properly configured or if subscribers are not registered correctly.
2. **Incorrect connection status**: May happen if the connection status is not properly determined before creating the message.
3. **Multiple conflicting messages**: Could occur if multiple components are publishing connection status messages with different values.
4. **Memory leaks**: May happen if subscribers are not properly unregistered when they are no longer needed.
5. **Thread safety issues**: Could occur if the message is modified after publication.

### Debugging Steps
1. Verify that the EventBus is properly configured
2. Check if subscribers are correctly registered with the EventBus
3. Confirm that the connection status is correctly determined
4. Monitor the sequence of connection event messages to detect conflicts
5. Check for memory leaks by monitoring subscriber registrations

### Resolution
- For message not being received:
  - Verify that the EventBus is properly configured
  - Check if subscribers are correctly registered
  - Ensure that the message is actually being published
- For incorrect connection status:
  - Implement proper connection status detection
  - Add logging to track connection status changes
- For multiple conflicting messages:
  - Centralize connection status determination
  - Implement a single source of truth for connection status
- For memory leaks:
  - Ensure that subscribers are unregistered when no longer needed
  - Use weak references for subscribers where appropriate
- For thread safety issues:
  - Make the ConnectionEventMessage immutable
  - Avoid modifying the message after publication

### Monitoring
- Log all connection status changes
- Monitor the frequency of connection status changes
- Track the number of subscribers to connection event messages
- Set up alerts for rapid connection status fluctuations
- Monitor system performance during connection status changes