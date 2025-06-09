# PropertyUtil Documentation

## Purpose
The PropertyUtil class is a central configuration component in the AHM LC Sales Interface Service. It provides a single access point for all application settings, making it easy to retrieve configuration values throughout the application without hardcoding them. Think of it as the application's "settings manager" that keeps all configuration in one organized place.

## How It Works
PropertyUtil works like a bridge between the application's code and its configuration files. When any part of the application needs a configuration value (like a queue name, URL, or email address), it asks PropertyUtil to retrieve that value from the application's properties.

### Step-by-Step Process
1. **Configuration Loading**: When the application starts, Spring automatically loads settings from the application.properties file
2. **PropertyUtil Initialization**: Spring creates the PropertyUtil component and injects the Environment object
3. **Value Retrieval**: When code needs a configuration value, it calls the appropriate method on PropertyUtil
4. **Value Delivery**: PropertyUtil retrieves the value from the Environment and returns it to the calling code

## Key Components

### Fields
- `env`: The Spring Environment object that contains all configuration properties

### Methods
PropertyUtil provides numerous getter methods organized by functional areas:

#### Queue Configuration
- `getLCReceivingQueueName()`: Gets the queue name for receiving LC messages
- `getSalesReceivingQueueName()`: Gets the queue name for receiving YMS messages
- `getSalesShippingQueueName()`: Gets the queue name for sending messages to YMS

#### System Configuration
- `getProductType()`: Gets the product type (e.g., "FRAME")
- `getPlantName()`: Gets the plant name (e.g., "MAP")
- `getGALCUrl()`: Gets the base URL for the GALC system
- `getTrackingUrl()`: Gets the complete URL for tracking services

#### Process Configuration
- `getProcessPoint(String messageType)`: Gets the process point ID for a specific message type
- `getAFOffProcessPoint()`: Gets the process point ID for assembly finish operations
- `getCCCPartName()`: Gets the CCC part name for shipping
- `getKeyNoPartName()`: Gets the key number part name for shipping
- `getAdcProcessCode()`: Gets the ADC process code
- `getPartInstalled()`: Gets the parts installed configuration
- `getSendLocation()`: Gets the send location
- `getTranType()`: Gets the transaction type

#### Return Process Configuration
- `getPropertyByDefectName()`: Gets the defect name for returns
- `getPropertyByRepairName()`: Gets the repair area name for returns
- `getBackoutPartList()`: Gets the list of parts to back out during returns
- `updateNaqEnable()`: Checks if NAQ updates are enabled

#### Job Control
- `shippingJobEnable()`: Checks if the shipping job is enabled
- `statusJobEnable()`: Checks if the status job is enabled

#### Email Configuration
- `getEmailTo()`: Gets the list of email recipients for notifications
- `getEmailFrom()`: Gets the sender email address for notifications
- `getEmailSubject()`: Gets the subject line for notification emails

#### Destination Configuration
- `getDestinationSite()`: Gets the destination site (e.g., "YMS")
- `getDestinationEnv()`: Gets the destination environment (e.g., "test")

#### Timeout Configuration
- `getReadTimeout()`: Gets the read timeout for HTTP connections
- `getConnectTimeout()`: Gets the connection timeout for HTTP connections

## Interactions
The PropertyUtil class interacts with many components in the system:

### Direct Dependencies
- **Spring Environment**: Provides access to all configuration properties
- **application.properties**: The source file containing all configuration values

### Used By
- **Transaction Tasks**: Both ReceivingTransactionTask and ShippingTransactionTask use PropertyUtil to get queue names and other settings
- **Message Handlers**: All message handlers use PropertyUtil to get process point IDs and other configuration
- **Email Sender**: Uses PropertyUtil to get email configuration
- **Service Classes**: Many service classes use PropertyUtil to get URLs and timeouts
- **Schedulers**: Use PropertyUtil to determine if jobs are enabled

## Configuration Source
The PropertyUtil values come from the application.properties file, which includes settings like:

```properties
# Queue Configuration
sales.shipping.queue.name=RQ.AH1_YMS01.GALC_YMS_MAP_AUTO_REQ.001
sales.receiving.queue.name=LQ.YMS_GALC_MAP_INFO_REQ.001
lc.receiving.queue.name=LQ.TEST.01

# System Configuration
lc.product.type=FRAME
lc.plant.name=MAP
lc.url=http://qmap1was.ham.am.honda.com:8005
lc.tracking.url=/TrackingService/track

# Process Configuration
lc.ccc.part.name=VQSHIPCCC
lc.AFOFF.process.id=AAF1OF1PQ0111
lc.key.part.name=VQSHIPKEYNO
lc.adc.process.code=50A

# Job Control
lc.ship.msg.job.enable=TRUE
lc.status.msg.job.enable=TRUE

# Email Configuration
spring.mail.from=No-Reply-AHM@na.honda.com
spring.mail.to=ambica_gawarla@na.honda.com
spring.mail.subject=Sales Interface Service alert
```

## Visual Workflow

### Configuration Loading Flow
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  application.   │────>│  Spring         │────>│  Environment    │────>│  PropertyUtil   │
│  properties     │     │  Framework      │     │  Object         │     │  Component      │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
   Configuration         Loads Properties        Stores Properties       Provides Access
      Source                                                              Methods
```

### Property Retrieval Flow
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Application    │────>│  PropertyUtil   │────>│  Environment    │────>│  Application    │
│  Component      │     │  Method Call    │     │  Lookup         │     │  Component      │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
   Needs Setting         Requests Value          Retrieves Value         Uses Setting
                                                                          Value
```

## Data Flow

```
┌─────────────────┐                                  ┌─────────────────┐
│  Configuration  │                                  │  Application    │
│  Properties     │                                  │  Components     │
└────────┬────────┘                                  └────────▲────────┘
         │                                                    │
         │                                                    │
         │                                                    │
         │         ┌─────────────────────────────┐            │
         └────────>│                             │────────────┘
                   │        PropertyUtil         │
         ┌────────>│                             │────────────┐
         │         └─────────────────────────────┘            │
         │                                                    │
         │                                                    │
         │                                                    │
┌────────▼────────┐                                  ┌────────┴────────┐
│  Spring         │                                  │  Configuration  │
│  Environment    │                                  │  Updates        │
└─────────────────┘                                  └─────────────────┘
```

## Example Use Cases

### Example 1: Retrieving Queue Names in Transaction Tasks

When the application needs to read messages from a queue:

```java
// In ReceivingTransactionTask.java
@Autowired
private PropertyUtil propertyUtil;

@Override
public void execute() {
    try {
        // Get the queue name from PropertyUtil
        String queueName = propertyUtil.getSalesReceivingQueueName();
        
        // Use the queue name to receive messages
        String message = queueManagerService.recv(queueName);
        
        // Process the message...
    } catch (Exception ex) {
        logger.error("Error processing message", ex.getMessage());
    }
}
```

### Example 2: Configuring Email Notifications

When sending error notifications via email:

```java
// In EmailSender.java
@Autowired
private PropertyUtil propertyUtil;

public void sendEmail(String messageId, List<String> errorList) {
    SimpleMailMessage message = new SimpleMailMessage();
    
    // Get email configuration from PropertyUtil
    message.setFrom(propertyUtil.getEmailFrom());
    message.setTo(propertyUtil.getEmailTo());
    message.setSubject(propertyUtil.getEmailSubject());
    
    // Build and send the message...
    mailSender.send(message);
}
```

### Example 3: Controlling Scheduled Jobs

When determining if a scheduled job should run:

```java
// In ShippingMessageScheduler.java
@Autowired
PropertyUtil propertyUtil;

@Scheduled(cron = "${ship.scheduledTasks.cron.expression}")
public void execute() {
    // Check if the job is enabled in configuration
    boolean isEnabled = propertyUtil.shippingJobEnable();
    
    if (isEnabled) {
        // Run the job...
        shippingTransactionTask.execute();
    } else {
        logger.info("Shipping job is disabled in configuration");
    }
}
```

## Debugging Production Issues

### Common Issues and Solutions

#### 1. Incorrect Queue Names

**Symptoms:**
- Messages not being sent or received
- Connection errors to queues

**Debugging Steps:**
1. Check the current queue configuration:
   ```sql
   -- If using a database for logging:
   SELECT message 
   FROM application_logs 
   WHERE message LIKE '%Queue-%' 
   ORDER BY timestamp DESC 
   LIMIT 10;
   ```
2. Verify the queue names in application.properties
3. Check if the queues exist in the IBM MQ server
4. Test queue connectivity directly from the application server

#### 2. URL Connection Issues

**Symptoms:**
- Timeout errors when connecting to GALC
- Services unable to retrieve data

**Debugging Steps:**
1. Check the current URL configuration:
   ```sql
   -- If using a database for logging:
   SELECT message 
   FROM application_logs 
   WHERE message LIKE '%URL%' OR message LIKE '%Connection%' 
   ORDER BY timestamp DESC 
   LIMIT 20;
   ```
2. Verify the URLs in application.properties
3. Test connectivity to the URLs from the application server:
   ```bash
   curl -v http://qmap1was.ham.am.honda.com:8005/TrackingService/track
   ```
4. Check if timeout values need adjustment

#### 3. Missing or Incorrect Process Point IDs

**Symptoms:**
- Tracking operations failing
- Error messages about invalid process points

**Debugging Steps:**
1. Check the process point being used:
   ```sql
   -- If using a database for logging:
   SELECT message 
   FROM application_logs 
   WHERE message LIKE '%process point%' OR message LIKE '%process.id%' 
   ORDER BY timestamp DESC 
   LIMIT 20;
   ```
2. Verify the process point IDs in application.properties
3. Check if all required process points are defined for each message type
4. Test with a known valid process point ID

### Visual Debugging Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ 1. Identify     │────>│ 2. Check Logs   │────>│ 3. Verify       │
│    Config Issue │     │    for Clues    │     │    Properties   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
┌─────────────────┐     ┌─────────────────┐            ▼
│ 6. Update       │<────│ 5. Test New     │<────┌─────────────────┐
│    Config       │     │    Settings     │     │ 4. Check System │
└─────────────────┘     └─────────────────┘     │    Connectivity │
                                                └─────────────────┘
```

## Debugging Queries

### Check for Queue Configuration Issues
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE '%queue%' 
   OR message LIKE '%Queue%'
   OR message LIKE '%MQ%'
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check for URL and Connection Issues
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE '%URL%' 
   OR message LIKE '%url%'
   OR message LIKE '%connect%'
   OR message LIKE '%timeout%'
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check for Process Point Issues
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE '%process point%' 
   OR message LIKE '%process.id%'
   OR message LIKE '%tracking%'
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check for Email Configuration Issues
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE '%email%' 
   OR message LIKE '%mail%'
   OR message LIKE '%smtp%'
ORDER BY timestamp DESC 
LIMIT 20;
```

## Summary

The PropertyUtil class is a critical component in the AHM LC Sales Interface Service that centralizes all configuration settings. It provides a clean, organized way to access application properties throughout the codebase, making the application more maintainable and easier to configure.

Think of PropertyUtil as the application's "settings manager" - it ensures that all parts of the system can easily access the configuration they need without hardcoding values or duplicating logic. This centralization makes it much easier to update settings when needed, as changes only need to be made in one place.

While PropertyUtil doesn't directly interact with the database, it influences nearly every aspect of the application's behavior through the configuration values it provides, making it an essential part of the application architecture.