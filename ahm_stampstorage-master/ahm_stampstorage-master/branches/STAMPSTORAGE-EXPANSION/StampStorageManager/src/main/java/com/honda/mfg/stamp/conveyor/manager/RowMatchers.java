package com.honda.mfg.stamp.conveyor.manager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 5/13/13 Time: 3:03 PM To
 * change this template use File | Settings | File Templates.
 */
public final class RowMatchers {

	private static final int ONE_PART_TYPE = 1;
	private static final Logger LOG = LoggerFactory.getLogger(RowMatchers.class);

	private RowMatchers() {

	}

	public static Matcher<StorageRow> hasMaxCapacityOf(final int maxCapacity) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.getCapacity().intValue() == maxCapacity;
			}

			public void describeTo(final Description desc) {
				desc.appendText("max capacity is ").appendValue(maxCapacity);
			}
		};
	}

	public static Matcher<StorageRow> isCurrentCapacityEmpty() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.isEmpty();
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow is EMPTY");
			}
		};
	}

	public static Matcher<StorageRow> isCurrentCapacityPartial() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return !storageRow.isEmpty() && !storageRow.isFull();
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow current capacity is PARTIAL");
			}
		};
	}

	public static Matcher<StorageRow> isCurrentCapacityMixed() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.getDieNumbersForAllCarriers().size() > ONE_PART_TYPE;
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow has carriers with many part types");
			}
		};
	}

	public static Matcher<StorageRow> isCurrentCapacityFull() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.isFull();
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow is FULL");
			}
		};
	}

	public static Matcher<StorageRow> hasCarriersWithSameDieNumber() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.getDieNumbersForAllCarriers().size() == ONE_PART_TYPE;
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow only has carriers with one part area ");
			}
		};
	}

	// MG Request from customer Exhaust Storein Rules - does not mix without empty
	public static Matcher<StorageRow> isLaneWithSingleDieNumberNotOf(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {

			@Override
			public void describeTo(final Description description) {
				description.appendText("StorageRow is partial but and doesn't have current die.")
						.appendValue(dieNumber);
			}

			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.getDieNumbersForAllCarriers().size() == ONE_PART_TYPE
						&& !storageRow.getDieNumbersForAllCarriers().contains(dieNumber);
			}

		};
	}

	public static Matcher<StorageRow> isLaneWithOnlySingleDieNumberOf(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.getDieNumbersForAllCarriers().contains(dieNumber)
						&& storageRow.getDieNumbersForAllCarriers().size() == ONE_PART_TYPE;
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow only has carriers with part area of ").appendValue(dieNumber);
			}
		};
	}

	public static Matcher<StorageRow> hasMixedDieNumbersIncluding(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.getDieNumbersForAllCarriers().contains(dieNumber)
						&& storageRow.getDieNumbersForAllCarriers().size() > ONE_PART_TYPE;
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow has carriers with many part types including  part area of ")
						.appendValue(dieNumber);
			}
		};
	}

	public static Matcher<StorageRow> hasCarrierAtTheLaneInWithDieNumber(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				Die die = storageRow.getCarrierAtLaneIn() != null && storageRow.getCarrierAtLaneIn().getDie() != null
						? storageRow.getCarrierAtLaneIn().getDie()
						: null;

				return !storageRow.isEmpty() && (die.getId().equals(dieNumber));
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow only has carriers with part area of ").appendValue(dieNumber);
			}
		};
	}

	public static Matcher<StorageRow> hasCarrierAtTheLaneOutWithDieNumber(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {

				Die die = storageRow.getCarrierAtRowOut() != null && storageRow.getCarrierAtRowOut().getDie() != null
						? storageRow.getCarrierAtRowOut().getDie()
						: null;
				if (die == null) {
					return false;
				} else {
					return !storageRow.isEmpty() && die.getId().equals(dieNumber);
				}
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow only has carriers with part area of ").appendValue(dieNumber);
			}
		};
	}

	public static Matcher<StorageRow> hasCarriersWithDieNumber(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				if (!storageRow.isEmpty() && !(storageRow.getDieNumbersForAllCarriers().contains(null)
						&& storageRow.getDieNumbersForAllCarriers().size() == ONE_PART_TYPE)) {
					return storageRow.getDieNumbersForAllCarriers().contains(dieNumber);
				} else {
					return false;
				}
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow only has carriers with part area of ").appendValue(dieNumber);
			}
		};
	}

	public static Matcher<StorageRow> hasEmptyCarriers() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return (storageRow.getDieNumbersForAllCarriers().contains(999L)
						|| storageRow.getDieNumbersForAllCarriers().contains(null))
						&& storageRow.getDieNumbersForAllCarriers().size() == ONE_PART_TYPE;
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow has empty carriers ");
			}
		};
	}

	public static Matcher<StorageRow> hasCarrierArrivedInLane() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.hasAnyCarrierArrivedInLane();
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow has carriers with prodRunNo");
			}
		};
	}

	public static Matcher<StorageRow> hasCarrierWithProdRunNoAndCarrierStatusNormal(final Integer prodRunNo) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.hasCarrierOfProdRunNo(prodRunNo) && storageRow.carrierAtLaneOutIsNotOnHold()
						&& storageRow.hasAnyCarrierArrivedInLane();
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow has carriers with prodRunNo");
			}
		};
	}

	public static Matcher<StorageRow> hasBlockedCarrierWithDieNumberAtTheLaneOut(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {

				Die die = storageRow.getCarrierAtRowOut() != null && storageRow.getCarrierAtRowOut().getDie() != null
						? storageRow.getCarrierAtRowOut().getDie()
						: null;
				if (die != null) {
					return !storageRow.isEmpty() && !(die.getId().equals(dieNumber));
				} else {
					return false;
				}
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow only has carriers with part area of ").appendValue(dieNumber);
			}
		};
	}

	public static Matcher<StorageRow> hasCarrierWithDieAndCarrierStatusShippable(final Long dieNumber) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return storageRow.hasShippableCarriersWithDieNumber(dieNumber);
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow has carriers with prodRunNo");
			}
		};
	}

	public static Matcher<StorageRow> isLaneAvailable() {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow storageRow) {
				return (storageRow.getAvailability() == StopAvailability.AVAILABLE);
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageRow is last released StorageRow");
			}
		};
	}

	public static Matcher<StorageRow> inStorageArea(final StorageArea area) {
		return new TypeSafeMatcher<StorageRow>() {
			@Override
			public boolean matchesSafely(final StorageRow row) {
				return row.getStorageArea().equals(area);
			}

			public void describeTo(final Description desc) {
				desc.appendText("StorageArea- ").appendValue(area.toString());
			}
		};
	}
}
