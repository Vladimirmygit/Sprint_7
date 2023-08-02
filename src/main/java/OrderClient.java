import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderClient {
    private static final String ORDERS_ENDPOINT = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .contentType("application/json")
                .body(order)
                .post(ORDERS_ENDPOINT);
    }

    @Step("Удаление заказа по ID")
    public void deleteOrderById(String orderId) {
        given()
                .pathParam("id", orderId)
                .when()
                .delete(ORDERS_ENDPOINT + "/{id}")
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}
