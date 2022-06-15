import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public final class Steps {

    @Step("Create a courier")
    public static Courier createCourier(String login, String password, String firstName) {
        return new Courier(login, password, firstName);
    }

    @Step("POST for creation a courier")
    public static Response postForCreationCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Login as a courier")
    public static String getId(Courier courier) {
        String jsonCourier = "{\"login\": \"" + courier.getLogin() + "\", \"password\": \"" + courier.getPassword() + "\"}";
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonCourier)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id").toString();
    }

    @Step("Delete a courier")
    public static void deleteCourier(String id) {
        given().delete("/api/v1/courier/{id}", id).then().statusCode(200);
    }

    @Step("Create an order")
    public static Order createOrder(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @Step("POST for creation an order")
    public static Response postForCreationOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Get track number of order")
    public static String getTrack(Response response) {
        return response.then().extract().body().path("track").toString();
    }

    @Step("Cancel an order")
    public static void cancelOrder(String track) {
        given().queryParams("track", track).when()
                .put("/api/v1/orders/cancel").then().statusCode(200);
    }

    @Step("Login")
    public static Response login(String body){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post("/api/v1/courier/login");
    }
}
