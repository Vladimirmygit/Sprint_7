import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class CourierLoginTest extends BaseTest {

    private String courierId;

    @Test
    @DisplayName("Тест успешной авторизации курьера")
    @Description("Проверка, что курьер может успешно авторизоваться")
    public void courierCanLoginSuccessfully() {
        String login = "Login" + UUID.randomUUID().toString(); // Генерация уникального логина
        String password = "12345678";

        // Создание курьера с указанным логином и паролем
        Response createResponse = courierClient.createCourier(login, password);
        createResponse.then().statusCode(201).body("ok", equalTo(true));

        // Получение ID курьера из метода логина
        courierId = courierClient.loginCourierAndGetId(login, password);

        // Проверка, что ID не пустой
        assertThat(courierId, notNullValue());
    }


    @Test
    @DisplayName("Проверка, что запрос вернет ошибку, если не указать все обязательные поля")
    @Description("Проверка, что система вернет ошибку статусом кода 404 Not Found при неправильном логине или пароле")
    public void loginRequiresAllMandatoryFields() {
        // Попытка авторизации без указания всех обязательных полей (логина и пароля)
        Response response = courierClient.loginCourier("", "");

        // Проверка, что запрос вернет ошибку статусом кода 400 Bad Request
        response.then().statusCode(400);

        // Проверка, что в ответе содержится ожидаемое сообщение об ошибке
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Проверка, что система вернет ошибку при неправильном логине или пароле")
    @Description("Проверка, что система вернет ошибку статусом кода 404 Not Found при неправильном логине или пароле")
    public void loginFailsWithIncorrectCredentials() {
        String login = "ninja12";
        String incorrectPass = "12342";

        // Попытка авторизации с неправильным логином и паролем
        Response response = courierClient.loginCourier(login, incorrectPass);

        // Проверка, что запрос вернет ошибку
        response.then().statusCode(404);
    }

    @Test
    @DisplayName("Проверка, что запрос вернет ошибку при авторизации под несуществующим пользователем")
    @Description("Проверка, что запрос вернет ошибку статусом кода 404 Not Found при авторизации под несуществующим пользователем")
    public void loginFailsWithNonExistentCourier() {
        String login = "non";
        String password = "Pass";

        // Попытка авторизации с несуществующим логином и паролем
        Response response = courierClient.loginCourier(login, password);

        // Проверка, что запрос вернет ошибку
        response.then().statusCode(404);
    }
}
