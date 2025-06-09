# WeldSchedule Technical Documentation

## Purpose
WeldSchedule.java defines an entity that represents the production schedule for a welding line. This class models the planned and remaining production quantities for both left-hand and right-hand parts, as well as information about current and next models. It serves as a reference for production planning and tracking.

## Logic/Functionality
- Represents production schedules for welding lines
- Tracks planned and remaining production quantities for left and right parts
- Stores information about current and next models
- Provides methods to find and manage schedule records
- Implements JPA entity functionality for database persistence

## Flow
1. Production schedules are created or updated with planned quantities
2. As production progresses, remaining quantities are updated
3. When a model change occurs, current model is updated
4. Next model information is maintained for planning purposes
5. The system uses this information for production planning and tracking

## Key Elements
- `weldLine`: The identifier for the welding line
- `leftHandProdPlan`: The planned production quantity for left-hand parts
- `leftHandProdRemaining`: The remaining production quantity for left-hand parts
- `rightHandProdPlan`: The planned production quantity for right-hand parts
- `rightHandProdRemaining`: The remaining production quantity for right-hand parts
- `currentModel`: The current model in production
- `nextModel`: The next model planned for production
- `nextQuantity`: The planned quantity for the next model
- Standard JPA entity methods for persistence operations

## Usage
```java
// Find a weld schedule by ID
WeldSchedule schedule = WeldSchedule.findWeldSchedule(1L);
System.out.println("Weld line: " + schedule.getWeldLine());
System.out.println("Current model: " + schedule.getCurrentModel());
System.out.println("Left-hand planned: " + schedule.getLeftHandProdPlan());
System.out.println("Left-hand remaining: " + schedule.getLeftHandProdRemaining());
System.out.println("Right-hand planned: " + schedule.getRightHandProdPlan());
System.out.println("Right-hand remaining: " + schedule.getRightHandProdRemaining());
System.out.println("Next model: " + schedule.getNextModel());
System.out.println("Next quantity: " + schedule.getNextQuantity());

// Find all weld schedules
List<WeldSchedule> allSchedules = WeldSchedule.findAllWeldSchedules();
System.out.println("Total schedules: " + allSchedules.size());

// Find weld schedule entries with pagination
List<WeldSchedule> scheduleEntries = WeldSchedule.findWeldScheduleEntries(0, 10);
System.out.println("Schedule entries (page 1): " + scheduleEntries.size());

// Create a new weld schedule
WeldSchedule newSchedule = new WeldSchedule();
newSchedule.setWeldLine(1);
newSchedule.setLeftHandProdPlan(100);
newSchedule.setLeftHandProdRemaining(100);
newSchedule.setRightHandProdPlan(100);
newSchedule.setRightHandProdRemaining(100);
newSchedule.setCurrentModel(1);
newSchedule.setNextModel(2);
newSchedule.setNextQuantity(50);
newSchedule.persist();

// Update an existing weld schedule
WeldSchedule existingSchedule = WeldSchedule.findWeldSchedule(1L);
existingSchedule.setLeftHandProdRemaining(90);
existingSchedule.setRightHandProdRemaining(90);
existingSchedule.merge();

// Update production quantities
WeldSchedule schedule = WeldSchedule.findWeldSchedule(1L);
int leftConsumed = 5;
int rightConsumed = 5;
schedule.setLeftHandProdRemaining(schedule.getLeftHandProdRemaining() - leftConsumed);
schedule.setRightHandProdRemaining(schedule.getRightHandProdRemaining() - rightConsumed);
schedule.merge();

// Update current model
WeldSchedule schedule = WeldSchedule.findWeldSchedule(1L);
schedule.setCurrentModel(schedule.getNextModel());
schedule.setLeftHandProdPlan(schedule.getNextQuantity());
schedule.setLeftHandProdRemaining(schedule.getNextQuantity());
schedule.setRightHandProdPlan(schedule.getNextQuantity());
schedule.setRightHandProdRemaining(schedule.getNextQuantity());
schedule.setNextModel(3);
schedule.setNextQuantity(75);
schedule.merge();

// Delete a weld schedule
WeldSchedule obsoleteSchedule = WeldSchedule.findWeldSchedule(2L);
if (obsoleteSchedule != null) {
    obsoleteSchedule.remove();
}
```

## Debugging and Production Support

### Common Issues
1. Inconsistent production quantities
2. Missing or incorrect model references
3. Negative remaining quantities
4. Orphaned schedule records
5. Duplicate schedule records for the same weld line
6. Inconsistent current and next model information
7. Performance issues with schedule queries

### Debugging Steps
1. Check for inconsistent production quantities:
   ```java
   // Check for inconsistent production quantities
   System.out.println("Checking for inconsistent production quantities:");
   
   // Get all schedules
   List<WeldSchedule> allSchedules = WeldSchedule.findAllWeldSchedules();
   
   // Check for inconsistent quantities
   for (WeldSchedule schedule : allSchedules) {
       System.out.println("  Weld line: " + schedule.getWeldLine());
       
       // Get quantities
       Integer leftPlan = schedule.getLeftHandProdPlan();
       Integer leftRemaining = schedule.getLeftHandProdRemaining();
       Integer rightPlan = schedule.getRightHandProdPlan();
       Integer rightRemaining = schedule.getRightHandProdRemaining();
       
       System.out.println("    Left planned: " + leftPlan);
       System.out.println("    Left remaining: " + leftRemaining);
       System.out.println("    Right planned: " + rightPlan);
       System.out.println("    Right remaining: " + rightRemaining);
       
       // Check for null quantities
       if (leftPlan == null) {
           System.out.println("    WARNING: Null left planned quantity");
       }
       
       if (leftRemaining == null) {
           System.out.println("    WARNING: Null left remaining quantity");
       }
       
       if (rightPlan == null) {
           System.out.println("    WARNING: Null right planned quantity");
       }
       
       if (rightRemaining == null) {
           System.out.println("    WARNING: Null right remaining quantity");
       }
       
       // Check for remaining > planned
       if (leftRemaining != null && leftPlan != null && leftRemaining > leftPlan) {
           System.out.println("    WARNING: Left remaining quantity exceeds planned quantity");
       }
       
       if (rightRemaining != null && rightPlan != null && rightRemaining > rightPlan) {
           System.out.println("    WARNING: Right remaining quantity exceeds planned quantity");
       }
       
       // Check for negative remaining
       if (leftRemaining != null && leftRemaining < 0) {
           System.out.println("    WARNING: Negative left remaining quantity");
       }
       
       if (rightRemaining != null && rightRemaining < 0) {
           System.out.println("    WARNING: Negative right remaining quantity");
       }
   }
   ```

2. Check for missing or incorrect model references:
   ```java
   // Check for missing or incorrect model references
   System.out.println("Checking for missing or incorrect model references:");
   
   // Get all schedules
   List<WeldSchedule> allSchedules = WeldSchedule.findAllWeldSchedules();
   
   // Get all model IDs
   List<Model> allModels = Model.findAllModels();
   Set<Integer> modelIds = new HashSet<>();
   for (Model model : allModels) {
       modelIds.add(model.getId().intValue());
   }
   
   // Check for missing or incorrect model references
   for (WeldSchedule schedule : allSchedules) {
       System.out.println("  Weld line: " + schedule.getWeldLine());
       
       // Get model references
       Integer currentModel = schedule.getCurrentModel();
       Integer nextModel = schedule.getNextModel();
       
       System.out.println("    Current model: " + currentModel);
       System.out.println("    Next model: " + nextModel);
       
       // Check for null models
       if (currentModel == null) {
           System.out.println("    WARNING: Null current model");
       }
       
       if (nextModel == null) {
           System.out.println("    WARNING: Null next model");
       }
       
       // Check for non-existent models
       if (currentModel != null && !modelIds.contains(currentModel)) {
           System.out.println("    WARNING: Current model " + currentModel + " does not exist");
       }
       
       if (nextModel != null && !modelIds.contains(nextModel)) {
           System.out.println("    WARNING: Next model " + nextModel + " does not exist");
       }
       
       // Check for same current and next model
       if (currentModel != null && nextModel != null && currentModel.equals(nextModel)) {
           System.out.println("    WARNING: Current model and next model are the same");
       }
   }
   ```

3. Check for negative remaining quantities:
   ```java
   // Check for negative remaining quantities
   System.out.println("Checking for negative remaining quantities:");
   
   // Get all schedules
   List<WeldSchedule> allSchedules = WeldSchedule.findAllWeldSchedules();
   
   // Check for negative remaining quantities
   boolean hasNegativeQuantities = false;
   for (WeldSchedule schedule : allSchedules) {
       Integer leftRemaining = schedule.getLeftHandProdRemaining();
       Integer rightRemaining = schedule.getRightHandProdRemaining();
       
       if (leftRemaining != null && leftRemaining < 0) {
           hasNegativeQuantities = true;
           System.out.println("  Weld line " + schedule.getWeldLine() + 
                             " has negative left remaining quantity: " + leftRemaining);
       }
       
       if (rightRemaining != null && rightRemaining < 0) {
           hasNegativeQuantities = true;
           System.out.println("  Weld line " + schedule.getWeldLine() + 
                             " has negative right remaining quantity: " + rightRemaining);
       }
   }
   
   if (!hasNegativeQuantities) {
       System.out.println("  No negative remaining quantities found");
   }
   ```

4. Check for orphaned schedule records:
   ```java
   // Check for orphaned schedule records
   System.out.println("Checking for orphaned schedule records:");
   
   // Get all schedules
   List<WeldSchedule> allSchedules = WeldSchedule.findAllWeldSchedules();
   
   // Get all weld lines
   List<OrderMgr> allOrderMgrs = OrderMgr.findAllOrderMgrs();
   Set<Integer> weldLines = new HashSet<>();
   for (OrderMgr orderMgr : allOrderMgrs) {
       // Assuming weld line is derived from order manager ID or some other property
       // This is just an example and should be adjusted based on actual implementation
       weldLines.add(orderMgr.getId().intValue());
   }
   
   // Check for orphaned schedule records
   boolean hasOrphanedRecords = false;
   for (WeldSchedule schedule : allSchedules) {
       Integer weldLine = schedule.getWeldLine();
       
       if (weldLine == null) {
           hasOrphanedRecords = true;
           System.out.println("  Schedule " + schedule.getId() + " has null weld line");
           continue;
       }
       
       if (!weldLines.contains(weldLine)) {
           hasOrphanedRecords = true;
           System.out.println("  Schedule " + schedule.getId() + 
                             " references non-existent weld line: " + weldLine);
       }
   }
   
   if (!hasOrphanedRecords) {
       System.out.println("  No orphaned schedule records found");
   }
   ```

5. Check for duplicate schedule records for the same weld line:
   ```java
   // Check for duplicate schedule records for the same weld line
   System.out.println("Checking for duplicate schedule records for the same weld line:");
   
   // Get all schedules
   List<WeldSchedule> allSchedules = WeldSchedule.findAllWeldSchedules();
   
   // Group schedules by weld line
   Map<Integer, List<WeldSchedule>> schedulesByWeldLine = new HashMap<>();
   for (WeldSchedule schedule : allSchedules) {
       Integer weldLine = schedule.getWeldLine();
       
       if (weldLine == null) {
           continue;
       }
       
       if (!schedulesByWeldLine.containsKey(weldLine)) {
           schedulesByWeldLine.put(weldLine, new ArrayList<>());
       }
       
       schedulesByWeldLine.get(weldLine).add(schedule);
   }
   
   // Check for duplicates
   boolean hasDuplicates = false;
   for (Map.Entry<Integer, List<WeldSchedule>> entry : schedulesByWeldLine.entrySet()) {
       if (entry.getValue().size() > 1) {
           hasDuplicates = true;
           System.out.println("  Weld line " + entry.getKey() + 
                             " has " + entry.getValue().size() + " schedule records");
           
           System.out.println("    Duplicate records:");
           for (WeldSchedule schedule : entry.getValue()) {
               System.out.println("      ID: " + schedule.getId() + 
                                 ", Current model: " + schedule.getCurrentModel() + 
                                 ", Next model: " + schedule.getNextModel());
           }
       }
   }
   
   if (!hasDuplicates) {
       System.out.println("  No duplicate schedule records found");
   }
   ```

6. Check for inconsistent current and next model information:
   ```java
   // Check for inconsistent current and next model information
   System.out.println("Checking for inconsistent current and next model information:");
   
   // Get all schedules
   List<WeldSchedule> allSchedules = WeldSchedule.findAllWeldSchedules();
   
   // Check for inconsistent model information
   for (WeldSchedule schedule : allSchedules) {
       System.out.println("  Weld line: " + schedule.getWeldLine());
       
       // Get model information
       Integer currentModel = schedule.getCurrentModel();
       Integer nextModel = schedule.getNextModel();
       Integer nextQuantity = schedule.getNextQuantity();
       
       System.out.println("    Current model: " + currentModel);
       System.out.println("    Next model: " + nextModel);
       System.out.println("    Next quantity: " + nextQuantity);
       
       // Check for null next model with non-null next quantity
       if (nextModel == null && nextQuantity != null) {
           System.out.println("    WARNING: Null next model with non-null next quantity");
       }
       
       // Check for non-null next model with null next quantity
       if (nextModel != null && nextQuantity == null) {
           System.out.println("    WARNING: Non-null next model with null next quantity");
       }
       
       // Check for zero next quantity
       if (nextQuantity != null && nextQuantity == 0) {
           System.out.println("    WARNING: Zero next quantity");
       }
       
       // Check for negative next quantity
       if (nextQuantity != null && nextQuantity < 0) {
           System.out.println("    WARNING: Negative next quantity");
       }
   }
   ```

7. Test schedule query performance:
   ```java
   // Test schedule query performance
   System.out.println("Testing schedule query performance:");
   
   // Test findAllWeldSchedules
   long startTime = System.currentTimeMillis();
   List<WeldSchedule> allSchedulesPerf = WeldSchedule.findAllWeldSchedules();
   long endTime = System.currentTimeMillis();
   System.out.println("  findAllWeldSchedules: " + (endTime - startTime) + 
                     "ms for " + allSchedulesPerf.size() + " schedules");
   
   // Test findWeldSchedule by ID
   if (!allSchedulesPerf.isEmpty()) {
       WeldSchedule sampleSchedule = allSchedulesPerf.get(0);
       startTime = System.currentTimeMillis();
       WeldSchedule foundSchedule = WeldSchedule.findWeldSchedule(sampleSchedule.getId());
       endTime = System.currentTimeMillis();
       
       System.out.println("  findWeldSchedule(ID): " + (endTime - startTime) + 
                         "ms, found: " + (foundSchedule != null ? "Yes" : "No"));
   }
   
   // Test findWeldScheduleEntries with pagination
   startTime = System.currentTimeMillis();
   List<WeldSchedule> scheduleEntries = WeldSchedule.findWeldScheduleEntries(0, 10);
   endTime = System.currentTimeMillis();
   
   System.out.println("  findWeldScheduleEntries: " + (endTime - startTime) + 
                     "ms, found: " + scheduleEntries.size() + " schedules");
   ```

### Resolution
- For inconsistent production quantities: Update quantities to consistent values
- For missing or incorrect model references: Update model references
- For negative remaining quantities: Reset remaining quantities to zero or positive values
- For orphaned schedule records: Remove or update orphaned records
- For duplicate schedule records: Remove duplicate records
- For inconsistent current and next model information: Update model information
- For performance issues: Optimize queries or add indexes

### Monitoring
- Monitor production quantity changes
- Track model references
- Monitor for negative remaining quantities
- Track orphaned schedule records
- Monitor for duplicate schedule records
- Track current and next model information
- Monitor schedule query performance
- Set up alerts for inconsistent production quantities
- Track model changes
- Monitor for orphaned or duplicate schedule records