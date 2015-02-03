package lab.s2jh.core.web.json;

import java.io.IOException;
import java.util.Date;

import lab.s2jh.core.util.DateUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ShortDateTimeJsonSerializer extends JsonSerializer<Date> {

    public final static String SHORT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        if (value != null) {
            jgen.writeString(DateUtils.formatShortTime(value));
        }
    }

    public Class<Date> handledType() {
        return Date.class;
    }
}
