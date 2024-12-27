package net.dunice.intensive.basics.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userRepository;

    private final UserMapper userMapper;

    public UserDto getUserDtoById(Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::mapEntityToDto)
                .orElseThrow(UserNotFoundException::new);
    }
}
