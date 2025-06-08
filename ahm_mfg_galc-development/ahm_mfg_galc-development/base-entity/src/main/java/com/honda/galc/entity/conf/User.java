package com.honda.galc.entity.conf;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "GAL105TBX")
public class User extends AuditEntry{
    @Id
    @Column(name = "USER_ID")
    @Auditable(isPartOfPrimaryKey = true, sequence = 1)
    private String userId;

    @Column(name = "ROW_INSERT_TSTMP")
    private Timestamp rowInsertTstmp;

    @Column(name = "ROW_UPDATE_TSTMP")
    private Timestamp rowUpdateTstmp;

    @Column(name = "ROW_DISUSE_TSTMP")
    private Timestamp rowDisuseTstmp;

    @Column(name = "ROW_HISTRC_CNTER")
    @Auditable(isPartOfPrimaryKey = true, sequence = 5)
    private int expireDays;

    @Column(name = "ROW_UPDATE_USER")
    private String rowUpdateUser;
   
   
    @Auditable(isPartOfPrimaryKey = true, sequence = 3)
    private String passwd;

    @Column(name = "PASSWD_1")
    private String passwd1;

    @Column(name = "PASSWD_2")
    private String passwd2;

    @Column(name = "PASSWD_3")
    private String passwd3;

    @Column(name = "PASSWD_4")
    private String passwd4;

    @Column(name = "PASSWD_5")
    private String passwd5;

    @Column(name = "PASSWD_UPD_DT")
    @Auditable(isPartOfPrimaryKey = true, sequence = 4)
    private String passwdUpdateDate;

    @Column(name = "ERROR_FREQ_1")
    private short errorFreq1;

    @Column(name = "ERROR_FREQ_2")
    private short errorFreq2;

    @Column(name = "LOGIN_STATUS")
    private String loginStatus;

    @Column(name = "USER_NAME")
    @Auditable(isPartOfPrimaryKey = true, sequence = 2)
    private String userName;

    private static final long serialVersionUID = 1L;
    
    private static final String DATE_FORMAT = "yyyyMMdd";

    public User() {
        super();
    }

    public String getUserId() {
        return StringUtils.trimToEmpty(this.userId);
    }
    
    public String getId() {
    	return StringUtils.trimToEmpty(getUserId());
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getRowInsertTstmp() {
        return this.rowInsertTstmp;
    }

    public void setRowInsertTstmp(Timestamp rowInsertTstmp) {
        this.rowInsertTstmp = rowInsertTstmp;
    }

    public Timestamp getRowUpdateTstmp() {
        return this.rowUpdateTstmp;
    }

    public void setRowUpdateTstmp(Timestamp rowUpdateTstmp) {
        this.rowUpdateTstmp = rowUpdateTstmp;
    }

    public Timestamp getRowDisuseTstmp() {
        return this.rowDisuseTstmp;
    }

    public void setRowDisuseTstmp(Timestamp rowDisuseTstmp) {
        this.rowDisuseTstmp = rowDisuseTstmp;
    }

    public int getExpireDays() {
        return this.expireDays;
    }

    public void setExpireDays(int expireDays) {
        this.expireDays = expireDays;
    }

    public String getRowUpdateUser() {
        return StringUtils.trimToEmpty(this.rowUpdateUser);
    }

    public void setRowUpdateUser(String rowUpdateUser) {
        this.rowUpdateUser = rowUpdateUser;
    }

    public String getPasswd() {
        return StringUtils.trimToEmpty(this.passwd);
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswd1() {
        return StringUtils.trimToEmpty(this.passwd1);
    }

    public void setPasswd1(String passwd1) {
        this.passwd1 = passwd1;
    }

    public String getPasswd2() {
        return StringUtils.trimToEmpty(this.passwd2);
    }

    public void setPasswd2(String passwd2) {
        this.passwd2 = passwd2;
    }

    public String getPasswd3() {
        return StringUtils.trimToEmpty(this.passwd3);
    }

    public void setPasswd3(String passwd3) {
        this.passwd3 = passwd3;
    }

    public String getPasswd4() {
        return StringUtils.trimToEmpty(this.passwd4);
    }

    public void setPasswd4(String passwd4) {
        this.passwd4 = passwd4;
    }

    public String getPasswd5() {
        return StringUtils.trimToEmpty(this.passwd5);
    }

    public void setPasswd5(String passwd5) {
        this.passwd5 = passwd5;
    }

    public Date getPasswordUpdateDate() {
    	SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    	Date date = null;
		try {
			date = formatter.parse(passwdUpdateDate);
		} catch (ParseException e) {
			date = new Date(System.currentTimeMillis());
		}
		return date;
    }
    
    public String getPasswordUpdateDateString() {
        return StringUtils.trimToEmpty(this.passwdUpdateDate);
        
    }    

    public void setPasswordUpdateDate(String passwdUpdateDate) {
        this.passwdUpdateDate = passwdUpdateDate;
    }

    public short getErrorFreq1() {
        return this.errorFreq1;
    }

    public void setErrorFreq1(short errorFreq1) {
        this.errorFreq1 = errorFreq1;
    }

    public short getErrorFreq2() {
        return this.errorFreq2;
    }

    public void setErrorFreq2(short errorFreq2) {
        this.errorFreq2 = errorFreq2;
    }

    public String getLoginStatus() {
        return StringUtils.trimToEmpty(this.loginStatus);
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUserName() {
        return StringUtils.trimToEmpty(this.userName);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public boolean isPasswordExpired() {
    	if(expireDays <= 0) return false;
    	long expireTime = this.getPasswordUpdateDate().getTime() + 
    	         (24L*60*60*100*expireDays);
    	return expireTime < System.currentTimeMillis();
    }
    

	@Override
	public String toString() {
		return "User [userId=" + userId + ", rowInsertTstmp=" + rowInsertTstmp + ", rowUpdateTstmp=" + rowUpdateTstmp
				+ ", rowDisuseTstmp=" + rowDisuseTstmp + ", expireDays=" + expireDays + ", rowUpdateUser="
				+ rowUpdateUser + ", passwd=" + passwd + ", passwd1=" + passwd1 + ", passwd2=" + passwd2 + ", passwd3="
				+ passwd3 + ", passwd4=" + passwd4 + ", passwd5=" + passwd5 + ", passwdUpdateDate=" + passwdUpdateDate
				+ ", errorFreq1=" + errorFreq1 + ", errorFreq2=" + errorFreq2 + ", loginStatus=" + loginStatus
				+ ", userName=" + userName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + errorFreq1;
		result = prime * result + errorFreq2;
		result = prime * result + expireDays;
		result = prime * result + ((loginStatus == null) ? 0 : loginStatus.hashCode());
		result = prime * result + ((passwd == null) ? 0 : passwd.hashCode());
		result = prime * result + ((passwd1 == null) ? 0 : passwd1.hashCode());
		result = prime * result + ((passwd2 == null) ? 0 : passwd2.hashCode());
		result = prime * result + ((passwd3 == null) ? 0 : passwd3.hashCode());
		result = prime * result + ((passwd4 == null) ? 0 : passwd4.hashCode());
		result = prime * result + ((passwd5 == null) ? 0 : passwd5.hashCode());
		result = prime * result + ((passwdUpdateDate == null) ? 0 : passwdUpdateDate.hashCode());
		result = prime * result + ((rowDisuseTstmp == null) ? 0 : rowDisuseTstmp.hashCode());
		result = prime * result + ((rowInsertTstmp == null) ? 0 : rowInsertTstmp.hashCode());
		result = prime * result + ((rowUpdateTstmp == null) ? 0 : rowUpdateTstmp.hashCode());
		result = prime * result + ((rowUpdateUser == null) ? 0 : rowUpdateUser.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (errorFreq1 != other.errorFreq1)
			return false;
		if (errorFreq2 != other.errorFreq2)
			return false;
		if (expireDays != other.expireDays)
			return false;
		if (loginStatus == null) {
			if (other.loginStatus != null)
				return false;
		} else if (!loginStatus.equals(other.loginStatus))
			return false;
		if (passwd == null) {
			if (other.passwd != null)
				return false;
		} else if (!passwd.equals(other.passwd))
			return false;
		if (passwd1 == null) {
			if (other.passwd1 != null)
				return false;
		} else if (!passwd1.equals(other.passwd1))
			return false;
		if (passwd2 == null) {
			if (other.passwd2 != null)
				return false;
		} else if (!passwd2.equals(other.passwd2))
			return false;
		if (passwd3 == null) {
			if (other.passwd3 != null)
				return false;
		} else if (!passwd3.equals(other.passwd3))
			return false;
		if (passwd4 == null) {
			if (other.passwd4 != null)
				return false;
		} else if (!passwd4.equals(other.passwd4))
			return false;
		if (passwd5 == null) {
			if (other.passwd5 != null)
				return false;
		} else if (!passwd5.equals(other.passwd5))
			return false;
		if (passwdUpdateDate == null) {
			if (other.passwdUpdateDate != null)
				return false;
		} else if (!passwdUpdateDate.equals(other.passwdUpdateDate))
			return false;
		if (rowDisuseTstmp == null) {
			if (other.rowDisuseTstmp != null)
				return false;
		} else if (!rowDisuseTstmp.equals(other.rowDisuseTstmp))
			return false;
		if (rowInsertTstmp == null) {
			if (other.rowInsertTstmp != null)
				return false;
		} else if (!rowInsertTstmp.equals(other.rowInsertTstmp))
			return false;
		if (rowUpdateTstmp == null) {
			if (other.rowUpdateTstmp != null)
				return false;
		} else if (!rowUpdateTstmp.equals(other.rowUpdateTstmp))
			return false;
		if (rowUpdateUser == null) {
			if (other.rowUpdateUser != null)
				return false;
		} else if (!rowUpdateUser.equals(other.rowUpdateUser))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

  
    
}
