package com.honda.galc.common.logging;


import java.io.Flushable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.ObjectMessage;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.StopWatch;
import org.perf4j.chart.GoogleChartGenerator;
import org.perf4j.chart.StatisticsChartGenerator;
import org.perf4j.helpers.MiscUtils;
import org.perf4j.helpers.StatsValueRetriever;
  
  /**
   * This appender is designed to be attached to an {@link AsyncCoalescingStatisticsAppender}. It takes the incoming
   * GroupedTimingStatistics log messages and uses this data to update a graphical view of the logged statistics. If
   * ANOTHER appender is then attached to this appender then the graph URLs will be written to the appender on a scheduled
   * basis. Alternatively, the graph can be viewed by setting up a
   * {@link org.perf4j.log4j.servlet.GraphingServlet} to expose the graph images.
   *
   * @author Alex Devine
   */

@Plugin(name = "GraphingStatisticsAppender", category = "Core", elementType = "appender", printObject = true)
public class GraphingStatisticsAppender extends AbstractAppender implements AppenderAttachable, Flushable {

	/**
     * This class keeps track of all appenders of this type that have been created. This allows static access to
     * the appenders from the org.perf4j.log4j.servlet.GraphingServlet class.
     */
    protected final static Map<String, GraphingStatisticsAppender> APPENDERS_BY_NAME =
              Collections.synchronizedMap(new LinkedHashMap<String, GraphingStatisticsAppender>());
  
      // --- configuration options ---
      /**
       * The type of data to display on the graph. Defaults to "Mean" to display mean values. Acceptable values are any
       * constant name from the {@link org.perf4j.helpers.StatsValueRetriever} class, such as Mean, Min, Max, Count,
       * StdDev or TPS.
       */
    private String graphType = StatsValueRetriever.MEAN_VALUE_RETRIEVER.getValueName();
      /**
       * A comma-separated list of the tag names that should be graphed. If not set then a separate series will be
       * displayed on the graph for each tag name logged.
       */
    private String tagNamesToGraph = null;
      /**
       * Gets the number of data points that will be written on each graph before the graph URL is written to any
       * attached appenders. Thus, this option is only relevant if there are attached appenders.
       * Defaults to <tt>StatisticsChartGenerator.DEFAULT_MAX_DATA_POINTS</tt>.
       */
    private int dataPointsPerGraph = StatisticsChartGenerator.DEFAULT_MAX_DATA_POINTS;
  
      // --- contained objects/state variables ---
      /**
       * The chart genertor, initialized in the <tt>activateOptions</tt> method, that stores the data for the chart.
       */
      private StatisticsChartGenerator chartGenerator;
      /**
       * Keeps track of the number of logged GroupedTimingStatistics, which is used to determine when a graph should
       * be written to any attached appenders.
       */
      private AtomicLong numLoggedStatistics = new AtomicLong();
      /**
       * Keeps track of whether there is existing data that hasn't yet been flushed to downstream appenders.
       */
      private volatile boolean hasUnflushedData = false;
      /**
       * Keeps track of the Level of the last appended event. This is just used to determine what level we send to OUR
       * downstream events.
       */
      private Level lastAppendedEventLevel = Level.INFO;
      /**
       * Any downstream appenders are contained in this AppenderAttachableImpl
       */
      private final AppenderAttachableImpl downstreamAppenders = new AppenderAttachableImpl();
  
      // --- options ---
  
      protected GraphingStatisticsAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
		// TODO Auto-generated constructor stub
      }

     /**
      * The <b>GraphType</b> option is used to specify the data that should be displayed on the graph. Acceptable
      * values are Mean, Min, Max, Count, StdDev and TPS (for transactions per second). Defaults to Mean if not
      * explicitly set.
      *
      * @return The value of the GraphType option
      */
     public String getGraphType() {
         return graphType;
     }
 
     /**
      * Sets the value of the <b>GraphType</b> option. This must be a valid type, one of
      * Mean, Min, Max, Count, StdDev or TPS (for transactions per second).
      *
      * @param graphType The new value for the GraphType option.
      */
     public void setGraphType(String graphType) {
        this.graphType = graphType;
     }
 
     /**
      * The <b>TagNamesToGraph</b> option is used to specify which tags should be logged as a data series on the
      * graph. If not specified ALL tags will be drawn on the graph, one series for each tag.
      *
      * @return The value of the TagNamesToGraph option
      */
     public String getTagNamesToGraph() {
         return tagNamesToGraph;
     }
 
     /**
      * Sets the value of the <b>TagNamesToGraph</b> option.
      *
      * @param tagNamesToGraph The new value for the TagNamesToGraph option.
      */
     public void setTagNamesToGraph(String tagNamesToGraph) {
         this.tagNamesToGraph = tagNamesToGraph;
     }
 
     /**
      * The <b>DataPointsPerGraph</b> option is used to specify how much data should be displayed on each graph before
      * it is written to any attached appenders. Defaults to <tt>StatisticsChartGenerator.DEFAULT_MAX_DATA_POINTS</tt>.
      *
      * @return The value of the DataPointsPerGraph option
      */
     public int getDataPointsPerGraph() {
         return dataPointsPerGraph;
     }

     /**
      * Sets the value of the <b>DataPointsPerGraph</b> option.
      *
      * @param dataPointsPerGraph The new value for the DataPointsPerGraph option.
      */
     public void setDataPointsPerGraph(int dataPointsPerGraph) {
         if (dataPointsPerGraph <= 0) {
             throw new IllegalArgumentException("The DataPointsPerGraph option must be positive");
         }
         this.dataPointsPerGraph = dataPointsPerGraph;
     }
 
     public void activateOptions() {
         chartGenerator = createChartGenerator();
 
         //update the static APPENDERS_BY_NAME object
        if (getName() != null) {
             APPENDERS_BY_NAME.put(getName(), this);
         }
     }
 
     /**
      * Helper method creates a new StatisticsChartGenerator based on the options set on this appender. By default
      * a GoogleChartGenerator is created, though subclasses may override this method to create a different type of
      * chart generator.
      *
      * @return A newly created StatisticsChartGenerator.
      */
     protected StatisticsChartGenerator createChartGenerator() {
        StatsValueRetriever statsValueRetriever = StatsValueRetriever.DEFAULT_RETRIEVERS.get(getGraphType());
         if (statsValueRetriever == null) {
             throw new RuntimeException("Unknown GraphType: " + getGraphType() +
                                        ". See the StatsValueRetriever class for the list of acceptable types.");
         }
 
         //create the chart generator and set the enabled tags
         GoogleChartGenerator retVal = new GoogleChartGenerator(statsValueRetriever);
         if (getTagNamesToGraph() != null) {
             Set<String> enabledTags =
                     new HashSet<String>(Arrays.asList(MiscUtils.splitAndTrim(getTagNamesToGraph(), ",")));
             retVal.setEnabledTags(enabledTags);
         }
 
         return retVal;
     }
 
     // --- exposed objects ---
 
     /**
      * Gets the contained StatisticsChartGenerator that is used to generate the graphs.
      *
      * @return The StatisticsChartGenerator used by this appender.
      */
     public StatisticsChartGenerator getChartGenerator() {
         return chartGenerator;
     }
 
    /**
     * This static method returns any created GraphingStatisticsAppender by its name.
     *
     * @param appenderName the name of the GraphingStatisticsAppender to return
     * @return the specified GraphingStatisticsAppender, or null if not found
     */
     public static GraphingStatisticsAppender getAppenderByName(String appenderName) {
         return APPENDERS_BY_NAME.get(appenderName);
     }
 
     /**
      * This static method returns an unmodifiable collection of all GraphingStatisticsAppenders that have been created.
      *
      * @return The collection of GraphingStatisticsAppenders created in this VM.
      */
     public static Collection<GraphingStatisticsAppender> getAllGraphingStatisticsAppenders() {
         return Collections.unmodifiableCollection(APPENDERS_BY_NAME.values());
     }
 
     // --- appender attachable methods ---
 
     public void addAppender(Appender appender) {
         synchronized (downstreamAppenders) {
             downstreamAppenders.addAppender(appender);
         }
     }
 
     public Enumeration getAllAppenders() {
         synchronized (downstreamAppenders) {
             return downstreamAppenders.getAllAppenders();
         }
     }
 
     public Appender getAppender(String name) {
         synchronized (downstreamAppenders) {
             return downstreamAppenders.getAppender(name);
         }
     }
 
     public boolean isAttached(Appender appender) {
         synchronized (downstreamAppenders) {
             return downstreamAppenders.isAttached(appender);
         }
     }
 
     public void removeAllAppenders() {
         synchronized (downstreamAppenders) {
             downstreamAppenders.removeAllAppenders();
         }
     }
 
     public void removeAppender(Appender appender) {
         synchronized (downstreamAppenders) {
             downstreamAppenders.removeAppender(appender);
         }
     }
 
     public void removeAppender(String name) {
         synchronized (downstreamAppenders) {
             downstreamAppenders.removeAppender(name);
         }
     }
 
     // --- appender methods ---
 
     public void append(LogEvent event) {
         Object logMessage = event.getMessage();
         if (logMessage instanceof GroupedTimingStatistics && chartGenerator != null) {
             chartGenerator.appendData((GroupedTimingStatistics) logMessage);
             hasUnflushedData = true;
             lastAppendedEventLevel = event.getLevel();
 
             //output the graph if necessary to any attached appenders
        //   if ((numLoggedStatistics.incrementAndGet() % getDataPointsPerGraph()) == 0) {
                 flush();
          //   }
         }
     }
 
     public boolean requiresLayout() {
         return false;
     }
 
     public void close() {
         //close any downstream appenders
         synchronized (downstreamAppenders) {
             flush();
 
             for (Enumeration enumer = downstreamAppenders.getAllAppenders();
                  enumer != null && enumer.hasMoreElements();) {
                 Appender appender = (Appender) enumer.nextElement();
                 appender.stop();
             }
         }
     }
 
 
    public void flush() {
         synchronized(downstreamAppenders) {
             if (hasUnflushedData && downstreamAppenders.getAllAppenders() != null) {
                 downstreamAppenders.appendLoopOnAppenders(

/*
                		 new LogEvent(Logger.class.getName(),
                                          Logger.getLogger(StopWatch.DEFAULT_LOGGER_NAME),
                                          System.currentTimeMillis(),
                                          lastAppendedEventLevel,
                                          chartGenerator.getChartUrl(),
                                          null)
 */
                        
                        Log4jLogEvent.newBuilder().setLoggerName(StopWatch.DEFAULT_LOGGER_NAME)
                        							.setTimeMillis(System.currentTimeMillis())
                        							.setLevel(lastAppendedEventLevel)
                        							.setMessage(new ObjectMessage(chartGenerator.getChartUrl()))
                        							.build()
                 );
                hasUnflushedData = false;
             }
        }
     }
 
	public String toString() {
		return this.getClass().getName() + " - " + (getName() == null ? "" : getName());
	}


    @PluginFactory
    public static GraphingStatisticsAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                              @PluginElement("Layout") Layout<?> layout,
                                              @PluginElement("Filters") Filter filter) {
	 
        return new GraphingStatisticsAppender(name == null ? "EventBus" : name, filter, layout);
    }
}
