# DataStaleEventAdaptor Technical Documentation

## Purpose
The DataStaleEventAdaptor class is responsible for detecting MES (Manufacturing Execution System) device initialization and uninitialization events and publishing corresponding StaleDataMessage and ConnectionEventMessage notifications. It serves as an adaptor between the MES connection events and the stamp storage system's internal event handling mechanism.

## Logic/Functionality
- Subscribes to ConnectionInitialized and ConnectionUninitialized events
- Publishes StaleDataMessage events to indicate whether data should be considered stale
- Publishes ConnectionEventMessage events to indicate connection status
- Provides a simple mechanism for detecting and propagating connection state changes

### Key Methods:
- **processDataFresh(ConnectionInitialized msg)**: Handles connection initialization events
- **processDataStale(ConnectionUninitialized msg)**: Handles connection uninitialization events

## Flow
1. The DataStaleEventAdaptor is instantiated, registering itself as a subscriber to connection events
2. When a ConnectionInitialized event is received:
   - A StaleDataMessage with stale=false is published
   - A ConnectionEventMessage with connected=true is published
3. When a ConnectionUninitialized event is received:
   - A StaleDataMessage with stale=true is published
   - A ConnectionEventMessage with connected=false is published
4. Other components in the system subscribe to these messages and take appropriate action

## Key Elements
- **Constructor**: Registers the adaptor as a subscriber to connection events
- **processDataFresh()**: Method that handles connection initialization events
- **processDataStale()**: Method that handles connection uninitialization events

## Usage
The DataStaleEventAdaptor is typically instantiated once during system startup. It automatically registers itself as a subscriber to connection events and requires no further interaction.

```java
// Example instantiation
DataStaleEventAdaptor adaptor = new DataStaleEventAdaptor();

// No further interaction is needed; the adaptor will automatically
// process connection events and publish corresponding messages
```

## Debugging and Production Support

### Common Issues
1. **Events not being received**: Could occur if the EventBus is not properly configured or if the adaptor is not registered correctly.
2. **Events not being published**: May happen if there's an issue with the EventBus or if the adaptor methods are not being called.
3. **Multiple adaptors active**: Could cause duplicate or conflicting messages if multiple instances are active.
4. **Memory leaks**: May occur if the adaptor is not properly unregistered when no longer needed.
5. **Incorrect event handling**: Could happen if the adaptor does not correctly interpret connection events.

### Debugging Steps
1. Verify that the EventBus is properly configured
2. Check if the adaptor is correctly registered as a subscriber
3. Confirm that connection events are being generated
4. Monitor the sequence of published messages to detect duplicates or conflicts
5. Check for memory leaks by monitoring subscriber registrations

### Resolution
- For events not being received:
  - Verify that the EventBus is properly configured
  - Check if the adaptor is correctly registered
  - Ensure that connection events are being generated
- For events not being published:
  - Check if the adaptor methods are being called
  - Verify that the EventBus is functioning correctly
- For multiple adaptors active:
  - Implement a singleton pattern for the adaptor
  - Add logging to detect multiple instances
- For memory leaks:
  - Implement a method to unregister the adaptor when no longer needed
  - Use weak references for subscribers where appropriate
- For incorrect event handling:
  - Add logging to track event processing
  - Verify that the adaptor correctly interprets connection events

### Monitoring
- Log all connection events and published messages
- Monitor the frequency of connection state changes
- Track the number of stale data periods
- Set up alerts for prolonged stale data conditions
- Monitor system performance during connection state changes
- Track the correlation between connection events and system behavior