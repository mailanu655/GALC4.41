# CarrierRelease Technical Documentation

## Purpose
CarrierRelease.java serves as an entity that manages and tracks carrier release requests in the stamp storage system. It maintains information about carriers that need to be moved from their current location to a specified destination, acting as a queue for carrier movement operations. This entity is crucial for coordinating carrier movements throughout the storage system.

## Logic/Functionality
- Represents carrier release requests in the system
- Maps to the CARRIER_RELEASE_TBX database table
- Stores current location, destination, source, and timestamp information
- Uses the carrier number as the primary key
- Provides methods for persisting, retrieving, and managing release requests
- Supports transaction management for database operations
- Implements query methods for finding release requests by various criteria

## Flow
1. A carrier movement request is initiated by a user or system process
2. A CarrierRelease record is created or updated with the carrier's current location, destination, and source
3. The release manager processes the release requests in the queue
4. The carrier is physically moved from its current location to the destination
5. Once the movement is complete, the release request may be archived or removed

## Key Elements
- `id`: The carrier number (primary key)
- `currentLocation`: The current location of the carrier (Stop entity)
- `destination`: The destination location for the carrier (Stop entity)
- `source`: The source system or process that initiated the release request
- `requestTimestamp`: The timestamp when the release request was created
- JPA entity mapping annotations for database persistence
- Static methods for finding and managing release requests
- Transaction management annotations for database operations

## Usage
```java
// Create a new carrier release request
Long carrierNumber = 1001L;
Stop currentLocation = Stop.findStop(100L);
Stop destination = Stop.findStop(200L);
String source = "SYSTEM";

CarrierRelease.saveCarrierRelease(carrierNumber, currentLocation, destination, source);

// Find a carrier release request
CarrierRelease release = CarrierRelease.findCarrierRelease(carrierNumber);
if (release != null) {
    System.out.println("Release request found for carrier " + carrierNumber);
    System.out.println("Current location: " + release.getCurrentLocation().getName());
    System.out.println("Destination: " + release.getDestination().getName());
    System.out.println("Requested at: " + release.getRequestTimestamp());
}

// Find all release requests for a specific stop
List<CarrierRelease> releases = CarrierRelease.findCarrierReleaseEntriesAtStop(stopId);
System.out.println("Found " + releases.size() + " release requests for stop " + stopId);

// Find all release requests in a storage area
List<Long> stopIds = new ArrayList<>();
stopIds.add(100L);
stopIds.add(101L);
stopIds.add(102L);

List<CarrierRelease> areaReleases = CarrierRelease.findCarrierReleaseEntriesInStorageArea(stopIds);
System.out.println("Found " + areaReleases.size() + " release requests in the storage area");

// Find release requests that need destination updates
List<CarrierRelease> updateNeeded = CarrierRelease.findCarrierReleaseEntriesInStorageAreaThatNeedDestinationUpdated(stopIds);
System.out.println("Found " + updateNeeded.size() + " release requests that need destination updates");
```

## Debugging and Production Support

### Common Issues
1. Release requests not being processed
2. Duplicate release requests for the same carrier
3. Stale release requests remaining in the system
4. Inconsistencies between release requests and actual carrier locations
5. Performance issues with large numbers of release requests
6. Release requests with invalid location or destination references
7. Timing issues with release request processing

### Debugging Steps
1. Verify release request data:
   ```java
   // Check a specific release request
   Long carrierNumber = 1001L;
   CarrierRelease release = CarrierRelease.findCarrierRelease(carrierNumber);
   
   if (release != null) {
       System.out.println("Release request for carrier " + carrierNumber + ":");
       System.out.println("  Current Location: " + release.getCurrentLocation().getId() + 
                          " (" + release.getCurrentLocation().getName() + ")");
       System.out.println("  Destination: " + release.getDestination().getId() + 
                          " (" + release.getDestination().getName() + ")");
       System.out.println("  Source: " + release.getSource());
       System.out.println("  Request Time: " + release.getRequestTimestamp());
   } else {
       System.out.println("No release request found for carrier " + carrierNumber);
   }
   ```

2. Check for inconsistencies with carrier data:
   ```java
   // Compare release request with actual carrier data
   Long carrierNumber = 1001L;
   CarrierRelease release = CarrierRelease.findCarrierRelease(carrierNumber);
   CarrierMes carrier = CarrierMes.findCarrierByCarrierNumber(carrierNumber.intValue());
   
   if (release != null && carrier != null) {
       System.out.println("Carrier " + carrierNumber + " data comparison:");
       System.out.println("  Release Current Location: " + release.getCurrentLocation().getId());
       System.out.println("  Carrier Current Location: " + carrier.getCurrentLocation());
       System.out.println("  Release Destination: " + release.getDestination().getId());
       System.out.println("  Carrier Destination: " + carrier.getDestination());
       
       boolean locationMatch = release.getCurrentLocation().getId().equals(carrier.getCurrentLocation());
       boolean destinationMatch = release.getDestination().getId().equals(carrier.getDestination());
       
       System.out.println("  Location Match: " + locationMatch);
       System.out.println("  Destination Match: " + destinationMatch);
   }
   ```

3. Check for stale release requests:
   ```java
   // Find potentially stale release requests
   long currentTime = System.currentTimeMillis();
   long staleThreshold = 30 * 60 * 1000; // 30 minutes
   
   String sql = "SELECT o FROM CarrierRelease o WHERE o.requestTimestamp < :threshold";
   Query q = entityManager().createQuery(sql, CarrierRelease.class);
   q.setParameter("threshold", new Timestamp(currentTime - staleThreshold));
   
   List<CarrierRelease> staleReleases = q.getResultList();
   System.out.println("Found " + staleReleases.size() + " potentially stale release requests");
   
   for (CarrierRelease staleRelease : staleReleases) {
       System.out.println("  Carrier " + staleRelease.getId() + 
                          ", Requested at " + staleRelease.getRequestTimestamp());
   }
   ```

4. Check for release requests with invalid references:
   ```java
   // Find release requests with invalid location references
   String sql = "SELECT o FROM CarrierRelease o WHERE o.currentLocation IS NULL OR o.destination IS NULL";
   Query q = entityManager().createQuery(sql, CarrierRelease.class);
   
   List<CarrierRelease> invalidReleases = q.getResultList();
   System.out.println("Found " + invalidReleases.size() + " release requests with invalid references");
   
   for (CarrierRelease invalidRelease : invalidReleases) {
       System.out.println("  Carrier " + invalidRelease.getId() + 
                          ", Current Location: " + invalidRelease.getCurrentLocation() + 
                          ", Destination: " + invalidRelease.getDestination());
   }
   ```

5. Check for release requests in specific areas:
   ```java
   // Find release requests in a specific storage area
   List<Long> stopIds = new ArrayList<>();
   // Add stop IDs for the area
   stopIds.add(100L);
   stopIds.add(101L);
   stopIds.add(102L);
   
   List<CarrierRelease> areaReleases = CarrierRelease.findCarrierReleaseEntriesInStorageArea(stopIds);
   System.out.println("Found " + areaReleases.size() + " release requests in the storage area");
   
   for (CarrierRelease areaRelease : areaReleases) {
       System.out.println("  Carrier " + areaRelease.getId() + 
                          ", Current Location: " + areaRelease.getCurrentLocation().getName() + 
                          ", Destination: " + areaRelease.getDestination().getName());
   }
   ```

### Resolution
- For unprocessed release requests: Check the release manager service and ensure it's running properly
- For duplicate requests: Implement uniqueness constraints and validation
- For stale requests: Implement a cleanup process for old release requests
- For inconsistencies: Synchronize release request data with carrier data
- For performance issues: Optimize queries and implement indexing
- For invalid references: Implement referential integrity constraints
- For timing issues: Implement proper synchronization and transaction management

### Monitoring
- Track the number of release requests in the system
- Monitor the age of release requests to identify stale entries
- Track release request processing rates
- Monitor for release requests that fail to complete
- Set up alerts for abnormal release request patterns
- Track release request queue lengths by area
- Monitor for inconsistencies between release requests and carrier data