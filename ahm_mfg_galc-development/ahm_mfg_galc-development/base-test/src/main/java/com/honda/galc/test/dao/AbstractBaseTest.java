package com.honda.galc.test.dao;

import java.io.File;
import java.lang.reflect.Proxy;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.AfOnSequenceDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.HttpServiceInvocationHandler;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.IService;
import com.honda.galc.service.TrackingService;
import com.honda.galc.test.data.setup.DataProvider;
import com.honda.galc.test.data.setup.Environment;

/**
 * @author Subu Kathiresan
 * @date Jan 26, 2012
 */
public abstract class AbstractBaseTest {

	protected final String USER_AGENT = "Mozilla/5.0";
	protected final int INVOCATION_SUCCESS = 201;
	protected final String SQL_FILE_EXT = ".sql";

	protected static final String sqlFileDir = "sqlfiles" + File.separator;
	protected static String url = "";
	
	protected FrameDao frameDao = null;
	protected EngineDao engineDao = null;
	protected InProcessProductDao inProcessProductDao = null;
	protected InstalledPartDao installedPartDao = null;
	protected ProductResultDao productResultDao = null;
	protected ComponentPropertyDao componentPropertyDao = null;
	protected AfOnSequenceDao afOnSequenceDao = null;
	protected ProcessPointDao processPointDao = null;
	
	public AbstractBaseTest() {
		String sqlFileName = System.getProperty("sqlFile");
		if (StringUtils.isEmpty(sqlFileName)) {
			sqlFileName = getClass().getSimpleName() + SQL_FILE_EXT;
		}
		executeSqlIfProvided(sqlFileName);	
	}

	@Rule 
	public static TestName name = new TestName();
	
	@Before
	public void before() {
		System.out.println("\nExecuting " + name.getMethodName() + "()");
		executeSqlIfProvided(name.getMethodName() + SQL_FILE_EXT);
	}
	
	@After
	public void after() {
		System.out.println("Finished executing " + name.getMethodName() + "()\n");
	}
	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		Environment env = getEnvironment();
		url = env.getUrl();
		System.out.println("Environment: " + env.name());
		System.out.println("Server URL : " + env.getUrl());
    }
    
	protected static Environment getEnvironment() {
		Environment env = Environment.LOCAL;
		try {
			env = Enum.valueOf(Environment.class, System.getProperty("environment"));
		} catch (Exception ex) {
			System.out.println("Environment is not specified, using HMIN_LOCAL");
		}
		return env;
	}

	protected static void executeSqlIfProvided(String sqlFileName) {
		System.out.println("Attempting to execute SQL file: " + sqlFileDir + sqlFileName);
		File file = new File(sqlFileDir + sqlFileName);
		if (file.exists()) {
			new DataProvider(getEnvironment(), isLogSql()).readAndExecuteSqlFromFile(sqlFileDir + sqlFileName);
		} else {
			System.out.println(file.getAbsolutePath() + " not found. Skipping SQL execution");
		}
	}
	
	protected static boolean isLogSql() {
		return Boolean.parseBoolean(System.getProperty("logSql"));
	}
	
	public void executeSqlFile(String filePath) {
		getDataProvider().readAndExecuteSqlFromFile(filePath);
	}
	
	public DataProvider getDataProvider() {
		Environment env = Enum.valueOf(Environment.class, System.getProperty("environment"));
		return new DataProvider(env, isLogSql());
	}
	
    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    }

	@Before
	public void setUpBeforeTest() throws Exception {}

    @After
    public void tearDownAfterTest() {
    }

	public TrackingService getTrackingService() {
		return getService(TrackingService.class);
	}

	public FrameDao getFrameDao() {
		if (frameDao == null) {
			frameDao = getDao(FrameDao.class);
		}
		return frameDao;
	}
	
	public EngineDao getEngineDao() {
		if (engineDao == null) {
			engineDao = getDao(EngineDao.class);
		}
		return engineDao;
	}
	
	public InProcessProductDao getInProcessProductDao() {
		if (inProcessProductDao == null) {
			inProcessProductDao = getDao(InProcessProductDao.class);
		}
		return inProcessProductDao;
	}
	
	public ProductResultDao getProductResultDao() {
		if (productResultDao == null) {
			productResultDao = getDao(ProductResultDao.class);
		}
		return productResultDao;
	}
	
	public ComponentPropertyDao getComponentPropertyDao() {
		if (componentPropertyDao == null) {
			componentPropertyDao = getDao(ComponentPropertyDao.class);
		}
		return componentPropertyDao;
	}
	
	public AfOnSequenceDao getAfOnSequenceDao() {
		if (afOnSequenceDao == null) {
			afOnSequenceDao = getDao(AfOnSequenceDao.class);
		}
		return afOnSequenceDao;
	}
	
	public ProcessPointDao getProcessPointDao() {
		if (processPointDao == null) {
			processPointDao = getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}
	
	public InstalledPartDao getInstalledPartDao() {
		if (installedPartDao == null) {
			installedPartDao = getDao(InstalledPartDao.class);
		}
		return installedPartDao;
	}
	
	public <T extends IDaoService<?, ?>> T getDao(Class<T> daoService) {
		return getProxyInstance(daoService);
	}

    public <T extends IService> T getService(Class<T> service) {
        return getProxyInstance(service);
    }

    @SuppressWarnings("unchecked")
	private <T> T getProxyInstance(Class<T> service) {
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[] {service}, 
				new HttpServiceInvocationHandler(url, service.getSimpleName()));
	}
}
