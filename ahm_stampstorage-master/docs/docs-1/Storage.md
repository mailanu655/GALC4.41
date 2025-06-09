# Storage Technical Documentation

## Purpose
`Storage` is a high-level interface that defines the contract for storage operations in the stamp storage system. It extends the `StoreOutManager` interface and adds methods for storing carriers, managing carrier destinations, handling empty carriers, and maintaining the storage state. This interface serves as the primary entry point for components that need to interact with the storage system.

## Logic/Functionality
The interface declares methods for:

### Carrier Storage and Management
- `store(Carrier carrier)`: Stores a carrier in an appropriate storage location based on business rules
- `sendCarrierUpdateMessage(Carrier carrier)`: Sends a message to update carrier information in the system
- `recalculateCarrierDestination(Carrier carrier)`: Recalculates and updates a carrier's destination

### Storage State Management
- `getStorageState()`: Retrieves the current storage state
- `reloadStorageState()`: Reloads the storage state from the data source
- `resetStorageStateAndBackOrder()`: Resets both the storage state and back order information

### Empty Carrier Management
- `retrieveEmptyCarrier()`: Retrieves an empty carrier from any storage area
- `retrieveEmptyCarrier(StorageArea area)`: Retrieves an empty carrier from a specific storage area
- `retrieveEmptyCarrierForOldWeldLine()`: Retrieves an empty carrier specifically for the old weld line
- `retrieveEmptyCarrierForBAreaEmptyStorage()`: Retrieves an empty carrier specifically for B area empty storage

## Flow
1. Components that need to perform storage operations depend on the Storage interface
2. A concrete implementation (like StorageImpl) is injected or provided
3. Components call the interface methods to store carriers, retrieve carriers, or manage the storage state
4. The implementation applies business rules and delegates to specialized managers as needed
5. The storage state is updated to reflect changes in the system

## Key Elements
- Extension of the StoreOutManager interface (inheriting the `retrieve(Die die)` method)
- Method declarations for storage operations
- No implementation details (as it's an interface)
- Serves as a contract for implementations
- Comprehensive coverage of storage-related operations

## Usage
```java
// Example: Component with Storage dependency
public class CarrierProcessor {
    private Storage storage;
    
    public CarrierProcessor(Storage storage) {
        this.storage = storage;
    }
    
    // Store a carrier
    public void processIncomingCarrier(Carrier carrier) {
        storage.store(carrier);
    }
    
    // Retrieve a carrier for a specific die
    public StorageRow retrieveCarrierForDie(Die die) {
        return storage.retrieve(die);  // Inherited from StoreOutManager
    }
    
    // Retrieve an empty carrier
    public StorageRow getEmptyCarrier() {
        return storage.retrieveEmptyCarrier();
    }
    
    // Update carrier information
    public void updateCarrier(Carrier carrier) {
        storage.sendCarrierUpdateMessage(carrier);
    }
    
    // Reload storage state after system changes
    public void refreshStorageState() {
        storage.reloadStorageState();
    }
}
```

## Debugging and Production Support

### Common Issues
1. **Implementation Complexity**: Implementations may be complex due to the breadth of functionality
2. **State Management**: Storage state management may lead to inconsistencies
3. **Performance Bottlenecks**: Storage operations may become performance bottlenecks
4. **Concurrency Issues**: Multiple threads accessing storage may cause concurrency issues
5. **Back Order Management**: Back order tracking may become inconsistent

### Debugging Steps
1. Verify which implementation is being used in the running system
   ```java
   logger.debug("Storage implementation: {}", storage.getClass().getName());
   ```
2. Check the storage state for consistency
   ```java
   StorageState state = storage.getStorageState();
   logger.debug("Storage state: isStale={}, rowCount={}, backOrderCount={}",
       state.isStale(), state.getRows().size(),
       state.getBackOrder() != null ? state.getBackOrder().size() : 0);
   ```
3. Monitor storage operation performance
   ```java
   long startTime = System.currentTimeMillis();
   storage.store(carrier);
   long duration = System.currentTimeMillis() - startTime;
   logger.debug("Store operation took {} ms", duration);
   ```
4. Look for concurrency-related issues in logs
5. Verify back order management is working correctly

### Resolution
- Ensure implementations properly manage storage state
- Implement performance optimizations for critical operations
- Add concurrency controls to prevent state inconsistencies
- Provide mechanisms to recover from inconsistent states
- Enhance back order management to prevent orphaned back orders

### Monitoring
- Track storage operation metrics (frequency, duration)
- Monitor storage state consistency
- Log storage operation failures with detailed information
- Alert on persistent storage state inconsistencies
- Monitor empty carrier availability across storage areas
- Track back order fulfillment rates