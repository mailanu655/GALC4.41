package com.honda.galc.client.immobi;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.device.lotcontrol.immobi.IImmobiDeviceListener;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiDeviceStatusInfo;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiMessage;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiMessageItem;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiMessageType;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiSerialDevice;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiStateMachine;
import com.honda.galc.client.device.lotcontrol.immobi.ImmobiStateMachineType;
import com.honda.galc.device.DeviceType;
import com.honda.galc.util.StringUtil;

import gnu.io.SerialPort;

public class LotControlImmobiTest implements IImmobiDeviceListener {

	public ImmobiSerialDevice immobiTestDevice = null;

	protected Map<String, Integer> deviceAccessKeys = new HashMap<String, Integer>();

	private boolean sendVinNg = false;
	private boolean sendMtocNg = false;
	private boolean sendRegNg = false;
	private boolean sendAbort = false;

	@Before
	public void before() {
		if (getImmobiDevice() == null) {
			immobiTestDevice = new ImmobiSerialDevice();
			immobiTestDevice.setBaudRate(9600);
			immobiTestDevice.setDataBits(SerialPort.DATABITS_7);
			immobiTestDevice.setType(DeviceType.Serial);
			immobiTestDevice.setName("immobi");
			immobiTestDevice.setPortName("COM1");
			immobiTestDevice.setParity(SerialPort.PARITY_EVEN);
			immobiTestDevice.setEnabled(true);
			immobiTestDevice.setStopBits(SerialPort.STOPBITS_2);
			immobiTestDevice.setTimeOut(90000);

		}
		if (!getImmobiDevice().isActive()) {
			getImmobiDevice().activate();
		}
		getImmobiDevice().registerListener(this);
	}

	@After
	public void after() {
		getImmobiDevice().unregisterListener(this);
		getImmobiDevice().getStateMachine().reset();
		getImmobiDevice().deActivate();
	}

	@Test(timeout = 1000)
	public void testImmobiProcessWithoutKeyScan() {
		try {
			getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITHOUT_KEYSCAN);
			getImmobiDevice().processVIN("5J6RM4H32GL000912", "GT1WYB600 YR578MX   T ", "100");
			int immobiState = getImmobiDevice().getCurrentState();
			boolean flag = true;
			while (flag) {
				if(immobiState ==ImmobiStateMachine.COMPLETED_VIN_PROCESSING){
					flag = false;
					break;
				}
				immobiState = getImmobiDevice().getCurrentState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 1000)
	public void testImmobiProcessWithoutKeyScanAndReceiveVinNoGood() {
		try {
			this.sendVinNg = true;
			getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITHOUT_KEYSCAN);
			getImmobiDevice().processVIN("5J6RM4H32GL000912", "GT1WYB600 YR578MX   T ", "100");
			int immobiState = getImmobiDevice().getCurrentState();
			boolean flag = true;
			while (flag) {
				if(immobiState ==ImmobiStateMachine.ABORT){
					flag = false;
					this.sendVinNg = false;
					break;
				}
				immobiState = getImmobiDevice().getCurrentState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 1000)
	public void testImmobiProcessWithoutKeyScanAndReceiveMtocNoGood() {
		try {
			this.sendMtocNg = true;
			getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITHOUT_KEYSCAN);
			getImmobiDevice().processVIN("5J6RM4H32GL000912", "GT1WYB600 YR578MX   T ", "100");
			int immobiState = getImmobiDevice().getCurrentState();
			boolean flag = true;
			while (flag) {
				if(immobiState ==ImmobiStateMachine.ABORT){
					flag = false;
					this.sendMtocNg = false;
					break;
				}
				immobiState = getImmobiDevice().getCurrentState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 1000)
	public void testImmobiProcessWithoutKeyScanAndReceiveRegNoGood() {
		try {
			this.sendRegNg = true;
			getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITHOUT_KEYSCAN);
			getImmobiDevice().processVIN("5J6RM4H32GL000912", "GT1WYB600 YR578MX   T ", "100");
			int immobiState = getImmobiDevice().getCurrentState();
			boolean flag = true;
			while (flag) {
				if(immobiState ==ImmobiStateMachine.ABORT){
					flag = false;
					this.sendRegNg = false;
					break;
				}
				immobiState = getImmobiDevice().getCurrentState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 1000)
	public void testImmobiProcessWithoutKeyScanAndReceiveAbort() {
		try {
			this.sendAbort = true;
			getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITH_KEYSCAN);
			getImmobiDevice().setFirstKeyScan("FIRST_KEY_SCAN");
			getImmobiDevice().setSecondKeyScan("SECOND_KEY_SCAN");
			getImmobiDevice().processVIN("5J6RM4H32GL000912", "GT1WYB600 YR578MX   T ", "100");
			int immobiState = getImmobiDevice().getCurrentState();
			boolean flag = true;
			while (flag) {
				if(immobiState ==ImmobiStateMachine.ABORT){
					flag = false;
					this.sendAbort = false;
					break;
				}
				immobiState = getImmobiDevice().getCurrentState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(timeout = 2000)
	public void testImmobiProcessWithKeyScan() {
		try {
			getImmobiDevice().setStateMachineType(ImmobiStateMachineType.WITH_KEYSCAN);
			getImmobiDevice().setFirstKeyScan("FIRST_KEY_SCAN");
			getImmobiDevice().setSecondKeyScan("SECOND_KEY_SCAN");
			getImmobiDevice().processVIN("5J6RM4H32GL000912", "GT1WYB600 YR578MX   T ", "100");

			int immobiState = getImmobiDevice().getCurrentState();
			boolean flag = true;
			while (flag) {
				if(immobiState ==ImmobiStateMachine.COMPLETED_VIN_PROCESSING_WITH_KEYSCAN){
					flag = false;
					break;
				}
				immobiState = getImmobiDevice().getCurrentState();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void handleStatusChange(ImmobiDeviceStatusInfo statusInfo) {

		try {
			int immobiState = statusInfo.getState();

			System.out.println(" listener : " + statusInfo.getDisplayMessage());
			if (immobiState == ImmobiStateMachine.READY_FOR_SEND_VIN) {

				ImmobiMessage immobiMsg = ImmobiMessageType.vin_ack.createMessage();
				String segmentData = sendVinNg ? "NOT_EXPECTED_VIN" : getImmobiDevice().getVIN();
				if (segmentData != null) {
					immobiMsg.setLength(StringUtil.padLeft(
							Integer.toString(
									ImmobiMessage.HEADER_SIZE + ImmobiMessage.LENGTH_FIELD_SIZE + segmentData.length()),
							ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setDataSegmentLength(
							StringUtil.padLeft(Integer.toString(segmentData.length() + ImmobiMessage.LENGTH_FIELD_SIZE),
									ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setSegmentData(segmentData);
				}

				getImmobiDevice().getMessageHandler().enqueue(getMsgIem(immobiMsg.toString()));
			} else if (immobiState == (ImmobiStateMachine.VIN_ACK + ImmobiStateMachine.SEND_MTOC)) {

				ImmobiMessage immobiMsg = ImmobiMessageType.mtoc_ack.createMessage();
				String segmentData = sendMtocNg ? "NOT_EXPECTED_MTOC" : getImmobiDevice().getMTOC();
				if (segmentData != null) {
					immobiMsg.setLength(StringUtil.padLeft(
							Integer.toString(
									ImmobiMessage.HEADER_SIZE + ImmobiMessage.LENGTH_FIELD_SIZE + segmentData.length()),
							ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setDataSegmentLength(
							StringUtil.padLeft(Integer.toString(segmentData.length() + ImmobiMessage.LENGTH_FIELD_SIZE),
									ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setSegmentData(segmentData);
				}

				getImmobiDevice().getMessageHandler().enqueue(getMsgIem(immobiMsg.toString()));
			} else if (immobiState == (ImmobiStateMachine.VIN_ACK + ImmobiStateMachine.SEND_MTOC
					+ ImmobiStateMachine.MTOC_ACK + ImmobiStateMachine.SEND_KEY_ID)) {
				if (sendAbort) {
					ImmobiMessage immobiMsg = ImmobiMessageType.abort.createMessage();
					getImmobiDevice().getMessageHandler().enqueue(getMsgIem(immobiMsg.toString()));
				} else {

					ImmobiMessage immobiMsg = ImmobiMessageType.key_ack.createMessage();
					String segmentData = "";
					if (segmentData != null) {
						immobiMsg
								.setLength(
										StringUtil.padLeft(
												Integer.toString(ImmobiMessage.HEADER_SIZE
														+ ImmobiMessage.LENGTH_FIELD_SIZE + segmentData.length()),
										ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
						immobiMsg.setDataSegmentLength(StringUtil.padLeft(
								Integer.toString(segmentData.length() + ImmobiMessage.LENGTH_FIELD_SIZE),
								ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
						immobiMsg.setSegmentData(segmentData);
					}

					getImmobiDevice().getMessageHandler().enqueue(getMsgIem(immobiMsg.toString()));
				}
			} else if (immobiState == (ImmobiStateMachine.VIN_ACK + ImmobiStateMachine.SEND_MTOC
					+ ImmobiStateMachine.MTOC_ACK + ImmobiStateMachine.SEND_MTOC_OK)) {

				ImmobiMessage immobiMsg = ImmobiMessageType.reg_done.createMessage();
				String segmentData = sendRegNg ? "NOT_EXPECTED_VIN, NOT_EXPECTED_MTOC"
						: getImmobiDevice().getVIN() + "," + getImmobiDevice().getMTOC();
				if (segmentData != null) {
					immobiMsg.setLength(StringUtil.padLeft(
							Integer.toString(
									ImmobiMessage.HEADER_SIZE + ImmobiMessage.LENGTH_FIELD_SIZE + segmentData.length()),
							ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setDataSegmentLength(
							StringUtil.padLeft(Integer.toString(segmentData.length() + ImmobiMessage.LENGTH_FIELD_SIZE),
									ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setSegmentData(segmentData);
				}
				getImmobiDevice().getMessageHandler().enqueue(getMsgIem(immobiMsg.toString()));

			} else if (immobiState == (ImmobiStateMachine.VIN_ACK + ImmobiStateMachine.SEND_MTOC
					+ ImmobiStateMachine.MTOC_ACK + ImmobiStateMachine.SEND_KEY_ID + ImmobiStateMachine.KEY_ID_ACK
					+ ImmobiStateMachine.SEND_KEY_ID_OK)) {

				ImmobiMessage immobiMsg = ImmobiMessageType.reg_done.createMessage();
				String segmentData = sendRegNg ? "NOT_EXPECTED_VIN, NOT_EXPECTED_MTOC"
						: getImmobiDevice().getVIN() + "," + getImmobiDevice().getMTOC();
				if (segmentData != null) {
					immobiMsg.setLength(StringUtil.padLeft(
							Integer.toString(
									ImmobiMessage.HEADER_SIZE + ImmobiMessage.LENGTH_FIELD_SIZE + segmentData.length()),
							ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setDataSegmentLength(
							StringUtil.padLeft(Integer.toString(segmentData.length() + ImmobiMessage.LENGTH_FIELD_SIZE),
									ImmobiMessage.LENGTH_FIELD_SIZE, '0'));
					immobiMsg.setSegmentData(segmentData);
				}

				getImmobiDevice().getMessageHandler().enqueue(getMsgIem(immobiMsg.toString()));
			} else if (immobiState == ImmobiStateMachine.COMPLETED_VIN_PROCESSING) {
				System.out.println("Completed Processing");
				getImmobiDevice().setState(ImmobiStateMachine.COMPLETED_VIN_PROCESSING);
			} else if (immobiState == ImmobiStateMachine.COMPLETED_VIN_PROCESSING_WITH_KEYSCAN) {
				System.out.println("Completed Processing With Key scans");
				getImmobiDevice().setState(ImmobiStateMachine.COMPLETED_VIN_PROCESSING_WITH_KEYSCAN);
			} else {
				String errorMessage = getImmobiDevice().getStateMachine().getErrorMessage();
				System.out.println(errorMessage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private ImmobiSerialDevice getImmobiDevice() {
		// TODO Auto-generated method stub
		return this.immobiTestDevice;
	}

	public void controlGranted(String deviceId) {
		// TODO Auto-generated method stub

	}

	public void controlRevoked(String deviceId) {
		// TODO Auto-generated method stub

	}

	public String getApplicationName() {
		return "ImmobiApplication";
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}

	public void cleanUp() {
		// TODO Auto-generated method stub

	}

	public Integer getDeviceAccessKey(String deviceId) {
		return deviceAccessKeys.get(deviceId);
	}

	public void setDeviceAccessKey(String deviceId, Integer key) {
		deviceAccessKeys.put(deviceId, key);
	}

	public void update(Object obj) {
		// TODO Auto-generated method stub

	}

	public String getListenerId() {
		// TODO Auto-generated method stub
		return "immobiTestListener";
	}
	
	public ImmobiMessageItem getMsgIem(String message) {
		return new ImmobiMessageItem(getImmobiDevice(), message, getImmobiDevice().getLogger());
	}
}
