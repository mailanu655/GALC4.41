# StorageStateImpl Technical Documentation

## Purpose
`StorageStateImpl` is the concrete implementation of the `StorageState` interface that maintains the current state of the storage system. It manages the collection of storage rows, tracks back orders, and provides methods for querying and manipulating the storage state. This class serves as the central repository of information about the current state of carriers and rows in the storage system.

## Logic/Functionality
The class implements various methods for managing the storage state:

### Row Querying
- `queryForRow(Matcher<StorageRow> matcher)`: Finds the first row that matches the specified criteria
- `queryForRows(Matcher<StorageRow> matcher)`: Finds all rows that match the specified criteria using Guava filtering
- `queryForRows(Matcher<StorageRow> matcher, List<StorageRow> laneImpls)`: Finds matching rows within a specific subset of rows
- `getRows()`: Returns the list of all rows in the storage state

### Carrier Lane Management
- `storeInLane(Carrier carrier, StorageRow storageRow)`: Stores a carrier in a specific lane, updating its destination if needed
- `releaseCarrierFromLane(StorageRow storageRow)`: Releases a carrier from a lane
- `releaseCarrierIfExistsAtHeadOfLane(Carrier carrier)`: Releases a carrier if it exists at the head of a lane
- `storeInLaneIfDestinationIsALaneAndAlreadyNotExistsInStorageSystem(Carrier carrier)`: Stores a carrier in its destination lane if it's not already in the system
- `getCarrierPositionInLane(Long laneStopConveyorId, Integer carrierNumber)`: Gets the position of a carrier in a lane
- `addCarriersToLane(List<Carrier> carriers, Stop lanestop)`: Adds multiple carriers to a lane, handling existing carriers
- `removeCarrierFromRow(Integer carrierNumber, Long laneStopConveyorId)`: Removes a carrier from a row
- `reorderCarriersInRow(Long laneStopConveyorId)`: Reorders carriers in a row based on their current state
- `populateRow(List<Carrier> carriersAlreadyInRow, List<Carrier> carriersMovingIntoRow, long laneStopConveyorId)`: Populates a row with carriers

### Carrier Management
- `sendCarrierUpdateMessage(Carrier carrier)`: Sends a message to update carrier information via EventBus
- `updateCarrier(Carrier carrier)`: Updates a carrier in the storage state
- `carrierExistsInStorageState(Carrier carrier)`: Checks if a carrier exists in the storage state
- `hadValidLaneDestination(Carrier carrier)`: Checks if a carrier has a valid lane destination
- `removeCarrierFromStorageState(Carrier carrier)`: Removes a carrier from the storage state
- `getCarrier(CarrierMes carrierMes)`: Creates a Carrier object from CarrierMes data
- `exists(List<Carrier> carriers, Carrier c)`: Checks if a carrier exists in a list

### Back Order Management
- `isCarrierPartsOnBackOrder(Die die)`: Checks if parts for a die are on back order
- `getBackOrder()`: Gets the list of back orders
- `setBackOrder(List<BackOrder> backOrder)`: Sets the list of back orders
- `removeFromBackOrderList(Die die, OrderMgr orderMgr)`: Removes a specific back order
- `removeFromBackOrderList(OrderMgr orderMgr)`: Removes all back orders for an order manager

### State Management
- `isStale()`: Checks if the storage state is stale
- `setStale(boolean stale)`: Sets the stale flag for the storage state
- `updateLane(StorageRow row)`: Updates a lane in the storage state
- `getLaneCapacityByAssociatedStop(Stop stop)`: Gets the capacity of a lane associated with a stop

### Utility Methods
- `equals(Object obj)`: Compares this storage state with another
- `areLanesSame(List<StorageRow> lhsLanes, List<StorageRow> rhsLanes)`: Compares two lists of lanes
- `toString()`: Provides a string representation of the storage state
- `hashCode()`: Returns the hash code for this object

## Flow
1. The class is instantiated with a list of storage rows
2. It maintains the stale flag and back order list
3. Clients query the state for rows matching specific criteria
4. Carriers are stored in and released from lanes
5. The state is updated as carriers move through the system
6. Back orders are tracked and managed
7. Carrier update messages are sent to notify the system of changes

## Key Elements
- List of storage rows representing the physical storage system
- List of back orders for tracking unfulfilled requests
- Stale flag to indicate if the state needs refreshing
- PredicateWrapper for adapting Hamcrest matchers to Guava predicates
- EventBus integration for sending carrier update messages
- Equality comparison logic for state comparison
- Carrier position tracking in lanes

## Usage
```java
// Example: Create a StorageStateImpl instance
List<StorageRow> rows = getStorageRows();
StorageState state = new StorageStateImpl(rows);

// Example: Find empty rows
List<StorageRow> emptyRows = state.queryForRows(RowMatchers.isCurrentCapacityEmpty());

// Example: Store a carrier in a lane
StorageRow row = emptyRows.get(0);
Carrier carrier = getCarrier();
state.storeInLane(carrier, row);

// Example: Release a carrier from a lane
Carrier releasedCarrier = state.releaseCarrierFromLane(row);

// Example: Update a carrier
carrier.setCarrierStatus(CarrierStatus.SHIPPABLE);
state.updateCarrier(carrier);
state.sendCarrierUpdateMessage(carrier);

// Example: Check if a die is on back order
Die die = Die.findDie(123L);
boolean onBackOrder = state.isCarrierPartsOnBackOrder(die);

// Example: Add a back order
List<BackOrder> backOrders = new ArrayList<>();
backOrders.add(new BackOrder(456L, 123L));
state.setBackOrder(backOrders);
```

## Debugging and Production Support

### Common Issues
1. **State Inconsistencies**: The storage state may become inconsistent with the physical system
2. **Concurrency Issues**: Multiple threads accessing the state may cause inconsistencies
3. **Back Order Management**: Back order tracking may become inconsistent
4. **Stale State**: The state may become stale and not reflect the current system condition
5. **Carrier Position Errors**: Carrier positions in lanes may be incorrectly calculated
6. **EventBus Failures**: Carrier update messages may not be delivered

### Debugging Steps
1. Check if the state is stale
   ```java
   if (state.isStale()) {
       logger.warn("Storage state is stale and may not reflect the current system");
   }
   ```
2. Verify row consistency
   ```java
   for (StorageRow row : state.getRows()) {
       logger.debug("Row {}: capacity={}, current={}, isEmpty={}, isFull={}",
           row.getId(), row.getCapacity(), row.getCurrentCarrierCount(),
           row.isEmpty(), row.isFull());
   }
   ```
3. Check carrier positions in lanes
   ```java
   Integer carrierNumber = 123;
   Long laneStopId = 456L;
   int position = state.getCarrierPositionInLane(laneStopId, carrierNumber);
   logger.debug("Carrier {} position in lane {}: {}", carrierNumber, laneStopId, position);
   ```
4. Verify back order consistency
   ```java
   List<BackOrder> backOrders = state.getBackOrder();
   logger.debug("Back orders: {}", backOrders);
   for (BackOrder backOrder : backOrders) {
       logger.debug("Back order: orderMgrId={}, dieId={}",
           backOrder.getOrderMgrId(), backOrder.getDieId());
   }
   ```
5. Monitor EventBus message delivery
   ```java
   // Add an EventBus subscriber to monitor carrier update messages
   @EventSubscriber(eventClass = CarrierUpdateMessage.class)
   public void onCarrierUpdate(CarrierUpdateMessage message) {
       logger.debug("Received carrier update message for carrier: {}",
           message.getCarrier().getCarrierNumber());
   }
   ```

### Resolution
- Implement periodic state consistency checks
- Add concurrency controls to prevent state inconsistencies
- Enhance back order management logic
- Implement mechanisms to detect and recover from stale states
- Improve carrier position calculation
- Add error handling for EventBus failures
- Consider adding transaction support for critical operations

### Monitoring
- Track state operation metrics (frequency, duration)
- Monitor state consistency with the physical system
- Log state operation details for troubleshooting
- Alert on persistent state inconsistencies
- Track back order creation and fulfillment
- Monitor the stale state flag to detect potential issues
- Track carrier positions and movements through the system
- Monitor EventBus message delivery