package com.iceberg.externalapi;

import com.paypal.http.HttpResponse;
import com.paypal.payouts.CreatePayoutResponse;
import com.paypal.payouts.PayoutBatch;
import com.paypal.payouts.PayoutItemResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PayPalServiceImplTest {
//    private Logger logger = LoggerFactory.getLogger(PayPalServiceImplTest.class);
//
//    @Resource
//    private PayPalService payPalService;
//
//    @Test
//    public void createPayoutBatchTest() throws IOException {
//      logger.info("create payout batch test starts");
//        try{
//            HttpResponse<CreatePayoutResponse> createPayoutResponse = payPalService.createPayoutBatch();
//            boolean res=false;
//            if(createPayoutResponse!=null){
//                res=true;
//            }
//
//            assertEquals(true,res);
//
//            int i = 0;
//            HttpResponse<PayoutBatch> getBatchResponse;
//            do {
//                getBatchResponse = payPalService.getPayoutBatch(createPayoutResponse.result().batchHeader().payoutBatchId());
//                if (getBatchResponse.result().batchHeader().batchStatus().equals("SUCCESS")) {
//                    break;
//                }
//                Thread.sleep(1000);
//                i++;
//            } while (i <= 5);
//
//            if (i < 5) {
//                HttpResponse<PayoutItemResponse> response=payPalService.cancelPayoutItem(getBatchResponse.result().items().get(0).payoutItemId());
//                logger.info(response.toString());
//            } else {
//                System.out.println("Payout create request is still not processed");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void createPayoutTest() throws IOException{
//      logger.info("create payout test starts");
//        try{
//            HttpResponse<CreatePayoutResponse> createPayoutResponse = payPalService.createPayout("payout-sdk-test@paypal.com", "USD", "1.00");
//            boolean res=false;
//            if(createPayoutResponse!=null){
//                res=true;
//            }
//            assertEquals(true,res);
//            HttpResponse<PayoutBatch> getBatchResponse;
//            getBatchResponse = payPalService.getPayoutBatch(createPayoutResponse.result().batchHeader().payoutBatchId());
//            HttpResponse<PayoutItemResponse> response=payPalService.cancelPayoutItem(getBatchResponse.result().items().get(0).payoutItemId());
//            logger.info(response.toString());
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void cancelPayoutItemTest() throws IOException, InterruptedException{
//      logger.info("cancel payout test starts");
//        try{
//            HttpResponse<CreatePayoutResponse> createPayoutResponse = payPalService.createPayoutBatch();
//            int i = 0;
//            HttpResponse<PayoutBatch> getBatchResponse;
//            do {
//                getBatchResponse = payPalService.getPayoutBatch(createPayoutResponse.result().batchHeader().payoutBatchId());
//                if (getBatchResponse.result().batchHeader().batchStatus().equals("SUCCESS")) {
//                    break;
//                }
//                Thread.sleep(1000);
//                i++;
//            } while (i <= 5);
//            boolean res=false;
//            if (i < 5) {
//                HttpResponse<PayoutItemResponse> response=payPalService.cancelPayoutItem(getBatchResponse.result().items().get(0).payoutItemId());
//
//                if(response!=null){
//                    res=true;
//                }
//
//                assertEquals(true,res);
//                res=false;
//            } else {
//                System.out.println("Payout create request is still not processed");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getPayoutBatchAndItemTest() throws IOException {
//      logger.info("get payout test starts");
//        try{
//            HttpResponse<CreatePayoutResponse> createPayoutResponse = payPalService.createPayoutBatch();
//            int i = 0;
//            HttpResponse<PayoutBatch> getBatchResponse;
//
//            boolean res=false;
//            do {
//                getBatchResponse = payPalService.getPayoutBatch(createPayoutResponse.result().batchHeader().payoutBatchId());
//                if (getBatchResponse.result().batchHeader().batchStatus().equals("SUCCESS")) {
//                    break;
//                }
//                Thread.sleep(1000);
//                if(getBatchResponse!=null){
//                    res=true;
//                }
//
//                assertEquals(true,res);
//                res=false;
//                i++;
//            } while (i <= 5);
//            HttpResponse<PayoutItemResponse> payoutItemResponseHttpResponse=payPalService.getPayoutItem(createPayoutResponse.result().batchHeader().payoutBatchId());
//            if (payoutItemResponseHttpResponse!=null){
//                res=true;
//            }
//            assertEquals(true,res);
//
//            if (i < 5) {
//                HttpResponse<PayoutItemResponse> response=payPalService.cancelPayoutItem(getBatchResponse.result().items().get(0).payoutItemId());
//                logger.info(response.toString());
//            } else {
//                System.out.println("Payout create request is still not processed");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
