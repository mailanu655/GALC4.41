# BackOrderProcessor Technical Documentation

## Purpose
The BackOrderProcessor class is responsible for managing back-ordered parts in the stamp storage system. It listens for carrier status messages and determines whether to store carriers or direct them to fulfill back-ordered parts based on the current system state.

## Logic/Functionality
- Subscribes to carrier status messages via the EventBus
- Processes carriers that have parts on back order
- Determines whether to store carriers or direct them to fulfill back orders
- Manages the back order list in the storage state
- Publishes carrier update messages to redirect carriers to appropriate queue stops

### Key Methods:
- **carrierStatusMessageListener**: Main event handler that processes carrier status messages
- **publishCarrierUpdate**: Publishes carrier update messages to redirect carriers
- **turnOffSubscriber**: Unregisters the event subscriber

## Flow
1. Receives carrier status messages from the EventBus
2. Checks if the carrier's die is on back order
3. If the carrier is not shippable (ON_HOLD status), it stores the carrier
4. If the carrier is shippable:
   - Retrieves active fulfillment orders
   - For each order, checks if the carrier's die matches the order's left or right die
   - If there's space in the queue and enough carriers, clears the back order
   - If there's space but not enough carriers, redirects the carrier to the queue and marks it as RELEASED
   - If there's no space in the queue, clears the back order
5. If no back order exists for the carrier's die, stores the carrier

## Key Elements
- **storage**: Reference to the Storage manager for storing carriers and accessing storage state
- **helper**: NewFulfillmentHelper instance for accessing helper methods
- **deliveryCycleSize**: Configuration parameter for the number of carriers in a delivery cycle
- **clearBackOrder**: Flag indicating whether to clear back orders

## Usage
The BackOrderProcessor is used in the stamp storage system to handle situations where parts are on back order. It's instantiated with a Storage instance, a NewFulfillmentHelper, and a delivery cycle size parameter. It automatically registers itself as an event subscriber to listen for carrier status messages.

```java
// Example instantiation
BackOrderProcessor processor = new BackOrderProcessor(
    storageInstance, 
    helperInstance, 
    "FULFILLMENT_CARRIER_CYCLE_SIZE"
);
```

## Debugging and Production Support

### Common Issues
1. **Carriers not being directed to fulfill back orders**: May occur if the queue is full or if the carrier status is ON_HOLD.
2. **Back orders not being cleared**: Could happen if there's an issue with the storage state or if the helper methods aren't functioning correctly.
3. **Multiple carriers being directed to the same queue**: May occur if multiple carriers with the same die are processed simultaneously.
4. **Incorrect die matching**: Could happen if the die information in the carrier status message is incorrect.
5. **Exception during processing**: May occur due to null references or other runtime exceptions.

### Debugging Steps
1. Check the logs for error messages or exceptions
2. Verify that the carrier status messages are being received correctly
3. Confirm that the storage state's back order list is being updated properly
4. Check if the helper methods are returning the expected values
5. Verify that the carrier update messages are being published correctly

### Resolution
- For carriers not being directed to fulfill back orders:
  - Check if the queue has available space using `helper.isSpaceAvailableInQueue()`
  - Verify that the carrier is not in ON_HOLD status
- For back orders not being cleared:
  - Check if `storage.getStorageState().removeFromBackOrderList()` is being called
  - Verify that the back order list in the storage state is being updated
- For multiple carriers being directed to the same queue:
  - Implement synchronization or locking mechanisms to prevent concurrent processing
- For incorrect die matching:
  - Verify that the die information in the carrier status message is correct
  - Check if the die comparison logic is functioning properly
- For exceptions during processing:
  - Add null checks for all object references
  - Implement proper exception handling with specific error messages

### Monitoring
- Monitor the size of the back order list in the storage state
- Track the number of carriers being directed to fulfill back orders
- Monitor the queue capacity to ensure there's space for carriers
- Log all carrier redirections and back order clearances
- Set up alerts for exceptions or errors during processing