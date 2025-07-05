package utils;

import io.restassured.path.json.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class CalculateTaxFromJSON {
    String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\postgresResponse.json";

    @Test
    public void calculateTax() {
        String response = Utils.convertJSONToString(filePath);
        JSONObject jsonObject = new JSONObject(response);
        JSONArray orderItems = jsonObject.getJSONArray("orderItems");
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < orderItems.length(); i++) {
            JSONObject orderItem = orderItems.getJSONObject(i);
            JSONObject orderItemPrice = orderItem.getJSONObject("orderItemPrice");
            JSONArray taxItems = orderItemPrice.getJSONArray("taxItems");
            for (int j = 0; j < taxItems.length(); j++) {
                JSONObject taxItem = taxItems.getJSONObject(j);
                double taxAmount = taxItem.getDouble("taxAmount");
                JSONArray taxExtensions = taxItem.getJSONArray("taxItemExtension");
                for (int k = 0; k < taxExtensions.length(); k++) {
                    JSONObject taxExtension = taxExtensions.getJSONObject(k);
                    if (taxExtension.getString("name").equalsIgnoreCase("taxDescription")) {
                        String taxDescription = taxExtension.getString("value");
                        map.put(taxDescription, taxAmount);
                        break;
                    }
                }
            }
        }
        System.out.println("Extracted Tax Data from DB");
        for(Map.Entry<String, Object> entry : map.entrySet()){
            System.out.println("Tax Description: " +entry.getKey() + " :: Tax Amount: " +entry.getValue());
        }
        JsonPath jsonPath = new JsonPath(response);
        double totalAmount = jsonPath.getDouble("orderPrice.totalAmount");
        System.out.println(totalAmount);
    }
}
