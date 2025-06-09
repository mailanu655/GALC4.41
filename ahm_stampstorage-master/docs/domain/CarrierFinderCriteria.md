# CarrierFinderCriteria Technical Documentation

## Purpose
CarrierFinderCriteria.java provides a structured way to define search criteria for querying carriers in the stamp storage system. It encapsulates various filter parameters that can be used to locate carriers based on specific attributes, enabling flexible and precise carrier searches throughout the application.

## Logic/Functionality
- Defines a set of search criteria fields for carrier queries
- Supports filtering by carrier attributes such as number, location, status, etc.
- Includes BitInfo integration for maintenance-related searches
- Provides getter and setter methods for all search parameters
- Used by data access methods to construct database queries
- Enables complex, multi-criteria searches with a clean interface

## Flow
1. An instance of CarrierFinderCriteria is created
2. Search parameters are set based on the specific search requirements
3. The criteria object is passed to a finder method (typically in CarrierMes)
4. The finder method uses the criteria to construct a database query
5. The query is executed, and matching carriers are returned
6. The results can be further processed or displayed to users

## Key Elements
- `carrierNumber`: Filter by specific carrier number
- `die`: Filter by die/part loaded on the carrier
- `currentLocation`: Filter by current carrier location
- `destination`: Filter by carrier destination
- `carrierStatus`: Filter by carrier status
- `press`: Filter by associated press
- `productionRunNo`: Filter by production run number
- `bitInfo`: Filter by maintenance bit flags

## Usage
```java
// Create a new criteria object
CarrierFinderCriteria criteria = new CarrierFinderCriteria();

// Set search parameters
criteria.setCarrierNumber(1001);  // Specific carrier number
criteria.setDie(dieObject);       // Specific die
criteria.setCurrentLocation(locationStop);  // Current location
criteria.setCarrierStatus(CarrierStatus.LOADED);  // Carrier status

// Set maintenance bit criteria if needed
BitInfo bitInfo = new BitInfo();
bitInfo.setMaintRequired(true);  // Only carriers requiring maintenance
criteria.setBitInfo(bitInfo);

// Use the criteria to find carriers
List<CarrierMes> carriers = CarrierMes.findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(
    criteria, null, null);

// Process the results
for (CarrierMes carrier : carriers) {
    System.out.println("Found carrier: " + carrier.getCarrierNumber());
}
```

## Debugging and Production Support

### Common Issues
1. Empty search results when criteria are too restrictive
2. Too many results when criteria are too broad
3. Incorrect criteria combinations leading to unexpected results
4. Performance issues with complex criteria on large datasets
5. BitInfo criteria not properly applied in queries
6. Null parameter handling inconsistencies

### Debugging Steps
1. Verify criteria settings:
   ```java
   CarrierFinderCriteria criteria = new CarrierFinderCriteria();
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
   CarrierFinderCriteria criteria = new CarrierFinderCriteria();
   criteria.setCarrierNumber(1001);
   
   // Find carriers with just carrier number
   List<CarrierMes> carriers = CarrierMes.findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(
       criteria, null, null);
   
   System.out.println("Found " + carriers.size() + " carriers with number " + criteria.getCarrierNumber());
   
   // If successful, add more criteria incrementally
   criteria.setCarrierStatus(CarrierStatus.LOADED);
   carriers = CarrierMes.findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(
       criteria, null, null);
   
   System.out.println("Found " + carriers.size() + " carriers with number " + criteria.getCarrierNumber() + 
                      " and status " + criteria.getCarrierStatus());
   ```

3. Check BitInfo criteria:
   ```java
   CarrierFinderCriteria criteria = new CarrierFinderCriteria();
   
   // Set BitInfo criteria
   BitInfo bitInfo = criteria.getBitInfo();
   bitInfo.setMaintRequired(true);
   
   System.out.println("BitInfo settings:");
   System.out.println("Maintenance Required: " + bitInfo.getMaintRequired());
   System.out.println("Bit1: " + bitInfo.getShowBit1());
   System.out.println("Bit2: " + bitInfo.getShowBit2());
   
   // Test query with BitInfo criteria
   List<CarrierMes> carriers = CarrierMes.findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(
       criteria, null, null);
   
   System.out.println("Found " + carriers.size() + " carriers requiring maintenance");
   ```

### Resolution
- For empty results: Gradually relax criteria until results appear, then refine
- For too many results: Add additional criteria to narrow the search
- For incorrect combinations: Validate criteria combinations before executing queries
- For performance issues: Add pagination (page and size parameters) to limit result sets
- For BitInfo issues: Ensure BitInfo is properly initialized and set before querying
- For null handling: Implement consistent null checks in finder methods

### Monitoring
- Log search criteria used in production queries
- Track query execution times for different criteria combinations
- Monitor result set sizes to identify potential performance issues
- Alert on queries that return unusually large result sets
- Track frequency of different criteria combinations to optimize common searches
- Periodically review and optimize finder methods based on usage patterns