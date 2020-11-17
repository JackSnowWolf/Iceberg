package com.iceberg.externalapi;

import com.paypal.http.HttpResponse;
import com.paypal.payouts.CreatePayoutResponse;
import com.paypal.payouts.PayoutBatch;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PayPalServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(PayPalServiceImplTest.class);

    @Resource
    private PayPalService payPalService;

    @Test
    public void createPayoutBatchTest() throws IOException {
        try{
            HttpResponse<CreatePayoutResponse> response=payPalService.createPayoutBatch();

            boolean result=false;
            if(response!=null){
                result=true;
            }
            assertEquals(true,result);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void cancelPayoutItemTest() throws IOException, InterruptedException{
        try{
            HttpResponse<CreatePayoutResponse> createPayoutResponse = payPalService.createPayoutBatch();
            int i = 0;
            HttpResponse<PayoutBatch> getBatchResponse;
            do {
                getBatchResponse = payPalService.getPayoutBatch(createPayoutResponse.result().batchHeader().payoutBatchId());
                if (getBatchResponse.result().batchHeader().batchStatus().equals("SUCCESS")) {
                    break;
                }
                Thread.sleep(1000);
                i++;
            } while (i <= 5);

            if (i < 5) {
                payPalService.cancelPayoutItem(getBatchResponse.result().items().get(0).payoutItemId());
            } else {
                System.out.println("Payout create request is still not processed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
