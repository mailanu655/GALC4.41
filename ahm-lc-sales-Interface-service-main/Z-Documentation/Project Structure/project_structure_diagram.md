# Honda AHM LC Sales Interface Service - Visual Flow Diagram

## Project Structure Overview

```
ahm-lc-sales-Interface-service
│
├── Main Application
│   └── LCSalesInterfaceApplication.java (Entry point)
│
├── Controller Layer
│   └── LCSalesInterfaceController.java (REST endpoints)
│
├── Scheduler Layer
│   ├── ShippingMessageScheduler.java (Scheduled shipping tasks)
│   └── StatusMessageScheduler.java (Scheduled status updates)
│
├── Task Layer
│   ├── ITransactionTask.java (Interface)
│   ├── ShippingTransactionTask.java (Shipping logic)
│   └── ReceivingTransactionTask.java (Status update logic)
│
├── Handler Layer
│   ├── StatusMessageHandlerFactory.java (Routes messages)
│   ├── AhReceiveMessageHandler.java
│   ├── AhParkingChangeMessageHandler.java
│   ├── DealerAssignMessageHandler.java
│   ├── FactoryReturnMessageHandler.java
│   ├── ShipmentConfirmMessageHandler.java
│   └── SimpleStatusMessageHandler.java
│
├── Service Layer
│   ├── IQueueManagerService.java (Interface)
│   ├── QueueManagerService.java (Message queue operations)
│   ├── ShippingStatusService.java (Status management)
│   ├── ShippingTransactionService.java (Transaction management)
│   ├── BaseGalcService.java
│   ├── FrameService.java
│   ├── FrameShipConfirmationService.java
│   ├── FrameSpecService.java
│   └── Other specialized services...
│
├── Model Layer
│   ├── ShippingStatus.java
│   ├── ShippingTransaction.java
│   ├── Frame.java
│   ├── FrameSpec.java
│   └── Other data models...
│
├── Message Models
│   ├── DataContainer.java
│   ├── ShippingMessage.java
│   ├── ShippingVehicle.java
│   ├── StatusMessage.java
│   ├── StatusVehicle.java
│   └── Transaction.java
│
├── Utility Layer
│   ├── EmailSender.java (Error notifications)
│   ├── JSONUtil.java (JSON processing)
│   └── PropertyUtil.java (Configuration)
│
└── Configuration
    ├── application.properties
    └── application.yml
```

## Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                         Honda AHM LC Sales Interface Service            │
│                                                                         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                ┌─────────────────────────────────────┐
                │                                     │
                ▼                                     ▼
┌───────────────────────────┐           ┌───────────────────────────┐
│                           │           │                           │
│   Shipping Flow (GALC→YMS)│           │  Status Flow (YMS→GALC)   │
│                           │           │                           │
└─────────────┬─────────────┘           └─────────────┬─────────────┘
              │                                       │
              ▼                                       ▼
┌─────────────────────────────┐           ┌─────────────────────────────┐
│ ShippingMessageScheduler    │           │ StatusMessageScheduler      │
│ (Scheduled task)            │           │ (Scheduled task)            │
└─────────────┬───────────────┘           └─────────────┬───────────────┘
              │                                         │
              ▼                                         ▼
┌─────────────────────────────┐           ┌─────────────────────────────┐
│ ShippingTransactionTask     │           │ ReceivingTransactionTask    │
│ (Business logic)            │           │ (Business logic)            │
└─────────────┬───────────────┘           └─────────────┬───────────────┘
              │                                         │
              ▼                                         ▼
┌─────────────────────────────┐           ┌─────────────────────────────┐
│ QueueManagerService         │           │ StatusMessageHandlerFactory │
│ (Message queue operations)  │           │ (Routes to specific handler) │
└─────────────┬───────────────┘           └─────────────┬───────────────┘
              │                                         │
              ▼                                         ▼
┌─────────────────────────────┐           ┌─────────────────────────────┐
│ ShippingStatusService       │           │ Specific Message Handlers   │
│ ShippingTransactionService  │           │ (Process by status type)    │
└─────────────┬───────────────┘           └─────────────┬───────────────┘
              │                                         │
              ▼                                         ▼
┌─────────────────────────────┐           ┌─────────────────────────────┐
│ IBM MQ Queue                │           │ ShippingStatusService       │
│ (YMS Input Queue)           │           │ (Update GALC)               │
└─────────────────────────────┘           └─────────────────────────────┘
```

## Message Processing Flow

### Shipping Message Flow (GALC → YMS)
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│             │    │             │    │             │    │             │    │             │
│    GALC     │───►│  Read Data  │───►│  Format     │───►│  Send to    │───►│    YMS      │
│   System    │    │  from GALC  │    │  Message    │    │  YMS Queue  │    │   System    │
│             │    │             │    │             │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
```

### Status Message Flow (YMS → GALC)
```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│             │    │             │    │             │    │             │    │             │
│    YMS      │───►│  Read from  │───►│  Process    │───►│  Update     │───►│    GALC     │
│   System    │    │  YMS Queue  │    │  Status     │    │  GALC       │    │   System    │
│             │    │             │    │             │    │             │    │             │
└─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘    └─────────────┘
```

## Status Transition Diagram

```
┌───────────┐
│           │
│  VQ-SHIP  │  (Vehicle ready for shipping from factory)
│           │
└─────┬─────┘
      │
      ▼
┌───────────┐
│           │
│  AH-RCVD  │  (Vehicle received at American Honda)
│           │
└─────┬─────┘
      │
      ▼
┌───────────┐
│           │
│ DLR-ASGN  │  (Vehicle assigned to dealer)
│           │
└─────┬─────┘
      │
      ├─────────────┐
      │             │
      ▼             ▼
┌───────────┐  ┌───────────┐
│           │  │           │
│  AH-SHIP  │  │  AH-RTN   │  (Vehicle shipped to dealer OR returned to factory)
│           │  │           │
└─────┬─────┘  └───────────┘
      │
      ▼
┌───────────┐
│           │
│ DLR-RCPT  │  (Vehicle received by dealer)
│           │
└───────────┘
```

## Key Integration Points

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                         Sales Interface Service                         │
│                                                                         │
└───────────┬─────────────────────────┬─────────────────────────┬─────────┘
            │                         │                         │
            ▼                         ▼                         ▼
┌───────────────────────┐  ┌───────────────────────┐  ┌───────────────────────┐
│                       │  │                       │  │                       │
│     IBM MQ Queues     │  │     GALC REST API     │  │     Email System      │
│                       │  │                       │  │                       │
└───────────────────────┘  └───────────────────────┘  └───────────────────────┘
```