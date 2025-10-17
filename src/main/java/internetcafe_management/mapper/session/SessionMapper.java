package internetcafe_management.mapper.session;

import internetcafe_management.dto.SessionDTO;
import internetcafe_management.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mapping(target = "sessionId", ignore = true) // vì DTO không có id
    Session toEntity(SessionDTO dto);

    SessionDTO toDTO(Session entity);
}
