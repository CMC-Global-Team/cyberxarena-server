package internetcafe_management.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ComputerStatusConverter implements AttributeConverter<Computer.ComputerStatus, String> {

    @Override
    public String convertToDatabaseColumn(Computer.ComputerStatus status) {
        if (status == null) {
            return null;
        }
        return status.toJsonValue();
    }

    @Override
    public Computer.ComputerStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return Computer.ComputerStatus.fromString(dbData);
    }
}
