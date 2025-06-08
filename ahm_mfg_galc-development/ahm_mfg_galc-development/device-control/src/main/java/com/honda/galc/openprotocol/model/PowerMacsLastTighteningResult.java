package com.honda.galc.openprotocol.model;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
  * @author Vivek_Bettada
  * 2017-03-02
  */
@XStreamAlias("OPMessage")
public class PowerMacsLastTighteningResult extends AbstractOPMessage implements ILastTighteningResult 
{
        @Tag(name="TOTAL_NO_OF_MESSAGES")
        @XStreamAlias("TOTAL_NO_OF_MESSAGES")
        private String _totalNoOfMessages = "";

        @Tag(name="MESSAGE_NUMBER")
        @XStreamAlias("MESSAGE_NUMBER")
        private String _messageNumber = "";

        @Tag(name="DATA_NO_SYSTEM")
        @XStreamAlias("DATA_NO_SYSTEM")
        private String _tighteningId = "";

        @Tag(name="STATION_NO")
        @XStreamAlias("STATION_NO")
        private String _stationNo = "";

        @Tag(name="STATION_NAME")
        @XStreamAlias("STATION_NAME")
        private String _stationName = "";

        @Tag(name="TIME")
        @XStreamAlias("TIME")
        private String _time = "";

        @Tag(name="MODE_NO")
        @XStreamAlias("MODE_NO")
        private String _modeNo = "";

        @Tag(name="MODE_NAME")
        @XStreamAlias("MODE_NAME")
        private String _modeName = "";

        @Tag(name="SIMPLE_STATUS")
        @XStreamAlias("SIMPLE_STATUS")
        private String _simpleStatus;

        @Tag(name="PM_STATUS")
        @XStreamAlias("PM_STATUS")
        private String _pmStatus;

        @Tag(name="WP_ID")
        @XStreamAlias("WP_ID")
        private String _productId = "";

        @Tag(name="NUMBER_OF_BOLTS")
        @XStreamAlias("NUMBER_OF_BOLTS")
        private String _numberOfBolts = "";

        @Tag(name="ORDINAL_BOLT_NUMBER")
        @XStreamAlias("ORDINAL_BOLT_NUMBER")
        private String _ordinalBoltNumber = "";

        @Tag(name="SIMPLE_BOLT_STATUS")
        @XStreamAlias("SIMPLE_BOLT_STATUS")
        private String _simpleBoltStatus;

        @Tag(name="TORQUE_STATUS")
        @XStreamAlias("TORQUE_STATUS")
        private String _torqueStatus;

        @Tag(name="ANGLE_STATUS")
        @XStreamAlias("ANGLE_STATUS")
        private String _angleStatus;

        @Tag(name="BOLT_T")
        @XStreamAlias("BOLT_T")
        private String _boltT = "";

        @Tag(name="BOLT_A")
        @XStreamAlias("BOLT_A")
        private String _boltA = "";

        @Tag(name="BOLT_T_HIGH_LIMIT")
        @XStreamAlias("BOLT_T_HIGH_LIMIT")
        private String _boltTHighLimit = "";

        @Tag(name="BOLT_T_LOW_LIMIT")
        @XStreamAlias("BOLT_T_LOW_LIMIT")
        private String _boltTLowLimit = "";

        @Tag(name="BOLT_A_HIGH_LIMIT")
        @XStreamAlias("BOLT_A_HIGH_LIMIT")
        private String _boltAHighLimit = "";

        @Tag(name="BOLT_A_LOW_LIMIT")
        @XStreamAlias("BOLT_A_LOW_LIMIT")
        private String _boltALowLimit = "";

        @Tag(name="NUMBER_OF_SPECIAL_VALUES")
        @XStreamAlias("NUMBER_OF_SPECIAL_VALUES")
        private String _numberOfSpecialValues = "";

        public String getTotalNoOfMessages() {
                return _totalNoOfMessages;
        }

        public void setTotalNoOfMessages(String totalNoOfMessages) {
                _totalNoOfMessages = totalNoOfMessages;
        }

        public String getMessageNumber() {
                return _messageNumber;
        }

        public void setMessageNumber(String messageNumber) {
                _messageNumber = messageNumber;
        }

        public String getDataNoSystem() {
                return _tighteningId;
        }

        public void setDataNoSystem(String dataNoSystem) {
        	_tighteningId = dataNoSystem;
        }

        public String getStationNo() {
                return _stationNo;
        }

        public void setStationNo(String stationNo) {
                _stationNo = stationNo;
        }

        public String getStationName() {
                return _stationName;
        }

        public void setStationName(String stationName) {
                _stationName = stationName;
        }

        public String getTime() {
                return _time;
        }

        public void setTime(String time) {
                _time = time;
        }

        public String getModeNo() {
                return _modeNo;
        }

        public void setModeNo(String modeNo) {
                _modeNo = modeNo;
        }

        public String getModeName() {
                return _modeName;
        }

        public void setModeName(String modeName) {
                _modeName = modeName;
        }

        public int getSimpleStatus() {
            if(StringUtils.isBlank(_simpleStatus))  {
        		return 0;
        	}
        	else  {
                int val = 0;
                try {
					val = Integer.valueOf(_simpleStatus);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                return val;
        	}
        }

        public void setSimpleStatus(String simpleStatus) {
                _simpleStatus = simpleStatus;
        }

        public int getPmStatus() {
            if(StringUtils.isBlank(_pmStatus))  {
        		return 0;
        	}
        	else  {
                int val = 0;
                try {
					val = Integer.valueOf(_pmStatus);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                return val;
        	}
        }

        public void setPmStatus(String pmStatus) {
                _pmStatus = pmStatus;
        }

        public String getWpId() {
                return _productId;
        }

        public void setWpId(String wpId) {
        	_productId = wpId;
        }

        public String getNumberOfBolts() {
                return _numberOfBolts;
        }

        public void setNumberOfBolts(String numberOfBolts) {
                _numberOfBolts = numberOfBolts;
        }

        public String getOrdinalBoltNumber() {
                return _ordinalBoltNumber;
        }

        public void setOrdinalBoltNumber(String ordinalBoltNumber) {
                _ordinalBoltNumber = ordinalBoltNumber;
        }

        public int getSimpleBoltStatus() {
            if(StringUtils.isBlank(_simpleBoltStatus))  {
        		return 0;
        	}
        	else  {
                int val = 0;
                try {
					val = Integer.valueOf(_simpleBoltStatus);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                return val;
        	}
        }

        public void setSimpleBoltStatus(String simpleBoltStatus) {
                _simpleBoltStatus = simpleBoltStatus;
        }

        public int getTorqueStatus() {
            if(StringUtils.isBlank(_torqueStatus))  {
        		return 0;
        	}
        	else  {
                int val = 0;
                try {
					val = Integer.valueOf(_torqueStatus);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                return val;
        	}
        }

        public void setTorqueStatus(String torqueStatus) {
                _torqueStatus = torqueStatus;
        }

        public int getAngleStatus() {
        	if(StringUtils.isBlank(_angleStatus))  {
        		return 0;
        	}
        	else  {
                int val = 0;
                try {
					val = Integer.valueOf(_angleStatus);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                return val;
        	}
        }

        public void setAngleStatus(String angleStatus) {
                _angleStatus = angleStatus;
        }

        public String getBoltT() {
                return _boltT;
        }

        public void setBoltT(String boltT) {
                _boltT = boltT;
        }

        public String getBoltA() {
                return _boltA;
        }

        public void setBoltA(String boltA) {
                _boltA = boltA;
        }

        public String getBoltTHighLimit() {
                return _boltTHighLimit;
        }

        public void setBoltTHighLimit(String boltTHighLimit) {
                _boltTHighLimit = boltTHighLimit;
        }

        public String getBoltTLowLimit() {
                return _boltTLowLimit;
        }

        public void setBoltTLowLimit(String boltTLowLimit) {
                _boltTLowLimit = boltTLowLimit;
        }

        public String getBoltAHighLimit() {
                return _boltAHighLimit;
        }

        public void setBoltAHighLimit(String boltAHighLimit) {
                _boltAHighLimit = boltAHighLimit;
        }

        public String getBoltALowLimit() {
                return _boltALowLimit;
        }

        public void setBoltALowLimit(String boltALowLimit) {
                _boltALowLimit = boltALowLimit;
        }

        public String getNumberOfSpecialValues() {
                return _numberOfSpecialValues;
        }

        public void setNumberOfSpecialValues(String numberOfSpecialValues) {
                _numberOfSpecialValues = numberOfSpecialValues;
        }

		public double getAngle() {
			String sVal =  getBoltA();
        	if(StringUtils.isBlank(sVal))  {
        		return 0.0;
        	}
        	else  {
                double val = 0.0;
                try {
					val = Double.valueOf(sVal);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                return val;
        	}
		}

		public void setAngle(double angle) {
			setBoltA(String.valueOf(angle));			
		}

		public int getTighteningStatus() {
        	return getSimpleBoltStatus();
		}

		public double getTorque() {
			String sVal =  getBoltT();
        	if(StringUtils.isBlank(sVal))  {
        		return 0.0;
        	}
        	else  {
                double val = 0.0;
                try {
					val = Double.valueOf(sVal);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
                return val;
        	}
		}

		public void setTorque(double torque) {
			setBoltT(String.valueOf(torque));
			
		}

		public void setProductId(String productId) {
			setWpId(productId);
		}

		public String getProductId() {
			return getWpId();
		}

		public void setTighteningId(String id) {
			setDataNoSystem(id);			
		}

		public String getTighteningId() {
			return getDataNoSystem();
		}

}