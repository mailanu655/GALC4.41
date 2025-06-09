# StorageStateContextImpl Technical Documentation

## Purpose
`StorageStateContextImpl` is a concrete implementation of both the `StorageStateContext` and `StorageLifeCycle` interfaces. It serves as the primary context for the storage state, managing the initialization, loading, and maintenance of the storage state. This class is responsible for building and maintaining an accurate representation of the physical storage system.

## Logic/Functionality
The class implements various methods for managing the storage state context:

### Storage State Management
- `getStorageState()`: Returns the current storage state, initializing it if necessary
- `reload()`: Reloads the storage state from the data source, preserving back orders
- `initializeState()`: Initializes the state data structures
- `rebuildLanes()`: Rebuilds the lanes based on the current system state
- `rebuildActualCarriersInRows()`: Rebuilds the carriers in rows based on database data
- `sortCarriersLists()`: Sorts the carrier lists for consistent ordering

### Carrier Management
- `populateCarrier(CarrierMes carrierMes)`: Delegates to the helper to transform CarrierMes data into a Carrier object
- `getCarrier(Integer carrierNumber)`: Retrieves a carrier by its number from the database
- `populateRowWithCarriers(StorageRow row)`: Populates a row with its carriers
- `findAllCarriersForRow(StorageRow row)`: Finds all carriers for a specific row
- `isCarrierInRow(CarrierMes carrierMes)`: Determines if a carrier is in a row based on location and destination
- `getCarriersWithInvalidDestination()`: Returns carriers with invalid destinations

### Die Management
- `addDie(Long dieNumber, StorageRow row)`: Associates a die with a storage row in the lastUsedRowMap
- `getRow(Die die)`: Gets the row associated with a die from the lastUsedRowMap
- `getEmptyDie()`: Delegates to the helper to get a reference to the empty die

### System Interaction
- `saveToAuditLog(String nodeId, String message, String source)`: Delegates to the helper to save an entry to the audit log
- `spaceAvailable(Stop stop)`: Delegates to the helper to check if space is available at a stop
- `healthCheck(StaleDataMessage msg)`: Event subscriber method that handles stale data messages

## Flow
1. The class is instantiated with a StorageStateContextHelper dependency
2. It registers as an event subscriber for StaleDataMessage events
3. When getStorageState() is called, it initializes the state if necessary
4. The reload() method is called to refresh the state from the database
5. The state is built by:
   - Initializing data structures
   - Loading carriers from the database
   - Determining which carriers are in rows vs. moving to rows
   - Building storage rows with their carriers
6. The built state is maintained and updated as the system changes
7. When a StaleDataMessage is received, it updates the stale flag and may reload the state

## Key Elements
- StorageStateContextHelper dependency for database and utility operations
- EventBus subscription for StaleDataMessage events
- Storage state instance (StorageStateImpl)
- Lists for carriers in rows and carriers moving to rows
- List for carriers with invalid destinations
- List for carrier-populated rows
- Map for tracking the last used row for each die
- Initialization and rebuilding logic for the storage state

## Usage
```java
// Example: Create a StorageStateContextImpl instance
StorageStateContextHelper helper = new StorageStateContextHelperImpl();
StorageStateContext context = new StorageStateContextImpl(helper);

// Example: Get the storage state
StorageState state = context.getStorageState();

// Example: Reload the state
context.reload();

// Example: Get a carrier
CarrierMes carrierMes = context.getCarrier(123);

// Example: Create a carrier from MES data
Carrier carrier = context.populateCarrier(carrierMes);

// Example: Associate a die with a row
Die die = Die.findDie(456L);
StorageRow row = StorageRow.findStorageRow(789L);
context.addDie(die.getId(), row);

// Example: Get the row for a die
StorageRow dieRow = context.getRow(die);

// Example: Check space availability
Stop stop = Stop.findStop(101L);
boolean spaceAvailable = context.spaceAvailable(stop);
```

## Debugging and Production Support

### Common Issues
1. **Stale State**: The storage state may become stale and not reflect the current system
2. **Event Subscription Issues**: StaleDataMessage events may not be received
3. **Database Connectivity**: Methods that interact with the database may fail
4. **Memory Usage**: Building the state may consume significant memory
5. **Concurrency Issues**: Multiple threads accessing the context may cause inconsistencies
6. **Invalid Carrier Data**: Carriers with invalid destinations may cause issues

### Debugging Steps
1. Check if the state is stale
   ```java
   if (storageState != null && storageState.isStale()) {
       logger.warn("Storage state is stale and may not reflect the current system");
   }
   ```
2. Verify event subscription is working
   ```java
   // In a test or diagnostic method
   StaleDataMessage testMsg = new StaleDataMessage(true);
   EventBus.publish(testMsg);
   // Check logs for "Received StaleDataMessage. Stale? true"
   ```
3. Monitor database operations during reload
   ```java
   long startTime = System.currentTimeMillis();
   List<CarrierMes> allCarriersInStorage = helper.findAllCarriersInStorage();
   long duration = System.currentTimeMillis() - startTime;
   logger.debug("Found {} carriers in storage in {} ms", allCarriersInStorage.size(), duration);
   ```
4. Track memory usage during state building
   ```java
   long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
   rebuildLanes();
   long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
   logger.debug("Rebuilding lanes used approximately {} bytes", endMemory - startMemory);
   ```
5. Check for carriers with invalid destinations
   ```java
   List<CarrierMes> invalidDestinationCarriers = context.getCarriersWithInvalidDestination();
   if (!invalidDestinationCarriers.isEmpty()) {
       logger.warn("Found {} carriers with invalid destinations", invalidDestinationCarriers.size());
       for (CarrierMes carrier : invalidDestinationCarriers) {
           logger.warn("Carrier {} has invalid destination: {}", 
               carrier.getCarrierNumber(), carrier.getDestination());
       }
   }
   ```

### Resolution
- Implement periodic state refresh to prevent staleness
- Add error handling for database operations
- Optimize memory usage during state building
- Add concurrency controls to prevent state inconsistencies
- Enhance handling of carriers with invalid destinations
- Implement recovery mechanisms for event subscription issues
- Consider adding transaction support for critical operations

### Monitoring
- Track state reload frequency and duration
- Monitor memory usage during state building
- Log carriers with invalid destinations
- Alert on persistent stale state conditions
- Track die-to-row associations
- Monitor event subscription health
- Track the number of carriers in rows vs. moving to rows