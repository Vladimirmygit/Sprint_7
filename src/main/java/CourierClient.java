import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierClient {
    private static final String COURIER_ENDPOINT = "/api/v1/courier";

    @Step("Создание курьера")
    public Response createCourier(String login, String password) {
        return given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post(COURIER_ENDPOINT);
    }

    @Step("Авторизация курьера и получение ID")
    public String loginCourierAndGetId(String login, String password) {
        Response response = loginCourier(login, password);
        response.then().statusCode(200).body("id", notNullValue());
        return response.asString();
    }


    @Step("Авторизация курьера")
    public Response loginCourier(String login, String password) {
        return given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post(COURIER_ENDPOINT + "/login");
    }
    @Step("Проверка уникальности логина курьера")
    public void assertCourierLoginIsUnique(Courier courier) {
        given()
                .contentType("application/json")
                .body("{\"login\": \"" + courier.getLogin() + "\", \"password\": \"password\"}")
                .when()
                .post(COURIER_ENDPOINT + "/login")
                .then()
                .statusCode(404); // Проверяем, что логин не существует (статус 404 - Not Found)
    }

    @Step("Создание курьера и получение ID")
    public String createCourierAndGetId(String login, String password) {
        // Создание курьера
        Response response = createCourier(login, password);

        // Проверка успешного создания (статус 201 - Created)
        response.then().statusCode(201).body("ok", equalTo(true));

        // Получение ID курьера из метода логина
        return loginCourierAndGetId(login, password);
    }
    @Step("Удаление курьера по ID")
    public void deleteCourierById(String courierId) {
        given()
                .pathParam("id", courierId)
                .when()
                .delete(COURIER_ENDPOINT + "/{id}")
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}
