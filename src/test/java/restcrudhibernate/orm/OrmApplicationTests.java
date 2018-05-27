package restcrudhibernate.orm;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import restcrudhibernate.OrmApplication;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrmApplicationTests {

    @BeforeClass
    public static void setUp() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 80;
    }
    @Test
    public void shouldReturnOkStatusCode()  {
        given().port(port).when().request("GET", "/players/get").then().statusCode(200);
    }
    @Test
    public void shouldReturnStatus200AfterAddingPlayerWithWrongNickname() {
        given().port(port).when().request("POST", "/player/add?nickname=&vacban=0&accountvalue=322.43&steamid=99999999999").then().statusCode(400);
    }

    @Test
    public void shouldReturnStatus200()  {
        given().port(port).when().request("GET", "/games/get").then().statusCode(200);
    }

    @Test
    public void shouldReturnStatus404()  {
        given().port(port).when().request("GET", "/Guest/get?id=-1").then().statusCode(404);
    }

    @Test
    public void shouldReturnStatus400WhenDeletingNonExistingObject()  {
        given().port(port).when().request("DELETE", "/Guest/delete?id=a").then().statusCode(404);
    }

    @Test
    public void shouldReturnJSONWhenGettingPlayer()  {
        given().port(port).when().request("GET", "/players/get").then().contentType(ContentType.JSON);
    }

    @Test
    public void shouldReturnOkStatusCodeWhenGettingRoom()  {
        given().port(port).when().request("GET", "/player/get?steamid=1").then().statusCode(200);
    }


    @Test
    public void shouldReturnStatus200AfterGettingGameWithId2() {
        given().port(port).when().request("GET", "/game/get?appid=2").then().statusCode(200);
    }

    @Test
    public void shouldReturnStatus406WhenWrongIdBuyingWrongGame()  {
        given().port(port).when().request("POST", "/buy/-1/-1").then().statusCode(406);
    }

    @Test
    public void shouldReturnStatus404WhenAddringReservationWithNonExistingRoomId()  {
        given().port(port).when().request("PUT", "/Guest/reserve?guestID=2&roomID=10000").then().statusCode(404);
    }

}
