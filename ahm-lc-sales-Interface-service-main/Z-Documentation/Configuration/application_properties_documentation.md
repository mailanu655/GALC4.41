# Application Properties Documentation

## Purpose

The `application.properties` file is the central configuration hub for the AHM LC Sales Interface Service. It serves as a single source of truth for all application settings, allowing the application to be configured without changing code. Think of it as the "control panel" for the entire application, where you can adjust settings like:

- Queue connections
- Email notifications
- System URLs
- Process IDs
- Job scheduling
- Timeout values

## How It Works

The application.properties file works through a simple key-value pair system. When the application starts:

1. Spring automatically loads all settings from this file
2. The PropertyUtil component provides access to these settings through easy-to-use methods
3. Other components request configuration values from PropertyUtil as needed
4. Changes to settings only require updating this file and restarting the application

## Key Components

The application.properties file is organized into several functional sections:

### IBM MQ Configuration
```properties
ibm.mq.queueManager=MAP_TGAL
ibm.mq.channel=ADMIN.SVRCONN
ibm.mq.connName=qmap1mq.ham.am.honda.com(1414)
ibm.mq.user=was7adm
```
These settings control how the application connects to IBM MQ, which is used for message queuing.

### Email Configuration
```properties
spring.mail.host=SMTPGTW1.ham.am.honda.com
spring.mail.port=25
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
spring.mail.from=No-Reply-AHM@na.honda.com
spring.mail.to=ambica_gawarla@na.honda.com
spring.mail.subject=Sales Interface Service alert
```
These settings control how the application sends email notifications for alerts and errors.

### Queue Configuration
```properties
#sales.shipping.queue.name=RQ.AH1_YMS01.GALC_YMS_MAP_AUTO_REQ.001
#sales.receiving.queue.name=LQ.YMS_GALC_MAP_INFO_REQ.001
#lc.receiving.queue.name=LQ.TEST.01
```
These settings define the queue names for sending and receiving messages.

### System Configuration
```properties
lc.product.type=FRAME
lc.plant.name=MAP
lc.url=http://qmap1was.ham.am.honda.com:8005
lc.tracking.url=/TrackingService/track
lc.findbykey.method=findByKey?
lc.save.method=save
```
These settings define core system parameters like product type, plant name, and service URLs.

### Process Configuration
```properties
lc.ccc.part.name=VQSHIPCCC
lc.AFOFF.process.id=AAF1OF1PQ0111
lc.key.part.name=VQSHIPKEYNO
lc.adc.process.code=50A
lc.part.installed=
lc.tran.type=PR
lc.destination.site=YMS
lc.destination.env=test
```
These settings define process-specific parameters like part names, process IDs, and destination information.

### Job Control
```properties
lc.ship.msg.job.enable=TRUE
ship.scheduledTasks.cron.expression=0 */1 * * * ?
lc.status.msg.job.enable=TRUE
status.scheduledTasks.cron.expression=0 */1 * * * ?
```
These settings control whether scheduled jobs are enabled and how often they run.

### Process Point IDs
```properties
AH-RCVD.process.id=AAH1RE1P00101
DLR-ASGN.process.id=AAH1SC1P00101
AH-SHIP.process.id=AAH1DC1P00101
AH-RTN.process.id=AVQ1FR1P00101
PPO-ON.process.id=AAH1PP1P00101
PPO-OFF.process.id=AAH1PP1P00102
ON-TRN.process.id=AAH1SC1P00103
SHIPPER.process.id=AAH1SC1P00102
DLR-RCPT.process.id=AAH1DL1P00102
DLR-RTN.process.id=AAH1DL1P00101
```
These settings define process point IDs for different message types.

### Return Process Configuration
```properties
AH-RTN.naq.defect.name=AH_RETURN
AH-RTN.naq.repair.area=VQ_FACTORY_RETURN
AH-RTN.backout.part=INJ_MATERIALLOT1
AH-RTN.naq.update=FALSE
```
These settings control how return processes are handled.

### Logging Configuration
```properties
serviceName=Sales Interface Service
applicationID=YMS
logging.level.root=error
logging.level.org.springframework=error
logging.level.com.honda.ahm=info
```
These settings control logging behavior, including log levels for different components.

## Interactions

The application.properties file interacts with many components in the system:

### Direct Dependencies

- **Spring Framework**: Automatically loads the properties file at startup
- **PropertyUtil**: Provides programmatic access to all configuration values
- **Environment Object**: Spring's internal representation of all configuration properties

### Used By

- **Transaction Tasks**: Both ReceivingTransactionTask and ShippingTransactionTask use these properties to get queue names and other settings
- **Message Handlers**: All message handlers use these properties to get process point IDs and other configuration
- **Email Sender**: Uses these properties to get email configuration
- **Service Classes**: Many service classes use these properties to get URLs and timeouts
- **Schedulers**: Use these properties to determine if jobs are enabled and when to run

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
│  application.   │                                  │  Application    │
│  properties     │                                  │  Components     │
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

## Database Interactions

The application.properties file does not directly interact with any database. However, it indirectly affects database operations through:

1. **Connection Settings**: If the application were to use a database, connection settings would be defined here
2. **Service Configuration**: Services that interact with databases would use configuration from this file
3. **Logging Configuration**: Database logging levels and settings are controlled here

## Debugging Production Issues

### Common Issues and Solutions

#### 1. Queue Connection Issues

**Symptoms:**
- Messages not being sent or received
- Connection errors to queues
- Error logs showing MQ connection failures

**Debugging Steps:**
1. Check the IBM MQ configuration in application.properties
2. Verify the queue names are correct
3. Test connectivity to the MQ server:
   ```bash
   telnet qmap1mq.ham.am.honda.com 1414
   ```
4. Check if the queues exist in the IBM MQ server
5. Verify the user has proper permissions

#### 2. Email Notification Issues

**Symptoms:**
- Alerts not being sent
- Error logs showing mail sending failures

**Debugging Steps:**
1. Check the email configuration in application.properties
2. Verify the SMTP server is accessible:
   ```bash
   telnet SMTPGTW1.ham.am.honda.com 25
   ```
3. Test sending a simple email through the SMTP server
4. Check if the recipient email addresses are valid

#### 3. Scheduled Job Issues

**Symptoms:**
- Jobs not running at expected times
- Missing processing of messages

**Debugging Steps:**
1. Check the job control settings in application.properties
2. Verify the cron expressions are correct
3. Check application logs for scheduler startup messages
4. Temporarily set more verbose logging for the scheduler

#### 4. URL Connection Issues

**Symptoms:**
- Timeout errors when connecting to GALC
- Services unable to retrieve data

**Debugging Steps:**
1. Check the URL configuration in application.properties
2. Verify the URLs are accessible:
   ```bash
   curl -v http://qmap1was.ham.am.honda.com:8005/TrackingService/track
   ```
3. Check if timeout values need adjustment
4. Test connectivity from the application server

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
SELECT timestamp, level, logger, message 
FROM application_logs 
WHERE message LIKE '%queue%' 
   OR message LIKE '%Queue%'
   OR message LIKE '%MQ%'
   OR message LIKE '%connection%'
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check for Email Notification Issues
```sql
-- If using a database for logging:
SELECT timestamp, level, logger, message 
FROM application_logs 
WHERE message LIKE '%email%' 
   OR message LIKE '%mail%'
   OR message LIKE '%smtp%'
   OR message LIKE '%notification%'
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check for Scheduled Job Issues
```sql
-- If using a database for logging:
SELECT timestamp, level, logger, message 
FROM application_logs 
WHERE message LIKE '%schedule%' 
   OR message LIKE '%job%'
   OR message LIKE '%cron%'
   OR message LIKE '%enable%'
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check for URL Connection Issues
```sql
-- If using a database for logging:
SELECT timestamp, level, logger, message 
FROM application_logs 
WHERE message LIKE '%URL%' 
   OR message LIKE '%url%'
   OR message LIKE '%connect%'
   OR message LIKE '%timeout%'
   OR message LIKE '%http%'
ORDER BY timestamp DESC 
LIMIT 20;
```

## Summary

The application.properties file is the central configuration hub for the AHM LC Sales Interface Service. It provides a clean, organized way to configure all aspects of the application without changing code. By understanding this file and how it's used throughout the application, you can:

1. **Configure the application** for different environments
2. **Troubleshoot issues** by checking and adjusting configuration
3. **Understand the application's behavior** by seeing what settings control different features
4. **Make changes safely** without modifying code

Think of application.properties as the application's "control panel" - it gives you the power to adjust how the application works without having to understand all the underlying code.