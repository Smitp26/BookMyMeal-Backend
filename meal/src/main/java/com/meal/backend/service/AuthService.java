package com.meal.backend.service;

import com.meal.backend.dto.EmployeeDto;
import com.meal.backend.dto.SignupRequest;
import com.meal.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    EmployeeDto createUser(SignupRequest signupRequest);
}
