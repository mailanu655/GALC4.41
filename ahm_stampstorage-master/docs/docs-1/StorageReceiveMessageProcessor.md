# StorageReceiveMessageProcessor Technical Documentation

## Purpose
The StorageReceiveMessageProcessor class is responsible for processing carrier status messages and managing carrier movement within the storage system. It handles carriers entering and exiting storage lanes, updates the storage state, and directs carriers to appropriate destinations based on their current location and status.

## Logic/Functionality
- Subscribes to CarrierStatusMessage events
- Processes carriers entering and exiting storage lanes
- Manages carrier storage and retrieval
- Handles carriers with back-ordered parts
- Updates the storage state when carriers move between locations
- Directs carriers to consumption stops when they reach delivery stops
- Removes defects when carriers reach consumption stops

### Key Methods:
- **carrierStatusMessageListener(CarrierStatusMessage statusMessage)**: Main event handler that processes carrier status messages
- **markCarrierConsumed(Carrier carrier)**: Marks a carrier as consumed when it reaches a consumption stop
- **isStoreInStop(Stop stop)**: Checks if a stop is a store-in stop
- **isRecircStop(Stop stop)**: Checks if a stop is a recirculation stop
- **getConsumptionStopByStopArea(StopArea stopArea)**: Gets the consumption stop for a specific stop area
- **removeDefects(Carrier carrier)**: Removes defects associated with a carrier
- **publishCarrierUpdate(Integer carrierNumber, Stop destination)**: Publishes a carrier update message

## Flow
1. Receives a carrier status message
2. Extracts the carrier, current location, and destination from the message
3. Based on the current location and destination:
   - If both are store-in stops or both are recirculation stops:
     - If the carrier's die is on back order, processes it accordingly
     - Otherwise, stores the carrier
   - If both are row stops:
     - If they're different, reloads the storage state
     - If they're the same, reorders carriers in the row
   - If the current stop is a release check or empty carrier delivery stop:
     - If the destination is not a row stop, removes the carrier from the storage state
     - Otherwise, reorders carriers in the destination row
   - If both the current stop and destination are full carrier delivery stops:
     - Directs the carrier to the appropriate consumption stop
     - Marks the carrier as consumed
   - If the current stop is a full carrier consumption stop:
     - Removes defects associated with the carrier

## Key Elements
- **storage**: Reference to the Storage manager for storing carriers and accessing storage state
- **message**: The last received Message
- **testMode**: Flag indicating whether the processor is in test mode
- **carrierStatusMessageListener()**: Main method that processes carrier status messages
- **markCarrierConsumed()**: Method that marks carriers as consumed
- **publishCarrierUpdate()**: Method that publishes carrier update messages

## Usage
The StorageReceiveMessageProcessor is typically instantiated during system startup with a Storage instance and requires no further interaction. It automatically processes carrier status messages and manages carrier movement within the storage system.

```java
// Example instantiation
Storage storage = new Storage(...);
StorageReceiveMessageProcessor processor = new StorageReceiveMessageProcessor(storage);

// No further interaction is needed; the processor will automatically
// process carrier status messages and manage carrier movement

// For testing, a test mode can be specified
StorageReceiveMessageProcessor testProcessor = new StorageReceiveMessageProcessor(storage, 1);
```

## Debugging and Production Support

### Common Issues
1. **Carriers not being stored**: Could occur if the storage manager is not functioning correctly or if the carrier status message is invalid.
2. **Storage state not being updated**: May happen if there's an issue with the storage state or if the carrier movement is not properly tracked.
3. **Carriers not being directed to consumption stops**: Could occur if the consumption stop determination logic is flawed or if the carrier update message is not published correctly.
4. **Defects not being removed**: May happen if there's an issue with the defect removal logic or if the carrier information is incorrect.
5. **Exception during processing**: Could occur due to null references or other runtime exceptions.

### Debugging Steps
1. Check the logs for error messages or exceptions
2. Verify that the storage manager is functioning correctly
3. Confirm that carrier status messages have valid current location, destination, and die information
4. Check if the storage state is being updated correctly
5. Verify that carrier update messages are being published correctly
6. Check if defects are being removed when carriers reach consumption stops

### Resolution
- For carriers not being stored:
  - Verify that the storage manager is functioning correctly
  - Check if the carrier status message has valid information
  - Add logging to track the storage process
- For storage state not being updated:
  - Check if the storage state is functioning correctly
  - Verify that carrier movement is being properly tracked
  - Implement storage state validation and recovery
- For carriers not being directed to consumption stops:
  - Review the consumption stop determination logic
  - Verify that carrier update messages are being published correctly
  - Add logging to track carrier direction changes
- For defects not being removed:
  - Check if the defect removal logic is functioning correctly
  - Verify that carrier information is correct
  - Add logging to track defect removal
- For exceptions during processing:
  - Add null checks for all object references
  - Implement proper exception handling with specific error messages
  - Add detailed logging for exception scenarios

### Monitoring
- Monitor the number of carriers stored and retrieved
- Track the number of carriers directed to consumption stops
- Monitor the number of defects removed
- Log all carrier movements and direction changes
- Set up alerts for exceptions during processing
- Monitor the storage state for consistency
- Track the correlation between carrier status messages and storage operations