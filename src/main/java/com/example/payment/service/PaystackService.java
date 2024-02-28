package com.example.payment.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public class PaystackService {

    @Value("${paystack.api.secret-key}")
    private String secretKey;

    public String initializePayment(double amount, String email, String callbackUrl) throws IOException {
        //http client
        CloseableHttpClient client = HttpClients.createDefault();
        //paystack url to initialise a payment from the backend
        HttpPost httpPost = new HttpPost("https://api.paystack.co/transaction/initialize");

        //validating security measures
        httpPost.setHeader("Authorization", "Bearer " + secretKey);
        httpPost.setHeader("Content-Type", "application/json");

        //json object to carry body of transaction
        JSONObject json = new JSONObject();

        //amounts are processed in subunit of currency used(ISO 4217)
        // Paystack API expects amount in subunit hence multiplied by 100
        json.put("amount", amount * 100);
        json.put("email", email);
        json.put("callback_url", callbackUrl);
//        json.put("currency",Currency);


        //converting json to string and wrapping it in a string entity
        StringEntity entity = new StringEntity(json.toString());
        //posting converted string-entity
        httpPost.setEntity(entity);

        //getting the response after the response
        CloseableHttpResponse response = client.execute(httpPost);

        //converting the response to string from string entity
        String responseBody = EntityUtils.toString(response.getEntity());

        System.out.println("Paystack API Response: " + responseBody);


        client.close();

        //getting the transaction reference from the response body
        JSONObject jsonResponse = new JSONObject(responseBody);
        if (jsonResponse.has("data")) {
            JSONObject dataObject = jsonResponse.getJSONObject("data");
            return dataObject.getString("reference");
        } else {
            // Handle the case where "data" is not present in the response
            return "Reference not available"; // You may adjust this as needed
        }
    }
}
