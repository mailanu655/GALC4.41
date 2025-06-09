# Defect Technical Documentation

## Purpose
Defect.java defines an entity that represents quality issues or defects identified in parts carried by carriers in the stamp storage system. This entity tracks defect information including type, location, repair status, and associated carrier details, enabling quality control and defect management throughout the manufacturing process.

## Logic/Functionality
- Represents a defect entity in the system
- Maps to the DEFECT_TBX database table
- Stores defect information including carrier number, production run, defect type, rework method, location, timestamps, and repair status
- Provides JPA entity mapping for database persistence
- Implements standard CRUD operations for defect management
- Supports transaction management for database operations
- Includes methods for finding and retrieving defects by various criteria

## Flow
1. When a quality issue is identified in a part, a defect record is created
2. The defect is associated with a specific carrier and production run
3. The defect type, location, and rework method are recorded
4. The defect status is tracked (repaired or not repaired)
5. Defects can be queried by carrier number and production run
6. Defects can be updated when repairs are completed
7. Defect records can be removed when no longer needed

## Key Elements
- `id`: The unique identifier for the defect (primary key)
- `carrierNumber`: The carrier number associated with the defect
- `productionRunNo`: The production run number associated with the defect
- `defectType`: The type of defect (enumerated value)
- `reworkMethod`: The method used to repair the defect (enumerated value)
- `xArea`: The X coordinate of the defect location
- `yArea`: The Y coordinate of the defect location
- `defectTimestamp`: The timestamp when the defect was recorded
- `defectRepaired`: Boolean flag indicating if the defect has been repaired
- `note`: Additional notes about the defect
- `source`: The source system or process that identified the defect
- JPA entity mapping annotations for database persistence
- Standard CRUD methods (persist, remove, merge, etc.)
- Static finder methods for retrieving defects

## Usage
```java
// Create a new defect
Defect defect = new Defect();
defect.setCarrierNumber(1001);
defect.setProductionRunNo(5001);
defect.setDefectType(DEFECT_TYPE.SCRATCH);
defect.setReworkMethod(REWORK_METHOD.POLISH);
defect.setXArea(150);
defect.setYArea("A5");
defect.setDefectTimestamp(new Date());
defect.setDefectRepaired(false);
defect.setNote("Deep scratch on surface");
defect.setSource("VISUAL_INSPECTION");
defect.persist();

// Find defects by carrier number and production run
Integer carrierNumber = 1001;
Integer productionRunNo = 5001;
List<Defect> defects = Defect.findDefectsByCarrierNumberAndProductionRunNo(carrierNumber, productionRunNo);

System.out.println("Found " + defects.size() + " defects for carrier " + carrierNumber + 
                   " and production run " + productionRunNo);

for (Defect d : defects) {
    System.out.println("Defect ID: " + d.getId());
    System.out.println("Type: " + d.getDefectType());
    System.out.println("Location: X=" + d.getXArea() + ", Y=" + d.getYArea());
    System.out.println("Repaired: " + d.getDefectRepaired());
    System.out.println("Notes: " + d.getNote());
}

// Update a defect's repair status
Long defectId = 1L;
Defect defectToUpdate = Defect.findDefect(defectId);
if (defectToUpdate != null) {
    defectToUpdate.setDefectRepaired(true);
    defectToUpdate.setNote(defectToUpdate.getNote() + " - Repaired on " + new Date());
    defectToUpdate.merge();
    System.out.println("Defect " + defectId + " marked as repaired");
}

// Remove all defects for a carrier and production run
Defect.removeDefectsByCarrierNumberAndProductionRunNo(carrierNumber, productionRunNo);
System.out.println("Removed all defects for carrier " + carrierNumber + 
                   " and production run " + productionRunNo);
```

## Debugging and Production Support

### Common Issues
1. Missing or incomplete defect information
2. Incorrect association with carriers or production runs
3. Defects not being properly tracked or updated
4. Performance issues when querying defects
5. Inconsistencies between defect records and actual part conditions
6. Transaction-related issues during defect management operations
7. Enumeration value mismatches for defect types or rework methods

### Debugging Steps
1. Verify defect data:
   ```java
   // Check a specific defect
   Long defectId = 1L;
   Defect defect = Defect.findDefect(defectId);
   
   if (defect != null) {
       System.out.println("Defect ID: " + defect.getId());
       System.out.println("Carrier Number: " + defect.getCarrierNumber());
       System.out.println("Production Run: " + defect.getProductionRunNo());
       System.out.println("Defect Type: " + defect.getDefectType());
       System.out.println("Rework Method: " + defect.getReworkMethod());
       System.out.println("Location: X=" + defect.getXArea() + ", Y=" + defect.getYArea());
       System.out.println("Timestamp: " + defect.getDefectTimestamp());
       System.out.println("Repaired: " + defect.getDefectRepaired());
       System.out.println("Note: " + defect.getNote());
       System.out.println("Source: " + defect.getSource());
       System.out.println("Version: " + defect.getVersion());
   } else {
       System.out.println("Defect not found with ID: " + defectId);
   }
   ```

2. Check for defects with missing information:
   ```java
   // Find defects with missing critical information
   String sql = "SELECT o FROM Defect o WHERE o.defectType IS NULL OR o.reworkMethod IS NULL " +
                "OR o.xArea IS NULL OR o.yArea IS NULL OR o.defectTimestamp IS NULL";
   Query q = entityManager().createQuery(sql, Defect.class);
   
   List<Defect> incompleteDefects = q.getResultList();
   System.out.println("Found " + incompleteDefects.size() + " defects with missing information");
   
   for (Defect incompleteDefect : incompleteDefects) {
       System.out.println("  Defect ID: " + incompleteDefect.getId());
       System.out.println("  Carrier: " + incompleteDefect.getCarrierNumber());
       System.out.println("  Type: " + incompleteDefect.getDefectType());
       System.out.println("  Rework Method: " + incompleteDefect.getReworkMethod());
       System.out.println("  Location: X=" + incompleteDefect.getXArea() + 
                          ", Y=" + incompleteDefect.getYArea());
       System.out.println("  Timestamp: " + incompleteDefect.getDefectTimestamp());
   }
   ```

3. Check for defects by carrier and production run:
   ```java
   // Find defects for a specific carrier and production run
   Integer carrierNumber = 1001;
   Integer productionRunNo = 5001;
   
   List<Defect> defects = Defect.findDefectsByCarrierNumberAndProductionRunNo(
       carrierNumber, productionRunNo);
   
   System.out.println("Found " + defects.size() + " defects for carrier " + 
                      carrierNumber + " and production run " + productionRunNo);
   
   for (Defect defect : defects) {
       System.out.println("  Defect ID: " + defect.getId());
       System.out.println("  Type: " + defect.getDefectType());
       System.out.println("  Repaired: " + defect.getDefectRepaired());
       System.out.println("  Note: " + defect.getNote());
   }
   ```

4. Check for unrepaired defects:
   ```java
   // Find unrepaired defects
   String sql = "SELECT o FROM Defect o WHERE o.defectRepaired = false OR o.defectRepaired IS NULL";
   Query q = entityManager().createQuery(sql, Defect.class);
   
   List<Defect> unrepairedDefects = q.getResultList();
   System.out.println("Found " + unrepairedDefects.size() + " unrepaired defects");
   
   for (Defect unrepairedDefect : unrepairedDefects) {
       System.out.println("  Defect ID: " + unrepairedDefect.getId());
       System.out.println("  Carrier: " + unrepairedDefect.getCarrierNumber());
       System.out.println("  Type: " + unrepairedDefect.getDefectType());
       System.out.println("  Timestamp: " + unrepairedDefect.getDefectTimestamp());
   }
   ```

5. Test defect persistence:
   ```java
   // Test creating and persisting a new defect
   try {
       Defect testDefect = new Defect();
       testDefect.setCarrierNumber(9999);
       testDefect.setProductionRunNo(9999);
       testDefect.setDefectType(DEFECT_TYPE.SCRATCH);
       testDefect.setReworkMethod(REWORK_METHOD.POLISH);
       testDefect.setXArea(150);
       testDefect.setYArea("A5");
       testDefect.setDefectTimestamp(new Date());
       testDefect.setDefectRepaired(false);
       testDefect.setNote("Test defect - " + System.currentTimeMillis());
       testDefect.setSource("TEST");
       
       System.out.println("Persisting test defect");
       testDefect.persist();
       System.out.println("Defect persisted with ID: " + testDefect.getId());
       
       // Verify the defect was persisted
       Defect verifyDefect = Defect.findDefect(testDefect.getId());
       if (verifyDefect != null) {
           System.out.println("Defect verified in database");
           
           // Clean up the test defect
           verifyDefect.remove();
           System.out.println("Test defect removed");
       } else {
           System.out.println("Failed to verify defect in database");
       }
   } catch (Exception e) {
       System.out.println("Error testing defect persistence: " + e.getMessage());
       e.printStackTrace();
   }
   ```

### Resolution
- For missing information: Implement validation to ensure required fields are provided
- For incorrect associations: Verify carrier and production run references
- For tracking issues: Implement proper status updates and tracking mechanisms
- For performance issues: Optimize queries and implement indexing
- For inconsistencies: Implement regular reconciliation processes
- For transaction issues: Ensure proper transaction management and error handling
- For enumeration mismatches: Validate enumeration values and implement proper error handling

### Monitoring
- Track the number of defects in the system
- Monitor the ratio of repaired to unrepaired defects
- Track defect creation and resolution rates
- Monitor for defects that remain unrepaired for extended periods
- Set up alerts for carriers with multiple defects
- Track defect patterns by type, location, or production run
- Monitor for unusual spikes in defect creation