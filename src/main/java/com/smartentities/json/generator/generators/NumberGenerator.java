package com.smartentities.json.generator.generators;

import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.Schema;

import java.util.Random;

public class NumberGenerator extends JsonValueGenerator<Number> {

	private double MAX_BOUND = Long.MAX_VALUE;
	private double MIN_BOUND = Long.MIN_VALUE;

	public NumberGenerator(Schema schema) {
		super(schema);
	}

	/**
	 * Generates a random number which is valid under the user specified schema. Currently supports (exclusive)
	 * minimums/maximums, multiples and integer options.
	 * @return Number: Either a valid Integer or Double.
	 */
	@Override
	public Number generate() {
		double number = 0.0;
		Random random = new Random();

		if (schema instanceof NumberSchema) {
			NumberSchema sc = (NumberSchema) schema;

			double max = 0.0;
			boolean hasMax = true;
			double min = 0.0;
			boolean hasMin = true;
			double multiple = 1.0;
			boolean hasMultiple = true;
			boolean exclusiveMax = sc.isExclusiveMaximum();
			boolean exclusiveMin = sc.isExclusiveMinimum();

			// Get the range
			try {
				max = sc.getMaximum().doubleValue();
				exclusiveMax = false;
			} catch (NullPointerException e) {
				try {
					max = sc.getExclusiveMaximumLimit().doubleValue();
					exclusiveMax = true;
				} catch (NullPointerException e1) {
					hasMax = false;
				}
			}
			try {
				min = sc.getMinimum().doubleValue();
				exclusiveMin = false;
			} catch (NullPointerException e) {
				try {
					min = sc.getExclusiveMinimumLimit().doubleValue();
					exclusiveMin = true;
				} catch (NullPointerException e1) {
					hasMin = false;
				}
			}
			try {
				multiple = sc.getMultipleOf().doubleValue();
				if (multiple == 0.0) {
					// Edge case, result is always 0
					if (sc.requiresInteger()) {
						return 0;
					}
					return 0.0;
				}
			} catch (NullPointerException e) {
				hasMultiple = false;
			}

			// Set min and max correctly
			if (!hasMax && !hasMin) {
				min = MIN_BOUND;
				max = MAX_BOUND;
			} else if (hasMax && !hasMin) {
				min = MIN_BOUND;
			} else if (hasMin && !hasMax) {
				max = MAX_BOUND;
			}

			// If not exclusive, extend max to make sure the bound can also be selected
			if (!exclusiveMax) {
				if (hasMultiple) {
					max += multiple;
				} else {
					max++;
				}
			}

			// If not exclusive, extend min to make sure the bound can also be selected
			if (!exclusiveMin) {
				if (hasMultiple) {
					min -= multiple;
				} else {
					min--;
				}
			}

			number = ((random.nextDouble() * (max - min) + min));
			if (hasMultiple) {
				number = number - (number % multiple);
			}

			if (sc.requiresInteger()) {
				// If it should be an integer, cast it to an int
				return (int) number;
			}
			return number;
		}
		return null;
	}
}