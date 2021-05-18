package com.smartentities.json.generator.generators;

import java.util.Map;
import java.util.Map.Entry;

import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.json.JSONObject;

import com.smartentities.json.generator.GeneratorFactory;

public class ObjectGenerator extends JsonValueGenerator<JSONObject> {

	public ObjectGenerator(Schema schema) {
		super(schema);
	}

	/**
	 * Generates a valid Object from the specified schema.
	 * Generates all properties and ignores the "additionalProperties" and "required" fields.
	 * Currently supports minimum/maximum lengths.
	 * @return JSONObject: A valid object as specified in the schema
	 */
	@Override
	public JSONObject generate() {
		if (schema instanceof ObjectSchema) {
			ObjectSchema sc = (ObjectSchema) schema;
			Map<String, Schema> map = sc.getPropertySchemas();
			int lenProperties = map.size();
			JSONObject object = new JSONObject();
			int minProperties = 0;
			boolean hasMin = true;
			int maxProperties = 0;
			boolean hasMax = true;

			try {
				minProperties = sc.getMinProperties();
			} catch (NullPointerException e) {
				hasMin = false;
			}

			try {
				maxProperties = sc.getMaxProperties();
			} catch (NullPointerException e) {
				hasMax = false;
			}

			// Check if correct amount of items are specified
			if (hasMax && hasMin) {
				if (maxProperties < minProperties) {
					throw new IllegalArgumentException("Minimal length should be smaller than maximal length. Please check schema.");
				}

				if (lenProperties > maxProperties || lenProperties < minProperties) {
					throw new IllegalArgumentException("Too many/few items specified. Please check schema.");
				}
			} else if (hasMax) {
				if (lenProperties > maxProperties) {
					throw new IllegalArgumentException("Too many items specified. Please check schema.");
				}
			} else if (hasMin) {
				if (lenProperties < minProperties) {
					throw new IllegalArgumentException("Too few items specified. Please check schema.");
				}
			}

			for (Entry<String, Schema> entry : map.entrySet()) {
				String key = entry.getKey();

				object.put(key, GeneratorFactory.getGenerator(entry.getValue()).generate());
			}
			return object;
		}
		return null;
	}
}
