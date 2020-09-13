package com.redprojects.mediateca;

import com.redprojects.mediateca.communication.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.json.Json;
import javax.json.JsonObject;

@SpringBootTest
class MediaTecaMediaServerApplicationTests {

	@Test
	void contextLoads() {
		Response response = new Response("Test");

		response.actionSucceeded();
		response.setMessage("Testing New Data Implementation");
		response.addBodyElement("authToken", "apoirgjpowirjpoijadsffawelfkhlashdjkfhwefhiuhgiu23948y9834uyfgqpohqygkldhfgliaqwu");
		String output = response.toJson().toString();
		System.out.println(output);

		// expected resuts
		JsonObject expectedResult = Json.createObjectBuilder()
				.add("action", "Test")
				.add("status", Json.createObjectBuilder()
						.add("actionSuccess", "FAILED")
						.add("message", "Testing New Data Implementation")
						.build())
				.build();

		System.out.println(expectedResult.toString());
	}

}
