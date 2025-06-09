# DeviceSendMessageProcessor Technical Documentation

## Purpose
The DeviceSendMessageProcessor class is responsible for sending messages from the stamp storage system to external devices (such as MES or PLC systems). It converts internal message formats to external formats and sends them through the appropriate connection device. This class acts as a bridge between the system's internal messaging system and external communication protocols.

## Logic/Functionality
- Subscribes to various internal message events (CarrierUpdateMessage, PlcAlarmMessage, etc.)
- Converts internal message formats to external GeneralMessage formats
- Sends converted messages to external devices through a ConnectionDevice
- Provides access to the last sent message for testing and debugging
- Handles different types of messages with specific event handlers

### Key Methods:
- **carrierUpdateStorageMessageListener(CarrierUpdateMessage infoMessage)**: Handles carrier update messages
- **plcAlarmMessageListener(PlcAlarmMessage infoMessage)**: Handles PLC alarm messages
- **resetAlarmMessageListener(ResetAlarmMessage infoMessage)**: Handles alarm reset messages
- **statusUpdateStorageMessageListener(StatusUpdateMessage infoMessage)**: Handles status update messages
- **getMessage()**: Returns the last sent message for testing and debugging

## Flow
1. The DeviceSendMessageProcessor is instantiated with a ConnectionDevice, registering itself as a subscriber to various message events
2. When a message event is received:
   - The message is stored for reference
   - A MesMessageBuilder is used to convert the internal message to an external GeneralMessage
   - If the ConnectionDevice is available, the converted message is sent
   - The sent message is logged
3. External devices receive and process the sent messages

## Key Elements
- **device**: ConnectionDevice instance used to send messages to external devices
- **message**: The last sent Message
- **Event handler methods**: Methods that handle different types of internal messages
- **getMessage()**: Method that returns the last sent message

## Usage
The DeviceSendMessageProcessor is typically instantiated during system startup with a ConnectionDevice and requires no further interaction. It automatically processes outgoing messages and sends them to external devices.

```java
// Example instantiation
ConnectionDevice device = new ConnectionDevice(...);
DeviceSendMessageProcessor processor = new DeviceSendMessageProcessor(device);

// No further interaction is needed; the processor will automatically
// process outgoing messages and send them to external devices

// For testing or debugging, the last sent message can be accessed
Message lastMessage = processor.getMessage();
```

## Debugging and Production Support

### Common Issues
1. **Messages not being sent**: Could occur if the ConnectionDevice is null or not properly initialized.
2. **Messages not being converted**: May happen if the MesMessageBuilder is not functioning correctly or if the message format is unexpected.
3. **External devices not receiving messages**: Could occur if there's an issue with the connection or if the external device is not properly configured.
4. **Incorrect message format**: May happen if the MesMessageBuilder does not correctly convert the internal message format.
5. **Memory leaks**: Could occur if the processor is not properly unregistered when no longer needed.

### Debugging Steps
1. Verify that the ConnectionDevice is properly initialized and not null
2. Check if the processor is correctly registered as a subscriber to the appropriate message events
3. Confirm that internal messages are being generated and published to the EventBus
4. Examine the converted message content to ensure it's in the expected format
5. Verify that the external device is receiving and processing the sent messages

### Resolution
- For messages not being sent:
  - Verify that the ConnectionDevice is properly initialized and not null
  - Add null checks before sending messages
  - Implement retry logic for failed sends
- For messages not being converted:
  - Check if the MesMessageBuilder is functioning correctly
  - Verify that the internal message format is as expected
  - Add logging to track the conversion process
- For external devices not receiving messages:
  - Check the connection status
  - Verify that the external device is properly configured
  - Implement connection monitoring and recovery
- For incorrect message format:
  - Review and update the MesMessageBuilder implementation
  - Add validation for converted messages
  - Test with known message formats
- For memory leaks:
  - Ensure that the processor is unregistered when no longer needed
  - Use weak references for subscribers where appropriate

### Monitoring
- Log all sent messages and their content
- Monitor the number of messages sent by type
- Track the number of failed sends
- Set up alerts for high message rates or send failures
- Monitor system performance during high message volumes
- Track the correlation between sent messages and external device behavior
- Monitor the connection status with external devices