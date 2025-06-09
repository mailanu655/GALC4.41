# OrderMgr Technical Documentation

## Purpose
OrderMgr.java defines an entity that represents a weld line manager in the stamping system. This class manages the configuration and operational parameters for a specific weld line, including delivery and consumption stops, queue locations, and capacity settings. It serves as a central point of reference for order fulfillment operations, connecting the physical weld line with the order management system.

## Logic/Functionality
- Represents a weld line configuration in the system
- Defines delivery and consumption locations for parts
- Specifies queue locations for left and right parts
- Sets capacity limits for delivery operations
- Provides methods to find and manage weld line configurations
- Implements JPA entity functionality for database persistence

## Flow
1. OrderMgr entities are configured for each weld line in the system
2. WeldOrder entities reference an OrderMgr to determine delivery locations
3. The system uses OrderMgr information to route carriers to appropriate locations
4. OrderFulfillment processes use OrderMgr data to determine where to queue and deliver parts
5. The system uses OrderMgr capacity settings to manage delivery operations
6. Consumption processes use OrderMgr data to determine where parts are consumed

## Key Elements
- `lineName`: The name of the weld line
- `deliveryStop`: The stop where carriers are delivered to the weld line
- `leftConsumptionStop`: The stop where left-hand parts are consumed
- `rightConsumptionStop`: The stop where right-hand parts are consumed
- `leftConsumptionExit`: The exit location for left-hand carriers after consumption
- `rightConsumptionExit`: The exit location for right-hand carriers after consumption
- `maxDeliveryCapacity`: The maximum number of carriers that can be delivered at once
- `leftQueueStop`: The queue location for left-hand parts
- `rightQueueStop`: The queue location for right-hand parts
- Static finder methods for retrieving OrderMgr entities

## Usage
```java
// Find an OrderMgr by ID
OrderMgr orderMgr = OrderMgr.findOrderMgr(1L);

// Display OrderMgr details
System.out.println("Weld Line: " + orderMgr.getLineName());
System.out.println("Delivery Stop: " + orderMgr.getDeliveryStop().getName());
System.out.println("Left Consumption Stop: " + orderMgr.getLeftConsumptionStop().getName());
System.out.println("Right Consumption Stop: " + orderMgr.getRightConsumptionStop().getName());
System.out.println("Left Consumption Exit: " + orderMgr.getLeftConsumptionExit().getName());
System.out.println("Right Consumption Exit: " + orderMgr.getRightConsumptionExit().getName());
System.out.println("Max Delivery Capacity: " + orderMgr.getMaxDeliveryCapacity());
System.out.println("Left Queue Stop: " + orderMgr.getLeftQueueStop().getName());
System.out.println("Right Queue Stop: " + orderMgr.getRightQueueStop().getName());

// Find all OrderMgr entities
List<OrderMgr> allOrderMgrs = OrderMgr.findAllOrderMgrs();
System.out.println("Total weld lines: " + allOrderMgrs.size());

// Create a new OrderMgr
OrderMgr newOrderMgr = new OrderMgr();
newOrderMgr.setLineName("Weld Line 5");
newOrderMgr.setDeliveryStop(Stop.findStop(101L));
newOrderMgr.setLeftConsumptionStop(Stop.findStop(102L));
newOrderMgr.setRightConsumptionStop(Stop.findStop(103L));
newOrderMgr.setLeftConsumptionExit(Stop.findStop(104L));
newOrderMgr.setRightConsumptionExit(Stop.findStop(105L));
newOrderMgr.setMaxDeliveryCapacity(4);
newOrderMgr.setLeftQueueStop(Stop.findStop(106L));
newOrderMgr.setRightQueueStop(Stop.findStop(107L));
newOrderMgr.persist();

// Update an existing OrderMgr
orderMgr.setMaxDeliveryCapacity(6);
orderMgr.merge();

// Delete an OrderMgr
OrderMgr orderMgrToDelete = OrderMgr.findOrderMgr(2L);
orderMgrToDelete.remove();

// Find active orders for a weld line
WeldOrder activeOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
if (activeOrder != null) {
    System.out.println("Active order: " + activeOrder.getId());
    System.out.println("Model: " + activeOrder.getModel().getName());
    System.out.println("Left quantity: " + activeOrder.getLeftQuantity());
    System.out.println("Right quantity: " + activeOrder.getRightQuantity());
}

// Find active delivery order for a weld line
WeldOrder activeDeliveryOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
if (activeDeliveryOrder != null) {
    System.out.println("Active delivery order: " + activeDeliveryOrder.getId());
    System.out.println("Delivery status: " + activeDeliveryOrder.getDeliveryStatus());
}

// Find all orders for a weld line
List<WeldOrder> weldLineOrders = WeldOrder.findAllWeldOrdersByOrderMgr(orderMgr);
System.out.println("Total orders for weld line: " + weldLineOrders.size());
```

## Debugging and Production Support

### Common Issues
1. Missing or incorrect stop references
2. Inconsistent delivery and consumption stop configurations
3. Invalid maximum delivery capacity settings
4. Multiple active orders for the same weld line
5. Orphaned orders after weld line configuration changes
6. Inconsistent queue stop configurations
7. Performance issues with weld line order queries

### Debugging Steps
1. Verify OrderMgr integrity:
   ```java
   // Check for missing or invalid stop references
   List<OrderMgr> allOrderMgrs = OrderMgr.findAllOrderMgrs();
   System.out.println("Checking OrderMgr integrity:");
   
   for (OrderMgr orderMgr : allOrderMgrs) {
       System.out.println("OrderMgr: " + orderMgr.getId() + " - " + orderMgr.getLineName());
       
       // Check delivery stop
       if (orderMgr.getDeliveryStop() == null) {
           System.out.println("  WARNING: Missing delivery stop");
       }
       
       // Check consumption stops
       if (orderMgr.getLeftConsumptionStop() == null) {
           System.out.println("  WARNING: Missing left consumption stop");
       }
       
       if (orderMgr.getRightConsumptionStop() == null) {
           System.out.println("  WARNING: Missing right consumption stop");
       }
       
       // Check consumption exits
       if (orderMgr.getLeftConsumptionExit() == null) {
           System.out.println("  WARNING: Missing left consumption exit");
       }
       
       if (orderMgr.getRightConsumptionExit() == null) {
           System.out.println("  WARNING: Missing right consumption exit");
       }
       
       // Check queue stops
       if (orderMgr.getLeftQueueStop() == null) {
           System.out.println("  WARNING: Missing left queue stop");
       }
       
       if (orderMgr.getRightQueueStop() == null) {
           System.out.println("  WARNING: Missing right queue stop");
       }
       
       // Check max delivery capacity
       if (orderMgr.getMaxDeliveryCapacity() == null) {
           System.out.println("  WARNING: Missing max delivery capacity");
       } else if (orderMgr.getMaxDeliveryCapacity() <= 0) {
           System.out.println("  WARNING: Invalid max delivery capacity: " + 
                             orderMgr.getMaxDeliveryCapacity());
       }
   }
   ```

2. Verify stop references:
   ```java
   // Verify that all referenced stops exist
   System.out.println("Verifying stop references:");
   
   for (OrderMgr orderMgr : OrderMgr.findAllOrderMgrs()) {
       System.out.println("OrderMgr: " + orderMgr.getId() + " - " + orderMgr.getLineName());
       
       // Check delivery stop
       if (orderMgr.getDeliveryStop() != null) {
           Stop deliveryStop = Stop.findStop(orderMgr.getDeliveryStop().getId());
           if (deliveryStop == null) {
               System.out.println("  WARNING: Delivery stop not found: " + 
                                 orderMgr.getDeliveryStop().getId());
           }
       }
       
       // Check left consumption stop
       if (orderMgr.getLeftConsumptionStop() != null) {
           Stop leftConsumptionStop = Stop.findStop(orderMgr.getLeftConsumptionStop().getId());
           if (leftConsumptionStop == null) {
               System.out.println("  WARNING: Left consumption stop not found: " + 
                                 orderMgr.getLeftConsumptionStop().getId());
           }
       }
       
       // Check right consumption stop
       if (orderMgr.getRightConsumptionStop() != null) {
           Stop rightConsumptionStop = Stop.findStop(orderMgr.getRightConsumptionStop().getId());
           if (rightConsumptionStop == null) {
               System.out.println("  WARNING: Right consumption stop not found: " + 
                                 orderMgr.getRightConsumptionStop().getId());
           }
       }
       
       // Check left consumption exit
       if (orderMgr.getLeftConsumptionExit() != null) {
           Stop leftConsumptionExit = Stop.findStop(orderMgr.getLeftConsumptionExit().getId());
           if (leftConsumptionExit == null) {
               System.out.println("  WARNING: Left consumption exit not found: " + 
                                 orderMgr.getLeftConsumptionExit().getId());
           }
       }
       
       // Check right consumption exit
       if (orderMgr.getRightConsumptionExit() != null) {
           Stop rightConsumptionExit = Stop.findStop(orderMgr.getRightConsumptionExit().getId());
           if (rightConsumptionExit == null) {
               System.out.println("  WARNING: Right consumption exit not found: " + 
                                 orderMgr.getRightConsumptionExit().getId());
           }
       }
       
       // Check left queue stop
       if (orderMgr.getLeftQueueStop() != null) {
           Stop leftQueueStop = Stop.findStop(orderMgr.getLeftQueueStop().getId());
           if (leftQueueStop == null) {
               System.out.println("  WARNING: Left queue stop not found: " + 
                                 orderMgr.getLeftQueueStop().getId());
           }
       }
       
       // Check right queue stop
       if (orderMgr.getRightQueueStop() != null) {
           Stop rightQueueStop = Stop.findStop(orderMgr.getRightQueueStop().getId());
           if (rightQueueStop == null) {
               System.out.println("  WARNING: Right queue stop not found: " + 
                                 orderMgr.getRightQueueStop().getId());
           }
       }
   }
   ```

3. Check for multiple active orders:
   ```java
   // Check for multiple active orders per weld line
   System.out.println("Checking for multiple active orders per weld line:");
   
   for (OrderMgr orderMgr : OrderMgr.findAllOrderMgrs()) {
       System.out.println("OrderMgr: " + orderMgr.getId() + " - " + orderMgr.getLineName());
       
       // Check for active fulfillment orders
       List<WeldOrder> activeOrders = WeldOrder.findWeldOrdersInProcessByOrderMgr(orderMgr);
       if (activeOrders.size() > 1) {
           System.out.println("  WARNING: Multiple active fulfillment orders found: " + 
                             activeOrders.size());
           
           for (WeldOrder order : activeOrders) {
               System.out.println("    Order ID: " + order.getId());
               System.out.println("    Status: " + order.getOrderStatus());
               System.out.println("    Created: " + order.getCreatedDate());
               System.out.println();
           }
       } else if (activeOrders.size() == 1) {
           System.out.println("  One active fulfillment order found: " + activeOrders.get(0).getId());
       } else {
           System.out.println("  No active fulfillment orders found");
       }
       
       // Check for active delivery orders
       List<WeldOrder> activeDeliveryOrders = WeldOrder.findWeldOrdersDeliveringByOrderMgr(orderMgr);
       if (activeDeliveryOrders.size() > 1) {
           System.out.println("  WARNING: Multiple active delivery orders found: " + 
                             activeDeliveryOrders.size());
           
           for (WeldOrder order : activeDeliveryOrders) {
               System.out.println("    Order ID: " + order.getId());
               System.out.println("    Delivery Status: " + order.getDeliveryStatus());
               System.out.println("    Created: " + order.getCreatedDate());
               System.out.println();
           }
       } else if (activeDeliveryOrders.size() == 1) {
           System.out.println("  One active delivery order found: " + 
                             activeDeliveryOrders.get(0).getId());
       } else {
           System.out.println("  No active delivery orders found");
       }
   }
   ```

4. Check for orphaned orders:
   ```java
   // Check for orphaned orders (orders referencing non-existent OrderMgr)
   System.out.println("Checking for orphaned orders:");
   
   // Get all OrderMgr IDs
   Set<Long> orderMgrIds = new HashSet<>();
   for (OrderMgr orderMgr : OrderMgr.findAllOrderMgrs()) {
       orderMgrIds.add(orderMgr.getId());
   }
   
   // Check all orders
   List<WeldOrder> allOrders = WeldOrder.findAllWeldOrders();
   int orphanedOrders = 0;
   
   for (WeldOrder order : allOrders) {
       if (order.getOrderMgr() != null && !orderMgrIds.contains(order.getOrderMgr().getId())) {
           System.out.println("  WARNING: Orphaned order found: " + order.getId());
           System.out.println("    References non-existent OrderMgr: " + 
                             order.getOrderMgr().getId());
           orphanedOrders++;
       }
   }
   
   if (orphanedOrders == 0) {
       System.out.println("  No orphaned orders found");
   } else {
       System.out.println("  Total orphaned orders: " + orphanedOrders);
   }
   ```

5. Verify queue stop configurations:
   ```java
   // Verify queue stop configurations
   System.out.println("Verifying queue stop configurations:");
   
   for (OrderMgr orderMgr : OrderMgr.findAllOrderMgrs()) {
       System.out.println("OrderMgr: " + orderMgr.getId() + " - " + orderMgr.getLineName());
       
       // Check if left and right queue stops are the same
       if (orderMgr.getLeftQueueStop() != null && orderMgr.getRightQueueStop() != null &&
           orderMgr.getLeftQueueStop().getId().equals(orderMgr.getRightQueueStop().getId())) {
           System.out.println("  WARNING: Left and right queue stops are the same: " + 
                             orderMgr.getLeftQueueStop().getId());
       }
       
       // Check if queue stops are valid for queuing
       if (orderMgr.getLeftQueueStop() != null) {
           Stop leftQueueStop = Stop.findStop(orderMgr.getLeftQueueStop().getId());
           if (leftQueueStop != null && 
               leftQueueStop.getStopType() != StopType.QUEUE && 
               leftQueueStop.getStopType() != StopType.STORAGE) {
               System.out.println("  WARNING: Left queue stop has invalid type: " + 
                                 leftQueueStop.getStopType());
           }
       }
       
       if (orderMgr.getRightQueueStop() != null) {
           Stop rightQueueStop = Stop.findStop(orderMgr.getRightQueueStop().getId());
           if (rightQueueStop != null && 
               rightQueueStop.getStopType() != StopType.QUEUE && 
               rightQueueStop.getStopType() != StopType.STORAGE) {
               System.out.println("  WARNING: Right queue stop has invalid type: " + 
                                 rightQueueStop.getStopType());
           }
       }
   }
   ```

6. Test performance:
   ```java
   // Test performance of common operations
   System.out.println("Testing performance of common operations:");
   
   // Test findAllOrderMgrs
   long startTime = System.currentTimeMillis();
   List<OrderMgr> allOrderMgrs = OrderMgr.findAllOrderMgrs();
   long endTime = System.currentTimeMillis();
   System.out.println("  findAllOrderMgrs: " + (endTime - startTime) + 
                     "ms for " + allOrderMgrs.size() + " records");
   
   // Test findWeldOrdersByOrderMgr for each OrderMgr
   for (OrderMgr orderMgr : allOrderMgrs) {
       startTime = System.currentTimeMillis();
       List<WeldOrder> orders = WeldOrder.findAllWeldOrdersByOrderMgr(orderMgr);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findAllWeldOrdersByOrderMgr for " + orderMgr.getLineName() + 
                         ": " + (endTime - startTime) + "ms for " + orders.size() + " orders");
   }
   
   // Test findActiveOrderForOrderMgr for each OrderMgr
   for (OrderMgr orderMgr : allOrderMgrs) {
       startTime = System.currentTimeMillis();
       WeldOrder activeOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
       endTime = System.currentTimeMillis();
       
       System.out.println("  findActiveOrderForOrderMgr for " + orderMgr.getLineName() + 
                         ": " + (endTime - startTime) + "ms, found: " + 
                         (activeOrder != null ? "Yes" : "No"));
   }
   ```

7. Check for inconsistent delivery and consumption stop configurations:
   ```java
   // Check for inconsistent delivery and consumption stop configurations
   System.out.println("Checking for inconsistent delivery and consumption stop configurations:");
   
   for (OrderMgr orderMgr : OrderMgr.findAllOrderMgrs()) {
       System.out.println("OrderMgr: " + orderMgr.getId() + " - " + orderMgr.getLineName());
       
       // Check if delivery stop is the same as any consumption stop
       if (orderMgr.getDeliveryStop() != null) {
           if (orderMgr.getLeftConsumptionStop() != null && 
               orderMgr.getDeliveryStop().getId().equals(
                   orderMgr.getLeftConsumptionStop().getId())) {
               System.out.println("  WARNING: Delivery stop is the same as left consumption stop: " + 
                                 orderMgr.getDeliveryStop().getId());
           }
           
           if (orderMgr.getRightConsumptionStop() != null && 
               orderMgr.getDeliveryStop().getId().equals(
                   orderMgr.getRightConsumptionStop().getId())) {
               System.out.println("  WARNING: Delivery stop is the same as right consumption stop: " + 
                                 orderMgr.getDeliveryStop().getId());
           }
       }
       
       // Check if left and right consumption stops are the same
       if (orderMgr.getLeftConsumptionStop() != null && orderMgr.getRightConsumptionStop() != null &&
           orderMgr.getLeftConsumptionStop().getId().equals(
               orderMgr.getRightConsumptionStop().getId())) {
           System.out.println("  WARNING: Left and right consumption stops are the same: " + 
                             orderMgr.getLeftConsumptionStop().getId());
       }
       
       // Check if consumption stops and exits are the same
       if (orderMgr.getLeftConsumptionStop() != null && orderMgr.getLeftConsumptionExit() != null &&
           orderMgr.getLeftConsumptionStop().getId().equals(
               orderMgr.getLeftConsumptionExit().getId())) {
           System.out.println("  WARNING: Left consumption stop and exit are the same: " + 
                             orderMgr.getLeftConsumptionStop().getId());
       }
       
       if (orderMgr.getRightConsumptionStop() != null && 
           orderMgr.getRightConsumptionExit() != null &&
           orderMgr.getRightConsumptionStop().getId().equals(
               orderMgr.getRightConsumptionExit().getId())) {
           System.out.println("  WARNING: Right consumption stop and exit are the same: " + 
                             orderMgr.getRightConsumptionStop().getId());
       }
   }
   ```

### Resolution
- For missing or incorrect stop references: Update OrderMgr with correct stop references
- For inconsistent configurations: Correct the configuration to ensure proper flow
- For invalid capacity settings: Update with appropriate capacity values
- For multiple active orders: Resolve conflicting orders and update status
- For orphaned orders: Update or remove orphaned orders
- For inconsistent queue configurations: Correct queue stop references
- For performance issues: Optimize queries and consider indexing

### Monitoring
- Track the number of active orders per weld line
- Monitor for configuration changes to OrderMgr entities
- Track order processing times for each weld line
- Monitor for orphaned orders
- Track delivery and consumption operations
- Monitor for queue capacity issues
- Set up alerts for configuration inconsistencies