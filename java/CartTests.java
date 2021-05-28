import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CartTests {
    private static String COOKIE = "Nop.customer=a3706724-8ba7-4776-b4ac-217dcdaded4c;";

    @Test
    @DisplayName("Adding a product to the cart")
    void addingProductInCartTest() {
        //check how many items are in cart
        int quantity = given()
                .contentType("text/html")
                .cookie(COOKIE)
                .when()
                .get("http://demowebshop.tricentis.com/cart")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .htmlPath()
                .getInt("**.findAll { it.@class == 'quantity' }");

        //add a product in the cart
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(COOKIE)
                .body("addtocart_13.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/13/1")
                .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                .body("updatetopcartsectionhtml", is("(" + (quantity + 1) + ")"));

        //check the number of items in the cart on UI
        open("http://demowebshop.tricentis.com/computing-and-internet");
        $(".cart-label").click();

        Cookie cookie = new Cookie("Nop.customer", "a3706724-8ba7-4776-b4ac-217dcdaded4c");
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
        Selenide.refresh();

        $(byAttribute("name", "itemquantity1802345"))
                .shouldHave(attribute("value", String.valueOf(quantity + 1)));
    }
}
