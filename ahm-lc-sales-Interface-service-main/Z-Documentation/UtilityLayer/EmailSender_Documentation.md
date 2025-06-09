# EmailSender Documentation

## Purpose
The EmailSender class is a utility component responsible for sending error notifications via email when issues occur in the AHM LC Sales Interface Service. It provides a simple way for the application to alert system administrators about problems that require attention, ensuring that critical errors don't go unnoticed.

## How It Works
The EmailSender works like a digital messenger that delivers error reports to the right people. When something goes wrong in the application, it collects all the error details and sends them in an email to the designated recipients.

### Step-by-Step Process
1. **Error Collection**: When an error occurs in any part of the application, the error details are collected in a list.
2. **Email Creation**: The EmailSender creates a new email message with:
   - A sender address (who the email is from)
   - Recipient addresses (who should receive the notification)
   - A subject line (what the email is about)
   - The error details in the body of the email
3. **Logging**: Before sending, it logs the email details for tracking purposes
4. **Delivery**: The email is sent through the configured mail server

## Key Components

### Fields
- `logger`: Records important information about the email sending process
- `mailSender`: The Spring component that handles the actual email delivery
- `propertyUtil`: Provides access to email configuration settings from the application properties

### Methods
- `sendEmail(String messageId, List<String> errorList)`: The main method that creates and sends an error notification email
  - `messageId`: Identifies which part of the application encountered the error
  - `errorList`: Contains the detailed error messages to be included in the email

## Interactions
The EmailSender interacts with several components in the system:

### Direct Dependencies
- **JavaMailSender**: Spring's email sending interface that handles the technical aspects of email delivery
- **PropertyUtil**: Provides configuration values like email addresses and subject lines from the application properties

### Used By
- **ReceivingTransactionTask**: Uses EmailSender to notify about errors when processing incoming messages
- **ShippingTransactionTask**: Uses EmailSender to notify about errors when processing outgoing shipment data

## Configuration
The EmailSender is configured through the application.properties file with these settings:

```properties
spring.mail.host=SMTPGTW1.ham.am.honda.com
spring.mail.port=25
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
spring.mail.from=No-Reply-AHM@na.honda.com
spring.mail.to=ambica_gawarla@na.honda.com
spring.mail.subject=Sales Interface Service alert
```

## Visual Workflow

```
┌─────────────────────┐     ┌─────────────────┐     ┌───────────────────┐
│ Application Service │     │   EmailSender   │     │    Mail Server    │
│  (Task Layer)       │     │  (Utility Layer)│     │                   │
└─────────┬───────────┘     └────────┬────────┘     └─────────┬─────────┘
          │                          │                        │
          │                          │                        │
          │ 1. Detects Error         │                        │
          │                          │                        │
          │ 2. Collects Error Details│                        │
          │                          │                        │
          │ 3. Calls sendEmail()     │                        │
          │─────────────────────────>│                        │
          │                          │                        │
          │                          │ 4. Creates Email       │
          │                          │    Message             │
          │                          │                        │
          │                          │ 5. Logs Email Details  │
          │                          │                        │
          │                          │ 6. Sends Email         │
          │                          │─────────────────────────>
          │                          │                        │
          │                          │ 7. Email Delivered     │
          │                          │<─────────────────────────
          │                          │                        │
          │ 8. Process Continues     │                        │
          │<─────────────────────────│                        │
          │                          │                        │
```

## Data Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Error Occurs   │────>│  Error Details  │────>│  Email Message  │────>│ System Admin    │
│  in Application │     │  Collected      │     │  Created & Sent │     │ Inbox           │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
```

## Example Use Case

Let's say the application tries to process a shipping message but encounters an error because the VIN number is missing:

```java
// In ShippingTransactionTask.java
try {
    // Process shipping data
    if (StringUtils.isBlank(productId)) {
        String msg = " No ProductId in message, received -" + message;
        logger.error(msg);
        errorMessages.add(msg);
    }
    // More processing...
} catch (Exception e) {
    logger.error(e.getMessage());
    errorMessages.add(e.getMessage());
}

// If errors occurred, send email notification
if (!errorMessages.isEmpty()) {
    emailSender.sendEmail(getClass().getName() + " : ", errorMessages);
}
```

The system administrator would receive an email with:
- **From**: No-Reply-AHM@na.honda.com
- **To**: ambica_gawarla@na.honda.com
- **Subject**: Sales Interface Service alert
- **Body**:
  ```
  com.honda.ahm.lc.task.ShippingTransactionTask : 
  No ProductId in message, received - {json message content}
  ```

## Debugging Production Issues

### Common Issues and Solutions

#### 1. Emails Not Being Sent

**Symptoms:**
- Errors occur but no email notifications are received
- No email-related errors in logs

**Debugging Steps:**
1. Check mail server connectivity:
   ```sql
   -- No direct database query needed
   -- Instead, check application logs for mail connectivity issues
   ```
2. Verify mail configuration in application.properties
3. Test SMTP server connection from the application server:
   ```bash
   telnet SMTPGTW1.ham.am.honda.com 25
   ```

#### 2. Incorrect Email Recipients

**Symptoms:**
- Error notifications not reaching the right people

**Debugging Steps:**
1. Check the current email configuration:
   ```sql
   -- No direct database query needed
   -- Check application.properties file
   ```
2. Update the spring.mail.to property in application.properties

#### 3. Missing Error Details

**Symptoms:**
- Email notifications received but with incomplete error information

**Debugging Steps:**
1. Check the application logs for the full error details:
   ```sql
   -- If using a database for logging:
   SELECT message, timestamp, level 
   FROM application_logs 
   WHERE level = 'ERROR' 
   AND timestamp > (CURRENT_TIMESTAMP - INTERVAL '1 DAY')
   ORDER BY timestamp DESC;
   ```
2. Verify that all exceptions are properly caught and added to the errorMessages list

### Visual Debugging Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ 1. Identify     │────>│ 2. Check        │────>│ 3. Verify       │
│    Issue        │     │    Logs         │     │    Config       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
┌─────────────────┐     ┌─────────────────┐            ▼
│ 6. Implement    │<────│ 5. Develop      │<────┌─────────────────┐
│    Fix          │     │    Solution     │     │ 4. Test Mail    │
└─────────────────┘     └─────────────────┘     │    Server       │
                                                └─────────────────┘
```

## Debugging Queries

Since EmailSender doesn't directly interact with the database, debugging focuses on application logs and configuration:

### Check for Email Sending Attempts
```sql
-- If using a database for logging:
SELECT timestamp, message 
FROM application_logs 
WHERE message LIKE 'Sending email: From:%' 
ORDER BY timestamp DESC 
LIMIT 20;
```

### Check for Email Configuration Issues
```sql
-- If using a database for configuration (most implementations use properties files):
SELECT property_key, property_value 
FROM application_properties 
WHERE property_key LIKE 'spring.mail.%';
```

### Check for Recent Errors
```sql
-- If using a database for logging:
SELECT timestamp, logger_name, level, message 
FROM application_logs 
WHERE level = 'ERROR' 
AND timestamp > (CURRENT_TIMESTAMP - INTERVAL '1 HOUR')
ORDER BY timestamp DESC;
```

## Summary

The EmailSender is a simple but critical component that ensures system administrators are promptly notified when errors occur in the AHM LC Sales Interface Service. By sending detailed error notifications via email, it helps maintain system reliability and enables quick response to issues.

Think of EmailSender as the system's alarm bell - it doesn't fix problems itself, but it makes sure the right people know when something needs attention.