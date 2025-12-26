package com.burgers.tests;

import com.burgers.API.ApiUser;
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
        testUser = new User("testEmail_" + System.currentTimeMillis() + "@email.com", "parolOdin123", "Sponge Bob");
        Response response = ApiUser.createUser(testUser);
        token = takingAccessToken(response);
        response.then().statusCode(SC_OK).body("success", equalTo(true));
    }

    @Test
    @DisplayName("Пользователь который уже зарегистрирован")
    public void createDoubleUserFail(){
        testUser = new User("testEmail_Duplicate" + "@email.com", "parolOdin123", "Sponge Bob");
        Response firstResponse = ApiUser.createUser(testUser);
        token = takingAccessToken(firstResponse);

        Response secondResponse = ApiUser.createUser(testUser);
        secondResponse.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("User already exists"));

    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailFail(){
        testUser = new User(null, "password321", "patrick");

        Response response = ApiUser.createUser(testUser);
        response.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordFail(){
        testUser = new User("testEmail_" + System.currentTimeMillis() + "@email.com", null, "Squid");

        Response response = ApiUser.createUser(testUser);
        response.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameFail(){
        testUser = new User("testEmail_" + System.currentTimeMillis() + "@email.com", "password3142", null);

        Response response = ApiUser.createUser(testUser);
        response.then().statusCode(SC_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }

}
