import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CourierAPIHelper {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";

    public static String loginCourier(String login, String password) {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .when()
                .post(BASE_URI + "/api/v1/courier/login");

        response.then().statusCode(200);
        return response.jsonPath().getString("id");
    }
}
