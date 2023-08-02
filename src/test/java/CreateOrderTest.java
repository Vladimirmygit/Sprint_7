import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {
    private String color;

    public CreateOrderTest(String color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static List<String> colorData() {
        return Arrays.asList("BLACK", "GREY", "");
    }

    @Test
    @DisplayName("Тест создания заказа с различными вариантами цветов")
    @Description("Проверка успешного создания заказа с различными вариантами цветов")
    public void testCreateOrderWithColors() {
        String firstName = "Naruto";
        String lastName = "Uchiha";
        String address = "Konoha, 142 apt.";
        int metroStation = 4;
        String phone = "+7 800 355 35 35";
        int rentTime = 5;
        String deliveryDate = "2020-06-06";
        String comment = "Saske, come back to Konoha";

        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, Arrays.asList(color));

        Response response = orderClient.createOrder(order);

        response.then().statusCode(201).body("track", notNullValue());
    }
}
