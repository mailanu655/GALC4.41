package com.honda.shared.domain;

import java.io.Serializable;

/**
 * User: Jeffrey M Lutz
 * Date: 2/17/11
 */
public interface ValueObject<T> extends Serializable {

    /**
     * Value objects compare by the values of their attributes, they don't have an identity.
     *
     * @param other The other value object.
     * @return <code>true</code> if the given value object's and this value object's attributes are the same.
     */
    boolean sameValueAs(T other);

}