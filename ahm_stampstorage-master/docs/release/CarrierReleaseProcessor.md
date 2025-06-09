# CarrierReleaseProcessor Technical Documentation

## Purpose
The `CarrierReleaseProcessor` class is responsible for processing carrier status messages and managing the release of carriers from storage locations in the stamp storage system. It handles the logic for determining when and how carriers should be released based on their current location, destination, and position in the storage area.

## Logic/Functionality
- Subscribes to `CarrierStatusMessage` events using the EventBus system
- Processes carrier status updates to determine if carriers should be released
- Manages the release of carriers that have reached their destination
- Updates the storage state when carriers are released
- Handles special cases for different types of stops (release check, recirculation)

## Flow
1. Receives carrier status messages via EventBus subscription
2. Checks if the carrier is at the head of a stop (buffer position 1)
3. Determines if the carrier has reached its destination
4. Checks if there are any other carriers being released from the same storage area
5. If conditions are met, publishes a carrier update message to release the carrier
6. Updates the storage state to reflect the carrier's movement
7. Handles special cases for release check stops and recirculation stops

## Key Elements
- **process()**: Main method that processes carrier status messages for standard stops
- **releaseCheckProcessing()**: Handles carrier status messages specifically for release check stops
- **publishCarrierUpdateMessageAndUpdateStorageState()**: Creates and publishes carrier update messages
- **updateStorageState()**: Updates the storage state when carriers are released
- **EventBus subscriptions**: Uses annotation-based event subscription (@EventSubscriber)

## Usage
This class is automatically instantiated and registered with the EventBus system during application startup. It works in the background to process carrier status messages:

```java
// Example of how this class would be instantiated and used
Storage storage = new StorageImpl();
ReleaseManagerHelper helper = new ReleaseManagerHelperImpl();
CarrierReleaseProcessor processor = new CarrierReleaseProcessor(storage, helper);

// The processor will automatically receive carrier status messages via EventBus
// No direct method calls are typically needed as it works through event subscription
```

## Debugging and Production Support

### Common Issues
1. **Carriers not being released**: Carriers may get stuck at their destination
2. **Multiple carriers releasing simultaneously**: Conflicts when multiple carriers are set to release from the same area
3. **Stale data conditions**: Storage state becomes out of sync with actual carrier positions
4. **Race conditions**: Timing issues between carrier status updates and release processing
5. **Invalid stop references**: Errors when processing messages with invalid stop IDs

### Debugging Steps
1. **For carriers not being released**:
   - Check if the carrier is at the head of the stop (buffer=1)
   - Verify that the current location matches the destination
   - Check if there are other carriers releasing from the same storage area
   - Examine the CarrierRelease table for pending release entries
   - Review logs for any errors during release processing

2. **For multiple carriers releasing simultaneously**:
   - Check the `anyCarriersReleasingInStorageArea()` method results
   - Review the CarrierRelease table for multiple entries in the same area
   - Examine the timing of carrier status messages

3. **For stale data conditions**:
   - Check if StaleDataMessage events are being published
   - Verify that ReloadStorageStateMessage events are being processed
   - Examine the storage state for inconsistencies

### Resolution
1. **For carriers not being released**:
   - Manually clear the CarrierRelease table entry for the stuck carrier
   - Force a storage state reload to refresh carrier positions
   - Implement additional logging to track carrier movement

2. **For multiple carriers releasing simultaneously**:
   - Enhance the release logic to better handle concurrent releases
   - Implement a queuing mechanism for releases from the same area
   - Add transaction support to prevent race conditions

3. **For stale data conditions**:
   - Implement periodic storage state validation
   - Add reconciliation processes to align storage state with actual carrier positions
   - Enhance error recovery mechanisms

### Monitoring
1. **Track carrier release events**: Monitor the frequency and success of carrier releases
2. **Alert on stuck carriers**: Set up alerts for carriers that remain at their destination for too long
3. **Monitor storage state consistency**: Periodically validate that the storage state matches actual carrier positions
4. **Track event processing metrics**: Monitor EventBus subscription performance and message processing times
5. **Log carrier movement**: Maintain detailed logs of carrier movements for troubleshooting