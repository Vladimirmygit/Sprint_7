import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    private String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            // Удаление курьера по ID
            given()
                    .pathParam("id", courierId)
                    .when()
                    .delete("/api/v1/courier/{id}")
                    .then()
                    .statusCode(200)
                    .body("ok", equalTo(true));
        }
    }

    @Test
    @DisplayName("Тест успешной авторизации курьера")
    @Description("Проверка, что курьер может успешно авторизоваться")
    public void courierCanLoginSuccessfully() {
        String login = "Login22662612";
        String password = "Pass2626612";

        // Создание курьера с указанным логином и паролем
        Response createResponse = given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post("/api/v1/courier");

        createResponse.then().statusCode(201).body("ok", equalTo(true));

        // Получение ID курьера из метода логина
        courierId = CourierAPIHelper.loginCourier(login, password);
        // Проверка, что ID не пустой
        assert courierId != null;
        // Авторизация курьера
        Response response = given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post("/api/v1/courier/login");

        // Проверка успешной авторизации
        response.then().statusCode(200).body("id", notNullValue());
    }

    @Test
    @DisplayName("Тест наличия всех обязательных полей при авторизации")
    @Description("Проверка, что запрос вернет ошибку, если не указать все обязательные поля")
    public void loginRequiresAllMandatoryFields() {
        // Попытка авторизации без указания всех обязательных полей (логина и пароля)
        Response response = given()
                .contentType("application/json")
                .body("{\"login\": \"" + "\", \"password\": \"" + "\"}")
                .post("/api/v1/courier/login");

        // Проверка, что запрос вернет ошибку статусом кода 400 Bad Request
        response.then().statusCode(400);

        // Проверка, что в ответе содержится ожидаемое сообщение об ошибке
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Тест неправильных учетных данных при авторизации")
    @Description("Проверка, что система вернет ошибку при неправильном логине или пароле")
    public void loginFailsWithIncorrectCredentials() {
        String login = "ninja12";
        String incorrectPass = "12342";

        // Попытка авторизации с неправильным логином и паролем
        Response response = given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + incorrectPass + "\"}")
                .post("/api/v1/courier/login");

        // Проверка, что запрос вернет ошибку
        response.then().statusCode(404);
    }

    @Test
    @DisplayName("Тест авторизации несуществующего курьера")
    @Description("Проверка, что запрос вернет ошибку при авторизации под несуществующим пользователем")
    public void loginFailsWithNonExistentCourier() {
        String login = "non";
        String password = "Pass";

        // Попытка авторизации с несуществующим логином и паролем
        Response response = given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post("/api/v1/courier/login");

        // Проверка, что запрос вернет ошибку
        response.then().statusCode(404);
    }
}
