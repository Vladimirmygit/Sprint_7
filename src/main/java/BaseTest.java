import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseTest {
    protected static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    protected CourierClient courierClient;
    protected OrderClient orderClient;
    private String courierId;

    @Before
    public void baseSetUp() {
        RestAssured.baseURI = BASE_URI;
        courierClient = new CourierClient();
        orderClient = new OrderClient();
    }

    @After
    public void baseTearDown() {
        if (courierId != null) {
            // Удаление курьера по ID после каждого тестового метода
            courierClient.deleteCourierById(courierId);
            courierId = null;
        }
    }
}
