# StorageStateContextHelper Technical Documentation

## Purpose
`StorageStateContextHelper` is an interface that extends the `Helper` interface to provide additional functionality specifically for the storage state context. It defines methods for finding carriers in storage and checking space availability at stops. This interface serves as a specialized helper for the storage state context, providing operations that are essential for maintaining an accurate representation of the storage system.

## Logic/Functionality
The interface extends the `Helper` interface and adds two additional methods:

### Carrier Finding
- `findAllCarriersInStorage()`: Retrieves all carriers currently in the storage system

### Space Availability
- `spaceAvailable(Stop stop)`: Determines if space is available at a specific stop

Additionally, it inherits all methods from the `Helper` interface:
- Audit log management (`saveToAuditLog`)
- Parameter value retrieval (`getParmValue`)
- Die object retrieval (`getEmptyDie`, `findDieByNumber`)
- Stop retrieval (`findStopByConveyorId`)
- StorageRow retrieval (`findAllStorageRowsByStorageArea`)
- Stop retrieval by area (`getStopsByStorageArea`)
- Carrier population (`populateCarrier`)
- Alarm management (`generateAlarm`, `resetAlarm`, `archiveAlarm`)
- Utility methods (`pause`)

## Flow
1. Components that need storage state context helper functionality depend on the StorageStateContextHelper interface
2. A concrete implementation (like StorageStateContextHelperImpl) is injected or provided
3. Components call the interface methods to find carriers, check space availability, etc.
4. The implementation interacts with the database and domain objects to fulfill requests
5. The storage state context uses these helper methods to maintain an accurate state

## Key Elements
- Extension of the Helper interface
- Method declarations for carrier finding and space availability
- No implementation details (as it's an interface)
- Serves as a contract for implementations
- Specialized functionality for storage state context

## Usage
```java
// Example: Component with StorageStateContextHelper dependency
public class StorageStateContextManager {
    private StorageStateContextHelper helper;
    
    public StorageStateContextManager(StorageStateContextHelper helper) {
        this.helper = helper;
    }
    
    // Find all carriers in storage
    public List<CarrierMes> getAllCarriersInStorage() {
        return helper.findAllCarriersInStorage();
    }
    
    // Check if space is available at a stop
    public boolean canAcceptCarriersAtStop(Stop stop) {
        return helper.spaceAvailable(stop);
    }
    
    // Get all storage rows in an area
    public List<StorageRow> getRowsInArea(StorageArea area) {
        return helper.findAllStorageRowsByStorageArea(area);
    }
    
    // Create a carrier from MES data
    public Carrier createCarrier(CarrierMes carrierMes) {
        return helper.populateCarrier(carrierMes);
    }
    
    // Log an operation
    public void logOperation(String operation) {
        helper.saveToAuditLog("StorageStateContextManager", operation, "CONTEXT-MANAGER");
    }
}
```

## Debugging and Production Support

### Common Issues
1. **Database Connectivity**: Methods that interact with the database may fail if connection issues occur
2. **Missing Data**: Carrier or stop data may be missing or incomplete
3. **Space Calculation Errors**: Space availability calculations may be incorrect
4. **Performance Issues**: Finding all carriers in storage may be resource-intensive
5. **Inherited Helper Issues**: Issues from the Helper interface methods may affect this interface

### Debugging Steps
1. Check database connectivity for carrier and stop queries
   ```java
   try {
       List<CarrierMes> carriers = helper.findAllCarriersInStorage();
       logger.debug("Found {} carriers in storage", carriers.size());
   } catch (Exception e) {
       logger.error("Failed to find carriers in storage", e);
   }
   ```
2. Verify carrier and stop data completeness
   ```java
   List<CarrierMes> carriers = helper.findAllCarriersInStorage();
   for (CarrierMes carrier : carriers) {
       if (carrier.getCurrentLocation() == null || carrier.getDestination() == null) {
           logger.warn("Carrier {} has incomplete location data: current={}, destination={}",
               carrier.getCarrierNumber(), carrier.getCurrentLocation(), carrier.getDestination());
       }
   }
   ```
3. Check space availability calculations
   ```java
   boolean spaceAvailable = helper.spaceAvailable(stop);
   logger.debug("Space available at stop {}: {}", stop.getId(), spaceAvailable);
   ```
4. Monitor performance of carrier finding operations
   ```java
   long startTime = System.currentTimeMillis();
   List<CarrierMes> carriers = helper.findAllCarriersInStorage();
   long duration = System.currentTimeMillis() - startTime;
   logger.debug("Finding {} carriers took {} ms", carriers.size(), duration);
   ```

### Resolution
- Implement connection pooling for database operations
- Add validation for carrier and stop data
- Enhance space availability calculations with better error handling
- Optimize carrier finding operations for performance
- Consider caching frequently accessed data
- Implement retry logic for transient database issues

### Monitoring
- Track database operation performance
- Monitor carrier data completeness
- Log space availability calculations
- Alert on persistent database connectivity issues
- Track the number of carriers in storage
- Monitor helper method usage patterns