package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import testCases.BaseTest;

import java.util.Map;

public class BaseAPI extends BaseTest {
    RequestSpecification requestSpecification = RestAssured.given();
    Response response;
    String token;
    public String getAccessToken(String endpoint, Map<String, String> formParams) {
        response = requestSpecification.contentType("application/x-www-form-urlencoded").formParams(formParams).when().post(endpoint);
        System.out.println("Response Body: " + response.getBody().asString());
        token = response.jsonPath().getString("access_token");
        return token;
    }

    public Response doPostWithBearerToken(String token, String endpoint, String payload) {
        System.out.println("Endpoint URL: " + endpoint);
        System.out.println("Request Body: " + payload);
        try {
            response = requestSpecification.header("Authorization", "Bearer" + token).relaxedHTTPSValidation().contentType(ContentType.JSON).body(payload)
                    .when().post(endpoint).then().extract().response();
            System.out.println(response.asPrettyString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }
}
