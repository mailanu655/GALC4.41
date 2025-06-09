# RetrieveCycleManager Technical Documentation

## Purpose
The RetrieveCycleManager class is responsible for retrieving carriers from storage for fulfillment of weld orders. It selects appropriate carriers based on die requirements, handles mixed blocks in storage lanes, and manages back orders when required parts are not available.

## Logic/Functionality
- Retrieves carriers from storage based on die requirements
- Handles mixed blocks in storage lanes by recirculating blocking carriers
- Places parts on back order when they cannot be retrieved
- Selects and saves carriers for new fulfillment cycles
- Manages the process of unblocking lanes to access required carriers

### Key Methods:
- **process()**: Main method that processes carrier retrieval for an order
- **getCarriersForNewCycle()**: Retrieves carriers for a new fulfillment cycle
- **recirculateCarriers()**: Recirculates carriers blocking access to required carriers
- **selectAndSaveNewCarriers()**: Selects and saves carriers for a new cycle
- **placePartsOnBackOrder()**: Places parts on back order when they cannot be retrieved

## Flow
1. Checks if any other order is retrieving carriers or if the order is not in "InProcess" status
2. Gets the left and right dies for the order
3. Determines if more carriers are needed for each die
4. Checks the capacity of the left and right queue stops
5. If space is available and carriers are needed, selects and saves new carriers
6. Updates the order status to "RetrievingCarriers"

### Carrier Retrieval Flow:
1. Determines the quantity of parts needed and already queued
2. Retrieves carriers from storage lanes
3. If a lane has a mixed block, attempts to recirculate blocking carriers
4. If recirculation is successful, releases the required carrier
5. If recirculation fails or no carriers are available, places parts on back order
6. Returns the list of retrieved carriers

### Recirculation Flow:
1. Counts the number of carriers blocking access to the required die
2. If the count exceeds the recirculation limit, generates an alarm and throws an exception
3. Otherwise, recirculates blocking carriers until the required die is accessible
4. Returns a flag indicating whether recirculation was successful

## Key Elements
- **carrierCycleSize**: Configuration parameter for the number of carriers in a fulfillment cycle
- **recirculationCarrierReleaseCount**: Configuration parameter for the maximum number of carriers to recirculate
- **storage**: Reference to the Storage manager for retrieving carriers
- **process()**: Main method that orchestrates the retrieval process
- **getCarriersForNewCycle()**: Method that retrieves carriers from storage
- **recirculateCarriers()**: Method that handles mixed blocks in storage lanes
- **CannotUnBlockCarriersException**: Custom exception for when lanes cannot be unblocked

## Usage
The RetrieveCycleManager is used by the OrderFulfillmentManager to retrieve carriers for fulfillment of weld orders. It's typically instantiated and used within the retrieveCarriers() method of the OrderFulfillmentManager.

```java
// Example instantiation
RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(
    storageInstance,
    "FULFILLMENT_CARRIER_CYCLE_SIZE",
    "RECIRCULATION_CARRIER_RELEASE_COUNT"
);

// Example usage
retrieveCycleManager.process(order, helper, releaseManager);
```

## Debugging and Production Support

### Common Issues
1. **No carriers available**: Occurs when there are no carriers with the required die in storage.
2. **Mixed blocks in storage lanes**: Happens when carriers with different dies are mixed in a storage lane, blocking access to required carriers.
3. **Recirculation limit exceeded**: Occurs when the number of carriers blocking access exceeds the recirculation limit.
4. **Back order placement failures**: Could happen if there's an issue with updating the back order list in the storage state.
5. **Carrier selection failures**: May occur if there's an issue with selecting carriers from storage or if the die information is incorrect.

### Debugging Steps
1. Check the logs for error messages or exceptions, particularly NoApplicableRuleFoundException and CannotUnBlockCarriersException
2. Verify that there are carriers with the required die in storage
3. Check if storage lanes have mixed blocks and if recirculation is possible
4. Confirm that the back order list in the storage state is being updated correctly
5. Verify that carriers are being selected and saved correctly

### Resolution
- For no carriers available:
  - Place parts on back order using `placePartsOnBackOrder()`
  - Check if carriers with the required die are being added to storage
- For mixed blocks in storage lanes:
  - Attempt to recirculate blocking carriers using `recirculateCarriers()`
  - If recirculation fails, generate an alarm and place parts on back order
- For recirculation limit exceeded:
  - Increase the recirculation limit if appropriate
  - Manually reorganize storage lanes to reduce mixed blocks
- For back order placement failures:
  - Check if the storage state is functioning correctly
  - Verify that the back order list is being updated
- For carrier selection failures:
  - Check if the storage manager is functioning correctly
  - Verify that the die information is correct

### Monitoring
- Monitor the number of carriers retrieved
- Track the number of mixed blocks encountered
- Monitor the number of recirculation attempts and failures
- Track the number of back orders placed
- Log all carrier selections and recirculations
- Set up alerts for NoApplicableRuleFoundException and CannotUnBlockCarriersException
- Monitor the execution time of the `process()` method