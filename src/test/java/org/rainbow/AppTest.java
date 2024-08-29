package org.rainbow;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.Assert.*;

public class AppTest {

    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

    HttpClient client;

    @Before
    public void initialize() throws Exception {

        Runtime.getRuntime().exec("rm -rf /tmp/rainbow.db");

        logger.info("Starting the test server!");

        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    App.startServer();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(runnable).start();
        logger.info("The test server has been started!");

    }

    @Test
    public void v1Test() throws IOException, InterruptedException {

        Thread.sleep(1000);

        JSONObject data = null;

        logger.info("Running v1 test");

        JSONObject json = new JSONObject();
        json.put("hello", "world");

        HttpResponse<String> response;

        // Try accessing empty record
        logger.info("Sending the HTTP GET request");
        response = sendHTTPGETRequest(1);
        Assert.assertEquals(404, response.statusCode());


        // TEST INSERT
        logger.info("Sending the HTTP POST request");
        response = sendHTTPPOSTRequest(1, json);

        Assert.assertEquals(200, response.statusCode());

        JSONObject jsonObject = new JSONObject(response.body());
        Assert.assertTrue(jsonObject.has("data"));
        data = jsonObject.getJSONObject("data");
        Assert.assertTrue(data.has("hello"));


        // TEST UPDATE
        json.clear();
        json.put("one", "two");
        response = sendHTTPPOSTRequest(1, json);
        Assert.assertEquals(200, response.statusCode());

        jsonObject = new JSONObject(response.body());
        Assert.assertTrue(jsonObject.has("data"));
        data = jsonObject.getJSONObject("data");
        Assert.assertTrue(data.has("hello"));
        Assert.assertTrue(data.has("one"));


        // TEST DELETE
        json.clear();
        json.put("one", JSONObject.NULL);
        response = sendHTTPPOSTRequest(1, json);
        Assert.assertEquals(200, response.statusCode());

        jsonObject = new JSONObject(response.body());
        Assert.assertTrue(jsonObject.has("data"));
        data = jsonObject.getJSONObject("data");
        Assert.assertTrue(data.has("hello"));
        Assert.assertFalse(data.has("one"));
    }

    public HttpResponse sendHTTPGETRequest(Integer id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/record/" + id))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print the response status code and body
        logger.info("Status Code: " + response.statusCode());
        logger.info("Response Body: " + response.body());

        return response;
    }

    public HttpResponse sendHTTPPOSTRequest(Integer id, JSONObject json) throws IOException, InterruptedException {

        // Create the HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/v1/record/" + id))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print the response status code and body
        logger.info("Status Code: " + response.statusCode());
        logger.info("Response Body: " + response.body());

        return response;
    }

}