package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstants;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class GetAPIRequest extends BaseTest{
	

	//Test to validate the JOSN Response Schema
	@Test
	public void VerifyResponseScehma() {	
		try {
		String jsonSchema =	FileUtils.readFileToString(new File(FileNameConstants.JSON_SCEHMA), "UTF-8");
//		String jsonSchema =	FileUtils.readFileToString(new File("/Users/monikayadav/Desktop/back up/intertrust/src/test/resources/expectedjsonscehema.txt"), "UTF-8");	
		System.out.println(jsonSchema);	
		///validating response Schema by keeping the schema in .txt file 
		RestAssured
				.given()
				.queryParam("unitGroup", "metric")
                .queryParam("key", "J4ASV3TPXGFGAFG5Z23386F5B")
                .queryParam("contentType", "json")
				.baseUri("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Guragoan")
			.when()
				.get()
			.then()
				.assertThat()
				.statusCode(200)	
				.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	//Test to validate the Response headers
	@Test
	public void verifyResponseHeadersStatusCodes() {
		
		RestAssured
				.given()
				.queryParam("unitGroup", "metric")
                .queryParam("key", "J4ASV3TPXGFGAFG5Z23386F5B")
                .queryParam("contentType", "json")
				.baseUri("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Guragoan")
			.when()
				.get()
			.then()
				.assertThat()
				.statusCode(200)
				.statusLine("HTTP/1.1 200 ")
				.header("Content-Type", "application/json")
				.header("Access-Control-Allow-Origin", "*")
				.header("X-Requested-With, Content-Type, X-CodingpediaAccess-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia")
				.header("Transfer-Encoding", "chunked")
				.header("Content-Encoding", "gzip")
				.header("X-Powered-By", "Visual Crossing Weather")
				.header("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
				
	}
	
	//test to Validate all the keys are present in response and their values
	@Test
	public void verifyResponseKeysAvailable() {
		
		Response response =
		RestAssured
				.given()
				.queryParam("unitGroup", "metric")
                .queryParam("key", "J4ASV3TPXGFGAFG5Z23386F5B")
                .queryParam("contentType", "json")
				.baseUri("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Guragoan")
			.when()
				.get()
			.then()
				.assertThat()
				.statusCode(200)	
			.extract()
				.response();
		//Assertion of the keys
		Assert.assertTrue(response.getBody().asString().contains("queryCost"));
		Assert.assertTrue(response.getBody().asString().contains("latitude"));
		Assert.assertTrue(response.getBody().asString().contains("longitude"));
		Assert.assertTrue(response.getBody().asString().contains("resolvedAddress"));
		Assert.assertTrue(response.getBody().asString().contains("address"));
		Assert.assertTrue(response.getBody().asString().contains("timezone"));
		Assert.assertTrue(response.getBody().asString().contains("tzoffset"));
		Assert.assertTrue(response.getBody().asString().contains("description"));
		
		//Comparing the values of Keys 
		String value  = response.path("days.datetime").toString();
		Assert.assertTrue(value.contains("2023-07-23"));
		
		JSONArray jsonarray = JsonPath.read(response.body().asString(), "$.timezone");
		String timezone = jsonarray.get(0).toString();
		Assert.assertEquals(timezone, "Asia/Kolkata");
		
				
	}
	
	

}
