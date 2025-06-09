# GroupHoldFinderCriteria Technical Documentation

## Purpose
GroupHoldFinderCriteria.java defines a criteria class used for searching and filtering carriers in the system based on production run information, storage location, and other parameters. This class is specifically designed to support group hold operations, where multiple carriers need to be identified and processed together based on common characteristics such as production run number, date, or location.

## Logic/Functionality
- Serves as a data container for search criteria related to group hold operations
- Provides properties for filtering carriers by production run number, date, storage row, and other attributes
- Supports searching for carriers before or after a specific production run date
- Allows filtering by carrier status and press (robot) information
- Enables searching for carriers in specific storage rows
- Provides option to combine row and production run criteria for more targeted searches

## Flow
1. An instance of GroupHoldFinderCriteria is created and populated with search parameters
2. The criteria object is passed to search methods in the CarrierMes class
3. The search methods use the criteria to construct database queries
4. The queries return carriers matching the specified criteria
5. The matching carriers can then be processed for group hold operations

## Key Elements
- `productionRunNumber`: The production run number to search for
- `productionRunDate`: The production run date to use as a reference point
- `row`: The storage row to search in
- `numberAfterRunDate`: The number of carriers to retrieve after the specified production run date
- `numberBeforeRunDate`: The number of carriers to retrieve before the specified production run date
- `status`: The carrier status to filter by
- `robot`: The press (robot) to filter by
- `rowAndProdRun`: Boolean flag indicating whether to combine row and production run criteria

## Usage
```java
// Create a criteria object to find carriers from a specific production run
GroupHoldFinderCriteria criteria = new GroupHoldFinderCriteria();
criteria.setProductionRunNumber(12345);

// Find carriers matching the criteria
List<CarrierMes> carriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
    criteria, null, null);

System.out.println("Found " + carriers.size() + " carriers for production run " + 
                  criteria.getProductionRunNumber());

// Create a criteria object to find carriers before and after a specific date
GroupHoldFinderCriteria dateCriteria = new GroupHoldFinderCriteria();
dateCriteria.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
dateCriteria.setNumberBeforeRunDate(5); // Get 5 carriers before the date
dateCriteria.setNumberAfterRunDate(5);  // Get 5 carriers after the date

// Find carriers matching the date criteria
List<CarrierMes> dateCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
    dateCriteria, null, null);

System.out.println("Found " + dateCarriers.size() + 
                  " carriers around the specified production date");

// Create a criteria object to find carriers in a specific row with a specific status
GroupHoldFinderCriteria rowCriteria = new GroupHoldFinderCriteria();
StorageRow targetRow = StorageRow.findStorageRow(101L);
rowCriteria.setRow(targetRow);
rowCriteria.setStatus(CarrierStatus.ON_HOLD);
rowCriteria.setRowAndProdRun(true); // Combine row and production run criteria

// Find carriers matching the row criteria
List<CarrierMes> rowCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
    rowCriteria, null, null);

System.out.println("Found " + rowCarriers.size() + 
                  " carriers in row " + targetRow.getRowName() + 
                  " with status " + rowCriteria.getStatus());

// Create a criteria object to find carriers from a specific robot (press)
GroupHoldFinderCriteria robotCriteria = new GroupHoldFinderCriteria();
robotCriteria.setRobot(Press.PRESS_1);

// Find carriers matching the robot criteria
List<CarrierMes> robotCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
    robotCriteria, null, null);

System.out.println("Found " + robotCarriers.size() + 
                  " carriers from robot " + robotCriteria.getRobot());

// Count carriers matching criteria
Long count = CarrierMes.getGroupHoldCarrierCount(criteria);
System.out.println("Total count of carriers matching criteria: " + count);
```

## Debugging and Production Support

### Common Issues
1. No carriers found when criteria are too restrictive
2. Too many carriers found when criteria are too broad
3. Incorrect combination of criteria leading to unexpected results
4. Date-based searches returning incorrect results due to timestamp formatting
5. Row-based searches failing due to invalid row references
6. Performance issues with complex criteria combinations
7. Inconsistent results when using the same criteria in different contexts

### Debugging Steps
1. Verify criteria values:
   ```java
   // Check the values in a criteria object
   GroupHoldFinderCriteria criteria = new GroupHoldFinderCriteria();
   criteria.setProductionRunNumber(12345);
   criteria.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
   criteria.setNumberBeforeRunDate(5);
   criteria.setNumberAfterRunDate(5);
   
   System.out.println("Criteria values:");
   System.out.println("  Production Run Number: " + criteria.getProductionRunNumber());
   System.out.println("  Production Run Date: " + criteria.getProductionRunDate());
   System.out.println("  Number Before Run Date: " + criteria.getNumberBeforeRunDate());
   System.out.println("  Number After Run Date: " + criteria.getNumberAfterRunDate());
   System.out.println("  Row: " + (criteria.getRow() != null ? 
                                  criteria.getRow().getRowName() : "null"));
   System.out.println("  Status: " + criteria.getStatus());
   System.out.println("  Robot: " + criteria.getRobot());
   System.out.println("  Row And Prod Run: " + criteria.getRowAndProdRun());
   ```

2. Test with simplified criteria:
   ```java
   // Test with simplified criteria to isolate issues
   GroupHoldFinderCriteria simpleCriteria = new GroupHoldFinderCriteria();
   
   // Test with just production run number
   simpleCriteria.setProductionRunNumber(12345);
   List<CarrierMes> runCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       simpleCriteria, null, null);
   System.out.println("Found " + runCarriers.size() + 
                     " carriers with production run " + simpleCriteria.getProductionRunNumber());
   
   // Reset and test with just row
   simpleCriteria = new GroupHoldFinderCriteria();
   StorageRow targetRow = StorageRow.findStorageRow(101L);
   simpleCriteria.setRow(targetRow);
   simpleCriteria.setRowAndProdRun(true);
   List<CarrierMes> rowOnlyCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       simpleCriteria, null, null);
   System.out.println("Found " + rowOnlyCarriers.size() + 
                     " carriers in row " + targetRow.getRowName());
   
   // Reset and test with just robot
   simpleCriteria = new GroupHoldFinderCriteria();
   simpleCriteria.setRobot(Press.PRESS_1);
   List<CarrierMes> robotOnlyCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       simpleCriteria, null, null);
   System.out.println("Found " + robotOnlyCarriers.size() + 
                     " carriers from robot " + simpleCriteria.getRobot());
   ```

3. Check date-based searches:
   ```java
   // Test date-based searches
   GroupHoldFinderCriteria dateCriteria = new GroupHoldFinderCriteria();
   
   // Get current time
   Timestamp now = new Timestamp(System.currentTimeMillis());
   System.out.println("Current timestamp: " + now);
   
   // Test with date only
   dateCriteria.setProductionRunDate(now);
   List<CarrierMes> dateOnlyCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       dateCriteria, null, null);
   System.out.println("Found " + dateOnlyCarriers.size() + 
                     " carriers with exact production date");
   
   // Test with before date
   dateCriteria = new GroupHoldFinderCriteria();
   dateCriteria.setProductionRunDate(now);
   dateCriteria.setNumberBeforeRunDate(5);
   List<CarrierMes> beforeDateCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       dateCriteria, null, null);
   System.out.println("Found " + beforeDateCarriers.size() + 
                     " carriers before production date");
   
   // Test with after date
   dateCriteria = new GroupHoldFinderCriteria();
   dateCriteria.setProductionRunDate(now);
   dateCriteria.setNumberAfterRunDate(5);
   List<CarrierMes> afterDateCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       dateCriteria, null, null);
   System.out.println("Found " + afterDateCarriers.size() + 
                     " carriers after production date");
   
   // Test with both before and after
   dateCriteria = new GroupHoldFinderCriteria();
   dateCriteria.setProductionRunDate(now);
   dateCriteria.setNumberBeforeRunDate(5);
   dateCriteria.setNumberAfterRunDate(5);
   List<CarrierMes> aroundDateCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       dateCriteria, null, null);
   System.out.println("Found " + aroundDateCarriers.size() + 
                     " carriers around production date");
   ```

4. Test row and production run combination:
   ```java
   // Test row and production run combination
   GroupHoldFinderCriteria combinedCriteria = new GroupHoldFinderCriteria();
   StorageRow targetRow = StorageRow.findStorageRow(101L);
   combinedCriteria.setRow(targetRow);
   combinedCriteria.setProductionRunNumber(12345);
   
   // Test with rowAndProdRun = false
   combinedCriteria.setRowAndProdRun(false);
   List<CarrierMes> notCombinedCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       combinedCriteria, null, null);
   System.out.println("Found " + notCombinedCarriers.size() + 
                     " carriers with row and production run (not combined)");
   
   // Test with rowAndProdRun = true
   combinedCriteria.setRowAndProdRun(true);
   List<CarrierMes> combinedCarriers = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       combinedCriteria, null, null);
   System.out.println("Found " + combinedCarriers.size() + 
                     " carriers with row and production run (combined)");
   ```

5. Check for performance issues:
   ```java
   // Test performance with different criteria combinations
   long startTime = System.currentTimeMillis();
   
   // Simple criteria
   GroupHoldFinderCriteria simpleCriteria = new GroupHoldFinderCriteria();
   simpleCriteria.setProductionRunNumber(12345);
   List<CarrierMes> simpleResult = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       simpleCriteria, null, null);
   
   long simpleTime = System.currentTimeMillis();
   System.out.println("Simple criteria search took " + (simpleTime - startTime) + 
                     "ms and found " + simpleResult.size() + " carriers");
   
   // Complex criteria
   GroupHoldFinderCriteria complexCriteria = new GroupHoldFinderCriteria();
   complexCriteria.setProductionRunNumber(12345);
   complexCriteria.setProductionRunDate(new Timestamp(System.currentTimeMillis()));
   complexCriteria.setNumberBeforeRunDate(5);
   complexCriteria.setNumberAfterRunDate(5);
   complexCriteria.setRow(StorageRow.findStorageRow(101L));
   complexCriteria.setStatus(CarrierStatus.ON_HOLD);
   complexCriteria.setRobot(Press.PRESS_1);
   complexCriteria.setRowAndProdRun(true);
   
   List<CarrierMes> complexResult = CarrierMes.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
       complexCriteria, null, null);
   
   long complexTime = System.currentTimeMillis();
   System.out.println("Complex criteria search took " + (complexTime - simpleTime) + 
                     "ms and found " + complexResult.size() + " carriers");
   ```

### Resolution
- For no carriers found: Verify criteria values and loosen restrictions if necessary
- For too many carriers: Add more specific criteria to narrow the search
- For incorrect combinations: Ensure proper use of the rowAndProdRun flag
- For date issues: Verify timestamp formatting and date range parameters
- For row issues: Ensure valid row references and proper row criteria
- For performance issues: Optimize queries and limit result sets
- For inconsistent results: Ensure consistent criteria usage across different contexts

### Monitoring
- Track the number of carriers found for common criteria combinations
- Monitor search performance for different criteria types
- Track usage patterns of different criteria combinations
- Monitor for searches returning unexpectedly large or small result sets
- Set up alerts for performance degradation in carrier searches
- Track the distribution of carriers across different production runs
- Monitor for changes in carrier distribution patterns