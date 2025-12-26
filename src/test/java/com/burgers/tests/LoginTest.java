package com.burgers.tests;
import com.burgers.BaseTest;
import com.burgers.API.AuthorizationApi;
import com.burgers.API.ApiUser;
import com.burgers.model.Login;
import com.burgers.model.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest extends BaseTest {
    @Test
    @DisplayName("Логин пользователя")
    public void loginUserSuccess(){
        testUser = new User("TestLogin_" + System.currentTimeMillis() + "@email.com", "password321", "Egor");
        ApiUser.createUser(testUser);

        Login login = new Login(testUser.getEmail(), testUser.getPassword());

        Response response = AuthorizationApi.login(login);
        token = takingAccessToken(response);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(testUser.getEmail().toLowerCase()))
                .body("user.name", equalTo(testUser.getName()));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithWrongPasswordFail(){
        testUser = new User("WrongUserPassword_"+ System.currentTimeMillis()+ "@email.com", "password3212", "Victor");

        Response createUser = ApiUser.createUser(testUser);
        token = takingAccessToken(createUser);

        Login login = new Login(testUser.getEmail(), "wrongPassword213");

        Response response = AuthorizationApi.login(login);
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Логин с несуществующим email")
    public void  loginWithIncorrectEmailFail(){
        Login login = new Login("WrongEmail222@email.com", "130Password");

        Response response = AuthorizationApi.login(login);
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));


    }
}
