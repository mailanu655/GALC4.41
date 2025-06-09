# StorageStateContextHelperImpl Technical Documentation

## Purpose
`StorageStateContextHelperImpl` is a concrete implementation of the `StorageStateContextHelper` interface that extends `AbstractHelperImpl`. It provides specialized functionality for the storage state context, particularly for finding carriers in storage and checking space availability at stops. This class serves as a key component in maintaining an accurate representation of the storage system.

## Logic/Functionality
The class extends `AbstractHelperImpl` and implements the additional methods defined in `StorageStateContextHelper`:

### Carrier Finding
- `findAllCarriersInStorage()`: Retrieves all carriers currently in the storage system by:
  1. Finding all storage rows in the system
  2. Extracting the stop numbers from these rows
  3. Querying the database for all carriers with destinations matching these stop numbers

### Space Availability
- `spaceAvailable(Stop stop)`: Determines if space is available at a specific stop by:
  1. Counting the carriers with the specified stop as their destination
  2. Finding all storage rows in the A_AREA
  3. Calculating the total available capacity in non-blocked A_AREA rows
  4. Comparing the carrier count to the available capacity

Additionally, it inherits all implemented methods from `AbstractHelperImpl`:
- Audit log management
- Parameter value retrieval
- Die object retrieval
- Stop retrieval
- StorageRow retrieval
- Carrier population
- Alarm management
- Utility methods

## Flow
1. The class is typically instantiated as a singleton or dependency-injected component
2. The storage state context uses this helper to find carriers and check space availability
3. The implementation interacts with the database and domain objects to fulfill requests
4. The results are used to maintain an accurate storage state

## Key Elements
- Extension of AbstractHelperImpl
- Implementation of StorageStateContextHelper interface
- Logger for diagnostic information
- Database interaction through domain model classes
- Capacity calculation logic for space availability
- Carrier query logic for finding carriers in storage

## Usage
```java
// Example: Create a StorageStateContextHelperImpl instance
StorageStateContextHelper helper = new StorageStateContextHelperImpl();

// Example: Find all carriers in storage
List<CarrierMes> carriers = helper.findAllCarriersInStorage();
System.out.println("Found " + carriers.size() + " carriers in storage");

// Example: Check if space is available at a stop
Stop stop = Stop.findStop(123L);
boolean spaceAvailable = helper.spaceAvailable(stop);
System.out.println("Space available at stop " + stop.getId() + ": " + spaceAvailable);

// Example: Use inherited methods
Die emptyDie = helper.getEmptyDie();
List<StorageRow> rows = helper.findAllStorageRowsByStorageArea(StorageArea.A_AREA);
```

## Debugging and Production Support

### Common Issues
1. **Database Connectivity**: Methods that interact with the database may fail if connection issues occur
2. **Performance Issues**: Finding all carriers in storage may be resource-intensive
3. **Capacity Calculation Errors**: Space availability calculations may be incorrect
4. **Blocked Rows**: Blocked rows may not be properly excluded from capacity calculations
5. **Type Conversion Issues**: Converting between long and int values may cause issues with large numbers

### Debugging Steps
1. Check database connectivity for carrier and row queries
   ```java
   try {
       List<StorageRow> rows = StorageRow.findAllStorageRows();
       logger.debug("Found {} storage rows", rows.size());
       
       List<Long> stopNumbers = new ArrayList<>();
       for (StorageRow row : rows) {
           stopNumbers.add(row.getStop().getId());
       }
       
       List<CarrierMes> carriers = CarrierMes.findAllCarriersWithDestinationIn(stopNumbers);
       logger.debug("Found {} carriers with destinations in storage", carriers.size());
   } catch (Exception e) {
       logger.error("Failed to find carriers in storage", e);
   }
   ```
2. Monitor performance of carrier finding operations
   ```java
   long startTime = System.currentTimeMillis();
   List<CarrierMes> carriers = helper.findAllCarriersInStorage();
   long duration = System.currentTimeMillis() - startTime;
   logger.debug("Finding {} carriers took {} ms", carriers.size(), duration);
   ```
3. Verify space availability calculations
   ```java
   long count = CarrierMes.countCarriersWithDestinationStop(stop);
   logger.debug("Carriers with destination {}: {}", stop.getId(), count);
   
   List<StorageRow> rowsInA = StorageRow.findStorageRowsByArea(StorageArea.A_AREA);
   int capacity = 0;
   for (StorageRow row : rowsInA) {
       if (!row.isBlocked()) {
           long carrierCount = CarrierMes.countCarriersWithDestinationStop(row.getStop());
           int availableCapacity = row.getCapacity() - (int)carrierCount;
           if (availableCapacity > 0) {
               capacity += availableCapacity;
           }
           logger.debug("Row {}: capacity={}, carriers={}, available={}",
               row.getId(), row.getCapacity(), carrierCount, availableCapacity);
       } else {
           logger.debug("Row {} is blocked and excluded from capacity calculation", row.getId());
       }
   }
   
   logger.debug("Total available capacity in A_AREA: {}, carriers to store: {}", capacity, count);
   ```
4. Check for type conversion issues
   ```java
   long carrierCount = CarrierMes.countCarriersWithDestinationStop(row.getStop());
   if (carrierCount > Integer.MAX_VALUE) {
       logger.warn("Carrier count exceeds integer maximum: {}", carrierCount);
   }
   int intCarrierCount = (int)carrierCount;
   ```

### Resolution
- Implement connection pooling for database operations
- Optimize carrier finding operations for performance
- Enhance space availability calculations with better error handling
- Add explicit handling for blocked rows
- Use long instead of int for capacity calculations to avoid overflow
- Consider caching frequently accessed data
- Add more detailed logging for troubleshooting

### Monitoring
- Track database operation performance
- Monitor space availability calculations
- Log capacity utilization in storage areas
- Alert on persistent database connectivity issues
- Track the number of carriers in storage
- Monitor the number of blocked rows
- Track capacity utilization trends over time