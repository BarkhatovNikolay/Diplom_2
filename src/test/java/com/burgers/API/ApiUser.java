package com.burgers.API;
import com.burgers.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ApiUser {
private static final String BASE_URL = "https://stellarburgers.education-services.ru";
private static final String CREATE_USER = "/api/auth/register";
private static final String DELETE_USER = "/api/auth/user";

@Step("Создание пользователя")
    public static Response createUser(User user){
    return given()
            .header("Content-type", "application/json")
            .baseUri(BASE_URL)
            .body(user)
            .when()
            .post(CREATE_USER);
}
@Step("Удаление пользователя")
    public static Response deleteUser(String token){
    return given()
            .header("Content-type", "application/json")
            .header("Authorization", token)
            .baseUri(BASE_URL)
            .when()
            .delete(DELETE_USER);
 }
}
