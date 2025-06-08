# GALC Client UI Components Documentation

## Table of Contents

1. [Introduction](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#introduction)
2. [BBScales Module](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#bbscales-module)
3. [Unit Navigator Module](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#unit-navigator-module)
4. [Production Planning and Scheduling Modules](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#production-planning-and-scheduling-modules)
5. [Calendar and Date Picker Module](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#calendar-and-date-picker-module)
6. [Unit Part List Module](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#unit-part-list-module)
7. [Operation List Module](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#operation-list-module)
8. [Integration and Data Flow](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#integration-and-data-flow)
9. [Debugging Guide](vscode-webview://0iqalb0hiu5soandfpn97o5la98127ebaadlfq6mlcvadp6p6q0l/index.html?id=56800f41-b32a-4b37-8b24-1b3db0d7e7a7&origin=ebdecfa7-7dc8-4701-a483-276c57579b1f&swVersion=4&extensionId=ZencoderAI.zencoder&platform=electron&vscode-resource-base-authority=vscode-resource.vscode-cdn.net&parentOrigin=vscode-file%3A%2F%2Fvscode-app&purpose=webviewView#debugging-guide)

## Introduction

The GALC (Global Assembly Line Control) client application is a JavaFX-based system used in Honda manufacturing facilities to monitor and control various aspects of the vehicle assembly process. The UI components analyzed in this document are part of the client-side interface that operators and supervisors use to interact with the system.

These components are built using JavaFX, a modern UI framework for Java applications that provides rich graphical capabilities and follows an MVC (Model-View-Controller) architecture pattern.

## BBScales Module

### Purpose

The BBScales module provides a visual interface for monitoring and recording vehicle weight measurements at different points (front, rear, cross) during the manufacturing process. This helps ensure vehicles meet weight specifications and have proper weight distribution.

### Files

- **BBScalesFXMLPane.fxml**: Defines the UI layout for the scales interface
- **bbscales.css**: Provides styling for the BBScales interface

### How It Works

#### UI Layout

The BBScalesFXMLPane.fxml creates a grid-based interface that displays:

- VIN input field for vehicle identification
- Weight measurements for different points on the vehicle:
  - Left Front (LFrt)
  - Right Front (RFrt)
  - Left Rear (LRr)
  - Right Rear (RRr)
  - Left Cross (LCr)
  - Right Cross (RCr)
  - Cross Difference (Cr_diff)
  - Total Weight (Tot_val)
- Min/Max acceptable values for each measurement point
- Vehicle image in the center
- Mode and Clear buttons for operation control

#### Key Components

1. **LoggedTextField Components**:
   - Used for displaying weight values and min/max ranges
   - The "logged" prefix indicates these components track user interactions for audit purposes
   - Key fields include:
     - `vin`: For entering the vehicle identification number
     - `LFrt_val`, `RFrt_val`, `LRr_val`, `RRr_val`: For displaying corner weights
     - `LCr_val`, `RCr_val`: For cross weights
     - `Tot_val`: For total vehicle weight
2. **Visual Indicators**:
   - Vehicle image (`cc_car_img`) provides visual context
   - Weight values are prominently displayed with larger font sizes
   - Min/Max values show acceptable ranges for each measurement
3. **Control Buttons**:
   - `cMode`: Toggles between different operational modes (likely between manual and automatic reading)
   - `clear`: Resets the display or clears current readings

#### Data Flow

1. VIN is entered or scanned to identify the vehicle
2. Weight data is received from scale equipment through backend services
3. Values are displayed and compared against min/max specifications
4. Visual indicators show whether weights are within acceptable ranges
5. Data is logged to the system database for quality control purposes

### CSS Styling

The bbscales.css file defines styles for:

- Font sizes and weights for different text elements
- Color schemes for labels and values
- Button appearances
- Text field formatting

### Database Interactions

The BBScales module likely interacts with:

- Vehicle specification database (for min/max weight values)
- Production tracking database (to associate weights with specific VINs)
- Quality control database (to log weight measurements)

Typical database queries would include:

```sql
-- Fetch weight specifications for a model
SELECT model_code, lfront_min, lfront_max, rfront_min, rfront_max, 
       lrear_min, lrear_max, rrear_min, rrear_max,
       lcross_min, lcross_max, rcross_min, rcross_max,
       total_min, total_max
FROM vehicle_weight_specs
WHERE model_code = ?

-- Log weight measurements
INSERT INTO vehicle_weight_measurements
(vin, timestamp, lfront, rfront, lrear, rrear, lcross, rcross, total, operator_id)
VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?)
```

### Visual Representation

```
+---------------------------------------------------------------------+
|                           BBScales Interface                         |
+---------------------------------------------------------------------+
|                                                                     |
| VIN: [____________]                                                 |
|                                                                     |
| LFrt Max: [___]  [LFrt Value]    +-------------+    [RFrt Value]  RFrt Max: [___] |
| LFrt Min: [___]                  |             |                  RFrt Min: [___] |
|                                  |   Vehicle   |                                 |
|                                  |    Image    |                                 |
|                                  |             |                                 |
| LRr Max: [___]   [LRr Value]     +-------------+    [RRr Value]   RRr Max: [___] |
| LRr Min: [___]                                                     RRr Min: [___] |
|                                                                                 |
|                                                                                 |
| Left Cross: [___]    Cross Diff: [___]    Right Cross: [___]                   |
| min/max: [___/___]   min/max: [___/___]   min/max: [___/___]                   |
|                                                                                 |
|                      Total: [___]                                               |
|                      min/max: [___/___]                                         |
|                                                                                 |
| [Mode]                                                              [Clear]     |
+---------------------------------------------------------------------+
```

### Debugging Steps

1. **Check Scale Connectivity**:
   - Verify network connection to scale equipment
   - Check device status in system logs
2. **Verify Data Reception**:
   - Monitor incoming data packets from scales
   - Check for data format issues or communication errors
3. **Validate Specifications**:
   - Confirm correct min/max values are loaded for the vehicle model
   - Verify VIN is correctly associated with the expected model
4. **UI Rendering Issues**:
   - Check CSS loading and application
   - Verify FXML components are properly initialized

## Unit Navigator Module

### Purpose

The Unit Navigator provides a searchable, scrollable interface for navigating through vehicle units in the production system. It allows users to quickly find and select specific vehicles by VIN, lot number, or other criteria.

### Files

- **UnitNavigatorControl.fxml**: Standard navigator interface
- **UnitNavigatorRepairClientControl.fxml**: Specialized navigator for repair operations
- **UnitNavigatorWidget.css**: Styling for navigator components

### How It Works

#### UI Layout

Both navigator interfaces share a similar structure:

- A vertical list of items (likely vehicles or units)
- A scrollbar for navigation
- A "fast travel map" for quick jumping to different sections
- Search functionality (different implementations in each version)

#### Key Components

1. **Standard Navigator (UnitNavigatorControl.fxml)**:
   - `naviListVBox`: Contains the list of navigable items
   - `searchTextField`: Text input for search criteria
   - `searchButton`: Triggers the search operation
   - `scrollbar`: Standard scrollbar for list navigation
   - `fastTravelMap`: Canvas element for quick navigation to different sections
2. **Repair Navigator (UnitNavigatorRepairClientControl.fxml)**:
   - Similar structure but with a different search approach
   - Uses a `searchCriteriaButton` instead of a text field
   - Includes a title label identifying it as "Repair Client"

#### Styling (UnitNavigatorWidget.css)

- Defines styles for different cell states:
  - Regular cells (`nav-cell`)
  - Selected cells (`nav-cell-selected`)
  - Highlighted cells (`nav-cell-highlighted`)
  - Selected and highlighted cells (`nav-cell-selected-and-highlighted`)
- Customizes scrollbar appearance (hiding horizontal scrollbars)
- Defines button and label styles

#### Event Handling

Both navigators implement several event handlers:

- `onScroll`: Handles scrolling events in the list
- `mapClick`: Processes clicks on the fast travel map
- `searchButton`/`searchCriteriaButton`: Triggers search operations
- `onSearchKeyReleased`: Provides real-time search as the user types (in standard navigator)

### Data Flow

1. Vehicle data is loaded from the database into the navigator list
2. User can scroll through items or use search to filter the list
3. Selecting an item likely triggers loading of detailed information in other panels
4. The fast travel map provides a visual representation of where the current view is within the full dataset

### Database Interactions

The Unit Navigator likely queries:

- Vehicle production database
- Repair order database (for the repair client version)

Example queries:

```sql
-- Standard navigator might use:
SELECT vin, model_code, lot_number, build_date, current_station
FROM production_units
ORDER BY sequence_number

-- Repair client might use:
SELECT vin, model_code, repair_order_id, defect_code, status
FROM repair_orders
WHERE status = 'OPEN'
ORDER BY priority, create_date
```

### Visual Representation

```
+------------------------------------------+
|          Unit Navigator Interface        |
+------------------------------------------+
| [Search Text Field] [Find Button]        |
+---------------------------+------+-------+
|                           |      |       |
| Item 1                    |      |       |
|                           |      |       |
| Item 2                    |      | F     |
|                           |      | a     |
| Item 3                    |      | s     |
|                           |      | t     |
| Item 4                    |      |       |
|                           |      | M     |
| Item 5                    |      | a     |
|                           |      | p     |
| ...                       |      |       |
|                           |      |       |
+---------------------------+------+-------+
```

### Debugging Steps

1. **Data Loading Issues**:
   - Check database connectivity
   - Verify query execution and result sets
   - Look for exceptions in data transformation logic
2. **Search Functionality**:
   - Verify search criteria parsing
   - Check filter implementation
   - Test with various search inputs
3. **UI Rendering**:
   - Inspect list cell rendering
   - Verify scrollbar functionality
   - Check fast travel map drawing

## Production Planning and Scheduling Modules

### Purpose

These modules provide interfaces for viewing and managing production schedules at different levels of detail (lot-based and unit-based views). They help production managers and supervisors track progress, identify issues, and manage the production flow.

### Files

- **ProductionPlanWidgetFX.css**: Styling for production plan overview
- **ProductionScheduleLotViewWidgetFX.css**: Styling for lot-based schedule view
- **ProductionScheduleUnitViewWidgetFX.css**: Styling for unit-based schedule view

### How They Work

#### Production Plan Widget

This widget likely provides a high-level overview of production metrics with visual indicators:

- Color-coded indicators (red, green, blue boxes)
- Large, bold text for important values
- Clear labeling for different metrics

The CSS defines:

- Text styles for labels and values
- Color schemes for status indicators (red for issues, green for on-target)
- Layout properties for the widget container

#### Lot View Widget

Provides a tabular view of production lots with:

- Customized table styling
- Row highlighting for selected or special status lots
- Right-aligned number formatting

Key features in the CSS:

- Hides horizontal scrollbars for cleaner appearance
- Defines the `willhighlight` class for emphasizing specific rows
- Sets number alignment to right-justified

#### Unit View Widget

Similar to the lot view but focused on individual units with:

- Status-based row highlighting
- Special formatting for the current VIN
- Indicators for MTOC (Model Type Option Code) changes

The CSS defines classes for:

- `currentvin`: Highlights the currently selected vehicle (yellow)
- `mtocchange`: Indicates a change in model type (green)
- `normal`: Standard row styling
- `numberalignment`: Right-aligns numeric values

### Data Flow

1. Production plan data is retrieved from planning systems
2. Schedule data is organized into lot and unit views
3. Status information is applied to highlight special conditions
4. User interactions (selections, filters) update the displayed data

### Database Interactions

These modules likely interact with:

- Production planning database
- Manufacturing execution system
- Vehicle tracking system

Example queries:

```sql
-- Production plan summary
SELECT plan_date, target_units, actual_units, variance,
       start_time, end_time, efficiency_rate
FROM production_daily_plan
WHERE plan_date = CURRENT_DATE

-- Lot view data
SELECT lot_number, model_code, quantity, scheduled_start,
       actual_start, status, completion_percentage
FROM production_lots
WHERE scheduled_date BETWEEN ? AND ?
ORDER BY sequence_number

-- Unit view data
SELECT vin, model_code, lot_number, sequence_number,
       current_station, status, scheduled_completion
FROM production_units
WHERE lot_number = ?
ORDER BY sequence_number
```

### Visual Representation

```
Production Plan Widget:
+------------------------------------------+
|              Production Plan             |
+------------------------------------------+
| Plan:      [120]     | Actual:    [115]  |
| Variance:  [-5]      | Efficiency: [96%] |
| Start:     [07:00]   | End:       [15:30]|
+------------------------------------------+
| [Status Indicator: Green]                |
+------------------------------------------+

Lot View:
+----------------------------------------------------------+
| Lot #  | Model | Quantity | Start Time | Status | % Done |
+----------------------------------------------------------+
| L12345 | CRV   | 25       | 08:00      | Active | 45%    |
| L12346 | CIVIC | 30       | 10:30      | Pending| 0%     |
| L12347 | ACCORD| 20       | 13:00      | Pending| 0%     |
+----------------------------------------------------------+

Unit View:
+----------------------------------------------------------+
| VIN           | Model | Sequence | Station  | Status     |
+----------------------------------------------------------+
| 1HGCM82633A123456 | ACCORD | 1        | PAINT    | COMPLETE   |
| 1HGCM82633A123457 | ACCORD | 2        | ASSEMBLY | IN PROGRESS|
| 1HGCM82633A123458 | CIVIC  | 3        | PREP     | PENDING    |
+----------------------------------------------------------+
```

### Debugging Steps

1. **Data Retrieval Issues**:
   - Check database connection status
   - Verify query execution times and results
   - Look for data format inconsistencies
2. **Display Problems**:
   - Inspect CSS application to table elements
   - Check cell factory implementations
   - Verify highlighting logic
3. **Performance Concerns**:
   - Monitor data loading times
   - Check for excessive UI updates
   - Look for inefficient queries or rendering

## Calendar and Date Picker Module

### Purpose

The CalendarDateTimePickerFxControl provides a custom date and time selection interface for scheduling and filtering operations throughout the application.

### Files

- **CalendarDateTimePickerFxControl.css**: Styling for the calendar widget

### How It Works

This component creates a calendar-style date picker with:

- Month view with day cells
- Week numbers
- Today highlighting
- Selected date highlighting
- Inactive days (likely from other months) with subdued styling

#### Key Styling Elements

- `calendar-date-select`: Base class for the control
- `weekday-cell`: Styling for day-of-week headers
- `week-of-year-cell`: Styling for week number indicators
- `calendar-cell`: Standard day cells
- `calendar-cell-selected`: Currently selected date
- `calendar-cell-inactive`: Days from adjacent months
- `calendar-cell-today`: Current date highlighting

#### Visual States

The CSS defines different visual states for calendar cells:

- Normal state: Light blue background
- Hover state: Darker blue background
- Pressed state: Dark blue with green text
- Selected state: Dark blue with white text
- Today state: Yellow background
- Inactive state: Light gray with dark gray text

### Data Flow

1. Date/time data is passed to the control for initial display
2. User interactions update the selected date/time
3. Selection events trigger updates in parent components
4. The control maintains internal state for the current view (month/year)

### Integration Points

This component is likely used in:

- Production scheduling interfaces
- Report date range selection
- Maintenance scheduling
- Filter criteria for various data views

### Visual Representation

```
+------------------------------------------+
|          Calendar Date Picker            |
+------------------------------------------+
| << May 2023 >>                           |
+------------------------------------------+
| WK | Mo | Tu | We | Th | Fr | Sa | Su   |
+------------------------------------------+
| 18 | 1  | 2  | 3  | 4  | 5  | 6  | 7    |
| 19 | 8  | 9  | 10 | 11 | 12 | 13 | 14   |
| 20 | 15 | 16 | 17 | 18 | 19 | 20 | 21   |
| 21 | 22 | 23 | 24 | 25 | 26 | 27 | 28   |
| 22 | 29 | 30 | 31 | 1  | 2  | 3  | 4    |
+------------------------------------------+
| Time: [09:00] [AM]    [Cancel] [Select] |
+------------------------------------------+
```

### Debugging Steps

1. **Date Handling Issues**:
   - Check date formatting and parsing
   - Verify timezone handling
   - Test edge cases (month/year transitions)
2. **UI Rendering Problems**:
   - Inspect CSS application
   - Check cell generation logic
   - Verify event handling for selection

## Unit Part List Module

### Purpose

The UnitPartListWidget displays the parts associated with a specific vehicle unit, highlighting special conditions like expired parts or future-dated parts.

### Files

- **UnitPartListWidget.css**: Styling for the parts list display

### How It Works

This widget presents a tabular view of parts with:

- Color-coded rows based on part status
- Customized scrollbar behavior
- Clean empty row handling

#### Key Styling Elements

- Table styling with hidden horizontal scrollbars
- Status-based row highlighting:
  - `expired-part`: Red background (#ff4d4d) for parts past expiration
  - `future-dated-part`: Green background (#00FF7F) for parts not yet effective
  - `invalid-part`: Yellow-green background (#c1c100) for invalid parts

### Data Flow

1. Part data is retrieved based on the selected vehicle unit
2. Status logic determines highlighting for each part
3. The table displays part information with appropriate styling

### Database Interactions

This module likely queries:

- Parts inventory database
- Bill of materials database
- Engineering change notice database

Example queries:

```sql
-- Get parts for a specific VIN
SELECT p.part_number, p.description, p.supplier_code, 
       p.effective_date, p.expiration_date, p.status,
       bom.quantity, bom.position_code
FROM vehicle_parts vp
JOIN parts p ON vp.part_id = p.part_id
JOIN bill_of_materials bom ON vp.bom_id = bom.bom_id
WHERE vp.vin = ?
ORDER BY bom.position_code
```

### Visual Representation

```
+----------------------------------------------------------+
|                    Unit Part List                        |
+----------------------------------------------------------+
| Part Number | Description | Supplier | Quantity | Status |
+----------------------------------------------------------+
| 12345-ABC-A0| Front Bumper| SUPPLIER1| 1        | Valid  |
| 67890-XYZ-B1| Headlight   | SUPPLIER2| 2        | Expired|
| 54321-QRS-C2| Dashboard   | SUPPLIER3| 1        | Future |
+----------------------------------------------------------+
```

### Debugging Steps

1. **Part Data Issues**:
   - Verify part database connectivity
   - Check effective/expiration date calculations
   - Validate status determination logic
2. **Display Problems**:
   - Inspect CSS class application
   - Check table cell factories
   - Verify row highlighting logic

## Operation List Module

### Purpose

The OperationListWidget displays manufacturing operations associated with a production process, providing a clean, focused view of operations.

### Files

- **OperationListWidget.css**: Styling for the operations list

### How It Works

This widget creates a streamlined table view with:

- Hidden horizontal scrollbars for cleaner appearance
- Special handling for empty rows
- Consistent border styling

The CSS focuses on:

- Removing unnecessary scrollbar elements
- Setting appropriate background colors
- Managing border styling for empty cells

### Data Flow

1. Operation data is retrieved from the manufacturing system
2. The data is formatted and displayed in the table
3. User interactions may filter or sort the operations

### Database Interactions

This module likely queries:

- Process definition database
- Operation sequence database
- Work instruction database

Example queries:

```sql
-- Get operations for a process
SELECT operation_code, description, standard_time, 
       sequence_number, station_code, status
FROM manufacturing_operations
WHERE process_id = ?
ORDER BY sequence_number
```

### Visual Representation

```
+----------------------------------------------------------+
|                  Operation List                          |
+----------------------------------------------------------+
| Op Code | Description        | Time | Station | Status   |
+----------------------------------------------------------+
| OP1001  | Install Dashboard  | 5:30 | ASSY1   | Complete |
| OP1002  | Connect Wiring     | 3:45 | ASSY1   | Active   |
| OP1003  | Install Steering   | 4:15 | ASSY2   | Pending  |
+----------------------------------------------------------+
```

### Debugging Steps

1. **Data Loading Issues**:
   - Check operation database connectivity
   - Verify query execution
   - Look for data transformation errors
2. **UI Rendering Problems**:
   - Inspect CSS application
   - Check table cell factories
   - Verify empty row handling

## Integration and Data Flow

The components in this system work together to provide a comprehensive interface for vehicle manufacturing control:

1. **Production Planning Flow**:
   - Production plans define the overall schedule
   - Lot views break down plans into manageable groups
   - Unit views show individual vehicle details
2. **Vehicle Processing Flow**:
   - Unit Navigator allows selection of vehicles
   - Part List shows components for the selected vehicle
   - Operation List shows manufacturing steps
   - BBScales captures weight measurements during production
3. **Data Integration Points**:
   - VIN serves as the primary key linking most components
   - Lot numbers group related vehicles
   - Station codes track vehicle location in the process
   - Timestamps record progression through manufacturing steps

### System-Wide Data Flow Diagram

```
+----------------+      +-------------------+      +----------------+
| Production     |----->| Lot-Level        |----->| Unit-Level     |
| Planning       |      | Scheduling        |      | Scheduling     |
+----------------+      +-------------------+      +----------------+
                                                          |
                                                          v
+----------------+      +-------------------+      +----------------+
| BBScales       |<-----| Unit Navigator    |<---->| Unit Part List |
| Measurement    |      | Selection         |      | Component View |
+----------------+      +-------------------+      +----------------+
        |                       |                         |
        |                       |                         |
        v                       v                         v
+---------------------------------------------------------------+
|                     Database Layer                            |
| (Vehicle Data, Parts, Operations, Measurements, Schedules)    |
+---------------------------------------------------------------+
```

## Debugging Guide

### Common Issues and Solutions

#### 1. Data Display Problems

- **Symptoms**: Missing data, incorrect formatting, empty tables
- **Checks:**
  - Verify database connectivity
  - Check query execution in logs
  - Inspect data transformation logic
  - Validate CSS application

#### 2. UI Responsiveness Issues

- **Symptoms**: Slow loading, delayed updates, UI freezes
- **Checks**:
  - Monitor query execution times
  - Check for large result sets
  - Look for inefficient UI update patterns
  - Verify background thread usage for long operations

#### 3. Integration Failures

- **Symptoms**: Components not updating when others change
- **Checks:**
  - Verify event publishing/subscription
  - Check data passing between components
  - Validate state management

#### 4. Visual Styling Issues

- **Symptoms**: Incorrect colors, sizes, or layouts
- **Checks:**
  - Confirm CSS file loading
  - Check for CSS selector conflicts
  - Verify style class application
  - Inspect component hierarchy

### Logging and Monitoring

Key application properties for debugging:

```properties
# Enable detailed logging
logging.level.com.honda.galc=DEBUG
logging.level.com.honda.galc.client=TRACE

# Database connection monitoring
spring.datasource.log-statements=true
hibernate.show_sql=true

# UI performance tracking
javafx.logging.level=FINE
```

### Troubleshooting Workflow

1. **Identify the Problem Area**:
   - Which component is showing issues?
   - Is it data-related or UI-related?
   - Is it consistent or intermittent?
2. **Check Logs**:
   - Application logs for exceptions
   - Database logs for query issues
   - Network logs for connectivity problems
3. **Isolate the Component**:
   - Test the component in isolation if possible
   - Verify data inputs independently
   - Check component initialization
4. **Resolve and Test**:
   - Apply fixes based on findings
   - Test in isolation first
   - Verify in the integrated system
   - Document the solution for future reference

By following this structured approach to debugging, most issues in the GALC client application can be efficiently identified and resolved.