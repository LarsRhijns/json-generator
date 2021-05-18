package com.smartentities.json.generator.generators;

import org.everit.json.schema.EnumSchema;
import org.everit.json.schema.Schema;

import java.util.List;
import java.util.Random;

public class EnumGenerator extends JsonValueGenerator<Object> {

    public EnumGenerator(Schema schema) {
        super(schema);

    }

    @Override
    public Object generate() {
        Random random = new Random();
        List<Object> possibleValuesAsList = ((EnumSchema) schema).getPossibleValuesAsList();
        int len = possibleValuesAsList.size();

        // Choose random option from the options
        int index = random.nextInt(len);

        return possibleValuesAsList.get(index);
    }
}
