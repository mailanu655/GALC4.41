# Helper Technical Documentation

## Purpose
`Helper` is an interface that defines a contract for utility methods used throughout the stamp storage system. It abstracts common operations related to audit logging, parameter retrieval, die and stop management, carrier manipulation, and alarm handling. This interface serves as a central point for accessing utility functions across the application.

## Logic/Functionality
The interface declares methods for:

- **Audit log management**: `saveToAuditLog(String nodeId, String message, String source)` - Records system events and actions for auditing purposes
- **Parameter value retrieval**: `getParmValue(String name)` - Retrieves configuration parameters from the system
- **Die object retrieval**: 
  - `getEmptyDie()` - Gets a reference to the empty die (typically die #999)
  - `findDieByNumber(Long dieNumber)` - Finds a die by its identifier
- **Stop retrieval**: `findStopByConveyorId(Long conveyorId)` - Finds a stop location by its conveyor ID
- **StorageRow retrieval**: `findAllStorageRowsByStorageArea(StorageArea area)` - Gets all storage rows in a specific area
- **Carrier population**: `populateCarrier(CarrierMes carrierMes)` - Transforms CarrierMes data into a Carrier object
- **Alarm management**: 
  - `generateAlarm(Integer location, Integer alarmNumber)` - Creates a new alarm in the system
  - `resetAlarm(Integer alarmNumber)` - Resets an existing alarm
  - `archiveAlarm(Long id, String user)` - Archives an alarm for historical tracking
- **Utility methods**: `pause(int pauseSec)` - Pauses execution for a specified number of seconds

## Flow
1. Components that need utility functionality depend on the Helper interface
2. A concrete implementation (like AbstractHelperImpl) is injected or provided
3. Components call the interface methods without knowing the implementation details
4. The implementation handles the actual logic, database interactions, and system operations

## Key Elements
- Method declarations for common utility operations
- No implementation details (as it's an interface)
- Serves as a contract for implementations
- Covers a wide range of utility functions needed across the system

## Usage
```java
// Example: Component with Helper dependency
public class StorageProcessor {
    private Helper helper;
    
    public StorageProcessor(Helper helper) {
        this.helper = helper;
    }
    
    public void processCarrier(CarrierMes carrierMes) {
        // Convert CarrierMes to Carrier
        Carrier carrier = helper.populateCarrier(carrierMes);
        
        // Log the action
        helper.saveToAuditLog("StorageProcessor", 
            "Processing carrier " + carrier.getCarrierNumber(), 
            "STORAGE-PROCESSOR");
            
        // Process the carrier...
    }
    
    public void handleError(Integer location, Integer alarmCode) {
        // Generate an alarm
        helper.generateAlarm(location, alarmCode);
    }
}
```

## Debugging and Production Support

### Common Issues
1. **Implementation Mismatch**: Different implementations may behave inconsistently
2. **Missing Implementation**: Dependency injection may fail to provide an implementation
3. **Method Contract Violations**: Implementations may not adhere to the expected behavior
4. **Database Connectivity Issues**: Methods that interact with the database may fail if connection issues occur

### Debugging Steps
1. Verify which implementation is being used in the running system
   ```java
   logger.debug("Helper implementation: {}", helper.getClass().getName());
   ```
2. Check that the implementation correctly fulfills the interface contract
3. Review dependency injection configuration to ensure the correct implementation is provided
4. For database-related issues, check connection pool settings and database availability
5. Add logging around helper method calls to track execution flow and identify failures

### Resolution
- Ensure all implementations adhere to the interface contract
- Use a consistent implementation throughout the application
- Add validation in implementations to ensure contract compliance
- Implement proper error handling in methods that interact with external systems
- Consider adding fallback mechanisms for critical operations

### Monitoring
- Log which implementation is being used at application startup
- Monitor method call patterns to identify potential misuse
- Track error rates for helper methods to identify problematic areas
- Monitor database connection pool metrics for methods that interact with the database
- Set up alerts for repeated failures of critical helper methods