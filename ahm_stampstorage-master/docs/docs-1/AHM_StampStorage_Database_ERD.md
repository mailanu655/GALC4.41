# AHM Stamp Storage System Database ERD

## Database Overview

The AHM Stamp Storage System uses a relational database to manage carrier tracking, storage operations, and system state. Below is a detailed Entity-Relationship Diagram (ERD) showing the relationships between major system components.

## Core Tables

### 1. Carrier Management

```mermaid
erDiagram
    CARRIER_TBX ||--o{ CARRIER_HISTORY : "tracks"
    CARRIER_TBX ||--o{ CARRIER_MES : "integrates"
    CARRIER_TBX {
        long carrier_id PK
        int carrier_number
        int quantity
        int die_id FK
        int current_location FK
        int destination FK
        enum carrier_status
        timestamp load_timestamp
        timestamp unload_timestamp
        int maintenance_bits
    }

    CARRIER_HISTORY {
        long history_id PK
        int carrier_id FK
        int location_id FK
        timestamp timestamp
        string action
        string status
    }

    CARRIER_MES {
        long mes_id PK
        int carrier_id FK
        string mes_status
        timestamp last_update
        string mes_data
    }
```

### 2. Storage Management

```mermaid
erDiagram
    STORAGE_ROW ||--|{ CARRIER_TBX : "contains"
    STORAGE_ROW ||--o{ STORAGE_HISTORY : "tracks"
    STORAGE_ROW {
        long row_id PK
        string area_code
        int capacity
        int current_count
        enum storage_status
        timestamp last_modified
    }

    STORAGE_HISTORY {
        long history_id PK
        int row_id FK
        timestamp timestamp
        string action
        string details
    }
```

### 3. Die Management

```mermaid
erDiagram
    DIE ||--o{ CARRIER_TBX : "assigned_to"
    DIE ||--o{ DIE_INVENTORY : "tracks"
    DIE {
        long die_id PK
        string die_number
        string model
        enum die_type
        int production_volume
        timestamp last_used
    }

    DIE_INVENTORY {
        long inventory_id PK
        int die_id FK
        int quantity
        timestamp timestamp
        string location
    }
```

### 4. System Configuration

```mermaid
erDiagram
    PARM_SETTING ||--o{ SYSTEM_CONFIG : "configures"
    PARM_SETTING {
        long setting_id PK
        string param_name
        string param_value
        string description
        timestamp last_modified
    }

    SYSTEM_CONFIG {
        long config_id PK
        int setting_id FK
        string config_value
        timestamp effective_date
        boolean is_active
    }
```

### 5. Alarm Management

```mermaid
erDiagram
    ALARM_DEFINITION ||--o{ ALARM_EVENT : "triggers"
    ALARM_DEFINITION ||--o{ ALARM_CONTACT : "notifies"
    ALARM_DEFINITION {
        long alarm_id PK
        string alarm_code
        string description
        enum severity
        boolean requires_ack
    }

    ALARM_EVENT {
        long event_id PK
        int alarm_id FK
        timestamp timestamp
        string details
        boolean acknowledged
    }

    ALARM_CONTACT {
        long contact_id PK
        int alarm_id FK
        string name
        string email
        string phone
        enum contact_type
    }
```

## Table Relationships

### Primary Relationships

1. **Carrier to Storage**
   - Each carrier is assigned to one storage row
   - Storage rows can contain multiple carriers
   - Historical tracking maintained for both

2. **Die to Carrier**
   - Dies can be assigned to multiple carriers
   - Each carrier has one die assignment at a time
   - Inventory tracking for both entities

3. **Alarm System**
   - Definitions link to multiple events
   - Contact assignments for notifications
   - Event tracking and acknowledgment

## Data Types

### Common Fields

1. **Identifiers**
   - Primary Keys: BIGINT/LONG
   - Foreign Keys: INTEGER
   - Business Keys: VARCHAR(50)

2. **Timestamps**
   - Created: TIMESTAMP
   - Modified: TIMESTAMP
   - Effective: TIMESTAMP

3. **Status Fields**
   - Enums stored as VARCHAR(20)
   - Flags as BOOLEAN
   - Counts as INTEGER

## Indexing Strategy

### Primary Indexes

1. **Carrier Tracking**
   - Carrier Number (Unique)
   - Location ID
   - Status + Timestamp

2. **Storage Management**
   - Area Code + Row Number
   - Status + Capacity
   - Last Modified

3. **Die Management**
   - Die Number (Unique)
   - Model + Type
   - Production Volume

## Audit Trail

### History Tables

1. **Carrier History**
   - Tracks all carrier movements
   - Status changes
   - Location updates

2. **Storage History**
   - Capacity changes
   - Status updates
   - Configuration changes

3. **Alarm History**
   - Event occurrences
   - Acknowledgments
   - Resolution details

## Performance Considerations

### Optimization Strategies

1. **Partitioning**
   - History tables partitioned by date
   - Active vs. Archive separation
   - Performance-based segmentation

2. **Indexing**
   - Covering indexes for common queries
   - Filtered indexes for status-based queries
   - Maintenance windows for reindexing

3. **Archival**
   - Regular archival of historical data
   - Maintenance of active dataset size
   - Performance optimization through data lifecycle management
