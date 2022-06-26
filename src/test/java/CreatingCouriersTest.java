import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.*;

public class CreatingCouriersTest {
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void removingCourier() {
        if (courier != null) {
            String idCourier = Steps.getId(courier);
            Steps.deleteCourier(idCourier);
        }
    }

    @Test
    @DisplayName("Check creating a courier")
    public void courierCreatingSuccessfully() {
        courier = Steps.createCourier("dtest1234" + new Random().nextInt(1000), "1234", "saske");
        Response response = Steps.postForCreationCourier(courier);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Check creating two identical couriers")
    public void notPossibleToCreateTwoIdenticalCouriers() {
        courier = Steps.createCourier("dtest1234" + new Random().nextInt(1000), "1234", "saske");
        Steps.postForCreationCourier(courier);
        Steps.postForCreationCourier(courier).then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("Check validation of required login")
    public void notPossibleToCreateCourierWithoutLogin() {
        Courier courierWithoutLogin = Steps.createCourier("", "1234", "saske");
        Response response = Steps.postForCreationCourier(courierWithoutLogin);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check validation of required password")
    public void notPossibleToCreateCourierWithoutPassword() {
        Courier courierWithoutPassword = Steps.createCourier("dtest1234" + new Random().nextInt(1000), "", "saske");
        Response response = Steps.postForCreationCourier(courierWithoutPassword);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

}
