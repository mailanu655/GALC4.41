# RowMatchers Technical Documentation

## Purpose
`RowMatchers` is a utility class that provides various Hamcrest matcher implementations for `StorageRow` objects. These matchers are used for filtering and selecting storage rows based on different criteria such as capacity, carrier content, die numbers, and other properties. The class enables declarative, readable filtering of storage rows throughout the application.

## Logic/Functionality
The class implements numerous static factory methods that return `Matcher<StorageRow>` instances, each implementing specific matching logic:

### Capacity-related matchers:
- `hasMaxCapacityOf(int)`: Matches rows with a specific maximum capacity
- `isCurrentCapacityEmpty()`: Matches rows that are currently empty
- `isCurrentCapacityPartial()`: Matches rows that are partially filled
- `isCurrentCapacityFull()`: Matches rows that are completely full

### Die number-related matchers:
- `hasCarriersWithSameDieNumber()`: Matches rows where all carriers have the same die number
- `isLaneWithSingleDieNumberNotOf(Long)`: Matches rows with a single die number that is not the specified one
- `isLaneWithOnlySingleDieNumberOf(Long)`: Matches rows with only carriers of a specific die number
- `hasMixedDieNumbersIncluding(Long)`: Matches rows with multiple die numbers including the specified one

### Carrier position matchers:
- `hasCarrierAtTheLaneInWithDieNumber(Long)`: Matches rows with a carrier of a specific die number at the lane input
- `hasCarrierAtTheLaneOutWithDieNumber(Long)`: Matches rows with a carrier of a specific die number at the lane output
- `hasBlockedCarrierWithDieNumberAtTheLaneOut(Long)`: Matches rows where a carrier of a different die number is blocking the specified die number

### Carrier content matchers:
- `hasCarriersWithDieNumber(Long)`: Matches rows that have any carriers with the specified die number
- `hasEmptyCarriers()`: Matches rows that contain empty carriers
- `hasCarrierArrivedInLane()`: Matches rows where a carrier has arrived in the lane
- `hasCarrierWithProdRunNoAndCarrierStatusNormal(Integer)`: Matches rows with carriers of a specific production run number and normal status
- `hasCarrierWithDieAndCarrierStatusShippable(Long)`: Matches rows with carriers of a specific die number that are shippable

### Other matchers:
- `isLaneAvailable()`: Matches rows that are available for operations
- `inStorageArea(StorageArea)`: Matches rows in a specific storage area

## Flow
1. A client requests a specific matcher by calling one of the static factory methods
2. The method creates and returns an anonymous implementation of `TypeSafeMatcher<StorageRow>`
3. The client uses the matcher to filter or select storage rows
4. The matcher evaluates each row against its criteria and returns true or false
5. The client processes the matching rows as needed

## Key Elements
- Private constructor to prevent instantiation (utility class pattern)
- Static factory methods that return specialized matchers
- Anonymous inner classes implementing the `TypeSafeMatcher` interface
- Matching logic based on various StorageRow properties
- Description generation for debugging
- Constants for common values (e.g., ONE_PART_TYPE)

## Usage
```java
// Example: Find empty storage rows
List<StorageRow> rows = getStorageRows();
Matcher<StorageRow> emptyMatcher = RowMatchers.isCurrentCapacityEmpty();
List<StorageRow> emptyRows = new ArrayList<>();
for (StorageRow row : rows) {
    if (emptyMatcher.matches(row)) {
        emptyRows.add(row);
    }
}

// Example: Find rows with a specific die number
Matcher<StorageRow> dieMatcher = RowMatchers.hasCarriersWithDieNumber(123L);
List<StorageRow> rowsWithDie = new ArrayList<>();
for (StorageRow row : rows) {
    if (dieMatcher.matches(row)) {
        rowsWithDie.add(row);
    }
}

// Example: Use with PredicateWrapper for Guava filtering
Predicate<StorageRow> predicate = new PredicateWrapper<>(
    RowMatchers.isLaneWithOnlySingleDieNumberOf(456L));
Iterable<StorageRow> filteredRows = Iterables.filter(rows, predicate);
```

## Debugging and Production Support

### Common Issues
1. **Complex Matching Logic**: Some matchers have complex logic that may be difficult to understand
2. **Performance Impact**: Excessive use of matchers in tight loops can impact performance
3. **Null Handling**: Some matchers may not handle null properties correctly
4. **Description Clarity**: Matcher descriptions may not clearly explain why a match failed
5. **Inconsistent Results**: Matchers may produce inconsistent results if StorageRow implementation changes

### Debugging Steps
1. Use the matcher's `describeTo` method to understand its criteria
   ```java
   Description desc = new StringDescription();
   matcher.describeTo(desc);
   logger.debug("Matcher criteria: {}", desc.toString());
   ```
2. Log the properties of StorageRow objects that unexpectedly match or don't match
   ```java
   for (StorageRow row : rows) {
       boolean matches = matcher.matches(row);
       logger.debug("Row {}: matches={}, dieNumbers={}, capacity={}/{}",
           row.getId(), matches, row.getDieNumbersForAllCarriers(),
           row.getCurrentCarrierCount(), row.getCapacity());
   }
   ```
3. Check for null properties that might affect matching
   ```java
   if (row.getDieNumbersForAllCarriers() == null) {
       logger.warn("Row {} has null dieNumbers collection", row.getId());
   }
   ```
4. Review the matcher implementation for logical errors

### Resolution
- Enhance null handling in matcher implementations
- Improve matcher descriptions for better debugging
- Consider caching matcher results for frequently used matchers
- Add more specific matchers for complex conditions
- Implement defensive programming in matchers to handle edge cases

### Monitoring
- Monitor matcher application performance in performance-critical paths
- Log unexpected matching results with detailed information
- Track the frequency of matcher usage to identify potential optimization opportunities
- Consider adding metrics for filter operation performance
- Periodically review matcher implementations against StorageRow implementation changes