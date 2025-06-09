package org.svenson;

import java.util.Set;

/**
 * Interface that allows classes to also have dynamic properties
 *
 * @author fforw at gmx dot de
 */
public interface DynamicProperties {
    /**
     * Sets the attribute with the given name to the given value.
     *
     * @param name
     * @param value if <code>null</code>, the attribute is removed.
     */
    void setProperty(String name, Object value);

    /**
     * returns value of the attribute with the given name.
     *
     * @param name
     */
    Object getProperty(String name);

    /**
     * Returns the set of available dynamic attribute names.
     *
     * @return
     */
    Set<String> propertyNames();


    /**
     * Returns <code>true</code> if this DynamicProperties object has a property with the given name.
     *
     * @param name
     * @return
     * @since 1.4
     */

    boolean hasProperty(String name);

    /**
     * Removes the property with the given name from this DynamicProperties object.
     *
     * @param name
     * @return the previous value of the removed property.
     * @since 1.4
     */
    Object removeProperty(String name);
}
