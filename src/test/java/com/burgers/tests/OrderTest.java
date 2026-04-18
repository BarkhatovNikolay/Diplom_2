package com.burgers.tests;
import com.burgers.BaseTest;
import com.burgers.api.AuthorizationApi;
import com.burgers.api.OrderApi;
import com.burgers.model.Login;
import com.burgers.model.Order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static  org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class OrderTest extends BaseTest {

    private List<String> getValidIngredients() {
        Response ingredientsResponse = OrderApi.getIngredients();
        List<String> ingredients = ingredientsResponse.then()
                .extract()
                .path("data._id");

        return ingredients.subList(0, Math.min(2, ingredients.size()));
    }

    @Test
    @DisplayName("Заказ с авторизацией и ингредиентами")
    public void orderWithAuthAndIngredientsSuccess(){
        Login login = new Login(testUser.getEmail(), testUser.getPassword());

        Response loginResponse = AuthorizationApi.login(login);
        token = takingAccessToken(loginResponse);

        List<String> ingredients = getValidIngredients();
        Order order = new Order(ingredients);

        Response response = OrderApi.createOrderWithAuth(order, token);

        response.then().statusCode(SC_OK)
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    public void orderWithoutAuthAndWithIngredientsSuccess(){
        List<String> ingredients = getValidIngredients();
        Order order = new Order(ingredients);

        Response response = OrderApi.createOrderWithoutAuth(order);

        response.then().statusCode(SC_OK)
                .body("name", notNullValue())
                .body("order.number", notNullValue())
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void orderWithoutIngredientsFail(){
                Login login = new Login(testUser.getEmail(), testUser.getPassword());

                Response loginResponse = AuthorizationApi.login(login);
                token = takingAccessToken(loginResponse);

                Order order = new Order(Collections.emptyList());
                Response response = OrderApi.createOrderWithAuth(order, token);

                response.then().statusCode(SC_BAD_REQUEST)
                        .body("success", equalTo(false))
                        .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем")
    public void orderWithInvalidIngredientsHashFail(){
        Login login = new Login(testUser.getEmail(), testUser.getPassword());

        Response loginResponse = AuthorizationApi.login(login);
        token = takingAccessToken(loginResponse);

        Order order = new Order(Arrays.asList("invalid_hash_1", "invalid_hash_2"));
        Response response = OrderApi.createOrderWithAuth(order, token);

        response.then()
                .statusCode(greaterThanOrEqualTo(SC_INTERNAL_SERVER_ERROR));
    }
}
