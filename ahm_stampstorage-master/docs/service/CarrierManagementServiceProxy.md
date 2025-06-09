# CarrierManagementServiceProxy Technical Documentation

## Purpose
The `CarrierManagementServiceProxy` class serves as a proxy implementation of the `CarrierManagementService` interface. It extends the functionality of the base service implementation by adding additional operations related to carrier management, particularly focusing on operations that require interaction with external systems or that need to be executed asynchronously. This proxy pattern allows for separation of concerns between core service functionality and extended operations.

## Logic/Functionality
The proxy implementation provides several key functionalities beyond the base service:

1. **Carrier Saving**: Methods to save carrier information to the database and external systems
2. **Carrier Release Management**: Methods to release carriers from storage rows
3. **Carrier Destination Recalculation**: Methods to recalculate carrier destinations
4. **Row Management**: Methods to update row information and reorder carriers in rows
5. **Storage State Management**: Methods to reset and refresh the storage state
6. **Bulk Carrier Operations**: Methods to perform operations on multiple carriers simultaneously
7. **Alarm Management**: Methods to clear alarms in external systems

## Flow
The `CarrierManagementServiceProxy` operates in the following workflow:

1. The class is instantiated by Spring and initialized with the base service implementation
2. Controllers and other services inject this proxy through the `CarrierManagementService` interface
3. The proxy delegates basic operations to the base service implementation
4. For extended operations, the proxy implements additional logic or makes calls to external systems
5. The proxy may use asynchronous processing for operations that don't require immediate response

## Key Elements
- **saveCarrier**: Saves carrier information to the database and external systems
- **saveCarriersInToRow**: Saves multiple carriers into a storage row
- **addCarrierToRow**: Adds a carrier to a specific position in a storage row
- **removeCarrierFromRow**: Removes a carrier from a storage row
- **recalculateCarrierDestination**: Recalculates the destination for a carrier
- **updateRow**: Updates row information in the database
- **reorderCarriersInRow**: Reorders carriers within a storage row
- **resetStorageState**: Resets the storage state from the database
- **refreshStorageState**: Refreshes the storage state from the database
- **releaseCarriers**: Releases carriers from storage rows
- **releaseEmptyCarriersFromRows**: Releases empty carriers from storage rows
- **sendBulkCarrierStatusUpdate**: Updates the status of multiple carriers simultaneously
- **clearAlarm**: Clears an alarm in external systems

## Usage
The `CarrierManagementServiceProxy` is used in the following scenarios:

1. **Carrier Saving**: When saving carrier information
   ```java
   @Autowired
   CarrierManagementServiceProxy carrierManagementServiceProxy;
   
   public String update(Carrier carrier) {
       carrierManagementServiceProxy.saveCarrier(carrier);
       return "redirect:/carriers";
   }
   ```

2. **Row Management**: When updating row information or reordering carriers
   ```java
   carrierManagementServiceProxy.updateRow(row);
   ```

3. **Carrier Release**: When releasing carriers from storage rows
   ```java
   carrierManagementServiceProxy.releaseCarriers(row, count, destination, source);
   ```

4. **Storage State Management**: When resetting or refreshing the storage state
   ```java
   carrierManagementServiceProxy.resetStorageState();
   ```

5. **Bulk Carrier Operations**: When updating multiple carriers simultaneously
   ```java
   carrierManagementServiceProxy.sendBulkCarrierStatusUpdate(carrierList, status, user);
   ```

## Database Tables
The `CarrierManagementServiceProxy` interacts with the following database tables:

1. **CARRIER_MES**: Stores carrier information including carrier number, location, status, and maintenance bits
2. **CARRIER_RELEASE**: Stores information about carriers scheduled for release
3. **STORAGE_ROW**: Stores information about storage rows including capacity and area
4. **VALID_DESTINATION**: Stores valid destination information for stops
5. **ALARM_EVENT**: Stores alarm event information
6. **ALARM_DEFINITION**: Stores alarm definition information

## Debugging and Production Support

### Common Issues
1. **Carrier Save Failures**: Failures when saving carrier information
2. **Row Update Failures**: Failures when updating row information
3. **Carrier Release Failures**: Failures when releasing carriers from storage rows
4. **Storage State Reset Failures**: Failures when resetting the storage state
5. **Bulk Carrier Update Failures**: Failures when updating multiple carriers simultaneously
6. **Alarm Clear Failures**: Failures when clearing alarms in external systems
7. **Carrier Destination Recalculation Errors**: Errors when recalculating carrier destinations

### Debugging Steps
1. **Carrier Save Failures**:
   - Check the implementation of `saveCarrier` method
   - Verify the carrier information being saved
   - Check for database connection issues
   - Review logs for save operation errors
   - Test with known carrier information

2. **Row Update Failures**:
   - Check the implementation of `updateRow` method
   - Verify the row information being updated
   - Check for database connection issues
   - Review logs for update operation errors
   - Test with known row information

3. **Carrier Release Failures**:
   - Check the implementation of `releaseCarriers` method
   - Verify the carrier and destination information
   - Check for database connection issues
   - Review logs for release operation errors
   - Test with known carrier and destination information

4. **Storage State Reset Failures**:
   - Check the implementation of `resetStorageState` method
   - Verify the database state for carriers
   - Check for database connection issues
   - Review logs for reset operation errors
   - Test with a controlled storage state reset

5. **Bulk Carrier Update Failures**:
   - Check the implementation of `sendBulkCarrierStatusUpdate` method
   - Verify the carrier list and status information
   - Check for database connection issues
   - Review logs for bulk update operation errors
   - Test with known carrier list and status information

### Resolution
1. **Carrier Save Failures**:
   - Fix save operation logic in `saveCarrier`
   - Add validation for carrier information
   - Implement retry mechanisms for database operations
   - Enhance logging for save operations
   - Add error handling for save operation edge cases

2. **Row Update Failures**:
   - Fix update operation logic in `updateRow`
   - Add validation for row information
   - Implement retry mechanisms for database operations
   - Enhance logging for update operations
   - Add error handling for update operation edge cases

3. **Carrier Release Failures**:
   - Fix release operation logic in `releaseCarriers`
   - Add validation for carrier and destination information
   - Implement retry mechanisms for database operations
   - Enhance logging for release operations
   - Add error handling for release operation edge cases

4. **Storage State Reset Failures**:
   - Fix reset operation logic in `resetStorageState`
   - Add validation for storage state
   - Implement retry mechanisms for database operations
   - Enhance logging for reset operations
   - Add error handling for reset operation edge cases

5. **Bulk Carrier Update Failures**:
   - Fix bulk update operation logic in `sendBulkCarrierStatusUpdate`
   - Add validation for carrier list and status information
   - Implement retry mechanisms for database operations
   - Enhance logging for bulk update operations
   - Add error handling for bulk update operation edge cases

### Monitoring
1. **Carrier Save Operations**: Monitor the success rate of carrier save operations
   - Log carrier save operations with carrier numbers
   - Track save operation errors and failures
   - Alert on repeated save operation failures

2. **Row Update Operations**: Monitor the success rate of row update operations
   - Log row update operations with row IDs
   - Track update operation errors and failures
   - Alert on repeated update operation failures

3. **Carrier Release Operations**: Monitor the success rate of carrier release operations
   - Log carrier release operations with carrier numbers and destinations
   - Track release operation errors and failures
   - Alert on repeated release operation failures

4. **Storage State Reset Operations**: Monitor the success rate of storage state reset operations
   - Log storage state reset operations with timestamps
   - Track reset operation errors and failures
   - Alert on repeated reset operation failures

5. **Bulk Carrier Update Operations**: Monitor the success rate of bulk carrier update operations
   - Log bulk update operations with carrier counts and statuses
   - Track bulk update operation errors and failures
   - Alert on repeated bulk update operation failures

6. **Alarm Clear Operations**: Monitor the success rate of alarm clear operations
   - Log alarm clear operations with alarm numbers
   - Track clear operation errors and failures
   - Alert on repeated clear operation failures

7. **Carrier Destination Recalculation Operations**: Monitor the success rate of destination recalculation operations
   - Log destination recalculation operations with carrier numbers
   - Track recalculation operation errors and failures
   - Alert on repeated recalculation operation failures

Implement logging and metrics collection for these aspects to enable proactive monitoring and issue detection.