# PressActivity Technical Documentation

## Purpose
PressActivity.java defines an entity that represents the production activity of a press in the stamping system. This class tracks which dies are being used by which presses, along with production run information and quantity produced. It serves as a record of press operations and production metrics, providing historical data for analysis and reporting.

## Logic/Functionality
- Tracks press production activities
- Records which dies are being used by which presses
- Stores production run numbers for traceability
- Maintains quantity produced metrics
- Implements JPA entity functionality for database persistence
- Provides methods to find and manage press activity records

## Flow
1. When a press begins production with a specific die, a PressActivity record is created
2. As production continues, the quantity produced is updated
3. The system uses PressActivity data to track production metrics
4. Historical PressActivity records provide production history for analysis
5. PressActivity data can be used for reporting and planning

## Key Elements
- `pressName`: The name of the press
- `prodRunNumber`: The production run number
- `dieNumber`: The die being used in the press
- `quantityProduced`: The quantity of parts produced
- Static finder methods for retrieving PressActivity records
- Standard JPA entity methods for persistence operations

## Usage
```java
// Find a PressActivity by ID
PressActivity activity = PressActivity.findPressActivity(1L);
System.out.println("Press: " + activity.getPressName());
System.out.println("Production Run: " + activity.getProdRunNumber());
System.out.println("Die Number: " + activity.getDieNumber());
System.out.println("Quantity Produced: " + activity.getQuantityProduced());

// Find a PressActivity by press name
PressActivity pressActivity = PressActivity.findPressActivityByPressName("Press1");
if (pressActivity != null) {
    System.out.println("Current activity for Press1:");
    System.out.println("  Production Run: " + pressActivity.getProdRunNumber());
    System.out.println("  Die Number: " + pressActivity.getDieNumber());
    System.out.println("  Quantity Produced: " + pressActivity.getQuantityProduced());
} else {
    System.out.println("No activity found for Press1");
}

// Find all PressActivity records
List<PressActivity> allActivities = PressActivity.findAllPressActivitys();
System.out.println("Total press activities: " + allActivities.size());

// Create a new PressActivity
PressActivity newActivity = new PressActivity();
newActivity.setPressName("Press2");
newActivity.setProdRunNumber(12345);
newActivity.setDieNumber(678);
newActivity.setQuantityProduced(0);
newActivity.persist();

// Update an existing PressActivity
pressActivity.setQuantityProduced(pressActivity.getQuantityProduced() + 10);
pressActivity.merge();

// Delete a PressActivity
PressActivity oldActivity = PressActivity.findPressActivity(2L);
if (oldActivity != null) {
    oldActivity.remove();
}

// Find PressActivity records with pagination
List<PressActivity> pagedActivities = PressActivity.findPressActivityEntries(0, 10);
System.out.println("First 10 press activities:");
for (PressActivity activity : pagedActivities) {
    System.out.println("  Press: " + activity.getPressName() + 
                      ", Run: " + activity.getProdRunNumber() + 
                      ", Die: " + activity.getDieNumber() + 
                      ", Quantity: " + activity.getQuantityProduced());
}
```

## Debugging and Production Support

### Common Issues
1. Missing or incorrect press activity records
2. Inconsistent production run numbers
3. Inaccurate quantity produced values
4. Duplicate press activity records
5. Orphaned press activity records
6. Performance issues with press activity queries
7. Inconsistent die number references

### Debugging Steps
1. Verify press activity integrity:
   ```java
   // Check for missing or invalid press activity data
   System.out.println("Checking PressActivity integrity:");
   
   List<PressActivity> allActivities = PressActivity.findAllPressActivitys();
   System.out.println("Total press activities: " + allActivities.size());
   
   for (PressActivity activity : allActivities) {
       System.out.println("PressActivity: " + activity.getId());
       
       // Check press name
       if (activity.getPressName() == null || activity.getPressName().trim().isEmpty()) {
           System.out.println("  WARNING: Missing press name");
       }
       
       // Check production run number
       if (activity.getProdRunNumber() == null) {
           System.out.println("  WARNING: Missing production run number");
       }
       
       // Check die number
       if (activity.getDieNumber() == null) {
           System.out.println("  WARNING: Missing die number");
       }
       
       // Check quantity produced
       if (activity.getQuantityProduced() == null) {
           System.out.println("  WARNING: Missing quantity produced");
       } else if (activity.getQuantityProduced() < 0) {
           System.out.println("  WARNING: Invalid quantity produced: " + 
                             activity.getQuantityProduced());
       }
   }
   ```

2. Check for duplicate press activities:
   ```java
   // Check for duplicate press activities
   System.out.println("Checking for duplicate press activities:");
   
   Map<String, List<PressActivity>> activitiesByPress = new HashMap<>();
   
   for (PressActivity activity : PressActivity.findAllPressActivitys()) {
       String pressName = activity.getPressName();
       if (!activitiesByPress.containsKey(pressName)) {
           activitiesByPress.put(pressName, new ArrayList<>());
       }
       activitiesByPress.get(pressName).add(activity);
   }
   
   boolean hasDuplicates = false;
   for (Map.Entry<String, List<PressActivity>> entry : activitiesByPress.entrySet()) {
       if (entry.getValue().size() > 1) {
           hasDuplicates = true;
           System.out.println("  WARNING: Multiple activities found for press: " + entry.getKey());
           System.out.println("    Count: " + entry.getValue().size());
           
           for (PressActivity activity : entry.getValue()) {
               System.out.println("    ID: " + activity.getId() + 
                                 ", Run: " + activity.getProdRunNumber() + 
                                 ", Die: " + activity.getDieNumber() + 
                                 ", Quantity: " + activity.getQuantityProduced());
           }
       }
   }
   
   if (!hasDuplicates) {
       System.out.println("  No duplicate press activities found");
   }
   ```

3. Verify die references:
   ```java
   // Verify die references
   System.out.println("Verifying die references:");
   
   Set<Integer> validDieNumbers = new HashSet<>();
   for (Die die : Die.findAllDies()) {
       validDieNumbers.add(die.getId().intValue());
   }
   
   for (PressActivity activity : PressActivity.findAllPressActivitys()) {
       Integer dieNumber = activity.getDieNumber();
       if (dieNumber != null) {
           if (!validDieNumbers.contains(dieNumber)) {
               System.out.println("  WARNING: PressActivity " + activity.getId() + 
                                 " references non-existent die: " + dieNumber);
           }
       }
   }
   ```

4. Check for inconsistent production run numbers:
   ```java
   // Check for inconsistent production run numbers
   System.out.println("Checking for inconsistent production run numbers:");
   
   // Get all production run numbers from carriers
   Set<Integer> carrierProdRunNumbers = new HashSet<>();
   for (CarrierMes carrier : CarrierMes.findAllCarriers()) {
       if (carrier.getProductionRunNumber() != null) {
           carrierProdRunNumbers.add(carrier.getProductionRunNumber());
       }
   }
   
   // Check if press activity production run numbers exist in carriers
   for (PressActivity activity : PressActivity.findAllPressActivitys()) {
       Integer prodRunNumber = activity.getProdRunNumber();
       if (prodRunNumber != null && !carrierProdRunNumbers.contains(prodRunNumber)) {
           System.out.println("  WARNING: PressActivity " + activity.getId() + 
                             " has production run number not found in carriers: " + 
                             prodRunNumber);
       }
   }
   ```

5. Check for orphaned press activities:
   ```java
   // Check for orphaned press activities (no corresponding carriers)
   System.out.println("Checking for orphaned press activities:");
   
   // Get all press names
   Set<String> knownPresses = new HashSet<>();
   // Add known presses from your system
   knownPresses.add("Press1");
   knownPresses.add("Press2");
   knownPresses.add("Press3");
   knownPresses.add("Press4");
   
   for (PressActivity activity : PressActivity.findAllPressActivitys()) {
       String pressName = activity.getPressName();
       if (pressName != null && !knownPresses.contains(pressName)) {
           System.out.println("  WARNING: PressActivity " + activity.getId() + 
                             " references unknown press: " + pressName);
       }
   }
   ```

6. Test performance:
   ```java
   // Test performance of common operations
   System.out.println("Testing performance of common operations:");
   
   // Test findAllPressActivitys
   long startTime = System.currentTimeMillis();
   List<PressActivity> allActivitiesPerf = PressActivity.findAllPressActivitys();
   long endTime = System.currentTimeMillis();
   System.out.println("  findAllPressActivitys: " + (endTime - startTime) + 
                     "ms for " + allActivitiesPerf.size() + " records");
   
   // Test findPressActivityByPressName for each press
   Set<String> pressNames = new HashSet<>();
   for (PressActivity activity : allActivitiesPerf) {
       if (activity.getPressName() != null) {
           pressNames.add(activity.getPressName());
       }
   }
   
   for (String pressName : pressNames) {
       startTime = System.currentTimeMillis();
       PressActivity activity = PressActivity.findPressActivityByPressName(pressName);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findPressActivityByPressName for " + pressName + 
                         ": " + (endTime - startTime) + "ms, found: " + 
                         (activity != null ? "Yes" : "No"));
   }
   
   // Test findPressActivity by ID
   if (!allActivitiesPerf.isEmpty()) {
       PressActivity sampleActivity = allActivitiesPerf.get(0);
       startTime = System.currentTimeMillis();
       PressActivity foundActivity = PressActivity.findPressActivity(sampleActivity.getId());
       endTime = System.currentTimeMillis();
       
       System.out.println("  findPressActivity(ID): " + (endTime - startTime) + 
                         "ms, found: " + (foundActivity != null ? "Yes" : "No"));
   }
   ```

7. Check for inaccurate quantity produced values:
   ```java
   // Check for inaccurate quantity produced values
   System.out.println("Checking for inaccurate quantity produced values:");
   
   // Get carrier counts by production run number
   Map<Integer, Integer> carrierCountsByProdRun = new HashMap<>();
   for (CarrierMes carrier : CarrierMes.findAllCarriers()) {
       Integer prodRunNumber = carrier.getProductionRunNumber();
       Integer quantity = carrier.getQuantity();
       
       if (prodRunNumber != null && quantity != null) {
           if (!carrierCountsByProdRun.containsKey(prodRunNumber)) {
               carrierCountsByProdRun.put(prodRunNumber, 0);
           }
           carrierCountsByProdRun.put(prodRunNumber, 
                                     carrierCountsByProdRun.get(prodRunNumber) + quantity);
       }
   }
   
   // Compare with press activity quantities
   for (PressActivity activity : PressActivity.findAllPressActivitys()) {
       Integer prodRunNumber = activity.getProdRunNumber();
       Integer quantityProduced = activity.getQuantityProduced();
       
       if (prodRunNumber != null && quantityProduced != null && 
           carrierCountsByProdRun.containsKey(prodRunNumber)) {
           
           Integer carrierCount = carrierCountsByProdRun.get(prodRunNumber);
           int discrepancy = quantityProduced - carrierCount;
           
           if (Math.abs(discrepancy) > 10) { // Allow small discrepancies
               System.out.println("  WARNING: PressActivity " + activity.getId() + 
                                 " has quantity discrepancy for production run " + 
                                 prodRunNumber);
               System.out.println("    Press activity quantity: " + quantityProduced);
               System.out.println("    Carrier total quantity: " + carrierCount);
               System.out.println("    Discrepancy: " + discrepancy);
           }
       }
   }
   ```

### Resolution
- For missing or incorrect press activity records: Add or update records with correct information
- For inconsistent production run numbers: Align production run numbers with carrier data
- For inaccurate quantity produced values: Update quantities based on carrier counts
- For duplicate press activity records: Consolidate or remove duplicate records
- For orphaned press activity records: Update or remove orphaned records
- For performance issues: Optimize queries and consider indexing
- For inconsistent die number references: Update with correct die numbers

### Monitoring
- Track press activity creation and updates
- Monitor for duplicate press activities
- Track quantity produced metrics
- Monitor for orphaned press activities
- Track performance of press activity queries
- Monitor for inconsistencies between press activities and carriers
- Set up alerts for data integrity issues