package odontograme.converters;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.time.Instant;

/**
 * Created by immari on 1/15/2017.
 */
public final class InstantConverter implements Converter<String, Instant> {

    @Override
    public Instant convert(String source){
        return Instant.parse(source);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }

}
