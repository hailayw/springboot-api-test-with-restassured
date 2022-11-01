package bank.controller;

import bank.domain.Account;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.equalTo;

@WebMvcTest(BankController.class)
public class BankControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        RestAssured.port = 90990;//Integer.valueOf(9090);
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "";

        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void testGetOneAccount() {
        Integer accountNumber = 12345;
        Account account = new Account(12345,"John Au");

        //create account
        RestAssuredMockMvc
            .given()
                .contentType("application/json")
                .body(account)
            .when()
                .post("/accounts")
            .then()
                .statusCode(201);

        //read
        RestAssuredMockMvc
            .given()
            .when()
                .get("accounts/12345")
            .then()
                .contentType(ContentType.JSON)
                .body("accountNumber",equalTo(12345))
                .body("accountHolder", equalTo("John Au"))
                .body("balance", equalTo(0.0f))
                .statusCode(200);
        //cleanup
        RestAssuredMockMvc
                .given()
                .when()
                    .delete("accounts/"+accountNumber)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    void testCreateAccount() throws JSONException {
        Integer accountNumber = 12345;
        Account account = new Account(accountNumber,"John Au");
        //create account
        RestAssuredMockMvc
            .given()
                .contentType("application/json")
                .body(account)
            .when()
                .post("/accounts")
            .then()
                .statusCode(201);
        //cleanup
        RestAssuredMockMvc
                .given()
                .when()
                .delete("accounts/"+accountNumber)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    void testDeposit() {
        //create account and deposit
        Integer accountNumber = 12345;
        Account account = new Account(accountNumber,"John Au");
        double amount = 50;

        //create account
        RestAssuredMockMvc
            .given()
                .contentType("application/json")
                .body(account)
            .when()
                .post("/accounts")
            .then()
                .statusCode(201);
        //deposit
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                .when()
                    .post("/accounts/12345/deposits?amount="+amount)
                .then()
                    .body("balance",equalTo(50.0f))
                    .statusCode(200);
        //deposit
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                .when()
                    .post("/accounts/12345")
                .then()
                    .body("balance",equalTo(50.0f))
                    .statusCode(200);
        //cleanup
        RestAssuredMockMvc
                .given()
                .when()
                .delete("accounts/"+accountNumber)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    void testWithdraw() {
        //create account and deposit
        Integer accountNumber = 12345;
        Account account = new Account(accountNumber,"John Au");
        double depositAmount = 50;
        double withdrawAmount = 20;

        //create account
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                    .body(account)
                .when()
                    .post("/accounts")
                .then()
                    .statusCode(201);
        //deposit 50
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                .when()
                    .post("/accounts/12345/deposits?amount="+depositAmount)
                .then()
                    .body("balance",equalTo(50.0f))
                    .statusCode(200);
        //withdraw 20
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                .when()
                    .post("/accounts/12345/withdrawals?amount="+withdrawAmount)
                .then()
                    .body("balance",equalTo(30.0f))
                    .statusCode(200);
        //verify current balance = 30
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .when()
                    .get("/accounts/12345")
                .then()
                    .body("balance", equalTo(30.0f))
                    .statusCode(200);
        //cleanup
        RestAssuredMockMvc
                .given()
                .when()
                .delete("accounts/"+accountNumber)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    void testGetAccountByAccNumber() {
        //create account and deposit
        Integer accountNumber = 12345;
        Account account = new Account(accountNumber,"John Au");

        //create account
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                    .body(account)
                .when()
                    .post("/accounts")
                .then()
                    .statusCode(201);
        //fetch account
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                .when()
                    .get("/accounts/12345")
                .then()
                    .body("accountNumber",equalTo(12345))
                    .statusCode(200);
        //cleanup
        RestAssuredMockMvc
                .given()
                .when()
                .delete("accounts/"+accountNumber)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test
    void testRemoveAccount() throws JSONException {
        //create account and deposit
        Account account1 = new Account(12345,"John Au");
        Account account2 = new Account(23456,"James Jo");

        //create account1
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                    .body(account1)
                .when()
                    .post("/accounts")
                .then()
                    .statusCode(201);
        //create account2
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body(account2)
                .when()
                .post("/accounts")
                .then()
                .statusCode(201);
        //delete account1
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .when()
                    .delete("/accounts/12345")
                .then()
                    .statusCode(204);
        //fetch account1
        RestAssuredMockMvc
                .given()
                    .contentType("application/json")
                .when()
                    .get("/accounts/12345")
                .then()
                    .statusCode(404);
       //cleanup
        //fetch account1
        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .when()
                .get("/accounts/23456")
                .then()
                .statusCode(404);
    }
}