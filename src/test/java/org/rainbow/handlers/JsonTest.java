package org.rainbow.handlers;

import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTest extends TestCase {

    // {
    //  "name": "Mars",
    //  "hello": [
    //    "0",
    //    "1",
    //    "2"
    //  ],
    //  "city": "NY",
    //  "age": 32,
    //  "one": "two"
    //}
    public void testObjToJson() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put( "name", "Mars" );
        data.put( "age", 32 );
        data.put( "city", "NY" );
        JSONObject json = new JSONObject(data);

        JSONObject jList = new JSONObject();
        jList.put("hello", "world");

        List<Object> userList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            userList.add("" + i);
        }

        json.put("hello", userList);
        json.put("one", "two");

        System.out.printf("JSON: %s", json.toString());

        int i = 0;
        i++;

    }


    public void testValidJson() {
        // convert string to JSONObject
        String jsonString = "{\"name\":\"Mars\",\"hello\":[\"0\",\"1\",\"2\"],\"city\":\"NY\",\"age\":32,\"one\":\"two\"}";
        JSONObject json = new JSONObject(jsonString);
        JSONArray jArr = json.getJSONArray("hello");
        int i = 0;

        // try invalid casting
        try {
            JSONArray j2 = json.getJSONArray("one");
        } catch (JSONException e) {
            System.out.println("Valid exception caught!!!");
        }

        int j = 0;
    }

    public void testInvalidJson() {

        String jsonString = "{:\"Mars\",\"hello\":[\"0\",\"1\",\"2\"],\"city\":\"NY\",\"age\":32,\"one\":\"two\"}";
        try {
            JSONObject json = new JSONObject(jsonString);
        } catch (JSONException e) {
            System.out.println("Valid exception caught!!!");
            return;
        }
        int i = 0;

    }

    public void testDoGet() {
    }

    public void testDoPost() {
    }
}