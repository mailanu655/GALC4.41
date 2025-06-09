# RowComparators Technical Documentation

## Purpose
`RowComparators` is a utility class that provides various comparator implementations for `StorageRow` objects. These comparators are essential for sorting and prioritizing storage rows based on different criteria such as carrier counts, die numbers, and row identifiers.

## Logic/Functionality
The class implements several static factory methods that return `Comparator<StorageRow>` instances, each implementing a specific comparison logic:

- `getComparatorByCarriersCountOfDieNumberAtLaneOut`: Compares rows based on the count of carriers with a specific die number at the lane output end
- `getComparatorByCarriersCountOfDieNumberAtLaneIn`: Compares rows based on the count of carriers with a specific die number at the lane input end
- `getComparatorByDieNumberBlockingCarriersCountAtLaneOut`: Compares rows based on the count of carriers blocking a specific die number at the lane output end
- `getComparatorByCurrentCarrierCount`: Compares rows based on their total current carrier count
- `getComparatorByRowStopId`: Compares rows based on their stop IDs

## Flow
1. A client requests a specific comparator by calling one of the static factory methods
2. The method creates and returns an anonymous implementation of `Comparator<StorageRow>`
3. The client uses the comparator to sort or prioritize storage rows
4. The comparator is used in conjunction with Java's sorting methods or custom sorting logic

## Key Elements
- Private constructor to prevent instantiation (utility class pattern)
- Static factory methods that return specialized comparators
- Anonymous inner classes implementing the `Comparator` interface
- Comparison logic based on various StorageRow properties
- Natural ordering using the `compareTo` method of Integer and Long classes

## Usage
```java
// Example: Sort rows by carrier count
List<StorageRow> rows = getStorageRows();
Collections.sort(rows, RowComparators.getComparatorByCurrentCarrierCount());

// Example: Find row with minimum carriers of a specific die number
Long dieNumber = 123L;
Collections.min(rows, RowComparators.getComparatorByCarriersCountOfDieNumberAtLaneOut(dieNumber));
```

## Debugging and Production Support

### Common Issues
1. **Unexpected Sorting Results**: Comparators may not sort as expected if the StorageRow properties return null values
2. **Performance Issues**: Excessive sorting of large collections can impact system performance
3. **NullPointerExceptions**: May occur if comparing StorageRow objects with null properties
4. **Inconsistent Comparison Logic**: If the comparison logic doesn't establish a total ordering, sorting may be inconsistent

### Debugging Steps
1. Verify the StorageRow objects have valid property values by logging them before sorting
   ```java
   for (StorageRow row : rows) {
       logger.debug("Row {}: carriers count={}", row.getId(), row.getCurrentCarrierCount());
   }
   ```
2. Check that the correct comparator is being used for the intended sorting logic
3. Add logging within the compare method to trace comparison values
4. Verify that the StorageRow implementation correctly calculates the counts used in comparisons

### Resolution
- Add null checks in the comparator implementations
- Ensure StorageRow objects are properly initialized before sorting
- Consider caching comparison results for frequently sorted collections
- Implement defensive programming in the StorageRow methods that provide data to comparators

### Monitoring
- Monitor sorting operation execution time if performance issues are suspected
- Log unexpected sorting results with detailed information about the compared objects
- Track the frequency of comparator usage to identify potential optimization opportunities