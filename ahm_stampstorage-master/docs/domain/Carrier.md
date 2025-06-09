# Carrier Technical Documentation

## Purpose
Carrier.java is a core domain entity that represents a physical carrier in the stamp storage system. It tracks the carrier's location, contents, status, and movement throughout the facility. This class serves as the primary data model for carrier tracking and management operations.

## Logic/Functionality
- Represents a physical carrier that transports dies/parts through the facility
- Tracks carrier location, destination, and movement status
- Manages carrier contents (die, quantity) and production information
- Handles carrier status changes and maintenance flags
- Provides methods for carrier identification and comparison
- Supports bit-level maintenance tracking through maintenanceBits
- Implements JPA entity mapping for database persistence

## Flow
1. Carriers are created when new parts are loaded onto a physical carrier
2. The system tracks carrier movement through location and destination updates
3. Carriers can be queued, released, and moved between storage locations
4. Carrier status changes as it moves through different stages (loading, unloading, etc.)
5. Maintenance flags can be set to indicate carrier-specific maintenance needs
6. Carriers are associated with production runs and press information
7. Carrier history is maintained for tracking and auditing purposes

## Key Elements
- `carrierNumber`: Unique identifier for the carrier
- `die`: The die/part currently loaded on the carrier
- `quantity`: Number of parts on the carrier
- `currentLocation`: Current physical location of the carrier
- `destination`: Intended destination for the carrier
- `carrierStatus`: Current status of the carrier (EMPTY, LOADED, ON_HOLD, etc.)
- `press`: Press information associated with the carrier
- `productionRunNo`: Production run number for tracking
- `maintenanceBits`: Bit flags for tracking maintenance requirements
- `buffer`: Priority indicator for carrier movement
- `positionInLane`: Position of the carrier in a storage lane
- JPA entity mapping annotations for database persistence

## Usage
```java
// Create a new carrier
Carrier carrier = new Carrier();
carrier.setCarrierNumber(1001);
carrier.setDie(dieObject);
carrier.setQuantity(10);
carrier.setCurrentLocation(sourceLocation);
carrier.setDestination(targetLocation);
carrier.setCarrierStatus(CarrierStatus.LOADED);
carrier.setPress(Press.PRESS_1);
carrier.setProductionRunNo(12345);
carrier.setStampingProductionRunTimestamp(new Timestamp(System.currentTimeMillis()));

// Set maintenance flags if needed
carrier.setMaintenanceBits(5); // Sets bit0 and bit2 (binary 101)

// Check if carrier is moving
if (carrier.isMoving()) {
    System.out.println("Carrier is currently in transit");
}

// Compare carriers for loading similarity
Carrier otherCarrier = getCarrierFromDatabase(1002);
if (carrier.isLoadedLike(otherCarrier)) {
    System.out.println("Carriers contain the same type and quantity of parts");
}

// Persist carrier to database
carrier.persist();
```

## Debugging and Production Support

### Common Issues
1. Carrier location inconsistencies between physical and system state
2. Maintenance bit flags not properly set or interpreted
3. Carrier status transitions not properly tracked
4. Duplicate carrier numbers causing identification issues
5. Carrier movement deadlocks or circular routing
6. Inconsistent carrier data after system restarts
7. Performance issues with large numbers of carriers

### Debugging Steps
1. Verify carrier identity and location:
   ```java
   Carrier carrier = Carrier.findCarrier(carrierNumber);
   if (carrier != null) {
       System.out.println("Carrier #" + carrier.getCarrierNumber());
       System.out.println("Current location: " + carrier.getCurrentLocation().getName());
       System.out.println("Destination: " + carrier.getDestination().getName());
       System.out.println("Status: " + carrier.getCarrierStatus());
   } else {
       System.out.println("Carrier #" + carrierNumber + " not found in system");
   }
   ```

2. Check maintenance bits:
   ```java
   Carrier carrier = Carrier.findCarrier(carrierNumber);
   if (carrier != null) {
       Integer maintBits = carrier.getMaintenanceBits();
       System.out.println("Maintenance bits: " + maintBits);
       System.out.println("Maintenance required: " + carrier.getMaintRequired());
       System.out.println("Bit1 (cleaning): " + carrier.getBit1());
       System.out.println("Bit2 (inspection): " + carrier.getBit2());
   }
   ```

3. Validate carrier movement state:
   ```java
   Carrier carrier = Carrier.findCarrier(carrierNumber);
   if (carrier != null) {
       System.out.println("Is moving: " + carrier.isMoving());
       System.out.println("Is moving out of lane: " + carrier.isMovingOutOfLane());
       System.out.println("Is moving into lane: " + carrier.isMovingInToLane());
       System.out.println("Is in queue to move: " + carrier.isInQueueToMoveOutOfLane());
       System.out.println("Position in lane: " + carrier.getPositionInLane());
   }
   ```

### Resolution
- For location inconsistencies: Implement regular synchronization between physical and system state
- For maintenance bit issues: Use the provided bit methods rather than direct manipulation
- For status transition issues: Implement state machine validation for carrier status changes
- For duplicate carriers: Enforce unique carrier numbers and implement detection mechanisms
- For movement issues: Implement deadlock detection and resolution algorithms
- For data inconsistency: Implement transaction management and recovery mechanisms
- For performance issues: Optimize database queries and implement caching strategies

### Monitoring
- Track carrier movement rates and identify bottlenecks
- Monitor carrier status distribution (how many carriers in each status)
- Log carrier location changes for audit and troubleshooting
- Alert on carriers that remain in transition states for too long
- Monitor maintenance flag patterns to identify systemic issues
- Track carrier utilization rates and idle time
- Set up alerts for carriers with unexpected or invalid state combinations