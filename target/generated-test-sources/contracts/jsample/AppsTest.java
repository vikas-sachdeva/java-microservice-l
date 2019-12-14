package jsample;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.module.webtestclient.response.WebTestClientResponse;
import io.restassured.module.webtestclient.specification.WebTestClientRequestSpecification;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jsample.AppBaseClass;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.webtestclient.RestAssuredWebTestClient.*;
import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;

public class AppsTest extends AppBaseClass {

	@Test
	public void validate_addApp() throws Exception {
		// given:
			WebTestClientRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"name\":\"app-1\",\"running\":true}");

		// when:
			WebTestClientResponse response = given().spec(request)
					.post("/addApp");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['running']").isEqualTo(true);
			assertThatJson(parsedJson).field("['createdAt']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['id']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['name']").isEqualTo("app-1");
			assertThatJson(parsedJson).field("['lastModifiedDate']").matches("^\\s*\\S[\\S\\s]*");
	}

	@Test
	public void validate_deleteApp() throws Exception {
		// given:
			WebTestClientRequestSpecification request = given();

		// when:
			WebTestClientResponse response = given().spec(request)
					.delete("/deleteApp/2");

		// then:
			assertThat(response.statusCode()).isEqualTo(204);
	}

	@Test
	public void validate_getApp() throws Exception {
		// given:
			WebTestClientRequestSpecification request = given();

		// when:
			WebTestClientResponse response = given().spec(request)
					.get("/getApps");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['running']").matches("(true|false)");
			assertThatJson(parsedJson).field("['createdAt']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['id']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['name']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['lastModifiedDate']").matches("^\\s*\\S[\\S\\s]*");
	}

	@Test
	public void validate_updateApp() throws Exception {
		// given:
			WebTestClientRequestSpecification request = given()
					.header("Content-Type", "application/json")
					.body("{\"name\":\"app-3\",\"running\":false,\"lastModifiedDate\":\"2019-09-17T10:44:50.953+0000\"}");

		// when:
			WebTestClientResponse response = given().spec(request)
					.put("/updateApp/3");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");
		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['createdAt']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['id']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['running']").isEqualTo(false);
			assertThatJson(parsedJson).field("['lastModifiedDate']").matches("^\\s*\\S[\\S\\s]*");
			assertThatJson(parsedJson).field("['name']").isEqualTo("app-3");
	}

}
