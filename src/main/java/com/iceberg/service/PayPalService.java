package com.iceberg.service;

import com.paypal.http.HttpResponse;
import com.paypal.payouts.CreatePayoutResponse;
import com.paypal.payouts.PayoutBatch;
import com.paypal.payouts.PayoutItemResponse;
import com.paypal.payouts.PayoutsPostRequest;

import java.io.IOException;

public interface PayPalService {

    // Cancel Payouts
    HttpResponse<PayoutItemResponse> cancelPayoutItem(String itemId) throws IOException;

    // Create Payouts
    HttpResponse<CreatePayoutResponse> createPayout(String receiver, String currency, String value) throws IOException;

    PayoutsPostRequest buildRequestBody(String receiver, String currency, String value);

    // Create Payouts Batch
    HttpResponse<CreatePayoutResponse> createPayoutBatch() throws IOException;

    PayoutsPostRequest buildRequestBody();

    // Get Payouts Batch
    HttpResponse<PayoutBatch> getPayoutBatch(String batchId) throws IOException;

    // Get Payouts Item
    HttpResponse<PayoutItemResponse> getPayoutItem(String itemId) throws IOException;
}
