# StorageImpl Technical Documentation

## Purpose
`StorageImpl` is the primary implementation of the `Storage` interface, orchestrating the storage, retrieval, and management of carriers in the stamp storage system. It delegates to specialized managers for specific operations while maintaining the overall storage state. This class serves as the central coordinator for all storage-related operations in the system.

## Logic/Functionality
The class implements various storage operations:

### Carrier Storage Logic
The `store(Carrier carrier)` method implements complex logic for storing carriers based on:
- Whether the carrier is empty (die ID 999)
- The type of stop (STORE_IN_ALL_LANES, STORE_IN_C_LOW_LANES, etc.)
- The area of the stop (STORE_IN_ROUTE, STORE_OUT_ROUTE)
- Space availability in different storage areas

The method delegates to:
- `emptyManager.storeEmptyCarrier(carrier)` for empty carriers
- `storeInManager.store(carrier)` for general storage
- `storeInManager.subStore(carrier, StorageArea)` for area-specific storage

### Empty Carrier Management
- `retrieveEmptyCarrier()`: Delegates to emptyManager
- `retrieveEmptyCarrier(StorageArea)`: Delegates to emptyManager for area-specific retrieval
- `retrieveEmptyCarrierForOldWeldLine()`: Delegates to emptyManager for old weld line
- `retrieveEmptyCarrierForBAreaEmptyStorage()`: Delegates to emptyManager for B area

### Carrier Update Management
The `sendCarrierUpdateMessage(Carrier carrier)` method:
- Checks if the storage state is stale
- Retrieves the current carrier information from the database
- Compares current and new carrier properties
- Updates the carrier in the storage state if needed
- Sends update messages to the system
- Handles lane-specific logic for carriers in or moving to lanes

### Storage State Management
- `getStorageState()`: Returns the current storage state
- `reloadStorageState()`: Preserves back orders while reloading the state
- `resetStorageStateAndBackOrder()`: Completely resets the state including back orders
- `recalculateCarrierDestination(Carrier)`: Recalculates a carrier's destination and stores it

## Flow
1. The class is instantiated with dependencies for store-in, store-out, empty management, and state context
2. Clients call methods to store carriers, retrieve carriers, or manage the storage state
3. The implementation delegates to specialized managers and updates the storage state
4. For carrier updates, it synchronizes the storage state with the physical system
5. The storage state is maintained to reflect the current system state

## Key Elements
- Dependencies on specialized managers (storeInManager, storeOutManager, emptyManager)
- Storage state context for maintaining the system state
- Complex storage logic based on carrier and stop properties
- Carrier update synchronization logic
- Back order preservation during state reloads

## Usage
```java
// Example: Create a StorageImpl instance
StoreInManager storeInManager = new StoreInManagerImpl();
StoreOutManager storeOutManager = new StoreOutManagerImpl();
EmptyManagerImpl emptyManager = new EmptyManagerImpl();
StorageStateContext stateContext = new StorageStateContextImpl();
Storage storage = new StorageImpl(storeInManager, storeOutManager, emptyManager, stateContext);

// Example: Store a carrier
Carrier carrier = getCarrier();
storage.store(carrier);

// Example: Retrieve an empty carrier
StorageRow row = storage.retrieveEmptyCarrier();

// Example: Update carrier information
storage.sendCarrierUpdateMessage(updatedCarrier);

// Example: Recalculate carrier destination
storage.recalculateCarrierDestination(carrier);
```

## Debugging and Production Support

### Common Issues
1. **Complex Storage Logic**: The storage logic is complex and may be difficult to understand
2. **State Inconsistencies**: The storage state may become inconsistent with the physical system
3. **Delegation Failures**: Delegated operations to specialized managers may fail
4. **Carrier Update Synchronization**: Carrier updates may not properly synchronize with the storage state
5. **Stale State Handling**: Operations may fail if the storage state is stale

### Debugging Steps
1. Log the carrier properties and stop types during storage operations
   ```java
   logger.debug("Storing carrier: number={}, dieId={}, stopType={}, stopArea={}",
       carrier.getCarrierNumber(), 
       carrier.getDie() != null ? carrier.getDie().getId() : "null",
       carrier.getCurrentLocation() != null ? carrier.getCurrentLocation().getStopType() : "null",
       carrier.getCurrentLocation() != null ? carrier.getCurrentLocation().getStopArea() : "null");
   ```
2. Check the storage state for consistency with the physical system
   ```java
   if (storageStateContext.getStorageState().isStale()) {
       logger.warn("Storage state is stale, operations may be inconsistent");
   }
   ```
3. Verify that specialized managers are functioning correctly
   ```java
   try {
       StorageRow row = storeInManager.store(carrier);
       logger.debug("StoreInManager returned row: {}", row != null ? row.getId() : "null");
   } catch (Exception e) {
       logger.error("StoreInManager failed", e);
   }
   ```
4. Monitor carrier update operations for synchronization issues
   ```java
   logger.debug("Carrier update: number={}, currentLocation={}, destination={}",
       carrier.getCarrierNumber(),
       carrier.getCurrentLocation() != null ? carrier.getCurrentLocation().getId() : "null",
       carrier.getDestination() != null ? carrier.getDestination().getId() : "null");
   ```

### Resolution
- Add detailed logging for storage operations
- Implement periodic state consistency checks
- Add error handling for delegated operations
- Enhance carrier update synchronization logic
- Implement recovery mechanisms for stale states
- Consider adding transaction support for critical operations

### Monitoring
- Track storage operation metrics (frequency, duration, success/failure)
- Monitor storage state consistency
- Log storage operation details for troubleshooting
- Alert on persistent state inconsistencies
- Track carrier update operations to identify synchronization issues
- Monitor the frequency of state reloads
- Track back order preservation during reloads