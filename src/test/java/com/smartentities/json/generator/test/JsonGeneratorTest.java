package com.smartentities.json.generator.test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.smartentities.json.generator.GeneratorConfig;
import com.smartentities.json.generator.JsonGenerator;

public class JsonGeneratorTest {

	public static void main(String[] args) {
		try {
			String schemaPath = "src/test/resources/Schema4.json";
			GeneratorConfig generatorConfig= GeneratorConfig.fromSchemaPath(schemaPath);
			
			JsonGenerator jsonGenerator = new JsonGenerator(generatorConfig);
			
			String json = jsonGenerator.generate();
			System.out.println(json);
			
			
			//Validate generated message
			JSONTokener tokener = new JSONTokener(new ByteArrayInputStream(json.getBytes(Charset.defaultCharset())));
			char token = tokener.next();
			tokener.back();
			if (token == '{') {
				JSONObject jsonSubject = new JSONObject(tokener);
				Schema schema = SchemaLoader.load(generatorConfig.getJsonSchema());
				schema.validate(jsonSubject);
			} else if (token == '[') {
				JSONArray jsonSubject = new JSONArray(tokener);
				Schema schema = SchemaLoader.load(generatorConfig.getJsonSchema());
				schema.validate(jsonSubject);
			} else {
				throw new IllegalArgumentException("JSON Schema contains illegal syntax");
			}
			
		} catch (JSONException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
