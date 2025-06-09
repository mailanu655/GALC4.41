# Stop Technical Documentation

## Purpose
Stop.java defines an entity that represents physical locations or stops within the stamping system's conveyor network. This class models the various points where carriers can be located, including storage rows, loading/unloading stations, and transfer points. It provides a foundation for tracking carrier movement and managing the physical layout of the stamping system.

## Logic/Functionality
- Represents physical locations in the stamping system
- Categorizes stops by type and area
- Implements JPA entity functionality for database persistence
- Provides methods to find and manage stop records
- Supports querying for stops by various criteria
- Enables validation of carrier movement between stops

## Flow
1. Stops are defined in the system to represent the physical layout
2. Carriers are assigned to stops as they move through the system
3. The system tracks carrier locations by referencing stops
4. Business logic uses stop information to validate and control carrier movement
5. UI components display stop information to visualize system state

## Key Elements
- `name`: The name of the stop
- `stopType`: The type of stop (e.g., STORAGE, TRANSFER, LOAD, UNLOAD)
- `description`: A description of the stop
- `stopArea`: The area where the stop is located (e.g., ROW, PRESS, WELD)
- Static finder methods for retrieving stop records
- Standard JPA entity methods for persistence operations

## Usage
```java
// Find a stop by ID
Stop stop = Stop.findStop(1L);
System.out.println("Stop: " + stop.getName());
System.out.println("Type: " + stop.getStopType());
System.out.println("Area: " + stop.getStopArea());
System.out.println("Description: " + stop.getDescription());

// Find all stops
List<Stop> allStops = Stop.findAllStops();
System.out.println("Total stops: " + allStops.size());

// Find stops by type
List<Stop> storageStops = Stop.findAllStopsByType(StopType.STORAGE);
System.out.println("Storage stops: " + storageStops.size());
for (Stop storageStop : storageStops) {
    System.out.println("  " + storageStop.getName());
}

// Find stops by area
List<Stop> rowStops = Stop.findAllStopsByArea(StopArea.ROW);
System.out.println("Row stops: " + rowStops.size());
for (Stop rowStop : rowStops) {
    System.out.println("  " + rowStop.getName());
}

// Find stops by type and area
List<Stop> storageRowStops = Stop.findAllStopsByTypeAndArea(StopType.STORAGE, StopArea.ROW);
System.out.println("Storage row stops: " + storageRowStops.size());
for (Stop storageRowStop : storageRowStops) {
    System.out.println("  " + storageRowStop.getName());
}

// Create a new stop
Stop newStop = new Stop();
newStop.setId(101L);
newStop.setName("New Transfer Point");
newStop.setStopType(StopType.TRANSFER);
newStop.setStopArea(StopArea.PRESS);
newStop.setDescription("New transfer point between press and storage");
newStop.persist();

// Update an existing stop
Stop existingStop = Stop.findStop(1L);
existingStop.setDescription("Updated description");
existingStop.merge();

// Delete a stop
Stop obsoleteStop = Stop.findStop(2L);
if (obsoleteStop != null) {
    obsoleteStop.remove();
}

// Check if a stop is a row stop
Stop stop = Stop.findStop(1L);
if (stop.isRowStop()) {
    System.out.println(stop.getName() + " is a row stop");
} else {
    System.out.println(stop.getName() + " is not a row stop");
}

// Use a stop in carrier movement validation
Stop sourceStop = Stop.findStop(1L);
Stop destinationStop = Stop.findStop(2L);
List<Stop> validDestinations = ValidDestination.findValidDestinationsForGivenStop(sourceStop);
if (validDestinations.contains(destinationStop)) {
    System.out.println("Movement from " + sourceStop.getName() + 
                      " to " + destinationStop.getName() + " is valid");
} else {
    System.out.println("Movement from " + sourceStop.getName() + 
                      " to " + destinationStop.getName() + " is not valid");
}
```

## Debugging and Production Support

### Common Issues
1. Missing or incorrect stop definitions
2. Duplicate stop IDs
3. Inconsistent stop type and area combinations
4. Invalid stop references in carrier records
5. Missing valid destination configurations
6. Stops with incorrect or missing attributes
7. Performance issues with stop queries

### Debugging Steps
1. Check for missing or incorrect stop definitions:
   ```java
   // Check for missing or incorrect stop definitions
   System.out.println("Checking for missing or incorrect stop definitions:");
   
   List<Stop> allStops = Stop.findAllStops();
   System.out.println("Total stops: " + allStops.size());
   
   // Check for stops with missing or invalid attributes
   for (Stop stop : allStops) {
       System.out.println("  Stop: " + stop.getId() + " - " + stop.getName());
       
       if (stop.getName() == null || stop.getName().trim().isEmpty()) {
           System.out.println("    WARNING: Missing name");
       }
       
       if (stop.getStopType() == null) {
           System.out.println("    WARNING: Missing stop type");
       }
       
       if (stop.getStopArea() == null) {
           System.out.println("    WARNING: Missing stop area");
       }
       
       // Check for valid stop type and area combinations
       if (stop.getStopType() != null && stop.getStopArea() != null) {
           boolean validCombination = true;
           
           // Example validation rules (adjust based on actual business rules)
           if (stop.getStopType() == StopType.STORAGE && stop.getStopArea() != StopArea.ROW) {
               validCombination = false;
           } else if (stop.getStopType() == StopType.LOAD && 
                     (stop.getStopArea() != StopArea.PRESS && stop.getStopArea() != StopArea.WELD)) {
               validCombination = false;
           }
           
           if (!validCombination) {
               System.out.println("    WARNING: Invalid combination of stop type (" + 
                                 stop.getStopType() + ") and stop area (" + 
                                 stop.getStopArea() + ")");
           }
       }
   }
   ```

2. Check for duplicate stop IDs:
   ```java
   // Check for duplicate stop IDs
   System.out.println("Checking for duplicate stop IDs:");
   
   Map<Long, List<Stop>> stopsById = new HashMap<>();
   
   for (Stop stop : Stop.findAllStops()) {
       Long id = stop.getId();
       if (!stopsById.containsKey(id)) {
           stopsById.put(id, new ArrayList<>());
       }
       stopsById.get(id).add(stop);
   }
   
   boolean hasDuplicates = false;
   for (Map.Entry<Long, List<Stop>> entry : stopsById.entrySet()) {
       if (entry.getValue().size() > 1) {
           hasDuplicates = true;
           System.out.println("  WARNING: Duplicate stop ID: " + entry.getKey());
           System.out.println("    Stops with this ID:");
           for (Stop stop : entry.getValue()) {
               System.out.println("      " + stop.getName() + " (" + 
                                 stop.getStopType() + ", " + stop.getStopArea() + ")");
           }
       }
   }
   
   if (!hasDuplicates) {
       System.out.println("  No duplicate stop IDs found");
   }
   ```

3. Check for invalid stop references in carrier records:
   ```java
   // Check for invalid stop references in carrier records
   System.out.println("Checking for invalid stop references in carrier records:");
   
   // Get all stop IDs
   Set<Long> validStopIds = new HashSet<>();
   for (Stop stop : Stop.findAllStops()) {
       validStopIds.add(stop.getId());
   }
   
   // Check carrier current locations
   int invalidCurrentLocationCount = 0;
   for (CarrierMes carrier : CarrierMes.findAllCarriers()) {
       Long currentLocationId = carrier.getCurrentLocation();
       if (currentLocationId != null && !validStopIds.contains(currentLocationId)) {
           invalidCurrentLocationCount++;
           System.out.println("  WARNING: Carrier " + carrier.getCarrierNumber() + 
                             " has invalid current location: " + currentLocationId);
       }
   }
   
   if (invalidCurrentLocationCount == 0) {
       System.out.println("  No carriers with invalid current locations found");
   } else {
       System.out.println("  Found " + invalidCurrentLocationCount + 
                         " carriers with invalid current locations");
   }
   
   // Check carrier destinations
   int invalidDestinationCount = 0;
   for (CarrierMes carrier : CarrierMes.findAllCarriers()) {
       Long destinationId = carrier.getDestination();
       if (destinationId != null && !validStopIds.contains(destinationId)) {
           invalidDestinationCount++;
           System.out.println("  WARNING: Carrier " + carrier.getCarrierNumber() + 
                             " has invalid destination: " + destinationId);
       }
   }
   
   if (invalidDestinationCount == 0) {
       System.out.println("  No carriers with invalid destinations found");
   } else {
       System.out.println("  Found " + invalidDestinationCount + 
                         " carriers with invalid destinations");
   }
   ```

4. Check for missing valid destination configurations:
   ```java
   // Check for missing valid destination configurations
   System.out.println("Checking for missing valid destination configurations:");
   
   // Get all stops
   List<Stop> stops = Stop.findAllStops();
   
   // Check valid destinations for each stop
   for (Stop stop : stops) {
       List<Stop> validDestinations = ValidDestination.findValidDestinationsForGivenStop(stop);
       
       System.out.println("  Stop: " + stop.getName() + " (" + stop.getId() + ")");
       System.out.println("    Valid destinations: " + validDestinations.size());
       
       if (validDestinations.isEmpty()) {
           System.out.println("    WARNING: No valid destinations defined");
       } else {
           // Check for self-referential valid destinations
           boolean hasSelfReference = false;
           for (Stop destination : validDestinations) {
               if (destination.getId().equals(stop.getId())) {
                   hasSelfReference = true;
                   break;
               }
           }
           
           if (hasSelfReference) {
               System.out.println("    WARNING: Stop is defined as a valid destination for itself");
           }
       }
   }
   ```

5. Check for stops with incorrect or missing attributes:
   ```java
   // Check for stops with incorrect or missing attributes
   System.out.println("Checking for stops with incorrect or missing attributes:");
   
   // Define expected stop types and areas
   Set<StopType> expectedStopTypes = new HashSet<>(Arrays.asList(
       StopType.STORAGE,
       StopType.TRANSFER,
       StopType.LOAD,
       StopType.UNLOAD
   ));
   
   Set<StopArea> expectedStopAreas = new HashSet<>(Arrays.asList(
       StopArea.ROW,
       StopArea.PRESS,
       StopArea.WELD
   ));
   
   // Check each stop
   for (Stop stop : Stop.findAllStops()) {
       System.out.println("  Stop: " + stop.getName() + " (" + stop.getId() + ")");
       
       // Check stop type
       if (stop.getStopType() == null) {
           System.out.println("    WARNING: Missing stop type");
       } else if (!expectedStopTypes.contains(stop.getStopType())) {
           System.out.println("    WARNING: Unexpected stop type: " + stop.getStopType());
       }
       
       // Check stop area
       if (stop.getStopArea() == null) {
           System.out.println("    WARNING: Missing stop area");
       } else if (!expectedStopAreas.contains(stop.getStopArea())) {
           System.out.println("    WARNING: Unexpected stop area: " + stop.getStopArea());
       }
       
       // Check description
       if (stop.getDescription() == null || stop.getDescription().trim().isEmpty()) {
           System.out.println("    WARNING: Missing description");
       }
   }
   ```

6. Test stop query performance:
   ```java
   // Test stop query performance
   System.out.println("Testing stop query performance:");
   
   // Test findAllStops
   long startTime = System.currentTimeMillis();
   List<Stop> allStopsPerf = Stop.findAllStops();
   long endTime = System.currentTimeMillis();
   System.out.println("  findAllStops: " + (endTime - startTime) + 
                     "ms for " + allStopsPerf.size() + " stops");
   
   // Test findStop by ID
   if (!allStopsPerf.isEmpty()) {
       Stop sampleStop = allStopsPerf.get(0);
       startTime = System.currentTimeMillis();
       Stop foundStop = Stop.findStop(sampleStop.getId());
       endTime = System.currentTimeMillis();
       
       System.out.println("  findStop(ID): " + (endTime - startTime) + 
                         "ms, found: " + (foundStop != null ? "Yes" : "No"));
   }
   
   // Test findAllStopsByType
   for (StopType stopType : StopType.values()) {
       startTime = System.currentTimeMillis();
       List<Stop> typeStops = Stop.findAllStopsByType(stopType);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findAllStopsByType(" + stopType + "): " + 
                         (endTime - startTime) + "ms, found: " + typeStops.size());
   }
   
   // Test findAllStopsByArea
   for (StopArea stopArea : StopArea.values()) {
       startTime = System.currentTimeMillis();
       List<Stop> areaStops = Stop.findAllStopsByArea(stopArea);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findAllStopsByArea(" + stopArea + "): " + 
                         (endTime - startTime) + "ms, found: " + areaStops.size());
   }
   ```

7. Check for stop distribution:
   ```java
   // Check for stop distribution
   System.out.println("Checking for stop distribution:");
   
   // Count stops by type
   Map<StopType, Integer> stopsByType = new HashMap<>();
   for (Stop stop : Stop.findAllStops()) {
       StopType type = stop.getStopType();
       if (type != null) {
           stopsByType.put(type, stopsByType.getOrDefault(type, 0) + 1);
       }
   }
   
   System.out.println("  Stops by type:");
   for (Map.Entry<StopType, Integer> entry : stopsByType.entrySet()) {
       System.out.println("    " + entry.getKey() + ": " + entry.getValue());
   }
   
   // Count stops by area
   Map<StopArea, Integer> stopsByArea = new HashMap<>();
   for (Stop stop : Stop.findAllStops()) {
       StopArea area = stop.getStopArea();
       if (area != null) {
           stopsByArea.put(area, stopsByArea.getOrDefault(area, 0) + 1);
       }
   }
   
   System.out.println("  Stops by area:");
   for (Map.Entry<StopArea, Integer> entry : stopsByArea.entrySet()) {
       System.out.println("    " + entry.getKey() + ": " + entry.getValue());
   }
   
   // Count stops by type and area
   Map<String, Integer> stopsByTypeAndArea = new HashMap<>();
   for (Stop stop : Stop.findAllStops()) {
       StopType type = stop.getStopType();
       StopArea area = stop.getStopArea();
       if (type != null && area != null) {
           String key = type + "-" + area;
           stopsByTypeAndArea.put(key, stopsByTypeAndArea.getOrDefault(key, 0) + 1);
       }
   }
   
   System.out.println("  Stops by type and area:");
   for (Map.Entry<String, Integer> entry : stopsByTypeAndArea.entrySet()) {
       System.out.println("    " + entry.getKey() + ": " + entry.getValue());
   }
   ```

### Resolution
- For missing or incorrect stop definitions: Add or update stop records
- For duplicate stop IDs: Resolve duplicate IDs by updating or removing duplicates
- For inconsistent stop type and area combinations: Correct stop type and area combinations
- For invalid stop references: Update carrier records with valid stop references
- For missing valid destination configurations: Add valid destination configurations
- For stops with incorrect or missing attributes: Update stop records with correct attributes
- For performance issues: Optimize queries or add indexes

### Monitoring
- Monitor stop configuration changes
- Track carrier movement between stops
- Monitor for invalid stop references
- Track valid destination configuration changes
- Monitor stop query performance
- Set up alerts for missing or incorrect stop configurations
- Track stop distribution by type and area