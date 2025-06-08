package com.honda.galc.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * @author Subu Kathiresan
 * @date Jan 25, 2012
 */
public class ProductTrackerTest extends AbstractBaseTest {

	public static final int STATUS_OK = 1;
	protected static int waitTimeInSecs = 20;
	protected static int numOfProducts = 10;
	protected static String associateNo = "associate01";
	protected static String approverNo =  "approver01";
	
	protected static final int MAX_PRODUCTS = 100;	// only 100 products are set up in the db scripts

	public ProductTrackerTest() {
		try {
			waitTimeInSecs = Integer.parseInt(System.getProperty("waitTimeInSecs"));
			numOfProducts = Integer.parseInt(System.getProperty("numOfProducts"));
			numOfProducts = (numOfProducts > MAX_PRODUCTS) ? MAX_PRODUCTS : numOfProducts;
		} catch(Exception ex){}
	}
	
	/**
	 * product is neither the head nor the tail in line X
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario1_productNotHeadOrTail() {
		String processPointId = "PRODTR_PP02";
		String lineId = "PRODTR_LINE02";
		assertProductHistoryCount(0, processPointId);
		for (int i = 3, j = 1; j <= numOfProducts; j++, i++) {
			trackInNewThread(i, processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(numOfProducts+1, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}

	/**
	 * product is the head of line X
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario2_productIsHead() {
		String processPointId = "PRODTR_PP04";
		String lineId = "PRODTR_LINE04";
		assertProductHistoryCount(0, processPointId);
		for (int i = 201, j = 1; j <= numOfProducts; j++, i++) {
			trackFrame(getFrame(i), processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(numOfProducts+1, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}

	/**
	 * product is the tail of line X
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario3_productIsTail() {
		String processPointId = "PRODTR_PP06";
		String lineId = "PRODTR_LINE06";
		assertProductHistoryCount(0, processPointId);
		for (int i = 400+numOfProducts, j = 1; j <= numOfProducts; j++, i--) {
			trackFrame(getFrame(i), processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(numOfProducts+1, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}

	/**
	 * product does not exist in any line
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario4_productDoesNotExist() {
		String processPointId = "PRODTR_PP08";
		String lineId = "PRODTR_LINE08";
		assertProductHistoryCount(0, processPointId);
		for (int i = 601, j = 1; j <= numOfProducts; j++, i++) {
			trackInNewThread(i, processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(numOfProducts+1, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}

	/**
	 * product exists, but target line Y is empty
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario5_targetLineIsEmpty() {
		String processPointId = "PRODTR_PP10";
		String lineId = "PRODTR_LINE10";
		assertProductHistoryCount(0, processPointId);
		for (int i = 801, j = 1; j <= numOfProducts; j++, i++) {
			trackInNewThread(i, processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(numOfProducts, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}

	/**
	 * product exists, target line Y has only one product
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario6_targetLineHasOneProduct() {
		String processPointId = "PRODTR_PP12";
		String lineId = "PRODTR_LINE12";
		assertProductHistoryCount(0, processPointId);
		for (int i = 1001, j = 1; j <= numOfProducts; j++, i++) {
			trackInNewThread(i, processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(numOfProducts+1, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}

	/**
	 * product n does not exist in any line and target line Y is empty
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario7_productDoesNotExistAndLineYIsEmpty() {
		String processPointId = "PRODTR_PP14";
		String lineId = "PRODTR_LINE14";
		assertProductHistoryCount(0, processPointId);
		for (int i = 1201, j = 1; j <= numOfProducts; j++, i++) {
			trackInNewThread(i, processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(numOfProducts, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}

	/**
	 * move product in the same line X
	 * TRACKING_POINT_FLAG = 0, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario8_moveProductInSameLine() {
		String processPointId = "PRODTR_PP15";
		String lineId = "PRODTR_LINE15";
		assertProductHistoryCount(0, processPointId);
		for (int i = 1401, j = 1; j <= numOfProducts; j++, i++) {
			trackInNewThread(i, processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}
	
	/**
	 * move product in the same line  X with multiple process points
	 * access product at the same time.
	 * TRACKING_POINT_FLAG = 0, PASSING_COUNT_FLAG = 1
	 * 
	 * @RGALCDEV-1023
	 */
	@Test
	public void scenario9_moveProductInSameLineConcurrentPP() {
		String processPointId = "PRODTR_PP";
		String lineId = "PRODTR_LINE15";
		int waitTime = waitTimeInSecs;
		
		for (int ppidx = 100; ppidx < 129; ppidx++) {
			assertProductHistoryCount(0, processPointId + ppidx);
		}
		
		for (int i = 1401, j = 1; j <= numOfProducts; j++, i++) {
			for (int ppidx = 100; ppidx <= 129; ppidx++) {
				trackInNewThread(i, processPointId + ppidx, false);
			}
		}
		
		wait(60);
		for (int ppidx = 100; ppidx < 129; ppidx++) {
			System.out.println("\nTracking results for " + processPointId+ppidx);
			assertProductHistoryCount(numOfProducts, waitTime, processPointId + ppidx);
		}
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}
	
	/**
	 * add product to line 
	 * PP1: TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 * 
	 * move product in the same line
	 * PP2: TRACKING_POINT_FLAG = 0, PASSING_COUNT_FLAG = 1
	 * 
	 * track product at both PP1 & PP2 concurrently 
	 * 
	 * @RGALCDEV-4638
	 */
	@Test
	public void scenario10_multipleLinesWithDifferentTrackingFlags() {
		String processPointId1 = "PRODTR_PP14";		// Flags 1,1
		String processPointId2 = "PRODTR_PP19";		// Flags 0,1
		String lineId1 = "PRODTR_LINE14";
		String lineId2 = "PRODTR_LINE19";
		int waitTime = waitTimeInSecs;
		
		assertProductHistoryCount(0, processPointId1);
		assertProductHistoryCount(0, processPointId2);
		
		for (int i = 1201, j = 1; j <= numOfProducts; j++, i++) {
			trackFrame(getFrame(i), processPointId1, false);
			trackFrame(getFrame(i), processPointId2, false);
			wait(2);
		}
		
		for (int i = 1201, j = 1; j <= numOfProducts; j++, i++) {
			assertProductLastPassingPPId(waitTime, processPointId2, getFrame(i).getProductId());
		}
		
		assertProductHistoryCount(numOfProducts, waitTime, processPointId1);
		assertProductHistoryCount(numOfProducts, waitTime, processPointId2);
		
		assertLinkedListCount(numOfProducts, waitTimeInSecs, lineId1);
		assertOneTailPerLine(0, lineId1);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId2);
		assertOneTailPerLine(0, lineId2);
	}

	/**
	 * re-process product in the same line Y 
	 * product is next to tail in line Y
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario11_productIsNextToTailInTargetLine() {
		String processPointId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		assertProductHistoryCount(0, processPointId);
		for (int j = 1; j <= numOfProducts; j++) {
			if (j % 2 == 1) {
				trackFrame(getFrame(1599), processPointId);
			} else {
				trackFrame(getFrame(1600), processPointId);
			}
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}
	
	/**
	 * re-process product in the same line Y 
	 * product is next to head in line Y
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario12_productIsNextToHeadInTargetLine() {
		executeSqlFile(getInProcessSetupScript());
		String processPointId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		assertProductHistoryCount(0, processPointId);
		for (int i = 1502, j = 1; j <= numOfProducts; j++, i++) {
			trackInNewThread(i, processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}
	
	/**
	 * re-process product in the same line Y 
	 * product is neither head nor tail in line Y
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario13_productIsNeitherHeadNorTailInTargetLine() {
		executeSqlFile(getInProcessSetupScript());
		String processPointId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		assertProductHistoryCount(0, processPointId);
		for (int i = 1503, j = 1; j <= numOfProducts; j++, i++) {
			trackFrame(getFrame(i), processPointId);
		}
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
	}
	
	/**
	 * re-process product in the same line Y 
	 * product is already the tail in line Y
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario14_productIsTailInTargetLine() {
		executeSqlFile(getInProcessSetupScript());
		String processPointId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		assertProductHistoryCount(0, processPointId);
		trackFrame(new Frame("SIM0PRODTRACK1600"), processPointId);
		assertProductHistoryCount(1, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(0, lineId);
		assertTailProduct(2, lineId, "SIM0PRODTRACK1600");
	}
	
	/**
	 * re-process product in the same line Y 
	 * product is the head in line Y
	 * TRACKING_POINT_FLAG = 1, PASSING_COUNT_FLAG = 1
	 */
	@Test
	public void scenario15_productIsHeadInTargetLine() {
		executeSqlFile(getInProcessSetupScript());
		String processPointId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		assertProductHistoryCount(0, processPointId);
		trackFrame(new Frame("SIM0PRODTRACK1501"), processPointId);
		assertProductHistoryCount(1, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, "SIM0PRODTRACK1501");
	}
	
	/**
	 * product is tracked with actual timestamp. In this scenario..  
	 *   -create a product history
	 *   -re-arrange Product Sequence (linked list) 
	 *   -update Product Sequence LAST_PASSING_PROCESS_POINT_ID
	 *   -update Product TRACKING_STAUS 
	 *   -update Product LAST_PASSING_PROCESS_POINT_ID
	 */
	@Test
	public void scenario16_trackWithActualTimestamp() {
		executeSqlFile(getInProcessSetupScript());
		String notSoRecentPPId = "PRODTR_PP15";
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		
		assertProductHistoryCount(0, notSoRecentPPId);
		assertProductHistoryCount(0, mostRecentPPId);
		
		trackFrameUsingProductHistory(productId, notSoRecentPPId, getFrameDao().getDatabaseTimeStamp());
		wait(5);
		trackFrameUsingProductHistory(productId, mostRecentPPId, getFrameDao().getDatabaseTimeStamp());
		
		assertProductHistoryCount(1, waitTimeInSecs, notSoRecentPPId);
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
		
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * product is tracked with actual timestamp in the past. In this scenario..  
	 *   -create a product history
	 *   -don't re-arrange Product Sequence (linked list) 
	 *   -don't update Product Sequence LAST_PASSING_PROCESS_POINT_ID
	 *   -don't update Product TRACKING_STAUS 
	 *   -don't update Product LAST_PASSING_PROCESS_POINT_ID
	 */
	@Test
	public void scenario17_trackWithNotSoRecentActualTimestamp() {
		executeSqlFile(getInProcessSetupScript());
		String notSoRecentPPId = "PRODTR_PP15";
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		
		assertProductHistoryCount(0, notSoRecentPPId);
		assertProductHistoryCount(0, mostRecentPPId);
		
		Timestamp notSoRecentTime = getFrameDao().getDatabaseTimeStamp();
		wait(5);
		trackFrameUsingProductHistory(productId, mostRecentPPId, getFrameDao().getDatabaseTimeStamp());
		trackFrameUsingProductHistory(productId, notSoRecentPPId, notSoRecentTime);
		
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		assertProductHistoryCount(1, waitTimeInSecs, notSoRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
		
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * product is tracked with actual timestamp in the future. In this scenario..  
	 *   -create a product history
	 *   -re-arrange Product Sequence (linked list) 
	 *   -update Product Sequence LAST_PASSING_PROCESS_POINT_ID
	 *   -update Product TRACKING_STAUS 
	 *   -update Product LAST_PASSING_PROCESS_POINT_ID
	 */
	@Test
	public void scenario18_trackWithActualTimestampInFuture() {
		executeSqlFile(getInProcessSetupScript());
		String notSoRecentPPId = "PRODTR_PP15";
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		
		assertProductHistoryCount(0, notSoRecentPPId);
		assertProductHistoryCount(0, mostRecentPPId);
		
		trackFrameUsingProductHistory(productId, notSoRecentPPId, getFrameDao().getDatabaseTimeStamp());
		wait(5);
		trackFrameUsingProductHistory(productId, mostRecentPPId, new Timestamp(getFrameDao().getDatabaseTimeStamp().getTime() + 10000000));
		
		assertProductHistoryCount(1, waitTimeInSecs, notSoRecentPPId);
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
		
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * product is tracked with null actual timestamp. In this scenario..  
	 *   -create a product history
	 *   -re-arrange Product Sequence (linked list) 
	 *   -update Product Sequence LAST_PASSING_PROCESS_POINT_ID
	 *   -update Product TRACKING_STAUS 
	 *   -update Product LAST_PASSING_PROCESS_POINT_ID
	 */
	@Test
	public void scenario19_trackWithNullActualTimestamp() {
		executeSqlFile(getInProcessSetupScript());
		String notSoRecentPPId = "PRODTR_PP15";
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		
		assertProductHistoryCount(0, notSoRecentPPId);
		assertProductHistoryCount(0, mostRecentPPId);
		
		trackFrameUsingProductHistory(productId, notSoRecentPPId, getFrameDao().getDatabaseTimeStamp());
		wait(5);
		trackFrameUsingProductHistory(productId, mostRecentPPId, null);
		
		assertProductHistoryCount(1, waitTimeInSecs, notSoRecentPPId);
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
		
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * perform tracking with Associate Number
	 */
	@Test
	public void scenario20_TrackWithAssociateNumber() {
		executeSqlFile(getInProcessSetupScript());
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		assertProductHistoryCount(0, mostRecentPPId);
		
		trackFrameUsingProductHistory(productId, mostRecentPPId, getFrameDao().getDatabaseTimeStamp(), associateNo);
		
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		assertAssociateNumber(2, associateNo, productId, mostRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
				
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * perform tracking with Approver Number
	 */
	@Test
	public void scenario21_TrackWithApproverNumber() {
		executeSqlFile(getInProcessSetupScript());
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		assertProductHistoryCount(0, mostRecentPPId);
		
		trackFrameUsingProductHistory(productId, mostRecentPPId, getFrameDao().getDatabaseTimeStamp(), "", approverNo);
		assertApproverNumber(2, approverNo, productId, mostRecentPPId);
		
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
		
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * perform tracking with Associate Number and Approver Number
	 */
	@Test
	public void scenario22_TrackWithAssociateAndApproverNumber() {
		executeSqlFile(getInProcessSetupScript());
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		assertProductHistoryCount(0, mostRecentPPId);
		
		trackFrameUsingProductHistory(productId, mostRecentPPId, getFrameDao().getDatabaseTimeStamp(), associateNo, approverNo);
		assertAssociateNumber(2, associateNo, productId, mostRecentPPId);
		assertApproverNumber(2, approverNo, productId, mostRecentPPId);
		
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
		
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * perform tracking with Associate Number and Approver Number
	 * without providing actual timestamp
	 */
	@Test
	public void scenario23_TrackWithAssociateAndApproverNumberNoActualTimestamp() {
		executeSqlFile(getInProcessSetupScript());
		String mostRecentPPId = "PRODTR_PP16";
		String lineId = "PRODTR_LINE16";
		String productId = "SIM0PRODTRACK1501";
		assertProductHistoryCount(0, mostRecentPPId);
		
		trackFrameUsingProductHistory(productId, mostRecentPPId, null, associateNo, approverNo);
		assertAssociateNumber(2, associateNo, productId, mostRecentPPId);
		assertApproverNumber(2, approverNo, productId, mostRecentPPId);
		assertNotNull(getDataProvider().getActualTimestamp(productId, mostRecentPPId));
		
		assertProductHistoryCount(1, waitTimeInSecs, mostRecentPPId);
		
		assertLinkedListCount(MAX_PRODUCTS, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, productId);
		
		assertTrackingStatus(2, lineId, productId);
		assertProductLastPassingPPId(2, mostRecentPPId, productId);
		assertProductSeqLastPassingPPId(2, mostRecentPPId, productId);		
	}
	
	/**
	 * perform tracking using Tracking Rest Service
	 * Params: productType, productId, processPointId
	 */
	@Test
	public void scenario24_TrackingRestService_ProductType_ProcessPointId() {
		executeSqlFile(getInProcessSetupScript());
		String lineId = "PRODTR_LINE16";
		String ppId = "PRODTR_PP16";
		
		for (int i = 1401, j = 1; j <= numOfProducts; j++, i++) {
			String productId = getFrame(i).getProductId();
			String jsonPayload = "[{\"com.honda.galc.data.ProductType\": \"FRAME\"},{\"java.lang.String\": \"" + productId + "\"},{\"java.lang.String\": \"" + ppId + "\"}]";
			sendRestRequestInNewThread(jsonPayload, "RestWeb/TrackingService/track");    
		}
		
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, ppId);
		assertLinkedListCount(MAX_PRODUCTS + numOfProducts, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
	}
	
	/**
	 * perform tracking using Tracking Rest Service
	 * Params: productType, productHistory
	 */
	@Test
	public void scenario25_TrackingRestService_ProductType_ProductHistory() {
		executeSqlFile(getInProcessSetupScript());
		String lineId = "PRODTR_LINE16";
		String ppId = "PRODTR_PP16";
		
		for (int i = 1401, j = 1; j <= numOfProducts; j++, i++) {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
			String productId = getFrame(i).getProductId();
			String jsonPayload = "[{\"com.honda.galc.data.ProductType\": \"FRAME\"},{\"com.honda.galc.entity.product.ProductResult\" : {\"id\": {\"productId\": \"" + productId + "\",\"processPointId\" : \"" + ppId + "\",\"productType\" : \"Frame\", \"actualTimestamp\" : \"" + sdf.format(now)+ "\"}}}]";
			sendRestRequestInNewThread(jsonPayload, "RestWeb/TrackingService/track");    
		}
		
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, ppId);
		assertLinkedListCount(MAX_PRODUCTS + numOfProducts, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
	}
	
	/**
	 * perform tracking using Tracking Rest Service
	 * Params: product, processPointId
	 */
	@Test
	public void scenario26_TrackingRestService_Product_ProcessPointId() {
		executeSqlFile(getInProcessSetupScript());
		String lineId = "PRODTR_LINE16";
		String ppId = "PRODTR_PP16";
		
		for (int i = 1401, j = 1; j <= numOfProducts; j++, i++) {
			String productId = getFrame(i).getProductId();
			String jsonPayload = "[{\"com.honda.galc.entity.product.Frame\": {\"productId\": \"" + productId + "\",\"straightShipPercentage\": 0.0,\"engineStatus\": 1,\"afOnSequenceNumber\": 2,\"productionLot\": \"SIM 01AF201105100010\",\"productSpecCode\": \"XSIMX1111 XXX01     X \",  \"kdLotNumber\": \"HMI 01201104012801\",\"trackingProcessPointId\": \"PRODTR_PP16     \", \"trackingStatus\": \"PRODTR_LINE16   \" }}, {\"java.lang.String\": \"" + ppId + "\"}]";
			sendRestRequestInNewThread(jsonPayload, "RestWeb/TrackingService/track");    
		}
		
		assertProductHistoryCount(numOfProducts, waitTimeInSecs, ppId);
		assertLinkedListCount(MAX_PRODUCTS + numOfProducts, waitTimeInSecs, lineId);
		assertOneTailPerLine(2, lineId);
	}
		
	/**
	 * product is at the head of the line
	 * Process point type = ProductExit
	 * remove product from Product Sequence table 
	 * re-arrange product sequence 
	 * TRACKING_POINT_FLAG & PASSING_COUNT_FLAG values don't matter
	 */
	@Test
	public void scenario27_TrackAtProductExitPP_ProductAtHead() {
		executeSqlFile(getInProcessSetupScript());
		String processPointId = "PRODTR_PP17";
		String lineId = "PRODTR_LINE17";
		assertProductHistoryCount(0, processPointId);
		trackFrame(new Frame("SIM0PRODTRACK1601"), processPointId);
		assertProductHistoryCount(1, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS - 1, waitTimeInSecs, lineId);
		assertLinkedVin("SIM0PRODTRACK1602", "SIM0PRODTRACK1603", 2);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, "SIM0PRODTRACK1700");
	}
	
	/**
	 * product is at the tail of the line
	 * Process point type = ProductExit
	 * remove product from Product Sequence table 
	 * re-arrange product sequence 
	 * TRACKING_POINT_FLAG & PASSING_COUNT_FLAG values don't matter
	 */
	@Test
	public void scenario28_TrackAtProductExitPP_ProductAtTail() {
		executeSqlFile(getInProcessSetupScript());
		String processPointId = "PRODTR_PP17";
		String lineId = "PRODTR_LINE17";
		assertProductHistoryCount(0, processPointId);
		trackFrame(new Frame("SIM0PRODTRACK1700"), processPointId);
		assertProductHistoryCount(1, waitTimeInSecs, processPointId);
		assertLinkedListCount(MAX_PRODUCTS - 1, waitTimeInSecs, lineId);
		assertLinkedVin("SIM0PRODTRACK1698", "SIM0PRODTRACK1699", 2);
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, "SIM0PRODTRACK1699");
	}
	
	/**
	 * product is neither the tail nor the head of the line
	 * Process point type = ProductExit
	 * remove product from Product Sequence table 
	 * re-arrange product sequence 
	 * TRACKING_POINT_FLAG & PASSING_COUNT_FLAG values don't matter
	 */
	@Test
	public void scenario29_TrackAtProductExitPP_ProductNeitherTailNorHead() {
		executeSqlFile(getInProcessSetupScript());
		String processPointId = "PRODTR_PP17";
		String lineId = "PRODTR_LINE17";
		int vinCount = 30;			// number of products to exit factory 'simultaneously'
		int startIndex = 1650;
		assertProductHistoryCount(0, processPointId);
		for (int i=0, j=startIndex; i < vinCount; i++, j++) {
			wait(1);
			trackInNewThread(j, processPointId, false);
		}
		assertLinkedListCount(MAX_PRODUCTS - vinCount, waitTimeInSecs, lineId);
		assertProductHistoryCount(vinCount, waitTimeInSecs, processPointId);
		// make sure startIndex-1 is linked to startIndex+vinCount 
		assertLinkedVin(getFrameId(startIndex - 1), getFrameId(startIndex + vinCount), 2);	
		assertOneTailPerLine(2, lineId);
		assertTailProduct(2, lineId, "SIM0PRODTRACK1700");
	}
	
	private void assertLinkedVin(String vinMinus1, String expectedVin, int waitTimeInSecs) {
		wait(waitTimeInSecs);
		String linkedVin = getDataProvider().getLinkedVin(vinMinus1);
		System.out.println("Expected Linked vin: " + expectedVin);
		System.out.println("Actual   Linked vin: " + linkedVin);
		assertEquals(expectedVin, linkedVin);
	}
	
	private void assertProductHistoryCount(int val, String processPointId) {
		assertProductHistoryCount(val, 0, processPointId);
	}
	
	private void assertProductHistoryCount(int val, int waitTimeInSecs, String processPointId) {
		wait(waitTimeInSecs);
		int trackingCount = getProductHistoryCount(processPointId);
		System.out.println("Expected Product history count for process point " + processPointId + ": "  + val);
		System.out.println("Actual   Product history count for process point " + processPointId + ": " + trackingCount);
		assertEquals(val, trackingCount);
	}
	
	private void assertAssociateNumber(int waitTimeInSecs, String associateNo, String productId, String ppId) {
		wait(waitTimeInSecs);
		String actualAssociateNo = getDataProvider().getAssociateNo(productId, ppId);
		System.out.println("Expected Associate Number: " + associateNo);
		System.out.println("Actual   Associate Number: " + actualAssociateNo);
		assertEquals(associateNo, actualAssociateNo);
	}

	private void assertApproverNumber(int waitTimeInSecs, String approverNo, String productId, String ppId) {
		wait(waitTimeInSecs);
		String actualApproverNo = getDataProvider().getApproverNo(productId, ppId);
		System.out.println("Expected Approver Number: " + approverNo);
		System.out.println("Actual   Approver Number: " + approverNo);
		assertEquals(approverNo, actualApproverNo);
	}
	
	private void assertTrackingStatus(int waitTimeInSecs, String lineId, String productId) {
		wait(waitTimeInSecs);
		String trackingStatus = getDataProvider().getTrackingStatus(productId);
		System.out.println("Expected Tracking status for productId " + productId + ": " + lineId);
		System.out.println("Actual   Tracking status for productId " + productId + ": " + trackingStatus);
		assertEquals(lineId, trackingStatus);
	}
	
	private void assertProductLastPassingPPId(int waitTimeInSecs, String ppId, String productId) {
		wait(waitTimeInSecs);
		String lastPassingPPId = getDataProvider().getProductLastPassingPPId(productId);
		System.out.println("Expected last passing PPId for productId " + productId + "  : " + ppId);
		System.out.println("Actual   last passing PPId for productId " + productId + "  : " + lastPassingPPId);
		assertEquals(ppId, lastPassingPPId);
	}
	
	private void assertProductSeqLastPassingPPId(int waitTimeInSecs, String ppId, String productId) {
		wait(waitTimeInSecs);
		String lastPassingPPId = getDataProvider().getProductSeqLastPassingPPId(productId);
		System.out.println("Expected last passing PPId: " + ppId);
		System.out.println("Actual   last passing PPId: " + lastPassingPPId);
		assertEquals(ppId, lastPassingPPId);
	}
	
	private void assertOneTailPerLine(int waitTimeInSecs, String lineId) {
		wait(waitTimeInSecs);
		int tailCount = getDataProvider().getTailCount(lineId);
		System.out.println("Expected Tail count: " + 1);
		System.out.println("Actual   Tail count: " + tailCount);
		assertEquals(1, tailCount);
	}
	
	private void assertTailProduct(int waitTimeInSecs, String lineId, String productId) {
		wait(waitTimeInSecs);
		String tailProduct = getDataProvider().getTailProductId(lineId);
		System.out.println("Expected Tail product: " + productId);
		System.out.println("Actual   Tail product: " + tailProduct);
		assertEquals(productId, tailProduct);
	}
	
	private void assertLinkedListCount(int val, int waitTimeInSecs, String lineId) {
		wait(waitTimeInSecs);
		int linkedListCount = getDataProvider().getLinkedListCount(lineId);
		System.out.println("Expected new line Linkedlist count: " + val);
		System.out.println("Actual   new line Linkedlist count: " + linkedListCount);
		assertEquals(val, linkedListCount);
	}

	private void sendRestRequestInNewThread(final String jsonPayload, final String serviceName) {
		Thread t = new Thread() {
			public void run() {
				assertEquals(INVOCATION_SUCCESS, sendRestRequest(jsonPayload, serviceName));
			}
		};
		t.start();
	}

	private int sendRestRequest(String jsonPayload, String serviceName) {
		try {
			String url = getEnvironment().getUrl().replace("BaseWeb/HttpServiceHandler", serviceName);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
	 		// Send post request
			con.setDoOutput(true);
			con.getOutputStream().write(jsonPayload.getBytes());
			con.getOutputStream().flush();
			con.getOutputStream().close();
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending REST request to URL : " + url);
			System.out.println("Post parameters : " + jsonPayload);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
			return responseCode;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	private void trackInNewThread(int index, String processPointId) {
		trackInNewThread(index, processPointId, true);
	}
	
	private void trackInNewThread(int index, final String processPointId, final boolean setEngineStatus) {
		final int iter = index;
		Thread t = new Thread() {
			public void run() {
				trackFrame(getFrame(iter), processPointId, setEngineStatus);
			}
		};
		t.start();
	}
	
	private void trackFrameUsingProductHistory(String productId, String processPointId, Timestamp actualTimestamp) {
		trackFrameUsingProductHistory(productId, processPointId, actualTimestamp, "", "");
	}
	
	private void trackFrameUsingProductHistory(String productId, String processPointId, Timestamp actualTimestamp, String associateNo) {
		trackFrameUsingProductHistory(productId, processPointId, actualTimestamp, associateNo, "");
	}
	
	private void trackFrameUsingProductHistory(String productId, String processPointId, Timestamp actualTimestamp, String associateNo, String approverNo) {
		ProductHistory productHistory = ProductTypeUtil.createProductHistory(productId, processPointId, ProductType.FRAME);
		productHistory.setActualTimestamp(actualTimestamp);
		productHistory.setAssociateNo(associateNo);
		productHistory.setApproverNo(approverNo);
		
		getTrackingService().track(ProductType.FRAME, productHistory);
	}
	
	private boolean trackFrame(Frame frame, String processPointId) {
		return trackFrame(frame,processPointId,true);
	}

	private boolean trackFrame(Frame frame, String processPointId, boolean setEngineStatus) {
		try {
			//assign engine to frame and track frame
			if (setEngineStatus) {
				frame.setEngineStatus(STATUS_OK);
				getFrameDao().update(frame);
			}

			getTrackingService().track(ProductType.FRAME, frame.getProductId(), processPointId);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private String getFrameId(Integer index) {
		return "SIM0PRODTRACK" + StringUtils.leftPad(index.toString(), 4, '0');
	}
	
	private Frame getFrame(Integer index) {
		return getFrameDao().findByKey("SIM0PRODTRACK" + StringUtils.leftPad(index.toString(), 4, '0'));
	}

	public int getProductHistoryCount(String processPointId) {
		List<ProductResult> productResults = getProductResultDao().findByProcessPoint(processPointId);
		int count = 0;
		for(ProductResult result: productResults) {
			if (result.getProductId().startsWith("SIM0PRODTRACK")) {
				count++;
			} 
		}
		return count;
	}
	
	public String getInProcessSetupScript() {
		return sqlFileDir + "ProductTrackerTest_SetUpInProcessProducts.sql";
	}
	
	/**
	 * Perform a thread sleep operation with the given time in seconds
	 */
	private void wait(int timeInSecs){
		try {
			if (timeInSecs > 0)
				Thread.sleep(timeInSecs * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
