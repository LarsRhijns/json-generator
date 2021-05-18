package com.smartentities.json.generator.generators;

import com.github.curiousoddman.rgxgen.RgxGen;
import com.github.curiousoddman.rgxgen.config.RgxGenProperties;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class StringGenerator extends JsonValueGenerator<String> {

	String lower = "abcdefghijklmnopqrstuvwxyz";
	String upper = lower.toUpperCase(Locale.ROOT);
	String digits = "0123456789";
	String subset = lower + upper + digits;

	public StringGenerator(Schema schema) {
		super(schema);
	}

	/**
	 * Generates a valid string from the user specified schema. Currently supports minimum/maximum lengths and
	 * (regex) patterns.
	 * @return String: A valid string from the user specified schema.
	 */
	@Override
	public String generate() {
		Random random = new Random();
		int minLength = 0; // Default length is 8 to prevent very long strings
		boolean hasMinLength = true;
		int maxLength = 0; // Default length is 8 to prevent very long strings
		boolean hasMaxLength = true;
		String pattern = "";
		boolean hasPattern = true;
		int maxTrials = 100;
		String format = "";
		boolean hasFormat = false;
		String output = "";

		if (schema instanceof StringSchema) {
			StringSchema sc = (StringSchema) schema;
			try {
				maxLength = sc.getMaxLength();
			} catch (NullPointerException e) {
				hasMaxLength = false;
			}

			try {
				minLength = sc.getMinLength();
				if (minLength < 0) {
					minLength = 0;
				}
			} catch (NullPointerException e) {
				hasMinLength = false;
			}

			try {
				pattern = sc.getPattern().pattern();
			} catch (NullPointerException e) {
				hasPattern = false;
			}

			// TODO Implement formats

			// MaxLength may not be smaller than minLength
			if ((hasMaxLength && hasMinLength) && maxLength < minLength) {
				throw new IllegalArgumentException("Invalid string property length. Please check JSON schema");
			}

			if (hasPattern) { // Generate Regex and check if length matches global properties (max 100 trials)
				// TODO Make such that Rgx can generate with global property length (instead of trials till success)
				RgxGen rgxGen = new RgxGen(pattern);
				output = rgxGen.generate();
				int trial = 0;
				if (hasMinLength && hasMaxLength) {
					while (output.length() > maxLength || output.length() < minLength || trial == maxTrials) {
						output = rgxGen.generate();
						trial++;
					}
				} else if (hasMinLength) {
					while (output.length() < minLength || trial == maxTrials) {
						output = rgxGen.generate();
						trial++;
					}
				} else if (hasMaxLength) {
					while (output.length() > maxLength || trial == maxTrials) {
						output = rgxGen.generate();
						trial++;
					}
				}

				// If the length is still not valid, then throw an exception
				if ((hasMaxLength && (output.length() > maxLength)) || (hasMinLength && (output.length() < minLength))) {
					throw new IllegalStateException("Cannot generate regex with length properties. Please check/rewrite the specified regex");
				}
			} else {
				int randomLength = 8;
				if (hasMinLength && hasMaxLength) {
					randomLength = minLength + (int) (random.nextDouble() * (maxLength - minLength) + 1);
				} else if (hasMinLength) {
					// Just adds 1 to minLength to make sure length is long enough. Might be better another way.
					randomLength = minLength + (int) (random.nextDouble() * ((minLength + 1) - minLength) + 1);
				} else if (hasMaxLength) {
					// Assumes minLenght = 0. Might be better another way.
					randomLength = (int) (random.nextDouble() * (maxLength) + 1);
				}

				// Generate a random string of lenght randomLength
				StringBuilder sb = new StringBuilder(randomLength);
				for (int i = 0; i < randomLength; i++) {
					int index = (int)(subset.length() * random.nextDouble());
					sb.append(subset.charAt(index));
				}
				output = sb.toString();
			}
			return output;
		}
		return null;
	}
}