package com.burgers.tests;

import com.burgers.api.UserApi;
import com.burgers.BaseTest;
import com.burgers.model.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUsersTest extends BaseTest{
    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUser(){
        User newUser = new User(
                faker.internet().emailAddress(),
                faker.internet().password(8,12,true,true,true),
                faker.name().firstName());
        Response response = UserApi.createUser(newUser);
        token = takingAccessToken(response);
        response.then().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Пользователь который уже зарегистрирован")
    public void createDoubleUserFail(){
        testUser = new User(faker.internet().emailAddress(), "parolOdin123", "Sponge Bob");
        Response firstResponse = UserApi.createUser(testUser);
        token = takingAccessToken(firstResponse);

        Response secondResponse = UserApi.createUser(testUser);
        secondResponse.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("User already exists"));

    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailFail(){
        testUser = new User(null, "password321", faker.name().firstName());

        Response response = UserApi.createUser(testUser);
        response.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordFail(){
        testUser = new User(faker.internet().emailAddress(), null, faker.name().firstName());

        Response response = UserApi.createUser(testUser);
        response.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameFail(){
        testUser = new User(faker.internet().emailAddress(), faker.internet().password(8, 12), null);

        Response response = UserApi.createUser(testUser);
        response.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

}
