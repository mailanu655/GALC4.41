# StorageRow Technical Documentation

## Purpose
StorageRow.java defines an entity that represents a physical storage row in the stamping system. This class models the rows where carriers are stored, providing functionality to manage carrier queues, track row capacity, and handle carrier storage and retrieval operations. It serves as a critical component for managing the physical storage infrastructure of the stamping system.

## Logic/Functionality
- Represents physical storage rows in the stamping system
- Manages a queue of carriers stored in the row
- Tracks row capacity and availability
- Provides methods to store and release carriers
- Implements logic to check row conditions (empty, full, etc.)
- Supports querying for carriers by various criteria
- Handles carrier positioning within the row
- Manages row state (out of order, lane condition, etc.)

## Flow
1. StorageRows are defined in the system to represent physical storage infrastructure
2. Carriers are stored in rows using the store() method
3. The system tracks carrier positions within rows
4. Business logic uses row information to make storage and retrieval decisions
5. Carriers are released from rows using the release() method
6. UI components display row information to visualize system state

## Key Elements
- `rowName`: The name of the storage row
- `stop`: The stop associated with the row
- `capacity`: The maximum number of carriers the row can hold
- `storageArea`: The area where the row is located
- `availability`: The availability status of the row
- `carrierQueue`: A queue of carriers stored in the row (transient)
- Methods for storing and releasing carriers
- Methods for checking row conditions and carrier positions
- Static finder methods for retrieving row records

## Usage
```java
// Find a storage row by ID
StorageRow row = StorageRow.findStorageRow(1L);
System.out.println("Row: " + row.getRowName());
System.out.println("Capacity: " + row.getCapacity());
System.out.println("Current carrier count: " + row.getCurrentCarrierCount());
System.out.println("Is full: " + row.isFull());
System.out.println("Is empty: " + row.isEmpty());

// Find all storage rows
List<StorageRow> allRows = StorageRow.findAllStorageRows();
System.out.println("Total rows: " + allRows.size());

// Find storage rows by area
List<StorageRow> areaRows = StorageRow.findStorageRowsByArea(StorageArea.PRESS);
System.out.println("Rows in PRESS area: " + areaRows.size());
for (StorageRow areaRow : areaRows) {
    System.out.println("  " + areaRow.getRowName());
}

// Find storage row by stop
Stop stop = Stop.findStop(1L);
StorageRow rowByStop = StorageRow.findStorageRowsByStop(stop);
if (rowByStop != null) {
    System.out.println("Row for stop " + stop.getName() + ": " + rowByStop.getRowName());
}

// Store a carrier in a row
Carrier carrier = new Carrier();
carrier.setCarrierNumber(101);
carrier.setDie(Die.findDie(1L));
carrier.setQuantity(10);
carrier.setCarrierStatus(CarrierStatus.AVAILABLE);

StorageRow targetRow = StorageRow.findStorageRow(1L);
if (!targetRow.isFull()) {
    targetRow.store(carrier);
    System.out.println("Carrier " + carrier.getCarrierNumber() + 
                      " stored in row " + targetRow.getRowName());
    System.out.println("New carrier count: " + targetRow.getCurrentCarrierCount());
} else {
    System.out.println("Row " + targetRow.getRowName() + " is full");
}

// Release a carrier from a row
StorageRow sourceRow = StorageRow.findStorageRow(1L);
if (!sourceRow.isEmpty()) {
    Carrier releasedCarrier = sourceRow.release();
    System.out.println("Released carrier " + releasedCarrier.getCarrierNumber() + 
                      " from row " + sourceRow.getRowName());
    System.out.println("New carrier count: " + sourceRow.getCurrentCarrierCount());
} else {
    System.out.println("Row " + sourceRow.getRowName() + " is empty");
}

// Check if a carrier exists in a row
Carrier searchCarrier = new Carrier();
searchCarrier.setCarrierNumber(101);
StorageRow checkRow = StorageRow.findStorageRow(1L);
boolean exists = checkRow.carrierExistsInRow(searchCarrier);
System.out.println("Carrier " + searchCarrier.getCarrierNumber() + 
                  (exists ? " exists" : " does not exist") + 
                  " in row " + checkRow.getRowName());

// Get carrier at row out (first carrier in the queue)
StorageRow outRow = StorageRow.findStorageRow(1L);
Carrier outCarrier = outRow.getCarrierAtRowOut();
if (outCarrier != null) {
    System.out.println("Carrier at row out: " + outCarrier.getCarrierNumber());
} else {
    System.out.println("No carrier at row out");
}

// Get carrier at lane in (last carrier in the queue)
StorageRow inRow = StorageRow.findStorageRow(1L);
Carrier inCarrier = inRow.getCarrierAtLaneIn();
if (inCarrier != null) {
    System.out.println("Carrier at lane in: " + inCarrier.getCarrierNumber());
} else {
    System.out.println("No carrier at lane in");
}

// Check if a row has a carrier with a specific production run number
StorageRow prodRunRow = StorageRow.findStorageRow(1L);
boolean hasProdRun = prodRunRow.hasCarrierOfProdRunNo(123);
System.out.println("Row " + prodRunRow.getRowName() + 
                  (hasProdRun ? " has" : " does not have") + 
                  " a carrier with production run number 123");

// Get die numbers for all carriers in a row
StorageRow dieRow = StorageRow.findStorageRow(1L);
Set<Long> dieNumbers = dieRow.getDieNumbersForAllCarriers();
System.out.println("Die numbers in row " + dieRow.getRowName() + ":");
for (Long dieNumber : dieNumbers) {
    System.out.println("  " + dieNumber);
}

// Check lane condition
StorageRow conditionRow = StorageRow.findStorageRow(1L);
LaneCondition condition = conditionRow.getLaneCondition();
System.out.println("Lane condition for row " + conditionRow.getRowName() + ": " + condition);

// Check if physical space is available
StorageRow spaceRow = StorageRow.findStorageRow(1L);
boolean spaceAvailable = spaceRow.isPhysicalSpaceAvailable();
System.out.println("Physical space " + 
                  (spaceAvailable ? "is" : "is not") + 
                  " available in row " + spaceRow.getRowName());

// Create a new storage row
StorageRow newRow = new StorageRow();
newRow.setRowName("New Storage Row");
Stop newStop = Stop.findStop(10L);
newRow.setStop(newStop);
newRow.setCapacity(10);
newRow.setStorageArea(StorageArea.PRESS);
newRow.setAvailability(StopAvailability.AVAILABLE);
newRow.persist();

// Update an existing storage row
StorageRow existingRow = StorageRow.findStorageRow(1L);
existingRow.setCapacity(15);
existingRow.merge();

// Delete a storage row
StorageRow obsoleteRow = StorageRow.findStorageRow(2L);
if (obsoleteRow != null) {
    obsoleteRow.remove();
}
```

## Debugging and Production Support

### Common Issues
1. Inconsistent carrier queue state
2. Row capacity exceeded
3. Missing or incorrect row configurations
4. Carrier positioning issues
5. Lane condition calculation errors
6. Synchronization issues with concurrent operations
7. Performance issues with large carrier queues

### Debugging Steps
1. Check for inconsistent carrier queue state:
   ```java
   // Check for inconsistent carrier queue state
   System.out.println("Checking for inconsistent carrier queue state:");
   
   for (StorageRow row : StorageRow.findAllStorageRows()) {
       System.out.println("  Row: " + row.getRowName() + " (" + row.getId() + ")");
       
       // Get carrier count from queue
       int queueCount = row.getCurrentCarrierCount();
       
       // Get carrier count from database
       long dbCount = StorageRow.getCarrierCountInRow(row.getStop());
       
       System.out.println("    Queue count: " + queueCount);
       System.out.println("    Database count: " + dbCount);
       
       if (queueCount != dbCount) {
           System.out.println("    WARNING: Inconsistent carrier count");
           
           // Get carriers from database
           List<CarrierMes> dbCarriers = CarrierMes.findAllCarriersWithCurrentLocation(
               row.getStop().getId());
           
           // Get carriers from queue
           LinkedList<Carrier> queueCarriers = row.getCarriersAsLinkedList();
           
           System.out.println("    Database carriers:");
           for (CarrierMes dbCarrier : dbCarriers) {
               System.out.println("      " + dbCarrier.getCarrierNumber());
           }
           
           System.out.println("    Queue carriers:");
           for (Carrier queueCarrier : queueCarriers) {
               System.out.println("      " + queueCarrier.getCarrierNumber());
           }
       }
   }
   ```

2. Check for row capacity issues:
   ```java
   // Check for row capacity issues
   System.out.println("Checking for row capacity issues:");
   
   for (StorageRow row : StorageRow.findAllStorageRows()) {
       System.out.println("  Row: " + row.getRowName() + " (" + row.getId() + ")");
       
       // Get capacity
       int capacity = row.getCapacity();
       
       // Get current carrier count
       int currentCount = row.getCurrentCarrierCount();
       
       System.out.println("    Capacity: " + capacity);
       System.out.println("    Current count: " + currentCount);
       
       // Check if capacity is exceeded
       if (currentCount > capacity) {
           System.out.println("    WARNING: Capacity exceeded");
       }
       
       // Check if row is near capacity
       if (currentCount >= capacity * 0.9) {
           System.out.println("    WARNING: Row is near capacity (" + 
                             (currentCount * 100 / capacity) + "%)");
       }
       
       // Check if capacity is reasonable
       if (capacity <= 0) {
           System.out.println("    WARNING: Invalid capacity");
       } else if (capacity > 100) {
           System.out.println("    WARNING: Unusually large capacity");
       }
   }
   ```

3. Check for missing or incorrect row configurations:
   ```java
   // Check for missing or incorrect row configurations
   System.out.println("Checking for missing or incorrect row configurations:");
   
   for (StorageRow row : StorageRow.findAllStorageRows()) {
       System.out.println("  Row: " + row.getRowName() + " (" + row.getId() + ")");
       
       // Check for missing stop
       if (row.getStop() == null) {
           System.out.println("    WARNING: Missing stop");
       }
       
       // Check for missing storage area
       if (row.getStorageArea() == null) {
           System.out.println("    WARNING: Missing storage area");
       }
       
       // Check for missing availability
       if (row.getAvailability() == null) {
           System.out.println("    WARNING: Missing availability");
       }
       
       // Check for missing row name
       if (row.getRowName() == null || row.getRowName().trim().isEmpty()) {
           System.out.println("    WARNING: Missing row name");
       }
       
       // Check for missing capacity
       if (row.getCapacity() == null) {
           System.out.println("    WARNING: Missing capacity");
       }
       
       // Check for valid stop type
       if (row.getStop() != null && row.getStop().getStopType() != StopType.STORAGE) {
           System.out.println("    WARNING: Stop is not of type STORAGE");
       }
       
       // Check for valid stop area
       if (row.getStop() != null && row.getStop().getStopArea() != StopArea.ROW) {
           System.out.println("    WARNING: Stop is not in ROW area");
       }
   }
   ```

4. Check for carrier positioning issues:
   ```java
   // Check for carrier positioning issues
   System.out.println("Checking for carrier positioning issues:");
   
   for (StorageRow row : StorageRow.findAllStorageRows()) {
       if (row.isEmpty()) {
           continue;
       }
       
       System.out.println("  Row: " + row.getRowName() + " (" + row.getId() + ")");
       
       // Get carriers in row
       LinkedList<Carrier> carriers = row.getCarriersAsLinkedList();
       
       // Check for carriers with null position
       boolean hasNullPosition = false;
       for (Carrier carrier : carriers) {
           if (carrier.getPositionInLane() == 0) {
               hasNullPosition = true;
               System.out.println("    WARNING: Carrier " + carrier.getCarrierNumber() + 
                                 " has null position");
           }
       }
       
       // Check for duplicate positions
       Map<Integer, List<Carrier>> carriersByPosition = new HashMap<>();
       for (Carrier carrier : carriers) {
           int position = carrier.getPositionInLane();
           if (!carriersByPosition.containsKey(position)) {
               carriersByPosition.put(position, new ArrayList<>());
           }
           carriersByPosition.get(position).add(carrier);
       }
       
       boolean hasDuplicatePositions = false;
       for (Map.Entry<Integer, List<Carrier>> entry : carriersByPosition.entrySet()) {
           if (entry.getValue().size() > 1) {
               hasDuplicatePositions = true;
               System.out.println("    WARNING: Multiple carriers at position " + entry.getKey());
               System.out.println("      Carriers:");
               for (Carrier carrier : entry.getValue()) {
                   System.out.println("        " + carrier.getCarrierNumber());
               }
           }
       }
       
       // Check for position gaps
       Set<Integer> positions = new HashSet<>();
       for (Carrier carrier : carriers) {
           positions.add(carrier.getPositionInLane());
       }
       
       boolean hasPositionGaps = false;
       for (int i = 1; i <= carriers.size(); i++) {
           if (!positions.contains(i)) {
               hasPositionGaps = true;
               System.out.println("    WARNING: Missing carrier at position " + i);
           }
       }
       
       if (!hasNullPosition && !hasDuplicatePositions && !hasPositionGaps) {
           System.out.println("    No carrier positioning issues found");
       }
   }
   ```

5. Check for lane condition calculation issues:
   ```java
   // Check for lane condition calculation issues
   System.out.println("Checking for lane condition calculation issues:");
   
   for (StorageRow row : StorageRow.findAllStorageRows()) {
       System.out.println("  Row: " + row.getRowName() + " (" + row.getId() + ")");
       
       // Get lane condition
       LaneCondition condition = row.getLaneCondition();
       System.out.println("    Lane condition: " + condition);
       
       // Verify lane condition based on row state
       boolean isEmpty = row.isEmpty();
       boolean isFull = row.isFull();
       Set<Long> dieNumbers = row.getDieNumbersForAllCarriers();
       
       System.out.println("    Is empty: " + isEmpty);
       System.out.println("    Is full: " + isFull);
       System.out.println("    Die numbers: " + dieNumbers);
       
       // Check for inconsistencies
       if (isEmpty && condition != LaneCondition.VACANT && condition != LaneCondition.EMPTY) {
           System.out.println("    WARNING: Inconsistent lane condition - row is empty but condition is " + 
                             condition);
       }
       
       if (isFull && dieNumbers.size() == 1 && !dieNumbers.contains(null) && 
           condition != LaneCondition.FULL) {
           System.out.println("    WARNING: Inconsistent lane condition - row is full with one die type but condition is " + 
                             condition);
       }
       
       if (dieNumbers.size() > 1 && 
           condition != LaneCondition.MIXED && 
           condition != LaneCondition.MIXED_FRONT && 
           condition != LaneCondition.MIXED_BLOCK) {
           System.out.println("    WARNING: Inconsistent lane condition - row has multiple die types but condition is " + 
                             condition);
       }
       
       if (!isEmpty && !isFull && dieNumbers.size() == 1 && !dieNumbers.contains(null) && 
           condition != LaneCondition.PARTIAL) {
           System.out.println("    WARNING: Inconsistent lane condition - row is partially filled with one die type but condition is " + 
                             condition);
       }
   }
   ```

6. Check for synchronization issues:
   ```java
   // Check for synchronization issues
   System.out.println("Checking for potential synchronization issues:");
   
   for (StorageRow row : StorageRow.findAllStorageRows()) {
       System.out.println("  Row: " + row.getRowName() + " (" + row.getId() + ")");
       
       // Check for carriers marked as moving
       LinkedList<Carrier> carriers = row.getCarriersAsLinkedList();
       int movingOutCount = 0;
       int movingInCount = 0;
       int inQueueToMoveOutCount = 0;
       
       for (Carrier carrier : carriers) {
           if (carrier.isMovingOutOfLane()) {
               movingOutCount++;
               System.out.println("    Carrier " + carrier.getCarrierNumber() + 
                                 " is marked as moving out of lane");
           }
           
           if (carrier.isMovingInToLane()) {
               movingInCount++;
               System.out.println("    Carrier " + carrier.getCarrierNumber() + 
                                 " is marked as moving into lane");
           }
           
           if (carrier.isInQueueToMoveOutOfLane()) {
               inQueueToMoveOutCount++;
               System.out.println("    Carrier " + carrier.getCarrierNumber() + 
                                 " is marked as in queue to move out of lane");
           }
       }
       
       System.out.println("    Moving out count: " + movingOutCount);
       System.out.println("    Moving in count: " + movingInCount);
       System.out.println("    In queue to move out count: " + inQueueToMoveOutCount);
       
       // Check for potential deadlocks
       if (movingOutCount > 0 && movingInCount > 0) {
           System.out.println("    WARNING: Potential deadlock - carriers moving both in and out");
       }
       
       // Check for stalled movements
       if (movingOutCount > 0 || movingInCount > 0 || inQueueToMoveOutCount > 0) {
           System.out.println("    WARNING: Potential stalled movement - carriers marked as moving");
       }
   }
   ```

7. Check for performance issues:
   ```java
   // Check for performance issues
   System.out.println("Checking for potential performance issues:");
   
   // Check for large carrier queues
   for (StorageRow row : StorageRow.findAllStorageRows()) {
       int carrierCount = row.getCurrentCarrierCount();
       if (carrierCount > 50) {
           System.out.println("  WARNING: Row " + row.getRowName() + 
                             " has a large carrier queue (" + carrierCount + " carriers)");
       }
   }
   
   // Test performance of key operations
   if (!StorageRow.findAllStorageRows().isEmpty()) {
       StorageRow testRow = StorageRow.findAllStorageRows().get(0);
       
       // Test getCurrentCarrierCount
       long startTime = System.currentTimeMillis();
       testRow.getCurrentCarrierCount();
       long endTime = System.currentTimeMillis();
       System.out.println("  getCurrentCarrierCount: " + (endTime - startTime) + "ms");
       
       // Test getCarriersAsLinkedList
       startTime = System.currentTimeMillis();
       testRow.getCarriersAsLinkedList();
       endTime = System.currentTimeMillis();
       System.out.println("  getCarriersAsLinkedList: " + (endTime - startTime) + "ms");
       
       // Test getDieNumbersForAllCarriers
       startTime = System.currentTimeMillis();
       testRow.getDieNumbersForAllCarriers();
       endTime = System.currentTimeMillis();
       System.out.println("  getDieNumbersForAllCarriers: " + (endTime - startTime) + "ms");
       
       // Test getLaneCondition
       startTime = System.currentTimeMillis();
       testRow.getLaneCondition();
       endTime = System.currentTimeMillis();
       System.out.println("  getLaneCondition: " + (endTime - startTime) + "ms");
   }
   ```

### Resolution
- For inconsistent carrier queue state: Synchronize the carrier queue with the database
- For row capacity issues: Adjust row capacity or redistribute carriers
- For missing or incorrect row configurations: Update row configurations
- For carrier positioning issues: Correct carrier positions
- For lane condition calculation issues: Fix lane condition calculation logic
- For synchronization issues: Reset carrier movement flags or implement proper synchronization
- For performance issues: Optimize carrier queue operations or implement pagination

### Monitoring
- Monitor row capacity utilization
- Track carrier movement operations
- Monitor for carrier queue inconsistencies
- Track lane condition changes
- Monitor for synchronization issues
- Track performance of carrier queue operations
- Set up alerts for rows approaching capacity