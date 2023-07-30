import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class CreateCourierTest {
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
    @DisplayName("Новый курьер")
    @Description("Создание нового курьера")
    public void courierCreatedTest() {
        Courier courier = new Courier("Test224", "Test225", "Test226");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        System.out.println(response.getBody().asString());
        response.then().statusCode(201)
                .and()
                .body("ok", equalTo(true));

        // Получение ID курьера из метода логина
        courierId = CourierAPIHelper.loginCourier(courier.getLogin(), courier.getPassword());
        // Проверка, что ID не пустой
        assert courierId != null;
    }
    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCourierTest() {
        Courier courier = new Courier("adda2", "adda22", "adda21");

        // Первый запрос на создание курьера
        Response response1 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response1.then().assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));

        // Второй запрос на создание курьера с тем же логином
        Response response2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response2.then().assertThat().statusCode(409);

        // Получение ID курьера из метода логина
        courierId = CourierAPIHelper.loginCourier(courier.getLogin(), courier.getPassword());

        // Проверка, что ID не пустой
        assert courierId != null;
    }

    @Test
    @DisplayName("Нужно передать все обязательные поля")
    @Description("Проверка, что запрос возвращает ошибку, если не передать все обязательные поля")
    public void missingRequiredFieldsTest() {
        Courier courier = new Courier("Test553", "Test553", null);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().statusCode(400); // 400 - неверный запрос, недостаточно данных
    }
}


