package org.svenson.converter;

/**
 * Interface implemented by the repositories for {@link TypeConverter}s.
 *
 * @author fforw at gmx dot de
 * @see DefaultTypeConverterRepository
 */
public interface TypeConverterRepository {

    TypeConverter getConverterById(String id);

    <T extends TypeConverter> T getConverterByType(Class<T> t);
}
