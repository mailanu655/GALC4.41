# OrderFulfillment Technical Documentation

## Purpose
OrderFulfillment.java defines an entity that represents the fulfillment of a weld order in the stamping system. This class tracks the carriers assigned to fulfill specific orders, managing the entire lifecycle from carrier selection to delivery and consumption. It serves as a critical link between the order management system and the physical carriers containing stamped parts.

## Logic/Functionality
- Represents the assignment of carriers to weld orders
- Tracks the status of carriers throughout the fulfillment process
- Provides methods to find carriers based on various criteria
- Manages the relationship between carriers, dies, and orders
- Supports the tracking of carriers through different fulfillment stages
- Provides functionality to count carriers in different states
- Implements JPA entity functionality for database persistence

## Flow
1. When a weld order is created, OrderFulfillment records are created to track the carriers needed
2. Carriers are selected and assigned to fulfill the order
3. The system tracks the carriers through various states (selected, retrieved, queued, delivered, consumed)
4. As carriers move through the system, their fulfillment status is updated
5. The system uses OrderFulfillment records to determine which carriers to move and when
6. When carriers are consumed, the OrderFulfillment records are updated to reflect completion
7. The system uses OrderFulfillment data to track order progress and completion

## Key Elements
- `id`: Composite primary key (OrderFulfillmentPk) containing weld order, carrier number, and release cycle
- `quantity`: The quantity of parts in the carrier
- `die`: The die used to create the parts
- `currentLocation`: The current location of the carrier
- `destination`: The destination location for the carrier
- `productionRunNo`: The production run number associated with the carrier
- `carrierFulfillmentStatus`: The current status of the carrier in the fulfillment process
- `updateDate`: The timestamp of the last update to the fulfillment record
- Static finder methods for retrieving fulfillment records based on various criteria
- Methods for counting carriers in different fulfillment states

## Usage
```java
// Find an order fulfillment record
WeldOrder order = WeldOrder.findWeldOrder(1L);
Integer carrierNumber = 123;
Integer releaseCycle = 1;
OrderFulfillmentPk pk = new OrderFulfillmentPk(order, carrierNumber, releaseCycle);
OrderFulfillment fulfillment = OrderFulfillment.findOrderFulfillment(pk);

// Display fulfillment details
System.out.println("Order: " + fulfillment.getId().getWeldOrder().getId());
System.out.println("Carrier: " + fulfillment.getId().getCarrierNumber());
System.out.println("Cycle: " + fulfillment.getId().getReleaseCycle());
System.out.println("Die: " + fulfillment.getDie().getDescription());
System.out.println("Quantity: " + fulfillment.getQuantity());
System.out.println("Current Location: " + fulfillment.getCurrentLocation().getName());
System.out.println("Destination: " + fulfillment.getDestination().getName());
System.out.println("Status: " + fulfillment.getCarrierFulfillmentStatus());

// Create a new fulfillment record
WeldOrder newOrder = WeldOrder.findWeldOrder(2L);
Integer newCarrierNumber = 456;
Integer newReleaseCycle = 1;
OrderFulfillmentPk newPk = new OrderFulfillmentPk(newOrder, newCarrierNumber, newReleaseCycle);

OrderFulfillment newFulfillment = new OrderFulfillment();
newFulfillment.setId(newPk);
newFulfillment.setDie(Die.findDie(101L));
newFulfillment.setQuantity(10);
newFulfillment.setCurrentLocation(Stop.findStop(201L));
newFulfillment.setDestination(Stop.findStop(301L));
newFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.SELECTED);
newFulfillment.setUpdateDate(new Timestamp(System.currentTimeMillis()));
newFulfillment.persist();

// Update fulfillment status
fulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RETRIEVED);
fulfillment.setUpdateDate(new Timestamp(System.currentTimeMillis()));
fulfillment.merge();

// Find all fulfillments for an order
List<OrderFulfillment> orderFulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);
System.out.println("Order has " + orderFulfillments.size() + " fulfillments");

// Find fulfillments by carrier and status
Integer carrierToFind = 789;
List<OrderFulfillment> carrierFulfillments = 
    OrderFulfillment.findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(
        carrierToFind, CarrierFulfillmentStatus.DELIVERED);
System.out.println("Carrier " + carrierToFind + " has " + 
                  carrierFulfillments.size() + " delivered fulfillments");

// Find the maximum cycle count for an order
Integer maxCycle = OrderFulfillment.getMaxCycleCountForOrder(order);
System.out.println("Maximum cycle for order: " + maxCycle);

// Count fulfillments for a cycle
Integer cycleToCount = 2;
Integer fulfillmentCount = OrderFulfillment.countFulfillmentsForCycle(order, cycleToCount);
System.out.println("Cycle " + cycleToCount + " has " + fulfillmentCount + " fulfillments");

// Find order by carrier
Carrier carrier = Carrier.findCarrier(123L);
WeldOrder foundOrder = OrderFulfillment.findOrderByCarrier(carrier);
if (foundOrder != null) {
    System.out.println("Found order: " + foundOrder.getId() + " for carrier " + carrier.getCarrierNumber());
}

// Delete a fulfillment record
OrderFulfillment fulfillmentToDelete = OrderFulfillment.findOrderFulfillment(pk);
fulfillmentToDelete.remove();
```

## Debugging and Production Support

### Common Issues
1. Carriers assigned to multiple active orders simultaneously
2. Inconsistent fulfillment status transitions
3. Missing or incorrect location information
4. Orphaned fulfillment records after order cancellation
5. Incorrect cycle counting leading to duplicate assignments
6. Performance issues with large numbers of fulfillment records
7. Timing issues with status updates during carrier movement

### Debugging Steps
1. Verify fulfillment record integrity:
   ```java
   // Check for fulfillment records with missing or invalid references
   List<OrderFulfillment> allFulfillments = OrderFulfillment.findAllOrderFulfillments();
   System.out.println("Checking fulfillment record integrity:");
   
   for (OrderFulfillment fulfillment : allFulfillments) {
       System.out.println("Fulfillment: Order=" + fulfillment.getId().getWeldOrder().getId() + 
                         ", Carrier=" + fulfillment.getId().getCarrierNumber() + 
                         ", Cycle=" + fulfillment.getId().getReleaseCycle());
       
       // Check die reference
       if (fulfillment.getDie() == null) {
           System.out.println("  WARNING: Missing die reference");
       }
       
       // Check location references
       if (fulfillment.getCurrentLocation() == null) {
           System.out.println("  WARNING: Missing current location reference");
       }
       
       if (fulfillment.getDestination() == null) {
           System.out.println("  WARNING: Missing destination reference");
       }
       
       // Check status
       if (fulfillment.getCarrierFulfillmentStatus() == null) {
           System.out.println("  WARNING: Missing fulfillment status");
       }
       
       // Check for inconsistent status and location
       if (fulfillment.getCarrierFulfillmentStatus() == CarrierFulfillmentStatus.DELIVERED &&
           !fulfillment.getDestination().equals(fulfillment.getCurrentLocation())) {
           System.out.println("  WARNING: Delivered status but current location doesn't match destination");
           System.out.println("    Current: " + fulfillment.getCurrentLocation().getName());
           System.out.println("    Destination: " + fulfillment.getDestination().getName());
       }
   }
   ```

2. Check for carriers assigned to multiple active orders:
   ```java
   // Check for carriers assigned to multiple active orders
   Map<Integer, List<OrderFulfillment>> activeAssignments = new HashMap<>();
   
   // Get all active fulfillments
   List<OrderFulfillment> activeFulfillments = new ArrayList<>();
   for (OrderFulfillment fulfillment : OrderFulfillment.findAllOrderFulfillments()) {
       if (fulfillment.getCarrierFulfillmentStatus() != CarrierFulfillmentStatus.CONSUMED) {
           activeFulfillments.add(fulfillment);
       }
   }
   
   // Group by carrier number
   for (OrderFulfillment fulfillment : activeFulfillments) {
       Integer carrierNumber = fulfillment.getId().getCarrierNumber();
       if (!activeAssignments.containsKey(carrierNumber)) {
           activeAssignments.put(carrierNumber, new ArrayList<>());
       }
       activeAssignments.get(carrierNumber).add(fulfillment);
   }
   
   // Check for carriers with multiple assignments
   System.out.println("Checking for carriers with multiple active assignments:");
   for (Map.Entry<Integer, List<OrderFulfillment>> entry : activeAssignments.entrySet()) {
       if (entry.getValue().size() > 1) {
           System.out.println("  Carrier " + entry.getKey() + " has " + 
                             entry.getValue().size() + " active assignments:");
           
           for (OrderFulfillment fulfillment : entry.getValue()) {
               System.out.println("    Order: " + fulfillment.getId().getWeldOrder().getId());
               System.out.println("    Cycle: " + fulfillment.getId().getReleaseCycle());
               System.out.println("    Status: " + fulfillment.getCarrierFulfillmentStatus());
               System.out.println("    Die: " + 
                                 (fulfillment.getDie() != null ? 
                                  fulfillment.getDie().getDescription() : "null"));
               System.out.println();
           }
       }
   }
   ```

3. Verify fulfillment status transitions:
   ```java
   // Check for invalid status transitions
   System.out.println("Checking for invalid status transitions:");
   
   // Define valid transitions
   Map<CarrierFulfillmentStatus, Set<CarrierFulfillmentStatus>> validTransitions = new HashMap<>();
   validTransitions.put(CarrierFulfillmentStatus.SELECTED, 
                       new HashSet<>(Arrays.asList(CarrierFulfillmentStatus.RETRIEVED)));
   validTransitions.put(CarrierFulfillmentStatus.RETRIEVED, 
                       new HashSet<>(Arrays.asList(CarrierFulfillmentStatus.QUEUED, 
                                                  CarrierFulfillmentStatus.READY_TO_DELIVER)));
   validTransitions.put(CarrierFulfillmentStatus.QUEUED, 
                       new HashSet<>(Arrays.asList(CarrierFulfillmentStatus.READY_TO_DELIVER)));
   validTransitions.put(CarrierFulfillmentStatus.READY_TO_DELIVER, 
                       new HashSet<>(Arrays.asList(CarrierFulfillmentStatus.SELECTED_TO_DELIVER)));
   validTransitions.put(CarrierFulfillmentStatus.SELECTED_TO_DELIVER, 
                       new HashSet<>(Arrays.asList(CarrierFulfillmentStatus.DELIVERED)));
   validTransitions.put(CarrierFulfillmentStatus.DELIVERED, 
                       new HashSet<>(Arrays.asList(CarrierFulfillmentStatus.CONSUMED)));
   
   // Get recent fulfillment history
   // Note: This would require a history table or log to implement properly
   // This is a placeholder for the concept
   System.out.println("  Status transition validation would require history tracking");
   System.out.println("  Consider implementing a fulfillment history table to track transitions");
   ```

4. Check for orphaned fulfillment records:
   ```java
   // Check for orphaned fulfillment records
   System.out.println("Checking for orphaned fulfillment records:");
   
   for (OrderFulfillment fulfillment : OrderFulfillment.findAllOrderFulfillments()) {
       WeldOrder order = fulfillment.getId().getWeldOrder();
       
       // Check if order is cancelled or completed
       if (order.getOrderStatus() == OrderStatus.Cancelled || 
           order.getOrderStatus() == OrderStatus.ManuallyCompleted || 
           order.getOrderStatus() == OrderStatus.AutoCompleted) {
           
           // Check if fulfillment is not consumed
           if (fulfillment.getCarrierFulfillmentStatus() != CarrierFulfillmentStatus.CONSUMED) {
               System.out.println("  Orphaned fulfillment found:");
               System.out.println("    Order: " + order.getId() + " (Status: " + order.getOrderStatus() + ")");
               System.out.println("    Carrier: " + fulfillment.getId().getCarrierNumber());
               System.out.println("    Cycle: " + fulfillment.getId().getReleaseCycle());
               System.out.println("    Fulfillment Status: " + fulfillment.getCarrierFulfillmentStatus());
           }
       }
   }
   ```

5. Verify cycle counting:
   ```java
   // Check cycle counting for orders
   System.out.println("Checking cycle counting for orders:");
   
   List<WeldOrder> activeOrders = WeldOrder.findWeldOrdersInProcess();
   for (WeldOrder order : activeOrders) {
       System.out.println("Order: " + order.getId());
       
       // Get max cycle count
       Integer maxCycle = OrderFulfillment.getMaxCycleCountForOrder(order);
       System.out.println("  Max cycle count: " + maxCycle);
       
       // Check each cycle
       for (int cycle = 1; cycle <= maxCycle; cycle++) {
           List<OrderFulfillment> cycleFulfillments = 
               OrderFulfillment.findAllOrderFulfillmentsByOrderForCycle(order, cycle);
           System.out.println("  Cycle " + cycle + ": " + cycleFulfillments.size() + " fulfillments");
           
           // Check for duplicate carrier assignments within cycle
           Map<Integer, Integer> carrierCounts = new HashMap<>();
           for (OrderFulfillment fulfillment : cycleFulfillments) {
               Integer carrier = fulfillment.getId().getCarrierNumber();
               carrierCounts.put(carrier, carrierCounts.getOrDefault(carrier, 0) + 1);
           }
           
           for (Map.Entry<Integer, Integer> entry : carrierCounts.entrySet()) {
               if (entry.getValue() > 1) {
                   System.out.println("    WARNING: Carrier " + entry.getKey() + 
                                     " assigned " + entry.getValue() + " times in cycle " + cycle);
               }
           }
       }
   }
   ```

6. Test performance:
   ```java
   // Test performance of common operations
   System.out.println("Testing performance of common operations:");
   
   // Test findAllOrderFulfillments
   long startTime = System.currentTimeMillis();
   List<OrderFulfillment> allFulfillments = OrderFulfillment.findAllOrderFulfillments();
   long endTime = System.currentTimeMillis();
   System.out.println("  findAllOrderFulfillments: " + (endTime - startTime) + 
                     "ms for " + allFulfillments.size() + " records");
   
   // Test findAllOrderFulfillmentsByOrder for a specific order
   if (!allFulfillments.isEmpty()) {
       WeldOrder testOrder = allFulfillments.get(0).getId().getWeldOrder();
       
       startTime = System.currentTimeMillis();
       List<OrderFulfillment> orderFulfillments = 
           OrderFulfillment.findAllOrderFulfillmentsByOrder(testOrder);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findAllOrderFulfillmentsByOrder: " + (endTime - startTime) + 
                         "ms for order " + testOrder.getId() + " with " + 
                         orderFulfillments.size() + " fulfillments");
   }
   
   // Test findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus
   if (!allFulfillments.isEmpty()) {
       Integer testCarrier = allFulfillments.get(0).getId().getCarrierNumber();
       
       startTime = System.currentTimeMillis();
       List<OrderFulfillment> carrierFulfillments = 
           OrderFulfillment.findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(
               testCarrier, CarrierFulfillmentStatus.RETRIEVED);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus: " + 
                         (endTime - startTime) + "ms for carrier " + testCarrier + 
                         " with status RETRIEVED, found " + carrierFulfillments.size() + " records");
   }
   ```

### Resolution
- For carriers assigned to multiple orders: Identify and resolve conflicting assignments
- For inconsistent status transitions: Implement status validation and correction
- For missing location information: Update records with correct location data
- For orphaned fulfillment records: Clean up or mark as consumed
- For cycle counting issues: Implement validation for cycle assignments
- For performance issues: Optimize queries and consider indexing
- For timing issues: Implement transaction management and synchronization

### Monitoring
- Track the number of fulfillment records by status
- Monitor for carriers with multiple active assignments
- Track fulfillment status transition times
- Monitor for orphaned fulfillment records
- Track cycle counts for active orders
- Monitor performance of fulfillment-related queries
- Set up alerts for unusual status transitions or assignments