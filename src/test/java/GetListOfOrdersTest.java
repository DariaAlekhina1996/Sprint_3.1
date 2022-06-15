import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetListOfOrdersTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check list of orders")
    public void listOfOrders() {
        given()
                .get("/api/v1/orders")
                .then().body("orders", hasSize(greaterThanOrEqualTo(0)))
                .and()
                .statusCode(200);
    }
}

