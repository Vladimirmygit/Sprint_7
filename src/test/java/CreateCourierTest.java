import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

public class CreateCourierTest extends BaseTest {
    @Test
    @DisplayName("Новый курьер")
    @Description("Создание нового курьера")
    public void courierCreatedTest() {
        String baseLogin = "TestCourier"; // Базовая часть логина
        String password = "adda222";
        String name = "adda212";

        // Генерируем уникальный идентификатор (UUID) и добавляем его к базовой части логина
        String login = baseLogin + UUID.randomUUID().toString();

        Courier courier = new Courier(name, login, password);

        CourierClient courierClient = new CourierClient();

        // Проверка уникальности логина
        courierClient.assertCourierLoginIsUnique(courier);

        // Создание курьера и получение ID
        String courierId = courierClient.createCourierAndGetId(courier.getLogin(), courier.getPassword());

        // Проверка, что ID курьера не пустой
        assertThat(courierId, notNullValue());
    }


    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCourierTest() {
        String baseLogin = "TestCourier"; // Базовая часть логина
        String password = "adda222";
        String name = "adda212";

        // Генерируем уникальный идентификатор (UUID) и добавляем его к базовой части логина
        String login = baseLogin + UUID.randomUUID().toString();

        Courier courier = new Courier(name, login, password);

        CourierClient courierClient = new CourierClient();

        // Первый запрос на создание курьера
        Response response1 = courierClient.createCourier(courier.getLogin(), courier.getPassword());
        response1.then().assertThat().statusCode(201).and().body("ok", equalTo(true));

        // Получение ID курьера из метода логина
        String courierId = courierClient.loginCourierAndGetId(courier.getLogin(), courier.getPassword());

        // Второй запрос на создание курьера с тем же логином
        Response response2 = courierClient.createCourier(courier.getLogin(), courier.getPassword());
        response2.then().assertThat().statusCode(409);
    }

    @Test
    @DisplayName("Нужно передать все обязательные поля")
    @Description("Проверка, что запрос возвращает ошибку, если не передать все обязательные поля")
    public void missingRequiredFieldsTest() {
        String baseLogin = "TestCourier"; // Базовая часть логина
        String password = "";
        String name = "adda212";

        // Генерируем уникальный идентификатор (UUID) и добавляем его к базовой части логина
        String login = baseLogin + UUID.randomUUID().toString();

        Courier courier = new Courier(name, login, password);

        CourierClient courierClient = new CourierClient();

        // Попытка создания курьера с недостаточными данными
        Response response = courierClient.createCourier(courier.getLogin(), courier.getPassword());
        response.then().assertThat().statusCode(400); // 400 - неверный запрос, недостаточно данных
    }
}
