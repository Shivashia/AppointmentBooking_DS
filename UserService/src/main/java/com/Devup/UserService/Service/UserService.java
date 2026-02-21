package com.Devup.UserService.Service;

import com.Devup.UserService.Config.JwtUtil;
import com.Devup.UserService.DTO.*;
import com.Devup.UserService.Entity.Users;
import com.Devup.UserService.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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

    public JwtResponseDTO login(LoginDTO dto) {
        JwtResponseDTO responseDTO= new JwtResponseDTO();
        Users loginUser=userRepository.findByEmail(dto.getEmail());
        if (loginUser == null) {
            throw new RuntimeException("Invalid email");
        }

        if (!passwordEncoder.matches(dto.getPassword(), loginUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token =jwtUtil.generateToken(loginUser.getEmail(), loginUser.getRole());

        responseDTO.setRole(loginUser.getRole());
        responseDTO.setToken(token);
        responseDTO.setId(loginUser.getId());
        System.out.println("User retrived "+ loginUser.getEmail()+" jwt "+ responseDTO.getToken()+ responseDTO.getId());
        return responseDTO;

    }

    public UserDetailsDTO getUsersByEmail(String email){
        Users loginUser=userRepository.findByEmail(email);
        if (loginUser == null) {
            throw new RuntimeException("Invalid email");
        }
        UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
        userDetailsDTO.setName(loginUser.getName());
        userDetailsDTO.setEmail(loginUser.getEmail());
        userDetailsDTO.setAge(loginUser.getAge());
        userDetailsDTO.setGender(loginUser.getGender());
        userDetailsDTO.setRole(loginUser.getRole());
        userDetailsDTO.setSpecialization(loginUser.getSpecialization());
        userDetailsDTO.setId(loginUser.getId());
        System.out.println("User Service User details "+ userDetailsDTO.toString());
        return userDetailsDTO;
    }

    public List<DoctorDTO> getAllDocs() {

        List<Users> allUsers=userRepository.findAll();
        List<DoctorDTO> resList= new ArrayList<>();
        for(Users users:allUsers){
            if(users.getRole().equals("DOCTOR")){
                DoctorDTO doctorDTO= new DoctorDTO();
                doctorDTO.setName(users.getName());
                doctorDTO.setEmail(users.getEmail());
                doctorDTO.setId(users.getId());
                doctorDTO.setSpecialization(users.getSpecialization());
                resList.add(doctorDTO);
            }

        }
        return resList;
    }

    public UserDetailsDTO getUserById(int id) {
        Optional<Users> loginUser=userRepository.findById(id);
        if (loginUser == null) {
            throw new RuntimeException("Invalid email");
        }
        System.out.println("User returned "+ loginUser.get().toString());
        UserDetailsDTO userDetailsDTO= new UserDetailsDTO();
        userDetailsDTO.setName(loginUser.get().getName());
        userDetailsDTO.setEmail(loginUser.get().getEmail());
        userDetailsDTO.setAge(loginUser.get().getAge());
        userDetailsDTO.setGender(loginUser.get().getGender());
        userDetailsDTO.setRole(loginUser.get().getRole());
        userDetailsDTO.setSpecialization(loginUser.get().getSpecialization());
        userDetailsDTO.setId(loginUser.get().getId());
        System.out.println("User Service User details "+ userDetailsDTO.toString());
        return userDetailsDTO;
    }
}
