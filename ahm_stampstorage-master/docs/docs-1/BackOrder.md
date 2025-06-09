# BackOrder Technical Documentation

## Purpose
`BackOrder` is a simple data class that represents a back order in the stamp storage system. It associates an order manager ID with a die ID, indicating that parts produced by that die are on back order for a specific order. This class is used to track and manage unfulfilled part requests in the system.

## Logic/Functionality
The class provides:
- Storage for order manager ID and die ID
- Equality comparison based on both IDs
- String representation for logging and debugging
- Accessor methods for the stored IDs

The key functionality includes:
- Constructor that initializes both IDs
- `toString()` method for readable representation
- Getter methods for both IDs
- `equals()` method that compares BackOrder objects based on their IDs

## Flow
1. BackOrder objects are created when parts are needed but not available
2. They are stored in a collection within the storage state
3. When parts become available, the corresponding BackOrder is removed
4. The system uses BackOrder objects to prioritize fulfillment of pending orders

## Key Elements
- `orderMgrId`: The ID of the order manager requesting the parts
- `dieId`: The ID of the die that produces the needed parts
- `equals()` method for comparing BackOrder objects
- `toString()` method for debugging
- Null-safe equality comparison

## Usage
```java
// Example: Create a back order
BackOrder backOrder = new BackOrder(123L, 456L);

// Example: Check if a back order matches a specific die and order
if (backOrder.getOrderMgrId().equals(orderId) && backOrder.getDieId().equals(dieId)) {
    // Process the back order...
}

// Example: Add to a collection of back orders
List<BackOrder> backOrders = new ArrayList<>();
backOrders.add(new BackOrder(123L, 456L));

// Example: Check if a back order already exists in the collection
BackOrder newBackOrder = new BackOrder(123L, 456L);
if (!backOrders.contains(newBackOrder)) {
    backOrders.add(newBackOrder);
}
```

## Debugging and Production Support

### Common Issues
1. **Null IDs**: The equals method may throw NullPointerException if IDs are null
2. **Duplicate Back Orders**: Multiple identical back orders might be created
3. **Orphaned Back Orders**: Back orders might not be removed when fulfilled
4. **Missing Back Orders**: Back orders might not be created when needed

### Debugging Steps
1. Log all back orders in the system to identify potential duplicates or orphans
   ```java
   for (BackOrder backOrder : backOrders) {
       logger.debug("Back Order: {}", backOrder);
   }
   ```
2. Check for null IDs when creating back orders
   ```java
   if (orderId == null || dieId == null) {
       logger.warn("Attempting to create BackOrder with null ID(s): orderId={}, dieId={}", orderId, dieId);
   }
   ```
3. Verify that back orders are properly removed when fulfilled
   ```java
   logger.info("Removing back order: {}", backOrder);
   boolean removed = backOrders.remove(backOrder);
   if (!removed) {
       logger.warn("Failed to remove back order: {}", backOrder);
   }
   ```
4. Add logging when back orders are created and processed

### Resolution
- The equals method already includes null checks for IDs
- Implement a mechanism to prevent duplicate back orders
- Add periodic cleanup of orphaned back orders
- Enhance logging around back order creation and removal
- Consider adding timestamps to track when back orders were created

### Monitoring
- Track the number of back orders in the system
- Monitor the age of back orders to identify those that remain unfulfilled for too long
- Log back order creation and removal events
- Set up alerts for back orders that remain unfulfilled beyond a threshold
- Track back order fulfillment rates to identify potential system issues