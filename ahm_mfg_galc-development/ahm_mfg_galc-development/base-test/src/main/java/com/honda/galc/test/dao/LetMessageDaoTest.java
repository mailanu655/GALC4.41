package com.honda.galc.test.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Jan 21, 2016
 */
public class LetMessageDaoTest extends AbstractBaseTest {
	
	String productId = "";

	public LetMessageDaoTest() {
		
	}
	
	@Test
	public void saveValidMessages() {
		
		// get number  of records
		long i = 0;
		String xmlData = "";
		
		long startTime = System.currentTimeMillis();
		for (; i < 100; i++) {
			xmlData = readXmlFromFile(i);
			LetMessage letMessage = new LetMessage();
			letMessage.setMessageId(i);
			letMessage.setProductId(productId);
			letMessage.setActualTimestamp(new Timestamp(new Date().getTime()));
			letMessage.setTerminalId("T01");
			letMessage.setXmlMessageBody(xmlData);
			getDao(LetMessageDao.class).save(letMessage);
		}
		
		long timeTaken = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to save " + i + " LET xmls " + timeTaken + " ms");
		// assert number of records
	}
	
	@Test
	public void saveExceptionMessages() {
		
		// get number  of records
		long i = 0;
		String xmlData = "";
		
		long startTime = System.currentTimeMillis();
		for (; i < 100; i++) {
			xmlData = readXmlFromFile(i);
			LetMessage letMessage = new LetMessage();
			letMessage.setMessageId(i);
			letMessage.setProductId(productId);
			letMessage.setActualTimestamp(new Timestamp(new Date().getTime()));
			letMessage.setTerminalId("T01");
			letMessage.setExceptionMessageBody(xmlData + "blah blah blah");
			getDao(LetMessageDao.class).save(letMessage);
		}
		
		long timeTaken = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to save " + i + " LET xmls " + timeTaken + " ms");
		// assert number of records
	}

	@Test
	public void retrieveValidMessage() {
		long i = 0;
		long startTime = System.currentTimeMillis();

		ArrayList<LetMessage> msgs = new ArrayList<LetMessage>();
		for (; i < 100; i++) {
			msgs.add(getDao(LetMessageDao.class).findByKey(i));
		}
		long timeTaken = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to retrieve " + i + " LET xmls " + timeTaken + " ms");
	}
	
	@Test
	public void retrieveExceptionMessage() {
		long i = 0;
		long startTime = System.currentTimeMillis();

		ArrayList<LetMessage> msgs = new ArrayList<LetMessage>();
		for (; i < 100; i++) {
			msgs.add(getDao(LetMessageDao.class).findByKey(i));
		}
		long timeTaken = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to retrieve " + i + " LET xmls " + timeTaken + " ms");
	}
	
	private LetMessageDao getDao() {
		return ServiceFactory.getDao(LetMessageDao.class);	
	}
	
	private String readXmlFromFile(long iterationNum) {
		StringBuilder sb = new StringBuilder();
		productId = "19XFC1F9XGE0016" + StringUtil.padLeft(Long.toString(iterationNum), 2, '0');
		try {
			BufferedReader br = new BufferedReader(new FileReader("LetUnitTestFile.xml"));
			String nextLine = "";
			while ((nextLine = br.readLine()) != null) {
			    System.out.println("Writing: " + nextLine);
			    nextLine = nextLine.replace("@@VIN@@", productId);
			    sb.append(nextLine);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	    return sb.toString();
	}
}