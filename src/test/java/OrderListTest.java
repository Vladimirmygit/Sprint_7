import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest{

    @Test
    @DisplayName("Тест получения списка заказов")
    @Description("Проверка успешного получения списка заказов")
    public void testGetOrdersList() {
        RestAssured.baseURI = BASE_URI;

        Response response = RestAssured.given().get("/api/v1/orders");

        response.then().statusCode(200).body("orders", notNullValue());
    }
}