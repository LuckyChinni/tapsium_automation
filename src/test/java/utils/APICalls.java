package utils;

import io.restassured.response.Response;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class APICalls extends BaseAPI {
    String endpoint;
    String token;
    Response response;
    String accessToken;
    LocalTime tokenGenerateTime;
    int oAuthTimeOut = 100;
    String payload;
    Hashtable<String, String> dataTable;
    static String accountID;
    static String activationToken;

    public String createAccountToken() {
        endpoint = getData("endpoint");
        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant-type", "client_credentials");
        formParams.put("scope", "/order-mgmt");
        formParams.put("sourceSystem", "APP");
        formParams.put("client_secret", "SMAPP");
        formParams.put("client_id", "abc123**");
        System.out.println("Form Params: " + formParams);
        System.out.println("OAuth Url: " + endpoint);
        token = getAccessToken(endpoint, formParams);
        System.out.println("Access Token: " + token);
        tokenGenerateTime = LocalTime.now();
        return token;
    }

    public String generateAccountToken() {
        if (accessToken != null) {
            Duration timeElapsed = Duration.between(tokenGenerateTime, LocalTime.now());
            if (timeElapsed.toSeconds() + 60 > oAuthTimeOut)
                token = createAccountToken();
        } else
            token = createAccountToken();
        return token;
    }

    public String constructPayload(String payload, Hashtable<String, String> testData) {
        Pattern pattern = Pattern.compile("\\[[a-zA-Z0-9]+\\\\");
        Matcher matcher = pattern.matcher(payload.trim());
        List<String> variableList = new ArrayList<>();
        while (matcher.find())
            variableList.add(matcher.group());
        for (String value : variableList) {
            try {
                payload = payload.replace(value, testData.get(value.substring(1, value.length() - 1)));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        System.out.println(payload);
        return payload;
    }

    public Response createAccount() {
        token = generateAccountToken();
        endpoint = getData("url")+"/pep/createNewAccount/";
        payload = Utils.getResourceContent("/payloads/createAccountPayload");
        constructPayload(payload, dataTable);
        response = doPostWithBearerToken(token, endpoint, payload);
        accountID = response.body().jsonPath().getString("response.accountId");
        activationToken = response.body().jsonPath().getString("response.access_token");
        System.out.println(response.body().jsonPath().getString("response.accountId"));
        System.out.println(response.body().jsonPath().getString("status.code"));
        System.out.println(response.body().jsonPath().getString("status.description"));
        System.out.println(response.body().jsonPath().getString("response.access_token"));
        System.out.println(response.body().jsonPath().getString("AccessToken"));
        return response;
    }

    public Response activateServiceOrder(){
        endpoint = getData("url") + "/pep/order-mgmt/v2/serviceorder/";
        payload = Utils.getResourceContent("/payloads/activationServiceOrder");
        constructPayload(payload, dataTable);
        if(activationToken != null){
            response = doPostWithBearerToken(activationToken, endpoint, payload);
        }
        return response;
    }

}
