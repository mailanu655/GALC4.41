# Model Technical Documentation

## Purpose
Model.java defines an entity that represents a model in the stamping system. A model consists of a left die and a right die, which together form a complete part set. This class is central to the order fulfillment process as it defines what parts are needed for a specific vehicle model. The Model entity is used throughout the system to track and manage the production of specific vehicle components.

## Logic/Functionality
- Represents a vehicle model with associated left and right dies
- Provides methods to access and modify model attributes
- Implements JPA entity functionality for database persistence
- Includes methods for finding and managing model records
- Provides a mechanism to determine if a model is active based on the active status of its dies
- Supports the order fulfillment process by defining required parts

## Flow
1. Models are defined in the system with a name, description, and associated dies
2. The system uses models to determine what parts are needed for production
3. When creating orders, models are selected to specify the required parts
4. The system checks if models are active before allowing them to be used in orders
5. Models are used to track and manage the production of specific vehicle components

## Key Elements
- `name`: The name of the model
- `description`: A detailed description of the model
- `leftDie`: The die used for the left-side component
- `rightDie`: The die used for the right-side component
- Entity management methods for database operations
- Finder methods for retrieving models from the database
- `getActive()`: Method to determine if a model is active based on the active status of its dies

## Usage
```java
// Create a new model
Model model = new Model();
model.setName("Accord Front Door");
model.setDescription("Honda Accord Front Door Panel Set");

// Set the left and right dies
Die leftDie = Die.findDie(101L);  // Left door panel die
Die rightDie = Die.findDie(102L); // Right door panel die
model.setLeftDie(leftDie);
model.setRightDie(rightDie);

// Persist the model to the database
model.persist();

// Find a model by ID
Long modelId = 1L;
Model foundModel = Model.findModel(modelId);
System.out.println("Found model: " + foundModel.getName());
System.out.println("Description: " + foundModel.getDescription());
System.out.println("Left Die: " + foundModel.getLeftDie().getDescription());
System.out.println("Right Die: " + foundModel.getRightDie().getDescription());
System.out.println("Active: " + foundModel.getActive());

// Find all active models
List<Model> activeModels = Model.findActiveModels();
System.out.println("Found " + activeModels.size() + " active models");
for (Model activeModel : activeModels) {
    System.out.println("Active model: " + activeModel.getName());
}

// Update a model
Model modelToUpdate = Model.findModel(1L);
modelToUpdate.setDescription("Updated description for Accord Front Door Panel Set");
modelToUpdate.merge();

// Delete a model
Model modelToDelete = Model.findModel(2L);
modelToDelete.remove();

// Find all models
List<Model> allModels = Model.findAllModels();
System.out.println("Total models: " + allModels.size());

// Find models with pagination
int firstResult = 0;
int maxResults = 10;
List<Model> pagedModels = Model.findModelEntries(firstResult, maxResults);
System.out.println("Paged models: " + pagedModels.size());
```

## Debugging and Production Support

### Common Issues
1. Models with inactive dies being used in production orders
2. Missing or null die references in models
3. Duplicate model names causing confusion
4. Models not being properly persisted to the database
5. Inconsistencies between model data and actual production requirements
6. Performance issues when retrieving models with complex die relationships
7. Models becoming inactive unexpectedly due to die status changes

### Debugging Steps
1. Verify model data integrity:
   ```java
   // Check for models with null or inactive dies
   List<Model> allModels = Model.findAllModels();
   System.out.println("Checking model data integrity:");
   
   for (Model model : allModels) {
       System.out.println("Model: " + model.getName() + " (ID: " + model.getId() + ")");
       
       // Check left die
       if (model.getLeftDie() == null) {
           System.out.println("  WARNING: Left die is null");
       } else {
           System.out.println("  Left Die: " + model.getLeftDie().getDescription() + 
                             " (ID: " + model.getLeftDie().getId() + ")");
           System.out.println("  Left Die Active: " + model.getLeftDie().getActive());
       }
       
       // Check right die
       if (model.getRightDie() == null) {
           System.out.println("  WARNING: Right die is null");
       } else {
           System.out.println("  Right Die: " + model.getRightDie().getDescription() + 
                             " (ID: " + model.getRightDie().getId() + ")");
           System.out.println("  Right Die Active: " + model.getRightDie().getActive());
       }
       
       // Check model active status
       System.out.println("  Model Active: " + model.getActive());
       
       // Verify consistency
       boolean expectedActive = (model.getLeftDie() != null && model.getLeftDie().getActive() && 
                               model.getRightDie() != null && model.getRightDie().getActive());
       if (model.getActive() != expectedActive) {
           System.out.println("  WARNING: Model active status inconsistent with die status");
       }
       
       System.out.println();
   }
   ```

2. Check for duplicate model names:
   ```java
   // Check for duplicate model names
   List<Model> allModels = Model.findAllModels();
   Map<String, List<Model>> modelsByName = new HashMap<>();
   
   for (Model model : allModels) {
       String name = model.getName();
       if (!modelsByName.containsKey(name)) {
           modelsByName.put(name, new ArrayList<>());
       }
       modelsByName.get(name).add(model);
   }
   
   System.out.println("Checking for duplicate model names:");
   for (Map.Entry<String, List<Model>> entry : modelsByName.entrySet()) {
       if (entry.getValue().size() > 1) {
           System.out.println("  Duplicate name found: " + entry.getKey());
           System.out.println("  Number of duplicates: " + entry.getValue().size());
           
           for (Model model : entry.getValue()) {
               System.out.println("    Model ID: " + model.getId());
               System.out.println("    Description: " + model.getDescription());
               System.out.println("    Left Die: " + 
                                 (model.getLeftDie() != null ? model.getLeftDie().getId() : "null"));
               System.out.println("    Right Die: " + 
                                 (model.getRightDie() != null ? model.getRightDie().getId() : "null"));
               System.out.println();
           }
       }
   }
   ```

3. Verify model persistence:
   ```java
   // Test model persistence
   try {
       // Create a test model
       Model testModel = new Model();
       testModel.setName("Test Model " + System.currentTimeMillis());
       testModel.setDescription("Test model for persistence verification");
       
       // Set dies
       Die leftDie = Die.findDie(101L);
       Die rightDie = Die.findDie(102L);
       testModel.setLeftDie(leftDie);
       testModel.setRightDie(rightDie);
       
       // Persist
       System.out.println("Persisting test model: " + testModel.getName());
       testModel.persist();
       
       // Verify persistence
       Long testModelId = testModel.getId();
       System.out.println("Test model persisted with ID: " + testModelId);
       
       // Clear entity manager
       testModel.clear();
       
       // Find the model again
       Model foundModel = Model.findModel(testModelId);
       if (foundModel != null) {
           System.out.println("Successfully found persisted model: " + foundModel.getName());
           System.out.println("Description: " + foundModel.getDescription());
           
           // Clean up - remove the test model
           foundModel.remove();
           System.out.println("Test model removed");
       } else {
           System.out.println("ERROR: Failed to find persisted model with ID: " + testModelId);
       }
   } catch (Exception e) {
       System.out.println("ERROR during model persistence test: " + e.getMessage());
       e.printStackTrace();
   }
   ```

4. Check model usage in orders:
   ```java
   // Check model usage in orders
   List<Model> allModels = Model.findAllModels();
   System.out.println("Checking model usage in orders:");
   
   for (Model model : allModels) {
       // Find orders using this model
       String sql = "SELECT COUNT(o) FROM WeldOrder o WHERE o.model.id = :modelId";
       Query q = entityManager().createQuery(sql, Long.class);
       q.setParameter("modelId", model.getId());
       Long orderCount = (Long) q.getSingleResult();
       
       System.out.println("Model: " + model.getName() + " (ID: " + model.getId() + ")");
       System.out.println("  Used in " + orderCount + " orders");
       
       // Check if inactive model is used in active orders
       if (!model.getActive() && orderCount > 0) {
           String activeSql = "SELECT COUNT(o) FROM WeldOrder o WHERE o.model.id = :modelId AND " +
                             "(o.orderStatus = 'Queued' OR o.orderStatus = 'InProcess' OR " +
                             "o.orderStatus = 'RetrievingCarriers' OR o.orderStatus = 'DeliveringCarriers')";
           Query activeQ = entityManager().createQuery(activeSql, Long.class);
           activeQ.setParameter("modelId", model.getId());
           Long activeOrderCount = (Long) activeQ.getSingleResult();
           
           if (activeOrderCount > 0) {
               System.out.println("  WARNING: Inactive model used in " + activeOrderCount + 
                                 " active orders");
           }
       }
   }
   ```

5. Test model performance:
   ```java
   // Test model retrieval performance
   System.out.println("Testing model retrieval performance:");
   
   // Test findAllModels
   long startTime = System.currentTimeMillis();
   List<Model> allModels = Model.findAllModels();
   long endTime = System.currentTimeMillis();
   
   System.out.println("  findAllModels: " + (endTime - startTime) + "ms for " + 
                     allModels.size() + " models");
   
   // Test findActiveModels
   startTime = System.currentTimeMillis();
   List<Model> activeModels = Model.findActiveModels();
   endTime = System.currentTimeMillis();
   
   System.out.println("  findActiveModels: " + (endTime - startTime) + "ms for " + 
                     activeModels.size() + " models");
   
   // Test individual model retrieval
   if (!allModels.isEmpty()) {
       Long modelId = allModels.get(0).getId();
       
       startTime = System.currentTimeMillis();
       Model model = Model.findModel(modelId);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findModel: " + (endTime - startTime) + "ms for model ID " + modelId);
   }
   ```

### Resolution
- For models with inactive dies: Update the model to use active dies or mark the model as inactive
- For missing die references: Ensure all models have valid left and right die references
- For duplicate model names: Rename models to ensure unique names or add additional identifiers
- For persistence issues: Verify entity manager configuration and transaction management
- For data inconsistencies: Implement data validation and reconciliation processes
- For performance issues: Optimize queries and consider caching frequently used models
- For unexpected inactive models: Implement notifications when die status changes affect model status

### Monitoring
- Track the number of active vs. inactive models
- Monitor for changes in model status due to die status changes
- Track model usage in orders to identify critical models
- Monitor for models with missing or invalid die references
- Track performance metrics for model-related operations
- Set up alerts for critical model status changes
- Monitor for unexpected changes in model data