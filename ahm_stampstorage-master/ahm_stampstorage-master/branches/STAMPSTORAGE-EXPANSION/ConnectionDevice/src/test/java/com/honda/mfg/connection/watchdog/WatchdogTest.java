package com.honda.mfg.connection.watchdog;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

/**
 * User: Jeffrey M Lutz Date: 4/3/11
 */
public class WatchdogTest {

	@Test
	public void successfullyExerciseAllStateTransitions() {
		// Pre-condition setup
		WatchdogContext ctx = mock(WatchdogContext.class);

		Watchdog dog = new Watchdog(ctx);

		// Perform test
		dog.unhealthyEvent();
		verify(ctx, times(1)).close();

		dog.unhealthyEvent();
		verify(ctx, times(2)).close();

		dog.healthyEvent();
		verify(ctx, times(2)).close();

		dog.healthyEvent();
		verify(ctx, times(2)).close();

		dog.unhealthyEvent();
		verify(ctx, times(3)).close();

		dog.unhealthyEvent();
		verify(ctx, times(4)).close();

		dog.healthyEvent();
		verify(ctx, times(4)).close();

		dog.healthyEvent();
		verify(ctx, times(4)).close();

		dog.unhealthyEvent();
		verify(ctx, times(5)).close();

		dog.unhealthyEvent();
		verify(ctx, times(6)).close();
	}
}
