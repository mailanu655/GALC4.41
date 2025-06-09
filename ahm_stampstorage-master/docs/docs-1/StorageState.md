# StorageState Technical Documentation

## Purpose
`StorageState` is a comprehensive interface that defines the contract for managing the state of the storage system. It provides methods for querying rows, managing carriers in lanes, tracking back orders, and maintaining the overall system state. This interface serves as the central point for accessing and manipulating the current state of the storage system.

## Logic/Functionality
The interface declares methods for:

### Row Querying
- `queryForRow(Matcher<StorageRow> matcher)`: Finds the first row that matches the specified criteria
- `queryForRows(Matcher<StorageRow> matcher)`: Finds all rows that match the specified criteria
- `queryForRows(Matcher<StorageRow> matcher, List<StorageRow> laneImpls)`: Finds matching rows within a specific subset of rows
- `getRows()`: Retrieves all rows in the storage state

### Carrier Lane Management
- `storeInLane(Carrier carrier, StorageRow storageRow)`: Stores a carrier in a specific lane
- `releaseCarrierFromLane(StorageRow storageRow)`: Releases a carrier from a lane
- `releaseCarrierIfExistsAtHeadOfLane(Carrier carrier)`: Releases a carrier if it exists at the head of a lane
- `storeInLaneIfDestinationIsALaneAndAlreadyNotExistsInStorageSystem(Carrier carrier)`: Stores a carrier in its destination lane if it's not already in the system
- `getCarrierPositionInLane(Long laneStopConveyorId, Integer carrierNumber)`: Gets the position of a carrier in a lane
- `addCarriersToLane(List<Carrier> carriers, Stop lanestop)`: Adds multiple carriers to a lane
- `removeCarrierFromRow(Integer carrierNumber, Long laneStopConveyorId)`: Removes a carrier from a row
- `reorderCarriersInRow(Long laneStopConveyorId)`: Reorders carriers in a row

### Carrier Management
- `sendCarrierUpdateMessage(Carrier carrier)`: Sends a message to update carrier information
- `updateCarrier(Carrier carrier)`: Updates a carrier in the storage state
- `carrierExistsInStorageState(Carrier carrier)`: Checks if a carrier exists in the storage state
- `hadValidLaneDestination(Carrier carrier)`: Checks if a carrier has a valid lane destination
- `removeCarrierFromStorageState(Carrier carrier)`: Removes a carrier from the storage state

### Back Order Management
- `isCarrierPartsOnBackOrder(Die die)`: Checks if parts for a die are on back order
- `getBackOrder()`: Gets the list of back orders
- `setBackOrder(List<BackOrder> backOrder)`: Sets the list of back orders
- `removeFromBackOrderList(Die die, OrderMgr orderMgr)`: Removes a specific back order
- `removeFromBackOrderList(OrderMgr orderMgr)`: Removes all back orders for an order manager

### State Management
- `isStale()`: Checks if the storage state is stale
- `setStale(boolean stale)`: Sets the stale flag for the storage state
- `updateLane(StorageRow row)`: Updates a lane in the storage state

## Flow
1. Components that need to manage the storage state depend on the StorageState interface
2. A concrete implementation (like StorageStateImpl) is injected or provided
3. Components call the interface methods to query rows, manage carriers, etc.
4. The implementation maintains the state and applies business rules
5. The state reflects the current condition of the physical storage system

## Key Elements
- Method declarations for state management operations
- No implementation details (as it's an interface)
- Serves as a contract for implementations
- Comprehensive coverage of state management needs
- Support for querying with Hamcrest matchers

## Usage
```java
// Example: Component with StorageState dependency
public class StorageProcessor {
    private StorageState storageState;
    
    public StorageProcessor(StorageState storageState) {
        this.storageState = storageState;
    }
    
    // Find rows that match specific criteria
    public List<StorageRow> findEmptyRows() {
        return storageState.queryForRows(RowMatchers.isCurrentCapacityEmpty());
    }
    
    // Store a carrier in a lane
    public void storeCarrier(Carrier carrier, StorageRow row) {
        storageState.storeInLane(carrier, row);
    }
    
    // Release a carrier from a lane
    public Carrier releaseCarrier(StorageRow row) {
        return storageState.releaseCarrierFromLane(row);
    }
    
    // Check if a die is on back order
    public boolean isDieOnBackOrder(Die die) {
        return storageState.isCarrierPartsOnBackOrder(die);
    }
    
    // Update carrier information
    public void updateCarrier(Carrier carrier) {
        storageState.updateCarrier(carrier);
        storageState.sendCarrierUpdateMessage(carrier);
    }
}
```

## Debugging and Production Support

### Common Issues
1. **State Inconsistencies**: The storage state may become inconsistent with the physical system
2. **Concurrency Issues**: Multiple threads accessing the state may cause inconsistencies
3. **Performance Bottlenecks**: State operations may become performance bottlenecks
4. **Back Order Management**: Back order tracking may become inconsistent
5. **Stale State**: The state may become stale and not reflect the current system condition

### Debugging Steps
1. Verify the state consistency with the physical system
   ```java
   // Check if the state is stale
   if (storageState.isStale()) {
       logger.warn("Storage state is stale and may not reflect the current system state");
   }
   
   // Log the current state
   logger.debug("Current storage state: {} rows, {} back orders",
       storageState.getRows().size(),
       storageState.getBackOrder() != null ? storageState.getBackOrder().size() : 0);
   ```
2. Check for concurrency-related issues in logs
   ```java
   // Add synchronization for critical operations if needed
   synchronized(lockObject) {
       storageState.updateCarrier(carrier);
   }
   ```
3. Monitor state operation performance
   ```java
   long startTime = System.currentTimeMillis();
   List<StorageRow> rows = storageState.queryForRows(matcher);
   long duration = System.currentTimeMillis() - startTime;
   logger.debug("Query took {} ms and returned {} rows", duration, rows != null ? rows.size() : 0);
   ```
4. Verify back order tracking accuracy
   ```java
   logger.debug("Back orders: {}", storageState.getBackOrder());
   ```

### Resolution
- Implement periodic state consistency checks
- Add concurrency controls to prevent state inconsistencies
- Optimize performance-critical state operations
- Enhance back order management logic
- Implement mechanisms to detect and recover from stale states
- Consider adding transaction support for critical operations

### Monitoring
- Track state operation metrics (frequency, duration)
- Monitor state consistency with the physical system
- Log state operation details for troubleshooting
- Alert on persistent state inconsistencies
- Track back order creation and fulfillment
- Monitor the stale state flag to detect potential issues
- Track carrier positions and movements through the system