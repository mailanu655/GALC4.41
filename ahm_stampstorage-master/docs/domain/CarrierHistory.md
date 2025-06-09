# CarrierHistory Technical Documentation

## Purpose
CarrierHistory.java serves as a historical record of carrier movements and states in the stamp storage system. It archives carrier data to provide an audit trail of carrier activities, enabling historical tracking, reporting, and analysis of carrier movements throughout the facility.

## Logic/Functionality
- Maintains historical records of carrier states and movements
- Stores carrier attributes at specific points in time
- Provides data access methods for querying historical carrier data
- Implements JPA entity mapping for database persistence
- Supports filtering and searching of historical carrier records
- Enables audit trail and reporting capabilities

## Flow
1. When a carrier's state changes significantly, a history record is created
2. The current state of the carrier is captured in a CarrierHistory object
3. The history record is persisted to the database
4. Historical records can be queried for reporting and analysis
5. Records can be filtered by various criteria (carrier number, die, location, etc.)
6. Historical data provides insights into carrier movements and system performance

## Key Elements
- `carrierNumber`: The carrier number being tracked
- `dieNumber`: The die associated with the carrier at the time of recording
- `currentLocation`: The carrier's location at the time of recording
- `destination`: The carrier's destination at the time of recording
- `productionRunNumber`: The production run associated with the carrier
- `quantity`: The quantity of parts on the carrier
- `status`: The carrier's status at the time of recording
- `updateDate`: When the carrier state was recorded
- `source`: The source system or process that updated the carrier
- `carrierMesArchiveTstp`: Timestamp when the record was archived
- JPA entity mapping annotations for database persistence

## Usage
```java
// Query historical records for a specific carrier
CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();
criteria.setCarrierNumber(1001);
List<CarrierHistory> history = CarrierHistory.findCarrierHistoryByCarrierNumber(criteria, 1, 100);

// Process historical records
for (CarrierHistory record : history) {
    System.out.println("Timestamp: " + record.getUpdateDate());
    System.out.println("Location: " + record.getCurrentLocation().getName());
    System.out.println("Status: " + record.getStatus());
    System.out.println("Die: " + record.getDieNumber().getDescription());
    System.out.println("Quantity: " + record.getQuantity());
    System.out.println("---");
}

// Get count of historical records
Long count = CarrierHistory.getFindCarrierHistoryCount(criteria);
System.out.println("Total history records: " + count);

// Create a new history record (typically done by system processes)
CarrierHistory history = new CarrierHistory();
history.setCarrierNumber(1001);
history.setDieNumber(dieId);
history.setCurrentLocation(currentLocationId);
history.setDestination(destinationId);
history.setStatus(CarrierStatus.LOADED);
history.setQuantity(10);
history.setProductionRunNumber(12345);
history.setUpdateDate(new Timestamp(System.currentTimeMillis()));
history.setSource("SYSTEM");
history.setCarrierMesArchiveTstp(new Timestamp(System.currentTimeMillis()));
history.persist();
```

## Debugging and Production Support

### Common Issues
1. Missing history records for certain carrier operations
2. Inconsistent timestamps in historical data
3. Incorrect location or status information in history records
4. Performance issues with large history tables
5. Incomplete carrier history affecting reporting accuracy
6. Duplicate history records for the same event
7. Orphaned history records after carrier deletion

### Debugging Steps
1. Verify history record creation:
   ```java
   // Check if history records are being created for a specific carrier
   CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();
   criteria.setCarrierNumber(1001);
   
   List<CarrierHistory> history = CarrierHistory.findCarrierHistoryByCarrierNumber(criteria, 1, 10);
   System.out.println("Found " + history.size() + " history records for carrier " + criteria.getCarrierNumber());
   
   // Check the most recent record
   if (!history.isEmpty()) {
       CarrierHistory latest = history.get(0);
       System.out.println("Latest record timestamp: " + latest.getUpdateDate());
       System.out.println("Latest record location: " + latest.getCurrentLocation().getName());
       System.out.println("Latest record status: " + latest.getStatus());
   }
   ```

2. Check for data consistency:
   ```java
   // Compare current carrier state with history records
   Carrier carrier = Carrier.findCarrier(1001L);
   
   CarrierHistoryFinderCriteria criteria = new CarrierHistoryFinderCriteria();
   criteria.setCarrierNumber(carrier.getCarrierNumber());
   
   List<CarrierHistory> history = CarrierHistory.findCarrierHistoryByCarrierNumber(criteria, 1, 1);
   
   if (!history.isEmpty()) {
       CarrierHistory latest = history.get(0);
       System.out.println("Current carrier location: " + carrier.getCurrentLocation().getId());
       System.out.println("History record location: " + latest.getCurrentLocation().getId());
       System.out.println("Current carrier status: " + carrier.getCarrierStatus());
       System.out.println("History record status: " + latest.getStatus());
       System.out.println("Current carrier die: " + carrier.getDie().getId());
       System.out.println("History record die: " + latest.getDieNumber().getId());
   }
   ```

3. Analyze history record distribution:
   ```java
   // Check history record counts by time period
   Calendar cal = Calendar.getInstance();
   cal.add(Calendar.DAY_OF_MONTH, -1);
   Timestamp yesterday = new Timestamp(cal.getTimeInMillis());
   
   String sql = "SELECT COUNT(*) FROM CARRIER_MES_ARCHIVE_TBX WHERE UPDATE_DATE > ?";
   Query q = entityManager().createNativeQuery(sql);
   q.setParameter(1, yesterday);
   
   Long count = (Long) q.getSingleResult();
   System.out.println("History records in the last 24 hours: " + count);
   ```

### Resolution
- For missing records: Implement transaction management to ensure history records are created
- For timestamp issues: Standardize timestamp creation and ensure server time synchronization
- For incorrect data: Validate data before creating history records
- For performance issues: Implement archiving strategies for older history records
- For incomplete history: Add validation to ensure all required fields are populated
- For duplicate records: Add uniqueness constraints or deduplication logic
- For orphaned records: Implement referential integrity or cleanup processes

### Monitoring
- Track history record creation rates to identify anomalies
- Monitor history table size and growth rate
- Set up alerts for missing history records for critical operations
- Periodically validate history data consistency with current carrier states
- Monitor query performance on history tables
- Track history record access patterns to optimize queries
- Set up data retention policies for managing history record volume