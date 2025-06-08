package com.honda.galc.common.logging;


import java.util.Enumeration;

import org.apache.logging.log4j.core.Appender;

/**
   Interface for attaching appenders to objects.

   @author Ceki G&uuml;lc&uuml;
   @since 0.9.1 */
public interface AppenderAttachable {
  
  /**
     Add an appender.
   */
  public
  void addAppender(Appender newAppender);

  /**
     Get all previously added appenders as an Enumeration.  */
  public
  Enumeration getAllAppenders();

  /**
     Get an appender by name.
   */
  public
  Appender getAppender(String name);

  
  /**
     Returns <code>true</code> if the specified appender is in list of
     attached attached, <code>false</code> otherwise.

     @since 1.2 */
  public 
  boolean isAttached(Appender appender);

  /**
     Remove all previously added appenders.
  */
  void removeAllAppenders();


  /**
     Remove the appender passed as parameter from the list of appenders.
  */
   void removeAppender(Appender appender);


 /**
    Remove the appender with the name passed as parameter from the
    list of appenders.  
  */
 void
 removeAppender(String name);   
}

