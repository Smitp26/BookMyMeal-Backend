package com.meal.backend.service;

import com.meal.backend.dto.EmployeeDto;
import com.meal.backend.dto.SignupRequest;
import com.meal.backend.entity.Employee;
import com.meal.backend.enums.UserRole;
import com.meal.backend.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    private final EmployeeRepository employeeRepository;

    public AuthServiceImpl(EmployeeRepository userRepository) {
        this.employeeRepository = userRepository;
    }

    @Override
    public EmployeeDto createUser(SignupRequest signupRequest) {
        Employee user = new Employee();

        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.Customer);
        Employee createdUser = employeeRepository.save(user);
        EmployeeDto createdUserDto = new EmployeeDto();
        createdUserDto.setId(createdUser.getId());
        createdUserDto.setName(createdUser.getName());
        createdUserDto.setEmail(createdUser.getEmail());
        createdUserDto.setUserRole(createdUser.getUserRole());

        return createdUserDto;
    }

    @Service
    public static class UserDetailsServiceImpl implements UserDetailsService {

        private final EmployeeRepository employeeRepository;

        public UserDetailsServiceImpl(EmployeeRepository userRepository) {
            this.employeeRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

            //Write logic to get user from db
            Optional<Employee> optionalUser = employeeRepository.findFirstByEmail(email);

            if(optionalUser.isEmpty()) throw new UsernameNotFoundException("User Not Found",null);
            return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(),optionalUser.get().getPassword(),new ArrayList<>());

        }
    }
}
