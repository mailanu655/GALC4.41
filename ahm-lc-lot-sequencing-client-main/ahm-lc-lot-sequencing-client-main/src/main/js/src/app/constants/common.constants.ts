export enum LogLevel {
    CHECK = 7,      // Maximum detail.  Can be used for QA Testing Results to determine if a process was complete
    GUI = 6,        // Changes to the UI display.  For example: Button was changed from green background to red.
    DEBUG = 5,      // Messages to help debug an issue.  Placed at key points in the code for troubleshooting.
    INFO = 4,       // General message to be able to tell the story of what is happening in the application or UI.  User clicked on button A or scan dialog displayed.
    WARNING = 3,    // Abnormal occurred in the process but does not prevent from moving on with the process.  
    ERROR = 2,      // Abnormal occurred in the process and prevents the process from continuing
    FATAL = 1,      // Abnormal occurred and was not caught and handled by the application.  Should be very high level exceptions.
    OFF = 0,
}

export enum ProductType {
    FRAME = 'FRAME', ENGINE = 'ENGINE', MBPN = 'MBPN'//...TODO
}
