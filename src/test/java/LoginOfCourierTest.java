import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginOfCourierTest {
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
    @DisplayName("Check login as a courier")
    public void courierLoginSuccessfully() {
        courier = Steps.createCourier("dtest1234" + new Random().nextInt(1000), "1234", "saske");
        Steps.postForCreationCourier(courier).then().statusCode(201);
        Login login = new Login(courier.getLogin(), courier.getPassword());
        Response response = Steps.login(login);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check login as a courier without login")
    public void notPossibleToLoginWithoutLogin() {
        courier = Steps.createCourier("dtest1234" + new Random().nextInt(1000), "1234", "saske");
        Steps.postForCreationCourier(courier).then().statusCode(201);
        Login login = new Login("", courier.getPassword());
        Response response = Steps.login(login);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check login as a courier without password")
    public void notPossibleToLoginWithoutPassword() {
        courier = Steps.createCourier("dtest1234" + new Random().nextInt(1000), "1234", "saske");
        Steps.postForCreationCourier(courier).then().statusCode(201);
        Login login = new Login(courier.getLogin(), "");
        Response response = Steps.login(login);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check login as a courier with non - existent pair login-password")
    public void notPossibleToLoginWithNonExistentPairLoginPassword() {
        courier = Steps.createCourier("dtest1234" + new Random().nextInt(1000), "1234", "saske");
        Steps.postForCreationCourier(courier).then().statusCode(201);
        Login login = new Login(courier.getLogin(), courier.getPassword() + "1");
        Response response = Steps.login(login);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }
}