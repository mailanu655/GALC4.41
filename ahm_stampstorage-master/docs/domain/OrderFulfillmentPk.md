# OrderFulfillmentPk Technical Documentation

## Purpose
OrderFulfillmentPk.java defines a composite primary key class for the OrderFulfillment entity in the stamping system. This class uniquely identifies an order fulfillment record by combining a weld order reference, a carrier number, and a release cycle. As an embeddable class, it enables complex relationships between orders and carriers while maintaining data integrity through a well-defined composite key structure.

## Logic/Functionality
- Implements a composite primary key for OrderFulfillment entities
- Combines three fields (weld order, carrier number, and release cycle) to create a unique identifier
- Provides proper equals() and hashCode() methods for composite key comparison
- Implements Serializable to support JPA persistence
- Includes JSON serialization/deserialization support
- Enforces immutability through final fields and constructor-only initialization

## Flow
1. When a new OrderFulfillment record is created, an OrderFulfillmentPk is constructed with the required fields
2. The composite key uniquely identifies the fulfillment record in the database
3. The system uses the composite key to retrieve specific fulfillment records
4. The equals() and hashCode() methods ensure proper comparison and collection behavior
5. The toString() method provides a human-readable representation of the key

## Key Elements
- `weldOrder`: Reference to the WeldOrder entity this fulfillment is associated with
- `carrierNumber`: The carrier number assigned to this fulfillment
- `releaseCycle`: The release cycle number for this fulfillment
- Constructor that initializes all fields
- equals() and hashCode() methods for proper object comparison
- JSON serialization/deserialization methods for web service support
- toString() method for debugging and logging

## Usage
```java
// Create a new OrderFulfillmentPk
WeldOrder order = WeldOrder.findWeldOrder(1L);
Integer carrierNumber = 123;
Integer releaseCycle = 1;
OrderFulfillmentPk pk = new OrderFulfillmentPk(order, carrierNumber, releaseCycle);

// Use the primary key to find an OrderFulfillment
OrderFulfillment fulfillment = OrderFulfillment.findOrderFulfillment(pk);

// Access the components of the primary key
WeldOrder retrievedOrder = pk.getWeldOrder();
Integer retrievedCarrierNumber = pk.getCarrierNumber();
Integer retrievedReleaseCycle = pk.getReleaseCycle();

System.out.println("Order ID: " + retrievedOrder.getId());
System.out.println("Carrier Number: " + retrievedCarrierNumber);
System.out.println("Release Cycle: " + retrievedReleaseCycle);

// Compare two primary keys
WeldOrder anotherOrder = WeldOrder.findWeldOrder(2L);
OrderFulfillmentPk anotherPk = new OrderFulfillmentPk(anotherOrder, 456, 2);

boolean areEqual = pk.equals(anotherPk); // Should be false
System.out.println("Primary keys are equal: " + areEqual);

// Create a collection of primary keys
Set<OrderFulfillmentPk> pkSet = new HashSet<>();
pkSet.add(pk);
pkSet.add(anotherPk);
System.out.println("Set size: " + pkSet.size()); // Should be 2

// Convert to JSON
String json = pk.toJson();
System.out.println("JSON representation: " + json);

// Parse from JSON
OrderFulfillmentPk parsedPk = OrderFulfillmentPk.fromJsonToOrderFulfillmentPk(json);
System.out.println("Parsed primary key: " + parsedPk);

// Convert a collection to JSON
String collectionJson = OrderFulfillmentPk.toJsonArray(pkSet);
System.out.println("Collection JSON: " + collectionJson);

// Parse a collection from JSON
Collection<OrderFulfillmentPk> parsedCollection = 
    OrderFulfillmentPk.fromJsonArrayToOrderFulfillmentPks(collectionJson);
System.out.println("Parsed collection size: " + parsedCollection.size());
```

## Debugging and Production Support

### Common Issues
1. Null reference to WeldOrder in the composite key
2. Duplicate composite keys causing unique constraint violations
3. Serialization/deserialization issues with JSON
4. Inconsistent equals() and hashCode() implementations
5. Missing or incorrect release cycle values
6. Performance issues with complex composite key comparisons
7. Referential integrity problems when WeldOrder is deleted

### Debugging Steps
1. Verify primary key integrity:
   ```java
   // Check for null or invalid components in primary keys
   List<OrderFulfillment> allFulfillments = OrderFulfillment.findAllOrderFulfillments();
   System.out.println("Checking primary key integrity:");
   
   for (OrderFulfillment fulfillment : allFulfillments) {
       OrderFulfillmentPk pk = fulfillment.getId();
       System.out.println("Fulfillment ID: " + pk);
       
       // Check for null components
       if (pk.getWeldOrder() == null) {
           System.out.println("  WARNING: Null WeldOrder reference in primary key");
       }
       
       if (pk.getCarrierNumber() == null) {
           System.out.println("  WARNING: Null carrier number in primary key");
       }
       
       if (pk.getReleaseCycle() == null) {
           System.out.println("  WARNING: Null release cycle in primary key");
       }
       
       // Verify WeldOrder exists
       if (pk.getWeldOrder() != null) {
           WeldOrder order = WeldOrder.findWeldOrder(pk.getWeldOrder().getId());
           if (order == null) {
               System.out.println("  WARNING: Referenced WeldOrder does not exist: " + 
                                 pk.getWeldOrder().getId());
           }
       }
   }
   ```

2. Check for duplicate primary keys:
   ```java
   // Check for potential duplicate primary keys
   Map<String, List<OrderFulfillment>> fulfillmentsByKey = new HashMap<>();
   
   for (OrderFulfillment fulfillment : OrderFulfillment.findAllOrderFulfillments()) {
       OrderFulfillmentPk pk = fulfillment.getId();
       
       // Create a string representation of the key components
       String keyString = "";
       if (pk.getWeldOrder() != null) {
           keyString += pk.getWeldOrder().getId();
       } else {
           keyString += "null";
       }
       keyString += "|" + pk.getCarrierNumber() + "|" + pk.getReleaseCycle();
       
       if (!fulfillmentsByKey.containsKey(keyString)) {
           fulfillmentsByKey.put(keyString, new ArrayList<>());
       }
       fulfillmentsByKey.get(keyString).add(fulfillment);
   }
   
   System.out.println("Checking for duplicate primary keys:");
   for (Map.Entry<String, List<OrderFulfillment>> entry : fulfillmentsByKey.entrySet()) {
       if (entry.getValue().size() > 1) {
           System.out.println("  WARNING: Potential duplicate key found: " + entry.getKey());
           System.out.println("  Number of duplicates: " + entry.getValue().size());
           
           for (OrderFulfillment fulfillment : entry.getValue()) {
               System.out.println("    Fulfillment entity ID: " + System.identityHashCode(fulfillment));
               System.out.println("    Die: " + 
                                 (fulfillment.getDie() != null ? 
                                  fulfillment.getDie().getDescription() : "null"));
               System.out.println("    Status: " + fulfillment.getCarrierFulfillmentStatus());
               System.out.println();
           }
       }
   }
   ```

3. Test JSON serialization/deserialization:
   ```java
   // Test JSON serialization/deserialization
   System.out.println("Testing JSON serialization/deserialization:");
   
   List<OrderFulfillment> sampleFulfillments = 
       OrderFulfillment.findOrderFulfillmentEntries(0, 5);
   
   if (!sampleFulfillments.isEmpty()) {
       for (OrderFulfillment fulfillment : sampleFulfillments) {
           OrderFulfillmentPk pk = fulfillment.getId();
           
           try {
               // Serialize to JSON
               String json = pk.toJson();
               System.out.println("  Original key: " + pk);
               System.out.println("  JSON: " + json);
               
               // Deserialize from JSON
               OrderFulfillmentPk parsedPk = OrderFulfillmentPk.fromJsonToOrderFulfillmentPk(json);
               System.out.println("  Parsed key: " + parsedPk);
               
               // Verify equality
               boolean equal = pk.equals(parsedPk);
               System.out.println("  Keys equal after serialization/deserialization: " + equal);
               
               if (!equal) {
                   System.out.println("  WARNING: Keys not equal after serialization/deserialization");
                   System.out.println("    Original: WeldOrder=" + pk.getWeldOrder().getId() + 
                                     ", Carrier=" + pk.getCarrierNumber() + 
                                     ", Cycle=" + pk.getReleaseCycle());
                   System.out.println("    Parsed: WeldOrder=" + 
                                     (parsedPk.getWeldOrder() != null ? 
                                      parsedPk.getWeldOrder().getId() : "null") + 
                                     ", Carrier=" + parsedPk.getCarrierNumber() + 
                                     ", Cycle=" + parsedPk.getReleaseCycle());
               }
               
               System.out.println();
           } catch (Exception e) {
               System.out.println("  ERROR during JSON serialization/deserialization: " + 
                                 e.getMessage());
               e.printStackTrace();
           }
       }
   } else {
       System.out.println("  No fulfillment records found for testing");
   }
   ```

4. Verify equals() and hashCode() implementation:
   ```java
   // Test equals() and hashCode() implementation
   System.out.println("Testing equals() and hashCode() implementation:");
   
   List<OrderFulfillment> testFulfillments = 
       OrderFulfillment.findOrderFulfillmentEntries(0, 10);
   
   if (testFulfillments.size() >= 2) {
       // Get two different primary keys
       OrderFulfillmentPk pk1 = testFulfillments.get(0).getId();
       OrderFulfillmentPk pk2 = testFulfillments.get(1).getId();
       
       // Test reflexivity
       boolean reflexive = pk1.equals(pk1);
       System.out.println("  Reflexive (pk1.equals(pk1)): " + reflexive);
       
       // Test symmetry
       boolean pk1EqualsPk2 = pk1.equals(pk2);
       boolean pk2EqualsPk1 = pk2.equals(pk1);
       System.out.println("  Symmetric: pk1.equals(pk2) = " + pk1EqualsPk2 + 
                         ", pk2.equals(pk1) = " + pk2EqualsPk1);
       
       // Test with null
       boolean pk1EqualsNull = pk1.equals(null);
       System.out.println("  Null comparison (pk1.equals(null)): " + pk1EqualsNull);
       
       // Test with different type
       boolean pk1EqualsString = pk1.equals("not a primary key");
       System.out.println("  Different type comparison (pk1.equals(\"not a primary key\")): " + 
                         pk1EqualsString);
       
       // Test hashCode consistency with equals
       boolean hashCodeConsistent = (pk1.equals(pk2)) == (pk1.hashCode() == pk2.hashCode());
       System.out.println("  HashCode consistent with equals: " + hashCodeConsistent);
       
       // Create a copy of pk1 with same values
       OrderFulfillmentPk pk1Copy = new OrderFulfillmentPk(
           pk1.getWeldOrder(), pk1.getCarrierNumber(), pk1.getReleaseCycle());
       
       boolean pk1EqualsPk1Copy = pk1.equals(pk1Copy);
       boolean hashCodesEqual = pk1.hashCode() == pk1Copy.hashCode();
       
       System.out.println("  pk1.equals(pk1Copy): " + pk1EqualsPk1Copy);
       System.out.println("  pk1.hashCode() == pk1Copy.hashCode(): " + hashCodesEqual);
   } else {
       System.out.println("  Not enough fulfillment records for testing");
   }
   ```

5. Check release cycle values:
   ```java
   // Check release cycle values
   System.out.println("Checking release cycle values:");
   
   Map<Long, Set<Integer>> orderCycles = new HashMap<>();
   
   for (OrderFulfillment fulfillment : OrderFulfillment.findAllOrderFulfillments()) {
       OrderFulfillmentPk pk = fulfillment.getId();
       Long orderId = pk.getWeldOrder().getId();
       Integer cycle = pk.getReleaseCycle();
       
       if (!orderCycles.containsKey(orderId)) {
           orderCycles.put(orderId, new HashSet<>());
       }
       orderCycles.get(orderId).add(cycle);
   }
   
   for (Map.Entry<Long, Set<Integer>> entry : orderCycles.entrySet()) {
       Long orderId = entry.getKey();
       Set<Integer> cycles = entry.getValue();
       
       System.out.println("  Order " + orderId + " has " + cycles.size() + " release cycles");
       
       // Check for gaps in cycle sequence
       List<Integer> sortedCycles = new ArrayList<>(cycles);
       Collections.sort(sortedCycles);
       
       System.out.println("    Cycles: " + sortedCycles);
       
       boolean hasGaps = false;
       for (int i = 0; i < sortedCycles.size() - 1; i++) {
           if (sortedCycles.get(i + 1) - sortedCycles.get(i) > 1) {
               hasGaps = true;
               System.out.println("    WARNING: Gap detected between cycles " + 
                                 sortedCycles.get(i) + " and " + sortedCycles.get(i + 1));
           }
       }
       
       if (!hasGaps) {
           System.out.println("    No gaps detected in cycle sequence");
       }
       
       // Check for negative or zero cycles
       boolean hasInvalidCycles = false;
       for (Integer cycle : sortedCycles) {
           if (cycle <= 0) {
               hasInvalidCycles = true;
               System.out.println("    WARNING: Invalid cycle value detected: " + cycle);
           }
       }
       
       if (!hasInvalidCycles) {
           System.out.println("    No invalid cycle values detected");
       }
   }
   ```

6. Test performance:
   ```java
   // Test performance of primary key operations
   System.out.println("Testing performance of primary key operations:");
   
   List<OrderFulfillment> perfTestFulfillments = 
       OrderFulfillment.findOrderFulfillmentEntries(0, 100);
   
   if (!perfTestFulfillments.isEmpty()) {
       List<OrderFulfillmentPk> keys = new ArrayList<>();
       for (OrderFulfillment fulfillment : perfTestFulfillments) {
           keys.add(fulfillment.getId());
       }
       
       // Test equals() performance
       OrderFulfillmentPk testKey = keys.get(0);
       long startTime = System.currentTimeMillis();
       
       for (OrderFulfillmentPk key : keys) {
           testKey.equals(key);
       }
       
       long endTime = System.currentTimeMillis();
       System.out.println("  equals() performance: " + (endTime - startTime) + 
                         "ms for " + keys.size() + " comparisons");
       
       // Test hashCode() performance
       startTime = System.currentTimeMillis();
       
       for (OrderFulfillmentPk key : keys) {
           key.hashCode();
       }
       
       endTime = System.currentTimeMillis();
       System.out.println("  hashCode() performance: " + (endTime - startTime) + 
                         "ms for " + keys.size() + " calculations");
       
       // Test JSON serialization performance
       startTime = System.currentTimeMillis();
       
       for (OrderFulfillmentPk key : keys) {
           key.toJson();
       }
       
       endTime = System.currentTimeMillis();
       System.out.println("  toJson() performance: " + (endTime - startTime) + 
                         "ms for " + keys.size() + " serializations");
   } else {
       System.out.println("  No fulfillment records found for performance testing");
   }
   ```

7. Check referential integrity:
   ```java
   // Check referential integrity with WeldOrder
   System.out.println("Checking referential integrity with WeldOrder:");
   
   // Get all order IDs referenced in fulfillment primary keys
   Set<Long> referencedOrderIds = new HashSet<>();
   for (OrderFulfillment fulfillment : OrderFulfillment.findAllOrderFulfillments()) {
       OrderFulfillmentPk pk = fulfillment.getId();
       if (pk.getWeldOrder() != null) {
           referencedOrderIds.add(pk.getWeldOrder().getId());
       }
   }
   
   System.out.println("  Number of distinct orders referenced: " + referencedOrderIds.size());
   
   // Verify each referenced order exists
   int missingOrders = 0;
   for (Long orderId : referencedOrderIds) {
       WeldOrder order = WeldOrder.findWeldOrder(orderId);
       if (order == null) {
           System.out.println("  WARNING: Referenced order does not exist: " + orderId);
           missingOrders++;
       }
   }
   
   if (missingOrders == 0) {
       System.out.println("  All referenced orders exist");
   } else {
       System.out.println("  Found " + missingOrders + " missing referenced orders");
   }
   ```

### Resolution
- For null references: Implement validation to prevent null components in primary keys
- For duplicate keys: Enforce uniqueness constraints and validate before persistence
- For serialization issues: Update JSON serialization/deserialization logic
- For inconsistent equals/hashCode: Fix implementation to follow proper contract
- For incorrect release cycles: Implement validation for cycle values
- For performance issues: Optimize equals() and hashCode() implementations
- For referential integrity: Implement proper cascade operations or foreign key constraints

### Monitoring
- Track the number of fulfillment records by order
- Monitor for duplicate primary key attempts
- Track serialization/deserialization errors
- Monitor for missing or invalid cycle values
- Track performance of primary key operations
- Monitor for referential integrity violations
- Set up alerts for potential data integrity issues