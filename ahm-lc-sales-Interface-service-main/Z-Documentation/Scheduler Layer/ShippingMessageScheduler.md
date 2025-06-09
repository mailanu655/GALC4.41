# ShippingMessageScheduler Documentation

## Overview

The `ShippingMessageScheduler` is a critical component in the Honda AHM LC Sales Interface Service that automates the process of sending shipping messages from GALC (Global Assembly Line Control) to YMS (Yard Management System). It uses Spring's scheduling capabilities to periodically check for and process vehicles that are ready for shipping.

## File Information

- **File Path**: `src/main/java/com/honda/ahm/lc/scheduler/ShippingMessageScheduler.java`
- **Package**: `com.honda.ahm.lc.scheduler`

## Purpose

This scheduler serves as an automated trigger for the shipping message process. Its primary responsibilities are:

1. Running at configured intervals (defined by a cron expression)
2. Checking if the shipping job is enabled in the application configuration
3. If enabled, triggering the shipping transaction task to process messages

This automation ensures that vehicle shipping information is regularly sent from GALC to YMS without requiring manual intervention.

## Code Breakdown

Let's analyze the file line by line:

```java
package com.honda.ahm.lc.scheduler;
```
- **Line 1:** Defines the package name for this class, following the standard Java package naming convention.

```java
import java.text.SimpleDateFormat;
import java.util.Date;

import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.honda.ahm.lc.task.ShippingTransactionTask;
import com.honda.ahm.lc.util.PropertyUtil;
```
- **Lines 3-13:** Import statements for required classes:
  - Java utilities for date formatting and handling
  - Honda AHM LC custom logging framework
  - Spring Framework annotations for dependency injection, scheduling, and component registration
  - Honda AHM LC task for shipping transaction processing
  - Honda AHM LC utility for property management

```java
@Component
public class ShippingMessageScheduler {
```
- **Lines 15-16:** Class declaration with `@Component` annotation:
  - `@Component`: Marks the class as a Spring component, making it eligible for auto-detection and dependency injection

```java
protected Logger logger = LoggerFactory.getLogger(getClass());
```
- **Line 18:** Initializes a logger for this class to record information, warnings, and errors.

```java
@Autowired
ShippingTransactionTask shippingTransactionTask;

@Autowired
PropertyUtil propertyUtil;
```
- **Lines 20-24:** Dependency injection of required components:
  - `ShippingTransactionTask`: The task that performs the actual shipping message processing
  - `PropertyUtil`: Provides access to application properties, including scheduler configuration

```java
private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
private boolean isEnabled = false;
```
- **Lines 27-28:** Instance variables:
  - `sdf`: A date formatter for logging timestamps
  - `isEnabled`: A flag to track whether the job is enabled (initialized to false)

```java
@Scheduled(cron = "${ship.scheduledTasks.cron.expression}")
public void cronJobSch() {
```
- **Lines 30-31:** Method declaration with `@Scheduled` annotation:
  - `@Scheduled`: Configures the method to be executed periodically
  - `cron = "${ship.scheduledTasks.cron.expression}"`: Uses a cron expression from application properties to define the execution schedule
  - The default value in application.properties is `0 */1 * * * ?`, which means "every minute"

```java
Date now = new Date();
String strDate = sdf.format(now);

logger.info("ship.scheduledTasks.cron.expression executed at {}", strDate);
```
- **Lines 33-36:** Logging the execution time:
  - Creates a new Date object representing the current time
  - Formats the date using the SimpleDateFormat
  - Logs the execution time with an informational message

```java
isEnabled = propertyUtil.shippingJobEnable();
```
- **Line 38:** Checks if the shipping job is enabled:
  - Calls `propertyUtil.shippingJobEnable()` which reads the `lc.ship.msg.job.enable` property
  - The default value in application.properties is `TRUE`

```java
if (isEnabled) {
    shippingTransactionTask.execute();
} else {
    logger.info("Scheduled Jobs Not Enabled - Property JOBS_ENABLED = " + isEnabled);
}
```
- **Lines 41-45:** Conditional execution of the shipping task:
  - If the job is enabled, calls `shippingTransactionTask.execute()` to process shipping messages
  - If the job is disabled, logs an informational message

## Integration with Other Components

The `ShippingMessageScheduler` integrates with several other components in the codebase:

### 1. Application Entry Point

- **LCSalesInterfaceApplication.java**: The main application class enables scheduling with the `@EnableScheduling` annotation, which allows the `@Scheduled` annotation in this class to function.

### 2. Task Layer

- **ShippingTransactionTask.java**: The scheduler calls the `execute()` method of this task, which:
  - Reads messages from the GALC queue
  - Processes the messages to extract vehicle information
  - Formats the data into shipping messages
  - Sends the messages to the YMS queue
  - Updates GALC with shipping status

### 3. Utility Layer

- **PropertyUtil.java**: The scheduler uses this utility to:
  - Check if the shipping job is enabled via `shippingJobEnable()`
  - This method reads the `lc.ship.msg.job.enable` property from application.properties

### 4. Configuration

- **application.properties**: Contains the configuration for the scheduler:
  - `ship.scheduledTasks.cron.expression=0 */1 * * * ?` - Sets the schedule to run every minute
  - `lc.ship.msg.job.enable=TRUE` - Enables or disables the job

## Data Flow

The `ShippingMessageScheduler` initiates a data flow that moves information from GALC to YMS:

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     ShippingMessageScheduler                            │
│                     (Runs on cron schedule)                             │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     ShippingTransactionTask.execute()                   │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Read message from GALC Queue                        │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Process message data                                │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Send formatted message to YMS Queue                 │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                     Update GALC with shipping status                    │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## Database Interactions

The `ShippingMessageScheduler` itself doesn't directly interact with databases. However, it initiates a process through `ShippingTransactionTask` that leads to indirect database interactions:

### Indirect Database Interactions

When the scheduler triggers `shippingTransactionTask.execute()`, the following database interactions occur:

1. **Reading from GALC**:
   - The task reads messages from the GALC queue
   - It retrieves shipping transaction data from GALC via REST API calls

2. **Writing to GALC**:
   - After successfully sending a message to YMS, the task updates the shipping status in GALC
   - It marks the transaction as sent by setting the send flag to 'Y'
   - It updates the shipping status value from 0 to 1 (indicating "already sent to AH")

These interactions are handled by service classes that make REST API calls to the GALC system:

- `ShippingStatusService.findByProductId()`: Retrieves shipping status
- `ShippingStatusService.saveShippingStatus()`: Updates shipping status
- `ShippingTransactionService.get50ATransactionVin()`: Retrieves shipping transactions
- `ShippingTransactionService.saveShippingTransaction()`: Updates shipping transactions

## Scheduling Details

The scheduler uses Spring's `@Scheduled` annotation with a cron expression to determine when to run:

```java
@Scheduled(cron = "${ship.scheduledTasks.cron.expression}")
```

The cron expression is configured in application.properties:

```
ship.scheduledTasks.cron.expression=0 */1 * * * ?
```

This expression follows the format:

```
second minute hour day-of-month month day-of-week
```

The value `0 */1 * * * ?` means:
- `0`: At 0 seconds
- `*/1`: Every 1 minute
- `*`: Every hour
- `*`: Every day of the month
- `*`: Every month
- `?`: No specific day of the week

So the scheduler runs at the start of every minute.

## Error Handling

The scheduler itself doesn't implement explicit error handling. However, the `ShippingTransactionTask` it calls has comprehensive error handling:

1. It catches exceptions during processing
2. It logs error messages
3. It collects error messages in a list
4. If any errors occur, it sends an email notification with the error details

This ensures that any issues during the shipping process are properly logged and reported.

## Parallel Scheduler: StatusMessageScheduler

The `ShippingMessageScheduler` has a parallel component called `StatusMessageScheduler` that follows the same pattern but handles the reverse flow (YMS to GALC):

- It runs on a schedule defined by `status.scheduledTasks.cron.expression`
- It checks if the status job is enabled via `propertyUtil.statusJobEnable()`
- If enabled, it calls `receivingTransactionTask.execute()` to process status messages from YMS

Together, these two schedulers form a complete bidirectional communication system between GALC and YMS.

## Summary

The `ShippingMessageScheduler` is a critical automation component in the Honda AHM LC Sales Interface Service. It periodically triggers the process of sending shipping messages from GALC to YMS, ensuring that vehicle shipping information is regularly updated without manual intervention.

While the scheduler itself is relatively simple, it plays a vital role in the overall system by initiating a complex data flow that involves message queuing, data transformation, and REST API interactions. Its counterpart, the `StatusMessageScheduler`, completes the bidirectional communication by handling the reverse flow of status updates from YMS to GALC.

Together, these schedulers ensure that Honda's manufacturing and sales systems remain synchronized with up-to-date vehicle information.