package com.honda.galc.common.logging;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.honda.galc.common.logging.LogLevel;

public class LogLevelTest {
	
	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Test order of LogLevels by level.
	 */
	@Test
	public void testLogLevelOrderByLevel() {
		assertTrue(LogLevel.TRACE.level.intLevel() > LogLevel.DEBUG.level.intLevel());
		assertTrue(LogLevel.DEBUG.level.intLevel() > LogLevel.GUI.level.intLevel());
		assertTrue(LogLevel.GUI.level.intLevel() > LogLevel.DATABASE.level.intLevel());
		assertTrue(LogLevel.DATABASE.level.intLevel() > LogLevel.CHECK.level.intLevel());
		assertTrue(LogLevel.CHECK.level.intLevel() > LogLevel.INFO.level.intLevel());
		assertTrue(LogLevel.INFO.level.intLevel() > LogLevel.ERROR.level.intLevel());
		assertTrue(LogLevel.ERROR.level.intLevel() > LogLevel.EMERGENCY.level.intLevel());
	}

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Test order of LogLevels by ordinal.
	 */
	@Test
	public void testLogLevelOrderByOrdinal() {
		assertTrue(LogLevel.EMERGENCY.isHigher(LogLevel.ERROR));
		assertTrue(LogLevel.ERROR.isHigher(LogLevel.INFO));
		assertTrue(LogLevel.INFO.isHigher(LogLevel.CHECK));
		assertTrue(LogLevel.CHECK.isHigher(LogLevel.DATABASE));
		assertTrue(LogLevel.DATABASE.isHigher(LogLevel.GUI));
		assertTrue(LogLevel.GUI.isHigher(LogLevel.DEBUG));
		assertTrue(LogLevel.DEBUG.isHigher(LogLevel.TRACE));
	}

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Confirm getLogLevel returns correct value by description.
	 */
	@Test
	public void getLogLevel_confirmCorrectReturnType() {
		assertTrue(LogLevel.TRACE.equals(LogLevel.getLogLevel("trace")));
		assertTrue(LogLevel.TRACE.equals(LogLevel.getLogLevel("TRACE")));
		assertTrue(LogLevel.DEBUG.equals(LogLevel.getLogLevel("debug")));
		assertTrue(LogLevel.DEBUG.equals(LogLevel.getLogLevel("DEBUG")));
		assertTrue(LogLevel.GUI.equals(LogLevel.getLogLevel("gui")));
		assertTrue(LogLevel.GUI.equals(LogLevel.getLogLevel("GUI")));
		assertTrue(LogLevel.DATABASE.equals(LogLevel.getLogLevel("database")));
		assertTrue(LogLevel.DATABASE.equals(LogLevel.getLogLevel("DATABASE")));
		assertTrue(LogLevel.CHECK.equals(LogLevel.getLogLevel("check")));
		assertTrue(LogLevel.CHECK.equals(LogLevel.getLogLevel("CHECK")));
		assertTrue(LogLevel.INFO.equals(LogLevel.getLogLevel("info")));
		assertTrue(LogLevel.INFO.equals(LogLevel.getLogLevel("INFO")));
		assertTrue(LogLevel.ERROR.equals(LogLevel.getLogLevel("error")));
		assertTrue(LogLevel.ERROR.equals(LogLevel.getLogLevel("ERROR")));
		assertTrue(LogLevel.EMERGENCY.equals(LogLevel.getLogLevel("emergency")));
		assertTrue(LogLevel.EMERGENCY.equals(LogLevel.getLogLevel("EMERGENCY")));
	}

	/**
	 * @author 	Dylan Yang
	 * @date	Dec. 11, 2018
	 * 
	 * Confirm getLogLevelFromType returns correct value by type.
	 */
	@Test
	public void getLogLevelFromType() {
		assertTrue(LogLevel.TRACE.equals(LogLevel.getLogLevelFromType("t")));
		assertTrue(LogLevel.TRACE.equals(LogLevel.getLogLevelFromType("T")));
		assertTrue(LogLevel.DEBUG.equals(LogLevel.getLogLevelFromType("d")));
		assertTrue(LogLevel.DEBUG.equals(LogLevel.getLogLevelFromType("D")));
		assertTrue(LogLevel.GUI.equals(LogLevel.getLogLevelFromType("g")));
		assertTrue(LogLevel.GUI.equals(LogLevel.getLogLevelFromType("G")));
		assertTrue(LogLevel.DATABASE.equals(LogLevel.getLogLevelFromType("b")));
		assertTrue(LogLevel.DATABASE.equals(LogLevel.getLogLevelFromType("B")));
		assertTrue(LogLevel.CHECK.equals(LogLevel.getLogLevelFromType("c")));
		assertTrue(LogLevel.CHECK.equals(LogLevel.getLogLevelFromType("C")));
		assertTrue(LogLevel.INFO.equals(LogLevel.getLogLevelFromType("i")));
		assertTrue(LogLevel.INFO.equals(LogLevel.getLogLevelFromType("I")));
		assertTrue(LogLevel.ERROR.equals(LogLevel.getLogLevelFromType("e")));
		assertTrue(LogLevel.ERROR.equals(LogLevel.getLogLevelFromType("E")));
		assertTrue(LogLevel.EMERGENCY.equals(LogLevel.getLogLevelFromType("X")));
		assertTrue(LogLevel.EMERGENCY.equals(LogLevel.getLogLevelFromType("x")));
	}
}
