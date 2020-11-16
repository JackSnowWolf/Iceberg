package com.iceberg.externalapi.impl;

import com.iceberg.externalapi.PayPalClient;
import com.iceberg.externalapi.PayPalService;
import com.paypal.http.HttpResponse;
import com.paypal.http.serializer.Json;
import com.paypal.payouts.*;
import org.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PayPalServiceImpl extends PayPalClient implements PayPalService {

  @Override
  public HttpResponse<PayoutItemResponse> cancelPayoutItem(String itemId) throws IOException {
    PayoutsItemCancelRequest request = new PayoutsItemCancelRequest(itemId);
    HttpResponse<PayoutItemResponse> response = client().execute(request);
    System.out.println("Response Body:");
    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    return response;
  }

  @Override
  public HttpResponse<CreatePayoutResponse> createPayout(String receiver, String currency, String value)
      throws IOException {
    PayoutsPostRequest request = buildRequestBody(receiver, currency, value);
    HttpResponse<CreatePayoutResponse> response = client().execute(request);
    System.out.println("Response Body:");
    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    return response;
  }

  @Override
  public PayoutsPostRequest buildRequestBody(String receiver, String currency, String value) {
    List<PayoutItem> items = IntStream
        .range(1, 2).mapToObj(index -> new PayoutItem().senderItemId("Test_IceBerg_" + index)
            .note("Your" + value + "Payout!").receiver(receiver).amount(new Currency().currency(currency).value(value)))
        .collect(Collectors.toList());

    CreatePayoutRequest payoutBatch = new CreatePayoutRequest().senderBatchHeader(new SenderBatchHeader()
        .senderBatchId("IceBerg_PayPal_" + RandomStringUtils.randomAlphanumeric(7)).emailMessage("SDK payouts test txn")
        .emailSubject("This is a transaction from IceBerg").note("Enjoy your Payout!!").recipientType("EMAIL"))
        .items(items);

    return new PayoutsPostRequest().requestBody(payoutBatch);
  }

  // TODO
  // This is an example for viewing the function.
  @Override
  public HttpResponse<CreatePayoutResponse> createPayoutBatch() throws IOException {
    PayoutsPostRequest request = buildRequestBody();
    HttpResponse<CreatePayoutResponse> response = client().execute(request);
    System.out.println("Response Body:");
    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    return response;
  }

  // TODO
  // This is an example for viewing the function.
  @Override
  public PayoutsPostRequest buildRequestBody() {
    List<PayoutItem> items = IntStream.range(1, 6)
        .mapToObj(index -> new PayoutItem().senderItemId("Test_txn_" + index).note("Your 5$ Payout!")
            .receiver("payout-sdk-" + index + "@paypal.com").amount(new Currency().currency("USD").value("1.00")))
        .collect(Collectors.toList());

    CreatePayoutRequest payoutBatch = new CreatePayoutRequest().senderBatchHeader(new SenderBatchHeader()
        .senderBatchId("IceBerg_PayPal_" + RandomStringUtils.randomAlphanumeric(7)).emailMessage("SDK payouts test txn")
        .emailSubject("This is a transaction from IceBerg").note("Enjoy your Payout!!").recipientType("EMAIL"))
        .items(items);

    return new PayoutsPostRequest().requestBody(payoutBatch);
  }

  @Override
  public HttpResponse<PayoutBatch> getPayoutBatch(String batchId) throws IOException {
    PayoutsGetRequest request = new PayoutsGetRequest(batchId)
        // Optional parameters, maximum of 1000 items are retrieved by default.
        .page(1).pageSize(10).totalRequired(true);

    HttpResponse<PayoutBatch> response = client().execute(request);
    System.out.println("Response Body:");
    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    return response;
  }

  @Override
  public HttpResponse<PayoutItemResponse> getPayoutItem(String itemId) throws IOException {
    PayoutsItemGetRequest request = new PayoutsItemGetRequest(itemId);
    HttpResponse<PayoutItemResponse> response = client().execute(request);
    System.out.println("Response Body:");
    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
    return response;
  }
}
