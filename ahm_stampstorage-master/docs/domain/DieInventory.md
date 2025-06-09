# DieInventory Technical Documentation

## Purpose
DieInventory.java defines an entity that represents the inventory status of a specific die type in the stamp storage system. This class extends EntityBase and provides a way to track the quantity of parts for a particular die, including those on hold or requiring inspection. It serves as a data structure for inventory reporting and management.

## Logic/Functionality
- Extends EntityBase to inherit basic entity functionality
- Represents the inventory status of a specific die type
- Tracks the total quantity of parts for a die
- Tracks the quantity of parts on hold
- Tracks the quantity of parts requiring inspection
- Provides getters and setters for all inventory attributes
- Used primarily for inventory reporting and management

## Flow
1. DieInventory objects are typically created by query methods in the CarrierMes class
2. The objects are populated with inventory data from the database
3. The inventory information is used for reporting and decision-making
4. The objects provide a consolidated view of die-specific inventory
5. The data can be used to make production and storage decisions

## Key Elements
- `dieName`: The name or description of the die
- `quantity`: The total quantity of parts for this die in the system
- `holdQuantity`: The quantity of parts that are currently on hold
- `inspectionRequiredQuantity`: The quantity of parts that require inspection
- Constructor that takes an ID parameter (inherited from EntityBase)
- Getter and setter methods for all attributes

## Usage
```java
// Create a new die inventory object
long id = 1L;
DieInventory dieInventory = new DieInventory(id);
dieInventory.setDieName("Front Left Door Panel");
dieInventory.setQuantity(100L);
dieInventory.setHoldQuantity(10L);
dieInventory.setInspectionRequiredQuantity(5L);

// Access die inventory information
System.out.println("Die: " + dieInventory.getDieName());
System.out.println("Total Quantity: " + dieInventory.getQuantity());
System.out.println("Hold Quantity: " + dieInventory.getHoldQuantity());
System.out.println("Inspection Required: " + dieInventory.getInspectionRequiredQuantity());
System.out.println("Available Quantity: " + 
                  (dieInventory.getQuantity() - 
                   dieInventory.getHoldQuantity() - 
                   dieInventory.getInspectionRequiredQuantity()));

// Get inventory by area using CarrierMes methods
StopArea area = StopArea.STORAGE;
List<DieInventory> inventoryByArea = CarrierMes.findPartCountsByDiesByStopArea(area);

System.out.println("Inventory for area: " + area);
for (DieInventory inventory : inventoryByArea) {
    System.out.println("Die: " + inventory.getDieName());
    System.out.println("  Total Quantity: " + inventory.getQuantity());
    System.out.println("  Hold Quantity: " + inventory.getHoldQuantity());
    System.out.println("  Inspection Required: " + inventory.getInspectionRequiredQuantity());
    System.out.println("  Available Quantity: " + 
                      (inventory.getQuantity() - 
                       inventory.getHoldQuantity() - 
                       inventory.getInspectionRequiredQuantity()));
}

// Get total inventory using CarrierMes methods
List<DieInventory> totalInventory = CarrierMes.findPartCountsByDies();

System.out.println("Total Inventory:");
for (DieInventory inventory : totalInventory) {
    System.out.println("Die: " + inventory.getDieName());
    System.out.println("  Total Quantity: " + inventory.getQuantity());
    System.out.println("  Hold Quantity: " + inventory.getHoldQuantity());
    System.out.println("  Inspection Required: " + inventory.getInspectionRequiredQuantity());
    System.out.println("  Available Quantity: " + 
                      (inventory.getQuantity() - 
                       inventory.getHoldQuantity() - 
                       inventory.getInspectionRequiredQuantity()));
}
```

## Debugging and Production Support

### Common Issues
1. Incorrect inventory counts
2. Mismatches between reported inventory and actual carriers
3. Null or zero values for quantities
4. Missing die names or descriptions
5. Performance issues when generating inventory reports
6. Inconsistencies between different inventory queries
7. Inventory data not reflecting recent carrier status changes

### Debugging Steps
1. Verify inventory data for a specific die:
   ```java
   // Check inventory for a specific die
   String dieName = "Front Left Door Panel";
   
   // Get inventory by area
   StopArea area = StopArea.STORAGE;
   List<DieInventory> inventoryByArea = CarrierMes.findPartCountsByDiesByStopArea(area);
   
   DieInventory targetInventory = null;
   for (DieInventory inventory : inventoryByArea) {
       if (dieName.equals(inventory.getDieName())) {
           targetInventory = inventory;
           break;
       }
   }
   
   if (targetInventory != null) {
       System.out.println("Found inventory for die: " + dieName);
       System.out.println("  Total Quantity: " + targetInventory.getQuantity());
       System.out.println("  Hold Quantity: " + targetInventory.getHoldQuantity());
       System.out.println("  Inspection Required: " + targetInventory.getInspectionRequiredQuantity());
       System.out.println("  Available Quantity: " + 
                         (targetInventory.getQuantity() - 
                          targetInventory.getHoldQuantity() - 
                          targetInventory.getInspectionRequiredQuantity()));
   } else {
       System.out.println("No inventory found for die: " + dieName + " in area: " + area);
   }
   ```

2. Compare inventory with actual carrier counts:
   ```java
   // Compare inventory with actual carrier counts for a specific die
   String dieName = "Front Left Door Panel";
   Die die = null;
   
   // Find the die by name
   List<Die> allDies = Die.findAllDies();
   for (Die d : allDies) {
       if (dieName.equals(d.getDescription())) {
           die = d;
           break;
       }
   }
   
   if (die != null) {
       Long dieId = die.getId();
       
       // Get inventory from DieInventory
       List<DieInventory> totalInventory = CarrierMes.findPartCountsByDies();
       DieInventory targetInventory = null;
       for (DieInventory inventory : totalInventory) {
           if (dieName.equals(inventory.getDieName())) {
               targetInventory = inventory;
               break;
           }
       }
       
       // Count carriers directly
       String sql = "SELECT COUNT(c) FROM CarrierMes c WHERE c.dieNumber = :dieId";
       Query q = entityManager().createQuery(sql, Long.class);
       q.setParameter("dieId", dieId.intValue());
       Long carrierCount = (Long) q.getSingleResult();
       
       // Count carriers on hold
       String holdSql = "SELECT COUNT(c) FROM CarrierMes c WHERE c.dieNumber = :dieId AND c.status = :status";
       Query holdQuery = entityManager().createQuery(holdSql, Long.class);
       holdQuery.setParameter("dieId", dieId.intValue());
       holdQuery.setParameter("status", CarrierStatus.ON_HOLD.type());
       Long holdCount = (Long) holdQuery.getSingleResult();
       
       // Count carriers requiring inspection
       Query inspectionQuery = entityManager().createQuery(holdSql, Long.class);
       inspectionQuery.setParameter("dieId", dieId.intValue());
       inspectionQuery.setParameter("status", CarrierStatus.INSPECTION_REQUIRED.type());
       Long inspectionCount = (Long) inspectionQuery.getSingleResult();
       
       System.out.println("Comparison for die: " + dieName + " (ID: " + dieId + ")");
       System.out.println("  DieInventory Total: " + 
                         (targetInventory != null ? targetInventory.getQuantity() : "N/A"));
       System.out.println("  Actual Carrier Count: " + carrierCount);
       System.out.println("  DieInventory Hold: " + 
                         (targetInventory != null ? targetInventory.getHoldQuantity() : "N/A"));
       System.out.println("  Actual Hold Count: " + holdCount);
       System.out.println("  DieInventory Inspection: " + 
                         (targetInventory != null ? targetInventory.getInspectionRequiredQuantity() : "N/A"));
       System.out.println("  Actual Inspection Count: " + inspectionCount);
   } else {
       System.out.println("Die not found with name: " + dieName);
   }
   ```

3. Check for inventory with null or zero quantities:
   ```java
   // Check for inventory with null or zero quantities
   List<DieInventory> totalInventory = CarrierMes.findPartCountsByDies();
   
   System.out.println("Checking for inventory with null or zero quantities:");
   for (DieInventory inventory : totalInventory) {
       if (inventory.getQuantity() == null || inventory.getQuantity() == 0) {
           System.out.println("  Die: " + inventory.getDieName() + " has null or zero quantity");
       }
       
       if (inventory.getHoldQuantity() == null) {
           System.out.println("  Die: " + inventory.getDieName() + " has null hold quantity");
       }
       
       if (inventory.getInspectionRequiredQuantity() == null) {
           System.out.println("  Die: " + inventory.getDieName() + " has null inspection quantity");
       }
   }
   ```

4. Test inventory calculation performance:
   ```java
   // Test performance of inventory calculations
   long startTime = System.currentTimeMillis();
   
   List<DieInventory> areaInventory = CarrierMes.findPartCountsByDiesByStopArea(StopArea.STORAGE);
   
   long areaTime = System.currentTimeMillis();
   System.out.println("Time to calculate area inventory: " + (areaTime - startTime) + "ms");
   System.out.println("Area inventory size: " + areaInventory.size());
   
   List<DieInventory> totalInventory = CarrierMes.findPartCountsByDies();
   
   long totalTime = System.currentTimeMillis();
   System.out.println("Time to calculate total inventory: " + (totalTime - areaTime) + "ms");
   System.out.println("Total inventory size: " + totalInventory.size());
   ```

5. Check for recent carrier status changes:
   ```java
   // Check for recent carrier status changes that might affect inventory
   long recentTimeThreshold = System.currentTimeMillis() - (60 * 60 * 1000); // Last hour
   Timestamp recentTimestamp = new Timestamp(recentTimeThreshold);
   
   String sql = "SELECT c FROM CarrierMes c WHERE c.updateDate > :timestamp";
   Query q = entityManager().createQuery(sql, CarrierMes.class);
   q.setParameter("timestamp", recentTimestamp);
   
   List<CarrierMes> recentlyUpdatedCarriers = q.getResultList();
   
   System.out.println("Found " + recentlyUpdatedCarriers.size() + 
                     " carriers updated in the last hour");
   
   for (CarrierMes carrier : recentlyUpdatedCarriers) {
       System.out.println("  Carrier: " + carrier.getCarrierNumber());
       System.out.println("  Die: " + carrier.getDieNumber());
       System.out.println("  Status: " + carrier.getStatus());
       System.out.println("  Update Time: " + carrier.getUpdateDate());
   }
   ```

### Resolution
- For incorrect inventory counts: Reconcile inventory data with actual carrier counts
- For mismatches: Implement regular inventory reconciliation processes
- For null or zero values: Implement proper null handling and default values
- For missing die names: Ensure proper die references and descriptions
- For performance issues: Optimize inventory calculation queries
- For inconsistencies: Ensure consistent inventory calculation methods
- For outdated data: Implement real-time or frequent inventory updates

### Monitoring
- Track inventory levels for critical dies
- Monitor the ratio of available to total inventory
- Track inventory changes over time
- Monitor for unusual changes in inventory levels
- Set up alerts for low inventory of critical dies
- Track inventory calculation performance
- Monitor for discrepancies between inventory reports