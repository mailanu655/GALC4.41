package com.honda.mfg.mesproxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * User: Adam S. Kendell
 * Date: 6/21/11
 */
public class MesProxyProperties {

    private static final String PROPERTIES_FILE = "MesProxy.properties";

    //server port used to listen for incoming client connections.
    private static final String SERVER_PORT_PROPERTY = "serverPort";
    private static Integer serverPort;

    //maximum number of allowed incoming client connections
    private static Integer maximumAllowedServerConnections;
    private static final String MAXIMUM_ALLOWED_SERVER_MESSAGES_PROPERTY = "maximumAllowedServerConnections";

    //server port used to send messages from the mes Transport through the server connection
    private static final String TRANSPORT_PORT_PROPERTY = "transportPort";
    private static Integer transportPort;

    //maximum number of allowed transport connections
    private static final String MAXIMUM_ALLOWED_TRANSPORT_CONNECTIONS_PROPERTY = "maximumAllowedTransportConnections";
    private static Integer maximumAllowedTransportConnections;

    //list of addresses allowed to connect to the proxy
    private static final String ALLOWED_TRANSPORT_HOST_LIST_PROPERTY = "allowedTransportAddresses";
    private static String[] allowedTransportHostList;
    private static final String PROPERTY_DELIMITER = ", ";

    //client port used to send messages from the server to the mes listener.
    private static final String LISTENER_PORT_PROPERTY = "listenerPort";
    private static Integer listenerPort;

    //connection timeout for connecting to the mes listener.
    private static final String LISTENER_CONNECT_TIMEOUT_PROPERTY = "listenerConnectTimeout";
    private static Integer listenerConnectTimeout;

    //time to wait before attempting to reconnect to the listener.
    private static final String LISTENER_PAUSE_BEFORE_CONNECT_RETRY_PROPERTY = "listenerPauseBeforeConnectRetry";
    private static Integer listenerPauseBeforeConnectRetry;

    //time to wait before attempting to reconnect to the listener.
    private static final String MESSAGE_QUEUE_SIZE_PROPERTY = "messageQueueSize";
    private static Integer messageQueueSize;

    //time to wait between checking for inputs from the server and transport.
    private static final String PAUSE_BETWEEN_PROXY_OPERATIONS_PROPERTY = "pauseBetweenProxyOperations";
    private static Integer pauseBetweenProxyOperations;

    //indicates the end of a complete message.
    private static final String MESSAGE_TERMINATOR_PROPERTY = "messageTerminator";
    private static String messageTerminator;

    //number of allowed buffer messages
    private static final String BUFFER_MESSAGES_PROPERTY = "bufferMessages";
    private static Boolean bufferMessages;

    //proxy log file name
    private static final String LOG_FILE_NAME_PROPERTY = "logFileName";
    private static String logFileName;

    //proxy log file location
    private static final String LOG_FILE_PATH_PROPERTY = "logFilePath";
    private static String logFilePath;

    //format of the date used in the log file
    private static final String LOG_DATE_FORMAT_PROPERTY = "logDateFormat";
    private static DateFormat logDateFormat;

    //format of the date appended to the file name when the log file is split
    private static final String LOG_FILE_DATE_FORMAT_PROPERTY = "logFileDateFormat";
    private static DateFormat logFileDateFormat;

    //maximum size of the log file
    private static final String LOG_FILE_MAX_SIZE_PROPERTY = "logFileMaxSize";
    private static Integer logFileMaxSize;

    //maximum number of log files in the log path
    private static final String MAX_NUMBER_OF_LOG_FILES_PROPERTY = "maxNumberOfLogFiles";
    private static Integer maxNumberOfLogFiles;

    private static Properties properties = new Properties();
    private InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
    private static MesProxyProperties mesProxyProperties;

    private MesProxyProperties() {
        try {
            properties.load(is);
            loadPropertyValues();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static MesProxyProperties getInstance() {
        if (mesProxyProperties == null) {
            mesProxyProperties = new MesProxyProperties();
        }
        return mesProxyProperties;
    }

    public static Integer getServerPort() {
        return serverPort;
    }

    public static Integer getMaximumAllowedServerConnections() {
        return maximumAllowedServerConnections;
    }

    public static Integer getTransportPort() {
        return transportPort;
    }

    public static Integer getMaximumAllowedTransportConnections() {
        return maximumAllowedTransportConnections;
    }

    public static String[] getAllowedTransportHostList() {
        return allowedTransportHostList;
    }

    public static Integer getListenerPort() {
        return listenerPort;
    }

    public static Integer getListenerConnectTimeout() {
        return listenerConnectTimeout;
    }

    public static Integer getListenerPauseBeforeConnectRetry() {
        return listenerPauseBeforeConnectRetry;
    }

    public static Integer getMessageQueueSize() {
        return messageQueueSize;
    }

    public static Integer getPauseBetweenProxyOperations() {
        return pauseBetweenProxyOperations;
    }

    public static String getMessageTerminator() {
        return messageTerminator;
    }

    public static Boolean getBufferMessages() {
        return bufferMessages;
    }

    public static String getLogFilePath() {
        return logFilePath;
    }

    public static String getLogFileName() {
        return logFileName;
    }

    public static DateFormat getLogDateFormat() {
        return logDateFormat;
    }

    public static DateFormat getLogFileDateFormat() {
        return logFileDateFormat;
    }

    public static Integer getLogFileMaxSize() {
        return logFileMaxSize;
    }

    public static Integer getMaxNumberOfLogFiles() {
        return maxNumberOfLogFiles;
    }


    public static String getLocalHostName() {
        try {
            InetAddress address = InetAddress.getLocalHost();

            // Get hostname
            return address.getHostName();

        } catch (UnknownHostException e) {
            return null;
        }
    }

    private void loadPropertyValues() {

        String delimitedString = properties.getProperty(ALLOWED_TRANSPORT_HOST_LIST_PROPERTY);
        allowedTransportHostList = getArrayFromDelimitedString(delimitedString, PROPERTY_DELIMITER);
        serverPort = Integer.parseInt(properties.getProperty(SERVER_PORT_PROPERTY));
        maximumAllowedServerConnections = Integer.parseInt(properties.getProperty(MAXIMUM_ALLOWED_SERVER_MESSAGES_PROPERTY));
        transportPort = Integer.parseInt(properties.getProperty(TRANSPORT_PORT_PROPERTY));
        maximumAllowedTransportConnections = Integer.parseInt(properties.getProperty(MAXIMUM_ALLOWED_TRANSPORT_CONNECTIONS_PROPERTY));
        listenerPort = Integer.parseInt(properties.getProperty(LISTENER_PORT_PROPERTY));
        listenerPauseBeforeConnectRetry = Integer.parseInt(properties.getProperty(LISTENER_PAUSE_BEFORE_CONNECT_RETRY_PROPERTY));
        listenerConnectTimeout = Integer.parseInt(properties.getProperty(LISTENER_CONNECT_TIMEOUT_PROPERTY));
        messageQueueSize = Integer.parseInt(properties.getProperty(MESSAGE_QUEUE_SIZE_PROPERTY));
        pauseBetweenProxyOperations = Integer.parseInt(properties.getProperty(PAUSE_BETWEEN_PROXY_OPERATIONS_PROPERTY));
        messageTerminator = properties.getProperty(MESSAGE_TERMINATOR_PROPERTY);
        bufferMessages = Boolean.parseBoolean(properties.getProperty(BUFFER_MESSAGES_PROPERTY));
        logFileName = properties.getProperty(LOG_FILE_NAME_PROPERTY);
        logFilePath = properties.getProperty(LOG_FILE_PATH_PROPERTY);
        logDateFormat = new SimpleDateFormat(properties.getProperty(LOG_DATE_FORMAT_PROPERTY));
        logFileDateFormat = new SimpleDateFormat(properties.getProperty(LOG_FILE_DATE_FORMAT_PROPERTY));
        logFileMaxSize = Integer.parseInt(properties.getProperty(LOG_FILE_MAX_SIZE_PROPERTY));
        maxNumberOfLogFiles = Integer.parseInt(properties.getProperty(MAX_NUMBER_OF_LOG_FILES_PROPERTY));
    }

    private String[] getArrayFromDelimitedString(String delimitedString, String delimiter) {
        String[] retVal = delimitedString.split(delimiter);
        return retVal;
    }

}

