package internetcafe_management.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null) return "{}";
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Map to JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new HashMap<>();
        
        // Handle non-JSON strings by wrapping them in a map
        if (!dbData.trim().startsWith("{") && !dbData.trim().startsWith("[")) {
            Map<String, Object> result = new HashMap<>();
            result.put("value", dbData);
            return result;
        }
        
        try {
            return objectMapper.readValue(dbData, Map.class);
        } catch (IOException e) {
            // If JSON parsing fails, wrap the raw data in a map
            Map<String, Object> result = new HashMap<>();
            result.put("raw_data", dbData);
            result.put("parse_error", e.getMessage());
            return result;
        }
    }
}
