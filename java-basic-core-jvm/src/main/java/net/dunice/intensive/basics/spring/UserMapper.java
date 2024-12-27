package net.dunice.intensive.basics.spring;

import org.springframework.stereotype.Component;

@Component
public final class UserMapper {

    public UserDto mapEntityToDto(UserEntity entity) {
        return new UserDto(entity.getFirstname(), entity.getName(), entity.getLastname());
    }
}
