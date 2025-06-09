# FulfillmentProcessor Technical Documentation

## Purpose
The FulfillmentProcessor class is responsible for tracking and updating the status of carriers as they move through the fulfillment process. It listens for carrier status messages and updates the corresponding OrderFulfillment records based on the carrier's current location and destination.

## Logic/Functionality
- Subscribes to carrier status messages via the EventBus
- Updates carrier fulfillment status based on the carrier's current location
- Handles carriers at release check stops, queue stops, and inspection stops
- Publishes carrier update messages to redirect carriers to their final delivery destinations
- Validates carrier status messages and generates alarms for invalid messages

### Key Methods:
- **carrierStatusMessageListener**: Updates carrier fulfillment status for carriers in the fulfillment process
- **deliveryCarrierStatusMessageListener**: Handles carriers at inspection stops and updates their status for delivery
- **validateCarrierStatusMessage**: Validates carrier status messages and generates alarms for invalid messages
- **publishCarrierUpdateMessage**: Publishes carrier update messages to redirect carriers

## Flow
1. Receives carrier status messages from the EventBus
2. Validates the carrier status message
3. For the general carrier status listener:
   - Updates the carrier's current location and timestamp
   - If the carrier is at a release check stop and has RETRIEVED status, updates it to RELEASED
   - If the carrier is at a queue stop and doesn't have a delivery-related status, updates it to QUEUED
4. For the delivery carrier status listener:
   - Handles carriers at inspection stops
   - Updates the carrier's status to DELIVERED
   - If the carrier is shippable, updates its destination to the final delivery stop
   - If the carrier is not shippable, adds a comment to the order

## Key Elements
- **inspectionStop**: The stop where carriers are inspected before final delivery
- **newFulfillmentHelper**: Helper instance for accessing fulfillment-related methods
- **carrierNumber**: The number of the carrier being processed

## Usage
The FulfillmentProcessor is used in the stamp storage system to track and update the status of carriers as they move through the fulfillment process. It's instantiated with a NewFulfillmentHelper and a configuration parameter for the inspection stop.

```java
// Example instantiation
FulfillmentProcessor processor = new FulfillmentProcessor(
    helperInstance,
    "FULFILLMENT_CARRIER_INSPECTION_STOP"
);
```

## Debugging and Production Support

### Common Issues
1. **Invalid stop in carrier status message**: Occurs when the current location or destination in a carrier status message is invalid.
2. **Invalid die in carrier status message**: Happens when the die in a carrier status message is invalid.
3. **Carrier not found in order fulfillment**: Occurs when a carrier is not associated with any order fulfillment.
4. **Carrier status not updated**: Could happen if there's an issue with the database or if the carrier status message is not processed correctly.
5. **Carrier not redirected to final delivery stop**: May occur if the carrier is not shippable or if there's an issue with publishing the carrier update message.

### Debugging Steps
1. Check the logs for error messages or exceptions
2. Verify that the carrier status messages have valid current location, destination, and die
3. Confirm that the carrier is associated with an order fulfillment
4. Check if the carrier status is being updated in the database
5. Verify that carrier update messages are being published correctly

### Resolution
- For invalid stop in carrier status message:
  - Check the stop configuration in the system
  - Verify that the stop IDs in the carrier status message are valid
- For invalid die in carrier status message:
  - Check the die configuration in the system
  - Verify that the die ID in the carrier status message is valid
- For carrier not found in order fulfillment:
  - Check if the carrier is associated with an order
  - Verify that the order fulfillment records are created correctly
- For carrier status not updated:
  - Check if there's an issue with the database
  - Verify that the carrier status message is being processed correctly
- For carrier not redirected to final delivery stop:
  - Check if the carrier is shippable
  - Verify that the carrier update message is being published correctly

### Monitoring
- Monitor the number of carrier status messages received
- Track the number of carriers at each stage of the fulfillment process
- Log all carrier status updates and redirections
- Set up alerts for invalid carrier status messages
- Monitor carriers that remain at inspection stops for extended periods