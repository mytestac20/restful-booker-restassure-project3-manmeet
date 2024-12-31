package com.restful.booker.crudtest;

import com.restful.booker.constant.EndPoints;

import com.restful.booker.model.RestfulBookerPojo;
import com.restful.booker.testbase.TestBase;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookingTest extends TestBase {

    static String userName = "admin";
    static String password = "password123";
    static String token;
    static int bookingId;
    static String fName;
    static String lName;
    static String checkIn;
    static String checkOut;
    static String createUserName = "jigar";
    static String createPassword = "dhanani";
    static String firstName = "jigar1";
    static String lastName = "dh1";
    static int totalPrice = 900;
    static boolean depositpaid = true;
    static String additionalneeds = "Dinner";

    @Test(priority = 1)
    public void createToken() {

        RestfulBookerPojo restfulBookerPojo = new RestfulBookerPojo();
        restfulBookerPojo.setUsername(userName);
        restfulBookerPojo.setPassword(password);

        ValidatableResponse response = given().log().all()
                .header("Content-Type", "application/json")
                .when()
                .body(restfulBookerPojo)
                .post(EndPoints.GET_TOKEN)
                .then().log().all().statusCode(200);
        token = response.extract().path("token");
        System.out.println(token);
    }

    @Test(priority = 2)
    public void getAllBookingsIds() {
        ValidatableResponse response = given().log().all()
                .header("Content-Type", "application/json")
                .when()
                .get(EndPoints.GET_ALL_BOOKING_IDS)
                // response.prettyPrint();
                .then().log().all().statusCode(200);

        bookingId = response.extract().path("[0].bookingid");
        response.body("[0].bookingid", equalTo(bookingId));

    }

    @Test(priority = 3)
    public void getBookingById() {
        ValidatableResponse response = given().log().all()
                .header("Content-Type", "application/json")
                .pathParam("id", bookingId)
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then().log().all().statusCode(200);
        fName = response.extract().path("firstname");
        lName = response.extract().path("lastname");
        checkIn = response.extract().path("bookingdates.checkin");
        checkOut = response.extract().path("bookingdates.checkout");
        response.body("firstname", equalTo(fName),
                "lastname", equalTo(lName),
                "bookingdates.checkin", equalTo(checkIn),
                "bookingdates.checkout", equalTo(checkOut));
    }

    @Test(priority = 4)
    public void getBookingByFirstNameAndLastName() {
        Map<String, String> map = new HashMap<>();
        map.put("firstname", fName);
        map.put("lastname", lName);

        ValidatableResponse response = given().log().all()
                .header("Content-Type", "application/json")
                .queryParams(map)
                .when()
                .get(EndPoints.GET_BOOKING_BY_FIRSTNAME_LASTNAME)
                .then().log().all().statusCode(200);

        response.body("",hasItem(hasEntry("bookingid",1139)));
    }

    @Test(priority = 5)
    public void getBookingByCheckInAndCheckOut() {

        Map<String, String> date = new HashMap<>();
        date.put("checkin", checkIn);
        date.put("checkout", checkOut);

        ValidatableResponse response = given().log().all()
                .header("Content-Type", "application/json")
                .queryParams(date)
                .when()
                .get(EndPoints.GET_BOOKING_BY_CHECKING_CHECKOUT)

                .then().log().all().statusCode(200);
        response.body("",hasItem(hasEntry("bookingid",1139)));

    }

    @Test(priority = 6)
    public void createBooking() {

        String checkInDate = "2019-01-01";
        String checkOutDate = "2020-01-01";

        Map<String, String> booking = new HashMap<>();
        booking.put("checkin", checkInDate);
        booking.put("checkout", checkOutDate);

        RestfulBookerPojo userPojo = new RestfulBookerPojo();
        userPojo.setUsername(createUserName);
        userPojo.setPassword(createPassword);
        userPojo.setFirstname(firstName);
        userPojo.setLastname(lastName);
        userPojo.setTotalprice(totalPrice);
        userPojo.setDepositpaid(depositpaid);
        userPojo.setCheckin(checkInDate);
        userPojo.setCheckout(checkOutDate);
        userPojo.setAdditionalneeds(additionalneeds);
        userPojo.setBookingdates(booking);


        ValidatableResponse response = given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when()
                .body(userPojo)
                .post(EndPoints.CREATE_BOOKING)
                .then().log().all().statusCode(200);
    }

    @Test(priority = 7)
    public void updateBooking() {
        String checkInDate = "2019-01-01";
        String checkOutDate = "2020-01-01";

        Map<String, String> booking = new HashMap<>();
        booking.put("checkin", checkInDate);
        booking.put("checkout", checkOutDate);

        RestfulBookerPojo userPojo = new RestfulBookerPojo();
        userPojo.setUsername(createUserName + "updated");
        userPojo.setPassword(createPassword + "updated");
        userPojo.setFirstname(firstName + "updated");
        userPojo.setLastname(lastName + "updated");
        userPojo.setTotalprice(totalPrice);
        userPojo.setDepositpaid(depositpaid);
        userPojo.setCheckin(checkInDate);
        userPojo.setCheckout(checkOutDate);
        userPojo.setAdditionalneeds(additionalneeds);
        userPojo.setBookingdates(booking);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");
        header.put("Cookie", "token=" + token);

        Response response = given().log().all()
                .headers(header)
                .pathParam("id", bookingId)
                .when()
                .body(userPojo)
                .put(EndPoints.UPDATE_BOOKING_BY_ID);
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 8)
    public void partialBooking() {
        String checkInDate = "2019-01-01";
        String checkOutDate = "2020-01-01";

        Map<String, String> booking = new HashMap<>();
        booking.put("checkin", checkInDate);
        booking.put("checkout", checkOutDate);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept", "application/json");
        header.put("Cookie", "token=" + token);

        RestfulBookerPojo userPojo = new RestfulBookerPojo();
        userPojo.setUsername(createUserName + "updated");
        userPojo.setPassword(createPassword);
        userPojo.setFirstname(firstName);
        userPojo.setLastname(lastName);
        userPojo.setTotalprice(totalPrice);
        userPojo.setDepositpaid(depositpaid);
        userPojo.setCheckin(checkInDate);
        userPojo.setCheckout(checkOutDate);
        userPojo.setAdditionalneeds(additionalneeds);
        userPojo.setBookingdates(booking);


        Response response = given().log().all()
                .headers(header)
                .pathParam("id", bookingId)
                .when()
                .body(userPojo)
                .patch(EndPoints.UPDATE_BOOKING_BY_ID);
        response.prettyPrint();
        response.then().statusCode(200);
    }

    @Test(priority = 9)
    public void deleteTheBooking() {
        given().log().all()
                .header("Cookie", "token=" + token)
                .pathParam("id", bookingId)
                .when()
                .delete(EndPoints.DELETE_BOOKING_BY_ID)
                .then().log().all()
                .statusCode(201);


        given()
                .pathParam("id", bookingId)
                .when()
                .get(EndPoints.GET_BOOKING_BY_ID)
                .then().log().all().statusCode(404);

    }
}