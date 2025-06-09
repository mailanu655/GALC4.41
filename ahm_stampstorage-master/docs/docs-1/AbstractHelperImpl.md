# AbstractHelperImpl Technical Documentation

## Purpose
`AbstractHelperImpl` is a concrete implementation of the `Helper` interface that provides utility methods for common operations in the stamp storage system. This class serves as the primary implementation of utility functions used throughout the application, including audit logging, parameter retrieval, alarm management, and carrier data manipulation.

## Logic/Functionality
This class implements various helper methods defined in the Helper interface:

- **Audit log management**: `saveToAuditLog(String nodeId, String message, String source)` - Persists audit log entries using the AuditErrorLog domain class
- **Parameter value retrieval**: `getParmValue(String name)` - Retrieves and parses parameter values from the ParmSetting domain class
- **Die object retrieval**: 
  - `getEmptyDie()` - Returns the die with ID 999 (representing an empty die)
  - `findDieByNumber(Long dieNumber)` - Finds a die by its ID with null/negative value handling
- **Stop retrieval**: `findStopByConveyorId(Long conveyorId)` - Finds a stop by its conveyor ID with null/negative value handling
- **StorageRow retrieval**: `findAllStorageRowsByStorageArea(StorageArea area)` - Retrieves all storage rows in a specific area
- **Stop retrieval by area**: `getStopsByStorageArea(StorageArea area)` - Gets all stop numbers for a specific storage area
- **Carrier population**: `populateCarrier(CarrierMes carrierMes)` - Transforms CarrierMes data into a Carrier object with proper relationships
- **Alarm management**: 
  - `generateAlarm(Integer location, Integer alarmNumber)` - Creates a new alarm if no uncleared alarm of the same type exists
  - `resetAlarm(Integer alarmNumber)` - Resets an alarm and notifies the PLC via EventBus
  - `archiveAlarm(Long id, String user)` - Archives an alarm with user information and timestamps
- **Utility methods**: `pause(int pauseSec)` - Pauses execution for a specified number of seconds

## Flow
1. The class is typically instantiated as a singleton or dependency-injected component
2. Other components call its methods to perform common operations
3. The implementation interacts with the database and domain objects to fulfill requests
4. For alarm operations, it publishes events to the EventBus for system-wide notification
5. Error handling is implemented for critical operations

## Key Elements
- Logger for diagnostic information
- Implementation of all Helper interface methods
- Database interaction through domain model classes
- Event publishing for alarm management
- Carrier data transformation logic
- Error handling for database operations and parameter parsing

## Usage
```java
// Example: Create and populate a Carrier from CarrierMes data
Helper helper = new AbstractHelperImpl();
CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(123);
Carrier carrier = helper.populateCarrier(carrierMes);

// Example: Generate an alarm
helper.generateAlarm(100, 505); // location 100, alarm number 505

// Example: Reset an alarm
helper.resetAlarm(505);

// Example: Get parameter value
int maxRetries = helper.getParmValue("MAX_RETRIES");
```

## Debugging and Production Support

### Common Issues
1. **Database Connection Issues**: Methods that interact with the database may fail if connection issues occur
2. **Missing Data**: Methods like `findDieByNumber` may return null if the requested data doesn't exist
3. **Event Bus Failures**: Alarm reset operations may fail if the EventBus is not properly configured
4. **Parsing Errors**: Parameter value retrieval may fail if values cannot be parsed as integers
5. **Exception Handling**: Some methods use direct exception printing rather than proper logging

### Debugging Steps
1. Check the application logs for exceptions related to database operations
   ```java
   // Look for log entries like:
   // Cannot Parse: [parameter value]
   ```
2. Verify that the required data exists in the database (dies, stops, etc.)
3. Ensure the EventBus is properly configured and functioning
   ```java
   // Check if EventBus subscribers are receiving messages
   // Look for log entries related to alarm reset notifications
   ```
4. Add additional logging for parameter values that fail to parse
5. Monitor stack traces for exceptions in helper methods

### Resolution
- The class already includes error handling for parameter parsing
- Add additional error handling for database operations
- Implement retry logic for transient database issues
- Add fallback mechanisms for EventBus failures
- Replace direct exception printing with proper logging
- Consider adding validation for input parameters

### Monitoring
- Monitor database connection pool metrics
- Track alarm generation and reset operations
- Log carrier population failures with detailed information
- Monitor parameter retrieval failures
- Set up alerts for repeated exceptions in helper methods
- Track execution time of database operations to identify performance issues