# CarrierHistoryFinderCriteria Technical Documentation

## Purpose
CarrierHistoryFinderCriteria.java provides a structured way to define search criteria for querying historical carrier records in the stamp storage system. It encapsulates various filter parameters that can be used to locate carrier history records based on specific attributes, enabling flexible and precise historical data searches.

## Logic/Functionality
- Defines a set of search criteria fields for carrier history queries
- Supports filtering by carrier attributes such as number, die, location, etc.
- Provides getter and setter methods for all search parameters
- Used by data access methods to construct database queries for historical data
- Enables complex, multi-criteria searches with a clean interface
- Facilitates reporting and analysis of historical carrier movements

## Flow
1. An instance of CarrierHistoryFinderCriteria is created
2. Search parameters are set based on the specific search requirements
3. The criteria object is passed to a finder method in CarrierHistory
4. The finder method uses the criteria to construct a database query
5. The query is executed, and matching carrier history records are returned
6. The results can be further processed for reporting or analysis

## Key Elements
- `carrierNumber`: Filter by specific carrier number
- `die`: Filter by die/part loaded on the carrier
- `currentLocation`: Filter by carrier location at the time of recording
- `destination`: Filter by carrier destination at the time of recording
- `carrierStatus`: Filter by carrier status at the time of recording
- `press`: Filter by associated press
- `productionRunNo`: Filter by production run number

## Usage
```java
// Create a new criteria object
CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();

// Set search parameters
criteria.setCarrierNumber(1001);  // Specific carrier number
criteria.setDie(dieObject);       // Specific die
criteria.setCurrentLocation(locationStop);  // Location at time of recording
criteria.setCarrierStatus(CarrierStatus.LOADED);  // Status at time of recording

// Use the criteria to find carrier history records
List<CarrierHistory> historyRecords = CarrierHistory.findCarrierHistoryByCarrierNumber(
    criteria, 1, 100);  // Page 1, 100 records per page

// Get total count of matching records
Long totalCount = CarrierHistory.getFindCarrierHistoryCount(criteria);

// Process the results
System.out.println("Found " + historyRecords.size() + " records out of " + totalCount + " total matches");
for (CarrierHistory record : historyRecords) {
    System.out.println("Timestamp: " + record.getUpdateDate());
    System.out.println("Location: " + record.getCurrentLocation().getName());
    System.out.println("Status: " + record.getStatus());
    System.out.println("---");
}
```

## Debugging and Production Support

### Common Issues
1. Empty search results when criteria are too restrictive
2. Too many results when criteria are too broad
3. Incorrect criteria combinations leading to unexpected results
4. Performance issues with complex criteria on large history datasets
5. Null parameter handling inconsistencies
6. Date range filtering not working as expected

### Debugging Steps
1. Verify criteria settings:
   ```java
   CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();
   criteria.setCarrierNumber(1001);
   
   System.out.println("Criteria settings:");
   System.out.println("Carrier Number: " + criteria.getCarrierNumber());
   System.out.println("Die: " + criteria.getDie());
   System.out.println("Current Location: " + criteria.getCurrentLocation());
   System.out.println("Destination: " + criteria.getDestination());
   System.out.println("Carrier Status: " + criteria.getCarrierStatus());
   System.out.println("Press: " + criteria.getPress());
   System.out.println("Production Run: " + criteria.getProductionRunNo());
   ```

2. Test with simplified criteria:
   ```java
   // Start with minimal criteria
   CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();
   criteria.setCarrierNumber(1001);
   
   // Find history records with just carrier number
   List<CarrierHistory> historyRecords = CarrierHistory.findCarrierHistoryByCarrierNumber(
       criteria, 1, 100);
   
   System.out.println("Found " + historyRecords.size() + " history records with carrier number " + 
                      criteria.getCarrierNumber());
   
   // If successful, add more criteria incrementally
   criteria.setCarrierStatus(CarrierStatus.LOADED);
   historyRecords = CarrierHistory.findCarrierHistoryByCarrierNumber(
       criteria, 1, 100);
   
   System.out.println("Found " + historyRecords.size() + " history records with carrier number " + 
                      criteria.getCarrierNumber() + " and status " + criteria.getCarrierStatus());
   ```

3. Check query generation:
   ```java
   // Create criteria
   CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();
   criteria.setCarrierNumber(1001);
   criteria.setDie(dieObject);
   
   // Log SQL query (requires access to query logging)
   // Enable SQL logging in your logging configuration
   
   // Execute query
   List<CarrierHistory> historyRecords = CarrierHistory.findCarrierHistoryByCarrierNumber(
       criteria, 1, 100);
   
   // Check results
   System.out.println("Query returned " + historyRecords.size() + " results");
   ```

4. Test pagination:
   ```java
   CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();
   criteria.setCarrierNumber(1001);
   
   // Get total count
   Long totalCount = CarrierHistory.getFindCarrierHistoryCount(criteria);
   System.out.println("Total matching records: " + totalCount);
   
   // Test pagination
   int pageSize = 10;
   int totalPages = (int) Math.ceil(totalCount / (double) pageSize);
   
   for (int page = 1; page <= Math.min(totalPages, 3); page++) {
       List<CarrierHistory> historyRecords = CarrierHistory.findCarrierHistoryByCarrierNumber(
           criteria, page, pageSize);
       
       System.out.println("Page " + page + ": " + historyRecords.size() + " records");
       
       // Print first record on each page
       if (!historyRecords.isEmpty()) {
           CarrierHistory first = historyRecords.get(0);
           System.out.println("  First record timestamp: " + first.getUpdateDate());
       }
   }
   ```

### Resolution
- For empty results: Gradually relax criteria until results appear, then refine
- For too many results: Add additional criteria to narrow the search
- For incorrect combinations: Validate criteria combinations before executing queries
- For performance issues: Ensure proper indexing on the history table and use pagination
- For null handling: Implement consistent null checks in finder methods
- For date range issues: Ensure proper timestamp formatting and range boundaries

### Monitoring
- Log search criteria used in production queries
- Track query execution times for different criteria combinations
- Monitor result set sizes to identify potential performance issues
- Alert on queries that return unusually large result sets
- Track frequency of different criteria combinations to optimize common searches
- Monitor history table growth and implement archiving strategies if needed
- Set up periodic data quality checks to ensure history record integrity