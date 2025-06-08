/**
 * DatabaseResult.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public class DatabaseResult implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected DatabaseResult(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    };

    public static final java.lang.String _Success = "Success";
    public static final java.lang.String _NoRowsAffected = "NoRowsAffected";
    public static final java.lang.String _DuplicateKeyConstraintViolation = "DuplicateKeyConstraintViolation";
    public static final java.lang.String _Unknown = "Unknown";
    public static final DatabaseResult Success = new DatabaseResult(_Success);
    public static final DatabaseResult NoRowsAffected = new DatabaseResult(_NoRowsAffected);
    public static final DatabaseResult DuplicateKeyConstraintViolation = new DatabaseResult(_DuplicateKeyConstraintViolation);
    public static final DatabaseResult Unknown = new DatabaseResult(_Unknown);
    public java.lang.String getValue() { return _value_;}
    public static DatabaseResult fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        DatabaseResult enumeration = (DatabaseResult)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static DatabaseResult fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}

    private java.lang.Object readResolve() throws java.io.ObjectStreamException {
        return fromValue(_value_);
    }
}
