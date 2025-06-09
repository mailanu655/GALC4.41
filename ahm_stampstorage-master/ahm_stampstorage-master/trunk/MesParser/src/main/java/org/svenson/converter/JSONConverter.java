package org.svenson.converter;

import java.lang.annotation.*;

/**
 * Annotation that marks a Bean setter or getter method as being subject to conversion when generating or parsing JSON.
 *
 * @author fforw at gmx dot de
 * @see TypeConverter
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(value = ElementType.METHOD)
public @interface JSONConverter {
    String name() default "";

    Class<? extends TypeConverter> type() default TypeConverter.class;
}
