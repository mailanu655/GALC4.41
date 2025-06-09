package com.honda.mfg.stamp.conveyor.manager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import com.google.common.base.Predicate;

/**
 * User: Jeffrey M Lutz Date: 3/3/11
 * <p/>
 * REFERENCE:
 * http://bloggingfoo.blogspot.com/2010/08/building-flexible-query-apis-with_21.html
 */
public class PredicateWrapper<StorageRow> implements Predicate<StorageRow> {
	private final Matcher<StorageRow> matcher;

	public PredicateWrapper(final Matcher<StorageRow> matcher) {
		this.matcher = matcher;
	}

	public boolean apply(final StorageRow input) {

		Description desc = new StringDescription();
		matcher.describeTo(desc);
		return matcher.matches(input);
	}
}
