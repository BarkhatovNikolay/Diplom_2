package com.burgers.API;

import com.burgers.model.Login;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthorizationApi {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private static final String LOGIN = "/api/auth/login";

    @Step("Логин пользователя")
    public static Response login(Login login){
        System.out.println("Logging in user: " + login.getEmail());

        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(login)
                .when()
                .post(LOGIN);

        System.out.println("Login response status: " + response.statusCode());

        return response;
    }
}
