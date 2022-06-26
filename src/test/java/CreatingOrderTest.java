import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreatingOrderTest {

    private final String[] colors;

    public CreatingOrderTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[] getColors() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check creating an order")
    public void orderCreatingSuccessfully() {
        Order order = Steps.createOrder("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2022-06-06", "Saske, come back to Konoha", colors);
        Response response = Steps.postForCreationOrder(order);
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
        String trackOrder = Steps.getTrack(response);
        Steps.cancelOrder(trackOrder);
    }
}
