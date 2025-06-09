# Application.yml Documentation

## Purpose

The `application.yml` file is a specialized configuration file in the AHM LC Sales Interface Service that focuses primarily on logging settings. While the `application.properties` file handles most application configuration, `application.yml` provides a more structured way to configure the logging system. Think of it as the "control panel" specifically for the application's logging behavior.

## How It Works

The application.yml file uses YAML format (YAML Ain't Markup Language), which offers a more readable and hierarchical structure than properties files. When the application starts:

1. Spring automatically loads settings from this file alongside application.properties
2. The logging system (Logback) reads these settings to configure itself
3. Throughout the application lifecycle, all logging follows these configurations
4. Changes to logging settings only require updating this file and restarting the application

## Key Components

The application.yml file contains the following logging configuration sections:

```yaml
logging:
  config: classpath:logback.xml
  maxFileSize: 200MB
  totalSizeCap: 1GB
  maxHistory: 31
  path: /var/logs/application/
  level:
    com.honda: INFO
```

### Logging Configuration Path
```yaml
config: classpath:logback.xml
```
This setting tells Spring where to find the detailed logging configuration file. The `logback.xml` file contains the specific appender configurations, patterns, and other detailed settings.

### Log File Size Management
```yaml
maxFileSize: 200MB
totalSizeCap: 1GB
maxHistory: 31
```
These settings control how log files are managed:
- `maxFileSize`: The maximum size a single log file can reach before it's rotated
- `totalSizeCap`: The maximum total size of all archived log files
- `maxHistory`: How many days of log files to keep before deletion

### Log Storage Location
```yaml
path: /var/logs/application/
```
This setting defines where log files will be stored on the server.

### Log Levels
```yaml
level:
  com.honda: INFO
```
This setting controls the verbosity of logs for specific packages:
- `com.honda: INFO` means all classes in the com.honda package will log at INFO level or higher (WARN, ERROR)

## Interactions

The application.yml file interacts with several components in the system:

### Direct Dependencies

- **Spring Framework**: Automatically loads the YAML file at startup
- **Logback**: Uses these settings to configure logging behavior
- **logback.xml/logback-f.xml**: References properties from application.yml

### Used By

- **All Application Components**: Every class that uses logging is affected by these settings
- **Log Files**: The structure, size, and rotation of log files are controlled by these settings
- **Monitoring Systems**: Log levels affect what information is available for monitoring

## Visual Workflow

### Configuration Loading Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  application.   │────>│  Spring         │────>│  Logback        │────>│  Logger         │
│  yml            │     │  Framework      │     │  Configuration  │     │  Instances      │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
   Configuration         Loads YAML             Configures Logging      Used Throughout
      Source                                                             Application
```

### Log Level Decision Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Application    │────>│  Logger.info()  │────>│  Check Level    │────>│  Log File or    │
│  Component      │     │  Method Call    │     │  Configuration  │     │  Console        │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
   Needs to Log          Calls Log Method        Is INFO enabled?        Output if Enabled
                                                                          
```

## Data Flow

```
┌─────────────────┐                                  ┌─────────────────┐
│  application.   │                                  │  Application    │
│  yml            │                                  │  Components     │
└────────┬────────┘                                  └────────┬────────┘
         │                                                    │
         │                                                    │
         ▼                                                    ▼
┌────────────────────┐                               ┌────────────────────┐
│                    │                               │                    │
│  Logback           │◄──────────────────────────────│  Logger Calls      │
│  Configuration     │                               │  (info, error...)  │
│                    │                               │                    │
└────────┬───────────┘                               └────────────────────┘
         │                                                    
         │                                                    
         ▼                                                    
┌────────────────────┐                                        
│                    │                                        
│  Log Files         │                                        
│  & Rotation        │                                        
│                    │                                        
└────────────────────┘                                        
```

## Example Use Cases

### Example 1: How Logging Configuration Affects Application Behavior

```java
// In any application component
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;

public class ShippingTransactionTask {
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    public void execute() {
        // This will be logged because INFO level is enabled for com.honda package
        logger.info("Starting shipping transaction task");
        
        try {
            // Process shipping transactions...
            
            // This will be logged because INFO level is enabled
            logger.info("Processed {} shipping transactions", count);
        } catch (Exception ex) {
            // This will always be logged because ERROR is higher than INFO
            logger.error("Error processing shipping transactions: {}", ex.getMessage());
        }
        
        // This would NOT be logged if level was set to INFO (because DEBUG is lower)
        logger.debug("Detailed processing information: {}", detailedInfo);
    }
}
```

### Example 2: How Log File Rotation Works

When the application runs for an extended period:

1. Logs are written to `/var/logs/application/lc-product-service.log` (the main log file)
2. When this file reaches 200MB (as specified by `maxFileSize`), it's renamed to include the date and a counter
3. A new main log file is created
4. Old log files are compressed to save space
5. If total size exceeds 1GB (`totalSizeCap`), oldest files are deleted
6. Files older than 31 days (`maxHistory`) are automatically deleted

Example log files after running for several months:
```
lc-product-service.log                  # Current log file
lc-product-service_2023-06-01_0.log.gz  # Archived and compressed log file
lc-product-service_2023-06-01_1.log.gz  # Second archive from same day
lc-product-service_2023-06-02_0.log.gz  # Archive from next day
```

## Integration with Other Files

The application.yml file integrates with several other files in the codebase:

### 1. logback.xml

This is the main logging configuration file referenced by application.yml. It defines:
- Console output format
- Basic logging behavior

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<configuration>
    <property name="pattern" value="%d [%thread] %-5level %logger{36} - %msg%n" />
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${pattern}</pattern>
        </encoder>
    </appender>
    <root level="info">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

### 2. logback-f.xml

This is an extended logging configuration that directly references application.yml properties:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<configuration scan="false" scanPeriod="10 seconds">

    <property resource="application.yml" />
    <springProperty scope="context" name="spring.application.name" source="spring.application.name" defaultValue="lc-product-service" />
    <springProperty scope="context" name="logging.maxFileSize" source="logging.maxFileSize" defaultValue="200MB" />
    <springProperty scope="context" name="logging.totalSizeCap" source="logging.totalSizeCap" defaultValue="1GB" />
    <springProperty scope="context" name="logging.maxHistory" source="logging.maxHistory" defaultValue="31" />
    <property name="logging.dir" value="${LOG_PATH:-/var/logs/application/}${spring.application.name}" />
    <property name="logging.file" value="${spring.application.name}" />
    <property name="pattern" value="%d [%thread] %-5level %logger{36} - %msg%n" />

    <!-- Appender configurations -->
    <!-- ... -->
</configuration>
```

### 3. LCSalesInterfaceApplication.java

The main application class that triggers the loading of application.yml:

```java
@SpringBootApplication
@EnableJms
@ComponentScan(basePackages = { "com.honda.ahm.lc" })
@EnableCaching
@EnableScheduling
public class LCSalesInterfaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LCSalesInterfaceApplication.class, args);
    }
}
```

## Database Interactions

The application.yml file does not directly interact with any database. However, it indirectly affects database operations through:

1. **Logging of Database Operations**: The log level settings control how much information is logged about database interactions
2. **Performance Impact**: Excessive logging can impact database performance
3. **Troubleshooting**: Proper log settings are essential for debugging database issues

## Debugging Production Issues

### Common Issues and Solutions

#### 1. Missing Log Files

**Symptoms:**
- Log files not appearing in expected location
- Unable to find logs for troubleshooting

**Debugging Steps:**
1. Check the `path` setting in application.yml
2. Verify the application has write permissions to that directory
3. Check if the directory exists and create it if needed
4. Verify the application user has appropriate permissions
5. Check disk space availability

#### 2. Log Files Too Large or Too Many

**Symptoms:**
- Disk space filling up quickly
- Too many log files to manage effectively

**Debugging Steps:**
1. Check the `maxFileSize`, `totalSizeCap`, and `maxHistory` settings
2. Adjust values to appropriate levels for your environment
3. Consider implementing log rotation at the OS level as a backup
4. Set up disk space monitoring alerts

#### 3. Missing Important Log Information

**Symptoms:**
- Unable to find needed information in logs
- Logs not detailed enough for troubleshooting

**Debugging Steps:**
1. Check the log level settings for relevant packages
2. Temporarily increase log levels (e.g., from INFO to DEBUG)
3. Restart the application to apply changes
4. Remember to revert to normal levels after troubleshooting

#### 4. Too Much Log Information

**Symptoms:**
- Logs filled with unnecessary information
- Difficult to find relevant information

**Debugging Steps:**
1. Check the log level settings for relevant packages
2. Adjust levels to be more restrictive (e.g., from DEBUG to INFO)
3. Consider using more specific package-level settings
4. Restart the application to apply changes

### Visual Debugging Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ 1. Identify     │────>│ 2. Check Log    │────>│ 3. Verify       │
│    Logging Issue│     │    Files        │     │    YAML Settings │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
┌─────────────────┐     ┌─────────────────┐            ▼
│ 6. Monitor      │<────│ 5. Restart      │<────┌─────────────────┐
│    Results      │     │    Application  │     │ 4. Adjust       │
└─────────────────┘     └─────────────────┘     │    Settings     │
                                                └─────────────────┘
```

## Debugging Queries

Since the application doesn't use a traditional database, these queries are for analyzing log files rather than database tables.

### Check for Error Logs
```bash
# Using grep to search log files
grep "ERROR" /var/logs/application/lc-product-service.log

# Count errors by type
grep "ERROR" /var/logs/application/lc-product-service.log | awk -F' - ' '{print $2}' | sort | uniq -c | sort -nr
```

### Check for Queue-Related Issues
```bash
# Find all queue-related log entries
grep -i "queue\|jms\|mq" /var/logs/application/lc-product-service.log

# Find queue connection issues
grep -i "queue\|jms\|mq" /var/logs/application/lc-product-service.log | grep -i "error\|exception\|fail"
```

### Check for Scheduled Task Execution
```bash
# Find all scheduler-related log entries
grep -i "schedule\|cron\|task" /var/logs/application/lc-product-service.log

# Check if scheduled tasks are running at expected times
grep "ship.scheduledTasks.cron.expression executed" /var/logs/application/lc-product-service.log | tail -n 20
```

### Check for REST API Issues
```bash
# Find all REST API-related log entries
grep -i "rest\|http\|url\|api" /var/logs/application/lc-product-service.log

# Find REST API errors
grep -i "rest\|http\|url\|api" /var/logs/application/lc-product-service.log | grep -i "error\|exception\|fail\|timeout"
```

## Summary

The application.yml file is a specialized configuration file focused on logging settings for the AHM LC Sales Interface Service. While simpler than application.properties, it plays a crucial role in:

1. **Controlling log verbosity**: Determining what information gets logged
2. **Managing log files**: Controlling file size, rotation, and retention
3. **Directing log output**: Specifying where logs are stored

By understanding this file and how it's used throughout the application, you can:

1. **Configure logging** for different environments
2. **Troubleshoot issues** by adjusting log levels
3. **Manage disk space** by controlling log file size and retention
4. **Find relevant information** by targeting specific package log levels

Think of application.yml as the "logging control panel" - it gives you the power to adjust how the application logs information without having to understand all the underlying code.