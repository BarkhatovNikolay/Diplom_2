package com.burgers;

import com.burgers.api.UserApi;
import com.burgers.model.User;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static org.apache.http.HttpStatus.*;

public class BaseTest {
    protected String token;
    protected User testUser;
    protected Faker faker;

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        faker = new Faker();

        if (this.getClass().getSimpleName().equals("LoginTest") ||
            this.getClass().getSimpleName().equals("OrderTest")) {
            createTestUser();
        }
    }

    protected void createTestUser(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 12, true,true,true);
        String name = faker.name().firstName();

        testUser = new User(email, password, name);
        Response response = UserApi.createUser(testUser);
        token = takingAccessToken(response);
    }

    protected String takingAccessToken(Response response) {
        return response
                .then()
                .extract()
                .path("accessToken");
    }
    @After
    public void tearDown(){
        if (token != null){
            try {
                Response deleteResponse = UserApi.deleteUser(token);
                deleteResponse.then().statusCode(SC_ACCEPTED);
            } catch (Exception e){
                System.err.println("Failed to delete user: " + e.getMessage());
            }
        }
    }
}

