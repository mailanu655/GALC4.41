# StatusMessageScheduler Required Properties

The following properties are needed for the StatusMessageScheduler to function properly:

## Direct Properties

| Property Name | Example Value | Description |
|---------------|--------------|-------------|
| `lc.status.msg.job.enable` | `TRUE` | Enables or disables the status message job |
| `status.scheduledTasks.cron.expression` | `0 */1 * * * ?` | Cron expression that defines when the scheduler runs (example: every minute) |

## Indirect Properties (Used by Dependencies)

These properties are used by components that StatusMessageScheduler depends on:

### Queue Configuration (used by ReceivingTransactionTask)

| Property Name | Example Value | Description |
|---------------|--------------|-------------|
| `sales.receiving.queue.name` | `LQ.YMS_GALC_MAP_INFO_REQ.001` | Name of the queue from which status messages are read |

### IBM MQ Configuration (used by JmsTemplate)

| Property Name | Example Value | Description |
|---------------|--------------|-------------|
| `ibm.mq.queueManager` | `MAP_TGAL` | IBM MQ queue manager name |
| `ibm.mq.channel` | `ADMIN.SVRCONN` | IBM MQ channel name |
| `ibm.mq.connName` | `qmap1mq.ham.am.honda.com(1414)` | IBM MQ connection name and port |
| `ibm.mq.user` | `was7adm` | IBM MQ username |

### Email Alert Configuration (used when errors occur)

| Property Name | Example Value | Description |
|---------------|--------------|-------------|
| `spring.mail.host` | `SMTPGTW1.ham.am.honda.com` | SMTP server for sending email alerts |
| `spring.mail.port` | `25` | SMTP server port |
| `spring.mail.from` | `No-Reply-AHM@na.honda.com` | From email address for alerts |
| `spring.mail.to` | `ambica_gawarla@na.honda.com` | Recipients of error alert emails |
| `spring.mail.subject` | `Sales Interface Service alert` | Subject line for alert emails |

### Process IDs for Different Status Types

These properties define the process IDs for different status message types that will be handled:

| Property Name | Example Value | Description |
|---------------|--------------|-------------|
| `AH-RCVD.process.id` | `AAH1RE1P00101` | Process ID for AHM Receiving status |
| `DLR-ASGN.process.id` | `AAH1SC1P00101` | Process ID for Dealer Assigned status |
| `AH-SHIP.process.id` | `AAH1DC1P00101` | Process ID for Shipment Confirmed status |
| `AH-RTN.process.id` | `AVQ1FR1P00101` | Process ID for Factory Return status |
| `PPO-ON.process.id` | `AAH1PP1P00101` | Process ID for PPO ON status |
| `PPO-OFF.process.id` | `AAH1PP1P00102` | Process ID for PPO OFF status |
| `ON-TRN.process.id` | `AAH1SC1P00103` | Process ID for Loaded to Train status |
| `SHIPPER.process.id` | `AAH1SC1P00102` | Process ID for Shipper status |
| `DLR-RCPT.process.id` | `AAH1DL1P00102` | Process ID for Dealer Receipt status |
| `DLR-RTN.process.id` | `AAH1DL1P00101` | Process ID for Dealer Return status |

## Sample Configuration

Here's a sample configuration with all the required properties:

```properties
# Enable/disable the status message job
lc.status.msg.job.enable=TRUE

# Cron expression for the status message scheduler (runs every minute)
status.scheduledTasks.cron.expression=0 */1 * * * ?

# Queue name for receiving messages
sales.receiving.queue.name=LQ.YMS_GALC_MAP_INFO_REQ.001

# IBM MQ Configuration
ibm.mq.queueManager=MAP_TGAL
ibm.mq.channel=ADMIN.SVRCONN
ibm.mq.connName=qmap1mq.ham.am.honda.com(1414)
ibm.mq.user=was7adm

# Email Configuration
spring.mail.host=SMTPGTW1.ham.am.honda.com
spring.mail.port=25
spring.mail.from=No-Reply-AHM@na.honda.com
spring.mail.to=ambica_gawarla@na.honda.com
spring.mail.subject=Sales Interface Service alert

# Process IDs for different status types
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