# CarrierMes Technical Documentation

## Purpose
CarrierMes.java serves as the primary entity representing carriers in the MES (Manufacturing Execution System) database. It stores the current state of all carriers in the system, including their location, contents, status, and other attributes. This entity is central to the stamp storage system's operation, providing real-time carrier tracking and management capabilities.

## Logic/Functionality
- Represents the current state of carriers in the system
- Provides comprehensive data access methods for carrier operations
- Implements JPA entity mapping for database persistence
- Supports complex queries for carrier location, content, and status
- Manages carrier maintenance bits for tracking carrier conditions
- Facilitates carrier inventory reporting and analysis
- Supports carrier movement and tracking operations

## Flow
1. Carriers are created or updated in the system through various operations
2. The CarrierMes entity stores the current state of each carrier
3. Business logic queries and updates carrier information as needed
4. Carrier data is used for decision-making in storage and retrieval operations
5. Inventory reports and analysis are generated based on carrier data
6. Historical carrier data is archived to CarrierHistory when significant changes occur

## Key Elements
- `carrierNumber`: Unique identifier for the carrier
- `dieNumber`: The die/part loaded on the carrier
- `quantity`: The quantity of parts on the carrier
- `currentLocation`: The current location of the carrier
- `destination`: The destination the carrier is moving to
- `status`: The current status of the carrier (e.g., LOADED, EMPTY, ON_HOLD)
- `originationLocation`: The press or location where the carrier originated
- `productionRunNumber`: The production run associated with the carrier
- `productionRunDate`: The date of the production run
- `maintenanceBits`: Bit flags for tracking carrier maintenance conditions
- `buffer`: Priority indicator for carrier movement
- `updateDate`: Timestamp of the last update to the carrier
- `source`: The source system or process that updated the carrier
- JPA entity mapping annotations for database persistence
- Extensive query methods for carrier operations

## Usage
```java
// Find a carrier by carrier number
CarrierMes carrier = CarrierMes.findCarrierByCarrierNumber(1001);

// Update carrier location
carrier.setCurrentLocation(newLocationId);
carrier.setDestination(destinationId);
carrier.setUpdateDate(new Date());
carrier.merge();

// Query carriers in a specific location
List<CarrierMes> carriersInLocation = CarrierMes.findAllCarriersWithCurrentLocation(locationId);

// Find carriers with specific die
Die die = Die.findDie(dieId);
CarrierFinderCriteria criteria = new CarrierFinderCriteria();
criteria.setDie(die);
List<CarrierMes> carriersWithDie = CarrierMes.findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(
    criteria, 1, 100);

// Get inventory by die
List<DieInventory> inventory = CarrierMes.findPartCountsByDies();

// Check maintenance bits
Integer maintBits = carrier.getMaintenanceBits();
if (carrier.getMaintRequired() == 1) {
    System.out.println("Carrier requires maintenance");
}

// Create a new carrier
CarrierMes newCarrier = new CarrierMes();
newCarrier.setCarrierNumber(1001);
newCarrier.setDieNumber(dieId);
newCarrier.setQuantity(10);
newCarrier.setCurrentLocation(currentLocationId);
newCarrier.setDestination(destinationId);
newCarrier.setStatus(CarrierStatus.LOADED.type());
newCarrier.setOriginationLocation(Press.PRESS_A.type());
newCarrier.setProductionRunNumber(12345);
newCarrier.setProductionRunDate(new Date());
newCarrier.setUpdateDate(new Date());
newCarrier.setSource("SYSTEM");
newCarrier.persist();
```

## Debugging and Production Support

### Common Issues
1. Inconsistent carrier state (location doesn't match actual position)
2. Missing or incorrect carrier data
3. Duplicate carrier numbers in the system
4. Maintenance bit flags not properly set or interpreted
5. Performance issues with large carrier datasets
6. Carrier status not reflecting actual carrier condition
7. Orphaned carriers (no valid location or destination)

### Debugging Steps
1. Verify carrier data integrity:
   ```java
   // Check a specific carrier
   Integer carrierNumber = 1001;
   CarrierMes carrier = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
   
   if (carrier != null) {
       System.out.println("Carrier " + carrierNumber + " found:");
       System.out.println("  Current Location: " + carrier.getCurrentLocation());
       System.out.println("  Destination: " + carrier.getDestination());
       System.out.println("  Status: " + carrier.getStatus());
       System.out.println("  Die: " + carrier.getDieNumber());
       System.out.println("  Quantity: " + carrier.getQuantity());
       System.out.println("  Last Updated: " + carrier.getUpdateDate());
       System.out.println("  Maintenance Bits: " + carrier.getMaintenanceBits());
   } else {
       System.out.println("Carrier " + carrierNumber + " not found");
   }
   ```

2. Check for carriers in unexpected states:
   ```java
   // Find carriers with mismatched location and destination
   String sql = "SELECT o FROM CarrierMes o WHERE o.currentLocation = o.destination AND o.status != :emptyStatus";
   Query q = entityManager().createQuery(sql, CarrierMes.class);
   q.setParameter("emptyStatus", CarrierStatus.EMPTY.type());
   
   List<CarrierMes> carriers = q.getResultList();
   System.out.println("Found " + carriers.size() + " carriers with potential state issues");
   
   for (CarrierMes carrier : carriers) {
       System.out.println("Carrier " + carrier.getCarrierNumber() + 
                          " at location " + carrier.getCurrentLocation() + 
                          " with status " + carrier.getStatus());
   }
   ```

3. Analyze maintenance bit flags:
   ```java
   // Check carriers with maintenance flags
   String sql = "SELECT o FROM CarrierMes o WHERE o.maintenanceBits IS NOT NULL AND o.maintenanceBits > 0";
   Query q = entityManager().createQuery(sql, CarrierMes.class);
   
   List<CarrierMes> carriers = q.getResultList();
   System.out.println("Found " + carriers.size() + " carriers with maintenance flags");
   
   for (CarrierMes carrier : carriers) {
       System.out.println("Carrier " + carrier.getCarrierNumber() + 
                          " has maintenance bits: " + carrier.getMaintenanceBits());
       System.out.println("  Maint Required: " + carrier.getMaintRequired());
       System.out.println("  Bit1: " + carrier.getBit1());
       System.out.println("  Bit2: " + carrier.getBit2());
       // ... other bits as needed
   }
   ```

4. Check for data inconsistencies:
   ```java
   // Find carriers with invalid die numbers
   String sql = "SELECT o FROM CarrierMes o WHERE o.dieNumber IS NOT NULL AND o.dieNumber NOT IN (SELECT d.id FROM Die d)";
   Query q = entityManager().createQuery(sql, CarrierMes.class);
   
   List<CarrierMes> carriers = q.getResultList();
   System.out.println("Found " + carriers.size() + " carriers with invalid die numbers");
   
   // Find carriers with invalid locations
   sql = "SELECT o FROM CarrierMes o WHERE o.currentLocation IS NOT NULL AND o.currentLocation NOT IN (SELECT s.id FROM Stop s)";
   q = entityManager().createQuery(sql, CarrierMes.class);
   
   carriers = q.getResultList();
   System.out.println("Found " + carriers.size() + " carriers with invalid locations");
   ```

### Resolution
- For inconsistent carrier state: Update carrier data to reflect actual position
- For missing data: Implement data validation and required field checks
- For duplicate carriers: Implement uniqueness constraints and validation
- For maintenance bit issues: Ensure proper bit manipulation and interpretation
- For performance issues: Optimize queries and implement indexing
- For status issues: Implement status validation and consistency checks
- For orphaned carriers: Implement data integrity checks and cleanup processes

### Monitoring
- Track carrier movement rates and patterns
- Monitor carrier state changes for anomalies
- Set up alerts for carriers in unexpected states
- Track query performance on carrier tables
- Monitor carrier data consistency with physical system
- Set up periodic data validation checks
- Track maintenance bit patterns for preventive maintenance
- Monitor carrier inventory levels by die/part
- Set up alerts for carriers that haven't moved in extended periods