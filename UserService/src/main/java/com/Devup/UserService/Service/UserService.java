package com.Devup.UserService.Service;

import com.Devup.UserService.DTO.UsersRegistrationDTO;
import com.Devup.UserService.Entity.Users;
import com.Devup.UserService.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class UserService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users registerUser(UsersRegistrationDTO dto) {
        System.out.println("registering the user");
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setPhone(dto.getPhone());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setSpecialization(dto.getSpecialization());

        return userRepository.save(user);
    }
}
