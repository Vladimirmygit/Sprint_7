import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class OrderListTest {
    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Тест получения списка заказов")
    @Description("Проверка успешного получения списка заказов")
    public void testGetOrdersList() {
        Response response = given()
                .get("/api/v1/orders");

        response.then().statusCode(200).body("orders", notNullValue());
    }
}