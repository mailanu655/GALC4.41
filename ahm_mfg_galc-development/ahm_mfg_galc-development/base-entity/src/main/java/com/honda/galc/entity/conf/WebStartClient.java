package com.honda.galc.entity.conf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.product.Feature;

/**
 * 
 * <h3>WebStartClient Class description</h3>
 * <p> WebStartClient description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jul 19, 2010
 *
 *
 */

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="WSCLIENTS_TBX")
public class WebStartClient extends AuditEntry {
	@Id
	@Column(name="IP_ADDRESS")
	private String ipAddress;

	@Column(name="HOST_NAME")
	private String hostName;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="BUILD_ID")
	private String buildId;
	
	@Column(name="ASSET_NUMBER")
	private String assetNumber;
	
	@Column(name="COLUMN_LOCATION")
	private String columnLocation;
	
	@Column(name="SHUTDOWN_FLAG")
	private Integer shutdownFlag;
	
	@Column(name="HEARTBEAT_TIMESTAMP")
	private Date heartBeatTimestamp;

	@Column(name = "FEATURE_TYPE")
    private String featureType;
    
    @Column(name = "FEATURE_ID")
    private String featureId;
    
    @Column(name = "PHONE_EXTENSION")
    private String phoneExtension;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="FEATURE_ID", referencedColumnName="FEATURE_ID", insertable=false, updatable=false),
		@JoinColumn(name="FEATURE_TYPE", referencedColumnName="FEATURE_TYPE", insertable=false, updatable=false)
		})
	private Feature feature;
	

	private static final long serialVersionUID = 1L;

	
	public WebStartClient() {
		super();
	}
	
	public WebStartClient(String ipAddress,String hostName,String description,String buildId) {
		
		this.ipAddress = ipAddress;
		this.hostName = hostName;
		this.description = description;
		this.buildId = buildId;
		
	}

	public WebStartClient(String ipAddress, String hostName,
			String description, String buildId, String assetNumber,
			String columnLocation, Integer shutdownFlag, String featureType,
			String featureId) {

		this(ipAddress, hostName, description, buildId);
		this.assetNumber = assetNumber;
		this.columnLocation = columnLocation;
		this.shutdownFlag = shutdownFlag;
		this.featureType = featureType;
		this.featureId = featureId;

	}

	public String getIpAddress() {
		return StringUtils.trim(this.ipAddress);
	}
	
	public String getId(){
		return getIpAddress();
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHostName() {
		return StringUtils.trim(this.hostName);
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBuildId() {
		return this.buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getColumnLocation() {
		return columnLocation;
	}

	public void setColumnLocation(String columnLocation) {
		this.columnLocation = columnLocation;
	}

	public int getShutdownFlag() {
		return this.shutdownFlag;
	}

	public boolean isShutdownFlagOn() {
		return this.shutdownFlag > -1;
	}

	public boolean isShutdownRequested() {
		return this.shutdownFlag > 0;
	}

	public void setShutdownFlag(int shutdownFlag) {
		this.shutdownFlag = shutdownFlag;
	}
	
	public Date getHeartBeatTimestamp() {
		return heartBeatTimestamp;
	}
	
	public void setHeartBeatTimestamp(Date heartBeatTimestamp) {
		this.heartBeatTimestamp = heartBeatTimestamp;
	}
	
	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	
	
	
	@Override
	public String toString() {
		return toString(getBuildId(),getHostName(),getIpAddress(),getFeatureType(),getFeatureId());
	}
	
	 @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebStartClient other = (WebStartClient) obj;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		if (featureType == null) {
			if (other.featureType != null)
				return false;
		} else if (!featureType.equals(other.featureType))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		return true;
	}
	 
	 @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		return result;
	}

	/**
	 * @return the phoneExtension
	 */
	public String getPhoneExtension() {
		return StringUtils.trimToEmpty(phoneExtension);
	}

	/**
	 * @param phoneExtension the phoneExtension to set
	 */
	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

}