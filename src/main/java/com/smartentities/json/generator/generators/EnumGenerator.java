package com.smartentities.json.generator.generators;

import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.Schema;

import java.util.List;
import java.util.Random;

public class EnumGenerator extends JsonValueGenerator<Object> {

    public EnumGenerator(Schema schema) {
        super(schema);

    }

    /**
     * Generates a random valid item from the specified items.
     * @return Object: A valid item from the enumerated options.
     */
    @Override
    public Object generate() {
        Random random = new Random();
        List<Object> possibleValuesAsList = ((EnumSchema) schema).getPossibleValuesAsList();
        int len = possibleValuesAsList.size();

        // Choose random item from the options
        int index = random.nextInt(len);

        return possibleValuesAsList.get(index);
    }
}
