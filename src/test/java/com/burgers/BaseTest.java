package com.burgers;

import com.burgers.API.ApiUser;
import com.burgers.model.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static org.apache.http.HttpStatus.*;

public class BaseTest {
    protected String token;
    protected User testUser;

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
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
                Response deleteResponse = ApiUser.deleteUser(token);
                deleteResponse.then().statusCode(SC_ACCEPTED);
            } catch (Exception e){
                System.err.println("Failed to delete user: " + e.getMessage());
            }
        }
    }
}

