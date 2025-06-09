# Die Technical Documentation

## Purpose
Die.java defines an entity that represents a stamping die used in the manufacturing process. This entity stores information about dies, including their identification, description, part production volume, and visual representation details. Dies are fundamental components in the stamp storage system as they define the types of parts that can be produced and stored in carriers.

## Logic/Functionality
- Represents a die entity in the system
- Maps to the DIE_TBX database table
- Stores die information including description, part production volume, BPM part number, and visual representation details
- Provides JPA entity mapping for database persistence
- Implements standard CRUD operations for die management
- Supports transaction management for database operations
- Includes methods for finding and retrieving dies by various criteria
- Manages active/inactive status of dies

## Flow
1. Dies are defined and stored in the database during system setup or configuration
2. Dies are associated with carriers to identify the type of parts being carried
3. Dies are referenced by models to define left and right components
4. Dies are used in production planning and inventory management
5. Dies can be activated or deactivated as needed
6. Die information is used for visual representation in the user interface

## Key Elements
- `id`: The unique identifier for the die (primary key)
- `description`: A descriptive name or identifier for the die
- `partProductionVolume`: The production volume category for parts made with this die
- `bpmPartNumber`: The BPM (Bill of Process Management) part number associated with the die
- `imageFileName`: The filename of an image representing the die
- `active`: Boolean flag indicating if the die is currently active
- `textColor`: The color to use for text when displaying die information
- `backgroundColor`: The background color to use when displaying die information
- JPA entity mapping annotations for database persistence
- Standard CRUD methods (persist, remove, merge, etc.)
- Static finder methods for retrieving dies

## Usage
```java
// Create a new die
Die die = new Die();
die.setId(1001L);  // Usually set explicitly for dies
die.setDescription("Front Left Door Panel");
die.setPartProductionVolume(PartProductionVolume.HIGH);
die.setBpmPartNumber("BPM-12345");
die.setImageFileName("front_left_door.png");
die.setActive(true);
die.setTextColor("#FFFFFF");
die.setBackgroundColor("#0000FF");
die.persist();

// Find a die by ID
Long dieId = 1001L;
Die foundDie = Die.findDie(dieId);
if (foundDie != null) {
    System.out.println("Found die: " + foundDie.getDescription());
    System.out.println("Part Production Volume: " + foundDie.getPartProductionVolume());
    System.out.println("BPM Part Number: " + foundDie.getBpmPartNumber());
    System.out.println("Active: " + foundDie.getActive());
}

// Update a die
foundDie.setDescription("Front Left Door Panel - Updated");
foundDie.setActive(false);
foundDie.merge();

// Find all active dies
List<Die> activeDies = Die.findActiveDies();
System.out.println("Found " + activeDies.size() + " active dies");

// Find all dies
List<Die> allDies = Die.findAllDies();
System.out.println("Found " + allDies.size() + " total dies");

// Find dies with pagination
int page = 1;
int pageSize = 10;
List<Die> pagedDies = Die.findDieEntries((page - 1) * pageSize, pageSize);
System.out.println("Page " + page + " contains " + pagedDies.size() + " dies");

// Remove a die
foundDie.remove();
```

## Debugging and Production Support

### Common Issues
1. Missing or incomplete die information
2. Incorrect die associations with carriers or models
3. Inactive dies being used in active production
4. Missing image files for visual representation
5. Performance issues when querying dies
6. Duplicate die IDs or descriptions
7. Transaction-related issues during die management operations

### Debugging Steps
1. Verify die data:
   ```java
   // Check a specific die
   Long dieId = 1001L;
   Die die = Die.findDie(dieId);
   
   if (die != null) {
       System.out.println("Die ID: " + die.getId());
       System.out.println("Description: " + die.getDescription());
       System.out.println("Part Production Volume: " + die.getPartProductionVolume());
       System.out.println("BPM Part Number: " + die.getBpmPartNumber());
       System.out.println("Image File Name: " + die.getImageFileName());
       System.out.println("Active: " + die.getActive());
       System.out.println("Text Color: " + die.getTextColor());
       System.out.println("Background Color: " + die.getBackgroundColor());
       System.out.println("Version: " + die.getVersion());
   } else {
       System.out.println("Die not found with ID: " + dieId);
   }
   ```

2. Check for dies with missing information:
   ```java
   // Find dies with missing critical information
   String sql = "SELECT o FROM Die o WHERE o.description IS NULL OR o.partProductionVolume IS NULL";
   Query q = entityManager().createQuery(sql, Die.class);
   
   List<Die> incompleteDies = q.getResultList();
   System.out.println("Found " + incompleteDies.size() + " dies with missing information");
   
   for (Die incompleteDie : incompleteDies) {
       System.out.println("  Die ID: " + incompleteDie.getId());
       System.out.println("  Description: " + incompleteDie.getDescription());
       System.out.println("  Part Production Volume: " + incompleteDie.getPartProductionVolume());
       System.out.println("  Active: " + incompleteDie.getActive());
   }
   ```

3. Check for inactive dies being used in carriers:
   ```java
   // Find carriers using inactive dies
   String sql = "SELECT c FROM Carrier c WHERE c.die IN " +
                "(SELECT d FROM Die d WHERE d.active = false OR d.active IS NULL)";
   Query q = entityManager().createQuery(sql, Carrier.class);
   
   List<Carrier> carriersWithInactiveDies = q.getResultList();
   System.out.println("Found " + carriersWithInactiveDies.size() + 
                      " carriers using inactive dies");
   
   for (Carrier carrier : carriersWithInactiveDies) {
       System.out.println("  Carrier Number: " + carrier.getCarrierNumber());
       System.out.println("  Die ID: " + carrier.getDie().getId());
       System.out.println("  Die Description: " + carrier.getDie().getDescription());
       System.out.println("  Die Active: " + carrier.getDie().getActive());
   }
   ```

4. Check for dies used in models:
   ```java
   // Find models using specific dies
   Long dieId = 1001L;
   
   String leftSql = "SELECT m FROM Model m WHERE m.leftDie.id = :dieId";
   Query leftQuery = entityManager().createQuery(leftSql, Model.class);
   leftQuery.setParameter("dieId", dieId);
   
   List<Model> leftModels = leftQuery.getResultList();
   System.out.println("Found " + leftModels.size() + 
                      " models using die " + dieId + " as left die");
   
   for (Model model : leftModels) {
       System.out.println("  Model ID: " + model.getId());
       System.out.println("  Model Name: " + model.getName());
       System.out.println("  Model Description: " + model.getDescription());
   }
   
   String rightSql = "SELECT m FROM Model m WHERE m.rightDie.id = :dieId";
   Query rightQuery = entityManager().createQuery(rightSql, Model.class);
   rightQuery.setParameter("dieId", dieId);
   
   List<Model> rightModels = rightQuery.getResultList();
   System.out.println("Found " + rightModels.size() + 
                      " models using die " + dieId + " as right die");
   
   for (Model model : rightModels) {
       System.out.println("  Model ID: " + model.getId());
       System.out.println("  Model Name: " + model.getName());
       System.out.println("  Model Description: " + model.getDescription());
   }
   ```

5. Test die persistence:
   ```java
   // Test creating and persisting a new die
   try {
       Die testDie = new Die();
       testDie.setId(9999L);
       testDie.setDescription("Test Die - " + System.currentTimeMillis());
       testDie.setPartProductionVolume(PartProductionVolume.MEDIUM);
       testDie.setBpmPartNumber("BPM-TEST-" + System.currentTimeMillis());
       testDie.setImageFileName("test_die.png");
       testDie.setActive(true);
       testDie.setTextColor("#000000");
       testDie.setBackgroundColor("#FFFFFF");
       
       System.out.println("Persisting test die");
       testDie.persist();
       System.out.println("Die persisted with ID: " + testDie.getId());
       
       // Verify the die was persisted
       Die verifyDie = Die.findDie(testDie.getId());
       if (verifyDie != null) {
           System.out.println("Die verified in database");
           
           // Clean up the test die
           verifyDie.remove();
           System.out.println("Test die removed");
       } else {
           System.out.println("Failed to verify die in database");
       }
   } catch (Exception e) {
       System.out.println("Error testing die persistence: " + e.getMessage());
       e.printStackTrace();
   }
   ```

### Resolution
- For missing information: Implement validation to ensure required fields are provided
- For incorrect associations: Verify die references in carriers and models
- For inactive dies: Implement checks to prevent using inactive dies in production
- For missing images: Implement a process to verify and manage die images
- For performance issues: Optimize queries and implement indexing
- For duplicate dies: Implement uniqueness constraints and validation
- For transaction issues: Ensure proper transaction management and error handling

### Monitoring
- Track the number of active and inactive dies in the system
- Monitor for dies with missing or incomplete information
- Track die usage in carriers and models
- Monitor for changes to die status (active/inactive)
- Set up alerts for attempts to use inactive dies
- Track die inventory and usage patterns
- Monitor for missing or invalid die images