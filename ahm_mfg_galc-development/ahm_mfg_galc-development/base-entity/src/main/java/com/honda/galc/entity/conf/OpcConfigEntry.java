package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.OPCReadSource;
import com.honda.galc.entity.enumtype.OPCServerClientMode;
import com.honda.galc.entity.enumtype.OPCServerClientType;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * OpcConfigEntry represents OPC configuration entry
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Aug 31, 2009</TD>
 * <TD>&nbsp;</TD>
 * <TD>Created</TD>
 * </TR>
 * </TABLE>
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL650TBX")
public class OpcConfigEntry extends AuditEntry {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATA_READY_TAG")
    private String dataReadyTag;

    @Column(name = "OPC_INSTANCE_NAME")
    private String opcInstanceName;

    private int enabled;

    @Column(name = "NEEDS_LISTENER")
    private int needsListener;

    @Column(name = "DEVICE_ID")
    private String deviceId;

    @Column(name = "REPLY_DATA_READY_TAG")
    private String replyDataReadyTag;

    @Column(name = "SERVER_URL")
    private String serverUrl;

    @Column(name = "SERVER_CLIENT_TYPE")
    private int serverClientTypeId;

    @Column(name = "SERVER_CLIENT_MODE")
    private int serverClientModeId;

    @Column(name = "LISTENER_CLASS")
    private String listenerClass;

    @Column(name = "READ_SOURCE")
    private short readSourceId;
    
    @Column(name = "PROCESS_COMPLETE_TAG")
    private String processCompleteTag;

    public OpcConfigEntry() {
        super();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataReadyTag() {
        return StringUtils.trim(this.dataReadyTag);
    }

    public void setDataReadyTag(String dataReadyTag) {
        this.dataReadyTag = dataReadyTag;
    }

    public String getOpcInstanceName() {
        return StringUtils.trim(this.opcInstanceName);
    }

    public void setOpcInstanceName(String opcInstanceName) {
        this.opcInstanceName = opcInstanceName;
    }

    public int getEnabled() {
        return this.enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getNeedsListener() {
        return this.needsListener;
    }

    public void setNeedsListener(int needsListener) {
        this.needsListener = needsListener;
    }

    public String getDeviceId() {
        return StringUtils.trim(this.deviceId);
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getReplyDataReadyTag() {
        return StringUtils.trim(this.replyDataReadyTag);
    }

    public void setReplyDataReadyTag(String replyDataReadyTag) {
        this.replyDataReadyTag = replyDataReadyTag;
    }

    public String getServerUrl() {
        return StringUtils.trim(this.serverUrl);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getServerClientTypeId() {
        return this.serverClientTypeId;
    }

    public void setServerClientTypeId(int serverClientTypeId) {
        this.serverClientTypeId = serverClientTypeId;
    }

    public OPCServerClientType getServerClientType() {
        return OPCServerClientType.getType(serverClientTypeId);
    }

    public void setServerClientType(OPCServerClientType type) {
        this.serverClientTypeId = type.getId();
    }

    public int getServerClientModeId() {
        return this.serverClientModeId;
    }

    public void setServerClientModeId(int serverClientModeId) {
        this.serverClientModeId = serverClientModeId;
    }

    public OPCServerClientMode getServerClientMode() {
        return OPCServerClientMode.getType(serverClientModeId);
    }

    public void setServerClientMode(OPCServerClientMode mode) {
        this.serverClientModeId = mode.getId();
    }

    public String getListenerClass() {
        return StringUtils.trim(this.listenerClass);
    }

    public void setListenerClass(String listenerClass) {
        this.listenerClass = listenerClass;
    }

    public short getReadSourceId() {
        return this.readSourceId;
    }

    public void setReadSourceId(short readSourceId) {
        this.readSourceId = readSourceId;
    }

    public OPCReadSource getReadSource() {
        return OPCReadSource.getType(readSourceId);
    }

    public void setReadSource(OPCReadSource source) {
        this.readSourceId = (short) source.getId();
    }
    
    public String getProcessCompleteTag() {
		return processCompleteTag;
	}

	public void setProcessCompleteTag(String processCompleteTag) {
		this.processCompleteTag = processCompleteTag;
	}

	@Override
	public String toString() {
		return toString(getOpcInstanceName(),getDeviceId());
	}

}
