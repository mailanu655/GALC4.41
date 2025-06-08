# ProcessCycleTimerWidget Documentation

## Table of Contents

1. [Purpose](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#purpose)
2. [How It Works](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#how-it-works)
3. [Key Components](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#key-components)
4. [Data Flow](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#data-flow)
5. [Integration with Other Components](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#integration-with-other-components)
6. [Configuration Properties](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#configuration-properties)
7. [Visual Representation](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#visual-representation)
8. [Debugging Guide](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#debugging-guide)

## Purpose

The ProcessCycleTimerWidget is a visual component in the GALC (Global Assembly Line Control) system that monitors and displays cycle time metrics for manufacturing processes. It helps production supervisors and operators track:

- Current cycle time for the active process
- Target line speed (expected cycle time)
- Running average of recent cycle times

This widget serves as a real-time performance indicator, visually alerting users when processes are taking longer than expected, which could impact production targets.

## How It Works

The ProcessCycleTimerWidget operates as a countdown/count-up timer that:

1. **Initializes** with a configurable starting value based on the process point
2. **Counts up** in one-second intervals once a product process starts
3. **Changes color** based on whether the current time is within or exceeding the target
4. **Records** completed cycle times in a rolling sample (20 most recent cycles)
5. **Calculates** and displays the running average of these samples
6. **Resets** when a new product process begins

### Workflow

1. When a product process starts:
   - The widget retrieves the target line speed for the current process point
   - It displays this target in the "Line Speed" box
   - The timer begins counting from the configured start value
2. During the process:
   - The "Current" value increases each second
   - The background color remains green while under the target time
   - The background changes to red if the time exceeds the target
3. When a process completes:
   - The current cycle time is recorded in the sample array
   - The running average is recalculated and displayed
   - The timer resets to the starting value
   - The widget is ready for the next cycle
4. If a product is cancelled:
   - The timer stops
   - The current cycle is not added to the average calculation

## Key Components

### Visual Elements

1. **Current Cycle Display**
   - Shows the elapsed time for the current process
   - Background color indicates status:
     - Green: Within target time
     - Red: Exceeding target time
   - Large, bold font for visibility from a distance
2. **Line Speed Display**
   - Shows the target cycle time for the current process
   - Retrieved from configuration based on process point and line ID
   - Serves as the reference value for performance measurement
3. **Running Average Display**
   - Shows the average cycle time of recent processes (up to 20 samples)
   - Background color indicates overall performance:
     - Green: Average is within target
     - Red: Average is exceeding target

### Controller Logic

The Java controller (ProcessCycleTimerWidget.java) handles:

1. **Timer Management**
   - Creates and controls a JavaFX Timeline for second-by-second updates
   - Manages starting, stopping, and resetting the timer
2. **Cycle Recording**
   - Maintains an array of the 20 most recent cycle times
   - Implements a circular buffer pattern for continuous recording
3. **Average Calculation**
   - Computes the running average of valid cycle times
   - Updates the display with the calculated value
4. **Visual Feedback**
   - Changes rectangle fill colors based on performance thresholds
   - Updates text values with formatted time values
5. **Configuration Integration**
   - Retrieves line speed targets from the property service
   - Supports line-specific configurations with fallback to defaults

## Data Flow

### Input Data

1. **Process Point Configuration**
   - Target line speeds for different production lines
   - Starting count value for the timer
2. **Product Events**
   - Product start events trigger the timer
   - Product finish events stop the timer and record the cycle
   - Product cancellation events stop the timer without recording

### Internal Data

1. **Cycle Time Array**
   - Stores the 20 most recent cycle times
   - Initialized with `Integer.MIN_VALUE` to identify unused slots
2. **Current Index**
   - Tracks the position in the cycle time array for the next recording
   - Implements circular buffer behavior
3. **Current Cycle Time**
   - Tracks the elapsed seconds for the active process
   - Updated every second by the timer

### Output Data

1. **Visual Displays**
   - Current cycle time (seconds)
   - Target line speed (seconds)
   - Running average (seconds)
2. **Status Indicators**
   - Color-coded backgrounds for performance feedback

## Integration with Other Components

### Event System Integration

The widget registers with the EventBusUtil to receive system events:

```java
EventBusUtil.register(this);
```

### Product Controller Integration

The widget extends AbstractWidget and receives a ProductController in its constructor:

```java
public ProcessCycleTimerWidget(ProductController productController) {
    super(ViewId.PROCESS_CYCLETIMER_WIDGET, productController);
    // ...
}
```

### Process Point Integration

The widget retrieves the current process point from the application context:

```java
ProcessPoint processPoint = ClientMainFx.getInstance().getApplicationContext().getProcessPoint();
```

### Property Service Integration

The widget uses the PropertyService to retrieve configuration values:



```java
lineSpeedMap = PropertyService.getPropertyBean(PDDAPropertyBean.class, processPointId).getLineSpeed(Integer.class);
```

## Configuration Properties

The widget relies on several configuration properties stored in the PDDAPropertyBean:

1. **Line Speed Configuration**

   - Maps line IDs to their target cycle times in seconds

   - Includes a "DEFAULT" entry as a fallback

   - Example configuration:

     ```
     lineSpeed.LINE1=60
     lineSpeed.LINE2=75
     lineSpeed.DEFAULT=90
     ```

2. **Count Start Second**

   - Defines the initial value for the timer

   - Retrieved using:

     ```java
     PropertyService.getPropertyBean(PDDAPropertyBean.class, processPointId).getCountStartSecond();
     ```

   - Example configuration:

     ```
     countStartSecond=0
     ```

## Visual Representation

```
+------------------------------------------+
|         Process Cycle Timer              |
+------------------------------------------+
|                                          |
|  +----------+  +----------+  +----------+|
|  |          |  |          |  |          ||
|  |    42    |  |    60    |  |    58    ||
|  |          |  |          |  |          ||
|  |          |  |          |  |          ||
|  +----------+  +----------+  +----------+|
|    Current      Line Speed   Running Avg |
|                                          |
+------------------------------------------+
```

### Color Coding Logic

```
IF currentCycleTime <= referenceLineSpeed THEN
    currentCycleRect.setFill(Color.GREEN)
ELSE
    currentCycleRect.setFill(Color.RED)
END IF

IF mean <= referenceLineSpeed THEN
    runningAveRect.setFill(Color.GREEN)
ELSE
    runningAveRect.setFill(Color.RED)
END IF
```

## Debugging Guide

### Common Issues and Solutions

#### 1. Timer Not Starting

- **Symptoms**: Current cycle value remains at initial value
- Possible Causes:
  - Product start event not received
  - Exception during timer initialization
- Checks:
  - Verify product controller is properly connected
  - Check for exceptions in application logs
  - Confirm event bus registration is successful

#### 2. Incorrect Line Speed Values

- **Symptoms**: Target time seems wrong for the process
- Possible Causes:
  - Missing configuration for the current line
  - Property service not returning expected values
- Checks:
  - Verify line speed configuration in properties
  - Check process point ID is correct
  - Confirm DEFAULT line speed is configured as fallback

#### 3. Running Average Not Updating

- **Symptoms**: Average value doesn't change after cycles
- Possible Causes:
  - Cycle times not being recorded
  - Calculation logic error
- Checks:
  - Add logging to verify cycle recording
  - Check cycleIndex incrementation
  - Verify calculateMean() is being called

### Debugging Steps

1. **Enable Detailed Logging**

   ```properties
   logging.level.com.honda.galc.client.dc.view.widget=DEBUG
   ```

2. **Add Diagnostic Output** Add temporary logging in key methods:

   ```java
   System.out.println("Process started: " + productModel.getProductId());
   System.out.println("Line speed: " + this.referenceLineSpeed);
   System.out.println("Cycle recorded: " + currentCycleTime);
   ```

3. **Check Configuration** Verify property values are correctly set:

   ```java
   Map<String, Integer> speeds = PropertyService.getPropertyBean(PDDAPropertyBean.class, processPointId).getLineSpeed(Integer.class);
   System.out.println("Available line speeds: " + speeds);
   System.out.println("Current line: " + lineId);
   System.out.println("Count start: " + countStartSecond);
   ```

4. **Verify Event Flow** Add breakpoints or logging in event methods:

   - processProductStarted()
   - processProductFinished()
   - processProductCancelled()

### Example Diagnostic Queries

To check configuration in the database:

```sql
-- Check process point configuration
SELECT process_point_id, property_name, property_value
FROM process_point_properties
WHERE process_point_id = ? AND property_name LIKE 'lineSpeed%';

-- Check default configuration
SELECT property_name, property_value
FROM system_properties
WHERE property_name LIKE 'lineSpeed%';
```

To check cycle time history:

```sql
-- Check recent cycle times for a process point
SELECT product_id, process_point_id, start_time, end_time, 
       TIMESTAMPDIFF(SECOND, start_time, end_time) as cycle_time
FROM process_operations
WHERE process_point_id = ?
ORDER BY end_time DESC
LIMIT 20;
```

------

This widget plays a crucial role in monitoring production efficiency by providing real-time visual feedback on process cycle times. By comparing current and average cycle times against target values, it helps operators and supervisors quickly identify when processes are taking longer than expected, allowing for timely interventions to maintain production flow.