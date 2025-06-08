# SearchByProductFilterPane.fxml Documentation

## Purpose

The SearchByProductFilterPane is a user interface component within the GALC (Global Assembly Line Control) system that provides a flexible search interface for finding products in the manufacturing database. It allows users to search for products using different criteria such as tracking status, production lot, sequence range, and product ID range.

This component serves as a critical tool for production staff to quickly locate specific products or groups of products in the manufacturing system, which is essential for quality control, tracking, and production management.

## How It Works

The SearchByProductFilterPane operates as part of a tab-based interface in the GALC client application. It follows the Model-View-Controller (MVC) pattern:

1. **View (FXML)**: Defines the UI layout and components
2. **Controller**: Handles user interactions and business logic
3. **Model**: Manages data operations and database queries

### Workflow

1. User selects a plant from the dropdown
2. User chooses a search method by selecting one of the radio buttons:
   - Tracking Status
   - Production Lot
   - Sequence Range
   - Product ID Range
3. User enters the required search parameters
4. User clicks the "Search" button
5. The system queries the database based on the selected criteria
6. Results are displayed to the user in a product list view

## Key Components

### UI Elements

| Component                  | Type        | Purpose                                     |
| -------------------------- | ----------- | ------------------------------------------- |
| **processPointComboBox**   | ComboBox    | Allows selection of a manufacturing plant   |
| **trackStsRdBtn**          | RadioButton | Selects tracking status search mode         |
| **trackingStatusComboBox** | ComboBox    | Selects specific tracking status            |
| **prodLotRdBtn**           | RadioButton | Selects production lot search mode          |
| **prodLotTextField**       | TextField   | Input field for production lot number       |
| **seqRangeRdBtn**          | RadioButton | Selects sequence range search mode          |
| **seqRangeTxtField1**      | TextField   | Start sequence number                       |
| **seqRangeTxtField2**      | TextField   | End sequence number                         |
| **productIdRangeRdBtn**    | RadioButton | Selects product ID range search mode        |
| **productIdRangeTxt1**     | TextField   | Start product ID                            |
| **productIdRangeTxt2**     | TextField   | End product ID                              |
| **searchBtn**              | Button      | Initiates the search operation              |
| **searchGroup**            | ToggleGroup | Groups radio buttons for mutual exclusivity |

### Controller Logic

The `SearchByProductFilterController` class handles:

1. **Initialization**: Sets up event handlers and initial data
2. **Event Handling**: Responds to user interactions
3. **Validation**: Ensures input data is valid before searching
4. **Search Execution**: Calls the model to perform database queries
5. **Result Processing**: Publishes search results to the application

### Data Flow

```
User Input → Controller Validation → Model Query → Database → Results → Event Publication → UI Update
```

## Interactions with Other Components

The SearchByProductFilterPane interacts with several other components in the GALC system:

1. **BulkProductInputPane**: Parent container that hosts this component in a tab
2. **ProductController**: Manages product-related operations and data
3. **SearchByFilterModel**: Handles database queries for product searches
4. **EventBusUtil**: Facilitates communication between components using events
5. **QiProgressBar**: Displays progress during search operations
6. **MultiLineHelper**: Provides support for multi-line manufacturing environments

## Database Interactions

The component interacts with the database through the `SearchByFilterModel` class, which uses Data Access Objects (DAOs) to perform queries:

### Key Database Queries

1. **Find Products by Production Lot**:

   ```sql
   -- Simplified representation of the actual query
   SELECT * FROM products 
   WHERE production_lot = ? 
   LIMIT ?
   ```

2. **Find Products by Tracking Status**:

   ```sql
   -- Simplified representation of the actual query
   SELECT * FROM products 
   WHERE tracking_status = ? 
   LIMIT ?
   ```

3. **Find Products by Sequence Range**:

   ```sql
   -- Simplified representation of the actual query
   SELECT * FROM products 
   WHERE sequence_number BETWEEN ? AND ? 
   LIMIT ?
   ```

4. **Find Products by Product ID Range**:

   ```sql
   -- Simplified representation of the actual query
   SELECT * FROM products 
   WHERE product_id BETWEEN ? AND ? 
   LIMIT ?
   ```

5. **Find All Tracking Statuses by Plant**:

   ```sql
   -- Simplified representation of the actual query
   SELECT DISTINCT tracking_status 
   FROM products 
   WHERE plant_name = ?
   ```

## Static Properties

The component uses several static properties from configuration files:

1. **Maximum Product Fetch Limit**: Controls how many products can be returned in a single search

   ```
   qi.product.max.products.fetch=1000
   ```

2. **Product Type Configuration**: Determines which product types are available for search

   ```
   product.type=FRAME
   ```

3. **Multi-Line Configuration**: Controls whether multi-line search is enabled

   ```
   multiline.enabled=true
   ```

## Visual Representation

### Component Layout

```
+-----------------------------------------------------------------------+
|                                                                       |
| [Select Plant ▼]                                                      |
|                                                                       |
| ○ Tracking Status    ○ Production Lot    ○ Sequence Range    ○ Product ID Range |
| [Status ▼]           [____________]      Start: [_______]    Start: [_______] |
|                                          End:   [_______]    End:   [_______] |
|                      [Search]                                         |
|                                                                       |
+-----------------------------------------------------------------------+
```

### Data Flow Diagram

```
+------------------+     +------------------------+     +-------------------+
| User Interface   |     | Controller             |     | Model             |
| (FXML)           |---->| (SearchByProductFilter |---->| (SearchByFilter   |
|                  |     | Controller)            |     | Model)            |
+------------------+     +------------------------+     +-------------------+
        ^                          |                            |
        |                          v                            v
        |                +------------------------+     +-------------------+
        |                | Event Bus              |     | Database          |
        +----------------| (EventBusUtil)         |     | (DAOs)            |
                         +------------------------+     +-------------------+
```

## Example Usage Scenarios

### Scenario 1: Finding Products by Production Lot

1. User selects "MAP" plant from the dropdown
2. User selects "Production Lot" radio button
3. User enters "LOT12345" in the production lot field
4. User clicks "Search"
5. System displays all products from production lot "LOT12345"

### Scenario 2: Finding Products by Tracking Status

1. User selects "ELP" plant from the dropdown
2. User selects "Tracking Status" radio button
3. User selects "ASSEMBLY(ASM)" from the tracking status dropdown
4. User clicks "Search"
5. System displays all products currently in the ASSEMBLY tracking status

### Scenario 3: Finding Products by Sequence Range

1. User selects "MAP" plant from the dropdown
2. User selects "Sequence Range" radio button
3. User enters "1000" in the Start field
4. User enters "1050" in the End field
5. User clicks "Search"
6. System displays all products with sequence numbers between 1000 and 1050

## Debugging Steps for Production Issues

If issues occur with the SearchByProductFilterPane in production, follow these steps:

1. **Verify UI Visibility**:
   - Check if all search options are visible based on product type
   - For FRAME products, all search options should be visible
   - For non-FRAME products, Sequence Range option may be hidden
   - For non-FRAME products in multi-line environments, Product ID Range may be hidden
2. **Check Input Validation**:
   - Ensure inputs don't contain special characters
   - Verify numeric fields contain only numbers
   - Confirm date ranges are valid
3. **Examine Database Connectivity**:
   - Verify database connection is active
   - Check for timeout errors in logs
   - Ensure query execution time is reasonable
4. **Review Search Results**:
   - If no results are returned, verify search criteria
   - Check if maximum product fetch limit is reached
   - Examine database logs for query execution
5. **Inspect Event Publication**:
   - Verify ProductEvent is published correctly
   - Check if subscribers are receiving the event
   - Ensure event handlers are processing the event correctly

## Troubleshooting Common Issues

Here's a more organized version of your table for issues, possible causes, and solutions:

| Issue                            | Possible Cause                  | Solution                             |
| -------------------------------- | ------------------------------- | ------------------------------------ |
| **Search button disabled**       | No plant selected               | Select a plant from the dropdown     |
| **No results found**             | Invalid search criteria         | Verify input values are correct      |
| **Too many results**             | Search criteria too broad       | Narrow search criteria               |
| **Sequence Range not visible**   | Non-FRAME product type          | Switch to a FRAME product type       |
| **Product ID Range not visible** | Non-FRAME product in multi-line | Switch to a FRAME product type       |
| **Special character error**      | Invalid input                   | Remove special characters from input |
| **Time range error**             | End time before start time      | Correct the time range               |

This format provides a clear and concise overview, making it easier to troubleshoot and resolve issues.

## Conclusion

The SearchByProductFilterPane is a versatile and powerful component in the GALC system that enables users to efficiently search for products using various criteria. Its flexible design accommodates different product types and manufacturing environments, making it an essential tool for production management and quality control.