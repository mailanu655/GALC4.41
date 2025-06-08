package com.honda.galc.client.codebroadcast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;

public class CodeBroadcastCode {
	private final String key;
	private final String label;
	private final String value;
	private final Set<ChangeListener> changeListeners;
	private boolean confirmed;

	public CodeBroadcastCode(final String key, final String label, final String value) {
		this.key = key;
		this.label = label;
		this.value = value;
		this.changeListeners = new HashSet<ChangeListener>();
		this.confirmed = false;
	}

	public String getKey() {
		return this.key;
	}

	public String getLabel() {
		return this.label;
	}

	public String getValue() {
		return this.value;
	}

	protected Set<ChangeListener> getChangeListeners() {
		return this.changeListeners;
	}

	/**
	 * Adds a ChangeListener to this code.<br>
	 * No modification is made if the given ChangeListener is already registered.
	 */
	public void addChangeListener(ChangeListener changeListener) {
		getChangeListeners().add(changeListener);
	}

	/**
	 * Removes a ChangeListener from this code.<br>
	 * No modification is made if the given ChangeListener is not already registered.
	 */
	public void removeChangeListener(ChangeListener changeListener) {
		getChangeListeners().remove(changeListener);
	}

	public boolean getConfirmed() {
		if (StringUtils.isBlank(getValue())) {
			return false;
		}
		return this.confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		boolean stateChanged = (this.confirmed != confirmed);
		this.confirmed = confirmed;
		if (stateChanged) {
			fireStateChanged();
		}
	}

	protected void fireStateChanged() {
		final ChangeEvent changeEvent = new ChangeEvent(this);
		Iterator<ChangeListener> iterator = getChangeListeners().iterator();
		while (iterator.hasNext()) {
			ChangeListener changeListener = iterator.next();
			changeListener.stateChanged(changeEvent);
		}
	}
}