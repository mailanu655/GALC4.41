package com.honda.shared.domain;

/**
 * User: jeffreylutz
 * Date: 3/6/11
 */
public interface Entity<T> {

    /**
     * Entities compare by identity, not by attributes.
     *
     * @param other The other entity.
     * @return true if the identities are the same, regardles of other attributes.
     */
    boolean sameIdentityAs(T other);
}
