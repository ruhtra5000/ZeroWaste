package com.zerowaste.zerowaste.application.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerowaste.zerowaste.application.interfaces.UsersRepository;
import com.zerowaste.zerowaste.application.services.users.exceptions.UserWithSameEmailAlreadyExistsException;
import com.zerowaste.zerowaste.domain.entities.user.User;
import com.zerowaste.zerowaste.domain.entities.user.UserRole;

@Service
public class RegisterUserService {
    @Value("${app.bcrypt.encoder.strength}")
    private int bcryptEncoderStrength;

    @Autowired
    private UsersRepository usersRepository;

    public void execute(RegisterUserDTO dto) throws UserWithSameEmailAlreadyExistsException {
        User user = usersRepository.findByEmail(dto.email());

        if (user != null) {
            throw new UserWithSameEmailAlreadyExistsException("User with same email already exists");
        }

        String encriptedPassword = new BCryptPasswordEncoder(bcryptEncoderStrength).encode(dto.password());

        user = new User();

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(encriptedPassword);
        user.setRole(UserRole.valueOf(dto.role()));

        usersRepository.save(user);
    }
}
