/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductResultId;
import com.honda.galc.net.Request;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde
 * @Date May 4, 2012
 *
 */
public class IpuLinesideStationRefreshProcessor extends RefreshProcessor {

	public static final String LINESIDE_TERMINAL_KEY = "lineSideTerminal";
	public static final String REFRESH_CMD_EXECUTOR_CLASS = "com.honda.galc.client.datacollection.processor.RefreshCommandExecutor";
	public static final String REFRESH_CMD = "executeRefresh";
	public static final int REFRESH_TIMEOUT = 20000;
	
	private ApplicationContext _appContext = null;
	private TerminalDao _terminalDao = null;
	private ProductResultDao _productResultDao = null;
	protected DataCollectionController _controller = null;
	
	public IpuLinesideStationRefreshProcessor(ClientContext context) {
		super(context);
		_appContext = context.getAppContext();
	}

	public void registerDeviceListener(DeviceListener listener) {
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().debug("IpuLinesideStationRefreshProcessor : Enter execute()");
		
		try {
			sendRefresh(productId.getProductId());
			getController().getFsm().messageSentOk();
			Logger.getLogger().debug("IpuLinesideStationRefreshProcessor : Exit execute()");
		} catch(Exception ex) {
			ex.printStackTrace();
			getController().getFsm().messageSentNg();
			return false;
		}
		return true;
	}
	
	public void sendRefresh(final String productId) {
		ProductResultId productResultId = new ProductResultId();
		productResultId.setProcessPointId(context.getProcessPointId());
		productResultId.setProductId(productId);
		ProductResult productResult = new ProductResult();
		productResult.setId(productResultId);
		
		final ProductResult finalProductResult = productResult;
		
		Runnable notifyWorker = new Runnable() {
			public void run() {
				List<ProductResult> productResults = new ArrayList<ProductResult>();
			    long endTime = System.currentTimeMillis() + REFRESH_TIMEOUT;
				while ((productResults.size() < 1) && (System.currentTimeMillis() < endTime)){
					try {
						productResults = getProductResultDao().findByProductAndProcessPoint(finalProductResult);
					} catch (Exception ex) {
						ex.printStackTrace();
						getLogger().error(ex, "IpuLinesideStationRefreshProcessor: Unable to retrieve ProductResult for product " + productId);
					}
					
					try {
						getLogger().debug("IpuLinesideStationRefreshProcessor: Waiting for persistence to complete for product " + productId);
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				sendRefreshCmdToLineSideTerminal();
			}
		};

		Thread worker = new Thread(notifyWorker);
		worker.start();
	}
	
	/**
	 * send 'refresh' command to IPU line side terminal
	 */
	public boolean sendRefreshCmdToLineSideTerminal() {
		Terminal lineSideTerminal = null;
		try {
			lineSideTerminal = getLineSideTerminal();
			Socket socket = TCPSocketFactory.getSocket(lineSideTerminal.getIpAddress(), lineSideTerminal.getPort());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Object[] array = new Object[] {};
			Request req = new Request(REFRESH_CMD_EXECUTOR_CLASS, REFRESH_CMD, array);
			oos.writeObject(req);
			oos.flush();
			oos.close();
			getLogger().info("Refresh command successfully sent to line side terminal " + lineSideTerminal.getHostName());
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Exception occurred while transmitting the Refresh Command to Line Side Station " + (lineSideTerminal == null ? "" : lineSideTerminal.getHostName()));
			return false;
		}
		return true;
	}
	
	/**
	 * returns the line side terminal name which the headless terminal
	 * will communicate with
	 * 
	 * @return
	 */
	private Terminal getLineSideTerminal() {
		String lineSideTerminalName = PropertyService.getProperty(_appContext.getTerminalId(), LINESIDE_TERMINAL_KEY);
		return getTerminalDao().findByKey(lineSideTerminalName);
	}

	public TerminalDao getTerminalDao() {
		if(_terminalDao == null)
			_terminalDao = ServiceFactory.getDao(TerminalDao.class);
		return _terminalDao;
	}
	
	public ProductResultDao getProductResultDao() {
		if (_productResultDao == null) {
			_productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		}
		return _productResultDao;
	}
}
