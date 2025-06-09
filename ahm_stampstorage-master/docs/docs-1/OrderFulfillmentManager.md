# OrderFulfillmentManager Technical Documentation

## Purpose
The OrderFulfillmentManager class is responsible for managing the fulfillment of weld orders in the stamp storage system. It implements the Fulfillment interface and handles the process of retrieving carriers from storage, releasing them to queue stops, and updating order status accordingly.

## Logic/Functionality
- Implements the Fulfillment interface with run() and getOrderManager() methods
- Manages the retrieval and release of carriers for weld orders
- Updates order status based on fulfillment progress
- Handles stale data and storage state reload events
- Tracks queued quantities for left and right dies

### Key Methods:
- **run()**: Main execution method that processes active orders
- **retrieveCarriers()**: Initiates the retrieval of carriers from storage
- **releaseCarriers()**: Releases carriers to queue stops
- **allCarriersRetrieved()**: Checks if all required carriers have been retrieved
- **updateQueuedQty()**: Updates the queued quantity for an order
- **healthCheck()**: Handles stale data events
- **reOrderCarriers()**: Handles storage state reload events

## Flow
1. Retrieves the active order from the order manager
2. Checks if the storage state is stale
3. Updates the queued quantities for the order
4. Based on the order status:
   - If "InProcess": Initiates carrier retrieval
   - If "RetrievingCarriers": Releases carriers to queue stops
5. If no active order is found, clears any back orders for the order manager
6. Handles exceptions and logs error messages

### Carrier Retrieval Flow:
1. Checks if any other order is in "RetrievingCarriers" status
2. If all carriers have been retrieved, updates order status to "AutoCompleted"
3. Otherwise, checks if there are any carriers in "RELEASED", "SELECTED", or "RETRIEVED" status
4. If no carriers are in these statuses, initiates a new retrieval cycle

### Carrier Release Flow:
1. Gets the minimum cycle count for carriers in "SELECTED" status
2. If carriers are available for release, processes them using the ReleaseCycleManager
3. If no carriers are available for release, updates order status to "InProcess"
4. If carriers are in "RETRIEVED" status, waits for them to be released

## Key Elements
- **orderMgr**: Reference to the OrderMgr that manages the order
- **storage**: Reference to the Storage manager for retrieving carriers
- **newFulfillmentHelper**: Helper instance for accessing fulfillment-related methods
- **releaseManager**: Manager for releasing carriers
- **fulfillmentCycleSize**: Configuration parameter for the number of carriers in a fulfillment cycle
- **recirculationCarrierReleaseCount**: Configuration parameter for the number of carriers to recirculate

## Usage
The OrderFulfillmentManager is used in the stamp storage system to manage the fulfillment of weld orders. It's instantiated with an OrderMgr, a Storage instance, a NewFulfillmentHelper, a ReleaseManager, and configuration parameters.

```java
// Example instantiation
OrderFulfillmentManager manager = new OrderFulfillmentManager(
    orderMgrInstance,
    storageInstance,
    helperInstance,
    releaseManagerInstance,
    "FULFILLMENT_CARRIER_CYCLE_SIZE",
    "RECIRCULATION_CARRIER_RELEASE_COUNT"
);

// Example usage
manager.run();
```

## Debugging and Production Support

### Common Issues
1. **Stale storage state**: Occurs when the storage state becomes stale, preventing carrier retrieval.
2. **Carriers not being retrieved**: Could happen if there's an issue with the storage manager or if the retrieval cycle is not initiated correctly.
3. **Carriers not being released**: May occur if there's an issue with the release manager or if the carrier status isn't updated correctly.
4. **Order status not updating**: Could happen if there's an issue with the database or if the order status update logic is not functioning correctly.
5. **Concurrent order processing**: Multiple orders being processed simultaneously may cause conflicts.

### Debugging Steps
1. Check the logs for error messages or exceptions
2. Verify that the storage state is not stale
3. Check if carriers are being retrieved from storage
4. Confirm that carriers are being released to queue stops
5. Verify that order status is being updated correctly
6. Check if multiple orders are being processed simultaneously

### Resolution
- For stale storage state:
  - Reload the storage state using `storage.reloadStorageState()`
  - Check if there are any stale data events being published
- For carriers not being retrieved:
  - Check if the storage manager is functioning correctly
  - Verify that the retrieval cycle is being initiated
- For carriers not being released:
  - Check if the release manager is functioning correctly
  - Verify that carrier status is being updated
- For order status not updating:
  - Check if there's an issue with the database
  - Verify that the order status update logic is correct
- For concurrent order processing:
  - Implement synchronization or locking mechanisms
  - Ensure that only one instance of OrderFulfillmentManager is processing each order

### Monitoring
- Monitor the storage state for staleness
- Track the number of carriers retrieved and released
- Monitor order status changes
- Log all significant events in the fulfillment process
- Set up alerts for stale data events and exceptions
- Monitor the execution time of the `run()` method
- Track the number of orders processed and their status