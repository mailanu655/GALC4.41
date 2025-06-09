# WeldOrder Technical Documentation

## Purpose
WeldOrder.java defines an entity that represents an order for parts to be sent to a welding line. This class models the requirements for parts needed by welding operations, tracks order status, and manages the fulfillment process. It serves as a central component for coordinating the delivery of parts from storage to welding lines.

## Logic/Functionality
- Represents orders for parts needed by welding lines
- Tracks order status, delivery status, and fulfillment progress
- Manages quantities for left and right parts
- Tracks delivered and consumed quantities
- Associates orders with specific models and order managers
- Provides methods to find and manage order records
- Supports querying for orders by various criteria
- Implements business logic for order processing

## Flow
1. Orders are created with specific model and quantity requirements
2. The system tracks order status through various states (Initialized, Queued, InProcess, etc.)
3. Carriers are selected and retrieved to fulfill the order
4. Parts are delivered to welding lines
5. Consumed quantities are tracked
6. Orders are completed when all required parts are delivered and consumed

## Key Elements
- `orderMgr`: The order manager (welding line) associated with the order
- `orderSequence`: The sequence number of the order
- `model`: The model associated with the order
- `orderStatus`: The current status of the order
- `deliveryStatus`: The current delivery status of the order
- `leftQuantity` and `rightQuantity`: The required quantities for left and right parts
- `leftDeliveredQuantity` and `rightDeliveredQuantity`: The delivered quantities
- `leftConsumedQuantity` and `rightConsumedQuantity`: The consumed quantities
- `leftQueuedQty` and `rightQueuedQty`: The queued quantities
- Various comments fields for order, delivery, and fulfillment
- Static finder methods for retrieving order records
- Methods to check order status and conditions

## Usage
```java
// Find a weld order by ID
WeldOrder order = WeldOrder.findWeldOrder(1L);
System.out.println("Order: " + order.getId());
System.out.println("Model: " + order.getModel().getName());
System.out.println("Left quantity: " + order.getLeftQuantity());
System.out.println("Right quantity: " + order.getRightQuantity());
System.out.println("Order status: " + order.getOrderStatus());
System.out.println("Delivery status: " + order.getDeliveryStatus());

// Find all weld orders
List<WeldOrder> allOrders = WeldOrder.findAllWeldOrders();
System.out.println("Total orders: " + allOrders.size());

// Find orders by order manager
OrderMgr orderMgr = OrderMgr.findOrderMgr(1L);
List<WeldOrder> ordersByMgr = WeldOrder.findAllWeldOrdersByOrderMgr(orderMgr);
System.out.println("Orders for " + orderMgr.getLineName() + ": " + ordersByMgr.size());

// Find orders by status
OrderStatus status = OrderStatus.InProcess;
List<WeldOrder> ordersByStatus = WeldOrder.findWeldOrdersByOrderStatus(status);
System.out.println("Orders with status " + status + ": " + ordersByStatus.size());

// Find active order for order manager
OrderMgr orderMgr = OrderMgr.findOrderMgr(1L);
WeldOrder activeOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
if (activeOrder != null) {
    System.out.println("Active order for " + orderMgr.getLineName() + ": " + activeOrder.getId());
} else {
    System.out.println("No active order for " + orderMgr.getLineName());
}

// Find active order for delivery by order manager
OrderMgr orderMgr = OrderMgr.findOrderMgr(1L);
WeldOrder activeDeliveryOrder = WeldOrder.findActiveOrderForDeliveryByOrderMgr(orderMgr);
if (activeDeliveryOrder != null) {
    System.out.println("Active delivery order for " + orderMgr.getLineName() + 
                      ": " + activeDeliveryOrder.getId());
} else {
    System.out.println("No active delivery order for " + orderMgr.getLineName());
}

// Find orders in process
List<WeldOrder> ordersInProcess = WeldOrder.findWeldOrdersInProcess();
System.out.println("Orders in process: " + ordersInProcess.size());

// Find orders delivering
List<WeldOrder> ordersDelivering = WeldOrder.findWeldOrdersDelivering();
System.out.println("Orders delivering: " + ordersDelivering.size());

// Find orders pending
List<WeldOrder> ordersPending = WeldOrder.findWeldOrdersPending();
System.out.println("Orders pending: " + ordersPending.size());

// Create a new weld order
WeldOrder newOrder = new WeldOrder();
OrderMgr orderMgr = OrderMgr.findOrderMgr(1L);
Model model = Model.findModel(1L);
newOrder.setOrderMgr(orderMgr);
newOrder.setOrderSequence(1);
newOrder.setModel(model);
newOrder.setLeftQuantity(10);
newOrder.setRightQuantity(10);
newOrder.setOrderStatus(OrderStatus.Initialized);
newOrder.setDeliveryStatus(OrderStatus.Initialized);
newOrder.setCreatedDate(new Timestamp(System.currentTimeMillis()));
newOrder.setCreatedBy("System");
newOrder.persist();

// Update an existing weld order
WeldOrder existingOrder = WeldOrder.findWeldOrder(1L);
existingOrder.setLeftQuantity(15);
existingOrder.setRightQuantity(15);
existingOrder.setOrderStatus(OrderStatus.Queued);
existingOrder.merge();

// Update order status
WeldOrder order = WeldOrder.findWeldOrder(1L);
order.setOrderStatus(OrderStatus.InProcess);
order.merge();

// Update delivery status
WeldOrder order = WeldOrder.findWeldOrder(1L);
order.setDeliveryStatus(OrderStatus.InProcess);
order.merge();

// Update delivered quantities
WeldOrder order = WeldOrder.findWeldOrder(1L);
order.setLeftDeliveredQuantity(5);
order.setRightDeliveredQuantity(5);
order.merge();

// Update consumed quantities
WeldOrder order = WeldOrder.findWeldOrder(1L);
order.setLeftConsumedQuantity(3);
order.setRightConsumedQuantity(3);
order.merge();

// Check if order is left only
WeldOrder order = WeldOrder.findWeldOrder(1L);
if (order.isLeftOnly()) {
    System.out.println("Order is left only");
} else {
    System.out.println("Order is not left only");
}

// Check if order is right only
WeldOrder order = WeldOrder.findWeldOrder(1L);
if (order.isRightOnly()) {
    System.out.println("Order is right only");
} else {
    System.out.println("Order is not right only");
}

// Get left die
WeldOrder order = WeldOrder.findWeldOrder(1L);
Die leftDie = order.getLeftDie();
if (leftDie != null) {
    System.out.println("Left die: " + leftDie.getDescription());
} else {
    System.out.println("No left die");
}

// Get right die
WeldOrder order = WeldOrder.findWeldOrder(1L);
Die rightDie = order.getRightDie();
if (rightDie != null) {
    System.out.println("Right die: " + rightDie.getDescription());
} else {
    System.out.println("No right die");
}

// Check if a die is the left die for the order
WeldOrder order = WeldOrder.findWeldOrder(1L);
Die die = Die.findDie(1L);
if (order.isLeftDie(die)) {
    System.out.println("Die is the left die for the order");
} else {
    System.out.println("Die is not the left die for the order");
}

// Check if a die is the right die for the order
WeldOrder order = WeldOrder.findWeldOrder(1L);
Die die = Die.findDie(1L);
if (order.isRightDie(die)) {
    System.out.println("Die is the right die for the order");
} else {
    System.out.println("Die is not the right die for the order");
}

// Get left queue stop
WeldOrder order = WeldOrder.findWeldOrder(1L);
Stop leftQueueStop = order.getLeftQueueStop();
if (leftQueueStop != null) {
    System.out.println("Left queue stop: " + leftQueueStop.getName());
} else {
    System.out.println("No left queue stop");
}

// Get right queue stop
WeldOrder order = WeldOrder.findWeldOrder(1L);
Stop rightQueueStop = order.getRightQueueStop();
if (rightQueueStop != null) {
    System.out.println("Right queue stop: " + rightQueueStop.getName());
} else {
    System.out.println("No right queue stop");
}

// Check if order is in process
WeldOrder order = WeldOrder.findWeldOrder(1L);
if (order.isOrderInProcess()) {
    System.out.println("Order is in process");
} else {
    System.out.println("Order is not in process");
}

// Check if delivery is in process
WeldOrder order = WeldOrder.findWeldOrder(1L);
if (order.isDeliveryInProcess()) {
    System.out.println("Delivery is in process");
} else {
    System.out.println("Delivery is not in process");
}

// Check if order is pending
WeldOrder order = WeldOrder.findWeldOrder(1L);
if (order.isPending()) {
    System.out.println("Order is pending");
} else {
    System.out.println("Order is not pending");
}

// Delete a weld order
WeldOrder obsoleteOrder = WeldOrder.findWeldOrder(2L);
if (obsoleteOrder != null) {
    obsoleteOrder.remove();
}
```

## Debugging and Production Support

### Common Issues
1. Inconsistent order status
2. Mismatched delivered and consumed quantities
3. Orders stuck in specific states
4. Missing or incorrect model references
5. Missing or incorrect order manager references
6. Duplicate active orders for a welding line
7. Incorrect order sequence numbers

### Debugging Steps
1. Check for inconsistent order status:
   ```java
   // Check for inconsistent order status
   System.out.println("Checking for inconsistent order status:");
   
   // Get all orders
   List<WeldOrder> allOrders = WeldOrder.findAllWeldOrders();
   
   // Check for inconsistent status
   for (WeldOrder order : allOrders) {
       System.out.println("  Order: " + order.getId());
       System.out.println("    Order status: " + order.getOrderStatus());
       System.out.println("    Delivery status: " + order.getDeliveryStatus());
       
       // Check for inconsistent order status
       if (order.getOrderStatus() == null) {
           System.out.println("    WARNING: Null order status");
       }
       
       // Check for inconsistent delivery status
       if (order.getDeliveryStatus() == null) {
           System.out.println("    WARNING: Null delivery status");
       }
       
       // Check for inconsistent status combinations
       if (order.getOrderStatus() == OrderStatus.ManuallyCompleted || 
           order.getOrderStatus() == OrderStatus.AutoCompleted) {
           if (order.getDeliveryStatus() == OrderStatus.Initialized || 
               order.getDeliveryStatus() == OrderStatus.Queued) {
               System.out.println("    WARNING: Order is completed but delivery is not started");
           }
       }
       
       if (order.getOrderStatus() == OrderStatus.Initialized || 
           order.getOrderStatus() == OrderStatus.Queued) {
           if (order.getDeliveryStatus() == OrderStatus.InProcess || 
               order.getDeliveryStatus() == OrderStatus.DeliveringCarriers || 
               order.getDeliveryStatus() == OrderStatus.Delivered) {
               System.out.println("    WARNING: Order is not started but delivery is in progress");
           }
       }
       
       if (order.getOrderStatus() == OrderStatus.Cancelled && 
           order.getDeliveryStatus() != OrderStatus.Cancelled) {
           System.out.println("    WARNING: Order is cancelled but delivery is not cancelled");
       }
   }
   ```

2. Check for mismatched delivered and consumed quantities:
   ```java
   // Check for mismatched delivered and consumed quantities
   System.out.println("Checking for mismatched delivered and consumed quantities:");
   
   // Get all orders
   List<WeldOrder> allOrders = WeldOrder.findAllWeldOrders();
   
   // Check for mismatched quantities
   for (WeldOrder order : allOrders) {
       System.out.println("  Order: " + order.getId());
       
       // Get quantities
       Integer leftQuantity = order.getLeftQuantity();
       Integer rightQuantity = order.getRightQuantity();
       Integer leftDeliveredQuantity = order.getLeftDeliveredQuantity();
       Integer rightDeliveredQuantity = order.getRightDeliveredQuantity();
       Integer leftConsumedQuantity = order.getLeftConsumedQuantity();
       Integer rightConsumedQuantity = order.getRightConsumedQuantity();
       
       System.out.println("    Left quantity: " + leftQuantity);
       System.out.println("    Left delivered: " + leftDeliveredQuantity);
       System.out.println("    Left consumed: " + leftConsumedQuantity);
       System.out.println("    Right quantity: " + rightQuantity);
       System.out.println("    Right delivered: " + rightDeliveredQuantity);
       System.out.println("    Right consumed: " + rightConsumedQuantity);
       
       // Check for null quantities
       if (leftQuantity == null) {
           System.out.println("    WARNING: Null left quantity");
       }
       
       if (rightQuantity == null) {
           System.out.println("    WARNING: Null right quantity");
       }
       
       // Check for delivered > required
       if (leftDeliveredQuantity != null && leftQuantity != null && 
           leftDeliveredQuantity > leftQuantity) {
           System.out.println("    WARNING: Left delivered quantity exceeds required quantity");
       }
       
       if (rightDeliveredQuantity != null && rightQuantity != null && 
           rightDeliveredQuantity > rightQuantity) {
           System.out.println("    WARNING: Right delivered quantity exceeds required quantity");
       }
       
       // Check for consumed > delivered
       if (leftConsumedQuantity != null && leftDeliveredQuantity != null && 
           leftConsumedQuantity > leftDeliveredQuantity) {
           System.out.println("    WARNING: Left consumed quantity exceeds delivered quantity");
       }
       
       if (rightConsumedQuantity != null && rightDeliveredQuantity != null && 
           rightConsumedQuantity > rightDeliveredQuantity) {
           System.out.println("    WARNING: Right consumed quantity exceeds delivered quantity");
       }
       
       // Check for completed orders with incomplete delivery
       if ((order.getOrderStatus() == OrderStatus.ManuallyCompleted || 
            order.getOrderStatus() == OrderStatus.AutoCompleted) && 
           ((leftQuantity != null && leftDeliveredQuantity != null && 
             leftDeliveredQuantity < leftQuantity) || 
            (rightQuantity != null && rightDeliveredQuantity != null && 
             rightDeliveredQuantity < rightQuantity))) {
           System.out.println("    WARNING: Order is completed but delivery is incomplete");
       }
   }
   ```

3. Check for orders stuck in specific states:
   ```java
   // Check for orders stuck in specific states
   System.out.println("Checking for orders stuck in specific states:");
   
   // Get current timestamp
   Timestamp now = new Timestamp(System.currentTimeMillis());
   
   // Define thresholds (in milliseconds)
   long initializedThreshold = 24 * 60 * 60 * 1000; // 24 hours
   long queuedThreshold = 12 * 60 * 60 * 1000; // 12 hours
   long inProcessThreshold = 8 * 60 * 60 * 1000; // 8 hours
   long deliveringThreshold = 4 * 60 * 60 * 1000; // 4 hours
   
   // Get all orders
   List<WeldOrder> allOrders = WeldOrder.findAllWeldOrders();
   
   // Check for stuck orders
   for (WeldOrder order : allOrders) {
       if (order.getCreatedDate() == null) {
           System.out.println("  Order " + order.getId() + " has null created date");
           continue;
       }
       
       long age = now.getTime() - order.getCreatedDate().getTime();
       
       System.out.println("  Order: " + order.getId());
       System.out.println("    Age: " + (age / (60 * 60 * 1000)) + " hours");
       System.out.println("    Order status: " + order.getOrderStatus());
       System.out.println("    Delivery status: " + order.getDeliveryStatus());
       
       // Check for stuck initialized orders
       if (order.getOrderStatus() == OrderStatus.Initialized && age > initializedThreshold) {
           System.out.println("    WARNING: Order stuck in Initialized state for " + 
                             (age / (60 * 60 * 1000)) + " hours");
       }
       
       // Check for stuck queued orders
       if (order.getOrderStatus() == OrderStatus.Queued && age > queuedThreshold) {
           System.out.println("    WARNING: Order stuck in Queued state for " + 
                             (age / (60 * 60 * 1000)) + " hours");
       }
       
       // Check for stuck in-process orders
       if (order.getOrderStatus() == OrderStatus.InProcess && age > inProcessThreshold) {
           System.out.println("    WARNING: Order stuck in InProcess state for " + 
                             (age / (60 * 60 * 1000)) + " hours");
       }
       
       // Check for stuck retrieving carriers orders
       if (order.getOrderStatus() == OrderStatus.RetrievingCarriers && age > inProcessThreshold) {
           System.out.println("    WARNING: Order stuck in RetrievingCarriers state for " + 
                             (age / (60 * 60 * 1000)) + " hours");
       }
       
       // Check for stuck delivering carriers orders
       if (order.getDeliveryStatus() == OrderStatus.DeliveringCarriers && 
           age > deliveringThreshold) {
           System.out.println("    WARNING: Order stuck in DeliveringCarriers state for " + 
                             (age / (60 * 60 * 1000)) + " hours");
       }
   }
   ```

4. Check for missing or incorrect model references:
   ```java
   // Check for missing or incorrect model references
   System.out.println("Checking for missing or incorrect model references:");
   
   // Get all orders
   List<WeldOrder> allOrders = WeldOrder.findAllWeldOrders();
   
   // Check for missing or incorrect model references
   for (WeldOrder order : allOrders) {
       System.out.println("  Order: " + order.getId());
       
       // Check for null model
       if (order.getModel() == null) {
           System.out.println("    WARNING: Null model");
           continue;
       }
       
       // Check for model existence
       Model model = Model.findModel(order.getModel().getId());
       if (model == null) {
           System.out.println("    WARNING: Model " + order.getModel().getId() + 
                             " does not exist");
           continue;
       }
       
       System.out.println("    Model: " + model.getName() + " (" + model.getId() + ")");
       
       // Check for null left die
       if (model.getLeftDie() == null) {
           System.out.println("    WARNING: Model has null left die");
       }
       
       // Check for null right die
       if (model.getRightDie() == null) {
           System.out.println("    WARNING: Model has null right die");
       }
       
       // Check for left die existence
       if (model.getLeftDie() != null) {
           Die leftDie = Die.findDie(model.getLeftDie().getId());
           if (leftDie == null) {
               System.out.println("    WARNING: Left die " + model.getLeftDie().getId() + 
                                 " does not exist");
           }
       }
       
       // Check for right die existence
       if (model.getRightDie() != null) {
           Die rightDie = Die.findDie(model.getRightDie().getId());
           if (rightDie == null) {
               System.out.println("    WARNING: Right die " + model.getRightDie().getId() + 
                                 " does not exist");
           }
       }
       
       // Check for left quantity with no left die
       if (order.getLeftQuantity() > 0 && model.getLeftDie() == null) {
           System.out.println("    WARNING: Order has left quantity but model has no left die");
       }
       
       // Check for right quantity with no right die
       if (order.getRightQuantity() > 0 && model.getRightDie() == null) {
           System.out.println("    WARNING: Order has right quantity but model has no right die");
       }
   }
   ```

5. Check for missing or incorrect order manager references:
   ```java
   // Check for missing or incorrect order manager references
   System.out.println("Checking for missing or incorrect order manager references:");
   
   // Get all orders
   List<WeldOrder> allOrders = WeldOrder.findAllWeldOrders();
   
   // Check for missing or incorrect order manager references
   for (WeldOrder order : allOrders) {
       System.out.println("  Order: " + order.getId());
       
       // Check for null order manager
       if (order.getOrderMgr() == null) {
           System.out.println("    WARNING: Null order manager");
           continue;
       }
       
       // Check for order manager existence
       OrderMgr orderMgr = OrderMgr.findOrderMgr(order.getOrderMgr().getId());
       if (orderMgr == null) {
           System.out.println("    WARNING: Order manager " + order.getOrderMgr().getId() + 
                             " does not exist");
           continue;
       }
       
       System.out.println("    Order manager: " + orderMgr.getLineName() + 
                         " (" + orderMgr.getId() + ")");
       
       // Check for null delivery stop
       if (orderMgr.getDeliveryStop() == null) {
           System.out.println("    WARNING: Order manager has null delivery stop");
       }
       
       // Check for null left consumption stop
       if (orderMgr.getLeftConsumptionStop() == null) {
           System.out.println("    WARNING: Order manager has null left consumption stop");
       }
       
       // Check for null right consumption stop
       if (orderMgr.getRightConsumptionStop() == null) {
           System.out.println("    WARNING: Order manager has null right consumption stop");
       }
       
       // Check for null left queue stop
       if (orderMgr.getLeftQueueStop() == null) {
           System.out.println("    WARNING: Order manager has null left queue stop");
       }
       
       // Check for null right queue stop
       if (orderMgr.getRightQueueStop() == null) {
           System.out.println("    WARNING: Order manager has null right queue stop");
       }
       
       // Check for left quantity with no left consumption stop
       if (order.getLeftQuantity() > 0 && orderMgr.getLeftConsumptionStop() == null) {
           System.out.println("    WARNING: Order has left quantity but order manager has no left consumption stop");
       }
       
       // Check for right quantity with no right consumption stop
       if (order.getRightQuantity() > 0 && orderMgr.getRightConsumptionStop() == null) {
           System.out.println("    WARNING: Order has right quantity but order manager has no right consumption stop");
       }
   }
   ```

6. Check for duplicate active orders for a welding line:
   ```java
   // Check for duplicate active orders for a welding line
   System.out.println("Checking for duplicate active orders for a welding line:");
   
   // Get all order managers
   List<OrderMgr> orderMgrs = OrderMgr.findAllOrderMgrs();
   
   // Check for duplicate active orders
   for (OrderMgr orderMgr : orderMgrs) {
       System.out.println("  Order manager: " + orderMgr.getLineName() + 
                         " (" + orderMgr.getId() + ")");
       
       // Get active orders
       List<WeldOrder> activeOrders = WeldOrder.findWeldOrdersInProcessByOrderMgr(orderMgr);
       
       System.out.println("    Active orders: " + activeOrders.size());
       
       if (activeOrders.size() > 1) {
           System.out.println("    WARNING: Multiple active orders");
           
           for (WeldOrder order : activeOrders) {
               System.out.println("      Order: " + order.getId() + 
                                 ", Status: " + order.getOrderStatus());
           }
       }
       
       // Get active delivery orders
       List<WeldOrder> activeDeliveryOrders = WeldOrder.findWeldOrdersDeliveringByOrderMgr(orderMgr);
       
       System.out.println("    Active delivery orders: " + activeDeliveryOrders.size());
       
       if (activeDeliveryOrders.size() > 1) {
           System.out.println("    WARNING: Multiple active delivery orders");
           
           for (WeldOrder order : activeDeliveryOrders) {
               System.out.println("      Order: " + order.getId() + 
                                 ", Delivery status: " + order.getDeliveryStatus());
           }
       }
   }
   ```

7. Check for incorrect order sequence numbers:
   ```java
   // Check for incorrect order sequence numbers
   System.out.println("Checking for incorrect order sequence numbers:");
   
   // Get all order managers
   List<OrderMgr> orderMgrs = OrderMgr.findAllOrderMgrs();
   
   // Check for incorrect order sequence numbers
   for (OrderMgr orderMgr : orderMgrs) {
       System.out.println("  Order manager: " + orderMgr.getLineName() + 
                         " (" + orderMgr.getId() + ")");
       
       // Get all orders for this order manager
       List<WeldOrder> orders = WeldOrder.findAllWeldOrdersByOrderMgr(orderMgr);
       
       // Sort orders by sequence number
       Map<Integer, List<WeldOrder>> ordersBySequence = new HashMap<>();
       for (WeldOrder order : orders) {
           if (order.getOrderSequence() == null) {
               System.out.println("    WARNING: Order " + order.getId() + 
                                 " has null sequence number");
               continue;
           }
           
           if (!ordersBySequence.containsKey(order.getOrderSequence())) {
               ordersBySequence.put(order.getOrderSequence(), new ArrayList<>());
           }
           
           ordersBySequence.get(order.getOrderSequence()).add(order);
       }
       
       // Check for duplicate sequence numbers
       for (Map.Entry<Integer, List<WeldOrder>> entry : ordersBySequence.entrySet()) {
           if (entry.getValue().size() > 1) {
               System.out.println("    WARNING: Multiple orders with sequence number " + 
                                 entry.getKey());
               
               for (WeldOrder order : entry.getValue()) {
                   System.out.println("      Order: " + order.getId() + 
                                     ", Status: " + order.getOrderStatus());
               }
           }
       }
       
       // Check for sequence gaps
       List<Integer> sequences = new ArrayList<>(ordersBySequence.keySet());
       Collections.sort(sequences);
       
       for (int i = 1; i < sequences.size(); i++) {
           if (sequences.get(i) - sequences.get(i - 1) > 1) {
               System.out.println("    WARNING: Sequence gap between " + 
                                 sequences.get(i - 1) + " and " + sequences.get(i));
           }
       }
   }
   ```

### Resolution
- For inconsistent order status: Update order status to a consistent state
- For mismatched delivered and consumed quantities: Correct quantity values
- For orders stuck in specific states: Update order status or cancel stuck orders
- For missing or incorrect model references: Update model references
- For missing or incorrect order manager references: Update order manager references
- For duplicate active orders: Cancel or complete duplicate orders
- For incorrect order sequence numbers: Update sequence numbers

### Monitoring
- Monitor order status changes
- Track delivered and consumed quantities
- Monitor for orders stuck in specific states
- Track model and order manager references
- Monitor for duplicate active orders
- Track order sequence numbers
- Set up alerts for inconsistent order states
- Monitor for orders with mismatched quantities
- Track order completion times