# ReleaseManager Technical Documentation

## Purpose
The `ReleaseManager` interface defines the contract for components responsible for releasing carriers from storage locations in the stamp storage system. It provides the core functionality for determining when and how carriers should be released based on their current location, destination, and system conditions.

## Logic/Functionality
- Defines a single method `releaseCarrier()` that handles the logic for releasing carriers
- Takes parameters for carrier number, destination, source, and a boolean flag for release manager control
- Serves as the primary interface for components that need to trigger carrier releases

## Flow
1. Components that need to release carriers call the `releaseCarrier()` method
2. The implementation determines if the carrier can be released immediately or needs to be queued
3. If the carrier can be released immediately, a carrier update message is published
4. If the carrier needs to be queued, it is saved to the CarrierRelease table
5. The carrier is eventually released when conditions allow

## Key Elements
- **releaseCarrier()**: The core method for releasing carriers, which takes:
  - `carrierNumber`: The identifier for the carrier to release
  - `destination`: The target stop for the carrier
  - `source`: The source of the release request
  - `releaseManager`: A boolean flag that determines if the release manager should control the release process

## Usage
This interface is implemented by classes that provide carrier release functionality and is used by components that need to trigger carrier releases:

```java
// Example of how this interface would be used
ReleaseManager releaseManager = new ReleaseManagerImpl(releaseManagerHelper);

// Release a carrier to a specific destination
Integer carrierNumber = 1001;
Stop destination = Stop.findStop(789L);
String source = "USER_INTERFACE";
boolean useReleaseManager = true;

releaseManager.releaseCarrier(carrierNumber, destination, source, useReleaseManager);
```

## Debugging and Production Support

### Common Issues
1. **Carriers not being released**: Carriers may not be released when expected
2. **Incorrect destination assignment**: Carriers may be sent to the wrong destination
3. **Release manager flag misuse**: Incorrect use of the releaseManager flag
4. **Missing or invalid parameters**: Null or invalid values for required parameters

### Debugging Steps
1. **For carriers not being released**:
   - Check if the releaseManager flag is set correctly
   - Verify that the carrier exists and is in a releasable state
   - Examine the implementation's logic for determining when to release
   - Check for any error conditions that might prevent release

2. **For incorrect destination assignment**:
   - Verify that the destination Stop object is valid
   - Check if the destination is reachable from the carrier's current location
   - Examine the routing logic in the implementation

3. **For release manager flag issues**:
   - Review the documentation to understand the correct usage of the flag
   - Check how the flag affects the release decision in the implementation

### Resolution
1. **For carriers not being released**:
   - Implement additional logging in the release process
   - Add validation for carrier state before attempting release
   - Enhance error handling to provide better feedback

2. **For incorrect destination assignment**:
   - Add validation for destination stops
   - Implement destination reachability checks
   - Enhance routing logic to prevent invalid destinations

3. **For release manager flag issues**:
   - Clarify the documentation for the flag
   - Add validation to ensure proper flag usage
   - Consider simplifying the release logic

### Monitoring
1. **Track carrier release requests**: Monitor the frequency and source of release requests
2. **Alert on failed releases**: Set up alerts for release attempts that fail
3. **Monitor release queue**: Track the size and age of the carrier release queue
4. **Log release decisions**: Maintain detailed logs of release decisions for troubleshooting