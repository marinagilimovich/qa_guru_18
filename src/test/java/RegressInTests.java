import models.UserData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegressInTests {
    //    Using models
    @Test
    void singleResourceSuccessfulTest() {
        UserData data = Specs.request
                .when()
                .get("/unknown/2")
                .then()
                .spec(Specs.responseSpec)
                .log().body()
                .extract().as(UserData.class);

        assertEquals(2, data.getData().getId());
        assertEquals("fuchsia rose", data.getData().getName());
    }

    //    Using lombok
    @Test
    void registrationSuccessfulTest() {
        testlombok.User data = Specs.request
                .body("{ \"email\": \"eve.holt@reqres.in\", " +
                        "\"password\": \"pistol\" }")
                .when()
                .post("/register")
                .then()
                .spec(Specs.responseSpec)
                .log().body()
                .extract().as(testlombok.User.class);

        assertEquals(4, data.getId());
        assertEquals("QpwL5tke4Pnpja7X4", data.getToken());
    }

    @Test
    void loginSuccessfulTestWithLombok() {
        testlombok.User data = Specs.request
                .body("{ \"email\": \"eve.holt@reqres.in\", " +
                        "\"password\": \"cityslicka\" }")
                .when()
                .post("/login")
                .then()
                .spec(Specs.responseSpec)
                .log().body()
                .extract().as(testlombok.User.class);

        assertEquals("QpwL5tke4Pnpja7X4", data.getToken());
    }

    // groovy
    @Test
    void listUsersTest() {
        Specs.request
                .when()
                .get("/users?page=2")
                .then()
                .log().body()
                .spec(Specs.responseSpec)
                .body("data.findAll{it.last_name = 'Edwards'}.email.flatten()",
                        hasItem("george.edwards@reqres.in"));
    }

}
