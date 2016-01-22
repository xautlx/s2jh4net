package lab.s2jh.core.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class JsonUtils {

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> readValue(String content) {
        try {
            return objectMapper.readValue(content, Map.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Maps.newHashMap();
    }

    public static List readListValue(String content) {
        try {
            return objectMapper.readValue(content, List.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Lists.newArrayList();
    }

    public static void main(String[] args) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("id", 123);
        data.put("time", DateUtils.formatTime(DateUtils.currentDate()));
        System.out.println(JsonUtils.writeValueAsString(data));
    }
}
