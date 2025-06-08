package com.honda.galc.client;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


public class RelaunchUtil {
	private static final String SUN_JAVA_COMMAND = "sun.java.command";
	private static final String JAVA_HOME = "java.home";
	private static final String CLASSPATH = "java.class.path";
	private static final String FILESEP   = "file.separator";
	private static PrintStream out;
	
	public static void restart() throws IOException {
		try {
			
			
			final String launchCommand = CommandInfo.getInstance().getLaunchCommand();
			
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						Runtime.getRuntime().exec(launchCommand);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Error while trying to restart the application", e);
		}
	}

	

	
	static final class CommandInfo {
		private static   CommandInfo commandInfo;
		private String   executable;
		private String   argumentList;
		private final static String WEBSTART = "javaws";
		private final static String JAVA     = "java";
		
		
		public static CommandInfo getInstance() {
			if (commandInfo == null) {
	  		    commandInfo =  new CommandInfo();
			}
			return commandInfo;
		}
		
		public String getLaunchCommand() {
			String cmd;
			
			if (isWindows()) {
				cmd	= String.format("\"%s\" %s", executable, argumentList);
			} else {
				cmd	= String.format("%s %s", executable, argumentList);
			}
			
			System.out.println("Launch command = " + cmd);
            return cmd;
		}
		
		private CommandInfo() {
			//List<String>  vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
			
			if (isWebstartJVM()) {
				executable =  getJavaCommand(WEBSTART);
				argumentList = System.getProperty("jnlpx.origFilenameArg","default jnlp"); 
			} else {
				executable =  getJavaCommand(JAVA);
				argumentList = getArgs();
			}
		}
		
		private boolean isWebstartJVM() {
			try {
			   Class.forName("javax.jnlp.ServiceManager");
			   return true;
			} catch (ClassNotFoundException ex) {
			   return false;
			}
		}
		
		private String getJavaCommand(String exe) {
			String javaHome = System.getProperty(JAVA_HOME);
			String pathSeparator  = System.getProperty(FILESEP);
		 	return String.format("%s%sbin%s%s",javaHome,pathSeparator,pathSeparator,exe);
		}

		
		private static String getArgs() {
			StringBuilder cmd = new StringBuilder(); 
			
			String[] command = System.getProperty(SUN_JAVA_COMMAND).split(" ");
			
			if (command[0].endsWith(".jar")) {
				cmd.append("-jar " + new File(command[0]).getPath() + " ");
			}  else {
				cmd.append("-cp \"" + System.getProperty(CLASSPATH) + "\" " + command[0] + " ");
			}

			
			for (int i = 1; i < command.length; i++) {
               cmd.append(command[i]).append(' ');
			}

			return cmd.toString();
		}
		
		public static boolean isWindows() {
			String OS = System.getProperty("os.name");
			return (OS.toLowerCase().indexOf("win") >= 0);
		}
	}
}
