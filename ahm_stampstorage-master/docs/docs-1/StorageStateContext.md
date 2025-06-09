# StorageStateContext Technical Documentation

## Purpose
`StorageStateContext` is an interface that defines the contract for managing the context of the storage state. It provides methods for accessing the storage state, managing carriers, handling dies, and interacting with the storage system's context. This interface serves as a bridge between the storage state and the rest of the system, providing contextual information and operations.

## Logic/Functionality
The interface declares methods for:

### Storage State Access
- `getStorageState()`: Retrieves the current storage state
- `reload()`: Reloads the storage state from the data source

### Carrier Management
- `populateCarrier(CarrierMes carrierMes)`: Transforms CarrierMes data into a Carrier object
- `getCarrier(Integer carrierNumber)`: Retrieves a carrier by its number
- `getCarriersWithInvalidDestination()`: Gets carriers with invalid destinations

### Die Management
- `addDie(Long dieNumber, StorageRow row)`: Associates a die with a storage row
- `getRow(Die die)`: Gets the row associated with a die
- `getEmptyDie()`: Gets a reference to the empty die

### System Interaction
- `saveToAuditLog(String nodeId, String message, String source)`: Saves an entry to the audit log
- `spaceAvailable(Stop stop)`: Checks if space is available at a stop

## Flow
1. Components that need to interact with the storage state context depend on the StorageStateContext interface
2. A concrete implementation (like StorageStateContextImpl) is injected or provided
3. Components call the interface methods to access the storage state, manage carriers, etc.
4. The implementation maintains the context and provides access to the storage state
5. The context serves as a mediator between the storage state and the rest of the system

## Key Elements
- Method declarations for context management operations
- No implementation details (as it's an interface)
- Serves as a contract for implementations
- Bridge between the storage state and the system
- Support for carrier and die management

## Usage
```java
// Example: Component with StorageStateContext dependency
public class StorageProcessor {
    private StorageStateContext context;
    
    public StorageProcessor(StorageStateContext context) {
        this.context = context;
    }
    
    // Access the storage state
    public void processStorageState() {
        StorageState state = context.getStorageState();
        // Process the state...
    }
    
    // Process a carrier from MES
    public void processCarrierMes(CarrierMes carrierMes) {
        Carrier carrier = context.populateCarrier(carrierMes);
        // Process the carrier...
    }
    
    // Check if a die has an associated row
    public boolean hasDieAssignment(Die die) {
        StorageRow row = context.getRow(die);
        return row != null;
    }
    
    // Log an operation
    public void logOperation(String operation) {
        context.saveToAuditLog("StorageProcessor", operation, "STORAGE-PROCESSOR");
    }
    
    // Reload the context
    public void refreshContext() {
        context.reload();
    }
}
```

## Debugging and Production Support

### Common Issues
1. **Stale Context**: The context may become stale and not reflect the current system state
2. **Invalid Carrier Data**: CarrierMes data may be invalid or incomplete
3. **Missing Die Associations**: Dies may not have associated rows
4. **Space Calculation Errors**: Space availability calculations may be incorrect
5. **Reload Failures**: The reload operation may fail due to external dependencies

### Debugging Steps
1. Verify the context is up-to-date
   ```java
   // Check if the storage state is stale
   StorageState state = context.getStorageState();
   if (state.isStale()) {
       logger.warn("Storage state is stale and may not reflect the current system state");
       context.reload();
   }
   ```
2. Check carrier data validity
   ```java
   CarrierMes carrierMes = context.getCarrier(carrierNumber);
   if (carrierMes == null) {
       logger.warn("Carrier {} not found", carrierNumber);
   } else {
       logger.debug("Carrier {}: dieNumber={}, location={}, destination={}",
           carrierMes.getCarrierNumber(), carrierMes.getDieNumber(),
           carrierMes.getCurrentLocation(), carrierMes.getDestination());
   }
   ```
3. Verify die associations
   ```java
   StorageRow row = context.getRow(die);
   if (row == null) {
       logger.warn("No row associated with die {}", die.getId());
   } else {
       logger.debug("Die {} is associated with row {}", die.getId(), row.getId());
   }
   ```
4. Check space availability calculations
   ```java
   boolean spaceAvailable = context.spaceAvailable(stop);
   logger.debug("Space available at stop {}: {}", stop.getId(), spaceAvailable);
   ```

### Resolution
- Implement periodic context refresh to prevent staleness
- Add validation for carrier data
- Enhance die association management
- Improve space availability calculations
- Add error handling for reload failures
- Consider adding caching with proper invalidation

### Monitoring
- Track context reload frequency and duration
- Monitor carrier data validity
- Track die association changes
- Log space availability calculations
- Alert on persistent context issues
- Monitor invalid destination carriers
- Track audit log entries for system operations