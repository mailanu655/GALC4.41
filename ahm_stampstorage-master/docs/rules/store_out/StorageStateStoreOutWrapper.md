# StorageStateStoreOutWrapper Technical Documentation

## Purpose
The `StorageStateStoreOutWrapper` class provides a wrapper around the storage state to facilitate store-out operations in the stamp storage system. It encapsulates the logic for finding appropriate storage rows from which to retrieve carriers based on various criteria such as die number, storage area, and lane condition.

## Logic/Functionality
- Wraps the storage state to provide specialized query methods for store-out operations
- Provides methods to find appropriate storage rows based on different criteria
- Handles queries for partial rows, full rows, mixed front rows, and mixed blocked rows
- Supports filtering by storage area and die number
- Uses row matchers and comparators to find and sort storage rows
- Prioritizes retrieving carriers with the oldest production run

## Flow
1. The class is instantiated with a reference to the storage state context
2. Store-out rules call the wrapper's methods to find appropriate storage rows
3. The wrapper determines the oldest production run for a given die
4. The wrapper queries the storage state using row matchers to filter rows
5. The wrapper sorts the filtered rows using comparators to find the most appropriate row
6. The selected row is returned to the calling rule for further processing

## Key Elements
- **getStorageState()**: Retrieves the storage state from the context
- **getOldestProductionRunNo()**: Determines the oldest production run number for a given die
- **getRowsWithOldestProductionRunPartCarriers()**: Finds rows containing carriers with the oldest production run
- **getStorageRowsWithOldestProductionRunPartCarriersForStorageArea()**: Finds rows in a specific area with the oldest production run
- **getAppropriatePartialRowForDie()**: Finds a partial row containing carriers with a specific die
- **getAppropriateMixedFrontRowForDie()**: Finds a mixed row with a specific die at the front
- **getAppropriateFullRowForDie()**: Finds a full row containing carriers with a specific die
- **getAppropriateMixedBlockedRowForDie()**: Finds a mixed row with blocked carriers of a specific die
- **getStorageRowsByArea()**: Retrieves all storage rows in a specific area

## Usage
This class is used by store-out rules to find appropriate storage rows for retrieving carriers:

```java
// Example of how this class would be used
StorageStateContext storageStateContext = new StorageStateContextImpl();
StorageStateStoreOutWrapper wrapper = new StorageStateStoreOutWrapper(storageStateContext);

// Find an appropriate full row for a specific die in a storage area
Long dieId = 123L;
Die die = Die.findDie(dieId);
StorageArea area = StorageArea.C_HIGH;
List<StorageRow> rows = wrapper.getStorageRowsWithOldestProductionRunPartCarriersForStorageArea(die, area);
StorageRow row = wrapper.getAppropriateFullRowForDie(dieId, rows);
if (row != null) {
    // Retrieve a carrier from the selected row
}
```

## Debugging and Production Support

### Common Issues
1. **No appropriate rows found**: System may not find suitable rows for retrieving carriers
2. **Incorrect row selection**: System may select inappropriate rows based on criteria
3. **Performance issues with large result sets**: Queries and sorting may be inefficient for large numbers of rows
4. **Stale storage state**: Storage state may be out of sync with actual carrier positions
5. **Production run determination issues**: System may not correctly determine the oldest production run

### Debugging Steps
1. **For no appropriate rows found**:
   - Check if rows matching the criteria exist in the storage system
   - Verify that the row matchers are correctly filtering rows
   - Examine the storage state to ensure it reflects actual row conditions
   - Check for any exceptions during query execution

2. **For incorrect row selection**:
   - Review the row matchers to ensure they correctly identify rows
   - Check if the comparators are sorting rows as expected
   - Verify that the selection logic chooses the most appropriate row

3. **For performance issues**:
   - Analyze query execution for inefficient operations
   - Consider optimizing row matchers and comparators
   - Look for opportunities to cache frequently accessed data

4. **For production run determination issues**:
   - Check if the `getOldestProductionRunNo()` method is working correctly
   - Verify that carriers have valid production run numbers
   - Examine the logic for comparing production run timestamps

### Resolution
1. **For no appropriate rows found**:
   - Add additional logging to track row selection criteria
   - Implement fallback mechanisms for when no rows are found
   - Enhance error handling to provide better feedback

2. **For incorrect row selection**:
   - Update the row matchers and comparators
   - Add validation to ensure correct row selection
   - Enhance logging to track row selection decisions

3. **For performance issues**:
   - Optimize queries to reduce processing time
   - Implement caching for frequently accessed data
   - Consider batch processing for large operations

4. **For production run determination issues**:
   - Enhance the production run determination logic
   - Add validation for production run numbers and timestamps
   - Implement fallback mechanisms for when production run information is missing

### Monitoring
1. **Track row selection operations**: Monitor the frequency and success of row selection
2. **Alert on row selection failures**: Set up alerts for when appropriate rows cannot be found
3. **Monitor query performance**: Track the performance of row queries and sorting
4. **Log row selection decisions**: Maintain detailed logs of row selection for troubleshooting
5. **Monitor production run determination**: Track the accuracy of oldest production run determination