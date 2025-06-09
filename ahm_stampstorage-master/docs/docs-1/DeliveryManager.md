# DeliveryManager Technical Documentation

## Purpose
The DeliveryManager class is responsible for managing the delivery of carriers from queue stops to weld lines. It implements the Fulfillment interface and handles the process of selecting carriers from queues, updating their status, and releasing them to delivery destinations.

## Logic/Functionality
- Implements the Fulfillment interface with run() and getOrderManager() methods
- Manages the delivery of carriers from queue stops to weld lines
- Updates carrier fulfillment status as they move through the delivery process
- Tracks delivered quantities and updates order status accordingly
- Handles exceptions such as wrong die in queue

### Key Methods:
- **run()**: Main execution method that processes active orders for delivery
- **addCarriersInQueueNotPartOfActiveOrderToOrder()**: Adds carriers in the queue that aren't part of the active order
- **queuesHveReqCntOfCarriersWithCorrectDieToDeliver()**: Checks if queues have required count of carriers with correct die
- **deliverCarriers()**: Processes carriers that are ready to be delivered
- **updateDeliveredQty()**: Updates the delivered quantity for an order
- **getCarriersToDeliverFromQueueRow()**: Selects carriers from a queue row for delivery

## Flow
1. Retrieves the active order for delivery from the order manager
2. Updates the delivered quantities for the order
3. If the order is in "InProcess" status:
   - If all required quantities have been delivered, marks the order as "AutoCompleted"
   - Otherwise, adds carriers in the queue to the order if they're not already part of it
   - Checks if there's space available to deliver carriers
   - If no other order is delivering carriers, selects carriers from queues for delivery
   - Updates the order status to "DeliveringCarriers" if carriers are selected
4. If the order is in "DeliveringCarriers" status:
   - Processes carriers that are ready to be delivered
   - Updates carrier status to "READY_TO_DELIVER" and sets their destination
   - Releases carriers to the delivery destination

## Key Elements
- **orderMgr**: Reference to the OrderMgr that manages the order
- **newFulfillmentHelper**: Helper instance for accessing fulfillment-related methods
- **releaseManager**: Manager for releasing carriers
- **deliveryCarrierCycleSize**: Configuration parameter for the number of carriers in a delivery cycle
- **fulfillmentCarrierInspectionStop**: Configuration parameter for the inspection stop ID

## Usage
The DeliveryManager is used in the stamp storage system to manage the delivery of carriers from queue stops to weld lines. It's instantiated with an OrderMgr, a NewFulfillmentHelper, a ReleaseManager, and configuration parameters.

```java
// Example instantiation
DeliveryManager manager = new DeliveryManager(
    orderMgrInstance,
    helperInstance,
    releaseManagerInstance,
    "DELIVERY_CARRIER_CYCLE_SIZE",
    "FULFILLMENT_CARRIER_INSPECTION_STOP"
);

// Example usage
manager.run();
```

## Debugging and Production Support

### Common Issues
1. **Wrong die in queue**: Occurs when a carrier in the queue has a die that doesn't match the expected die for that queue.
2. **Not enough carriers in queue**: Happens when there aren't enough carriers in the queue to fulfill the delivery requirements.
3. **No space available to deliver**: Occurs when there's no space available at the delivery destination.
4. **Another order delivering carriers**: Happens when another order is already in the process of delivering carriers.
5. **Carriers not being released**: Could occur if there's an issue with the release manager or if the carrier status isn't updated correctly.

### Debugging Steps
1. Check the logs for error messages or exceptions, particularly WrongDieInQueueException
2. Verify that the carriers in the queue have the correct die
3. Check if there's space available at the delivery destination
4. Confirm that no other order is in "DeliveringCarriers" status
5. Verify that the carrier status is being updated correctly
6. Check if the release manager is functioning properly

### Resolution
- For wrong die in queue:
  - Manually correct the queue row
  - Reset the alarm
  - Place the order back in process
- For not enough carriers in queue:
  - Wait for more carriers to be added to the queue
  - Check if carriers are being released from storage
- For no space available to deliver:
  - Wait for space to become available
  - Check if carriers at the delivery destination are being processed
- For another order delivering carriers:
  - Wait for the other order to complete delivery
  - Check if the other order is stuck in "DeliveringCarriers" status
- For carriers not being released:
  - Check if the release manager is functioning properly
  - Verify that the carrier status is being updated correctly

### Monitoring
- Monitor the number of carriers in the queue
- Track the delivered quantities for each order
- Monitor the space available at delivery destinations
- Log all carrier selections and releases
- Set up alerts for exceptions or errors during processing, especially WrongDieInQueueException
- Monitor orders that remain in "DeliveringCarriers" status for extended periods