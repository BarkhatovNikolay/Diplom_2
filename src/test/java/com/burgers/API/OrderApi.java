package com.burgers.API;
import com.burgers.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static  io.restassured.RestAssured.given;

public class OrderApi {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private static final String CREATE_ORDER = "/api/orders";
    private static final String INGREDIENTS = "/api/ingredients";

    @Step("Создание заказа и авторизация")
    public static Response createOrderWithAuth(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }
    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutAuth(Order order){
        return given()
                .header("Content-type","application/json")
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }

    @Step("Получения списка ингредиентов")
    public static Response getIngredients(){
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .when()
                .get(INGREDIENTS);
    }
}
