package com.smartentities.json.generator.generators;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;
import org.json.JSONArray;

import com.smartentities.json.generator.GeneratorFactory;

import java.util.List;
import java.util.Random;

public class ArrayGenerator extends JsonValueGenerator<JSONArray> {

	public ArrayGenerator(Schema schema) {
		super(schema);
	}

	/**
	 * Generates the items specified in the array. Assumes that the amount of items is within the range specified
	 * in the schema.
	 * @return JSONArray output: A valid array as specified in the schema.
	 */
	@Override
	public JSONArray generate() {
		if (schema instanceof ArraySchema) {
			Random random = new Random();
			ArraySchema sc = (ArraySchema) schema;
			List<Schema> schemas = sc.getItemSchemas();
			int lenItems = schemas.size();
			Schema allItemSchema = sc.getAllItemSchema();
			JSONArray output = new JSONArray();
			int minItems = 0;
			boolean hasMinItems = true;
			int maxItems = 0;
			boolean hasMaxItems = true;

			try {
				minItems = sc.getMinItems();
			} catch (NullPointerException e) {
				hasMinItems = false;
			}

			try {
				maxItems = sc.getMaxItems();
			} catch (NullPointerException e) {
				hasMaxItems = false;
			}

			// Check if correct amount of items are specified
			if (hasMaxItems && hasMinItems) {
				if (maxItems < minItems) {
					throw new IllegalArgumentException("Minimal length should be smaller than maximal length. Please check schema.");
				}

				if (lenItems > maxItems || lenItems < minItems) {
					throw new IllegalArgumentException("Too many/few items specified. Please check schema.");
				}
			} else if (hasMaxItems) {
				if (lenItems > maxItems) {
					throw new IllegalArgumentException("Too many items specified. Please check schema.");
				}
			} else if (hasMinItems) {
				if (lenItems < minItems) {
					throw new IllegalArgumentException("Too few items specified. Please check schema.");
				}
			}

			for (Schema s : schemas) {
				output.put(GeneratorFactory.getGenerator(s).generate());
			}

			return output;
		}
		return null;
	}
}