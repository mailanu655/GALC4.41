package org.svenson;

import java.lang.annotation.*;

/**
 * Annotates a map or collection getter/setter with the type expected within the collection.
 *
 * @author fforw at gmx dot de
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
@Documented
public @interface JSONTypeHint {
    Class value();
}
