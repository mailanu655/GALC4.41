/**
 * NCATCollectorSoap.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf371106.07 v22511220251
 */

package ham.honda.com;

public interface NCATCollectorSoap extends java.rmi.Remote {
    public ham.honda.com.DatabaseResult headLightAlignmentInsert(ham.honda.com.HeadLightAlignmentBE headLightAlignmentBE) throws java.rmi.RemoteException;
    public ham.honda.com.DatabaseResult kemkraftDataInsert(ham.honda.com.KemkraftDataBE kemkraftDataBE) throws java.rmi.RemoteException;
    public ham.honda.com.DatabaseResult wheelAlignmentInsert(ham.honda.com.WheelAlignmentBE wheelAlignmentBE) throws java.rmi.RemoteException;
}
