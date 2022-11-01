import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import bank.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class BookRESTTest {

    @BeforeEach
    public static void setup() {
        RestAssured.port = Integer.valueOf(9090);
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "";
    }

    @Test
    public void testGetOneAccount() {
        // add the Account to be fetched
        Integer accountNumber = 12345;
        Account account = new Account(12345,"John Au");
        given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts").then()
                .statusCode(201);

        given()
                .when()
                .get("accounts/12345")
                .then()
                .contentType(ContentType.JSON)
                .and()
                .body("accountNumber",equalTo(12345))
                .body("accountHol0der", equalTo("John Au"))
                .body("balance", equalTo(0.0));
        //cleanup
        given()
                .when()
                .delete("accounts/"+accountNumber);
    }

    @Test
    public void testDeleteBook() {
        // add the Account to be deleted account
        Integer accountNumber = 12345;
        Account account = new Account(12345,"John Au");
        given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts").then()
                .statusCode(201);

        given()
                .when()
                .delete("accounts/"+accountNumber);

        given()
                .when()
                .get("accounts/"+accountNumber)
                .then()
                .statusCode(404)
                .and()
                .body("errorMessage",equalTo("Account with accountNumber= 123456789 is not available"));
    }

    @Test
    public void testAddBook() {
        // add the Account
        Integer accountNumber = 12345;
        Account account = new Account(12345,"John Au");
        given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts").then()
                .statusCode(201);
        // get the Account and verify
        given()
                .when()
                .get("accounts/12345")
                .then()
                .statusCode(200)
                .and()
                .body("accountNumber",equalTo(accountNumber));
        //cleanup
        given()
                .when()
                .delete("accounts/12345");
    }

}
