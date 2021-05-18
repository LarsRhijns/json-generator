package com.smartentities.json.generator.generators;

import org.everit.json.schema.Schema;

import java.util.Random;

public class BooleanGenerator extends JsonValueGenerator<Boolean> {

	public BooleanGenerator(Schema schema) {
		super(schema);
	}

	/**
	 * Generates a random boolean with 50/50 chance.
	 * @return Boolean
	 */
	@Override
	public Boolean generate() {
		Random random = new Random();
		return random.nextBoolean();
	}
}