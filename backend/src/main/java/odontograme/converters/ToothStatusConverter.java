package odontograme.converters;

import odontograme.patientrecords.odontogram.Tooth;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToothStatusConverter implements Converter<String, Tooth.ToothStatus> {
    @Override
    public Tooth.ToothStatus convert(String source) {
        return Tooth.ToothStatus.valueOf(source);
    }
}
