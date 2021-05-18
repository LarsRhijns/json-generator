package com.smartentities.json.generator.generators;

import com.smartentities.json.generator.GeneratorFactory;
import org.everit.json.schema.ReferenceSchema;
import org.everit.json.schema.Schema;

public class ReferenceGenerator extends JsonValueGenerator<Object> {

    public ReferenceGenerator(Schema schema) {
        super(schema);
    }

    /**
     * Looks up the referred object. Uses the generate of that object.
     * @return Object: The referenced object generation
     */
    @Override
    public Object generate() {
        Schema referredSchema = ((ReferenceSchema) schema).getReferredSchema();

        return GeneratorFactory.getGenerator(referredSchema).generate();
    }
}
