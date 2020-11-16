package com.iceberg.externalapi;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class PayPalClient {

  /**
   * Setting up PayPal SDK environment with PayPal Access credentials. For demo
   * purpose, we are using SandboxEnvironment. In production this will be
   * LiveEnvironment.
   */
  private PayPalEnvironment environment = new PayPalEnvironment.Sandbox(
      "AeMY_PpJZTsA2luizwgwtspTkbEeEuz1DdAFZmtII9huPiuhMxETWCg2DRKE31ZmHYGPyJw07WR5iHFN",
      "EBcXHq6z6MQjXt5MvB7nqSuE1_1_QCJixb67tLcKDjkUOxEneQIl01LkCdr4oNb5ai9eHvyOoQPRbzYc");

  /**
   * PayPal HTTP client instance with environment which has access credentials
   * context. This can be used invoke PayPal API's provided the credentials have
   * the access to do so.
   */
  PayPalHttpClient client = new PayPalHttpClient(environment);

  /**
   * Method to get client object
   *
   * @return PayPalHttpClient client
   */
  public PayPalHttpClient client() {
    return this.client;
  }
}